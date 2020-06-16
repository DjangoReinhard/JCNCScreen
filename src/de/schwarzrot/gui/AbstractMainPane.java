package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       AbstractMainPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    3.11.2019 by Django Reinhard
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


import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.AndCondition;
import de.schwarzrot.logic.EqualCondition;
import de.schwarzrot.logic.FalseCondition;
import de.schwarzrot.logic.GreaterCondition;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.logic.NotCondition;
import de.schwarzrot.logic.OrCondition;
import de.schwarzrot.logic.SmallerCondition;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.BufferDescriptor;
import de.schwarzrot.nml.InterpState;
import de.schwarzrot.nml.TaskExecState;
import de.schwarzrot.nml.TaskMode;
import de.schwarzrot.nml.TaskState;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.system.ErrorReader;
import de.schwarzrot.system.MachineControl;
import de.schwarzrot.widgets.ConditionButton;
import de.schwarzrot.widgets.PowerButton;
import de.schwarzrot.widgets.SoftkeyButton;

import layout.SpringUtilities;


public abstract class AbstractMainPane extends JDesktopPane
      implements ActionListener, PropertyChangeListener {
   class PowerHandler implements ActionListener {
      protected PowerHandler(LCStatus status, CommandWriter cmdWriter) {
         this.status    = status;
         this.cmdWriter = cmdWriter;
      }


      @SuppressWarnings("unchecked")
      @Override
      public void actionPerformed(ActionEvent arg0) {
         if (status.getModel(LCStatus.MN_TaskState).getValue() == TaskState.EStop) {
            cmdWriter.clearEStop();
         } else {
            switch ((TaskState) status.getModel(LCStatus.MN_TaskState).getValue()) {
               case EStop:
                  cmdWriter.clearEStop();
                  break;
               case EStopReset:
               case MachineOff:
                  cmdWriter.machinePowerON();
                  break;
               case MachineOn:
               default:
                  cmdWriter.machinePowerOff();
                  status.getModel(LCStatus.MN_TaskMode).setValue(TaskMode.TaskModeManual);
                  appMode.setValue(ApplicationMode.AmMachineOff);
                  break;
            }
         }
      }


      private LCStatus      status;
      private CommandWriter cmdWriter;
   }


   @SuppressWarnings("unchecked")
   protected AbstractMainPane(CommandWriter cmdWriter, ErrorReader errorReader) {
      setLayout(new GridBagLayout());
      setOpaque(true);
      setBackground(UITheme.getColor(UITheme.Main_grid_color));
      this.cmdWriter     = cmdWriter;
      this.errorReader   = errorReader;
      applicationButtons = new ButtonGroup();
      this.powerHandler  = new PowerHandler(LCStatus.getStatus(), cmdWriter);
      appMode            = LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode);
      absPosition        = LCStatus.getStatus().getModel("absPosition");
      appMode.addPropertyChangeListener(this);
      if (LCStatus.getStatus().getModel(LCStatus.MN_TaskState).getValue() == TaskState.MachineOn) {
         appMode.setValue(ApplicationMode.AmManual);
      } else {
         appMode.setValue(ApplicationMode.AmMachineOff);
      }
      errorActive = LCStatus.getStatus().getModel(LCStatus.MN_ErrorActive);
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().compareTo(ApplicationMode.AmAuto.name()) == 0) {
         cmdWriter.setTaskModeAuto();
         appMode.setValue(ApplicationMode.AmAuto);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmMDI.name()) == 0) {
         cmdWriter.setTaskModeMDI();
         appMode.setValue(ApplicationMode.AmMDI);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmEdit.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmEdit);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmTools.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmTools);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmOffsets.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmOffsets);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmTouch.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmTouch);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmManual.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmManual);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmWheel.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmWheel);
      } else if (e.getActionCommand().compareTo(ApplicationMode.AmSettings.name()) == 0) {
         cmdWriter.setTaskModeManual();
         appMode.setValue(ApplicationMode.AmSettings);
      }
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if (LCStatus.MN_ApplicationMode.compareTo(evt.getPropertyName()) == 0) {
         ApplicationMode am = (ApplicationMode) evt.getNewValue();

         if (am != ApplicationMode.AmMessageLog)
            oldAppMode = am;
      }
   }


   protected JComponent createAdditionalToolbar(int orientation) {
      JPanel buttonPane = new JPanel();

      setOpaque(true);
      buttonPane.setBackground(UITheme.getColor(UITheme.Toolbar_grid_color));
      buttonPane.setLayout(new BoxLayout(buttonPane, orientation));
      createToolbar2(buttonPane);

      return buttonPane;
   }


   protected JComponent createInfoPart() {
      JPanel pane = new JPanel();

      pane.setBackground(UITheme.getColor(UITheme.Info_grid_color));
      pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

      pane.add(new ToolInfoPane());
      pane.add(new SpeedPane());
      pane.add(new ActiveCodesPane());

      return pane;
   }


   protected JComponent createMainToolbar() {
      JPanel buttonPane = new JPanel();

      buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
      buttonPane.setOpaque(true);
      buttonPane.setBackground(UITheme.getColor(UITheme.Toolbar_grid_color));
      createToolbar1(buttonPane);
      createToolbar3(buttonPane);
      PowerButton pb = new PowerButton("images/SK_PowerOff.png", "images/SK_PowerOff_1.png",
            "images/SK_PowerOn.png");
      pb.addActionListener(powerHandler);
      buttonPane.add(pb);

      return buttonPane;
   }


   @SuppressWarnings("unchecked")
   protected JComponent createMessagePane() {
      JPanel pane = new JPanel(new SpringLayout());

      message = new JTextField();
      message.setFont(UITheme.getFont(UITheme.MessageLOG_message_font));
      message.setForeground(UITheme.getColor(UITheme.MessageLOG_message_foreground));
      message.setBackground(UITheme.getColor(UITheme.MessageLOG_message_background));
      message.setEditable(false);
      message.setBorder(new EmptyBorder(0, 15, 0, 0));
      message.setText(" ");
      JButton btHistory = new ConditionButton(LCStatus.getStatus().lm("messages"),
            new NotCondition<ApplicationMode>(LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode),
                  ApplicationMode.AmMachineOff));
      btHistory.setActionCommand(ApplicationMode.AmMessageLog.name());
      btHistory.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (ApplicationMode.AmMessageLog.name().compareTo(e.getActionCommand()) == 0) {
               btHistory.setActionCommand(oldAppMode.name());
               appMode.setValue(ApplicationMode.AmMessageLog);
               errorActive.setValue(true);
            } else {
               btHistory.setActionCommand(ApplicationMode.AmMessageLog.name());
               appMode.setValue(oldAppMode);
               errorActive.setValue(false);
            }
         }
      });
      applicationButtons.add(btHistory);
      pane.add(message);
      pane.add(btHistory);
      errorReader.setUIComponents(message, btHistory);
      SpringUtilities.makeCompactGrid(pane, 1, 2, 0, 0, 0, 0);

      return pane;
   }


   @SuppressWarnings("unchecked")
   protected JComponent createToolbar1(JPanel buttonPane) {
      AbstractButton sb     = null;
      LCStatus       status = LCStatus.getStatus();

      sb = new SoftkeyButton("images/SK_AutoStart.png", "images/SK_AutoStart_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new OrCondition(new GreaterCondition<Double>(status.getModel(LCStatus.MN_DTG), 0.0),
                  new GreaterCondition<Integer>(status.getModel(LCStatus.MN_InterpState),
                        InterpState.Reading.getStateNum())));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            switch (appMode.getValue()) {
               case AmMDI: {
                  String command = paneStack.getMdiPane().getMDICommand();

                  cmdWriter.execMDI(command);
                  paneStack.getMdiPane().registerCommand(command);
               }
                  break;

               case AmAuto: {
                  //                  int l = status.getGCodeInfo().getFrontendLine();
                  //
                  //                  if (l > 0) {
                  //                     System.err.println("gcode-line: #"
                  //                                        + paneStack.getGcodeLister()
                  //                                                   .getCurrentLine()
                  //                                        + " <> motion-line: #"
                  //                                        + l);
                  //                     cmdWriter.runFromLine(paneStack.getGcodeLister()
                  //                                                    .getCurrentLine());
                  //                  } else {
                  cmdWriter.execGCode();
                  //                  }
               }
                  break;
               default:
                  break;
            }
         }
      });
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_AutoPause.png", "images/SK_AutoPause_active.png",
            new AndCondition(new GreaterCondition<Integer>(status.getModel(LCStatus.MN_InterpState), 1),
                  new EqualCondition<Boolean>(errorActive, false)),
            new EqualCondition<Integer>(status.getModel(LCStatus.MN_InterpState), 3));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            cmdWriter.pauseGCodeExecution();
         }
      });
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_AutoStop.png", "images/SK_AutoStop_active.png",
            new AndCondition(new GreaterCondition<Integer>(status.getModel(LCStatus.MN_InterpState), 1),
                  new EqualCondition<Boolean>(errorActive, false)),
            new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                  TaskExecState.TaskExecWait4Motion.getStateNum()));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            cmdWriter.setTaskAbort();
            LCStatus.getStatus().getGCodeInfo().setBackendLine(0);
            LCStatus.getStatus().getGCodeInfo().setFrontendLine(0);
         }
      });
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_Auto.png", "images/SK_Auto_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new AndCondition(new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmAuto),
                  new EqualCondition<TaskMode>(status.getModel(LCStatus.MN_TaskMode),
                        TaskMode.TaskModeAuto)));
      sb.setActionCommand(ApplicationMode.AmAuto.name());
      sb.addActionListener(this);
      //      sb.setSelected(true);
      applicationButtons.add(sb);
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_Manual.png", "images/SK_Manual_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                        TaskExecState.TaskExecWait4Motion.getStateNum()),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmManual));
      sb.setActionCommand(ApplicationMode.AmManual.name());
      sb.addActionListener(this);
      applicationButtons.add(sb);
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_MDI.png", "images/SK_MDI_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                        TaskExecState.TaskExecWait4Motion.getStateNum()),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmMDI));
      sb.setActionCommand(ApplicationMode.AmMDI.name());
      sb.addActionListener(this);
      applicationButtons.add(sb);
      buttonPane.add(sb);

      sb = new SoftkeyButton("images/SK_Edit.png", "images/SK_Edit_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                        TaskExecState.TaskExecWait4Motion.getStateNum()),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmEdit));
      sb.addActionListener(this);
      sb.setActionCommand(ApplicationMode.AmEdit.name());
      applicationButtons.add(sb);
      buttonPane.add(sb);

      return buttonPane;
   }


   @SuppressWarnings("unchecked")
   protected JComponent createToolbar2(JPanel buttonPane) {
      LCStatus       status = LCStatus.getStatus();

      AbstractButton sb     = new SoftkeyButton("images/SK_SingleStep.png", "images/SK_SingleStep_active.png",
            new AndCondition(
                  new EqualCondition<TaskMode>(status.getModel(LCStatus.MN_TaskMode), TaskMode.TaskModeAuto),
                  new EqualCondition<Boolean>(errorActive, false)),
            new EqualCondition<Boolean>(status.getModel(LCStatus.MN_SingleStep), true));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            status.getModel(LCStatus.MN_SingleStep)
                  .setValue(!((Boolean) status.getModel(LCStatus.MN_SingleStep).getValue()));
         }

      });
      buttonPane.add(sb);
      sb = new SoftkeyButton("images/SK_Cool_Mist.png", "images/SK_Cool_Mist_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<Boolean>(status.getModel(BufferDescriptor.CoolMist), true));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            if (((AbstractButton) ae.getSource()).isSelected()) {
               cmdWriter.enableMist(false);
            } else {
               cmdWriter.enableMist(true);
            }
         }

      });
      buttonPane.add(sb);
      sb = new SoftkeyButton("images/SK_Cool_Flood.png", "images/SK_Cool_Flood_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<Boolean>(status.getModel(BufferDescriptor.CoolFlood), true));
      sb.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            if (((AbstractButton) ae.getSource()).isSelected()) {
               cmdWriter.enableFlood(false);
            } else {
               cmdWriter.enableFlood(true);
            }
         }

      });
      buttonPane.add(sb);

      ButtonGroup bg = new ButtonGroup();
      sb = new SoftkeyButton("images/SK_Spindle_CW.png", "images/SK_Spindle_CW_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<Integer>(status.getModel(LCStatus.MN_SpindleDir), 1));
      sb.setActionCommand("startSpindleCW");
      sb.addActionListener(MachineControl.getInstance());
      buttonPane.add(sb);
      bg.add(sb);
      sb = new SoftkeyButton("images/SK_Spindle_Stop.png", "images/SK_Spindle_Stop_active.png",
            new AndCondition(
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(errorActive, false)),
            new EqualCondition<Integer>(status.getModel(LCStatus.MN_SpindleDir), 0));
      sb.setActionCommand("stopSpindle");
      sb.addActionListener(MachineControl.getInstance());
      buttonPane.add(sb);
      bg.add(sb);
      sb = new SoftkeyButton("images/SK_Spindle_CCW.png", "images/SK_Spindle_CCW_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<Integer>(status.getModel(LCStatus.MN_SpindleDir), -1));
      sb.setActionCommand("startSpindleCCW");
      sb.addActionListener(MachineControl.getInstance());
      buttonPane.add(sb);
      bg.add(sb);

      return buttonPane;
   }


   @SuppressWarnings("unchecked")
   protected JComponent createToolbar3(JPanel buttonPane) {
      LCStatus       status = LCStatus.getStatus();
      AbstractButton sb     = new SoftkeyButton("images/SK_Settings.png", "images/SK_Settings_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                        TaskExecState.TaskExecWait4Motion.getStateNum()),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmSettings));
      sb.addActionListener(this);
      sb.setActionCommand(ApplicationMode.AmSettings.name());
      applicationButtons.add(sb);
      buttonPane.add(sb);
      sb = new SoftkeyButton("images/SK_Touch.png", "images/SK_Touch_active.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  // TODO: remove when touch is
                  // available
                  new FalseCondition(),
                  new SmallerCondition<Integer>(status.getModel(LCStatus.MN_ExecState),
                        TaskExecState.TaskExecWait4Motion.getStateNum()),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmTouch));
      sb.addActionListener(this);
      sb.setEnabled(false);
      sb.setActionCommand(ApplicationMode.AmTouch.name());
      applicationButtons.add(sb);
      buttonPane.add(sb);
      buttonPane.add(Box.createHorizontalGlue());

      SoftkeyButton sbPosRelative = new SoftkeyButton("images/SK_PosRelative.png",
            "images/SK_PosAbsolute.png",
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(errorActive, false) }),
            new EqualCondition<Boolean>(absPosition, true));
      sbPosRelative.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            status.getModel(LCStatus.MN_AbsPosition)
                  .setValue(!((Boolean) status.getModel(LCStatus.MN_AbsPosition).getValue()));
         }
      });
      buttonPane.add(sbPosRelative);

      buttonPane.add(Box.createHorizontalGlue());

      return buttonPane;
   }


   private JTextField                  message;
   private ValueModel<ApplicationMode> appMode;
   private ValueModel<Boolean>         absPosition;
   private ValueModel<Boolean>         errorActive;
   private ApplicationMode             oldAppMode;
   PowerHandler                        powerHandler;
   ButtonGroup                         applicationButtons;
   PaneStack                           paneStack;
   CommandWriter                       cmdWriter;
   ErrorReader                         errorReader;
   private static final long           serialVersionUID = 1L;
}
