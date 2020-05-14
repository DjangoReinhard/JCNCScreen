package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       LinearJogPane.java
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
import java.awt.FlowLayout;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.system.MachineControl;


public class LinearJogPane extends JPanel {
   public LinearJogPane(Character axisLetter, int w, int h, ICondition condEnable) {
      this(axisLetter, w, h, condEnable, false);
   }


   public LinearJogPane(Character axisLetter, int w, int h, ICondition condEnable, boolean noCenterGap) {
      this.noCenterGap = noCenterGap;
      isVertical       = w < h;
      diameter         = (int) (0.95 * (isVertical ? h : w));
      gap              = isVertical ? h - diameter : w - diameter;
      other            = isVertical ? w : h;
      Dimension s = new Dimension(w, h);

      setPreferredSize(s);
      setMinimumSize(s);
      setOpaque(true);
      setBackground(UITheme.getColor("JogButtonPane:background"));
      FlowLayout fl = new FlowLayout();

      fl.setHgap(0);
      fl.setVgap(0);
      setLayout(fl);
      posText = String.format("%C+", axisLetter);
      negText = String.format("%C-", axisLetter);
      createComponents(this, condEnable);
   }


   protected void createComponents(JPanel p, ICondition condEnable) {
      double        r      = diameter / 2;
      double        w      = noCenterGap ? r * 0.9 : r * 0.75;
      double        w3     = w * 0.5;
      double        w2     = w3 * 0.618;
      double        w1     = w3 * 0.382;
      double        r3o    = r;
      double        r3i    = r - w3;
      double        r2o    = r3i;
      double        r2i    = r3i - w2;
      double        r1o    = r2i;
      double        r1i    = r2i - w1;
      JLabel        margin = new JLabel(" ");
      MouseListener ml     = MachineControl.getInstance();
      JButton       bt     = null;

      if (isVertical) {
         margin.setPreferredSize(new Dimension(other, gap / 2));
         p.add(margin);
         bt = new JogButton(posText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(posText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(posText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);

         margin = new JLabel(" ");
         margin.setPreferredSize(new Dimension(other, (int) r1i * 2));
         p.add(margin);

         bt = new JogButton(negText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(negText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(negText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      } else {
         margin.setPreferredSize(new Dimension(gap / 2, other));
         p.add(margin);
         bt = new JogButton(negText, 3, r3o - r3i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(negText, 2, r2o - r2i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(negText, 1, r1o - r1i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);

         margin = new JLabel(" ");
         margin.setPreferredSize(new Dimension((int) r1i * 2, other));
         p.add(margin);

         bt = new JogButton(posText, 1, r1o - r1i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(posText, 2, r2o - r2i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);

         bt = new JogButton(posText, 3, r3o - r3i, other, condEnable, false);
         bt.addMouseListener(ml);
         p.add(bt);
      }
   }


   private int               diameter;
   private int               other;
   private int               gap;
   private boolean           isVertical;
   private boolean           noCenterGap;
   private String            posText;
   private String            negText;
   private static final long serialVersionUID = 1L;
}
