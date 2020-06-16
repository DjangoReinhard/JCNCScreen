package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       UITheme.java
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import de.schwarzrot.bean.IStyles;
import de.schwarzrot.bean.ITheme;


public class UITheme {
   public static void exportTheme(File f) {
      UIDefaults  ud = UIManager.getDefaults();
      PrintWriter pw = null;

      try {
         pw = new PrintWriter(f);

         for (Map.Entry e : ud.entrySet()) {
            e.getKey();
            StringBuilder sb = new StringBuilder("UIM ");

            sb.append(e.getKey());
            sb.append(" = ");
            sb.append(e.getValue());
            pw.println(sb.toString());
         }
         t.export(pw);
         pw.flush();
         pw.close();
      } catch (FileNotFoundException e1) {
         e1.printStackTrace();
      }

   }


   public static Boolean getBoolean(String key) {
      Object v = getValue(key);

      return (v instanceof Boolean) ? (Boolean) v : null;
   }


   public static Color getColor(String key) {
      Object v = getValue(key);

      return (v instanceof Color) ? (Color) v : null;
   }


   public static Dimension getDimension(String key) {
      Object v = getValue(key);

      return (v instanceof Dimension) ? (Dimension) v : null;
   }


   public static File getFile(String key) {
      Object v = getValue(key);

      return (v instanceof File) ? (File) v : null;
   }


   public static Font getFont(String key) {
      Object v = getValue(key);

      return (v instanceof Font) ? (Font) v : null;
   }


   public static NumberFormat getFormat(String key) {
      Object v = getValue(key);

      return (v instanceof NumberFormat) ? (NumberFormat) v : null;
   }


   public static int getInt(String key) {
      Object v = getValue(key);

      return (v instanceof Integer) ? (int) v : null;
   }


   public static IStyles getStyles(String key) {
      Object v = getValue(key);

      return (v instanceof IStyles) ? (IStyles) v : null;
   }


   public static String getThemeName() {
      return themeName;
   }


   public static void put(String key, Object o) {
      settings.put(key, o);
   }


   public static void setupDefaults(ITheme theme) {
      if (theme != null) {
         importTheme(((AbstractTheme) theme).getSettings());
      }
   }


   public static void setupDefaults(String theme) {
      if (themeName != null)
         return;
      //      determineAvailableFonts();
      settings.put(GCode_basedir, new File("/usr/local/src/linuxcnc-dev", "nc_files"));
      if (theme != null) {
         if ("dark".compareTo(theme) == 0) {
            themeName = "dark";
            t         = new ThemeDark(settings);
         } else if ("rma".compareToIgnoreCase(theme) == 0) {
            themeName = "RMA";
            t         = new ThemeRMA(settings);
         } else {
            themeName = "Gtk";
            t         = new ThemeGtk(settings);
         }
      } else {
         themeName = "RMA";
         t         = new ThemeRMA(settings);
      }
   }


   protected static void determineAvailableFonts() {
      GraphicsEnvironment gEnv  = GraphicsEnvironment.getLocalGraphicsEnvironment();
      JLabel              dummy = new JLabel(" ");
      FontMetrics         fm;
      int                 w0, w1;

      allFontsList  = new DefaultListModel<String>();
      monoFontsList = new DefaultListModel<String>();
      for (Font f : gEnv.getAllFonts()) {
         Font fnt = f.deriveFont(10);

         if (!allFontsList.contains(fnt.getFamily())) {
            allFontsList.addElement(fnt.getFamily());
         }
         fm = dummy.getFontMetrics(fnt);
         w0 = SwingUtilities.computeStringWidth(fm, "ii");
         w1 = SwingUtilities.computeStringWidth(fm, "WW");

         if (w0 == w1) {
            if (!monoFontsList.contains(fnt.getFamily())) {
               monoFontsList.addElement(fnt.getFamily());
            }
         }
      }
   }


   protected static void dumpMap(Map<String, Object> m) {
      for (String k : m.keySet()) {
         System.err.println(k + "\t=>\t" + m.get(k));
      }
   }


   protected static Object getValue(String key) {
      return settings.get(key);
   }


   protected static void importTheme(Map<String, Object> m) {
      for (String k : m.keySet()) {
         settings.put(k, m.get(k));
      }
   }


   private static Map<String, Object>      settings;
   private static ITheme                   t;
   private static String                   themeName;
   private static DefaultListModel<String> allFontsList;
   private static DefaultListModel<String> monoFontsList;
   public static final String              LED_on_color                      = ITheme.LED_on_color;
   public static final String              LED_off_color                     = ITheme.LED_off_color;
   public static final String              Toolbar_button_size               = ITheme.Toolbar_button_size;
   public static final String              DRO_pos_format                    = ITheme.DRO_pos_format;
   public static final String              DRO_speed_format                  = ITheme.DRO_speed_format;
   public static final String              Application_mode_portrait         = ITheme.Application_mode_portrait;
   public static final String              ActiveCode_background             = ITheme.ActiveCode_background;
   public static final String              ActiveCode_font                   = ITheme.ActiveCode_font;
   public static final String              ActiveCode_foreground             = ITheme.ActiveCode_foreground;
   public static final String              ActiveCode_grid_color             = ITheme.ActiveCode_grid_color;
   public static final String              AppSettings_background            = ITheme.AppSettings_background;
   public static final String              AppSettings_font                  = ITheme.AppSettings_font;
   public static final String              AppSettings_foreground            = ITheme.AppSettings_foreground;
   public static final String              AppSettings_tab_font              = ITheme.AppSettings_tab_font;
   public static final String              Axis_background                   = ITheme.Axis_background;
   public static final String              Axis_font                         = ITheme.Axis_font;
   public static final String              Axis_foreground                   = ITheme.Axis_foreground;
   public static final String              Button_focus                      = ITheme.Button_focus;
   public static final String              Button_foreground                 = ITheme.Button_foreground;
   public static final String              Button_select                     = ITheme.Button_select;
   public static final String              DRO_abs_background                = ITheme.DRO_abs_background;
   public static final String              DRO_abs_font                      = ITheme.DRO_abs_font;
   public static final String              DRO_abs_foreground                = ITheme.DRO_abs_foreground;
   public static final String              DRO_dtg_background                = ITheme.DRO_dtg_background;
   public static final String              DRO_dtg_font                      = ITheme.DRO_dtg_font;
   public static final String              DRO_dtg_foreground                = ITheme.DRO_dtg_foreground;
   public static final String              DRO_grid_color                    = ITheme.DRO_grid_color;
   public static final String              DRO_rel_background                = ITheme.DRO_rel_background;
   public static final String              DRO_rel_font                      = ITheme.DRO_rel_font;
   public static final String              DRO_rel_foreground                = ITheme.DRO_rel_foreground;
   public static final String              DRO_speed_background              = ITheme.DRO_speed_background;
   public static final String              DRO_speed_font                    = ITheme.DRO_speed_font;
   public static final String              DRO_speed_foreground              = ITheme.DRO_speed_foreground;
   public static final String              DRO_speed_header_background       = ITheme.DRO_speed_header_background;
   public static final String              DRO_speed_header_font             = ITheme.DRO_speed_header_font;
   public static final String              DRO_speed_header_foreground       = ITheme.DRO_speed_header_foreground;
   public static final String              FileManager_altbackground         = ITheme.FileManager_altbackground;
   public static final String              FileManager_background            = ITheme.FileManager_background;
   public static final String              FileManager_font                  = ITheme.FileManager_font;
   public static final String              FileManager_foreground            = ITheme.FileManager_foreground;
   public static final String              FileManager_grid_color            = ITheme.FileManager_grid_color;
   public static final String              FileManager_header_font           = ITheme.FileManager_header_font;
   public static final String              FileManager_row_height            = ITheme.FileManager_row_height;
   public static final String              FileManager_selected_background   = ITheme.FileManager_selected_background;
   public static final String              FileManager_selected_foreground   = ITheme.FileManager_selected_foreground;
   public static final String              Fixture_background                = ITheme.Fixture_background;
   public static final String              Fixture_entry_font                = ITheme.Fixture_entry_font;
   public static final String              Fixture_foreground                = ITheme.Fixture_foreground;
   public static final String              Fixture_grid_color                = ITheme.Fixture_grid_color;
   public static final String              Fixture_header_border             = ITheme.Fixture_header_border;
   public static final String              Fixture_header_font               = ITheme.Fixture_header_font;
   public static final String              Fixture_header_foreground         = ITheme.Fixture_header_foreground;
   public static final String              Fixture_prompt_font               = ITheme.Fixture_prompt_font;
   public static final String              GCode_edit_background             = ITheme.GCode_edit_background;
   public static final String              GCode_edit_font                   = ITheme.GCode_edit_font;
   public static final String              GCode_edit_foreground             = ITheme.GCode_edit_foreground;
   public static final String              GCode_filename_background         = ITheme.GCode_filename_background;
   public static final String              GCode_filename_changed_foreground = ITheme.GCode_filename_changed_foreground;
   public static final String              GCode_filename_font               = ITheme.GCode_filename_font;
   public static final String              GCode_filename_foreground         = ITheme.GCode_filename_foreground;
   public static final String              GCode_grid_color                  = ITheme.GCode_grid_color;
   public static final String              GCode_line_altbackground          = ITheme.GCode_line_altbackground;
   public static final String              GCode_line_background             = ITheme.GCode_line_background;
   public static final String              GCode_line_font                   = ITheme.GCode_line_font;
   public static final String              GCode_line_foreground             = ITheme.GCode_line_foreground;
   public static final String              GCode_line_selected_background    = ITheme.GCode_line_selected_background;
   public static final String              GCode_line_selected_foreground    = ITheme.GCode_line_selected_foreground;
   public static final String              GCode_number_font                 = ITheme.GCode_number_font;
   public static final String              GCode_number_foreground           = ITheme.GCode_number_foreground;
   public static final String              GCode_row_height                  = ITheme.GCode_row_height;
   public static final String              GCode_styles                      = ITheme.GCode_styles;
   public static final String              GCode_basedir                     = ITheme.GCode_basedir;
   public static final String              Icon_disabled_factor              = ITheme.Icon_disabled_factor;
   public static final String              Info_grid_color                   = ITheme.Info_grid_color;
   public static final String              JogButtonPane_background          = ITheme.JogButtonPane_background;
   public static final String              JogButton_border                  = ITheme.JogButton_border;
   public static final String              JogButton_disabled_foreground     = ITheme.JogButton_disabled_foreground;
   public static final String              JogButton_disabled_shadow         = ITheme.JogButton_disabled_shadow;
   public static final String              JogButton_font                    = ITheme.JogButton_font;
   public static final String              JogButton_foreground              = ITheme.JogButton_foreground;
   public static final String              JogButton_gradient_disabled_end   = ITheme.JogButton_gradient_disabled_end;
   public static final String              JogButton_gradient_disabled_start = ITheme.JogButton_gradient_disabled_start;
   public static final String              JogButton_gradient_end            = ITheme.JogButton_gradient_end;
   public static final String              JogButton_gradient_selected_end   = ITheme.JogButton_gradient_selected_end;
   public static final String              JogButton_gradient_selected_start = ITheme.JogButton_gradient_selected_start;
   public static final String              JogButton_gradient_start          = ITheme.JogButton_gradient_start;
   public static final String              JogButton_shadow                  = ITheme.JogButton_shadow;
   public static final String              Label_background                  = ITheme.Label_background;
   public static final String              Label_disabledForeground          = ITheme.Label_disabledForeground;
   public static final String              Label_foreground                  = ITheme.Label_foreground;
   public static final String              MDI_background                    = ITheme.MDI_background;
   public static final String              MDI_foreground                    = ITheme.MDI_foreground;
   public static final String              Main_grid_color                   = ITheme.Main_grid_color;
   public static final String              MessageLOG_grid_color             = ITheme.MessageLOG_grid_color;
   public static final String              MessageLOG_header_font            = ITheme.MessageLOG_header_font;
   public static final String              MessageLOG_message_background     = ITheme.MessageLOG_message_background;
   public static final String              MessageLOG_message_font           = ITheme.MessageLOG_message_font;
   public static final String              MessageLOG_message_foreground     = ITheme.MessageLOG_message_foreground;
   public static final String              MessageLOG_message_row_height     = ITheme.MessageLOG_message_row_height;
   public static final String              OptionPane_background             = ITheme.OptionPane_background;
   public static final String              Panel_background                  = ITheme.Panel_background;
   public static final String              Panel_foreground                  = ITheme.Panel_foreground;
   public static final String              ProgressBar_background            = ITheme.ProgressBar_background;
   public static final String              ProgressBar_border                = ITheme.ProgressBar_border;
   public static final String              ProgressBar_foreground            = ITheme.ProgressBar_foreground;
   public static final String              RadioButton_background            = ITheme.RadioButton_background;
   public static final String              RadioButton_foreground            = ITheme.RadioButton_foreground;
   public static final String              ScrollBar_background              = ITheme.ScrollBar_background;
   public static final String              ScrollBar_shadow                  = ITheme.ScrollBar_shadow;
   public static final String              Slider_background                 = ITheme.Slider_background;
   public static final String              Speed_grid_color                  = ITheme.Speed_grid_color;
   public static final String              Spinner_background                = ITheme.Spinner_background;
   public static final String              Spinner_foreground                = ITheme.Spinner_foreground;
   public static final String              TabbedPane_borderHightlightColor  = ITheme.TabbedPane_borderHightlightColor;
   public static final String              TabbedPane_darkShadow             = ITheme.TabbedPane_darkShadow;
   public static final String              TabbedPane_foreground             = ITheme.TabbedPane_foreground;
   public static final String              TabbedPane_selectHighlight        = ITheme.TabbedPane_selectHighlight;
   public static final String              TabbedPane_selected               = ITheme.TabbedPane_selected;
   public static final String              TabbedPane_tabInsets              = ITheme.TabbedPane_tabInsets;
   public static final String              TabbedPane_unselectedBackground   = ITheme.TabbedPane_unselectedBackground;
   public static final String              TableHeader_background            = ITheme.TableHeader_background;
   public static final String              TableHeader_cellBorder            = ITheme.TableHeader_cellBorder;
   public static final String              TableHeader_focusCellBackground   = ITheme.TableHeader_focusCellBackground;
   public static final String              TableHeader_foreground            = ITheme.TableHeader_foreground;
   public static final String              Table_altbackground               = ITheme.Table_altbackground;
   public static final String              Table_background                  = ITheme.Table_background;
   public static final String              Table_focusCellBackground         = ITheme.Table_focusCellBackground;
   public static final String              Table_focusCellForeground         = ITheme.Table_focusCellForeground;
   public static final String              Table_focusCellHighlightBorder    = ITheme.Table_focusCellHighlightBorder;
   public static final String              Table_foreground                  = ITheme.Table_foreground;
   public static final String              Table_gridColor                   = ITheme.Table_gridColor;
   public static final String              Table_scrollPaneBorder            = ITheme.Table_scrollPaneBorder;
   public static final String              Table_selectionBackground         = ITheme.Table_selectionBackground;
   public static final String              Table_selectionForeground         = ITheme.Table_selectionForeground;
   public static final String              TextField_background              = ITheme.TextField_background;
   public static final String              TextField_border                  = ITheme.TextField_border;
   public static final String              TextField_caretForeground         = ITheme.TextField_caretForeground;
   public static final String              TextField_foreground              = ITheme.TextField_foreground;
   public static final String              TextField_shadow                  = ITheme.TextField_shadow;
   public static final String              ToolManager_altbackground         = ITheme.ToolManager_altbackground;
   public static final String              ToolManager_background            = ITheme.ToolManager_background;
   public static final String              ToolManager_editor_entry_font     = ITheme.ToolManager_editor_entry_font;
   public static final String              ToolManager_editor_prompt_font    = ITheme.ToolManager_editor_prompt_font;
   public static final String              ToolManager_editor_background     = ITheme.ToolManager_editor_background;
   public static final String              ToolManager_font                  = ITheme.ToolManager_font;
   public static final String              ToolManager_foreground            = ITheme.ToolManager_foreground;
   public static final String              ToolManager_grid_color            = ITheme.ToolManager_grid_color;
   public static final String              ToolManager_header_font           = ITheme.ToolManager_header_font;
   public static final String              ToolManager_row_height            = ITheme.ToolManager_row_height;
   public static final String              ToolManager_selected_background   = ITheme.ToolManager_selected_background;
   public static final String              ToolManager_selected_foreground   = ITheme.ToolManager_selected_foreground;
   public static final String              ToolTable_background              = ITheme.ToolTable_background;
   public static final String              ToolTable_foreground              = ITheme.ToolTable_foreground;
   public static final String              ToolTable_grid_color              = ITheme.ToolTable_grid_color;
   public static final String              ToolTable_header_font             = ITheme.ToolTable_header_font;
   public static final String              ToolTable_row_height              = ITheme.ToolTable_row_height;
   public static final String              ToolTable_selected_background     = ITheme.ToolTable_selected_background;
   public static final String              ToolTable_selected_foreground     = ITheme.ToolTable_selected_foreground;
   public static final String              ToolTable_table_font              = ITheme.ToolTable_table_font;
   public static final String              Tool_desc_background              = ITheme.Tool_desc_background;
   public static final String              Tool_desc_font                    = ITheme.Tool_desc_font;
   public static final String              Tool_desc_foreground              = ITheme.Tool_desc_foreground;
   public static final String              Tool_editor_entry                 = ITheme.Tool_editor_entry;
   public static final String              Tool_editor_grid_color            = ITheme.Tool_editor_grid_color;
   public static final String              Tool_editor_prompt                = ITheme.Tool_editor_prompt;
   public static final String              Tool_info_background              = ITheme.Tool_info_background;
   public static final String              Tool_info_font                    = ITheme.Tool_info_font;
   public static final String              Tool_info_foreground              = ITheme.Tool_info_foreground;
   public static final String              Tool_info_grid_color              = ITheme.Tool_info_grid_color;
   public static final String              Tool_number_background            = ITheme.Tool_number_background;
   public static final String              Tool_number_font                  = ITheme.Tool_number_font;
   public static final String              Tool_number_foreground            = ITheme.Tool_number_foreground;
   public static final String              Toolbar_grid_color                = ITheme.Toolbar_grid_color;
   static {
      settings = new HashMap<String, Object>();
   }
}
