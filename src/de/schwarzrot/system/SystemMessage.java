package de.schwarzrot.system;
/* 
 * **************************************************************************
 * 
 *  file:       SystemMessage.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    7.10.2019 by Django Reinhard
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

import java.util.Date;


public class SystemMessage implements Comparable<SystemMessage> {
   public static enum MessageType {
                                   NMLError(1),
                                   NMLText(2),
                                   NMLDisplay(3),
                                   CommandLog(7),
                                   OperatorError(11),
                                   OperatorText(12),
                                   OperatorDisplay(13);

      private MessageType(int type) {
         this.type = type;
      }


      public int getTypeNum() {
         return type;
      }

      private int type;
   }


   private SystemMessage(MessageType mt, Date when, String message) {
      this.type    = mt;
      this.time    = when;
      this.message = message;
   }


   SystemMessage(MessageType mt, String message) {
      this(mt, new Date(), message);
   }


   SystemMessage(String message) {
      this(MessageType.CommandLog, new Date(), message);
   }


   @Override
   public int compareTo(SystemMessage other) {
      if (other.getTime().getTime() == getTime().getTime()) {
         return other.getType().compareTo(getType());
      } 
      return other.getTime().compareTo(getTime());
   }


   public String getMessage() {
      return message;
   }


   public Date getTime() {
      return time;
   }


   public MessageType getType() {
      return type;
   }


   public void setMessage(String message) {
      this.message = message;
   }


   public void setTime(Date time) {
      this.time = time;
   }


   public void setType(MessageType type) {
      this.type = type;
   }


   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append(type.getTypeNum());
      sb.append(" ");
      sb.append(time.getTime());
      sb.append(" ");
      sb.append(message);

      return sb.toString();
   }


   public static SystemMessage parseMessage(String source) {
      String[] parts = source.split("\\s+", 3);

      if (parts.length < 3) return null;
      int         typeNum = Integer.parseInt(parts[0]);
      Date        when    = new Date(Long.parseLong(parts[1]));
      MessageType mt      = null;

      switch (typeNum) {
      case 1:
         mt = MessageType.NMLError;
         break;
      case 2:
         mt = MessageType.NMLText;
         break;
      case 3:
         mt = MessageType.NMLDisplay;
         break;
      case 7:
         mt = MessageType.CommandLog;
         break;
      case 11:
         mt = MessageType.OperatorError;
         break;
      case 12:
         mt = MessageType.OperatorText;
         break;
      case 13:
         mt = MessageType.OperatorDisplay;
         break;
      }
      return new SystemMessage(mt, when, parts[2]);
   }

   private MessageType type;
   private String      message;
   private Date        time;
}
