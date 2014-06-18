/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.navigator.search.CidsSearchExecutor;
import Sirius.navigator.search.dynamic.SearchControlPanel;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.NbBundle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.SwingWorker;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = QuerySearchMethod.class)
public class SearchQuerySearchMethod implements QuerySearchMethod, PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchQuerySearchMethod.class);

    //~ Instance fields --------------------------------------------------------

    private QuerySearch querySearch;
    private boolean searching = false;
    private SwingWorker<Node[], Void> searchThread;

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setQuerySearch(final QuerySearch querySearch) {
        this.querySearch = querySearch;
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
            ComponentRegistry.getRegistry().getSearchResultsTree().cancelNodeLoading();
        } else {
            final MetaObjectNodeServerSearch search = querySearch.getServerSearch();

            ComponentRegistry.getRegistry().getSearchResultsTree().addPropertyChangeListener("browse", this);
            searchThread = CidsSearchExecutor.searchAndDisplayResults(
                    search,
                    this,
                    this,
                    false,
                    true);
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
}
