package de.schwarzrot.database;
/* 
 * **************************************************************************
 * 
 *  file:       ToolCatDBTable.java
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import de.schwarzrot.bean.ToolCategory;
import de.schwarzrot.bean.ToolProfile;
import de.schwarzrot.util.DatabaseUtils;


public class ToolCatDBTable implements IDatabaseTable<ToolCategory> {
   public ToolCatDBTable() {
      this(false);
   }


   public ToolCatDBTable(boolean readInAdvance) {
      this.basicList     = new BasicEventList<ToolCategory>();
      this.readInAdvance = readInAdvance;
   }


   @Override
   public void createTable() throws SQLException {
      String    createString = "CREATE TABLE \"" + ToolCatDBTable.TableName
                               + "\" ("
                               + "id int NOT NULL, "
                               + "toolProfile int NOT NULL, "
                               + "catName varchar(50), "
                               + "PRIMARY KEY (id) "
                               + ")";
      Statement stmt         = null;

      try {
         System.out.println("create table with: " + createString);

         stmt = cp.getConnection().createStatement();
         stmt.executeUpdate(createString);
      }
      catch (SQLException e) {
         DatabaseUtils.printSQLException(e);
      }
      finally {
         if (stmt != null) {
            stmt.close();
         }
      }
   }


   @Override
   public void dropTable() throws SQLException {
      Statement stmt = null;

      try {
         stmt = cp.getConnection().createStatement();

         if (this.dbms.equals("mysql")) {
            stmt.executeUpdate("DROP TABLE IF EXISTS "
                               + ToolCatDBTable.TableName);
         } else if (this.dbms.equals("derby")) {
            stmt.executeUpdate("DROP TABLE " + ToolCatDBTable.TableName);
         }
      }
      catch (SQLException e) {
         DatabaseUtils.printSQLException(e);
      }
      finally {
         if (stmt != null) {
            stmt.close();
         }
      }
   }


   @Override
   public void exportCSV(EventList<ToolCategory> list) throws IOException {
      Iterator<ToolCategory> i  = list.iterator();
      StringBuilder          sb = new StringBuilder("id, Profile, Name");

      System.out.println(sb.toString());
      while (i.hasNext()) {
         ToolCategory tc = i.next();

         sb = new StringBuilder();
         sb.append(tc.getId());
         sb.append(", ").append(tc.getToolProfile().name());
         sb.append(", ").append(tc.getName());

         System.out.println(sb.toString());
      }
   }


   @Override
   public final EventList<ToolCategory> getBasicList() {
      return basicList;
   }


   @Override
   public long getCount() {
      try {
         if (psCounter == null) {
            psCounter = cp.getConnection()
                          .prepareStatement("SELECT COUNT(id) FROM "
                                            + ToolCatDBTable.TableName);
         }
         ResultSet rs = psCounter.executeQuery();

         rs.next();
         return rs.getLong(1);
      }
      catch (SQLException se) {
         DatabaseUtils.printSQLException(se);
      }
      return 0;
   }


   @Override
   public void importCSV(String fileName) throws IOException, SQLException {
      BufferedReader br     =
                        new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                                 Charset.forName("UTF-8")));
      String         line   = null;
      String[]       fields = null;
      boolean        first  = true;

      while ((line = br.readLine()) != null) {
         if (line.isEmpty()) break;
         fields = line.split(",\\s*");

         if (first) {
            first = false;
            validateFieldNames(fields);
         } else importRecord(fields);
      }
      br.close();
   }


   @Override
   public boolean isEmpty() {
      try {
         if (psCounter == null) {
            psCounter = cp.getConnection()
                          .prepareStatement("SELECT COUNT(id) FROM "
                                            + ToolCatDBTable.TableName);
         }
         ResultSet rs = psCounter.executeQuery();

         rs.next();
         int recordsInTable = rs.getInt(1);

         return recordsInTable < 1;
      }
      catch (SQLException se) {
         DatabaseUtils.printSQLException(se);
      }
      return true;
   }


   @Override
   public void readData(EventList<ToolCategory> list) throws SQLException {
      // Date from = null;
      StringBuilder qryB = new StringBuilder("SELECT ");

      qryB.append("id");
      qryB.append(", toolProfile");
      qryB.append(", catName");
      qryB.append(" FROM ").append(ToolCatDBTable.TableName);

      // System.out.println("read ToolCatTable ...");
      // try {
      PreparedStatement ps =
                           cp.getConnection().prepareStatement(qryB.toString());
      // from = new Date();
      ResultSet    rs = ps.executeQuery();
      ToolCategory tc;

      list.getReadWriteLock().writeLock().lock();
      while (rs.next()) {
         switch (rs.getInt("toolProfile")) {
         case 1:
            tc = new ToolCategory(ToolProfile.BullNose);
            break;
         case 2:
            tc = new ToolCategory(ToolProfile.BallNose);
            break;
         case 3:
            tc = new ToolCategory(ToolProfile.VCutter);
            break;
         case 4:
            tc = new ToolCategory(ToolProfile.Drill);
            break;
         case 5:
            tc = new ToolCategory(ToolProfile.Lathe);
            break;
         default:
            tc = new ToolCategory(ToolProfile.EndMill);
            break;
         }
         tc.setId(rs.getInt("id"));
         tc.setName(rs.getString("CatName"));

         list.add(tc);
      }
      list.getReadWriteLock().writeLock().unlock();
      // Date to = new Date();

      rs.close();
      ps.close();

      // System.out.println(list.size() + " loaded in time: "
      // + (to.getTime() - from.getTime()));
      // }
      // catch (SQLException se) {
      // RechiUtils.printSQLException(se);
      // }
   }


   @Override
   public void removeRecord(ToolCategory rec) throws SQLException {
      if (rec == null) return;
      if (psDelete == null) {
         psDelete = cp.getConnection()
                      .prepareStatement("DELETE FROM "
                                        + ToolCatDBTable.TableName
                                        + " WHERE id=?");
      }
      psDelete.setInt(1, rec.getId());
      // Date before = new Date();

      psDelete.execute();

      // Date after = new Date();
      //
      // System.out.println("removing of tool-category-record took " + (after.getTime() -
      // before.getTime())
      // + "ms");
   }


   @Override
   public void saveRecord(ToolCategory rec) throws SQLException {
      if (rec == null) return;
      rec.dump();
      boolean newRecord = true;
      // Date before = null, after = null;

      if (rec.getId() != 0) {
         try {
            if (psCheckPrimary == null) {
               psCheckPrimary =
                              cp.getConnection()
                                .prepareStatement("SELECT COUNT(id) Anzahl FROM "
                                                  + ToolCatDBTable.TableName
                                                  + " WHERE id=?");
            }
            psCheckPrimary.setInt(1, rec.getId());
            // before = new Date();
            ResultSet rs = psCheckPrimary.executeQuery();

            // after = new Date();
            // System.out.println("check of primary key took " + (after.getTime() -
            // before.getTime())
            // + "ms");
            rs.next();
            if (rs.getInt("Anzahl") > 0) newRecord = false;
         }
         catch (SQLException e) {
            e.printStackTrace();
         }
      } else {
         long count = getCount();

         rec.setId((int) count + 1);
      }
      if (newRecord) {
         // have to perform an insert
         if (psInsert == null) {
            psInsert = cp.getConnection()
                         .prepareStatement("INSERT INTO "
                                           + ToolCatDBTable.TableName
                                           + " VALUES (?, ?, ?)");
         }
         psInsert.setInt(1, rec.getId());
         psInsert.setInt(2, rec.getToolProfile().ordinal());
         psInsert.setString(3, rec.getName());

         // before = new Date();
         psInsert.execute();
         // after = new Date();
      } else {
         // have to do an update
         if (psUpdate == null) {
            psUpdate = cp.getConnection()
                         .prepareStatement("UPDATE " + ToolCatDBTable.TableName
                                           + " SET CatName=? "
                                           + " WHERE id=?");
         }
         psUpdate.setString(1, rec.getName());
         psUpdate.setInt(2, rec.getId());

         // before = new Date();
         psUpdate.execute();
         // after = new Date();
      }
      // System.out.println("saving of record took " + (after.getTime() - before.getTime()) + "ms");
   }


   @Override
   public void setConnectionProvider(IConnectionProvider cp) {
      this.cp = cp;
   }


   @Override
   public void setDbms(String dbms) {
      this.dbms = dbms;
   }


   @Override
   public void setupCache(List<ToolCategory> list) {
      for (ToolCategory tc : list) {
         catCache.put(tc.getId(), tc);
      }
   }


   @Override
   public boolean shouldReadInAdvance() {
      return this.readInAdvance;
   }


   protected void importRecord(String[] values) throws SQLException {
      // for (int i = 0; i < values.length; ++i) {
      // System.out.println(values[i]);
      // }
      // System.out.println();
      //
      ToolCategory tc = null;

      if (ToolProfile.EndMill.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.EndMill);
      } else if (ToolProfile.BullNose.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.BullNose);
      } else if (ToolProfile.BallNose.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.BallNose);
      } else if (ToolProfile.VCutter.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.VCutter);
      } else if (ToolProfile.Drill.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.Drill);
      } else if (ToolProfile.Lathe.name().compareTo(values[1]) == 0) {
         tc = new ToolCategory(ToolProfile.Lathe);
      }
      tc.setId(Integer.parseInt(values[0]));
      if (values.length > 2) tc.setName(values[2]);

      saveRecord(tc);
   }


   // id, Profile, Name
   protected void validateFieldNames(String[] names) {
      for (int i = 0; i < names.length; ++i) {
         switch (i) {
         case 0:
            if ("id".compareToIgnoreCase(names[i]) != 0) {
               throw new IllegalArgumentException("ungültige Spaltenbezeichnung: "
                                                  + names[i]);
            }
            break;
         case 1:
            if ("Profile".compareToIgnoreCase(names[i]) != 0) {
               throw new IllegalArgumentException("ungültige Spaltenbezeichnung: "
                                                  + names[i]);
            }
            break;
         case 2:
            if ("Name".compareToIgnoreCase(names[i]) != 0) {
               throw new IllegalArgumentException("ungültige Spaltenbezeichnung: "
                                                  + names[i]);
            }
            break;
         default:
            throw new IllegalArgumentException("die Tabelle hat mehr Spalten als erlaubt");
         }
      }
   }


   public static ToolCategory getCategoryForId(int id) {
      return catCache.get(id);
   }

   private IConnectionProvider               cp;
   private String                            dbms;
   private boolean                           readInAdvance;
   private EventList<ToolCategory>           basicList;
   private PreparedStatement                 psCheckPrimary;
   private PreparedStatement                 psCheckAmount;
   private PreparedStatement                 psCounter;
   private PreparedStatement                 psInsert;
   private PreparedStatement                 psUpdate;
   private PreparedStatement                 psDelete;
   private static Map<Integer, ToolCategory> catCache  =
                                                      new HashMap<Integer, ToolCategory>();
   public final static String                TableName = "LCTOOLCAT";
}
