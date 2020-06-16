package de.schwarzrot.model;
/*
 * **************************************************************************
 *
 *  file:       GCodeInfo.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    8.9.2019 by Django Reinhard
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


import java.beans.PropertyChangeEvent;


public class GCodeInfo extends AbstractModel {
   public int getBackendLine() {
      return beLine;
   }


   public String getFileName() {
      return fileName;
   }


   public int getFrontendLine() {
      return feLine;
   }


   public double getProcessTime() {
      return processTime;
   }


   public void setBackendLine(int line) {
      int ov = this.beLine;
      firePropertyChange(new PropertyChangeEvent(this, BackEndLine, ov, line));
      this.beLine = line;
   }


   public void setEditFile(String fileName) {
      String ov = this.editFile;
      firePropertyChange(new PropertyChangeEvent(this, EditFile, ov, fileName));
      this.editFile = fileName;
   }


   public void setFileName(String fileName) {
      String ov = this.fileName;
      firePropertyChange(new PropertyChangeEvent(this, FileName, ov, fileName));
      this.fileName = fileName;
   }


   public void setFrontendLine(int line) {
      int ov = this.feLine;
      firePropertyChange(new PropertyChangeEvent(this, FrontEndLine, ov, line));
      this.feLine = line;
   }


   public void setProcessTime(double processTime) {
      double ov = this.processTime;
      firePropertyChange(new PropertyChangeEvent(this, ProcessTime, ov, processTime));
      this.processTime = processTime;
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(getFileName());

      sb.append("\nFrontend: ");
      sb.append(feLine);
      sb.append("\nBackend: ");
      sb.append(beLine);

      return sb.toString();
   }


   private String             fileName;
   private String             editFile;
   private int                feLine;
   private int                beLine;
   private double             processTime;
   public static final String EditFile     = "editFile";
   public static final String FileName     = "fileName";
   public static final String BackEndLine  = "backendLine";
   public static final String FrontEndLine = "frontendLine";
   public static final String ProcessTime  = "processTime";
}
