/*
 * **************************************************************************
 *
 *  file:       de_schwarzrot_system_CommandWriter.cc
 *  project:    GUI for linuxcnc
 *  subproject: C-stub to access nml-structure in shared memory
 *  purpose:    send commands to linuxcnc backend
 *  created:    18.4.2020 by Django Reinhard
 *              followed code from linuxcnc
 *  copyright:  all rights reserved
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * **************************************************************************
 */
/*
 * JNI implementation of a bridge between linuxcnc and java (ui)
 *
 * compile with:
g++ -c -I. \
    -Ilc/src/libnml/linklist \
    -Ilc/src/libnml/cms \
    -Ilc/src/libnml/rcs \
    -Ilc/src/libnml/inifile \
    -Ilc/src/libnml/os_intf \
    -Ilc/src/libnml/nml \
    -Ilc/src/libnml/buffer \
    -Ilc/src/libnml/posemath \
    -Ilc/src/rtapi \
    -Ilc/src/hal \
    -Ilc/src/emc/nml_intf \
    -Ilc/src/emc/kinematics \
    -Ilc/src/emc/tp \
    -Ilc/src/emc/motion \
    -Ilc/src/emc/ini \
    -Ilc/src/emc/rs274ngc \
    -Ilc/src/emc/sai \
    -Ilc/src/emc/pythonplugin \
    -Ilc/include \
    -I/usr/include/python2.7 \
    -I/usr/lib/jvm/java-8-openjdk-amd64/include \
    -g -O2    -DULAPI  -g -Wall -Os -fwrapv -Woverloaded-virtual -fPIC -fno-strict-aliasing \
    -MP -MD \
    de_schwarzrot_status_StatusReader.cc \
    -o jniLinuxCNC.o

 * then link as shared lib with:
g++ -Llc/lib \
    -Wl,-rpath,lc/lib \
    -shared \
    -o jniLinuxCNC.so \
    jniLinuxCNC.o \
    lc/lib/liblinuxcnc.a \
    lc/lib/libnml.so.0 \
    lc/lib/liblinuxcncini.so \
    -L/usr/X11R6/lib \
    -lm -lGL
 *
 */
#define __STDC_FORMAT_MACROS

#include <Python.h>
#include "config.h"
#include "rcs.hh"
#include "emc.hh"
#include "emc_nml.hh"
#include "kinematics.h"
#include "config.h"
#include "inifile.hh"
#include "timer.hh"
#include "nml_oi.hh"
#include "rcs_print.hh"

#include  <de_schwarzrot_system_CommandWriter.h>


/*
 * ini-file: /usr/local/src/linuxcnc-dev/configs/sim/axis/axis.ini
 */
#define EMC_COMMAND_TIMEOUT 5.0  // how long to wait until timeout
#define EMC_COMMAND_DELAY   0.01 // how long to sleep between checks


struct CommandChannel {
  PyObject_HEAD
  RCS_CMD_CHANNEL*     c;
  RCS_STAT_CHANNEL*    s;
  int                  serial;
  };
static CommandChannel cc = {0};


static int sendCommand(CommandChannel* cc, RCS_CMD_MSG& cmd) {
  /*
  fprintf(stderr
        , "                        \tmessage #%d (%s) of size %ld\n"
        , cmd.type
        , emc_symbol_lookup(cmd.type)
        , cmd.size);
  */
  if (cc->c->write(&cmd)) return -1;
  cc->serial = cmd.serial_number;

  double start = etime();

  while (etime() - start < EMC_COMMAND_TIMEOUT) {
        EMC_STAT* stat  = (EMC_STAT *)cc->s->get_address();
        int serial_diff = stat->echo_serial_number - cc->serial;

        if (cc->s->peek() == EMC_STAT_TYPE && serial_diff >= 0) return 0;
        esleep(EMC_COMMAND_DELAY);
        }
  return -1;
  }

#ifdef __cplusplus
extern "C" {
#endif


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    init
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_de_schwarzrot_system_CommandWriter_init(JNIEnv* env
                                                                  , jobject thisObject) {
  const char* nmlFile = EMC2_DEFAULT_NMLFILE;

  cc.c = new RCS_CMD_CHANNEL(emcFormat,  "emcCommand", "xemc", nmlFile);
  cc.s = new RCS_STAT_CHANNEL(emcFormat, "emcStatus",  "xemc", nmlFile);

  return 0;
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    homeAxis
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_homeAxis(JNIEnv* env
                                                                      , jobject thisObject
                                                                      , jint    jointNum) {
  EMC_JOINT_HOME jh;

  jh.joint = jointNum;
  sendCommand(&cc, jh);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    loadTaskPlan
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_loadTaskPlan(JNIEnv* env
                                                                          , jobject thisObject
                                                                          , jstring gcodeFile) {
  EMC_TASK_PLAN_CLOSE pc;
  EMC_TASK_PLAN_OPEN  po;
  const char*         fileName = env->GetStringUTFChars(gcodeFile, NULL);

  sendCommand(&cc, pc);
  strcpy(po.file, fileName);
  sendCommand(&cc, po);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    loadToolTable
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_loadToolTable(JNIEnv* env
                                                                           , jobject thisObject
                                                                           , jstring toolTableFile) {
  EMC_TOOL_LOAD_TOOL_TABLE ltt;
  const char*              fileName = NULL;

  if (toolTableFile != NULL) {
     fileName = env->GetStringUTFChars(toolTableFile, NULL);
     strcpy(ltt.file, fileName);
     }
  sendCommand(&cc, ltt);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    sendMDICommand
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_sendMDICommand(JNIEnv* env
                                                                            , jobject thisObject
                                                                            , jstring command) {
  EMC_TASK_PLAN_EXECUTE pe;
  const char*           cmd = env->GetStringUTFChars(command, NULL);

  strcpy(pe.command, cmd);
  sendCommand(&cc, pe);
  }


#define LOCAL_AUTO_RUN      (0)
#define LOCAL_AUTO_PAUSE    (1)
#define LOCAL_AUTO_RESUME   (2)
#define LOCAL_AUTO_STEP     (3)
#define LOCAL_AUTO_REVERSE  (4)
#define LOCAL_AUTO_FORWARD  (5)

/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setAuto
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setAuto(JNIEnv* env
                                                                     , jobject thisObject
                                                                     , jint    autoMode
                                                                     , jint    line) {
  int rv = 0;

  switch (autoMode) {
    case 0: {
         EMC_TASK_PLAN_RUN pr;

         pr.line = line;
//         fprintf(stderr, "cmdWriter.setAuto(run) - line: %d\n", line);
         rv = sendCommand(&cc, pr);
         } break;
    case 1: {
         EMC_TASK_PLAN_PAUSE pp;

//         fputs("cmdWriter.setAuto(pause)\n", stderr);
         rv = sendCommand(&cc, pp);
         } break;
    case 2: {
         EMC_TASK_PLAN_RESUME pr;

//         fputs("cmdWriter.setAuto(resume)\n", stderr);
         rv = sendCommand(&cc, pr);
         } break;
    case 3: {
         EMC_TASK_PLAN_STEP ps;

//         fputs("cmdWriter.setAuto(step)\n", stderr);
         rv = sendCommand(&cc, ps);
         } break;
    case 4: {
         EMC_TASK_PLAN_REVERSE pr;

//         fputs("cmdWriter.setAuto(reverse)\n", stderr);
         rv = sendCommand(&cc, pr);
         } break;
    case 5: {
         EMC_TASK_PLAN_FORWARD pf;

//         fputs("cmdWriter.setAuto(forward)\n", stderr);
         rv = sendCommand(&cc, pf);
         } break;
    }
  if (rv) fputs("changing interpreters auto mode FAILED!", stderr);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setBlockDelete
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setBlockDelete(JNIEnv*  env
                                                                            , jobject  thisObject
                                                                            , jboolean enable) {
  EMC_TASK_PLAN_SET_BLOCK_DELETE bd;

  if (enable) bd.state = 1;
  else        bd.state = 0;
  sendCommand(&cc, bd);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setFeedOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setFeedOverride(JNIEnv* env
                                                                             , jobject thisObject
                                                                             , jdouble rate) {
  EMC_TRAJ_SET_SCALE ss;

  ss.scale = rate;
  sendCommand(&cc, ss);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setFlood
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setFlood(JNIEnv*  env
                                                                      , jobject  thisObject
                                                                      , jboolean enable) {
  if (enable) {
     EMC_COOLANT_FLOOD_ON fo;

     sendCommand(&cc, fo);
     }
  else {
     EMC_COOLANT_FLOOD_OFF fo;

     sendCommand(&cc, fo);
     }
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setMist
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setMist(JNIEnv*  env
                                                                     , jobject  thisObject
                                                                     , jboolean enable) {
  if (enable) {
     EMC_COOLANT_MIST_ON mo;

     sendCommand(&cc, mo);
     }
  else {
     EMC_COOLANT_MIST_OFF mo;

     sendCommand(&cc, mo);
     }
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setOptionalStop
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setOptionalStop(JNIEnv*  env
                                                                             , jobject  thisObject
                                                                             , jboolean enable) {
  EMC_TASK_PLAN_SET_OPTIONAL_STOP os;

  os.state = enable;
  sendCommand(&cc, os);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setRapidOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setRapidOverride(JNIEnv* env
                                                                              , jobject thisObject
                                                                              , jdouble rate) {
  EMC_TRAJ_SET_RAPID_SCALE rs;

  rs.scale = rate;
  sendCommand(&cc, rs);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setSpindle
 * Signature: (ZI)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setSpindle(JNIEnv*  env
                                                                        , jobject  thisObject
                                                                        , jboolean enable
                                                                        , jint     speed
                                                                        , jint     direction) {
  if (enable) {
     EMC_SPINDLE_ON so;

     so.speed = direction * speed;
     so.spindle = 0;
     sendCommand(&cc, so);
     }
  else {
     EMC_SPINDLE_OFF so;

     so.spindle = 0;
     sendCommand(&cc, so);
     }
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setSpindleOverride
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setSpindleOverride(JNIEnv* env
                                                                                , jobject thisObject
                                                                                , jdouble rate) {
  EMC_TRAJ_SET_SPINDLE_SCALE ss;

  ss.spindle = 0;
  ss.scale = rate;
  sendCommand(&cc, ss);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setTaskMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setTaskMode(JNIEnv* env
                                                                         , jobject thisObject
                                                                         , jint    mode) {
  EMC_TASK_SET_MODE sm;

  sm.mode = (EMC_TASK_MODE_ENUM)mode;
  sendCommand(&cc, sm);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    setTaskState
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_setTaskState(JNIEnv* env
                                                                          , jobject thisObject
                                                                          , jint    state) {
  EMC_TASK_SET_STATE ss;

  ss.state = (EMC_TASK_STATE_ENUM)state;
  sendCommand(&cc, ss);
  }


/*
 * Class:     de_schwarzrot_system_CommandWriter
 * Method:    taskAbort
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_CommandWriter_taskAbort(JNIEnv* env
                                                                       , jobject thisObject) {
  EMC_TASK_ABORT ta;

  sendCommand(&cc, ta);
  }

#ifdef __cplusplus
}
#endif
