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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.widgets.AbstractJogConditionButton;


public class JogManager extends MouseAdapter implements ItemListener, MouseListener, ActionListener {
   @SuppressWarnings("unchecked")
   private JogManager(CommandWriter cmdWriter) {
      this.cmdWriter = cmdWriter;
      jogSingleStep  = new ValueModel<Boolean>("jogSingleStep", false);
      allHomed       = LCStatus.getStatus().getModel("allHomed");
      increments     = LCStatus.getStatus().getSetup().getDisplaySettings().getIncrements();
      allHomed.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent e) {
            if ("allHomed".compareTo(e.getPropertyName()) == 0) {
               if (!((Boolean) e.getOldValue()) && ((Boolean) e.getNewValue())) {
                  cmdWriter.setTaskModeAuto();
               }
            }
         }
      });
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().compareTo("HomeAll") == 0) {
         cmdWriter.homeAll();
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
         jogSingleStep.setValue(((JCheckBox) source).isSelected());
      }
   }


   @Override
   public void mouseClicked(MouseEvent e) {
      if (jogSingleStep.getValue()) {
         if (((JButton) e.getSource()).isEnabled()) {
            int    level    = ((AbstractJogConditionButton) (e.getSource())).getLevel();
            double speed    = LCStatus.getStatus().getSpeedInfo().getNominalFeed() / 60.0;
            double stepSize = increments.get(level);

            cmdWriter.jogStep(((JButton) e.getSource()).getText(), stepSize, speed);
         }
      }
   }


   @Override
   public void mousePressed(MouseEvent e) {
      if (!jogSingleStep.getValue()) {
         if (((JButton) e.getSource()).isEnabled()) {
            double speed = LCStatus.getStatus().getSpeedInfo().getNominalFeed() / 60.0;

            cmdWriter.startJogging(((JButton) e.getSource()).getText(), speed);
         }
      }
   }


   @Override
   public void mouseReleased(MouseEvent e) {
      if (!jogSingleStep.getValue()) {
         if (((JButton) e.getSource()).isEnabled()) {
            cmdWriter.stopJogging(((JButton) e.getSource()).getText());
         }
      }
   }


   public static JogManager getInstance() {
      if (instance == null) {
         instance = new JogManager(LCStatus.getStatus().getApp().getCommandWriter());
      }
      return instance;
   }


   private final CommandWriter cmdWriter;
   private ValueModel<Boolean> allHomed;
   private ValueModel<Boolean> jogSingleStep;
   private List<Double>        increments;
   private static JogManager   instance;
}
