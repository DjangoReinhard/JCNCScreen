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
      //      [   992] active_gcodes
      //      [  1060] active_mcodes
      //      [  1104] active_settings
      //      [  1168] queued_mdi_commands
      //      [  1392] linear_units
      //      [  1400] angular_units
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
      //      [  1408] cycle_time
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
      //      [  6592] spindle[0]
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

      bufferEntries.put("absPosX", new BufferEntry("absPosX", 1480, 8, BufferEntryType.Double));
      bufferEntries.put("relPosX", new BufferEntry("relPosX", 1552, 8, BufferEntryType.Double));
      bufferEntries.put("g5xOffsX", new BufferEntry("g5xOffsX", 760, 8, BufferEntryType.Double));
      bufferEntries.put("g92OffsX", new BufferEntry("g92OffsX", 840, 8, BufferEntryType.Double));
      bufferEntries.put("toolOffsX", new BufferEntry("toolOffsX", 920, 8, BufferEntryType.Double));
      bufferEntries.put("dtgX", new BufferEntry("dtgX", 1744, 8, BufferEntryType.Double));
      bufferEntries.put("rotationXY", new BufferEntry("rotationXY", 912, 8, BufferEntryType.Double));
      bufferEntries.put("file", new BufferEntry("file", 247, 255, BufferEntryType.String));
      bufferEntries.put("readLine", new BufferEntry("readLine", 240, 4, BufferEntryType.Integer));
      bufferEntries.put("motionLine", new BufferEntry("motionLine", 232, 4, BufferEntryType.Integer));
      bufferEntries.put("currentLine", new BufferEntry("currentLine", 236, 4, BufferEntryType.Integer));
      bufferEntries.put("joint_0_enabled", new BufferEntry("joint_0_enabled", 2116, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_1_enabled", new BufferEntry("joint_1_enabled", 2332, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_2_enabled", new BufferEntry("joint_2_enabled", 2548, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_3_enabled", new BufferEntry("joint_3_enabled", 2764, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_4_enabled", new BufferEntry("joint_4_enabled", 2980, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_5_enabled", new BufferEntry("joint_5_enabled", 3196, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_6_enabled", new BufferEntry("joint_6_enabled", 3412, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_7_enabled", new BufferEntry("joint_7_enabled", 3628, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_8_enabled", new BufferEntry("joint_8_enabled", 3844, 1, BufferEntryType.Byte));

      bufferEntries.put("joint_0_homed", new BufferEntry("joint_0_homed", 2114, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_1_homed", new BufferEntry("joint_1_homed", 2330, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_2_homed", new BufferEntry("joint_2_homed", 2546, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_3_homed", new BufferEntry("joint_3_homed", 2762, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_4_homed", new BufferEntry("joint_4_homed", 2978, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_5_homed", new BufferEntry("joint_5_homed", 3194, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_6_homed", new BufferEntry("joint_6_homed", 3410, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_7_homed", new BufferEntry("joint_7_homed", 3626, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_8_homed", new BufferEntry("joint_8_homed", 3842, 1, BufferEntryType.Byte));

      bufferEntries.put("activeGCodes", new BufferEntry("activeGCodes", 992, 1, BufferEntryType.Byte));
      bufferEntries.put("activeMCodes", new BufferEntry("activeMCodes", 1060, 1, BufferEntryType.Byte));
      bufferEntries.put("axisMask", new BufferEntry("axisMask", 1428, 1, BufferEntryType.Byte));
      bufferEntries.put("joints", new BufferEntry("joints", 1416, 1, BufferEntryType.Byte));

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
      bufferEntries.put("spindles", new BufferEntry("spindles", 6592, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleSpeed", new BufferEntry("spindleSpeed", 6696, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleScale", new BufferEntry("spindleScale", 6704, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleDir", new BufferEntry("spindleDir", 6728, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleIncreasing",
            new BufferEntry("spindleIncreasing", 6736, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleOverrideEnable",
            new BufferEntry("spindleOverrideEnable", 6752, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleEnabled", new BufferEntry("spindleEnable", 6740, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleHomed", new BufferEntry("spindleHomed", 6753, 1, BufferEntryType.Byte));

      bufferEntries.put("feedrate", new BufferEntry("feedrate", 1464, 1, BufferEntryType.Byte));
      bufferEntries.put("rapidrate", new BufferEntry("rapidrate", 1472, 1, BufferEntryType.Byte));
      bufferEntries.put("velocity", new BufferEntry("velocity", 1624, 1, BufferEntryType.Byte));
      bufferEntries.put("velocityA", new BufferEntry("velocityA", 5496, 1, BufferEntryType.Byte));
      bufferEntries.put("velocityJ", new BufferEntry("velocityJ", 2104, 1, BufferEntryType.Byte));
      bufferEntries.put("acceleration", new BufferEntry("acceleration", 1632, 1, BufferEntryType.Byte));
      bufferEntries.put("max_velocity", new BufferEntry("max_velocity", 1640, 1, BufferEntryType.Byte));
      bufferEntries.put("max_acceleration",
            new BufferEntry("max_acceleration", 1648, 1, BufferEntryType.Byte));
      bufferEntries.put("toolTable", new BufferEntry("toolTable", 9816, 1, BufferEntryType.Byte));
      bufferEntries.put("toolInSpindle", new BufferEntry("toolInSpindle", 9812, 1, BufferEntryType.Byte));
      bufferEntries.put("pocketPrepared", new BufferEntry("pocketPrepared", 9808, 1, BufferEntryType.Byte));

      bufferEntries.put("taskMode", new BufferEntry("taskMode", 212, 1, BufferEntryType.Byte));
      bufferEntries.put("taskState", new BufferEntry("taskState", 216, 1, BufferEntryType.Byte));
      bufferEntries.put("execState", new BufferEntry("execState", 220, 1, BufferEntryType.Byte));
      bufferEntries.put("interpState", new BufferEntry("interpState", 224, 1, BufferEntryType.Byte));

      bufferEntries.put("programUnits", new BufferEntry("programUnits", 1144, 1, BufferEntryType.Byte));
      bufferEntries.put("distance2Go", new BufferEntry("distance2Go", 1752, 1, BufferEntryType.Byte));
      bufferEntries.put("activeSettings", new BufferEntry("activeSettings", 1104, 3, BufferEntryType.Double));

      bufferEntries.put("current_vel", new BufferEntry("current_vel", 1824, 1, BufferEntryType.Byte));
      bufferEntries.put("feed_override_enabled",
            new BufferEntry("feed_override_enabled", 1832, 1, BufferEntryType.Byte));
      bufferEntries.put("adaptive_feed_enabled",
            new BufferEntry("adaptive_feed_enabled", 1833, 1, BufferEntryType.Byte));
      bufferEntries.put("feed_hold_enabled",
            new BufferEntry("feed_hold_enabled", 1834, 1, BufferEntryType.Byte));

      bufferEntries.put("coolMist", new BufferEntry("coolMist", 122032, 1, BufferEntryType.Integer));
      bufferEntries.put("coolFlood", new BufferEntry("coolFlood", 122036, 1, BufferEntryType.Integer));
      bufferEntries.put("lube", new BufferEntry("lube", 122256, 1, BufferEntryType.Integer));
      bufferEntries.put("estop", new BufferEntry("estop", 122144, 1, BufferEntryType.Integer));
      bufferEntries.put("debug", new BufferEntry("debug", 122264, 1, BufferEntryType.Integer));
   }
}
