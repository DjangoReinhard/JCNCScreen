package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       Preview3DPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    24.5.2020 by Django Reinhard
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


import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeCanvasContext;


public class Preview3DPane extends JPanel {
   public Preview3DPane(SimpleApplication app) {
      Preview3DPane.app = app;

      app.createCanvas();
      app.startCanvas();

      context = (JmeCanvasContext) app.getContext();
      canvas  = context.getCanvas();
      setLayout(new BorderLayout());
      add(canvas, BorderLayout.CENTER);
   }


   private static SimpleApplication app;
   private static JmeCanvasContext  context;
   private static Canvas            canvas;
}
