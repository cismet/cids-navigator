/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.awt.EventQueue;

import java.beans.PropertyChangeListener;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;

import de.cismet.cids.navigator.utils.DirectedMetaObjectNodeComparator;
import de.cismet.cids.navigator.utils.MetaTreeNodeVisualization;

import de.cismet.tools.CismetThreadPool;

import de.cismet.tools.collections.HashArrayList;

/**
 * Der SearchTree dient zum Anzeigen von Suchergebnissen. Neben der Funktionalit\u00E4t, die er von GenericMetaTree
 * erbt, bietet er zusaetzlich noch die Moeglichkeit, die Suchergebnisse schrittweise anzuzeigen. D.h. es wird immer nur
 * ein kleiner Ausschnitt fester Groesse aus der gesamten Ergebissmenge angezeigt. Um durch die Ergebnissmenge zu
 * navigieren stellt der SearchTree spezielle Methoden bereit.
 *
 * @version  $Revision$, $Date$
 */
public class SearchResultsTree extends MetaCatalogueTree {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger log = Logger.getLogger(SearchResultsTree.class);

    //~ Instance fields --------------------------------------------------------

    private boolean empty = true;
    private HashArrayList<Node> resultNodes = new HashArrayList<Node>();
    private final RootTreeNode rootNode;
    private Thread runningNameLoader = null;
    private SwingWorker<ArrayList<DefaultMetaTreeNode>, Void> refreshWorker;
    private boolean syncWithMap = false;
    private boolean ascending = true;
    private final WaitTreeNode waitTreeNode = new WaitTreeNode();

    //~ Constructors -----------------------------------------------------------

    /**
     * Erzeugt einen neuen, leeren, SearchTree. Es werden jeweils 50 Objekte angezeigt.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public SearchResultsTree() throws Exception {
        this(true, 2);
    }

    /**
     * Creates a new SearchResultsTree object.
     *
     * @param   useThread       DOCUMENT ME!
     * @param   maxThreadCount  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public SearchResultsTree(final boolean useThread, final int maxThreadCount) throws Exception {
        super(new RootTreeNode(), false, useThread, maxThreadCount);
        this.rootNode = (RootTreeNode)this.defaultTreeModel.getRoot();
        defaultTreeModel.setAsksAllowsChildren(true);
        this.defaultTreeModel.setAsksAllowsChildren(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.
     *
     * @param  nodes  Ergebnisse, die im SearchTree angezeigt werden sollen.
     */
    public void setResultNodes(final Node[] nodes) {
        if (log.isInfoEnabled()) {
            log.info("[SearchResultsTree] filling tree with '" + nodes.length + "' nodes"); // NOI18N
        }

        if ((nodes == null) || (nodes.length < 1)) {
            empty = true;
            resultNodes.clear();
        } else {
            empty = false;
            resultNodes = new HashArrayList<Node>(Arrays.asList(nodes));
        }

        if (resultNodes.size() > 0) {
            refreshTree(true);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void syncWithMap() {
        syncWithMap(isSyncWithMap());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sync  DOCUMENT ME!
     */
    public void syncWithMap(final boolean sync) {
        if (sync) {
            if (log.isDebugEnabled()) {
                log.debug("syncWithMap");                                                   // NOI18N
            }
            try {
                final PluginSupport map = PluginRegistry.getRegistry().getPlugin("cismap"); // NOI18N
                final List<DefaultMetaTreeNode> v = new ArrayList<DefaultMetaTreeNode>();
                final DefaultTreeModel dtm = (DefaultTreeModel)getModel();

                for (int i = 0; i < ((DefaultMetaTreeNode)dtm.getRoot()).getChildCount(); ++i) {
                    if (resultNodes.get(i) instanceof MetaObjectNode) {
                        final DefaultMetaTreeNode otn = (DefaultMetaTreeNode)((DefaultMetaTreeNode)dtm.getRoot())
                                    .getChildAt(i);
                        v.add(otn);
                    }
                }

                MetaTreeNodeVisualization.getInstance().addVisualization(v);
            } catch (Throwable t) {
                log.warn("Fehler beim synchronisieren der Suchergebnisse mit der Karte", t); // NOI18N
            }
        }
    }

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.<br>
     * Diese Ergebnisse koennen an eine bereits vorhandene Ergebnissmenge angehaengt werden
     *
     * @param  nodes     Ergebnisse, die im SearchTree angezeigt werden sollen.
     * @param  append    Ergebnisse anhaengen.
     * @param  listener  DOCUMENT ME!
     */
    public void setResultNodes(final Node[] nodes, final boolean append, final PropertyChangeListener listener) {
        if (log.isInfoEnabled()) {
            log.info("[SearchResultsTree] " + (append ? "appending" : "setting") + " '" + nodes.length + "' nodes"); // NOI18N
        }

        if ((append == true) && ((nodes == null) || (nodes.length < 1))) {
            return;
        } else if ((append == false) && ((nodes == null) || (nodes.length < 1))) {
            this.clear();
            return;
        } else if ((append == true) && (empty == false)) {
            resultNodes.addAll(Arrays.asList(nodes));
        } else {
            this.clear();
            resultNodes = new HashArrayList<Node>(Arrays.asList(nodes));
        }

        empty = false;
        refreshTree(true, listener);

        if (!getModel().getRoot().equals(rootNode)) {
            ((DefaultTreeModel)getModel()).setRoot(rootNode);
            ((DefaultTreeModel)getModel()).reload();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  initialFill  sort DOCUMENT ME!
     */
    private void refreshTree(final boolean initialFill) {
        refreshTree(initialFill, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  initialFill  sort DOCUMENT ME!
     * @param  listener     DOCUMENT ME!
     */
    private void refreshTree(final boolean initialFill, final PropertyChangeListener listener) {
        if ((refreshWorker != null) && !refreshWorker.isDone()) {
            log.warn("Refreshing search result tree is triggered while another refresh process is still not done.");
            refreshWorker.cancel(true);
        }
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    rootNode.removeAllChildren();
                    if (resultNodes.size() > 0) {
                        rootNode.add(waitTreeNode);
                    }

                    defaultTreeModel.nodeStructureChanged(rootNode);

                    refreshWorker = new RefreshTreeWorker(initialFill);
                    refreshWorker.addPropertyChangeListener(listener);
                    CismetThreadPool.execute(refreshWorker);
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    private void checkForDynamicNodes() {
        final DefaultTreeModel dtm = (DefaultTreeModel)getModel();
        final DefaultMetaTreeNode node = (DefaultMetaTreeNode)dtm.getRoot();
        if (runningNameLoader != null) {
            runningNameLoader.interrupt();
        }

        final Thread t = new Thread() {

                @Override
                public void run() {
                    runningNameLoader = this;
                    for (int i = 0; i < dtm.getChildCount(node); ++i) {
                        if (interrupted()) {
                            break;
                        }
                        try {
                            final DefaultMetaTreeNode n = (DefaultMetaTreeNode)dtm.getChild(node, i);

                            if ((n != null) && (n.getNode().getName() == null) && n.isObjectNode()) {
                                try {
                                    final ObjectTreeNode on = ((ObjectTreeNode)n);
                                    EventQueue.invokeLater(new Runnable() {

                                            @Override
                                            public void run() {
                                                n.getNode()
                                                        .setName(
                                                            org.openide.util.NbBundle.getMessage(
                                                                SearchResultsTree.class,
                                                                "SearchResultsTree.checkForDynamicNodes().loadName")); // NOI18N
                                                dtm.nodeChanged(on);
                                            }
                                        });
                                    if (log.isDebugEnabled()) {
                                        log.debug("caching object node");                                              // NOI18N
                                    }
                                    final MetaObject MetaObject = SessionManager.getProxy()
                                                .getMetaObject(on.getMetaObjectNode().getObjectId(),
                                                    on.getMetaObjectNode().getClassId(),
                                                    on.getMetaObjectNode().getDomain());
                                    on.getMetaObjectNode().setObject(MetaObject);
                                    EventQueue.invokeLater(new Runnable() {

                                            @Override
                                            public void run() {
                                                n.getNode().setName(MetaObject.toString());
                                                dtm.nodeChanged(on);
                                            }
                                        });
                                } catch (final Exception t) {
                                    log.error("could not retrieve meta object of node '" + this + "'", t); // NOI18N
                                }
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug("n.getNode().getName()!=null: " + n.getNode().getName() + ":"); // NOI18N
                                }
                            }
                        } catch (final Exception e) {
                            log.error("Error while loading the name", e);                                  // NOI18N
                        }
                    }
                    runningNameLoader = null;
                }
            };

        CismetThreadPool.execute(t);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Node> getResultNodes() {
        return resultNodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   n  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean containsNode(final Node n) {
        return (n != null) && resultNodes.contains(n);
    }

    /**
     * Diese Funktion dient dazu, eine Selektion von Knoten aus dem SearchTree zu loeschen.
     *
     * @param   selectedNodes  Die Knoten, die geloescht werden sollen.
     *
     * @return  true, wenn mindestens ein Knoten geloescht wurde.
     */
    public boolean removeResultNodes(final DefaultMetaTreeNode[] selectedNodes) {
        if (log.isInfoEnabled()) {
            log.info("[SearchResultsTree] removing '" + selectedNodes + "' nodes"); // NOI18N
        }
        boolean deleted = false;

        if ((selectedNodes == null) || (selectedNodes.length < 1)) {
            return deleted;
        }

        final List tmpNodeVector = new ArrayList();
        tmpNodeVector.addAll(Arrays.asList(resultNodes));

        for (int i = 0; i < tmpNodeVector.size(); i++) {
            for (int j = 0; j < selectedNodes.length;) {
                if ((i < tmpNodeVector.size()) && selectedNodes[j].equalsNode((Node)tmpNodeVector.get(i))) {
                    tmpNodeVector.remove(i);
                    deleted = true;
                } else {
                    ++j;
                }
            }
        }

        if (deleted) {
            this.setResultNodes((Node[])tmpNodeVector.toArray(new Node[tmpNodeVector.size()]), false, null);
        }

        return deleted;
    }

    /**
     * DOCUMENT ME!
     */
    public void removeSelectedResultNodes() {
        final Collection selectedNodes = this.getSelectedNodes();
        if (selectedNodes != null) {
            this.removeResultNodes(selectedNodes);
            refreshTree(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   selectedNodes  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeResultNodes(final Collection selectedNodes) {
        if (log.isInfoEnabled()) {
            log.info("[SearchResultsTree] removing '" + selectedNodes + "' nodes"); // NOI18N
        }
        boolean deleted = false;

        try {
            if ((selectedNodes != null) && (selectedNodes.size() > 0)) {
                final ArrayList all = new ArrayList(Arrays.asList(resultNodes));
                final ArrayList allWorkingCopy = new ArrayList(Arrays.asList(resultNodes));
                final ArrayList selectionTreeNodes = new ArrayList(selectedNodes);
                final ArrayList selection = new ArrayList();
                for (final Object selO : selectionTreeNodes) {
                    if (selO instanceof DefaultMetaTreeNode) {
                        final Node n = ((DefaultMetaTreeNode)selO).getNode();
                        selection.add(n);
                        deleted = true;
                    }
                }
                resultNodes.removeAll(selection);

//                if (resultNodes.size() == 0) {
//                    clear();
//                } else {
//                    defaultTreeModel.nodeStructureChanged(rootNode);
//                }
            }

//
//                for (final Object allO : all) {
//                    for (final Object selO : selection) {
//                        if ((allO instanceof MetaObjectNode) && (selO instanceof MetaObjectNode)) {
//                            final MetaObjectNode allMON = (MetaObjectNode)allO;
//                            final MetaObjectNode selMON = (MetaObjectNode)selO;
//                            if ((allMON.getObjectId() == selMON.getObjectId())
//                                        && (allMON.getClassId() == selMON.getClassId())
//                                        && (allMON.getId() == selMON.getId())
//                                        && allMON.getDomain().equals(selMON.getDomain())
//                                        && allMON.toString().equals(selMON.toString())) {
//                                allWorkingCopy.remove(allO);
//                                deleted = true;
//                            }
//                        } else if ((allO instanceof MetaNode) && (selO instanceof MetaNode)) {
//                            final MetaNode allMN = (MetaNode)allO;
//                            final MetaNode selMN = (MetaNode)selO;
//                            if ((allMN.getId() == selMN.getId())
//                                        && allMN.getDomain().equals(selMN.getDomain())
//                                        && allMN.toString().equals(selMN.toString())) {
//                                allWorkingCopy.remove(allO);
//                                deleted = true;
//                            }
//                        }
//                    }
//                }
//
//                this.setResultNodes((Node[])allWorkingCopy.toArray(new Node[allWorkingCopy.size()]), false, null);
//            }
        } catch (Exception e) {
            log.error("Fehler beim Entfernen eines Objektes aus den Suchergebnissen", e);
        }
        return deleted;
    }

    /**
     * Setzt den SearchTree komplett zurueck und entfernt alle Knoten.
     */
    public void clear() {
        log.info("[SearchResultsTree] removing all nodes"); // NOI18N
        resultNodes.clear();
        empty = true;
        rootNode.removeAllChildren();
        firePropertyChange("browse", 0, 1);                 // NOI18N
        defaultTreeModel.nodeStructureChanged(rootNode);
        System.gc();
    }

    /**
     * Returns a flag indicating whether the SearchResultsTree is empty or not.
     *
     * @return  Flag indicating whether tree is empty or not.
     */
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSyncWithMap() {
        return syncWithMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  syncWithMap  DOCUMENT ME!
     */
    public void setSyncWithMap(final boolean syncWithMap) {
        this.syncWithMap = syncWithMap;
    }

    /**
     * Changes the sort order to ascending or descending according to the given parameter.
     *
     * @param  ascending  Whether to sort ascending ( <code>true</code>) or descending ( <code>false</code>).
     */
    public void sort(final boolean ascending) {
        this.ascending = ascending;
        refreshTree(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void cancelNodeLoading() {
        if ((refreshWorker != null) && !refreshWorker.isDone()) {
            refreshWorker.cancel(true);
            rootNode.removeAllChildren();
            defaultTreeModel.nodeStructureChanged(rootNode);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SwingWorker getNodeLoadingWorker() {
        return refreshWorker;
    }

    /**
     * A SwingWorker which encapsulates sorting the results and refreshing the
     * tree. This worker is needed since it could be necessary to load every
     * object during the first sort process on a certain result set.
     *
     * @version $Revision$, $Date$
     */

    //J-
    private class RefreshTreeWorker extends SwingWorker<ArrayList<DefaultMetaTreeNode>, Void> {

        //~ Instance fields ----------------------------------------------------
        private boolean initialFill = false;
        private DirectedMetaObjectNodeComparator comparator;

        //~ Constructors -------------------------------------------------------
        /**
         * Creates a new RefreshTreeWorker object.
         *
         * @param initialFill A flag indicating whether to sort the result set
         * or not.
         */
        public RefreshTreeWorker(final boolean initialFill) {
            this.initialFill = initialFill;
            comparator = new DirectedMetaObjectNodeComparator(ascending);
        }

        //~ Methods ------------------------------------------------------------
        @Override
        protected ArrayList<DefaultMetaTreeNode> doInBackground() throws Exception {
            if (!isCancelled()) {
                Collections.sort(resultNodes, comparator);
            }


            final ArrayList<DefaultMetaTreeNode> nodesToAdd = new ArrayList<DefaultMetaTreeNode>(resultNodes.size());

            for (int i = 0; i < resultNodes.size(); i++) {
                if (resultNodes.get(i) instanceof MetaNode) {
                    final PureTreeNode iPTN = new PureTreeNode((MetaNode) resultNodes.get(i));
                    nodesToAdd.add(iPTN);

                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] PureNode Children added");
                } else if (resultNodes.get(i) instanceof MetaClassNode) {
                    final ClassTreeNode iCTN = new ClassTreeNode((MetaClassNode) resultNodes.get(i));
                    nodesToAdd.add(iCTN);
                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] ClassNode Children added");
                } else if (resultNodes.get(i) instanceof MetaObjectNode) {
                    final ObjectTreeNode otn = new ObjectTreeNode((MetaObjectNode) resultNodes.get(i));
                    // toString aufrufen, damit das MetaObject nicht erst im CellRenderer des MetaCatalogueTree vom
                    // Server geholt wird
                    otn.toString();
                    nodesToAdd.add(otn);
                    // if(LOG.isDebugEnabled())LOG.debug("[DefaultTreeNodeLoader] ObjectNode Children added");
                } else {
                    log.fatal("[DefaultTreeNodeLoader] Wrong Node Type: '" + resultNodes.get(i) + "'");            // NOI18N
                    throw new Exception("[DDefaultTreeNodeLoader] Wrong Node Type: '" + resultNodes.get(i) + "'"); // NOI18N
                }
            }
            return nodesToAdd;
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                comparator.cancel();
            }
            rootNode.removeAllChildren();
            try {
                if (!isCancelled()) {
                    ArrayList<DefaultMetaTreeNode> result = get();
                    if (!isCancelled()) {
                        for (DefaultMetaTreeNode dmtn : result) {
                            rootNode.add(dmtn);

                        }
                        MetaTreeNodeRenderer renderer = new MetaTreeNodeRenderer();
                        setRowHeight(getCellRenderer().getTreeCellRendererComponent(SearchResultsTree.this, rootNode.getFirstChild(), false, false, false, 0, false).getHeight());

                        setLargeModel(result.size() > 15);

                        SearchResultsTree.this.firePropertyChange("browse", 0, 1); // NOI18N


                        defaultTreeModel.nodeStructureChanged(rootNode);


                        if (initialFill) {
                            syncWithMap();
                            checkForDynamicNodes();
                        }
                    }
                }
            } catch (InterruptedException ex) {
                log.error("Error occured while refreshing search results tree", ex);
            } catch (ExecutionException ex) {
                log.error("Error occured while refreshing search results tree", ex);
            }



        }
    }
    //J+
}
