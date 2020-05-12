package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       ThemeRMA.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    26.4.2020 by Django Reinhard
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


import java.awt.Color;
import java.util.Map;

import javax.swing.UIManager;

import de.schwarzrot.bean.GCodeStyles;


public class ThemeRMA extends AbstractTheme {
   public ThemeRMA(Map<String, Object> settings) {
      super(settings);
      UIManager.put("ProgressBar.foreground", new Color(50, 70, 255));

      settings.put("GCode:styles", new GCodeStyles());

      settings.put("ActiveCode:background", new Color(217, 229, 241));
      settings.put("ActiveCode:font", plain20);
      settings.put("ActiveCode:foreground", Color.GRAY);
      settings.put("ActiveCode:grid.color", new Color(157, 186, 208));
      settings.put("AppSettings:background", new Color(238, 238, 238));
      settings.put("AppSettings:font", plain20);
      settings.put("AppSettings:foreground", Color.BLACK);
      settings.put("AppSettings:tab.font", plain22);
      settings.put("Axis:background", new Color(157, 186, 208));
      settings.put("Axis:font", bold40);
      settings.put("Axis:foreground", new Color(230, 235, 255));
      settings.put("DRO:abs.background", new Color(230, 255, 235));
      settings.put("DRO:abs.font", bold40);
      settings.put("DRO:abs.foreground", new Color(1, 143, 5));
      settings.put("DRO:dtg.background", new Color(255, 235, 230));
      settings.put("DRO:dtg.font", boldItalic40);
      settings.put("DRO:dtg.foreground", Color.RED);
      settings.put("DRO:grid.color", new Color(157, 186, 208));
      settings.put("DRO:rel.background", new Color(230, 235, 255));
      settings.put("DRO:rel.font", bold40);
      settings.put("DRO:rel.foreground", Color.BLUE);
      settings.put("DRO:speed.background", new Color(217, 229, 241));
      settings.put("DRO:speed.font", italic35);
      settings.put("DRO:speed.foreground", Color.BLACK);
      settings.put("DRO:speed.header.background", new Color(157, 186, 208));
      settings.put("DRO:speed.header.font", bold40);
      settings.put("DRO:speed.header.foreground", new Color(217, 229, 241));
      settings.put("FileManager:altbackground", new Color(157, 186, 208));
      settings.put("FileManager:background", new Color(217, 229, 241));
      settings.put("FileManager:font", plain22);
      settings.put("FileManager:foreground", Color.BLACK);
      settings.put("FileManager:grid.color", new Color(192, 192, 192));
      settings.put("FileManager:header.font", bold24);
      settings.put("FileManager:row.height", 34);
      settings.put("FileManager:selected.background", new Color(50, 70, 255));
      settings.put("FileManager:selected.foreground", Color.WHITE);
      settings.put("Fixture:background", new Color(253, 255, 219));
      settings.put("Fixture:entry.font", plain20);
      settings.put("Fixture:foreground", Color.BLACK);
      settings.put("Fixture:grid.color", new Color(157, 186, 208));
      settings.put("Fixture:header.border", Color.BLUE);
      settings.put("Fixture:header.font", plain22);
      settings.put("Fixture:header.foreground", Color.BLACK);
      settings.put("Fixture:prompt.font", bold20);
      settings.put("GCode:edit.background", new Color(254, 255, 236));
      settings.put("GCode:edit.font", plain20);
      settings.put("GCode:edit.foreground", Color.BLACK);
      settings.put("GCode:filename.background", new Color(254, 255, 236));
      settings.put("GCode:filename.changed.foreground", Color.BLACK);
      settings.put("GCode:filename.font", plain18);
      settings.put("GCode:filename.foreground", new Color(192, 192, 192));
      settings.put("GCode:grid.color", Color.WHITE);
      settings.put("GCode:line.altbackground", new Color(253, 255, 219));
      settings.put("GCode:line.background", new Color(254, 255, 236));
      settings.put("GCode:line.font", mono20);
      settings.put("GCode:line.foreground", Color.BLACK);
      settings.put("GCode:line.selected.background", new Color(217, 229, 241));
      settings.put("GCode:line.selected.foreground", Color.BLACK);
      settings.put("GCode:number.font", italic18);
      settings.put("GCode:number.foreground", new Color(192, 192, 192));
      settings.put("GCode:row.height", 32);
      settings.put("JogButton:font", bold20);
      settings.put("JogButton:gradient.start", new Color(198, 224, 234));
      settings.put("JogButton:gradient.end", new Color(90, 116, 140));
      settings.put("JogButton:gradient.disabled.start", Color.LIGHT_GRAY);
      settings.put("JogButton:gradient.disabled.end", Color.GRAY);
      settings.put("JogButton:gradient.selected.start", new Color(189, 159, 42));
      settings.put("JogButton:gradient.selected.end", new Color(237, 217, 84));
      settings.put("JogButton:border", Color.WHITE);
      settings.put("JogButton:foreground", Color.WHITE);
      settings.put("JogButton:disabled.foreground", Color.LIGHT_GRAY);
      settings.put("JogButton:shadow", Color.BLACK);
      settings.put("JogButton:disabled.shadow", Color.GRAY);
      settings.put("JogButtonPane:background", UIManager.get("Panel.background"));
      settings.put("Icon:disabled.factor", 75);
      settings.put("Info:grid.color", Color.WHITE);
      settings.put("MDI:background", new Color(254, 255, 236));
      settings.put("MDI:foreground", Color.BLACK);
      settings.put("Main:grid.color", new Color(157, 186, 208));
      settings.put("MessageLOG:grid.color", new Color(192, 192, 192));
      settings.put("MessageLOG:header.font", bold24);
      settings.put("MessageLOG:message.background", new Color(238, 238, 238));
      settings.put("MessageLOG:message.font", plain20);
      settings.put("MessageLOG:message.foreground", Color.BLACK);
      settings.put("MessageLOG:message.row.height", 30);
      settings.put("Speed:grid.color", new Color(157, 186, 208));
      settings.put("Tool:desc.background", new Color(157, 186, 208));
      settings.put("Tool:desc.font", plain30);
      settings.put("Tool:desc.foreground", Color.BLACK);
      settings.put("Tool:editor.entry", plain20);
      settings.put("Tool:editor.grid.color", new Color(17, 199, 0));
      settings.put("Tool:editor.prompt", bold20);
      settings.put("Tool:info.background", new Color(157, 186, 208));
      settings.put("Tool:info.font", plain30);
      settings.put("Tool:info.foreground", Color.BLACK);
      settings.put("Tool:info.grid.color", new Color(192, 192, 192));
      settings.put("Tool:number.background", new Color(217, 229, 241));
      settings.put("Tool:number.font", bold40);
      settings.put("Tool:number.foreground", new Color(50, 70, 255));
      settings.put("ToolManager:altbackground", new Color(17, 199, 0));
      settings.put("ToolManager:background", new Color(230, 255, 235));
      settings.put("ToolManager:editor.entry.font", plain20);
      settings.put("ToolManager:editor.prompt.font", plain20);
      settings.put("ToolManager:font", plain22);
      settings.put("ToolManager:foreground", Color.BLACK);
      settings.put("ToolManager:grid.color", new Color(192, 192, 192));
      settings.put("ToolManager:header.font", bold24);
      settings.put("ToolManager:row.height", 34);
      settings.put("ToolManager:selected.background", new Color(1, 143, 5));
      settings.put("ToolManager:selected.foreground", new Color(230, 255, 235));
      settings.put("ToolTable:background", new Color(204, 254, 203));
      settings.put("ToolTable:foreground", Color.BLACK);
      settings.put("ToolTable:grid.color", Color.BLACK);
      settings.put("ToolTable:header.font", bold24);
      settings.put("ToolTable:row.height", 34);
      settings.put("ToolTable:selected.background", Color.BLACK);
      settings.put("ToolTable:selected.foreground", Color.WHITE);
      settings.put("ToolTable:table.font", plain22);
      settings.put("Toolbar:grid.color", new Color(192, 192, 192));
   }
}
