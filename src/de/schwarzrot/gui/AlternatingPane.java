package de.schwarzrot.gui;
/* 
 * **************************************************************************
 * 
 *  file:       AlternatingPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    14.4.2020 by Django Reinhard
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


import java.awt.CardLayout;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.schwarzrot.model.ValueModel;
import de.schwarzrot.widgets.SoftkeyButton;


public class AlternatingPane extends JPanel implements HierarchyListener, PropertyChangeListener {
   public AlternatingPane(ValueModel<Boolean> model, SoftkeyButton btActive, SoftkeyButton btInactive) {
      super(new CardLayout());
      PropertyKey = model.getName();
      toggleState = model;
      toggleState.addPropertyChangeListener(this);
      paneStack = (CardLayout) getLayout();
      super.add(btActive, True);
      super.add(btInactive, False);
      btActive.addHierarchyListener(this);
      btInactive.addHierarchyListener(this);
      paneStack.show(this, toggleState.getValue() ? True : False);
   }


   @Override
   public void hierarchyChanged(HierarchyEvent e) {
      JComponent component = (JComponent) e.getSource();

      if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0 && component.isShowing()) {
         // System.out.println("hierarchy changed to: " + component);
         component.transferFocus();
      }
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if (PropertyKey.compareTo(evt.getPropertyName()) == 0) {
         System.err.println("property of toggleState changed: " + evt.getNewValue());

         paneStack.show(this, (boolean) evt.getNewValue() ? True : False);
      }
   }


   private CardLayout          paneStack;
   private ValueModel<Boolean> toggleState;
   private final String        PropertyKey;
   private final String        True             = "True";
   private final String        False            = "False";
   private static final long   serialVersionUID = 1L;
}
