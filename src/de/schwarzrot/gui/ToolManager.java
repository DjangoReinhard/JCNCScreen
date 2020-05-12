package de.schwarzrot.gui;

/*
 * **************************************************************************
 *
 *  file:       ToolManager.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    10.11.2019 by Django Reinhard
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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.ToolCategory;
import de.schwarzrot.bean.ToolDefinition;
import de.schwarzrot.bean.ToolLibrary;
import de.schwarzrot.bean.ToolProfile;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.format.ToolDefTableFormat;
import de.schwarzrot.jar.JarInfo;
import de.schwarzrot.linuxcnc.data.CategoryInfo;
import de.schwarzrot.linuxcnc.data.LibInfo;
import de.schwarzrot.linuxcnc.data.ToolInfo;
import de.schwarzrot.linuxcnc.export.IExportHandler;
import de.schwarzrot.system.CommandWriter;
import de.schwarzrot.util.PropertyAccessor;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import layout.SpringUtilities;


public class ToolManager extends AbstractTreeView<ToolDefinition, ToolDefTableFormat>
      implements FocusListener, KeyListener {
   class CBProfileRenderer extends JLabel implements ListCellRenderer<ToolProfile> {
      @Override
      public Component getListCellRendererComponent(JList<? extends ToolProfile> list, ToolProfile value,
            int index, boolean isSelected, boolean cellHasFocus) {
         setText(LCStatus.getStatus().lm(value.name()));

         return this;
      }


      private static final long serialVersionUID = 1L;
   }

   class NewCategory extends JPanel {
      public NewCategory(Font f) {
         tfName = new JTextField(15);
         tfName.setFont(f);
         tfName.requestFocusInWindow();
         ToolProfile[] options = new ToolProfile[] { ToolProfile.BallNose, ToolProfile.BullNose,
               ToolProfile.Drill, ToolProfile.EndMill, ToolProfile.Lathe, ToolProfile.VCutter };
         cbProfile = new JComboBox<ToolProfile>(options);
         cbProfile.setPreferredSize(new Dimension(250, 35));
         cbProfile.setFont(f);
         cbProfile.setRenderer(new CBProfileRenderer());
         JLabel l1 = new JLabel("Name: ", JLabel.CENTER);
         JLabel l2 = new JLabel("Profil: ", JLabel.CENTER);

         l1.setFont(f);
         l2.setFont(f);
         setLayout(new SpringLayout());
         add(l1);
         add(tfName);
         add(l2);
         add(cbProfile);
         SpringUtilities.makeCompactGrid(this, 2, 2, 0, 0, 2, 2);
      }


      @Override
      public String getName() {
         return tfName.getText();
      }


      public ToolProfile getProfile() {
         return (ToolProfile) cbProfile.getSelectedItem();
      }


      private JTextField             tfName;
      private JComboBox<ToolProfile> cbProfile;
      private static final long      serialVersionUID = 1L;
   };


   public ToolManager(String title, CommandWriter cmdWriter) {
      this(title, LCStatus.getStatus().getToolLibrary(), cmdWriter);
   }


   public ToolManager(String title, ToolLibrary toolLib, CommandWriter cmdWriter) {
      super(title, toolLib.getRoot(), new ToolDefTableFormat());
      this.toolLibrary = toolLib;
      //      this.cmdWriter   = cmdWriter;
      editorIsActive   = false;
      paTool           = new PropertyAccessor(ToolDefinition.class);
   }


   @Override
   public void focusGained(FocusEvent e) {
      System.err.println(">>> focus gained ...: " + e.getComponent());
   }


   @Override
   public void focusLost(FocusEvent e) {
      System.err.println("<<< focus lost ...: " + e.getComponent());
      if (!editorIsActive) {
         System.err.println("<<< but editor is NOT active");
         return;
      }
      System.err.println("\topposite is: " + e.getOppositeComponent());
      System.out.println("\tEvent is " + (e.isTemporary() ? "temporary" : "static"));

      if (e.getComponent() instanceof JTextField) {
         if (!(e.getOppositeComponent() instanceof JTextField)) {
            entryFields[ToolName].requestFocus();
         }
      }
   }


   @Override
   public void keyPressed(KeyEvent e) {
   }


   @Override
   public void keyReleased(KeyEvent e) {
      // System.out.println("pressed Key with code: #" + e.getKeyCode());
      // F1 == 112 ... F12 == 123
      if (e.getKeyCode() == 121) {
         // F10 aka save Editor
         saveEditor();
         editorIsActive = false;
         getTableComponent().requestFocus();
      } else if (e.getKeyCode() == 119) {
         // F8 aka abort Editor
         abortEditor();
         editorIsActive = false;
         getTreeComponent().requestFocus();
         // } else if (e.getKeyCode() == 123) {
         // // F12 aka export tree
         // exportTree(toolLibrary);
      }
   }


   @Override
   public void keyTyped(KeyEvent e) {
   }


   @Override
   public void setBackground(Color bgCol) {
      super.setBackground(bgCol);
      if (toolEdit != null)
         toolEdit.setBackground(bgCol);
   }


   @Override
   public void setFont(Font f) {
      super.setFont(f);
      setEditorFont(new Font(f.getName(), f.getStyle(), f.getSize() - 2));
   }


   @SuppressWarnings("unchecked")
   @Override
   public void valueChanged(ListSelectionEvent e) {
      int n = getTableComponent().getSelectedRow();

      if (editorIsActive)
         return;
      if (n < 0)
         return;
      editorRow = getTableComponent().convertRowIndexToModel(n);

      try {
         bean2Edit = ((DefaultEventTableModel<ToolDefinition>) getTableComponent().getModel())
               .getElementAt(editorRow);
      } catch (IndexOutOfBoundsException ex) {
      }
      populateEditor(bean2Edit);
   }


   @Override
   public void valueChanged(TreeSelectionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();

      if (editorIsActive)
         return;
      if (node == null)
         return;
      List<ToolDefinition> list = null;
      Object               ud   = node.getUserObject();

      if (node.getChildCount() < 1 && ud instanceof ToolCategory) {
         list = ((ToolCategory) ud).getTools();
      } else {
         list = new ArrayList<ToolDefinition>();
         loadToolsFromNode(node, list);
      }
      reloadToolList(list);
   }


   protected void abortEditor() {
      populateEditor(null);
      getTreeComponent().requestFocus();
   }


   @Override
   protected void configureColumns(TableColumnModel m) {
      m.getColumn(0).setPreferredWidth(40);
      m.getColumn(1).setPreferredWidth(80);
      m.getColumn(2).setPreferredWidth(600);
      m.getColumn(3).setPreferredWidth(80);
      m.getColumn(4).setPreferredWidth(110);
      m.getColumn(5).setPreferredWidth(110);
   }


   protected JComponent createToolEditor() {
      JPanel p = null;

      if (toolEdit == null) {
         p           = new JPanel();
         entryFields = new JTextField[ToolDefinition.propertyNames.length + 1];
         prompts     = new JLabel[ToolDefinition.propertyNames.length + 1];
         LCStatus status = LCStatus.getStatus();

         for (int i = 0; i < ToolDefinition.propertyNames.length; ++i) {
            prompts[i] = new JLabel(status.lm(ToolDefinition.propertyNames[i]));
            if (i == 0) {
               entryFields[i] = new JTextField(5);
               entryFields[i].setFocusable(false);
            } else {
               entryFields[i] = new JTextField();
            }
            prompts[i].setFont(UITheme.getFont(getUIId() + ":editor.prompt.font"));
            prompts[i].setForeground(UITheme.getColor(getUIId() + ":editor.foreground"));
            entryFields[i].setFont(UITheme.getFont(getUIId() + ":editor.entry.font"));
            entryFields[i].addFocusListener(this);
            entryFields[i].addKeyListener(this);
         }
         p.setLayout(new GridBagLayout());
         p.setBackground(UITheme.getColor(getUIId() + ":editor.background"));
         GridBagConstraints c = new GridBagConstraints();

         c.fill      = GridBagConstraints.BOTH;
         c.insets    = new Insets(1, 4, 1, 1);
         c.gridx     = 0;
         c.gridy     = 0;
         c.weightx   = 0;
         c.weighty   = 0;
         c.gridwidth = 1;
         p.add(entryFields[ToolNumber], c);

         c.gridx     = 1;
         c.gridwidth = 4;
         c.weightx   = 0.875;
         p.add(entryFields[ToolName], c);

         // line 2
         c.gridx     = 0;
         c.gridy     = 1;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[Note], c);

         c.gridx     = 2;
         c.gridwidth = 3;
         c.weightx   = 0.75;
         p.add(entryFields[Note], c);

         // line 3
         c.gridx     = 0;
         c.gridy     = 2;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[Comment], c);

         c.gridx     = 2;
         c.gridwidth = 3;
         c.weightx   = 0.75;
         p.add(entryFields[Comment], c);

         // line 4
         c.gridx     = 0;
         c.gridy     = 3;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[Material], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[Material], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[Coating], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[Coating], c);

         // line 5
         c.gridx     = 0;
         c.gridy     = 4;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[ColletDiameter], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[ColletDiameter], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[ColletLength], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[ColletLength], c);

         // line 6
         c.gridx     = 0;
         c.gridy     = 5;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[ShankDiameter], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[ShankDiameter], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[FreeLength], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[FreeLength], c);

         // line 7
         c.gridx     = 0;
         c.gridy     = 6;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[SlopeAngle], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[SlopeAngle], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[Flutes], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[Flutes], c);

         // line 8
         c.gridx     = 0;
         c.gridy     = 7;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[FluteDiameter], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[FluteDiameter], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[FluteLength], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[FluteLength], c);

         // line 9
         c.gridx     = 0;
         c.gridy     = 8;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[CuttingRadius], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[CuttingRadius], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[CuttingLength], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[CuttingLength], c);

         // line 10
         c.gridx     = 0;
         c.gridy     = 9;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[TipDiameter], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[TipDiameter], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[CuttingAngle], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[CuttingAngle], c);

         // line 11
         c.gridx     = 0;
         c.gridy     = 10;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[PartCode], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[PartCode], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[ToothLoad], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[ToothLoad], c);

         // line 12
         c.gridx     = 0;
         c.gridy     = 11;
         c.gridwidth = 2;
         c.weightx   = 0;
         p.add(prompts[HelixAngle], c);

         c.gridx     = 2;
         c.gridwidth = 1;
         c.weightx   = 0.25;
         p.add(entryFields[HelixAngle], c);

         c.gridx   = 3;
         c.weightx = 0;
         p.add(prompts[MaxRampAngle], c);

         c.gridx   = 4;
         c.weightx = 0.25;
         p.add(entryFields[MaxRampAngle], c);
      } else {
         p = (JPanel) toolEdit;
      }
      return p;
   }


   @Override
   protected JComponent createUI(JComponent treeView, JComponent tableView) {
      toolEdit = createToolEditor();
      JSplitPane splitPane  = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableView, toolEdit);
      JSplitPane vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, splitPane);

      if (UITheme.getBoolean("Application:mode.portrait")) {
         splitPane.setDividerLocation(680);
      } else {
         splitPane.setDividerLocation(590);
      }
      splitPane.setDividerSize(1);
      vSplitPane.setDividerLocation(300);
      vSplitPane.setDividerSize(1);

      return vSplitPane;
   }


   protected void exportNode(IExportHandler exHdr, DefaultMutableTreeNode node) throws Exception {
      ToolCategory cat = (ToolCategory) node.getUserObject();
      CategoryInfo ci  = cat.toInfo();

      exHdr.openCategory(ci);
      for (ToolDefinition td : cat.getTools()) {
         if (!td.isSelected())
            continue;
         ToolInfo ti = td.toInfo();

         exHdr.openTool(ti);
         exHdr.closeTool(ti);
      }
      for (int i = 0; i < node.getChildCount(); ++i) {
         exportNode(exHdr, (DefaultMutableTreeNode) node.getChildAt(i));
      }
      exHdr.closeCategory(ci);
   }


   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected IExportHandler loadHandler(JarInfo jarInfo) {
      IExportHandler rv = null;
      StringBuilder  ub = new StringBuilder("file://");

      ub.append(jarInfo.getPath());
      URL url = null;

      try {
         url = new URL(ub.toString());
         URLClassLoader ucl     = URLClassLoader.newInstance(new URL[] { url });
         Class          ehClass = ucl.loadClass(jarInfo.getClassName());
         Constructor    c       = ehClass.getDeclaredConstructor(new Class[0]);
         Object         ni      = c.newInstance(new Object[0]);

         if (ni instanceof IExportHandler) {
            rv = (IExportHandler) ni;
         }
      } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException
            | InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e) {
         e.printStackTrace();
      }
      return rv;
   }


   protected void loadHandlerAndPerformExport(JarInfo jarInfo, String exportFileName, ToolLibrary lib) {
      IExportHandler exHdr = loadHandler(jarInfo);

      if (exHdr != null) {
         DefaultMutableTreeNode root = lib.getRoot();

         try {
            LibInfo li = lib.toInfo();

            exHdr.openLibrary(li, exportFileName);
            for (int i = 0; i < root.getChildCount(); ++i) {
               exportNode(exHdr, (DefaultMutableTreeNode) lib.getRoot().getChildAt(i));
            }
            exHdr.closeLibrary(li);
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
   }


   protected void loadToolsFromNode(DefaultMutableTreeNode n, List<ToolDefinition> l) {
      Iterator<TreeNode> children = n.children().asIterator();

      while (children.hasNext()) {
         DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) children.next();
         Object                 ud      = subNode.getUserObject();

         if (ud instanceof ToolCategory) {
            l.addAll(((ToolCategory) ud).getTools());
         }
         if (subNode.getChildCount() > 0)
            loadToolsFromNode(subNode, l);
      }
   }


   @Override
   protected void performDebug(ActionEvent ae) {
      Dimension s = getSize();

      System.out.println("Tool-Manager has size of: " + s.getWidth() + "x" + s.getHeight());
      s = toolEdit.getSize();
      System.out.println(" Tool-Editor has size of: " + s.getWidth() + "x" + s.getHeight());
   }


   @Override
   protected void performDelete4Table(ActionEvent e) {
   }


   @Override
   protected void performExport4Tree(ActionEvent e) {
      Map<String, JarInfo>       exportHandlers = LCStatus.getStatus().getApp().getExportHandlers();
      File                       exportTarget   = new File("/tmp", "tools.export");
      ExportHandlerSelectionPane p              = new ExportHandlerSelectionPane(exportTarget,
            exportHandlers);
      int                        rv             = LCStatus.getStatus().getApp()
            .xtendedInputDialog("exportieren mit ...", p);
      if (rv == JOptionPane.OK_OPTION) {
         JarInfo ji = p.getSelectedHandler();
         exportTarget = p.getSelectedFile();

         System.out.println("selected handler is: " + ji.getName());
         System.out.println("export target is: " + exportTarget.getAbsolutePath());
         loadHandlerAndPerformExport(ji, exportTarget.getAbsolutePath(), toolLibrary);
      }
   }


   @Override
   protected void performNew4Table(ActionEvent e) {
      System.err.println("perform new 4 table ...");
      bean2Edit = new ToolDefinition();
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();
      Object                 ud   = node.getUserObject();

      if (ud instanceof ToolCategory) {
         bean2Edit.setToolCategory((ToolCategory) ud);
         populateEditor(bean2Edit);
         performOnEnterAction(null);
      }
   }


   @Override
   protected void performNew4Tree(ActionEvent e) {
      System.err.println("\tperform new 4 tree ...");
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
            .getLastSelectedPathComponent();
      if (node == null)
         return;
      Object ud = node.getUserObject();

      if (dlgCat == null)
         dlgCat = new NewCategory(editorFont);
      int rv = JOptionPane.showConfirmDialog(SwingUtilities.windowForComponent(this), dlgCat, "Kategorie neu",
            JOptionPane.OK_CANCEL_OPTION);

      System.out.println("back from dialog setVisible ...");
      if (rv == JOptionPane.OK_OPTION) {
         ToolCategory c    = new ToolCategory(dlgCat.getProfile());
         String       name = dlgCat.getName();

         if (name != null && !name.isEmpty())
            c.setName(dlgCat.getName());

         System.out.println("name ..: " + dlgCat.getName());
         System.out.println("profile: " + dlgCat.getProfile());
         if (ud instanceof ToolCategory || ud instanceof String) {
            TreePath newPath = new TreePath(toolLibrary.addCategory(c).getPath());

            getTreeComponent().scrollPathToVisible(newPath);
            getTreeComponent().setSelectionPath(newPath);
            getTreeComponent().updateUI();
         }
      }
   }


   @Override
   protected void performOnEnterAction(ActionEvent e) {
      // System.err.println(" >>> This is an ENTER action!");
      editorIsActive = true;
      entryFields[ToolName].requestFocus();
   }


   @Override
   protected void performSelect4Table(ActionEvent e) {
      int sr = getTableComponent().getSelectedRow();
      int i  = getTableComponent().convertRowIndexToModel(sr);

      if (i < 0)
         return;
      @SuppressWarnings("unchecked")
      ToolDefinition td = ((DefaultEventTableModel<ToolDefinition>) getTableComponent().getModel())
            .getElementAt(i);

      td.setSelected(!td.isSelected());
      getTableComponent().updateUI();
   }


   protected void populateEditor(ToolDefinition td) {
      if (td == null) {
         for (int i = 0; i < ToolDefinition.propertyNames.length; ++i) {
            entryFields[i].setText(EmptyString);
         }
      } else {
         for (int i = 0; i < ToolDefinition.propertyNames.length; ++i) {
            Object value = paTool.getProperty(td, ToolDefinition.propertyNames[i]);

            if (value != null)
               entryFields[i].setText(value.toString());
            else
               entryFields[i].setText(EmptyString);
         }
      }
   }


   protected void reloadToolList(List<ToolDefinition> l) {
      if (l == null)
         return;
      EventList<ToolDefinition> tools = getTableList();
      tools.getReadWriteLock().writeLock().lock();
      tools.clear();
      tools.addAll(l);
      tools.getReadWriteLock().writeLock().unlock();
      getTableComponent().invalidate();
      getTableComponent().updateUI();
   }


   protected void saveEditor() {
      for (int i = 0; i < ToolDefinition.propertyNames.length; ++i) {
         System.out.println("save property >" + ToolDefinition.propertyNames[i] + "<");
         paTool.setProperty(bean2Edit, ToolDefinition.propertyNames[i], entryFields[i].getText());
      }
      if (bean2Edit.getToolNumber() == 0) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeComponent()
               .getLastSelectedPathComponent();
         Object                 ud   = node.getUserObject();

         if (ud instanceof ToolCategory) {
            toolLibrary.addTool(bean2Edit);
            reloadToolList(((ToolCategory) ud).getTools());
         }
      }
   }


   protected void setEditorFont(Font f) {
      editorFont = f;
      if (entryFields == null)
         return;
      for (int i = 0; i < ToolDefinition.propertyNames.length; ++i) {
         prompts[i].setFont(f);
         entryFields[i].setFont(f);
      }
   }


   private JTextField[]        entryFields;
   private JLabel[]            prompts;
   private JComponent          toolEdit;
   private boolean             editorIsActive;
   private NewCategory         dlgCat;
   private Font                editorFont;
   private int                 editorRow;
   private ToolDefinition      bean2Edit;
   protected ToolLibrary       toolLibrary;
   protected PropertyAccessor  paTool;
   private static final String EmptyString      = "";
   private static final int    ToolName         = 1;
   private static final int    ToolNumber       = 0;
   private static final int    ColletDiameter   = 2;
   private static final int    ColletLength     = 3;
   private static final int    ShankDiameter    = 4;
   private static final int    FreeLength       = 5;
   private static final int    SlopeAngle       = 6;
   private static final int    Flutes           = 7;
   private static final int    FluteDiameter    = 8;
   private static final int    FluteLength      = 9;
   private static final int    CuttingRadius    = 10;
   private static final int    CuttingLength    = 11;
   private static final int    TipDiameter      = 13;
   private static final int    CuttingAngle     = 12;
   private static final int    PartCode         = 14;
   private static final int    Material         = 15;
   private static final int    Coating          = 16;
   private static final int    ToothLoad        = 17;
   private static final int    HelixAngle       = 18;
   private static final int    MaxRampAngle     = 19;
   private static final int    Comment          = 20;
   private static final int    Note             = 21;
   private static final long   serialVersionUID = 1L;
}
