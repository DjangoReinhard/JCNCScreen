package de.schwarzrot.model;
/*
 * **************************************************************************
 *
 *  file:       ValueModel.java
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


import java.beans.PropertyChangeEvent;


public class ValueModel<T extends Comparable<T>> extends AbstractModel {
   public ValueModel(String name, T initialValue) {
      this.name  = name;
      this.value = initialValue;
   }


   public String getName() {
      return name;
   }


   public T getValue() {
      return value;
   }


   public void setValue(T value) {
      T ov = this.value;
      this.value = value;

      if (ov.compareTo(this.value) != 0) {
         //         if ("applicationMode".compareTo(name) == 0) {
         //            System.out.println("old-value: " + ov);
         //            System.out.println("new-value: " + value);
         //            Thread.dumpStack();
         //         }
         firePropertyChange(new PropertyChangeEvent(this, this.name, ov, this.value));
      }
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("ValueModel(\"");

      sb.append(name);
      sb.append("\") == ");
      sb.append(value);

      return sb.toString();
   }


   private final String name;
   private T            value;
}
