package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       ToolInfoPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    29.9.2019 by Django Reinhard
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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolEntry;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.ToolInfo;
import de.schwarzrot.widgets.ToolNumberLabel;


public class ToolInfoPane extends JPanel implements PropertyChangeListener {
   public ToolInfoPane() {
      setLayout(new GridBagLayout());
      tools = LCStatus.getStatus().getSetup().getTools();
      setOpaque(true);
      setBackground(UITheme.getColor("Tool:info.grid.color"));
      addComponents2Pane();
   }


   @Override
   public void propertyChange(PropertyChangeEvent e) {
      if (ToolInfo.ActiveToolNum.compareTo(e.getPropertyName()) == 0) {
         System.out.println("active tool changed to T" + e.getNewValue());
         for (ToolEntry t : tools) {
            if (t.getToolNumber() == (int) e.getNewValue()) {
               toolNumber.setValue(t.getToolNumber());
               toolLength.setText(String.format("DL: %.3f", t.getOffset().getZ()));
               toolRadius.setText(String.format("DR: %.3f", t.getDiameter() / 2));
               toolDesc.setText(t.getDescription());
            }
         }
      } else if (ToolInfo.NextToolNum.compareTo(e.getPropertyName()) == 0) {
         System.out.println("prepare tool T" + e.getNewValue() + " for next change");
      } else if (CanonPosition.Z.compareTo(e.getPropertyName()) == 0) {
         toolLength.setText(String.format("DL: %.3f", e.getNewValue()));
      }
   }


   protected void addComponents2Pane() {
      GridBagConstraints c = new GridBagConstraints();

      c.fill       = GridBagConstraints.BOTH;
      c.insets     = new Insets(1, 1, 1, 1);
      c.weightx    = 0;
      c.weighty    = 1.0;
      c.gridx      = 0;
      c.gridy      = 0;
      c.gridheight = 3;
      toolNumber   = new ToolNumberLabel();
      LCStatus.getStatus().getToolInfo().addPropertyChangeListener(this);
      LCStatus.getStatus().getToolInfo().getOffset().addPropertyChangeListener(this);
      add(toolNumber, c);

      c.weightx    = 1.0;
      c.weighty    = 0.3;
      c.gridx      = 1;
      c.gridheight = 1;
      c.gridwidth  = 1;
      toolDesc     = new JLabel(LCStatus.getStatus().lm("description"));
      toolDesc.setOpaque(true);
      toolDesc.setFont(UITheme.getFont("Tool:desc.font"));
      toolDesc.setForeground(UITheme.getColor("Tool:desc.foreground"));
      toolDesc.setBackground(UITheme.getColor("Tool:desc.background"));
      add(toolDesc, c);

      c.gridy     = 1;
      c.gridwidth = 1;
      toolLength  = new JLabel("DL: 0");
      toolLength.setOpaque(true);
      toolLength.setFont(UITheme.getFont("Tool:info.font"));
      toolLength.setForeground(UITheme.getColor("Tool:info.foreground"));
      toolLength.setBackground(UITheme.getColor("Tool:info.background"));
      add(toolLength, c);

      c.gridy    = 2;
      toolRadius = new JLabel("DR: 0");
      toolRadius.setOpaque(true);
      toolRadius.setFont(UITheme.getFont("Tool:info.font"));
      toolRadius.setForeground(UITheme.getColor("Tool:info.foreground"));
      toolRadius.setBackground(UITheme.getColor("Tool:info.background"));
      add(toolRadius, c);
   }


   private JLabel            toolLength;
   private JLabel            toolRadius;
   private JLabel            toolDesc;
   private ToolNumberLabel   toolNumber;
   private List<ToolEntry>   tools;
   private static final long serialVersionUID = 1L;
}
