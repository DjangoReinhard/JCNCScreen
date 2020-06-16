package de.schwarzrot.system;
/* 
 * **************************************************************************
 * 
 *  file:       RS274Reader.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    22.5.2020 by Django Reinhard
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


import java.util.logging.Logger;

import com.jme3.math.Vector3f;

import de.schwarzrot.util.IGeoLineParser;
import de.schwarzrot.widgets.jme3.ICreator3D;


public class RS274Reader implements IGeoLineParser {
   public RS274Reader(boolean changeYwithZ) {
      this.useXZ = changeYwithZ;
   }


   @Override
   public boolean isYZmapped() {
      return useXZ;
   }


   @Override
   public Vector3f parseLine(String line, Vector3f last, ICreator3D primFactory) {
      if (line.startsWith("READ => "))
         return last;
      String[] parts   = line.split("\\s+", 4);
      int      lineNum = 0;

      if (parts.length < 4)
         return last;
      if (parts[2].startsWith("N")) {
         try {
            lineNum = Integer.parseInt(parts[2].substring(1));
         } catch (Throwable t) {
         }
      }
      if (parts[3].contains("(")) {
         String[] subp = parts[3].split("[(,)]\\s*");

         if (subp.length == 7) {
            //straight line or fast move
            if (subp[0].contains("V")) {
               // fast move
               if (last == null) {
                  if (useXZ) {
                     last = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[3]),
                           -Float.parseFloat(subp[2]));
                  } else {
                     last = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[2]),
                           Float.parseFloat(subp[3]));
                  }
               } else {
                  Vector3f next = null;

                  if (useXZ) {
                     next = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[3]),
                           -Float.parseFloat(subp[2]));

                  } else {
                     next = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[2]),
                           Float.parseFloat(subp[3]));
                  }
                  primFactory.createLine(last, next, lineNum, true);
                  last = next;
               }
            } else {
               if (last == null) {
                  if (useXZ) {
                     last = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[3]),
                           -Float.parseFloat(subp[2]));
                  } else {
                     last = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[2]),
                           Float.parseFloat(subp[3]));
                  }
               } else {
                  Vector3f next = null;

                  if (useXZ) {
                     next = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[3]),
                           -Float.parseFloat(subp[2]));
                  } else {
                     next = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[2]),
                           Float.parseFloat(subp[3]));
                  }
                  primFactory.createLine(last, next, lineNum, false);
                  last = next;
               }
            }
         } else if (subp.length == 10) {
            // arc
            if (last == null) {
               throw new UnsupportedOperationException("can't do arc without start position!");
            } else {
               Vector3f center = null;
               Vector3f next   = null;
               int      c      = Integer.parseInt(subp[5]);

               if (useXZ) {
                  next   = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[6]),
                        -Float.parseFloat(subp[2]));
                  center = new Vector3f(Float.parseFloat(subp[3]), Float.parseFloat(subp[6]),
                        -Float.parseFloat(subp[4]));
               } else {
                  next   = new Vector3f(Float.parseFloat(subp[1]), Float.parseFloat(subp[2]),
                        Float.parseFloat(subp[6]));
                  center = new Vector3f(Float.parseFloat(subp[3]), Float.parseFloat(subp[4]),
                        Float.parseFloat(subp[6]));
               }
               primFactory.createArc(last, next, center, lineNum, c > 0);
               last = next;
            }
         }
      }
      return last;
   }


   private boolean       useXZ;
   private static Logger l;
   static {
      l = Logger.getLogger("");
   }
}
