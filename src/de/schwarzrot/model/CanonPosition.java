package de.schwarzrot.model;
/*
 * **************************************************************************
 *
 *  file:       CanonPosition.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    1.9.2019 by Django Reinhard
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


public class CanonPosition extends AbstractModel {
   public CanonPosition(/* String prefix */) {
      // this.prefix = prefix;
      //      x = -1;
      //      y = -1;
      //      z = -1;
      //      a = -1;
      //      b = -1;
      //      c = -1;
      //      u = -1;
      //      v = -1;
      //      w = -1;
   }


   public double getA() {
      return a;
   }


   public double getB() {
      return b;
   }


   public double getC() {
      return c;
   }


   public double getRotation() {
      return rotation;
   }


   public double getU() {
      return u;
   }


   public double getV() {
      return v;
   }


   public double getW() {
      return w;
   }


   public double getX() {
      return x;
   }


   public double getY() {
      return y;
   }


   public double getZ() {
      return z;
   }


   public void setA(double a) {
      double ov = this.a;
      this.a = a;
      if (ov != a)
         firePropertyChange(new PropertyChangeEvent(this, A, ov, a));
   }


   public void setB(double b) {
      double ov = this.b;
      this.b = b;
      if (ov != b)
         firePropertyChange(new PropertyChangeEvent(this, B, ov, b));
   }


   public void setC(double c) {
      double ov = this.c;
      this.c = c;
      if (ov != c)
         firePropertyChange(new PropertyChangeEvent(this, C, ov, c));
   }


   public void setRotation(double rotation) {
      double ov = this.rotation;

      this.rotation = rotation;
      if (ov != rotation)
         firePropertyChange(new PropertyChangeEvent(this, R, ov, rotation));
   }


   public void setU(double u) {
      double ov = this.u;
      this.u = u;
      if (ov != u)
         firePropertyChange(new PropertyChangeEvent(this, U, ov, u));
   }


   public void setV(double v) {
      double ov = this.v;
      this.v = v;
      if (ov != v)
         firePropertyChange(new PropertyChangeEvent(this, V, ov, v));
   }


   public void setW(double w) {
      double ov = this.w;
      this.w = w;
      if (ov != v)
         firePropertyChange(new PropertyChangeEvent(this, W, ov, w));
   }


   public void setX(double x) {
      double ov = this.x;
      this.x = x;
      if (ov != x)
         firePropertyChange(new PropertyChangeEvent(this, X, ov, x));
   }


   public void setY(double y) {
      double ov = this.y;
      this.y = y;
      if (ov != y)
         firePropertyChange(new PropertyChangeEvent(this, Y, ov, y));
   }


   public void setZ(double z) {
      double ov = this.z;
      this.z = z;
      if (ov != z)
         firePropertyChange(new PropertyChangeEvent(this, Z, ov, z));
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append(x);
      sb.append("/");
      sb.append(y);
      sb.append("/");
      sb.append(z);
      sb.append("-(");
      sb.append(a);
      sb.append("/");
      sb.append(b);
      sb.append("/");
      sb.append(c);
      sb.append(")-");
      sb.append(u);
      sb.append("/");
      sb.append(v);
      sb.append("/");
      sb.append(w);
      sb.append(" - rotation: ");
      sb.append(rotation);

      return sb.toString();
   }


   private double             x;
   private double             y;
   private double             z;
   private double             a;
   private double             b;
   private double             c;
   private double             u;
   private double             v;
   private double             w;
   private double             rotation;
   public static final String A = "A";
   public static final String B = "B";
   public static final String C = "C";
   public static final String R = "R";
   public static final String U = "U";
   public static final String V = "V";
   public static final String W = "W";
   public static final String X = "X";
   public static final String Y = "Y";
   public static final String Z = "Z";
}
