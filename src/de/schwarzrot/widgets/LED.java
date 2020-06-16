package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       LED.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.9.2019 by Django Reinhard
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


import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.ValueModel;


public class LED extends JLabel implements PropertyChangeListener {
   public LED(ValueModel<Boolean> model) {
      this.model = model;
      Dimension size = new Dimension(10, 60);

      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      offColor = UITheme.getColor(UITheme.LED_off_color);
      onColor  = UITheme.getColor(UITheme.LED_on_color);
      setOpaque(true);
      setState(this.model.getValue());
      model.addPropertyChangeListener(this);
   }


   @Override
   public void propertyChange(PropertyChangeEvent e) {
      if (e.getPropertyName().compareTo(model.getName()) == 0) {
         setState(model.getValue());
      }
   }


   public void setValue(Object value) {
      if (value instanceof Boolean) {
         setState((Boolean) value);
      } else if (value instanceof Number) {
         setState(((Number) value).intValue() != 0);
      }
   }


   protected void setState(Boolean state) {
      setBackground(state ? onColor : offColor);
   }


   private Color               offColor;
   private Color               onColor;
   private ValueModel<Boolean> model;
   private static final long   serialVersionUID = 1L;
}
