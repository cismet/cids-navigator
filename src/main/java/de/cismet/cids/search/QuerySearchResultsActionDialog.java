/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeListener;

import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class QuerySearchResultsActionDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(QuerySearchResultsActionDialog.class);

    //~ Instance fields --------------------------------------------------------

    private final List<QuerySearchResultsAction> actions;
    private final Action closeAction;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.cismet.cids.search.QuerySearchResultsActionPanel querySearchResultsActionPanel1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearchResultsActionDialog object.
     *
     * @param  parent  DOCUMENT ME!
     * @param  modal   DOCUMENT ME!
     * @param  action  DOCUMENT ME!
     */
    public QuerySearchResultsActionDialog(final java.awt.Frame parent,
            final boolean modal,
            final QuerySearchResultsAction action) {
        this(parent, modal, Arrays.asList(action));
    }

    /**
     * Creates new form AbfrageDialog.
     *
     * @param  parent   DOCUMENT ME!
     * @param  modal    DOCUMENT ME!
     * @param  actions  DOCUMENT ME!
     */
    public QuerySearchResultsActionDialog(final java.awt.Frame parent,
            final boolean modal,
            final List<QuerySearchResultsAction> actions) {
        super(parent, modal);

        this.actions = actions;
        final String text = org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionDialog.class,
                "QuerySearchResultsActionDialog.closeAction.text");
        this.closeAction = new AbstractAction(text) {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    setVisible(false);
                }
            };

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        querySearchResultsActionPanel1 = new de.cismet.cids.search.QuerySearchResultsActionPanel(actions, closeAction);

        setTitle(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionDialog.class,
                "QuerySearchResultsActionDialog.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        querySearchResultsActionPanel1.setLayout(new java.awt.FlowLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(querySearchResultsActionPanel1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  results  DOCUMENT ME!
     */
    public void setSearchResults(final List<CidsBean> results) {
        querySearchResultsActionPanel1.setSearchResults(results);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel getQuerySearchResultsActionPanel() {
        return querySearchResultsActionPanel1;
    }
}
