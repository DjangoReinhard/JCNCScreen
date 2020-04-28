package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       HomeAllButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    28.9.2019 by Django Reinhard
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


import de.schwarzrot.logic.ICondition;


public class HomeAllButton extends SoftkeyButton {
   public HomeAllButton(String iconPath, String selectedIconPath, ICondition cond2Enable,
         ICondition cond2Select) {
      super(iconPath, selectedIconPath, cond2Enable, cond2Select);
   }


   private static final long serialVersionUID = 1L;

   // @Override
   // public void propertyChange(PropertyChangeEvent e) {
   // if (e.getPropertyName().compareTo("taskState") == 0) {
   // machineIsOn = e.getNewValue().equals(TaskState.MachineOn);
   // }
   // if (e.getPropertyName().compareTo("state") == 0) {
   // allJointsHomed = (Boolean) e.getNewValue();
   // }
   // checkState();
   // }
   //
   //
   // protected void checkState() {
   // if (machineIsOn) {
   // if (!allJointsHomed) setEnabled(true);
   // else setEnabled(false);
   // } else setEnabled(false);
   // }
   //
   // private boolean allJointsHomed;
   // private boolean machineIsOn;
}
