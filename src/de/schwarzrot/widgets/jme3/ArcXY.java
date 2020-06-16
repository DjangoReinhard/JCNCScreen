package de.schwarzrot.widgets.jme3;
/* 
 * **************************************************************************
 * 
 *  file:       ArcXY.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    22.5.2020 by Django Reinhard
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


import java.nio.FloatBuffer;
import java.util.logging.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;


public class ArcXY extends Mesh {
   // based on the work of Lim, YongHoon
   public ArcXY(Vector3f from, Vector3f to, Vector3f center, int lineNum, boolean clockwise) {
      FloatBuffer vertexBuf = createBuffer(from, to, center, clockwise);

      this.lineNum   = lineNum;
      this.clockwise = clockwise;
      setBuffer(Type.Position, 3, vertexBuf);
      setMode(Mode.LineStrip);
      updateBound();
      updateCounts();
   }


   public int getLineNum() {
      return lineNum;
   }


   public boolean isClockwise() {
      return clockwise;
   }


   //   arc: 663,672/431,162/-5,000 => 666,950/447,600/-5,000 C: -278,587/627,599/-5,000
   //   radius check: 962.51733 <> 962.51746
   //   angles[0] - a0: 0.20553064 a1: 0.18811582
   //   angles[1] - a0: 0.20553064 a1: 0.18811582
   //   angles[2] - a0: 0.20553064 a1: 0.18811582  arc: -0.017414823
   //   start with theta: 0.20553064 for 32 samples and stepsize: -5.442132E-4
   //   segment: (663.672, 431.16205, 0.0)  =>      (663.7788, 431.67487, 0.0)
   //   segment: (663.7788, 431.67487, 0.0) =>      (663.8852, 432.18774, 0.0)
   private FloatBuffer createBuffer(Vector3f from, Vector3f to, Vector3f center, boolean clockwise) {
      Vector3f s  = from.subtract(center);
      Vector3f e  = to.subtract(center);
      float    r  = s.length();
      //      float    r1   = e.length();

      //      l.log(Level.INFO,
      //            String.format(Locale.US, "arc: %.3f/%.3f/%.3f -> %.3f/%.3f/%.3f\tC: %.3f/%.3f/%.3f %s\tR: %.3f",
      //                  from.x, from.y, from.z, to.x, to.y, to.z, center.x, center.y, center.z,
      //                  (clockwise ? "CW" : "CCW"), r));
      //      l.log(Level.INFO, String.format(Locale.US, "rel.: %.3f/%.3f/%.3f  =>  %.3f/%.3f/%.3f", s.x, s.y, s.z,
      //            e.x, e.y, e.z));

      float    a0 = FastMath.atan2(s.y, s.x);
      float    a1 = FastMath.atan2(e.y, e.x);

      //      l.log(Level.INFO, "angles[0] - a0: " + a0 + "\ta1: " + a1);

      if (a0 < 0)
         a0 += FastMath.TWO_PI;
      if (a1 < 0)
         a1 += FastMath.TWO_PI;

      float arc = a1 - a0;

      //      l.log(Level.INFO, "angles[1] - a0: " + a0 + "\ta1: " + a1 + "\tarc: " + arc);

      if (!clockwise && a0 > a1 && arc < 0)
         arc += FastMath.TWO_PI;
      else if (clockwise && a0 < a1 && arc > 0)
         arc -= FastMath.TWO_PI;

      //      l.log(Level.INFO, "angles[2] - a0: " + a0 + "\ta1: " + a1 + "\tarc: " + arc + "\tdir: "
      //            + (clockwise ? "CW" : "CCW"));

      float       step  = arc / samples;
      float       zStep = (to.z - from.z) / samples;
      float       x     = 0, y = 0, z = from.getZ(), theta = a0;
      FloatBuffer buf   = BufferUtils.createVector3Buffer(samples + 1);

      if (!clockwise && step < 0)
         step *= -1;
      buf.rewind();

      //      l.log(Level.INFO, "start with theta: " + theta + " and stepsize: " + step);

      for (int i = 0; i <= samples; ++i, theta += step, z += zStep) {
         x = center.x + r * FastMath.cos(theta);
         y = center.y + r * FastMath.sin(theta);

         //         l.log(Level.INFO, String.format(Locale.US, "point: %.3f/%.3f/%.3f\ttheta: %.5f", x, y, z, theta));

         buf.put(x).put(y).put(z);
      }
      return buf;
   }


   private int           lineNum;
   private boolean       clockwise;
   private static int    samples = 64;
   private static Logger l;
   static {
      l = Logger.getLogger("");
   }
}
