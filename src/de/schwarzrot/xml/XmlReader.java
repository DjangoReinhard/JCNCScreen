package de.schwarzrot.xml;
/* 
 * **************************************************************************
 * 
 *  file:       XmlReader.java
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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class XmlReader {
   public XmlReader(ContentHandler handler) throws ParserConfigurationException,
                                            SAXException {
      this.contentHandler = handler;
      SAXParserFactory spf = SAXParserFactory.newInstance();

      spf.setNamespaceAware(true);
      sp = spf.newSAXParser();
   }


   public void parse(String inputFilename) throws SAXException, IOException {
      XMLReader xmlReader = sp.getXMLReader();

      xmlReader.setContentHandler(contentHandler);
      xmlReader.parse(convertToFileURL(inputFilename));
   }


   protected String convertToFileURL(String filename) {
      String path = new File(filename).getAbsolutePath();

      if (File.separatorChar != '/') {
         path = path.replace(File.separatorChar, '/');
      }

      if (!path.startsWith("/")) {
         path = "/" + path;
      }
      return "file:" + path;
   }

   private SAXParser      sp;
   private ContentHandler contentHandler;
}
