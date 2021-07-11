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
import java.util.Set;

import de.schwarzrot.nml.BufferEntry.BufferEntryType;


/**
 * Buffer descriptor for NML buffer of linuxcnc 2.9ff
 */
public class BufferDescriptor implements IBufferDescriptor {
   @Override
   public BufferEntry get(String key) {
      return bufferEntries.get(key);
   }


   @Override
   public Set<String> keySet() {
      return bufferEntries.keySet();
   }


   private static final Map<String, BufferEntry> bufferEntries;
   private static final boolean                  nmlHasToolTable = false;
   static {
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

      bufferEntries.put(BufferDescriptor.Spindles,
            new BufferEntry(BufferDescriptor.Spindles, 6592, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleSpeed,
            new BufferEntry(BufferDescriptor.SpindleSpeed, 6696, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleScale,
            new BufferEntry(BufferDescriptor.SpindleScale, 6704, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleDir,
            new BufferEntry(BufferDescriptor.SpindleDir, 6732, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleIncreasing,
            new BufferEntry(BufferDescriptor.SpindleIncreasing, 6740, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleOverrideEnable,
            new BufferEntry(BufferDescriptor.SpindleOverrideEnable, 6756, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleEnabled,
            new BufferEntry(BufferDescriptor.SpindleEnabled, 6744, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.SpindleHomed,
            new BufferEntry(BufferDescriptor.SpindleHomed, 6757, 1, BufferEntryType.Byte));

      bufferEntries.put(BufferDescriptor.Feedrate,
            new BufferEntry(BufferDescriptor.Feedrate, 1464, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.Rapidrate,
            new BufferEntry(BufferDescriptor.Rapidrate, 1472, 1, BufferEntryType.Byte));
      bufferEntries.put(BufferDescriptor.VelocityT,
            new BufferEntry(BufferDescriptor.VelocityT, 1624, 1, BufferEntryType.Byte));
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
            new BufferEntry(BufferDescriptor.Distance2Go, 1744, 1, BufferEntryType.Byte));
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

      if (nmlHasToolTable) {
         bufferEntries.put(BufferDescriptor.ToolTable,
               new BufferEntry(BufferDescriptor.ToolTable, 9816, 1, BufferEntryType.Byte));

         bufferEntries.put(BufferDescriptor.CoolFlood,
               new BufferEntry(BufferDescriptor.CoolFlood, 122036, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.CoolMist,
               new BufferEntry(BufferDescriptor.CoolMist, 122032, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Debug,
               new BufferEntry(BufferDescriptor.Debug, 122264, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Estop,
               new BufferEntry(BufferDescriptor.Estop, 122144, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Lube,
               new BufferEntry(BufferDescriptor.Lube, 122256, 1, BufferEntryType.Integer));
      } else {
         bufferEntries.put(BufferDescriptor.CoolFlood,
               new BufferEntry(BufferDescriptor.CoolFlood, 10036, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.CoolMist,
               new BufferEntry(BufferDescriptor.CoolMist, 10032, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Debug,
               new BufferEntry(BufferDescriptor.Debug, 10264, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Estop,
               new BufferEntry(BufferDescriptor.Estop, 10144, 1, BufferEntryType.Integer));
         bufferEntries.put(BufferDescriptor.Lube,
               new BufferEntry(BufferDescriptor.Lube, 10256, 1, BufferEntryType.Integer));
      }
   }
}
