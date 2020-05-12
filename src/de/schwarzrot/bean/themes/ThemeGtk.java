package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       ThemeGtk.java
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


public class ThemeGtk extends AbstractTheme {
   public ThemeGtk(Map<String, Object> settings) {
      super(settings);
      settings.put("GCode:styles", new GCodeStyles());

      settings.put("ActiveCode:background", UIManager.get("Panel.background"));
      settings.put("ActiveCode:font", plain20);
      settings.put("ActiveCode:foreground", Color.GRAY);
      settings.put("ActiveCode:grid.color", Color.LIGHT_GRAY);
      settings.put("AppSettings:background", UIManager.get("Label.background"));
      settings.put("AppSettings:font", plain20);
      settings.put("AppSettings:foreground", UIManager.get("Label.foreground"));
      settings.put("AppSettings:tab.font", plain22);
      settings.put("Axis:background", UIManager.get("TextField.background"));
      settings.put("Axis:font", bold40);
      settings.put("Axis:foreground", Color.GRAY);
      settings.put("DRO:abs.background", UIManager.get("Panel.background"));
      settings.put("DRO:abs.font", bold40);
      settings.put("DRO:abs.foreground", new Color(1, 143, 5));
      settings.put("DRO:dtg.background", UIManager.get("Panel.background"));
      settings.put("DRO:dtg.font", boldItalic40);
      settings.put("DRO:dtg.foreground", Color.RED);
      settings.put("DRO:grid.color", Color.WHITE);
      settings.put("DRO:rel.background", UIManager.get("Panel.background"));
      settings.put("DRO:rel.font", bold40);
      settings.put("DRO:rel.foreground", Color.BLUE);
      settings.put("DRO:speed.background", UIManager.get("Panel.background"));
      settings.put("DRO:speed.font", italic35);
      settings.put("DRO:speed.foreground", UIManager.get("Label.foreground"));
      settings.put("DRO:speed.header.background", UIManager.get("TextField.background"));
      settings.put("DRO:speed.header.font", bold40);
      settings.put("DRO:speed.header.foreground", UIManager.get("TextField.foreground"));
      settings.put("FileManager:altbackground", UIManager.get("Table.background"));
      settings.put("FileManager:background", UIManager.get("Table.background"));
      settings.put("FileManager:font", plain22);
      settings.put("FileManager:foreground", UIManager.get("Table.foreground"));
      settings.put("FileManager:grid.color", Color.LIGHT_GRAY);
      settings.put("FileManager:header.font", bold24);
      settings.put("FileManager:row.height", 34);
      settings.put("FileManager:selected.background", UIManager.get("Table.selectionBackground"));
      settings.put("FileManager:selected.foreground", UIManager.get("Table.selectionForeground"));
      settings.put("Fixture:background", UIManager.get("Label.background"));
      settings.put("Fixture:entry.font", plain20);
      settings.put("Fixture:foreground", UIManager.get("Label.foreground"));
      settings.put("Fixture:grid.color", UIManager.get("Panel.background"));
      settings.put("Fixture:header.border", UIManager.get("TableHeader.cellBorder"));
      settings.put("Fixture:header.font", plain22);
      settings.put("Fixture:header.foreground", UIManager.get("TableHeader.foreground"));
      settings.put("Fixture:prompt.font", bold20);
      settings.put("GCode:edit.background", UIManager.get("TextField.background"));
      settings.put("GCode:edit.font", plain20);
      settings.put("GCode:edit.foreground", UIManager.get("TextField.foreground"));
      settings.put("GCode:filename.background", UIManager.get("TextField.background"));
      settings.put("GCode:filename.changed.foreground", UIManager.get("TextField.foreground"));
      settings.put("GCode:filename.font", plain18);
      settings.put("GCode:filename.foreground", Color.GRAY);
      settings.put("GCode:grid.color", Color.LIGHT_GRAY);
      settings.put("GCode:line.altbackground", UIManager.get("Table.background"));
      settings.put("GCode:line.background", UIManager.get("Table.background"));
      settings.put("GCode:line.font", mono20);
      settings.put("GCode:line.foreground", UIManager.get("Table.foreground"));
      settings.put("GCode:line.selected.background", UIManager.get("Table.selectionBackground"));
      settings.put("GCode:line.selected.foreground", UIManager.get("Table.selectionForeground"));
      settings.put("GCode:number.font", italic18);
      settings.put("GCode:number.foreground", Color.LIGHT_GRAY);
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
      settings.put("MDI:background", UIManager.get("Table.background"));
      settings.put("MDI:foreground", UIManager.get("Table.fpreground"));
      settings.put("Main:grid.color", Color.LIGHT_GRAY);
      settings.put("MessageLOG:grid.color", Color.LIGHT_GRAY);
      settings.put("MessageLOG:header.font", bold24);
      settings.put("MessageLOG:message.background", UIManager.get("Table.background"));
      settings.put("MessageLOG:message.font", plain20);
      settings.put("MessageLOG:message.foreground", UIManager.get("Table.foreground"));
      settings.put("MessageLOG:message.row.height", 30);
      settings.put("Speed:grid.color", UIManager.get("Panel.background"));
      settings.put("Tool:desc.background", UIManager.get("TextField.background"));
      settings.put("Tool:desc.font", plain30);
      settings.put("Tool:desc.foreground", Color.GRAY);
      settings.put("Tool:editor.entry", plain20);
      settings.put("Tool:editor.grid.color", UIManager.get("Panel.background"));
      settings.put("Tool:editor.prompt", bold20);
      settings.put("Tool:info.background", UIManager.get("TextField.background"));
      settings.put("Tool:info.font", plain30);
      settings.put("Tool:info.foreground", Color.GRAY);
      settings.put("Tool:info.grid.color", UIManager.get("Panel.background"));
      settings.put("Tool:number.background", UIManager.get("Label.background"));
      settings.put("Tool:number.font", bold40);
      settings.put("Tool:number.foreground", UIManager.get("Label.foreground"));
      settings.put("ToolManager:altbackground", UIManager.get("Table.background"));
      settings.put("ToolManager:background", UIManager.get("Table.background"));
      settings.put("ToolManager:editor.entry.font", plain20);
      settings.put("ToolManager:editor.prompt.font", plain20);
      settings.put("ToolManager:editor.background", UIManager.get("Label.background"));
      settings.put("ToolManager:editor.foreground", UIManager.get("Label.foreground"));
      settings.put("ToolManager:font", plain22);
      settings.put("ToolManager:foreground", UIManager.get("Table.foreground"));
      settings.put("ToolManager:grid.color", Color.LIGHT_GRAY);
      settings.put("ToolManager:header.font", bold24);
      settings.put("ToolManager:row.height", 34);
      settings.put("ToolManager:selected.background", UIManager.get("Table.selectionBackground"));
      settings.put("ToolManager:selected.foreground", UIManager.get("Table.selectionForeground"));
      settings.put("ToolTable:background", UIManager.get("Table.background"));
      settings.put("ToolTable:foreground", UIManager.get("Table.foreground"));
      settings.put("ToolTable:grid.color", Color.LIGHT_GRAY);
      settings.put("ToolTable:header.font", bold24);
      settings.put("ToolTable:row.height", 34);
      settings.put("ToolTable:selected.background", UIManager.get("Table.selectionBackground"));
      settings.put("ToolTable:selected.foreground", UIManager.get("Table.selectionForeground"));
      settings.put("ToolTable:table.font", plain22);
      settings.put("Toolbar:grid.color", UIManager.get("Panel.background"));
   }
}
