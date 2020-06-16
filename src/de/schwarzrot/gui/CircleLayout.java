package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       CircleLayout.java
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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.widgets.CircleButton;
import de.schwarzrot.widgets.RoundButton;


/**
 * a layout manager that organizes buttons in form of concentric circles.
 * That buttons need to be of type {@code CircleButton}. For the center area a
 * round button of type {@code RoundButton} is supported.
 * <p>
 * Button separation of a circle is based on angles (given in degree), so the
 * constructor takes an array of doubles, that specify the angles.
 * <p>
 * The following code demonstrates the typical usage:
 * <pre>
 *    JPanel pane = new JPanel();
 *
 *    setLayout(new CircleLayout(new double[] { -70, 20, 110, 200 }));
 * </pre>
 *
 * @see LayoutManager2
 * @author django
 */
public class CircleLayout implements LayoutManager2 {
   /**
    * @param angles
    *           an array of doubles that specified the angles where the buttons
    *           start/end
    */
   public CircleLayout(double[] angles) {
      this.angles      = angles;
      this.constraints = new HashMap<Component, CircleConstraints>();
      this.cLevels     = 1;
   }


   /**
    * allthough we only need an integer as some kind of index, we have to use
    * {@code CircleConstraint} class, as containers only support index -1 at
    * creation time.
    * So its possible to overwrite {@link JPanel} and overwrite method
    * <pre>Component add(Component c, int sequence)</pre> like this:
    * <pre>
    *    &#64;Override
    *    public Component add(Component c, int index) {
    *       addImpl(c, index, -1);
    *       return c;
    *    }
    * </pre> which uses {@code index} in the sense of an index, but not the
    * default meaning of {@link Container}s index ...
    * <p>
    * ... or use
    * {@code CircleConstraints} to specify the sequence of buttons like this
    * code example:
    * <pre>
    *    CircleConstraints cc = new CircleConstraints();
    *
    *    cc.sequence = 1;
    *    add(new NewCircleButton("my circle-button", 3), cc);
    * </pre>
    */
   @Override
   public void addLayoutComponent(Component c, Object constraints) {
      if (c instanceof CircleButton) {
         CircleButton cb = (CircleButton) c;

         if (constraints instanceof CircleConstraints) {
            CircleConstraints cc = (CircleConstraints) constraints;

            /*
             * level is the number of circles where the buttons may be placed.
             */
            cc.level = cb.getLevel();
            if (cc.level < 1 || cc.level > 5) {
               // level 0 is the round button (optionally) in the center, so ...
               throw new InvalidParameterException("Only levels between 1 and 5 are supported!");
            }
            cLevels = Math.max(cLevels, cc.level);
            setConstraints(c, cc);
         } else if (constraints instanceof Integer) {
            CircleConstraints cc = new CircleConstraints();

            /*
             * level is the number of circles where the buttons may be placed.
             */
            cc.level = cb.getLevel();
            if (cc.level < 1 || cc.level > 5) {
               // level 0 is the round button (optionally) in the center, so ...
               throw new InvalidParameterException("Only levels between 1 and 5 are supported!");
            }
            cc.sequence = (Integer) constraints;
            cLevels     = Math.max(cLevels, cc.level);
            setConstraints(c, cc);
         } else {
            throw new InvalidParameterException(
                  "constraints must be of type CircleConstraints or integer only!");
         }
      } else if (c instanceof RoundButton) {
         center = (RoundButton) c;
      }
   }


   @Override
   public void addLayoutComponent(String arg0, Component c) {
      // not this way!
   }


   @Override
   public float getLayoutAlignmentX(Container p) {
      return 0;
   }


   @Override
   public float getLayoutAlignmentY(Container p) {
      return 0;
   }


   @Override
   public void invalidateLayout(Container p) {
   }


   @Override
   public void layoutContainer(Container p) {
      synchronized (p.getTreeLock()) {
         //TODO:
         //         Insets    insets   = p.getInsets();
         Dimension size         = p.getSize();
         double    diameter     = (int) (0.95 * (size.width < size.height ? size.width : size.height));
         int       centerRadius = 67;
         int       width        = (int) (diameter / 2 - centerRadius);
         double[]  r            = new double[5];
         double[]  f            = factors[cLevels - 2];
         double    xCenter      = size.width / 2;
         double    yCenter      = size.height / 2;
         double    ov           = centerRadius;

         /*
          * buttons are part of a circle, depending on the buttons sequence
          * from CircleConstraints and the angles given to constructor.
          *
          * Calculate width of each circle, based on the predefined factors
          */
         for (int i = 0; i < f.length; ++i) {
            r[i] = ov + f[i] * width;
            ov   = r[i];
         }

         if (center != null) {
            /*
             * if we do have a button for the center area, shape it
             */
            Shape           shape       = new Ellipse2D.Double(xCenter - centerRadius, yCenter - centerRadius,
                  2 * centerRadius, 2 * centerRadius);
            Area            centerShape = new Area(shape);
            AffineTransform t           = new AffineTransform();
            Rectangle       bounds      = shape.getBounds();

            t.setToTranslation(-1 * bounds.x, -1 * bounds.y);
            centerShape.transform(t);
            center.setBounds(shape.getBounds());
            center.setShape(centerShape);
         }

         // now process the rest of components ...
         for (Component comp : p.getComponents()) {
            CircleConstraints cc = constraints.get(comp);

            // but process buttons of type CircleButtons only!
            if (cc == null || !(comp instanceof CircleButton))
               continue;
            CircleButton c          = (CircleButton) comp;
            double       startAngle = angles[cc.sequence - 1];
            double       endAngle   = angles[cc.sequence == angles.length ? 0 : cc.sequence];
            double       sizeAngle  = endAngle - startAngle;

            if (sizeAngle < 0)
               sizeAngle = endAngle + 360 - startAngle;
            double    rOuter   = r[cc.level - 1];
            double    rInner   = cc.level == 1 ? centerRadius : r[cc.level - 2];
            double    xOff     = xCenter - rOuter;
            double    yOff     = yCenter - rOuter;
            Arc2D     a        = new Arc2D.Double(xOff, yOff, 2 * rOuter, 2 * rOuter, startAngle, sizeAngle,
                  Arc2D.PIE);
            Ellipse2D e        = new Ellipse2D.Double(xOff + rOuter - rInner, yOff + rOuter - rInner,
                  2 * rInner, 2 * rInner);
            Area      resShape = new Area(a);

            resShape.subtract(new Area(e));
            Rectangle bounds = resShape.getBounds();

            c.setBounds(bounds);
            c.setCenter(xCenter, yCenter);
            c.setRadius(rOuter);
            Area            clientShape = new Area(resShape);
            AffineTransform t           = new AffineTransform();

            t.setToTranslation(-1 * bounds.x, -1 * bounds.y);
            clientShape.transform(t);
            c.setShape(clientShape);

            double angle = startAngle + sizeAngle / 2;
            double rT    = rInner + (rOuter - rInner) / 2;
            double xTOff = rT * Math.sin(Math.toRadians(90 + angle));
            double yTOff = rT * Math.cos(Math.toRadians(90 + angle));
            Point  cT    = new Point((int) (xCenter + (Math.abs(xTOff) > 0.1 ? xTOff : 0)),
                  (int) (yCenter + (Math.abs(yTOff) > 0.1 ? yTOff : 0)));
            Point  pT    = SwingUtilities.convertPoint(p, cT, c);
            Point  pC    = SwingUtilities.convertPoint(p, new Point((int) xCenter, (int) yCenter), c);

            c.setTextCenter(pT.x, pT.y);
            c.setCenter(pC.x, pC.y);
            int  fontSize      = (int) ((rOuter - rInner) * 0.45);
            Font jogButtonFont = UITheme.getFont(UITheme.JogButton_font);

            c.setShadowFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize + 1));
            c.setFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize));
         }
      }
   }


   @Override
   public Dimension maximumLayoutSize(Container p) {
      return new Dimension(1200, 1200);
   }


   @Override
   public Dimension minimumLayoutSize(Container p) {
      return new Dimension(400, 400);
   }


   @Override
   public Dimension preferredLayoutSize(Container p) {
      return new Dimension(600, 600);
   }


   @Override
   public void removeLayoutComponent(Component c) {
   }


   protected void setConstraints(Component c, Object constraints) {
      if (constraints instanceof CircleConstraints) {
         this.constraints.put(c, (CircleConstraints) ((CircleConstraints) constraints).clone());
      }
   }


   private int                               cLevels;
   private RoundButton                       center;
   private double[]                          angles;
   private Map<Component, CircleConstraints> constraints;
   private static final double[][]           factors = { { 0.382, 0.618 }, { 0.191, 0.309, 0.5 },
         { 0.106, 0.171, 0.276, 0.447 }, { 0.0612, 0.0992, 0.1604, 0.2594, 0.4198 } };
}
