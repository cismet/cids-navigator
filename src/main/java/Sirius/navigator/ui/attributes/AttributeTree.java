/*
 * AttributeTree.java
 *
 * Created on 1. Juli 2004, 14:16
 */

package Sirius.navigator.ui.attributes;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.dnd.peer.*;
import java.io.*;

import org.apache.log4j.Logger;

import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import de.cismet.tools.CismetThreadPool;

import java.beans.Beans;

/**
 *
 * @author  pascal
 */
public class AttributeTree extends JTree
{
    private final Logger logger = Logger.getLogger(this.getClass());;
    
    private AttributeNode rootNode = null;
    
    /**
     * Holds value of property ignoreSubsitute.
     */
    private boolean ignoreSubsitute = true;
    
    /**
     * Holds value of property ignoreArrayHelperObjects.
     */
    private boolean ignoreArrayHelperObjects = true;
    
    /** Creates a new instance of AttributeTree */
    public AttributeTree()
    {
        super(new DefaultTreeModel(null));
        this.setCellRenderer(new IconRenderer());
    }
    
    public void setTreeNode(final Object node)
    {
        this.setNewAttributeRequest(true);
        if(node != null && node instanceof DefaultMetaTreeNode)
        {
            if(logger.isDebugEnabled())logger.debug("creating new AttributeTreeThread");
            this.clear();
            
            CismetThreadPool.execute(new Thread("AttributeTreeThread")
            {
                public void run()
                {
                    AttributeTree.this.setNewAttributeRequest(false);
                    if(logger.isDebugEnabled())logger.debug("AttributeTreeThread started");
                    synchronized(AttributeTree.this)
                    {
                        try
                        {
                            if(AttributeTree.this.isNewAttributeRequest())
                            {
                                if(logger.isDebugEnabled())logger.debug("AttributeTreeThread: new request, aborting thread (1)");
                                AttributeTree.this.notifyAll();
                                return;
                            }
                            
                            if(logger.isDebugEnabled())logger.debug("AttributeTreeThread running");
                            if(((DefaultMetaTreeNode)node).isClassNode())
                            {
                                AttributeTree.this.rootNode = new ClassAttributeNode(node.toString(), AttributeTree.this.isIgnoreSubsitute(), AttributeTree.this.ignoreArrayHelperObjects, AttributeTree.this.ignoreInvisibleAttributes, ((ClassTreeNode)node).getMetaClass());
                            }
                            else if(((DefaultMetaTreeNode)node).isObjectNode())
                            {
                                AttributeTree.this.rootNode = new ObjectAttributeNode(node.toString(), AttributeTree.this.isIgnoreSubsitute(), AttributeTree.this.ignoreArrayHelperObjects, AttributeTree.this.ignoreInvisibleAttributes, ((ObjectTreeNode)node).getMetaObject());
                            }
                            else
                            {
                                AttributeTree.this.rootNode = null;
                            }
                            
                            if(AttributeTree.this.isNewAttributeRequest())
                            {
                                if(logger.isDebugEnabled())logger.debug("AttributeTreeThread: new request, aborting thread (2)");
                                AttributeTree.this.notifyAll();
                                return;
                            }
                            
                            if(logger.isDebugEnabled())logger.debug("AttributeTreeThread performing GUI update");
                            SwingUtilities.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    if(logger.isDebugEnabled())logger.debug("AttributeTreeThread: setting new root node '" + rootNode + "'");
                                    if(AttributeTree.this.isNewAttributeRequest())
                                    {
                                        if(logger.isDebugEnabled())logger.debug("AttributeTreeThread: new request, aborting thread (1)");
                                        return;
                                    }
                                    
                                    ((DefaultTreeModel)AttributeTree.this.getModel()).setRoot(rootNode);
                                    if(logger.isDebugEnabled())logger.debug("AttributeTreeThread GUI update in progress");
                                    ((DefaultTreeModel)AttributeTree.this.getModel()).reload();
                                    AttributeTree.this.setSelectionRow(0);
                                }
                            });
                        }
                        catch(Exception exp)
                        {
                            logger.error("could not update attribute tree", exp);
                            AttributeTree.this.clear();
                        }
                        
                        if(logger.isDebugEnabled())logger.debug("AttributeTreeThread: notifiy waiting threads");
                        AttributeTree.this.notifyAll();
                    }
                    if(logger.isDebugEnabled())logger.debug("AttributeTreeThread finished");
                }
            });
        }
        else
        {
            this.clear();
        }
    }
    
    public void clear()
    {
        ((DefaultTreeModel)this.getModel()).setRoot(null);
        ((DefaultTreeModel)this.getModel()).reload();
        this.setSelectionRow(0);
        this.rootNode = null;
    }
    
    private class IconRenderer extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            this.setIcon(((AttributeNode)value).getIcon());
            
            return this;
        }
    }
    
    public synchronized AttributeNode getRootNode()
    {
        return this.rootNode;
    }
    
    /**
     * Getter for property obeySubsitute.
     * @return Value of property obeySubsitute.
     */
    public boolean isIgnoreSubsitute()
    {
        return this.ignoreSubsitute;
    }
    
    /**
     * Setter for property obeySubsitute.
     * @param obeySubsitute New value of property obeySubsitute.
     */
    public void setIgnoreSubsitute(boolean ignoreSubsitute)
    {
        this.ignoreSubsitute = ignoreSubsitute;
    }
    
    /**
     * Getter for property ignoreArrayHelperObjects.
     * @return Value of property ignoreArrayHelperObjects.
     */
    public boolean isIgnoreArrayHelperObjects()
    {
        
        return this.ignoreArrayHelperObjects;
    }
    
    /**
     * Setter for property ignoreArrayHelperObjects.
     * @param ignoreArrayHelperObjects New value of property ignoreArrayHelperObjects.
     */
    public void setIgnoreArrayHelperObjects(boolean ignoreArrayHelperObjects)
    {
        
        this.ignoreArrayHelperObjects = ignoreArrayHelperObjects;
    }
    
    /**
     * Holds value of property ignoreInvisibleAttributes.
     */
    private boolean ignoreInvisibleAttributes;
    
    /**
     * Getter for property ignoreInvisibleAttributes.
     * @return Value of property ignoreInvisibleAttributes.
     */
    public boolean isIgnoreInvisibleAttributes()
    {
        
        return this.ignoreInvisibleAttributes;
    }
    
    /**
     * Setter for property ignoreInvisibleAttributes.
     * @param ignoreInvisibleAttributes New value of property ignoreInvisibleAttributes.
     */
    public void setIgnoreInvisibleAttributes(boolean ignoreInvisibleAttributes)
    {
        
        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }
    
    /**
     * Holds value of property newAttributeRequest.
     */
    private boolean newAttributeRequest;
    
    /**
     * Getter for property newAttributeRequest.
     * @return Value of property newAttributeRequest.
     */
    private synchronized boolean isNewAttributeRequest()
    {
        
        return this.newAttributeRequest;
    }
    
    /**
     * Setter for property newAttributeRequest.
     * @param newAttributeRequest New value of property newAttributeRequest.
     */
    private synchronized void setNewAttributeRequest(boolean newAttributeRequest)
    {
        
        this.newAttributeRequest = newAttributeRequest;
    }
}
