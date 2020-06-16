package de.schwarzrot.util;
/*
 * **************************************************************************
 *
 *  file:       PositionCalculator.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    7.9.2019 by Django Reinhard
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


import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.Position;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.LengthUnit;


public class PositionCalculator {
   public PositionCalculator() {
      pos = new CanonPosition();
   }


   public CanonPosition getPosition() {
      return pos;
   }


   @Override
   public String toString() {
      return pos.toString();
   }


   @SuppressWarnings("unchecked")
   public synchronized void update(CanonPosition actualPosition, CanonPosition g5x_Offset,
         CanonPosition toolOffset, CanonPosition g92_Offset, double rotationXY) {
      x = actualPosition.getX() - g5x_Offset.getX() - toolOffset.getX();
      y = actualPosition.getY() - g5x_Offset.getY() - toolOffset.getY();
      z = actualPosition.getZ() - g5x_Offset.getZ() - toolOffset.getZ();
      a = actualPosition.getA() - g5x_Offset.getA() - toolOffset.getA();
      b = actualPosition.getB() - g5x_Offset.getB() - toolOffset.getB();
      c = actualPosition.getC() - g5x_Offset.getC() - toolOffset.getC();
      u = actualPosition.getU() - g5x_Offset.getU() - toolOffset.getU();
      v = actualPosition.getV() - g5x_Offset.getV() - toolOffset.getV();
      w = actualPosition.getW() - g5x_Offset.getW() - toolOffset.getW();

      if (absPosition == null)
         absPosition = LCStatus.getStatus().getModel("absPosition");

      if (rotationXY != 0) {
         double ang = Math.toRadians(-1 * rotationXY);
         double xr  = x * Math.cos(ang) - Math.sin(ang);
         double yr  = y * Math.sin(ang) + Math.acos(ang);

         x = xr;
         y = yr;
      }
      x -= g92_Offset.getX();
      y -= g92_Offset.getY();
      z -= g92_Offset.getZ();
      a -= g92_Offset.getA();
      b -= g92_Offset.getB();
      c -= g92_Offset.getC();
      u -= g92_Offset.getU();
      v -= g92_Offset.getV();
      w -= g92_Offset.getW();

      if (unit == null)
         unit = LCStatus.getStatus().getSetup().getUnit();

      if (absPosition.getValue()) {
         pos.setX(convertUnit(actualPosition.getX()));
         pos.setY(convertUnit(actualPosition.getY()));
         pos.setZ(convertUnit(actualPosition.getZ()));
         pos.setA(convertUnit(actualPosition.getA()));
         pos.setB(convertUnit(actualPosition.getB()));
         pos.setC(convertUnit(actualPosition.getC()));
         pos.setU(convertUnit(actualPosition.getU()));
         pos.setV(convertUnit(actualPosition.getV()));
         pos.setW(convertUnit(actualPosition.getW()));
      } else {
         pos.setX(convertUnit(x));
         pos.setY(convertUnit(y));
         pos.setZ(convertUnit(z));
         pos.setA(convertUnit(a));
         pos.setB(convertUnit(b));
         pos.setC(convertUnit(c));
         pos.setU(convertUnit(u));
         pos.setV(convertUnit(v));
         pos.setW(convertUnit(w));
      }
   }


   @SuppressWarnings("unchecked")
   public synchronized void update(Position actualPosition, Position g5x_Offset, Position toolOffset,
         Position g92_Offset, double rotationXY) {
      x = actualPosition.x - g5x_Offset.x - toolOffset.x;
      y = actualPosition.y - g5x_Offset.y - toolOffset.y;
      z = actualPosition.z - g5x_Offset.z - toolOffset.z;
      a = actualPosition.a - g5x_Offset.a - toolOffset.a;
      b = actualPosition.b - g5x_Offset.b - toolOffset.b;
      c = actualPosition.c - g5x_Offset.c - toolOffset.c;
      u = actualPosition.u - g5x_Offset.u - toolOffset.u;
      v = actualPosition.v - g5x_Offset.v - toolOffset.v;
      w = actualPosition.w - g5x_Offset.w - toolOffset.w;

      if (absPosition == null)
         absPosition = LCStatus.getStatus().getModel("absPosition");

      if (rotationXY != 0) {
         double ang = Math.toRadians(-1 * rotationXY);
         double xr  = x * Math.cos(ang) - Math.sin(ang);
         double yr  = y * Math.sin(ang) + Math.acos(ang);

         x = xr;
         y = yr;
      }
      x -= g92_Offset.x;
      y -= g92_Offset.y;
      z -= g92_Offset.z;
      a -= g92_Offset.a;
      b -= g92_Offset.b;
      c -= g92_Offset.c;
      u -= g92_Offset.u;
      v -= g92_Offset.v;
      w -= g92_Offset.w;

      if (unit == null)
         unit = LCStatus.getStatus().getSetup().getUnit();

      if (absPosition.getValue()) {
         pos.setX(convertUnit(actualPosition.x));
         pos.setY(convertUnit(actualPosition.y));
         pos.setZ(convertUnit(actualPosition.z));
         pos.setA(convertUnit(actualPosition.a));
         pos.setB(convertUnit(actualPosition.b));
         pos.setC(convertUnit(actualPosition.c));
         pos.setU(convertUnit(actualPosition.u));
         pos.setV(convertUnit(actualPosition.v));
         pos.setW(convertUnit(actualPosition.w));
      } else {
         pos.setX(convertUnit(x));
         pos.setY(convertUnit(y));
         pos.setZ(convertUnit(z));
         pos.setA(convertUnit(a));
         pos.setB(convertUnit(b));
         pos.setC(convertUnit(c));
         pos.setU(convertUnit(u));
         pos.setV(convertUnit(v));
         pos.setW(convertUnit(w));
      }
   }


   protected double convertUnit(double v) {
      switch (unit) {
         case Inch:
            return v * 25.40;
         case CM:
            return v * 10;
      }
      return v;
   }


   private CanonPosition       pos;
   private ValueModel<Boolean> absPosition;
   private LengthUnit          unit;
   private double              x;
   private double              y;
   private double              z;
   private double              a;
   private double              b;
   private double              c;
   private double              u;
   private double              v;
   private double              w;
}
