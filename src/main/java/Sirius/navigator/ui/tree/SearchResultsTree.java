/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.awt.EventQueue;
import java.awt.Toolkit;

import java.beans.PropertyChangeListener;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import de.cismet.cids.navigator.utils.DirectedMetaObjectNodeComparator;
import de.cismet.cids.navigator.utils.MetaTreeNodeVisualization;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

import de.cismet.cids.utils.ClassloadingHelper;

import de.cismet.cismap.commons.interaction.CismapBroker;

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

    protected HashArrayList<Node> resultNodes = new HashArrayList<Node>();

    protected boolean muteResultNodeListeners = false;

    private boolean empty = true;
    private final RootTreeNode rootNode;
    private Thread runningNameLoader = null;
    private SwingWorker<ArrayList<DefaultMetaTreeNode>, Void> refreshWorker;
    private boolean syncWithMap = false;
    private boolean ascending = true;
    private final WaitTreeNode waitTreeNode = new WaitTreeNode();
    private boolean syncWithRenderer;
    private MetaObjectNodeServerSearch underlyingSearch;

    private ArrayList<ResultNodeListener> resultNodeListeners = new ArrayList<ResultNodeListener>();

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
            fireResultNodesCleared();
        } else {
            empty = false;
            resultNodes = new HashArrayList<Node>(Arrays.asList(filterNodesWithoutPermission(nodes)));
            fireResultNodesChanged();
        }

        if (resultNodes.size() > 0) {
            refreshTree(true);
        }
    }

    /**
     * Removes all nodes, which references a cidsBean without read permissions. This is required to consider the
     * CustomBeanPermissionProvider.
     *
     * @param   nodes  the nodes to check for read permissions
     *
     * @return  an array with all nodes with read permissions.
     */
    private Node[] filterNodesWithoutPermission(final Node[] nodes) {
        final List<Node> nodeList = new ArrayList<Node>();

        if (!PropertyManager.USE_CUSTOM_BEAN_PERMISSION_PROVIDER_FOR_SEARCH) {
            return nodes;
        }

        for (final Node nodeToCheck : nodes) {
            if (nodeToCheck instanceof MetaObjectNode) {
                final MetaObjectNode mon = (MetaObjectNode)nodeToCheck;
                final MetaClass mc = ClassCacheMultiple.getMetaClass(mon.getDomain(), mon.getClassId());

                if (existsCustomBeanPermissonProviderForClass(mc)) {
                    if (mon.getObject() == null) {
                        try {
                            final MetaObject MetaObject = SessionManager.getProxy()
                                        .getMetaObject(mon.getObjectId(),
                                            mon.getClassId(),
                                            mon.getDomain());
                            mon.setObject(MetaObject);
                        } catch (ConnectionException e) {
                            log.error("Cannot load meta object to check the read permissions", e);
                            continue;
                        }
                    }

                    if (!mon.getObject().getBean().hasObjectReadPermission(SessionManager.getSession().getUser())) {
                        continue;
                    }
                }
            }
            nodeList.add(nodeToCheck);
        }

        return nodeList.toArray(new Node[nodeList.size()]);
    }

    /**
     * Checks if a CustomBeanPermissionProvider for the given class exists.
     *
     * @param   mc  the class, it should be checked for
     *
     * @return  true, iff a CistomBeanPermissionProvider for the given class exists
     */
    private boolean existsCustomBeanPermissonProviderForClass(final MetaClass mc) {
        try {
            final Class cpp = ClassloadingHelper.getDynamicClass(mc,
                    ClassloadingHelper.CLASS_TYPE.PERMISSION_PROVIDER);

            if (log.isDebugEnabled()) {
                log.debug("custom read permission provider retrieval result: " + cpp); // NOI18N
            }

            return cpp != null;
        } catch (Exception ex) {
            log.warn("error during creation of custom permission provider", ex); // NOI18N

            return true;
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
     * @param  rnl  DOCUMENT ME!
     */
    public void addResultNodeListener(final ResultNodeListener rnl) {
        resultNodeListeners.add(rnl);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rnl  DOCUMENT ME!
     */
    public void removeResultNodeListener(final ResultNodeListener rnl) {
        resultNodeListeners.add(rnl);
    }

    /**
     * DOCUMENT ME!
     */
    public void fireResultNodesChanged() {
        if (!muteResultNodeListeners) {
            for (final ResultNodeListener rnl : resultNodeListeners) {
                rnl.resultNodesChanged();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireResultNodesFiltered() {
        if (!muteResultNodeListeners) {
            for (final ResultNodeListener rnl : resultNodeListeners) {
                rnl.resultNodesFiltered();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireResultNodesCleared() {
        if (!muteResultNodeListeners) {
            for (final ResultNodeListener rnl : resultNodeListeners) {
                rnl.resultNodesCleared();
            }
        }
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
            if (!isSyncWithRenderer()) {
                PluginRegistry.getRegistry()
                        .getPluginDescriptor("cismap")
                        .getUIDescriptor("cismap")
                        .getView()
                        .makeVisible();
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
     * DOCUMENT ME!
     */
    public void syncWithRenderer() {
        syncWithRenderer(isSyncWithRenderer());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sync  DOCUMENT ME!
     */
    public void syncWithRenderer(final boolean sync) {
        if (sync) {
            if (log.isDebugEnabled()) {
                log.debug("syncWithRenderer"); // NOI18N
            }
            if (!isSyncWithMap()) {
                ComponentRegistry.getRegistry().getGUIContainer().select(ComponentRegistry.DESCRIPTION_PANE);
            }
            setSelectionInterval(0, getRowCount());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodes     DOCUMENT ME!
     * @param  append    DOCUMENT ME!
     * @param  listener  DOCUMENT ME!
     */
    public void setResultNodes(final Node[] nodes, final boolean append, final PropertyChangeListener listener) {
        setResultNodes(nodes, append, listener, false);
    }

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.<br>
     * Diese Ergebnisse koennen an eine bereits vorhandene Ergebnissmenge angehaengt werden
     *
     * @param  nodes       Ergebnisse, die im SearchTree angezeigt werden sollen.
     * @param  append      Ergebnisse anhaengen.
     * @param  listener    DOCUMENT ME!
     * @param  simpleSort  if true, sorts the search results alphabetically. Usually set to false, as a more specific
     *                     sorting order is wished.
     */
    public void setResultNodes(final Node[] nodes,
            final boolean append,
            final PropertyChangeListener listener,
            final boolean simpleSort) {
        setResultNodes(nodes, append, listener, simpleSort, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObjectNodeServerSearch getUnderlyingSearch() {
        return underlyingSearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  underlyingSearch  DOCUMENT ME!
     */
    public void setUnderlyingSearch(final MetaObjectNodeServerSearch underlyingSearch) {
        this.underlyingSearch = underlyingSearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        try {
            final SearchResultsTree tree = new SearchResultsTree();
            frame.setSize(100, 100);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.<br>
     * Diese Ergebnisse koennen an eine bereits vorhandene Ergebnissmenge angehaengt werden
     *
     * @param  nodes       Ergebnisse, die im SearchTree angezeigt werden sollen.
     * @param  append      Ergebnisse anhaengen.
     * @param  listener    DOCUMENT ME!
     * @param  simpleSort  if true, sorts the search results alphabetically. Usually set to false, as a more specific
     *                     sorting order is wished.
     * @param  sortActive  if false, no sort will be done (the value of simpleSort will be ignored, if sortActive is
     *                     false)
     */
    public void setResultNodes(final Node[] nodes,
            final boolean append,
            final PropertyChangeListener listener,
            final boolean simpleSort,
            final boolean sortActive) {
        if (log.isInfoEnabled()) {
            log.info("[SearchResultsTree] " + (append ? "appending" : "setting") + " '" + nodes.length + "' nodes"); // NOI18N
        }

        if ((append == true) && ((nodes == null) || (nodes.length < 1))) {
            return;
        } else if ((append == false) && ((nodes == null) || (nodes.length < 1))) {
            this.clear();
            return;
        } else if ((append == true) && (empty == false)) {
            resultNodes.addAll(Arrays.asList(filterNodesWithoutPermission(nodes)));
        } else {
            this.clear();
            resultNodes = new HashArrayList<Node>(Arrays.asList(filterNodesWithoutPermission(nodes)));
        }

        empty = false;
        refreshTree(true, listener, simpleSort, sortActive);
        fireResultNodesChanged();
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
    protected void refreshTree(final boolean initialFill) {
        refreshTree(initialFill, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  initialFill  DOCUMENT ME!
     * @param  listener     DOCUMENT ME!
     */
    private void refreshTree(final boolean initialFill, final PropertyChangeListener listener) {
        refreshTree(initialFill, listener, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  initialFill  DOCUMENT ME!
     * @param  listener     DOCUMENT ME!
     * @param  simpleSort   DOCUMENT ME!
     */
    private void refreshTree(final boolean initialFill,
            final PropertyChangeListener listener,
            final boolean simpleSort) {
        refreshTree(initialFill, listener, simpleSort, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  initialFill  sort DOCUMENT ME!
     * @param  listener     DOCUMENT ME!
     * @param  simpleSort   if true, sorts the search results alphabetically. Usually set to false, as a more specific
     *                      sorting order is wished.
     * @param  sortActive   simpleSort if false, no sort will be done (the value of simpleSort will be ignored, if
     *                      sortActive is false)
     */
    private void refreshTree(final boolean initialFill,
            final PropertyChangeListener listener,
            final boolean simpleSort,
            final boolean sortActive) {
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

                    refreshWorker = new RefreshTreeWorker(initialFill, simpleSort, sortActive);
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

        final Thread t = new Thread("SearchResultsTree checkForDynamicNodes()") {

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
        //J-
        boolean deleted = false;

        if ((selectedNodes == null) || (selectedNodes.length < 1)) {
            return deleted;
        }

        final List tmpNodeVector = new ArrayList();
        tmpNodeVector.addAll(Arrays.asList(resultNodes));

        for (int i = 0; i < tmpNodeVector.size(); i++) {
            for (int j = 0; j < selectedNodes.length; j++) {
                if ((i < tmpNodeVector.size()) && selectedNodes[j].equalsNode((Node)tmpNodeVector.get(i))) {
                    tmpNodeVector.remove(i);
                    deleted = true;
                    j--;
                }
            }
        }
        //J+

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
                fireResultNodesChanged();

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
        fireResultNodesCleared();
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
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSyncWithRenderer() {
        return syncWithRenderer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  syncWithRenderer  syncWithMap DOCUMENT ME!
     */
    public void setSyncWithRenderer(final boolean syncWithRenderer) {
        this.syncWithRenderer = syncWithRenderer;
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
        private boolean simpleSort = false;
        private boolean sortActive = true;

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

        /**
         *
         * @param initialFill
         * @param simpleSort if true, sorts the search results alphabetically.
         * Usually set to false, as a more specific sorting order is wished.
         */
        public RefreshTreeWorker(final boolean initialFill, boolean simpleSort) {
            this(initialFill, simpleSort, true);
        }

        /**
         *
         * @param initialFill
         * @param simpleSort if true, sorts the search results alphabetically.
         * Usually set to false, as a more specific sorting order is wished.
         * @param sortActive if false, no sort will be done (the value of
         * simpleSort will be ignored, if sortActive is false)
         */
        public RefreshTreeWorker(final boolean initialFill, boolean simpleSort, boolean sortActive) {
            this.initialFill = initialFill;
            comparator = new DirectedMetaObjectNodeComparator(ascending);
            this.simpleSort = simpleSort;
            this.sortActive = sortActive;
        }

        //~ Methods ------------------------------------------------------------
        @Override
        protected ArrayList<DefaultMetaTreeNode> doInBackground() throws Exception {
            Thread.currentThread().setName("RefreshTreeWorker");
            if (!isCancelled() && sortActive) {
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
                        //MetaTreeNodeRenderer renderer = new MetaTreeNodeRenderer();
                        if(!result.isEmpty()) {
                            setRowHeight(getCellRenderer().getTreeCellRendererComponent(SearchResultsTree.this, rootNode.getFirstChild(), false, false, false, 0, false).getHeight());
                        }

                        setLargeModel(result.size() > 15);

                        SearchResultsTree.this.firePropertyChange("browse", 0, 1); // NOI18N

                        defaultTreeModel.nodeStructureChanged(rootNode);

                        if (initialFill) {
                            syncWithMap();
                            syncWithRenderer();
                            checkForDynamicNodes();
                            if (simpleSort && sortActive) {
                                SearchResultsTree.this.sort(true);
                            }
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
