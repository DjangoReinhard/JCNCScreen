package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       ConfigHolder.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    20.10.2019 by Django Reinhard
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


import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import com.jme3.app.SimpleApplication;

import de.schwarzrot.bean.GCodeLine;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.PaneStack;


public class ConfigHolder {
   public ConfigHolder(String appName, List<SystemMessage> messages) {
      File base        = new File(System.getProperty(HomeDirectory), ".config");
      File linuxcncDir = new File(base, "linuxcnc");

      this.appBase  = new File(linuxcncDir, appName);
      this.errorLog = messages;

      // System.out.println("ConfigHolder(\"" + appName + "\")");
      // System.out.println("app-base is: " + appBase.getAbsolutePath());

      readConfigs();
   }


   public SimpleApplication getJME3App() {
      return this.jme3App;
   }


   public void saveConfigs() {
      File messageLog = new File(appBase, "message.log");

      try {
         if (messageLog.canWrite() || messageLog.createNewFile()) {
            exportMessages(messageLog);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      File preferences = new File(appBase, "preferences");

      try {
         if (preferences.canWrite() || preferences.createNewFile()) {
            exportPreferences(preferences);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      File mdiHistory = new File(appBase, "mdi.history");

      try {
         if (mdiHistory.canWrite() || mdiHistory.createNewFile()) {
            exportMDIHistory(mdiHistory);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      File uiTheme = new File(appBase, "theme");

      try {
         if (uiTheme.canWrite() || uiTheme.createNewFile()) {
            UITheme.exportTheme(uiTheme);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   public void setErrorLog(List<SystemMessage> list) {
      this.errorLog = list;
   }


   public void setJME3App(SimpleApplication app) {
      this.jme3App = app;
   }


   protected void dumpEnv() {
      Properties sp = System.getProperties();

      for (Object k : sp.keySet()) {
         System.out.println("[" + k + "] -> " + sp.get(k));
      }
   }


   protected void exportMDIHistory(File historyFile) {
      List<GCodeLine> list = LCStatus.getStatus().getMDIHistory();
      PrintWriter     pw   = null;

      try {
         pw = new PrintWriter(historyFile, "UTF-8");
         for (GCodeLine l : list) {
            pw.println(l.getLine());
         }
      } catch (FileNotFoundException | UnsupportedEncodingException e) {
         e.printStackTrace();
      } finally {
         if (pw != null)
            pw.close();
      }
   }


   protected void exportMessages(File messageFile) {
      PrintWriter pw = null;

      try {
         pw = new PrintWriter(messageFile, "UTF-8");

         for (SystemMessage sm : errorLog) {
            pw.println(sm);
         }
      } catch (FileNotFoundException | UnsupportedEncodingException e) {
         e.printStackTrace();
      } finally {
         if (pw != null)
            pw.close();
      }
   }


   protected void exportPreferences(File prefFile) {
      PaneStack   ps     = PaneStack.getInstance();
      PrintWriter pw     = null;
      //      AppSettingsPane asp    = (AppSettingsPane) ps.getSettingsPane();
      LCStatus    status = LCStatus.getStatus();

      try {
         pw = new PrintWriter(prefFile, "UTF-8");

         pw.println("auto-gcode = " + status.getGCodeInfo().getFileName());
         pw.println("edit-gcode = " + ps.getEditGCodePane().getFileName());
         pw.println("feed-factor = " + status.getSpeedInfo().getFeedFactor());
         pw.println("rapid-factor = " + status.getSpeedInfo().getRapidFactor());
         pw.println("spindle-factor = " + status.getSpeedInfo().getSpindleFactor());
         pw.println("theme = " + UITheme.getThemeName());

         //TODO: rework app-settings pane to support all themed values
         //
         //         printColor(pw, "DRO:abs.background", asp.getAbsPos().getBackground());
         //         printColor(pw, "DRO:abs.foreground", asp.getAbsPos().getForeground());
         //         printFont(pw, "DRO:abs.font", asp.getAbsPos().getFont());
         //
         //         printColor(pw, "DRO:rel.background", asp.getRelPos().getBackground());
         //         printColor(pw, "DRO:rel.foreground", asp.getRelPos().getForeground());
         //         printFont(pw, "DRO:rel.font", asp.getRelPos().getFont());
         //
         //         printColor(pw, "DRO:dtg.background", asp.getRelPos().getBackground());
         //         printColor(pw, "DRO:dtg.foreground", asp.getRelPos().getForeground());
         //         printFont(pw, "DRO:dtg.font", asp.getRelPos().getFont());
         //
         //         printColor(pw, "GCode:line.background", asp.getGCode().getBackground());
         //         printColor(pw, "GCode:line.foreground", asp.getGCode().getForeground());
         //         printFont(pw, "GCode:line.font", asp.getGCode().getFont());
         //         //speed
         //         printColor(pw, "DRO:speed.background", asp.getSpeed().getBackground());
         //         printColor(pw, "DRO:speed.foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "DRO:speed.font", asp.getSpeed().getFont());
         //         //axisletter
         //         printColor(pw, "Axis:background", asp.getSpeed().getBackground());
         //         printColor(pw, "Axis:foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "Axis:font", asp.getSpeed().getFont());
         //         //toolnum
         //         printColor(pw, "Tool:number.background", asp.getSpeed().getBackground());
         //         printColor(pw, "Tool:number.foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "Tool:number.font", asp.getSpeed().getFont());
         //         //toolinfo
         //         printColor(pw, "Tool:info.background", asp.getSpeed().getBackground());
         //         printColor(pw, "Tool:info.foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "Tool:info.font", asp.getSpeed().getFont());
         //         //filemgr
         //         printColor(pw, "FileManager:background", asp.getSpeed().getBackground());
         //         printColor(pw, "FileManager:foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "FileManager:font", asp.getSpeed().getFont());
         //         //tooltable
         //         printColor(pw, "ToolTable.background", asp.getSpeed().getBackground());
         //         printColor(pw, "ToolTable.foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "ToolTable.font", asp.getSpeed().getFont());
         //         //fixturetable
         //         printColor(pw, "Fixture:background", asp.getSpeed().getBackground());
         //         printColor(pw, "Fixture:foreground", asp.getSpeed().getForeground());
         //         printFont(pw, "Fixture:table.font", asp.getSpeed().getFont());
      } catch (FileNotFoundException | UnsupportedEncodingException e) {
         e.printStackTrace();
      } finally {
         if (pw != null)
            pw.close();
      }
   }


   protected void importMDIHistory(File historyFile) {
      List<GCodeLine> list = LCStatus.getStatus().getMDIHistory();
      BufferedReader  br   = null;
      String          line = null;
      int             n    = 0;

      try {
         br = new BufferedReader(new FileReader(historyFile));

         while ((line = br.readLine()) != null) {
            if (line.strip().isEmpty())
               continue;
            list.add(new GCodeLine(++n, line));
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException e) {
         }
      }
   }


   protected void importMessages(File messageLog) {
      BufferedReader br   = null;
      String         line = null;
      SystemMessage  sm   = null;

      try {
         br = new BufferedReader(new FileReader(messageLog));

         while ((line = br.readLine()) != null) {
            if ((sm = SystemMessage.parseMessage(line)) != null) {
               errorLog.add(sm);
            }
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException e) {
         }
      }
   }


   protected void importPreferences(File prefFile) {
      LCStatus       status    = LCStatus.getStatus();
      CommandWriter  cmdWriter = status.getApp().getCommandWriter();
      BufferedReader br        = null;
      String         line      = null;

      try {
         br = new BufferedReader(new FileReader(prefFile));

         //         pw.println("auto-gcode = " + status.getGCodeInfo().getFileName());
         //         pw.println("edit-gcode = " + ps.getEditGCodePane().getFileName());
         //         pw.println("feed-factor = " + status.getSpeedInfo().getFeedFactor());
         //         pw.println("rapid-factor = " + status.getSpeedInfo().getRapidFactor());
         //         pw.println("spindle-factor = " + status.getSpeedInfo().getSpindleFactor());
         //         pw.println("theme = " + UITheme.getThemeName());
         while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s*=\\s*", 2);

            if ("auto-gcode".compareTo(parts[0]) == 0) {
               //               status.getGCodeInfo().setFileName(parts[1]);
               // issue nml?
            } else if ("edit-gcode".compareTo(parts[0]) == 0) {
               status.getGCodeInfo().setEditFile(parts[1]);
            } else if ("feed-factor".compareTo(parts[0]) == 0) {
               // issue nml?
            } else if ("rapid-factor".compareTo(parts[0]) == 0) {
               // issue nml?
            } else if ("spindle-factor".compareTo(parts[0]) == 0) {
               // issue nml?
            } else if ("theme".compareTo(parts[0]) == 0) {
               UITheme.setupDefaults(parts[1]);
            }
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException e) {
         }
      }
   }


   protected void printColor(PrintWriter pw, String id, Color c) {
      StringBuilder sb = new StringBuilder(id);

      sb.append(" = ");
      sb.append(c.getRed());
      sb.append(", ");
      sb.append(c.getGreen());
      sb.append(", ");
      sb.append(c.getBlue());

      pw.println(sb.toString());
   }


   protected void printFont(PrintWriter pw, String id, Font f) {
      StringBuilder sb = new StringBuilder(id);

      sb.append(" = ");
      sb.append(f.getFamily());
      sb.append(",");
      if ((f.getStyle() & Font.BOLD) != 0) {
         sb.append(" BOLD");
      }
      if ((f.getStyle() & Font.ITALIC) != 0) {
         sb.append(" ITALIC");
      }
      sb.append(", ");
      sb.append(f.getSize());

      pw.println(sb.toString());
   }


   protected void readConfigs() {
      // dumpEnv();
      if (!appBase.exists())
         appBase.mkdirs();
      else {
         // ok, fetch saved stuff ...
         File preferences = new File(appBase, "preferences");

         if (preferences.exists() && preferences.canRead()) {
            importPreferences(preferences);
         }
         File mdiHistory = new File(appBase, "mdi.history");

         if (mdiHistory.exists() && mdiHistory.canRead()) {
            importMDIHistory(mdiHistory);
         }
         File messageLog = new File(appBase, "message.log");

         if (messageLog.exists() && messageLog.canRead()) {
            importMessages(messageLog);
         }
      }
   }


   private List<SystemMessage> errorLog;
   private final File          appBase;
   private SimpleApplication   jme3App;
   private static final String HomeDirectory = "user.home";
}
