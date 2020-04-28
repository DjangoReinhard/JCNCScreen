package de.schwarzrot.xml;
/* 
 * **************************************************************************
 * 
 *  file:       XmlWriter.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    16.11.2019 by Django Reinhard
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import de.schwarzrot.bean.ToolLibrary;


public class XmlWriter {
   public XmlWriter(String outputFilename) {
      XMLOutputFactory xof = XMLOutputFactory.newInstance();
      FileWriter       fw  = null;
      try {
         fw        = new FileWriter(new File(outputFilename));
         xmlWriter = xof.createXMLStreamWriter(fw);
      }
      catch (IOException | XMLStreamException e) {
         e.printStackTrace();
      }
   }


   public void close() throws XMLStreamException {
      xmlWriter.close();
   }


   public void writeXML(CamBamImportHandler cHdr,
                        ToolLibrary lib) throws XMLStreamException {
      xmlWriter.writeStartDocument();
      xmlWriter.writeCharacters("\n");
      cHdr.writeLibrary(xmlWriter, lib);
      xmlWriter.writeEndDocument();
      xmlWriter.writeCharacters("\n");
   }

   private XMLStreamWriter xmlWriter;
}
