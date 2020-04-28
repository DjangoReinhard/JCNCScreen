package de.schwarzrot.bean;
/* 
 * **************************************************************************
 * 
 *  file:       Fixtures.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    3.10.2019 by Django Reinhard
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

import de.schwarzrot.model.CanonPosition;


public class Fixtures {
   public Fixtures() {
      commonOffset = new CanonPosition();
      g54Offset    = new CanonPosition();
      g55Offset    = new CanonPosition();
      g56Offset    = new CanonPosition();
      g57Offset    = new CanonPosition();
      g58Offset    = new CanonPosition();
      g59Offset    = new CanonPosition();
      g591Offset   = new CanonPosition();
      g592Offset   = new CanonPosition();
      g593Offset   = new CanonPosition();
   }


   public CanonPosition getCommonOffset() {
      return commonOffset;
   }


   public CanonPosition getG54Offset() {
      return g54Offset;
   }


   public CanonPosition getG55Offset() {
      return g55Offset;
   }


   public CanonPosition getG56Offset() {
      return g56Offset;
   }


   public CanonPosition getG57Offset() {
      return g57Offset;
   }


   public CanonPosition getG58Offset() {
      return g58Offset;
   }


   public CanonPosition getG591Offset() {
      return g591Offset;
   }


   public CanonPosition getG592Offset() {
      return g592Offset;
   }


   public CanonPosition getG593Offset() {
      return g592Offset;
   }


   public CanonPosition getG59Offset() {
      return g59Offset;
   }

   private CanonPosition commonOffset;
   private CanonPosition g54Offset;
   private CanonPosition g55Offset;
   private CanonPosition g56Offset;
   private CanonPosition g57Offset;
   private CanonPosition g58Offset;
   private CanonPosition g59Offset;
   private CanonPosition g591Offset;
   private CanonPosition g592Offset;
   private CanonPosition g593Offset;
}
