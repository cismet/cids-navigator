/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginTree.java
 *
 * Created on 11. Mai 2003, 16:49
 */
package Sirius.navigator.plugin.ui.manager;

import Sirius.navigator.plugin.*;
import Sirius.navigator.resource.*;

import java.awt.*;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   Peter Alzheimer
 * @version  $Revision$, $Date$
 */
public class PluginTree extends JTree {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager resources = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final PluginTreeNode rootNode;

    /** Holds value of property initialized. (lazy initialization) */
    private boolean initialized;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginTree.
     */
    public PluginTree() {
        super();

        rootNode = new PluginTreeNode();
        this.setModel(new DefaultTreeModel(rootNode));
        this.setCellRenderer(new PluginTreeNodeRenderer());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setEditable(false);
        this.setRootVisible(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void init() {
        rootNode.removeAllChildren();

        final Iterator iterator = PluginRegistry.getRegistry().getPluginDescriptors();
        while (iterator.hasNext()) {
            rootNode.add(new PluginTreeNode((PluginDescriptor)iterator.next()));
        }

        if (SwingUtilities.isEventDispatchThread()) {
            ((DefaultTreeModel)this.getModel()).nodeStructureChanged(rootNode);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        ((DefaultTreeModel)PluginTree.this.getModel()).nodeStructureChanged(rootNode);
                    }
                });
        }

        this.setInitialized(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PluginTreeNode getSelectedNode() {
        Object object = null;
        final TreePath treePath = this.getSelectionPath();

        if ((treePath != null) && ((object = treePath.getLastPathComponent()) != null)) {
            return (PluginTreeNode)object;
        }

        return null;
    }

    /**
     * Getter for property initialized.
     *
     * @return  Value of property initialized.
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Setter for property initialized.
     *
     * @param  initialized  New value of property initialized.
     */
    private void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class PluginTreeNodeRenderer extends DefaultTreeCellRenderer {

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getTreeCellRendererComponent(final JTree tree,
                final java.lang.Object value,
                final boolean selected,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (value instanceof PluginTreeNode) {
                this.setIcon(((PluginTreeNode)value).getIcon());
            }

            return this;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class PluginTreeNode extends DefaultMutableTreeNode {

        //~ Instance fields ----------------------------------------------------

        private final Icon icon;
        private final PluginDescriptor pluginDescriptor;
        private final PluginMethodDescriptor methodDescriptor;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PluginTreeNode object.
         */
        public PluginTreeNode() {
            super(org.openide.util.NbBundle.getMessage(PluginTree.class, "PluginTree.PluginTreeNode.rootNode")); // NOI18N
            this.pluginDescriptor = null;
            this.methodDescriptor = null;
            this.icon = resources.getIcon("plugin_node_root.gif");
        }

        /**
         * Creates a new PluginTreeNode object.
         *
         * @param  pluginDescriptor  DOCUMENT ME!
         */
        public PluginTreeNode(final PluginDescriptor pluginDescriptor) {
            super(pluginDescriptor.getName());
            this.pluginDescriptor = pluginDescriptor;
            this.methodDescriptor = null;
            this.icon = resources.getIcon("plugin_node_plugin.gif");

            if (pluginDescriptor.isPluginMethodsAvailable()) {
                this.addMethods();
            }
        }

        /**
         * Creates a new PluginTreeNode object.
         *
         * @param  methodDescriptor  DOCUMENT ME!
         */
        public PluginTreeNode(final PluginMethodDescriptor methodDescriptor) {
            super(methodDescriptor.getName());
            this.pluginDescriptor = null;
            this.methodDescriptor = methodDescriptor;
            this.icon = resources.getIcon("plugin_node_method.gif");
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        protected void addMethods() {
            final Iterator iterator = this.pluginDescriptor.getMethodDescriptors();
            while (iterator.hasNext()) {
                this.add(new PluginTreeNode((PluginMethodDescriptor)iterator.next()));
            }
        }

        // .....................................................................

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Icon getIcon() {
            return this.icon;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isPluginNode() {
            return (this.pluginDescriptor != null) ? true : false;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isPluginMethodNode() {
            return (this.methodDescriptor != null) ? true : false;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public PluginDescriptor getPluginDescriptor() {
            return this.pluginDescriptor;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public PluginMethodDescriptor getPluginMethodDescriptor() {
            return this.methodDescriptor;
        }
    }
}
