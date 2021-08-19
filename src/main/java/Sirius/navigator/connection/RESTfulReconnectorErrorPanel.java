/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 jruiz
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * RESTfulReconnectorErrorPanel.java
 *
 * Created on 25.08.2010, 12:55:35
 */
package Sirius.navigator.connection;

import org.apache.commons.io.FileUtils;

import java.awt.BorderLayout;
import java.awt.Container;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;

import de.cismet.reconnector.DefaultReconnectorErrorPanel;
import de.cismet.reconnector.ReconnectorErrorPanelWithApply;

import static Sirius.navigator.connection.DefaultSSLConfigProvider.CLIENT_CERT_KEYSTORE_FILE_NAME;
import static Sirius.navigator.connection.DefaultSSLConfigProvider.CLIENT_CERT_PASS_PREFS_KEY;
import static Sirius.navigator.connection.DefaultSSLConfigProvider.FILE_SEP;
import static Sirius.navigator.connection.DefaultSSLConfigProvider.SERVER_CERT_FILE_NAME;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class RESTfulReconnectorErrorPanel extends javax.swing.JPanel implements ReconnectorErrorPanelWithApply {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RESTfulReconnector.class);

    //~ Instance fields --------------------------------------------------------

    private final JFileChooser fileChooser;
    private final ProxyOptionsPanel panProxyOptions;
    private DefaultReconnectorErrorPanel errPan;
    private final Preferences cidsPrefs;
    private FileFilter keystoreFileFilter = new FileFilter() {

            @Override
            public boolean accept(final File f) {
                if ((f.isFile() && f.getName().endsWith(".keystore")) || f.isDirectory()) { // NOI18N
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return ".keystore";
            }
        };

    private FileFilter derFileFilter = new FileFilter() {

            @Override
            public boolean accept(final File f) {
                if ((f.isFile() && f.getName().endsWith(".der")) || f.isDirectory()) { // NOI18N
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return ".der";
            }
        };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCheck;
    private javax.swing.JButton cmdGetClientCert;
    private javax.swing.JButton cmdGetServerCert;
    private javax.swing.JPanel errPanWrapper;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblClientCertIndicator;
    private javax.swing.JLabel lblServerCertIndicator;
    private javax.swing.JPanel panCertOptionsWrapper;
    private javax.swing.JPanel panProxyOptionsWrapper;
    private javax.swing.JToggleButton tbCerts;
    private javax.swing.JToggleButton tbProxy;
    private javax.swing.JPasswordField txtClientCertPass;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form RESTfulReconnectorErrorPanel.
     *
     * @param  panProxyOptions  DOCUMENT ME!
     */
    public RESTfulReconnectorErrorPanel(final ProxyOptionsPanel panProxyOptions) {
        this.panProxyOptions = panProxyOptions;

        initComponents();
        panProxyOptionsWrapper.add(panProxyOptions, BorderLayout.CENTER);

        panCertOptionsWrapper.setVisible(false);
        panProxyOptionsWrapper.setVisible(false);

        cidsPrefs = Preferences.userNodeForPackage(DefaultSSLConfigProvider.class);
        txtClientCertPass.setText(cidsPrefs.get(CLIENT_CERT_PASS_PREFS_KEY, ""));
        txtClientCertPass.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(final DocumentEvent e) {
                    updatePass();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    updatePass();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    updatePass();
                }
            });
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(keystoreFileFilter);
        fileChooser.addChoosableFileFilter(derFileFilter);
        checkGUIState();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void updatePass() {
        cidsPrefs.put(CLIENT_CERT_PASS_PREFS_KEY, new String(txtClientCertPass.getPassword()));
    }

    /**
     * DOCUMENT ME!
     */
    private void checkGUIState() {
        if (DefaultSSLConfigProvider.LOCAL_SERVER_CERT_FILE.exists()) {
            lblServerCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                    RESTfulReconnectorErrorPanel.class,
                    "RESTfulReconnectorErrorPanel.found"));
        } else {
            final InputStream classPathServerCertIS = DefaultSSLConfigProvider.class.getResourceAsStream(
                    SERVER_CERT_FILE_NAME);
            if (classPathServerCertIS != null) {
                lblServerCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                        RESTfulReconnectorErrorPanel.class,
                        "RESTfulReconnectorErrorPanel.classpath"));
                try {
                    classPathServerCertIS.close();
                } catch (IOException ex) {
                    LOG.warn("Problems closing testStream", ex);
                }
            } else {
                lblServerCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                        RESTfulReconnectorErrorPanel.class,
                        "RESTfulReconnectorErrorPanel.notfound"));
            }
        }
        if (DefaultSSLConfigProvider.CLIENT_CERT_KEYSTORE_FILE.exists()) {
            lblClientCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                    RESTfulReconnectorErrorPanel.class,
                    "RESTfulReconnectorErrorPanel.found"));
            txtClientCertPass.setEnabled(true);
        } else {
            lblClientCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                    RESTfulReconnectorErrorPanel.class,
                    "RESTfulReconnectorErrorPanel.notfound"));
            txtClientCertPass.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message    DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void setError(final String message, final Throwable exception) {
        errPan = new DefaultReconnectorErrorPanel(message, exception);
        errPanWrapper.removeAll();
        errPanWrapper.add(errPan);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        errPanWrapper = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        tbProxy = new javax.swing.JToggleButton();
        tbCerts = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JSeparator();
        panProxyOptionsWrapper = new javax.swing.JPanel();
        panCertOptionsWrapper = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblServerCertIndicator = new javax.swing.JLabel();
        lblClientCertIndicator = new javax.swing.JLabel();
        cmdGetServerCert = new javax.swing.JButton();
        cmdGetClientCert = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cmdCheck = new javax.swing.JButton();
        txtClientCertPass = new javax.swing.JPasswordField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));

        setLayout(new java.awt.GridBagLayout());

        errPanWrapper.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(errPanWrapper, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        add(jSeparator1, gridBagConstraints);

        tbProxy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sirius/navigator/connection/proxy.png"))); // NOI18N
        tbProxy.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.tbProxy.text"));                                                        // NOI18N
        tbProxy.setFocusPainted(false);
        tbProxy.setMaximumSize(null);
        tbProxy.setMinimumSize(null);
        tbProxy.setPreferredSize(null);
        tbProxy.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbProxyActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tbProxy, gridBagConstraints);

        tbCerts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sirius/navigator/connection/cert.png"))); // NOI18N
        tbCerts.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.tbCerts.text"));                                                       // NOI18N
        tbCerts.setFocusPainted(false);
        tbCerts.setMaximumSize(null);
        tbCerts.setMinimumSize(null);
        tbCerts.setPreferredSize(null);
        tbCerts.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbCertsActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tbCerts, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        add(jSeparator3, gridBagConstraints);

        panProxyOptionsWrapper.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    RESTfulReconnectorErrorPanel.class,
                    "RESTfulReconnectorErrorPanel.tbProxy.text"))); // NOI18N
        panProxyOptionsWrapper.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(panProxyOptionsWrapper, gridBagConstraints);

        panCertOptionsWrapper.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    RESTfulReconnectorErrorPanel.class,
                    "RESTfulReconnectorErrorPanel.tbCerts.text"))); // NOI18N
        panCertOptionsWrapper.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panCertOptionsWrapper.add(jLabel1, gridBagConstraints);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panCertOptionsWrapper.add(jLabel2, gridBagConstraints);

        lblServerCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.lblServerCertIndicator.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        panCertOptionsWrapper.add(lblServerCertIndicator, gridBagConstraints);

        lblClientCertIndicator.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.lblClientCertIndicator.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        panCertOptionsWrapper.add(lblClientCertIndicator, gridBagConstraints);

        cmdGetServerCert.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.cmdGetServerCert.text")); // NOI18N
        cmdGetServerCert.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdGetServerCertActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panCertOptionsWrapper.add(cmdGetServerCert, gridBagConstraints);

        cmdGetClientCert.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.cmdGetClientCert.text")); // NOI18N
        cmdGetClientCert.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdGetClientCertActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panCertOptionsWrapper.add(cmdGetClientCert, gridBagConstraints);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panCertOptionsWrapper.add(jLabel5, gridBagConstraints);

        cmdCheck.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.cmdCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panCertOptionsWrapper.add(cmdCheck, gridBagConstraints);

        txtClientCertPass.setColumns(20);
        txtClientCertPass.setText(org.openide.util.NbBundle.getMessage(
                RESTfulReconnectorErrorPanel.class,
                "RESTfulReconnectorErrorPanel.txtClientCertPass.text")); // NOI18N
        txtClientCertPass.setMinimumSize(new java.awt.Dimension(100, 28));
        txtClientCertPass.setPreferredSize(new java.awt.Dimension(100, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        panCertOptionsWrapper.add(txtClientCertPass, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(panCertOptionsWrapper, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public void apply() {
        panProxyOptions.applyChanges();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbProxyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_tbProxyActionPerformed
        panProxyOptionsWrapper.setVisible(tbProxy.isSelected());

        // hochpendeln bis JDialog
        Container parent = this;
        do {
            parent = parent.getParent();
        } while ((parent != null) && !(parent instanceof JDialog));
        if (parent != null) {
            ((JDialog)parent).pack();
        }
    } //GEN-LAST:event_tbProxyActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbCertsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_tbCertsActionPerformed
        panCertOptionsWrapper.setVisible(tbCerts.isSelected());
        // hochpendeln bis JDialog
        Container parent = this;
        do {
            parent = parent.getParent();
        } while ((parent != null) && !(parent instanceof JDialog));
        if (parent != null) {
            ((JDialog)parent).pack();
        }
    } //GEN-LAST:event_tbCertsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdGetServerCertActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdGetServerCertActionPerformed
        fileChooser.setFileFilter(derFileFilter);
        final int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                FileUtils.copyFile(fileChooser.getSelectedFile(),
                    new File(DefaultSSLConfigProvider.CIDS_DIR + FILE_SEP + SERVER_CERT_FILE_NAME));
            } catch (final IOException ex) {
                LOG.error(ex, ex);
            }
            checkGUIState();
        }
    }                                                                                    //GEN-LAST:event_cmdGetServerCertActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdGetClientCertActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdGetClientCertActionPerformed
        fileChooser.setFileFilter(keystoreFileFilter);
        final int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                FileUtils.copyFile(
                    fileChooser.getSelectedFile(),
                    new File(DefaultSSLConfigProvider.CIDS_DIR + FILE_SEP + CLIENT_CERT_KEYSTORE_FILE_NAME));
            } catch (final IOException ex) {
                LOG.error(ex, ex);
            }
            checkGUIState();
        }
    }                                                                                    //GEN-LAST:event_cmdGetClientCertActionPerformed
}
