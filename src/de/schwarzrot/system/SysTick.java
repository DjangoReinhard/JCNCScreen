package de.schwarzrot.system;
/* 
 * **************************************************************************
 * 
 *  file:       SysTick.java
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

import java.util.Timer;
import java.util.TimerTask;

public class SysTick {
   public SysTick(TimerTask task) {
      this(task, 40);
   }
   
   public SysTick(TimerTask task, long refreshRate) {
      this.period = refreshRate;
      Timer t = new Timer();
      
      t.scheduleAtFixedRate(task, 0, period);
   }

   private final long period; // milliseconds
}
