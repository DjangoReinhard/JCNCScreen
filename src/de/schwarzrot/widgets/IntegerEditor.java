package de.schwarzrot.widgets;
/* 
 * **************************************************************************
 * 
 *  file:       IntegerEditor.java
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

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


public class IntegerEditor extends DefaultCellEditor {
   public IntegerEditor(int min, int max) {
      super(new JFormattedTextField());
      tf           = (JFormattedTextField) getComponent();
      this.minimum = min;
      this.maximum = max;

      //      tf.setFont(settings.getGCodeFont());
      //      tf.setBackground(settings.getToolNumberBackground());
      intFormat    = NumberFormat.getIntegerInstance();
      NumberFormatter intFormatter = new NumberFormatter(intFormat);
      intFormatter.setFormat(intFormat);
      intFormatter.setMinimum(minimum);
      intFormatter.setMaximum(maximum);
      tf.setFormatterFactory(new DefaultFormatterFactory(intFormatter));
      tf.setValue(minimum);
      tf.setHorizontalAlignment(JTextField.TRAILING);
      tf.setFocusLostBehavior(JFormattedTextField.PERSIST);

      tf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
      tf.getActionMap().put("check", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
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

      if (o instanceof Integer) {
         return o;
      } else if (o instanceof Number) {
         return ((Number) o).intValue();
      } else {
         int v = 0;

         try {
            v = Integer.parseInt(o.toString());
            return v;
         } catch (Throwable t) {
            return null;
         }
      }
   }


   @Override
   public boolean stopCellEditing() {
      JFormattedTextField ftf = (JFormattedTextField) getComponent();

      if (ftf.isEditValid()) {
         try {
            ftf.commitEdit();
         } catch (ParseException e) {
         }
      } else {
         if (!userSaysRevert()) {
            return false;
         }
      }
      return super.stopCellEditing();
   }


   protected boolean userSaysRevert() {
      Toolkit.getDefaultToolkit().beep();
      tf.selectAll();
      Object[] options = { "Bearbeiten", "Rückgängig" };
      int      answer  = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(tf),
            "Der Wert muss eine Ganzzahl zwischen " + minimum + " und " + maximum + " sein.\n"
                  + "Der Wert kann entweder weiter bearbeitet, " + "oder zurückgesetzt werden.",
            "Ungültige Eingabe", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options,
            options[1]);

      if (answer == 1) { // Revert!
         tf.setValue(tf.getValue());

         return true;
      }
      return false;
   }


   private JFormattedTextField tf;
   private NumberFormat        intFormat;
   private int                 minimum;
   private int                 maximum;
   private static final long   serialVersionUID = 1L;
}
