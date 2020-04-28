package de.schwarzrot.util;
/* 
 * **************************************************************************
 * 
 *  file:       SimpleFile.java
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SimpleFile extends File {
   public SimpleFile(File path) {
      super(path.getAbsolutePath());
   }


   @Override
   public int compareTo(File other) {      
      return this.getName().compareTo(other.getName());
   }


   public String toString() {
      StringBuilder sb = new StringBuilder(getName());

      sb.append("\t");
      sb.append(sdf.format(new Date(this.lastModified())));
      sb.append("\t");
      sb.append(this.length());

      return sb.toString();
   }

   private static final SimpleDateFormat sdf              =
                                             new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static final long             serialVersionUID = 1L;
}
