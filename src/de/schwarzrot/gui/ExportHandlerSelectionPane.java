package de.schwarzrot.gui;
/* 
 * **************************************************************************
 * 
 *  file:       ExportHandlerSelectionPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    5.12.2019 by Django Reinhard
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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import de.schwarzrot.jar.JarInfo;


public class ExportHandlerSelectionPane extends JPanel implements
                                        ActionListener {
   public ExportHandlerSelectionPane(File exT, Map<String, JarInfo> handlers) {
      exportHandlers = handlers;
      exportTarget   = exT;
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      ButtonGroup  group        = new ButtonGroup();
      JRadioButton button       = null;
      JPanel       topPane      = new JPanel(new FlowLayout());
      JTextField   fileName     = new JTextField(15);
      JButton      btChooseFile = new JButton("File");
      boolean      first        = true;

      fileName.setEditable(false);
      fileName.setFocusable(false);
      fileName.setBorder(BorderFactory.createLoweredBevelBorder());
      fileName.setText(exT.getName());
      fileName.setPreferredSize(new Dimension(fileName.getPreferredSize().width,
                                              btChooseFile.getPreferredSize().height));
      btChooseFile.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();

            fc.setCurrentDirectory(exT.getParentFile());
            if (fc.showOpenDialog(topPane) == JFileChooser.APPROVE_OPTION) {
               exportTarget = fc.getSelectedFile();
               fileName.setText(exportTarget.getName());
            } else {
               exportTarget = null;
            }
         }
      });
      topPane.add(fileName);
      topPane.add(btChooseFile);
      add(topPane);
      for (String n : handlers.keySet()) {
         button = new JRadioButton(n);
         button.setActionCommand(n);
         if (first) {
            first           = false;
            selectedHandler = button.getActionCommand();
            button.setSelected(true);
         }
         button.addActionListener(this);
         group.add(button);
         add(button);
      }
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      selectedHandler = ((JRadioButton) e.getSource()).getActionCommand();
   }


   public File getSelectedFile() {
      return exportTarget;
   }


   public JarInfo getSelectedHandler() {
      return exportHandlers.get(selectedHandler);
   }

   private Map<String, JarInfo> exportHandlers;
   private File                 exportTarget;
   private String               selectedHandler;
   private static final long    serialVersionUID = 1L;
}
