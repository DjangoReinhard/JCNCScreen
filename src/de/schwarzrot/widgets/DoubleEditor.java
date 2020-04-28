package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       DoubleEditor.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines                           
 *  created:    6.10.2019 by Django Reinhard
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


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


public class DoubleEditor extends DefaultCellEditor {
   public DoubleEditor(double min, double max) {
      super(new JFormattedTextField());
      tf           = (JFormattedTextField) getComponent();
      this.minimum = min;
      this.maximum = max;

      //      tf.setFont(settings.getGCodeFont());
      //      tf.setBackground(settings.getToolNumberBackground());
      doubleFormat = NumberFormat.getNumberInstance(Locale.US);
      NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
      doubleFormatter.setFormat(doubleFormat);
      doubleFormatter.setMinimum(minimum);
      doubleFormatter.setMaximum(maximum);
      tf.setFormatterFactory(new DefaultFormatterFactory(doubleFormatter));
      tf.setValue(minimum);
      tf.setHorizontalAlignment(JTextField.TRAILING);
      tf.setFocusLostBehavior(JFormattedTextField.PERSIST);

      tf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
      tf.getActionMap().put("check", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // System.out.println("actionPerformed ...");
            if (!tf.isEditValid()) {
               if (userSaysRevert()) {
                  tf.postActionEvent();
               }
            } else
               try {
                  tf.commitEdit();
                  tf.postActionEvent();
               } catch (java.text.ParseException pe) {
               }
         }
      });
   }


   @Override
   public Object getCellEditorValue() {
      JFormattedTextField ftf = (JFormattedTextField) getComponent();
      Object              o   = ftf.getValue();

      System.out.println("getCellEditorValue(" + o + ") - instance of " + o.getClass().getName());
      if (o instanceof Double) {
         return o;
      } else if (o instanceof Number) {
         return ((Number) o).doubleValue();
      } else {
         double v = 0;

         try {
            v = doubleFormat.parse(o.toString()).doubleValue();
            return v;
         } catch (Throwable t) {
            return null;
         }
      }
   }


   @Override
   public boolean stopCellEditing() {
      JFormattedTextField ftf = (JFormattedTextField) getComponent();
      Object              o   = ftf.getValue();

      System.out.println("stopCellEditing(" + o + ") ...");
      if (ftf.isEditValid()) {
         try {
            ftf.commitEdit();
         } catch (ParseException e) {
         }
      } else if (!userSaysRevert()) {
         System.out.println("continue editing? ...");

         return false;
      }
      return super.stopCellEditing();
   }


   protected boolean userSaysRevert() {
      Toolkit.getDefaultToolkit().beep();
      tf.selectAll();
      Object[] options = { "Bearbeiten", "Rückgängig" };
      int      answer  = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(tf),
            "Die Eingabe muss eine Zahl zwischen " + minimum + " und " + maximum + " sein.\n"
                  + "Der Wert kann entweder weiter bearbeitet, " + "oder zurückgesetzt werden.",
            "Ungültige Eingabe", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options,
            options[1]);

      if (answer == 1) { // Revert!
         Object v = tf.getValue();

         System.out.println("revert ... (v = " + v + ") -> " + v.getClass().getName());

         tf.setValue(v);

         return true;
      }
      return false;
   }


   private JFormattedTextField tf;
   private NumberFormat        doubleFormat;
   private double              minimum;
   private double              maximum;
   private static final long   serialVersionUID = 1L;
}
