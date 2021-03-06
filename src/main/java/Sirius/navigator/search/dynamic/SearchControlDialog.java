/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchControlDialog.java
 *
 * Created on 13.12.2011, 15:54:15
 */
package Sirius.navigator.search.dynamic;

import org.openide.util.Exceptions;

import java.awt.Color;
import java.awt.EventQueue;

import de.cismet.cids.server.search.CidsServerSearch;
import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class SearchControlDialog extends javax.swing.JDialog implements SearchControlListener,
    ConnectionContextProvider {

    //~ Instance fields --------------------------------------------------------

    private MetaObjectNodeServerSearch search;
    private final ConnectionContext connectionContext;

    private SearchControlPanel pnlSearchCancel;
    private Color foregroundColor;
    private boolean allowUserToCloseDialog;
    private boolean isCloseButtonShown;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JPanel pnlControls;
    private javax.swing.JSeparator sepMessage;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchControlDialog.
     *
     * @param  parent             DOCUMENT ME!
     * @param  modal              DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public SearchControlDialog(final java.awt.Frame parent,
            final boolean modal,
            final ConnectionContext connectionContext) {
        this(parent, modal, null, connectionContext);
    }

    /**
     * Creates new form SearchControlDialog.
     *
     * @param  parent             DOCUMENT ME!
     * @param  modal              DOCUMENT ME!
     * @param  search             DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public SearchControlDialog(final java.awt.Frame parent,
            final boolean modal,
            final MetaObjectNodeServerSearch search,
            final ConnectionContext connectionContext) {
        super(parent, modal);
        this.search = search;
        this.connectionContext = connectionContext;

        initComponents();

        pnlSearchCancel = new SearchControlPanel(this, getConnectionContext());
        foregroundColor = lblMessage.getForeground();
        lblMessage.setForeground(lblMessage.getBackground());
        pnlControls.add(pnlSearchCancel);
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

        btnClose = new javax.swing.JButton();
        lblIcon = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        sepMessage = new javax.swing.JSeparator();
        pnlControls = new javax.swing.JPanel();

        btnClose.setText(org.openide.util.NbBundle.getMessage(
                SearchControlDialog.class,
                "SearchControlDialog.btnClose.text")); // NOI18N
        btnClose.setFocusPainted(false);
        btnClose.setMaximumSize(new java.awt.Dimension(100, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(58, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(100, 25));
        btnClose.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCloseActionPerformed(evt);
                }
            });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(SearchControlDialog.class, "SearchControlDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 200));
        addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    formWindowClosing(evt);
                }
            });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/Sirius/navigator/search/dynamic/SearchControlDialog_lblIcon.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblIcon, gridBagConstraints);

        lblMessage.setText(org.openide.util.NbBundle.getMessage(
                SearchControlDialog.class,
                "SearchControlDialog.lblMessage.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblMessage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sepMessage, gridBagConstraints);

        pnlControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlControls, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void formWindowClosing(final java.awt.event.WindowEvent evt) { //GEN-FIRST:event_formWindowClosing
        if (allowUserToCloseDialog) {
            setVisible(false);
        }
    }                                                                      //GEN-LAST:event_formWindowClosing

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCloseActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
    }                                                                            //GEN-LAST:event_btnCloseActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    public void setSearch(final MetaObjectNodeServerSearch search) {
        this.search = search;

        if (isCloseButtonShown) {
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        pnlControls.remove(btnClose);
                        pnlControls.add(pnlSearchCancel);
                        validate();
                        isCloseButtonShown = false;
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsServerSearch getSearch() {
        return search;
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
        pnlSearchCancel.startSearch(simpleSort);
    }

    @Override
    public MetaObjectNodeServerSearch assembleSearch() {
        return search;
    }

    @Override
    public void searchStarted() {
        lblMessage.setText(org.openide.util.NbBundle.getMessage(
                SearchControlDialog.class,
                "SearchControlDialog.lblMessage.text"));
        lblMessage.setForeground(foregroundColor);
        allowUserToCloseDialog = false;
    }

    @Override
    public void searchDone(final int numberOfResults) {
        if (numberOfResults == 0) {
            lblMessage.setText(org.openide.util.NbBundle.getMessage(
                    SearchControlDialog.class,
                    "SearchControlDialog.lblMessage_emptyResult.text"));
            pnlControls.remove(pnlSearchCancel);
            pnlControls.add(btnClose);
            allowUserToCloseDialog = true;
            isCloseButtonShown = true;
            validate();
            repaint();
        } else {
            setVisible(false);
        }
    }

    @Override
    public void searchCanceled() {
        setVisible(false);
    }

    @Override
    public boolean suppressEmptyResultMessage() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        final String[] LAFS = new String[] {
                "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel",
                "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
                "com.jgoodies.looks.windows.WindowsLookAndFeel",
                "com.jgoodies.looks.plastic.PlasticLookAndFeel",
                "com.jgoodies.looks.plastic.Plastic3DLookAndFeel",
                "com.jgoodies.looks.plastic.PlasticXPLookAndFeel"
            };

        try {
            javax.swing.UIManager.setLookAndFeel(LAFS[4]);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SearchControlDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchControlDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchControlDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchControlDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        final SearchControlDialog dialog = new SearchControlDialog(new javax.swing.JFrame(), true, null);

        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                            @Override
                            public void windowClosing(final java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                    dialog.searchStarted();
                    StaticSwingTools.showDialog(dialog);
                }
            });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        final Runnable run = new Runnable() {

                @Override
                public void run() {
                    dialog.searchDone(1);
                }
            };

        EventQueue.invokeLater(run);
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
