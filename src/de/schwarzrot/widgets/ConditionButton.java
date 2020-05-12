package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       ConditionButton.java
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


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import de.schwarzrot.logic.ICondition;


public class ConditionButton extends JButton implements PropertyChangeListener {
   public ConditionButton(String label, ICondition condEnable) {
      super(label);
      this.condEnable = condEnable;
      this.condEnable.addPropertyChangeListener(this);
      this.setFocusPainted(false);
      this.setOpaque(false);
      this.setContentAreaFilled(false);
      checkState();
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      checkState();
   }


   protected void checkState() {
      setEnabled(condEnable.eval());
   }


   private final ICondition  condEnable;
   private static final long serialVersionUID = 1L;
}
