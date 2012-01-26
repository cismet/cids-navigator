/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.NavigatorConcurrency;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.types.treenode.WaitTreeNode;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class MetaTreeRefreshCache implements TreeModelListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(MetaTreeRefreshCache.class);
    private static final transient Pattern WC_PATTERN = Pattern.compile("(?<!\\\\)\\*{1}|(?<!\\\\)\\?{1}"); // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final transient Map<String, DefaultMetaTreeNode> nodeCache;
    private final transient ExecutorService cacheUpdateDispatcher;

    // we don't need sync on this var since wrong states are tolerable
    private transient boolean valid;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaTreeRefreshCache object.
     */
    public MetaTreeRefreshCache() {
        valid = true;
        nodeCache = new HashMap<String, DefaultMetaTreeNode>();
        cacheUpdateDispatcher = Executors.newSingleThreadExecutor(NavigatorConcurrency.createThreadFactory(
                    "meta-tree-refresh-cache-update-dispatcher", // NOI18N
                    new CacheExceptionHandler()));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * The cache allows wildcards in the artificalId string. There are two wildcard characters:
     *
     * @param   artificialId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<DefaultMetaTreeNode> get(final String artificialId) {
        if (valid) {
            final Set<DefaultMetaTreeNode> nodes = new HashSet<DefaultMetaTreeNode>();

            final Matcher m = WC_PATTERN.matcher(artificialId);
            if (m.find()) {
                m.reset();
                final StringBuilder regexbuilder = new StringBuilder();

                int beginIndex = 0;
                while (m.find()) {
                    regexbuilder.append(Pattern.quote(artificialId.substring(beginIndex, m.start())));
                    regexbuilder.append((artificialId.charAt(m.start()) == '*') ? ".*" : ".?"); // NOI18N

                    beginIndex = m.end();
                }

                if (beginIndex < artificialId.length()) {
                    regexbuilder.append(artificialId.substring(beginIndex));
                }

                final Pattern regex = Pattern.compile(regexbuilder.toString());

                for (final String key : nodeCache.keySet()) {
                    if (regex.matcher(key).matches()) {
                        nodes.add(nodeCache.get(key));
                    }
                }
            } else {
                final DefaultMetaTreeNode node = nodeCache.get(artificialId);
                if (node != null) {
                    nodes.add(node);
                }
            }

            return nodes;
        } else {
            LOG.warn("cache is invalid, tree ui probably not accurate anymore, perform manual tree refresh"); // NOI18N
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final String s = "rainfall.*";
        final Matcher m = WC_PATTERN.matcher(s);
        final StringBuilder sb = new StringBuilder();

        int beginIndex = 0;
        while (m.find()) {
            sb.append(Pattern.quote(s.substring(beginIndex, m.start())));
            beginIndex = m.end();
            if (s.charAt(m.start()) == '*') {
                sb.append(".*");
            } else {
                sb.append(".?");
            }
        }

        if (beginIndex < s.length()) {
            sb.append(s.substring(beginIndex));
        }

        System.out.println(sb.toString());
        System.out.println(beginIndex);
        System.out.println(s.length());

        final Pattern p = Pattern.compile(sb.toString());
        System.out.println("***="
                    + p.matcher("***").matches());

//        final String[] anySplit = "abc*ds\\*gh?beda\\* dalskd*".split(WC_ANY_SPLIT_PATTERN);
//        final StringBuilder regex = new StringBuilder();
//        for(final String anyPart : anySplit){
//            final StringBuilder sb = new StringBuilder();
//            final String[] oneSplit = anyPart.split(WC_ONE_SPLIT_PATTERN);
//            for(final String onePart : oneSplit){
//                sb.append(Pattern.quote(onePart));
//                sb.append(".?");
//            }
//
//            regex.append(sb);
//            regex.append(".*");
//        }
//
//
//
//        System.out.println(regex.toString());
    }

    /**
     * The cache will be cleared and initialised with the currently expanded paths.
     */
    private void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("init refresh cache"); // NOI18N
        }

        valid = true;
        nodeCache.clear();

        // NOTE: Maybe we want to use a reference initialised by the constructor to not bind this implementation to the
        // current registry implementation and/or the navigator at all. If so consider to change the DefaultMetaTreeNode
        // (or at least extract an interface) to minimise/loosen dependencies.
        final MetaCatalogueTree tree = ComponentRegistry.getRegistry().getCatalogueTree();

        final Object root = tree.getModel().getRoot();

        if (root == null) {
            LOG.warn("cannot initialise tree refresh cache, empty root"); // NOI18N

            return;
        }

        final TreePath path = new TreePath(root);
        final Enumeration<TreePath> paths = tree.getExpandedDescendants(path);

        if (paths == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("nothing to initialise, root not expanded"); // NOI18N
            }

            return;
        }

        final List nodes = new ArrayList();

        while (paths.hasMoreElements()) {
            nodes.add(paths.nextElement().getLastPathComponent());
        }

        cacheUpdateDispatcher.submit(new CacheUpdater(nodes.toArray(), true));
    }

    @Override
    public void treeNodesChanged(final TreeModelEvent e) {
        // not needed currently, probably interface an advanced state of refresh mechanism this will be handled
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValid() {
        return valid;
    }

    @Override
    public void treeNodesInserted(final TreeModelEvent e) {
        if (!valid) {
            LOG.warn("cache is invalid, won't update cache, need reinit"); // NOI18N

            return;
        }

        final Object[] inserted = e.getChildren();

        if (inserted == null) {
            // nothing to do
            return;
        }

        cacheUpdateDispatcher.submit(new CacheUpdater(inserted, true));
    }

    @Override
    public void treeNodesRemoved(final TreeModelEvent e) {
        if (!valid) {
            LOG.warn("cache is invalid, won't update cache, need reinit"); // NOI18N

            return;
        }

        final Object[] removed = e.getChildren();

        if (removed == null) {
            // nothing to do
            return;
        }

        cacheUpdateDispatcher.submit(new CacheUpdater(removed, false));
    }

    @Override
    public void treeStructureChanged(final TreeModelEvent e) {
        if (e.getPath().length == 1) {
            // root was changed, first clear the current cache,
            // then scan all open paths for artificial ids since the current hard refresh operations use "explore" to
            // expand the tree path to the previously selected node and afterwards fire a structure changed with the
            // root node as origin
            init();
        } else if (!valid) {
            LOG.warn("cache is invalid, won't update cache, need reinit"); // NOI18N
        } else {
            // unfortunately Navigator fires tree structure changed and not any inserted/removed events
            final Object o = e.getTreePath().getLastPathComponent();

            if (o instanceof TreeNode) {
                if (o instanceof WaitTreeNode) {
                    // ignore
                    return;
                }

                final TreeNode dmtn = (TreeNode)o;
                final List children = Collections.list(dmtn.children());

                if (children.isEmpty()) {
                    return;
                }

                cacheUpdateDispatcher.submit(new CacheUpdater(children.toArray(), true));
            } else {
                LOG.warn("illegal node in tree: " + o); // NOI18N
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class CacheUpdater implements Runnable {

        //~ Instance fields ----------------------------------------------------

        private final transient Object[] nodes;
        private final transient boolean insert;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CacheUpdater object.
         *
         * @param  nodes   DOCUMENT ME!
         * @param  insert  DOCUMENT ME!
         */
        public CacheUpdater(final Object[] nodes, final boolean insert) {
            this.nodes = nodes;
            this.insert = insert;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            for (final Object o : nodes) {
                if (o instanceof DefaultMetaTreeNode) {
                    if (o instanceof WaitTreeNode) {
                        // ignore WaitTreeNodes, they're only temporary
                        continue;
                    } else if (o instanceof RootTreeNode) {
                        // ignore RootTreeNode, it's not of interest to refresh requests
                        continue;
                    }

                    final DefaultMetaTreeNode dmtn = (DefaultMetaTreeNode)o;
                    if (dmtn.getNode() == null) {
                        LOG.warn("DefaultMetaTreeNode without backing Node: " + o); // NOI18N
                    } else {
                        final Node node = dmtn.getNode();
                        final String artificialId = node.getArtificialId();

                        if (artificialId != null) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("found artificial id for node, updating cache [node=" + node // NOI18N
                                            + "|artificalId=" // NOI18N
                                            + artificialId + "]"); // NOI18N
                            }

                            if (nodeCache.containsKey(artificialId) == insert) {
                                valid = false;
                                nodeCache.clear();

                                final String keyword = insert ? "already" : "not";                       // NOI18N
                                final String message = "the artificial id is " + keyword + " in cache, " // NOI18N
                                            + "cache corrupt or illegal tree, invalidating cache: "      // NOI18N
                                            + artificialId;

                                LOG.error(message);
                                throw new IllegalStateException(message);
                            }

                            if (insert) {
                                nodeCache.put(artificialId, dmtn);
                            } else {
                                nodeCache.remove(artificialId);
                            }
                        }
                    }
                } else {
                    LOG.warn("received unknown node type: " + o); // NOI18N
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class CacheExceptionHandler implements Thread.UncaughtExceptionHandler {

        //~ Static fields/initializers -----------------------------------------

        private static final transient Logger LOG = Logger.getLogger(CacheExceptionHandler.class);

        //~ Methods ------------------------------------------------------------

        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            if (e instanceof Error) {
                LOG.fatal("encountered error in thread: " + t, e);

                throw (Error)e;
            } else {
                LOG.error("encountered exception in refresh cache, " // NOI18N
                            + "tree ui will probably be not accurate anymore, thread: " + t, // NOI18N
                    e);
            }
        }
    }
}
