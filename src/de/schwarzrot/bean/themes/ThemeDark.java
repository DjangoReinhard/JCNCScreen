package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       ThemeDark.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    17.4.2020 by Django Reinhard
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
import java.awt.Insets;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import de.schwarzrot.bean.GCodeStylesDark;


public class ThemeDark extends AbstractTheme {
   public ThemeDark(Map<String, Object> settings) {
      super(settings);
      UIManager.put("Button.focus", Color.GRAY);
      UIManager.put("Button.foreground", Color.BLUE);
      UIManager.put("Button.select", Color.GRAY);
      UIManager.put("Label.background", Color.DARK_GRAY);
      UIManager.put("Label.disabledForeground", Color.GRAY);
      UIManager.put("Label.foreground", Color.ORANGE);
      UIManager.put("OptionPane.background", Color.DARK_GRAY);
      UIManager.put("Panel.background", Color.DARK_GRAY);
      UIManager.put("Panel.foreground", Color.ORANGE);
      UIManager.put("ProgressBar.background", Color.BLACK);
      UIManager.put("ProgressBar.border", new EmptyBorder(0, 0, 0, 0));
      UIManager.put("ProgressBar.foreground", Color.ORANGE);
      UIManager.put("RadioButton.background", Color.DARK_GRAY);
      UIManager.put("RadioButton.foreground", Color.ORANGE);
      UIManager.put("ScrollBar.background", Color.BLACK);
      UIManager.put("ScrollBar.shadow", Color.GRAY);
      UIManager.put("Slider.background", Color.DARK_GRAY);
      UIManager.put("Spinner.background", Color.DARK_GRAY);
      UIManager.put("Spinner.foreground", Color.ORANGE);
      UIManager.put("TabbedPane.borderHightlightColor", Color.ORANGE);
      UIManager.put("TabbedPane.darkShadow", Color.DARK_GRAY);
      UIManager.put("TabbedPane.foreground", Color.BLUE);
      UIManager.put("TabbedPane.selectHighlight", Color.YELLOW);
      UIManager.put("TabbedPane.selected", Color.ORANGE);
      UIManager.put("TabbedPane.tabInsets", new Insets(3, 30, 5, 30));
      UIManager.put("TabbedPane.unselectedBackground", Color.GRAY);
      UIManager.put("Table.altbackground", Color.DARK_GRAY);
      UIManager.put("Table.background", Color.BLACK);
      UIManager.put("Table.focusCellBackground", Color.BLUE);
      UIManager.put("Table.focusCellForeground", Color.YELLOW);
      UIManager.put("Table.focusCellHighlightBorder", new LineBorder(Color.GRAY, 1));
      UIManager.put("Table.foreground", Color.ORANGE);
      UIManager.put("Table.gridColor", Color.BLUE);
      UIManager.put("Table.scrollPaneBorder", new LineBorder(new Color(0, 0, 80), 1));
      UIManager.put("Table.selectionBackground", Color.BLUE);
      UIManager.put("Table.selectionForeground", Color.YELLOW);
      UIManager.put("TableHeader.background", Color.DARK_GRAY);
      UIManager.put("TableHeader.cellBorder", new LineBorder(Color.BLACK, 1));
      UIManager.put("TableHeader.focusCellBackground", Color.BLACK);
      UIManager.put("TableHeader.foreground", new Color(217, 229, 241));
      UIManager.put("TextField.background", Color.BLACK);
      UIManager.put("TextField.border", new LineBorder(new Color(90, 116, 140), 1));
      UIManager.put("TextField.caretForeground", Color.YELLOW);
      UIManager.put("TextField.foreground", Color.ORANGE);
      UIManager.put("TextField.shadow", Color.BLUE);

      settings.put("GCode:styles", new GCodeStylesDark());

      settings.put("ActiveCode:background", Color.BLACK);
      settings.put("ActiveCode:font", plain20);
      settings.put("ActiveCode:foreground", Color.GRAY);
      settings.put("ActiveCode:grid.color", Color.DARK_GRAY);
      settings.put("AppSettings:background", new Color(0, 0, 50));
      settings.put("AppSettings:font", plain20);
      settings.put("AppSettings:foreground", Color.LIGHT_GRAY);
      settings.put("AppSettings:tab.font", plain22);
      settings.put("Axis:background", Color.BLACK);
      settings.put("Axis:font", bold40);
      settings.put("Axis:foreground", Color.GRAY);
      settings.put("DRO:abs.background", new Color(0, 70, 0));
      settings.put("DRO:abs.font", bold40);
      settings.put("DRO:abs.foreground", new Color(230, 255, 230));
      settings.put("DRO:dtg.background", new Color(70, 0, 0));
      settings.put("DRO:dtg.font", boldItalic40);
      settings.put("DRO:dtg.foreground", new Color(255, 230, 230));
      settings.put("DRO:grid.color", Color.DARK_GRAY);
      settings.put("DRO:rel.background", new Color(0, 0, 70));
      settings.put("DRO:rel.font", bold40);
      settings.put("DRO:rel.foreground", new Color(230, 230, 255));
      settings.put("DRO:speed.background", Color.DARK_GRAY);
      settings.put("DRO:speed.font", italic35);
      settings.put("DRO:speed.foreground", Color.LIGHT_GRAY);
      settings.put("DRO:speed.header.background", Color.BLACK);
      settings.put("DRO:speed.header.font", bold40);
      settings.put("DRO:speed.header.foreground", Color.GRAY);
      settings.put("FileManager:altbackground", Color.DARK_GRAY);
      settings.put("FileManager:background", Color.BLACK);
      settings.put("FileManager:font", plain22);
      settings.put("FileManager:foreground", Color.GRAY);
      settings.put("FileManager:grid.color", Color.DARK_GRAY);
      settings.put("FileManager:header.font", bold24);
      settings.put("FileManager:row.height", 34);
      settings.put("FileManager:selected.background", Color.ORANGE);
      settings.put("FileManager:selected.foreground", Color.DARK_GRAY);
      settings.put("Fixture:background", new Color(30, 0, 0));
      settings.put("Fixture:entry.font", plain22);
      settings.put("Fixture:foreground", Color.BLACK);
      settings.put("Fixture:grid.color", Color.DARK_GRAY);
      settings.put("Fixture:header.border", new Color(150, 0, 0));
      settings.put("Fixture:header.font", bold22);
      settings.put("Fixture:header.foreground", Color.RED);
      settings.put("Fixture:prompt.font", bold20);
      settings.put("GCode:edit.background", Color.BLACK);
      settings.put("GCode:edit.font", plain20);
      settings.put("GCode:edit.foreground", Color.YELLOW);
      settings.put("GCode:filename.background", Color.DARK_GRAY);
      settings.put("GCode:filename.changed.foreground", Color.YELLOW);
      settings.put("GCode:filename.font", plain18);
      settings.put("GCode:filename.foreground", Color.ORANGE);
      settings.put("GCode:grid.color", Color.DARK_GRAY);
      settings.put("GCode:line.altbackground", Color.BLACK);
      settings.put("GCode:line.background", new Color(30, 30, 30));
      settings.put("GCode:line.font", mono20);
      settings.put("GCode:line.foreground", Color.LIGHT_GRAY);
      settings.put("GCode:line.selected.background", new Color(217, 229, 241));
      settings.put("GCode:line.selected.foreground", Color.BLACK);
      settings.put("GCode:number.font", italic18);
      settings.put("GCode:number.foreground", Color.LIGHT_GRAY);
      settings.put("GCode:row.height", 32);
      settings.put("Icon:disabled.factor", 30);
      settings.put("Info:grid.color", Color.GRAY);
      settings.put("MDI:background", new Color(254, 255, 236));
      settings.put("MDI:foreground", Color.BLACK);
      settings.put("Main:grid.color", Color.DARK_GRAY);
      settings.put("MessageLOG:grid.color", Color.GRAY);
      settings.put("MessageLOG:header.font", bold22);
      settings.put("MessageLOG:message.background", Color.BLACK);
      settings.put("MessageLOG:message.font", plain20);
      settings.put("MessageLOG:message.foreground", Color.WHITE);
      settings.put("MessageLOG:message.row.height", 30);
      settings.put("Speed:grid.color", Color.GRAY);
      settings.put("Tool:desc.background", Color.BLACK);
      settings.put("Tool:desc.font", plain30);
      settings.put("Tool:desc.foreground", Color.LIGHT_GRAY);
      settings.put("Tool:editor.entry", plain20);
      settings.put("Tool:editor.grid.color", new Color(0, 80, 0));
      settings.put("Tool:editor.prompt", bold20);
      settings.put("Tool:info.background", Color.BLACK);
      settings.put("Tool:info.font", plain30);
      settings.put("Tool:info.foreground", Color.LIGHT_GRAY);
      settings.put("Tool:info.grid.color", Color.GRAY);
      settings.put("Tool:number.background", Color.DARK_GRAY);
      settings.put("Tool:number.font", bold40);
      settings.put("Tool:number.foreground", Color.LIGHT_GRAY);
      settings.put("ToolManager:altbackground", new Color(0, 0, 80));
      settings.put("ToolManager:background", new Color(0, 0, 50));
      settings.put("ToolManager:editor.entry.font", plain22);
      settings.put("ToolManager:editor.prompt.font", plain20);
      settings.put("ToolManager:font", plain22);
      settings.put("ToolManager:foreground", new Color(200, 200, 255));
      settings.put("ToolManager:grid.color", new Color(0, 0, 100));
      settings.put("ToolManager:header.font", bold24);
      settings.put("ToolManager:row.height", 34);
      settings.put("ToolManager:selected.background", Color.ORANGE);
      settings.put("ToolManager:selected.foreground", new Color(0, 0, 50));
      settings.put("ToolTable:background", new Color(0, 30, 0));
      settings.put("ToolTable:foreground", new Color(230, 255, 230));
      settings.put("ToolTable:grid.color", new Color(0, 80, 0));
      settings.put("ToolTable:header.font", bold24);
      settings.put("ToolTable:row.height", 34);
      settings.put("ToolTable:selected.background", new Color(15, 200, 0));
      settings.put("ToolTable:selected.foreground", new Color(0, 30, 0));
      settings.put("ToolTable:table.font", plain22);
      settings.put("Toolbar:grid.color", Color.GRAY);
   }
}
