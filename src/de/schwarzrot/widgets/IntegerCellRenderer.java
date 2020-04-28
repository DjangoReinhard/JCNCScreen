package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       IntegerCellRenderer.java
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

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


public class IntegerCellRenderer extends DefaultTableCellRenderer {
   public IntegerCellRenderer() {
      nf = NumberFormat.getIntegerInstance();
      nf.setGroupingUsed(true);
      this.setHorizontalAlignment(SwingConstants.RIGHT);
   }


   public void setValue(Object value) {
      if (value != null && value instanceof Number) {
         super.setValue(nf.format(((Number) value).longValue()));
      }
   }

   private final NumberFormat nf;
   private static final long  serialVersionUID = 1L;
}
