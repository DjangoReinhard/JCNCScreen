package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       ColorChooserPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    24.11.2019 by Django Reinhard
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.schwarzrot.bean.themes.UITheme;


public class ColorChooserPane extends JPanel implements ActionListener, ChangeListener {
   public ColorChooserPane() {
      super(new BorderLayout());
      setOpaque(true);
      setBackground(UITheme.getColor(UITheme.Main_grid_color));
      addComponents();
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource().equals(cbBackground)) {
         if (cbBackground.isSelected()) {
            cc.setColor(banner.getBackground());
         } else {
            cc.setColor(banner.getForeground());
         }
      }
   }


   public Color getBackgroundColor() {
      return banner.getBackground();
   }


   public Color getForegroundColor() {
      return banner.getForeground();
   }


   public void selectBackground(boolean select) {
      cbBackground.setSelected(select);
   }


   public void setBackgroundColor(Color c) {
      banner.setBackground(c);
      if (cbBackground.isSelected()) {
         cc.setColor(banner.getBackground());
      } else {
         cc.setColor(banner.getForeground());
      }
   }


   public void setForegroundColor(Color c) {
      banner.setForeground(c);
      if (cbBackground.isSelected()) {
         cc.setColor(banner.getBackground());
      } else {
         cc.setColor(banner.getForeground());
      }
   }


   @Override
   public void stateChanged(ChangeEvent e) {
      Color newColor = cc.getColor();

      if (cbBackground.isSelected()) {
         banner.setBackground(newColor);
      } else {
         banner.setForeground(newColor);
      }
   }


   protected void addComponents() {
      banner = new JLabel("Beispieltext zur Farbwahl", JLabel.CENTER);
      banner.setForeground(Color.yellow);
      banner.setBackground(Color.blue);
      banner.setOpaque(true);
      banner.setFont(new Font("SansSerif", Font.BOLD, 24));
      banner.setPreferredSize(new Dimension(100, 65));

      cc = new JColorChooser(banner.getForeground());
      cc.getSelectionModel().addChangeListener(this);
      cc.setPreviewPanel(new JPanel());
      AbstractColorChooserPanel[] panes = cc.getChooserPanels();

      // show RGB-selection only
      cc.setChooserPanels(new AbstractColorChooserPanel[] { panes[3] });
      cbBackground = new JCheckBox("Hintergrundfarbe anpassen");

      cbBackground.setBorder(new EmptyBorder(0, 12, 15, 10));
      cbBackground.addActionListener(this);
      cbBackground.setBackground(UITheme.getColor(UITheme.Main_grid_color));
      cbBackground.setForeground(UITheme.getColor(UITheme.AppSettings_foreground));
      add(banner, BorderLayout.NORTH);
      add(cc, BorderLayout.CENTER);
      add(cbBackground, BorderLayout.SOUTH);
   }


   private JColorChooser     cc;
   private JLabel            banner;
   private JCheckBox         cbBackground;
   private static final long serialVersionUID = 1L;
}
