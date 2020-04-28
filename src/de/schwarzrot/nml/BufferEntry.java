package de.schwarzrot.nml;
/* 
 * **************************************************************************
 * 
 *  file:       BufferEntry.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    28.10.2019 by Django Reinhard
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

public class BufferEntry {
   public enum BufferEntryType {
                                unknown,
                                Logical,
                                Byte,
                                Short,
                                Integer,
                                Long,
                                Double,
                                String
   };


   public BufferEntry(String name, int offset, int size, BufferEntryType type) {
      this.name   = name;
      this.offset = offset;
      this.size   = size;
      this.type   = type;
   }

   public final String          name;
   public final int             offset;
   public final int             size;
   public final BufferEntryType type;
}
