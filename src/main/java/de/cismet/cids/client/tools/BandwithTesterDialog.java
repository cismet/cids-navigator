/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.error.ErrorInfo;

import java.awt.Frame;

import java.text.DecimalFormat;

import java.util.logging.Level;

import javax.swing.SwingWorker;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.actions.BandwidthTestAction;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/*
 * Copyright (C) 2013 cismet GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class BandwithTesterDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(BandwithTesterDialog.class);

    //~ Instance fields --------------------------------------------------------

    private long startTimeMs;
    private long stopTimeMs;
    private final String domain;
    private final Integer fileSizeInMb;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnStartDownload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BandwithTesterDialog object.
     *
     * @param  domain        DOCUMENT ME!
     * @param  fileSizeInMb  DOCUMENT ME!
     */
    public BandwithTesterDialog(final String domain, final Integer fileSizeInMb) {
        super(new javax.swing.JFrame(), true);
        this.domain = domain;
        this.fileSizeInMb = fileSizeInMb;
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnStartDownload = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(BandwithTesterDialog.class, "BandwithTesterDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(400, 240));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            btnStartDownload,
            org.openide.util.NbBundle.getMessage(
                BandwithTesterDialog.class,
                "BandwithTesterDialog.btnStartDownload.text"));          // NOI18N
        btnStartDownload.setActionCommand(org.openide.util.NbBundle.getMessage(
                BandwithTesterDialog.class,
                "BandwithTesterDialog.btnStartDownload.actionCommand")); // NOI18N
        btnStartDownload.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnStartDownloadActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(btnStartDownload, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(BandwithTesterDialog.class, "BandwithTesterDialog.jLabel1.text")); // NOI18N
        jLabel1.setMinimumSize(new java.awt.Dimension(300, 70));
        jLabel1.setPreferredSize(new java.awt.Dimension(300, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jLabel1, gridBagConstraints);

        jProgressBar1.setString(org.openide.util.NbBundle.getMessage(
                BandwithTesterDialog.class,
                "BandwithTesterDialog.jProgressBar1.string")); // NOI18N
        jProgressBar1.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jProgressBar1, gridBagConstraints);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.openide.awt.Mnemonics.setLocalizedText(
            btnClose,
            org.openide.util.NbBundle.getMessage(BandwithTesterDialog.class, "BandwithTesterDialog.btnClose.text")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCloseActionPerformed(evt);
                }
            });
        jPanel3.add(btnClose);

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCloseActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCloseActionPerformed
        System.exit(0);
    }                                                                            //GEN-LAST:event_btnCloseActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnStartDownloadActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnStartDownloadActionPerformed
        downloadStarted();

        new SwingWorker<byte[], Object>() {

                @Override
                protected byte[] doInBackground() throws Exception {
                    startTimeMs = System.currentTimeMillis();
                    final byte[] ret = downloadTestFile(domain, fileSizeInMb);
                    stopTimeMs = System.currentTimeMillis();
                    return ret;
                }

                @Override
                protected void done() {
                    try {
                        final byte[] ret = get();
                        final long timeNeededMs = stopTimeMs - startTimeMs;
                        downloadFinished(((long)ret.length * 8 * 1000) / timeNeededMs);
                    } catch (final Exception ex) {
                        downloadAborted(ex);
                        LOG.error("error while executing swingworker", ex);
                        final ErrorInfo errorInfo = new ErrorInfo(
                                org.openide.util.NbBundle.getMessage(
                                    BandwithTesterDialog.class,
                                    "BandwithTesterDialog.error.title"),
                                org.openide.util.NbBundle.getMessage(
                                    BandwithTesterDialog.class,
                                    "BandwithTesterDialog.error.message")
                                        + ex.getCause().getMessage(),
                                null,
                                null,
                                ex,
                                Level.ALL,
                                null);
                        JXErrorPane.showDialog(null, errorInfo);
                    }
                }
            }.execute();
    } //GEN-LAST:event_btnStartDownloadActionPerformed

    /**
     * DOCUMENT ME!
     */
    private void downloadStarted() {
        jProgressBar1.setIndeterminate(true);
        btnStartDownload.setEnabled(false);
        jProgressBar1.setString(org.openide.util.NbBundle.getMessage(
                BandwithTesterDialog.class,
                "BandwidthTesterDialog.jProgressBar1.string.running"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ex  DOCUMENT ME!
     */
    private void downloadAborted(final Exception ex) {
        btnStartDownload.setEnabled(true);
        jProgressBar1.setIndeterminate(false);
        jProgressBar1.setString(org.openide.util.NbBundle.getMessage(
                BandwithTesterDialog.class,
                "BandwidthTesterDialog.jProgressBar1.string.error") + ex.getLocalizedMessage());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bandwith  DOCUMENT ME!
     */
    private void downloadFinished(final long bandwith) {
        btnStartDownload.setEnabled(true);
        jProgressBar1.setIndeterminate(false);
        final DecimalFormat df = new DecimalFormat("#.##");
        if (bandwith < 1024) {
            jProgressBar1.setString(Long.toString(bandwith) + " Bit/s");
        } else if (bandwith < 1048576) {
            jProgressBar1.setString(df.format(bandwith / 1024d) + " kBit/s");
        } else {
            jProgressBar1.setString(df.format(bandwith / 1048576d) + " MBit/s");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();

        final String callServerURL = args[0];
        final String domain = args[1];
        final Integer fileSize = Integer.valueOf(args[2]);

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        final BandwithTesterDialog dialog = new BandwithTesterDialog(domain, fileSize);
                        dialog.login(callServerURL, domain);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                                @Override
                                public void windowClosing(final java.awt.event.WindowEvent e) {
                                    System.exit(0);
                                }
                            });
                        dialog.pack();
                        StaticSwingTools.showDialog(dialog);
                        System.exit(0);
                    } catch (final Exception ex) {
                        LOG.fatal("Fehler beim Starten des Bandbreiten-Testers.", ex);
                        final ErrorInfo errorInfo = new ErrorInfo(
                                org.openide.util.NbBundle.getMessage(
                                    BandwithTesterDialog.class,
                                    "BandwithTesterDialog.login.error.title"),
                                org.openide.util.NbBundle.getMessage(
                                    BandwithTesterDialog.class,
                                    "BandwithTesterDialog.login.error.message"),
                                null,
                                null,
                                ex,
                                Level.ALL,
                                null);
                        JXErrorPane.showDialog(null, errorInfo);
                        System.exit(1);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain        DOCUMENT ME!
     * @param   fileSizeInMb  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private byte[] downloadTestFile(final String domain, final int fileSizeInMb) throws Exception {
        final Object ret = SessionManager.getProxy().executeTask(
                BandwidthTestAction.TASK_NAME,
                domain,
                fileSizeInMb);

        if (ret instanceof Exception) {
            throw (Exception)ret;
        }

        final byte[] fileContent = (byte[])ret;
        return fileContent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  callServerURL  DOCUMENT ME!
     * @param  domain         DOCUMENT ME!
     */
    private void login(final String callServerURL, final String domain) {
        final CidsAuthentification cidsAuth = new CidsAuthentification(callServerURL, domain);
        final JXLoginPane login = new JXLoginPane(cidsAuth);

        final JXLoginPane.JXLoginDialog loginDialog = new JXLoginPane.JXLoginDialog((Frame)null, login);

        login.setPassword("".toCharArray());

        try {
            ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
        } catch (final Exception ex) {
            LOG.info("could nor request focus", ex);
        }
        StaticSwingTools.showDialog(loginDialog);

        if (loginDialog.getStatus() != JXLoginPane.Status.SUCCEEDED) {
            System.exit(0);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class CidsAuthentification extends LoginService {

        //~ Static fields/initializers -----------------------------------------

        public static final String CONNECTION_PROXY_CLASS =
            "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler";
        public static final String CONNECTION_CLASS = "Sirius.navigator.connection.RESTfulConnection";

        //~ Instance fields ----------------------------------------------------

        private final String callServerURL;
        private final String domain;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CidsAuthentification object.
         *
         * @param  callServerURL  DOCUMENT ME!
         * @param  domain         DOCUMENT ME!
         */
        public CidsAuthentification(final String callServerURL, final String domain) {
            this.callServerURL = callServerURL;
            this.domain = domain;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   name      DOCUMENT ME!
         * @param   password  DOCUMENT ME!
         * @param   server    DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  Exception  DOCUMENT ME!
         */
        @Override
        public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
            System.setProperty("sun.rmi.transport.connectionTimeout", "15");
            final String[] split = name.split("@");
            final String user = (split.length > 1) ? split[0] : name;
            final String group = (split.length > 1) ? split[1] : null;

            try {
                final Connection connection = ConnectionFactory.getFactory()
                            .createConnection(CONNECTION_CLASS, callServerURL);
                final ConnectionInfo connectionInfo = new ConnectionInfo();
                connectionInfo.setCallserverURL(callServerURL);
                connectionInfo.setPassword(new String(password));
                connectionInfo.setUserDomain(domain);
                connectionInfo.setUsergroup(group);
                connectionInfo.setUsergroupDomain(domain);
                connectionInfo.setUsername(user);
                final ConnectionSession session = ConnectionFactory.getFactory()
                            .createSession(connection, connectionInfo, true);
                final ConnectionProxy proxy = ConnectionFactory.getFactory()
                            .createProxy(CONNECTION_PROXY_CLASS, session);
                SessionManager.init(proxy);

                ClassCacheMultiple.setInstance(domain);
                return true;
            } catch (Throwable t) {
                LOG.error("Fehler beim Anmelden", t);
                return false;
            }
        }
    }
}
