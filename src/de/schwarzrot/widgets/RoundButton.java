package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       RoundButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    5.5.2020 by Django Reinhard
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;


public class RoundButton extends AbstractJogConditionButton {
   public RoundButton(String text, ICondition condEnable) {
      super(text, condEnable, 0);
   }


   @Override
   public boolean contains(int x, int y) {
      return shape.contains(x, y);
   }


   @Override
   public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Paint   oldPaint  = g2.getPaint();
      Stroke  oldStroke = g2.getStroke();
      boolean enabled   = getModel().isEnabled();

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
      g2.fill(symbolShadow);
      g2.setPaint(enabled ? getForeground() : getDisabledForeground());
      g2.fill(symbol);

      g2.setStroke(oldStroke);
      g2.setPaint(oldPaint);
   }


   public void setShape(Shape s) {
      this.shape = s;
      Rectangle r       = shape.getBounds();
      int       radius  = Math.min(r.width, r.height) / 2;
      int       xCenter = r.width / 2;
      int       yCenter = r.height / 2;

      calcPaint(xCenter, yCenter, radius);
      calcSymbol(xCenter, yCenter, radius);
   }


   protected void calcPaint(int x, int y, int r) {
      Point2D center = new Point2D.Double(x, y);
      float   radius = (float) (1.6 * r);
      float[] dist   = { 0.45f, 0.6f };
      Color[] colors = { UITheme.getColor("JogButton:gradient.end"),
            UITheme.getColor("JogButton:gradient.start") };
      Color[] dCols  = { UITheme.getColor("JogButton:gradient.disabled.end"),
            UITheme.getColor("JogButton:gradient.disabled.start") };

      paint         = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.NO_CYCLE);
      paintDisabled = new RadialGradientPaint(center, radius, dist, dCols, CycleMethod.NO_CYCLE);
      radius        = (float) (1.6 * r);
      float[] dist1   = { 0.45f, 0.6f };
      Color[] colors1 = { UITheme.getColor("JogButton:gradient.selected.start"),
            UITheme.getColor("JogButton:gradient.selected.end") };

      paint1 = new RadialGradientPaint(center, radius, dist1, colors1, CycleMethod.NO_CYCLE);
   }


   protected void calcSymbol(int x, int y, int r) {
      symbol = new Path2D.Float(new Ellipse2D.Float(x - 12, y - 12, 24, 24));

      symbol.moveTo(x + 11, y);
      symbol.lineTo(x + 25, y + 10);
      symbol.lineTo(x + 25, y + 4);
      symbol.lineTo(x + 48, y + 4);
      symbol.lineTo(x + 48, y - 4);
      symbol.lineTo(x + 25, y - 4);
      symbol.lineTo(x + 25, y - 10);
      symbol.lineTo(x + 11, y);

      symbol.moveTo(x - 11, y);
      symbol.lineTo(x - 25, y + 10);
      symbol.lineTo(x - 25, y + 4);
      symbol.lineTo(x - 48, y + 4);
      symbol.lineTo(x - 48, y - 4);
      symbol.lineTo(x - 25, y - 4);
      symbol.lineTo(x - 25, y - 10);
      symbol.lineTo(x - 11, y);

      symbol.moveTo(x, y + 11);
      symbol.lineTo(x + 10, y + 25);
      symbol.lineTo(x + 4, y + 25);
      symbol.lineTo(x + 4, y + 48);
      symbol.lineTo(x - 4, y + 48);
      symbol.lineTo(x - 4, y + 25);
      symbol.lineTo(x - 10, y + 25);
      symbol.lineTo(x, y + 11);

      symbol.moveTo(x, y - 11);
      symbol.lineTo(x + 10, y - 25);
      symbol.lineTo(x + 4, y - 25);
      symbol.lineTo(x + 4, y - 48);
      symbol.lineTo(x - 4, y - 48);
      symbol.lineTo(x - 4, y - 25);
      symbol.lineTo(x - 10, y - 25);
      symbol.lineTo(x, y - 11);
      AffineTransform t = new AffineTransform();

      t.scale(1.03, 1.03);
      symbolShadow = new Path2D.Float(symbol);
      symbolShadow.transform(t);
   }


   @Override
   protected void paintBorder(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Color      oc = g2.getColor();

      g.setColor(getBorderColor());
      g2.draw(shape);
      g.setColor(oc);
   }


   private Paint             paint;
   private Paint             paint1;
   private Paint             paintDisabled;
   private Shape             shape;
   private Path2D            symbol;
   private Path2D            symbolShadow;
   private static final long serialVersionUID = 1L;
}
