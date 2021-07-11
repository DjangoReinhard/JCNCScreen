/*
 * file:        dumpBufDesc.cc
 * purpose:     helper to dump member offset - used to check matching definitions
 * created:     190901
 */
#define __STDC_FORMAT_MACROS
#include <inttypes.h>
#include "config.h"
#include "emc.hh"
#include "emc_nml.hh"


#define O(x) (((long) &(emcStatus->x)) - ((long)emcStatus))


struct MemberDef {
  const char* name;
  long offset;
  const char* cmt;
  };

static MemberDef StatusMembers[] = {
// start like axis
// stat
//    {"echo_serial_number",  O(echo_serial_number)},
//    {"echo_serial_number",  O(echo_serial_number)},
//    {"state",               O(status)},

// task
    {"taskMode",            O(task.mode)},
    {"taskState",           O(task.state)},
    {"execState",           O(task.execState)},
    {"interpState",         O(task.interpState)},
//    {"call_level",          O(task.callLevel)},
    {"readLine",            O(task.readLine)},
    {"motionLine",          O(task.motionLine)},
    {"currentLine",         O(task.currentLine)},
    {"file",                O(task.file)},
//    {"command",             O(task.command)},
    {"programUnits",        O(task.programUnits)},
//    {"interpreter_errcode", O(task.interpreter_errcode)},
//    {"optional_stop",       O(task.optional_stop_state)},
//    {"block_delete",        O(task.block_delete_state)},
//    {"task_paused",         O(task.task_paused)},
//    {"input_timeout",       O(task.input_timeout)},
    {"rotationXY",          O(task.rotation_xy)},
//    {"delay_left",          O(task.delayLeft)},
//    {"queued_mdi_commands", O(task.queuedMDIcommands)
//      , "Number of MDI commands queued waiting to run." },
// motion
//   EMC_TRAJ_STAT traj
//    {"linear_units",        O(motion.traj.linearUnits)},
//    {"angular_units",       O(motion.traj.angularUnits)},
//    {"cycle_time",          O(motion.traj.cycleTime)},
    {"joints",              O(motion.traj.joints)},
    {"spindles",            O(motion.spindle)},
    {"axisMask",            O(motion.traj.axis_mask)},
//    {"motion_mode",         O(motion.traj.mode)
//      , "The current mode of the Motion controller.  One of TRAJ_MODE_FREE,\n"
//        "TRAJ_MODE_COORD, or TRAJ_MODE_TELEOP." },
//    {"enabled",             O(motion.traj.enabled)},
//    {"inpos",               O(motion.traj.inpos)},
//    {"queue",               O(motion.traj.queue)},
//    {"active_queue",        O(motion.traj.activeQueue)},
//    {"queue_full",          O(motion.traj.queueFull)},
//    {"id",                  O(motion.traj.id)},
//    {"paused",              O(motion.traj.paused)},
    {"feedrate",            O(motion.traj.scale)},
    {"rapidrate",           O(motion.traj.rapid_scale)},
    {"velocityT",           O(motion.traj.velocity)},
    {"velocityA",           O(motion.axis[0].velocity)},
    {"velocityJ",           O(motion.joint[0].velocity)},
    {"acceleration",        O(motion.traj.acceleration)},
    {"max_velocity",        O(motion.traj.maxVelocity)},
    {"max_acceleration",    O(motion.traj.maxAcceleration)},

//    {"probe_tripped",       O(motion.traj.probe_tripped)},
//    {"probing",             O(motion.traj.probing)},
//    {"probe_val",           O(motion.traj.probeval)},
//    {"kinematics_type",     O(motion.traj.kinematics_type)},
//    {"motion_type",         O(motion.traj.motion_type)
//      , "The type of the currently executing motion (one of MOTION_TYPE_TRAVERSE,\n"
//        "MOTION_TYPE_FEED, MOTION_TYPE_ARC, MOTION_TYPE_TOOLCHANGE,\n"
//        "MOTION_TYPE_PROBING, or MOTION_TYPE_INDEXROTARY), or 0 if no motion is\n"
//        "currently taking place."},
    {"distance2Go",           O(motion.traj.distance_to_go)},
    {"current_vel",           O(motion.traj.current_vel)},
    {"feed_override_enabled", O(motion.traj.feed_override_enabled)},
    {"adaptive_feed_enabled", O(motion.traj.adaptive_feed_enabled)},
    {"feed_hold_enabled",     O(motion.traj.feed_hold_enabled)},
//axis end

    {"activeGCodes",   O(task.activeGCodes)},
    {"activeMCodes",   O(task.activeMCodes)},
    {"activeSettings", O(task.activeSettings)},

    {"absPosX",     O(motion.traj.position.tran.x)},
    {"relPosX",     O(motion.traj.actualPosition.tran.x)},
    {"dtgX",        O(motion.traj.dtg.tran.x)},
    {"g5xOffsX",    O(task.g5x_offset.tran.x)},
    {"g92OffsX",    O(task.g92_offset.tran.x)},
    {"toolOffsX",   O(task.toolOffset.tran.x)},

    {"joint_0_enabled", O(motion.joint[0].enabled)},
    {"joint_1_enabled", O(motion.joint[1].enabled)},
    {"joint_2_enabled", O(motion.joint[2].enabled)},
    {"joint_3_enabled", O(motion.joint[3].enabled)},
    {"joint_4_enabled", O(motion.joint[4].enabled)},
    {"joint_5_enabled", O(motion.joint[5].enabled)},
    {"joint_6_enabled", O(motion.joint[6].enabled)},
    {"joint_7_enabled", O(motion.joint[7].enabled)},
    {"joint_8_enabled", O(motion.joint[8].enabled)},

    {"joint_0_homed", O(motion.joint[0].homed)},
    {"joint_1_homed", O(motion.joint[1].homed)},
    {"joint_2_homed", O(motion.joint[2].homed)},
    {"joint_3_homed", O(motion.joint[3].homed)},
    {"joint_4_homed", O(motion.joint[4].homed)},
    {"joint_5_homed", O(motion.joint[5].homed)},
    {"joint_6_homed", O(motion.joint[6].homed)},
    {"joint_7_homed", O(motion.joint[7].homed)},
    {"joint_8_homed", O(motion.joint[8].homed)},


// EMC_SPINDLE_STAT motion.spindle
    {"spindleSpeed",             O(motion.spindle[0].speed)},
    {"spindleScale",             O(motion.spindle[0].spindle_scale)},
//    {"Spindle[0]css_max",        O(motion.spindle[0].css_maximum)},
//    {"Spindle[0]css_fak",        O(motion.spindle[0].css_factor)},
    {"spindleDir",               O(motion.spindle[0].direction)},
//    {"Spindle[0]brake",          O(motion.spindle[0].brake)},
    {"spindleIncreasing",        O(motion.spindle[0].increasing)},
    {"spindleEnable",            O(motion.spindle[0].enabled)},
//    {"Spindle[0]orient_state",   O(motion.spindle[0].orient_state)},
//    {"Spindle[0]orient_fault",   O(motion.spindle[0].orient_fault)},
    {"spindleOverrideEnable",    O(motion.spindle[0].spindle_override_enabled)},
    {"spindleHomed",             O(motion.spindle[0].homed)},
// io
// EMC_TOOL_STAT io.tool
    {"pocketPrepared",  O(io.tool.pocketPrepped),
        "The index into the stat.tool_table list of the tool currently prepped for\n"
        "tool change, or -1 no tool is prepped.  On a Random toolchanger this is the\n"
        "same as the tool's pocket number.  On a Non-random toolchanger it's a random\n"
        "small integer."
    },
    {"toolInSpindle",  O(io.tool.toolInSpindle),
        "The tool number of the currently loaded tool, or 0 if no tool is loaded."
    },
#ifdef TOOL_NML
    {"toolTable",      O(io.tool.toolTable)},
#endif
// EMC_COOLANT_STAT io.cooland
    {"coolMist",   O(io.coolant.mist)},
    {"coolFlood",  O(io.coolant.flood)},

// EMC_AUX_STAT     io.aux
    {"estop",      O(io.aux.estop)},

// EMC_LUBE_STAT    io.lube
    {"lube",       O(io.lube.on)},
//    {"lube_level", O(io.lube.level)},

    {"debug",      O(debug)},
    {NULL}
};
/* */

/*
 *
 */
int main(int cArgs, char** pArgs) {
  for (const MemberDef *p = StatusMembers;
       p->offset > 0;
       ++p) {
      fprintf(stdout, "[%6ld] %s\n", p->offset, p->name);
      }
  }
