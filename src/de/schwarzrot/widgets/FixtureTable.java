package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       FixtureTable.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    3.10.2019 by Django Reinhard
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


import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.schwarzrot.bean.Fixture;
import de.schwarzrot.bean.Fixtures;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.format.FixtureTableFormat;
import de.schwarzrot.system.CommandWriter;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;


public class FixtureTable extends JPanel implements FocusListener, KeyListener, ListSelectionListener {
   public FixtureTable(CommandWriter cmdWriter) {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      Fixtures f = LCStatus.getStatus().getSetup().getFixtures();
      fixtures = new BasicEventList<Fixture>();
      fixtures.add(new Fixture("G92", f.getCommonOffset()));
      fixtures.add(new Fixture("G54", f.getG54Offset()));
      fixtures.add(new Fixture("G55", f.getG55Offset()));
      fixtures.add(new Fixture("G56", f.getG56Offset()));
      fixtures.add(new Fixture("G57", f.getG57Offset()));
      fixtures.add(new Fixture("G58", f.getG58Offset()));
      fixtures.add(new Fixture("G59", f.getG59Offset()));
      fixtures.add(new Fixture("G59.1", f.getG591Offset()));
      fixtures.add(new Fixture("G59.2", f.getG592Offset()));
      fixtures.add(new Fixture("G59.3", f.getG593Offset()));
      DefaultEventTableModel<Fixture> fixtureTableModel = new DefaultEventTableModel<Fixture>(fixtures,
            new FixtureTableFormat());
      table = new JTable(fixtureTableModel);
      table.setFillsViewportHeight(true);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.setCellSelectionEnabled(true);
      table.addKeyListener(this);
      table.addFocusListener(this);
      table.getSelectionModel().addListSelectionListener(this);

      table.setFont(UITheme.getFont("Fixture:table.font"));
      table.setRowHeight(UITheme.getInt("Fixture:row.height"));
      table.setBackground(UITheme.getColor("Fixture:background"));
      table.setSelectionBackground(UITheme.getColor("Fixture:selected.background"));
      table.setGridColor(UITheme.getColor("Fixture:grid.color"));
      table.getTableHeader().setFont(UITheme.getFont("Fixture:header.font"));
      table.setDefaultEditor(Double.class, new DoubleEditor(-1000, 1000));
      JScrollPane scrollPane = new JScrollPane(table);

      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      add(scrollPane);
   }


   @Override
   public void focusGained(FocusEvent e) {
      int row = table.getSelectedRow();
      int col = table.getSelectedColumn();

      table.editCellAt(row, col < 1 ? 1 : col);
   }


   @Override
   public void focusLost(FocusEvent e) {
   }


   @Override
   public void keyPressed(KeyEvent e) {
   }


   @Override
   public void keyReleased(KeyEvent e) {
      if (selectionKeys.contains(e.getKeyCode())) {
         int row = table.getSelectedRow();
         int col = table.getSelectedColumn();

         // System.out.println("selected cell is at row: " + row
         // + " and column: "
         // + col);
         if (col > 1)
            table.editCellAt(row, col);
      } else {
         System.out.println("keycode was: " + e.getKeyCode());
      }
   }


   @Override
   public void keyTyped(KeyEvent arg0) {
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
      // TODO Auto-generated method stub

   }


   private EventList<Fixture>         fixtures;
   private JTable                     table;
   private CommandWriter              cmdWriter;
   private static final List<Integer> selectionKeys;
   private static final long          serialVersionUID = 1L;
   static {
      selectionKeys = new ArrayList<Integer>();
      selectionKeys.add(9); // TAB
      selectionKeys.add(10); // ENTER
      selectionKeys.add(16); // Shift TAB
      selectionKeys.add(37); // cursor left
      selectionKeys.add(38); // cursor up
      selectionKeys.add(39); // cursor right
      selectionKeys.add(40); // cursor down
   };
}
