package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       FixtureEditor.java
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


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.system.CommandWriter;


public class FixtureEditor extends JPanel implements ActionListener {
   public FixtureEditor(AppSetup setup, CommandWriter cw, String title, CanonPosition p) {
      this.setup    = setup;
      this.title    = title;
      commandWriter = cw;
      setOpaque(true);
      setLayout(new GridLayout(0, 2));
      entries = new JTextField[9];
      TitledBorder b = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.getColor("Fixture:header.border")), title);

      b.setTitleFont(UITheme.getFont("Fixture:header.font"));
      b.setTitleColor(UITheme.getColor("Fixture:header.foreground"));
      this.setBorder(b);
      setBackground(UITheme.getColor("Fixture:background"));
      addComponents(this, p);
   }


   @Override
   public void actionPerformed(ActionEvent arg0) {
      StringBuilder sb = new StringBuilder();

      if ("Offset".compareTo(title.strip()) == 0) {
         sb.append("G92 ");
      } else if ("G54".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P1 ");
      } else if ("G55".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P2 ");
      } else if ("G56".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P3 ");
      } else if ("G57".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P4 ");
      } else if ("G58".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P5 ");
      } else if ("G59".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P6 ");
      } else if ("G59.1".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P7 ");
      } else if ("G59.2".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P8 ");
      } else if ("G59.3".compareTo(title.strip()) == 0) {
         sb.append("G10 L2 P9 ");
      } else {
         throw new UnsupportedOperationException("unknown/unsupported fixture name: " + title);
      }

      for (int i = 0; i < entries.length; ++i) {
         if (entries[i] == null)
            continue;
         String value  = entries[i].getText();
         String pvalue = value.replace(',', '.');
         double d      = 0;

         try {
            d = Double.parseDouble(pvalue);

            System.out.print("gonna save fixture " + title + " - axis ");
            switch (i) {
               case 0:
                  System.out.print("X ");
                  sb.append(String.format(Locale.US, "X%.3f ", d));
                  break;
               case 1:
                  System.out.print("Y ");
                  sb.append(String.format(Locale.US, "Y%.3f ", d));
                  break;
               case 2:
                  System.out.print("Z ");
                  sb.append(String.format(Locale.US, "Z%.3f ", d));
                  break;
               case 3:
                  System.out.print("A ");
                  sb.append(String.format(Locale.US, "A%.3f ", d));
                  break;
               case 4:
                  System.out.print("B ");
                  sb.append(String.format(Locale.US, "B%.3f ", d));
                  break;
               case 5:
                  System.out.print("C ");
                  sb.append(String.format(Locale.US, "C%.3f ", d));
                  break;
               case 6:
                  System.out.print("U ");
                  sb.append(String.format(Locale.US, "U%.3f ", d));
                  break;
               case 7:
                  System.out.print("V ");
                  sb.append(String.format(Locale.US, "V%.3f ", d));
                  break;
               case 8:
                  System.out.print("W ");
                  sb.append(String.format(Locale.US, "W%.3f ", d));
                  break;
            }
         } catch (Throwable t) {
         } finally {
            entries[i].setText(String.format(format, d));
         }
         System.out.println("new value: >" + d + "<");
      }
      if (commandWriter != null) {
         System.err.println("new MDI-command: " + sb.toString());
         commandWriter.setFixture(sb.toString());
      }
   }


   public void addComponents(JPanel p, CanonPosition pos) {
      JLabel prompt;
      int    n = 0;

      if (setup.hasXAxis()) {
         prompt     = new JLabel("X: ", JLabel.RIGHT);
         entries[0] = new JTextField(30);
         entries[0].setHorizontalAlignment(JTextField.RIGHT);
         entries[0].setText(String.format(format, pos.getX()));
         format(prompt, entries[0]);
         p.add(prompt);
         p.add(entries[0]);
      }
      if (setup.hasYAxis()) {
         prompt     = new JLabel("Y: ", JLabel.RIGHT);
         entries[1] = new JTextField(30);
         entries[1].setHorizontalAlignment(JTextField.RIGHT);
         entries[1].setText(String.format(format, pos.getY()));
         format(prompt, entries[1]);
         p.add(prompt);
         p.add(entries[1]);
      }
      if (setup.hasZAxis()) {
         prompt     = new JLabel("Z: ", JLabel.RIGHT);
         entries[2] = new JTextField(30);
         entries[2].setHorizontalAlignment(JTextField.RIGHT);
         entries[2].setText(String.format(format, pos.getZ()));
         format(prompt, entries[2]);
         p.add(prompt);
         p.add(entries[2]);
      }
      if (setup.hasAAxis()) {
         prompt     = new JLabel("A: ", JLabel.RIGHT);
         entries[3] = new JTextField(30);
         entries[3].setHorizontalAlignment(JTextField.RIGHT);
         entries[3].setText(String.format(format, pos.getA()));
         format(prompt, entries[3]);
         p.add(prompt);
         p.add(entries[3]);
      }
      if (setup.hasBAxis()) {
         prompt     = new JLabel("B: ", JLabel.RIGHT);
         entries[4] = new JTextField(30);
         entries[4].setHorizontalAlignment(JTextField.RIGHT);
         entries[4].setText(String.format(format, pos.getB()));
         format(prompt, entries[4]);
         p.add(prompt);
         p.add(entries[4]);
      }
      if (setup.hasCAxis()) {
         prompt     = new JLabel("C: ", JLabel.RIGHT);
         entries[5] = new JTextField(30);
         entries[5].setHorizontalAlignment(JTextField.RIGHT);
         entries[5].setText(String.format(format, pos.getC()));
         format(prompt, entries[5]);
         p.add(prompt);
         p.add(entries[5]);
      }
      if (setup.hasUAxis()) {
         prompt     = new JLabel("U: ", JLabel.RIGHT);
         entries[6] = new JTextField(30);
         entries[6].setHorizontalAlignment(JTextField.RIGHT);
         entries[6].setText(String.format(format, pos.getU()));
         format(prompt, entries[6]);
         p.add(prompt);
         p.add(entries[6]);
      }
      if (setup.hasVAxis()) {
         prompt     = new JLabel("V: ", JLabel.RIGHT);
         entries[7] = new JTextField(30);
         entries[7].setHorizontalAlignment(JTextField.RIGHT);
         entries[7].setText(String.format(format, pos.getV()));
         format(prompt, entries[7]);
         p.add(prompt);
         p.add(entries[7]);
      }
      if (setup.hasWAxis()) {
         prompt     = new JLabel("W: ", JLabel.RIGHT);
         entries[8] = new JTextField(30);
         entries[8].setHorizontalAlignment(JTextField.RIGHT);
         entries[8].setText(String.format(format, pos.getW()));
         format(prompt, entries[8]);
         p.add(prompt);
         p.add(entries[8]);
      }
      btSave = new JButton("save");
      btSave.addActionListener(this);
      p.add(btSave);
      p.add(new JLabel(" "));
   }


   protected void format(JLabel l, JTextField f) {
      l.setFont(UITheme.getFont("Fixture:prompt.font"));
      l.setForeground(UITheme.getColor("Fixture:foreground"));
      f.setFont(UITheme.getFont("Fixture:entry.font"));
   }


   private AppSetup            setup;
   private JTextField[]        entries;
   private String              title;
   private JButton             btSave;
   private CommandWriter       commandWriter;
   private static final String format           = "%.3f";
   private static final long   serialVersionUID = 1L;
}
