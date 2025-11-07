/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeTree.java
 *
 * Created on 1. Juli 2004, 14:16
 */
package Sirius.navigator.ui.attributes;

import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import org.apache.log4j.Logger;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import de.cismet.tools.CismetThreadPool;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class AttributeTree extends JTree {

    //~ Instance fields --------------------------------------------------------

    private final Logger logger = Logger.getLogger(this.getClass());

    private AttributeNode rootNode = null;

    /** Holds value of property ignoreSubsitute. */
    private boolean ignoreSubsitute = true;

    /** Holds value of property ignoreArrayHelperObjects. */
    private boolean ignoreArrayHelperObjects = true;

    /** Holds value of property ignoreInvisibleAttributes. */
    private boolean ignoreInvisibleAttributes;

    /** Holds value of property newAttributeRequest. */
    private boolean newAttributeRequest;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of AttributeTree.
     */
    public AttributeTree() {
        super(new DefaultTreeModel(null));
        this.setCellRenderer(new IconRenderer());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  node  DOCUMENT ME!
     */
    public void setTreeNode(final Object node) {
        this.setNewAttributeRequest(true);
        if ((node != null) && (node instanceof DefaultMetaTreeNode)) {
            if (logger.isDebugEnabled()) {
                logger.debug("creating new AttributeTreeThread"); // NOI18N
            }
            try {
                EventQueue.invokeAndWait(new Thread("attribute tree first clear") {

                        @Override
                        public void run() {
                            AttributeTree.this.clear();
                        }
                    });
            } catch (Exception e) {
                // nothing to do
            }
            CismetThreadPool.execute(new Thread("AttributeTreeThread") // NOI18N
                {

                    @Override
                    public void run() {
                        AttributeTree.this.setNewAttributeRequest(false);
                        if (logger.isDebugEnabled()) {
                            logger.debug("AttributeTreeThread started");                                       // NOI18N
                        }
                        synchronized (AttributeTree.this) {
                            try {
                                if (AttributeTree.this.isNewAttributeRequest()) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("AttributeTreeThread: new request, aborting thread (1)"); // NOI18N
                                    }
                                    AttributeTree.this.notifyAll();
                                    return;
                                }

                                if (logger.isDebugEnabled()) {
                                    logger.debug("AttributeTreeThread running"); // NOI18N
                                }
                                if (((DefaultMetaTreeNode)node).isClassNode()) {
                                    AttributeTree.this.rootNode = new ClassAttributeNode(
                                            node.toString(),
                                            AttributeTree.this.isIgnoreSubsitute(),
                                            AttributeTree.this.ignoreArrayHelperObjects,
                                            AttributeTree.this.ignoreInvisibleAttributes,
                                            ((ClassTreeNode)node).getMetaClass());
                                } else if (((DefaultMetaTreeNode)node).isObjectNode()) {
                                    AttributeTree.this.rootNode = new ObjectAttributeNode(
                                            node.toString(),
                                            AttributeTree.this.isIgnoreSubsitute(),
                                            AttributeTree.this.ignoreArrayHelperObjects,
                                            AttributeTree.this.ignoreInvisibleAttributes,
                                            ((ObjectTreeNode)node).getMetaObject());
                                } else {
                                    AttributeTree.this.rootNode = null;
                                }

                                if (AttributeTree.this.isNewAttributeRequest()) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("AttributeTreeThread: new request, aborting thread (2)"); // NOI18N
                                    }
                                    AttributeTree.this.notifyAll();
                                    return;
                                }

                                if (logger.isDebugEnabled()) {
                                    logger.debug("AttributeTreeThread performing GUI update"); // NOI18N
                                }
                                SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (logger.isDebugEnabled()) {
                                                logger.debug(
                                                    "AttributeTreeThread: setting new root node '"
                                                            + rootNode
                                                            + "'");                                               // NOI18N
                                            }
                                            if (AttributeTree.this.isNewAttributeRequest()) {
                                                if (logger.isDebugEnabled()) {
                                                    logger.debug(
                                                        "AttributeTreeThread: new request, aborting thread (1)"); // NOI18N
                                                }
                                                return;
                                            }

                                            ((DefaultTreeModel)AttributeTree.this.getModel()).setRoot(rootNode);
                                            if (logger.isDebugEnabled()) {
                                                logger.debug("AttributeTreeThread GUI update in progress"); // NOI18N
                                            }
                                            ((DefaultTreeModel)AttributeTree.this.getModel()).reload();
                                            AttributeTree.this.setSelectionRow(0);
                                        }
                                    });
                            } catch (Exception exp) {
                                logger.error("could not update attribute tree", exp);                       // NOI18N
                                AttributeTree.this.clear();
                            }

                            if (logger.isDebugEnabled()) {
                                logger.debug("AttributeTreeThread: notifiy waiting threads"); // NOI18N
                            }
                            AttributeTree.this.notifyAll();
                        }
                        if (logger.isDebugEnabled()) {
                            logger.debug("AttributeTreeThread finished");                     // NOI18N
                        }
                    }
                });
        } else {
            EventQueue.invokeLater(new Thread("attribute tree clear") {

                    @Override
                    public void run() {
                        AttributeTree.this.clear();
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        ((DefaultTreeModel)this.getModel()).setRoot(null);
        ((DefaultTreeModel)this.getModel()).reload();
        this.setSelectionRow(0);
        this.rootNode = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized AttributeNode getRootNode() {
        return this.rootNode;
    }

    /**
     * Getter for property obeySubsitute.
     *
     * @return  Value of property obeySubsitute.
     */
    public boolean isIgnoreSubsitute() {
        return this.ignoreSubsitute;
    }

    /**
     * Setter for property obeySubsitute.
     *
     * @param  ignoreSubsitute  New value of property obeySubsitute.
     */
    public void setIgnoreSubsitute(final boolean ignoreSubsitute) {
        this.ignoreSubsitute = ignoreSubsitute;
    }

    /**
     * Getter for property ignoreArrayHelperObjects.
     *
     * @return  Value of property ignoreArrayHelperObjects.
     */
    public boolean isIgnoreArrayHelperObjects() {
        return this.ignoreArrayHelperObjects;
    }

    /**
     * Setter for property ignoreArrayHelperObjects.
     *
     * @param  ignoreArrayHelperObjects  New value of property ignoreArrayHelperObjects.
     */
    public void setIgnoreArrayHelperObjects(final boolean ignoreArrayHelperObjects) {
        this.ignoreArrayHelperObjects = ignoreArrayHelperObjects;
    }

    /**
     * Getter for property ignoreInvisibleAttributes.
     *
     * @return  Value of property ignoreInvisibleAttributes.
     */
    public boolean isIgnoreInvisibleAttributes() {
        return this.ignoreInvisibleAttributes;
    }

    /**
     * Setter for property ignoreInvisibleAttributes.
     *
     * @param  ignoreInvisibleAttributes  New value of property ignoreInvisibleAttributes.
     */
    public void setIgnoreInvisibleAttributes(final boolean ignoreInvisibleAttributes) {
        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }

    /**
     * Getter for property newAttributeRequest.
     *
     * @return  Value of property newAttributeRequest.
     */
    private synchronized boolean isNewAttributeRequest() {
        return this.newAttributeRequest;
    }

    /**
     * Setter for property newAttributeRequest.
     *
     * @param  newAttributeRequest  New value of property newAttributeRequest.
     */
    private synchronized void setNewAttributeRequest(final boolean newAttributeRequest) {
        this.newAttributeRequest = newAttributeRequest;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class IconRenderer extends DefaultTreeCellRenderer {

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getTreeCellRendererComponent(final JTree tree,
                final Object value,
                final boolean selected,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            this.setIcon(((AttributeNode)value).getIcon());

            return this;
        }
    }
}
