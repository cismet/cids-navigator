/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.types.iterator.TreeNodeIterator;
import Sirius.navigator.types.iterator.TreeNodeRestriction;
import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.PureTreeNode;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.types.treenode.TreeNodeLoader;
import Sirius.navigator.ui.widget.IconCheckBox;

import Sirius.server.middleware.types.MetaClassNode;
import Sirius.server.middleware.types.MetaNode;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ConnectionContext;
import de.cismet.cids.server.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class SearchSelectionTree extends MetaCatalogueTree {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(SearchSelectionTree.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchSelectionTree object.
     *
     * @param  rootNodes          DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public SearchSelectionTree(final Node[] rootNodes, final ClientConnectionContext connectionContext) {
        this(new RootTreeNode(rootNodes, new SelectionTreeNodeLoader(connectionContext), connectionContext),
            connectionContext);
    }

    /**
     * Creates a new SearchSelectionTree object.
     *
     * @param  rootNode           DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public SearchSelectionTree(final RootTreeNode rootNode, final ClientConnectionContext connectionContext) {
        super(rootNode, false, true, 1, connectionContext);

        try {
            if (LOG.isDebugEnabled()) {
                LOG.warn("Exploring all nodes of search selection tree root node");           // NOI18N
            }
            rootNode.exploreAll();
        } catch (Exception exp) {
            LOG.error("cound not explore all nodes of search selection tree root node", exp); // NOI18N
        }

        this.setSelectionModel(null);
        this.setCellRenderer(new CheckBoxTreeCellRenderer());
        this.setRowHeight(0);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ClassTreeNode[] getClassNodes() {
        final Enumeration enu = ((DefaultMutableTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();
        final List<ClassTreeNode> nodes = new ArrayList<ClassTreeNode>();

        if (enu == null) {
            return null;
        }

        while (enu.hasMoreElements()) {
            final DefaultMetaTreeNode tempNode = (DefaultMetaTreeNode)enu.nextElement();

            if ((tempNode != null) && tempNode.isClassNode()) {
                nodes.add((ClassTreeNode)tempNode);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[SearchSelectionTree] ClassNodes: " + nodes.size()); // NOI18N
        }

        return nodes.toArray(new ClassTreeNode[nodes.size()]);
    }

    /**
     * DOCUMENT ME!
     */
    public void deselectAllNodes() {
        this.clearSelection();

        final Enumeration enu = ((DefaultMutableTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();
        final TreeNodeIterator iterator = new TreeNodeIterator(enu);

        while (iterator.hasNext()) {
            iterator.next().setSelected(false);
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<String> getSelectedClassNodeKeys() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getSelectedClassNodeKeys() called"); // NOI18N
        }

        final List<String> selectedClassNodeKeys = new ArrayList<String>();
        final Enumeration enu = ((DefaultMutableTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();
        final TreeNodeIterator iterator = new TreeNodeIterator(enu, new TreeNodeRestriction());

        while (iterator.hasNext()) {
            final DefaultMetaTreeNode node = iterator.next();
            if (node.isSelected()) {
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("selected class node '" + node.toString() + "' key: " + node.getKey()); // NOI18N
                    }
                    selectedClassNodeKeys.add(node.getKey());
                } catch (final Exception exp) {
                    LOG.warn("could not add class node key", exp);                                        // NOI18N
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("ignoring class node '" + node.toString() + "' (not selected)");                // NOI18N
            }
        }

        return selectedClassNodeKeys;
    }

    /**
     * select all class nodes.
     *
     * @param  classNodeKeys  DOCUMENT ME!
     */
    public void setSelectedClassNodeKeys(final List classNodeKeys) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("selecting '" + classNodeKeys.size() + "'class nodes"); // NOI18N
        }
        this.clearSelection();

        final DefaultTreeModel model = (DefaultTreeModel)this.getModel();
        final DefaultMetaTreeNode rootNode = (DefaultMetaTreeNode)model.getRoot();
        final Enumeration enu = rootNode.breadthFirstEnumeration();

        while (enu.hasMoreElements()) {
            final DefaultMetaTreeNode tempNode = (DefaultMetaTreeNode)enu.nextElement();
            tempNode.setSelected(false);

            final Iterator iterator = classNodeKeys.iterator();
            while (iterator.hasNext()) {
                try {
                    final Object key = iterator.next();

                    if ((key != null) && (tempNode.getKey() != null) && tempNode.getKey().equals(key)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("selecting class node '" + tempNode + "' (" + key + ")"); // NOI18N
                        }
                        tempNode.setSelected(true);
                    }
                } catch (final Exception exp) {
                    LOG.warn("could not compare class node key", exp);                          // NOI18N
                }
            }
        }

        this.revalidate();
        this.repaint();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * ---------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    final class CheckBoxTreeCellRenderer extends IconCheckBox implements TreeCellRenderer {

        //~ Instance fields ----------------------------------------------------

        /** Is the value currently selected. */
        protected boolean selected;
        /** True if has focus. */
        protected boolean hasFocus;

        // Colors
        /** Color to use for the foreground for selected nodes. */
        protected Color textSelectionColor;

        /** Color to use for the foreground for non-selected nodes. */
        protected Color textNonSelectionColor;

        /** Color to use for the background when a node is selected. */
        protected Color backgroundSelectionColor;

        /** Color to use for the background when the node isn't selected. */
        protected Color backgroundNonSelectionColor;

        /** Color to use for the background when the node isn't selected. */
        protected Color borderSelectionColor;
        /** True if draws focus border around icon as well. */
        private boolean drawsFocusBorderAroundIcon;

        //~ Constructors -------------------------------------------------------

        /**
         * Returns a new instance of DefaultTreeCellRenderer. Alignment is set to left aligned. Icons and text color are
         * determined from the UIManager.
         */
        public CheckBoxTreeCellRenderer() {
            setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));       // NOI18N
            setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));         // NOI18N
            setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground")); // NOI18N
            setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));   // NOI18N
            setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));    // NOI18N
            final Object value = UIManager.get("Tree.drawsFocusBorderAroundIcon");       // NOI18N
            drawsFocusBorderAroundIcon = ((value != null) && ((Boolean)value).booleanValue());
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Sets the color the text is drawn with when the node is selected.
         *
         * @param  newColor  DOCUMENT ME!
         */
        public void setTextSelectionColor(final Color newColor) {
            textSelectionColor = newColor;
        }

        /**
         * Returns the color the text is drawn with when the node is selected.
         *
         * @return  DOCUMENT ME!
         */
        public Color getTextSelectionColor() {
            return textSelectionColor;
        }

        /**
         * Sets the color the text is drawn with when the node isn't selected.
         *
         * @param  newColor  DOCUMENT ME!
         */
        public void setTextNonSelectionColor(final Color newColor) {
            textNonSelectionColor = newColor;
        }

        /**
         * Returns the color the text is drawn with when the node isn't selected.
         *
         * @return  DOCUMENT ME!
         */
        public Color getTextNonSelectionColor() {
            return textNonSelectionColor;
        }

        /**
         * Sets the color to use for the background if node is selected.
         *
         * @param  newColor  DOCUMENT ME!
         */
        public void setBackgroundSelectionColor(final Color newColor) {
            backgroundSelectionColor = newColor;
        }

        /**
         * Returns the color to use for the background if node is selected.
         *
         * @return  DOCUMENT ME!
         */
        public Color getBackgroundSelectionColor() {
            return backgroundSelectionColor;
        }

        /**
         * Sets the background color to be used for non selected nodes.
         *
         * @param  newColor  DOCUMENT ME!
         */
        public void setBackgroundNonSelectionColor(final Color newColor) {
            backgroundNonSelectionColor = newColor;
        }

        /**
         * Returns the background color to be used for non selected nodes.
         *
         * @return  DOCUMENT ME!
         */
        public Color getBackgroundNonSelectionColor() {
            return backgroundNonSelectionColor;
        }

        /**
         * Sets the color to use for the border.
         *
         * @param  newColor  DOCUMENT ME!
         */
        public void setBorderSelectionColor(final Color newColor) {
            borderSelectionColor = newColor;
        }

        /**
         * Returns the color the border is drawn.
         *
         * @return  DOCUMENT ME!
         */
        public Color getBorderSelectionColor() {
            return borderSelectionColor;
        }

        /**
         * Subclassed to map <code>FontUIResource</code>s to null. If <code>font</code> is null, or a <code>
         * FontUIResource</code>, this has the effect of letting the font of the JTree show through. On the other hand,
         * if <code>font</code> is non-null, and not a <code>FontUIResource</code>, the font becomes <code>font</code>.
         *
         * @param  font  DOCUMENT ME!
         */
        @Override
        public void setFont(Font font) {
            if (font instanceof FontUIResource) {
                font = null;
            }
            super.setFont(font);
        }

        /**
         * Subclassed to map <code>ColorUIResource</code>s to null. If <code>color</code> is null, or a <code>
         * ColorUIResource</code>, this has the effect of letting the background color of the JTree show through. On the
         * other hand, if <code>color</code> is non-null, and not a <code>ColorUIResource</code>, the background becomes
         * <code>color</code>.
         *
         * @param  color  DOCUMENT ME!
         */
        @Override
        public void setBackground(Color color) {
            if (color instanceof ColorUIResource) {
                color = null;
            }
            super.setBackground(color);
        }

        /**
         * Configures the renderer based on the passed in components. The value is set from messaging the tree with
         * <code>convertValueToText</code>, which ultimately invokes <code>toString</code> on <code>value</code>. The
         * foreground color is set based on the selection and the icon is set based on on leaf and expanded.
         *
         * @param   tree      DOCUMENT ME!
         * @param   value     DOCUMENT ME!
         * @param   sel       DOCUMENT ME!
         * @param   expanded  DOCUMENT ME!
         * @param   leaf      DOCUMENT ME!
         * @param   row       DOCUMENT ME!
         * @param   hasFocus  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public Component getTreeCellRendererComponent(final JTree tree,
                final java.lang.Object value,
                final boolean sel,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
            final String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
            this.hasFocus = hasFocus;
            setText(stringValue);

            if (sel) {
                setForeground(getTextSelectionColor());
            } else {
                setForeground(getTextNonSelectionColor());
            }

            if (!tree.isEnabled()) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }

            setComponentOrientation(tree.getComponentOrientation());

            final DefaultMetaTreeNode treeNode = (DefaultMetaTreeNode)value;
            this.setSelected(treeNode.isSelected());
            this.setEnabled(treeNode.isEnabled());

            if (expanded == true) {
                this.setIcon(treeNode.getOpenIcon());
            } else if (leaf == true) {
                this.setIcon(treeNode.getLeafIcon());
            } else {
                this.setIcon(treeNode.getClosedIcon());
            }

            return this;
        }
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
final class SelectionTreeNodeLoader implements TreeNodeLoader, ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(SelectionTreeNodeLoader.class);

    //~ Instance fields --------------------------------------------------------

    private final ClientConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SelectionTreeNodeLoader object.
     *
     * @param  connectionContext  DOCUMENT ME!
     */
    SelectionTreeNodeLoader(final ClientConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean addChildren(final DefaultMetaTreeNode node) throws Exception {
        return this.addChildren(node, node.getChildren());
    }

    @Override
    public boolean addChildren(final DefaultMetaTreeNode node, final Node[] children) throws Exception {
        boolean explored = true;

        // WaitNode entfernen!
        node.removeChildren();

        if (children == null) {
            return false;
        }

        DefaultMetaTreeNode childNode;

        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof MetaNode) {
                childNode = new PureTreeNode((MetaNode)children[i], getConnectionContext());
                childNode.setAllowsChildren(checkForChildren(childNode));
                childNode.setSelected(node.isSelected());
                childNode.setEnabled(!childNode.isLeaf());
                node.add(childNode);

                explored &= children[i].isValid();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[SelectionTreeNodeLoader] PureNode Children added"); // NOI18N
                }
            } else if (children[i] instanceof MetaClassNode) {
                childNode = new ClassTreeNode((MetaClassNode)children[i], getConnectionContext());
                childNode.setAllowsChildren(checkForChildren(childNode));
                childNode.setSelected(node.isSelected());
                node.add(childNode);
                explored &= children[i].isValid();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[SelectionTreeNodeLoader] ClassNode Children added");                     // NOI18N
                }
            } else if (children[i] instanceof MetaObjectNode) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[SelectionTreeNodeLoader] ObjectNodes not allowed here: " + children[i]); // NOI18N
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[SelectionTreeNodeLoader] Wrong Node Type: " + children[i]);              // NOI18N
                }
                throw new Exception("Wrong Node Type: " + children[i]);                                  // NOI18N
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("[SelectionTreeNodeLoader] Children #" + i + 1 + " added."); // NOI18N
            }
        }

        return explored;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean checkForChildren(final DefaultMetaTreeNode node) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("cycle check: '" + node + "'"); // NOI18N
        }

        if (!node.isLeaf() && !node.isWaitNode()) {
            try {
                final Node[] children = node.getChildren();

                if ((children != null) && (children.length > 0)) {
                    final TreeNode[] anchestors = node.getPath();
                    final ArrayList childrenList = new ArrayList(children.length);

                    for (int i = 0; i < children.length; i++) {
                        boolean recursive = false;

                        if ((children[i] instanceof MetaNode) || (children[i] instanceof MetaClassNode)) {
                            for (int j = i; j < anchestors.length; j++) {
                                if ((((DefaultMetaTreeNode)anchestors[j]).getID() == children[i].getId())
                                            && ((DefaultMetaTreeNode)anchestors[j]).getDomain().equals(
                                                children[i].getDomain())) {
                                    recursive = true;
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("[SelectionTreeNodeLoader] cycle detected: " + children[i] // NOI18N
                                                    + " id: " + children[i].getId() + " LocalServerName: " // NOI18N
                                                    + children[i].getDomain());
                                    }
                                    break;
                                }
                            }

                            if (!recursive) {
                                childrenList.add(children[i]);
                            }
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Node Type not allowed here" + children[i]); // NOI18N
                            }
                        }
                    }
                }
            } catch (final Exception exp) {
                LOG.error(
                    "[SelectionTreeNodeLoader] Exception at checkForChildren(DefaultMetaTreeNode node): " // NOI18N
                            + node,
                    exp);
            }
        }
        return false;
    }

    @Override
    public ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
