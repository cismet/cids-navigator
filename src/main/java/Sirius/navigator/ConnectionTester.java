/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 mscholl
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
 * ConnectionTester.java
 *
 * Created on Jul 5, 2010, 4:38:47 PM
 */
package Sirius.navigator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.openide.util.Lookup;

import java.awt.EventQueue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;

import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.Proxy;

import de.cismet.tools.gui.TextAreaAppender;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ConnectionTester extends javax.swing.JFrame {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ConnectionTester.class);

    //~ Instance fields --------------------------------------------------------

    private final String connectionUrl;
    private final boolean compressionEnabled;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStore;
    private javax.swing.JButton btnTest;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPane1;
    private de.cismet.lookupoptions.options.ProxyOptionsPanel proxyOptionsPanel1;
    private javax.swing.JTextArea txaLog;
    private javax.swing.JTextArea txaOut;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ConnectionTester.
     *
     * @param  connectionUrl       DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     */
    public ConnectionTester(final String connectionUrl, final boolean compressionEnabled) {
        this.connectionUrl = connectionUrl;
        this.compressionEnabled = compressionEnabled;

        initComponents();
        initLog();

        setTitle(getTitle() + ": " + connectionUrl);

        proxyOptionsPanel1.update();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initLog() {
        TextAreaAppender.setTextArea(txaLog, new JTextArea());
        final Properties logProperties = new Properties();
        logProperties.put("log4j.rootLogger", "DEBUG, CONSOLE, TEXTAREA");
        logProperties.put("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");      // A standard console
                                                                                              // appender
        logProperties.put("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout"); // See:
                                                                                              // http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
        logProperties.put("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss} [%12.12t] %5.5p %c: %m%n");

        logProperties.put("log4j.appender.TEXTAREA", "de.cismet.tools.gui.TextAreaAppender");  // Our custom appender
        logProperties.put("log4j.appender.TEXTAREA.layout", "org.apache.log4j.PatternLayout"); // See:
                                                                                               // http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
        logProperties.put("log4j.appender.TEXTAREA.layout.ConversionPattern", "%d{HH:mm:ss} [%12.12t] %5.5p %c: %m%n");

        PropertyConfigurator.configure(logProperties);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        btnTest = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaOut = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaLog = new javax.swing.JTextArea();
        btnStore = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        proxyOptionsPanel1 = new de.cismet.lookupoptions.options.ProxyOptionsPanel();

        jScrollPane2.setViewportView(jEditorPane1);

        jScrollPane3.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(ConnectionTester.class, "ConnectionTester.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnTest.setText("Test"); // NOI18N
        btnTest.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnTestActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnTest, gridBagConstraints);

        txaOut.setColumns(20);
        txaOut.setRows(5);
        jScrollPane1.setViewportView(txaOut);

        jTabbedPane1.addTab("Out", jScrollPane1);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        txaLog.setColumns(20);
        txaLog.setRows(5);
        txaLog.setAutoscrolls(false);
        jScrollPane4.setViewportView(txaLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 744;
        gridBagConstraints.ipady = 345;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane4, gridBagConstraints);

        btnStore.setText("save"); // NOI18N
        btnStore.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnStoreActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnStore, gridBagConstraints);

        final org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 0, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 0, Short.MAX_VALUE));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel3, gridBagConstraints);

        jTabbedPane1.addTab("Log", jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(proxyOptionsPanel1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnTestActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_btnTestActionPerformed
    {                                                                         //GEN-HEADEREND:event_btnTestActionPerformed
        try {
            txaOut.setText("");

            proxyOptionsPanel1.applyChanges();

            final Proxy proxy = proxyOptionsPanel1.getProxy();

            txaOut.setText("Connection test running...\n\n");

            final RESTfulSerialInterfaceConnector connector = new RESTfulSerialInterfaceConnector(
                    connectionUrl,
                    proxy,
                    Lookup.getDefault().lookup(SSLConfigProvider.class).getSSLConfig(),
                    compressionEnabled);

            final Thread establisher = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final String ret = connector.getDomains(ConnectionContext.createDeprecated()).length
                                            + " domain(s) retrieved\n\nSUCCESS";
                                EventQueue.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            txaOut.append(ret);
                                        }
                                    });
                            } catch (final Exception ex) {
                                appendException(ex);
                            }
                        }
                    });
            establisher.start();
        } catch (final Exception ex) {
            appendException(ex);
        }
    } //GEN-LAST:event_btnTestActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  ex  DOCUMENT ME!
     */
    private void appendException(final Exception ex) {
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    txaOut.append(ex.getMessage() + "\n");
                    txaOut.append("STACKTRACE: \n");
                    for (final StackTraceElement ste : ex.getStackTrace()) {
                        txaOut.append(ste.toString() + "\n");
                    }

                    Throwable cause = ex.getCause();
                    while (cause != null) {
                        txaOut.append("\n\n");
                        txaOut.append("CAUSE: ");
                        txaOut.append(cause.getMessage());
                        txaOut.append("\n");
                        txaOut.append("STACKTRACE: \n");
                        for (final StackTraceElement ste : cause.getStackTrace()) {
                            txaOut.append(ste.toString() + "\n");
                        }

                        cause = cause.getCause();
                    }
                    txaOut.append("\nFAILURE");
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnStoreActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_btnStoreActionPerformed
    {                                                                          //GEN-HEADEREND:event_btnStoreActionPerformed
        BufferedOutputStream bos = null;
        try {
            final JFileChooser chooser = new JFileChooser();
            final int answer = chooser.showSaveDialog(this);
            if (answer == JFileChooser.APPROVE_OPTION) {
                bos = new BufferedOutputStream(new FileOutputStream(chooser.getSelectedFile()));
                bos.write(txaLog.getText().getBytes());
            }
        } catch (final Exception e) {
            LOG.error("cannot save log", e);
            if (bos != null) {
                try {
                    bos.close();
                } catch (final IOException ex) {
                    LOG.error("cannot close stream", ex);
                }
            }
        }
    }                                                                          //GEN-LAST:event_btnStoreActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param   args  the command line arguments
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        if ((args.length <= 0) || !(args[0] instanceof String)) {
            System.out.println("missing connectionUrl argument");
            System.exit(1);
        }
        final String arg0 = (String)args[0];
        boolean arg1 = false;
        if (args.length > 1) {
            try {
                arg1 = Boolean.parseBoolean(args[1]);
            } catch (final Exception ex) {
                LOG.warn("error while parsing compressionEnabled argument", ex);
            }
        }
        final String callserverUrl;
        final boolean compressionEnabled;
        if (arg0.endsWith(".cfg")) {
            final String cfgFile = args[0];
            final Properties properties = new Properties();
            if ((cfgFile.indexOf("http://") == 0) || (cfgFile.indexOf("https://") == 0)
                        || (cfgFile.indexOf("file:/") == 0)) {
                properties.load(new URL(cfgFile).openStream());
            } else {
                properties.load(new BufferedInputStream(new FileInputStream(cfgFile)));
            }
            callserverUrl = properties.getProperty("callserverURL");
            compressionEnabled = Boolean.parseBoolean(properties.getProperty("compressionEnabled", "true"));
        } else {
            callserverUrl = arg0;
            compressionEnabled = arg1;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new ConnectionTester(callserverUrl, compressionEnabled).setVisible(true);
                }
            });
    }
}
