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
      double x = actualPosition.getX() - g5x_Offset.getX() - toolOffset.getX();
      double y = actualPosition.getY() - g5x_Offset.getY() - toolOffset.getY();
      double z = actualPosition.getZ() - g5x_Offset.getZ() - toolOffset.getZ();
      double a = actualPosition.getA() - g5x_Offset.getA() - toolOffset.getA();
      double b = actualPosition.getB() - g5x_Offset.getB() - toolOffset.getB();
      double c = actualPosition.getC() - g5x_Offset.getC() - toolOffset.getC();
      double u = actualPosition.getU() - g5x_Offset.getU() - toolOffset.getU();
      double v = actualPosition.getV() - g5x_Offset.getV() - toolOffset.getV();
      double w = actualPosition.getW() - g5x_Offset.getW() - toolOffset.getW();

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
}
