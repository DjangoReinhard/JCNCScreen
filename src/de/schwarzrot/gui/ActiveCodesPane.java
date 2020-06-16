package de.schwarzrot.gui;
/*
 * **************************************************************************
 *
 *  file:       ActiveCodesPane.java
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


import java.awt.GridLayout;

import javax.swing.JPanel;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.model.ActiveCodes;
import de.schwarzrot.util.BindUtils;
import de.schwarzrot.widgets.ActiveCode;
import de.schwarzrot.widgets.ActiveGCode;


public class ActiveCodesPane extends JPanel {
   public ActiveCodesPane() {
      GridLayout grid = new GridLayout(0, 4);

      setLayout(grid);
      grid.setHgap(2);
      grid.setVgap(2);
      setBackground(UITheme.getColor(UITheme.ActiveCode_grid_color));
      setOpaque(true);
      createGUI();
   }


   protected void createGUI() {
      ActiveCodes ac = LCStatus.getStatus().getActiveCodes();

      if (aGC1 == null) {
         aGC1  = new ActiveGCode();
         aGC2  = new ActiveGCode();
         aGC3  = new ActiveGCode();
         aGC4  = new ActiveGCode();
         aGC5  = new ActiveGCode();
         aGC6  = new ActiveGCode();
         aGC7  = new ActiveGCode();
         aGC8  = new ActiveGCode();
         aGC9  = new ActiveGCode();
         aGC10 = new ActiveGCode();
         aGC11 = new ActiveGCode();
         aGC12 = new ActiveGCode();
         aGC13 = new ActiveGCode();
         aGC14 = new ActiveGCode();
         aGC15 = new ActiveGCode();

         aMC1  = new ActiveCode();
         aMC2  = new ActiveCode();
         aMC3  = new ActiveCode();
         aMC4  = new ActiveCode();
         aMC5  = new ActiveCode();
         aMC6  = new ActiveCode();
         aMC7  = new ActiveCode();

         BindUtils.bind("gcode1", ac, aGC1);
         BindUtils.bind("gcode2", ac, aGC2);
         BindUtils.bind("gcode3", ac, aGC3);
         BindUtils.bind("gcode4", ac, aGC4);
         BindUtils.bind("gcode5", ac, aGC5);
         BindUtils.bind("gcode6", ac, aGC6);
         BindUtils.bind("gcode7", ac, aGC7);
         BindUtils.bind("gcode8", ac, aGC8);
         BindUtils.bind("gcode9", ac, aGC9);
         BindUtils.bind("gcode10", ac, aGC10);
         BindUtils.bind("gcode11", ac, aGC11);
         BindUtils.bind("gcode12", ac, aGC12);
         BindUtils.bind("gcode13", ac, aGC13);
         BindUtils.bind("gcode14", ac, aGC14);
         BindUtils.bind("gcode15", ac, aGC15);

         BindUtils.bind("mcode1", ac, aMC1);
         BindUtils.bind("mcode2", ac, aMC2);
         BindUtils.bind("mcode3", ac, aMC3);
         BindUtils.bind("mcode4", ac, aMC4);
         BindUtils.bind("mcode5", ac, aMC5);
         BindUtils.bind("mcode6", ac, aMC6);
         BindUtils.bind("mcode7", ac, aMC7);
      }
      add(aGC1);
      add(aGC4);
      add(aGC8);
      add(aMC1);

      add(aGC6);
      add(aGC2);
      add(aGC3);
      add(aMC2);

      add(aGC7);
      add(aGC12);
      add(aGC13);
      add(aMC3);

      add(aGC14);
      add(aGC5);
      add(aGC15);
      add(aMC4);

      add(aGC9);
      add(aGC10);
      add(aGC11);
      add(aMC5);

      ac.reset();
   }


   private ActiveGCode       aGC1;
   private ActiveGCode       aGC2;
   private ActiveGCode       aGC3;
   private ActiveGCode       aGC4;
   private ActiveGCode       aGC5;
   private ActiveGCode       aGC6;
   private ActiveGCode       aGC7;
   private ActiveGCode       aGC8;
   private ActiveGCode       aGC9;
   private ActiveGCode       aGC10;
   private ActiveGCode       aGC11;
   private ActiveGCode       aGC12;
   private ActiveGCode       aGC13;
   private ActiveGCode       aGC14;
   private ActiveGCode       aGC15;
   private ActiveCode        aMC1;
   private ActiveCode        aMC2;
   private ActiveCode        aMC3;
   private ActiveCode        aMC4;
   private ActiveCode        aMC5;
   private ActiveCode        aMC6;
   private ActiveCode        aMC7;
   private static final long serialVersionUID = 1L;
}
