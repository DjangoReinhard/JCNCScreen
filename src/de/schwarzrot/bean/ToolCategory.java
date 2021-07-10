package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       ToolCategory.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    10.11.2019 by Django Reinhard
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


import java.util.List;

import de.schwarzrot.linuxcnc.data.CategoryInfo;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;


public class ToolCategory {
   public ToolCategory(ToolProfile profile) {
      this.profile = profile;
      tools        = new BasicEventList<ToolDefinition>();
   }


   public void addTool(ToolDefinition td) {
      tools.getReadWriteLock().writeLock().lock();
      tools.add(td);
      tools.getReadWriteLock().writeLock().unlock();
   }


   public void dump() {
      StringBuilder sb = new StringBuilder("tool-Category: #");

      sb.append(getId());
      sb.append(" => ").append(getName());
      sb.append(" - ").append(getToolProfile().name());

      System.out.println(sb.toString());
   }


   public int getId() {
      return id;
   }


   public String getName() {
      return name;
   }


   public ToolProfile getToolProfile() {
      return profile;
   }


   public List<ToolDefinition> getTools() {
      return tools;
   }


   public void setId(int id) {
      this.id = id;
   }


   public void setName(String name) {
      this.name = name;
   }


   public CategoryInfo toInfo() {
      CategoryInfo ci = new CategoryInfo();

      ci.setProfile(profile.name());
      ci.setType(profile.getProfile());
      if (getName() != null && !getName().isBlank())
         ci.setName(getName());

      return ci;
   }


   public String toLocalString() {
      StringBuilder sb = new StringBuilder();

      if (name != null) {
         sb.append(name);
         sb.append(" (");
      }
      sb.append(LCStatus.getStatus().lm(profile.name()));
      if (name != null)
         sb.append(")");

      return sb.toString();
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      if (name != null) {
         sb.append(name);
         sb.append(" - ");
      }
      sb.append(profile.name());

      return sb.toString();
   }


   private final ToolProfile         profile;
   private String                    name;
   private int                       id;
   private EventList<ToolDefinition> tools;
}
