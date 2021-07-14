/*
 * file:    TestNML.cc
 *
 */
//#include <rcs.hh>
#include <stat_msg.hh>
#include <cmd_msg.hh>
#include <emc_nml.hh>
#include <stdio.h>
#include <sys/time.h>
#include <cstring>


RCS_CMD_CHANNEL*    cCmd;
RCS_STAT_CHANNEL*   cStat;
NML*                cErr;
EMC_STAT*           status;


#define EMC_COMMAND_TIMEOUT 5.0  // how long to wait until timeout
#define EMC_COMMAND_DELAY   0.01 // seconds to sleep between checks


void sleep(double s) {
  struct timeval tv;

  tv.tv_sec  = s;
  tv.tv_usec = s * 1000000;
  select (0, NULL, NULL, NULL, &tv);
  }


int init() {
  cCmd = new RCS_CMD_CHANNEL(emcFormat, "emcCommand", "xemc", EMC2_DEFAULT_NMLFILE);
  if (!cCmd || !cCmd->valid()) {
     delete cCmd;
     cCmd = NULL;

     return -1;
     }
  cErr = new NML(emcFormat, "emcError", "xemc", EMC2_DEFAULT_NMLFILE);
  if (!cErr || !cErr->valid()) {
     delete cErr;
     delete cCmd;
     cErr = NULL;
     cCmd = NULL;

     return -2;
     }
  cStat = new RCS_STAT_CHANNEL(emcFormat, "emcStatus", "xemc", EMC2_DEFAULT_NMLFILE);
  if (!cStat || !cStat->valid()) {
     delete cCmd;
     delete cErr;
     delete cStat;
     cCmd  = NULL;
     cErr  = NULL;
     cStat = NULL;

     return -3;
     }
  else {
     status = static_cast<EMC_STAT*>(cStat->get_address());
     }
  return 0;
  }


int fetchMessage(char* errBuf) {
  if (!cErr->valid()) return -1;
  NMLTYPE type = cErr->read();

  if (!type) return -2;

  strncpy(errBuf, ((EMC_OPERATOR_ERROR*)cErr->get_address())->error, LINELEN-1);

  return 0;
  }


int sendCommand(RCS_CMD_MSG& msg) {
  if (cCmd->write(&msg)) return -1;

  for (double end = 0.0
     ; end < EMC_COMMAND_TIMEOUT
     ; end += EMC_COMMAND_DELAY) {
      cStat->peek();

      if ((status->echo_serial_number - msg.serial_number) >= 0) return 0;
      sleep(EMC_COMMAND_DELAY);
      }
  return -1;
  }


int clearEStop() {
  EMC_TASK_SET_STATE tss;

  tss.state = (EMC_TASK_STATE_ENUM)2;

  return sendCommand(tss);
  }


int machineOn() {
  EMC_TASK_SET_STATE tss;

  tss.state = (EMC_TASK_STATE_ENUM)4;

  return sendCommand(tss);
  }


int homeAxis(int axis) {
  EMC_JOINT_HOME jh;

  jh.joint = axis;

  return sendCommand(jh);
  }


int jogStep(int axis, double stepSize, double speed) {
  EMC_JOG_INCR ji;

  ji.joint_or_axis = axis;
  ji.incr          = stepSize;
  ji.vel           = speed;
  ji.jjogmode      = 0;

  return sendCommand(ji);
  }


int jogStart(int axis, double speed) {
  EMC_JOG_CONT jc;

  jc.joint_or_axis = axis;
  jc.vel           = speed;
  jc.jjogmode      = 0;

  return sendCommand(jc);
  }


int jogStop(int axis) {
  EMC_JOG_STOP js;

  js.joint_or_axis = axis;
  js.jjogmode      = 0;

  return sendCommand(js);
  }


int loadTaskPlan(const char* gcodeFile) {
  EMC_TASK_PLAN_CLOSE pc;
  EMC_TASK_PLAN_OPEN  po;

  if (sendCommand(pc)) return -1;
  strcpy(po.file, gcodeFile);

  return sendCommand(po);
  }


int sendMDICommand(const char* cmd) {
  EMC_TASK_PLAN_EXECUTE pe;

  strcpy(pe.command, cmd);

  return sendCommand(pe);
  }


int setAuto(int autoMode, int line) {
  switch (autoMode) {
    case 0: {
         EMC_TASK_PLAN_RUN pr;

         pr.line = line;

         return sendCommand(pr);
         }
    case 1: {
         EMC_TASK_PLAN_PAUSE pp;

         return sendCommand(pp);
         }
    case 2: {
         EMC_TASK_PLAN_RESUME pr;

         return sendCommand(pr);
         }
    case 3: {
         EMC_TASK_PLAN_STEP ps;

         return sendCommand(ps);
         }
    case 4: {
         EMC_TASK_PLAN_REVERSE pr;

         return sendCommand(pr);
         }
    case 5: {
         EMC_TASK_PLAN_FORWARD pf;

         return sendCommand(pf);
         }
    }
  return -99;
  }


int setBlockDelete(int enable) {
  EMC_TASK_PLAN_SET_BLOCK_DELETE bd;

  if (enable) bd.state = 1;
  else        bd.state = 0;

  return sendCommand(bd);
  }


int setFeedOverride(double factor) {
  EMC_TRAJ_SET_SCALE ss;

  ss.scale = factor;

  return sendCommand(ss);
  }


int setFlood(int enable) {
  if (!enable) {
     EMC_COOLANT_FLOOD_OFF cf;

     return sendCommand(cf);
     }
  EMC_COOLANT_FLOOD_ON cf;

  return sendCommand(cf);
  }


int setMist(int enable) {
  if (!enable) {
     EMC_COOLANT_MIST_OFF cm;

     return sendCommand(cm);
     }
  EMC_COOLANT_MIST_ON cm;

  return sendCommand(cm);
  }


int setOptionalStop(int enable) {
  EMC_TASK_PLAN_SET_OPTIONAL_STOP os;

  os.state = enable;

  return sendCommand(os);
  }


int setRapidOverride(double factor) {
  EMC_TRAJ_SET_RAPID_SCALE rs;

  rs.scale = factor;

  return sendCommand(rs);
  }


int setSpindle(int enable, int speed, int direction) {
  if (!enable) {
     EMC_SPINDLE_OFF so;

     so.spindle = 0;

     return sendCommand(so);
     }
  EMC_SPINDLE_ON so;

  so.speed = direction * speed;
  so.spindle = 0;

  return sendCommand(so);
  }


int setSpindleOverride(double factor) {
  EMC_TRAJ_SET_SPINDLE_SCALE ss;

  ss.spindle = 0;
  ss.scale   = factor;

  return sendCommand(ss);
  }


int setTaskMode(int mode) {
  EMC_TASK_SET_MODE tm;

  tm.mode = (EMC_TASK_MODE_ENUM)mode;

  return sendCommand(tm);
  }


int setTaskState(int state) {
  EMC_TASK_SET_STATE ss;

  ss.state = (EMC_TASK_STATE_ENUM)state;

  return sendCommand(ss);
}


int taskAbort() {
  EMC_TASK_ABORT ta;

  return sendCommand(ta);
  }


int main(int cArgs, char** pArgs) {
  if (init()) {
     fprintf(stderr, "failed to create NML-channels to backend!\n");
     exit(-1);
     }
  cStat->peek();
  printf("task-mode : %d\n", status->task.mode);
  printf("task-state: %d\n", status->task.state);

  if (clearEStop()) fprintf(stderr, "failed to clear EStop!\n");
  cStat->peek();
  printf("task-mode: %d\n", status->task.mode);
  printf("task-state: %d\n", status->task.state);

  if (machineOn()) fprintf(stderr, "failed to turn machine on!\n");
  cStat->peek();
  printf("task-mode: %d\n", status->task.mode);
  printf("task-state: %d\n", status->task.state);

  if (homeAxis(-1)) fprintf(stderr, "failed to home all axis!\n");
  cStat->peek();
  printf("task-mode: %d\n", status->task.mode);
  printf("task-state: %d\n", status->task.state);


  return 0;
  }
