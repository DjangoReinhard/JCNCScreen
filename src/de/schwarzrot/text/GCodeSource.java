package de.schwarzrot.text;
/* 
 * **************************************************************************
 * 
 *  file:       GCodeSource.java
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


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import de.schwarzrot.bean.IStyles;


public class GCodeSource extends DefaultStyledDocument {
   public GCodeSource(IStyles s) {
      this(s, null);
   }


   public GCodeSource(IStyles s, AbstractDocument ad) {
      super();
      importStyles(s);
      if (ad != null) {
         try {
            insertString(0, ad.getText(0, ad.getLength()), SynStyles.get("default"));
         } catch (BadLocationException e) {
            e.printStackTrace();
         }
         styleContent();
      }
   }


   @Override
   public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
      super.insertString(offset, str, a);
      styleContent();
   }


   @Override
   public void remove(int offs, int len) throws BadLocationException {
      super.remove(offs, len);
      styleContent();
   }


   @Override
   public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
      super.replace(offset, length, text, attrs);
      styleContent();
   }


   protected void applyStyle(int from, int to, AttributeSet attr) {
      for (int pos = from; pos < to; ++pos) {
         setCharacterAttributes(pos, 1, attr, true);
      }
   }


   protected synchronized void styleContent() {
      String text = null;

      try {
         int l = getLength();

         if (l > 0) {
            text = getText(0, getLength());

            for (String k : PatternOrder) {
               Matcher m     = GCodePattern.get(k).matcher(text);
               int     start = 0;

               while (m.find(start)) {
                  applyStyle(m.start(), m.end(), SynStyles.get(k));
                  start = m.end();
               }
            }
         }
      } catch (BadLocationException e) {
         e.printStackTrace();
      }
   }


   private void importStyles(IStyles s) {
      for (String k : PatternOrder) {
         AttributeSet a = s.getStyle(k);

         SynStyles.put(k, a);
      }
   }


   public static final String                Comment          = "Comment";
   public static final String                LineComment      = "LineComment";
   public static final String                GCode            = "GCode";
   public static final String                MCode            = "MCode";
   public static final String                Motion           = "Motion";
   public static final String                Tool             = "Tool";
   public static final String                Feed             = "Feed";
   public static final String                Speed            = "Speed";
   public static final String                Number           = "Number";
   public static final String                Pos              = "Pos";
   public static final String                XPos             = "Pos+";
   public static final String                Var              = "Var";
   private static Map<String, AttributeSet>  SynStyles;
   private static final Map<String, Pattern> GCodePattern;
   private static final String[]             PatternOrder     = { Motion, GCode, MCode, Tool, Feed, Speed,
         Number, Pos, XPos, Var, Comment, LineComment };
   private static final long                 serialVersionUID = 1L;
   static {
      GCodePattern = new HashMap<String, Pattern>();
      SynStyles    = new HashMap<String, AttributeSet>();

      GCodePattern.put(Number, Pattern.compile("N\\d+", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(GCode, Pattern.compile("G\\d\\d+\\.?\\d*", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(MCode, Pattern.compile("M\\d+\\.?\\d*", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Motion, Pattern.compile("G0|G1|G2|G3", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Pos,
            Pattern.compile("[ABCUVWXYZ]([+-]?\\d+[,.]?\\d*|\\[.+?\\])", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(XPos,
            Pattern.compile("[EIJKLQR]([+-]?\\d+[,.]?\\d*|\\[.+?\\])", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Tool, Pattern.compile("[THD]\\d*[,.]?\\d*", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Feed, Pattern.compile("F\\d*[,.]?\\d*", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Speed, Pattern.compile("S\\d*[,.]?\\d*", Pattern.CASE_INSENSITIVE));
      GCodePattern.put(Var, Pattern.compile("#\\<.+?\\>"));
      GCodePattern.put(LineComment, Pattern.compile(";.*$"));
      GCodePattern.put(Comment, Pattern.compile("\\(.+?\\)"));
   };
}
