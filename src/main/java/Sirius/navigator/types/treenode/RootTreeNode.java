/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.MetaClassNode;
import Sirius.server.middleware.types.MetaNode;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class RootTreeNode extends DefaultMetaTreeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger log = Logger.getLogger(RootTreeNode.class);

    //~ Instance fields --------------------------------------------------------

    private final TreeNodeLoader treeNodeLoader;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RootTreeNode object.
     */
    public RootTreeNode() // throws Exception
    {
        super(null);
        this.treeNodeLoader = new DefaultTreeNodeLoader();
    }

    /**
     * Dieser Konstruktor erzeugt einen neue RootNode bzw. DefaultMetaTreeNode und heangt auch schon einige Children an.
     * Mit diesem Konstruktor ist es also moeglich einen kompletten Baum in einen bestehenden Baum einzuhaengen.<br>
     * Als RootNode sollte er nicht angezeigt werden. (JTree.setRootVisible(false);)
     *
     * @param  topNodes  ein Array von Nodes
     */
    public RootTreeNode(final Node[] topNodes)                                                                  // throws Exception
    {
        this(topNodes, new DefaultTreeNodeLoader());
    }

    /**
     * Creates a new RootTreeNode object.
     *
     * @param  topNodes        DOCUMENT ME!
     * @param  treeNodeLoader  DOCUMENT ME!
     */
    public RootTreeNode(final Node[] topNodes, final TreeNodeLoader treeNodeLoader) // throws Exception
    {
        super(null);
        this.treeNodeLoader = treeNodeLoader;
        this.setAllowsChildren(true);

        this.addChildren(topNodes);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  topNodes  DOCUMENT ME!
     */
    public void addChildren(final Node[] topNodes) {
        this.removeAllChildren();

        try {
            treeNodeLoader.addChildren(RootTreeNode.this, topNodes);
        } catch (Exception e) {
            log.error("could not add children", e);
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    // ----------------------------------------------------------------------------
    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return this.treeNodeLoader;
    }

    // ----------------------------------------------------------------------------
    @Override
    public synchronized void explore() throws Exception {
    }

    @Override
    public boolean isRootNode() {
        return true;
    }

    @Override
    public boolean isWaitNode() {
        return false;
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
    public boolean isExplored() {
        return true;
    }

    // ----------------------------------------------------------------------------
    @Override
    public String toString() {
        return org.openide.util.NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.toString().returnValue"); // NOI18N
    }

    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.getDescription().returnValue"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       node  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Override
    public boolean equalsNode(final Node node) {
        // LOG.warn("method 'equalsNode()' should not be called on RootNode");
        return false;
    }

    @Override
    public boolean equals(final DefaultMetaTreeNode node) {
        // LOG.warn("method 'equals()' should not be called on RootNode");
        return false;
    }

    @Override
    public ImageIcon getOpenIcon() {
        // LOG.warn("method 'getOpenIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public ImageIcon getClosedIcon() {
        // LOG.warn("method 'getClosedIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public ImageIcon getLeafIcon() {
        // LOG.warn("method 'getLeafIcon()' should not be called on RootNode");
        return null;
    }

    @Override
    public int getID() {
        log.warn("method 'getID()' should not be called on RootNode"); // NOI18N
        return -1;
    }

    @Override
    public String getDomain() {
        log.warn("method 'getDomain()' should not be called on RootNode"); // NOI18N
        return null;
    }

    @Override
    public int getClassID() {
        return -1;
    }

    @Override
    public String getKey() throws Exception {
        return null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class DefaultTreeNodeLoader implements TreeNodeLoader {

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean addChildren(final DefaultMetaTreeNode node) throws Exception {
            return this.addChildren(node, node.getChildren());
        }

        @Override
        public boolean addChildren(final DefaultMetaTreeNode node, final Node[] children) throws Exception {
            boolean explored = true;

            // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] Begin
            // addChildren("+children.length+")"); WaitNode entfernen!
            final boolean inEDT = EventQueue.isDispatchThread();

            {
                final Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            node.removeChildren();
                        }
                    };
                if (inEDT) {
                    r.run();
                } else {
                    EventQueue.invokeLater(r);
                }
            }
            if (children == null) {
                return false;
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof MetaNode) {
                    final PureTreeNode iPTN = new PureTreeNode((MetaNode)children[i]);

                    final Runnable r = new Runnable() {

                            @Override
                            public void run() {
                                node.add(iPTN);
                            }
                        };
                    if (inEDT) {
                        r.run();
                    } else {
                        EventQueue.invokeLater(r);
                    }

                    explored &= children[i].isValid();
                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] PureNode Children added");
                } else if (children[i] instanceof MetaClassNode) {
                    final ClassTreeNode iCTN = new ClassTreeNode((MetaClassNode)children[i]);

                    final Runnable r = new Runnable() {

                            @Override
                            public void run() {
                                node.add(iCTN);
                            }
                        };
                    if (inEDT) {
                        r.run();
                    } else {
                        EventQueue.invokeLater(r);
                    }
                    explored &= children[i].isValid();
                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] ClassNode Children added");
                } else if (children[i] instanceof MetaObjectNode) {
                    final ObjectTreeNode otn = new ObjectTreeNode((MetaObjectNode)children[i]);
                    // toString aufrufen, damit das MetaObject nicht erst im CellRenderer des MetaCatalogueTree vom
                    // Server geholt wird

                    if ((otn.getMetaObject(false) == null) && (otn.getMetaObjectNode().getName() == null)) {
                        de.cismet.tools.CismetThreadPool.execute(new javax.swing.SwingWorker<Void, Void>() {

                                @Override
                                protected Void doInBackground() throws Exception {
                                    otn.getMetaObject(true);
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    try {
                                        final Void result = get();
                                        ((DefaultTreeModel)ComponentRegistry.getRegistry().getSearchResultsTree()
                                                    .getModel()).nodeChanged(otn);
                                        ((DefaultTreeModel)ComponentRegistry.getRegistry().getCatalogueTree()
                                                    .getModel()).nodeChanged(otn);
                                    } catch (Exception e) {
                                        log.error("Exception in Background Thread", e);
                                    }
                                }
                            });
                    }

                    final Runnable r = new Runnable() {

                            @Override
                            public void run() {
                                node.add(otn);
                            }
                        };
                    if (inEDT) {
                        r.run();
                    } else {
                        EventQueue.invokeLater(r);
                    }
                    explored &= children[i].isValid();
                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] ObjectNode Children added");
                } else {
                    log.fatal("[DefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'");            // NOI18N
                    throw new Exception("[DDefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'"); // NOI18N
                }
            }

            return explored;
        }
    }
}
