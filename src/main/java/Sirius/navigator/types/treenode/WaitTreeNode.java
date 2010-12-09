/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import Sirius.server.middleware.types.*;

import javax.swing.ImageIcon;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class WaitTreeNode extends DefaultMetaTreeNode {

    //~ Constructors -----------------------------------------------------------

    /**
     * Dieser Konstruktor erzeugt einen neue Wait Node. Diese Node wird angezeigt, w\u00E4hrend Daten vom Server geladen
     * werden.
     */
    public WaitTreeNode() {
        super(null);
        this.explored = true;
        this.setAllowsChildren(false);
    }

    //~ Methods ----------------------------------------------------------------

    // ----------------------------------------------------------------------------
    // public void setTreeNodeLoader(TreeNodeLoader treeNodeLoader){}
    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return null;
    }

    @Override
    public Node[] getChildren() throws Exception {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public synchronized void explore() throws Exception {
    }

    // public boolean isLeaf() {return true;}
    // public boolean getAllowsChildren() {return false;}
    @Override
    public boolean isRootNode() {
        return false;
    }

    @Override
    public boolean isWaitNode() {
        return true;
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
    public String toString() {
        return org.openide.util.NbBundle.getMessage(WaitTreeNode.class, "WaitTreeNode.toString().returnValue"); // NOI18N
    }

    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(WaitTreeNode.class, "WaitTreeNode.getDescription().returnValue"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String[][] getAttributes() {
        logger.warn("method 'getAttributes()' should not be called on WaitNode"); // NOI18N
        return null;
    }

    @Override
    public boolean equalsNode(final Node node) {
        logger.warn("method 'equalsNode()' should not be called on WaitNode"); // NOI18N
        return false;
    }

    @Override
    public boolean equals(final DefaultMetaTreeNode node) {
        logger.warn("method 'equals()' should not be called on WaitNode"); // NOI18N
        return false;
    }

    @Override
    public ImageIcon getOpenIcon() {
        logger.warn("method 'getOpenIcon()' should not be called on WaitNode"); // NOI18N
        return null;
    }

    @Override
    public ImageIcon getClosedIcon() {
        logger.warn("method 'getClosedIcon()' should not be called on WaitNode"); // NOI18N
        return null;
    }

    @Override
    public ImageIcon getLeafIcon() {
        logger.warn("method 'getLeafIcon()' should not be called on WaitNode"); // NOI18N
        return null;
    }

    @Override
    public int getID() {
        logger.warn("method 'getID()' should not be called on WaitNode"); // NOI18N
        return -1;
    }

    @Override
    public String getDomain() {
        logger.warn("method 'getDomain()' should not be called on WaitNode"); // NOI18N
        return null;
    }

    @Override
    public String getKey() throws Exception {
        return null;
    }

    @Override
    public int getClassID() {
        return -1;
    }
}
