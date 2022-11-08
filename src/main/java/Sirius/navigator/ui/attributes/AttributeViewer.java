/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeViewer.java
 *
 * Created on 1. Juli 2004, 13:42
 */
package Sirius.navigator.ui.attributes;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import Sirius.navigator.resource.*;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.dnd.*;
import Sirius.navigator.ui.tree.MetaCatalogueTree;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.newuser.permission.PermissionHolder;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingWorker;
import javax.swing.event.*;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.tools.gui.GUIWindow;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = GUIWindow.class)
public class AttributeViewer extends javax.swing.JPanel implements EmbededControlBar, GUIWindow {

    //~ Instance fields --------------------------------------------------------

    protected SwingWorker worker = null;

    private final ResourceManager resources = ResourceManager.getManager();
    private Object treeNode = null;
    private final Logger logger;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Sirius.navigator.ui.attributes.AttributeTable attributeTable;
    private Sirius.navigator.ui.attributes.AttributeTree attributeTree;
    private javax.swing.JPanel controlBar;
    private javax.swing.JButton editButton;
    private Sirius.navigator.ui.widget.TitleBar titleBar;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form AttributeViewer.
     */
    public AttributeViewer() {
//        this.resources = ResourceManager.getManager();
        this.logger = Logger.getLogger(this.getClass());

        initComponents();

        // this.attributeTree.setTransferHandler(new AttributeTree.MetaObjectTransferHandler());

        this.attributeTree.addTreeSelectionListener(new AttributeListener());
        this.attributeTree.setIgnoreInvisibleAttributes(true);
        final AttributeNodeDnDHandler attributeNodeDnDHandler = new AttributeNodeDnDHandler(this.attributeTree);

        // XXX test ...
        // this.attributeTree.setIgnoreSubsitute(false);
        // this.attributeTree.setIgnoreArrayHelperObjects(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  treeNode  DOCUMENT ME!
     */
    public void setTreeNodes(final List<Object> treeNode) {
        if ((treeNode != null) && (treeNode.size() == 1)) {
            setTreeNode(treeNode.get(0));
        } else {
            clear();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  treeNode  DOCUMENT ME!
     */
    public void setTreeNode(final Object treeNode) {
        if (logger.isDebugEnabled()) {
            logger.debug("setTreeNode: " + ((treeNode != null) ? treeNode.hashCode() : " null"));
        }
        editButton.setEnabled(false);
        if ((worker != null) && !worker.isDone() && !worker.isCancelled()) {
            logger.warn("cancelling running getMetaObject worker thread of tree node "
                        + ((treeNode != null) ? treeNode.hashCode() : " null"));
            worker.cancel(false);
            worker = null;
        }

        this.treeNode = treeNode;
        this.attributeTable.clear();
        this.attributeTree.setTreeNode(treeNode);

        if ((treeNode != null) && PropertyManager.getManager().isEditable()
                    && (treeNode instanceof ObjectTreeNode)) {
            final ObjectTreeNode objectTreeNode = (ObjectTreeNode)treeNode;
            if (objectTreeNode.isMetaObjectFilled()) {
                this.editButton.setEnabled(CidsBean.checkWritePermission(
                        SessionManager.getSession().getUser(),
                        objectTreeNode.getMetaObject().getBean()));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("starting getMetaObject worker thread for tree node " + treeNode.hashCode());
                }
                worker = new SwingWorker<MetaObject, Void>() {

                        @Override
                        protected MetaObject doInBackground() throws Exception {
                            return objectTreeNode.getMetaObject();
                        }

                        @Override
                        protected void done() {
                            if (logger.isDebugEnabled()) {
                                logger.debug("MetaObject loaded from server for tree node " + treeNode.hashCode());
                            }
                            try {
                                final MetaObject metaObject = this.get();

                                if (!isCancelled()) {
                                    editButton.setEnabled(CidsBean.checkWritePermission(
                                            SessionManager.getSession().getUser(),
                                            metaObject.getBean()));
                                } else {
                                    logger.warn("getMetaObject worker cancelled for tree node " + treeNode.hashCode());
                                }
                            } catch (InterruptedException ex) {
                                logger.warn(ex.getMessage(), ex);
                            } catch (ExecutionException ex) {
                                logger.error(ex.getMessage(), ex);
                                editButton.setEnabled(false);
                            } catch (CancellationException cex) {
                                if (logger.isDebugEnabled()) {
                                    logger.warn("getMetaObject worker thread forcibly cancelled for tree node:"
                                                + treeNode.hashCode() + ": " + cex.getMessage(),
                                        cex);
                                }
                            }
                        }
                    };
                worker.execute();
            }
        } else {
            this.editButton.setEnabled(false);
        }
    }

    @Override
    public void setControlBarVisible(final boolean isVisible) {
        controlBar.setVisible(isVisible);
    }

    @Override
    public Vector<AbstractButton> getControlBarButtons() {
        final Vector<AbstractButton> buttons = new Vector<AbstractButton>();
        buttons.add(editButton);
        return buttons;
    }

    /**
     * Getter for property treeNode.
     *
     * @return  Value of property treeNode.
     */
    public Object getTreeNode() {
        return this.treeNode;
    }

    /**
     * Clears the table an the tree.
     */
    public void clear() {
        this.attributeTree.clear();
        this.attributeTable.clear();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        controlBar = new javax.swing.JPanel();
        titleBar = new Sirius.navigator.ui.widget.TitleBar();
        editButton = new javax.swing.JButton();
        final javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane();
        final javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane();
        attributeTable = new Sirius.navigator.ui.attributes.AttributeTable();
        final javax.swing.JScrollPane treeScrollPane = new javax.swing.JScrollPane();
        attributeTree = new Sirius.navigator.ui.attributes.AttributeTree();

        setLayout(new java.awt.BorderLayout());

        controlBar.setLayout(new java.awt.GridBagLayout());

        titleBar.setIcon(resources.getIcon("floatingframe.gif"));
        titleBar.setTitle(org.openide.util.NbBundle.getMessage(
                AttributeViewer.class,
                "AttributeViewer.titleBar.title")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlBar.add(titleBar, gridBagConstraints);

        editButton.setIcon(resources.getIcon("objekt_bearbeiten.gif"));
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                AttributeViewer.class,
                "AttributeViewer.editButton.toolTipText")); // NOI18N
        editButton.setActionCommand("edit");
        editButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editButton.setContentAreaFilled(false);
        editButton.setEnabled(false);
        editButton.setFocusPainted(false);
        editButton.setMaximumSize(new java.awt.Dimension(16, 16));
        editButton.setMinimumSize(new java.awt.Dimension(16, 16));
        editButton.setPreferredSize(new java.awt.Dimension(16, 16));
        editButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    editButtonActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        controlBar.add(editButton, gridBagConstraints);

        add(controlBar, java.awt.BorderLayout.NORTH);

        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(PropertyManager.getManager().isAdvancedLayout());

        tableScrollPane.setPreferredSize(new java.awt.Dimension(250, 150));

        attributeTable.setMinimumSize(new java.awt.Dimension(100, 50));
        attributeTable.setPreferredSize(null);
        tableScrollPane.setViewportView(attributeTable);

        splitPane.setTopComponent(tableScrollPane);

        treeScrollPane.setPreferredSize(new java.awt.Dimension(250, 150));
        treeScrollPane.setRequestFocusEnabled(false);

        attributeTree.setMaximumSize(null);
        attributeTree.setMinimumSize(new java.awt.Dimension(100, 50));
        attributeTree.setPreferredSize(null);
        treeScrollPane.setViewportView(attributeTree);

        splitPane.setBottomComponent(treeScrollPane);

        add(splitPane, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void editButtonActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_editButtonActionPerformed
    {                                                                            //GEN-HEADEREND:event_editButtonActionPerformed
        if ((this.getTreeNode() != null) && (this.getTreeNode() instanceof ObjectTreeNode)
                    && (ComponentRegistry.getRegistry().getAttributeEditor() != null)) {
            final ObjectTreeNode selectedNode = (ObjectTreeNode)this.getTreeNode();
            logger.info("evt.getModifiers():" + evt.getModifiers());             // NOI18N

            final MetaCatalogueTree metaCatalogueTree = ComponentRegistry.getRegistry().getCatalogueTree();

            if (MethodManager.getManager().checkPermission(
                            (MetaObjectNode)selectedNode.getNode(),
                            PermissionHolder.WRITEPERMISSION)) {
                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);

                if (ComponentRegistry.getRegistry().getActiveCatalogue() == metaCatalogueTree) {
                    ComponentRegistry.getRegistry()
                            .getAttributeEditor()
                            .setTreeNode(metaCatalogueTree.getSelectionPath(), selectedNode);
                } else {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(selectedNode);
                }
            } else {
                logger.warn("insufficient permission to edit node " + selectedNode); // NOI18N
            }
        }
    }                                                                                //GEN-LAST:event_editButtonActionPerformed

    @Override
    public JComponent getGuiComponent() {
        return this;
    }

    @Override
    public String getPermissionString() {
        return GUIWindow.NO_PERMISSION;
    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public Icon getViewIcon() {
        return null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class AttributeListener implements TreeSelectionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void valueChanged(final TreeSelectionEvent e) {
            final Object object = e.getPath().getLastPathComponent();

            if (e.isAddedPath() && (object != null)) {
                // AttributePanel.this.setAttributes(((AttributeNode)object).getMetaAttributes());
                attributeTable.setAttributes(((AttributeNode)object).getAttributes());
            } else {
                // AttributePanel.this.tableModel.clear();
                attributeTable.clear();
            }
        }
    }
}
