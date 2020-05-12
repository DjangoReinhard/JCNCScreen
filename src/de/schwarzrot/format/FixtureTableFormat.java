package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       FixtureTableFormat.java
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

import de.schwarzrot.bean.Fixture;
import de.schwarzrot.bean.IAxisMask;
import de.schwarzrot.bean.LCStatus;


public class FixtureTableFormat implements
                                AdvancedTableFormat<Fixture>,
                                WritableTableFormat<Fixture> {
   public FixtureTableFormat() {
      IAxisMask setup = LCStatus.getStatus().getSetup();

      int columns = 1;
      columnHeaders    = new String[11];
      columnHeaders[0] = LCStatus.getStatus().lm("fixture");
      if (setup.hasXAxis()) {
         columnHeaders[columns++] = "X";
      }
      if (setup.hasYAxis()) {
         columnHeaders[columns++] = "Y";
      }
      if (setup.hasZAxis()) {
         columnHeaders[columns++] = "Z";
      }
      if (setup.hasAAxis()) {
         columnHeaders[columns++] = "A";
      }
      if (setup.hasBAxis()) {
         columnHeaders[columns++] = "B";
      }
      if (setup.hasCAxis()) {
         columnHeaders[columns++] = "C";
      }
      if (setup.hasUAxis()) {
         columnHeaders[columns++] = "U";
      }
      if (setup.hasVAxis()) {
         columnHeaders[columns++] = "V";
      }
      if (setup.hasWAxis()) {
         columnHeaders[columns++] = "W";
      }
      columnCount = columns;
   }


   @Override
   public Class getColumnClass(int c) {
      if (c > 0) return Double.class;
      return String.class;
   }


   @Override
   public Comparator getColumnComparator(int arg0) {
      return null;
   }


   @Override
   public int getColumnCount() {
      return columnCount;
   }


   @Override
   public String getColumnName(int c) {
      return columnHeaders[c];
   }


   @Override
   public Object getColumnValue(Fixture fixture, int n) {
      if (n == 0) {
         return fixture.getName();
      } else if (columnHeaders[n].compareTo("X") == 0) {
         return fixture.getX();
      } else if (columnHeaders[n].compareTo("Y") == 0) {
         return fixture.getY();
      } else if (columnHeaders[n].compareTo("Z") == 0) {
         return fixture.getZ();
      } else if (columnHeaders[n].compareTo("A") == 0) {
         return fixture.getA();
      } else if (columnHeaders[n].compareTo("B") == 0) {
         return fixture.getB();
      } else if (columnHeaders[n].compareTo("C") == 0) {
         return fixture.getC();
      } else if (columnHeaders[n].compareTo("U") == 0) {
         return fixture.getU();
      } else if (columnHeaders[n].compareTo("V") == 0) {
         return fixture.getV();
      } else if (columnHeaders[n].compareTo("W") == 0) {
         return fixture.getW();
      }

      return null;
   }


   @Override
   public boolean isEditable(Fixture f, int c) {
      return c > 0;
   }


   @Override
   public Fixture setColumnValue(Fixture f, Object v, int n) {
      double value = 0;

      if (v instanceof Double) value = (Double) v;
      else if (v instanceof Number) value = ((Number) v).doubleValue();
      else {
         try {
            value =
                  NumberFormat.getInstance().parse(v.toString()).doubleValue();
         }
         catch (ParseException e) {}
      }
      if (columnHeaders[n].compareTo("X") == 0) {
         f.setX(value);
      } else if (columnHeaders[n].compareTo("Y") == 0) {
         f.setY(value);
      } else if (columnHeaders[n].compareTo("Z") == 0) {
         f.setZ(value);
      } else if (columnHeaders[n].compareTo("A") == 0) {
         f.setA(value);
      } else if (columnHeaders[n].compareTo("B") == 0) {
         f.setB(value);
      } else if (columnHeaders[n].compareTo("C") == 0) {
         f.setC(value);
      } else if (columnHeaders[n].compareTo("U") == 0) {
         f.setU(value);
      } else if (columnHeaders[n].compareTo("V") == 0) {
         f.setV(value);
      } else if (columnHeaders[n].compareTo("W") == 0) {
         f.setW(value);
      }
      return f;
   }

   private final int      columnCount;
   private final String[] columnHeaders;
}
