package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       CommandWriter.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.10.2019 by Django Reinhard
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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Timer;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.InterpState;
import de.schwarzrot.nml.SpindleDirection;
import de.schwarzrot.nml.TaskAutoMode;
import de.schwarzrot.nml.TaskMode;
import de.schwarzrot.nml.TaskState;


public class CommandWriter {
   class JogInfo {
      public int axis;
      public int direction;
   }


   public CommandWriter(List<SystemMessage> log, StatusReader statusReader) {
      this.log          = log;
      this.statusReader = statusReader;
      init();
   }


   public void clearEStop() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdClearEStop")));
      setTaskState(TaskState.EStopReset.getStateNum());
   }


   public void enableFlood(boolean enable) {
      StringBuilder sb = new StringBuilder(LCStatus.getStatus().lm("cmdFloodEnable"));

      sb.append(" ");
      if (enable)
         sb.append(LCStatus.getStatus().lm("enable"));
      else
         sb.append(LCStatus.getStatus().lm("disable"));
      log.add(new SystemMessage(sb.toString()));
      setFlood(enable);
   }


   public void enableMist(boolean enable) {
      StringBuilder sb = new StringBuilder(LCStatus.getStatus().lm("cmdMistEnable"));

      sb.append(" ");
      if (enable)
         sb.append(LCStatus.getStatus().lm("enable"));
      else
         sb.append(LCStatus.getStatus().lm("disable"));
      log.add(new SystemMessage(sb.toString()));
      setMist(enable);
   }


   public void enableOptionalStop() {
      log.add(new SystemMessage("cmdOptonalStop"));
      setOptionalStop(true);
   }


   public void enableOptionalStop(boolean enable) {
      if (enable)
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdOptionalStopEnable")));
      else
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdOptionalStopDisable")));
      setOptionalStop(enable);
   }


   public void enableSpindle(boolean enable, int speed, SpindleDirection direction) {
      System.out
            .println("COMMAND: enable spindle with parameters enable == " + (enable ? "enable" : "disable")
                  + " - speed == " + speed + " - direction == " + direction.getDirection());
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdSpindle"),
            LCStatus.getStatus().lm(enable ? "enable" : "disable"), speed, direction.getDirection())));
      setSpindle(enable, speed, direction.getDirection());
   }


   public void execGCode() {
      if (((Boolean) LCStatus.getStatus().getModel("singleStep").getValue())) {
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecSingleStep")));
         setAuto(TaskAutoMode.AutoStep);
      } else {
         if (LCStatus.getStatus().getModel("interpState").getValue()
               .equals(InterpState.Paused.getStateNum())) {
            resumeGCodeExecution();
         } else {
            log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecAutoRun")));
            setAuto(TaskAutoMode.AutoRun);
         }
      }
   }


   public void execMDI(String command) {
      if (command.length() > 254) {
         log.add(new SystemMessage(SystemMessage.MessageType.OperatorError,
               LCStatus.getStatus().lm("errMDI2Long")));
         throw new IllegalArgumentException("command length must not exceed 255 characters!");
      }
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdExecMDI"), command)));
      sendMDICommand(command);
   }


   public void homeAll() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdHomeAll")));
      ValueModel<TaskMode> tm = LCStatus.getStatus().getModel("taskMode");
      System.err.println("execute home all axis - task mode is: " + tm.getValue());
      homeAxis(-1);
   }


   public void jogStep(String what, double stepSize, double speed) {
      System.out.println("jogStep: " + what + " - stepSize: " + stepSize + " - speed: " + speed);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      jogStep(ji.axis, stepSize * ji.direction, speed);
   }


   public void loadGCodeFile(File gcodeFile) {
      if (gcodeFile.exists() && gcodeFile.canRead()) {
         log.add(new SystemMessage(
               String.format(LCStatus.getStatus().lm("cmdLoadGCodeFile"), gcodeFile.getAbsolutePath())));
         loadTaskPlan(gcodeFile.getAbsolutePath());
      }
   }


   public void loadToolsDefinition(File toolsFile) {
      if (toolsFile != null && toolsFile.exists() && toolsFile.canRead()) {
         log.add(new SystemMessage(
               String.format(LCStatus.getStatus().lm("cmdLoadToolTable"), toolsFile.getAbsolutePath())));
         loadToolTable(toolsFile.getAbsolutePath());
      } else {
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdReloadToolTable")));
         loadToolTable(null);
      }
      ActionListener al = new ActionListener() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              statusReader.readToolsDefinitions();
                           }
                        };
      // TODO: may be we have to adjust delay
      Timer          tm = new Timer(1000, al);

      tm.setRepeats(false);
      tm.start();
   }


   public void machinePowerOff() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdPowerOff")));
      setTaskState(TaskState.MachineOff.getStateNum());
   }


   public void machinePowerON() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdPowerOn")));
      setTaskState(TaskState.MachineOn.getStateNum());
   }


   public void pauseGCodeExecution() {
      setAuto(TaskAutoMode.AutoPause);
   }


   public void resumeGCodeExecution() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecAutoResume")));
      setAuto(TaskAutoMode.AutoResume);
   }


   public void runFromLine(int line) {
      LCStatus status = LCStatus.getStatus();

      if (((Boolean) status.getModel("singleStep").getValue())) {
         log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdStepFromLine"), line)));
         // NOTE: as linuxcnc does not support singlestep from given line,
         // we need a sequence like this:
         // 1. remember feed factor and fast feed factor
         // 2. set feed factor and fast feed factor to 0
         // 3. execute "run from line"
         // 4. issue "pause"
         // 5. restore feed factor and fast feed factor
         // 6. execute "step"
         double feedFactor  = status.getSpeedInfo().getFeedFactor();
         double rapidFactor = status.getSpeedInfo().getRapidFactor();

         setFeedOverride(0);
         setRapidOverride(0);
         setAuto(TaskAutoMode.AutoRun.ordinal(), line);
         setAuto(TaskAutoMode.AutoPause);
         setFeedOverride(feedFactor);
         setRapidOverride(rapidFactor);
         setAuto(TaskAutoMode.AutoStep);
      } else {
         log.add(new SystemMessage(String.format("cmdRunFromLine", line)));
         setAuto(TaskAutoMode.AutoRun.ordinal(), line);
      }
   }


   public void setFeedRate(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdFeedRateOverride"), rate)));
      setFeedOverride(rate);
   }


   /*
    * this command will be issued from panel in manual editing mode,
    * so we have to fake mdi-mode and switch back again
    */
   public void setFixture(String command) {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdSetFixture")));
      sendHiddenMDI(command);
   }


   public void setRapidRate(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdRapidRateOverride"), rate)));
      setRapidOverride(rate);
   }


   public void setSpindleSpeedFactor(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdSpindleSpeedOverride"), rate)));
      setSpindleOverride(rate);
   }


   public void setTaskAbort() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdAbortTask")));
      taskAbort();
   }


   public void setTaskModeAuto() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeAuto")));
      setTaskMode(TaskMode.TaskModeAuto.getMode());
   }


   public void setTaskModeManual() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeManual")));
      setTaskMode(TaskMode.TaskModeManual.getMode());
   }


   public void setTaskModeMDI() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeMDI")));
      setTaskMode(TaskMode.TaskModeMDI.getMode());
   }


   public void setToolProperties(String toolChangeCmd) {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdSetToolProperties")));
      sendHiddenMDI(toolChangeCmd);
   }


   public void skipCComment() {
      skipCComment(true);
   }


   public void skipCComment(boolean enable) {
      setBlockDelete(enable);
   }


   public void startJogging(String what, double speed) {
      System.out.println("jogStart: " + what + " - speed: " + speed);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      jogStart(ji.axis, ji.direction * speed);
   }


   public void startSpindleCCW(int speed) {
      this.setSpindle(true, speed, -1);
   }


   public void startSpindleCW(int speed) {
      this.setSpindle(true, speed, 1);
   }


   public void stopJogging(String what) {
      System.out.println("jogStop: " + what);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      jogStop(ji.axis);
   }


   public void stopSpindle() {
      this.setSpindle(false, 0, 0);
   }


   protected void determineJogInfo(JogInfo ji, String what) {
      switch (what.charAt(0)) {
         case 'X':
         case 'x':
            ji.axis = 0;
            break;
         case 'Y':
         case 'y':
            ji.axis = 1;
            break;
         case 'Z':
         case 'z':
            ji.axis = 2;
            break;
         case 'A':
         case 'a':
            ji.axis = 3;
            break;
         case 'B':
         case 'b':
            ji.axis = 4;
            break;
         case 'C':
         case 'c':
            ji.axis = 5;
            break;
         case 'U':
         case 'u':
            ji.axis = 6;
            break;
         case 'V':
         case 'v':
            ji.axis = 7;
            break;
         case 'W':
         case 'w':
            ji.axis = 8;
            break;
      }
      if (what.charAt(1) == '-')
         ji.direction = -1;
      else
         ji.direction = 1;
   }


   protected native final void homeAxis(int jointNum);


   protected native final int init();


   protected native final void jogStart(int axis, double speed);


   protected native final void jogStep(int axis, double stepSize, double speed);


   protected native final void jogStop(int axis);


   protected native final void loadTaskPlan(String fileName);


   protected native final void loadToolTable(String fileName);


   protected void sendHiddenMDI(String command) {
      ValueModel<ApplicationMode> v  = LCStatus.getStatus().getModel("applicationMode");
      ApplicationMode             am = v.getValue();

      setTaskModeMDI();
      execMDI(command);
      setTaskModeManual();
      v.setValue(am);
   }


   protected native final void sendMDICommand(String command);


   protected native final void setAuto(int autoMode, int fromLine);


   protected final void setAuto(TaskAutoMode mode) {
      setAuto(mode.ordinal(), 0);
   }


   protected native final void setBlockDelete(boolean enable);


   protected native final void setFeedOverride(double rate);


   protected native final void setFlood(boolean enable);


   protected native final void setMist(boolean enable);


   protected native final void setOptionalStop(boolean stop);


   protected native final void setRapidOverride(double rate);


   protected native final void setSpindle(boolean enable, int speed, int direction);


   protected native final void setSpindleOverride(double rate);


   protected native final void setTaskMode(int mode);


   protected native final void setTaskState(int state);


   protected native final void taskAbort();


   private final List<SystemMessage> log;
   private final StatusReader        statusReader;
   private static final String       OffsetMASK  = "G92 X%.3f Y%.3f Z%.3f A%.3f B%.3f C%.3f U%.3f V%.3f W%.3f";
   private static final String       FixtureMASK = "G10 L2 P%d X%.3f Y%.3f Z%.3f A%.3f B%.3f C%.3f U%.3f V%.3f W%.3f";
}
