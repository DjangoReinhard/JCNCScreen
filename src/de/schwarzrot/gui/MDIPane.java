package de.schwarzrot.gui;
/* 
 * **************************************************************************
 * 
 *  file:       MDIPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    20.10.2019 by Django Reinhard
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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.schwarzrot.bean.ITheme;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.system.CommandWriter;


public class MDIPane extends JPanel {
   public MDIPane(ITheme settings, CommandWriter cmdWriter) {
      this.settings  = settings;
      this.cmdWriter = cmdWriter;
      createComponents(settings);
   }


   protected void createComponents(ITheme settings) {
      setLayout(new BorderLayout());
      mdiHistory = new JTextArea();
      mdiHistory.setEditable(false);
      mdiHistory.setForeground(UITheme.getColor("MDI:foreground"));
      mdiHistory.setBackground(UITheme.getColor("MDI:background"));
      JPanel cmdPane = new JPanel();

      cmdPane.setLayout(new BoxLayout(cmdPane, BoxLayout.LINE_AXIS));
      JScrollPane sp = new JScrollPane(mdiHistory);

      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      add(sp, BorderLayout.CENTER);
      command = new JTextField();
      command.setForeground(UITheme.getColor("MDI:foreground"));
      command.setBackground(UITheme.getColor("MDI:background"));
      btExecCmd = new JButton(LCStatus.getStatus().lm("execute"));

      cmdPane.add(command);
      cmdPane.add(btExecCmd);
      add(cmdPane, BorderLayout.SOUTH);
   }


   private ITheme            settings;
   private CommandWriter     cmdWriter;
   private JTextArea         mdiHistory;
   private JTextField        command;
   private JButton           btExecCmd;
   private static final long serialVersionUID = 1L;
}
