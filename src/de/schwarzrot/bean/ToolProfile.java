package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       ToolProfile.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    9.11.2019 by Django Reinhard
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

public enum ToolProfile {
   EndMill(0), BullNose(1), VCutter(2), BallNose(3), Drill(4), Lathe(5);


   private ToolProfile(int profile) {
      this.profile = profile;
   }


   public int getProfile() {
      return profile;
   }


   public static ToolProfile parse(String source) {
      if (EndMill.name().compareTo(source) == 0) {
         return EndMill;
      } else if (BullNose.name().compareTo(source) == 0) {
         return BullNose;
      } else if (BallNose.name().compareTo(source) == 0) {
         return BallNose;
      } else if (VCutter.name().compareTo(source) == 0) {
         return VCutter;
      } else if (Drill.name().compareTo(source) == 0) {
         return Drill;
      } else if (Lathe.name().compareTo(source) == 0) {
         return Lathe;
      }
      return null;
   }


   private final int profile;
}
