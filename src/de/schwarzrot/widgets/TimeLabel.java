package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       TimeLabel.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    16.9.2019 by Django Reinhard
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

import javax.swing.JLabel;

import de.schwarzrot.bean.ITheme;
import de.schwarzrot.model.IValueClient;


public class TimeLabel extends JLabel implements IValueClient {
   public TimeLabel(ITheme settings) {
      setHorizontalAlignment(JLabel.RIGHT);
      Dimension size = new Dimension(150, 30);
      setPreferredSize(size);
      setMinimumSize(size);
      setValue(0);
   }


   public void setValue(Object value) {
      if (value != null) {
         System.out.println("time is " + value);
         if (value instanceof Number) {
            int iv = ((Number) value).intValue();
            int s  = iv % 60;
            int m  = iv / 60;

            if (m > 0) setText(String.format("%d:%02d", m, s));
            else setText(String.format("%02d", s));
         }
      }
   }

   private static final long serialVersionUID = 1L;
}
