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

public class ClassTreeNode extends DefaultMetaTreeNode {

    protected ImageIcon nodeIcon;
    private static final ResourceManager resource = ResourceManager.getManager();

    public ClassTreeNode(MetaClassNode metaClassNode) {
        super(metaClassNode);

        try {
            MetaClass metaClass = this.getMetaClass();
            if (metaClass != null && metaClass.getObjectIconData().length > 0) {
                this.nodeIcon = new ImageIcon(metaClass.getObjectIconData());
            } else {
                this.nodeIcon = resource.getIcon("ClassNodeIcon.gif");//NOI18N
            }
        } catch (Exception exp) {
            this.nodeIcon = resource.getIcon("ClassNodeIcon.gif");//NOI18N
        }
    }

    // DefaultMetaTreeNode Methods -----------------------------------------------
    public MetaClassNode getMetaClassNode() {
        return (MetaClassNode) this.userObject;
    }

    public TreeNodeLoader getTreeNodeLoader() {
        return ((DefaultMetaTreeNode) this.getParent()).getTreeNodeLoader();
    }

    // --------------------------------------------------------------------------
    public final synchronized void explore() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("[ClassNode] Begin explore()");//NOI18N
        }

        if (!isExplored() && !getMetaClassNode().isLeaf()) {
            this.explored = this.getTreeNodeLoader().addChildren(this);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("[ClassNode] End explore()");//NOI18N
        }
    }

    public final boolean isRootNode() {
        return false;
    }

    public final boolean isWaitNode() {
        return false;
    }

    public final boolean isPureNode() {
        return false;
    }

    public final boolean isClassNode() {
        return true;
    }

    public final boolean isObjectNode() {
        return false;
    }

    // ---------------------------------------------------------------------------
    public final String toString() {
        return getMetaClassNode().getName();
    }

    public final String getDescription() {
        return getMetaClassNode().getDescription();
    }

    public final ImageIcon getOpenIcon() {
        return this.nodeIcon;
    }

    public final ImageIcon getClosedIcon() {
        return this.nodeIcon;
    }

    public final ImageIcon getLeafIcon() {
        return this.nodeIcon;
    }

    public final boolean equals(DefaultMetaTreeNode node) {
        if (node.isClassNode() && this.getID() == node.getID() && this.getDomain().equals(node.getDomain())) {
            //NavigatorLogger.printMessage("<TREENODE> equals: true");
            return true;
        } else {
            //NavigatorLogger.printMessage("<TREENODE> equals: false");
            return false;
        }
    }

    public final boolean equalsNode(Node node) {
        if (node instanceof MetaClassNode && getMetaClassNode().getDomain().equals(node.getDomain()) && getMetaClassNode().getId() == node.getId()) {
            return true;
        } else {
            return false;
        }
    }

    // ===========================================================================
    public final int getID() {
        return getMetaClassNode().getId();
    }

    public final String getDomain() {
        return getMetaClassNode().getDomain();
    }

    // ---------------------------------------------------------------------------
    public final MetaClass getMetaClass() throws Exception {
        return SessionManager.getProxy().getMetaClass(this.getMetaClassNode().getClassId(), this.getDomain());
    }

    public final long getSearchMask() {
        // XXX !!!
        return 255L;
    }

    @Override
    public int getClassID() {
        return this.getMetaClassNode().getClassId();
    }




    public String getKey() throws Exception {
        return this.getMetaClass().getKey().toString();
    }
}