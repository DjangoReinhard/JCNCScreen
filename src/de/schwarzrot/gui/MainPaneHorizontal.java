package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       MainPaneHorizontal.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    3.11.2019 by Django Reinhard
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
import java.awt.GridBagConstraints;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.system.ErrorReader;
import de.schwarzrot.widgets.PowerButton;


public class MainPaneHorizontal extends AbstractMainPane {
   public MainPaneHorizontal(CommandWriter cmdWriter, ErrorReader errorReader) {
      super(cmdWriter, errorReader);
      createGUI();
   }


   protected void createGUI() {
      GridBagConstraints c = new GridBagConstraints();

      paneStack    = PaneStack.getInstance(cmdWriter, errorReader);
      c.gridx      = 0;
      c.gridy      = 0;
      c.gridwidth  = 1;
      c.gridheight = 1;
      c.weightx    = 0;
      c.weighty    = 0.5;
      c.fill       = GridBagConstraints.VERTICAL;
      add(new PositionPane(), c);

      c.gridx      = 0;
      c.gridy      = 1;
      c.gridwidth  = 1;
      c.gridheight = 1;
      c.weightx    = 0;
      c.weighty    = 0.3;
      c.fill       = GridBagConstraints.BOTH;
      add(createInfoPart(), c);

      c.gridx      = 0;
      c.gridy      = 2;
      c.gridwidth  = 1;
      c.gridheight = 1;
      c.weightx    = 0;
      c.weighty    = 0;
      c.fill       = GridBagConstraints.HORIZONTAL;
      add(createMessagePane(), c);

      c.gridx      = 1;
      c.gridy      = 0;
      c.gridwidth  = 1;
      c.gridheight = 3;
      c.weightx    = 1;
      c.weighty    = 1;
      c.fill       = GridBagConstraints.BOTH;
      add(paneStack, c);

      c.gridx      = 0;
      c.gridy      = 3;
      c.gridwidth  = 2;
      c.gridheight = 1;
      c.weightx    = 1;
      c.weighty    = 0;
      c.fill       = GridBagConstraints.HORIZONTAL;
      add(createMainToolbar(), c);

      // c.gridx = 3;
      // c.gridy = 2;
      // c.gridwidth = 1;
      // c.gridheight = 1;
      // c.weightx = 0.2;
      // c.weighty = 0;
      // c.fill = GridBagConstraints.HORIZONTAL;
      // add(createAdditionalToolbar(settings, BoxLayout.X_AXIS), c);
   }


   @Override
   protected JComponent createMainToolbar() {
      JPanel buttonPane = new JPanel();

      buttonPane.setOpaque(true);
      buttonPane.setBackground(UITheme.getColor(UITheme.Toolbar_grid_color));
      buttonPane.setForeground(Color.RED);
      buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
      createToolbar1(buttonPane);
      createToolbar3(buttonPane);
      createToolbar2(buttonPane);
      PowerButton pb = new PowerButton("images/SK_PowerOff.png", "images/SK_PowerOff_1.png",
            "images/SK_PowerOn.png");
      pb.addActionListener(powerHandler);
      buttonPane.add(pb);

      return buttonPane;
   }


   private static final long serialVersionUID = 1L;
}
