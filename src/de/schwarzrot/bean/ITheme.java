package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       ITheme.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    14.4.2020 by Django Reinhard
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


import java.io.PrintWriter;


public interface ITheme {
   public void export(PrintWriter pw);


   public static final String DefaultFont                       = "Verdana";
   public static final String MonoFont                          = "Monospace";
   public static final String LED_on_color                      = "LED:on.color";
   public static final String LED_off_color                     = "LED:off.color";
   public static final String Toolbar_button_size               = "Toolbar:button.size";
   public static final String DRO_pos_format                    = "DRO:pos.format";
   public static final String DRO_speed_format                  = "DRO:speed.format";
   public static final String Application_mode_portrait         = "Application:mode.portrait";
   public static final String ActiveCode_background             = "ActiveCode:background";
   public static final String ActiveCode_font                   = "ActiveCode:font";
   public static final String ActiveCode_foreground             = "ActiveCode:foreground";
   public static final String ActiveCode_grid_color             = "ActiveCode:grid.color";
   public static final String AppSettings_background            = "AppSettings:background";
   public static final String AppSettings_font                  = "AppSettings:font";
   public static final String AppSettings_foreground            = "AppSettings:foreground";
   public static final String AppSettings_tab_font              = "AppSettings:tab.font";
   public static final String Axis_background                   = "Axis:background";
   public static final String Axis_font                         = "Axis:font";
   public static final String Axis_foreground                   = "Axis:foreground";
   public static final String Button_focus                      = "Button_focus";
   public static final String Button_foreground                 = "Button_foreground";
   public static final String Button_select                     = "Button_select";
   public static final String DRO_abs_background                = "DRO:abs.background";
   public static final String DRO_abs_font                      = "DRO:abs.font";
   public static final String DRO_abs_foreground                = "DRO:abs.foreground";
   public static final String DRO_dtg_background                = "DRO:dtg.background";
   public static final String DRO_dtg_font                      = "DRO:dtg.font";
   public static final String DRO_dtg_foreground                = "DRO:dtg.foreground";
   public static final String DRO_grid_color                    = "DRO:grid.color";
   public static final String DRO_rel_background                = "DRO:rel.background";
   public static final String DRO_rel_font                      = "DRO:rel.font";
   public static final String DRO_rel_foreground                = "DRO:rel.foreground";
   public static final String DRO_speed_background              = "DRO:speed.background";
   public static final String DRO_speed_font                    = "DRO:speed.font";
   public static final String DRO_speed_foreground              = "DRO:speed.foreground";
   public static final String DRO_speed_header_background       = "DRO:speed.header.background";
   public static final String DRO_speed_header_font             = "DRO:speed.header.font";
   public static final String DRO_speed_header_foreground       = "DRO:speed.header.foreground";
   public static final String FileManager_altbackground         = "FileManager:altbackground";
   public static final String FileManager_background            = "FileManager:background";
   public static final String FileManager_font                  = "FileManager:font";
   public static final String FileManager_foreground            = "FileManager:foreground";
   public static final String FileManager_grid_color            = "FileManager:grid.color";
   public static final String FileManager_header_font           = "FileManager:header.font";
   public static final String FileManager_row_height            = "FileManager:row.height";
   public static final String FileManager_selected_background   = "FileManager:selected.background";
   public static final String FileManager_selected_foreground   = "FileManager:selected.foreground";
   public static final String Fixture_background                = "Fixture:background";
   public static final String Fixture_entry_font                = "Fixture:entry.font";
   public static final String Fixture_foreground                = "Fixture:foreground";
   public static final String Fixture_grid_color                = "Fixture:grid.color";
   public static final String Fixture_header_border             = "Fixture:header.border";
   public static final String Fixture_header_font               = "Fixture:header.font";
   public static final String Fixture_header_foreground         = "Fixture:header.foreground";
   public static final String Fixture_prompt_font               = "Fixture:prompt.font";
   public static final String GCode_edit_background             = "GCode:edit.background";
   public static final String GCode_edit_font                   = "GCode:edit.font";
   public static final String GCode_edit_foreground             = "GCode:edit.foreground";
   public static final String GCode_filename_background         = "GCode:filename.background";
   public static final String GCode_filename_changed_foreground = "GCode:filename.changed.foreground";
   public static final String GCode_filename_font               = "GCode:filename.font";
   public static final String GCode_filename_foreground         = "GCode:filename.foreground";
   public static final String GCode_grid_color                  = "GCode:grid.color";
   public static final String GCode_line_altbackground          = "GCode:line.altbackground";
   public static final String GCode_line_background             = "GCode:line.background";
   public static final String GCode_line_font                   = "GCode:line.font";
   public static final String GCode_line_foreground             = "GCode:line.foreground";
   public static final String GCode_line_selected_background    = "GCode:line.selected.background";
   public static final String GCode_line_selected_foreground    = "GCode:line.selected.foreground";
   public static final String GCode_number_font                 = "GCode:number.font";
   public static final String GCode_number_foreground           = "GCode:number.foreground";
   public static final String GCode_row_height                  = "GCode:row.height";
   public static final String GCode_styles                      = "GCode:styles";
   public static final String GCode_basedir                     = "GCode:basedir";
   public static final String Icon_disabled_factor              = "Icon:disabled.factor";
   public static final String Info_grid_color                   = "Info:grid.color";
   public static final String JogButtonPane_background          = "JogButtonPane:background";
   public static final String JogButton_border                  = "JogButton:border";
   public static final String JogButton_disabled_foreground     = "JogButton:disabled.foreground";
   public static final String JogButton_disabled_shadow         = "JogButton:disabled.shadow";
   public static final String JogButton_font                    = "JogButton:font";
   public static final String JogButton_foreground              = "JogButton:foreground";
   public static final String JogButton_gradient_disabled_end   = "JogButton:gradient.disabled.end";
   public static final String JogButton_gradient_disabled_start = "JogButton:gradient.disabled.start";
   public static final String JogButton_gradient_end            = "JogButton:gradient.end";
   public static final String JogButton_gradient_selected_end   = "JogButton:gradient.selected.end";
   public static final String JogButton_gradient_selected_start = "JogButton:gradient.selected.start";
   public static final String JogButton_gradient_start          = "JogButton:gradient.start";
   public static final String JogButton_shadow                  = "JogButton:shadow";
   public static final String Label_background                  = "Label_background";
   public static final String Label_disabledForeground          = "Label_disabledForeground";
   public static final String Label_foreground                  = "Label_foreground";
   public static final String MDI_background                    = "MDI:background";
   public static final String MDI_foreground                    = "MDI:foreground";
   public static final String Main_grid_color                   = "Main:grid.color";
   public static final String MessageLOG_grid_color             = "MessageLOG:grid.color";
   public static final String MessageLOG_header_font            = "MessageLOG:header.font";
   public static final String MessageLOG_message_background     = "MessageLOG:message.background";
   public static final String MessageLOG_message_font           = "MessageLOG:message.font";
   public static final String MessageLOG_message_foreground     = "MessageLOG:message.foreground";
   public static final String MessageLOG_message_row_height     = "MessageLOG:message.row.height";
   public static final String OptionPane_background             = "OptionPane_background";
   public static final String Panel_background                  = "Panel_background";
   public static final String Panel_foreground                  = "Panel_foreground";
   public static final String ProgressBar_background            = "ProgressBar_background";
   public static final String ProgressBar_border                = "ProgressBar_border";
   public static final String ProgressBar_foreground            = "ProgressBar_foreground";
   public static final String RadioButton_background            = "RadioButton_background";
   public static final String RadioButton_foreground            = "RadioButton_foreground";
   public static final String ScrollBar_background              = "ScrollBar_background";
   public static final String ScrollBar_shadow                  = "ScrollBar_shadow";
   public static final String Slider_background                 = "Slider_background";
   public static final String Speed_grid_color                  = "Speed:grid.color";
   public static final String Spinner_background                = "Spinner_background";
   public static final String Spinner_foreground                = "Spinner_foreground";
   public static final String TabbedPane_borderHightlightColor  = "TabbedPane_borderHightlightColor";
   public static final String TabbedPane_darkShadow             = "TabbedPane_darkShadow";
   public static final String TabbedPane_foreground             = "TabbedPane_foreground";
   public static final String TabbedPane_selectHighlight        = "TabbedPane_selectHighlight";
   public static final String TabbedPane_selected               = "TabbedPane_selected";
   public static final String TabbedPane_tabInsets              = "TabbedPane_tabInsets";
   public static final String TabbedPane_unselectedBackground   = "TabbedPane_unselectedBackground";
   public static final String TableHeader_background            = "TableHeader_background";
   public static final String TableHeader_cellBorder            = "TableHeader_cellBorder";
   public static final String TableHeader_focusCellBackground   = "TableHeader_focusCellBackground";
   public static final String TableHeader_foreground            = "TableHeader_foreground";
   public static final String Table_altbackground               = "Table_altbackground";
   public static final String Table_background                  = "Table_background";
   public static final String Table_focusCellBackground         = "Table_focusCellBackground";
   public static final String Table_focusCellForeground         = "Table_focusCellForeground";
   public static final String Table_focusCellHighlightBorder    = "Table_focusCellHighlightBorder";
   public static final String Table_foreground                  = "Table_foreground";
   public static final String Table_gridColor                   = "Table_gridColor";
   public static final String Table_scrollPaneBorder            = "Table_scrollPaneBorder";
   public static final String Table_selectionBackground         = "Table_selectionBackground";
   public static final String Table_selectionForeground         = "Table_selectionForeground";
   public static final String TextField_background              = "TextField_background";
   public static final String TextField_border                  = "TextField_border";
   public static final String TextField_caretForeground         = "TextField_caretForeground";
   public static final String TextField_foreground              = "TextField_foreground";
   public static final String TextField_shadow                  = "TextField_shadow";
   public static final String ToolManager_altbackground         = "ToolManager:altbackground";
   public static final String ToolManager_background            = "ToolManager:background";
   public static final String ToolManager_editor_entry_font     = "ToolManager:editor.entry.font";
   public static final String ToolManager_editor_prompt_font    = "ToolManager:editor.prompt.font";
   public static final String ToolManager_editor_background     = "ToolManager:editor.background";
   public static final String ToolManager_font                  = "ToolManager:font";
   public static final String ToolManager_foreground            = "ToolManager:foreground";
   public static final String ToolManager_grid_color            = "ToolManager:grid.color";
   public static final String ToolManager_header_font           = "ToolManager:header.font";
   public static final String ToolManager_row_height            = "ToolManager:row.height";
   public static final String ToolManager_selected_background   = "ToolManager:selected.background";
   public static final String ToolManager_selected_foreground   = "ToolManager:selected.foreground";
   public static final String ToolTable_background              = "ToolTable:background";
   public static final String ToolTable_foreground              = "ToolTable:foreground";
   public static final String ToolTable_grid_color              = "ToolTable:grid.color";
   public static final String ToolTable_header_font             = "ToolTable:header.font";
   public static final String ToolTable_row_height              = "ToolTable:row.height";
   public static final String ToolTable_selected_background     = "ToolTable:selected.background";
   public static final String ToolTable_selected_foreground     = "ToolTable:selected.foreground";
   public static final String ToolTable_table_font              = "ToolTable:table.font";
   public static final String Tool_desc_background              = "Tool:desc.background";
   public static final String Tool_desc_font                    = "Tool:desc.font";
   public static final String Tool_desc_foreground              = "Tool:desc.foreground";
   public static final String Tool_editor_entry                 = "Tool:editor.entry";
   public static final String Tool_editor_grid_color            = "Tool:editor.grid.color";
   public static final String Tool_editor_prompt                = "Tool:editor.prompt";
   public static final String Tool_info_background              = "Tool:info.background";
   public static final String Tool_info_font                    = "Tool:info.font";
   public static final String Tool_info_foreground              = "Tool:info.foreground";
   public static final String Tool_info_grid_color              = "Tool:info.grid.color";
   public static final String Tool_number_background            = "Tool:number.background";
   public static final String Tool_number_font                  = "Tool:number.font";
   public static final String Tool_number_foreground            = "Tool:number.foreground";
   public static final String Toolbar_grid_color                = "Toolbar:grid.color";
}
