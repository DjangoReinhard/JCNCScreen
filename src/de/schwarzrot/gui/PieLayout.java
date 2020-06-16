package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       PieLayout.java
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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.widgets.PieButton;


public class PieLayout implements LayoutManager2 {
   public PieLayout(double[] angles) {
      this(angles, false);
   }


   public PieLayout(double[] angles, boolean upsideDown) {
      this.angles    = new double[angles.length + 1];
      this.angles[0] = 1;
      for (int i = 0; i < angles.length; ++i) {
         this.angles[i + 1] = angles[i];
      }
      this.upsideDown  = upsideDown;
      this.constraints = new HashMap<Component, CircleConstraints>();
   }


   @Override
   public void addLayoutComponent(Component c, Object constraints) {
      if (c instanceof PieButton) {
         if (constraints instanceof CircleConstraints) {
            CircleConstraints cc = (CircleConstraints) constraints;

            setConstraints(c, cc);
         } else if (constraints instanceof Integer) {
            CircleConstraints cc = new CircleConstraints();

            cc.sequence = (Integer) constraints;
            setConstraints(c, cc);
         } else {
            throw new InvalidParameterException(
                  "constraints must be of type CircleConstraints or integer only!");
         }
      }
   }


   @Override
   public void addLayoutComponent(String arg, Component c) {
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
         Dimension size       = p.getSize();
         double    rOuter     = 0.95 * size.height;
         double    rInner     = 0.2 * size.height;
         boolean   isVertical = size.height > size.width;
         double    xCenter    = 0.5 * size.width;
         double    yCenter    = upsideDown ? 0 : size.height;

         if (isVertical) {
            rOuter  = 0.95 * size.width;
            rInner  = 0.2 * size.width;
            xCenter = upsideDown ? 0 : size.width;
            yCenter = 0.5 * size.height;
         }
         double xOffset = xCenter - rOuter;
         double yOffset = yCenter - rOuter;

         //         System.out.print("create pie layout: ");
         //         if (isVertical)
         //            System.out.print("vertical");
         //         else
         //            System.out.print("horizontal");
         //         if (upsideDown)
         //            System.out.print(" upsideDown");
         //         System.out.print(" with angles: ");
         //         for (double a : angles) {
         //            System.out.print("\t" + a);
         //         }
         //         System.out.println();

         for (Component comp : p.getComponents()) {
            CircleConstraints cc        = constraints.get(comp);
            double            angStart  = 0;
            double            angSize   = 0;
            double            angCenter = 90;

            if (isVertical)
               angCenter = 180;
            if (upsideDown)
               angCenter += 180;
            for (int i = 1; i <= Math.abs(cc.sequence); ++i) {
               angStart += angles[i - 1];
               angSize   = angles[i];
            }
            if (cc.sequence > 0)
               angStart = angCenter - angStart - angSize;
            else
               angStart += angCenter;

            //            System.out.println(
            //                  "pie #" + cc.sequence + " has angles - start: " + angStart + " - size: " + angSize);

            // but process buttons of type PieButtons only!
            if (cc == null || !(comp instanceof PieButton))
               continue;
            PieButton pb       = (PieButton) comp;
            Arc2D     a        = new Arc2D.Double(xOffset, yOffset, 2 * rOuter, 2 * rOuter, angStart, angSize,
                  Arc2D.PIE);
            Ellipse2D e        = new Ellipse2D.Double(xOffset + rOuter - rInner, yOffset + rOuter - rInner,
                  2 * rInner, 2 * rInner);
            Area      resShape = new Area(a);

            resShape.subtract(new Area(e));
            Rectangle bounds = resShape.getBounds();

            pb.setBounds(bounds);
            Area            clientShape = new Area(resShape);
            AffineTransform t           = new AffineTransform();

            t.setToTranslation(-1 * bounds.x, -1 * bounds.y);
            clientShape.transform(t);
            pb.setShape(clientShape);

            double angle = angStart + angSize / 2;
            double rT    = rInner + (rOuter - rInner) / 2;
            double xTOff = rT * Math.sin(Math.toRadians(90 + angle));
            double yTOff = rT * Math.cos(Math.toRadians(90 + angle));
            Point  cT    = new Point((int) (xCenter + (Math.abs(xTOff) > 0.1 ? xTOff : 0)),
                  (int) (yCenter + (Math.abs(yTOff) > 0.1 ? yTOff : 0)));
            Point  pT    = SwingUtilities.convertPoint(p, cT, pb);
            Point  pC    = SwingUtilities.convertPoint(p, new Point((int) xCenter, (int) yCenter), pb);

            pb.setRadius(rOuter);
            pb.setTextCenter(pT.x, pT.y);
            pb.setCenter(pC.x, pC.y);
            int  fontSize      = (int) angSize;
            Font jogButtonFont = UITheme.getFont(UITheme.JogButton_font);

            pb.setShadowFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize + 1));
            pb.setFont(new Font(jogButtonFont.getFamily(), jogButtonFont.getStyle(), fontSize));
         }
      }
   }


   @Override
   public Dimension maximumLayoutSize(Container p) {
      return new Dimension(5000, 5000);
   }


   @Override
   public Dimension minimumLayoutSize(Container p) {
      return new Dimension(400, 200);
   }


   @Override
   public Dimension preferredLayoutSize(Container p) {
      return new Dimension(500, 250);
   }


   @Override
   public void removeLayoutComponent(Component p) {
   }


   protected void setConstraints(Component c, Object constraints) {
      if (constraints instanceof CircleConstraints) {
         this.constraints.put(c, (CircleConstraints) ((CircleConstraints) constraints).clone());
      }
   }


   private boolean                           upsideDown;
   private double[]                          angles;
   private Map<Component, CircleConstraints> constraints;
}
