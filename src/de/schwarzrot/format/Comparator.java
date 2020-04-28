package de.schwarzrot.format;
/* 
 * **************************************************************************
 * 
 *  file:       Comparator.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    18.4.2020 by Django Reinhard
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

public class Comparator<T extends Comparable<T>> implements java.util.Comparator<T> {
   @Override
   public int compare(T a, T b) {
      if (a == null && b != null)
         return -1;
      if (a != null && b == null)
         return 1;
      if (a == null && b == null)
         return 0;
      return a.compareTo(b);
   }

}
