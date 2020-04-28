package de.schwarzrot.gui;
/* 
 * **************************************************************************
 * 
 *  file:       JogPane.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    13.10.2019 by Django Reinhard
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
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.schwarzrot.bean.ITheme;
import de.schwarzrot.system.CommandWriter;


public class JogPane extends JPanel {
   public JogPane(ITheme settings, CommandWriter cmdWriter) {
      this.cmdWriter = cmdWriter;
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
      add(createManualJogPane(settings));
      add(createJogWheelPane(settings));
   }
   
   protected JComponent createManualJogPane(ITheme settings) {
      JPanel pane = new JPanel();
      Font fButton = new Font("Verdana", Font.BOLD, 45);
      
      pane.setLayout(new GridLayout(0, 3));
      pane.setPreferredSize(new Dimension(600, 600));
      
      btJogXPos = new JButton("X", new ImageIcon("images/go-next.png"));
      btJogXNeg = new JButton("X", new ImageIcon("images/go-previous.png"));
      btJogYPos = new JButton("Y", new ImageIcon("images/go-up.png"));
      btJogYNeg = new JButton("Y", new ImageIcon("images/go-down.png"));
      
      btJogXPos.setFont(fButton);
      btJogXNeg.setFont(fButton);
      btJogYPos.setFont(fButton);
      btJogYNeg.setFont(fButton);
      btJogXNeg.setHorizontalTextPosition(JButton.LEFT);
      btJogXPos.setHorizontalTextPosition(JButton.RIGHT);
      btJogYPos.setVerticalTextPosition(JButton.TOP);
      btJogYPos.setHorizontalTextPosition(JButton.CENTER);
      btJogYNeg.setVerticalTextPosition(JButton.BOTTOM);
      btJogYNeg.setHorizontalTextPosition(JButton.CENTER);
      btJogXPos.setContentAreaFilled(false);
      btJogXNeg.setContentAreaFilled(false);
      btJogYPos.setContentAreaFilled(false);
      btJogYNeg.setContentAreaFilled(false);
      btJogXPos.setBorderPainted(false);
      btJogXNeg.setBorderPainted(false);
      btJogYPos.setBorderPainted(false);
      btJogYNeg.setBorderPainted(false);
      pane.add(new JLabel(" "));
      pane.add(btJogYPos);
      pane.add(new JLabel(" "));
      pane.add(btJogXNeg);
      pane.add(new JLabel(" "));
      pane.add(btJogXPos);
      pane.add(new JLabel(" "));
      pane.add(btJogYNeg);
      pane.add(new JLabel(" "));
      
      return pane;
   }
   
   protected JComponent createJogWheelPane(ITheme settings) {
      JPanel pane = new JPanel();
      
      pane.setPreferredSize(new Dimension(600, 600));
      
      return pane;
   }

   private JButton           btJogXPos;
   private JButton           btJogXNeg;
   private JButton           btJogYPos;
   private JButton           btJogYNeg;
   private JButton           btJogZPos;
   private JButton           btJogZNeg;
   private JButton           btJogUPos;
   private JButton           btJogUNeg;
   private JButton           btJogVPos;
   private JButton           btJogVNeg;
   private JButton           btJogWPos;
   private JButton           btJogWNeg;
   private JButton           btJogAPos;
   private JButton           btJogANeg;
   private JButton           btJogBPos;
   private JButton           btJogBNeg;
   private JButton           btJogCPos;
   private JButton           btJogCNeg;
   private CommandWriter     cmdWriter;
   private static final long serialVersionUID = 1L;
}
