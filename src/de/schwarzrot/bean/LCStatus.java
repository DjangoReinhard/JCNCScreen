package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       LCStatus.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    21.9.2019 by Django Reinhard
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.app.LinuxCNCClient;
import de.schwarzrot.model.ActiveCodes;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.GCodeInfo;
import de.schwarzrot.model.SpeedInfo;
import de.schwarzrot.model.ToolInfo;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.BufferDescriptor;
import de.schwarzrot.nml.TaskMode;
import de.schwarzrot.nml.TaskState;
import de.schwarzrot.util.PositionCalculator;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;


public class LCStatus {
   @SuppressWarnings("rawtypes")
   private LCStatus() {
      try {
         messages = ResourceBundle.getBundle(LinuxCNCClient.class.getSimpleName());
      } catch (Throwable t) {
         messages = ResourceBundle.getBundle(LinuxCNCClient.class.getSimpleName(), new Locale("en", "EN"));
      }
      positionCalculator = new PositionCalculator();
      distanceToGo       = new CanonPosition();
      gcodeInfo          = new GCodeInfo();
      models             = new HashMap<String, ValueModel>();
      activeCodes        = new ActiveCodes();
      setup              = new AppSetup(this, true); // true: read tooltable from file, false: read tooltable from nml-status
      speedInfo          = new SpeedInfo(setup.getUnit());
      toolInfo           = new ToolInfo(setup.getTools());
      mdiHistory         = new BasicEventList<GCodeLine>();
      setupModels();
   }


   public void checkLocale() {
      System.out.println("local check: AppTitle == > " + messages.getString("AppTitle") + " <");
   }


   public void dump() {
      System.out.println("setup ............: " + setup);
      System.out.println("positionCalculator: " + positionCalculator);
      System.out.println("distanceToGo .....: " + distanceToGo);
      System.out.println("gcodeInfo ........: " + gcodeInfo);
      System.out.println("activeCodes ......: " + activeCodes);
      System.out.println("speedInfo ........: " + speedInfo);
      System.out.println("tool-table follows:");

      List<ToolEntry> tools = setup.getTools();

      for (ToolEntry te : tools) {
         System.out.println(te);
      }
      System.out.println();

      for (String sk : models.keySet()) {
         System.out.println(models.get(sk));
      }
      System.out.println();
      System.out.println(models.get("taskState"));
      System.out.println(models.get("taskMode"));
      System.out.println(models.get("execState"));
      System.out.println(models.get("interpState"));
      System.out.println();
   }


   public ActiveCodes getActiveCodes() {
      return activeCodes;
   }


   public LinuxCNCClient getApp() {
      return app;
   }


   public CanonPosition getDistanceToGo() {
      return distanceToGo;
   }


   public GCodeInfo getGCodeInfo() {
      return gcodeInfo;
   }


   public EventList<GCodeLine> getMDIHistory() {
      return mdiHistory;
   }


   @SuppressWarnings("rawtypes")
   public ValueModel getModel(String name) {
      return models.get(name);
   }


   public PositionCalculator getPositionCalculator() {
      return positionCalculator;
   }


   public AppSetup getSetup() {
      return this.setup;
   }


   public SpeedInfo getSpeedInfo() {
      return speedInfo;
   }


   public ToolInfo getToolInfo() {
      return toolInfo;
   }


   public ToolLibrary getToolLibrary() {
      return toolLibrary;
   }


   @SuppressWarnings("rawtypes")
   public Map<String, ValueModel> getValueModels() {
      return models;
   }


   public String lm(String key) {
      if (messages == null)
         return key;
      String localized = null;

      try {
         localized = messages.getString(key);
      } catch (MissingResourceException e) {
      }
      if (localized == null)
         return key;
      return localized;
   }


   public void setApp(LinuxCNCClient app) {
      this.app = app;
   }


   @SuppressWarnings("unchecked")
   public void setExecState(int execState) {
      models.get(MN_ExecState).setValue(execState);
   }


   @SuppressWarnings("unchecked")
   public void setInterpState(int state) {
      models.get(MN_InterpState).setValue(state);
   }


   @SuppressWarnings("unchecked")
   public void setTaskMode(int taskMode) {
      switch (taskMode) {
         case 1:
            models.get(MN_TaskMode).setValue(TaskMode.TaskModeManual);
            break;
         case 2:
            models.get(MN_TaskMode).setValue(TaskMode.TaskModeAuto);
            break;
         case 3:
            models.get(MN_TaskMode).setValue(TaskMode.TaskModeMDI);
            break;
         default:
            throw new IllegalArgumentException("unknown/invalid task mode #" + taskMode);
      }
   }


   @SuppressWarnings("unchecked")
   public void setTaskState(int taskState) {
      switch (taskState) {
         case 1:
            models.get(MN_TaskState).setValue(TaskState.EStop);
            break;
         case 2:
            models.get(MN_TaskState).setValue(TaskState.EStopReset);
            break;
         case 3:
            models.get(MN_TaskState).setValue(TaskState.MachineOff);
            break;
         case 4:
            models.get(MN_TaskState).setValue(TaskState.MachineOn);
            break;
         default:
            throw new IllegalArgumentException("unknown/invalid task state");
      }
   }


   public void setToolLibrary(ToolLibrary toolLibrary) {
      this.toolLibrary = toolLibrary;
   }


   protected void setupModels() {
      models.put(MN_TaskState, new ValueModel<TaskState>(MN_TaskState, TaskState.EStop));
      models.put(MN_TaskMode, new ValueModel<TaskMode>(MN_TaskMode, TaskMode.TaskModeManual));
      models.put(MN_ExecState, new ValueModel<Integer>(MN_ExecState, -1));
      models.put(MN_InterpState, new ValueModel<Integer>(MN_InterpState, -1));
      models.put(MN_ErrorActive, new ValueModel<Boolean>(MN_ErrorActive, false));
      models.put(MN_ApplicationMode,
            new ValueModel<ApplicationMode>(MN_ApplicationMode, ApplicationMode.AmMachineOff));
      models.put(MN_SpindleDir, new ValueModel<Integer>(MN_SpindleDir, 0));
      models.put(MN_SingleStep, new ValueModel<Boolean>(MN_SingleStep, false));
      models.put(MN_AbsPosition, new ValueModel<Boolean>(MN_AbsPosition, false));
      models.put(MN_DTG, new ValueModel<Double>(MN_DTG, 0.0));
   }


   public static LCStatus getStatus() {
      if (instance == null)
         instance = new LCStatus();
      return instance;
   }


   private PositionCalculator      positionCalculator;
   private CanonPosition           distanceToGo;
   private GCodeInfo               gcodeInfo;
   private ActiveCodes             activeCodes;
   private SpeedInfo               speedInfo;
   private ToolInfo                toolInfo;
   private ToolLibrary             toolLibrary;
   private ResourceBundle          messages;
   private AppSetup                setup;
   private LinuxCNCClient          app;
   private EventList<GCodeLine>    mdiHistory;
   @SuppressWarnings("rawtypes")
   private Map<String, ValueModel> models;
   public static String            MN_TaskState       = BufferDescriptor.TaskState;
   public static String            MN_ApplicationMode = "applicationMode";
   public static String            MN_TaskMode        = BufferDescriptor.TaskMode;
   public static String            MN_ExecState       = BufferDescriptor.ExecState;
   public static String            MN_AllHomed        = "allHomed";
   public static String            MN_ErrorActive     = "errorActive";
   public static String            MN_InterpState     = BufferDescriptor.InterpState;
   public static String            MN_SpindleDir      = BufferDescriptor.SpindleDir;
   public static String            MN_SingleStep      = "singleStep";
   public static String            MN_AbsPosition     = "absPosition";
   public static String            MN_DTG             = "DTG";
   private static LCStatus         instance;
}
