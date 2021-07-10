package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       AppSetup.java
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


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.LengthUnit;
import de.schwarzrot.util.ConfigParser;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;


public class AppSetup implements PropertyChangeListener, IAxisMask {
   public AppSetup(LCStatus status) {
      this(status, false);
   }


   public AppSetup(LCStatus status, boolean processToolTableFile) {
      this.status               = status;
      this.processTooltableFile = processToolTableFile;
      properties                = new HashMap<String, Map<String, String>>();
      parameters                = new HashMap<Integer, Double>();
      tools                     = new BasicEventList<ToolEntry>();
      displaySettings           = new DisplaySettings();
      fixture                   = new Fixtures();
      allHomed                  = new ValueModel<Boolean>("allHomed", false);
      unit                      = LengthUnit.MM;
      joints2Axis               = new int[9];

      if (status != null)
         setupModels();
   }


   public String findProperty(String key) {
      for (String g : properties.keySet()) {
         Map<String, String> sm = properties.get(g);

         for (String k : sm.keySet()) {
            if (k.compareTo(key) == 0) {
               return sm.get(k);
            }
         }
      }
      return null;
   }


   public int getAJoint() {
      return joints2Axis[3];
   }


   public ValueModel<Boolean> getAllHomed() {
      return allHomed;
   }


   public int getBJoint() {
      return joints2Axis[4];
   }


   public int getCJoint() {
      return joints2Axis[5];
   }


   public DisplaySettings getDisplaySettings() {
      return displaySettings;
   }


   public Fixtures getFixtures() {
      return fixture;
   }


   public File getIniFile() {
      return iniFile;
   }


   public int getNumberOfJoints() {
      return numJoints;
   }


   public int getNumberOfSpindles() {
      return numSpindles;
   }


   public Map<Integer, Double> getParameters() {
      return parameters;
   }


   public Map<String, String> getProperties(String groupName) {
      return properties.get(groupName);
   }


   public String getProperty(String groupName, String keyName) {
      return properties.get(groupName).get(keyName);
   }


   public EventList<ToolEntry> getTools() {
      return tools;
   }


   public File getToolTableFile() {
      return toolTableFile;
   }


   public int getUJoint() {
      return joints2Axis[6];
   }


   public LengthUnit getUnit() {
      return unit;
   }


   public File getVarFile() {
      return varFile;
   }


   public int getVJoint() {
      return joints2Axis[7];
   }


   public int getWJoint() {
      return joints2Axis[8];
   }


   public int getXJoint() {
      return joints2Axis[0];
   }


   public int getYJoint() {
      return joints2Axis[1];
   }


   public int getZJoint() {
      return joints2Axis[2];
   }


   @Override
   public boolean hasAAxis() {
      return (axisMask & 0x08) != 0;
   }


   @Override
   public boolean hasAxis(Character c) {
      switch (c) {
         case 'A':
         case 'a':
            return hasAAxis();
         case 'B':
         case 'b':
            return hasBAxis();
         case 'C':
         case 'c':
            return hasCAxis();
         case 'U':
         case 'u':
            return hasUAxis();
         case 'V':
         case 'v':
            return hasVAxis();
         case 'W':
         case 'w':
            return hasWAxis();
         case 'X':
         case 'x':
            return hasXAxis();
         case 'Y':
         case 'y':
            return hasYAxis();
         case 'Z':
         case 'z':
            return hasZAxis();
      }
      return false;
   }


   @Override
   public boolean hasBAxis() {
      return (axisMask & 0x10) != 0;
   }


   @Override
   public boolean hasCAxis() {
      return (axisMask & 0x20) != 0;
   }


   @Override
   public boolean hasUAxis() {
      return (axisMask & 0x40) != 0;
   }


   @Override
   public boolean hasVAxis() {
      return (axisMask & 0x80) != 0;
   }


   @Override
   public boolean hasWAxis() {
      return (axisMask & 0x100) != 0;
   }


   @Override
   public boolean hasXAxis() {
      return (axisMask & 0x01) != 0;
   }


   @Override
   public boolean hasYAxis() {
      return (axisMask & 0x02) != 0;
   }


   @Override
   public boolean hasZAxis() {
      return (axisMask & 0x04) != 0;
   }


   public void parseIniFile(String fileName) {
      ConfigParser ifp = new ConfigParser(this);

      iniFile = new File(fileName);
      System.out.println("use INI-File: " + iniFile.getAbsolutePath());
      properties = ifp.parseIniFile(fileName);
      ifp.processDisplaySection(displaySettings, properties.get("DISPLAY"));

      if (properties.containsKey("TRAJ"))
         processAxisSetup(getProperty("TRAJ", "COORDINATES"));

      // search for fixture file definition
      if (properties.containsKey("RS274NGC")) {
         String varFileName = getProperty("RS274NGC", "PARAMETER_FILE");

         if (varFileName != null) {
            varFile    = new File(iniFile.getParentFile(), varFileName);
            parameters = ifp.parseVarFile(varFile.getAbsolutePath());
            ifp.extractFixturesFromParameters(fixture, parameters);
         }
      }

      // search for tool table file definition
      if (properties.containsKey("EMCIO")) {
         String toolDef = getProperty("EMCIO", "TOOL_TABLE");
         toolTableFile = new File(iniFile.getParentFile(), toolDef);

         if (processTooltableFile) {
            System.out.println("process tool file: " + toolTableFile.getAbsolutePath());
            ifp.processTools(toolTableFile.getAbsolutePath());
         }
      }
      //      System.out.println("============ INI - Settings   Start =============");
      //      System.out.println(this);
      //      System.out.println(dumpProperties());
      //      System.out.println("============ INI - Settings   END   =============");
   }


   @Override
   public void propertyChange(PropertyChangeEvent e) {
      if (e.getPropertyName().startsWith(ModelPrefix) && e.getPropertyName().endsWith(ModelPostfix)) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               checkHomedState();
            }
         });
      }
   }


   public void setAxisMask(int axisMask) {
      // axismask is set from backend by StatusReader
      if (this.axisMask != 0 && (axisMask != this.axisMask))
         throw new UnsupportedOperationException("application setup is not allowed to change!");
      this.axisMask = axisMask;
   }


   public void setNumJoints(int numJoints) {
      if (this.numJoints != 0)
         throw new UnsupportedOperationException("application setup is not allowed to change!");
      this.numJoints = numJoints;
   }


   public void setNumSpindles(int numSpindles) {
      if (this.numSpindles != 0)
         throw new UnsupportedOperationException("application setup is not allowed to change!");
      this.numSpindles = numSpindles;
   }


   public void setToolTableFile(File toolTableFile) {
      this.toolTableFile = toolTableFile;
   }


   public void setUnit(int unitID) {
      switch (unitID) {
         case 1:
            this.unit = LengthUnit.Inch;
            break;
         case 3:
            this.unit = LengthUnit.CM;
            break;
         case 2:
         default:
            this.unit = LengthUnit.MM;
            break;
      }
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("setup looks like (");

      sb.append(axisMask);
      sb.append("):");
      sb.append("\nX-Axis ");
      sb.append(hasXAxis() ? "YES" : "NO");
      sb.append("\nY-Axis ");
      sb.append(hasYAxis() ? "YES" : "NO");
      sb.append("\nZ-Axis ");
      sb.append(hasZAxis() ? "YES" : "NO");
      sb.append("\nA-Axis ");
      sb.append(hasAAxis() ? "YES" : "NO");
      sb.append("\nB-Axis ");
      sb.append(hasBAxis() ? "YES" : "NO");
      sb.append("\nC-Axis ");
      sb.append(hasCAxis() ? "YES" : "NO");
      sb.append("\nU-Axis ");
      sb.append(hasUAxis() ? "YES" : "NO");
      sb.append("\nV-Axis ");
      sb.append(hasVAxis() ? "YES" : "NO");
      sb.append("\nW-Axis ");
      sb.append(hasWAxis() ? "YES" : "NO");
      sb.append("\njoints: ");
      sb.append(getNumberOfJoints());
      sb.append("\nspindles: ");
      sb.append(getNumberOfSpindles());
      sb.append("\nlength units: ");
      switch (unit) {
         case Inch:
            sb.append("inches");
            break;
         case MM:
            sb.append("mm");
            break;
         case CM:
            sb.append("cm");
            break;
      }
      return sb.toString();
   }


   @SuppressWarnings("rawtypes")
   protected void checkHomedState() {
      boolean                 allJointsHomed = true;
      Map<String, ValueModel> models         = status.getValueModels();

      if (this.hasXAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getXJoint())).getValue())) {
            System.out.println("X Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasYAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getYJoint())).getValue())) {
            System.out.println("Y Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasZAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getZJoint())).getValue())) {
            System.out.println("Z Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasAAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getAJoint())).getValue())) {
            System.out.println("A Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasBAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getBJoint())).getValue())) {
            System.out.println("B Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasCAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getCJoint())).getValue())) {
            System.out.println("C Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasUAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getUJoint())).getValue())) {
            System.out.println("U Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasVAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getVJoint())).getValue())) {
            System.out.println("V Axis is not homed");
            allJointsHomed = false;
         }
      }
      if (this.hasWAxis()) {
         if (!((Boolean) models.get(String.format(jointModelPattern, getWJoint())).getValue())) {
            System.out.println("W Axis is not homed");
            allJointsHomed = false;
         }
      }
      boolean ov = allHomed.getValue();

      allHomed.setValue(allJointsHomed);
      if (allJointsHomed && !ov) {
         System.out.println("\nnow all JOINTS are homed.\n");
      }
   }


   protected String dumpProperties() {
      StringBuilder sb = new StringBuilder("properties from ini-file:");

      for (String group : properties.keySet()) {
         Map<String, String> groupSettings = properties.get(group);

         sb.append("\ngroup [");
         sb.append(group);
         sb.append("]:");
         for (String key : groupSettings.keySet()) {
            sb.append("\n\t");
            sb.append(key);
            sb.append("\t\t=>\t");
            sb.append(groupSettings.get(key));
         }
      }
      return sb.toString();
   }


   protected void processAxisSetup(String axisPattern) {
      int[] joints2Axis = new int[9];
      int   joint       = 0;

      for (char c : axisPattern.toCharArray()) {
         if (c == ' ' || c == '\t')
            continue;

         switch (c) {
            case 'x':
            case 'X':
               joints2Axis[0] = joint;
               break;
            case 'y':
            case 'Y':
               joints2Axis[1] = joint;
               break;
            case 'z':
            case 'Z':
               joints2Axis[2] = joint;
               break;
            case 'a':
            case 'A':
               joints2Axis[3] = joint;
               break;
            case 'b':
            case 'B':
               joints2Axis[4] = joint;
               break;
            case 'c':
            case 'C':
               joints2Axis[5] = joint;
               break;
            case 'u':
            case 'U':
               joints2Axis[6] = joint;
               break;
            case 'v':
            case 'V':
               joints2Axis[7] = joint;
               break;
            case 'w':
            case 'W':
               joints2Axis[8] = joint;
               break;
         }
         ++joint;
      }
   }


   @SuppressWarnings("rawtypes")
   protected void setupModels() {
      Map<String, ValueModel> models = status.getValueModels();
      ValueModel              m      = null;

      for (String n : listOfSignals) {
         m = models.get(n);
         if (m == null) {
            m = new ValueModel<Boolean>(n, false);
            models.put(n, m);
         }
         m.addPropertyChangeListener(this);
      }
      if (!models.containsKey("allHomed"))
         models.put("allHomed", allHomed);
   }


   private int                              axisMask;
   private int                              numJoints;
   private int                              numSpindles;
   private boolean                          processTooltableFile;
   private ValueModel<Boolean>              allHomed;
   private LengthUnit                       unit;
   private EventList<ToolEntry>             tools;
   private LCStatus                         status;
   private DisplaySettings                  displaySettings;
   private Fixtures                         fixture;
   private File                             toolTableFile;
   private File                             iniFile;
   private File                             varFile;
   private int[]                            joints2Axis;
   private Map<String, Map<String, String>> properties;
   private Map<Integer, Double>             parameters;
   public static final String               ModelPrefix       = "joint_";
   public static final String               ModelPostfix      = "_homed";
   public static final String               jointModelPattern = "joint_%d_homed";
   private static final String[]            listOfSignals     = { "joint_0_homed", "joint_1_homed",
         "joint_2_homed", "joint_3_homed", "joint_4_homed", "joint_5_homed", "joint_6_homed", "joint_7_homed",
         "joint_8_homed" };
}
