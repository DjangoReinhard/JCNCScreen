package de.schwarzrot.util;
/* 
 * **************************************************************************
 * 
 *  file:       BindUtils.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    7.9.2019 by Django Reinhard
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

import java.util.ArrayList;
import java.util.List;

import de.schwarzrot.model.AbstractModel;
import de.schwarzrot.model.IValueClient;
import de.schwarzrot.model.ValueObserver;

public class BindUtils {
   public static void bind(String propertyName, AbstractModel model, IValueClient client) {
      ValueObserver vo = new ValueObserver(propertyName, client);
      Object curValue = model.getPropertyValue(propertyName);
      
//      System.out.println("BindUtils(" + propertyName + ") - got value: " + curValue);
      model.addPropertyChangeListener(vo);
      client.setValue(curValue);
      observers.add(vo);
   }
   
   private static List<ValueObserver> observers;
   static {
      observers = new ArrayList<ValueObserver>();
   }
}
