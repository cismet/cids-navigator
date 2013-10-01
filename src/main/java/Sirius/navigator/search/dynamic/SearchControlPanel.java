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
import Sirius.navigator.ui.tree.SearchResultsTree;

import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.URL;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class SearchControlPanel extends javax.swing.JPanel implements PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchControlPanel.class);

    //~ Instance fields --------------------------------------------------------

    private SearchControlListener listener;
    private SwingWorker<Node[], Void> searchThread;
    private boolean searching = false;
    private ImageIcon iconSearch;
    private ImageIcon iconCancel;

    private boolean simpleSort;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearchCancel;
    private org.jdesktop.swingx.JXBusyLabel lblBusyIcon;
    private javax.swing.Box.Filler strGap;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchControlPanel.
     *
     * @param  listener  DOCUMENT ME!
     */
    public SearchControlPanel(final SearchControlListener listener) {
        if (listener == null) {
            LOG.warn("Given listener is null. Panel won't work.");
        }

        this.listener = listener;
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

            ComponentRegistry.getRegistry().getSearchResultsTree().addPropertyChangeListener("browse", this);
            searchThread = CidsSearchExecutor.searchAndDisplayResults(
                    search,
                    this,
                    this,
                    listener.suppressEmptyResultMessage(),
                    simpleSort);
            searching = true;
            setControlsAccordingToState();
            listener.searchStarted();
        }
    } //GEN-LAST:event_btnSearchCancelActionPerformed

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
}
