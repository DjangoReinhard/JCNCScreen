package de.schwarzrot.model;
/* 
 * **************************************************************************
 * 
 *  file:       ValueObserver.java
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;


public class ValueObserver implements PropertyChangeListener {
   public ValueObserver(String propertyName, IValueClient client) {
      this.propertyName = propertyName;
      this.valueClient  = client;
   }


   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().compareTo(propertyName) == 0) {
         if (((Comparable) evt.getOldValue()).compareTo(evt.getNewValue()) != 0) {
            try {
               SwingUtilities.invokeAndWait(new Runnable() {
                  public void run() {
                     valueClient.setValue(evt.getNewValue());
                  }
               });
            }
            catch (Throwable t) {}
         }
      }
   }

   private final String       propertyName;
   private final IValueClient valueClient;
}
