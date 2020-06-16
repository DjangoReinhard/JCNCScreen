package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       PowerButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    28.9.2019 by Django Reinhard
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
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.nml.TaskState;


public class PowerButton extends JButton implements PropertyChangeListener {
   public PowerButton(String iconOff, String iconOff1, String iconON) {
      this.setPreferredSize(UITheme.getDimension("Toolbar:button.size"));
      this.setMinimumSize(UITheme.getDimension("Toolbar:button.size"));
      this.setMaximumSize(UITheme.getDimension("Toolbar:button.size"));
      this.setBackground(UITheme.getColor("Toolbar:grid.color"));
      this.setBorder(new EmptyBorder(1, 1, 1, 1));
      this.setFocusPainted(false);
      this.setOpaque(false);
      this.setContentAreaFilled(false);
      this.imgOff  = loadIcon(iconOff);
      this.imgOff1 = loadIcon(iconOff1);
      this.imgON   = loadIcon(iconON);
      LCStatus.getStatus().getModel(LCStatus.MN_TaskState).addPropertyChangeListener(this);
      changeIcon((TaskState) LCStatus.getStatus().getModel(LCStatus.MN_TaskState).getValue());
   }


   @Override
   public void propertyChange(PropertyChangeEvent e) {
      if (e.getPropertyName().compareTo(LCStatus.MN_TaskState) == 0) {
         changeIcon((TaskState) e.getNewValue());
      }
   }


   protected void changeIcon(TaskState state) {
      switch (state) {
         case MachineOn:
            setIcon(imgON);
            break;
         case MachineOff:
         case EStopReset:
            setIcon(imgOff1);
            break;
         default:
            setIcon(imgOff);
            break;
      }
   }


   protected ImageIcon loadIcon(String iconPath) {
      File      f    = new File(iconPath);
      ImageIcon icon = null;

      if (f.exists() && f.canRead())
         icon = new ImageIcon(iconPath);
      else {
         URL url = getClass().getClassLoader().getResource(iconPath);
         icon = new ImageIcon(url);
      }
      return icon;
   }


   private ImageIcon         imgOff;
   private ImageIcon         imgOff1;
   private ImageIcon         imgON;
   private static final long serialVersionUID = 1L;
}
