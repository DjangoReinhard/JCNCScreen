package de.schwarzrot.model;
/* 
 * **************************************************************************
 * 
 *  file:       ToolInfo.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    29.9.2019 by Django Reinhard
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
import java.util.List;

import de.schwarzrot.bean.ToolEntry;


public class ToolInfo extends AbstractModel {
   public ToolInfo(List<ToolEntry> tools) {
      this.tools      = tools;
      this.offset     = new CanonPosition();
      this.activeTool = 99;
   }


   public ToolEntry getActiveTool() {
      return tools.get(activeTool);
   }


   public int getActiveToolNum() {
      return activeTool;
   }


   public ToolEntry getNextTool() {
      return tools.get(nextTool);
   }


   public int getNextToolNum() {
      return nextTool;
   }


   public CanonPosition getOffset() {
      return offset;
   }


   public void setActiveToolNum(int activeTool) {
      int ov = this.activeTool;
      this.activeTool = activeTool;
      if (ov != this.activeTool) firePropertyChange(new PropertyChangeEvent(this,
                                                                            "activeToolNum",
                                                                            ov,
                                                                            activeTool));
   }


   public void setNextToolNum(int nextTool) {
      int ov = this.nextTool;
      this.nextTool = nextTool;
      if (ov != this.nextTool) firePropertyChange(new PropertyChangeEvent(this,
                                                                          "nextToolNum",
                                                                          ov,
                                                                          nextTool));
   }


   public void setOffset(CanonPosition offset) {
      this.offset = offset;
   }

   private int             activeTool;
   private int             nextTool;
   private CanonPosition   offset;
   private List<ToolEntry> tools;
}
