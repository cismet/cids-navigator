/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeNodeTransferable.java
 *
 * Created on 15. September 2004, 11:03
 */
package Sirius.navigator.ui.dnd;

import Sirius.navigator.ui.attributes.*;

import org.apache.log4j.Logger;

import java.awt.datatransfer.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class AttributeNodeTransferable implements MetaTransferable {

    //~ Static fields/initializers ---------------------------------------------

    static DataFlavor[] dataFlavors = null;

    //~ Instance fields --------------------------------------------------------

    private ObjectAttributeNode objectAttributeNode;

    /** Holds value of property transferAction. */
    private int transferAction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of AttributeNodeTransferable.
     *
     * @param  objectAttributeNode  DOCUMENT ME!
     */
    public AttributeNodeTransferable(final ObjectAttributeNode objectAttributeNode) {
        this.objectAttributeNode = objectAttributeNode;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException {
        if (getTransferDataFlavors().length > 0) {
            if (getTransferDataFlavors()[0].equals(flavor)) {
                return this.objectAttributeNode;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        return null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // lazily construct flavors
        if (dataFlavors == null) {
            dataFlavors = new DataFlavor[1];

            try {
                // ObjectAttributeNode
                final String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class="
                            + ObjectAttributeNode.class.getName();                                   // NOI18N
                final DataFlavor dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName("a ObjectAttributeNode");                         // NOI18N
                dataFlavors[0] = dataFlavor;
            } catch (ClassNotFoundException cnfe) {
                Logger.getLogger(AttributeNodeTransferable.class)
                        .error("getTransferDataFlavors() could not create DnD data flavours", cnfe); // NOI18N
                dataFlavors = new DataFlavor[0];
            }
        }

        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        if ((getTransferDataFlavors().length > 0) && dataFlavors[0].equals(flavor)) {
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
