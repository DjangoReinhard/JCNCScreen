package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       GCodeStyles.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    12.4.2020 by Django Reinhard
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import de.schwarzrot.text.GCodeSource;


public class GCodeStyles implements IStyles {
   public GCodeStyles() {
      StyleContext              ctx  = StyleContext.getDefaultStyleContext();
      Map<String, AttributeSet> map  = SynStyles = new HashMap<String, AttributeSet>();
      SimpleAttributeSet        attr = new SimpleAttributeSet(
            ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));

      attr.addAttribute(StyleConstants.Foreground, Color.YELLOW);
      map.put("default", attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 18));
      attr.addAttribute(StyleConstants.Foreground, Color.lightGray);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Number, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.RED);
      attr.addAttribute(StyleConstants.Bold, true);
      map.put(GCodeSource.GCode, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, DarkRed);
      attr.addAttribute(StyleConstants.Bold, true);
      map.put(GCodeSource.Motion, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.MAGENTA);
      attr.addAttribute(StyleConstants.Bold, true);
      map.put(GCodeSource.MCode, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.BLUE);
      map.put(GCodeSource.Pos, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.BLACK);
      map.put(GCodeSource.XPos, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.PINK);
      attr.addAttribute(StyleConstants.Bold, true);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Tool, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.PINK);
      attr.addAttribute(StyleConstants.Bold, true);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Feed, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, Color.PINK);
      attr.addAttribute(StyleConstants.Bold, true);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Speed, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 20));
      attr.addAttribute(StyleConstants.Foreground, DarkCyan);
      attr.addAttribute(StyleConstants.Bold, true);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Var, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 18));
      attr.addAttribute(StyleConstants.Foreground, DarkGreen);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.Comment, attr);

      attr = new SimpleAttributeSet(ctx.addAttribute(ctx.getEmptySet(), StyleConstants.FontSize, 18));
      attr.addAttribute(StyleConstants.Foreground, DarkGreen);
      attr.addAttribute(StyleConstants.Italic, true);
      map.put(GCodeSource.LineComment, attr);
   }


   @Override
   public AttributeSet getStyle(String key) {
      return SynStyles.get(key);
   }


   private Map<String, AttributeSet> SynStyles;
   private static final Color        DarkGreen = new Color(0, 120, 0);
   private static final Color        DarkRed   = new Color(180, 0, 0);
   private static final Color        DarkCyan  = new Color(0, 180, 180);
}
