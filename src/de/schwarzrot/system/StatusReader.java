package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       StatusReader.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    2.9.2019 by Django Reinhard
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


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Map;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolEntry;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.SpeedInfo;
import de.schwarzrot.model.ToolInfo;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.BufferEntry;
import de.schwarzrot.nml.IBufferDescriptor;

import ca.odell.glazedlists.EventList;


public class StatusReader {
   public StatusReader(ErrorReader errorReader, IBufferDescriptor bufDesc) {
      this.initializationCompleted = false;
      this.errorReader             = errorReader;
      this.bufDesc                 = bufDesc;
      this.activeSpindle           = 0;
      statusBuffer                 = init();
      statusBuffer.order(ByteOrder.LITTLE_ENDIAN);
      status = LCStatus.getStatus();
      //      checkBuffer();
      readSetup();
      update(); // at least once before timer start
      this.initializationCompleted = true;
   }


   public ByteBuffer getStatusBuffer() {
      return statusBuffer;
   }


   public boolean isInitializationCompleted() {
      return initializationCompleted;
   }


   public void update() {
      // Date begin = new Date();
      SystemMessage sm = errorReader.fetchMessage();

      if (sm != null) {
         System.out.println("got Error-Message: " + sm.getMessage());
         sm.setTime(new Date());
         errorReader.riseError(sm);
         return;
      }
      readStatus();
      if (statusBuffer == null)
         return;

      handlePosition();
      handleGCodeFile();
      handleSignals();
      handleActiveCodes();
      handleSpeed();
      handleStates();
      handleToolChange();
      // Date end = new Date();
      //
      // System.out.println("update took " + (end.getTime() - begin.getTime()));
      //      System.out.println();
      //      System.out.println(LCStatus.getStatus().getModel("taskState"));
      //      System.out.println(LCStatus.getStatus().getModel("taskMode"));
      //      System.out.println(LCStatus.getStatus().getModel("execState"));
      //      System.out.println(LCStatus.getStatus().getModel("interpState"));
      //      System.out.println(LCStatus.getStatus().getModel("applicationMode"));
   }


   protected native String getString(int offset, int length);


   protected void handleActiveCodes() {
      BufferEntry e = bufDesc.get("activeGCodes");
      int         v;

      for (int i = 0; i < 16; ++i) {
         v = statusBuffer.getInt(e.offset + 4 * i);

         // System.out.println("got active GCode(" + i + "): " + v);

         switch (i) {
            case 0:
               status.getActiveCodes().setGcode0(v);
               break;
            case 1:
               status.getActiveCodes().setGcode1(v);
               break;
            case 2:
               status.getActiveCodes().setGcode2(v);
               break;
            case 3:
               status.getActiveCodes().setGcode3(v);
               break;
            case 4:
               status.getActiveCodes().setGcode4(v);
               break;
            case 5:
               status.getActiveCodes().setGcode5(v);
               break;
            case 6:
               status.getActiveCodes().setGcode6(v);
               break;
            case 7:
               status.getActiveCodes().setGcode7(v);
               break;
            case 8:
               status.getActiveCodes().setGcode8(v);
               break;
            case 9:
               status.getActiveCodes().setGcode9(v);
               break;
            case 10:
               status.getActiveCodes().setGcode10(v);
               break;
            case 11:
               status.getActiveCodes().setGcode11(v);
               break;
            case 12:
               status.getActiveCodes().setGcode12(v);
               break;
            case 13:
               status.getActiveCodes().setGcode13(v);
               break;
            case 14:
               status.getActiveCodes().setGcode14(v);
               break;
            case 15:
               status.getActiveCodes().setGcode15(v);
               break;
         }
      }

      e = bufDesc.get("activeMCodes");
      for (int i = 0; i < 10; ++i) {
         v = statusBuffer.getInt(e.offset + 4 * i);

         // System.out.println("got active MCode(" + i + "): " + v);

         switch (i) {
            case 0:
               status.getActiveCodes().setMcode0(v);
               break;
            case 1:
               status.getActiveCodes().setMcode1(v);
               break;
            case 2:
               status.getActiveCodes().setMcode2(v);
               break;
            case 3:
               status.getActiveCodes().setMcode3(v);
               break;
            case 4:
               status.getActiveCodes().setMcode4(v);
               break;
            case 5:
               status.getActiveCodes().setMcode5(v);
               break;
            case 6:
               status.getActiveCodes().setMcode6(v);
               break;
            case 7:
               status.getActiveCodes().setMcode7(v);
               break;
            case 8:
               status.getActiveCodes().setMcode8(v);
               break;
            case 9:
               status.getActiveCodes().setMcode9(v);
               break;
         }
      }
      e = bufDesc.get("activeSettings");
      // System.out.println("??? activeSettings[0] == "
      // + statusBuffer.getDouble(e.offset));
      status.getSpeedInfo().setNominalFeed(statusBuffer.getDouble(e.offset + 8));
      status.getSpeedInfo().setSpindleNominalSpeed(statusBuffer.getDouble(e.offset + 16));
   }


   protected void handleGCodeFile() {
      BufferEntry e = bufDesc.get("file");

      status.getGCodeInfo().setFileName(getString(e.offset, e.size));

      // e = bufDesc.get("readLine");
      // status.getGCodeInfo().setReadLine(statusBuffer.getInt(e.offset));

      e = bufDesc.get("motionLine");
      int line       = status.getGCodeInfo().getFrontendLine();
      int remoteLine = statusBuffer.getInt(e.offset);

      status.getGCodeInfo().setBackendLine(remoteLine);
      //      if (remoteLine > line)
      status.getGCodeInfo().setFrontendLine(remoteLine);

      // e = bufDesc.get("currentLine");
      // status.getGCodeInfo().setCurrentLine(statusBuffer.getInt(e.offset));

      // e = bufferEntries.get("axisMask");
      // int axis = statusBuffer.getInt(e.offset);
      //
      // System.out.println("axis mask is " + String.format("0x%X", axis));
      // System.out.println(status.getGCodeInfo());
      //      System.out.println("\tbackend line: #" + status.getGCodeInfo().getBackendLine());
      //      System.out.println("\tfrontend line: #" + status.getGCodeInfo().getFrontendLine());
   }


   @SuppressWarnings("unchecked")
   protected int handleMultiSpindle() {
      BufferEntry e;
      boolean     spindleSeen = false;
      int         enabled;
      int         cSpindles   = 0;

      // [ 6600] spindle_speed
      // [ 6608] spindle_scale
      // [ 6632] spindle_dir
      // [ 6644] spindle_enabled
      for (int i = 0; i < 8; ++i) {
         e       = bufDesc.get("spindleEnabled");
         enabled = statusBuffer.getInt(e.offset + i * 168);

         if (enabled != 0) {
            ++cSpindles;
            spindleSeen = true;
            e           = bufDesc.get("spindleSpeed");
            status.getSpeedInfo().setSpindleCurSpeed(statusBuffer.getDouble(e.offset + i * 168));

            e = bufDesc.get("spindleScale");
            status.getSpeedInfo().setSpindleFactor(statusBuffer.getDouble(e.offset + i * 168) * 100.0);

            e = bufDesc.get("spindleDir");
            status.getModel("spindleDir").setValue(statusBuffer.getInt(e.offset + i * 168));
         }
      }
      if (!spindleSeen) {
         status.getModel("spindleDir").setValue(0);
         status.getSpeedInfo().setSpindleCurSpeed(0);
      }
      // System.out.println(status.getSpeedInfo());
      // System.out.println();

      return cSpindles;
   }


   @SuppressWarnings("unchecked")
   protected void handlePosition() {
      // System.out.println("StatusReader.update() ...");
      //      BufferEntry   e    = bufDesc.get("relPosX");
      BufferEntry   e    = bufDesc.get("absPosX");
      CanonPosition p    = new CanonPosition();
      CanonPosition g5xO = new CanonPosition();
      CanonPosition tO   = new CanonPosition();
      CanonPosition g92O = new CanonPosition();

      p.setX(statusBuffer.getDouble(e.offset));
      p.setY(statusBuffer.getDouble(e.offset + 8));
      p.setZ(statusBuffer.getDouble(e.offset + 16));
      p.setA(statusBuffer.getDouble(e.offset + 24));
      p.setB(statusBuffer.getDouble(e.offset + 32));
      p.setC(statusBuffer.getDouble(e.offset + 40));
      p.setU(statusBuffer.getDouble(e.offset + 48));
      p.setV(statusBuffer.getDouble(e.offset + 56));
      p.setW(statusBuffer.getDouble(e.offset + 64));

      e = bufDesc.get("g5xOffsX");
      g5xO.setX(statusBuffer.getDouble(e.offset));
      g5xO.setY(statusBuffer.getDouble(e.offset + 8));
      g5xO.setZ(statusBuffer.getDouble(e.offset + 16));
      g5xO.setA(statusBuffer.getDouble(e.offset + 24));
      g5xO.setB(statusBuffer.getDouble(e.offset + 32));
      g5xO.setC(statusBuffer.getDouble(e.offset + 40));
      g5xO.setU(statusBuffer.getDouble(e.offset + 48));
      g5xO.setV(statusBuffer.getDouble(e.offset + 56));
      g5xO.setW(statusBuffer.getDouble(e.offset + 64));
      // System.out.println("g5x Offset: " + g5xO);

      e = bufDesc.get("toolOffsX");
      tO.setX(statusBuffer.getDouble(e.offset));
      tO.setY(statusBuffer.getDouble(e.offset + 8));
      tO.setZ(statusBuffer.getDouble(e.offset + 16));
      tO.setA(statusBuffer.getDouble(e.offset + 24));
      tO.setB(statusBuffer.getDouble(e.offset + 32));
      tO.setC(statusBuffer.getDouble(e.offset + 40));
      tO.setU(statusBuffer.getDouble(e.offset + 48));
      tO.setV(statusBuffer.getDouble(e.offset + 56));
      tO.setW(statusBuffer.getDouble(e.offset + 64));
      //      System.out.println("toolOffset: " + tO);

      e = bufDesc.get("g92OffsX");
      g92O.setX(statusBuffer.getDouble(e.offset));
      g92O.setY(statusBuffer.getDouble(e.offset + 8));
      g92O.setZ(statusBuffer.getDouble(e.offset + 16));
      g92O.setA(statusBuffer.getDouble(e.offset + 24));
      g92O.setB(statusBuffer.getDouble(e.offset + 32));
      g92O.setC(statusBuffer.getDouble(e.offset + 40));
      g92O.setU(statusBuffer.getDouble(e.offset + 48));
      g92O.setV(statusBuffer.getDouble(e.offset + 56));
      g92O.setW(statusBuffer.getDouble(e.offset + 64));
      // System.out.println("g92 Offset: " + g92O);

      e = bufDesc.get("rotationXY");
      double v = statusBuffer.getDouble(e.offset);

      status.getPositionCalculator().update(p, g5xO, tO, g92O, v);

      e = bufDesc.get("dtgX");
      status.getDistanceToGo().setX(statusBuffer.getDouble(e.offset));
      status.getDistanceToGo().setY(statusBuffer.getDouble(e.offset + 8));
      status.getDistanceToGo().setZ(statusBuffer.getDouble(e.offset + 16));
      status.getDistanceToGo().setA(statusBuffer.getDouble(e.offset + 24));
      status.getDistanceToGo().setB(statusBuffer.getDouble(e.offset + 32));
      status.getDistanceToGo().setC(statusBuffer.getDouble(e.offset + 40));
      status.getDistanceToGo().setU(statusBuffer.getDouble(e.offset + 48));
      status.getDistanceToGo().setV(statusBuffer.getDouble(e.offset + 56));
      status.getDistanceToGo().setW(statusBuffer.getDouble(e.offset + 64));
      // System.out.println("dtg: " + distanceToGo);
      e = bufDesc.get("distance2Go");
      status.getModel("DTG").setValue(statusBuffer.getDouble(e.offset));
   }


   @SuppressWarnings({ "incomplete-switch", "unchecked" })
   protected void handleSignals() {
      @SuppressWarnings("rawtypes")
      Map<String, ValueModel> models = status.getValueModels();
      BufferEntry             e      = null;
      ValueModel<Boolean>     m      = null;

      for (String n : listOfSignals) {
         e = bufDesc.get(n);
         m = models.get(n);

         if (m == null) {
            m = new ValueModel<Boolean>(n, false);
            models.put(n, m);
            //            System.out.print("process signal \"" + n + "\", with NEWly CREATED model ");
            //         } else {
            //            System.out.print("process signal \"" + n + "\", with existing model ");
         }
         switch (e.type) {
            case Byte:
               m.setValue(statusBuffer.get(e.offset) != 0);
               break;
            case Short:
               m.setValue(statusBuffer.getShort(e.offset) != 0);
               break;
            case Integer:
               m.setValue(statusBuffer.getInt(e.offset) != 0);
               break;
         }
         //         if ((n.compareTo("coolMist") == 0) || (n.compareTo("coolFlood") == 0))
         //            System.out.println(" value<" + n + "> is: " + m.getValue());
      }
   }


   // [ 1440] feedrate
   // [ 1448] rapidrate
   // [ 1600] velocity
   // [ 1608] acceleration
   // [ 1616] max_velocity
   // [ 1624] max_acceleration
   protected void handleSpeed() {
      BufferEntry e     = bufDesc.get("feedrate");
      double      value = statusBuffer.getDouble(e.offset);
      SpeedInfo   si    = status.getSpeedInfo();

      si.setFeedFactor(value * 100.0);

      e     = bufDesc.get("rapidrate");
      value = statusBuffer.getDouble(e.offset);
      si.setRapidFactor(value * 100.0);

      e     = bufDesc.get("acceleration");
      value = statusBuffer.getDouble(e.offset);
      si.setAccel(value);

      e     = bufDesc.get("max_velocity");
      value = statusBuffer.getDouble(e.offset);
      // speed is in unit/s
      si.setMaxSpeed(value * 60);

      e     = bufDesc.get("current_vel");
      value = statusBuffer.getDouble(e.offset);
      // speed is in unit/s
      si.setCurFeed(value * 60);

      e     = bufDesc.get("max_acceleration");
      value = statusBuffer.getDouble(e.offset);
      si.setMaxAccel(value);

      handleSpindle();
   }


   @SuppressWarnings("unchecked")
   protected int handleSpindle() {
      BufferEntry e;

      e = bufDesc.get("spindleSpeed");
      status.getSpeedInfo().setSpindleCurSpeed(statusBuffer.getDouble(e.offset + activeSpindle * 168));

      e = bufDesc.get("spindleScale");
      status.getSpeedInfo().setSpindleFactor(statusBuffer.getDouble(e.offset + activeSpindle * 168) * 100.0);

      e = bufDesc.get("spindleDir");
      status.getModel("spindleDir").setValue(statusBuffer.getInt(e.offset + activeSpindle * 168));

      return 1;
   }


   protected void handleStates() {
      BufferEntry e = bufDesc.get("taskMode");

      status.setTaskMode(statusBuffer.getInt(e.offset));
      e = bufDesc.get("taskState");
      status.setTaskState(statusBuffer.getInt(e.offset));
      e = bufDesc.get("execState");
      status.setExecState(statusBuffer.getInt(e.offset));
      e = bufDesc.get("interpState");
      status.setInterpState(statusBuffer.getInt(e.offset));

      // status.dump();
      //      System.out.println(status.getModel("taskState"));
      //      System.out.println(status.getModel("taskMode"));
      //      System.out.println(status.getModel("execState"));
      //      System.out.println(status.getModel("interpState"));
      //      System.out.println();
   }


   protected void handleToolChange() {
      BufferEntry e        = bufDesc.get("toolInSpindle");
      int         toolNum  = statusBuffer.getInt(e.offset);
      ToolInfo    toolInfo = status.getToolInfo();

      if (toolNum == 0)
         toolNum = 99;
      toolInfo.setActiveToolNum(toolNum);
      e = bufDesc.get("pocketPrepared");
      toolInfo.setNextToolNum(statusBuffer.getInt(e.offset));

      e = bufDesc.get("toolOffsX");
      toolInfo.getOffset().setX(statusBuffer.getDouble(e.offset));
      toolInfo.getOffset().setY(statusBuffer.getDouble(e.offset + 8));
      toolInfo.getOffset().setZ(statusBuffer.getDouble(e.offset + 16));
      toolInfo.getOffset().setA(statusBuffer.getDouble(e.offset + 24));
      toolInfo.getOffset().setB(statusBuffer.getDouble(e.offset + 32));
      toolInfo.getOffset().setC(statusBuffer.getDouble(e.offset + 40));
      toolInfo.getOffset().setU(statusBuffer.getDouble(e.offset + 48));
      toolInfo.getOffset().setV(statusBuffer.getDouble(e.offset + 56));
      toolInfo.getOffset().setW(statusBuffer.getDouble(e.offset + 64));
   }


   protected void readSetup() {
      readStatus();
      BufferEntry e        = bufDesc.get("axisMask");
      int         axisMask = statusBuffer.getInt(e.offset);

      e = bufDesc.get("joints");
      int numJoints = statusBuffer.getInt(e.offset);

      //      System.out.println("SETUP: axis mask is " + String.format("0x%X", axisMask));
      status.getSetup().setAxisMask(axisMask);
      status.getSetup().setNumJoints(numJoints);

      e = bufDesc.get("programUnits");
      status.getSetup().setUnit(statusBuffer.getInt(e.offset));

      e = bufDesc.get("spindles");
      int numSpindles = statusBuffer.getInt(e.offset);

      status.getSetup().setNumSpindles(numSpindles);

      readToolsDefinitions();
      handleToolChange();
      handleSignals();
   }


   protected native final void readStatus();


   protected void readToolsDefinitions() {
      EventList<ToolEntry> tools   = status.getSetup().getTools();
      BufferEntry          e       = bufDesc.get("toolTable");
      ToolEntry            tool    = null;
      int                  base    = 0;
      int                  toolNum = 0;

      tools.getReadWriteLock().writeLock().lock();
      tools.clear();

      for (int i = 0; i < ToolEntry.MaxNumOfToolEntries; ++i) {
         base    = e.offset + i * 112;
         toolNum = statusBuffer.getInt(base);

         if (toolNum < 0)
            continue;
         tool = new ToolEntry();

         tool.setToolNumber(toolNum);
         tool.setPocketUsed(statusBuffer.getInt(base + 4));
         tool.getOffset().setX(statusBuffer.getDouble(base + 8));
         tool.getOffset().setY(statusBuffer.getDouble(base + 16));
         tool.getOffset().setZ(statusBuffer.getDouble(base + 24));
         tool.getOffset().setA(statusBuffer.getDouble(base + 32));
         tool.getOffset().setB(statusBuffer.getDouble(base + 40));
         tool.getOffset().setC(statusBuffer.getDouble(base + 48));
         tool.getOffset().setU(statusBuffer.getDouble(base + 56));
         tool.getOffset().setV(statusBuffer.getDouble(base + 64));
         tool.getOffset().setW(statusBuffer.getDouble(base + 72));
         tool.setDiameter(statusBuffer.getDouble(base + 80));
         tool.setFrontAngle(statusBuffer.getDouble(base + 88));
         tool.setBackAngle(statusBuffer.getDouble(base + 96));
         tool.setOrientation(statusBuffer.getInt(base + 104));

         // System.out.println("got tool ...: " + tool);
         tools.add(tool);
      }
      tools.getReadWriteLock().writeLock().unlock();
      // System.out.println("\n" + status.getSetup() + "\n");
      //      for (ToolEntry te : tools) {
      //         System.out.println("\ngot tool: " + te);
      //      }
   }


   private void checkBuffer() {
      if (statusBuffer.hasArray())
         System.out.println("buffer has array");
      else
         System.out.println("buffer has NO array");

      if (statusBuffer.isDirect())
         System.out.println("buffer is direct");
      else
         System.out.println("buffer is NOT direct");

      if (statusBuffer.order() == ByteOrder.BIG_ENDIAN)
         System.out.println("buffer has BIG ENDIAN byte order");
      else
         System.out.println("buffer has LITTLE ENDIAN byte order");
   }


   private native final ByteBuffer init();

   //   [    20] echo_serial_number
   //   [    20] echo_serial_number
   //   [    24] state
   //   [   212] task_mode
   //   [   216] task_state
   //   [   220] exec_state
   //   [   224] interp_state
   //   [   228] call_level
   //   [   240] read_line
   //   [   232] motion_line
   //   [   236] current_line
   //   [   247] file
   //   [   502] command
   //   [  1144] program_units
   //   [  1148] interpreter_errcode
   //   [   244] optional_stop
   //   [   245] block_delete
   //   [  1152] task_paused
   //   [   246] input_timeout
   //   [   912] rotation_xy
   //   [  1160] delay_left
   //   [   992] active_gcodes
   //   [  1060] active_mcodes
   //   [  1104] active_settings
   //   [  1168] queued_mdi_commands
   //   [  1392] linear_units
   //   [  1400] angular_units
   //   [  1416] joints
   //   [  6592] spindle[0]
   //   [  1428] axis_mask
   //   [  1432] motion_mode
   //   [  1436] enabled
   //   [  1437] inpos
   //   [  1440] queue
   //   [  1444] active_queue
   //   [  1448] queue_full
   //   [  1452] id
   //   [  1408] cycle_time
   //   [  1480] position
   //   [  1552] actual_pos
   //   [  1752] dtg
   //   [   760] g5x_offset
   //   [   840] g92_offset
   //   [   920] tool_offset
   //   [  2116] joint_x_enabled
   //   [  2332] joint_y_enabled
   //   [  2548] joint_z_enabled
   //   [  2764] joint_a_enabled
   //   [  2980] joint_b_enabled
   //   [  3196] joint_c_enabled
   //   [  3412] joint_u_enabled
   //   [  3628] joint_v_enabled
   //   [  3844] joint_w_enabled
   //   [  2114] joint_x_homed
   //   [  2330] joint_y_homed
   //   [  2546] joint_z_homed
   //   [  2762] joint_a_homed
   //   [  2978] joint_b_homed
   //   [  3194] joint_c_homed
   //   [  3410] joint_u_homed
   //   [  3626] joint_v_homed
   //   [  3842] joint_w_homed
   //   [  1456] paused
   //   [  1464] feedrate
   //   [  1472] rapidrate
   //   [  1624] velocityT
   //   [  5496] velocityA
   //   [  2104] velocityJ
   //   [  1632] acceleration
   //   [  1640] max_velocity
   //   [  1648] max_acceleration
   //   [  1728] probe_tripped
   //   [  1729] probing
   //   [  1732] probe_val
   //   [  1736] kinematics_type
   //   [  1740] motion_type
   //   [  1744] distance_to_go
   //   [  1824] current_vel
   //   [  1832] feed_override_enabled
   //   [  1833] adaptive_feed_enabled
   //   [  1834] feed_hold_enabled
   //   [  6696] spindle[0]speed
   //   [  6704] spindle[0]scale
   //   [  6712] spindle[0]css_max
   //   [  6720] spindle[0]css_fak
   //   [  6728] spindle[0]dir
   //   [  6732] spindle[0]brake
   //   [  6736] spindle[0]increasing
   //   [  6740] spindle[0]enabled
   //   [  6744] spindle[0]orient_state
   //   [  6748] spindle[0]orient_fault
   //   [  6752] spindle[0]override_enable
   //   [  6753] spindle[0]homed
   //   [  9808] pocket_prepped
   //   [  9812] tool_in_spindle
   //   [  9816] tool_table
   //   [122032] mist
   //   [122036] flood
   //   [122144] estop
   //   [122256] lube
   //   [122260] lube_level
   //   [122264] debug


   // [ 992] active_gcodes
   // [ 1056] active_mcodes
   private void printActiveCodes() {
      int gcBase = 992;
      int mcBase = 1056;

      for (int i = 0; i < 16; ++i) {
         System.out.println("active G-Code: " + statusBuffer.getInt(gcBase + i * 4));
      }
      for (int i = 0; i < 10; ++i) {
         System.out.println("active M-Code: " + statusBuffer.getInt(mcBase + i * 4));
      }
   }


   private ByteBuffer              statusBuffer;
   private LCStatus                status;
   private ErrorReader             errorReader;
   private boolean                 initializationCompleted;
   private final IBufferDescriptor bufDesc;
   private final int               activeSpindle;
   private static final String     NmlConfigFile = "/usr/local/src/linuxcnc-dev/configs/common/linuxcnc.nml";
   private static final String[]   listOfSignals = { "joint_0_enabled", "joint_1_enabled", "joint_2_enabled",
         "joint_3_enabled", "joint_4_enabled", "joint_5_enabled", "joint_6_enabled", "joint_7_enabled",
         "joint_8_enabled", "joint_0_homed", "joint_1_homed", "joint_2_homed", "joint_3_homed",
         "joint_4_homed", "joint_5_homed", "joint_6_homed", "joint_7_homed", "joint_8_homed",
         "feed_override_enabled", "adaptive_feed_enabled", "feed_hold_enabled", "coolMist", "coolFlood",
         "lube", "estop", "debug" };
}
