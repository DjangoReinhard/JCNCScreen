package de.schwarzrot.model;
/*
 * **************************************************************************
 *
 *  file:       AbstractModel.java
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractModel {
   protected AbstractModel() {
      listeners = new ArrayList<PropertyChangeListener>();
   }


   public void addPropertyChangeListener(PropertyChangeListener listener) {
      listeners.add(listener);
   }


   public void firePropertyChange(PropertyChangeEvent evt) {
      //      SwingUtilities.invokeLater(new Runnable() {
      //         public void run() {
      propagateEvent(evt);
      //         }
      //      });
   }


   public Object getPropertyValue(String propertyName) {
      StringBuilder sb          = new StringBuilder(GetterPrefix);
      Object        returnValue = null;

      sb.append(propertyName.substring(0, 1).toUpperCase());
      sb.append(propertyName.substring(1));

      try {
         Method getter = getClass().getDeclaredMethod(sb.toString(), (Class<?>[]) null);

         returnValue = getter.invoke(this, (Object[]) null);
      } catch (NoSuchMethodException | SecurityException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }
      return returnValue;
   }


   public void removePropertyChangeListener(PropertyChangeListener listener) {
      if (listeners.contains(listener)) {
         listeners.remove(listener);
      }
   }


   protected void propagateEvent(PropertyChangeEvent evt) {
      if (listeners.size() > 0) {
         for (PropertyChangeListener pcl : listeners) {
            pcl.propertyChange(evt);
         }
      }
   }


   private List<PropertyChangeListener> listeners;
   private static final String          GetterPrefix = "get";
}
