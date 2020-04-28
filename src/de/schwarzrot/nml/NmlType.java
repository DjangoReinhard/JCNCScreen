package de.schwarzrot.nml;
/* 
 * **************************************************************************
 * 
 *  file:       NmlType.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    19.9.2019 by Django Reinhard
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

public enum NmlType {
                     EMC_OPERATOR_ERROR_TYPE(11),
                     EMC_OPERATOR_TEXT_TYPE(12),
                     EMC_OPERATOR_DISPLAY_TYPE(13),
                     EMC_NULL_TYPE(21),
                     EMC_SET_DEBUG_TYPE(22),
                     EMC_SYSTEM_CMD_TYPE(30),

                     // NML for EMC_JOINT
                     EMC_JOINT_SET_JOINT_TYPE(101),
                     EMC_JOINT_SET_UNITS_TYPE(102),
                     EMC_JOINT_SET_MIN_POSITION_LIMIT_TYPE(107),
                     EMC_JOINT_SET_MAX_POSITION_LIMIT_TYPE(108),
                     EMC_JOINT_SET_FERROR_TYPE(111),
                     EMC_JOINT_SET_HOMING_PARAMS_TYPE(112),
                     EMC_JOINT_SET_MIN_FERROR_TYPE(115),
                     EMC_JOINT_SET_MAX_VELOCITY_TYPE(116),
                     EMC_JOINT_INIT_TYPE(118),
                     EMC_JOINT_HALT_TYPE(119),
                     EMC_JOINT_ABORT_TYPE(120),
                     EMC_JOINT_ENABLE_TYPE(121),
                     EMC_JOINT_DISABLE_TYPE(122),
                     EMC_JOINT_HOME_TYPE(123),
                     EMC_JOG_CONT_TYPE(124),
                     EMC_JOG_INCR_TYPE(125),
                     EMC_JOG_ABS_TYPE(126),
                     EMC_JOINT_ACTIVATE_TYPE(127),
                     EMC_JOINT_DEACTIVATE_TYPE(128),
                     EMC_JOINT_OVERRIDE_LIMITS_TYPE(129),
                     EMC_JOINT_LOAD_COMP_TYPE(131),
                     EMC_JOINT_SET_BACKLASH_TYPE(134),
                     EMC_JOINT_UNHOME_TYPE(135),
                     EMC_JOG_STOP_TYPE(136),

                     EMC_JOINT_STAT_TYPE(198),
                     EMC_AXIS_STAT_TYPE(199),

                     // NML for EMC_TRAJ
                     // defs for termination conditions
                     EMC_TRAJ_TERM_COND_STOP(0),
                     EMC_TRAJ_TERM_COND_EXACT(1),
                     EMC_TRAJ_TERM_COND_BLEND(2),

                     EMC_TRAJ_SET_AXES_TYPE(201),
                     EMC_TRAJ_SET_UNITS_TYPE(202),
                     EMC_TRAJ_SET_CYCLE_TIME_TYPE(203),
                     EMC_TRAJ_SET_MODE_TYPE(204),
                     EMC_TRAJ_SET_VELOCITY_TYPE(205),
                     EMC_TRAJ_SET_ACCELERATION_TYPE(206),
                     EMC_TRAJ_SET_MAX_VELOCITY_TYPE(207),
                     EMC_TRAJ_SET_MAX_ACCELERATION_TYPE(208),
                     EMC_TRAJ_SET_SCALE_TYPE(209),
                     EMC_TRAJ_SET_RAPID_SCALE_TYPE(238),
                     EMC_TRAJ_SET_MOTION_ID_TYPE(210),

                     EMC_TRAJ_INIT_TYPE(211),
                     EMC_TRAJ_HALT_TYPE(212),
                     EMC_TRAJ_ENABLE_TYPE(213),
                     EMC_TRAJ_DISABLE_TYPE(214),
                     EMC_TRAJ_ABORT_TYPE(215),
                     EMC_TRAJ_PAUSE_TYPE(216),
                     EMC_TRAJ_STEP_TYPE(217),
                     EMC_TRAJ_RESUME_TYPE(218),
                     EMC_TRAJ_DELAY_TYPE(219),
                     EMC_TRAJ_LINEAR_MOVE_TYPE(220),
                     EMC_TRAJ_CIRCULAR_MOVE_TYPE(221),
                     EMC_TRAJ_SET_TERM_COND_TYPE(222),
                     EMC_TRAJ_SET_OFFSET_TYPE(223),
                     EMC_TRAJ_SET_G5X_TYPE(224),
                     EMC_TRAJ_SET_HOME_TYPE(225),
                     EMC_TRAJ_SET_ROTATION_TYPE(226),
                     EMC_TRAJ_SET_G92_TYPE(227),
                     EMC_TRAJ_CLEAR_PROBE_TRIPPED_FLAG_TYPE(228),
                     EMC_TRAJ_PROBE_TYPE(229),
                     EMC_TRAJ_SET_TELEOP_ENABLE_TYPE(230),
                     EMC_TRAJ_SET_SPINDLESYNC_TYPE(232),
                     EMC_TRAJ_SET_SPINDLE_SCALE_TYPE(233),
                     EMC_TRAJ_SET_FO_ENABLE_TYPE(234),
                     EMC_TRAJ_SET_SO_ENABLE_TYPE(235),
                     EMC_TRAJ_SET_FH_ENABLE_TYPE(236),
                     EMC_TRAJ_RIGID_TAP_TYPE(237),

                     EMC_TRAJ_STAT_TYPE(299),

                     // EMC_MOTION aggregate class type declaration
                     EMC_MOTION_INIT_TYPE(301),
                     EMC_MOTION_HALT_TYPE(302),
                     EMC_MOTION_ABORT_TYPE(303),
                     EMC_MOTION_SET_AOUT_TYPE(304),
                     EMC_MOTION_SET_DOUT_TYPE(305),
                     EMC_MOTION_ADAPTIVE_TYPE(306),

                     EMC_MOTION_STAT_TYPE(399),

                     // NML for EMC_TASK
                     EMC_TASK_INIT_TYPE(501),
                     EMC_TASK_HALT_TYPE(502),
                     EMC_TASK_ABORT_TYPE(503),
                     EMC_TASK_SET_MODE_TYPE(504),
                     EMC_TASK_SET_STATE_TYPE(505),
                     EMC_TASK_PLAN_OPEN_TYPE(506),
                     EMC_TASK_PLAN_RUN_TYPE(507),
                     EMC_TASK_PLAN_READ_TYPE(508),
                     EMC_TASK_PLAN_EXECUTE_TYPE(509),
                     EMC_TASK_PLAN_PAUSE_TYPE(510),
                     EMC_TASK_PLAN_STEP_TYPE(511),
                     EMC_TASK_PLAN_RESUME_TYPE(512),
                     EMC_TASK_PLAN_END_TYPE(513),
                     EMC_TASK_PLAN_CLOSE_TYPE(514),
                     EMC_TASK_PLAN_INIT_TYPE(515),
                     EMC_TASK_PLAN_SYNCH_TYPE(516),
                     EMC_TASK_PLAN_SET_OPTIONAL_STOP_TYPE(517),
                     EMC_TASK_PLAN_SET_BLOCK_DELETE_TYPE(518),
                     EMC_TASK_PLAN_OPTIONAL_STOP_TYPE(519),
                     EMC_TASK_PLAN_REVERSE_TYPE(520),
                     EMC_TASK_PLAN_FORWARD_TYPE(521),

                     EMC_TASK_STAT_TYPE(599),

                     // EMC_TOOL type declarations
                     EMC_TOOL_INIT_TYPE(1101),
                     EMC_TOOL_HALT_TYPE(1102),
                     EMC_TOOL_ABORT_TYPE(1103),
                     EMC_TOOL_PREPARE_TYPE(1104),
                     EMC_TOOL_LOAD_TYPE(1105),
                     EMC_TOOL_UNLOAD_TYPE(1106),
                     EMC_TOOL_LOAD_TOOL_TABLE_TYPE(1107),
                     EMC_TOOL_SET_OFFSET_TYPE(1108),
                     EMC_TOOL_SET_NUMBER_TYPE(1109),
                     // the following message is sent to io at the very start of an M6
                     // even before emccanon issues the move to toolchange position
                     EMC_TOOL_START_CHANGE_TYPE(1110),

                     EMC_EXEC_PLUGIN_CALL_TYPE(1112),
                     EMC_IO_PLUGIN_CALL_TYPE(1113),
                     EMC_TOOL_STAT_TYPE(1199),

                     // EMC_AUX type declarations
                     EMC_AUX_ESTOP_ON_TYPE(1206),
                     EMC_AUX_ESTOP_OFF_TYPE(1207),
                     EMC_AUX_ESTOP_RESET_TYPE(1208),
                     EMC_AUX_INPUT_WAIT_TYPE(1209),

                     EMC_AUX_STAT_TYPE(1299),

                     // EMC_SPINDLE type declarations
                     EMC_SPINDLE_ON_TYPE(1304),
                     EMC_SPINDLE_OFF_TYPE(1305),
                     EMC_SPINDLE_INCREASE_TYPE(1309),
                     EMC_SPINDLE_DECREASE_TYPE(1310),
                     EMC_SPINDLE_CONSTANT_TYPE(1311),
                     EMC_SPINDLE_BRAKE_RELEASE_TYPE(1312),
                     EMC_SPINDLE_BRAKE_ENGAGE_TYPE(1313),
                     EMC_SPINDLE_SPEED_TYPE(1316),
                     EMC_SPINDLE_ORIENT_TYPE(1317),
                     EMC_SPINDLE_WAIT_ORIENT_COMPLETE_TYPE(1318),

                     EMC_SPINDLE_STAT_TYPE(1399),

                     // EMC_COOLANT type declarations
                     EMC_COOLANT_MIST_ON_TYPE(1404),
                     EMC_COOLANT_MIST_OFF_TYPE(1405),
                     EMC_COOLANT_FLOOD_ON_TYPE(1406),
                     EMC_COOLANT_FLOOD_OFF_TYPE(1407),

                     EMC_COOLANT_STAT_TYPE(1499),

                     // EMC_LUBE type declarations
                     EMC_LUBE_ON_TYPE(1504),
                     EMC_LUBE_OFF_TYPE(1505),
                     EMC_LUBE_STAT_TYPE(1599),

                     // EMC_IO aggregate class type declaration
                     EMC_IO_INIT_TYPE(1601),
                     EMC_IO_HALT_TYPE(1602),
                     EMC_IO_ABORT_TYPE(1603),
                     EMC_IO_SET_CYCLE_TIME_TYPE(1604),

                     EMC_IO_STAT_TYPE(1699),

                     // EMC aggregate class type declaration
                     // these are placeholders
                     EMC_LOG_TYPE_IO_CMD(21), // command into EMC IO controller
                     EMC_LOG_TYPE_TASK_CMD(51), // command into EMC Task controller

                     EMC_INIT_TYPE(1901),
                     EMC_HALT_TYPE(1902),
                     EMC_ABORT_TYPE(1903),
                     EMC_STAT_TYPE(1999);

   public int getTypeNum() {
      return type;
   }


   private NmlType(int type) {
      this.type = type;
   }

   private final int type;
}
