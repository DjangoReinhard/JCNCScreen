package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       FixturePane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    18.4.2020 by Django Reinhard
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


import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.Fixtures;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.widgets.FixtureEditor;


public class FixturePane extends JPanel {
   public FixturePane(AppSetup setup, CommandWriter cw) {
      this.setup = setup;
      GridLayout l = new GridLayout(0, 3);

      l.setHgap(30);
      l.setVgap(30);
      setLayout(l);
      setBackground(UITheme.getColor("Fixture:grid.color"));
      addComponents(this, cw);
      setPreferredSize(new Dimension(1193, 1074));
   }


   public void addComponents(JPanel p, CommandWriter cw) {
      Fixtures f = setup.getFixtures();

      add(new FixtureEditor(setup, cw, " Offset ", f.getCommonOffset()));
      add(new FixtureEditor(setup, cw, " G54 ", f.getG54Offset()));
      add(new FixtureEditor(setup, cw, " G55 ", f.getG55Offset()));
      add(new FixtureEditor(setup, cw, " G56 ", f.getG56Offset()));
      add(new FixtureEditor(setup, cw, " G57 ", f.getG57Offset()));
      add(new FixtureEditor(setup, cw, " G58 ", f.getG58Offset()));
      add(new FixtureEditor(setup, cw, " G59 ", f.getG59Offset()));
      add(new FixtureEditor(setup, cw, " G59.1 ", f.getG591Offset()));
      add(new FixtureEditor(setup, cw, " G59.2 ", f.getG592Offset()));
   }


   private AppSetup          setup;
   private static final long serialVersionUID = 1L;
}
