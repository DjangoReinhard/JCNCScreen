package de.schwarzrot.bean;
/*
 * **************************************************************************
 *
 *  file:       ToolEntry.java
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


import de.schwarzrot.model.CanonPosition;


public class ToolEntry implements Comparable<ToolEntry> {
   public ToolEntry() {
      offset = new CanonPosition();
   }


   @Override
   public int compareTo(ToolEntry o) {
      return this.getToolNumber() - o.getToolNumber();
   }


   public double getBackAngle() {
      return backAngle;
   }


   public String getDescription() {
      return description;
   }


   public double getDiameter() {
      return diameter;
   }


   public double getFrontAngle() {
      return frontAngle;
   }


   public CanonPosition getOffset() {
      return offset;
   }


   public int getOrientation() {
      return orientation;
   }


   public int getPocketUsed() {
      return pocketUsed;
   }


   public int getToolNumber() {
      return toolNumber;
   }


   public boolean isModified() {
      return modified;
   }


   public void setBackAngle(double backAngle) {
      this.backAngle = backAngle;
   }


   public void setDescription(String description) {
      this.description = description;
   }


   public void setDiameter(double diameter) {
      this.diameter = diameter;
   }


   public void setFrontAngle(double frontAngle) {
      this.frontAngle = frontAngle;
   }


   public void setModified(boolean modified) {
      this.modified = modified;
   }


   public void setOrientation(int orientation) {
      this.orientation = orientation;
   }


   public void setPocketUsed(int pocketUsed) {
      this.pocketUsed = pocketUsed;
   }


   public void setToolNumber(int toolNumber) {
      this.toolNumber = toolNumber;
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("Tool T");

      sb.append(toolNumber);
      sb.append(" P");
      sb.append(pocketUsed);
      if (description != null && !description.isBlank())
         sb.append(" ").append(description);
      sb.append("\noffset: ");
      sb.append(offset.getX());
      sb.append("/");
      sb.append(offset.getY());
      sb.append("/");
      sb.append(offset.getZ());
      sb.append("/");
      sb.append(offset.getA());
      sb.append("/");
      sb.append(offset.getB());
      sb.append("/");
      sb.append(offset.getC());
      sb.append("/");
      sb.append(offset.getU());
      sb.append("/");
      sb.append(offset.getV());
      sb.append("/");
      sb.append(offset.getW());
      sb.append("\nradius: ");
      sb.append(diameter / 2);
      sb.append("\norientation: ");
      sb.append(orientation);

      return sb.toString();
   }


   // Index = 1
   // ToolProfile = EndMill
   // - End Mill
   // - Bull Nose
   // - Ball Nose
   // - V-Cutter
   // - Drill
   // - Lathe
   //
   // Diameter = 1
   // Flutes = 2 (Schneiden)
   // FluteLength = 0 (Schneidenlänge)
   // Length = 0 (z-Länge incl. Futter)
   // ShankDiameter = 0 (Schaft-Durchmesser)
   // Coating = 0 (Beschichtung)
   // Material = 0 (z.B. VHM, HSS)
   // Part Code = 0815
   // HelixAngle = 0
   // VeeAngle = 0 (Spitzenwinkel z.B. bei Gravierstichel o.ä.)
   // MaxRampAngle = 0
   // ToothLoad = 0 (fz)
   // AxialDepthOfCut = 0 (Z-Länge der Schneide, bzw. max ap)
   // RadialDepthOfCut = 0 (Schneidenbreite, bzw. max ae)
   // Collet Diameter = 0
   // Collet Length = 0
   // Comment = ""
   // Notes = ""
   private int             toolNumber;
   private int             pocketUsed;
   private CanonPosition   offset;
   private double          diameter;
   private double          frontAngle;
   private double          backAngle;
   private int             orientation;
   private String          description;
   private boolean         modified;
   public static final int MaxNumOfToolEntries = 56;
}
