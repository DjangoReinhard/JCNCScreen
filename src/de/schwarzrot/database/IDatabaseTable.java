package de.schwarzrot.database;
/* 
 * **************************************************************************
 * 
 *  file:       IDatabaseTable.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    17.11.2019 by Django Reinhard
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ca.odell.glazedlists.EventList;


public interface IDatabaseTable<T> {
   public void createTable() throws SQLException;


   public void dropTable() throws SQLException;


   public void exportCSV(EventList<T> list) throws IOException;


   public EventList<T> getBasicList();


   public long getCount();


   public void importCSV(String fileName) throws IOException, SQLException;


   public boolean isEmpty() throws SQLException;


   public void readData(EventList<T> list) throws SQLException;


   public void removeRecord(T c) throws SQLException;


   public void saveRecord(T c) throws SQLException;


   public void setConnectionProvider(IConnectionProvider cp);


   public void setDbms(String dbms);


   public void setupCache(List<T> list);


   public boolean shouldReadInAdvance();
}
