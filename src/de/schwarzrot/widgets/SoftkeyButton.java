package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       SoftkeyButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.9.2019 by Django Reinhard
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


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.net.URL;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;


public class SoftkeyButton extends ConditionButton implements ChangeListener {
   public SoftkeyButton(String iconPath, String selectedIconPath, ICondition condEnable,
         ICondition condActive) {
      super(null, condEnable);
      this.condActive = condActive;
      this.condActive.addPropertyChangeListener(this);
      this.setPreferredSize(UITheme.getDimension("Toolbar:button.size"));
      this.setMinimumSize(UITheme.getDimension("Toolbar:button.size"));
      this.setMaximumSize(UITheme.getDimension("Toolbar:button.size"));
      this.setBackground(UITheme.getColor("Toolbar:grid.color"));
      this.setBorder(new EmptyBorder(1, 1, 1, 1));
      this.setOpaque(true);
      //      ImageIcon icon = new ImageIcon(iconPath);
      ImageIcon icon = loadIcon(iconPath);

      this.setIcon(icon);
      if (selectedIconPath != null)
         this.setSelectedIcon(loadIcon(selectedIconPath));
      this.setDisabledIcon(createDisabledIcon(icon));
      checkState();
   }


   @Override
   public void propertyChange(PropertyChangeEvent e) {
      checkState();
   }


   @Override
   public void stateChanged(ChangeEvent arg0) {
      checkState();
   }


   @Override
   protected void checkState() {
      super.checkState();
      if (condActive != null && this.isEnabled())
         model.setSelected(condActive.eval());
   }


   protected ImageIcon createDisabledIcon(ImageIcon i) {
      GrayFilter    filter    = new GrayFilter(true, UITheme.getInt("Icon:disabled.factor"));
      ImageProducer prod      = new FilteredImageSource(i.getImage().getSource(), filter);
      Image         grayImage = Toolkit.getDefaultToolkit().createImage(prod);

      return new ImageIcon(grayImage);
   }


   protected ImageIcon loadIcon(String iconPath) {
      File      f    = new File(iconPath);
      ImageIcon icon = null;

      if (f.exists() && f.canRead())
         icon = new ImageIcon(iconPath);
      else {
         URL url = getClass().getClassLoader().getResource(iconPath);
         icon = new ImageIcon(url);
      }
      return icon;
   }


   private final ICondition  condActive;
   private static final long serialVersionUID = 1L;
}
