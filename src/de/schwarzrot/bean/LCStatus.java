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
import de.schwarzrot.nml.TaskMode;
import de.schwarzrot.nml.TaskState;
import de.schwarzrot.util.PositionCalculator;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;


public class LCStatus {
   @SuppressWarnings("rawtypes")
   private LCStatus() {
      try {
         messages = ResourceBundle.getBundle("LinuxCNCClient");
      } catch (Throwable t) {
         messages = ResourceBundle.getBundle("LinuxCNCClient", new Locale("en", "EN"));
      }
      positionCalculator = new PositionCalculator();
      distanceToGo       = new CanonPosition();
      gcodeInfo          = new GCodeInfo();
      models             = new HashMap<String, ValueModel>();
      activeCodes        = new ActiveCodes();
      speedInfo          = new SpeedInfo();
      setup              = new AppSetup(this, true); // true: read tooltable from file, false: read tooltable from nml-status
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
      models.get("execState").setValue(execState);
   }


   @SuppressWarnings("unchecked")
   public void setInterpState(int state) {
      models.get("interpState").setValue(state);
   }


   @SuppressWarnings("unchecked")
   public void setTaskMode(int taskMode) {
      switch (taskMode) {
         case 1:
            models.get("taskMode").setValue(TaskMode.TaskModeManual);
            break;
         case 2:
            models.get("taskMode").setValue(TaskMode.TaskModeAuto);
            break;
         case 3:
            models.get("taskMode").setValue(TaskMode.TaskModeMDI);
            break;
         default:
            throw new IllegalArgumentException("unknown/invalid task mode");
      }
   }


   @SuppressWarnings("unchecked")
   public void setTaskState(int taskState) {
      switch (taskState) {
         case 1:
            models.get("taskState").setValue(TaskState.EStop);
            break;
         case 2:
            models.get("taskState").setValue(TaskState.EStopReset);
            break;
         case 3:
            models.get("taskState").setValue(TaskState.MachineOff);
            break;
         case 4:
            models.get("taskState").setValue(TaskState.MachineOn);
            break;
         default:
            throw new IllegalArgumentException("unknown/invalid task state");
      }
   }


   public void setToolLibrary(ToolLibrary toolLibrary) {
      this.toolLibrary = toolLibrary;
   }


   protected void setupModels() {
      models.put("taskState", new ValueModel<TaskState>("taskState", TaskState.EStop));
      models.put("taskMode", new ValueModel<TaskMode>("taskMode", TaskMode.TaskModeManual));
      models.put("execState", new ValueModel<Integer>("execState", -1));
      models.put("interpState", new ValueModel<Integer>("interpState", -1));
      models.put("errorActive", new ValueModel<Boolean>("errorActive", false));
      models.put("applicationMode",
            new ValueModel<ApplicationMode>("applicationMode", ApplicationMode.AmMachineOff));
      models.put("spindleDir", new ValueModel<Integer>("spindleDir", 0));
      models.put("singleStep", new ValueModel<Boolean>("singleStep", false));
      models.put("absPosition", new ValueModel<Boolean>("absPosition", false));
      models.put("DTG", new ValueModel<Double>("DTG", 0.0));
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
   private static LCStatus         instance;
}
