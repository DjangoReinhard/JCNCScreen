package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       GCodeTableFormat.java
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

import ca.odell.glazedlists.gui.TableFormat;
import de.schwarzrot.bean.GCodeLine;


public class GCodeTableFormat implements TableFormat<GCodeLine> {

   @Override
   public int getColumnCount() {
      return 2;
   }

   // don't return column names, as we don't want headers to be displayed
   @Override
   public String getColumnName(int arg0) {
      return null;
   }

   @Override
   public Object getColumnValue(GCodeLine line, int column) {
      if (column == 0) return line.getLineNumber();
      else if (column == 1) return line.getLine();
      throw new IllegalStateException("wrong/unsupported column id! " + column);
   }
}
