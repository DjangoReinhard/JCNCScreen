package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       DRO.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    7.9.2019 by Django Reinhard
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
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.IValueClient;


public class DRO extends JLabel implements IValueClient {
   public DRO() {
      super();
      this.setHorizontalAlignment(JLabel.RIGHT);
      this.setOpaque(true);
      format = UITheme.getFormat(UITheme.DRO_pos_format);
   }


   public Dimension calcMinSize() {
      return calcMinSize(-9999.999);
   }


   public Dimension calcMinSize(double value) {
      FontMetrics fm = this.getFontMetrics(this.getFont());
      int         w  = SwingUtilities.computeStringWidth(fm, format.format(value));

      return new Dimension(w, fm.getHeight());
   }


   public void setFormat(NumberFormat format) {
      this.format = format;
   }


   @Override
   public void setValue(Object value) {
      if (value != null && value instanceof Double) {

         setText(format.format(value));
      }
   }


   private NumberFormat      format;
   private static final long serialVersionUID = 1L;
}
