package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       ToolTableFormat.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    28.9.2019 by Django Reinhard
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
import de.schwarzrot.bean.ToolEntry;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;


public class ToolTableFormat implements AdvancedTableFormat<ToolEntry>, WritableTableFormat<ToolEntry> {
   public ToolTableFormat() {
      integerComparator = new Comparator<Integer>();
      doubleComparator  = new Comparator<Double>();
      stringComparator  = new Comparator<String>();
   }


   @Override
   public Class getColumnClass(int c) {
      switch (c) {
         case 0:
            return Integer.class;
         case 1:
            return Integer.class;
         case 2:
            return Double.class;
         case 3:
            return Double.class;
         case 4:
            return String.class;
      }
      return Object.class;
   }


   @Override
   public java.util.Comparator getColumnComparator(int c) {
      switch (c) {
         case 0:
         case 1:
            return integerComparator;
         case 2:
         case 3:
            return doubleComparator;
         case 4:
            return stringComparator;
      }
      return null;
   }


   @Override
   public int getColumnCount() {
      return 5;
   }


   @Override
   public String getColumnName(int col) {
      switch (col) {
         case 0:
            return LCStatus.getStatus().lm("Stool");
         case 1:
            return LCStatus.getStatus().lm("slot");
         case 2:
            return LCStatus.getStatus().lm("length");
         case 3:
            return LCStatus.getStatus().lm("diameter");
         case 4:
            return LCStatus.getStatus().lm("description");
      }
      return null;
   }


   @Override
   public Object getColumnValue(ToolEntry tool, int col) {
      switch (col) {
         case 0:
            return tool.getToolNumber();
         case 1:
            return tool.getPocketUsed();
         case 2:
            return tool.getOffset().getZ();
         case 3:
            return tool.getDiameter();
         case 4:
            return tool.getDescription();
      }
      return null;
   }


   @Override
   public boolean isEditable(ToolEntry tool, int col) {
      if (col > 1)
         return true;
      return false;
   }


   @Override
   public ToolEntry setColumnValue(ToolEntry tool, Object newValue, int col) {
      System.out.println("setColumValue(T" + tool.getToolNumber() + " nv: " + newValue + " col: " + col);
      switch (col) {
         case 0: {
            int v = tool.getToolNumber();

            try {
               v = Integer.parseInt((String) newValue);
               tool.setToolNumber(v);
            } catch (Throwable t) {
            }
         }
            break;
         case 1: {
            int v = tool.getPocketUsed();

            try {
               v = Integer.parseInt((String) newValue);
               tool.setPocketUsed(v);
            } catch (Throwable t) {
            }
         }
            break;
         case 2: {
            if (newValue instanceof String) {
               double v = tool.getOffset().getZ();

               try {
                  v = Double.parseDouble((String) newValue);
                  tool.getOffset().setZ(v);
                  tool.setModified(true);
               } catch (Throwable t) {
                  t.printStackTrace();
               }
            } else {
               tool.getOffset().setZ((double) newValue);
               tool.setModified(true);
            }
         }
            break;
         case 3: {
            double v = tool.getDiameter();

            try {
               v = Double.parseDouble((String) newValue);
               tool.setDiameter(v);
               tool.setModified(true);
            } catch (Throwable t) {
            }
         }
            break;
         case 4:
            tool.setDescription((String) newValue);
            tool.setModified(true);
            break;
      }
      return tool;
   }


   private java.util.Comparator integerComparator;
   private java.util.Comparator doubleComparator;
   private java.util.Comparator stringComparator;
}
