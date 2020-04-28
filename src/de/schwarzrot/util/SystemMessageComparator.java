package de.schwarzrot.util;
/* 
 * **************************************************************************
 * 
 *  file:       SystemMessageComparator.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    26.10.2019 by Django Reinhard
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

import java.util.Comparator;

import de.schwarzrot.system.SystemMessage;

public class SystemMessageComparator implements Comparator<SystemMessage> {
   @Override
   public int compare(SystemMessage m1, SystemMessage m2) {
      if (m1.getTime().getTime() == m2.getTime().getTime()) {
         return m1.getType().compareTo(m2.getType());
      } 
      return m1.getTime().compareTo(m2.getTime());
   }
}
