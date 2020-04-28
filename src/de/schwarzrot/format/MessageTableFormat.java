package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       MessageTableFormat.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    12.10.2019 by Django Reinhard
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

import java.text.DateFormat;

import ca.odell.glazedlists.gui.TableFormat;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.system.SystemMessage;


public class MessageTableFormat implements TableFormat<SystemMessage> {
   public MessageTableFormat() {
      df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
   }
   
   @Override
   public int getColumnCount() {
      return 3;
   }


   @Override
   public String getColumnName(int c) {
      switch (c) {
      case 0:
         return LCStatus.getStatus().lm("when");
      case 1:
         return LCStatus.getStatus().lm("type");
      case 2:
         return LCStatus.getStatus().lm("message");
      }
      return null;
   }


   @Override
   public Object getColumnValue(SystemMessage m, int c) {
      switch (c) {
      case 0:
         return df.format(m.getTime());
      case 1:
         return m.getType();
      case 2:
         return m.getMessage();
      }
      return null;
   }

   private DateFormat df;
}
