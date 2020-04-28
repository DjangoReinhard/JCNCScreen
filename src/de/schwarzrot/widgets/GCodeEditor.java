package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       GCodeEditor.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    25.11.2019 by Django Reinhard
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


import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.GCodeLine;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.gui.PaneStack;
import de.schwarzrot.system.CommandWriter;

import ca.odell.glazedlists.BasicEventList;
import layout.SpringUtilities;


public class GCodeEditor extends GCodeLister {
   public GCodeEditor(CommandWriter cmdWriter) {
      super(cmdWriter, new BasicEventList<GCodeLine>());
      abortAction    = new AbstractAction() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void actionPerformed(ActionEvent e) {
                           LCStatus.getStatus().getModel("applicationMode").setValue(ApplicationMode.AmEdit);
                        }


                        private static final long serialVersionUID = 1L;
                     };
      loadFileAction = new AbstractAction() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void actionPerformed(ActionEvent e) {
                           Object s = e.getSource(); // SimpleFile

                           try {
                              loadFile(((File) s).getAbsolutePath());
                              SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                   table.requestFocus();
                                                }
                                             });
                           } catch (FileNotFoundException e1) {
                              e1.printStackTrace();
                           }
                           LCStatus.getStatus().getModel("applicationMode").setValue(ApplicationMode.AmEdit);
                        }


                        private static final long serialVersionUID = 1L;
                     };
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      int selected = table.getSelectedRow();

      if (selected >= 0) {
         try {
            int i = table.convertRowIndexToModel(selected);

            curLine = lines.get(i);
         } catch (IndexOutOfBoundsException ex) {
         }
         if (curLine != null)
            editField.setText(curLine.getLine());
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
      p.add(createTopAddon(cmdWriter), c);

      c.gridy   = 1;
      c.weighty = 1;
      p.add(scrollPane, c);

      c.gridy   = 2;
      c.weighty = 0;
      p.add(createBottomAddon(cmdWriter), c);
   }


   protected JComponent createBottomAddon(CommandWriter cmdWriter) {
      JPanel pane = new JPanel(new SpringLayout());

      editField = new JTextField();
      btInsert  = new JButton(LCStatus.getStatus().lm("insert"));
      btInsert.setMnemonic(KeyEvent.VK_INSERT);
      btReplace = new JButton(LCStatus.getStatus().lm("replace"));
      btReplace.setMnemonic(KeyEvent.VK_F6);
      btSearch = new JButton(LCStatus.getStatus().lm("search"));
      btSearch.setMnemonic(KeyEvent.VK_CONTROL | KeyEvent.VK_F);
      btRemove = new JButton(LCStatus.getStatus().lm("delete"));
      btRemove.setMnemonic(KeyEvent.VK_DELETE);
      btRemove.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            int selected = table.getSelectedRow();

            if (selected >= 0) {
               int i = table.convertRowIndexToModel(selected);

               lines.getReadWriteLock().writeLock().lock();
               lines.remove(i);
               renumberList(lines);
               lines.getReadWriteLock().writeLock().unlock();
               setValue(selected);
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     table.requestFocus();
                     setChanged();
                  }
               });
            }
         }
      });
      btInsert.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            String newLine  = editField.getText();
            int    selected = table.getSelectedRow();

            // System.out.println("should insert line >" + newLine + "<");
            lines.getReadWriteLock().writeLock().lock();
            if (selected >= 0) {
               int i = table.convertRowIndexToModel(selected);

               if (lines.size() > 0) {
                  System.out.println("previous line is: >" + lines.get(i) + "<");
                  lines.add(i + 1, new GCodeLine(i + 1, newLine));
               } else {
                  lines.add(new GCodeLine(i + 1, newLine));
               }
               setValue(i + 1);
            } else {
               // System.out.println("as first line!");
               lines.add(0, new GCodeLine(0, newLine));
               setValue(0);
            }
            renumberList(lines);
            lines.getReadWriteLock().writeLock().unlock();
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  table.requestFocus();
                  setChanged();
               }
            });
         }
      });
      btReplace.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String newLine  = editField.getText();
            int    selected = table.getSelectedRow();

            if (selected >= 0) {
               int i = table.convertRowIndexToModel(selected);

               // System.out.println("should replace line >" + lines.get(i) + "<");
               // System.out.println("with line: >" + newLine + "<");
               lines.get(i).setLine(newLine);
               updateUI();
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     table.requestFocus();
                     setChanged();
                  }
               });
            }
         }
      });
      btSearch.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String searchPattern = LCStatus.getStatus().getApp()
                  .inputDialog(LCStatus.getStatus().lm("Search"), LCStatus.getStatus().lm("search4"));

            if (searchPattern != null) {
               for (long i = curLine.getLineNumber(); i < lines.size(); ++i) {
                  if (lines.get((int) i).getLine().contains(searchPattern)) {
                     int s = table.convertRowIndexToView((int) i);
                     table.changeSelection(s, 1, true, false);
                     break;
                  }
               }
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     editField.requestFocus();
                  }
               });
            }
         }
      });
      editField.setFont(UITheme.getFont("GCode:edit.font"));
      editField.setForeground(UITheme.getColor("GCode:edit.foreground"));
      editField.setBackground(UITheme.getColor("GCode:edit.background"));
      pane.add(btRemove);
      pane.add(btReplace);
      pane.add(editField);
      pane.add(btInsert);
      pane.add(btSearch);
      SpringUtilities.makeCompactGrid(pane, 1, 5, 0, 0, 0, 0);

      return pane;
   }


   protected JComponent createTopAddon(CommandWriter cmdWriter) {
      JPanel  pane       = new JPanel(new SpringLayout());
      JButton btLoadFile = new JButton(LCStatus.getStatus().lm("file"));

      btSaveFile = new JButton(LCStatus.getStatus().lm("save"));
      btLoadFile.addActionListener(new ActionListener() {
         @SuppressWarnings("unchecked")
         @Override
         public void actionPerformed(ActionEvent e) {
            LCStatus.getStatus().getModel("applicationMode").setValue(ApplicationMode.AmFileManager);
            FileManager fm = PaneStack.getInstance().getFileManager();

            fm.setOnEnterAction(loadFileAction);
            fm.setOnAbortAction(abortAction);
         }
      });
      btSaveFile.setEnabled(false);
      btSaveFile.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String file2Save = fileName.getText();

            try {
               writeGCodeFile(file2Save, lines);
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     btSaveFile.setEnabled(false);
                     fileName.setForeground(UITheme.getColor("GCode:filename.foreground"));
                  }
               });
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      });
      fileName = new JTextField();
      fileName.setEditable(false);
      fileName.setFocusable(false);
      fileName.setFont(UITheme.getFont("GCode:filename.font"));
      fileName.setBackground(UITheme.getColor("GCode:filename.background"));
      fileName.setForeground(UITheme.getColor("GCode:filename.foreground"));
      pane.add(fileName);
      pane.add(btLoadFile);
      pane.add(btSaveFile);
      SpringUtilities.makeCompactGrid(pane, 1, 3, 0, 0, 0, 0);

      return pane;
   }


   protected void setChanged() {
      if (btSaveFile.isEnabled())
         return;
      fileName.setForeground(UITheme.getColor("GCode:filename.changed.foreground"));
      btSaveFile.setEnabled(true);
   }


   protected void writeGCodeFile(String fileName, List<GCodeLine> content)
         throws IOException, UnsupportedEncodingException {
      System.out.println("should save file >" + fileName + "<");
      File        out = new File(fileName);
      PrintWriter pw  = new PrintWriter(out, "UTF-8");

      for (GCodeLine l : content) {
         pw.println(l.getLine());
      }
      pw.flush();
      pw.close();
   }


   private JButton           btInsert;
   private JButton           btReplace;
   private JButton           btSearch;
   private JButton           btRemove;
   private JButton           btSaveFile;
   private GCodeLine         curLine;
   private Action            abortAction;
   private Action            loadFileAction;
   private static final long serialVersionUID = 1L;
}
