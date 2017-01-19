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

import org.openide.util.NbBundle;

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

    private static final transient Logger LOG = Logger.getLogger(RootTreeNode.class);

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
    public RootTreeNode(final Node[] topNodes) {
        this(topNodes, new DefaultTreeNodeLoader());
    }

    /**
     * Creates a new RootTreeNode object.
     *
     * @param  topNodes        DOCUMENT ME!
     * @param  treeNodeLoader  DOCUMENT ME!
     */
    public RootTreeNode(final Node[] topNodes, final TreeNodeLoader treeNodeLoader) {
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
            LOG.error("could not add children", e); // NOI18N
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return this.treeNodeLoader;
    }

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
        return NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.toString().returnValue"); // NOI18N
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(RootTreeNode.class, "RootTreeNode.getDescription().returnValue"); // NOI18N
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
        return false;
    }

    @Override
    public boolean equals(final DefaultMetaTreeNode node) {
        return false;
    }

    @Override
    public ImageIcon getOpenIcon() {
        return null;
    }

    @Override
    public ImageIcon getClosedIcon() {
        return null;
    }

    @Override
    public ImageIcon getLeafIcon() {
        return null;
    }

    @Override
    public int getID() {
        LOG.warn("method 'getID()' should not be called on RootNode"); // NOI18N
        return -1;
    }

    @Override
    public String getDomain() {
        LOG.warn("method 'getDomain()' should not be called on RootNode"); // NOI18N
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
            final boolean inEDT = EventQueue.isDispatchThread();

            // scope limiting 'r' so that it can be reused later
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
                } else if (children[i] instanceof MetaObjectNode) {
                    final ObjectTreeNode otn = new ObjectTreeNode((MetaObjectNode)children[i]);
                    // getMetaobject aufrufen, damit das MetaObject nicht erst im CellRenderer des MetaCatalogueTree vom
                    // Server geholt wird

                    if ((otn.getMetaObject(false) == null) && (otn.getMetaObjectNode().getName() == null)) {
                        de.cismet.tools.CismetThreadPool.execute(new javax.swing.SwingWorker<Void, Void>() {

                                @Override
                                protected Void doInBackground() throws Exception {
                                    Thread.currentThread().setName("RootTreeNode addChildren()");
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
                                    } catch (final Exception e) {
                                        LOG.error("Exception in Background Thread", e);
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
                } else {
                    final String message = "[DefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'"; // NOI18N
                    LOG.error(message);
                    throw new IllegalStateException(message);
                }
            }

            return explored;
        }
    }
}
