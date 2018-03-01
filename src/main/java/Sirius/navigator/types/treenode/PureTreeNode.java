/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import Sirius.navigator.resource.ResourceManager;

import Sirius.server.middleware.types.MetaNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import javax.swing.ImageIcon;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class PureTreeNode extends DefaultMetaTreeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(PureTreeNode.class);

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final transient ImageIcon openIcon;
    private final transient ImageIcon closedIcon;
    private final transient ImageIcon leafIcon;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PureTreeNode object.
     *
     * @param  metaNode           DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public PureTreeNode(final MetaNode metaNode, final ClientConnectionContext connectionContext) {
        super(metaNode, connectionContext);

        openIcon = resource.getIcon("NodeIconOpen.gif");     // NOI18N
        closedIcon = resource.getIcon("NodeIconClosed.gif"); // NOI18N
        leafIcon = resource.getIcon("NodeIconClosed.gif");   // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaNode getMetaNode() {
        return (MetaNode)this.userObject;
    }

    // DefaultMetaTreeNode Methods -----------------------------------------------
    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return ((DefaultMetaTreeNode)this.getParent()).getTreeNodeLoader();
    }

    // --------------------------------------------------------------------------
    @Override
    public final synchronized void explore() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[PureNode] Begin explore()"); // NOI18N
        }

        if (!isExplored() && !getMetaNode().isLeaf()) {
            this.explored = this.getTreeNodeLoader().addChildren(this);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[PureNode] End explore()"); // NOI18N
        }
    }

    @Override
    public final boolean isRootNode() {
        return false;
    }

    @Override
    public final boolean isWaitNode() {
        return false;
    }

    @Override
    public final boolean isObjectNode() {
        return false;
    }

    @Override
    public final boolean isPureNode() {
        return true;
    }

    @Override
    public final boolean isClassNode() {
        return false;
    }

    // ---------------------------------------------------------------------------
    @Override
    public final String toString() {
        return getMetaNode().getName();
    }

    @Override
    public final String getDescription() {
        return getMetaNode().getDescription();
    }

    @Override
    public final ImageIcon getOpenIcon() {
        return this.openIcon;
    }

    @Override
    public final ImageIcon getClosedIcon() {
        return this.closedIcon;
    }

    @Override
    public final ImageIcon getLeafIcon() {
        return this.leafIcon;
    }

    @Override
    public final boolean equals(final DefaultMetaTreeNode node) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("equals pure node :" + node);     // NOI18N
        }
        if (node.isPureNode() && (this.getID() == node.getID()) && this.getDomain().equals(node.getDomain())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("equals pure node :" + node); // NOI18N
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean equalsNode(final Node node) {
        if ((node instanceof MetaNode) && getMetaNode().getDomain().equals(node.getDomain())
                    && (getMetaNode().getId() == node.getId())) {
            return true;
        } else {
            return false;
        }
    }

    // ===========================================================================
    @Override
    public final int getID() {
        return getMetaNode().getId();
    }

    @Override
    public final String getDomain() {
        return getMetaNode().getDomain();
    }

    @Override
    public final int getClassID() {
        return getMetaNode().getClassId();
    }

    @Override
    public String getKey() throws Exception {
        // XXX ???
        return null;
    }
}
