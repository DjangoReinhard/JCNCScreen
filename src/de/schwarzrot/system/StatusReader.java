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


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.Position;
import de.schwarzrot.bean.ToolEntry;
import de.schwarzrot.model.ActiveCodes;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.SpeedInfo;
import de.schwarzrot.model.ToolInfo;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.BufferDescriptor;
import de.schwarzrot.nml.BufferEntry;
import de.schwarzrot.nml.IBufferDescriptor;

import ca.odell.glazedlists.EventList;


public class StatusReader {
   public StatusReader(ErrorReader errorReader, IBufferDescriptor bufDesc) {
      this.initializationCompleted = false;
      this.errorReader             = errorReader;
      this.bufDesc                 = bufDesc;
      this.activeSpindle           = 0;
      this.oldMotionLine           = -1;
      this.p                       = new Position();
      this.g5xO                    = new Position();
      this.tO                      = new Position();
      this.g92O                    = new Position();
      //      this.si                      = status.getSpeedInfo();
      //      this.models                  = status.getValueModels();
      try {
         socket       = new DatagramSocket(6421);
         address      = InetAddress.getByName("localhost");
         sendBuf      = new byte[1280];
         sendBuffer   = ByteBuffer.wrap(sendBuf);
         statusBuffer = init();
         statusBuffer.order(ByteOrder.LITTLE_ENDIAN);
         status = LCStatus.getStatus();
         //      checkBuffer();
         readSetup();
         update(); // at least once before timer start
         this.initializationCompleted = true;
      } catch (SocketException | UnknownHostException e) {
         e.printStackTrace();
      }
   }


   public ByteBuffer getStatusBuffer() {
      return statusBuffer;
   }


   public boolean isInitializationCompleted() {
      return initializationCompleted;
   }


   public void update() {
      //      long          start = System.currentTimeMillis();
      SystemMessage sm = errorReader.fetchMessage();

      if (sm != null) {
         l.log(Level.SEVERE, "got Error-Message: " + sm.getMessage());

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
      //      broadCastStatus();
      //      long end = System.currentTimeMillis();
      //
      //      l.log(Level.INFO, "update took " + (end - start) + "ms");
      //
      //      System.out.println();
      //      System.out.println(LCStatus.getStatus().getModel("taskState"));
      //      System.out.println(LCStatus.getStatus().getModel("taskMode"));
      //      System.out.println(LCStatus.getStatus().getModel("execState"));
      //      System.out.println(LCStatus.getStatus().getModel("interpState"));
      //      System.out.println(LCStatus.getStatus().getModel("applicationMode"));
   }


   protected void broadCastStatus() {
      if (socket == null)
         return;

      new Thread(new Runnable() {
         @Override
         public void run() {
            updateSendBuf();
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, 6421);

            try {
               socket.send(packet);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }).start();
   }


   protected native String getString(int offset, int length);


   protected void handleActiveCodes() {
      e = bufDesc.get(BufferDescriptor.ActiveGCodes);
      ActiveCodes ac = status.getActiveCodes();

      for (int i = 0; i < 16; ++i) {
         iv = statusBuffer.getInt(e.offset + 4 * i);

         switch (i) {
            case 0:
               ac.setGcode0(iv);
               break;
            case 1:
               ac.setGcode1(iv);
               break;
            case 2:
               ac.setGcode2(iv);
               break;
            case 3:
               ac.setGcode3(iv);
               break;
            case 4:
               ac.setGcode4(iv);
               break;
            case 5:
               ac.setGcode5(iv);
               break;
            case 6:
               ac.setGcode6(iv);
               break;
            case 7:
               ac.setGcode7(iv);
               break;
            case 8:
               ac.setGcode8(iv);
               break;
            case 9:
               ac.setGcode9(iv);
               break;
            case 10:
               ac.setGcode10(iv);
               break;
            case 11:
               ac.setGcode11(iv);
               break;
            case 12:
               ac.setGcode12(iv);
               break;
            case 13:
               ac.setGcode13(iv);
               break;
            case 14:
               ac.setGcode14(iv);
               break;
            case 15:
               ac.setGcode15(iv);
               break;
         }
      }

      e = bufDesc.get(BufferDescriptor.ActiveMCodes);
      for (int i = 0; i < 10; ++i) {
         iv = statusBuffer.getInt(e.offset + 4 * i);

         switch (i) {
            case 0:
               ac.setMcode0(iv);
               break;
            case 1:
               ac.setMcode1(iv);
               break;
            case 2:
               ac.setMcode2(iv);
               break;
            case 3:
               ac.setMcode3(iv);
               break;
            case 4:
               ac.setMcode4(iv);
               break;
            case 5:
               ac.setMcode5(iv);
               break;
            case 6:
               ac.setMcode6(iv);
               break;
            case 7:
               ac.setMcode7(iv);
               break;
            case 8:
               ac.setMcode8(iv);
               break;
            case 9:
               ac.setMcode9(iv);
               break;
         }
      }
      e = bufDesc.get(BufferDescriptor.ActiveSettings);
      status.getSpeedInfo().setNominalFeed(statusBuffer.getDouble(e.offset + 8));
      status.getSpeedInfo().setSpindleNominalSpeed(statusBuffer.getDouble(e.offset + 16));
   }


   protected void handleGCodeFile() {
      e = bufDesc.get(BufferDescriptor.File);
      status.getGCodeInfo().setFileName(getString(e.offset, e.size));

      e = bufDesc.get(BufferDescriptor.MotionLine);
      //      int line       = status.getGCodeInfo().getFrontendLine();
      int remoteLine = statusBuffer.getInt(e.offset);

      if (remoteLine != oldMotionLine) {
         //         l.log(Level.INFO, "backend executes line #" + remoteLine);
         status.getGCodeInfo().setBackendLine(remoteLine);

         //TODO: check setting of frontend line!
         //      if (remoteLine > line)
         status.getGCodeInfo().setFrontendLine(remoteLine);

         oldMotionLine = remoteLine;
      }
   }


   protected void handlePosition() {
      e      = bufDesc.get(BufferDescriptor.AbsPosX);

      p.x    = statusBuffer.getDouble(e.offset);
      p.y    = statusBuffer.getDouble(e.offset + 8);
      p.z    = statusBuffer.getDouble(e.offset + 16);
      p.a    = statusBuffer.getDouble(e.offset + 24);
      p.b    = statusBuffer.getDouble(e.offset + 32);
      p.c    = statusBuffer.getDouble(e.offset + 40);
      p.u    = statusBuffer.getDouble(e.offset + 48);
      p.v    = statusBuffer.getDouble(e.offset + 56);
      p.w    = statusBuffer.getDouble(e.offset + 64);

      //      l.log(Level.INFO, "pos: " + p);

      e      = bufDesc.get(BufferDescriptor.G5xOffsX);
      g5xO.x = statusBuffer.getDouble(e.offset);
      g5xO.y = statusBuffer.getDouble(e.offset + 8);
      g5xO.z = statusBuffer.getDouble(e.offset + 16);
      g5xO.a = statusBuffer.getDouble(e.offset + 24);
      g5xO.b = statusBuffer.getDouble(e.offset + 32);
      g5xO.c = statusBuffer.getDouble(e.offset + 40);
      g5xO.u = statusBuffer.getDouble(e.offset + 48);
      g5xO.v = statusBuffer.getDouble(e.offset + 56);
      g5xO.w = statusBuffer.getDouble(e.offset + 64);
      // System.out.println("g5x Offset: " + g5xO);

      e      = bufDesc.get(BufferDescriptor.ToolOffsX);
      tO.x   = statusBuffer.getDouble(e.offset);
      tO.y   = statusBuffer.getDouble(e.offset + 8);
      tO.z   = statusBuffer.getDouble(e.offset + 16);
      tO.a   = statusBuffer.getDouble(e.offset + 24);
      tO.b   = statusBuffer.getDouble(e.offset + 32);
      tO.c   = statusBuffer.getDouble(e.offset + 40);
      tO.u   = statusBuffer.getDouble(e.offset + 48);
      tO.v   = statusBuffer.getDouble(e.offset + 56);
      tO.w   = statusBuffer.getDouble(e.offset + 64);
      //      System.out.println("toolOffset: " + tO);

      e      = bufDesc.get(BufferDescriptor.G92OffsX);
      g92O.x = statusBuffer.getDouble(e.offset);
      g92O.y = statusBuffer.getDouble(e.offset + 8);
      g92O.z = statusBuffer.getDouble(e.offset + 16);
      g92O.a = statusBuffer.getDouble(e.offset + 24);
      g92O.b = statusBuffer.getDouble(e.offset + 32);
      g92O.c = statusBuffer.getDouble(e.offset + 40);
      g92O.u = statusBuffer.getDouble(e.offset + 48);
      g92O.v = statusBuffer.getDouble(e.offset + 56);
      g92O.w = statusBuffer.getDouble(e.offset + 64);
      // System.out.println("g92 Offset: " + g92O);

      e      = bufDesc.get(BufferDescriptor.RotationXY);
      dv     = statusBuffer.getDouble(e.offset);

      status.getPositionCalculator().update(p, g5xO, tO, g92O, dv);

      e = bufDesc.get(BufferDescriptor.DtgX);
      status.getDistanceToGo().setX(statusBuffer.getDouble(e.offset));
      status.getDistanceToGo().setY(statusBuffer.getDouble(e.offset + 8));
      status.getDistanceToGo().setZ(statusBuffer.getDouble(e.offset + 16));
      status.getDistanceToGo().setA(statusBuffer.getDouble(e.offset + 24));
      status.getDistanceToGo().setB(statusBuffer.getDouble(e.offset + 32));
      status.getDistanceToGo().setC(statusBuffer.getDouble(e.offset + 40));
      status.getDistanceToGo().setU(statusBuffer.getDouble(e.offset + 48));
      status.getDistanceToGo().setV(statusBuffer.getDouble(e.offset + 56));
      status.getDistanceToGo().setW(statusBuffer.getDouble(e.offset + 64));

      //      // System.out.println("dtg: " + distanceToGo);
      //      e = bufDesc.get(BufferDescriptor.Distance2Go);
      //      status.getModel(LCStatus.MN_DTG).setValue(statusBuffer.getDouble(e.offset));
   }


   @SuppressWarnings({ "incomplete-switch", "unchecked" })
   protected void handleSignals() {
      if (models == null)
         models = status.getValueModels();
      e = null;
      m = null;

      for (String n : listOfSignals) {
         e = bufDesc.get(n);
         m = models.get(n);

         if (m == null) {
            //            l.log(Level.SEVERE, "use of uninitialized model >" + n + "<");
            m = new ValueModel<Boolean>(n, false);
            models.put(n, m);
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
      }
   }


   // [ 1440] feedrate
   // [ 1448] rapidrate
   // [ 1600] velocity
   // [ 1608] acceleration
   // [ 1616] max_velocity
   // [ 1624] max_acceleration
   protected void handleSpeed() {
      if (si == null)
         si = status.getSpeedInfo();
      e  = bufDesc.get(BufferDescriptor.Feedrate);
      dv = statusBuffer.getDouble(e.offset);

      si.setFeedFactor(dv * 100.0);

      e  = bufDesc.get(BufferDescriptor.Rapidrate);
      dv = statusBuffer.getDouble(e.offset);
      si.setRapidFactor(dv * 100.0);

      e  = bufDesc.get(BufferDescriptor.Acceleration);
      dv = statusBuffer.getDouble(e.offset);
      si.setAccel(dv);

      e  = bufDesc.get(BufferDescriptor.Max_velocity);
      dv = statusBuffer.getDouble(e.offset);
      // speed is in unit/s
      si.setMaxSpeed(dv * 60);

      e  = bufDesc.get(BufferDescriptor.Current_vel);
      dv = statusBuffer.getDouble(e.offset);
      // speed is in unit/s
      si.setCurFeed(dv * 60);

      e  = bufDesc.get(BufferDescriptor.Max_acceleration);
      dv = statusBuffer.getDouble(e.offset);
      si.setMaxAccel(dv);

      handleSpindle();
   }


   @SuppressWarnings("unchecked")
   protected int handleSpindle() {
      e = bufDesc.get(BufferDescriptor.SpindleSpeed);
      si.setSpindleCurSpeed(statusBuffer.getDouble(e.offset + activeSpindle * 168));

      e = bufDesc.get(BufferDescriptor.SpindleScale);
      si.setSpindleFactor(statusBuffer.getDouble(e.offset + activeSpindle * 168) * 100.0);

      e = bufDesc.get(BufferDescriptor.SpindleDir);
      status.getModel(BufferDescriptor.SpindleDir)
            .setValue(statusBuffer.getInt(e.offset + activeSpindle * 168));

      return 1;
   }


   protected void handleStates() {
      BufferEntry e = bufDesc.get(BufferDescriptor.TaskMode);

      status.setTaskMode(statusBuffer.getInt(e.offset));
      e = bufDesc.get(BufferDescriptor.TaskState);
      status.setTaskState(statusBuffer.getInt(e.offset));
      e = bufDesc.get(BufferDescriptor.ExecState);
      status.setExecState(statusBuffer.getInt(e.offset));
      e = bufDesc.get(BufferDescriptor.InterpState);
      status.setInterpState(statusBuffer.getInt(e.offset));

      //      status.dump();
      //      System.out.println(status.getModel("taskState"));
      //      System.out.println(status.getModel("taskMode"));
      //      System.out.println(status.getModel("execState"));
      //      System.out.println(status.getModel("interpState"));
      //      System.out.println();
   }


   protected void handleToolChange() {
      e  = bufDesc.get(BufferDescriptor.ToolInSpindle);
      iv = statusBuffer.getInt(e.offset);
      ToolInfo toolInfo = status.getToolInfo();

      if (iv == 0)
         iv = 99;
      toolInfo.setActiveToolNum(iv);
      e = bufDesc.get(BufferDescriptor.PocketPrepared);
      toolInfo.setNextToolNum(statusBuffer.getInt(e.offset));

      e = bufDesc.get(BufferDescriptor.ToolOffsX);
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
      e = bufDesc.get(BufferDescriptor.AxisMask);
      int axisMask = statusBuffer.getInt(e.offset);

      e = bufDesc.get(BufferDescriptor.Joints);
      int numJoints = statusBuffer.getInt(e.offset);

      //      System.out.println("SETUP: axis mask is " + String.format("0x%X", axisMask));
      status.getSetup().setAxisMask(axisMask);
      status.getSetup().setNumJoints(numJoints);

      e = bufDesc.get(BufferDescriptor.ProgramUnits);
      status.getSetup().setUnit(statusBuffer.getInt(e.offset));

      e = bufDesc.get(BufferDescriptor.Spindles);
      int numSpindles = statusBuffer.getInt(e.offset);

      status.getSetup().setNumSpindles(numSpindles);

      //      readToolsDefinitions();
      //      handleToolChange();
      handleSignals();
   }


   protected native final void readStatus();


   protected void readToolsDefinitions() {
      EventList<ToolEntry> tools   = status.getSetup().getTools();
      ToolEntry            tool    = null;
      int                  base    = 0;
      int                  toolNum = 0;

      e = bufDesc.get(BufferDescriptor.ToolTable);
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


   protected void updateSendBuf() {
      CanonPosition pos = status.getPositionCalculator().getPosition();
      int           i   = 0;

      sendBuffer.putDouble(0, pos.getX());
      sendBuffer.putDouble(++i, pos.getY());
      sendBuffer.putDouble(++i, pos.getZ());
      sendBuffer.putDouble(++i, pos.getA());
      sendBuffer.putDouble(++i, pos.getB());
      sendBuffer.putDouble(++i, pos.getC());
      sendBuffer.putDouble(++i, pos.getU());
      sendBuffer.putDouble(++i, pos.getV());
      sendBuffer.putDouble(++i, pos.getW());
      pos = status.getDistanceToGo();
      sendBuffer.putDouble(++i, pos.getX());
      sendBuffer.putDouble(++i, pos.getY());
      sendBuffer.putDouble(++i, pos.getZ());
      sendBuffer.putDouble(++i, pos.getA());
      sendBuffer.putDouble(++i, pos.getB());
      sendBuffer.putDouble(++i, pos.getC());
      sendBuffer.putDouble(++i, pos.getU());
      sendBuffer.putDouble(++i, pos.getV());
      sendBuffer.putDouble(++i, pos.getW());
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
   private DatagramSocket          socket;
   private byte[]                  sendBuf;
   private ByteBuffer              sendBuffer;
   private InetAddress             address;
   private BufferEntry             e;
   private Position                p;
   private Position                g5xO;
   private Position                tO;
   private Position                g92O;
   private double                  dv;
   private int                     iv;
   private int                     oldMotionLine;
   private ValueModel<Boolean>     m;
   private Map<String, ValueModel> models;
   private SpeedInfo               si;
   private static Logger           l;
   private static final String     NmlConfigFile = "/usr/local/src/linuxcnc-dev/configs/common/linuxcnc.nml";
   private static final String[]   listOfSignals = { "joint_0_enabled", "joint_1_enabled", "joint_2_enabled",
         "joint_3_enabled", "joint_4_enabled", "joint_5_enabled", "joint_6_enabled", "joint_7_enabled",
         "joint_8_enabled", "joint_0_homed", "joint_1_homed", "joint_2_homed", "joint_3_homed",
         "joint_4_homed", "joint_5_homed", "joint_6_homed", "joint_7_homed", "joint_8_homed",
         "feed_override_enabled", "adaptive_feed_enabled", "feed_hold_enabled", "coolMist", "coolFlood",
         "lube", "estop", "debug" };
   static {
      l = Logger.getLogger(StatusReader.class.getName());
   }
}
