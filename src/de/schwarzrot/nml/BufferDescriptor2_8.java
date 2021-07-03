package de.schwarzrot.nml;
/*
 * **************************************************************************
 *
 *  file:       BufferDescriptor2_8.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    29.10.2019 by Django Reinhard
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
import java.util.Set;

import de.schwarzrot.nml.BufferEntry.BufferEntryType;


/**
 * Buffer descriptor for NML buffer of linuxcnc 2.9ff
 */
public class BufferDescriptor2_8 implements IBufferDescriptor {
   @Override
   public BufferEntry get(String key) {
      return bufferEntries.get(key);
   }


   @Override
   public Set<String> keySet() {
      return bufferEntries.keySet();
   }


   private static final Map<String, BufferEntry> bufferEntries;
   static {
      // [ 20] echo_serial_number
      // [ 24] state
      // [ 212] task_mode
      // [ 216] task_state
      // [ 220] exec_state
      // [ 224] interp_state
      // [ 228] call_level
      // [ 240] read_line
      // [ 232] motion_line
      // [ 236] current_line
      // [ 247] file
      // [ 502] command
      // [ 1128] program_units                  <<<
      // [ 1132] interpreter_errcode            <<<
      // [ 244] optional_stop
      // [ 245] block_delete
      // [ 1136] task_paused                    <<<
      // [ 246] input_timeout
      // [ 912] rotation_xy
      // [ 1144] delay_left                     <<<
      // [ 992] active_gcodes
      // [ 1060] active_mcodes                  <<<
      // [ 1104] active_settings                <<<
      // [ 1152] queued_mdi_commands            <<<
      // [ 1376] linear_units                   <<<
      // [ 1384] angular_units                  <<<
      // [ 1400] joints                         <<<
      // [ 1408] axis_mask                      <<<
      // [ 1412] motion_mode                    <<<
      // [ 1416] enabled                        <<<
      // [ 1417] inpos                          <<<
      // [ 1420] queue                          <<<
      // [ 1424] active_queue                   <<<
      // [ 1428] queue_full                     <<<
      // [ 1432] id                             <<<
      // [ 1392] cycle_time                     <<<
      // [ 1464] position                       <<<
      // [ 1536] actual_pos                     <<<
      // [ 1736] dtg                            <<<
      // [ 760] g5x_offset
      // [ 840] g92_offset
      // [ 920] tool_offset
      // [ 2084] joint_x_enabled                <<<
      // [ 2300] joint_y_enabled                <<<
      // [ 2516] joint_z_enabled                <<<
      // [ 2732] joint_a_enabled                <<<
      // [ 2948] joint_b_enabled                <<<
      // [ 3164] joint_c_enabled                <<<
      // [ 3380] joint_u_enabled                <<<
      // [ 3596] joint_v_enabled                <<<
      // [ 3812] joint_w_enabled                <<<
      // [ 2082] joint_x_homed                  <<<
      // [ 2298] joint_y_homed                  <<<
      // [ 2514] joint_z_homed                  <<<
      // [ 2730] joint_a_homed                  <<<
      // [ 2946] joint_b_homed                  <<<
      // [ 3162] joint_c_homed                  <<<
      // [ 3378] joint_u_homed                  <<<
      // [ 3594] joint_v_homed                  <<<
      // [ 3810] joint_w_homed                  <<<
      // [ 1436] paused                         <<<
      // [ 1440] feedrate
      // [ 1448] rapidrate
      // [ 1608] velocityT                      <<<
      // [ 3952] velocityA                      <<<
      // [ 2072] velocityJ                      <<<
      // [ 1616] acceleration                   <<<
      // [ 1624] max_velocity                   <<<
      // [ 1632] max_acceleration               <<<
      // [ 1712] probe_tripped                  <<<
      // [ 1713] probing                        <<<
      // [ 1716] probe_val                      <<<
      // [ 1720] kinematics_type                <<<
      // [ 1724] motion_type                    <<<
      // [ 1728] distance_to_go                 <<<
      // [ 1808] current_vel                    <<<
      // [ 1816] feed_override_enabled          <<<
      // [ 1818] adaptive_feed_enabled          <<<
      // [ 1819] feed_hold_enabled              <<<
      // [ 6968] pocket_prepped                 <<<
      // [ 6972] tool_in_spindle                <<<
      // [ 6976] tool_table                     <<<
      // [ 13352] mist                          <<<
      // [ 13356] flood                         <<<
      // [ 13464] estop                         <<<
      // [ 13576] lube                          <<<
      // [ 13580] lube_level                    <<<
      // [ 13584] debug                         <<<
      // filename is: /usr/local/src/linuxcnc-dev/share/axis/images/axis.ngc
      // filename check: /usr/local/src/linuxcnc-dev/share/axis/images/axis.ngc
      // NMLmsg - size: 16, offset of type: 0
      // NMLmsg - size: 16, offset of automatically_clear: -23735234280
      // RCS_CMD_MSG: 24, offset of serial_number: 16
      bufferEntries = new HashMap<String, BufferEntry>();

      bufferEntries.put("absPosX", new BufferEntry("absPosX", 1464, 8, BufferEntryType.Double));
      bufferEntries.put("relPosX", new BufferEntry("relPosX", 1536, 8, BufferEntryType.Double));
      bufferEntries.put("g5xOffsX", new BufferEntry("g5xOffsX", 760, 8, BufferEntryType.Double));
      bufferEntries.put("g92OffsX", new BufferEntry("g92OffsX", 840, 8, BufferEntryType.Double));
      bufferEntries.put("toolOffsX", new BufferEntry("toolOffsX", 920, 8, BufferEntryType.Double));
      bufferEntries.put("dtgX", new BufferEntry("dtgX", 1736, 8, BufferEntryType.Double));
      bufferEntries.put("rotationXY", new BufferEntry("rotationXY", 912, 8, BufferEntryType.Double));
      bufferEntries.put("file", new BufferEntry("file", 247, 255, BufferEntryType.String));
      bufferEntries.put("readLine", new BufferEntry("readLine", 240, 4, BufferEntryType.Integer));
      bufferEntries.put("motionLine", new BufferEntry("motionLine", 232, 4, BufferEntryType.Integer));
      bufferEntries.put("currentLine", new BufferEntry("currentLine", 236, 4, BufferEntryType.Integer));
      bufferEntries.put("joint_x_enabled", new BufferEntry("joint_x_enabled", 2084, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_y_enabled", new BufferEntry("joint_y_enabled", 2300, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_z_enabled", new BufferEntry("joint_z_enabled", 2516, 1, BufferEntryType.Byte));
      // [ 2084] joint_x_enabled                <<<
      // [ 2300] joint_y_enabled                <<<
      // [ 2516] joint_z_enabled                <<<
      // [ 2732] joint_a_enabled                <<<
      // [ 2948] joint_b_enabled                <<<
      // [ 3164] joint_c_enabled                <<<
      // [ 3380] joint_u_enabled                <<<
      // [ 3596] joint_v_enabled                <<<
      // [ 3812] joint_w_enabled                <<<
      bufferEntries.put("joint_a_enabled", new BufferEntry("joint_a_enabled", 2732, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_b_enabled", new BufferEntry("joint_b_enabled", 2948, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_c_enabled", new BufferEntry("joint_c_enabled", 3164, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_u_enabled", new BufferEntry("joint_u_enabled", 3380, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_v_enabled", new BufferEntry("joint_v_enabled", 3596, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_w_enabled", new BufferEntry("joint_w_enabled", 3812, 1, BufferEntryType.Byte));

      bufferEntries.put("joint_x_homed", new BufferEntry("joint_x_homed", 2082, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_y_homed", new BufferEntry("joint_y_homed", 2298, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_z_homed", new BufferEntry("joint_z_homed", 2514, 1, BufferEntryType.Byte));
      // [ 2082] joint_x_homed                  <<<
      // [ 2298] joint_y_homed                  <<<
      // [ 2514] joint_z_homed                  <<<
      // [ 2730] joint_a_homed                  <<<
      // [ 2946] joint_b_homed                  <<<
      // [ 3162] joint_c_homed                  <<<
      // [ 3378] joint_u_homed                  <<<
      // [ 3594] joint_v_homed                  <<<
      // [ 3810] joint_w_homed                  <<<
      bufferEntries.put("joint_a_homed", new BufferEntry("joint_a_homed", 2730, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_b_homed", new BufferEntry("joint_b_homed", 2946, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_c_homed", new BufferEntry("joint_c_homed", 3162, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_u_homed", new BufferEntry("joint_u_homed", 3378, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_v_homed", new BufferEntry("joint_v_homed", 3594, 1, BufferEntryType.Byte));
      bufferEntries.put("joint_w_homed", new BufferEntry("joint_w_homed", 3810, 1, BufferEntryType.Byte));
      bufferEntries.put("estop", new BufferEntry("estop", 13464, 1, BufferEntryType.Integer));
      bufferEntries.put("debug", new BufferEntry("debug", 13584, 1, BufferEntryType.Integer));
      // [ 992] active_gcodes
      // [ 1056] active_mcodes
      bufferEntries.put("activeGCodes", new BufferEntry("activeGCodes", 992, 1, BufferEntryType.Byte));
      bufferEntries.put("activeMCodes", new BufferEntry("activeMCodes", 1060, 1, BufferEntryType.Byte));
      // [ 1404] axis_mask
      bufferEntries.put("axisMask", new BufferEntry("axisMask", 1408, 1, BufferEntryType.Byte));
      bufferEntries.put("joints", new BufferEntry("joints", 1400, 1, BufferEntryType.Byte));
      bufferEntries.put("spindles", new BufferEntry("spindles", 1396, 1, BufferEntryType.Byte));
      // [ 6600] spindle_speed
      // [ 6608] spindle_scale
      // [ 6632] spindle_dir
      // [ 6644] spindle_enabled
      bufferEntries.put("spindleSpeed", new BufferEntry("spindles", 6600, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleScale", new BufferEntry("spindleScale", 6608, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleDir", new BufferEntry("spindles", 6632, 1, BufferEntryType.Byte));
      bufferEntries.put("spindleEnabled", new BufferEntry("spindles", 6644, 1, BufferEntryType.Byte));
      // [ 1440] feedrate
      // [ 1448] rapidrate
      // [ 1600] velocity
      // [ 1608] acceleration
      // [ 1616] max_velocity
      // [ 1624] max_acceleration
      bufferEntries.put("feedrate", new BufferEntry("feedrate", 1440, 1, BufferEntryType.Byte));
      bufferEntries.put("rapidrate", new BufferEntry("rapidrate", 1448, 1, BufferEntryType.Byte));
      bufferEntries.put("velocity", new BufferEntry("velocity", 1608, 1, BufferEntryType.Byte));
      bufferEntries.put("velocityA", new BufferEntry("velocityA", 3952, 1, BufferEntryType.Byte));
      bufferEntries.put("velocityJ", new BufferEntry("velocityJ", 2072, 1, BufferEntryType.Byte));
      bufferEntries.put("acceleration", new BufferEntry("acceleration", 1608, 1, BufferEntryType.Byte));
      bufferEntries.put("max_velocity", new BufferEntry("max_velocity", 1624, 1, BufferEntryType.Byte));
      bufferEntries.put("max_acceleration",
            new BufferEntry("max_acceleration", 1624, 1, BufferEntryType.Byte));
      // [ 9712] pocket_prepped
      // [ 9716] tool_in_spindle
      // [ 9720] tool_table
      bufferEntries.put("toolTable", new BufferEntry("toolTable", 6976, 1, BufferEntryType.Byte));
      bufferEntries.put("toolInSpindle", new BufferEntry("toolInSpindle", 6972, 1, BufferEntryType.Byte));
      bufferEntries.put("pocketPrepared", new BufferEntry("pocketPrepared", 9712, 1, BufferEntryType.Byte));

      // [ 212] task_mode - manual, auto, mdi
      // [ 216] task_state - estop, estop_reset, off, on
      // [ 220] exec_state - error, done, waiting ...
      // [ 224] interp_state - idle, reading, paused, waiting
      bufferEntries.put("taskMode", new BufferEntry("taskMode", 212, 1, BufferEntryType.Byte));
      bufferEntries.put("taskState", new BufferEntry("taskState", 216, 1, BufferEntryType.Byte));
      bufferEntries.put("execState", new BufferEntry("execState", 220, 1, BufferEntryType.Byte));
      bufferEntries.put("interpState", new BufferEntry("interpState", 224, 1, BufferEntryType.Byte));
      // [ 1120] program_units
      bufferEntries.put("programUnits", new BufferEntry("programUnits", 1128, 1, BufferEntryType.Byte));
      bufferEntries.put("distance2Go", new BufferEntry("distance2Go", 1728, 1, BufferEntryType.Byte));
      bufferEntries.put("activeSettings", new BufferEntry("activeSettings", 1104, 3, BufferEntryType.Double));
      // [ 13352] mist                          <<<
      // [ 13356] flood                         <<<
      // [ 13464] estop                         <<<
      // [ 13576] lube                          <<<
      // [ 13580] lube_level                    <<<
      bufferEntries.put("current_vel", new BufferEntry("current_vel", 1800, 1, BufferEntryType.Byte));
      bufferEntries.put("feed_override_enabled",
            new BufferEntry("feed_override_enabled", 1808, 1, BufferEntryType.Byte));
      bufferEntries.put("adaptive_feed_enabled",
            new BufferEntry("adaptive_feed_enabled", 1809, 1, BufferEntryType.Byte));
      bufferEntries.put("feed_hold_enabled",
            new BufferEntry("feed_hold_enabled", 1810, 1, BufferEntryType.Byte));
      bufferEntries.put("coolMist", new BufferEntry("coolMist", 13352, 1, BufferEntryType.Integer));
      bufferEntries.put("coolFlood", new BufferEntry("coolFlood", 13356, 1, BufferEntryType.Integer));
      bufferEntries.put("lube", new BufferEntry("lube", 13576, 1, BufferEntryType.Integer));
   }
}
