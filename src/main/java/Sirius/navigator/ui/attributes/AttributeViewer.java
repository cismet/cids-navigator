/*
 * AttributeViewer.java
 *
 * Created on 1. Juli 2004, 13:42
 */
package Sirius.navigator.ui.attributes;

import Sirius.navigator.method.MethodManager;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import javax.swing.event.*;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.dnd.*;
import Sirius.navigator.ui.tree.MetaCatalogueTree;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import org.apache.log4j.Logger;
import Sirius.server.newuser.permission.PermissionHolder;
import java.util.Vector;
import javax.swing.AbstractButton;

/**
 *
 * @author  pascal
 */
public class AttributeViewer extends javax.swing.JPanel implements EmbededControlBar {

    private final ResourceManager resources;
    private Object treeNode = null;
    private final Logger logger;

    /** Creates new form AttributeViewer */
    public AttributeViewer() {
        this.resources = ResourceManager.getManager();
        this.logger = Logger.getLogger(this.getClass());

        initComponents();

        //this.attributeTree.setTransferHandler(new AttributeTree.MetaObjectTransferHandler());

        this.attributeTree.addTreeSelectionListener(new AttributeListener());
        this.attributeTree.setIgnoreInvisibleAttributes(true);
        AttributeNodeDnDHandler attributeNodeDnDHandler = new AttributeNodeDnDHandler(this.attributeTree);

    // XXX test ...
    // this.attributeTree.setIgnoreSubsitute(false);
    // this.attributeTree.setIgnoreArrayHelperObjects(false);
    }

    public void setTreeNode(Object treeNode) {
        this.treeNode = treeNode;
        this.attributeTable.clear();
        this.attributeTree.setTreeNode(treeNode);

        if (treeNode != null && treeNode instanceof ObjectTreeNode && PropertyManager.getManager().isEditable()) {
            this.editButton.setEnabled(true);

            MetaObject mo = ((ObjectTreeNode) treeNode).getMetaObject();
//            HashMap hm=new HashMap();
//            try {
//                hm.put(mo.getClassKey(),((ObjectTreeNode)treeNode).getMetaClass());
//            }
//            catch (Throwable t) {
//                logger.fatal(t,t);
//            }
        //logger.fatal(MetaObjectXMLSerializer.getInstance().MetaObjectToXML(mo,hm));
        } else {
            this.editButton.setEnabled(false);
        }



    }

    public void setControlBarVisible(boolean isVisible) {
        controlBar.setVisible(isVisible);
    }

    public Vector<AbstractButton> getControlBarButtons() {
        Vector<AbstractButton> buttons = new Vector<AbstractButton>();
        buttons.add(editButton);
        return buttons;
    }

    /**
     * Getter for property treeNode.
     * @return Value of property treeNode.
     */
    public Object getTreeNode() {
        return this.treeNode;
    }

    /**
     * Clears the table an the tree
     */
    public void clear() {
        this.attributeTree.clear();
        this.attributeTable.clear();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        controlBar = new javax.swing.JPanel();
        titleBar = new Sirius.navigator.ui.widget.TitleBar();
        editButton = new javax.swing.JButton();
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane();
        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane();
        attributeTable = new Sirius.navigator.ui.attributes.AttributeTable();
        javax.swing.JScrollPane treeScrollPane = new javax.swing.JScrollPane();
        attributeTree = new Sirius.navigator.ui.attributes.AttributeTree();

        setLayout(new java.awt.BorderLayout());

        controlBar.setLayout(new java.awt.GridBagLayout());

        titleBar.setIcon(resources.getIcon("floatingframe.gif"));
        titleBar.setTitle(resources.getString("attribute.viewer.title"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlBar.add(titleBar, gridBagConstraints);

        editButton.setIcon(resources.getIcon(resources.getString("attribute.viewer.edit.icon")));
        editButton.setToolTipText(resources.getString("attribute.viewer.edit.tooltip"));
        editButton.setActionCommand("edit");
        editButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editButton.setContentAreaFilled(false);
        editButton.setEnabled(false);
        editButton.setFocusPainted(false);
        editButton.setMaximumSize(new java.awt.Dimension(16, 16));
        editButton.setMinimumSize(new java.awt.Dimension(16, 16));
        editButton.setPreferredSize(new java.awt.Dimension(16, 16));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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

        attributeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
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
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editButtonActionPerformed
    {//GEN-HEADEREND:event_editButtonActionPerformed
        if (this.getTreeNode() != null && this.getTreeNode() instanceof ObjectTreeNode && ComponentRegistry.getRegistry().getAttributeEditor() != null) {
            ObjectTreeNode selectedNode = (ObjectTreeNode) this.getTreeNode();
            logger.info("evt.getModifiers():" + evt.getModifiers());

            MetaCatalogueTree metaCatalogueTree = ComponentRegistry.getRegistry().getCatalogueTree();

            if ((java.awt.event.ActionEvent.CTRL_MASK & evt.getModifiers()) > 0) {
                logger.warn("performing unauthorized edit action");
                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);
                if (ComponentRegistry.getRegistry().getActiveCatalogue() == metaCatalogueTree) {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(metaCatalogueTree.getSelectionPath(), selectedNode);
                } else {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(selectedNode);
                }

            } else if (MethodManager.getManager().checkPermission((MetaObjectNode) selectedNode.getNode(), PermissionHolder.WRITEPERMISSION)) {
                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);

                if (ComponentRegistry.getRegistry().getActiveCatalogue() == metaCatalogueTree) {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(metaCatalogueTree.getSelectionPath(), selectedNode);
                } else {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(selectedNode);
                }



            } else {
                logger.warn("insufficient permission to edit node " + selectedNode);
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Sirius.navigator.ui.attributes.AttributeTable attributeTable;
    private Sirius.navigator.ui.attributes.AttributeTree attributeTree;
    private javax.swing.JPanel controlBar;
    private javax.swing.JButton editButton;
    private Sirius.navigator.ui.widget.TitleBar titleBar;
    // End of variables declaration//GEN-END:variables

    private class AttributeListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            Object object = e.getPath().getLastPathComponent();

            if (e.isAddedPath() && object != null) {
                //AttributePanel.this.setAttributes(((AttributeNode)object).getMetaAttributes());
                attributeTable.setAttributes(((AttributeNode) object).getAttributes());
            } else {
                //AttributePanel.this.tableModel.clear();
                attributeTable.clear();
            }
        }
    }
}
