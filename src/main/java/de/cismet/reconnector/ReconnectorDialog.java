/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.reconnector;

import org.openide.util.NbBundle;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ReconnectorDialog extends javax.swing.JDialog implements ReconnectorListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReconnectorDialog.class);

    //~ Instance fields --------------------------------------------------------

    private final Reconnector reconnector;
    private Component errorComponent;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnIgnore;
    private javax.swing.JButton btnRetry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labTryingToConnect;
    private javax.swing.JLabel lblCanceled;
    private javax.swing.JPanel panCanceled;
    private javax.swing.JPanel panError;
    private javax.swing.JPanel panProgress;
    private javax.swing.JPanel panRetry;
    private javax.swing.JProgressBar pb;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ReconnectorDialog.
     *
     * @param  parent       DOCUMENT ME!
     * @param  reconnector  DOCUMENT ME!
     */
    public ReconnectorDialog(final JFrame parent, final Reconnector reconnector) {
        super(parent);
        this.reconnector = reconnector;

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void showPending() {
        panError.removeAll();
        errorComponent = null;

        getContentPane().removeAll();
        getContentPane().add(panProgress, java.awt.BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  errorComponent  DOCUMENT ME!
     */
    private void showError(final Component errorComponent) {
        this.errorComponent = errorComponent;

        getContentPane().removeAll();
        getContentPane().add(panRetry, java.awt.BorderLayout.CENTER);

        panError.removeAll();
        if (errorComponent != null) {
            panError.add(errorComponent, BorderLayout.CENTER);
        }
        panError.revalidate();
    }

    @Override
    public void connecting() {
        showPending();
        showDialog();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isWaitingForUser() {
        return isVisible() && !getContentPane().equals(panProgress);
    }

    @Override
    public void connectionFailed(final ReconnectorEvent event) {
//        if (!SwingUtilities.isEventDispatchThread()) {
//            try {
//                SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            connectionFailed(event);
//                        }
//                    });
//            } catch (final Exception ex) {
//            }
//        } else {
        showError(event.getComponent());
        showDialog();
        StaticSwingTools.showDialog(this);
//        }
    }

    @Override
    public void connectionCanceled() {
//        if (!SwingUtilities.isEventDispatchThread()) {
//            try {
//                SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            connectionCanceled();
//                        }
//                    });
//            } catch (final Exception ex) {
//            }
//        } else {
//            if (isVisible()) {
        showError(panCanceled);
        showDialog();
//            }
//        }
    }

    /**
     * DOCUMENT ME!
     */
    private void showDialog() {
        revalidate();
        validate();
        repaint();
        pack();
//        StaticSwingTools.showDialog(this);
    }

    /**
     * DOCUMENT ME!
     */
    private void close() {
        setVisible(false);
    }

    @Override
    public void connectionCompleted() {
        close();
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panProgress = new javax.swing.JPanel();
        pb = new javax.swing.JProgressBar();
        btnCancel = new javax.swing.JButton();
        labTryingToConnect = new javax.swing.JLabel();
        panRetry = new javax.swing.JPanel();
        panError = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnIgnore = new javax.swing.JButton();
        btnRetry = new javax.swing.JButton();
        panCanceled = new javax.swing.JPanel();
        lblCanceled = new javax.swing.JLabel();

        panProgress.setMinimumSize(new java.awt.Dimension(200, 88));
        panProgress.setLayout(new java.awt.GridBagLayout());

        pb.setBorderPainted(false);
        pb.setIndeterminate(true);
        pb.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        panProgress.add(pb, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnCancel,
            org.openide.util.NbBundle.getMessage(ReconnectorDialog.class, "ReconnectorDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCancelActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panProgress.add(btnCancel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            labTryingToConnect,
            org.openide.util.NbBundle.getMessage(ReconnectorDialog.class, "ReconnectorDialog.labTryingToConnect.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        panProgress.add(labTryingToConnect, gridBagConstraints);

        panRetry.setMinimumSize(new java.awt.Dimension(200, 53));
        panRetry.setLayout(new java.awt.BorderLayout());

        panError.setLayout(new java.awt.BorderLayout());
        panRetry.add(panError, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnExit,
            org.openide.util.NbBundle.getMessage(ReconnectorDialog.class, "ReconnectorDialog.btnExit.text")); // NOI18N
        btnExit.setPreferredSize(new java.awt.Dimension(125, 29));
        btnExit.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnExitActionPerformed(evt);
                }
            });
        jPanel1.add(btnExit);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnIgnore,
            org.openide.util.NbBundle.getMessage(ReconnectorDialog.class, "ReconnectorDialog.btnIgnore.text")); // NOI18N
        btnIgnore.setPreferredSize(new java.awt.Dimension(125, 29));
        btnIgnore.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnIgnoreActionPerformed(evt);
                }
            });
        jPanel1.add(btnIgnore);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnRetry,
            org.openide.util.NbBundle.getMessage(ReconnectorDialog.class, "ReconnectorDialog.btnRetry.text")); // NOI18N
        btnRetry.setPreferredSize(new java.awt.Dimension(125, 29));
        btnRetry.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnRetryActionPerformed(evt);
                }
            });
        jPanel1.add(btnRetry);

        panRetry.add(jPanel1, java.awt.BorderLayout.SOUTH);

        panCanceled.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            lblCanceled,
            NbBundle.getMessage(ReconnectorDialog.class, "connection_canceled"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panCanceled.add(lblCanceled, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(NbBundle.getMessage(Reconnector.class, "Reconnector.useDialog().reconnectorDialog.title"));
        setAlwaysOnTop(true);
        setModal(true);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
        close();
        connectionCanceled();
    }                                                                             //GEN-LAST:event_btnCancelActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnExitActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnExitActionPerformed
        System.exit(1);
    }                                                                           //GEN-LAST:event_btnExitActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnIgnoreActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnIgnoreActionPerformed
        reconnector.doAbort();
        close();
    }                                                                             //GEN-LAST:event_btnIgnoreActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRetryActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRetryActionPerformed
        if (errorComponent instanceof ReconnectorErrorPanelWithApply) {
            ((ReconnectorErrorPanelWithApply)errorComponent).apply();
        }
        close();
    }                                                                            //GEN-LAST:event_btnRetryActionPerformed
}
