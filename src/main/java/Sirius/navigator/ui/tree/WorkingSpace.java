/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.Node;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class WorkingSpace extends SearchResultsTreePanel implements DropTargetListener {

    //~ Instance fields --------------------------------------------------------

    DataFlavor nodesFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" // NOI18N
                    + java.util.Collection.class.getName(),
            "a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects");

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WorkingSpace object.
     *
     * @param  workingSpaceTree  DOCUMENT ME!
     */
    public WorkingSpace(final WorkingSpaceTree workingSpaceTree) {
        super(workingSpaceTree);
        new DropTarget(this, this);
    }

    /**
     * Creates a new WorkingSpace object.
     *
     * @param  workingSpaceTree  DOCUMENT ME!
     * @param  advancedLayout    DOCUMENT ME!
     */
    public WorkingSpace(final WorkingSpaceTree workingSpaceTree, final boolean advancedLayout) {
        super(workingSpaceTree, advancedLayout);
        new DropTarget(this, this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void dragEnter(final DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(final DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(final DropTargetEvent dte) {
    }

    @Override
    public void drop(final DropTargetDropEvent dtde) {
        if (dtde.getTransferable().isDataFlavorSupported(nodesFlavor)) {
            // Drop von MetaObjects
            try {
                final Object object = dtde.getTransferable().getTransferData(nodesFlavor);

                if (object instanceof Collection) {
                    final Collection c = (Collection)object;
                    final DefaultMetaTreeNode[] type = new DefaultMetaTreeNode[0];
                    final DefaultMetaTreeNode[] draggedTreeNodes = (DefaultMetaTreeNode[])c.toArray(type);
                    if ((draggedTreeNodes != null) && (draggedTreeNodes.length > 0)) {
                        final Node[] draggedNodes = new Node[draggedTreeNodes.length];

                        for (int i = 0; i < draggedTreeNodes.length; i++) {
                            draggedNodes[i] = draggedTreeNodes[i].getNode();
                        }

                        ComponentRegistry.getRegistry().getWorkingSpaceTree().setResultNodes(draggedNodes, true, null);
                        dtde.acceptDrop(dtde.getDropAction());
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
