package de.schwarzrot.nml;
/* 
 * **************************************************************************
 * 
 *  file:       InterpState.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    19.10.2019 by Django Reinhard
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

/*
 * keep in sync with linuxcnc-dev/src/emc/nml_intf/emc.hh
 */
public enum InterpState {
                         Idle(1),
                         Reading(2),
                         Paused(3),
                         Waiting(4);

   private InterpState(int state) {
      this.state = state;
   }


   public int getStateNum() {
      return state;
   }

   private final int state;
}
