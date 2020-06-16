package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       SpeedPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    25.9.2019 by Django Reinhard
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.SpeedInfo;
import de.schwarzrot.system.MachineControl;
import de.schwarzrot.util.BindUtils;
import de.schwarzrot.widgets.DRO;
import de.schwarzrot.widgets.ProgressBar;


public class SpeedPane extends JPanel {
   public SpeedPane() {
      setLayout(new GridBagLayout());
      setOpaque(true);
      setBackground(UITheme.getColor(UITheme.Speed_grid_color));
      addComponents2Pane();
   }


   // TODO: externalize font and colors
   protected void addComponents2Pane() {
      GridBagConstraints c     = new GridBagConstraints();
      AppSetup           setup = LCStatus.getStatus().getSetup();
      NumberFormat       nf    = UITheme.getFormat(UITheme.DRO_speed_format);
      Color              cf    = UITheme.getColor(UITheme.DRO_speed_foreground);
      Color              cb    = UITheme.getColor(UITheme.DRO_speed_background);
      Font               f     = UITheme.getFont(UITheme.DRO_speed_font);
      MouseWheelListener mwl   = MachineControl.getInstance();
      MouseListener      ml    = MachineControl.getInstance();
      DRO                dro;

      c.fill       = GridBagConstraints.BOTH;
      c.weightx    = 0;
      c.weighty    = 0.3;
      c.gridx      = 0;
      c.gridy      = 0;
      c.gridheight = 2;
      c.gridwidth  = 1;
      add(createLabel(" F", true), c);

      c.gridx      = 1;
      c.gridheight = 1;
      c.weightx    = 0.4;
      dro          = createDRO(nf, cf, cb, f);
      dro.setName(SpeedInfo.NominalFeed);
      BindUtils.bind(SpeedInfo.NominalFeed, LCStatus.getStatus().getSpeedInfo(), dro);
      dro.addMouseWheelListener(mwl);
      dro.addMouseListener(ml);
      add(dro, c);

      c.gridx      = 2;
      c.gridheight = 1;
      c.weightx    = 0.6;
      dro          = createDRO(nf, cf, cb, f);
      dro.setName(SpeedInfo.CurFeed);
      BindUtils.bind(SpeedInfo.CurFeed, LCStatus.getStatus().getSpeedInfo(), dro);
      dro.addMouseWheelListener(mwl);
      dro.addMouseListener(ml);
      add(dro, c);

      c.gridx     = 1;
      c.gridy     = 1;
      c.gridwidth = 2;
      c.weighty   = 0;
      c.weightx   = 1;
      double lim = 1.0;

      try {
         lim = Double.parseDouble(setup.findProperty("MAX_FEED_OVERRIDE"));
      } catch (Throwable t) {
      }
      ProgressBar pg = new ProgressBar(0, Double.valueOf(lim * 100).intValue());

      pg.setName("feedOverride");
      BindUtils.bind(SpeedInfo.FeedFactor, LCStatus.getStatus().getSpeedInfo(), pg);
      pg.addMouseWheelListener(mwl);
      add(pg, c);

      c.gridx      = 0;
      c.gridy      = 2;
      c.gridheight = 2;
      c.gridwidth  = 1;
      c.weightx    = 0;
      c.weighty    = 0.3;
      add(createLabel(" FF", true), c);

      c.gridx      = 1;
      c.weightx    = 1;
      c.gridheight = 1;
      c.gridwidth  = 2;
      dro          = createDRO(nf, cf, cb, f);
      dro.setName("curRapid");
      BindUtils.bind(SpeedInfo.MaxSpeed, LCStatus.getStatus().getSpeedInfo(), dro);
      dro.addMouseWheelListener(mwl);
      dro.addMouseListener(ml);
      add(dro, c);

      c.gridx     = 1;
      c.gridy     = 3;
      c.gridwidth = 2;
      c.weighty   = 0;
      pg          = new ProgressBar(0, 100);
      pg.setName("rapidOverride");
      BindUtils.bind(SpeedInfo.RapidFactor, LCStatus.getStatus().getSpeedInfo(), pg);
      pg.addMouseWheelListener(MachineControl.getInstance());
      pg.addMouseListener(ml);
      add(pg, c);

      c.gridx      = 0;
      c.gridy      = 4;
      c.gridheight = 2;
      c.gridwidth  = 1;
      c.weightx    = 0;
      c.weighty    = 0.3;
      add(createLabel(" S", true), c);
      Dimension size;

      c.gridx      = 1;
      c.gridheight = 1;
      c.weightx    = 0.4;
      dro          = createDRO(nf, cf, cb, f);
      size         = dro.calcMinSize(23000);
      dro.setMinimumSize(size);
      dro.setPreferredSize(size);
      dro.setName("nomSpindle");
      BindUtils.bind(SpeedInfo.SpindleNominalSpeed, LCStatus.getStatus().getSpeedInfo(), dro);
      dro.addMouseWheelListener(mwl);
      dro.addMouseListener(ml);
      add(dro, c);

      c.gridx   = 2;
      c.weightx = 0.6;
      dro       = createDRO(nf, cf, cb, f);
      dro.setMinimumSize(size);
      dro.setPreferredSize(size);
      dro.setName("curSpindle");
      BindUtils.bind(SpeedInfo.SpindleCurSpeed, LCStatus.getStatus().getSpeedInfo(), dro);
      dro.addMouseWheelListener(mwl);
      dro.addMouseListener(ml);
      add(dro, c);

      c.gridx     = 1;
      c.gridy     = 5;
      c.gridwidth = 2;
      c.weighty   = 0;
      lim         = 1.0;

      try {
         lim = Double.parseDouble(setup.findProperty("MAX_SPINDLE_OVERRIDE"));
      } catch (Throwable t) {
      }
      pg = new ProgressBar(0, Double.valueOf(lim * 100).intValue());
      pg.setName("spindleOverride");
      BindUtils.bind(SpeedInfo.SpindleFactor, LCStatus.getStatus().getSpeedInfo(), pg);
      pg.addMouseWheelListener(mwl);
      pg.addMouseListener(ml);
      add(pg, c);
   }


   protected DRO createDRO(NumberFormat nf, Color cf, Color cb, Font f) {
      DRO dro = new DRO();

      dro.setFormat(nf);
      dro.setFont(f);
      dro.setForeground(cf);
      dro.setBackground(cb);

      return dro;
   }


   private JLabel createLabel(String labelText, boolean isHeader) {
      JLabel label = new JLabel(labelText);

      label.setOpaque(true);
      label.setFont(UITheme.getFont(UITheme.DRO_speed_header_font));
      FontMetrics fm = this.getFontMetrics(label.getFont());
      Dimension   s  = new Dimension(SwingUtilities.computeStringWidth(fm, "T99"), fm.getHeight());

      label.setMinimumSize(s);
      label.setPreferredSize(s);
      if (isHeader) {
         label.setForeground(UITheme.getColor(UITheme.DRO_speed_header_foreground));
         label.setBackground(UITheme.getColor(UITheme.DRO_speed_header_background));
      } else
         label.setHorizontalAlignment(JLabel.RIGHT);

      return label;
   }


   private static final long serialVersionUID = 1L;
}
