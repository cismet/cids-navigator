/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchControlPanel.java
 *
 * Created on 13.12.2011, 10:36:40
 */
package Sirius.navigator.search.dynamic;

import Sirius.navigator.search.CidsSearchExecutor;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.SearchRuntimeException;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.NbBundle;

import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.URL;

import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class SearchControlPanel extends javax.swing.JPanel implements PropertyChangeListener,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchControlPanel.class);

    //~ Instance fields --------------------------------------------------------

    private SearchControlListener listener;
    private SwingWorker<Node[], Void> searchThread;
    private SwingWorker<Boolean, Void> searchPreparationThread;
    private boolean searching = false;
    private ImageIcon iconSearch;
    private ImageIcon iconCancel;

    private boolean simpleSort;
    private final ConnectionContext connectionContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearchCancel;
    private org.jdesktop.swingx.JXBusyLabel lblBusyIcon;
    private javax.swing.Box.Filler strGap;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchControlPanel object.
     *
     * @param  listener  DOCUMENT ME!
     */
    public SearchControlPanel(final SearchControlListener listener) {
        this(listener, ConnectionContext.createDeprecated());
    }

    /**
     * Creates new form SearchControlPanel.
     *
     * @param  listener           DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public SearchControlPanel(final SearchControlListener listener, final ConnectionContext connectionContext) {
        if (listener == null) {
            LOG.warn("Given listener is null. Panel won't work.");
        }

        this.listener = listener;
        this.connectionContext = connectionContext;

        initComponents();

        final URL iconSearch = getClass().getResource(
                "/Sirius/navigator/search/dynamic/SearchControlPanel_btnSearchCancel.png");
        if (iconSearch != null) {
            this.iconSearch = new ImageIcon(iconSearch);
        } else {
            this.iconSearch = new ImageIcon();
        }

        final URL iconCancel = getClass().getResource(
                "/Sirius/navigator/search/dynamic/SearchControlPanel_btnSearchCancel_cancel.png");
        if (iconCancel != null) {
            this.iconCancel = new ImageIcon(iconCancel);
        } else {
            this.iconCancel = new ImageIcon();
        }

        btnSearchCancel.setIcon(this.iconSearch);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblBusyIcon = new org.jdesktop.swingx.JXBusyLabel(new java.awt.Dimension(20, 20));
        strGap = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0),
                new java.awt.Dimension(5, 25),
                new java.awt.Dimension(5, 32767));
        btnSearchCancel = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(125, 25));
        setPreferredSize(new java.awt.Dimension(125, 25));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 0, 0));

        lblBusyIcon.setEnabled(false);
        add(lblBusyIcon);
        add(strGap);

        btnSearchCancel.setText(org.openide.util.NbBundle.getMessage(
                SearchControlPanel.class,
                "SearchControlPanel.btnSearchCancel.text"));        // NOI18N
        btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchControlPanel.class,
                "SearchControlPanel.btnSearchCancel.toolTipText")); // NOI18N
        btnSearchCancel.setMaximumSize(new java.awt.Dimension(
                100,
                (new Double(getMaximumSize().getHeight()).intValue())));
        btnSearchCancel.setMinimumSize(new java.awt.Dimension(
                100,
                (new Double(getMinimumSize().getHeight()).intValue())));
        btnSearchCancel.setPreferredSize(new java.awt.Dimension(
                100,
                (new Double(getPreferredSize().getHeight()).intValue())));
        btnSearchCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnSearchCancelActionPerformed(evt);
                }
            });
        add(btnSearchCancel);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnSearchCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnSearchCancelActionPerformed
        if (LOG.isInfoEnabled()) {
            LOG.info((searching ? "Cancel" : "Search") + " button was clicked.");
        }

        if (searching) {
            if (listener == null) {
                LOG.error("Search should be started, but listener is null.");
                return;
            }

            if (searchThread != null) {
                searchThread.cancel(true);
            }

            if (searchPreparationThread != null) {
                searchPreparationThread.cancel(true);
            }

            ComponentRegistry.getRegistry().getSearchResultsTree().cancelNodeLoading();
        } else {
            if (listener == null) {
                LOG.error("Search should be started, but listener is null.");
                return;
            }
            final MetaObjectNodeServerSearch search = listener.assembleSearch();
            if (search == null) {
                LOG.warn("The listener didn't provide a search.");
                return;
            }

            searching = true;
            setControlsAccordingToState();
            listener.searchStarted();

            searchPreparationThread = new SwingWorker<Boolean, Void>() {

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return SearchControlPanel.this.checkIfSearchShouldBeStarted(this, search);
                    }

                    @Override
                    protected void done() {
                        boolean startSearch = false;
                        try {
                            startSearch = get();
                        } catch (InterruptedException ex) {
                            LOG.error("Could not start search.", ex);
                        } catch (ExecutionException ex) {
                            LOG.error("Could not start search.", ex);
                        } catch (CancellationException ex) {
                            LOG.info("Search cancelled.", ex);
                        }

                        if (startSearch) {
                            ComponentRegistry.getRegistry()
                                    .getSearchResultsTree()
                                    .addPropertyChangeListener("browse", SearchControlPanel.this);
                            searchThread = CidsSearchExecutor.searchAndDisplayResults(
                                    search,
                                    SearchControlPanel.this,
                                    SearchControlPanel.this,
                                    listener.suppressEmptyResultMessage(),
                                    simpleSort,
                                    getConnectionContext());
                        } else {
                            searching = false;
                            setControlsAccordingToState();
                            listener.searchCanceled();
                        }
                    }
                };
            searchPreparationThread.execute();
        }
    } //GEN-LAST:event_btnSearchCancelActionPerformed

    /**
     * This method is called before the search is actually started and gives a possibility to abort the search. In the
     * default implementation it always returns true, but subclasses can override this method.
     *
     * <p>Note: the method is called in the doInBackground() of a SwingWorker and therefor not in the EDT</p>
     *
     * @param   calledBySwingWorker  the SwingWorker instance by which this method was called, to check e.g. if the
     *                               SwingWorker was canceled
     * @param   search               the search, which will be started or aborted later on
     *
     * @return  true: the search will be started. False: the search will be aborted
     */
    public boolean checkIfSearchShouldBeStarted(final SwingWorker calledBySwingWorker,
            final MetaObjectNodeServerSearch search) {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    private void setControlsAccordingToState() {
        if (searching) {
            btnSearchCancel.setText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel_cancel.text"));        // NOI18N
            btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel_cancel.toolTipText")); // NOI18N
            btnSearchCancel.setIcon(iconCancel);
            lblBusyIcon.setEnabled(true);
            lblBusyIcon.setBusy(true);
        } else {
            btnSearchCancel.setText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel.text"));               // NOI18N
            btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel.toolTipText"));        // NOI18N
            btnSearchCancel.setIcon(iconSearch);
            lblBusyIcon.setEnabled(false);
            lblBusyIcon.setBusy(false);
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
        if (listener == null) {
            LOG.error("Got an event from a search thread but listener is null. Skip processing.");
            return;
        }

        final SwingWorker source = (SwingWorker)evt.getSource();

        if (SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
            if (source.isCancelled()) {
                searching = false;
                listener.searchCanceled();
                setControlsAccordingToState();
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
                                SearchControlPanel.class,
                                "SearchControlPanel.propertyChange(PropertyChangeEvent).title");

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

                        JXErrorPane.showDialog(getRootPane(), errorInfo);
                    }
                }

                // SearchControlPanel is used as listener of the search thread and as a listener for the thread which
                // refreshes the SearchResultsTree. So this point is reached by on of two conditions:
                // - The search thread is done.
                // - Refreshing the SearchResultsTree is done.
                // SearchControlPanel can display normal mode only if:
                // - Search is done and has no results (refreshing the SearchResultsTree is not started).
                // - Or refreshing SearchResultsTree is done.
                if ((source.equals(searchThread) && (results == 0))
                            || !source.equals(searchThread)) {
                    searching = false;
                    listener.searchDone(results);
                    setControlsAccordingToState();
                }
            }
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);

        lblBusyIcon.setEnabled(searching);
        btnSearchCancel.setEnabled(enabled);
    }

    /**
     * DOCUMENT ME!
     */
    public void startSearch() {
        startSearch(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  simpleSort  if true, sorts the search results alphabetically. Usually set to false, as a more specific
     *                     sorting order is wished.
     */
    public void startSearch(final boolean simpleSort) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Start search programmatically.");
        }
        this.simpleSort = simpleSort;
        btnSearchCancel.doClick();
        this.simpleSort = false;
    }

    @Override
    public void setMaximumSize(final Dimension maximumSize) {
        btnSearchCancel.setMaximumSize(new java.awt.Dimension(100, (new Double(maximumSize.getHeight()).intValue())));
        super.setMaximumSize(maximumSize);
    }

    @Override
    public void setMinimumSize(final Dimension minimumSize) {
        btnSearchCancel.setMinimumSize(new java.awt.Dimension(100, (new Double(minimumSize.getHeight()).intValue())));
        super.setMinimumSize(minimumSize);
    }

    @Override
    public void setPreferredSize(final Dimension preferredSize) {
        btnSearchCancel.setPreferredSize(new java.awt.Dimension(
                100,
                (new Double(preferredSize.getHeight()).intValue())));
        super.setPreferredSize(preferredSize);
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
