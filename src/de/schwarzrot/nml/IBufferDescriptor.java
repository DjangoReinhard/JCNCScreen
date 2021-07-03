package de.schwarzrot.nml;
/*
 * **************************************************************************
 *
 *  file:       IBufferDescriptor.java
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


import java.util.Set;


public interface IBufferDescriptor {
   public BufferEntry get(String key);


   public Set<String> keySet();


   public static final String AbsPosX               = "absPosX";
   public static final String Acceleration          = "acceleration";
   public static final String ActiveGCodes          = "activeGCodes";
   public static final String ActiveMCodes          = "activeMCodes";
   public static final String ActiveSettings        = "activeSettings";
   public static final String Adaptive_feed_enabled = "adaptive_feed_enabled";
   public static final String AxisMask              = "axisMask";
   public static final String CoolFlood             = "coolFlood";
   public static final String CoolMist              = "coolMist";
   public static final String CurrentLine           = "currentLine";
   public static final String Current_vel           = "current_vel";
   public static final String Debug                 = "debug";
   public static final String Distance2Go           = "distance2Go";
   public static final String DtgX                  = "dtgX";
   public static final String Estop                 = "estop";
   public static final String ExecState             = "execState";
   public static final String Feed_hold_enabled     = "feed_hold_enabled";
   public static final String Feed_override_enabled = "feed_override_enabled";
   public static final String Feedrate              = "feedrate";
   public static final String File                  = "file";
   public static final String G5xOffsX              = "g5xOffsX";
   public static final String G92OffsX              = "g92OffsX";
   public static final String InterpState           = "interpState";
   public static final String Joint_0_enabled       = "joint_0_enabled";
   public static final String Joint_0_homed         = "joint_0_homed";
   public static final String Joint_1_enabled       = "joint_1_enabled";
   public static final String Joint_1_homed         = "joint_1_homed";
   public static final String Joint_2_enabled       = "joint_2_enabled";
   public static final String Joint_2_homed         = "joint_2_homed";
   public static final String Joint_3_enabled       = "joint_3_enabled";
   public static final String Joint_3_homed         = "joint_3_homed";
   public static final String Joint_4_enabled       = "joint_4_enabled";
   public static final String Joint_4_homed         = "joint_4_homed";
   public static final String Joint_5_enabled       = "joint_5_enabled";
   public static final String Joint_5_homed         = "joint_5_homed";
   public static final String Joint_6_enabled       = "joint_6_enabled";
   public static final String Joint_6_homed         = "joint_6_homed";
   public static final String Joint_7_enabled       = "joint_7_enabled";
   public static final String Joint_7_homed         = "joint_7_homed";
   public static final String Joint_8_enabled       = "joint_8_enabled";
   public static final String Joint_8_homed         = "joint_8_homed";
   public static final String Joints                = "joints";
   public static final String Lube                  = "lube";
   public static final String Max_acceleration      = "max_acceleration";
   public static final String Max_velocity          = "max_velocity";
   public static final String MotionLine            = "motionLine";
   public static final String PocketPrepared        = "pocketPrepared";
   public static final String ProgramUnits          = "programUnits";
   public static final String Rapidrate             = "rapidrate";
   public static final String ReadLine              = "readLine";
   public static final String RelPosX               = "relPosX";
   public static final String RotationXY            = "rotationXY";
   public static final String SpindleDir            = "spindleDir";
   public static final String SpindleEnabled        = "spindleEnable";
   public static final String SpindleHomed          = "spindleHomed";
   public static final String SpindleIncreasing     = "spindleIncreasing";
   public static final String SpindleOverrideEnable = "spindleOverrideEnable";
   public static final String SpindleScale          = "spindleScale";
   public static final String SpindleSpeed          = "spindleSpeed";
   public static final String Spindles              = "spindles";
   public static final String TaskMode              = "taskMode";
   public static final String TaskState             = "taskState";
   public static final String ToolInSpindle         = "toolInSpindle";
   public static final String ToolOffsX             = "toolOffsX";
   public static final String ToolTable             = "toolTable";
   public static final String VelocityT             = "velocityT";
   public static final String VelocityA             = "velocityA";
   public static final String VelocityJ             = "velocityJ";
}
