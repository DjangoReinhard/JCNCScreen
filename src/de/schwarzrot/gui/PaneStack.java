package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       PaneStack.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    27.10.2019 by Django Reinhard
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


import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.AndCondition;
import de.schwarzrot.logic.EqualCondition;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.nml.TaskMode;
import de.schwarzrot.nml.TaskState;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.system.ErrorReader;
import de.schwarzrot.util.BindUtils;
import de.schwarzrot.widgets.AutoGCodeLister;
import de.schwarzrot.widgets.FileManager;
import de.schwarzrot.widgets.GCodeEditor;
import de.schwarzrot.widgets.GCodeLister;
import de.schwarzrot.widgets.ToolTable;


public class PaneStack extends JPanel implements HierarchyListener, PropertyChangeListener {
   @SuppressWarnings("unchecked")
   private PaneStack(CommandWriter cmdWriter) {
      super(new CardLayout());
      this.cmdWriter = cmdWriter;
      appMode        = LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode);
      appMode.addPropertyChangeListener(this);
      taskMode = LCStatus.getStatus().getModel(LCStatus.MN_TaskMode);
      taskMode.addPropertyChangeListener(this);
      execState = LCStatus.getStatus().getModel(LCStatus.MN_ExecState);
      execState.addPropertyChangeListener(this);
      allHomed = LCStatus.getStatus().getModel(LCStatus.MN_AllHomed);
      allHomed.addPropertyChangeListener(this);
      paneStack = (CardLayout) getLayout();
   }


   @Override
   public void add(Component comp, Object constraints) {
      super.add(comp, constraints);
      comp.addHierarchyListener(this);
   }


   public String getCurrentPage() {
      return currentPage.name();
   }


   public GCodeEditor getEditGCodePane() {
      return editGCodePane;
   }


   public FileManager getFileManager() {
      return fileManager;
   }


   public FixturePane getFixturePane() {
      return fixturePane;
   }


   public AutoGCodeLister getGcodeLister() {
      return gcodeLister;
   }


   public JComponent getManualPane() {
      return manualPane;
   }


   public GCodeLister getMdiPane() {
      return mdiPane;
   }


   public MessageLogPane getMessageLogPane() {
      return messageLogPane;
   }


   public CardLayout getPaneStack() {
      return paneStack;
   }


   public JComponent getSettingsPane() {
      return settingsPane;
   }


   public ToolTable getToolTable() {
      return toolTable;
   }


   public JComponent getTouchPane() {
      return touchPane;
   }


   public JComponent getWheelPane() {
      return wheelPane;
   }


   @Override
   public void hierarchyChanged(HierarchyEvent e) {
      JComponent component = (JComponent) e.getSource();

      if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0 && component.isShowing()) {
         //         System.err.println("hierarchy changed to: " + component);
         component.transferFocus();
      }
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if (LCStatus.MN_ApplicationMode.compareTo(evt.getPropertyName()) == 0) {
         ApplicationMode am = (ApplicationMode) evt.getNewValue();

         validateAppMode(am);
         selectPane(am);
      } else if (LCStatus.MN_TaskMode.compareTo(evt.getPropertyName()) == 0) {
         TaskMode tm = (TaskMode) evt.getNewValue();

         switch (tm) {
            case TaskModeAuto:
               appMode.setValue(ApplicationMode.AmAuto);
               break;
            case TaskModeMDI:
               appMode.setValue(ApplicationMode.AmMDI);
               break;
            case TaskModeManual:
               if (appMode.getValue() == ApplicationMode.AmAuto
                     || appMode.getValue() == ApplicationMode.AmMDI) {
                  appMode.setValue(ApplicationMode.AmManual);
               }
               break;
         }
      } else if (LCStatus.MN_AllHomed.compareTo(evt.getPropertyName()) == 0) {
         if ((boolean) evt.getNewValue())
            cmdWriter.setTaskModeAuto();
      }
   }


   protected void selectPane(ApplicationMode mode) {
      //      System.err.println("select pane from Stack: " + mode.name());

      currentPage = mode;
      paneStack.show(this, mode.name());
   }


   protected void validateAppMode(ApplicationMode am) {
      //TODO: check es
      switch (taskMode.getValue()) {
         case TaskModeAuto:
            if (am != ApplicationMode.AmAuto) {
               //               System.err.println("validate appmode: appMode == " + am + " - taskmode is auto ...");

               appMode.setValue(ApplicationMode.AmAuto);
            }
            break;
         case TaskModeMDI:
            if (am != ApplicationMode.AmMDI) {
               //               System.err.println("validate appmode: appMode == " + am + " - taskmode is mdi ...");

               appMode.setValue(ApplicationMode.AmMDI);
            }
            break;
      }
   }


   private void createUI(ErrorReader errorReader) {
      LCStatus            status      = LCStatus.getStatus();
      AppSetup            setup       = status.getSetup();
      ValueModel<Boolean> errorActive = status.getModel(LCStatus.MN_ErrorActive);

      fileManager    = new FileManager(LCStatus.getStatus().lm("NC-files"),
            UITheme.getFile(UITheme.GCode_basedir));
      gcodeLister    = new AutoGCodeLister(cmdWriter);
      //TODO:
      manualPane     = new JogPane(setup,
            new AndCondition(
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), false)),
            new AndCondition(new ICondition[] {
                  new EqualCondition<TaskState>(status.getModel(LCStatus.MN_TaskState), TaskState.MachineOn),
                  new EqualCondition<Boolean>(status.getModel(LCStatus.MN_AllHomed), true),
                  new EqualCondition<Boolean>(errorActive, false),
                  new EqualCondition<ApplicationMode>(appMode, ApplicationMode.AmManual) }));

      messageLogPane = new MessageLogPane(errorReader.getLog());
      mdiPane        = new GCodeLister(cmdWriter, LCStatus.getStatus().getMDIHistory());
      // // TODO
      // wheelPane = new JLabel("Wheely? ... not yet!");

      // TODO
      touchPane      = new JLabel("touch? ... not yet!");
      BindUtils.bind("frontendLine", LCStatus.getStatus().getGCodeInfo(), gcodeLister);
      BindUtils.bind("fileName", LCStatus.getStatus().getGCodeInfo(), gcodeLister.getFileHandler());

      add(gcodeLister, ApplicationMode.AmAuto.name());
      add(mdiPane, ApplicationMode.AmMDI.name());
      add(manualPane, ApplicationMode.AmManual.name());
      add(touchPane, ApplicationMode.AmTouch.name());
      add(messageLogPane, ApplicationMode.AmMessageLog.name());
      // add(wheelPane, ApplicationMode.AmWheel.name());
      editGCodePane = new GCodeEditor(cmdWriter);
      settingsPane  = new AppSettingsPane(cmdWriter);

      add(editGCodePane, ApplicationMode.AmEdit.name());
      add(settingsPane, ApplicationMode.AmSettings.name());
      add(fileManager, ApplicationMode.AmFileManager.name());
      appMode.setValue(ApplicationMode.AmManual);
      selectPane(ApplicationMode.AmManual);
   }


   public static PaneStack getInstance() {
      return instance;
   }


   public static PaneStack getInstance(CommandWriter cmdWriter, ErrorReader errorReader) {
      if (instance == null) {
         instance = new PaneStack(cmdWriter);
         instance.createUI(errorReader);
      }
      return instance;
   }


   private CardLayout                  paneStack;
   private AutoGCodeLister             gcodeLister;
   private ToolTable                   toolTable;
   private FixturePane                 fixturePane;
   private JComponent                  touchPane;
   private GCodeLister                 mdiPane;
   private JComponent                  manualPane;
   private JComponent                  wheelPane;
   private GCodeEditor                 editGCodePane;
   private JComponent                  settingsPane;
   private MessageLogPane              messageLogPane;
   private FileManager                 fileManager;
   private CommandWriter               cmdWriter;
   private ValueModel<ApplicationMode> appMode;
   private ValueModel<TaskMode>        taskMode;
   private ValueModel<Integer>         execState;
   private ValueModel<Boolean>         allHomed;
   private ApplicationMode             currentPage;
   private static PaneStack            instance;
   private static final long           serialVersionUID = 1L;
}
