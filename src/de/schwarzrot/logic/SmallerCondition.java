package de.schwarzrot.logic;
/* 
 * **************************************************************************
 * 
 *  file:       SmallerCondition.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    10.10.2019 by Django Reinhard
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

import de.schwarzrot.model.ValueModel;


public class SmallerCondition<T extends Comparable<T>> implements ICondition {
   public SmallerCondition(ValueModel<T> model, T value) {
      this.model = model;
      this.value = value;
   }


   @Override
   public void addPropertyChangeListener(PropertyChangeListener pcl) {
      model.addPropertyChangeListener(pcl);
   }


   @Override
   public boolean eval() {
      if (model.getValue() == null) return true;
      if (value == null) return false;
      int rv = model.getValue().compareTo(value);

      return rv < 0;
   }

   private ValueModel<T> model;
   private T             value;
}
