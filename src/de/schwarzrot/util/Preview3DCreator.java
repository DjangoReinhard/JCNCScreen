package de.schwarzrot.util;
/*
 * **************************************************************************
 *
 *  file:       Preview3DCreator.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    25.5.2020 by Django Reinhard
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


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.util.JmeFormatter;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolEntry;
import de.schwarzrot.model.CanonPosition;
import de.schwarzrot.model.ToolInfo;
import de.schwarzrot.widgets.jme3.ArcXY;
import de.schwarzrot.widgets.jme3.ArcYZ;
import de.schwarzrot.widgets.jme3.ICreator3D;
import de.schwarzrot.widgets.jme3.Line;


/**
 * based upon TestParallelProjection from JME3 and FocusCameraStates from
 * jayfella
 *
 * @author django
 */
public class Preview3DCreator extends SimpleApplication
      implements AnalogListener, ActionListener, ICreator3D, PropertyChangeListener {
   /*
    * helper class to access application instance
    */
   abstract class AppRunner implements Runnable {
      protected AppRunner(ICreator3D app) {
         this.app = app;
      }


      ICreator3D app;
   }


   public Preview3DCreator(float[] limits) {
      size          = new Vector3f(limits[1] - limits[0], limits[5] - limits[4], limits[3] - limits[2]);
      baseLoc       = new Vector3f();
      modelMinLimit = new Vector3f();
      modelMaxLimit = new Vector3f();
      rotation      = new Quaternion();
      nick          = new Quaternion();

      l.log(Level.INFO, "workarea size is: " + size);

      // here's the right place to change AppSettings the way we want it to be
      AppSettings settings = new AppSettings(true);

      settings.setAudioRenderer(null);
      settings.setWidth(1000);
      settings.setWidth(1000);
      setSettings(settings);
      setPauseOnLostFocus(false);

      toolPos = LCStatus.getStatus().getPositionCalculator().getPosition();

      toolPos.addPropertyChangeListener(this);
      toolInfo = LCStatus.getStatus().getToolInfo();
      toolInfo.addPropertyChangeListener(this);
      geoCache         = new HashMap<Integer, Geometry>();
      currentSelection = -1;
   }


   /*
    * we don't do the axis mapping here, it's done by geoParser
    */
   @Override
   public void createArc(Vector3f from, Vector3f to, Vector3f center, int lineNum, boolean clockwise) {
      //      l.log(Level.INFO, "arc: " + from + " => " + to + "\tc: " + center + "\tdir: "
      //            + (clockwise ? "CW" : "CCW") + " line #" + lineNum);

      mesh = useYZ ? new ArcYZ(from, to, center, lineNum, clockwise)
            : new ArcXY(from, to, center, lineNum, clockwise);
      geo  = new Geometry(PrimArc, mesh);
      geo.setMaterial(unusedPath);
      geoCache.put(lineNum, geo);
      modelRoot.attachChild(geo);
      updateLimits(from, to);
   }


   /*
    * we don't do the axis mapping here, it's done by geoParser
    */
   @Override
   public void createLine(Vector3f from, Vector3f to, int lineNum, boolean fastMove) {
      //      l.log(Level.INFO,
      //            "line: " + from + " => " + to + " mode: " + (fastMove ? "FAST" : "reg") + " line #" + lineNum);

      mesh = new Line(from, to, lineNum, fastMove);
      geo  = new Geometry(PrimLine, mesh);
      geo.setMaterial(fastMove ? this.fastMove : this.unusedPath);
      geoCache.put(lineNum, geo);
      modelRoot.attachChild(geo);
      if (!fastMove)
         updateLimits(from, to);
   }


   @Override
   public void onAction(String name, boolean isPressed, float tpf) {
      switch (name) {
         case Restore:
            restoreScene();
            break;
         case TellPos:
            tellScene();
            break;
         case RotateTrigger:
            rotate = isPressed;
            break;
         case ToggleTranslate:
            translate = isPressed;
            break;
         case MoveModelTrigger:
            moveModel = isPressed;
            break;
      }
   }


   @Override
   public void onAnalog(String name, float value, float tpf) {
      switch (name) {
         case ZoomIN:
            zoomFactor -= 0.5f * zoomFactor * tpf;
            resizeView();
            break;
         case ZoomOUT:
            zoomFactor += 0.5f * zoomFactor * tpf;
            resizeView();
            break;
         case MouseZoomIN:
            zoomFactor -= 18f * zoomFactor * tpf;
            resizeView();
            break;
         case MouseZoomOUT:
            zoomFactor += 18f * zoomFactor * tpf;
            resizeView();
            break;
         default:
            if (moveModel) {
               // model will only move up and down from workplace
               // This motion is only to compensate different workpiece height
               switch (name) {
                  case KeyUp:
                     modelOffset += 0.6 * zoomFactor * tpf;
                     moveModel();
                     break;
                  case KeyDown:
                     modelOffset -= 0.6 * zoomFactor * tpf;
                     moveModel();
                     break;
                  case MouseMoveUp:
                     modelOffset -= 4 * zoomFactor * tpf;
                     moveModel();
                     break;
                  case MouseMoveDown:
                     modelOffset += 4 * zoomFactor * tpf;
                     moveModel();
                     break;
               }
            } else if (rotate) {
               switch (name) {
                  case KeyLeft:
                     rotation.fromAngleAxis(-0.001f * zoomFactor * tpf, Vector3f.UNIT_Y);
                     rotateDesk();
                     break;
                  case KeyRight:
                     rotation.fromAngleAxis(0.001f * zoomFactor * tpf, Vector3f.UNIT_Y);
                     rotateDesk();
                     break;
                  case KeyUp:
                     ensureNickLimits(-0.001f * zoomFactor * tpf);
                     break;
                  case KeyDown:
                     ensureNickLimits(0.001f * zoomFactor * tpf);
                     break;
                  case MouseMoveLeft:
                     rotation.fromAngleAxis(0.025f * zoomFactor * tpf, Vector3f.UNIT_Y);
                     rotateDesk();
                     break;
                  case MouseMoveRight:
                     rotation.fromAngleAxis(-0.025f * zoomFactor * tpf, Vector3f.UNIT_Y);
                     rotateDesk();
                     break;
                  case MouseMoveUp:
                     ensureNickLimits(0.009f * zoomFactor * tpf);
                     break;
                  case MouseMoveDown:
                     ensureNickLimits(-0.009f * zoomFactor * tpf);
                     break;
               }
            } else if (translate) {
               switch (name) {
                  case MouseMoveLeft:
                     baseLoc.x += 5f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case MouseMoveRight:
                     baseLoc.x -= 5f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case MouseMoveUp:
                     baseLoc.z += 5f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case MouseMoveDown:
                     baseLoc.z -= 5f * zoomFactor * tpf;
                     shiftBase();
                     break;
               }
            } else {
               switch (name) {
                  case KeyLeft:
                     baseLoc.x -= 0.8f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case KeyRight:
                     baseLoc.x += 0.8f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case KeyUp:
                     baseLoc.z -= 0.8f * zoomFactor * tpf;
                     shiftBase();
                     break;
                  case KeyDown:
                     baseLoc.z += 0.8f * zoomFactor * tpf;
                     shiftBase();
                     break;
               }
            }
            break;
      }
   }


   @Override
   public void processGeomFile(File f) throws FileNotFoundException {
      processGeomFile(new FileReader(f));
   }


   //   INFORMATION AutoGCodeLister 05:26:18 load gcode file took: 14571ms
   //   INFORMATION  05:20:40 time consumed by processGeomFile: 21.902ms
   //
   //   INFORMATION  05:23:36 time consumed by processGeomFile: 449ms
   //   INFORMATION AutoGCodeLister 05:23:37 load gcode file took: 1204ms
   @Override
   public void processGeomFile(Reader r) {
      //      Thread.dumpStack();
      long start = System.currentTimeMillis();
      this.enqueue(new AppRunner(this) {
         @Override
         public void run() {
            long           start = System.currentTimeMillis();
            BufferedReader br    = null;
            Vector3f       last  = null;
            String         line;

            wait4Material();
            reset();
            try {
               br = new BufferedReader(r);

               while ((line = br.readLine()) != null) {
                  last = geoParser.parseLine(line, last, app);
               }
            } catch (IOException e) {
               e.printStackTrace();
            } finally {
               if (br != null) {
                  try {
                     br.close();
                  } catch (IOException ex) {
                  }
               }
               restoreScene();
               long end = System.currentTimeMillis();

               l.log(Level.INFO, "time consumed by processGeomFile: " + (end - start) + "ms");
            }
         }
      });
      long end = System.currentTimeMillis();

      l.log(Level.INFO, "time outside of enqueue: " + (end - start) + "ms");
   }


   @Override
   public void propertyChange(PropertyChangeEvent pce) {
      if (pce.getSource().equals(toolPos)) {
         //         l.log(Level.INFO, "property change of tool-tip: " + toolPos.getX() + " / " + toolPos.getY() + " / "
         //               + toolPos.getZ());
         this.enqueue(new AppRunner(this) {
            @Override
            public void run() {
               toolBase.setLocalTranslation((float) toolPos.getX(), (float) toolPos.getZ(),
                     (float) -toolPos.getY());
            }
         });
      } else if (pce.getSource().equals(toolInfo)) {
         this.enqueue(new AppRunner(this) {
            @Override
            public void run() {
               ToolEntry te = toolInfo.getActiveTool();

               if (te != null) {
                  toolBase.detachAllChildren();
                  createTool((float) te.getDiameter() / 2.0f);
               }
            }
         });
      } else {
         l.log(Level.WARNING, "property change event of unknown source ...");
      }
   }


   public void saveScene() {
      //      machine.getLocalRotation();
      //      camLoc;
      //      zoomFactor;
      //      modelOffset
   }


   public void setGeoParser(IGeoLineParser p) {
      this.geoParser = p;
      this.useYZ     = p.isYZmapped();
   }


   @Override
   public void simpleInitApp() {
      flyCam.setEnabled(false);
      cam.setParallelProjection(true);
      Vector3f camLoc = new Vector3f(0, 2f * size.y, size.z);

      l.log(Level.INFO, "camera location: " + camLoc);

      cam.setLocation(camLoc);
      cam.lookAt(camLoc.negate(), camDir);

      l.log(Level.INFO, "camera direction: " + cam.getDirection());

      createDesk();
      createTool(0);
      registerInputs();
      resizeView();
   }


   public void unregisterInputs() {
      inputManager.deleteMapping(Restore);
      inputManager.deleteMapping(TellPos);
      inputManager.deleteMapping(ZoomIN);
      inputManager.deleteMapping(ZoomOUT);
      inputManager.deleteMapping(KeyLeft);
      inputManager.deleteMapping(KeyRight);
      inputManager.deleteMapping(KeyUp);
      inputManager.deleteMapping(KeyDown);
      inputManager.deleteMapping(MouseMoveRight);
      inputManager.deleteMapping(MouseMoveLeft);
      inputManager.deleteMapping(MouseMoveUp);
      inputManager.deleteMapping(MouseMoveDown);
      inputManager.deleteMapping(MouseZoomIN);
      inputManager.deleteMapping(MouseZoomOUT);
      inputManager.deleteMapping(RotateTrigger);
      inputManager.deleteMapping(ToggleTranslate);
      inputManager.deleteMapping(MoveModelTrigger);

      inputManager.removeListener(this);
   }


   /*
    * called from cnc-application to change color of a line segment.
    * Coupling is base on line-number from gcode-file
    */
   public synchronized void update(int selectedLine) {
      //      l.log(Level.INFO, "selected line changed from #" + currentSelection + " to #" + selectedLine);
      this.enqueue(new Runnable() {
         @Override
         public void run() {
            if (currentSelection >= 0) {
               for (int i = currentSelection; i < selectedLine; ++i) {
                  geo = geoCache.get(i);
                  if (geo != null) {
                     //                  l.log(Level.FINEST,
                     //                        "<< ok, found old primitive #" + currentSelection + " and set to oldPath!");
                     if (geo.getMesh() instanceof Line) {
                        if (((Line) geo.getMesh()).isFastMove()) {
                           geo.setMaterial(oldFastMove);
                        } else {
                           geo.setMaterial(oldPath);
                        }
                     } else {
                        geo.setMaterial(oldPath);
                     }
                  }
               }
            }
            currentSelection = selectedLine;
            geo              = geoCache.get(currentSelection);
            if (geo != null) {
               //               l.log(Level.FINEST, ">> ok, found new primitive #" + currentSelection + " and set to active!");
               if (geo.getMesh() instanceof Line) {
                  if (((Line) geo.getMesh()).isFastMove()) {
                     geo.setMaterial(nextFastMove);
                  } else {
                     geo.setMaterial(currentPath);
                  }
               } else {
                  geo.setMaterial(currentPath);
               }
            }
         }
      });
   }


   // creation of graphic primitives is too early, so have to wait for material
   // to become available
   @Override
   public void wait4Material() {
      Material m = null;

      while (m == null) {
         Thread.yield();
         try {
            Thread.sleep(100, 0);
         } catch (InterruptedException e) {
         }
         if (assetManager != null) {
            m = new Material(assetManager, MATUnshaded);
         }
      }
   }


   protected void clearPath() {
      int      mx = geoCache.size();
      Geometry g;

      for (int i = 0; i < mx; ++i) {
         g = geoCache.get(i);
         if (g != null) {
            if (g.getMesh() instanceof Line && ((Line) g.getMesh()).isFastMove()) {
               g.setMaterial(fastMove);
            } else {
               g.setMaterial(unusedPath);
            }
         }
      }
   }


   protected void createDesk() {
      shiftBase = new Node("ShiftBase");
      nickBase  = new Node("NickBase");
      turnTable = new Node("TurnTable");
      modelBase = new Node("ModelBase");
      modelRoot = new Node("ModelRoot");
      toolBase  = new Node("ToolBase");

      rootNode.attachChild(shiftBase);
      shiftBase.attachChild(nickBase);
      nickBase.attachChild(turnTable);
      turnTable.attachChild(modelBase);
      modelBase.attachChild(toolBase);
      modelBase.attachChild(modelRoot);

      // the working area
      Geometry   box             = new Geometry("Box", new WireBox(size.x, size.y, size.z));
      // its our desktop, so create a nice looking surface
      Geometry   ground          = new Geometry("Ground", new Quad(size.x * 2, size.z * 2));
      Material   m               = new Material(assetManager, MATUnshaded);
      Quaternion initialRotation = new Quaternion();

      m.getAdditionalRenderState().setWireframe(true);
      m.setColor(MATColor, ColorRGBA.Green);
      box.setMaterial(m);
      box.setLocalTranslation(0, size.y, 0);

      initialRotation.fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
      turnTable.rotate(initialRotation);
      turnTable.attachChild(box);

      m = new Material(assetManager, MATUnshaded);
      m.setTexture(MATColorMap, assetManager.loadTexture("images/WorkPlane01.png"));
      ground.setMaterial(m);
      ground.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
      ground.setLocalTranslation(-size.x, 0, size.z);
      turnTable.attachChild(ground);

      createOrigin(modelBase);
   }


   protected void createOrigin(Node parent) {
      // visual use of axis does not follow axis from jme3,
      // so create some arrows to show usage and direction of each axis
      // follow color scheme of blender (x is red, y is green and z is blue)
      Mesh     mesh = new Arrow(new Vector3f(100f, 0, 0));
      Geometry geo  = new Geometry(PrimArrow, mesh);
      Material m    = new Material(assetManager, MATUnshaded);

      m.setColor(MATColor, ColorRGBA.Red);
      m.getAdditionalRenderState().setLineWidth(3);
      geo.setMaterial(m);
      parent.attachChild(geo);

      mesh = new Arrow(new Vector3f(0, 0, -100f));
      geo  = new Geometry(PrimArrow, mesh);
      m    = new Material(assetManager, MATUnshaded);
      m.setColor(MATColor, ColorRGBA.Green);
      m.getAdditionalRenderState().setLineWidth(3);
      geo.setMaterial(m);
      parent.attachChild(geo);

      mesh = new Arrow(new Vector3f(0, 100f, 0));
      geo  = new Geometry(PrimArrow, mesh);
      m    = new Material(assetManager, MATUnshaded);
      m.setColor(MATColor, ColorRGBA.Blue);
      m.getAdditionalRenderState().setLineWidth(3);
      geo.setMaterial(m);
      parent.attachChild(geo);

      if (currentPath == null) {
         // create material/colors that will be used by primitive creation/update
         unusedPath = new Material(assetManager, MATUnshaded);
         unusedPath.setColor(MATColor, ColorRGBA.White);
         //         unusedPath.setColor(MATColor, colUnusedPath);
         unusedPath.getAdditionalRenderState().setLineWidth(2);
         oldPath = new Material(assetManager, MATUnshaded);
         oldPath.setColor(MATColor, colOldPath);
         oldPath.getAdditionalRenderState().setLineWidth(3);
         currentPath = new Material(assetManager, MATUnshaded);
         currentPath.setColor(MATColor, colCurPath);
         currentPath.getAdditionalRenderState().setLineWidth(3);
         fastMove = new Material(assetManager, MATUnshaded);
         fastMove.setColor(MATColor, ColorRGBA.Gray);
         fastMove.getAdditionalRenderState().setLineWidth(1);
         nextFastMove = new Material(assetManager, MATUnshaded);
         nextFastMove.setColor(MATColor, ColorRGBA.Cyan);
         nextFastMove.getAdditionalRenderState().setLineWidth(1);
         oldFastMove = new Material(assetManager, MATUnshaded);
         oldFastMove.setColor(MATColor, ColorRGBA.Blue);
         oldFastMove.getAdditionalRenderState().setLineWidth(1);
      }
   }


   protected void createTool(float radius) {
      l.log(Level.INFO, "create tool with radius " + radius);
      Geometry geo = new Geometry("Tool", new Cylinder(2, 16, Math.max(1f, radius), 100, true));
      Material m   = new Material(assetManager, MATShowNormals);

      geo.setMaterial(m);
      geo.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
      geo.setLocalTranslation(0, 50, 0);

      toolBase.attachChild(geo);
   }


   protected void ensureNickLimits(float delta) {
      l.log(Level.INFO, "change nick angle by delta: " + delta);
      nickAngle += delta;

      if (nickAngle < nickMinLimit)
         nickAngle = nickMinLimit;
      if (nickAngle > nickMaxLimit)
         nickAngle = nickMaxLimit;
      l.log(Level.INFO, "nick angle (limited) is now: " + nickAngle);

      nick.fromAngleAxis(nickAngle, Vector3f.UNIT_X);
      l.log(Level.INFO, "nick converted to Quaternion: " + nick);
      nickBase.setLocalRotation(nick);
   }


   protected void moveModel() {
      modelBase.setLocalTranslation(0, modelOffset, 0);
   }


   protected void registerInputs() {
      inputManager.addMapping(ToggleTranslate, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
      inputManager.addMapping(MouseMoveRight, new MouseAxisTrigger(MouseInput.AXIS_X, true));
      inputManager.addMapping(MouseMoveLeft, new MouseAxisTrigger(MouseInput.AXIS_X, false));
      inputManager.addMapping(MouseMoveUp, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
      inputManager.addMapping(MouseMoveDown, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
      inputManager.addMapping(MouseZoomIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
      inputManager.addMapping(MouseZoomOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
      inputManager.addMapping(ZoomIN, new KeyTrigger(KeyInput.KEY_ADD));
      inputManager.addMapping(ZoomOUT, new KeyTrigger(KeyInput.KEY_SUBTRACT));
      inputManager.addMapping(Restore, new KeyTrigger(KeyInput.KEY_R));
      inputManager.addMapping(TellPos, new KeyTrigger(KeyInput.KEY_T));
      inputManager.addMapping(KeyLeft, new KeyTrigger(KeyInput.KEY_LEFT));
      inputManager.addMapping(KeyRight, new KeyTrigger(KeyInput.KEY_RIGHT));
      inputManager.addMapping(KeyUp, new KeyTrigger(KeyInput.KEY_UP));
      inputManager.addMapping(KeyDown, new KeyTrigger(KeyInput.KEY_DOWN));
      inputManager.addMapping(MoveModelTrigger, new KeyTrigger(KeyInput.KEY_LMENU),
            new KeyTrigger(KeyInput.KEY_RMENU), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
      inputManager.addMapping(RotateTrigger, new KeyTrigger(KeyInput.KEY_LSHIFT),
            new KeyTrigger(KeyInput.KEY_RSHIFT), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

      inputManager.addListener(this, Restore, ZoomIN, ZoomOUT, MouseZoomIN, MouseZoomOUT, KeyLeft, KeyRight,
            KeyUp, KeyDown, KeyForward, KeyBack, RotateTrigger, MoveModelTrigger, TellPos, ToggleTranslate,
            MouseMoveRight, MouseMoveLeft, MouseMoveUp, MouseMoveDown);
   }


   protected void reset() {
      modelRoot.detachAllChildren();
      geoCache.clear();
      modelMinLimit.x = 9999;
      modelMinLimit.y = 9999;
      modelMinLimit.z = 9999;
      modelMaxLimit.x = 0;
      modelMaxLimit.y = 0;
      modelMaxLimit.z = 0;
      if (currentPath == null)
         createOrigin(modelBase);
   }


   protected void resizeView() {
      float aspect = (float) cam.getWidth() / (float) cam.getHeight();

      // calculate Viewport (use big limits to avoid clipping)
      cam.setFrustum(-2000, 6000, -aspect * zoomFactor, aspect * zoomFactor, zoomFactor, -zoomFactor);
   }


   /*
    * put values in, that tellScene() printed
    */
   protected void restoreScene() {
      // key trigger is too fast, so have to limit usage to one call
      if (restored) {
         return;
      }
      restored  = true;
      // 3D_Chips
      //      INFORMATION Preview3DCreator 10:32:31  location: (-25.226168, 0.0, 7.533745)
      //      INFORMATION Preview3DCreator 10:32:31      nick: 0.19230837
      //      INFORMATION Preview3DCreator 10:32:31  rotation: (0.0, 0.5569135, 0.0, -0.83058834)
      //      INFORMATION Preview3DCreator 10:32:31 elevation: 124.57374
      //      INFORMATION Preview3DCreator 10:32:31      zoom: 244.67558
      //      INFORMATION Preview3DCreator 10:50:31 model bounds (LBH): 6747.0063 x 7550.216 x 2855.6309
      //
      //      INFORMATION Preview3DCreator 11:08:28  location: (15.20514, 0.0, 25.372692)
      //      INFORMATION Preview3DCreator 11:08:28      nick: 0.2734436
      //      INFORMATION Preview3DCreator 11:08:28  rotation: (0.0, 0.4668734, 0.0, -0.8843415)
      //      INFORMATION Preview3DCreator 11:08:28 elevation: 154.41266
      //      INFORMATION Preview3DCreator 11:08:28      zoom: 266.20135
      //      INFORMATION Preview3DCreator 11:08:28 model bounds (xyz): 7541.354 x 4266.9004 x 7163.832
      //      INFORMATION Preview3DCreator 11:08:28  bounds: 420.0 x 162.0 x 449.024
      //
      // Axis
      //      INFORMATION Preview3DCreator 10:34:48  location: (-66.43312, 0.0, -13.313727)
      //      INFORMATION Preview3DCreator 10:34:48      nick: 0.044106115
      //      INFORMATION Preview3DCreator 10:34:48  rotation: (0.0, 0.2614584, 0.0, -0.96522796)
      //      INFORMATION Preview3DCreator 10:34:48 elevation: 2.775047
      //      INFORMATION Preview3DCreator 10:34:48      zoom: 41.87916
      //      INFORMATION Preview3DCreator 10:46:36 model bounds (LBH): 7452.013 x 6445.8164 x 2698.4731
      //
      //      INFORMATION Preview3DCreator 11:10:08  location: (-69.858116, 0.0, 120.660355)
      //      INFORMATION Preview3DCreator 11:10:08      nick: 0.31528896
      //      INFORMATION Preview3DCreator 11:10:08  rotation: (0.0, 0.1896416, 0.0, -0.98186934)
      //      INFORMATION Preview3DCreator 11:10:08 elevation: 154.41266
      //      INFORMATION Preview3DCreator 11:10:08      zoom: 45.426147
      //      INFORMATION Preview3DCreator 11:10:08 model bounds (xyz): 5967.423 x 4682.212 x 7739.292
      //      INFORMATION Preview3DCreator 11:10:08  bounds: 134.7363 x 5.0 x 20.8848
      //
      // Frästeil13
      //      INFORMATION Preview3DCreator 10:36:21  location: (-110.18828, 0.0, 39.314026)
      //      INFORMATION Preview3DCreator 10:36:21      nick: 0.060475737
      //      INFORMATION Preview3DCreator 10:36:21  rotation: (0.0, 0.28999144, 0.0, -0.95704204)
      //      INFORMATION Preview3DCreator 10:36:21 elevation: 11.032765
      //      INFORMATION Preview3DCreator 10:36:21      zoom: 74.54613
      //      INFORMATION Preview3DCreator 10:45:15 model bounds (LBH): 7440.683 x 6487.437 x 162.00429
      //
      //      INFORMATION Preview3DCreator 11:12:48  location: (-115.74112, 0.0, 190.21783)
      //      INFORMATION Preview3DCreator 11:12:48      nick: 0.038661633
      //      INFORMATION Preview3DCreator 11:12:48  rotation: (0.0, 0.34616548, 0.0, -0.9381887)
      //      INFORMATION Preview3DCreator 11:12:48 elevation: 154.41266
      //      INFORMATION Preview3DCreator 11:12:48      zoom: 74.60272
      //      INFORMATION Preview3DCreator 11:12:48 model bounds (xyz): 7133.4375 x 285.538 x 7345.373
      //      INFORMATION Preview3DCreator 11:12:48  bounds: 110.1555 x 10.5 x 189.0
      //
      //
      //    INFORMATION Preview3DCreator 16:14:35  location: (-184.49377, 0.0, 40.4632)
      baseLoc.x = -184.5f;
      baseLoc.y = 0;
      baseLoc.z = 40.5f;
      shiftBase();

      //    INFORMATION Preview3DCreator 16:14:35      nick: 0.02152655
      nickAngle = -0.0215f;
      ensureNickLimits(0);      // nick always uses absolute rotation

      //    INFORMATION Preview3DCreator 16:14:35  rotation: (0.0, 0.24656528, 0.0, -0.96914166)
      rotation = new Quaternion(0, 0.24656528f, 0, -0.96914166f);
      // rotateDesk uses relative rotation, so don't use it for restore
      //      rotateDesk();
      // instead use absolute rotation
      turnTable.setLocalRotation(rotation);

      //    INFORMATION TestTurnTable 20:06:51 elevation: 33.33061
      modelOffset = 33.33061f;
      moveModel();

      //    INFORMATION Preview3DCreator 16:14:35      zoom: 172.88155
      zoomFactor = 172f;
      resizeView();
   }


   protected void rotateDesk() {
      turnTable.rotate(rotation);
   }


   protected void shiftBase() {
      shiftBase.setLocalTranslation(baseLoc);
   }


   protected void tellScene() {
      //      BoundingBox bounds = (BoundingBox) getModelRoot().getWorldBound();
      BoundingBox b = (BoundingBox) modelRoot.getWorldBound();

      l.log(Level.INFO, " location: " + baseLoc);
      l.log(Level.INFO, "     nick: " + nickAngle);
      l.log(Level.INFO, " rotation: " + turnTable.getLocalRotation());
      l.log(Level.INFO, "elevation: " + modelOffset);
      l.log(Level.INFO, "     zoom: " + zoomFactor);
      l.log(Level.INFO,
            "model bounds (xyz): " + b.getXExtent() + " x " + b.getYExtent() + " x " + b.getZExtent());
      l.log(Level.INFO, " bounds: " + (modelMaxLimit.x - modelMinLimit.x) + " x "
            + (modelMaxLimit.y - modelMinLimit.y) + " x " + (modelMaxLimit.z - modelMinLimit.z));
   }


   protected void updateLimits(Vector3f from, Vector3f to) {
      modelMinLimit.x = Math.min(Math.min(from.x, modelMinLimit.x), to.x);
      modelMinLimit.y = Math.min(Math.min(from.y, modelMinLimit.y), to.y);
      modelMinLimit.z = Math.min(Math.min(from.z, modelMinLimit.z), to.z);
      modelMaxLimit.x = Math.max(Math.max(from.x, modelMaxLimit.x), to.x);
      modelMaxLimit.y = Math.max(Math.max(from.y, modelMaxLimit.y), to.y);
      modelMaxLimit.z = Math.max(Math.max(from.z, modelMaxLimit.z), to.z);
   }


   public static void main(String[] args) {
      JmeFormatter formatter      = new JmeFormatter();
      Handler      consoleHandler = new ConsoleHandler();
      consoleHandler.setFormatter(formatter);

      Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()[0]);
      Logger.getLogger("").addHandler(consoleHandler);
      Preview3DCreator app = new Preview3DCreator(
            // machine limits given from outside
            new float[] { 0, 900, 0, 1800, -500, 0 });

      app.start();
   }


   private IGeoLineParser         geoParser;
   private Map<Integer, Geometry> geoCache;
   private ToolInfo               toolInfo;
   private boolean                useYZ;
   private int                    currentSelection;
   private Material               unusedPath;
   private Material               oldPath;
   private Material               currentPath;
   private Material               oldFastMove;
   private Material               nextFastMove;
   private Material               fastMove;
   private Geometry               geo;
   private Node                   shiftBase;
   private Node                   nickBase;
   private Node                   turnTable;
   private Node                   modelBase;
   private Node                   modelRoot;
   private Node                   toolBase;
   private Mesh                   mesh;
   private boolean                rotate;
   private boolean                translate;
   private boolean                restored;
   private boolean                moveModel;
   private float                  modelOffset;
   private float                  nickAngle;
   private float                  zoomFactor       = 1800f;
   private CanonPosition          toolPos;
   private Quaternion             rotation;
   private Quaternion             nick;
   private Vector3f               baseLoc;
   private Vector3f               modelMinLimit;
   private Vector3f               modelMaxLimit;
   private final Vector3f         size;
   private static final Logger    l;
   private static final float     nickMaxLimit     = 0.5f;
   private static final float     nickMinLimit     = -0.5f;
   private static final Vector3f  camDir           = new Vector3f(0, 1, 0);
   //   private static final ColorRGBA colUnusedPath    = new ColorRGBA(0.98f, 0.5f, 0, 0.5f);
   private static final ColorRGBA colCurPath       = new ColorRGBA(0.93f, 0.85f, 0.33f, 1f);
   private static final ColorRGBA colOldPath       = new ColorRGBA(0.98f, 0.5f, 0, 1f);
   private static final String    ZoomIN           = "zoomIN";
   private static final String    ZoomOUT          = "zoomOUT";
   private static final String    Restore          = "Restore";
   private static final String    TellPos          = "TellPos";
   private static final String    RotateTrigger    = "trigRotation";
   private static final String    MoveModelTrigger = "trigModelMove";
   private static final String    KeyLeft          = "kLeft";
   private static final String    KeyRight         = "kRight";
   private static final String    KeyUp            = "kUp";
   private static final String    KeyDown          = "kDown";
   private static final String    KeyForward       = "kForward";
   private static final String    KeyBack          = "kBack";
   private static final String    ToggleTranslate  = "MToglTrans";
   private static final String    MouseZoomIN      = "MZoomIN";
   private static final String    MouseZoomOUT     = "MZoomOUT";
   private static final String    MouseMoveRight   = "MMRight";
   private static final String    MouseMoveLeft    = "MMLeft";
   private static final String    MouseMoveUp      = "MMUp";
   private static final String    MouseMoveDown    = "MMDown";
   private static final String    MATUnshaded      = "Common/MatDefs/Misc/Unshaded.j3md";
   private static final String    MATShowNormals   = "Common/MatDefs/Misc/ShowNormals.j3md";
   private static final String    MATColor         = "Color";
   private static final String    MATColorMap      = "ColorMap";
   private static final String    PrimArrow        = "Arrow";
   private static final String    PrimArc          = "Arc";
   private static final String    PrimLine         = "Line";
   static {
      l = Logger.getLogger(Preview3DCreator.class.getName());
   }
}
