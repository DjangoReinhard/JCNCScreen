package de.schwarzrot.util;
/* 
 * **************************************************************************
 * 
 *  file:       VectorMath.java
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


import com.jme3.math.Vector3f;


public class VectorMath {
   public static float angleBetween(Vector3f a, Vector3f b) {
      float v = (a.x * b.x + a.y * b.y + a.z * b.z) / ((float) Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z)
            * (float) Math.sqrt(b.x * b.x + b.y * b.y + b.z * b.z));

      return (float) Math.acos(v);
   }
}
