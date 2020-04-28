package de.schwarzrot.database;
/*
 * **************************************************************************
 *
 *  file:       ToolDBTable.java
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
import java.util.List;

import de.schwarzrot.bean.ToolDefinition;
import de.schwarzrot.util.DatabaseUtils;
import de.schwarzrot.util.PropertyAccessor;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;


public class ToolDBTable implements IDatabaseTable<ToolDefinition> {
   public ToolDBTable() {
      this(false);
   }


   public ToolDBTable(boolean readInAdvance) {
      super();
      this.readInAdvance = readInAdvance;
      this.basicList     = new BasicEventList<ToolDefinition>();
      this.paTool        = new PropertyAccessor(ToolDefinition.class);
   }


   @Override
   public void createTable() throws SQLException {
      String    createString = "CREATE TABLE \"" + ToolDBTable.TableName + "\" ("
            + "toolNumber int NOT NULL, " + "toolName varchar(50) NOT NULL, "
            + "toolLength numeric(7,3) NOT NULL, " + "toolCategory int NOT NULL, "
            + "colletDiameter numeric(7,3), " + "colletLength numeric(7,3), " + "shankDiameter numeric(7,3), "
            + "freeLength numeric(7,3), " + "slopeAngle numeric(7,3), " + "flutes int NOT NULL, "
            + "fluteDiameter numeric(7,3), " + "fluteLength numeric(7,3), " + "cuttingRadius numeric(7,3), "
            + "cuttingLength numeric(7,3), " + "cuttingAngle numeric(7,3), " + "tipDiameter numeric(7,3), "
            + "partCode varchar(50), " + "material varchar(20), " + "coating varchar(20), "
            + "toothLoad numeric(7,3), " + "helixAngle numeric(7,3), " + "maxRampAngle numeric(7,3), "
            + "comment varchar(254), " + "note varchar(254), " + "PRIMARY KEY (toolNumber) " + ")";
      // create table fails with foreign key definition, so do an alter table :(
      String    alterFK      = "ALTER TABLE " + ToolDBTable.TableName + " ADD CONSTRAINT fk_toolcat "
            + " FOREIGN KEY (toolCategory) " + " REFERENCES " + ToolCatDBTable.TableName + " (id)";
      Statement stmt         = null;

      try {
         System.out.println("create table with: " + createString);

         stmt = cp.getConnection().createStatement();
         stmt.executeUpdate(createString);
         stmt.close();
         stmt = cp.getConnection().createStatement();
         stmt.execute(alterFK);
      } catch (SQLException e) {
         DatabaseUtils.printSQLException(e);
      } finally {
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
            stmt.executeUpdate("DROP TABLE IF EXISTS " + ToolDBTable.TableName);
         } else if (this.dbms.equals("derby")) {
            stmt.executeUpdate("DROP TABLE " + ToolDBTable.TableName);
         }
      } catch (SQLException e) {
         DatabaseUtils.printSQLException(e);
      } finally {
         if (stmt != null) {
            stmt.close();
         }
      }
   }


   @Override
   public void exportCSV(EventList<ToolDefinition> list) throws IOException {
      StringBuilder sb = new StringBuilder(ToolDefinition.propertyNames[1]);

      for (int i = 2; i < ToolDefinition.propertyNames.length; ++i) {
         sb.append(", ").append(ToolDefinition.propertyNames[i]);
      }
      System.out.println(sb.toString());

      for (ToolDefinition td : list) {
         sb = new StringBuilder();

         sb.append(paTool.getProperty(td, ToolDefinition.propertyNames[1]));
         for (int i = 2; i < ToolDefinition.propertyNames.length; ++i) {
            sb.append(", ").append(paTool.getProperty(td, ToolDefinition.propertyNames[i]));
         }
         System.out.println(sb.toString());
      }
   }


   @Override
   public final EventList<ToolDefinition> getBasicList() {
      return basicList;
   }


   @Override
   public long getCount() {
      try {
         if (psCounter == null) {
            psCounter = cp.getConnection()
                  .prepareStatement("SELECT COUNT(toolNumber) FROM " + ToolDBTable.TableName);
         }
         ResultSet rs = psCounter.executeQuery();

         rs.next();

         return rs.getLong(1);
      } catch (SQLException se) {
         DatabaseUtils.printSQLException(se);
      }
      return 0;
   }


   @Override
   public void importCSV(String fileName) throws IOException, SQLException {
      BufferedReader br     = new BufferedReader(
            new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8")));
      String         line   = null;
      String[]       fields = null;
      boolean        first  = true;

      while ((line = br.readLine()) != null) {
         if (line.isEmpty())
            break;
         fields = line.split(",\\s*");

         if (first) {
            first = false;
            validateFieldNames(fields);
         } else
            importRecord(fields);
      }
      br.close();
   }


   @Override
   public boolean isEmpty() {
      try {
         if (psCounter == null) {
            psCounter = cp.getConnection()
                  .prepareStatement("SELECT COUNT(toolNumber) FROM " + ToolDBTable.TableName);
         }
         ResultSet rs = psCounter.executeQuery();

         rs.next();
         long recordsInTable = rs.getLong(1);

         return recordsInTable < 1;
      } catch (SQLException se) {
         DatabaseUtils.printSQLException(se);
      }
      return true;
   }


   @Override
   public void readData(EventList<ToolDefinition> list) throws SQLException {
      // Date from = null;
      StringBuilder qryB  = new StringBuilder("SELECT ");
      boolean       first = true;

      for (int i = 1; i < ToolDefinition.propertyNames.length; ++i) {
         if (first)
            first = false;
         else
            qryB.append(", ");
         qryB.append(ToolDefinition.propertyNames[i]);
      }
      qryB.append(", toolCategory");
      qryB.append(", toolNumber");
      qryB.append(" FROM ").append(ToolDBTable.TableName);

      System.out.println("read ToolTable ...");
      try {
         PreparedStatement ps = cp.getConnection().prepareStatement(qryB.toString());
         // from = new Date();
         ResultSet         rs = ps.executeQuery();
         ToolDefinition    td;

         list.getReadWriteLock().writeLock().lock();
         while (rs.next()) {
            td = new ToolDefinition();

            for (int i = 1; i < ToolDefinition.propertyNames.length; ++i) {
               paTool.setProperty(td, ToolDefinition.propertyNames[i],
                     rs.getObject(ToolDefinition.propertyNames[i]));
            }
            td.setToolCategory(ToolCatDBTable.getCategoryForId(rs.getInt("toolCategory")));
            td.setToolNumber(rs.getInt("toolNumber"));
            td.setClean();
            list.add(td);
         }
         list.getReadWriteLock().writeLock().unlock();
         // Date to = new Date();

         rs.close();
         ps.close();

         // System.out.println(list.size() + " loaded in time: "
         // + (to.getTime() - from.getTime()));
      } catch (SQLException se) {
         //         DatabaseUtils.printSQLException(se);
      }
   }


   @Override
   public void removeRecord(ToolDefinition rec) throws SQLException {
      if (rec == null)
         return;
      if (psDelete == null) {
         psDelete = cp.getConnection()
               .prepareStatement("DELETE FROM " + ToolDBTable.TableName + " WHERE toolNumber=?");
      }
      psDelete.setInt(1, rec.getToolNumber());
      // Date before = new Date();

      psDelete.execute();

      // Date after = new Date();
      //
      // System.out.println("removing of tool-record took " + (after.getTime() -
      // before.getTime())
      // + "ms");
   }


   @Override
   public void saveRecord(ToolDefinition rec) throws SQLException {
      if (rec == null)
         return;
      boolean newRecord = true;

      if (rec.getToolNumber() == 0) {
         if (psMaxPrimary == null) {
            psMaxPrimary = cp.getConnection()
                  .prepareStatement("SELECT MAX(toolNumber) LastPrimary FROM " + ToolDBTable.TableName);
         }
         ResultSet rs = psMaxPrimary.executeQuery();

         rs.next();
         rec.setToolNumber(rs.getInt("LastPrimary") + 1);
      } else {
         // Date before = null, after = null;
         if (psCheckPrimary == null) {
            psCheckPrimary = cp.getConnection().prepareStatement(
                  "SELECT COUNT(toolNumber) Anzahl FROM " + ToolDBTable.TableName + " WHERE toolNumber=?");
         }
         psCheckPrimary.setInt(1, rec.getToolNumber());
         // before = new Date();
         ResultSet rs = psCheckPrimary.executeQuery();

         // after = new Date();
         // System.out.println("check of primary key took " + (after.getTime() - before.getTime())
         // + "ms");
         rs.next();
         if (rs.getInt("Anzahl") > 0)
            newRecord = false;
      }
      rec.dump();

      if (newRecord) {
         // have to perform an insert
         if (psInsert == null) {
            psInsert = cp.getConnection()
                  .prepareStatement("INSERT INTO " + ToolDBTable.TableName + " VALUES (?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ? " + ")");
         }
         psInsert.setInt(1, rec.getToolNumber());
         psInsert.setString(2, rec.getToolName());
         psInsert.setDouble(3, rec.getToolLength());
         psInsert.setInt(4, rec.getToolCategory().getId());
         psInsert.setDouble(5, rec.getColletDiameter());
         psInsert.setDouble(6, rec.getColletLength());
         psInsert.setDouble(7, rec.getShankDiameter());
         psInsert.setDouble(8, rec.getFreeLength());
         psInsert.setDouble(9, rec.getSlopeAngle());
         psInsert.setInt(10, rec.getFlutes());
         psInsert.setDouble(11, rec.getFluteDiameter());
         psInsert.setDouble(12, rec.getFluteLength());
         psInsert.setDouble(13, rec.getCuttingRadius());
         psInsert.setDouble(14, rec.getCuttingLength());
         psInsert.setDouble(15, rec.getCuttingAngle());
         psInsert.setDouble(16, rec.getTipDiameter());
         psInsert.setString(17, rec.getPartCode());
         psInsert.setString(18, rec.getMaterial());
         psInsert.setString(19, rec.getCoating());
         psInsert.setDouble(20, rec.getToothLoad());
         psInsert.setDouble(21, rec.getHelixAngle());
         psInsert.setDouble(22, rec.getMaxRampAngle());
         psInsert.setString(23, rec.getComment());
         psInsert.setString(24, rec.getNote());

         // before = new Date();
         psInsert.execute();
         // after = new Date();
      } else {
         // have to do an update
         if (psUpdate == null) {
            psUpdate = cp.getConnection()
                  .prepareStatement("UPDATE " + ToolDBTable.TableName + " SET toolName=?, "
                        + " toolLength=?, " + " toolCategory=?, " + " colletDiameter=?, "
                        + " colletLength=?, " + " shankDiameter=?, " + " freeLength=?, " + " slopeAngle=?, "
                        + " flutes=?, " + " fluteDiameter=?, " + " fluteLength=?, " + " cuttingRadius=?, "
                        + " cuttingLength=?, " + " cuttingAngle=?, " + " tipDiameter=?, " + " partCode=?, "
                        + " material=?, " + " coating=?, " + " toothLoad=?, " + " helixAngle=?, "
                        + " maxRampAngle=?, " + " comment=?, " + " note=? " + " WHERE toolNumber=?");
         }
         psUpdate.setString(1, rec.getToolName());
         psUpdate.setDouble(2, rec.getToolLength());
         psUpdate.setInt(3, rec.getToolCategory().getId());
         psUpdate.setDouble(4, rec.getColletDiameter());
         psUpdate.setDouble(5, rec.getColletLength());
         psUpdate.setDouble(6, rec.getShankDiameter());
         psUpdate.setDouble(7, rec.getFreeLength());
         psUpdate.setDouble(8, rec.getSlopeAngle());
         psUpdate.setInt(9, rec.getFlutes());
         psUpdate.setDouble(10, rec.getFluteDiameter());
         psUpdate.setDouble(11, rec.getFluteLength());
         psUpdate.setDouble(12, rec.getCuttingRadius());
         psUpdate.setDouble(13, rec.getCuttingLength());
         psUpdate.setDouble(14, rec.getCuttingAngle());
         psUpdate.setDouble(15, rec.getTipDiameter());
         psUpdate.setString(16, rec.getPartCode());
         psUpdate.setString(17, rec.getMaterial());
         psUpdate.setString(18, rec.getCoating());
         psUpdate.setDouble(19, rec.getToothLoad());
         psUpdate.setDouble(20, rec.getHelixAngle());
         psUpdate.setDouble(21, rec.getMaxRampAngle());
         psUpdate.setString(22, rec.getComment());
         psUpdate.setString(23, rec.getNote());

         psUpdate.setInt(24, rec.getToolNumber());

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
   public void setupCache(List<ToolDefinition> list) {
   }


   @Override
   public boolean shouldReadInAdvance() {
      return readInAdvance;
   }


   protected void importRecord(String[] values) throws SQLException {
      // for (int i = 0; i < values.length; ++i) {
      // System.out.println(values[i]);
      // }
      // System.out.println();
      //
      ToolDefinition td = null;

      // td = new ToolDefinition(Integer.parseInt(values[0]),
      // Integer.parseInt(values[1]),
      // Integer.parseInt(values[2]),
      // Integer.parseInt(values[3]));
      saveRecord(td);
   }


   // fJahr, fMonat, fTag, Fahrer
   protected void validateFieldNames(String[] names) {
      for (int i = 0; i < names.length; ++i) {
         switch (i) {
            case 0:
               if ("fJahr".compareToIgnoreCase(names[i]) != 0) {
                  throw new IllegalArgumentException("ungültige Spaltenbezeichnung: " + names[i]);
               }
               break;
            case 1:
               if ("fMonat".compareToIgnoreCase(names[i]) != 0) {
                  throw new IllegalArgumentException("ungültige Spaltenbezeichnung: " + names[i]);
               }
               break;
            case 2:
               if ("fTag".compareToIgnoreCase(names[i]) != 0) {
                  throw new IllegalArgumentException("ungültige Spaltenbezeichnung: " + names[i]);
               }
               break;
            case 3:
               if ("Fahrer".compareToIgnoreCase(names[i]) != 0) {
                  throw new IllegalArgumentException("ungültige Spaltenbezeichnung: " + names[i]);
               }
               break;
            default:
               throw new IllegalArgumentException("die Tabelle hat mehr Spalten als erlaubt");
         }
      }
   }


   private IConnectionProvider       cp;
   private String                    dbms;
   private EventList<ToolDefinition> basicList;
   private boolean                   readInAdvance;
   private PreparedStatement         psCheckPrimary;
   private PreparedStatement         psCounter;
   private PreparedStatement         psInsert;
   private PreparedStatement         psUpdate;
   private PreparedStatement         psDelete;
   private PreparedStatement         psMaxPrimary;
   private PropertyAccessor          paTool;
   public final static String        TableName = "LCTOOLS";
}
