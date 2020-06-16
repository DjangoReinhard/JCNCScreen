package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       FontChooserPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    23.11.2019 by Django Reinhard
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


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.schwarzrot.bean.themes.UITheme;


public class FontChooserPane extends JPanel implements ActionListener, ListSelectionListener {
   public FontChooserPane() {
      GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
      FontMetrics         fm;
      int                 w0, w1;

      mAllFonts  = new DefaultListModel<String>();
      mMonoFonts = new DefaultListModel<String>();
      for (Font f : gEnv.getAllFonts()) {
         Font fnt = f.deriveFont(10);

         if (!mAllFonts.contains(fnt.getFamily())) {
            mAllFonts.addElement(fnt.getFamily());
         }
         fm = this.getFontMetrics(fnt);
         w0 = SwingUtilities.computeStringWidth(fm, "ii");
         w1 = SwingUtilities.computeStringWidth(fm, "WW");

         if (w0 == w1) {
            // System.out.println("found monospace font: " + f.getFamily());
            if (!mMonoFonts.contains(fnt.getFamily())) {
               mMonoFonts.addElement(fnt.getFamily());
            }
         }
      }
      setLayout(new BorderLayout());
      addComponents(this);
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource().equals(cbMono)) {
         int s = 0;

         if (cbMono.isSelected()) {
            lcFontFamilies.setModel(mMonoFonts);
            if (mMonoFonts.contains(fontFamily)) {
               s = mMonoFonts.indexOf(fontFamily);
               lcFontFamilies.setSelectedIndex(s);
            }
         } else {
            s = mAllFonts.indexOf(fontFamily);

            lcFontFamilies.setModel(mAllFonts);
            lcFontFamilies.setSelectedIndex(s);
         }
         lcFontFamilies.ensureIndexIsVisible(s);
         this.updateUI();
      }
   }


   public Font getSelectedFont() {
      return sampleText.getFont();
   }


   public void setFont2Change(Font f) {
      DefaultListModel<String> m = (DefaultListModel) lcFontFamilies.getModel();

      if (m.contains(f.getFamily())) {
         fontFamily = f.getFamily();
         lcFontFamilies.setSelectedValue(f.getFamily(), true);

         fontStyle = f.getStyle();
         if ((fontStyle & Font.BOLD) != 0 && ((fontStyle & Font.ITALIC) != 0)) {
            lcFontStyles.setSelectedValue(FS_BoldItalic, true);
         } else if ((fontStyle & Font.BOLD) != 0) {
            lcFontStyles.setSelectedValue(FS_Bold, true);
         } else if ((fontStyle & Font.ITALIC) != 0) {
            lcFontStyles.setSelectedValue(FS_Italic, true);
         }
         fontSize = f.getSize();
         lcFontSizes.setSelectedValue(fontSize, true);
      }
   }


   public void setSampleText(String st) {
      sampleText.setText(st);
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      if (e.getSource().equals(lcFontFamilies)) {
         String fn = lcFontFamilies.getSelectedValue();

         if (fn != null)
            fontFamily = fn;
      } else if (e.getSource().equals(lcFontStyles)) {
         String fs = lcFontStyles.getSelectedValue();

         if (FS_Bold.compareTo(fs) == 0) {
            fontStyle = Font.BOLD;
         } else if (FS_Italic.compareTo(fs) == 0) {
            fontStyle = Font.ITALIC;
         } else if (FS_BoldItalic.compareTo(fs) == 0) {
            fontStyle = Font.BOLD | Font.ITALIC;
         } else {
            fontStyle = Font.PLAIN;
         }
      } else if (e.getSource().equals(lcFontSizes)) {
         fontSize = lcFontSizes.getSelectedValue();
      }
      Font newFont = new Font(fontFamily, fontStyle, fontSize);

      sampleText.setFont(newFont);
      this.updateUI();
   }


   protected void addComponents(JPanel p) {
      cbMono = new JCheckBox("nur Schriftarten mit gleicher Buchstabenbreite");
      cbMono.setBorder(new EmptyBorder(15, 20, 0, 20));
      cbMono.addActionListener(this);
      cbMono.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      cbMono.setForeground(UITheme.getColor(UITheme.AppSettings_foreground));
      add(cbMono, BorderLayout.NORTH);
      lcFontFamilies = new JList<String>(mAllFonts);

      lcFontFamilies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      lcFontFamilies.setSelectedIndex(0);
      lcFontFamilies.addListSelectionListener(this);
      lcFontFamilies.setVisibleRowCount(15);
      lcFontFamilies.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      lcFontFamilies.setForeground(UITheme.getColor(UITheme.AppSettings_foreground));
      JScrollPane lsp0 = new JScrollPane(lcFontFamilies);

      lsp0.setBorder(new EmptyBorder(15, 20, 20, 10));
      lsp0.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      p.add(lsp0, BorderLayout.WEST);

      DefaultListModel<String> lmFS = new DefaultListModel<String>();

      lmFS.addElement(FS_Plain);
      lmFS.addElement(FS_Bold);
      lmFS.addElement(FS_Italic);
      lmFS.addElement(FS_BoldItalic);

      lcFontStyles = new JList<String>(lmFS);

      lcFontStyles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      lcFontStyles.setSelectedIndex(0);
      lcFontStyles.addListSelectionListener(this);
      lcFontStyles.setVisibleRowCount(5);
      lcFontStyles.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      lcFontStyles.setForeground(UITheme.getColor(UITheme.AppSettings_foreground));
      JScrollPane lsp1 = new JScrollPane(lcFontStyles);

      lsp1.setBorder(new EmptyBorder(15, 10, 20, 10));
      lsp1.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      p.add(lsp1, BorderLayout.CENTER);

      DefaultListModel<Integer> lmS  = new DefaultListModel<Integer>();
      int                       step = 1;

      for (int s = 9; s < 100; s += step) {
         if (s == 20)
            step = 2;
         if (s == 40)
            step = 5;
         if (s == 60)
            step = 10;
         lmS.addElement(s);
      }
      lcFontSizes = new JList<Integer>(lmS);
      lcFontSizes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      lcFontSizes.setSelectedIndex(0);
      lcFontSizes.addListSelectionListener(this);
      lcFontSizes.setVisibleRowCount(5);
      lcFontSizes.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      lcFontSizes.setForeground(UITheme.getColor(UITheme.AppSettings_foreground));
      JScrollPane lsp2 = new JScrollPane(lcFontSizes);

      lsp2.setBorder(new EmptyBorder(15, 10, 20, 20));
      lsp2.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      p.add(lsp2, BorderLayout.EAST);

      sampleText = new JTextField();
      sampleText.setText("daß Anfänger öfters üben sollten ist bekannt");
      sampleText.setBorder(new EmptyBorder(20, 20, 20, 20));
      p.add(sampleText, BorderLayout.SOUTH);

      Font f = sampleText.getFont();

      fontFamily = f.getFamily();
      fontStyle  = f.getStyle();
      fontSize   = f.getSize();
   }


   private JList<String>            lcFontFamilies;
   private JList<String>            lcFontStyles;
   private JList<Integer>           lcFontSizes;
   private JTextField               sampleText;
   private JCheckBox                cbMono;
   private String                   fontFamily;
   private int                      fontStyle;
   private int                      fontSize;
   private DefaultListModel<String> mAllFonts;
   private DefaultListModel<String> mMonoFonts;
   private static final String      FS_Plain         = "Normal";
   private static final String      FS_Bold          = "Fett";
   private static final String      FS_Italic        = "Kursiv";
   private static final String      FS_BoldItalic    = "Fett und Kursiv";
   private static final long        serialVersionUID = 1L;
}
