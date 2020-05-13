package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       CircleButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    7.5.2020 by Django Reinhard
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


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;


public class CircleButton extends AbstractJogConditionButton {
   public CircleButton(String text, ICondition condEnable) {
      this(text, condEnable, 0);
   }


   /**
    * creates a button, that will be shaped by CircleLayout
    *
    * @param text
    *           the buttons text
    * @param level
    *           the execution level/amount. Can be used to differentiate
    *           buttons with same text/actionCommand. {@code level} is used by
    *           {@code CircleLayout} to place the buttons on the corresponding
    *           circle
    */
   public CircleButton(String text, ICondition condEnable, int level) {
      super(text, condEnable, level);
   }


   @Override
   public boolean contains(int x, int y) {
      return shape.contains(x, y);
   }


   public void setCenter(double x, double y) {
      this.xCenter = x;
      this.yCenter = y;
      calcPaint();
   }


   public void setRadius(double r) {
      radius = r;
   }


   public void setShape(Shape s) {
      this.shape = s;
   }


   public void setTextCenter(double x, double y) {
      this.xText = x;
      this.yText = y;
      calcPaint();
   }


   protected void calcPaint() {
      if (radius == 0)
         return;
      Point2D center = new Point2D.Double(xCenter, yCenter);
      float   r0     = (float) (1.6 * radius);
      float[] dist   = { 0.4f, 0.55f };
      Color[] colors = { UITheme.getColor("JogButton:gradient.start"),
            UITheme.getColor("JogButton:gradient.end") };
      Color[] dCols  = { UITheme.getColor("JogButton:gradient.disabled.start"),
            UITheme.getColor("JogButton:gradient.disabled.end") };

      paint         = new RadialGradientPaint(center, r0, dist, colors, CycleMethod.NO_CYCLE);
      paintDisabled = new RadialGradientPaint(center, r0, dist, dCols, CycleMethod.NO_CYCLE);

      float   r1      = (float) (1.6 * radius);
      float[] dist1   = { 0.4f, 0.5f };
      Color[] colors1 = { UITheme.getColor("JogButton:gradient.selected.start"),
            UITheme.getColor("JogButton:gradient.selected.end") };

      paint1 = new RadialGradientPaint(center, r1, dist1, colors1, CycleMethod.NO_CYCLE);
   }


   @Override
   protected void paintBorder(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Color      oc = g2.getColor();

      g.setColor(getBorderColor());
      g2.draw(shape);
      g.setColor(oc);
   }


   @Override
   protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Paint       oldPaint  = g2.getPaint();
      Stroke      oldStroke = g2.getStroke();
      Font        oldFont   = g2.getFont();
      FontMetrics fm        = g2.getFontMetrics(getFont());
      Rectangle2D tb        = fm.getStringBounds(getText(), g2);
      boolean     enabled   = getModel().isEnabled();

      if (enabled) {
         if (getModel().isArmed() || getModel().isPressed())
            g2.setPaint(paint1);
         else
            g2.setPaint(paint);
      } else {
         g2.setPaint(paintDisabled);
      }

      g2.fill(shape);
      g2.setStroke(new BasicStroke(4));
      g2.setPaint(getBorderColor());
      g2.draw(shape);

      g2.setPaint(enabled ? getShadowColor() : getDisabledShadow());
      g2.setFont(getShadowFont());
      g2.drawString(getText(), (int) (xText + 2 - tb.getWidth() / 2),
            (int) (yText + 2 + fm.getAscent() * 0.35));

      g2.setFont(getFont());
      g2.setPaint(enabled ? getForeground() : getDisabledForeground());
      g2.drawString(getText(), (int) (xText - tb.getWidth() / 2), (int) (yText + fm.getAscent() * 0.35));

      g2.setFont(oldFont);
      g2.setStroke(oldStroke);
      g2.setPaint(oldPaint);
   }


   private double              xCenter;
   private double              yCenter;
   private double              radius;
   private double              xText;
   private double              yText;
   private RadialGradientPaint paint;
   private RadialGradientPaint paint1;
   private RadialGradientPaint paintDisabled;
   private Shape               shape;
   private static final long   serialVersionUID = 1L;
}
