/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchSearchTopicsDialog.java
 *
 * Created on 30.11.2011, 09:03:40
 */
package Sirius.navigator.search.dynamic;

import Sirius.navigator.search.CidsSearchExecutor;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.CidsServerSearch;
import Sirius.server.search.builtin.FullTextSearch;
import Sirius.server.search.builtin.GeoSearch;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.gui.metasearch.MetaSearch;
import de.cismet.cismap.commons.interaction.CismapBroker;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class SearchSearchTopicsDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchSearchTopicsDialog.class);
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox chkCaseSensitive;
    private javax.swing.JCheckBox chkHere;
    private javax.swing.Box.Filler gluBottom;
    private javax.swing.Box.Filler gluFiller;
    private javax.swing.Box.Filler gluTop;
    private javax.swing.JLabel lblSearchParameter;
    private javax.swing.JPanel pnlButtons;
    private Sirius.navigator.search.dynamic.SearchTopicsPanel pnlSearchTopics;
    private javax.swing.JScrollPane scpSearchTopics;
    private javax.swing.JSeparator sepButtons;
    private javax.swing.JSeparator sepSearchTopics;
    private javax.swing.JTextField txtSearchParameter;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchSearchTopicsDialog.
     *
     * @param  parent  DOCUMENT ME!
     * @param  modal   DOCUMENT ME!
     */
    public SearchSearchTopicsDialog(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);

        initComponents();

        final EnableSearchListener listener = new EnableSearchListener();
        pnlSearchTopics.setSearchTopics(MetaSearch.instance().getSearchTopics());
        pnlSearchTopics.registerItemListener(listener);
        txtSearchParameter.getDocument().addDocumentListener(listener);
        chkHere.addItemListener(listener);

        final Dimension searchTopicsDimension = pnlSearchTopics.getPreferredSize();
        final Dimension searchParameterDimension = txtSearchParameter.getPreferredSize();
        setMinimumSize(new Dimension(
                (int)(searchTopicsDimension.getWidth() + searchParameterDimension.getWidth() + 75),
                (int)searchTopicsDimension.getHeight()
                        + 100));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblSearchParameter = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        chkCaseSensitive = new javax.swing.JCheckBox();
        sepButtons = new javax.swing.JSeparator();
        txtSearchParameter = new javax.swing.JTextField();
        chkHere = new javax.swing.JCheckBox();
        gluFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        sepSearchTopics = new javax.swing.JSeparator();
        scpSearchTopics = new javax.swing.JScrollPane();
        pnlSearchTopics = new Sirius.navigator.search.dynamic.SearchTopicsPanel();
        gluTop = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        gluBottom = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 300));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblSearchParameter.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.lblSearchParameter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        getContentPane().add(lblSearchParameter, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        btnSearch.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.btnSearch.text")); // NOI18N
        btnSearch.setEnabled(false);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnSearchActionPerformed(evt);
                }
            });
        pnlButtons.add(btnSearch);

        btnClose.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.btnClose.text")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCloseActionPerformed(evt);
                }
            });
        pnlButtons.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(pnlButtons, gridBagConstraints);

        chkCaseSensitive.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.chkCaseSensitive.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        getContentPane().add(chkCaseSensitive, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(sepButtons, gridBagConstraints);

        txtSearchParameter.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.txtSearchParameter.text")); // NOI18N
        txtSearchParameter.setMinimumSize(new java.awt.Dimension(250, 25));
        txtSearchParameter.setPreferredSize(new java.awt.Dimension(250, 25));
        txtSearchParameter.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    txtSearchParameterActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        getContentPane().add(txtSearchParameter, gridBagConstraints);

        chkHere.setText(org.openide.util.NbBundle.getMessage(
                SearchSearchTopicsDialog.class,
                "SearchSearchTopicsDialog.chkHere.text")); // NOI18N
        chkHere.addItemListener(new java.awt.event.ItemListener() {

                @Override
                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    chkHereItemStateChanged(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        getContentPane().add(chkHere, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(gluFiller, gridBagConstraints);

        sepSearchTopics.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(sepSearchTopics, gridBagConstraints);

        scpSearchTopics.setViewportView(pnlSearchTopics);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(scpSearchTopics, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(gluTop, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(gluBottom, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnSearchActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnSearchActionPerformed
        final Collection<String> selectedSearchClasses = MetaSearch.instance().getSelectedSearchClassesForQuery();

        LOG.info("Starting search for '" + txtSearchParameter.getText() + "' in '" + selectedSearchClasses
                    + "'. Case sensitive? "
                    + chkCaseSensitive.isSelected() + ", Only in cismap? " + chkHere.isSelected());

        CidsServerSearch serverSearch = null;
        if (chkHere.isSelected()) {
            final BoundingBox boundingBox = CismapBroker.getInstance().getMappingComponent().getCurrentBoundingBox();
            final int srid = CrsTransformer.extractSridFromCrs(CismapBroker.getInstance().getMappingComponent()
                            .getMappingModel().getSrs().getCode());
            final Geometry transformed = CrsTransformer.transformToDefaultCrs(boundingBox.getGeometry(srid));
            // Damits auch mit -1 funzt:
            transformed.setSRID(CismapBroker.getInstance().getDefaultCrsAlias());

            serverSearch = new GeoSearch(transformed);
        } else {
            serverSearch = new FullTextSearch(txtSearchParameter.getText());
        }

        if (serverSearch != null) {
            serverSearch.setCaseSensitive(chkCaseSensitive.isSelected());
            serverSearch.setValidClassesFromStrings(selectedSearchClasses);
            CidsSearchExecutor.executeCidsSearchAndDisplayResults(serverSearch, new PropertyChangeListener() {

                    @Override
                    public void propertyChange(final PropertyChangeEvent evt) {
                        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                            try {
                                if (((SwingWorker<Node[], Void>)evt.getSource()).get().length > 0) {
                                    setVisible(false);
                                    dispose();
                                }
                            } catch (InterruptedException ex) {
                                LOG.warn(
                                    "Something went wrong while trying to retrieve search results in order to close search dialog.",
                                    ex);
                            } catch (ExecutionException ex) {
                                LOG.warn(
                                    "Something went wrong while trying to retrieve search results in order to close search dialog.",
                                    ex);
                            }
                        }
                    }
                });
        }
    } //GEN-LAST:event_btnSearchActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCloseActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
        dispose();
    }                                                                            //GEN-LAST:event_btnCloseActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void chkHereItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_chkHereItemStateChanged
        txtSearchParameter.setEnabled(evt.getStateChange() != ItemEvent.SELECTED);
    }                                                                          //GEN-LAST:event_chkHereItemStateChanged

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void txtSearchParameterActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_txtSearchParameterActionPerformed
        // Avoid invalid input.
        if (btnSearch.isEnabled()) {
            btnSearchActionPerformed(evt);
        }
    } //GEN-LAST:event_txtSearchParameterActionPerformed

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class EnableSearchListener implements DocumentListener, ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertUpdate(final DocumentEvent e) {
            enableSearchButton();
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
            enableSearchButton();
        }

        @Override
        public void changedUpdate(final DocumentEvent e) {
            enableSearchButton();
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            enableSearchButton();
        }

        /**
         * DOCUMENT ME!
         */
        private void enableSearchButton() {
            // SearchTopicsPanel updates MetaSearch using an ItemListener, so this check has to wait until
            // SearchTopicsPanel updated MetaSearch

            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        boolean enableSearchButton = true;
                        if (!chkHere.isSelected()) {
                            enableSearchButton &= (txtSearchParameter.getText() != null)
                                        && (txtSearchParameter.getText().trim().length() > 0);
                        }
                        enableSearchButton &= MetaSearch.instance().getSelectedSearchTopics().size() > 0;
                        btnSearch.setEnabled(enableSearchButton);
                    }
                });
        }
    }
}
