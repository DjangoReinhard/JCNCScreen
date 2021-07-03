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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.GCodeLine;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.PaneStack;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.util.Preview3DCreator;
import de.schwarzrot.widgets.jme3.ICreator3D;

import ca.odell.glazedlists.BasicEventList;
import layout.SpringUtilities;


public class AutoGCodeLister extends GCodeLister {
   abstract class ThreadReader extends Thread implements Runnable {
      ThreadReader(InputStream is) {
         isr = new InputStreamReader(is);
      }


      InputStreamReader isr;
   }


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
                             LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode)
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
                             LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode)
                                   .setValue(ApplicationMode.AmAuto);
                          }


                          private static final long serialVersionUID = 1L;
                       };
      appSetup         = LCStatus.getStatus().getSetup();
      loading          = false;
   }


   @Override
   public void loadFile(String fileName) throws FileNotFoundException {
      //      Thread.dumpStack();
      long start = System.currentTimeMillis();
      if (loading) {
         l.log(Level.SEVERE, "--- loadFile called, but loading is on the way ...");
         return;
      } else {
         l.log(Level.INFO, "+++ loadFile called from idle state ...");
      }
      loading = true;
      super.loadFile(fileName);
      ICreator3D preview3DCreator = LCStatus.getStatus().getApp().getJME3App();

      if (preview3DCreator != null) {
         ProcessBuilder pb = new ProcessBuilder(rs274, "-g", "-v", appSetup.getVarFile().getAbsolutePath(),
               "-i", appSetup.getIniFile().getAbsolutePath(), "-t",
               appSetup.getToolTableFile().getAbsolutePath());
         Process        ipProcess;

         for (String part : pb.command()) {
            l.log(Level.INFO, "processBuilder: " + part);
         }
         try {
            ipProcess = pb.start();
            new ThreadReader(ipProcess.getInputStream()) {
               @Override
               public void run() {
                  preview3DCreator.processGeomFile(isr);
               }
            }.start();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ipProcess.getOutputStream()));

            createIntermediateFile(bw);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      long end = System.currentTimeMillis();

      l.log(Level.INFO, "load gcode file took: " + (end - start) + "ms");
      loading = false;
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      int selected = table.getSelectedRow();

      //      l.log(Level.INFO, "list selection changed to #" + selected);
      if (selected > 2) {
         cellRect    = table.getCellRect(selected - 3, 0, true);
         cellRect.y += viewPort.getExtentSize().height;
         table.scrollRectToVisible(cellRect);
      }
      if (app != null && selected >= 0)
         app.update(selected + 1);
   }


   @Override
   protected void addComponents(JPanel p, CommandWriter cmdWriter) {
      GridBagConstraints c = new GridBagConstraints();

      app = LCStatus.getStatus().getApp().getJME3App();
      if (app != null) {
         Preview3DPane preview3D = new Preview3DPane(app);

         scrollPane.setPreferredSize(new Dimension(1000, 300));
         splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, preview3D, scrollPane);
         splitPane.setDividerLocation(800);
         splitPane.setDividerSize(2);
      }
      c.gridx   = 0;
      c.gridy   = 0;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 0;
      p.add(createAddon(cmdWriter), c);

      c.gridy   = 1;
      c.weighty = 1;
      if (app != null)
         p.add(splitPane, c);
      else
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
            LCStatus.getStatus().getModel(LCStatus.MN_ApplicationMode)
                  .setValue(ApplicationMode.AmFileManager);
            FileManager fm = PaneStack.getInstance().getFileManager();

            fm.setOnEnterAction(loadFileAction);
            fm.setOnAbortAction(abortFileManager);
         }
      });
      // processTime = new TimeLabel(settings);
      fileName = new JTextField();
      fileName.setEditable(false);
      fileName.setFocusable(false);
      Font  f  = UITheme.getFont(UITheme.GCode_filename_font);
      Color cb = UITheme.getColor(UITheme.GCode_filename_background);
      Color cf = UITheme.getColor(UITheme.GCode_filename_foreground);

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


   protected void createIntermediateFile(BufferedWriter bw) {
      try {
         for (GCodeLine gl : lines) {
            String        tmp = gl.getLine().strip().toUpperCase();
            StringBuilder sb  = new StringBuilder("N");

            if (tmp.startsWith("N")) {
               int n = 1;
               for (; n < tmp.length(); ++n) {
                  if (!Character.isDigit(tmp.charAt(n)))
                     break;
               }
               tmp = tmp.substring(n);
            }
            sb.append(gl.getLineNumber()).append(" ");
            sb.append(tmp);
            bw.write(sb.toString());
            bw.newLine();
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (bw != null) {
            try {
               bw.close();
            } catch (IOException e) {
            }
         }
      }
   }


   private Action            abortFileManager;
   private Action            loadFileAction;
   private JSplitPane        splitPane;
   private Rectangle         cellRect;
   private AppSetup          appSetup;
   private Preview3DCreator  app;
   private boolean           loading;
   private String            rs274            = "/usr/local/src/linuxcnc-dev/bin/rs274";
   //   private String            rs274            = "/usr/local/src/linuxcnc.beauty/bin/rs274";
   private static Logger     l;
   private static final long serialVersionUID = 1L;
   static {
      l = Logger.getLogger(AutoGCodeLister.class.getName());
   }
}
