package Sirius.navigator.types.treenode;

import javax.swing.ImageIcon;

import Sirius.server.middleware.types.*;
import Sirius.navigator.resource.*;

public class WaitTreeNode extends DefaultMetaTreeNode {

    /**
     * Dieser Konstruktor erzeugt einen neue Wait Node. Diese Node wird angezeigt,
     * w\u00E4hrend Daten vom Server geladen werden.
     */
    public WaitTreeNode() {
        super(null);
        this.explored = true;
        this.setAllowsChildren(false);
    }

    //----------------------------------------------------------------------------
    //public void setTreeNodeLoader(TreeNodeLoader treeNodeLoader){}
    public TreeNodeLoader getTreeNodeLoader() {
        return null;
    }

    public Node[] getChildren() throws Exception {
        return null;
    }

    public boolean isLeaf() {
        return true;
    }

    public synchronized void explore() throws Exception {
    }

    //public boolean isLeaf() {return true;}
    //public boolean getAllowsChildren() {return false;}
    public boolean isRootNode() {
        return false;
    }

    public boolean isWaitNode() {
        return true;
    }

    public boolean isPureNode() {
        return false;
    }

    public boolean isClassNode() {
        return false;
    }

    public boolean isObjectNode() {
        return false;
    }

    //----------------------------------------------------------------------------
    public String toString() {
        return ResourceManager.getManager().getString("node.wait.name");
    }

    public String getDescription() {
        return ResourceManager.getManager().getString("node.wait.description");
    }

    public String[][] getAttributes() {
        logger.warn("method 'getAttributes()' should not be called on WaitNode");
        return null;
    }

    public boolean equalsNode(Node node) {
        logger.warn("method 'equalsNode()' should not be called on WaitNode");
        return false;
    }

    public boolean equals(DefaultMetaTreeNode node) {
        logger.warn("method 'equals()' should not be called on WaitNode");
        return false;
    }

    public ImageIcon getOpenIcon() {
        logger.warn("method 'getOpenIcon()' should not be called on WaitNode");
        return null;
    }

    public ImageIcon getClosedIcon() {
        logger.warn("method 'getClosedIcon()' should not be called on WaitNode");
        return null;
    }

    public ImageIcon getLeafIcon() {
        logger.warn("method 'getLeafIcon()' should not be called on WaitNode");
        return null;
    }

    public int getID() {
        logger.warn("method 'getID()' should not be called on WaitNode");
        return -1;
    }

    public String getDomain() {
        logger.warn("method 'getDomain()' should not be called on WaitNode");
        return null;
    }

    public String getKey() throws Exception {
        return null;
    }

    @Override
    public int getClassID() {
        return -1;
    }
}