package Sirius.navigator.types.treenode;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
 */
import javax.swing.*;

import Sirius.server.middleware.types.*;
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;
import Sirius.navigator.resource.*;

public class PureTreeNode extends DefaultMetaTreeNode {

    private static ImageIcon openIcon = null;
    private static ImageIcon closedIcon = null;
    private static ImageIcon leafIcon = null;

    private static final ResourceManager resource = ResourceManager.getManager();

    public PureTreeNode(MetaNode metaNode) {
        super(metaNode);

        if (openIcon == null) {
            openIcon = resource.getIcon(
                    resource.getString("Sirius.navigator.types.treenode.PureTreeNode.openIcon"));
            closedIcon = resource.getIcon(
                    resource.getString("Sirius.navigator.types.treenode.PureTreeNode.closeIcon"));
            leafIcon = resource.getIcon(
                    resource.getString("Sirius.navigator.types.treenode.PureTreeNode.leafIcon"));
        }
    }

    public MetaNode getMetaNode() {
        return (MetaNode) this.userObject;
    }

    // DefaultMetaTreeNode Methods -----------------------------------------------
    public TreeNodeLoader getTreeNodeLoader() {
        return ((DefaultMetaTreeNode) this.getParent()).getTreeNodeLoader();
    }

    // --------------------------------------------------------------------------
    public final synchronized void explore() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("[PureNode] Begin explore()");
        }

        if (!isExplored() && !getMetaNode().isLeaf()) {
            this.explored = this.getTreeNodeLoader().addChildren(this);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("[PureNode] End explore()");
        }
    }

    public final boolean isRootNode() {
        return false;
    }

    public final boolean isWaitNode() {
        return false;
    }

    public final boolean isObjectNode() {
        return false;
    }

    public final boolean isPureNode() {
        return true;
    }

    public final boolean isClassNode() {
        return false;
    }

    // ---------------------------------------------------------------------------
    public final String toString() {
        return getMetaNode().getName();
    }

    public final String getDescription() {
        return getMetaNode().getDescription();
    }

    public final ImageIcon getOpenIcon() {
        return this.openIcon;
    }

    public final ImageIcon getClosedIcon() {
        return this.closedIcon;
    }

    public final ImageIcon getLeafIcon() {
        return this.leafIcon;
    }

    public final boolean equals(DefaultMetaTreeNode node) {
        if (logger.isDebugEnabled()) {
            logger.debug("equals pure node :" + node);
        }
        if (node.isPureNode() && this.getID() == node.getID() && this.getDomain().equals(node.getDomain())) {
            if (logger.isDebugEnabled()) {
                logger.debug("equals pure node :" + node);
            }
            return true;
        } else {
            return false;
        }
    }

    public final boolean equalsNode(Node node) {
        if (node instanceof MetaNode && getMetaNode().getDomain().equals(node.getDomain()) && getMetaNode().getId() == node.getId()) {
            return true;
        } else {
            return false;
        }
    }

    // ===========================================================================
    public final int getID() {
        return getMetaNode().getId();
    }

    public final String getDomain() {
        return getMetaNode().getDomain();
    }

    public final int getClassID() {
        return getMetaNode().getClassId();
    }

    public String getKey() throws Exception {
        // XXX ???
        return null;
    }
}
