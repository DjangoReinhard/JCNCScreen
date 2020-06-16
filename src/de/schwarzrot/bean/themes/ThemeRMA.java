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
      UIManager.put(ProgressBar_foreground, new Color(50, 70, 255));
      settings.put(GCode_styles, new GCodeStyles());

      settings.put(ActiveCode_background, new Color(217, 229, 241));
      settings.put(ActiveCode_font, plain20);
      settings.put(ActiveCode_foreground, Color.GRAY);
      settings.put(ActiveCode_grid_color, new Color(157, 186, 208));
      settings.put(AppSettings_background, new Color(238, 238, 238));
      settings.put(AppSettings_font, plain20);
      settings.put(AppSettings_foreground, Color.BLACK);
      settings.put(AppSettings_tab_font, plain22);
      settings.put(Axis_background, new Color(157, 186, 208));
      settings.put(Axis_font, bold40);
      settings.put(Axis_foreground, new Color(230, 235, 255));
      settings.put(DRO_abs_background, new Color(230, 255, 235));
      settings.put(DRO_abs_font, bold40);
      settings.put(DRO_abs_foreground, new Color(1, 143, 5));
      settings.put(DRO_dtg_background, new Color(255, 235, 230));
      settings.put(DRO_dtg_font, boldItalic40);
      settings.put(DRO_dtg_foreground, Color.RED);
      settings.put(DRO_grid_color, new Color(157, 186, 208));
      settings.put(DRO_rel_background, new Color(230, 235, 255));
      settings.put(DRO_rel_font, bold40);
      settings.put(DRO_rel_foreground, Color.BLUE);
      settings.put(DRO_speed_background, new Color(217, 229, 241));
      settings.put(DRO_speed_font, italic35);
      settings.put(DRO_speed_foreground, Color.BLACK);
      settings.put(DRO_speed_header_background, new Color(157, 186, 208));
      settings.put(DRO_speed_header_font, bold40);
      settings.put(DRO_speed_header_foreground, new Color(217, 229, 241));
      settings.put(FileManager_altbackground, new Color(157, 186, 208));
      settings.put(FileManager_background, new Color(217, 229, 241));
      settings.put(FileManager_font, plain22);
      settings.put(FileManager_foreground, Color.BLACK);
      settings.put(FileManager_grid_color, new Color(192, 192, 192));
      settings.put(FileManager_header_font, bold24);
      settings.put(FileManager_row_height, 34);
      settings.put(FileManager_selected_background, new Color(50, 70, 255));
      settings.put(FileManager_selected_foreground, Color.WHITE);
      settings.put(Fixture_background, new Color(253, 255, 219));
      settings.put(Fixture_entry_font, plain20);
      settings.put(Fixture_foreground, Color.BLACK);
      settings.put(Fixture_grid_color, new Color(157, 186, 208));
      settings.put(Fixture_header_border, Color.BLUE);
      settings.put(Fixture_header_font, plain22);
      settings.put(Fixture_header_foreground, Color.BLACK);
      settings.put(Fixture_prompt_font, bold20);
      settings.put(GCode_edit_background, new Color(254, 255, 236));
      settings.put(GCode_edit_font, plain20);
      settings.put(GCode_edit_foreground, Color.BLACK);
      settings.put(GCode_filename_background, new Color(254, 255, 236));
      settings.put(GCode_filename_changed_foreground, Color.BLACK);
      settings.put(GCode_filename_font, plain18);
      settings.put(GCode_filename_foreground, new Color(192, 192, 192));
      settings.put(GCode_grid_color, Color.WHITE);
      settings.put(GCode_line_altbackground, new Color(253, 255, 219));
      settings.put(GCode_line_background, new Color(254, 255, 236));
      settings.put(GCode_line_font, mono20);
      settings.put(GCode_line_foreground, Color.BLACK);
      settings.put(GCode_line_selected_background, new Color(217, 229, 241));
      settings.put(GCode_line_selected_foreground, Color.BLACK);
      settings.put(GCode_number_font, italic18);
      settings.put(GCode_number_foreground, new Color(192, 192, 192));
      settings.put(GCode_row_height, 32);
      settings.put(JogButton_font, bold20);
      settings.put(JogButton_gradient_start, new Color(198, 224, 234));
      settings.put(JogButton_gradient_end, new Color(90, 116, 140));
      settings.put(JogButton_gradient_disabled_start, Color.LIGHT_GRAY);
      settings.put(JogButton_gradient_disabled_end, Color.GRAY);
      settings.put(JogButton_gradient_selected_start, new Color(189, 159, 42));
      settings.put(JogButton_gradient_selected_end, new Color(237, 217, 84));
      settings.put(JogButton_border, Color.WHITE);
      settings.put(JogButton_foreground, Color.WHITE);
      settings.put(JogButton_disabled_foreground, Color.LIGHT_GRAY);
      settings.put(JogButton_shadow, Color.BLACK);
      settings.put(JogButton_disabled_shadow, Color.GRAY);
      settings.put(JogButtonPane_background, UIManager.get(Panel_background));
      settings.put(Icon_disabled_factor, 75);
      settings.put(Info_grid_color, Color.WHITE);
      settings.put(MDI_background, new Color(254, 255, 236));
      settings.put(MDI_foreground, Color.BLACK);
      settings.put(Main_grid_color, new Color(157, 186, 208));
      settings.put(MessageLOG_grid_color, new Color(192, 192, 192));
      settings.put(MessageLOG_header_font, bold24);
      settings.put(MessageLOG_message_background, new Color(238, 238, 238));
      settings.put(MessageLOG_message_font, plain20);
      settings.put(MessageLOG_message_foreground, Color.BLACK);
      settings.put(MessageLOG_message_row_height, 30);
      settings.put(Speed_grid_color, new Color(157, 186, 208));
      settings.put(Tool_desc_background, new Color(157, 186, 208));
      settings.put(Tool_desc_font, plain30);
      settings.put(Tool_desc_foreground, Color.BLACK);
      settings.put(Tool_editor_entry, plain20);
      settings.put(Tool_editor_grid_color, new Color(17, 199, 0));
      settings.put(Tool_editor_prompt, bold20);
      settings.put(Tool_info_background, new Color(157, 186, 208));
      settings.put(Tool_info_font, plain30);
      settings.put(Tool_info_foreground, Color.BLACK);
      settings.put(Tool_info_grid_color, new Color(192, 192, 192));
      settings.put(Tool_number_background, new Color(217, 229, 241));
      settings.put(Tool_number_font, bold40);
      settings.put(Tool_number_foreground, new Color(50, 70, 255));
      settings.put(ToolManager_altbackground, new Color(17, 199, 0));
      settings.put(ToolManager_background, new Color(230, 255, 235));
      settings.put(ToolManager_editor_entry_font, plain20);
      settings.put(ToolManager_editor_prompt_font, plain20);
      settings.put(ToolManager_font, plain22);
      settings.put(ToolManager_foreground, Color.BLACK);
      settings.put(ToolManager_grid_color, new Color(192, 192, 192));
      settings.put(ToolManager_header_font, bold24);
      settings.put(ToolManager_row_height, 34);
      settings.put(ToolManager_selected_background, new Color(1, 143, 5));
      settings.put(ToolManager_selected_foreground, new Color(230, 255, 235));
      settings.put(ToolTable_background, new Color(204, 254, 203));
      settings.put(ToolTable_foreground, Color.BLACK);
      settings.put(ToolTable_grid_color, Color.BLACK);
      settings.put(ToolTable_header_font, bold24);
      settings.put(ToolTable_row_height, 34);
      settings.put(ToolTable_selected_background, Color.BLACK);
      settings.put(ToolTable_selected_foreground, Color.WHITE);
      settings.put(ToolTable_table_font, plain22);
      settings.put(Toolbar_grid_color, new Color(192, 192, 192));
   }
}
