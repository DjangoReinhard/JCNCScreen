package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       ToolTable.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    28.9.2019 by Django Reinhard
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


import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolEntry;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.format.ToolTableFormat;
import de.schwarzrot.system.CommandWriter;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;


public class ToolTable extends JPanel implements ActionListener, ListSelectionListener {
   public ToolTable(CommandWriter cmdWriter) {
      this.cmdWriter = cmdWriter;
      this.tools     = LCStatus.getStatus().getSetup().getTools();
      sl             = new SortedList<ToolEntry>(tools);
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(createUI(createTable(), createEditor()));
      try {
         table.getSelectionModel().setSelectionInterval(0, 0);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   @Override
   public void actionPerformed(ActionEvent arg0) {
      String s          = toolLen.getText().strip().replace(',', '.');
      double toolLength = 0;
      double toolDia    = 0;

      try {
         toolLength = Double.parseDouble(s);
         s          = toolDiam.getText().strip().replace(',', '.');

         toolDia    = Double.parseDouble(s);
      } catch (Throwable t) {
      }
      String command = String.format(Locale.US, "G10 L1 P%d R%.3f Z%.3f", toolNum, toolDia / 2, toolLength);
      System.out.println(command);
      cmdWriter.setToolProperties(command);
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      if (e.getValueIsAdjusting())
         return;
      int row = table.convertRowIndexToModel(table.getSelectedRow());

      if (row < 0)
         return;

      ToolEntry te = sl.get(row);

      toolNum = te.getToolNumber();
      toolNumber.setText(String.format("T%d", te.getToolNumber()));
      toolDiam.setText(String.format("%.3f", te.getDiameter()));
      toolLen.setText(String.format("%.3f", te.getOffset().getZ()));
      toolDesc.setText(te.getDescription());
      //      System.out.println();
      //      System.out.println(te);
   }


   protected JComponent createEditor() {
      JPanel             p = new JPanel();
      GridBagConstraints c = new GridBagConstraints();

      p.setLayout(new GridBagLayout());
      p.setPreferredSize(new Dimension(800, 60));
      p.setOpaque(true);
      p.setBackground(UITheme.getColor("Tool:editor.grid.color"));
      toolNumber = new JLabel("T99", JLabel.CENTER);
      toolNumber.setOpaque(true);
      toolNumber.setForeground(UITheme.getColor("Tool:desc.foreground"));
      toolNumber.setBackground(UITheme.getColor("ToolTable:background"));
      toolNumber.setFont(UITheme.getFont("Tool:number.font"));
      FontMetrics fm = toolNumber.getFontMetrics(toolNumber.getFont());
      int         w  = SwingUtilities.computeStringWidth(fm, " T999 ");
      Dimension   d  = new Dimension(w, fm.getHeight());

      toolNumber.setMinimumSize(d);
      toolNumber.setPreferredSize(d);
      c.fill       = GridBagConstraints.VERTICAL;
      c.insets     = new Insets(1, 1, 1, 1);
      c.gridx      = 0;
      c.gridy      = 0;
      c.gridwidth  = 1;
      c.gridheight = 2;
      c.weightx    = 0;
      c.weighty    = 1;
      p.add(toolNumber, c);

      JLabel l = new JLabel(" L:");
      l.setOpaque(true);
      l.setForeground(UITheme.getColor("Tool:desc.foreground"));
      l.setBackground(UITheme.getColor("ToolTable:background"));
      l.setFont(UITheme.getFont("Tool:editor.prompt"));
      c.fill       = GridBagConstraints.BOTH;
      c.gridx      = 1;
      c.gridheight = 1;
      c.weightx    = 0;
      c.weighty    = 0.5;
      p.add(l, c);

      l = new JLabel(" D:");
      l.setOpaque(true);
      l.setForeground(UITheme.getColor("Tool:desc.foreground"));
      l.setBackground(UITheme.getColor("ToolTable:background"));
      l.setFont(UITheme.getFont("Tool:editor.prompt"));
      c.gridy = 1;
      p.add(l, c);

      toolLen = new JTextField(7);
      toolLen.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
      toolLen.setFont(UITheme.getFont("Tool:editor.entry"));
      fm = toolNumber.getFontMetrics(toolLen.getFont());
      w  = SwingUtilities.computeStringWidth(fm, "32.789,999");
      d  = new Dimension(w, fm.getHeight());

      toolLen.setMinimumSize(d);
      toolLen.setPreferredSize(d);
      c.gridx   = 2;
      c.gridy   = 0;
      c.weightx = 0;
      p.add(toolLen, c);

      toolDiam = new JTextField(7);
      toolDiam.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
      toolDiam.setFont(UITheme.getFont("Tool:editor.entry"));
      toolDiam.setMinimumSize(d);
      toolDiam.setPreferredSize(d);
      c.gridy = 1;
      p.add(toolDiam, c);

      toolDesc = new JLabel("tool description ...");
      toolDesc.setOpaque(true);
      toolDesc.setFont(UITheme.getFont("Tool:editor.entry"));
      toolDesc.setForeground(UITheme.getColor("Tool:desc.foreground"));
      toolDesc.setBackground(UITheme.getColor("ToolTable:background"));
      c.gridx      = 3;
      c.gridy      = 0;
      c.gridheight = 1;
      c.weightx    = 0.5;
      c.weighty    = 1;
      p.add(toolDesc, c);

      c.gridy = 1;
      JPanel bp = new JPanel();

      bp.setLayout(new BoxLayout(bp, BoxLayout.LINE_AXIS));
      bp.add(Box.createHorizontalStrut(30));
      bp.setOpaque(true);
      bp.setForeground(UITheme.getColor("Tool:desc.foreground"));
      bp.setBackground(UITheme.getColor("ToolTable:background"));
      btSave = new JButton(LCStatus.getStatus().lm("save"));
      btSave.addActionListener(this);
      bp.add(btSave);
      bp.add(Box.createHorizontalGlue());
      p.add(bp, c);

      return p;
   }


   protected JComponent createTable() {
      DefaultEventTableModel<ToolEntry> toolTableModel = new DefaultEventTableModel<ToolEntry>(sl,
            new ToolTableFormat());
      table = new JTable(toolTableModel);
      TableComparatorChooser<ToolEntry> tableSorter = TableComparatorChooser.install(table, sl,
            TableComparatorChooser.SINGLE_COLUMN);
      table.setFillsViewportHeight(true);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      setupColumnWidth(toolTableModel);
      table.getSelectionModel().addListSelectionListener(this);
      table.setFont(UITheme.getFont("ToolTable:table.font"));
      table.setForeground(UITheme.getColor("ToolTable:foreground"));
      table.setBackground(UITheme.getColor("ToolTable:background"));
      table.setSelectionBackground(UITheme.getColor("ToolTable:selected.background"));
      table.setSelectionForeground(UITheme.getColor("ToolTable:selected.foreground"));
      table.setGridColor(UITheme.getColor("ToolTable:grid.color"));
      table.setRowHeight(UITheme.getInt("ToolTable:row.height"));
      table.getTableHeader().setFont(UITheme.getFont("ToolTable:header.font"));

      JScrollPane scrollPane = new JScrollPane(table);

      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      return scrollPane;
   }


   protected JComponent createUI(JComponent table, JComponent editor) {
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, table, editor);

      splitPane.setResizeWeight(1);
      splitPane.setDividerSize(2);

      return splitPane;
   }


   protected void setupColumnWidth(TableModel tm) {
      TableColumn col = null;
      final int   mx  = tm.getColumnCount();

      for (int i = 0; i < mx; ++i) {
         col = table.getColumnModel().getColumn(i);

         if (i < 2) {
            col.setMinWidth(70);
            col.setMaxWidth(70);
         } else if (i == (mx - 1)) {
            col.setPreferredWidth(400);
         } else {
            col.setMinWidth(150);
            col.setMaxWidth(150);
         }
      }
   }


   private JTable                table;
   private JLabel                toolNumber;
   private JTextField            toolDiam;
   private JTextField            toolLen;
   private JLabel                toolDesc;
   private int                   toolNum;
   private JButton               btSave;
   private CommandWriter         cmdWriter;
   private EventList<ToolEntry>  tools;
   private SortedList<ToolEntry> sl;
   private static final long     serialVersionUID = 1L;
}
