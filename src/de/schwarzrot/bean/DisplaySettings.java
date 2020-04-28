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

public class DisplaySettings {
   public DisplaySettings() {
      feedOverrideMax = 100;
      spindleOverrideMin = 0;
      spindleOverrideMax = 100;
      defaultSpindleSpeed = 1000;
      cycleTime = 0.1;
      introTime = 5;
//      private double[] increments;
//      gcodeBaseDir;
//      introGraphic;
//      preferenceFile;
      droFormatMM = "%.3f";
      droFormatInch = "%.4f";      
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


   public double[] getIncrements() {
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


   public void setIncrements(double[] increments) {
      this.increments = increments;
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
