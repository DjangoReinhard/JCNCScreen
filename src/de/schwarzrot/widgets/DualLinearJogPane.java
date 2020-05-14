package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       DualLinearJogPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    6.5.2020 by Django Reinhard
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
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.schwarzrot.bean.IAxisMask;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.system.MachineControl;


public class DualLinearJogPane extends JPanel {
   public DualLinearJogPane(IAxisMask am, Character leftAxis, Character rightAxis, int w, int h,
         ICondition condEnable) {
      this.hasLeft  = am.hasAxis(leftAxis);
      this.hasRight = am.hasAxis(rightAxis);
      diameter      = (int) (0.95 * h);
      gap           = h - diameter;
      other         = w;
      this.width    = (int) (2.0 * w + 0.7 * gap);
      Dimension s = new Dimension(width, h);

      setPreferredSize(s);
      setMinimumSize(s);
      setOpaque(true);
      setBackground(UITheme.getColor("JogButtonPane:background"));
      FlowLayout fl = new FlowLayout();

      fl.setHgap(0);
      fl.setVgap(0);
      setLayout(fl);
      leftPosText  = String.format("%C+", leftAxis);
      leftNegText  = String.format("%C-", leftAxis);
      rightPosText = String.format("%C+", rightAxis);
      rightNegText = String.format("%C-", rightAxis);
      createComponents(this, condEnable);
   }


   protected void createComponents(JPanel pane, ICondition condEnable) {
      double        r      = diameter / 2;
      double        w      = r * 0.75;
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
      JPanel        p      = new JPanel();
      BoxLayout     bl     = new BoxLayout(p, BoxLayout.X_AXIS);
      MouseListener ml     = MachineControl.getInstance();
      ItemListener  il     = MachineControl.getInstance();
      JButton       bt     = null;

      margin.setPreferredSize(new Dimension((int) other, (int) (gap * 0.5)));
      pane.add(margin);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r3o - r3i)));
      if (hasLeft) {
         bt = new JogButton(leftPosText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightPosText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);

      p  = new JPanel();
      bl = new BoxLayout(p, BoxLayout.X_AXIS);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r2o - r2i)));
      if (hasLeft) {
         bt = new JogButton(leftPosText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightPosText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);

      p  = new JPanel();
      bl = new BoxLayout(p, BoxLayout.X_AXIS);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r1o - r1i)));
      if (hasLeft) {
         bt = new JogButton(leftPosText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightPosText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);

      p = new JPanel();
      p.setLayout(new GridLayout(2, 0));
      p.setPreferredSize(new Dimension((int) (2.0 * other + 0.7 * gap), (int) (r1i * 2)));
      // create checkbox here, as it is needed on setSelected()
      cbFastMove = new JCheckBox(LCStatus.getStatus().lm("rapidJog"));
      cbFastMove.setName("fastMove");

      cbContinuous = new JCheckBox(LCStatus.getStatus().lm("singleStep"));
      cbContinuous.setName("singleStep");
      cbContinuous.addItemListener(il);
      cbContinuous.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            Object o = e.getSource();

            if (o.equals(cbContinuous)) {
               if (cbContinuous.isSelected()) {
                  cbFastMove.setSelected(false);
                  cbFastMove.setEnabled(false);
               } else {
                  cbFastMove.setEnabled(true);
               }
            }
         }
      });
      cbContinuous.setHorizontalAlignment(JCheckBox.LEFT);
      cbContinuous.setVerticalAlignment(JCheckBox.CENTER);
      cbContinuous.setSelected(true);
      cbContinuous.setBorderPainted(false);
      cbContinuous.setFocusPainted(false);
      cbContinuous.setBackground(UITheme.getColor("JogButtonPane:background"));
      //      cbContinuous.setForeground(UITheme.getColor("JogButton:foreground"));
      cbContinuous.setFont(UITheme.getFont("MessageLOG:header.font"));
      p.add(cbContinuous);
      cbFastMove.addItemListener(il);
      cbFastMove.setHorizontalAlignment(JCheckBox.LEFT);
      cbFastMove.setVerticalAlignment(JCheckBox.CENTER);
      //      cbFastMove.setSelected(false);
      cbFastMove.setBorderPainted(false);
      cbFastMove.setFocusPainted(false);
      cbFastMove.setBackground(UITheme.getColor("JogButtonPane:background"));
      //      cbFastMove.setForeground(UITheme.getColor("JogButton:foreground"));
      cbFastMove.setFont(UITheme.getFont("MessageLOG:header.font"));
      p.add(cbFastMove);
      pane.add(p);

      p  = new JPanel();
      bl = new BoxLayout(p, BoxLayout.X_AXIS);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r1o - r1i)));
      if (hasLeft) {
         bt = new JogButton(leftNegText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightNegText, 1, other, r1o - r1i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);

      p  = new JPanel();
      bl = new BoxLayout(p, BoxLayout.X_AXIS);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r2o - r2i)));
      if (hasLeft) {
         bt = new JogButton(leftNegText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightNegText, 2, other, r2o - r2i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);

      p  = new JPanel();
      bl = new BoxLayout(p, BoxLayout.X_AXIS);
      p.setLayout(bl);
      p.setOpaque(false);
      p.setPreferredSize(new Dimension(width, (int) (r3o - r3i)));
      if (hasLeft) {
         bt = new JogButton(leftNegText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      p.add(Box.createHorizontalGlue());
      if (hasRight) {
         bt = new JogButton(rightNegText, 3, other, r3o - r3i, condEnable);
         bt.addMouseListener(ml);
         p.add(bt);
      }
      pane.add(p);
   }


   private String            leftPosText;
   private String            rightPosText;
   private String            leftNegText;
   private String            rightNegText;
   private boolean           hasLeft;
   private boolean           hasRight;
   private double            gap;
   private double            diameter;
   private double            other;
   private int               width;
   private JCheckBox         cbContinuous;
   private JCheckBox         cbFastMove;
   private static final long serialVersionUID = 1L;
}
