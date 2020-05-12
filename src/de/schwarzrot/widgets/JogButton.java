package de.schwarzrot.widgets;
/*
 * **************************************************************************
 *
 *  file:       JogButton.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    3.5.2020 by Django Reinhard
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.logic.ICondition;


public class JogButton extends AbstractJogConditionButton {
   public JogButton(String text, int level, double width, double height, ICondition condEnable) {
      this(text, level, width, height, condEnable, true);
   }


   public JogButton(String text, int level, double width, double height, ICondition condEnable,
         boolean isVertical) {
      super(text, condEnable, level);
      setPreferredSize(new Dimension((int) width, (int) height));
      setMinimumSize(new Dimension((int) width, (int) height));
      setMaximumSize(new Dimension((int) width, (int) height));
      int  fontSize      = (int) (Math.min(width, height) * 0.45);
      Font jogButtonFont = UITheme.getFont("JogButton:font");

      xText = width / 2;
      yText = height / 2;
      setShadowFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize + 2));
      setFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize));
      setActionCommand(String.format("%d %s", getLevel(), text));
      setBorderPainted(false);
      shape = new RoundRectangle2D.Double(0, 0, width, height, 15, 15);
      calcPaint(height);
   }


   @Override
   public void paintComponent(Graphics g) {
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


   protected void calcPaint(double height) {
      Point2D start  = new Point2D.Double(0, 0);
      Point2D end    = new Point2D.Double(0, height);
      float[] dist   = { 0.1f, 0.3f };
      Color[] colors = { UITheme.getColor("JogButton:gradient.start"),
            UITheme.getColor("JogButton:gradient.end") };
      Color[] dCols  = { UITheme.getColor("JogButton:gradient.disabled.start"),
            UITheme.getColor("JogButton:gradient.disabled.end") };

      paint         = new LinearGradientPaint(start, end, dist, colors, CycleMethod.NO_CYCLE);
      paintDisabled = new LinearGradientPaint(start, end, dist, dCols, CycleMethod.NO_CYCLE);
      float[] dist1   = { 0.7f, 0.9f };
      Color[] colors1 = { UITheme.getColor("JogButton:gradient.selected.start"),
            UITheme.getColor("JogButton:gradient.selected.end") };

      paint1 = new LinearGradientPaint(start, end, dist1, colors1, CycleMethod.NO_CYCLE);
   }


   private double            xText;
   private double            yText;
   private Paint             paint;
   private Paint             paint1;
   private Paint             paintDisabled;
   private Shape             shape;
   private static final long serialVersionUID = 1L;
}
