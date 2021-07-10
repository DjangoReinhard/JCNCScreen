package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       JogManager.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    10.5.2020 by Django Reinhard
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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.model.SpeedInfo;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.widgets.AbstractJogConditionButton;


public class MachineControl extends MouseAdapter
      implements ItemListener, MouseListener, ActionListener, MouseWheelListener {
   @SuppressWarnings("unchecked")
   private MachineControl(CommandWriter cmdWriter) {
      this.cmdWriter = cmdWriter;
      jogSingleStep  = new ValueModel<Boolean>("jogSingleStep", false);
      rapidJog       = new ValueModel<Boolean>("rapidJog", false);
      allHomed       = LCStatus.getStatus().getModel(LCStatus.MN_AllHomed);
      increments     = LCStatus.getStatus().getSetup().getDisplaySettings().getIncrements();
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if ("HomeAll".compareTo(e.getActionCommand()) == 0) {
         cmdWriter.homeAll();
      } else if ("startSpindleCW".compareTo(e.getActionCommand()) == 0) {
         SpeedInfo si    = LCStatus.getStatus().getSpeedInfo();
         int       speed = (int) (si.getSpindleNominalSpeed() * si.getSpindleFactor() / 100.0);

         cmdWriter.startSpindleCW(speed);
      } else if ("startSpindleCCW".compareTo(e.getActionCommand()) == 0) {
         SpeedInfo si    = LCStatus.getStatus().getSpeedInfo();
         int       speed = (int) (si.getSpindleNominalSpeed() * si.getSpindleFactor() / 100.0);

         cmdWriter.startSpindleCCW(speed);
      } else if ("stopSpindle".compareTo(e.getActionCommand()) == 0) {
         cmdWriter.stopSpindle();
      } else {
         System.err.println("unsupported action command: >" + e.getActionCommand() + "<");
      }
   }


   public ValueModel<Boolean> getAllHomedModel() {
      return allHomed;
   }


   public ValueModel<Boolean> getSingleStepModel() {
      return jogSingleStep;
   }


   @Override
   public void itemStateChanged(ItemEvent e) {
      Object source = e.getItemSelectable();

      if (source instanceof JCheckBox) {
         if ("singleStep".compareTo(((JCheckBox) source).getName()) == 0)
            jogSingleStep.setValue(((JCheckBox) source).isSelected());
         else if ("fastMove".compareTo(((JCheckBox) source).getName()) == 0)
            rapidJog.setValue(((JCheckBox) source).isSelected());
      }
   }


   @Override
   public void mouseClicked(MouseEvent e) {
      JComponent c = (JComponent) e.getSource();

      if (c.getName() == null) {
         if (c instanceof JButton && jogSingleStep.getValue()) {
            if (((JButton) e.getSource()).isEnabled()) {
               int    level    = ((AbstractJogConditionButton) (e.getSource())).getLevel();
               double speed    = getJogSpeed();
               double stepSize = increments[level];

               cmdWriter.jogStep(((JButton) e.getSource()).getText(), stepSize, speed);
            }
         }
      } else {
         if ("curFeed".compareTo(c.getName()) == 0 || "nomFeed".compareTo(c.getName()) == 0) {
            //         System.out.println("set feed to 0");
            cmdWriter.setFeedRate(0);
         } else if ("curRapid".compareTo(c.getName()) == 0) {
            //         System.out.println("set rapid move to 0");
            cmdWriter.setRapidRate(0);
         } else if ("curSpindle".compareTo(c.getName()) == 0 || "nomSpindle".compareTo(c.getName()) == 0) {
            //         System.out.println("set spindle-speed to 0");
            cmdWriter.setSpindleSpeedFactor(0);
         }
      }
   }


   @Override
   public void mousePressed(MouseEvent e) {
      JComponent c = (JComponent) e.getSource();

      if (c instanceof JButton && !jogSingleStep.getValue()) {
         if (((JButton) e.getSource()).isEnabled()) {
            double speed = getJogSpeed();

            cmdWriter.startJogging(((JButton) c).getText(), speed);
         }
      }
   }


   @Override
   public void mouseReleased(MouseEvent e) {
      JComponent c = (JComponent) e.getSource();

      if (c instanceof JButton && !jogSingleStep.getValue()) {
         if (((JButton) e.getSource()).isEnabled()) {
            cmdWriter.stopJogging(((JButton) e.getSource()).getText());
         }
      }
   }


   @Override
   public void mouseWheelMoved(MouseWheelEvent e) {
      JComponent c  = (JComponent) e.getSource();
      SpeedInfo  si = LCStatus.getStatus().getSpeedInfo();

      if (c.getName() == null)
         return;
      int su = e.getUnitsToScroll();

      if ("feedOverride".compareTo(c.getName()) == 0 || SpeedInfo.NominalFeed.compareTo(c.getName()) == 0
            || SpeedInfo.CurFeed.compareTo(c.getName()) == 0) {
         double ff = si.getFeedFactor() / 100.0;

         if (su < 0)
            ff += 0.01;
         else
            ff -= 0.01;
         //         System.out.println("feed changed by: " + su);
         cmdWriter.setFeedRate(ff);
      } else if ("rapidOverride".compareTo(c.getName()) == 0 || "curRapid".compareTo(c.getName()) == 0) {
         double rf = si.getRapidFactor() / 100.0;

         if (su < 0)
            rf += 0.01;
         else
            rf -= 0.01;
         //         System.out.println("rapid-feed changed by: " + su);
         cmdWriter.setRapidRate(rf);
      } else if ("spindleOverride".compareTo(c.getName()) == 0 || "curSpindle".compareTo(c.getName()) == 0
            || "nomSpindle".compareTo(c.getName()) == 0) {
         double sf = si.getSpindleFactor() / 100.0;

         if (su < 0)
            sf += 0.01;
         else
            sf -= 0.01;
         //         System.out.println("spindle-speed changed by: " + su);
         cmdWriter.setSpindleSpeedFactor(sf);
      }
   }


   protected double getJogSpeed() {
      SpeedInfo si    = LCStatus.getStatus().getSpeedInfo();

      double    feed  = si.getJogSpeed() / 60.0;
      double    ff    = si.getFeedFactor() / 100.0;
      double    sf    = si.getRapidFactor() / 100.0;
      double    speed = si.getMaxSpeed() / 60.0;

      System.err.println("feed: " + feed + " - feed-factor: " + ff);
      System.err.println("rapid-feed: " + speed + " - rapid-factor: " + sf);

      if (rapidJog.getValue()) {
         System.err.println("rapid jog requested");
         return speed * sf;
      } else {
         System.err.println("normal jog wanted");
         return feed * ff;
      }
   }


   public static MachineControl getInstance() {
      if (instance == null) {
         instance = new MachineControl(LCStatus.getStatus().getApp().getCommandWriter());
      }
      return instance;
   }


   private final CommandWriter   cmdWriter;
   private ValueModel<Boolean>   allHomed;
   private ValueModel<Boolean>   jogSingleStep;
   private ValueModel<Boolean>   rapidJog;
   private double[]              increments;
   private static MachineControl instance;
}
