/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.search.dynamic.SearchControlDialog;
import Sirius.navigator.search.dynamic.SearchProgressDialog;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.status.DefaultStatusChangeSupport;

import Sirius.server.middleware.types.Node;

import java.awt.EventQueue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingWorker;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;
import de.cismet.cids.server.search.SearchResultListener;
import de.cismet.cids.server.search.SearchResultListenerProvider;

import de.cismet.connectioncontext.ClientConnectionContext;

import de.cismet.tools.CismetThreadPool;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public final class CidsSearchExecutor {

    //~ Static fields/initializers ---------------------------------------------

    private static final DefaultStatusChangeSupport dscs = new DefaultStatusChangeSupport(new Object());
    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            CidsSearchExecutor.class);
    private static SearchProgressDialog searchProgressDialog;
    private static SearchControlDialog searchControlDialog;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    @Deprecated
    public static void searchAndDisplayResultsWithDialog(final MetaObjectNodeServerSearch search) {
        searchAndDisplayResultsWithDialog(search, ClientConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search             DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public static void searchAndDisplayResultsWithDialog(final MetaObjectNodeServerSearch search,
            final ClientConnectionContext connectionContext) {
        searchAndDisplayResultsWithDialog(search, false, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search      DOCUMENT ME!
     * @param  simpleSort  DOCUMENT ME!
     */
    @Deprecated
    public static void searchAndDisplayResultsWithDialog(final MetaObjectNodeServerSearch search,
            final boolean simpleSort) {
        searchAndDisplayResultsWithDialog(search, simpleSort, ClientConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search             DOCUMENT ME!
     * @param  simpleSort         if true, sorts the search results alphabetically. Usually set to false, as a more
     *                            specific sorting order is wished.
     * @param  connectionContext  DOCUMENT ME!
     */
    public static void searchAndDisplayResultsWithDialog(final MetaObjectNodeServerSearch search,
            final boolean simpleSort,
            final ClientConnectionContext connectionContext) {
        if (searchControlDialog == null) {
            searchControlDialog = new SearchControlDialog(ComponentRegistry.getRegistry().getNavigator(),
                    true,
                    connectionContext);
            searchControlDialog.pack();
        }

        searchControlDialog.setSearch(search);

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    searchControlDialog.startSearch(simpleSort);
                }
            });
        StaticSwingTools.showDialog(searchControlDialog);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search                 DOCUMENT ME!
     * @param   listener               DOCUMENT ME!
     * @param   searchResultsListener  DOCUMENT ME!
     * @param   connectionContext      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static SwingWorker<Node[], Void> searchAndDisplayResultsWithDialog(final MetaObjectNodeServerSearch search,
            final PropertyChangeListener listener,
            final PropertyChangeListener searchResultsListener,
            final ClientConnectionContext connectionContext) {
        final SwingWorker<Node[], Void> worker = new SwingWorker<Node[], Void>() {

                PropertyChangeListener cancelListener = null;

                @Override
                protected Node[] doInBackground() throws Exception {
                    Thread.currentThread().setName("CidsSearchExecutor searchAndDisplayResultsWithDialog()");
                    EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                cancelListener = new PropertyChangeListener() {

                                        @Override
                                        public void propertyChange(final PropertyChangeEvent evt) {
                                            log.fatal("CANCEL-->" + evt);
                                            cancel(true);
                                        }
                                    };
                                dscs.addPropertyChangeListener(cancelListener);
                                getSearchProgressDialog().pack();
                                getSearchProgressDialog().setLabelAnimation(true);
                                StaticSwingTools.showDialog(getSearchProgressDialog());
                            }
                        });

                    final Collection res = SessionManager.getProxy()
                                .customServerSearch(SessionManager.getSession().getUser(),
                                    search,
                                    connectionContext);
                    if (!isCancelled()) {
                        final ArrayList<Node> aln = new ArrayList<Node>(res.size());
                        for (final Object o : res) {
                            aln.add((Node)o);
                        }

                        final Node[] ret = aln.toArray(new Node[0]);
                        if (!isCancelled()) {
                            MethodManager.getManager().showSearchResults(search, ret, false, searchResultsListener);
                        }
                        return ret;
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        if (!isCancelled()) {
                            final Node[] res = get();

                            getSearchProgressDialog().setVisible(false);
                            getSearchProgressDialog().setLabelAnimation(false);
                        }
                    } catch (Exception ex) {
                        log.fatal("suchproblem", ex);
                    }
                    dscs.removePropertyChangeListener(cancelListener);
                }
            };

        worker.addPropertyChangeListener(listener);

        CismetThreadPool.execute(worker);

        return worker;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search                      DOCUMENT ME!
     * @param   searchListener              DOCUMENT ME!
     * @param   searchResultsTreeListener   DOCUMENT ME!
     * @param   suppressEmptyResultMessage  DOCUMENT ME!
     * @param   connectionContext           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static SwingWorker<Node[], Void> searchAndDisplayResults(final MetaObjectNodeServerSearch search,
            final PropertyChangeListener searchListener,
            final PropertyChangeListener searchResultsTreeListener,
            final boolean suppressEmptyResultMessage,
            final ClientConnectionContext connectionContext) {
        return searchAndDisplayResults(
                search,
                searchListener,
                searchResultsTreeListener,
                suppressEmptyResultMessage,
                false,
                connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search                      The search to perform.
     * @param   searchListener              A listener which will be informed about status changes of search thread.
     *                                      Usually a SearchControlPanel.
     * @param   searchResultsTreeListener   A listener which will be informed about status changes of the thread which
     *                                      refreshes the SearchResultsTree. Usually the same SearchControlPanel as
     *                                      listener.
     * @param   suppressEmptyResultMessage  A flag indicating that the user shouldn't be informed about an empty result.
     *                                      Since this message is generated in the called method to display the search
     *                                      results in the SearchResultsTree this flag decides about calling thismethod
     *                                      or not.
     * @param   simpleSort                  if true, sorts the search results alphabetically. Usually set to false, as a
     *                                      more specific sorting order is wished.
     * @param   connectionContext           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static SwingWorker<Node[], Void> searchAndDisplayResults(final MetaObjectNodeServerSearch search,
            final PropertyChangeListener searchListener,
            final PropertyChangeListener searchResultsTreeListener,
            final boolean suppressEmptyResultMessage,
            final boolean simpleSort,
            final ClientConnectionContext connectionContext) {
        final SwingWorker<Node[], Void> worker = new SwingWorker<Node[], Void>() {

                @Override
                protected Node[] doInBackground() throws Exception {
                    Thread.currentThread().setName("CidsSearchExecutor searchAndDisplayResults()");

                    Node[] result = null;
                    final Collection searchResult = SessionManager.getProxy()
                                .customServerSearch(SessionManager.getSession().getUser(),
                                    search,
                                    connectionContext);

                    if (isCancelled()) {
                        return result;
                    }

                    final ArrayList<Node> nodes = new ArrayList<Node>(searchResult.size());

                    for (final Object singleSearchResult : searchResult) {
                        nodes.add((Node)singleSearchResult);

                        if (isCancelled()) {
                            return result;
                        }
                    }

                    result = nodes.toArray(new Node[0]);
                    if (!isCancelled()) {
                        if (!suppressEmptyResultMessage || (result.length > 0)) {
                            MethodManager.getManager()
                                    .showSearchResults(search, result, false, searchResultsTreeListener, simpleSort);
                        }
                    }

                    if (search instanceof SearchResultListenerProvider) {
                        final SearchResultListenerProvider searchResultListenerProvider = (SearchResultListenerProvider)
                            search;
                        final SearchResultListener searchResultListener =
                            searchResultListenerProvider.getSearchResultListener();
                        if (searchResultListener != null) {
                            searchResultListener.searchDone(new ArrayList(searchResult));
                            searchResultListenerProvider.setSearchResultListener(null);
                        }
                    }
                    return result;
                }
            };

        worker.addPropertyChangeListener(searchListener);

        CismetThreadPool.execute(worker);

        return worker;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static SearchProgressDialog getSearchProgressDialog() {
        if (searchProgressDialog == null) {
            searchProgressDialog = new SearchProgressDialog(StaticSwingTools.getFirstParentFrame(
                        ComponentRegistry.getRegistry().getDescriptionPane()),
                    dscs);
        }
        return searchProgressDialog;
    }
}
