package de.schwarzrot.bean;
/* 
 * **************************************************************************
 * 
 *  file:       GCodeLine.java
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

public class GCodeLine {
   public GCodeLine(long number, String line) {
      lineNumber = number;
      this.line  = line;
   }


   @Override
   public boolean equals(Object obj) {
      if (obj instanceof String) {
         return line.strip().compareTo(((String) obj).strip()) == 0;
      } else if (obj instanceof GCodeLine) {
         return line.strip()
                    .compareTo(((GCodeLine) obj).getLine().strip()) == 0;
      }
      return super.equals(obj);
   }


   public String getLine() {
      return line;
   }


   public long getLineNumber() {
      return lineNumber;
   }


   public void setLine(String line) {
      this.line = line;
   }


   public void setLineNumber(long lineNumber) {
      this.lineNumber = lineNumber;
   }


   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append(lineNumber);
      sb.append(":  ");
      sb.append(line);

      return sb.toString();
   }

   private long   lineNumber;
   private String line;
}
