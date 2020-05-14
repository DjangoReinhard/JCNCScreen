package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       CircleButtonPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    1.5.2020 by Django Reinhard
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
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.CircleLayout;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.system.MachineControl;


public class CircleButtonPane extends AbstractShapedPanel {
   public CircleButtonPane(int w, int h, ICondition condEnableCenter, ICondition condEnable) {
      setLayout(new CircleLayout(new double[] { -45.0, 45.0, 135.0, 225.0 }));
      setPreferredSize(new Dimension(w, h));
      setOpaque(true);
      setBackground(UITheme.getColor("JogButtonPane:background"));
      createComponents(this, condEnableCenter, condEnable);
   }


   protected void createComponents(JPanel p, ICondition condEnableCenter, ICondition condEnable) {
      JButton        bt = new RoundButton("HomeAll", condEnableCenter);
      ActionListener al = MachineControl.getInstance();
      MouseListener  ml = MachineControl.getInstance();

      bt.addActionListener(al);
      add(bt);

      bt = new CircleButton("X+", condEnable, 3);
      bt.addMouseListener(ml);
      add(bt, 1);

      bt = new CircleButton("Y+", condEnable, 3);
      bt.addMouseListener(ml);
      add(bt, 2);

      bt = new CircleButton("X-", condEnable, 3);
      bt.addMouseListener(ml);
      add(bt, 3);

      bt = new CircleButton("Y-", condEnable, 3);
      bt.addMouseListener(ml);
      add(bt, 4);

      bt = new CircleButton("X+", condEnable, 2);
      bt.addMouseListener(ml);
      add(bt, 1);

      bt = new CircleButton("Y+", condEnable, 2);
      bt.addMouseListener(ml);
      add(bt, 2);

      bt = new CircleButton("X-", condEnable, 2);
      bt.addMouseListener(ml);
      add(bt, 3);

      bt = new CircleButton("Y-", condEnable, 2);
      bt.addMouseListener(ml);
      add(bt, 4);

      bt = new CircleButton("X+", condEnable, 1);
      bt.addMouseListener(ml);
      add(bt, 1);

      bt = new CircleButton("Y+", condEnable, 1);
      bt.addMouseListener(ml);
      add(bt, 2);

      bt = new CircleButton("X-", condEnable, 1);
      bt.addMouseListener(ml);
      add(bt, 3);

      bt = new CircleButton("Y-", condEnable, 1);
      bt.addMouseListener(ml);
      add(bt, 4);
   }


   private static final long serialVersionUID = 1L;
}
