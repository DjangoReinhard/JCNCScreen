package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       MessageLogPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    12.10.2019 by Django Reinhard
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


import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.format.MessageTableFormat;
import de.schwarzrot.system.SystemMessage;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;


public class MessageLogPane extends JPanel {
   public MessageLogPane(List<SystemMessage> log) {
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      this.log = log;
      SortedList<SystemMessage>             sl            = new SortedList<SystemMessage>(
            (EventList<SystemMessage>) this.log);
      DefaultEventTableModel<SystemMessage> msgTableModel = new DefaultEventTableModel<SystemMessage>(sl,
            new MessageTableFormat());
      table = new JTable(msgTableModel) {
         @Override
         public String getToolTipText(MouseEvent e) {
            String         tip             = null;
            java.awt.Point p               = e.getPoint();
            int            rowIndex        = rowAtPoint(p);
            int            colIndex        = columnAtPoint(p);
            int            realColumnIndex = convertColumnIndexToModel(colIndex);

            if (realColumnIndex == 2) {
               TableModel model = getModel();

               tip = (String) model.getValueAt(rowIndex, realColumnIndex);
            }
            return tip;
         }


         private static final long serialVersionUID = 1L;
      };
      table.setFont(UITheme.getFont(UITheme.MessageLOG_message_font));
      table.setGridColor(UITheme.getColor(UITheme.MessageLOG_grid_color));
      table.getTableHeader().setFont(UITheme.getFont(UITheme.MessageLOG_header_font));
      table.setRowHeight(UITheme.getInt(UITheme.MessageLOG_message_row_height));
      table.setFillsViewportHeight(true);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      setupColumnWidth(msgTableModel);
      JScrollPane scrollPane = new JScrollPane(table);

      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      add(scrollPane);
   }


   protected void setupColumnWidth(TableModel tm) {
      TableColumn col = null;

      for (int i = 0; i < tm.getColumnCount(); ++i) {
         col = table.getColumnModel().getColumn(i);

         if (i < 2) {
            col.setMinWidth(190);
            col.setMaxWidth(250);
         } else
            col.setPreferredWidth(400);
      }
   }


   private JTable              table;
   private List<SystemMessage> log;
   private static final long   serialVersionUID = 1L;
}
