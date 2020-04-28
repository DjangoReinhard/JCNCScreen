package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       AutoGCodeLister.java
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


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.GCodeLine;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.PaneStack;
import de.schwarzrot.system.CommandWriter;

import ca.odell.glazedlists.BasicEventList;
import layout.SpringUtilities;


public class AutoGCodeLister extends GCodeLister {
   public AutoGCodeLister(CommandWriter cmdWriter) {
      super(cmdWriter, new BasicEventList<GCodeLine>());
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      loadFileAction   = new AbstractAction() {
                          @SuppressWarnings("unchecked")
                          @Override
                          public void actionPerformed(ActionEvent e) {
                             Object s = e.getSource(); // SimpleFile

                             cmdWriter.loadGCodeFile(((File) s).getAbsoluteFile());
                             LCStatus.getStatus().getGCodeInfo().setBackendLine(0);
                             LCStatus.getStatus().getGCodeInfo().setFrontendLine(0);
                             LCStatus.getStatus().getModel("applicationMode")
                                   .setValue(ApplicationMode.AmAuto);
                          }


                          private static final long serialVersionUID = 1L;
                       };
      abortFileManager = new AbstractAction() {
                          @SuppressWarnings("unchecked")
                          @Override
                          public void actionPerformed(ActionEvent e) {
                             LCStatus.getStatus().getGCodeInfo().setBackendLine(0);
                             LCStatus.getStatus().getGCodeInfo().setFrontendLine(0);
                             LCStatus.getStatus().getModel("applicationMode")
                                   .setValue(ApplicationMode.AmAuto);
                          }


                          private static final long serialVersionUID = 1L;
                       };
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      int selected = table.getSelectedRow();

      if (selected > 2) {
         Rectangle r   = table.getCellRect(selected - 3, 0, true);
         int       vph = viewPort.getExtentSize().height;

         r.y += vph;
         table.scrollRectToVisible(r);
      }
   }


   @Override
   protected void addComponents(JPanel p, CommandWriter cmdWriter) {
      GridBagConstraints c = new GridBagConstraints();

      c.gridx   = 0;
      c.gridy   = 0;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 0;
      p.add(createAddon(cmdWriter), c);

      c.gridy   = 1;
      c.weighty = 1;
      p.add(scrollPane, c);
      setValue(0);
   }


   @Override
   protected JComponent createAddon(CommandWriter cmdWriter) {
      JPanel  pane       = new JPanel(new SpringLayout());
      JButton btLoadFile = new JButton(LCStatus.getStatus().lm("file"));

      btLoadFile.addActionListener(new ActionListener() {
         @SuppressWarnings("unchecked")
         @Override
         public void actionPerformed(ActionEvent e) {
            LCStatus.getStatus().getModel("applicationMode").setValue(ApplicationMode.AmFileManager);
            FileManager fm = PaneStack.getInstance().getFileManager();

            fm.setOnEnterAction(loadFileAction);
            fm.setOnAbortAction(abortFileManager);
         }
      });
      // processTime = new TimeLabel(settings);
      fileName = new JTextField();
      fileName.setEditable(false);
      fileName.setFocusable(false);
      Font  f  = UITheme.getFont("GCode:filename.font");
      Color cb = UITheme.getColor("GCode:filename.background");
      Color cf = UITheme.getColor("GCode:filename.foreground");

      if (f != null)
         fileName.setFont(f);
      if (cb != null)
         fileName.setBackground(cb);
      if (cf != null)
         fileName.setForeground(cf);
      pane.add(fileName);
      // pane.add(processTime);
      pane.add(btLoadFile);
      SpringUtilities.makeCompactGrid(pane, 1, 2, 0, 0, 0, 0);

      return pane;
   }


   private Action            abortFileManager;
   private Action            loadFileAction;
   private static final long serialVersionUID = 1L;
}
