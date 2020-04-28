package de.schwarzrot.util;
/*
 * **************************************************************************
 *
 *  file:       DatabaseUtils.java
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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import de.schwarzrot.database.IConnectionProvider;
import de.schwarzrot.database.IDatabaseTable;


public class DatabaseUtils implements IConnectionProvider {
   @SuppressWarnings("rawtypes")
   public DatabaseUtils() {
      this.dbms       = "derby";
      this.dbName     = "LCToolDB";
      this.serverName = "localhost";
      this.portNumber = 3306;
      this.userName   = "";
      this.password   = "";
      tables          = new ArrayList<IDatabaseTable>();
      try {
         importProperties("app.properties");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void addTable(IDatabaseTable t) {
      t.setConnectionProvider(this);
      t.setDbms(dbms);
      tables.add(t);
      if (t.shouldReadInAdvance()) {
         try {
            t.readData(t.getBasicList());
            t.setupCache(t.getBasicList());
         } catch (SQLException e) {
            //            DatabaseUtils.printSQLException(e);
         }
      }
   }


   public void createDatabase(Connection connArg, String dbNameArg, String dbmsArg) {
      if (dbmsArg.equals("mysql")) {
         try {
            Statement s                 = connArg.createStatement();
            String    newDatabaseString = "CREATE DATABASE IF NOT EXISTS " + dbNameArg;

            s.executeUpdate(newDatabaseString);
            System.out.println("Created database " + dbNameArg);
         } catch (SQLException e) {
            printSQLException(e);
         }
      }
   }


   @Override
   public Connection getConnection() throws SQLException {
      if (connection == null) {
         Properties connectionProps = new Properties();
         connectionProps.put("user", this.userName);
         connectionProps.put("password", this.password);
         String currentUrlString = null;

         if (this.dbms.equals("mysql")) {
            currentUrlString = "jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/";
            connection       = DriverManager.getConnection(currentUrlString, connectionProps);

            this.urlString   = currentUrlString + this.dbName;
            connection.setCatalog(this.dbName);
         } else if (this.dbms.equals("derby")) {
            this.urlString = "jdbc:" + this.dbms + ":" + this.dbName;

            connection     = DriverManager.getConnection(this.urlString + ";create=true", connectionProps);
         }
      }
      return connection;
   }


   public final String getDbms() {
      return dbms;
   }


   public final String getDbName() {
      return dbName;
   }


   @SuppressWarnings("rawtypes")
   public IDatabaseTable getDbTable(int index) {
      return tables.get(index);
   }


   public final String getPassword() {
      return password;
   }


   public final int getPortNumber() {
      return portNumber;
   }


   public final String getServerName() {
      return serverName;
   }


   public final String getUrlString() {
      return urlString;
   }


   public final String getUserName() {
      return userName;
   }


   public void initializeDatabase() throws SQLException {
      // Connection connArg, String dbNameArg, String dbmsArg) {
      createDatabase(getConnection(), dbName, dbms);
      setupTables(false);
   }


   public void setupTables() throws SQLException {
      setupTables(true);
   }


   @SuppressWarnings("rawtypes")
   public void setupTables(boolean readInAdvance) throws SQLException {
      for (IDatabaseTable dbT : tables) {
         System.out.println("drop table " + dbT.getClass().getName());
         dbT.dropTable();
      }

      for (IDatabaseTable dbT : tables) {
         System.out.println("create table " + dbT.getClass().getName());
         dbT.createTable();
      }
   }


   private void importProperties(String fileName) throws IOException, InvalidPropertiesFormatException {
      this.prop = new Properties();
      InputStream is = null;

      try {
         is = new FileInputStream(fileName);
         prop.loadFromXML(is);
         is.close();
      } catch (FileNotFoundException fe) {
         is = getClass().getClassLoader().getResourceAsStream(fileName);
         prop.loadFromXML(is);
         is.close();
      }
      this.dbms       = this.prop.getProperty("dbms");
      // this.jarFile = this.prop.getProperty("jar_file");
      // this.driver = this.prop.getProperty("driver");
      this.dbName     = this.prop.getProperty("database_name");
      this.userName   = this.prop.getProperty("user_name");
      this.password   = this.prop.getProperty("password");
      this.serverName = this.prop.getProperty("server_name");
      this.portNumber = Integer.parseInt(this.prop.getProperty("port_number"));

      //      System.out.println("Set the following properties:");
      //      System.out.println("dbms: " + dbms);
      //      // System.out.println("driver: " + driver);
      //      System.out.println("dbName: " + dbName);
      //      System.out.println("userName: " + userName);
      //      System.out.println("serverName: " + serverName);
      //      System.out.println("portNumber: " + portNumber);
   }


   public static void closeConnection(Connection connArg) {
      System.out.println("Releasing all open resources ...");
      try {
         if (connArg != null) {
            connArg.close();
            connArg = null;
         }
      } catch (SQLException sqle) {
         printSQLException(sqle);
      }
   }


   public static void cursorHoldabilitySupport(Connection conn) throws SQLException {
      DatabaseMetaData dbMetaData = conn.getMetaData();
      System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " + ResultSet.HOLD_CURSORS_OVER_COMMIT);
      System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " + ResultSet.CLOSE_CURSORS_AT_COMMIT);
      System.out.println("Default cursor holdability: " + dbMetaData.getResultSetHoldability());
      System.out.println("Supports HOLD_CURSORS_OVER_COMMIT? "
            + dbMetaData.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));
      System.out.println("Supports CLOSE_CURSORS_AT_COMMIT? "
            + dbMetaData.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT));
   }


   public static long databaseVersion() {
      return serialVersionUID;
   }


   public static boolean ignoreSQLException(String sqlState) {
      if (sqlState == null) {
         System.out.println("The SQL state is not defined!");
         return false;
      }
      // X0Y32: Jar file already exists in schema
      if (sqlState.equalsIgnoreCase("X0Y32"))
         return true;
      // 42Y55: Table already exists in schema
      if (sqlState.equalsIgnoreCase("42Y55"))
         return true;
      return false;
   }


   public static void printSQLException(SQLException ex) {
      for (Throwable e : ex) {
         if (e instanceof SQLException) {
            if (ignoreSQLException(((SQLException) e).getSQLState()) == false) {
               e.printStackTrace(System.err);
               System.err.println("SQLState: " + ((SQLException) e).getSQLState());
               System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
               System.err.println("Message: " + e.getMessage());
               Throwable t = ex.getCause();

               while (t != null) {
                  System.out.println("Cause: " + t);
                  t = t.getCause();
               }
            }
         }
      }
   }


   public static void rowIdLifetime(Connection conn) throws SQLException {
      DatabaseMetaData dbMetaData = conn.getMetaData();
      RowIdLifetime    lifetime   = dbMetaData.getRowIdLifetime();

      switch (lifetime) {
         case ROWID_UNSUPPORTED:
            System.out.println("ROWID type not supported");
            break;
         case ROWID_VALID_FOREVER:
            System.out.println("ROWID has unlimited lifetime");
            break;
         case ROWID_VALID_OTHER:
            System.out.println("ROWID has indeterminate lifetime");
            break;
         case ROWID_VALID_SESSION:
            System.out.println("ROWID type has lifetime that is valid for at least the containing session");
            break;
         case ROWID_VALID_TRANSACTION:
            System.out
                  .println("ROWID type has lifetime that is valid for at least the containing transaction");
      }
   }


   // private String jarFile;
   // private String driver;
   private String               dbms;
   private String               dbName;
   private String               userName;
   private String               password;
   private String               urlString;
   private String               serverName;
   private int                  portNumber;
   private Connection           connection;
   private Properties           prop;
   @SuppressWarnings("rawtypes")
   private List<IDatabaseTable> tables;
   private static final long    serialVersionUID = 2019L;
}
