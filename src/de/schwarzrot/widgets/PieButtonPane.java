package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       PieButtonPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    3.5.2020 by Django Reinhard
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
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.PieLayout;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.system.MachineControl;


public class PieButtonPane extends AbstractShapedPanel {
   public PieButtonPane(Character axisLetter, int w, int h, ICondition condEnable) {
      this(axisLetter, w, h, condEnable, false);
   }


   public PieButtonPane(Character axisLetter, int w, int h, ICondition condEnable, boolean upSideDown) {
      Dimension s = new Dimension(w, h);

      setPreferredSize(s);
      posText = String.format("%C+", axisLetter);
      negText = String.format("%C-", axisLetter);

      setOpaque(true);
      setBackground(UITheme.getColor("JogButtonPane:background"));
      setLayout(new PieLayout(new double[] { 17, 27, 45 }, upSideDown));
      createComponents(this, condEnable);
   }


   protected void createComponents(JPanel p, ICondition condEnable) {
      JButton       bt = new PieButton(posText, condEnable, 1);
      MouseListener ml = MachineControl.getInstance();

      bt.addMouseListener(ml);
      p.add(bt, 1);

      bt = new PieButton(posText, condEnable, 2);
      bt.addMouseListener(ml);
      p.add(bt, 2);

      bt = new PieButton(posText, condEnable, 3);
      bt.addMouseListener(ml);
      p.add(bt, 3);

      bt = new PieButton(negText, condEnable, 1);
      bt.addMouseListener(ml);
      p.add(bt, -1);

      bt = new PieButton(negText, condEnable, 2);
      bt.addMouseListener(ml);
      p.add(bt, -2);

      bt = new PieButton(negText, condEnable, 3);
      bt.addMouseListener(ml);
      p.add(bt, -3);
   }


   private String            posText;
   private String            negText;
   private static final long serialVersionUID = 1L;
}
