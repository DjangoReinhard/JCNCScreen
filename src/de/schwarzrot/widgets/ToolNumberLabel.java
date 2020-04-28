package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       ToolNumberLabel.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    29.9.2019 by Django Reinhard
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


import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.IValueClient;


public class ToolNumberLabel extends JLabel implements IValueClient {
   public ToolNumberLabel() {
      super();
      setOpaque(true);
      setValue(0);
      setFont(UITheme.getFont("Tool:number.font"));
      setForeground(UITheme.getColor("Tool:number.foreground"));
      setBackground(UITheme.getColor("Tool:number.background"));
      Dimension s = calcMinSize();

      setMinimumSize(s);
      setPreferredSize(s);
   }


   public Dimension calcMinSize() {
      FontMetrics fm = this.getFontMetrics(this.getFont());
      int         w  = SwingUtilities.computeStringWidth(fm, "T99");

      return new Dimension(w, fm.getHeight());
   }


   @Override
   public void setValue(Object value) {
      if (value != null && value instanceof Number) {
         setText(String.format("T%d", ((Number) value).intValue()));
      }
   }


   private static final long serialVersionUID = 1L;
}
