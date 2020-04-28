package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       ToolDefTableFormat.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    10.11.2019 by Django Reinhard
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
import de.schwarzrot.bean.ToolDefinition;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;


public class ToolDefTableFormat
      implements AdvancedTableFormat<ToolDefinition>, WritableTableFormat<ToolDefinition> {
   public ToolDefTableFormat() {
      booleanComparator = new Comparator<Boolean>();
      integerComparator = new Comparator<Integer>();
      doubleComparator  = new Comparator<Double>();
      stringComparator  = new Comparator<String>();
   }


   @Override
   public Class getColumnClass(int c) {
      switch (c) {
         case 0:
            return Boolean.class;
         case 1:
            return Integer.class;
         case 2:
            return String.class;
         case 3:
            return int.class;
         case 4:
            return double.class;
         case 5:
            return double.class;
      }
      return Object.class;
   }


   @Override
   public java.util.Comparator getColumnComparator(int c) {
      switch (c) {
         case 0:
            return booleanComparator;
         case 1:
            return integerComparator;
         case 2:
            return stringComparator;
         case 3:
            return integerComparator;
         case 4:
            return doubleComparator;
         case 5:
            return doubleComparator;
      }
      return null;
   }


   @Override
   public int getColumnCount() {
      return 6;
   }


   @Override
   public String getColumnName(int col) {
      switch (col) {
         case 0:
            return "";
         case 1:
            return LCStatus.getStatus().lm("Htool");
         case 2:
            return LCStatus.getStatus().lm("name");
         case 3:
            return LCStatus.getStatus().lm("Hflutes");
         case 4:
            // return LCStatus.getStatus().lm("flutelength");
            return LCStatus.getStatus().lm("length");
         case 5:
            // return LCStatus.getStatus().lm("flutediameter");
            return LCStatus.getStatus().lm("diameter");
      }
      return null;
   }


   @Override
   public Object getColumnValue(ToolDefinition tool, int col) {
      switch (col) {
         case 0:
            return tool.isSelected();
         case 1:
            return tool.getToolNumber();
         case 2:
            return tool.getToolName();
         case 3:
            return tool.getFlutes();
         case 4:
            return tool.getFluteLength();
         case 5:
            return tool.getFluteDiameter();
      }
      return null;
   }


   @Override
   public boolean isEditable(ToolDefinition tool, int col) {
      // if (col > 1) return true;
      return false;
   }


   @Override
   public ToolDefinition setColumnValue(ToolDefinition tool, Object newValue, int col) {
      System.out.println("setColumValue(T" + tool.getToolNumber() + " nv: " + newValue + " col: " + col);

      // switch (col) {
      // case 0: {
      // int v = tool.getToolNumber();
      //
      // try {
      // v = Integer.parseInt((String) newValue);
      // tool.setToolNumber(v);
      // }
      // catch (Throwable t) {}
      // }
      // break;
      // case 1: {
      // int v = tool.getPocketUsed();
      //
      // try {
      // v = Integer.parseInt((String) newValue);
      // tool.setPocketUsed(v);
      // }
      // catch (Throwable t) {}
      // }
      // break;
      // case 2: {
      // if (newValue instanceof String) {
      // double v = tool.getOffset().getZ();
      //
      // try {
      // v = Double.parseDouble((String) newValue);
      // tool.getOffset().setZ(v);
      // tool.setModified(true);
      // }
      // catch (Throwable t) {
      // t.printStackTrace();
      // }
      // } else {
      // tool.getOffset().setZ((double) newValue);
      // tool.setModified(true);
      // }
      // }
      // break;
      // case 3: {
      // double v = tool.getDiameter();
      //
      // try {
      // v = Double.parseDouble((String) newValue);
      // tool.setDiameter(v);
      // tool.setModified(true);
      // }
      // catch (Throwable t) {}
      // }
      // break;
      // case 4:
      // tool.setDescription((String) newValue);
      // tool.setModified(true);
      // break;
      // }
      return tool;
   }


   private java.util.Comparator<Boolean> booleanComparator;
   private java.util.Comparator<Integer> integerComparator;
   private java.util.Comparator<Double>  doubleComparator;
   private java.util.Comparator<String>  stringComparator;
}
