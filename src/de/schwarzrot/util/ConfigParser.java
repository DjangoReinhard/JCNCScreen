package de.schwarzrot.util;
/*
 * **************************************************************************
 *
 *  file:       ConfigParser.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    29.9.2019 by Django Reinhard
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


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.DisplaySettings;
import de.schwarzrot.bean.Fixtures;


public class ConfigParser {
   public ConfigParser(AppSetup setup) {
      this.appSetup = setup;
      this.ttHdr    = new LCToolTableImporter();
   }


   public void extractFixturesFromParameters(Fixtures fixture, Map<Integer, Double> parameters) {
      int i = 5210;

      fixture.getCommonOffset().setX(parameters.get(i + 1));
      fixture.getCommonOffset().setY(parameters.get(i + 2));
      fixture.getCommonOffset().setZ(parameters.get(i + 3));
      fixture.getCommonOffset().setA(parameters.get(i + 4));
      fixture.getCommonOffset().setB(parameters.get(i + 5));
      fixture.getCommonOffset().setC(parameters.get(i + 6));
      fixture.getCommonOffset().setU(parameters.get(i + 7));
      fixture.getCommonOffset().setV(parameters.get(i + 8));
      fixture.getCommonOffset().setW(parameters.get(i + 9));
      fixture.getG54Offset().setRotation(parameters.get(i + 10));

      i = 5220;
      fixture.getG54Offset().setX(parameters.get(i + 1));
      fixture.getG54Offset().setY(parameters.get(i + 2));
      fixture.getG54Offset().setZ(parameters.get(i + 3));
      fixture.getG54Offset().setA(parameters.get(i + 4));
      fixture.getG54Offset().setB(parameters.get(i + 5));
      fixture.getG54Offset().setC(parameters.get(i + 6));
      fixture.getG54Offset().setU(parameters.get(i + 7));
      fixture.getG54Offset().setV(parameters.get(i + 8));
      fixture.getG54Offset().setW(parameters.get(i + 9));
      fixture.getG54Offset().setRotation(parameters.get(i + 10));

      // 5241-5250 - Coordinate System 2, G55 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5240;
      fixture.getG55Offset().setX(parameters.get(i + 1));
      fixture.getG55Offset().setY(parameters.get(i + 2));
      fixture.getG55Offset().setZ(parameters.get(i + 3));
      fixture.getG55Offset().setA(parameters.get(i + 4));
      fixture.getG55Offset().setB(parameters.get(i + 5));
      fixture.getG55Offset().setC(parameters.get(i + 6));
      fixture.getG55Offset().setU(parameters.get(i + 7));
      fixture.getG55Offset().setV(parameters.get(i + 8));
      fixture.getG55Offset().setW(parameters.get(i + 9));
      fixture.getG55Offset().setRotation(parameters.get(i + 10));

      // 5261-5270 - Coordinate System 3, G56 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5260;
      fixture.getG56Offset().setX(parameters.get(i + 1));
      fixture.getG56Offset().setY(parameters.get(i + 2));
      fixture.getG56Offset().setZ(parameters.get(i + 3));
      fixture.getG56Offset().setA(parameters.get(i + 4));
      fixture.getG56Offset().setB(parameters.get(i + 5));
      fixture.getG56Offset().setC(parameters.get(i + 6));
      fixture.getG56Offset().setU(parameters.get(i + 7));
      fixture.getG56Offset().setV(parameters.get(i + 8));
      fixture.getG56Offset().setW(parameters.get(i + 9));
      fixture.getG56Offset().setRotation(parameters.get(i + 10));

      // 5281-5290 - Coordinate System 4, G57 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5280;
      fixture.getG57Offset().setX(parameters.get(i + 1));
      fixture.getG57Offset().setY(parameters.get(i + 2));
      fixture.getG57Offset().setZ(parameters.get(i + 3));
      fixture.getG57Offset().setA(parameters.get(i + 4));
      fixture.getG57Offset().setB(parameters.get(i + 5));
      fixture.getG57Offset().setC(parameters.get(i + 6));
      fixture.getG57Offset().setU(parameters.get(i + 7));
      fixture.getG57Offset().setV(parameters.get(i + 8));
      fixture.getG57Offset().setW(parameters.get(i + 9));
      fixture.getG57Offset().setRotation(parameters.get(i + 10));

      // 5301-5310 - Coordinate System 5, G58 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5300;
      fixture.getG58Offset().setX(parameters.get(i + 1));
      fixture.getG58Offset().setY(parameters.get(i + 2));
      fixture.getG58Offset().setZ(parameters.get(i + 3));
      fixture.getG58Offset().setA(parameters.get(i + 4));
      fixture.getG58Offset().setB(parameters.get(i + 5));
      fixture.getG58Offset().setC(parameters.get(i + 6));
      fixture.getG58Offset().setU(parameters.get(i + 7));
      fixture.getG58Offset().setV(parameters.get(i + 8));
      fixture.getG58Offset().setW(parameters.get(i + 9));
      fixture.getG58Offset().setRotation(parameters.get(i + 10));

      // 5321-5330 - Coordinate System 6, G59 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5320;
      fixture.getG59Offset().setX(parameters.get(i + 1));
      fixture.getG59Offset().setY(parameters.get(i + 2));
      fixture.getG59Offset().setZ(parameters.get(i + 3));
      fixture.getG59Offset().setA(parameters.get(i + 4));
      fixture.getG59Offset().setB(parameters.get(i + 5));
      fixture.getG59Offset().setC(parameters.get(i + 6));
      fixture.getG59Offset().setU(parameters.get(i + 7));
      fixture.getG59Offset().setV(parameters.get(i + 8));
      fixture.getG59Offset().setW(parameters.get(i + 9));
      fixture.getG59Offset().setRotation(parameters.get(i + 10));

      // 5341-5350 - Coordinate System 7, G59.1 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5340;
      fixture.getG591Offset().setX(parameters.get(i + 1));
      fixture.getG591Offset().setY(parameters.get(i + 2));
      fixture.getG591Offset().setZ(parameters.get(i + 3));
      fixture.getG591Offset().setA(parameters.get(i + 4));
      fixture.getG591Offset().setB(parameters.get(i + 5));
      fixture.getG591Offset().setC(parameters.get(i + 6));
      fixture.getG591Offset().setU(parameters.get(i + 7));
      fixture.getG591Offset().setV(parameters.get(i + 8));
      fixture.getG591Offset().setW(parameters.get(i + 9));
      fixture.getG591Offset().setRotation(parameters.get(i + 10));

      // 5361-5370 - Coordinate System 8, G59.2 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5360;
      fixture.getG592Offset().setX(parameters.get(i + 1));
      fixture.getG592Offset().setY(parameters.get(i + 2));
      fixture.getG592Offset().setZ(parameters.get(i + 3));
      fixture.getG592Offset().setA(parameters.get(i + 4));
      fixture.getG592Offset().setB(parameters.get(i + 5));
      fixture.getG592Offset().setC(parameters.get(i + 6));
      fixture.getG592Offset().setU(parameters.get(i + 7));
      fixture.getG592Offset().setV(parameters.get(i + 8));
      fixture.getG592Offset().setW(parameters.get(i + 9));
      fixture.getG592Offset().setRotation(parameters.get(i + 10));

      // 5381-5390 - Coordinate System 9, G59.3 for X, Y, Z, A, B, C, U, V, W & R. Persistent.
      i = 5380;
      fixture.getG593Offset().setX(parameters.get(i + 1));
      fixture.getG593Offset().setY(parameters.get(i + 2));
      fixture.getG593Offset().setZ(parameters.get(i + 3));
      fixture.getG593Offset().setA(parameters.get(i + 4));
      fixture.getG593Offset().setB(parameters.get(i + 5));
      fixture.getG593Offset().setC(parameters.get(i + 6));
      fixture.getG593Offset().setU(parameters.get(i + 7));
      fixture.getG593Offset().setV(parameters.get(i + 8));
      fixture.getG593Offset().setW(parameters.get(i + 9));
      fixture.getG593Offset().setRotation(parameters.get(i + 10));
   }


   public Map<String, Map<String, String>> parseIniFile(String fileName) {
      Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();
      BufferedReader                   br         = null;
      String                           line;

      try {
         br = new BufferedReader(new FileReader(fileName));

         while ((line = br.readLine()) != null) {
            if (line.isEmpty())
               continue;
            line = line.strip();

            if (line.isEmpty())
               continue;
            if (line.startsWith("#"))
               continue;
            if (line.startsWith("[")) {
               do {
                  String              group  = line.substring(1, line.length() - 1);
                  Map<String, String> subMap = new HashMap<String, String>();
                  String[]            parts  = null;

                  properties.put(group, subMap);

                  while ((line = br.readLine()) != null) {
                     if (line.isEmpty())
                        continue;
                     line = line.strip();

                     if (line.isEmpty())
                        continue;
                     if (line.startsWith("#"))
                        continue;

                     if (line.startsWith("["))
                        break;
                     parts = line.split("\\s*=\\s*", 2);

                     // System.out.println("found property for [" + group
                     // + "]: >> "
                     // + parts[0]
                     // + " => "
                     // + parts[1]);
                     if (subMap.containsKey(parts[0])) {
                        System.err.println("may be duplicate entry? [" + parts[0] + "]");
                        StringBuilder sb = new StringBuilder(subMap.get(parts[0]));

                        sb.append(",");
                        sb.append(parts[1]);

                        subMap.put(parts[0], sb.toString());
                     } else
                        subMap.put(parts[0], parts[1]);
                  }
               } while (line != null && line.startsWith("["));
            }
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return properties;
   }


   public Map<Integer, Double> parseVarFile(String fileName) {
      Map<Integer, Double> parameters = new HashMap<Integer, Double>();
      BufferedReader       br         = null;
      String               line       = null;
      double               v          = 0;
      int                  k          = 0;

      try {
         br = new BufferedReader(new FileReader(fileName));

         while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            v = 0;
            try {
               k = Integer.parseInt(parts[0]);

               if (parts.length > 1) {
                  v = Double.parseDouble(parts[1]);
               }
               parameters.put(k, v);
            } catch (Throwable t) {
            }
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return parameters;
   }


   public void processDisplaySection(DisplaySettings settings, Map<String, String> parameters) {
      for (String key : parameters.keySet()) {
         if (key.compareTo("MAX_FEED_OVERRIDE") == 0) {
            try {
               settings.setFeedOverrideMax(Double.parseDouble(parameters.get(key)));
            } catch (Throwable t) {
            }
         } else if (key.compareTo("MIN_SPINDLE_OVERRIDE") == 0) {
            settings.setSpindleOverrideMin(Double.parseDouble(parameters.get(key)));
         } else if (key.compareTo("MAX_SPINDLE_OVERRIDE") == 0) {
            settings.setSpindleOverrideMax(Double.parseDouble(parameters.get(key)));
         } else if (key.compareTo("DEFAULT_SPINDLE_SPEED") == 0) {
            settings.setDefaultSpindleSpeed(Integer.parseInt(parameters.get(key)));
         } else if (key.compareTo("PROGRAM_PREFIX") == 0) {
            settings.setGcodeBaseDir(parameters.get(key));
         } else if (key.compareTo("INTRO_GRAPHIC") == 0) {
            settings.setIntroGraphic(parameters.get(key));
         } else if (key.compareTo("INTRO_TIME") == 0) {
            settings.setIntroTime(Integer.parseInt(parameters.get(key)));
         } else if (key.compareTo("CYCLE_TIME") == 0) {
            settings.setCycleTime(Double.parseDouble(parameters.get(key)));
            // } else if (key.compareTo("POSITION_FEEDBACK") == 0) {
            // ;
            // } else if (key.compareTo("POSITION_OFFSET") == 0) {
            // ;
         } else if (key.compareTo("DRO_FORMAT_MM") == 0) {
            settings.setDroFormatMM(parameters.get(key));
         } else if (key.compareTo("DRO_FORMAT_INCH") == 0) {
            settings.setDroFormatInch(parameters.get(key));
         } else if (key.compareTo("PREFERENCE_FILE_PATH") == 0) {
            settings.setPreferenceFile(parameters.get(key));
         } else if (key.compareTo("INCREMENTS") == 0) {
            settings.setIncrements(parameters.get(key));
         }
      }
   }


   public void processTools(String toolDefFile) {
      ttHdr.importToolTable(toolDefFile);
   }


   private AppSetup            appSetup;
   private LCToolTableImporter ttHdr;
}
