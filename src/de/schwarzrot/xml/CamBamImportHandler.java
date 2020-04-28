package de.schwarzrot.xml;
/* 
 * **************************************************************************
 * 
 *  file:       CamBamImportHandler.java
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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.schwarzrot.bean.ToolCategory;
import de.schwarzrot.bean.ToolDefinition;
import de.schwarzrot.bean.ToolLibrary;
import de.schwarzrot.bean.ToolProfile;
import de.schwarzrot.bean.Tuple;
import de.schwarzrot.util.PropertyAccessor;


public class CamBamImportHandler extends DefaultHandler {
   public CamBamImportHandler() {
      ta = new PropertyAccessor(ToolDefinition.class);
   }


   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      String        buf = new String(ch);
      StringBuilder sb  = new StringBuilder("xml:characters:\t(");
      String        v   = buf.substring(start, start + length);

      if (v.strip().isEmpty())
         return;

      sb.append(" - start: ");
      sb.append(start);
      sb.append(" - length: ");
      sb.append(length);
      sb.append(")");

      System.out.println(sb.toString() + " value: " + v);

      if (propertyName.compareTo("profile") == 0) {
         String source = v;
         String name   = null;

         if (v.contains(" - ")) {
            String[] parts = v.split(" - ");

            name   = parts[0];
            source = parts[1];
         }
         ToolProfile tp = ToolProfile.parse(source);

         if (tp != null) {
            toolCategory = tools.getCategory(v);
            if (toolCategory == null) {
               toolCategory = new ToolCategory(tp);
               if (name != null)
                  toolCategory.setName(name);
               tools.addCategory(toolCategory);
            }
            tool.setToolCategory(toolCategory);
            // tools.addTool(tool);
         }
      } else {
         ta.setProperty(tool, propertyName, v);
      }
   }


   @Override
   public void endDocument() throws SAXException {
      System.out.println("\t\txml:endDocument");
   }


   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      System.out.println("\t\t" + localName + " - xml:endElement: ");
      if ("ToolDefinition".compareTo(localName) == 0) {
         tools.addTool(tool);
      }
   }


   public ToolLibrary getLibrary() {
      return tools;
   }


   @Override
   public void startDocument() throws SAXException {
      System.out.println("xml:startDocument");
   }


   @Override
   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      System.out.println("xml:startElement: " + localName);
      Tuple is = importNames.get(localName);

      if (is != null && "ToolLibrary".compareTo(is.internal) == 0) {
         tools = new ToolLibrary();
         if (atts != null) {
            Method tSetter;
            for (int i = 0; i < atts.getLength(); ++i) {
               String n = atts.getLocalName(i);
               String v = atts.getValue(i);
               System.out.println("\tAttribute: " + n + " => " + v);
               System.out.println("\tSetter-name: " + setterName(n));
               try {
                  tSetter = ToolLibrary.class.getMethod(setterName(n), String.class);
                  if (tSetter != null) {
                     tSetter.invoke(tools, v);
                  }
               } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException e) {
                  e.printStackTrace();
               }
            }
         }
      } else if ("ToolDefinition".compareTo(localName) == 0) {
         tool = new ToolDefinition();
         if (atts != null) {
            for (int i = 0; i < atts.getLength(); ++i) {
               Tuple  t = importNames.get(atts.getLocalName(i));
               String n = atts.getLocalName(i);
               String v = atts.getValue(i);

               if (n != null)
                  n = t.internal;
               System.out.println("\tAttribute: " + n + " => " + v);
               System.out.println("\tSetter-name: " + setterName(n));
               try {
                  Method setter = ToolDefinition.class.getMethod(setterName(n), String.class);
                  if (setter != null) {
                     setter.invoke(tool, v);
                  }
               } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException e) {
                  e.printStackTrace();
               }
            }
         }
      } else {
         Tuple t = importNames.get(localName);

         if (t == null) {
            // System.err.println(">>> xml:startElement: " + localName
            // + " <<< - unsupported start element!");
            propertyName = localName;
         } else {
            propertyName = t.internal;
         }
      }
   }


   public void writeLibrary(XMLStreamWriter xsw, ToolLibrary lib) throws XMLStreamException {
      String k = lib.getClass().getSimpleName();

      if (importNames.containsKey(k)) {
         k = importNames.get(k).internal;
      }
      xsw.writeStartElement(k);
      xsw.writeAttribute("Version", lib.getVersion());
      xsw.writeAttribute("ToolNameFormat", lib.getToolNameFormat());
      xsw.writeAttribute("NumberFormat", lib.getNumberFormat());

      writeCategory(xsw, lib.getRoot());

      xsw.writeCharacters("\n");
      xsw.writeEndElement();
   }


   protected String setterName(String attrName) {
      StringBuilder sb = new StringBuilder("set");

      sb.append(attrName.substring(0, 1).toUpperCase());
      sb.append(attrName.substring(1));

      return sb.toString();
   }


   protected void writeCategory(XMLStreamWriter xsw, DefaultMutableTreeNode node) throws XMLStreamException {
      for (int i = 0; i < node.getChildCount(); ++i) {
         writeCategory(xsw, (DefaultMutableTreeNode) node.getChildAt(i));
      }
      Object ud = node.getUserObject();

      if (ud instanceof ToolCategory) {
         ToolCategory tc = (ToolCategory) ud;

         // xsw.writeCharacters("\n");
         // xsw.writeStartElement(ToolCategory.class.getSimpleName());
         // xsw.writeAttribute("Profile", tc.getToolProfile().name());
         // if (tc.getName() != null && !tc.getName().isBlank()) {
         // xsw.writeAttribute("Name", tc.getName());
         // }
         for (ToolDefinition td : tc.getTools()) {
            writeTool(xsw, td);
         }
         // xsw.writeCharacters("\n");
         // xsw.writeEndElement();
      }
   }


   protected void writeTool(XMLStreamWriter xsw, ToolDefinition td) throws XMLStreamException {

      xsw.writeCharacters("\n");
      xsw.writeStartElement(td.getClass().getSimpleName());
      xsw.writeAttribute("Name", td.getToolName());

      ToolCategory tc = td.getToolCategory();

      xsw.writeCharacters("\n");
      xsw.writeStartElement("ToolProfile");
      if (tc.getName() != null && !tc.getName().isEmpty()) {
         xsw.writeCharacters(tc.getName());
         xsw.writeCharacters(" - ");
      }
      xsw.writeCharacters(tc.getToolProfile().name());
      xsw.writeEndElement();

      for (String pn : ToolDefinition.propertyNames) {
         String k = pn;
         Tuple  t = exportNames.get(pn);
         Object v = null;

         if (t != null)
            k = t.external;
         System.out.println("export property >" + k + "<");

         v = ta.getProperty(td, pn);
         if (v != null) {
            xsw.writeCharacters("\n");
            xsw.writeStartElement(k);
            xsw.writeCharacters(v.toString());
            xsw.writeEndElement();
         }
      }
      xsw.writeCharacters("\n");
      xsw.writeEndElement();
   }


   private ToolLibrary               tools;
   private ToolDefinition            tool;
   private ToolCategory              toolCategory;
   private PropertyAccessor          ta;
   private String                    propertyName;
   private static Map<String, Tuple> importNames;
   private static Map<String, Tuple> exportNames;
   static {
      importNames = new HashMap<String, Tuple>();
      exportNames = new HashMap<String, Tuple>();
      Tuple t = new Tuple("DBToolLibrary", "ToolLibrary");

      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("toolNameFormat", "ToolNameFormat");

      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("numberFormat", "NumberFormat");

      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("Index", "toolNumber");

      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("Name", "toolName");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("Diameter", "fluteDiameter");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("ToolProfile", "profile");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("Flutes", "flutes");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("FluteLength", "fluteLength");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("Length", "freeLength");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("ShankDiameter", "shankDiameter");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("HelixAngle", "helixAngle");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("VeeAngle", "cuttingAngle");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("MaxRampAngle", "maxRampAngle");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("ToothLoad", "toothLoad");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("AxialDepthOfCut", "cuttingLength");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);

      t = new Tuple("RadialDepthOfCut", "cuttingRadius");
      importNames.put(t.external, t);
      exportNames.put(t.internal, t);
   }
}
