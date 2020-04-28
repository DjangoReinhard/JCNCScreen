package de.schwarzrot.bean;
/* 
 * **************************************************************************
 * 
 *  file:       ToolLibrary.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    9.11.2019 by Django Reinhard
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

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import de.schwarzrot.linuxcnc.data.LibInfo;


public class ToolLibrary {
   public ToolLibrary() {
      tools          = new DefaultMutableTreeNode(this);
      toolProfiles   = new HashMap<String, DefaultMutableTreeNode>();
      toolCategories = new HashMap<String, ToolCategory>();
   }


   public DefaultMutableTreeNode addCategory(ToolCategory c) {
      DefaultMutableTreeNode n  = toolProfiles.get(c.getToolProfile().name());
      DefaultMutableTreeNode rv = new DefaultMutableTreeNode(c);

      toolCategories.put(c.toString(), c);
      if (n == null) {
         if (c.getName() != null && !c.getName().isEmpty()) {
            ToolCategory tc = new ToolCategory(c.getToolProfile());

            toolCategories.put(tc.toString(), tc);
            n = new DefaultMutableTreeNode(tc);
            tools.add(n);
            n.add(rv);
            toolProfiles.put(c.getToolProfile().name(), n);
         } else {
            tools.add(rv);
            toolProfiles.put(c.getToolProfile().name(), rv);
         }
      } else {
         n.add(rv);
      }
      return rv;
   }


   public void addTool(ToolDefinition td) {
      // ToolCategory tc = td.getToolCategory();

      if (td.getToolNumber() == 0) {
         td.setToolNumber(getNextToolNumber());
      }
      this.registerToolNumber(td);
      // tc.addTool(td);
   }


   public ToolCategory getCategory(String name) {
      return toolCategories.get(name);
   }


   public int getNextToolNumber() {
      return toolNumber + 1;
   }


   public String getNumberFormat() {
      return numberFormat;
   }


   public DefaultMutableTreeNode getRoot() {
      return tools;
   }


   public String getToolNameFormat() {
      return toolNameFormat;
   }


   public String getVersion() {
      return version;
   }


   public void setNumberFormat(String numberFormat) {
      this.numberFormat = numberFormat;
   }


   public void setToolNameFormat(String toolNameFormat) {
      this.toolNameFormat = toolNameFormat;
   }


   public void setVersion(String version) {
      this.version = version;
   }


   public LibInfo toInfo() {
      LibInfo rv = new LibInfo();

      rv.setNumberFormat(numberFormat);
      rv.setToolNameTemplate(toolNameFormat);
      rv.setVersion(version);

      return rv;
   }


   public Map<String, ToolCategory> toolCategories() {
      return toolCategories;
   }


   protected void registerToolNumber(ToolDefinition t) {
      if (t.getToolNumber() > toolNumber) toolNumber = t.getToolNumber();
   }

   private String                              version;
   private String                              toolNameFormat;
   private String                              numberFormat;
   private int                                 toolNumber;
   private DefaultMutableTreeNode              tools;
   private Map<String, ToolCategory>           toolCategories;
   private Map<String, DefaultMutableTreeNode> toolProfiles;
}
