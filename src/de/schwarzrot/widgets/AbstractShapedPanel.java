package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       AbstractShapedPanel.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    9.5.2020 by Django Reinhard
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


import java.awt.Component;

import javax.swing.JPanel;


/**
 * just a class for cleaner application code. I want to use index values in
 * layout manager, but not overload add() method in JPanel subclass, that adds
 * the components.
 * 
 * @author django
 */
public abstract class AbstractShapedPanel extends JPanel {
   @Override
   public Component add(Component c, int sequence) {
      // have to overload this call, as I want the sequence
      // reach layout manager
      addImpl(c, sequence, -1);
      return c;
   }


   private static final long serialVersionUID = 1L;
}
