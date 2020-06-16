package de.schwarzrot.model;
/*
 * **************************************************************************
 *
 *  file:       ActiveCodes.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.9.2019 by Django Reinhard
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


public class ActiveCodes extends AbstractModel {
   public int getGcode0() {
      return gcode0;
   }


   public int getGcode1() {
      return gcode1;
   }


   public int getGcode10() {
      return gcode10;
   }


   public int getGcode11() {
      return gcode11;
   }


   public int getGcode12() {
      return gcode12;
   }


   public int getGcode13() {
      return gcode13;
   }


   public int getGcode14() {
      return gcode14;
   }


   public int getGcode15() {
      return gcode15;
   }


   public int getGcode2() {
      return gcode2;
   }


   public int getGcode3() {
      return gcode3;
   }


   public int getGcode4() {
      return gcode4;
   }


   public int getGcode5() {
      return gcode5;
   }


   public int getGcode6() {
      return gcode6;
   }


   public int getGcode7() {
      return gcode7;
   }


   public int getGcode8() {
      return gcode8;
   }


   public int getGcode9() {
      return gcode9;
   }


   public int getMcode0() {
      return mcode0;
   }


   public int getMcode1() {
      return mcode1;
   }


   public int getMcode2() {
      return mcode2;
   }


   public int getMcode3() {
      return mcode3;
   }


   public int getMcode4() {
      return mcode4;
   }


   public int getMcode5() {
      return mcode5;
   }


   public int getMcode6() {
      return mcode6;
   }


   public int getMcode7() {
      return mcode7;
   }


   public int getMcode8() {
      return mcode8;
   }


   public int getMcode9() {
      return mcode9;
   }


   public void reset() {
      gcode0  = 0;
      gcode1  = 0;
      gcode2  = 0;
      gcode3  = 0;
      gcode4  = 0;
      gcode5  = 0;
      gcode6  = 0;
      gcode7  = 0;
      gcode8  = 0;
      gcode9  = 0;
      gcode10 = 0;
      gcode11 = 0;
      gcode12 = 0;
      gcode13 = 0;
      gcode14 = 0;
      gcode15 = 0;
      mcode0  = 0;
      mcode1  = 0;
      mcode2  = 0;
      mcode3  = 0;
      mcode4  = 0;
      mcode5  = 0;
      mcode6  = 0;
      mcode7  = 0;
      mcode8  = 0;
      mcode9  = 0;
   }


   public void setGcode0(int gcode0) {
      int ov = this.gcode0;
      firePropertyChange(new PropertyChangeEvent(this, GCode0, ov, gcode0));
      this.gcode0 = gcode0;
   }


   public void setGcode1(int gcode1) {
      int ov = this.gcode1;
      firePropertyChange(new PropertyChangeEvent(this, GCode1, ov, gcode1));
      this.gcode1 = gcode1;
   }


   public void setGcode10(int gcode10) {
      int ov = this.gcode10;
      firePropertyChange(new PropertyChangeEvent(this, GCode10, ov, gcode10));
      this.gcode10 = gcode10;
   }


   public void setGcode11(int gcode11) {
      int ov = this.gcode11;
      firePropertyChange(new PropertyChangeEvent(this, GCode11, ov, gcode11));
      this.gcode11 = gcode11;
   }


   public void setGcode12(int gcode12) {
      int ov = this.gcode12;
      firePropertyChange(new PropertyChangeEvent(this, GCode12, ov, gcode12));
      this.gcode12 = gcode12;
   }


   public void setGcode13(int gcode13) {
      int ov = this.gcode13;
      firePropertyChange(new PropertyChangeEvent(this, GCode13, ov, gcode13));
      this.gcode13 = gcode13;
   }


   public void setGcode14(int gcode14) {
      int ov = this.gcode14;
      firePropertyChange(new PropertyChangeEvent(this, GCode14, ov, gcode14));
      this.gcode14 = gcode14;
   }


   public void setGcode15(int gcode15) {
      int ov = this.gcode15;
      firePropertyChange(new PropertyChangeEvent(this, GCode15, ov, gcode15));
      this.gcode15 = gcode15;
   }


   public void setGcode2(int gcode2) {
      int ov = this.gcode2;
      firePropertyChange(new PropertyChangeEvent(this, GCode2, ov, gcode2));
      this.gcode2 = gcode2;
   }


   public void setGcode3(int gcode3) {
      int ov = this.gcode3;
      firePropertyChange(new PropertyChangeEvent(this, GCode3, ov, gcode3));
      this.gcode3 = gcode3;
   }


   public void setGcode4(int gcode4) {
      int ov = this.gcode4;
      firePropertyChange(new PropertyChangeEvent(this, GCode4, ov, gcode4));
      this.gcode4 = gcode4;
   }


   public void setGcode5(int gcode5) {
      int ov = this.gcode5;
      firePropertyChange(new PropertyChangeEvent(this, GCode5, ov, gcode5));
      this.gcode5 = gcode5;
   }


   public void setGcode6(int gcode6) {
      int ov = this.gcode6;
      firePropertyChange(new PropertyChangeEvent(this, GCode6, ov, gcode6));
      this.gcode6 = gcode6;
   }


   public void setGcode7(int gcode7) {
      int ov = this.gcode7;
      firePropertyChange(new PropertyChangeEvent(this, GCode7, ov, gcode7));
      this.gcode7 = gcode7;
   }


   public void setGcode8(int gcode8) {
      int ov = this.gcode8;
      firePropertyChange(new PropertyChangeEvent(this, GCode8, ov, gcode8));
      this.gcode8 = gcode8;
   }


   public void setGcode9(int gcode9) {
      int ov = this.gcode9;
      firePropertyChange(new PropertyChangeEvent(this, GCode9, ov, gcode9));
      this.gcode9 = gcode9;
   }


   public void setMcode0(int mcode0) {
      int ov = this.mcode0;
      firePropertyChange(new PropertyChangeEvent(this, MCode0, ov, mcode0));
      this.mcode0 = mcode0;
   }


   public void setMcode1(int mcode1) {
      int ov = this.mcode1;
      firePropertyChange(new PropertyChangeEvent(this, MCode1, ov, mcode1));
      this.mcode1 = mcode1;
   }


   public void setMcode2(int mcode2) {
      int ov = this.mcode2;
      firePropertyChange(new PropertyChangeEvent(this, MCode2, ov, mcode2));
      this.mcode2 = mcode2;
   }


   public void setMcode3(int mcode3) {
      int ov = this.mcode3;
      firePropertyChange(new PropertyChangeEvent(this, MCode3, ov, mcode3));
      this.mcode3 = mcode3;
   }


   public void setMcode4(int mcode4) {
      int ov = this.mcode4;
      firePropertyChange(new PropertyChangeEvent(this, MCode4, ov, mcode4));
      this.mcode4 = mcode4;
   }


   public void setMcode5(int mcode5) {
      int ov = this.mcode5;
      firePropertyChange(new PropertyChangeEvent(this, MCode5, ov, mcode5));
      this.mcode5 = mcode5;
   }


   public void setMcode6(int mcode6) {
      int ov = this.mcode6;
      firePropertyChange(new PropertyChangeEvent(this, MCode6, ov, mcode6));
      this.mcode6 = mcode6;
   }


   public void setMcode7(int mcode7) {
      int ov = this.mcode7;
      firePropertyChange(new PropertyChangeEvent(this, MCode7, ov, mcode7));
      this.mcode7 = mcode7;
   }


   public void setMcode8(int mcode8) {
      int ov = this.mcode8;
      firePropertyChange(new PropertyChangeEvent(this, MCode8, ov, mcode8));
      this.mcode8 = mcode8;
   }


   public void setMcode9(int mcode9) {
      int ov = this.mcode9;
      firePropertyChange(new PropertyChangeEvent(this, MCode9, ov, mcode9));
      this.mcode9 = mcode9;
   }


   @Override
   public String toString() {
      return "no active Codes :(";
   }


   private int                gcode0;
   private int                gcode1;
   private int                gcode2;
   private int                gcode3;
   private int                gcode4;
   private int                gcode5;
   private int                gcode6;
   private int                gcode7;
   private int                gcode8;
   private int                gcode9;
   private int                gcode10;
   private int                gcode11;
   private int                gcode12;
   private int                gcode13;
   private int                gcode14;
   private int                gcode15;
   private int                mcode0;
   private int                mcode1;
   private int                mcode2;
   private int                mcode3;
   private int                mcode4;
   private int                mcode5;
   private int                mcode6;
   private int                mcode7;
   private int                mcode8;
   private int                mcode9;
   public static final String GCode0  = "gcode0";
   public static final String GCode1  = "gcode1";
   public static final String GCode2  = "gcode2";
   public static final String GCode3  = "gcode3";
   public static final String GCode4  = "gcode4";
   public static final String GCode5  = "gcode5";
   public static final String GCode6  = "gcode6";
   public static final String GCode7  = "gcode7";
   public static final String GCode8  = "gcode8";
   public static final String GCode9  = "gcode9";
   public static final String GCode10 = "gcode10";
   public static final String GCode11 = "gcode11";
   public static final String GCode12 = "gcode12";
   public static final String GCode13 = "gcode13";
   public static final String GCode14 = "gcode14";
   public static final String GCode15 = "gcode15";
   public static final String MCode0  = "mcode0";
   public static final String MCode1  = "mcode1";
   public static final String MCode2  = "mcode2";
   public static final String MCode3  = "mcode3";
   public static final String MCode4  = "mcode4";
   public static final String MCode5  = "mcode5";
   public static final String MCode6  = "mcode6";
   public static final String MCode7  = "mcode7";
   public static final String MCode8  = "mcode8";
   public static final String MCode9  = "mcode9";
}
