package de.schwarzrot.bean;
/* 
 * **************************************************************************
 * 
 *  file:       ToolDefinition.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    9.11.2019 by Django Reinhard
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


import de.schwarzrot.linuxcnc.data.ToolInfo;
import de.schwarzrot.util.PropertyAccessor;


public class ToolDefinition implements Comparable<ToolDefinition> {
   @Override
   public int compareTo(ToolDefinition o) {
      if (o.getToolName() != null && getToolName() != null)
         return getToolName().compareTo(o.getToolName());
      return toolNumber - o.toolNumber;
   }


   public void dump() {
      StringBuilder sb = new StringBuilder("Tool-Definition:\n");

      for (String pn : propertyNames) {
         sb.append("\t");
         sb.append(pn);
         sb.append(" = ");
         sb.append(propAccessor.getProperty(this, pn));
         sb.append("\n");
      }
      System.out.println(sb.toString());
   }


   public String getCoating() {
      return coating;
   }


   public double getColletDiameter() {
      return colletDiameter;
   }


   public double getColletLength() {
      return colletLength;
   }


   public String getComment() {
      return comment;
   }


   public double getCuttingAngle() {
      return cuttingAngle;
   }


   public double getCuttingLength() {
      return cuttingLength;
   }


   public double getCuttingRadius() {
      return cuttingRadius;
   }


   public double getFluteDiameter() {
      return fluteDiameter;
   }


   public double getFluteLength() {
      return fluteLength;
   }


   public int getFlutes() {
      return flutes;
   }


   public double getFreeLength() {
      return freeLength;
   }


   public double getHelixAngle() {
      return helixAngle;
   }


   public String getMaterial() {
      return material;
   }


   public double getMaxRampAngle() {
      return maxRampAngle;
   }


   public String getNote() {
      return note;
   }


   public String getPartCode() {
      return partCode;
   }


   public ToolProfile getProfile() {
      return toolCategory.getToolProfile();
   }


   public double getShankDiameter() {
      return shankDiameter;
   }


   public double getSlopeAngle() {
      return slopeAngle;
   }


   public double getTipDiameter() {
      return tipDiameter;
   }


   public ToolCategory getToolCategory() {
      return toolCategory;
   }


   public double getToolLength() {
      return colletLength + freeLength;
   }


   public String getToolName() {
      return toolName;
   }


   public int getToolNumber() {
      return toolNumber;
   }


   public double getToothLoad() {
      return toothLoad;
   }


   public boolean isModified() {
      return this.modified;
   }


   public boolean isSelected() {
      return selected;
   }


   public void setClean() {
      this.modified = false;
   }


   public void setCoating(String coating) {
      if ((this.coating == null && coating != null)
            || (coating != null && this.coating.compareTo(coating) != 0)) {
         modified = true;
      }
      this.coating = coating;
   }


   public void setColletDiameter(double colletDiameter) {
      if (this.colletDiameter != colletDiameter)
         modified = true;
      this.colletDiameter = colletDiameter;
   }


   public void setColletLength(double colletLength) {
      if (this.colletLength != colletLength)
         modified = true;
      this.colletLength = colletLength;
   }


   public void setComment(String comment) {
      if ((this.comment == null && comment != null)
            || (comment != null && this.comment.compareTo(comment) != 0)) {
         modified = true;
      }
      this.comment = comment;
   }


   public void setCuttingAngle(double cuttingAngle) {
      if (this.cuttingAngle != cuttingAngle)
         modified = true;
      this.cuttingAngle = cuttingAngle;
   }


   public void setCuttingLength(double cuttingLength) {
      if (this.cuttingLength != cuttingLength)
         modified = true;
      this.cuttingLength = cuttingLength;
   }


   public void setCuttingRadius(double cuttingRadius) {
      if (this.cuttingRadius != cuttingRadius)
         modified = true;
      this.cuttingRadius = cuttingRadius;
   }


   public void setFluteDiameter(double fluteDiameter) {
      if (this.fluteDiameter != fluteDiameter)
         modified = true;
      this.fluteDiameter = fluteDiameter;
   }


   public void setFluteLength(double fluteLength) {
      if (this.fluteLength != fluteLength)
         modified = true;
      this.fluteLength = fluteLength;
   }


   public void setFlutes(int flutes) {
      if (this.flutes != flutes)
         modified = true;
      this.flutes = flutes;
   }


   public void setFreeLength(double freeLength) {
      if (this.freeLength != freeLength)
         modified = true;
      this.freeLength = freeLength;
   }


   public void setHelixAngle(double helixAngle) {
      if (this.helixAngle != helixAngle)
         modified = true;
      this.helixAngle = helixAngle;
   }


   public void setMaterial(String material) {
      if ((this.material == null && material != null)
            || (material != null && this.material.compareTo(material) != 0)) {
         modified = true;
      }
      this.material = material;
   }


   public void setMaxRampAngle(double maxRampAngle) {
      if (this.maxRampAngle != maxRampAngle)
         modified = true;
      this.maxRampAngle = maxRampAngle;
   }


   public void setNote(String note) {
      if ((this.note == null && note != null) || (note != null && this.note.compareTo(note) != 0)) {
         modified = true;
      }
      this.note = note;
   }


   public void setPartCode(String partCode) {
      if ((this.partCode == null && partCode != null)
            || (partCode != null && this.partCode.compareTo(partCode) != 0)) {
         modified = true;
      }
      this.partCode = partCode;
   }


   public void setSelected(boolean selected) {
      this.selected = selected;
   }


   public void setShankDiameter(double shaftDiameter) {
      if (this.shankDiameter != shaftDiameter)
         modified = true;
      this.shankDiameter = shaftDiameter;
   }


   public void setSlopeAngle(double slopeAngle) {
      if (this.slopeAngle != slopeAngle)
         modified = true;
      this.slopeAngle = slopeAngle;
   }


   public void setTipDiameter(double tipDiameter) {
      if (this.tipDiameter != tipDiameter)
         modified = true;
      this.tipDiameter = tipDiameter;
   }


   public void setToolCategory(ToolCategory toolCategory) {
      if (this.toolCategory != null) {
         this.toolCategory.getTools().remove(this);
      }
      this.toolCategory = toolCategory;
      this.toolCategory.addTool(this);
   }


   public void setToolLength(double l) {
      // do nothing - just allow PropertyAccessor not to fail
   }


   public void setToolName(String name) {
      if ((this.toolName == null && name != null)
            || (name != null && this.toolName.compareTo(toolName) != 0)) {
         modified = true;
      }
      this.toolName = name;
   }


   public void setToolNumber(int toolNumber) {
      if (this.toolNumber != toolNumber)
         modified = true;
      this.toolNumber = toolNumber;
   }


   public void setToothLoad(double toothLoad) {
      if (this.toothLoad != toothLoad)
         modified = true;
      this.toothLoad = toothLoad;
   }


   public ToolInfo toInfo() {
      ToolInfo ti = new ToolInfo();

      for (String sn : infoAccessor.setters()) {
         infoAccessor.setProperty(ti, sn, propAccessor.getProperty(this, sn));
      }
      return ti;
   }


   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("ToolDefinition - ");

      sb.append(toolName);
      sb.append("\n\ttoolNumber: ").append(toolNumber);
      sb.append("\n\tflutes: ").append(flutes);
      sb.append("\n\tfluteDiameter: ").append(fluteDiameter);
      sb.append("\n\tfluteLength: ").append(fluteLength);
      sb.append("\n\ttool length: ").append(getToolLength());

      return sb.toString();
   }


   private boolean                 selected;
   private boolean                 modified;
   private int                     toolNumber;
   private String                  toolName;
   private double                  colletDiameter;
   private double                  colletLength;
   private double                  shankDiameter;
   private double                  freeLength;
   private double                  slopeAngle;
   private int                     flutes;
   private double                  fluteDiameter;
   private double                  fluteLength;
   private double                  cuttingRadius;
   private double                  cuttingLength;
   private double                  tipDiameter;
   private double                  cuttingAngle;
   private String                  partCode;
   private String                  material;
   private String                  coating;
   private double                  toothLoad;
   private double                  helixAngle;
   private double                  maxRampAngle;
   private String                  comment;
   private String                  note;
   private ToolCategory            toolCategory;
   private static PropertyAccessor infoAccessor  = new PropertyAccessor(ToolInfo.class);
   private static PropertyAccessor propAccessor  = new PropertyAccessor(ToolDefinition.class);
   public static final String[]    propertyNames = new String[] { "toolNumber",            // 0
         "toolName",              // 1
         // "toolLength", // 2
         // "toolCategory", // xx
         "colletDiameter",        // 3
         "colletLength",          // 4
         "shankDiameter",         // 5
         "freeLength",            // 6
         "slopeAngle",            // 7
         "flutes",                // 8
         "fluteDiameter",         // 9
         "fluteLength",           // 10
         "cuttingRadius",         // 11
         "cuttingLength",         // 12
         "cuttingAngle",          // 13
         "tipDiameter",           // 14
         "partCode",              // 15
         "material",              // 16
         "coating",               // 17
         "toothLoad",             // 18
         "helixAngle",            // 19
         "maxRampAngle",          // 20
         "comment",               // 21
         "note" };                // 22
}
