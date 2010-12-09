/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
 */

import Sirius.navigator.types.iterator.*;
import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.awt.*;

import java.util.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.*;
//import Sirius.navigator.NavigatorLogger;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SearchSelectionTree extends MetaCatalogueTree {

    //~ Instance fields --------------------------------------------------------

    /*public SearchSelectionTree(Node rootNodes[])
     * {     super(new SelectableClassTreeNode(rootNodes), true, 1);     //super(new RootTreeNode(rootNodes), true, 1);
     *    this.initSearchSelectionTree();}*/

    protected Logger logger = Logger.getLogger(SearchSelectionTree.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchSelectionTree object.
     *
     * @param  rootNodes  DOCUMENT ME!
     */
    public SearchSelectionTree(final Node[] rootNodes) // throws Exception
    {
        // super(new RootTreeNode(rootNodes, new SelectionTreeNodeLoader()), true, 1);
        // super(new RootTreeNode(rootNodes), true, 1);
        // this.initSearchSelectionTree();

        this(new RootTreeNode(rootNodes, new SelectionTreeNodeLoader()));
    }

    /**
     * Creates a new SearchSelectionTree object.
     *
     * @param  rootNode  DOCUMENT ME!
     */
    public SearchSelectionTree(final RootTreeNode rootNode) {
        super(rootNode, false, true, 1);

        try {
            if (logger.isDebugEnabled()) {
                logger.warn("Exploring all nodes of search selection tree root node");           // NOI18N
            }
            rootNode.exploreAll();
        } catch (Exception exp) {
            logger.error("cound not explore all nodes of search selection tree root node", exp); // NOI18N
        }

        this.initSearchSelectionTree();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initSearchSelectionTree() {
        this.setSelectionModel(null);
        // this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setCellRenderer(new CheckBoxTreeCellRenderer());
        // this.addMouseListener(new ClassNodeSelectionListener());
        this.addTreeExpansionListener(new MetaCatalogueExpansionListener());
        // this.addTreeExpansionListener(new MetaTreeExpansionListener());

        this.setRowHeight(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ClassTreeNode[] getClassNodes() {
        final Enumeration enu = ((DefaultMutableTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();
        final Vector nodeVector = new Vector(10, 10);

        if (enu == null) {
            return null;
        }

        while (enu.hasMoreElements()) {
            final DefaultMetaTreeNode tempNode = (DefaultMetaTreeNode)enu.nextElement();

            if ((tempNode != null) && tempNode.isClassNode()) {
                nodeVector.add(tempNode);
            }
        }

        nodeVector.trimToSize();
        if (logger.isDebugEnabled()) {
            logger.debug("[SearchSelectionTree] ClassNodes: " + nodeVector.size()); // NOI18N
        }
        return (ClassTreeNode[])nodeVector.toArray(new ClassTreeNode[nodeVector.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public ClassTreeNode[] getSelectedClassNodes() {
        final ClassTreeNode[] classNodes = this.getClassNodes();
        ArrayList classNodesList = null;

        if ((classNodes != null) && (classNodes.length > 0)) {
            classNodesList = new ArrayList(classNodes.length);

            for (int i = 0; i < classNodes.length; i++) {
                if (logger.isDebugEnabled()) {
                    logger.debug("[SearchSelectionTree] classNode.isSelected(): " + classNodes[i] + " : "
                                + classNodes[i].isSelected());                                      // NOI18N
                }
                if (classNodes[i].isSelected()) {
                    classNodesList.add(classNodes[i]);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("[SearchSelectionTree] SelectedClassNodes: " + classNodesList.size()); // NOI18N
            }

            if (classNodesList.size() > 0) {
                return (ClassTreeNode[])classNodesList.toArray(new ClassTreeNode[classNodesList.size()]);
            }
        }

        return null;

        /*TreePath[] selectedPaths =  this.getSelectionPaths();
         *  if (selectedPaths != null && selectedPaths.length > 0) {     DefaultMetaTreeNode[] nodeArray = new
         * DefaultMetaTreeNode[selectedPaths.length];     int j = 0;      for (int i = 0; i < selectedPaths.length; i++)
         * { DefaultMetaTreeNode tempNode = (DefaultMetaTreeNode)selectedPaths[i].getLastPathComponent();
         * NavigatorLogger.printMessage("tempNode: " + tempNode + "isClassNode(): " + tempNode.isClassNode());
         * if(tempNode.isClassNode())             { nodeArray[j] = tempNode;                     j++;             } }
         * if(j > 0)     {             ClassTreeNode[] returnArray = new ClassTreeNode[j]; System.arraycopy(nodeArray,
         * 0, returnArray, 0, j);             return returnArray;     }     else     {
         *       return null;     } } else {      return null;}*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param       classNodes  DOCUMENT ME!
     *
     * @deprecated  use <code>selectClassNodes(java.util.List)</code>
     */
    public void selectClassNodes(final ClassTreeNode[] classNodes) {
        this.clearSelection();

        if ((classNodes != null) && (classNodes.length > 0)) {
            if (logger.isDebugEnabled()) {
                logger.debug("selecting '" + classNodes.length + "'class nodes"); // NOI18N
            }
            final DefaultTreeModel model = (DefaultTreeModel)this.getModel();
            final DefaultMetaTreeNode rootNode = (DefaultMetaTreeNode)model.getRoot();

            final Enumeration enu = rootNode.breadthFirstEnumeration();
            while (enu.hasMoreElements()) {
                final DefaultMetaTreeNode tempNode = (DefaultMetaTreeNode)enu.nextElement();
                tempNode.setSelected(false);

                for (int i = 0; i < classNodes.length; i++) {
                    if (classNodes[i].equals(tempNode)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("selecting class node '" + tempNode + "'"); // NOI18N
                        }
                        tempNode.setSelected(true);
                        // this.addSelectionPath(new TreePath(model.getPathToRoot(tempNode))); break;
                    }
                }
            }
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * new =====================================================================.
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
    public java.util.List getSelectedClassNodeKeys() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSelectedClassNodeKeys() called"); // NOI18N
        }

        final LinkedList selectedClassNodeKeys = new LinkedList();
        final Enumeration enu = ((DefaultMutableTreeNode)this.getModel().getRoot()).breadthFirstEnumeration();
        // TreeNodeIterator iterator = new TreeNodeIterator(enum, new TreeNodeRestriction(TreeNodeRestriction.CLASS));
        final TreeNodeIterator iterator = new TreeNodeIterator(enu, new TreeNodeRestriction());

        while (iterator.hasNext()) {
            final DefaultMetaTreeNode node = iterator.next();
            if (node.isSelected()) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("selected class node '" + node.toString() + "' key: " + node.getKey()); // NOI18N
                    }
                    selectedClassNodeKeys.add(node.getKey());
                } catch (Exception exp) {
                    logger.error("could not add class node key", exp);                                       // NOI18N
                }
            } else if (logger.isDebugEnabled()) {
                logger.debug("ignoring class node '" + node.toString() + "' (not selected)");                // NOI18N
            }
        }

        return selectedClassNodeKeys;
    }

    /**
     * select all class nodes.
     *
     * @param  classNodeKeys  DOCUMENT ME!
     */
    public void setSelectedClassNodeKeys(final java.util.List classNodeKeys) {
        if (logger.isDebugEnabled()) {
            logger.debug("selecting '" + classNodeKeys.size() + "'class nodes"); // NOI18N
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
                        if (logger.isDebugEnabled()) {
                            logger.debug("selecting class node '" + tempNode + "' (" + key + ")"); // NOI18N
                        }
                        tempNode.setSelected(true);
                        // break;
                    }
                } catch (Exception exp) {
                    logger.error("could not compare class node key", exp);                         // NOI18N
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
    class CheckBoxTreeCellRenderer extends Sirius.navigator.ui.widget.IconCheckBox implements TreeCellRenderer {

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
            setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));           // NOI18N
            setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));             // NOI18N
            setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));     // NOI18N
            setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));       // NOI18N
            setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));        // NOI18N
            final java.lang.Object value = UIManager.get("Tree.drawsFocusBorderAroundIcon"); // NOI18N
            drawsFocusBorderAroundIcon = ((value != null) && ((Boolean)value).booleanValue());

            // setPreferredSize(new Dimension(250, 22));
            // setMaximumSize(new Dimension(100, 22));
            // setMinimumSize(new Dimension(100, 22));
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

            /*CompoundIcon compoundIcon;
             * if(expanded == true) { compoundIcon = new CompoundIcon(treeNode.getOpenIcon(), this.getIcon()); } else
             * if(leaf == true) { compoundIcon = new CompoundIcon(treeNode.getLeafIcon(), this.getIcon()); } else {
             * compoundIcon = new CompoundIcon(treeNode.getClosedIcon(), this.getIcon()); }
             * this.setIcon(compoundIcon);*/

            return this;
        }

        /*public Dimension getPreferredSize()
         * { Dimension        retDimension = super.getPreferredSize();  if(retDimension != null)     retDimension = new
         * Dimension(retDimension.width + 3,     retDimension.height); return retDimension; }
         *
         * public void validate() {}
         *
         * public void revalidate() {}
         *
         * public void repaint(long tm, int x, int y, int width, int height) {}
         *
         * public void repaint(Rectangle r) {}
         *
         * protected void firePropertyChange(String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
         * // Strings get interned... if (propertyName=="text")     super.firePropertyChange(propertyName, oldValue,
         * newValue); }
         *
         * public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
         *
         * public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
         *
         * public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
         *
         * public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
         *
         * public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
         *
         * public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
         *
         * public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
         *
         * public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
         *
         * /*private class CompoundIcon implements Icon { private Icon icon1; private Icon icon2;  public CompoundIcon(
         * Icon icon1, Icon icon2 ) {     this.icon1 = icon1;     this.icon2 = icon2;      if(icon1 == null)     {
         * this.icon1 = Sirius.navigator.resource.ResourceManager.getManager().getIcon("x.gif");     }      if(icon2 ==
         * null)     {         this.icon2 = Sirius.navigator.resource.ResourceManager.getManager().getIcon("x.gif");   }
         * }  public int getIconHeight() {     return Math.max( icon1.getIconHeight(), icon2.getIconHeight() ); } public
         * int getIconWidth() {     return ( icon1.getIconWidth() + icon2.getIconWidth() ); }  public void paintIcon(
         * Component c, Graphics g, int x, int y ) {     icon1.paintIcon( c, g, x, y );     icon2.paintIcon(
         * c, g, x + icon1.getIconWidth(), y ); }}*/
    }
}

/**
 * -----------------------------------------------------------------------------.
 *
 * @version  $Revision$, $Date$
 */
class SelectionTreeNodeLoader implements TreeNodeLoader {

    //~ Instance fields --------------------------------------------------------

    protected Logger logger = Logger.getLogger(SelectionTreeNodeLoader.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean addChildren(final DefaultMetaTreeNode node) throws Exception {
        return this.addChildren(node, node.getChildren());
    }

    @Override
    public boolean addChildren(final DefaultMetaTreeNode node, final Node[] children) throws Exception {
        boolean explored = true;

        // logger.debug("[SelectionTreeNodeLoader] Begin addChildren()");
        // WaitNode entfernen!
        node.removeChildren();

        if (children == null) {
            return false;
        }

        DefaultMetaTreeNode childNode;

        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof MetaNode) {
                childNode = new PureTreeNode((MetaNode)children[i]);
                childNode.setAllowsChildren(checkForChildren(childNode));
                childNode.setSelected(node.isSelected());
                childNode.setEnabled(!childNode.isLeaf());
                node.add(childNode);

                // NavigatorLogger.printMessage("[!!!] Node: " + node); NavigatorLogger.printMessage("[!!!]
                // ChildPureNode: " + childNode); NavigatorLogger.printMessage("[!!!] ChildPureNode AllowsChildren Leaf
                // Enabled: " + childNode.getAllowsChildren() + " " + childNode.isLeaf() + " " + childNode.isEnabled());

                explored &= children[i].isValid();

                if (logger.isDebugEnabled()) {
                    logger.debug("[SelectionTreeNodeLoader] PureNode Children added"); // NOI18N
                }
            } else if (children[i] instanceof MetaClassNode) {
                childNode = new ClassTreeNode((MetaClassNode)children[i]);
                childNode.setAllowsChildren(checkForChildren(childNode));
                childNode.setSelected(node.isSelected());
                node.add(childNode);
                explored &= children[i].isValid();

                if (logger.isDebugEnabled()) {
                    logger.debug("[SelectionTreeNodeLoader] ClassNode Children added"); // NOI18N
                }
            } else if (children[i] instanceof MetaObjectNode) {
                // node.add(new ObjectTreeNode(new LocalObjectNode(children[i]))); explored &= children[i].isValid();
                // if(logger.isDebugEnabled())logger.debug("[SelectionTreeNodeLoader] ObjectNode Children added");

                if (logger.isDebugEnabled()) {
                    logger.debug("[SelectionTreeNodeLoader] ObjectNodes not allowed here: " + children[i]); // NOI18N
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("[SelectionTreeNodeLoader] Wrong Node Type: " + children[i]);              // NOI18N
                }
                // _TA_throw new Exception("<TREENODE> Fehler: falscher Node-Typ: " + children[i]);
                throw new Exception("Wrong Node Type: " + children[i]); // NOI18N
            }

            if (logger.isDebugEnabled()) {
                logger.debug("[SelectionTreeNodeLoader] Children #" + i + 1 + " added."); // NOI18N
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
        if (logger.isDebugEnabled()) {
            logger.debug("cycle check: '" + node + "'"); // NOI18N
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
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("[SelectionTreeNodeLoader] cycle detected: " + children[i]
                                                    + " id: " + children[i].getId() + " LocalServerName: "
                                                    + children[i].getDomain()); // NOI18N
                                    }
                                    break;
                                }
                            }

                            if (!recursive) {
                                // NavigatorLogger.printMessage("childrenList.add(children[i]): " + children[i]);
                                childrenList.add(children[i]);
                            }
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Node Type not allowed here" + children[i]); // NOI18N
                            }
                        }
                    }

                    /*if(childrenList.size() > 0)
                     * { node.setChildren((Node[])childrenList.toArray(new Node[childrenList.size()])); return true;}*/
                }
            } catch (Exception exp) {
                logger.error("[SelectionTreeNodeLoader] Exception at checkForChildren(DefaultMetaTreeNode node): "
                            + node,
                    exp); // NOI18N
            }
        }
        return false;
    }
}
