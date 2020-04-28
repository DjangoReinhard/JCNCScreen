package de.schwarzrot.util;
/* 
 * **************************************************************************
 * 
 *  file:       LCToolTableImporter.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    1.12.2019 by Django Reinhard
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolEntry;


public class LCToolTableImporter {
   public void importToolTable(String toolTableFile) {
      List<ToolEntry> tools    = LCStatus.getStatus().getSetup().getTools();
      File            toolsDef = new File(toolTableFile);
      BufferedReader  br       = null;

      if (toolsDef.exists() && toolsDef.canRead()) {
         tools.clear();

         try {
            br = new BufferedReader(new FileReader(toolsDef));
            String    line = null;
            ToolEntry tool = null;
            int       intValue;
            double    doubleValue;

            while ((line = br.readLine()) != null) {
               String[] parts     = line.split(";");
               String[] toolParts = parts[0].split("\\s+");

               tool = new ToolEntry();
               if (parts.length > 1) tool.setDescription(parts[1]);

               for (int i = 0; i < toolParts.length; ++i) {
                  try {
                     if (toolParts[i].startsWith("T")) {
                        intValue = Integer.parseInt(toolParts[i].substring(1));
                        tool.setToolNumber(intValue);
                     } else if (toolParts[i].startsWith("P")) {
                        intValue = Integer.parseInt(toolParts[i].substring(1));
                        tool.setPocketUsed(intValue);
                     } else if (toolParts[i].startsWith("D")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.setDiameter(doubleValue);
                     } else if (toolParts[i].startsWith("X")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setX(doubleValue);
                     } else if (toolParts[i].startsWith("Y")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setY(doubleValue);
                     } else if (toolParts[i].startsWith("Z")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setZ(doubleValue);
                     } else if (toolParts[i].startsWith("A")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setA(doubleValue);
                     } else if (toolParts[i].startsWith("B")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setB(doubleValue);
                     } else if (toolParts[i].startsWith("C")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setC(doubleValue);
                     } else if (toolParts[i].startsWith("U")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setU(doubleValue);
                     } else if (toolParts[i].startsWith("V")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setV(doubleValue);
                     } else if (toolParts[i].startsWith("W")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.getOffset().setW(doubleValue);
                     } else if (toolParts[i].startsWith("I")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.setFrontAngle(doubleValue);
                     } else if (toolParts[i].startsWith("J")) {
                        doubleValue =
                                    Double.parseDouble(toolParts[i].substring(1));
                        tool.setBackAngle(doubleValue);
                     } else if (toolParts[i].startsWith("Q")) {
                        intValue = Integer.parseInt(toolParts[i].substring(1));
                        tool.setOrientation(intValue);
                     }
                  }
                  catch (Throwable t) {}
               }
               tools.add(tool);
            }
         }
         catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
