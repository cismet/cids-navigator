/*
 * AttributeNodeDnDHandler.java
 *
 * Created on 15. September 2004, 11:21
 */

package Sirius.navigator.ui.dnd;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.dnd.peer.*;
import java.awt.*;
import java.io.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.attributes.*;

/**
 *
 * @author  pascal
 */
public class AttributeNodeDnDHandler implements DragGestureListener, DropTargetListener, DragSourceListener
{
    private Logger logger;
    
    private AttributeTree attributeTree;
    private DragSource dragSource;
    private DragSourceContext dragSourceContext;    
    
    private MetaTransferable metaTransferable;
    
    public AttributeNodeDnDHandler(AttributeTree attributeTree)
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.attributeTree = attributeTree;
        this.dragSource = DragSource.getDefaultDragSource();
        
        DragGestureRecognizer dragGestureRecognizer = dragSource.createDefaultDragGestureRecognizer(this.attributeTree, DnDConstants.ACTION_COPY + DnDConstants.ACTION_LINK, this);
        DropTarget dropTarget = new DropTarget(this.attributeTree, this);
    }
    
    public void dragGestureRecognized(DragGestureEvent dge)
    {
        if(logger.isDebugEnabled())logger.debug("dragGestureRecognized()");//NOI18N
        Object selectedNode = this.attributeTree.getSelectionPath().getLastPathComponent();
        if(selectedNode != null && selectedNode instanceof ObjectAttributeNode)
        {
            this.metaTransferable = new AttributeNodeTransferable((ObjectAttributeNode)selectedNode);
            this.metaTransferable.setTransferAction(dge.getDragAction());
            this.dragSource.startDrag(dge, this.getCursor(dge.getDragAction()), metaTransferable, this);
        }
        else if(logger.isDebugEnabled())
        {
            logger.warn("dragGestureRecognized() no valid selection for DnD operation");//NOI18N
        }
    }
    
    public void dragEnter(DropTargetDragEvent dtde)
    {
        if(logger.isDebugEnabled())logger.debug("dragEnter(DropTargetDragEvent)");//NOI18N
        dtde.rejectDrag();
    }
    
    public void dragExit(DropTargetEvent dte)
    {
    }
    
    public void dragOver(DropTargetDragEvent dtde)
    {
    }
    
    public void drop(DropTargetDropEvent dtde)
    {
    }
    
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
    }
    
    public void dragDropEnd(DragSourceDropEvent dsde)
    {
    }
    
    public void dragEnter(DragSourceDragEvent dsde)
    {
        if(logger.isDebugEnabled())logger.debug("dragEnter(DragSourceDragEvent)");//NOI18N
  
        DragSourceContext dragSourceContext = dsde.getDragSourceContext();
        dragSourceContext.setCursor(this.getCursor(dsde.getDropAction()));
    }
    
    public void dragExit(DragSourceEvent dse)
    {
        dse.getDragSourceContext().setCursor(DragSource.DefaultCopyNoDrop);
    }
    
    public void dragOver(DragSourceDragEvent dsde)
    {
    }
    
    public void dropActionChanged(DragSourceDragEvent dsde)
    {
        if(logger.isDebugEnabled())logger.debug("dropActionChanged(DragSourceDragEvent)");//NOI18N
        
        DragSourceContext dragSourceContext = dsde.getDragSourceContext();
        dragSourceContext.setCursor(this.getCursor(dsde.getUserAction()));
        this.metaTransferable.setTransferAction(dsde.getUserAction());
    } 
    
    private Cursor getCursor(int dragAction)
    {
        Cursor cursor = DragSource.DefaultCopyNoDrop;
        if((dragAction & DnDConstants.ACTION_MOVE) != 0)
        {
            if(logger.isDebugEnabled())logger.debug("getCursor(): ACTION_MOVE");//NOI18N
            cursor = DragSource.DefaultMoveDrop;
        }
        else if((dragAction & DnDConstants.ACTION_COPY) != 0)
        {
            if(logger.isDebugEnabled())logger.debug("getCursor(): ACTION_COPY");//NOI18N
            cursor = DragSource.DefaultCopyDrop;
        }
        else if((dragAction & DnDConstants.ACTION_LINK) != 0)
        {
            if(logger.isDebugEnabled())logger.debug("getCursor(): ACTION_LINK");//NOI18N
            cursor = DragSource.DefaultLinkDrop;
        }
        
        return cursor;
    }
}
