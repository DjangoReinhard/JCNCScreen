package de.schwarzrot.gui;
/* 
 * **************************************************************************
 * 
 *  file:       JogPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    4.5.2020 by Django Reinhard
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.schwarzrot.bean.IAxisMask;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.widgets.CircleButtonPane;
import de.schwarzrot.widgets.DualLinearJogPane;
import de.schwarzrot.widgets.LinearJogPane;
import de.schwarzrot.widgets.PieButtonPane;


public class JogPane extends JPanel {
   public JogPane(IAxisMask am, ICondition condEnableCenter, ICondition condEnable) {
      FlowLayout fl = new FlowLayout();

      fl.setHgap(0);
      fl.setVgap(0);

      setLayout(fl);
      setOpaque(true);

      //      setBackground(Color.RED);
      setPreferredSize(new Dimension(1200, 1150));
      this.am = am;
      createComponents(this, condEnableCenter, condEnable);
   }


   protected void createComponents(JPanel pane, ICondition condEnableCenter, ICondition condEnable) {
      JPanel     p  = new JPanel();
      FlowLayout fl = new FlowLayout();
      int        h0 = 520;
      int        h2 = 270;
      int        w2 = 550;

      if (!(am.hasCAxis() && am.hasUAxis())) {
         h0 = 725;
         h2 = 300;
         w2 = 600;
      }
      fl.setHgap(20);
      fl.setVgap(0);
      p.setOpaque(true);
      p.setLayout(fl);
      p.setPreferredSize(new Dimension(1200, h0));

      if (am.hasVAxis()) {
         p.add(new LinearJogPane('V', 150, h0, condEnable));
      }
      if (am.hasXAxis() || am.hasYAxis()) {
         p.add(new CircleButtonPane(h0, h0, condEnableCenter, condEnable));
      }
      p.add(new DualLinearJogPane(am, 'Z', 'W', 160, h0, condEnable));
      pane.add(p);

      if (am.hasUAxis() || am.hasCAxis()) {
         p = new JPanel();
         p.setPreferredSize(new Dimension(1200, 250));
         p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

         JPanel subPane = new JPanel();

         subPane.setOpaque(true);
         subPane.setPreferredSize(new Dimension(400, 250));
         subPane.setLayout(new BoxLayout(subPane, BoxLayout.Y_AXIS));
         subPane.add(Box.createVerticalGlue());
         if (am.hasUAxis()) {
            subPane.add(new LinearJogPane('U', 500, 180, condEnable, true));
         }
         subPane.add(Box.createVerticalGlue());
         p.add(subPane);

         p.add(Box.createHorizontalGlue());
         if (am.hasCAxis()) {
            PieButtonPane pp = new PieButtonPane('C', 580, 250, condEnable, true);

            pp.setAlignmentX(0.5f);
            pp.setAlignmentY(0.5f);
            p.add(pp);
         }
         p.add(Box.createHorizontalStrut(10));

         pane.add(p);
      }

      if (am.hasAAxis() || am.hasBAxis()) {
         p = new JPanel();
         p.setPreferredSize(new Dimension(1200, h2));
         p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
         p.add(Box.createHorizontalStrut(10));
         p.add(Box.createHorizontalGlue());
         if (am.hasAAxis()) {
            PieButtonPane pp = new PieButtonPane('A', w2, h2, condEnable);

            pp.setAlignmentX(0.5f);
            p.add(pp);
         }
         if (am.hasBAxis()) {
            p.add(Box.createHorizontalGlue());
            PieButtonPane pp = new PieButtonPane('B', w2, h2, condEnable);

            pp.setAlignmentX(0.5f);
            p.add(pp);
            p.add(Box.createHorizontalStrut(10));
         }
         pane.add(p);
      }
   }


   private IAxisMask         am;
   private static final long serialVersionUID = 1L;
}
