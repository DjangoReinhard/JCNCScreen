package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       ErrorReader.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    7.10.2019 by Django Reinhard
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.schwarzrot.model.ValueModel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.GlazedListsSwing;


public class ErrorReader implements ActionListener {
   public ErrorReader(List<SystemMessage> errorLog) {
      this.errorLog            = GlazedListsSwing.swingThreadProxyList((EventList<SystemMessage>) errorLog);
      this.confirmButtonActive = false;
      if (init() < 0)
         backendAvailable = false;
      else
         backendAvailable = true;
   }


   @Override
   public void actionPerformed(ActionEvent arg0) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ActionListener[] als = btConfirmMessage.getActionListeners();

            for (ActionListener al : als) {
               btConfirmMessage.removeActionListener(al);
            }
            for (ActionListener al : oldActionListeners) {
               btConfirmMessage.addActionListener(al);
            }
            btConfirmMessage.setText(oldBtText);
            btConfirmMessage.setActionCommand(oldActionCommand);
            // btConfirmMessage.invalidate();
            confirmButtonActive = false;
            messageField.setBackground(Color.lightGray);
            messageField.setForeground(Color.GRAY);
            errorActive.setValue(false);
            messageField.setText(clearMessage);
         }
      });
   }


   public List<SystemMessage> getLog() {
      return errorLog;
   }


   public boolean isBackendAvailable() {
      return backendAvailable;
   }


   public void riseError(SystemMessage sm) {
      switch (sm.getType()) {
         case NMLError:
         case OperatorError:
            flagError();
            break;
         case NMLText:
         case OperatorText:
            flagText();
            break;
         case NMLDisplay:
         case OperatorDisplay:
            flagDisplay();
            break;
      }
      errorActive.setValue(true);
      errorLog.add(sm);
      messageField.setText(sm.getMessage());
      activateConfirmButton();
   }


   public void setErrorSignal(ValueModel<Boolean> errorFlag) {
      errorActive = errorFlag;
   }


   public void setUIComponents(JTextField tf, JButton button) {
      messageField     = tf;
      btConfirmMessage = button;
   }


   protected void activateConfirmButton() {
      if (!confirmButtonActive) {
         confirmButtonActive = true;
         oldBtText           = btConfirmMessage.getText();
         oldActionListeners  = btConfirmMessage.getActionListeners();
         oldActionCommand    = btConfirmMessage.getActionCommand();
         for (ActionListener al : oldActionListeners) {
            btConfirmMessage.removeActionListener(al);
         }
         btConfirmMessage.setActionCommand(" ");
         btConfirmMessage.setText(confirmText);
         btConfirmMessage.addActionListener(this);
      }
   }


   protected native SystemMessage fetchMessage();


   protected void flagDisplay() {
      messageField.setBackground(Color.YELLOW);
      messageField.setForeground(Color.BLACK);
   }


   protected void flagError() {
      messageField.setBackground(Color.RED);
      messageField.setForeground(Color.YELLOW);
   }


   protected void flagText() {
      messageField.setBackground(Color.GREEN);
      messageField.setForeground(Color.YELLOW);
   }


   protected void resetError() {
      messageField.setBackground(Color.LIGHT_GRAY);
      messageField.setForeground(Color.GRAY);
      errorActive.setValue(false);
      messageField.setText(" ");
   }


   private native int init();


   private ValueModel<Boolean> errorActive;
   private JTextField          messageField;
   private JButton             btConfirmMessage;
   private String              oldBtText;
   private String              oldActionCommand;
   private ActionListener[]    oldActionListeners;
   private boolean             confirmButtonActive;
   private boolean             backendAvailable;
   private List<SystemMessage> errorLog;
   private static final String confirmText  = "Verstanden";
   private static final String clearMessage = " ";
   static {
      //      System.loadLibrary("linuxcncini");
      System.loadLibrary("nml");
      System.loadLibrary("linuxcnchal");
      System.loadLibrary("LinuxCNC");
   }
}
