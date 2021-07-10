package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       DisplaySettings.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    3.10.2019 by Django Reinhard
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DisplaySettings {
   public DisplaySettings() {
      feedOverrideMax     = 100;
      spindleOverrideMin  = 0;
      spindleOverrideMax  = 100;
      defaultSpindleSpeed = 1000;
      cycleTime           = 0.1;
      introTime           = 5;
      //      gcodeBaseDir;
      //      introGraphic;
      //      preferenceFile;
      droFormatMM         = "%.3f";
      droFormatInch       = "%.4f";
   }


   public double getCycleTime() {
      return cycleTime;
   }


   public int getDefaultSpindleSpeed() {
      return defaultSpindleSpeed;
   }


   public String getDroFormatInch() {
      return droFormatInch;
   }


   public String getDroFormatMM() {
      return droFormatMM;
   }


   public double getFeedOverrideMax() {
      return feedOverrideMax;
   }


   public String getGcodeBaseDir() {
      return gcodeBaseDir;
   }


   public final double[] getIncrements() {
      return increments;
   }


   public String getIntroGraphic() {
      return introGraphic;
   }


   public int getIntroTime() {
      return introTime;
   }


   public String getPreferenceFile() {
      return preferenceFile;
   }


   public double getSpindleOverrideMax() {
      return spindleOverrideMax;
   }


   public double getSpindleOverrideMin() {
      return spindleOverrideMin;
   }


   public void setCycleTime(double cycleTime) {
      this.cycleTime = cycleTime;
   }


   public void setDefaultSpindleSpeed(int defaultSpindleSpeed) {
      this.defaultSpindleSpeed = defaultSpindleSpeed;
   }


   public void setDroFormatInch(String droFormatInch) {
      this.droFormatInch = droFormatInch;
   }


   public void setDroFormatMM(String droFormatMM) {
      this.droFormatMM = droFormatMM;
   }


   public void setFeedOverrideMax(double feedOverrideMax) {
      this.feedOverrideMax = feedOverrideMax;
   }


   public void setGcodeBaseDir(String gcodeBaseDir) {
      this.gcodeBaseDir = gcodeBaseDir;
   }

   //   public void setIncrements(List<Double> l) {
   //      this.increments = l;
   //   }


   public void setIncrements(String configLine) {
      String[]     parts      = configLine.split(",\\s*");
      char[]       unitChars  = { 'c', 'm', 'i' };
      double[]     factors    = { 10, 1, 25.4, 0.0254 };
      List<Double> increments = new ArrayList<Double>();
      Integer      unit       = null;
      String       rawValue;
      double       value;

      System.out.println("config-line: >>" + configLine + "<<");
      for (String s : parts) {
         unit = null;
         for (int i = 0; i < unitChars.length; ++i) {
            int n = s.indexOf(unitChars[i]);

            if (n < 0)
               continue;
            String rawUnit = s.substring(n);

            unit = i;
            if (unit == 1) {
               if (rawUnit.startsWith("mil"))
                  unit = 3;
            }
            rawValue = s.substring(0, n).strip();
            int divide = rawValue.indexOf('/');

            if (divide > 0) {
               String rawCounter = rawValue.substring(0, divide);
               String rawDivisor = rawValue.substring(divide + 1);

               try {
                  int counter = Integer.parseInt(rawCounter);
                  int divisor = Integer.parseInt(rawDivisor);

                  value = (double) counter / (double) divisor;
                  increments.add(value * factors[unit]);
                  break;
               } catch (Throwable t) {
               }
            } else {
               try {
                  value = Double.parseDouble(rawValue);
                  increments.add(value * factors[unit]);
                  break;
               } catch (Throwable t) {
               }
            }
         }
      }
      Collections.sort(increments);
      int mx   = increments.size();
      int size = Math.min(3, mx);

      this.increments    = new double[size + 1];
      this.increments[0] = 0;
      for (int i = 0; i < size; ++i) {
         this.increments[i + 1] = increments.get(mx - size + i);
      }

      System.out.println("got these increments ...");
      boolean first = true;

      for (Double dv : this.increments) {
         if (first)
            first = false;
         else
            System.out.print(", ");
         System.out.print(dv);
      }
      System.out.println();
   }


   public void setIntroGraphic(String introGraphic) {
      this.introGraphic = introGraphic;
   }


   public void setIntroTime(int introTime) {
      this.introTime = introTime;
   }


   public void setPreferenceFile(String preferenceFile) {
      this.preferenceFile = preferenceFile;
   }


   public void setSpindleOverrideMax(double spindleOverrideMax) {
      this.spindleOverrideMax = spindleOverrideMax;
   }


   public void setSpindleOverrideMin(double spindleOverrideMin) {
      this.spindleOverrideMin = spindleOverrideMin;
   }


   private double   feedOverrideMax;
   private double   spindleOverrideMin;
   private double   spindleOverrideMax;
   private int      defaultSpindleSpeed;
   private double   cycleTime;
   private int      introTime;
   private double[] increments;
   private String   gcodeBaseDir;
   private String   introGraphic;
   private String   preferenceFile;
   private String   droFormatMM;
   private String   droFormatInch;
}
