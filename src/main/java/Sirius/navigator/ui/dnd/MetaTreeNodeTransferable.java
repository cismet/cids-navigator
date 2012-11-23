/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MetaTreeNodeTransferable.java
 *
 * Created on 15. September 2004, 10:42
 */
package Sirius.navigator.ui.dnd;

import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.tree.*;

import Sirius.server.middleware.types.MetaObjectNode;

import org.apache.log4j.Logger;

import java.awt.datatransfer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class MetaTreeNodeTransferable implements MetaTransferable {

    //~ Static fields/initializers ---------------------------------------------

    static DataFlavor[] dataFlavors = null;

    //~ Instance fields --------------------------------------------------------

    private MetaCatalogueTree metaCatalogueTree;
    private List<DefaultMetaTreeNode> metaNodes;

    /** Holds value of property transferAction. */
    private int transferAction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaTreeNodeTransferable.
     *
     * @param  metaCatalogueTree  DOCUMENT ME!
     */
    public MetaTreeNodeTransferable(final MetaCatalogueTree metaCatalogueTree) {
        this.metaCatalogueTree = metaCatalogueTree;
    }

    /**
     * Creates a new instance of MetaTreeNodeTransferable. If you use this constructor, the getTransferData method will
     * not return the selected node of a tree, but the given metaTreeNodes
     *
     * @param  metaNode  DOCUMENT ME!
     */
    public MetaTreeNodeTransferable(final List<DefaultMetaTreeNode> metaNode) {
        this.metaNodes = metaNode;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException {
        if (getTransferDataFlavors().length > 0) {
            if (getTransferDataFlavors()[0].equals(flavor)) {
                // Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): MetaTreeNode");

                if (this.metaCatalogueTree != null) {
                    return this.metaCatalogueTree.getSelectedNode();
                } else {
                    return this.metaNodes.get(0);
                }
            } else if (getTransferDataFlavors()[1].equals(flavor)) {
                // Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): Collection of nodes");
                if (this.metaCatalogueTree != null) {
                    return this.metaCatalogueTree.getSelectedNodes();
                } else {
                    return metaNodes;
                }
            } else {
                Logger.getLogger(MetaTreeNodeTransferable.class).warn("getTransferData(): UnsupportedFlavorException"); // NOI18N
                throw new UnsupportedFlavorException(flavor);
            }
        }

        // Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): no flavours available");
        return null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // lazily construct flavors
        if (dataFlavors == null) {
            dataFlavors = new DataFlavor[2];

            try {
                // MetaTreeNode
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class="
                            + DefaultMetaTreeNode.class.getName();           // NOI18N
                DataFlavor dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName("a DefaultMetaTreeNode"); // NOI18N
                dataFlavors[0] = dataFlavor;

                // Collection of nodes
                mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + java.util.Collection.class.getName(); // NOI18N
                dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName(
                    "a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects");        // NOI18N
                dataFlavors[1] = dataFlavor;
            } catch (ClassNotFoundException cnfe) {
                Logger.getLogger(MetaTreeNodeTransferable.class)
                        .error("getTransferDataFlavors() could not create DnD data flavours", cnfe);                 // NOI18N
                dataFlavors = new DataFlavor[0];
            }
        }

        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        if ((getTransferDataFlavors().length > 0) && (dataFlavors[0].equals(flavor) || dataFlavors[1].equals(flavor))) {
            return true;
        }

        return false;
    }

    /**
     * Getter for property transferAction.
     *
     * @return  Value of property transferAction.
     */
    @Override
    public int getTransferAction() {
        return this.transferAction;
    }

    /**
     * Setter for property transferAction.
     *
     * @param  transferAction  New value of property transferAction.
     */
    @Override
    public void setTransferAction(final int transferAction) {
        this.transferAction = transferAction;
    }
}
