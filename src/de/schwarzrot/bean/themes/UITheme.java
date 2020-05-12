package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       UITheme.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    17.4.2020 by Django Reinhard
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
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import de.schwarzrot.bean.IStyles;
import de.schwarzrot.bean.ITheme;


public class UITheme {
   public static void exportTheme(File f) {
      UIDefaults  ud = UIManager.getDefaults();
      PrintWriter pw = null;

      try {
         pw = new PrintWriter(f);

         for (Map.Entry e : ud.entrySet()) {
            e.getKey();
            StringBuilder sb = new StringBuilder("UIM ");

            sb.append(e.getKey());
            sb.append(" = ");
            sb.append(e.getValue());
            pw.println(sb.toString());
         }
         t.export(pw);
         pw.flush();
         pw.close();
      } catch (FileNotFoundException e1) {
         e1.printStackTrace();
      }

   }


   public static Boolean getBoolean(String key) {
      Object v = getValue(key);

      return (v instanceof Boolean) ? (Boolean) v : null;
   }


   public static Color getColor(String key) {
      Object v = getValue(key);

      return (v instanceof Color) ? (Color) v : null;
   }


   public static Dimension getDimension(String key) {
      Object v = getValue(key);

      return (v instanceof Dimension) ? (Dimension) v : null;
   }


   public static File getFile(String key) {
      Object v = getValue(key);

      return (v instanceof File) ? (File) v : null;
   }


   public static Font getFont(String key) {
      Object v = getValue(key);

      return (v instanceof Font) ? (Font) v : null;
   }


   public static NumberFormat getFormat(String key) {
      Object v = getValue(key);

      return (v instanceof NumberFormat) ? (NumberFormat) v : null;
   }


   public static int getInt(String key) {
      Object v = getValue(key);

      return (v instanceof Integer) ? (int) v : null;
   }


   public static IStyles getStyles(String key) {
      Object v = getValue(key);

      return (v instanceof IStyles) ? (IStyles) v : null;
   }


   public static String getThemeName() {
      return themeName;
   }


   public static void put(String key, Object o) {
      settings.put(key, o);
   }


   public static void setupDefaults(ITheme theme) {
      if (theme != null) {
         importTheme(((AbstractTheme) theme).getSettings());
      }
   }


   public static void setupDefaults(String theme) {
      settings.put("GCode:basedir", new File("/usr/local/src/linuxcnc-dev", "nc_files"));

      if (theme != null) {
         if ("dark".compareTo(theme) == 0) {
            themeName = "dark";
            t         = new ThemeDark(settings);
         } else if ("Gtk".compareTo(theme) == 0) {
            themeName = "Gtk";
            t         = new ThemeGtk(settings);
         }
      } else {
         themeName = "RMA";
         t         = new ThemeRMA(settings);
      }
   }


   protected static void dumpMap(Map<String, Object> m) {
      for (String k : m.keySet()) {
         System.err.println(k + "\t=>\t" + m.get(k));
      }
   }


   protected static Object getValue(String key) {
      return settings.get(key);
   }


   protected static void importTheme(Map<String, Object> m) {
      for (String k : m.keySet()) {
         settings.put(k, m.get(k));
      }
   }


   private static Map<String, Object> settings;
   private static ITheme              t;
   private static String              themeName;
   static {
      settings = new HashMap<String, Object>();
   }
}
