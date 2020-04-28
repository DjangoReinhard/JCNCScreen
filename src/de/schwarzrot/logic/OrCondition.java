package de.schwarzrot.logic;
/* 
 * **************************************************************************
 * 
 *  file:       OrCondition.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    5.10.2019 by Django Reinhard
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

import java.beans.PropertyChangeListener;


public class OrCondition implements ICondition {
   public OrCondition(ICondition c0, ICondition c1) {
      this(new ICondition[] { c0, c1 });
   }


   public OrCondition(ICondition[] c) {
      this.conds = c;
   }


   public void addPropertyChangeListener(PropertyChangeListener pcl) {
      for (ICondition c : conds) {
         c.addPropertyChangeListener(pcl);
      }
   }


   public boolean eval() {
      for (ICondition c : conds) {
         if (c.eval()) { return true; }
      }
      return false;
   }

   private ICondition[] conds;
}
