/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
 */

import Sirius.server.middleware.types.*;

import javax.swing.ImageIcon;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class RootTreeNode extends DefaultMetaTreeNode {

    //~ Instance fields --------------------------------------------------------

    private final TreeNodeLoader treeNodeLoader;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RootTreeNode object.
     */
    public RootTreeNode() // throws Exception
    {
        super(null);
        this.treeNodeLoader = new DefaultTreeNodeLoader();
    }

    /**
     * Dieser Konstruktor erzeugt einen neue RootNode bzw. DefaultMetaTreeNode und heangt auch schon einige Children an.
     * Mit diesem Konstruktor ist es also moeglich einen kompletten Baum in einen bestehenden Baum einzuhaengen.<br>
     * Als RootNode sollte er nicht angezeigt werden. (JTree.setRootVisible(false);)
     *
     * @param  topNodes  ein Array von Nodes
     */
    public RootTreeNode(final Node[] topNodes)                                                                  // throws Exception
    {
        this(topNodes, new DefaultTreeNodeLoader());
    }

    /**
     * Creates a new RootTreeNode object.
     *
     * @param  topNodes        DOCUMENT ME!
     * @param  treeNodeLoader  DOCUMENT ME!
     */
    public RootTreeNode(final Node[] topNodes, final TreeNodeLoader treeNodeLoader) // throws Exception
    {
        super(null);
        this.treeNodeLoader = treeNodeLoader;
        this.setAllowsChildren(true);

        this.addChildren(topNodes);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  topNodes  DOCUMENT ME!
     */
    public void addChildren(final Node[] topNodes) {
        this.removeAllChildren();
        try {
            this.treeNodeLoader.addChildren(this, topNodes);
        } catch (Exception exp) {
            logger.error("could not add children", exp); // NOI18N
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    // ----------------------------------------------------------------------------

    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return this.treeNodeLoader;
    }

    // ----------------------------------------------------------------------------

    @Override
    public synchronized void explore() throws Exception {
    }

    @Override
    public boolean isRootNode() {
        return true;
    }

    @Override
    public boolean isWaitNode() {
        return false;
    }

    @Override
    public boolean isPureNode() {
        return false;
    }

    @Override
    public boolean isClassNode() {
        return false;
    }

    @Override
    public boolean isObjectNode() {
        return false;
    }

    // ----------------------------------------------------------------------------

    @Override
    public boolean isExplored() {
        return true;
    }

    // ----------------------------------------------------------------------------

    @Override
    public String toString() {
        return org.openide.util.NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.toString().returnValue"); // NOI18N
    }

    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.getDescription().returnValue"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       node  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Override
    public boolean equalsNode(final Node node) {
        // logger.warn("method 'equalsNode()' should not be called on RootNode");
        return false;
    }

    @Override
    public boolean equals(final DefaultMetaTreeNode node) {
        // logger.warn("method 'equals()' should not be called on RootNode");
        return false;
    }

    @Override
    public ImageIcon getOpenIcon() {
        // logger.warn("method 'getOpenIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public ImageIcon getClosedIcon() {
        // logger.warn("method 'getClosedIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public ImageIcon getLeafIcon() {
        // logger.warn("method 'getLeafIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public int getID() {
        logger.warn("method 'getID()' should not be called on RootNode"); // NOI18N
        return -1;
    }

    @Override
    public String getDomain() {
        logger.warn("method 'getDomain()' should not be called on RootNode"); // NOI18N
        return null;
    }

    @Override
    public int getClassID() {
        return -1;
    }

    @Override
    public String getKey() throws Exception {
        return null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class DefaultTreeNodeLoader implements TreeNodeLoader {

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean addChildren(final DefaultMetaTreeNode node) throws Exception {
            return this.addChildren(node, node.getChildren());
        }

        @Override
        public boolean addChildren(final DefaultMetaTreeNode node, final Node[] children) throws Exception {
            boolean explored = true;

            // if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] Begin
            // addChildren("+children.length+")"); WaitNode entfernen!
            node.removeChildren();

            if (children == null) {
                return false;
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof MetaNode) {
                    node.add(new PureTreeNode((MetaNode)children[i]));
                    explored &= children[i].isValid();
                    // if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] PureNode Children added");
                } else if (children[i] instanceof MetaClassNode) {
                    node.add(new ClassTreeNode((MetaClassNode)children[i]));
                    explored &= children[i].isValid();
                    // if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] ClassNode Children added");
                } else if (children[i] instanceof MetaObjectNode) {
                    final ObjectTreeNode otn = new ObjectTreeNode((MetaObjectNode)children[i]);
                    // toString aufrufen, damit das MetaObject nicht erst im CellRenderer des MetaCatalogueTree vom
                    // Server geholt wird
                    otn.toString();
                    node.add(otn);
                    explored &= children[i].isValid();
                    // if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] ObjectNode Children added");
                } else {
                    logger.fatal("[DefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'");         // NOI18N
                    throw new Exception("[DDefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'"); // NOI18N
                }
            }

            return explored;
        }
    }
}
