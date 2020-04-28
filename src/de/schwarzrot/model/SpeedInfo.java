package de.schwarzrot.model;
/* 
 * **************************************************************************
 * 
 *  file:       SpeedInfo.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    22.9.2019 by Django Reinhard
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

import java.beans.PropertyChangeEvent;


public class SpeedInfo extends AbstractModel {
   public double getAccel() {
      return accel;
   }


   public double getCurFeed() {
      return curFeed;
   }


   public double getFeedFactor() {
      return feedFactor;
   }


   public double getMaxAccel() {
      return maxAccel;
   }


   public double getMaxSpeed() {
      return maxSpeed;
   }


   public double getNominalFeed() {
      return nomFeed;
   }


   public double getRapidFactor() {
      return rapidFactor;
   }


   public double getSpindleCurSpeed() {
      return spindleCurSpeed;
   }


   public double getSpindleFactor() {
      return spindleFactor;
   }


   public double getSpindleNominalSpeed() {
      return spindleNomSpeed;
   }


   public void setAccel(double accel) {
      double ov = this.accel;
      this.accel = accel;
      if (ov != this.accel) firePropertyChange(new PropertyChangeEvent(this,
                                                                       "accel",
                                                                       ov,
                                                                       accel));
   }


   public void setCurFeed(double feed) {
      double ov = this.curFeed;
      this.curFeed = feed;
      if (ov != this.curFeed) firePropertyChange(new PropertyChangeEvent(this,
                                                                         "curFeed",
                                                                         ov,
                                                                         feed));
   }


   public void setFeedFactor(double feedFactor) {
      double ov = this.feedFactor;
      this.feedFactor = feedFactor;
      if (ov != this.feedFactor) firePropertyChange(new PropertyChangeEvent(this,
                                                                            "feedFactor",
                                                                            ov,
                                                                            feedFactor));
   }


   public void setMaxAccel(double maxAccel) {
      double ov = this.maxAccel;
      this.maxAccel = maxAccel;
      if (ov != this.maxAccel) firePropertyChange(new PropertyChangeEvent(this,
                                                                          "maxAccel",
                                                                          ov,
                                                                          maxAccel));
   }


   public void setMaxSpeed(double maxSpeed) {
      double ov = this.maxSpeed;
      this.maxSpeed = maxSpeed;
      if (ov != this.maxSpeed) firePropertyChange(new PropertyChangeEvent(this,
                                                                          "maxSpeed",
                                                                          ov,
                                                                          maxSpeed));
   }


   public void setNominalFeed(double nomFeed) {
      double ov = this.nomFeed;
      this.nomFeed = nomFeed;
      if (ov != this.nomFeed) firePropertyChange(new PropertyChangeEvent(this,
                                                                         "nominalFeed",
                                                                         ov,
                                                                         nomFeed));
   }


   public void setRapidFactor(double rapidFactor) {
      double ov = this.rapidFactor;
      this.rapidFactor = rapidFactor;
      if (ov != this.rapidFactor) firePropertyChange(new PropertyChangeEvent(this,
                                                                             "rapidFactor",
                                                                             ov,
                                                                             rapidFactor));
   }


   public void setSpindleCurSpeed(double spindleSpeed) {
      double ov = this.spindleCurSpeed;
      this.spindleCurSpeed = spindleSpeed;
      if (ov != this.spindleCurSpeed) firePropertyChange(new PropertyChangeEvent(this,
                                                                                 "spindleCurSpeed",
                                                                                 ov,
                                                                                 spindleSpeed));
   }


   public void setSpindleFactor(double spindleFactor) {
      double ov = this.spindleFactor;
      this.spindleFactor = spindleFactor;
      if (ov != this.spindleFactor) firePropertyChange(new PropertyChangeEvent(this,
                                                                               "spindleFactor",
                                                                               ov,
                                                                               spindleFactor));
   }


   public void setSpindleNominalSpeed(double spindleNomSpeed) {
      double ov = this.spindleNomSpeed;
      this.spindleNomSpeed = spindleNomSpeed;
      if (ov != this.spindleNomSpeed) firePropertyChange(new PropertyChangeEvent(this,
                                                                                 "spindleNominalSpeed",
                                                                                 ov,
                                                                                 spindleNomSpeed));
   }


   public String toString() {
      StringBuilder sb = new StringBuilder("commanded work speed: ");
      
      sb.append(nomFeed);
      sb.append("work speed: ");
      sb.append(curFeed);
      sb.append("\nspeed factor: ");
      sb.append(feedFactor);
      sb.append("\nrapid speed: ");
      sb.append(maxSpeed);
      sb.append("\nrapid factor: ");
      sb.append(rapidFactor);
      sb.append("\naccel: ");
      sb.append(accel);
      sb.append("\nmax Accel: ");
      sb.append(maxAccel);
      sb.append("\ncommanded spindle speed: ");
      sb.append(spindleNomSpeed);
      sb.append("\nspindle speed: ");
      sb.append(spindleCurSpeed);
      sb.append("\nspindle factor: ");
      sb.append(spindleFactor);

      return sb.toString();
   }
   private double feedFactor;
   private double rapidFactor;
   private double curFeed;
   private double nomFeed;
   private double maxSpeed;
   private double accel;
   private double maxAccel;
   private double spindleFactor;
   private double spindleCurSpeed;
   private double spindleNomSpeed;
}
