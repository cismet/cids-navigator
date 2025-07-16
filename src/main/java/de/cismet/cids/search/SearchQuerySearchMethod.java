/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.search.CidsSearchExecutor;
import Sirius.navigator.search.dynamic.SearchControlPanel;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.SearchRuntimeException;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.NbBundle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;
import de.cismet.cids.server.search.builtin.QueryEditorCountStatement;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = QuerySearchMethod.class)
public class SearchQuerySearchMethod implements QuerySearchMethod, PropertyChangeListener, ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchQuerySearchMethod.class);

    //~ Instance fields --------------------------------------------------------

    private QuerySearch querySearch;
    private boolean searching = false;
    private SwingWorker<Node[], Void> searchThread;
    private SwingWorker<Long, Void> searchCountThread;

    private final ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setQuerySearch(final QuerySearch querySearch) {
        this.querySearch = querySearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSearching() {
        return searching;
    }

    @Override
    public void actionPerformed(final Object layer, final String query) {
        if (LOG.isInfoEnabled()) {
            LOG.info((searching ? "Cancel" : "Search") + " button was clicked.");
        }

        if (searching) {
            if (searchThread != null) {
                searchThread.cancel(true);
            }
            if (searchCountThread != null) {
                searchCountThread.cancel(true);
            }
            ComponentRegistry.getRegistry().getSearchResultsTree().cancelNodeLoading();
        } else {
            final MetaObjectNodeServerSearch search = querySearch.getServerSearch();

            ComponentRegistry.getRegistry().getSearchResultsTree().addPropertyChangeListener("browse", this);

            searchThread = CidsSearchExecutor.searchAndDisplayResults(
                    search,
                    this,
                    this,
                    false,
                    true,
                    getConnectionContext());

            if (querySearch.getPanginationPanel().getParent() != null) {
                searchCountThread = new SwingWorker<Long, Void>() {

                        @Override
                        protected Long doInBackground() throws Exception {
                            final List<Long> result = (List<Long>)SessionManager.getProxy()
                                        .customServerSearch(
                                                SessionManager.getSession().getUser(),
                                                new QueryEditorCountStatement(
                                                    SessionManager.getSession().getUser().getDomain(),
                                                    querySearch.getMetaClass().getTableName(),
                                                    querySearch.getWhereCause()),
                                                getConnectionContext());
                            return result.get(0);
                        }

                        @Override
                        protected void done() {
                            try {
                                final Long count = get();
                                if (!isCancelled()) {
                                    querySearch.getPanginationPanel().setTotal(count);
                                }
                            } catch (final Exception ex) {
                                LOG.error(ex, ex);
                            }
                        }
                    };
                searchCountThread.execute();
            }

            searching = true;
            querySearch.setControlsAccordingToState(searching);
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!(evt.getSource() instanceof SwingWorker)) {
            LOG.warn("Listened object is not of type 'SwingWorker'. Skipping process of event: '" + evt + "'.");
            return;
        }
        if (!"state".equalsIgnoreCase(evt.getPropertyName())) {
            return;
        }

        final SwingWorker source = (SwingWorker)evt.getSource();

        if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
            if (source.isCancelled()) {
                searching = false;
                querySearch.setControlsAccordingToState(searching);
            } else {
                int results = 0;

                try {
                    final Object obj = source.get();
                    if (obj instanceof Node[]) {
                        results = ((Node[])obj).length;
                    }
                    if (obj instanceof Collection) {
                        results = ((Collection)obj).size();
                    }
                } catch (InterruptedException ex) {
                    LOG.error("Search result can't be get().", ex);
                } catch (ExecutionException ex) {
                    LOG.error("Search result can't be get().", ex);

                    final JFrame mainWindow = ComponentRegistry.getRegistry().getMainWindow();

                    if ((ex.getCause() instanceof SearchRuntimeException)
                                && (mainWindow != null)) {
                        final SearchRuntimeException srx = (SearchRuntimeException)((ExecutionException)ex).getCause();

                        // If there is a SearchRuntimeException, the Error message should be displayed
                        final String title = NbBundle.getMessage(
                                CidsSearchExecutor.class,
                                "CidsSearchExecutor.searchAndDispolayResults.done().title");

                        JOptionPane.showMessageDialog(mainWindow, srx.getMessage(), title, JOptionPane.ERROR_MESSAGE);
                    } else {
                        final ErrorInfo errorInfo = new ErrorInfo(
                                org.openide.util.NbBundle.getMessage(
                                    SearchControlPanel.class,
                                    "SearchControlPanel.propertyChange(PropertyChangeEvent).JOptionPane_anon.title"),
                                org.openide.util.NbBundle.getMessage(
                                    SearchControlPanel.class,
                                    "SearchControlPanel.propertyChange(PropertyChangeEvent).JOptionPane_anon.message"),
                                null,
                                "ERROR",
                                ex.getCause(),
                                Level.WARNING,
                                null);

                        JXErrorPane.showDialog(querySearch.getRootPane(), errorInfo);
                    }
                }

                // this class is used as listener of the search thread and as a listener for the thread which
                // refreshes the SearchResultsTree. So this point is reached by on of two conditions:
                // - The search thread is done.
                // - Refreshing the SearchResultsTree is done.
                // SearchControlPanel can display normal mode only if:
                // - Search is done and has no results (refreshing the SearchResultsTree is not started).
                // - Or refreshing SearchResultsTree is done.
                if ((source.equals(searchThread) && (results == 0))
                            || !source.equals(searchThread)) {
                    searching = false;
                    querySearch.setControlsAccordingToState(searching);
                }
            }
        }
    }

    @Override
    public String toString() {
        return NbBundle.getMessage(SearchQuerySearchMethod.class, "SearchQuerySearchMethod.toString");
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
