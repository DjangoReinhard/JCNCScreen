package de.schwarzrot.nml;
/*
 * **************************************************************************
 *
 *  file:       BufferDescriptor.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    28.10.2019 by Django Reinhard
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


import java.util.HashMap;
import java.util.Map;

import de.schwarzrot.nml.BufferEntry.BufferEntryType;


/**
 * Buffer descriptor for NML buffer of linuxcnc 2.9ff
 */
public class BufferDescriptor implements IBufferDescriptor {
   @Override
   public BufferEntry get(String key) {
      return bufferEntries.get(key);
   }


   private static final Map<String, BufferEntry> bufferEntries;
   static {
      //      [    20] echo_serial_number
      //      [    20] echo_serial_number
      //      [    24] state
      //      [   212] task_mode
      //      [   216] task_state
      //      [   220] exec_state
      //      [   224] interp_state
      //      [   228] call_level
      //      [   240] read_line
      //      [   232] motion_line
      //      [   236] current_line
      //      [   247] file
      //      [   502] command
      //      [  1144] program_units
      //      [  1148] interpreter_errcode
      //      [   244] optional_stop
      //      [   245] block_delete
      //      [  1152] task_paused
      //      [   246] input_timeout
      //      [   912] rotation_xy
      //      [  1160] delay_left
      //      [  1168] queued_mdi_commands
      //      [  1392] linear_units
      //      [  1400] angular_units
      //      [  1408] cycle_time
      //      [  1416] joints
      //      [  6592] spindle[0]
      //      [  1428] axis_mask
      //      [  1432] motion_mode
      //      [  1436] enabled
      //      [  1437] inpos
      //      [  1440] queue
      //      [  1444] active_queue
      //      [  1448] queue_full
      //      [  1452] id
      //      [  1456] paused
      //      [  1464] feedrate
      //      [  1472] rapidrate
      //      [  1624] velocityT
      //      [  5496] velocityA
      //      [  2104] velocityJ
      //      [  1632] acceleration
      //      [  1640] max_velocity
      //      [  1648] max_acceleration
      //      [  1728] probe_tripped
      //      [  1729] probing
      //      [  1732] probe_val
      //      [  1736] kinematics_type
      //      [  1740] motion_type
      //      [  1744] distance_to_go
      //      [  1824] current_vel
      //      [  1832] feed_override_enabled
      //      [  1833] adaptive_feed_enabled
      //      [  1834] feed_hold_enabled
      //      [   992] active_gcodes
      //      [  1060] active_mcodes
      //      [  1104] active_settings
      //      [  1480] position
      //      [  1552] actual_pos
      //      [  1752] dtg
      //      [   760] g5x_offset
      //      [   840] g92_offset
      //      [   920] tool_offset
      //      [  2116] joint_x_enabled
      //      [  2332] joint_y_enabled
      //      [  2548] joint_z_enabled
      //      [  2764] joint_a_enabled
      //      [  2980] joint_b_enabled
      //      [  3196] joint_c_enabled
      //      [  3412] joint_u_enabled
      //      [  3628] joint_v_enabled
      //      [  3844] joint_w_enabled
      //      [  2114] joint_x_homed
      //      [  2330] joint_y_homed
      //      [  2546] joint_z_homed
      //      [  2762] joint_a_homed
      //      [  2978] joint_b_homed
      //      [  3194] joint_c_homed
      //      [  3410] joint_u_homed
      //      [  3626] joint_v_homed
      //      [  3842] joint_w_homed
      //      [  6696] spindle[0]speed
      //      [  6704] spindle[0]scale
      //      [  6712] spindle[0]css_max
      //      [  6720] spindle[0]css_fak
      //      [  6728] spindle[0]dir
      //      [  6732] spindle[0]brake
      //      [  6736] spindle[0]increasing
      //      [  6740] spindle[0]enabled
      //      [  6744] spindle[0]orient_state
      //      [  6748] spindle[0]orient_fault
      //      [  6752] spindle[0]override_enable
      //      [  6753] spindle[0]homed
      //      [  9808] pocket_prepped
      //      [  9812] tool_in_spindle
      //      [  9816] tool_table
      //      [122032] mist
      //      [122036] flood
      //      [122144] estop
      //      [122256] lube
      //      [122260] lube_level
      //      [122264] debug

      //      TASK_SET_STATE - size: 24, offset of state: 20
      //      JOINT_HOME - size: 24, offset of joint: 20
      //      TASK_PLAN_OPEN - size: 280, offset of file: 20
      //      TASK_PLAN_RUN - size: 24, offset of line: 20
      //      SPINDLE_STAT - size: 168, max. number of spindles: 8
      //      ToolTable - size: 112, max. number of tool-entries: 1001
      //      EmcPose - size: 72

      bufferEntries = new HashMap<String, BufferEntry>();

      bufferEntries.put(BufferDescriptor.AbsPosX,
            new BufferEntry(BufferDescriptor.AbsPosX, 1480, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.RelPosX,
            new BufferEntry(BufferDescriptor.RelPosX, 1552, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.G5xOffsX,
            new BufferEntry(BufferDescriptor.G5xOffsX, 760, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.G92OffsX,
            new BufferEntry(BufferDescriptor.G92OffsX, 840, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.ToolOffsX,
            new BufferEntry(BufferDescriptor.ToolOffsX, 920, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.DtgX,
            new BufferEntry(BufferDescriptor.DtgX, 1752, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.RotationXY,
            new BufferEntry(BufferDescriptor.RotationXY, 912, 8, BufferEntryType.Double));
      bufferEntries.put(BufferDescriptor.File,
            new BufferEntry(BufferDescriptor.File, 247, 255, BufferEntryType.String));
      bufferEntries.put(BufferDescriptor.ReadLine,
            new BufferEntry(BufferDescriptor.ReadLine, 240, 4, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.MotionLine,
            new BufferEntry(BufferDescriptor.MotionLine, 232, 4, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.CurrentLine,
            new BufferEntry(BufferDescriptor.CurrentLine, 236, 4, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.Joint_0_enabled,
            new BufferEntry(BufferDescriptor.Joint_0_enabled, 2116, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_1_enabled,
            new BufferEntry(BufferDescriptor.Joint_1_enabled, 2332, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_2_enabled,
            new BufferEntry(BufferDescriptor.Joint_2_enabled, 2548, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_3_enabled,
            new BufferEntry(BufferDescriptor.Joint_3_enabled, 2764, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_4_enabled,
            new BufferEntry(BufferDescriptor.Joint_4_enabled, 2980, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_5_enabled,
            new BufferEntry(BufferDescriptor.Joint_5_enabled, 3196, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_6_enabled,
            new BufferEntry(BufferDescriptor.Joint_6_enabled, 3412, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_7_enabled,
            new BufferEntry(BufferDescriptor.Joint_7_enabled, 3628, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_8_enabled,
            new BufferEntry(BufferDescriptor.Joint_8_enabled, 3844, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.Joint_0_homed,
            new BufferEntry(BufferDescriptor.Joint_0_homed, 2114, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_1_homed,
            new BufferEntry(BufferDescriptor.Joint_1_homed, 2330, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_2_homed,
            new BufferEntry(BufferDescriptor.Joint_2_homed, 2546, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_3_homed,
            new BufferEntry(BufferDescriptor.Joint_3_homed, 2762, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_4_homed,
            new BufferEntry(BufferDescriptor.Joint_4_homed, 2978, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_5_homed,
            new BufferEntry(BufferDescriptor.Joint_5_homed, 3194, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_6_homed,
            new BufferEntry(BufferDescriptor.Joint_6_homed, 3410, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_7_homed,
            new BufferEntry(BufferDescriptor.Joint_7_homed, 3626, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joint_8_homed,
            new BufferEntry(BufferDescriptor.Joint_8_homed, 3842, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.ActiveGCodes,
            new BufferEntry(BufferDescriptor.ActiveGCodes, 992, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.ActiveMCodes,
            new BufferEntry(BufferDescriptor.ActiveMCodes, 1060, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.AxisMask,
            new BufferEntry(BufferDescriptor.AxisMask, 1428, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Joints,
            new BufferEntry(BufferDescriptor.Joints, 1416, 1, BufferEntryType.Byte));

      //      [  6696] spindle[0]speed
      //      [  6704] spindle[0]scale
      //      [  6712] spindle[0]css_max
      //      [  6720] spindle[0]css_fak
      //      [  6728] spindle[0]dir
      //      [  6732] spindle[0]brake
      //      [  6736] spindle[0]increasing
      //      [  6740] spindle[0]enabled
      //      [  6744] spindle[0]orient_state
      //      [  6748] spindle[0]orient_fault
      //      [  6752] spindle[0]override_enable
      //      [  6753] spindle[0]homed
      bufferEntries.put(BufferDescriptor.Spindles,
            new BufferEntry(BufferDescriptor.Spindles, 6592, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleSpeed,
            new BufferEntry(BufferDescriptor.SpindleSpeed, 6696, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleScale,
            new BufferEntry(BufferDescriptor.SpindleScale, 6704, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleDir,
            new BufferEntry(BufferDescriptor.SpindleDir, 6728, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleIncreasing,
            new BufferEntry(BufferDescriptor.SpindleIncreasing, 6736, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleOverrideEnable,
            new BufferEntry(BufferDescriptor.SpindleOverrideEnable, 6752, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleEnabled,
            new BufferEntry(BufferDescriptor.SpindleEnabled, 6740, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleHomed,
            new BufferEntry(BufferDescriptor.SpindleHomed, 6753, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.Feedrate,
            new BufferEntry(BufferDescriptor.Feedrate, 1464, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Rapidrate,
            new BufferEntry(BufferDescriptor.Rapidrate, 1472, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Velocity,
            new BufferEntry(BufferDescriptor.Velocity, 1624, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.VelocityA,
            new BufferEntry(BufferDescriptor.VelocityA, 5496, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.VelocityJ,
            new BufferEntry(BufferDescriptor.VelocityJ, 2104, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Acceleration,
            new BufferEntry(BufferDescriptor.Acceleration, 1632, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Max_velocity,
            new BufferEntry(BufferDescriptor.Max_velocity, 1640, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Max_acceleration,
            new BufferEntry(BufferDescriptor.Max_acceleration, 1648, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.ToolTable,
            new BufferEntry(BufferDescriptor.ToolTable, 9816, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.ToolInSpindle,
            new BufferEntry(BufferDescriptor.ToolInSpindle, 9812, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.PocketPrepared,
            new BufferEntry(BufferDescriptor.PocketPrepared, 9808, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.TaskMode,
            new BufferEntry(BufferDescriptor.TaskMode, 212, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.TaskState,
            new BufferEntry(BufferDescriptor.TaskState, 216, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.ExecState,
            new BufferEntry(BufferDescriptor.ExecState, 220, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.InterpState,
            new BufferEntry(BufferDescriptor.InterpState, 224, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.ProgramUnits,
            new BufferEntry(BufferDescriptor.ProgramUnits, 1144, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Distance2Go,
            new BufferEntry(BufferDescriptor.Distance2Go, 1752, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.ActiveSettings,
            new BufferEntry(BufferDescriptor.ActiveSettings, 1104, 3, BufferEntryType.Double));

      bufferEntries.put(BufferDescriptor.Current_vel,
            new BufferEntry(BufferDescriptor.Current_vel, 1824, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Feed_override_enabled,
            new BufferEntry(BufferDescriptor.Feed_override_enabled, 1832, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Adaptive_feed_enabled,
            new BufferEntry(BufferDescriptor.Adaptive_feed_enabled, 1833, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Feed_hold_enabled,
            new BufferEntry(BufferDescriptor.Feed_hold_enabled, 1834, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.CoolMist,
            new BufferEntry(BufferDescriptor.CoolMist, 122032, 1, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.CoolFlood,
            new BufferEntry(BufferDescriptor.CoolFlood, 122036, 1, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.Lube,
            new BufferEntry(BufferDescriptor.Lube, 122256, 1, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.Estop,
            new BufferEntry(BufferDescriptor.Estop, 122144, 1, BufferEntryType.Integer));
      bufferEntries.put(BufferDescriptor.Debug,
            new BufferEntry(BufferDescriptor.Debug, 122264, 1, BufferEntryType.Integer));
   }
}
