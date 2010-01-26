/*
 * AttributeEditor.java
 *
 * Created on 1. Juli 2004, 13:42
 */
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.DescriptionPane;
import Sirius.navigator.ui.attributes.AttributeViewer;
import Sirius.navigator.ui.attributes.editor.AttributeEditor;
import Sirius.navigator.ui.tree.MetaCatalogueTree;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.newuser.User;
import de.cismet.tools.CismetThreadPool;
import de.cismet.tools.StaticDebuggingTools;
import de.cismet.tools.gui.ComponentWrapper;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.beanutils.BeanUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 *
 * @author  pascal
 */
public class NavigatorAttributeEditorGui extends AttributeEditor {
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Object treeNode = null;
    private MetaObject backupObject = null;
    private MetaObject editorObject = null;
    private final ResourceManager resources;
    private JComponent wrappedWaitingPanel;

    /** Creates new form AttributeEditor */
    public NavigatorAttributeEditorGui() {
        this.resources = ResourceManager.getManager();
        initComponents();
        if (!StaticDebuggingTools.checkHomeForFile("cidsNavigatorGuiHiddenDebugControls")) {
            panDebug.setVisible(false);
        }
        ComponentWrapper cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
        if (cw != null) {
            wrappedWaitingPanel = (JComponent) cw.wrapComponent(lblEditorCreation);
        }

        //Nur fuer die Uebergangsphase sollange noch von AttributeEditor geerbt wird
        ActionListener[] alr = commitButton.getActionListeners();
        for (ActionListener al : alr) {
            commitButton.removeActionListener(al);
        }
        alr = cancelButton.getActionListeners();
        for (ActionListener al : alr) {
            cancelButton.removeActionListener(al);
        }

        commitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (backupObject != null && !(backupObject.propertyEquals(editorObject)) || backupObject == null) {
                    int answer = JOptionPane.showConfirmDialog(NavigatorAttributeEditorGui.this, I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.commitButtonActionListener.JOptionPane.message"), I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.commitButtonActionListener.JOptionPane.title"), JOptionPane.YES_NO_CANCEL_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        saveIt();


                    } else if (answer == JOptionPane.CANCEL_OPTION) {
                    } else {
                        reloadFromDB();
                        clear();
                    }
                } else {
                    clear();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (backupObject != null && !(backupObject.propertyEquals(editorObject)) || backupObject == null) {
                    int answer = JOptionPane.showConfirmDialog(NavigatorAttributeEditorGui.this, I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.cancelButtonActionListener.JOptionPane.message"), I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.cancelButtonActionListener.JOptionPane.title"), JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        reloadFromDB();
                        clear();
                    }
                } else {
                    clear();
                }

            }
        });
    }

    @Override
    public void setControlBarVisible(boolean isVisible) {
        controlBar.setVisible(isVisible);
    }

    @Override
    public Vector<AbstractButton> getControlBarButtons() {
        Vector<AbstractButton> buttons = new Vector<AbstractButton>();
        buttons.add(commitButton);
        buttons.add(cancelButton);
        return buttons;
    }

    private void refreshAttributeTable() {
        AttributeViewer viewer = ComponentRegistry.getRegistry().getAttributeViewer();
        Object node = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNode();
        viewer.setTreeNode(node);
    }

    private void refreshTree() {
        if (treePath != null) {
            try {
                MetaCatalogueTree metaCatalogueTree = ComponentRegistry.getRegistry().getCatalogueTree();

                RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());

                ((DefaultTreeModel) metaCatalogueTree.getModel()).setRoot(rootTreeNode);
                ((DefaultTreeModel) metaCatalogueTree.getModel()).reload();

                metaCatalogueTree.exploreSubtree(treePath);
            } catch (Exception e) {
                log.error("Error when refreshing Tree", e);
            }
        }
    }

    private void createBackup(MetaObject orig) {
        try {
            int oid = orig.getID();
            int cid = orig.getMetaClass().getID();
            String domain = orig.getDomain();
            User user = SessionManager.getSession().getUser();
            backupObject = SessionManager.getConnection().getMetaObject(user, oid, cid, domain);
        } catch (Exception e) {
            log.error("Error during Backupcreation. Cannot detect whether the objects is changed.", e);
        }
    }

    private void reloadFromDB() {
        try {
            ObjectTreeNode otn = (ObjectTreeNode) treeNode;
            int oid = otn.getMetaObject().getID();

            if (oid == -1) {
                return;
            }

            int cid = otn.getMetaObject().getMetaClass().getID();
            String domain = otn.getMetaObject().getDomain();
            User user = SessionManager.getSession().getUser();


            MetaObject reloaded = SessionManager.getConnection().getMetaObject(user, oid, cid, domain);
            reloaded.setAllClasses();
            log.debug("Reloaded MO:" + reloaded.getDebugString());
            otn.setMetaObject(reloaded);

            refreshAttributeTable();
        } catch (Exception e) {
            log.error("Error durig reload from DB.", e);
            ErrorInfo ei = new ErrorInfo(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.reloadFromDB().ErrorInfo.title"),
                    I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.reloadFromDB().ErrorInfo.message"),
                    null, null, e, Level.SEVERE, null);
            JXErrorPane.showDialog(this, ei);
        }
    }

    private void saveIt() {
        ObjectTreeNode otn = (ObjectTreeNode) treeNode;
        MetaObject mo = otn.getMetaObject();
        try {
            mo.getBean().persist();
            refreshTree();
            JOptionPane jop = new JOptionPane(
                    I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.saveIt().successInfo.message"),
                    JOptionPane.INFORMATION_MESSAGE);

            final JDialog dialog = jop.createDialog(NavigatorAttributeEditorGui.this,
                    I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.saveIt().successInfo.title"));

            Timer t = new Timer(900, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();

                }
            });
            t.setRepeats(false);
            t.start();
            dialog.setVisible(true);
            clear();
        } catch (Exception ex) {
            log.error("Error while saving", ex);
            ErrorInfo ei = new ErrorInfo(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.saveIt().ErrorInfo.title"),
                    I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.saveIt().ErrorInfo.message"),
                    null, null, ex, Level.SEVERE, null);
            JXErrorPane.showDialog(NavigatorAttributeEditorGui.this, ei);
        }
        clear();
    }

    @Override
    public void setTreeNode(Object node) {
        if (treeNode != null && editorObject != null && backupObject != null) {
            if (!editorObject.propertyEquals(backupObject)) {
                int answer = JOptionPane.showConfirmDialog(NavigatorAttributeEditorGui.this,
                        I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.setTreeNode().confirmDialog.message"),
                        I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.setTreeNode().confirmDialog.title"),
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    saveIt();
                } else if (answer == JOptionPane.NO_OPTION) {
                    reloadFromDB();
                } else { //Cancel
                    return;
                }
            }
        }
        final AttributeViewer viewer = ComponentRegistry.getRegistry().getAttributeViewer();
        final MetaCatalogueTree tree = ComponentRegistry.getRegistry().getActiveCatalogue();
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                scpEditor.getViewport().removeAll();
                scpEditor.getViewport().setView(wrappedWaitingPanel);
                NavigatorAttributeEditorGui.this.revalidate();
            }
        });
        treeNode = node;
        if (treeNode instanceof ObjectTreeNode) {
            final DescriptionPane desc = ComponentRegistry.getRegistry().getDescriptionPane();
            CismetThreadPool.execute(new SwingWorker<JComponent, Void>() {

                @Override
                protected JComponent doInBackground() throws Exception {
                    ObjectTreeNode otn = (ObjectTreeNode) treeNode;
                    editorObject = otn.getMetaObject();
                    createBackup(editorObject);

                    editorObject.getBean().addPropertyChangeListener(new PropertyChangeListener() {

                        public void propertyChange(PropertyChangeEvent evt) {
                            viewer.repaint();
                            tree.repaint();
                            //commitButton.setEnabled(!backupObject.propertyEquals(mo)); //vielleicht einfach zuviel des guten

                        }
                    });
                    JComponent ed = CidsObjectEditorFactory.getInstance().getEditor(editorObject);
                    return ed;
                }

                @Override
                protected void done() {
                    try {
                        JComponent ed = get();
                        log.debug("editor:" + ed);
                        scpEditor.getViewport().removeAll();
                        scpEditor.getViewport().setView(ed);
                        NavigatorAttributeEditorGui.this.revalidate();
                        log.debug("editor added");
                        cancelButton.setEnabled(true);
                        commitButton.setEnabled(true);
                    } catch (Exception e) {
                        ErrorInfo ei = new ErrorInfo(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.done().ErrorInfo.title"),
                                I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.done().ErrorInfo.message"),
                                null, null, e, Level.SEVERE, null);
                        log.error("Error while displaying Editor" + ei.getState() + editorObject.getDebugString() + "\n", e);
                        JXErrorPane.showDialog(NavigatorAttributeEditorGui.this, ei);
                        clear();
                    }
                }
            });

        } else {
            log.warn("Given Treenode is not instance of ObjectTreeMode, but: " + node.getClass());
        }
    }

    @Override
    protected void clear() {
        scpEditor.getViewport().removeAll();
        commitButton.setEnabled(false);
        cancelButton.setEnabled(false);
        revalidate();
        repaint();
        ComponentRegistry.getRegistry().getAttributeViewer().repaint();
        this.treeNode = null;
        backupObject = null;
        editorObject = null;
    }

    public Object getTreeNode() {
        return treeNode;
    }

    public boolean isChanged() {

        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JToggleButton pinButton = new javax.swing.JToggleButton();
        javax.swing.JToggleButton editButton = new javax.swing.JToggleButton();
        editorScrollPane = new javax.swing.JScrollPane();
        lblEditorCreation = new javax.swing.JLabel();
        controlBar = new javax.swing.JPanel();
        commitButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        switchPanel = new javax.swing.JPanel();
        scpEditor = new javax.swing.JScrollPane();
        panDebug = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        pinButton.setIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.pinButton.icon")));
        pinButton.setToolTipText(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.pinButton.tooltip"));
        pinButton.setActionCommand("pin");
        pinButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pinButton.setContentAreaFilled(false);
        pinButton.setFocusPainted(false);
        pinButton.setMaximumSize(new java.awt.Dimension(16, 16));
        pinButton.setMinimumSize(new java.awt.Dimension(16, 16));
        pinButton.setPreferredSize(new java.awt.Dimension(16, 16));
        pinButton.setRolloverIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.pinButton.rolloverIcon")));
        pinButton.setRolloverSelectedIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.pinButton.rolloverSelectedIcon")));
        pinButton.setSelectedIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.pinButton.selectedIcon")));

        editButton.setIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.editButton.icon")));
        editButton.setToolTipText(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.editButton.tooltip"));
        editButton.setActionCommand("edit");
        editButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editButton.setContentAreaFilled(false);
        editButton.setFocusPainted(false);
        editButton.setMaximumSize(new java.awt.Dimension(16, 16));
        editButton.setMinimumSize(new java.awt.Dimension(16, 16));
        editButton.setPreferredSize(new java.awt.Dimension(16, 16));
        editButton.setRolloverIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.editButton.rolloverIcon")));
        editButton.setRolloverSelectedIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.editButton.rolloverSelectedIcon")));
        editButton.setSelectedIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.editButton.selectedIcon")));

        editorScrollPane.setPreferredSize(new java.awt.Dimension(250, 150));

        lblEditorCreation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEditorCreation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sirius/navigator/resource/img/load.png"))); // NOI18N

        setLayout(new java.awt.BorderLayout());

        controlBar.setLayout(new java.awt.GridBagLayout());

        commitButton.setIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.commitButton.icon")));
        commitButton.setToolTipText(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.commitButton.tooltip"));
        commitButton.setActionCommand("commit");
        commitButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        commitButton.setContentAreaFilled(false);
        commitButton.setEnabled(false);
        commitButton.setFocusPainted(false);
        commitButton.setMaximumSize(new java.awt.Dimension(16, 16));
        commitButton.setMinimumSize(new java.awt.Dimension(16, 16));
        commitButton.setPreferredSize(new java.awt.Dimension(16, 16));
        commitButton.setRolloverIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.commitButton.rolloverIcon")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        controlBar.add(commitButton, gridBagConstraints);

        cancelButton.setIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.cancelButton.icon")));
        cancelButton.setToolTipText(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.cancelButton.tooltip"));
        cancelButton.setActionCommand("cancel");
        cancelButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cancelButton.setContentAreaFilled(false);
        cancelButton.setEnabled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setMaximumSize(new java.awt.Dimension(16, 16));
        cancelButton.setMinimumSize(new java.awt.Dimension(16, 16));
        cancelButton.setPreferredSize(new java.awt.Dimension(16, 16));
        cancelButton.setRolloverIcon(resources.getIcon(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.cancelButton.rolloverIcon")));
        controlBar.add(cancelButton, new java.awt.GridBagConstraints());

        add(controlBar, java.awt.BorderLayout.NORTH);

        switchPanel.setLayout(new java.awt.BorderLayout());
        switchPanel.add(scpEditor, java.awt.BorderLayout.CENTER);

        panDebug.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText(I18N.getString("de.cismet.cids.editors.NavigatorAttributeEditorGui.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panDebug.add(jButton1);

        switchPanel.add(panDebug, java.awt.BorderLayout.NORTH);

        add(switchPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (getTreeNode() != null && getTreeNode() instanceof ObjectTreeNode) {
            MetaObject mo = ((ObjectTreeNode) getTreeNode()).getMetaObject();
            log.fatal("Current MetaObject:" + mo.getDebugString());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton commitButton;
    private javax.swing.JPanel controlBar;
    private javax.swing.JScrollPane editorScrollPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lblEditorCreation;
    private javax.swing.JPanel panDebug;
    private javax.swing.JScrollPane scpEditor;
    private javax.swing.JPanel switchPanel;
    // End of variables declaration//GEN-END:variables
}

