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
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;
import Sirius.navigator.resource.*;

import Sirius.server.middleware.types.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ClassTreeNode extends DefaultMetaTreeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    protected ImageIcon nodeIcon;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ClassTreeNode object.
     *
     * @param  metaClassNode  DOCUMENT ME!
     */
    public ClassTreeNode(final MetaClassNode metaClassNode) {
        super(metaClassNode);

        try {
            final MetaClass metaClass = this.getMetaClass();
            if ((metaClass != null) && (metaClass.getObjectIconData().length > 0)) {
                this.nodeIcon = new ImageIcon(metaClass.getObjectIconData());
            } else {
                this.nodeIcon = resource.getIcon("ClassNodeIcon.gif"); // NOI18N
            }
        } catch (Exception exp) {
            this.nodeIcon = resource.getIcon("ClassNodeIcon.gif");     // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DefaultMetaTreeNode Methods -----------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public MetaClassNode getMetaClassNode() {
        return (MetaClassNode)this.userObject;
    }

    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return ((DefaultMetaTreeNode)this.getParent()).getTreeNodeLoader();
    }

    // --------------------------------------------------------------------------
    @Override
    public final synchronized void explore() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("[ClassNode] Begin explore()"); // NOI18N
        }

        if (!isExplored() && !getMetaClassNode().isLeaf()) {
            this.explored = this.getTreeNodeLoader().addChildren(this);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("[ClassNode] End explore()"); // NOI18N
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
    public final boolean isPureNode() {
        return false;
    }

    @Override
    public final boolean isClassNode() {
        return true;
    }

    @Override
    public final boolean isObjectNode() {
        return false;
    }

    // ---------------------------------------------------------------------------
    @Override
    public final String toString() {
        return getMetaClassNode().getName();
    }

    @Override
    public final String getDescription() {
        return getMetaClassNode().getDescription();
    }

    @Override
    public final ImageIcon getOpenIcon() {
        return this.nodeIcon;
    }

    @Override
    public final ImageIcon getClosedIcon() {
        return this.nodeIcon;
    }

    @Override
    public final ImageIcon getLeafIcon() {
        return this.nodeIcon;
    }

    @Override
    public final boolean equals(final DefaultMetaTreeNode node) {
        if (node.isClassNode() && (this.getID() == node.getID()) && this.getDomain().equals(node.getDomain())) {
            // NavigatorLogger.printMessage("<TREENODE> equals: true");
            return true;
        } else {
            // NavigatorLogger.printMessage("<TREENODE> equals: false");
            return false;
        }
    }

    @Override
    public final boolean equalsNode(final Node node) {
        if ((node instanceof MetaClassNode) && getMetaClassNode().getDomain().equals(node.getDomain())
                    && (getMetaClassNode().getId() == node.getId())) {
            return true;
        } else {
            return false;
        }
    }

    // ===========================================================================
    @Override
    public final int getID() {
        return getMetaClassNode().getId();
    }

    @Override
    public final String getDomain() {
        return getMetaClassNode().getDomain();
    }
    /**
     * ---------------------------------------------------------------------------
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public final MetaClass getMetaClass() throws Exception {
        return SessionManager.getProxy().getMetaClass(this.getMetaClassNode().getClassId(), this.getDomain());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final long getSearchMask() {
        // XXX !!!
        return 255L;
    }

    @Override
    public int getClassID() {
        return this.getMetaClassNode().getClassId();
    }

    @Override
    public String getKey() throws Exception {
        return this.getMetaClass().getKey().toString();
    }
}
