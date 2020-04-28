package de.schwarzrot.bean;
/* 
 * **************************************************************************
 * 
 *  file:       Fixture.java
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


import de.schwarzrot.model.CanonPosition;


public class Fixture {
   public Fixture(String name, CanonPosition pos) {
      this.name = name;
      this.pos  = pos;
      rotation  = pos.getRotation();
   }


   public double getA() {
      return pos.getA();
   }


   public double getB() {
      return pos.getB();
   }


   public double getC() {
      return pos.getC();
   }


   public String getName() {
      return name;
   }


   public double getRotation() {
      return rotation;
   }


   public double getU() {
      return pos.getU();
   }


   public double getV() {
      return pos.getV();
   }


   public double getW() {
      return pos.getW();
   }


   public double getX() {
      return pos.getX();
   }


   public double getY() {
      return pos.getY();
   }


   public double getZ() {
      return pos.getZ();
   }


   public void setA(double a) {
      pos.setA(a);
   }


   public void setB(double b) {
      pos.setB(b);
   }


   public void setC(double c) {
      pos.setC(c);
   }


   public void setName(String name) {
      this.name = name;
   }


   public void setRotation(double rotation) {
      this.rotation = rotation;
   }


   public void setU(double u) {
      pos.setU(u);
   }


   public void setV(double v) {
      pos.setV(v);
   }


   public void setW(double w) {
      pos.setW(w);
   }


   public void setX(double x) {
      pos.setX(x);
   }


   public void setY(double y) {
      pos.setY(y);
   }


   public void setZ(double z) {
      pos.setZ(z);
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(name);

      sb.append(": ");
      sb.append(pos.getX());
      sb.append("/");
      sb.append(pos.getY());
      sb.append("/");
      sb.append(pos.getZ());
      sb.append("-(");
      sb.append(pos.getA());
      sb.append("/");
      sb.append(pos.getB());
      sb.append("/");
      sb.append(pos.getC());
      sb.append(")-");
      sb.append(pos.getU());
      sb.append("/");
      sb.append(pos.getV());
      sb.append("/");
      sb.append(pos.getW());
      sb.append(" - rotation: ");
      sb.append(rotation);

      return sb.toString();
   }


   private CanonPosition pos;
   private double        rotation;
   private String        name;
}
