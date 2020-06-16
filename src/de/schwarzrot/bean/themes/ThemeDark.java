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
      UIManager.put(Button_focus, Color.GRAY);
      UIManager.put(Button_foreground, Color.BLUE);
      UIManager.put(Button_select, Color.GRAY);
      UIManager.put(Label_background, Color.DARK_GRAY);
      UIManager.put(Label_disabledForeground, Color.GRAY);
      UIManager.put(Label_foreground, Color.ORANGE);
      UIManager.put(OptionPane_background, Color.DARK_GRAY);
      UIManager.put(Panel_background, Color.DARK_GRAY);
      UIManager.put(Panel_foreground, Color.ORANGE);
      UIManager.put(ProgressBar_background, Color.BLACK);
      UIManager.put(ProgressBar_border, new EmptyBorder(0, 0, 0, 0));
      UIManager.put(ProgressBar_foreground, Color.ORANGE);
      UIManager.put(RadioButton_background, Color.DARK_GRAY);
      UIManager.put(RadioButton_foreground, Color.ORANGE);
      UIManager.put(ScrollBar_background, Color.BLACK);
      UIManager.put(ScrollBar_shadow, Color.GRAY);
      UIManager.put(Slider_background, Color.DARK_GRAY);
      UIManager.put(Spinner_background, Color.DARK_GRAY);
      UIManager.put(Spinner_foreground, Color.ORANGE);
      UIManager.put(TabbedPane_borderHightlightColor, Color.ORANGE);
      UIManager.put(TabbedPane_darkShadow, Color.DARK_GRAY);
      UIManager.put(TabbedPane_foreground, Color.BLUE);
      UIManager.put(TabbedPane_selectHighlight, Color.YELLOW);
      UIManager.put(TabbedPane_selected, Color.ORANGE);
      UIManager.put(TabbedPane_tabInsets, new Insets(3, 30, 5, 30));
      UIManager.put(TabbedPane_unselectedBackground, Color.GRAY);
      UIManager.put(Table_altbackground, Color.DARK_GRAY);
      UIManager.put(Table_background, Color.BLACK);
      UIManager.put(Table_focusCellBackground, Color.BLUE);
      UIManager.put(Table_focusCellForeground, Color.YELLOW);
      UIManager.put(Table_focusCellHighlightBorder, new LineBorder(Color.GRAY, 1));
      UIManager.put(Table_foreground, Color.ORANGE);
      UIManager.put(Table_gridColor, Color.BLUE);
      UIManager.put(Table_scrollPaneBorder, new LineBorder(new Color(0, 0, 80), 1));
      UIManager.put(Table_selectionBackground, Color.BLUE);
      UIManager.put(Table_selectionForeground, Color.YELLOW);
      UIManager.put(TableHeader_background, Color.DARK_GRAY);
      UIManager.put(TableHeader_cellBorder, new LineBorder(Color.BLACK, 1));
      UIManager.put(TableHeader_focusCellBackground, Color.BLACK);
      UIManager.put(TableHeader_foreground, new Color(217, 229, 241));
      UIManager.put(TextField_background, Color.BLACK);
      UIManager.put(TextField_border, new LineBorder(new Color(90, 116, 140), 1));
      UIManager.put(TextField_caretForeground, Color.YELLOW);
      UIManager.put(TextField_foreground, Color.ORANGE);
      UIManager.put(TextField_shadow, Color.BLUE);

      settings.put(GCode_styles, new GCodeStylesDark());
      settings.put(ActiveCode_background, Color.BLACK);
      settings.put(ActiveCode_font, plain20);
      settings.put(ActiveCode_foreground, Color.GRAY);
      settings.put(ActiveCode_grid_color, Color.DARK_GRAY);
      settings.put(AppSettings_background, new Color(0, 0, 50));
      settings.put(AppSettings_font, plain20);
      settings.put(AppSettings_foreground, Color.LIGHT_GRAY);
      settings.put(AppSettings_tab_font, plain22);
      settings.put(Axis_background, Color.BLACK);
      settings.put(Axis_font, bold40);
      settings.put(Axis_foreground, Color.GRAY);
      settings.put(DRO_abs_background, new Color(0, 70, 0));
      settings.put(DRO_abs_font, bold40);
      settings.put(DRO_abs_foreground, new Color(230, 255, 230));
      settings.put(DRO_dtg_background, new Color(70, 0, 0));
      settings.put(DRO_dtg_font, boldItalic40);
      settings.put(DRO_dtg_foreground, new Color(255, 230, 230));
      settings.put(DRO_grid_color, Color.DARK_GRAY);
      settings.put(DRO_rel_background, new Color(0, 0, 70));
      settings.put(DRO_rel_font, bold40);
      settings.put(DRO_rel_foreground, new Color(230, 230, 255));
      settings.put(DRO_speed_background, Color.DARK_GRAY);
      settings.put(DRO_speed_font, italic35);
      settings.put(DRO_speed_foreground, Color.LIGHT_GRAY);
      settings.put(DRO_speed_header_background, Color.BLACK);
      settings.put(DRO_speed_header_font, bold40);
      settings.put(DRO_speed_header_foreground, Color.GRAY);
      settings.put(FileManager_altbackground, Color.DARK_GRAY);
      settings.put(FileManager_background, Color.BLACK);
      settings.put(FileManager_font, plain22);
      settings.put(FileManager_foreground, Color.GRAY);
      settings.put(FileManager_grid_color, Color.DARK_GRAY);
      settings.put(FileManager_header_font, bold24);
      settings.put(FileManager_row_height, 34);
      settings.put(FileManager_selected_background, Color.ORANGE);
      settings.put(FileManager_selected_foreground, Color.DARK_GRAY);
      settings.put(Fixture_background, new Color(30, 0, 0));
      settings.put(Fixture_entry_font, plain22);
      settings.put(Fixture_foreground, Color.BLACK);
      settings.put(Fixture_grid_color, Color.DARK_GRAY);
      settings.put(Fixture_header_border, new Color(150, 0, 0));
      settings.put(Fixture_header_font, bold22);
      settings.put(Fixture_header_foreground, Color.RED);
      settings.put(Fixture_prompt_font, bold20);
      settings.put(GCode_edit_background, Color.BLACK);
      settings.put(GCode_edit_font, plain20);
      settings.put(GCode_edit_foreground, Color.YELLOW);
      settings.put(GCode_filename_background, Color.DARK_GRAY);
      settings.put(GCode_filename_changed_foreground, Color.YELLOW);
      settings.put(GCode_filename_font, plain18);
      settings.put(GCode_filename_foreground, Color.ORANGE);
      settings.put(GCode_grid_color, Color.DARK_GRAY);
      settings.put(GCode_line_altbackground, Color.BLACK);
      settings.put(GCode_line_background, new Color(30, 30, 30));
      settings.put(GCode_line_font, mono20);
      settings.put(GCode_line_foreground, Color.LIGHT_GRAY);
      settings.put(GCode_line_selected_background, new Color(217, 229, 241));
      settings.put(GCode_line_selected_foreground, Color.BLACK);
      settings.put(GCode_number_font, italic18);
      settings.put(GCode_number_foreground, Color.LIGHT_GRAY);
      settings.put(GCode_row_height, 32);
      settings.put(JogButton_font, bold20);
      settings.put(JogButton_gradient_start, Color.ORANGE);
      settings.put(JogButton_gradient_end, Color.DARK_GRAY);
      settings.put(JogButton_gradient_disabled_start, Color.GRAY);
      settings.put(JogButton_gradient_disabled_end, Color.DARK_GRAY);
      settings.put(JogButton_gradient_selected_start, Color.GRAY);
      settings.put(JogButton_gradient_selected_end, Color.ORANGE);
      settings.put(JogButton_border, Color.GRAY);
      settings.put(JogButton_foreground, Color.WHITE);
      settings.put(JogButton_disabled_foreground, Color.LIGHT_GRAY);
      settings.put(JogButton_shadow, Color.BLACK);
      settings.put(JogButton_disabled_shadow, Color.GRAY);
      settings.put(JogButtonPane_background, UIManager.get(Panel_background));
      settings.put(Icon_disabled_factor, 30);
      settings.put(Info_grid_color, Color.GRAY);
      settings.put(MDI_background, new Color(254, 255, 236));
      settings.put(MDI_foreground, Color.BLACK);
      settings.put(Main_grid_color, Color.DARK_GRAY);
      settings.put(MessageLOG_grid_color, Color.GRAY);
      settings.put(MessageLOG_header_font, bold22);
      settings.put(MessageLOG_message_background, Color.BLACK);
      settings.put(MessageLOG_message_font, plain20);
      settings.put(MessageLOG_message_foreground, Color.WHITE);
      settings.put(MessageLOG_message_row_height, 30);
      settings.put(Speed_grid_color, Color.GRAY);
      settings.put(Tool_desc_background, Color.BLACK);
      settings.put(Tool_desc_font, plain30);
      settings.put(Tool_desc_foreground, Color.LIGHT_GRAY);
      settings.put(Tool_editor_entry, plain20);
      settings.put(Tool_editor_grid_color, new Color(0, 80, 0));
      settings.put(Tool_editor_prompt, bold20);
      settings.put(Tool_info_background, Color.BLACK);
      settings.put(Tool_info_font, plain30);
      settings.put(Tool_info_foreground, Color.LIGHT_GRAY);
      settings.put(Tool_info_grid_color, Color.GRAY);
      settings.put(Tool_number_background, Color.DARK_GRAY);
      settings.put(Tool_number_font, bold40);
      settings.put(Tool_number_foreground, Color.LIGHT_GRAY);
      settings.put(ToolManager_altbackground, new Color(0, 0, 80));
      settings.put(ToolManager_background, new Color(0, 0, 50));
      settings.put(ToolManager_editor_entry_font, plain22);
      settings.put(ToolManager_editor_prompt_font, plain20);
      settings.put(ToolManager_font, plain22);
      settings.put(ToolManager_foreground, new Color(200, 200, 255));
      settings.put(ToolManager_grid_color, new Color(0, 0, 100));
      settings.put(ToolManager_header_font, bold24);
      settings.put(ToolManager_row_height, 34);
      settings.put(ToolManager_selected_background, Color.ORANGE);
      settings.put(ToolManager_selected_foreground, new Color(0, 0, 50));
      settings.put(ToolTable_background, new Color(0, 30, 0));
      settings.put(ToolTable_foreground, new Color(230, 255, 230));
      settings.put(ToolTable_grid_color, new Color(0, 80, 0));
      settings.put(ToolTable_header_font, bold24);
      settings.put(ToolTable_row_height, 34);
      settings.put(ToolTable_selected_background, new Color(15, 200, 0));
      settings.put(ToolTable_selected_foreground, new Color(0, 30, 0));
      settings.put(ToolTable_table_font, plain22);
      settings.put(Toolbar_grid_color, Color.GRAY);
   }
}
