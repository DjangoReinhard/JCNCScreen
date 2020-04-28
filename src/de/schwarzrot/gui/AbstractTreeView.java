package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       AbstractTreeView.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    10.11.2019 by Django Reinhard
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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.widgets.IntegerCellRenderer;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;


public abstract class AbstractTreeView<T, F extends TableFormat<T>> extends JPanel
      implements ListSelectionListener, TreeSelectionListener {
   class LCTreeCellRenderer extends DefaultTreeCellRenderer {
      public LCTreeCellRenderer() {
         setFont(UITheme.getFont(getUIId() + ":font"));
         this.setTextNonSelectionColor(UITheme.getColor(getUIId() + ":foreground"));
         this.setTextSelectionColor(UITheme.getColor(getUIId() + ":selected.foreground"));
         this.setBackgroundSelectionColor(UITheme.getColor(getUIId() + ":selected.background"));
         this.setBackgroundNonSelectionColor(UITheme.getColor(getUIId() + ":background"));
      }


      @Override
      public Component getTreeCellRendererComponent(final JTree tree, final Object value,
            final boolean selected, final boolean expanded, final boolean leaf, final int row,
            final boolean hasFocus) {
         return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      }


      private static final long serialVersionUID = 1L;
   }


   protected AbstractTreeView(String title, DefaultMutableTreeNode rootNode, F tableFormat) {
      this.list4Table = new BasicEventList<T>();
      this.rootNode   = rootNode;
      this.rootNode.setUserObject(title);
      this.tableFormat  = tableFormat;
      abortAction       = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performAbort(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      debugAction       = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performDebug(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      toggleFocus       = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              System.err.println("toggle Pain ...");
                              if (table.isFocusOwner()) {
                                 tree.requestFocusInWindow();
                              } else {
                                 table.requestFocusInWindow();
                              }
                           }


                           private static final long serialVersionUID = 1L;
                        };
      tableOnEnter      = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performOnEnterAction(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      tableNewAction    = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performNew4Table(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      tableSelectAction = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performSelect4Table(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      tableDeleteAction = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performDelete4Table(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      treeNewAction     = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performNew4Tree(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      treeExportAction  = new AbstractAction() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              performExport4Tree(e);
                           }


                           private static final long serialVersionUID = 1L;
                        };
      this.treeView     = createTreeView();
      this.tableView    = createTableView();
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(createUI(treeView, tableView));
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            tree.setSelectionRow(0);
         }
      });
   }


   public JTable getTableComponent() {
      return table;
   }


   public JTree getTreeComponent() {
      return tree;
   }


   @Override
   public void setBackground(Color bgCol) {
      if (tree == null)
         return;
      tree.setBackground(bgCol);
      table.setBackground(bgCol);
   }


   @Override
   public void setFont(Font f) {
      if (table != null)
         table.setFont(f);
   }


   @Override
   public void setForeground(Color fgCol) {
      if (tree == null)
         return;
      tree.setForeground(fgCol);
      table.setForeground(fgCol);
   }


   public void setOnAbortAction(Action a) {
      this.onAbortAction = a;
   }


   public Action setOnEnterAction(Action a) {
      Action oldAction = this.onEnterAction;
      this.onEnterAction = a;

      return oldAction;
   }


   protected abstract void configureColumns(TableColumnModel m);


   protected JComponent createTableView() {
      SortedList<T>             sortedList = new SortedList<T>(list4Table);
      DefaultEventTableModel<T> tm         = new DefaultEventTableModel<T>(
            GlazedListsSwing.swingThreadProxyList(sortedList), tableFormat);
      table = new JTable(tm);
      TableComparatorChooser<T> tableSorter = TableComparatorChooser.install(table, sortedList,
            TableComparatorChooser.SINGLE_COLUMN);
      table.setFont(UITheme.getFont(getUIId() + ":font"));
      table.setForeground(UITheme.getColor(getUIId() + ":foreground"));
      table.setBackground(UITheme.getColor(getUIId() + ":background"));
      table.setSelectionForeground(UITheme.getColor(getUIId() + ":selected.foreground"));
      table.setSelectionBackground(UITheme.getColor(getUIId() + ":selected.background"));
      table.setGridColor(UITheme.getColor(getUIId() + ":grid.color"));
      table.setRowHeight(UITheme.getInt(getUIId() + ":row.height"));
      table.getTableHeader().setFont(UITheme.getFont(getUIId() + ":header.font"));
      table.setFocusTraversalKeysEnabled(false);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.getSelectionModel().addListSelectionListener(this);
      table.setFillsViewportHeight(true);
      table.setIntercellSpacing(new Dimension(5, 1));
      table.setDefaultRenderer(Long.class, new IntegerCellRenderer());
      configureColumns(table.getColumnModel());
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "togglePain");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "abort");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "execSelection");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "tableNew");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "tableSelect");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "tableRemove");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "debug");
      table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "treeExport");
      table.getActionMap().put("abort", abortAction);
      table.getActionMap().put("debug", debugAction);
      table.getActionMap().put("togglePain", toggleFocus);
      table.getActionMap().put("execSelection", tableOnEnter);
      table.getActionMap().put("tableNew", tableNewAction);
      table.getActionMap().put("tableSelect", tableSelectAction);
      table.getActionMap().put("tableDelete", tableDeleteAction);
      table.getActionMap().put("treeExport", treeExportAction);
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount() == 2) {
               tableOnEnter.actionPerformed(
                     new ActionEvent(e.getSource(), e.getID(), "onEnter", e.getWhen(), e.getModifiersEx()));
            }
         }
      });
      JScrollPane tableListScrollPane = new JScrollPane(table);

      tableListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      return tableListScrollPane;
   }


   protected JComponent createTreeView() {
      tree = new JTree(rootNode);
      tree.setForeground(UITheme.getColor(getUIId() + ":foreground"));
      tree.setBackground(UITheme.getColor(getUIId() + ":background"));
      tree.requestFocusInWindow();
      tree.setEditable(false);
      tree.setFocusTraversalKeysEnabled(false);
      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
      tree.setCellRenderer(new LCTreeCellRenderer());
      tree.addTreeSelectionListener(this);
      tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "togglePain");
      tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "abort");
      tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "treeNew");
      tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "debug");
      tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "treeExport");
      tree.getActionMap().put("abort", abortAction);
      tree.getActionMap().put("debug", debugAction);
      tree.getActionMap().put("togglePain", toggleFocus);
      tree.getActionMap().put("treeNew", treeNewAction);
      tree.getActionMap().put("treeExport", treeExportAction);
      JScrollPane treeView = new JScrollPane(tree);

      return treeView;
   }


   protected JComponent createUI(JComponent treeView, JComponent tableView) {
      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, tableView);
      splitPane.setDividerLocation(300);
      splitPane.setDividerSize(2);
      splitPane.setPreferredSize(new Dimension(1210, 800));

      return splitPane;
   }


   protected Action getOnAbortAction() {
      return onAbortAction;
   }


   protected Action getOnEnterAction() {
      return onEnterAction;
   }


   protected EventList<T> getTableList() {
      return list4Table;
   }


   protected final String getUIId() {
      return getClass().getSimpleName();
   }


   protected void performAbort(ActionEvent e) {
      Action abortAction = getOnAbortAction();
      if (abortAction != null)
         abortAction.actionPerformed(e);
   }


   protected void performDebug(ActionEvent e) {
   }


   protected abstract void performDelete4Table(ActionEvent e);


   protected abstract void performExport4Tree(ActionEvent e);


   protected abstract void performNew4Table(ActionEvent e);


   protected abstract void performNew4Tree(ActionEvent e);


   protected abstract void performOnEnterAction(ActionEvent e);


   protected abstract void performSelect4Table(ActionEvent e);


   private JComponent             treeView;
   private JComponent             tableView;
   private JTree                  tree;
   private JTable                 table;
   private DefaultMutableTreeNode rootNode;
   private EventList<T>           list4Table;
   private Action                 abortAction;
   private Action                 debugAction;
   private Action                 toggleFocus;
   private Action                 tableOnEnter;
   private Action                 tableNewAction;
   private Action                 tableSelectAction;
   private Action                 tableDeleteAction;
   private Action                 treeNewAction;
   private Action                 treeExportAction;
   private Action                 onEnterAction;
   private Action                 onAbortAction;
   private F                      tableFormat;
   private static final long      serialVersionUID = 1L;
}
