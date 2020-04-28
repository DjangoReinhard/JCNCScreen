package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       SimpleFileFormat.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    1.11.2019 by Django Reinhard
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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.util.SimpleFile;


public class SimpleFileFormat implements AdvancedTableFormat<SimpleFile> {
   public SimpleFileFormat() {
      sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      nf  = NumberFormat.getIntegerInstance();
      nf.setGroupingUsed(true);
      nf.setMaximumIntegerDigits(12);
   }


   @SuppressWarnings("rawtypes")
   @Override
   public Class getColumnClass(int c) {
      if (c == 1) { return Long.class; }
      return String.class;
   }


   @SuppressWarnings("rawtypes")
   @Override
   public Comparator getColumnComparator(int arg0) {
      return null;
   }


   @Override
   public int getColumnCount() {
      return 3;
   }


   @Override
   public String getColumnName(int c) {
      switch (c) {
      case 0:
         return LCStatus.getStatus().lm("modified");
      case 1:
         return LCStatus.getStatus().lm("size");
      case 2:
         return LCStatus.getStatus().lm("name");
      default:
         throw new IllegalArgumentException("unknown column #" + c);
      }
   }


   @Override
   public Object getColumnValue(SimpleFile f, int c) {
      switch (c) {
      case 0:
         return sdf.format(new Date(f.lastModified()));
      case 1:
         return f.length();
      case 2:
         return f.getName();
      default:
         throw new IllegalArgumentException("unknown column #" + c);
      }
   }

   private final SimpleDateFormat sdf;
   private final NumberFormat     nf;
}
