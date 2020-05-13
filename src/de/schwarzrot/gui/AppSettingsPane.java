package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       AppSettingsPane.java
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


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.widgets.ToolTable;


public class AppSettingsPane extends JTabbedPane implements ActionListener {
   public AppSettingsPane(CommandWriter cmdWriter) {
      this.setTabPlacement(JTabbedPane.BOTTOM);
      this.setFont(UITheme.getFont("AppSettings:tab.font"));
      status       = LCStatus.getStatus();
      labelFont    = UITheme.getFont("AppSettings:font");
      settingsPane = new JPanel();
      settingsPane.setBorder(new EmptyBorder(0, 10, 0, 10));
      settingsPane.setLayout(new GridBagLayout());
      addComponents(settingsPane);

      ToolManager toolManager = new ToolManager(LCStatus.getStatus().lm("tooltable"), cmdWriter);
      ToolTable   toolTable   = new ToolTable(cmdWriter);
      FixturePane fixturePane = new FixturePane(status.getSetup(), cmdWriter);

      this.addTab(LCStatus.getStatus().lm("ToolManager"), toolManager);
      this.addTab(LCStatus.getStatus().lm("Settings"), settingsPane);
      this.addTab(LCStatus.getStatus().lm("ToolTable"), toolTable);
      this.addTab(LCStatus.getStatus().lm("FixtureTable"), fixturePane);
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if ("rel".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(relPos)) {
            relPos.setFont(fontChooser.getSelectedFont());
            relPos.setForeground(colorChooser.getForegroundColor());
            relPos.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("abs".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(absPos)) {
            absPos.setFont(fontChooser.getSelectedFont());
            absPos.setForeground(colorChooser.getForegroundColor());
            absPos.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("dtg".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(dtgPos)) {
            dtgPos.setFont(fontChooser.getSelectedFont());
            dtgPos.setForeground(colorChooser.getForegroundColor());
            dtgPos.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("AxisLetter".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(axisLetter)) {
            axisLetter.setFont(fontChooser.getSelectedFont());
            axisLetter.setForeground(colorChooser.getForegroundColor());
            axisLetter.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("gcode".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(gCode)) {
            gCode.setFont(fontChooser.getSelectedFont());
            gCode.setForeground(colorChooser.getForegroundColor());
            gCode.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("speed".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(speed)) {
            speed.setFont(fontChooser.getSelectedFont());
            speed.setForeground(colorChooser.getForegroundColor());
            speed.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("toolNum".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(toolNumber)) {
            toolNumber.setFont(fontChooser.getSelectedFont());
            toolNumber.setForeground(colorChooser.getForegroundColor());
            toolNumber.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("toolInfo".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(toolInfo)) {
            toolInfo.setFont(fontChooser.getSelectedFont());
            toolInfo.setForeground(colorChooser.getForegroundColor());
            toolInfo.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("fileManager".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(fileManager)) {
            fileManager.setFont(fontChooser.getSelectedFont());
            fileManager.setForeground(colorChooser.getForegroundColor());
            fileManager.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("toolTable".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(toolTable)) {
            toolTable.setFont(fontChooser.getSelectedFont());
            toolTable.setForeground(colorChooser.getForegroundColor());
            toolTable.setBackground(colorChooser.getBackgroundColor());
         }
      } else if ("fixtureTable".compareTo(e.getActionCommand()) == 0) {
         if (showDialog(fixtureTable)) {
            fixtureTable.setFont(fontChooser.getSelectedFont());
            fixtureTable.setForeground(colorChooser.getForegroundColor());
            fixtureTable.setBackground(colorChooser.getBackgroundColor());
         }
      }
      this.updateUI();
   }


   public JLabel getAbsPos() {
      return absPos;
   }


   public JLabel getAxisLetter() {
      return axisLetter;
   }


   public JLabel getDtgPos() {
      return dtgPos;
   }


   public JLabel getFileManager() {
      return fileManager;
   }


   public JLabel getFixtureTable() {
      return fixtureTable;
   }


   public JLabel getGCode() {
      return gCode;
   }


   public JLabel getRelPos() {
      return relPos;
   }


   public JLabel getSpeed() {
      return speed;
   }


   public JLabel getToolInfo() {
      return toolInfo;
   }


   public JLabel getToolNumber() {
      return toolNumber;
   }


   public JLabel getToolTable() {
      return toolTable;
   }


   public boolean showDialog(JLabel c) {
      if (dialogPane == null) {
         dialogPane = new JPanel(new GridLayout(0, 2));
         dialogPane.setBackground(UITheme.getColor("Main:grid.color"));
         fontChooser  = new FontChooserPane();
         colorChooser = new ColorChooserPane();

         dialogPane.add(fontChooser);
         dialogPane.add(colorChooser);
      }
      if (c != null) {
         fontChooser.setFont2Change(c.getFont());
         fontChooser.setSampleText(c.getText());
         colorChooser.setForegroundColor(c.getForeground());
         colorChooser.setBackgroundColor(c.getBackground());
      }
      int rv = JOptionPane.showConfirmDialog(SwingUtilities.windowForComponent(this), dialogPane,
            LCStatus.getStatus().lm("Font+Colors"), JOptionPane.OK_CANCEL_OPTION);
      return rv == JOptionPane.OK_OPTION;
   }


   protected void addComponents(JPanel p) {
      p.setOpaque(true);
      p.setBackground(UITheme.getColor("AppSettings:background"));
      relPos = new JLabel(posSampleText);
      relPos.setOpaque(true);
      relPos.setBackground(UITheme.getColor("DRO:rel.background"));
      relPos.setForeground(UITheme.getColor("DRO:rel.foreground"));
      relPos.setFont(UITheme.getFont("DRO:rel.font"));

      absPos = new JLabel(posSampleText);
      absPos.setOpaque(true);
      absPos.setBackground(UITheme.getColor("DRO:abs.background"));
      absPos.setForeground(UITheme.getColor("DRO:abs.foreground"));
      absPos.setFont(UITheme.getFont("DRO:abs.font"));

      dtgPos = new JLabel(posSampleText);
      dtgPos.setOpaque(true);
      dtgPos.setBackground(UITheme.getColor("DRO:dtg.background"));
      dtgPos.setForeground(UITheme.getColor("DRO:dtg.foreground"));
      dtgPos.setFont(UITheme.getFont("DRO:dtg.font"));

      gCode = new JLabel(gcodeSampleText);
      gCode.setOpaque(true);
      gCode.setBackground(UITheme.getColor("GCode:line.background"));
      gCode.setForeground(UITheme.getColor("GCode:line.foreground"));
      gCode.setFont(UITheme.getFont("GCode:line.font"));

      speed = new JLabel("S 20.475");
      speed.setOpaque(true);
      speed.setBackground(UITheme.getColor("DRO:speed.background"));
      speed.setForeground(UITheme.getColor("DRO:speed.foreground"));
      speed.setFont(UITheme.getFont("DRO:speed.font"));

      axisLetter = new JLabel("X (-Axis)");
      axisLetter.setOpaque(true);
      axisLetter.setBackground(UITheme.getColor("Axis:background"));
      axisLetter.setForeground(UITheme.getColor("Axis:foreground"));
      axisLetter.setFont(UITheme.getFont("Axis:font"));

      toolNumber = new JLabel("T12");
      toolNumber.setOpaque(true);
      toolNumber.setBackground(UITheme.getColor("Tool:number.background"));
      toolNumber.setForeground(UITheme.getColor("Tool:number.foreground"));
      toolNumber.setFont(UITheme.getFont("Tool:number.font"));

      toolInfo = new JLabel("4mm Endmill 3 Flutes");
      toolInfo.setOpaque(true);
      toolInfo.setBackground(UITheme.getColor("Tool:info.background"));
      toolInfo.setForeground(UITheme.getColor("Tool:info.foreground"));
      toolInfo.setFont(UITheme.getFont("Tool:info.font"));

      fileManager = new JLabel("Butterfly.ngc");
      fileManager.setOpaque(true);
      fileManager.setBackground(UITheme.getColor("FileManager:background"));
      fileManager.setForeground(UITheme.getColor("FileManager:foreground"));
      fileManager.setFont(UITheme.getFont("FileManager:font"));

      toolTable = new JLabel("4mm Endmill 3 Flutes");
      toolTable.setOpaque(true);
      toolTable.setBackground(UITheme.getColor("ToolTable:background"));
      toolTable.setForeground(UITheme.getColor("ToolTable:foreground"));
      toolTable.setFont(UITheme.getFont("ToolTable:table.font"));

      fixtureTable = new JLabel("4mm Endmill 3 Flutes");
      fixtureTable.setOpaque(true);
      fixtureTable.setBackground(UITheme.getColor("Fixture:background"));
      fixtureTable.setForeground(UITheme.getColor("Fixture:foreground"));
      fixtureTable.setFont(UITheme.getFont("Fixture:table.font"));
      JButton            bt = new JButton(LCStatus.getStatus().lm("change"));
      GridBagConstraints c  = new GridBagConstraints();

      bt.setActionCommand("rel");
      bt.addActionListener(this);
      c.insets  = new Insets(2, 2, 2, 2);
      c.gridx   = 0;
      c.gridy   = 0;
      c.weightx = 0.1;
      c.weighty = 0.5;
      c.fill    = GridBagConstraints.BOTH;
      p.add(createLabel("relPos"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(relPos, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(status.lm("change"));
      bt.setActionCommand("abs");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 1;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("absPos"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(absPos, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(status.lm("change"));
      bt.setActionCommand("dtg");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 2;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("dtg"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(dtgPos, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(status.lm("change"));
      bt.setActionCommand("AxisLetter");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 3;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("axisL"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(axisLetter, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(status.lm("change"));
      bt.setActionCommand("gcode");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 4;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("GCodeLister"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(gCode, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(status.lm("change"));
      bt.setActionCommand("speed");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 5;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("Speed"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(speed, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(LCStatus.getStatus().lm("change"));
      bt.setActionCommand("toolNum");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 6;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("TNum"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(toolNumber, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(LCStatus.getStatus().lm("change"));
      bt.setActionCommand("toolInfo");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 7;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("ToolInfo"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(toolInfo, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(LCStatus.getStatus().lm("change"));
      bt.setActionCommand("fileManager");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 8;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("FileManager"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(fileManager, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(LCStatus.getStatus().lm("change"));
      bt.setActionCommand("toolTable");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 9;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("tooltable"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(toolTable, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);

      bt = new JButton(LCStatus.getStatus().lm("change"));
      bt.setActionCommand("fixtureTable");
      bt.addActionListener(this);
      c.gridx = 0;
      c.gridy = 10;
      c.fill  = GridBagConstraints.BOTH;
      p.add(createLabel("FixtureTable"), c);
      c.gridx   = 1;
      c.weightx = 0.8;
      p.add(fixtureTable, c);
      c.gridx   = 2;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 0.1;
      p.add(bt, c);
   }


   protected JLabel createLabel(String text) {
      JLabel l = new JLabel(status.lm(text));

      if (textColor == null)
         textColor = UITheme.getColor("AppSettings:foreground");

      l.setFont(labelFont);
      l.setForeground(textColor);

      return l;
   }


   private Font                labelFont;
   private Color               textColor;
   private JLabel              relPos;
   private JLabel              absPos;
   private JLabel              dtgPos;
   private JLabel              gCode;
   private JLabel              speed;
   private JLabel              axisLetter;
   private JLabel              toolNumber;
   private JLabel              toolInfo;
   private JLabel              fileManager;
   private JLabel              toolTable;
   private JLabel              fixtureTable;
   private JPanel              dialogPane;
   private JPanel              settingsPane;
   private LCStatus            status;
   private FontChooserPane     fontChooser;
   private ColorChooserPane    colorChooser;
   private static final String posSampleText    = "-0.123.456,789";
   private static final String gcodeSampleText  = "G0 X397 Y-512;";
   private static final long   serialVersionUID = 1L;
}
