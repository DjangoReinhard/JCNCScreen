package de.schwarzrot.widgets.jme3;
/* 
 * **************************************************************************
 * 
 *  file:       ICreator3D.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    22.5.2020 by Django Reinhard
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;

import com.jme3.math.Vector3f;


public interface ICreator3D {
   public void createArc(Vector3f from, Vector3f to, Vector3f center, int lineNum, boolean clockwise);


   public void createLine(Vector3f from, Vector3f to, int lineNum, boolean fastMove);


   public void processGeomFile(File f) throws FileNotFoundException;


   public void processGeomFile(Reader r);


   public void wait4Material();
}
