package de.schwarzrot.database;
/* 
 * **************************************************************************
 * 
 *  file:       DBToolLibrary.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    23.11.2019 by Django Reinhard
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

import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import ca.odell.glazedlists.EventList;
import de.schwarzrot.bean.ToolCategory;
import de.schwarzrot.bean.ToolDefinition;
import de.schwarzrot.bean.ToolLibrary;
import de.schwarzrot.util.DatabaseUtils;


public class DBToolLibrary extends ToolLibrary {
   public DBToolLibrary(DatabaseUtils dbUtil) {
      super();
      this.dbUtil = dbUtil;
      this.setVersion("0.1");
      this.setToolNameFormat("none");
      this.setNumberFormat("0.000");
      catTable  = (ToolCatDBTable) dbUtil.getDbTable(0);
      toolTable = (ToolDBTable) dbUtil.getDbTable(1);
      populateTree();
   }


   @Override
   public DefaultMutableTreeNode addCategory(ToolCategory c) {
      if (c.getId() == 0) {
         try {
            catTable.saveRecord(c);
         }
         catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
         }
      }
      return super.addCategory(c);
   }


   @Override
   public void addTool(ToolDefinition td) {
      if (td.getToolNumber() == 0 || td.isModified()) {
         try {
            toolTable.saveRecord(td);
         }
         catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
         }
      }
      super.addTool(td);
   }


   protected void populateTree() {

      System.out.println("populateTree ... ");
      for (ToolCategory tc : catTable.getBasicList()) {
         addCategory(tc);
      }
      toolList = toolTable.getBasicList();
      try {
         toolTable.readData(toolList);
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private ToolCatDBTable            catTable;
   private ToolDBTable               toolTable;
   private DatabaseUtils             dbUtil;
   private EventList<ToolDefinition> toolList;
}
