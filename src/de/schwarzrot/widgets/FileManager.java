package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       FileManager.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    1.11.2019 by Django Reinhard
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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.format.SimpleFileFormat;
import de.schwarzrot.gui.AbstractTreeView;
import de.schwarzrot.util.SimpleDirectory;
import de.schwarzrot.util.SimpleFile;

import ca.odell.glazedlists.swing.DefaultEventTableModel;


public class FileManager extends AbstractTreeView<SimpleFile, SimpleFileFormat> {
   public FileManager(String title, File baseDir) {
      this(title, baseDir, null);
   }


   public FileManager(String title, File baseDir, ActionListener onEnterActionListener) {
      super(title, readDirectory(baseDir), new SimpleFileFormat());
      this.baseDir = baseDir;
   }


   @Override
   public void valueChanged(ListSelectionEvent e) {
   }


   @Override
   public void valueChanged(TreeSelectionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();
      if (node == null)
         return;
      Object ud = node.getUserObject();

      if (ud instanceof SimpleDirectory) {
         reloadFileList((SimpleDirectory) ud);
      } else if (ud instanceof String) {
         reloadFileList(baseDir);
      }
   }


   @Override
   protected void configureColumns(TableColumnModel m) {
      m.getColumn(0).setPreferredWidth(210);
      m.getColumn(1).setPreferredWidth(150);
      m.getColumn(2).setPreferredWidth(530);
   }


   @Override
   protected void performDelete4Table(ActionEvent e) {
   }


   @Override
   protected void performExport4Tree(ActionEvent e) {
   }


   @Override
   protected void performNew4Table(ActionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();
      if (node == null)
         return;
      Object ud = node.getUserObject();

      if (ud instanceof SimpleDirectory) {
         String nf = LCStatus.getStatus().getApp().inputDialog("Dateiname:", "neue Datei anlegen");
         if (nf != null) {
            try {
               File newFile = new File(((File) ud).getAbsoluteFile(), nf);

               if (newFile.createNewFile()) {

                  System.err.println(">>>>>>>> Oups <<<<<<<<<<");
                  System.err.println(">>>>>>>> Oups <<<<<<<<<<");
                  System.err.println("new file created: [ " + newFile.getAbsolutePath() + " ] ");
                  System.err.println(">>>>>>>> Oups <<<<<<<<<<");
                  System.err.println(">>>>>>>> Oups <<<<<<<<<<");
               } else {
                  System.err.println(">>>>>> failed to create File [ " + newFile.getAbsolutePath() + " ]");
               }
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      }
   }


   @Override
   protected void performNew4Tree(ActionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();
      if (node == null)
         return;
      Object ud = node.getUserObject();

      if (ud instanceof SimpleDirectory) {
         String nf = LCStatus.getStatus().getApp().inputDialog("Verzeichnis:", "neues Verzeichnis erstellen");
         if (nf != null) {
            File newDir = new File(((File) ud).getAbsoluteFile(), nf);

            if (newDir.mkdirs()) {

               System.err.println(">>>>>>>> Oups <<<<<<<<<<");
               System.err.println(">>>>>>>> Oups <<<<<<<<<<");
               System.err.println("new dirctory created: [ " + newDir.getAbsolutePath() + " ] ");
               System.err.println(">>>>>>>> Oups <<<<<<<<<<");
               System.err.println(">>>>>>>> Oups <<<<<<<<<<");
            } else {
               System.err.println(">>>>>> failed to create Directory [ " + newDir.getAbsolutePath() + " ]");
            }
         }
      }
   }


   @Override
   protected void performOnEnterAction(ActionEvent e) {
      int        sr  = getTableComponent().getSelectedRow();
      int        row = getTableComponent().convertRowIndexToModel(sr);
      @SuppressWarnings("unchecked")
      SimpleFile sf  = ((DefaultEventTableModel<SimpleFile>) getTableComponent().getModel())
            .getElementAt(row);

      getOnEnterAction().actionPerformed(new ActionEvent(sf, row, "selected"));

      // TableColumnModel cm = getTableComponent().getColumnModel();
      //
      // for (int i = 0; i < cm.getColumnCount(); ++i) {
      // System.out.println("column #" + i
      // + " has width of "
      // + cm.getColumn(i).getWidth());
      // }
   }


   @Override
   protected void performSelect4Table(ActionEvent e) {
   }


   protected void reloadFileList(File directory) {
      if (directory != null && directory.isDirectory()) {
         getTableList().getReadWriteLock().writeLock().lock();
         getTableList().clear();

         for (File f : directory.listFiles()) {
            if (f.getName().startsWith("."))
               continue;
            if (f.isDirectory())
               continue;
            SimpleFile sf = new SimpleFile(f);

            // System.out.println("add simple file: " + sf);
            getTableList().add(sf);
         }
         getTableList().getReadWriteLock().writeLock().unlock();
         getTableComponent().invalidate();
         getTableComponent().updateUI();
      }
   }


   protected static DefaultMutableTreeNode readDirectory(File directory) {
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(new SimpleDirectory(directory));

      if (directory != null && directory.exists() && directory.isDirectory()) {
         for (File f : directory.listFiles()) {
            if (f.getName().startsWith("."))
               continue;
            if (f.isDirectory()) {
               node.add(readDirectory(f));
            }
         }
      }
      return node;
   }


   private File              baseDir;
   private static final long serialVersionUID = 1L;
}
