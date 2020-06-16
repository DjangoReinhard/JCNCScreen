package de.schwarzrot.bean.themes;
/*
 * **************************************************************************
 *
 *  file:       AbstractTheme.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    25.4.2020 by Django Reinhard
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
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Map;

import de.schwarzrot.bean.ITheme;


public abstract class AbstractTheme implements ITheme {
   protected AbstractTheme(Map<String, Object> settings) {
      this.settings = settings;
      plain18       = new Font(ITheme.DefaultFont, Font.PLAIN, 18);
      italic18      = new Font(ITheme.DefaultFont, Font.ITALIC, 18);
      italic35      = new Font(ITheme.DefaultFont, Font.ITALIC, 35);
      mono20        = new Font(ITheme.MonoFont, Font.PLAIN, 20);
      plain20       = new Font(ITheme.DefaultFont, Font.PLAIN, 20);
      plain22       = new Font(ITheme.DefaultFont, Font.PLAIN, 22);
      plain30       = new Font(ITheme.DefaultFont, Font.PLAIN, 30);
      bold20        = new Font(ITheme.DefaultFont, Font.BOLD, 20);
      bold22        = new Font(ITheme.DefaultFont, Font.BOLD, 22);
      bold24        = new Font(ITheme.DefaultFont, Font.BOLD, 24);
      bold40        = new Font(ITheme.DefaultFont, Font.BOLD, 40);
      boldItalic40  = new Font(ITheme.DefaultFont, Font.BOLD | Font.ITALIC, 40);

      settings.put(LED_on_color, Color.GREEN);
      settings.put(LED_off_color, Color.RED);

      settings.put(Toolbar_button_size, new Dimension(100, 100));
      NumberFormat positionFormat = NumberFormat.getInstance();

      positionFormat.setGroupingUsed(true);
      positionFormat.setMaximumFractionDigits(3);
      positionFormat.setMinimumFractionDigits(3);
      settings.put(DRO_pos_format, positionFormat);

      NumberFormat speedFormat = NumberFormat.getInstance();

      speedFormat.setGroupingUsed(true);
      speedFormat.setMaximumFractionDigits(0);
      settings.put(DRO_speed_format, speedFormat);
   }


   @Override
   public void export(PrintWriter pw) {
      //TODO
   }


   public final Map<String, Object> getSettings() {
      return settings;
   }


   protected Font                plain18;
   protected Font                italic18;
   protected Font                italic35;
   protected Font                mono20;
   protected Font                plain20;
   protected Font                plain22;
   protected Font                plain30;
   protected Font                bold20;
   protected Font                bold22;
   protected Font                bold24;
   protected Font                bold40;
   protected Font                boldItalic40;
   protected Map<String, Object> settings;
}
