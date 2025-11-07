/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeNodeDnDHandler.java
 *
 * Created on 15. September 2004, 11:21
 */
package Sirius.navigator.ui.dnd;

import Sirius.navigator.ui.attributes.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.dnd.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class AttributeNodeDnDHandler implements DragGestureListener, DropTargetListener, DragSourceListener {

    //~ Instance fields --------------------------------------------------------

    private Logger logger;

    private AttributeTree attributeTree;
    private DragSource dragSource;
    private DragSourceContext dragSourceContext;

    private MetaTransferable metaTransferable;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AttributeNodeDnDHandler object.
     *
     * @param  attributeTree  DOCUMENT ME!
     */
    public AttributeNodeDnDHandler(final AttributeTree attributeTree) {
        this.logger = Logger.getLogger(this.getClass());

        this.attributeTree = attributeTree;
        this.dragSource = DragSource.getDefaultDragSource();

        final DragGestureRecognizer dragGestureRecognizer = dragSource.createDefaultDragGestureRecognizer(
                this.attributeTree,
                DnDConstants.ACTION_COPY
                        + DnDConstants.ACTION_LINK,
                this);
        final DropTarget dropTarget = new DropTarget(this.attributeTree, this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void dragGestureRecognized(final DragGestureEvent dge) {
        if (logger.isDebugEnabled()) {
            logger.debug("dragGestureRecognized()");                                     // NOI18N
        }
        final Object selectedNode = this.attributeTree.getSelectionPath().getLastPathComponent();
        if ((selectedNode != null) && (selectedNode instanceof ObjectAttributeNode)) {
            this.metaTransferable = new AttributeNodeTransferable((ObjectAttributeNode)selectedNode);
            this.metaTransferable.setTransferAction(dge.getDragAction());
            this.dragSource.startDrag(dge, this.getCursor(dge.getDragAction()), metaTransferable, this);
        } else if (logger.isDebugEnabled()) {
            logger.warn("dragGestureRecognized() no valid selection for DnD operation"); // NOI18N
        }
    }

    @Override
    public void dragEnter(final DropTargetDragEvent dtde) {
        if (logger.isDebugEnabled()) {
            logger.debug("dragEnter(DropTargetDragEvent)"); // NOI18N
        }
        dtde.rejectDrag();
    }

    @Override
    public void dragExit(final DropTargetEvent dte) {
    }

    @Override
    public void dragOver(final DropTargetDragEvent dtde) {
    }

    @Override
    public void drop(final DropTargetDropEvent dtde) {
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }

    @Override
    public void dragDropEnd(final DragSourceDropEvent dsde) {
    }

    @Override
    public void dragEnter(final DragSourceDragEvent dsde) {
        if (logger.isDebugEnabled()) {
            logger.debug("dragEnter(DragSourceDragEvent)"); // NOI18N
        }

        final DragSourceContext dragSourceContext = dsde.getDragSourceContext();
        dragSourceContext.setCursor(this.getCursor(dsde.getDropAction()));
    }

    @Override
    public void dragExit(final DragSourceEvent dse) {
        dse.getDragSourceContext().setCursor(DragSource.DefaultCopyNoDrop);
    }

    @Override
    public void dragOver(final DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(final DragSourceDragEvent dsde) {
        if (logger.isDebugEnabled()) {
            logger.debug("dropActionChanged(DragSourceDragEvent)"); // NOI18N
        }

        final DragSourceContext dragSourceContext = dsde.getDragSourceContext();
        dragSourceContext.setCursor(this.getCursor(dsde.getUserAction()));
        this.metaTransferable.setTransferAction(dsde.getUserAction());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dragAction  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Cursor getCursor(final int dragAction) {
        Cursor cursor = DragSource.DefaultCopyNoDrop;
        if ((dragAction & DnDConstants.ACTION_MOVE) != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("getCursor(): ACTION_MOVE"); // NOI18N
            }
            cursor = DragSource.DefaultMoveDrop;
        } else if ((dragAction & DnDConstants.ACTION_COPY) != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("getCursor(): ACTION_COPY"); // NOI18N
            }
            cursor = DragSource.DefaultCopyDrop;
        } else if ((dragAction & DnDConstants.ACTION_LINK) != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("getCursor(): ACTION_LINK"); // NOI18N
            }
            cursor = DragSource.DefaultLinkDrop;
        }

        return cursor;
    }
}
