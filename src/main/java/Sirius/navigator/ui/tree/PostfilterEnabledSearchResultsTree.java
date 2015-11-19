/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.ui.tree.postfilter.PostFilter;
import Sirius.navigator.ui.tree.postfilter.PostFilterGUI;
import Sirius.navigator.ui.tree.postfilter.PostFilterListener;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import org.openide.util.Lookup;

import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.tools.collections.HashArrayList;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class PostfilterEnabledSearchResultsTree extends SearchResultsTree implements PostFilterListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(PostfilterEnabledSearchResultsTree.class);

    //~ Instance fields --------------------------------------------------------

    private HashArrayList<Node> resultNodesOriginal = new HashArrayList<Node>();

    private ArrayList<PostFilterGUI> availablePostFilterGUIs = new ArrayList<PostFilterGUI>();

    private ArrayList<PostFilter> filterArray = new ArrayList<PostFilter>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PostfilterEnabledSearchResultsTree object.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public PostfilterEnabledSearchResultsTree() throws Exception {
        final Collection<? extends PostFilterGUI> lookupResult = Lookup.getDefault().lookupAll(PostFilterGUI.class);
        availablePostFilterGUIs = new ArrayList<PostFilterGUI>(lookupResult);

        Collections.sort(availablePostFilterGUIs, new Comparator<PostFilterGUI>() {

                @Override
                public int compare(final PostFilterGUI o1, final PostFilterGUI o2) {
                    return o1.getDisplayOrderKeyPrio().compareTo(o2.getDisplayOrderKeyPrio());
                }
            });

        LOG.info(availablePostFilterGUIs.size() + " post filter GUIs available");
    }

    /**
     * Creates a new PostfilterEnabledSearchResultsTree object.
     *
     * @param   useThread       DOCUMENT ME!
     * @param   maxThreadCount  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public PostfilterEnabledSearchResultsTree(final boolean useThread, final int maxThreadCount) throws Exception {
        super(useThread, maxThreadCount);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isFiltered() {
        return resultNodesOriginal.size() > resultNodes.size();
    }

    @Override
    public void setResultNodes(final Node[] nodes,
            final boolean append,
            final PropertyChangeListener listener,
            final boolean simpleSort,
            final boolean sortActive) {
        super.setResultNodes(nodes, append, listener, simpleSort, sortActive); // To change body of generated
        // methods, choose Tools | Templates.
        resultNodesOriginal = new HashArrayList<Node>(super.resultNodes);
    }

    @Override
    public void setResultNodes(final Node[] nodes) {
        super.setResultNodes(nodes); // To change body of generated methods, choose Tools | Templates.
        resultNodesOriginal = new HashArrayList<Node>(super.resultNodes);
    }

    @Override
    public void setResultNodes(final Node[] nodes,
            final boolean append,
            final PropertyChangeListener listener,
            final boolean simpleSort) {
        super.setResultNodes(nodes, append, listener, simpleSort); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void setResultNodes(final Node[] nodes, final boolean append, final PropertyChangeListener listener) {
        super.setResultNodes(nodes, append, listener); // To change body of generated methods, choose Tools | Templates.
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodes       DOCUMENT ME!
     * @param  append      DOCUMENT ME!
     * @param  listener    DOCUMENT ME!
     * @param  simpleSort  DOCUMENT ME!
     * @param  sortActive  DOCUMENT ME!
     */
    public void internalSetResultNodes(final Node[] nodes,
            final boolean append,
            final PropertyChangeListener listener,
            final boolean simpleSort,
            final boolean sortActive) {
        super.muteResultNodeListeners = true;

        super.setResultNodes(nodes, append, listener, simpleSort, sortActive); // To change body of generated
        // methods, choose Tools | Templates.
        super.muteResultNodeListeners = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodes  DOCUMENT ME!
     */
    public void internalSetResultNodes(final Node[] nodes) {
        super.muteResultNodeListeners = true;
        super.setResultNodes(nodes); // To change body of generated methods, choose Tools | Templates.
        if (nodes.length == 0) {
            // super.setResultNodes(nodes);  does not referesh the tree by default
            // do it manually here:
            refreshTree(true);
        }
        super.muteResultNodeListeners = false;
    }

    /**
     * Nach jedem Suchergebnissrefresh wird ein Lookup auf die POstfilterGUIs gemacht Eine Änderung wird dann über den
     * PostFilterGUIContainerListener an den SearchresultTreePanel gefeuert Auf den GUI wird setSearchResults aufgerufen
     * Die GUIs initialisieren ihre GUI aus den search Results wenn in den PostfilterGUI ein FilterUpdate passiert wird
     * über einen Listener hier Bescheid gesagt wie geht das Chaining???
     *
     * @return  DOCUMENT ME!
     */
    public ArrayList<PostFilterGUI> getAvailablePostFilterGUIs() {
        return availablePostFilterGUIs;
    }

    @Override
    public void filterContentChanged(final PostFilter pf) {
        if (!filterArray.contains(pf)) {
            filterArray.add(pf);
        }
        filter();
    }

    /**
     * DOCUMENT ME!
     */
    public void clearFilter() {
        filterArray.clear();
        internalSetResultNodes(resultNodesOriginal.toArray(new Node[0]));
        super.fireResultNodesChanged();
    }

    /**
     * DOCUMENT ME!
     */
    void filter() {
        Collection<Node> nodes = new ArrayList<Node>(resultNodesOriginal);
        Collections.sort(filterArray, new Comparator<PostFilter>() {

                @Override
                public int compare(final PostFilter o1, final PostFilter o2) {
                    return o1.getFilterChainOrderKeyPrio().compareTo(o2.getFilterChainOrderKeyPrio());
                }
            });
        if (LOG.isDebugEnabled()) {
            LOG.debug("applying " + filterArray.size() + " post filters to " + nodes.size() + " nodes");
        }
        for (final PostFilter pf : filterArray) {
            nodes = pf.filter(nodes);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(nodes.size() + " nodes left after applying " + filterArray.size() + " filters to"
                        + resultNodesOriginal.size() + " nodes");
        }

        internalSetResultNodes(nodes.toArray(new Node[0]));
        super.fireResultNodesFiltered();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   collection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Collection<MetaClass> getAllMetaClassesForNodeCollection(final Collection<Node> collection) {
        final ArrayList<MetaClass> result = new ArrayList<MetaClass>();
        for (final Node n : collection) {
            final MetaClass mc = ClassCacheMultiple.getMetaClass(n.getDomain(), n.getClassId());
            if (!result.contains(mc)) {
                result.add(mc);
            }
        }
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   collection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Collection<String> getAllTableNamesForNodeCollection(final Collection<Node> collection) {
        final ArrayList<MetaClass> classes = new ArrayList<MetaClass>(getAllMetaClassesForNodeCollection(collection));
        final ArrayList<String> result = new ArrayList<String>(classes.size());
        for (final MetaClass mc : classes) {
            result.add(mc.getTableName());
        }
        return result;
    }
}
