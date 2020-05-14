package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       AbstractJogConditionButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    9.5.2020 by Django Reinhard
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


import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.InvalidParameterException;

import javax.swing.JButton;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.AndCondition;
import de.schwarzrot.logic.EqualCondition;
import de.schwarzrot.logic.ICondition;
import de.schwarzrot.logic.OrCondition;
import de.schwarzrot.model.ValueModel;
import de.schwarzrot.system.MachineControl;


public class AbstractJogConditionButton extends JButton {
   public AbstractJogConditionButton(String text, ICondition condEnable, int level) {
      super(text);
      this.level = new ValueModel<Integer>("jogLevel", level);
      ValueModel<Boolean> jogSingleStep = MachineControl.getInstance().getSingleStepModel();
      this.setContentAreaFilled(false);
      this.setFocusPainted(false);
      this.setOpaque(false);
      this.setForeground(UITheme.getColor("JogButton:foreground"));
      this.shadowColor        = UITheme.getColor("JogButton:shadow");
      this.borderColor        = UITheme.getColor("JogButton:border");
      this.disabledForeground = UITheme.getColor("JogButton:disabled.foreground");
      this.disabledShadow     = UITheme.getColor("JogButton:disabled.shadow");
      this.shadowFont         = getFont();
      this.condEnable         = new AndCondition(condEnable,
            new OrCondition(new EqualCondition<Boolean>(jogSingleStep, true),
                  new AndCondition(new EqualCondition<Boolean>(jogSingleStep, false),
                        new EqualCondition<Integer>(this.level, 3))));
      this.condEnable.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent e) {
            checkState();
         }
      });
      checkState();
   }


   public final Color getBorderColor() {
      return borderColor;
   }


   public final Color getDisabledForeground() {
      return disabledForeground;
   }


   public final Color getDisabledShadow() {
      return disabledShadow;
   }


   public int getLevel() {
      return level.getValue();
   }


   public final Color getShadowColor() {
      return shadowColor;
   }


   public final Color getShadowDisabled() {
      return shadowDisabled;
   }


   public final Font getShadowFont() {
      return shadowFont;
   }


   public final void setBorderColor(Color borderColor) {
      this.borderColor = borderColor;
   }


   public final void setDisabledForeground(Color disabledForeground) {
      this.disabledForeground = disabledForeground;
   }


   public final void setDisabledShadow(Color disabledShadow) {
      this.disabledShadow = disabledShadow;
   }


   public final void setShadowColor(Color shadowColor) {
      this.shadowColor = shadowColor;
   }


   public final void setShadowDisabled(Color shadowDisabled) {
      this.shadowDisabled = shadowDisabled;
   }


   public final void setShadowFont(Font shadowFont) {
      this.shadowFont = shadowFont;
   }


   protected void checkState() {
      setEnabled(condEnable.eval());
   }


   protected void setLevel(int level) {
      System.err.println("Oups - someone tries to change my level !?!");
      if (level < 1 || level > 5) {
         // level 0 is the round button in the center, so ...
         throw new InvalidParameterException("Only levels between 1 and 5 are supported!");
      }
      this.level.setValue(level);
   }


   private ValueModel<Integer> level;
   private Color               borderColor;
   private Color               shadowColor;
   private Color               shadowDisabled;
   private Color               disabledForeground;
   private Color               disabledShadow;
   private Font                shadowFont;
   private final ICondition    condEnable;
   private static final long   serialVersionUID = 1L;
}
