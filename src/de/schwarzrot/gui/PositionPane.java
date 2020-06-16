package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       PositionPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    7.9.2019 by Django Reinhard
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.util.BindUtils;
import de.schwarzrot.widgets.AxisLetter;
import de.schwarzrot.widgets.DRO;
import de.schwarzrot.widgets.LED;


public class PositionPane extends JPanel implements PropertyChangeListener {
   @SuppressWarnings("unchecked")
   public PositionPane() {
      setLayout(new GridBagLayout());
      setOpaque(true);
      setBackground(UITheme.getColor(UITheme.DRO_grid_color));
      absFont       = UITheme.getFont(UITheme.DRO_abs_font);
      relFont       = UITheme.getFont(UITheme.DRO_rel_font);
      absForeground = UITheme.getColor(UITheme.DRO_abs_foreground);
      relForeground = UITheme.getColor(UITheme.DRO_rel_foreground);
      absBackground = UITheme.getColor(UITheme.DRO_abs_background);
      relBackground = UITheme.getColor(UITheme.DRO_rel_background);
      absPosition   = LCStatus.getStatus().getModel(LCStatus.MN_AbsPosition);
      absPosition.addPropertyChangeListener(this);
      createGUI();
   }


   @Override
   public synchronized void propertyChange(PropertyChangeEvent pce) {
      boolean isAbsPosition = absPosition.getValue();

      if (isAbsPosition) {
         if (setup.hasXAxis()) {
            posX.setFont(absFont);
            posX.setForeground(absForeground);
            posX.setBackground(absBackground);
         }
         if (setup.hasYAxis()) {
            posY.setFont(absFont);
            posY.setForeground(absForeground);
            posY.setBackground(absBackground);
         }
         if (setup.hasZAxis()) {
            posZ.setFont(absFont);
            posZ.setForeground(absForeground);
            posZ.setBackground(absBackground);
         }
         if (setup.hasAAxis()) {
            posA.setFont(absFont);
            posA.setForeground(absForeground);
            posA.setBackground(absBackground);
         }
         if (setup.hasBAxis()) {
            posB.setFont(absFont);
            posB.setForeground(absForeground);
            posB.setBackground(absBackground);
         }
         if (setup.hasCAxis()) {
            posC.setFont(absFont);
            posC.setForeground(absForeground);
            posC.setBackground(absBackground);
         }
         if (setup.hasUAxis()) {
            posU.setFont(absFont);
            posU.setForeground(absForeground);
            posU.setBackground(absBackground);
         }
         if (setup.hasVAxis()) {
            posV.setFont(absFont);
            posV.setForeground(absForeground);
            posV.setBackground(absBackground);
         }
         if (setup.hasWAxis()) {
            posW.setFont(absFont);
            posW.setForeground(absForeground);
            posW.setBackground(absBackground);
         }
      } else {
         if (setup.hasXAxis()) {
            posX.setFont(relFont);
            posX.setForeground(relForeground);
            posX.setBackground(relBackground);
         }
         if (setup.hasYAxis()) {
            posY.setFont(relFont);
            posY.setForeground(relForeground);
            posY.setBackground(relBackground);
         }
         if (setup.hasZAxis()) {
            posZ.setFont(relFont);
            posZ.setForeground(relForeground);
            posZ.setBackground(relBackground);
         }
         if (setup.hasAAxis()) {
            posA.setFont(relFont);
            posA.setForeground(relForeground);
            posA.setBackground(relBackground);
         }
         if (setup.hasBAxis()) {
            posB.setFont(relFont);
            posB.setForeground(relForeground);
            posB.setBackground(relBackground);
         }
         if (setup.hasCAxis()) {
            posC.setFont(relFont);
            posC.setForeground(relForeground);
            posC.setBackground(relBackground);
         }
         if (setup.hasUAxis()) {
            posU.setFont(relFont);
            posU.setForeground(relForeground);
            posU.setBackground(relBackground);
         }
         if (setup.hasVAxis()) {
            posV.setFont(relFont);
            posV.setForeground(relForeground);
            posV.setBackground(relBackground);
         }
         if (setup.hasWAxis()) {
            posW.setFont(relFont);
            posW.setForeground(relForeground);
            posW.setBackground(relBackground);
         }
      }
   }


   @SuppressWarnings("unchecked")
   protected void createGUI() {
      if (setup == null) {
         setup = LCStatus.getStatus().getSetup();
         CanonPosition pos    = LCStatus.getStatus().getPositionCalculator().getPosition();
         CanonPosition dtg    = LCStatus.getStatus().getDistanceToGo();
         LCStatus      status = LCStatus.getStatus();
         DRO           dro    = new DRO();
         Font          f      = UITheme.getFont(UITheme.DRO_dtg_font);
         Color         cf     = UITheme.getColor(UITheme.DRO_dtg_foreground);
         Color         cb     = UITheme.getColor(UITheme.DRO_dtg_background);
         Dimension     size;

         dro.setFont(UITheme.getFont(UITheme.DRO_abs_font));
         size = dro.calcMinSize();

         if (setup.hasXAxis()) {
            posX   = new DRO();
            dtgX   = new DRO();
            xHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getXJoint())));

            setDROSize(posX, dtgX, size);
            dtgX.setFont(f);
            dtgX.setBackground(cb);
            dtgX.setForeground(cf);
            BindUtils.bind(CanonPosition.X, pos, posX);
            BindUtils.bind(CanonPosition.X, dtg, dtgX);
         }
         if (setup.hasYAxis()) {
            posY   = new DRO();
            dtgY   = new DRO();
            yHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getYJoint())));

            setDROSize(posY, dtgY, size);
            dtgY.setFont(f);
            dtgY.setBackground(cb);
            dtgY.setForeground(cf);
            BindUtils.bind(CanonPosition.Y, pos, posY);
            BindUtils.bind(CanonPosition.Y, dtg, dtgY);
         }
         if (setup.hasZAxis()) {
            posZ   = new DRO();
            dtgZ   = new DRO();
            zHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getZJoint())));

            setDROSize(posZ, dtgZ, size);
            dtgZ.setFont(f);
            dtgZ.setBackground(cb);
            dtgZ.setForeground(cf);
            BindUtils.bind(CanonPosition.Z, pos, posZ);
            BindUtils.bind(CanonPosition.Z, dtg, dtgZ);
         }
         if (setup.hasAAxis()) {
            posA   = new DRO();
            dtgA   = new DRO();
            aHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getAJoint())));

            setDROSize(posA, dtgA, size);
            dtgA.setFont(f);
            dtgA.setBackground(cb);
            dtgA.setForeground(cf);
            BindUtils.bind(CanonPosition.A, pos, posA);
            BindUtils.bind(CanonPosition.A, dtg, dtgA);
         }
         if (setup.hasBAxis()) {
            posB   = new DRO();
            dtgB   = new DRO();
            bHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getBJoint())));

            setDROSize(posB, dtgB, size);
            dtgB.setFont(f);
            dtgB.setBackground(cb);
            dtgB.setForeground(cf);
            BindUtils.bind(CanonPosition.B, pos, posB);
            BindUtils.bind(CanonPosition.B, dtg, dtgB);
         }
         if (setup.hasCAxis()) {
            posC   = new DRO();
            dtgC   = new DRO();
            cHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getCJoint())));

            setDROSize(posC, dtgC, size);
            dtgC.setFont(f);
            dtgC.setBackground(cb);
            dtgC.setForeground(cf);
            BindUtils.bind(CanonPosition.C, pos, posC);
            BindUtils.bind(CanonPosition.C, dtg, dtgC);
         }
         if (setup.hasUAxis()) {
            posU   = new DRO();
            dtgU   = new DRO();
            uHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getUJoint())));

            setDROSize(posU, dtgU, size);
            dtgU.setFont(f);
            dtgU.setBackground(cb);
            dtgU.setForeground(cf);
            BindUtils.bind(CanonPosition.U, pos, posU);
            BindUtils.bind(CanonPosition.U, dtg, dtgU);
         }
         if (setup.hasVAxis()) {
            posV   = new DRO();
            dtgV   = new DRO();
            vHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getVJoint())));

            setDROSize(posV, dtgV, size);
            dtgV.setFont(f);
            dtgV.setBackground(cb);
            dtgV.setForeground(cf);
            BindUtils.bind(CanonPosition.V, pos, posV);
            BindUtils.bind(CanonPosition.V, dtg, dtgV);
         }
         if (setup.hasWAxis()) {
            posW   = new DRO();
            dtgW   = new DRO();
            wHomed = new LED(status.getModel(String.format(AppSetup.jointModelPattern, setup.getWJoint())));

            setDROSize(posW, dtgW, size);
            dtgW.setFont(f);
            dtgW.setBackground(cb);
            dtgW.setForeground(cf);
            BindUtils.bind(CanonPosition.W, pos, posW);
            BindUtils.bind(CanonPosition.W, dtg, dtgW);
         }
      }
      GridBagConstraints c    = new GridBagConstraints();
      int                rows = 0;

      c.fill      = GridBagConstraints.BOTH;
      c.insets    = new Insets(1, 1, 1, 1);
      c.weighty   = 0.2;
      c.ipadx     = 5;
      c.ipady     = 5;
      c.gridwidth = 1;
      if (setup.hasXAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(xHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posX, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.X), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgX, c);
      }
      if (setup.hasYAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(yHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posY, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.Y), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgY, c);
      }
      if (setup.hasZAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(zHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posZ, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.Z), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgZ, c);
      }
      if (setup.hasAAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(aHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posA, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.A), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgA, c);
      }
      if (setup.hasBAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(bHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posB, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.B), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgB, c);
      }
      if (setup.hasCAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(cHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posC, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.C), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgC, c);
      }
      if (setup.hasUAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(uHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posU, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.U), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgU, c);
      }
      if (setup.hasVAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(vHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posV, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.V), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgV, c);
      }
      if (setup.hasWAxis()) {
         c.gridx   = 0;
         c.gridy   = rows++;
         c.weightx = 0;
         add(wHomed, c);

         c.gridx   = 1;
         c.weightx = 0.5;
         add(posW, c);

         c.gridx   = 2;
         c.weightx = 0;
         add(new AxisLetter(CanonPosition.W), c);

         c.gridx   = 3;
         c.weightx = 0.5;
         add(dtgW, c);
      }
      propertyChange(null);
   }


   protected void setDROSize(DRO p, DRO d, Dimension size) {
      p.setMinimumSize(size);
      p.setPreferredSize(size);
      d.setMinimumSize(size);
      d.setPreferredSize(size);
   }


   private AppSetup            setup;
   private DRO                 posX;
   private DRO                 posY;
   private DRO                 posZ;
   private DRO                 posA;
   private DRO                 posB;
   private DRO                 posC;
   private DRO                 posU;
   private DRO                 posV;
   private DRO                 posW;
   private DRO                 dtgX;
   private DRO                 dtgY;
   private DRO                 dtgZ;
   private DRO                 dtgA;
   private DRO                 dtgB;
   private DRO                 dtgC;
   private DRO                 dtgU;
   private DRO                 dtgV;
   private DRO                 dtgW;
   private LED                 xHomed;
   private LED                 yHomed;
   private LED                 zHomed;
   private LED                 aHomed;
   private LED                 bHomed;
   private LED                 cHomed;
   private LED                 uHomed;
   private LED                 vHomed;
   private LED                 wHomed;
   private Font                absFont;
   private Font                relFont;
   private Color               absForeground;
   private Color               relForeground;
   private Color               absBackground;
   private Color               relBackground;
   private ValueModel<Boolean> absPosition;
   private static final long   serialVersionUID = 1L;
}
