package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       GCodeLister.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.9.2019 by Django Reinhard
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import de.schwarzrot.bean.GCodeLine;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.format.GCodeTableFormat;
import de.schwarzrot.model.IValueClient;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.text.GCodeSource;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import layout.SpringUtilities;


public class GCodeLister extends JPanel implements ListSelectionListener, IValueClient {
   static class GCodeFileHandler implements IValueClient {
      protected GCodeFileHandler(GCodeLister lister) {
         this.lister = lister;
      }


      @Override
      public void setValue(Object value) {
         if (value != null && value instanceof String) {
            gcodeFile = new File((String) value);

            if (gcodeFile.exists() && gcodeFile.canRead()) {
               try {
                  lister.loadFile(gcodeFile.getAbsolutePath());
               } catch (FileNotFoundException e) {
                  e.printStackTrace();
               }
            }
         }
      }


      private GCodeLister lister;
      private File        gcodeFile;
   }
   static class GCodeLineNumberRenderer extends DefaultTableCellRenderer {
      public GCodeLineNumberRenderer() {
         super();
         setHorizontalAlignment(JLabel.RIGHT);
      }


      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
         if (table == null)
            return this;
         Font  f = UITheme.getFont("GCode:number.font");
         Color cb, cf;

         if (f != null)
            setFont(f);

         if (isSelected) {
            cb = UITheme.getColor("GCode:line.selected.background");
            cf = UITheme.getColor("GCode:line.selected.foreground");
         } else if ((row % 2) == 0) {
            cb = UITheme.getColor("GCode:line.altbackground");
            cf = UITheme.getColor("GCode:number.foreground");
         } else {
            cb = UITheme.getColor("GCode:line.background");
            cf = UITheme.getColor("GCode:number.foreground");
         }
         if (cf != null)
            setForeground(cf);
         if (cb != null)
            setBackground(cb);
         setValue(value);

         return this;
      }


      @Override
      public void setValue(Object value) {
         if (value != null) {
            if (value instanceof Long) {
               setText(String.format("%d: ", value));
            }
         }
      }


      private static final long serialVersionUID = 1L;
   }

   static class GCodeSourceRenderer extends JTextPane implements TableCellRenderer {
      public GCodeSourceRenderer() {
         setDocument(new GCodeSource(UITheme.getStyles("GCode:styles")));
         //         setForeground(settings.getGCodeForeground());
         //         setBackground(settings.getGCodeBackground());
         setEditable(false);
      }


      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
         if (table == null) {
            return this;
         }
         Font  f = UITheme.getFont("GCode:line.font");
         Color cb, cf;

         if (f != null)
            setFont(f);

         if (isSelected) {
            cb = UITheme.getColor("GCode:line.selected.background");
            cf = UITheme.getColor("GCode:line.selected.foreground");
         } else if ((row % 2) == 0) {
            cb = UITheme.getColor("GCode:line.altbackground");
            cf = UITheme.getColor("GCode:line.foreground");
         } else {
            cb = UITheme.getColor("GCode:line.background");
            cf = UITheme.getColor("GCode:line.foreground");
         }
         if (cf != null)
            setForeground(cf);
         if (cb != null)
            setBackground(cb);

         if (value != null && value instanceof String) {
            setText((String) value);
         }
         return this;
      }


      private static final long serialVersionUID = 1L;
   }


   public GCodeLister(CommandWriter cmdWriter, EventList<GCodeLine> lines) {
      super(new GridBagLayout());
      this.lines  = lines;
      fileHandler = new GCodeFileHandler(this);
      DefaultEventTableModel<GCodeLine> lineTableModel = new DefaultEventTableModel<GCodeLine>(lines,
            new GCodeTableFormat());
      setOpaque(true);
      setBackground(UITheme.getColor("GCode:line.background"));
      table = new JTable(lineTableModel);
      table.setRowHeight(UITheme.getInt("GCode:row.height"));
      table.setGridColor(UITheme.getColor("GCode:grid.color"));
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.getSelectionModel().addListSelectionListener(this);
      table.setBackground(UITheme.getColor("GCode:line.background"));
      table.getColumnModel().getColumn(0).setCellRenderer(new GCodeLineNumberRenderer());
      table.getColumnModel().getColumn(1).setCellRenderer(new GCodeSourceRenderer());
      table.setFillsViewportHeight(true);
      setupColumnWidth(lineTableModel);

      scrollPane = new JScrollPane(table);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setPreferredSize(new Dimension(1200, 1600));
      viewPort = scrollPane.getViewport();

      addComponents(this, cmdWriter);
   }


   public int getCurrentLine() {
      return table.getSelectedRow() + 1;
   }


   public GCodeFileHandler getFileHandler() {
      return fileHandler;
   }


   public String getFileName() {
      return fileName.getText();
   }


   public List<GCodeLine> getList() {
      return lines;
   }


   public String getMDICommand() {
      return editField.getText();
   }


   public TimeLabel getProcessTimeLabel() {
      return processTime;
   }


   public void loadFile(String fileName) throws FileNotFoundException {
      File newFile = new File(fileName);

      if (newFile.exists() && newFile.canRead()) {
         BufferedReader br   = null;
         String         line = null;
         long           i    = 0;

         lines.getReadWriteLock().writeLock().lock();
         LCStatus.getStatus().getGCodeInfo().setFrontendLine(0);
         try {
            lines.clear();
            br = new BufferedReader(new FileReader(newFile));

            while ((line = br.readLine()) != null) {
               lines.add(new GCodeLine(++i, line));
            }
            table.changeSelection(0, 0, false, false);
            this.fileName.setText(fileName);
         } catch (IOException e) {
            e.printStackTrace();
         }
         lines.getReadWriteLock().writeLock().unlock();
         setValue(LCStatus.getStatus().getGCodeInfo().getFrontendLine());
      } else {
         throw new FileNotFoundException("no file, or not readable: " + fileName);
      }
   }


   public void registerCommand(String cmd) {
      appendIfNotExists(lines, cmd);
   }


   @Override
   public void setValue(Object value) {
      if (value instanceof Integer) {
         table.changeSelection(((int) value) - 1, 0, false, false);
      }
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      int selected = table.getSelectedRow();

      if (selected >= 0)
         editField.setText(lines.get(selected).getLine());
   }


   protected void addComponents(JPanel p, CommandWriter cmdWriter) {
      GridBagConstraints c = new GridBagConstraints();

      c.gridx   = 0;
      c.gridy   = 0;
      c.fill    = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      p.add(scrollPane, c);

      c.gridy   = 1;
      c.weighty = 0;
      p.add(createAddon(cmdWriter), c);
   }


   protected void appendIfNotExists(EventList<GCodeLine> list, String cmd) {
      boolean found = false;

      for (GCodeLine gl : list) {
         if (cmd.compareTo(gl.getLine()) == 0) {
            found = true;
            break;
         }
      }
      if (!found) {
         list.getReadWriteLock().writeLock().lock();
         list.add(new GCodeLine(list.size() + 1, cmd));
         list.getReadWriteLock().writeLock().unlock();
      }
   }


   protected JComponent createAddon(CommandWriter cmdWriter) {
      JPanel pane = new JPanel(new SpringLayout());

      editField = new JTextField();
      JButton btRemove = new JButton(LCStatus.getStatus().lm("delete"));

      btRemove.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int i = table.convertRowIndexToModel(table.getSelectedRow());

            if (i >= 0 && i < lines.size()) {
               lines.getReadWriteLock().writeLock().lock();
               lines.remove(i);
               renumberList(lines);
               lines.getReadWriteLock().writeLock().unlock();
               table.changeSelection(i - 1, 0, false, false);
            }
         }
      });
      Font  f  = UITheme.getFont("GCode:edit.font");
      Color cb = UITheme.getColor("GCode:edit.background");
      Color cf = UITheme.getColor("GCode:edit.foreground");

      if (f != null)
         editField.setFont(f);

      if (cb != null)
         editField.setBackground(cb);
      if (cf != null)
         editField.setForeground(cf);
      pane.add(btRemove);
      pane.add(editField);
      SpringUtilities.makeCompactGrid(pane, 1, 2, 0, 0, 0, 0);

      return pane;
   }


   protected void renumberList(EventList<GCodeLine> list) {
      int i = 0;

      for (GCodeLine gl : list) {
         gl.setLineNumber(++i);
      }
   }


   protected void setupColumnWidth(TableModel tm) {
      TableColumn column = null;

      for (int i = 0; i < tm.getColumnCount(); ++i) {
         column = table.getColumnModel().getColumn(i);
         if (i == 0) {
            column.setMinWidth(100);
            column.setMaxWidth(100);
         } else {
            column.setPreferredWidth(800);
         }
      }
   }


   protected JTable               table;
   protected JScrollPane          scrollPane;
   protected JTextField           fileName;
   protected GCodeFileHandler     fileHandler;
   protected EventList<GCodeLine> lines;
   protected JViewport            viewPort;
   protected JTextField           editField;
   private TimeLabel              processTime;
   private static final long      serialVersionUID = 1L;
}
