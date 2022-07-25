/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import Sirius.navigator.connection.SessionManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.error.ErrorInfo;

import java.awt.Frame;

import java.util.logging.Level;

import javax.swing.JFrame;

import de.cismet.cids.server.actions.PublishCidsServerMessageAction;
import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class RemoteLog4JConfigChangerDialog extends javax.swing.JDialog implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(RemoteLog4JConfigChangerDialog.class);

    //~ Instance fields --------------------------------------------------------

    private final ConnectionContext connectionContext = ConnectionContext.createDummy();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form RemoteDebu.
     *
     * @param  parent  DOCUMENT ME!
     * @param  modal   DOCUMENT ME!
     */
    public RemoteLog4JConfigChangerDialog(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);
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

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.title")); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton1,
            org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jPanel1.add(jButton1);

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton2,
            org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jPanel1.add(jButton2);

        jPanel2.add(jPanel1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
        getContentPane().add(jPanel2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jTextField1.setText(org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jTextField1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField1, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<String>(
                new String[] { "DISABLED", "DEBUG", "INFO", "WARN", "ERROR", "FATAL" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel3,
            org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(4445, 0, 65535, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jSpinner1, gridBagConstraints);

        jTextField2.setText(org.openide.util.NbBundle.getMessage(
                RemoteLog4JConfigChangerDialog.class,
                "RemoteLog4JConfigChangerDialog.jTextField2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jTextField2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel3, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        try {
            final String userKey = jTextField1.getText();

            final RemoteLog4JConfig remoteConfig = new RemoteLog4JConfig(
                    jTextField2.getText(),
                    (Integer)jSpinner1.getValue(),
                    (String)jComboBox1.getSelectedItem());

            SessionManager.getConnection()
                    .executeTask(SessionManager.getSession().getUser(),
                        PublishCidsServerMessageAction.TASK_NAME,
                        SessionManager.getSession().getUser().getDomain(),
                        new ObjectMapper().writeValueAsString(remoteConfig),
                        getConnectionContext(),
                        new ServerActionParameter<>(
                            PublishCidsServerMessageAction.ParameterType.CATEGORY.toString(),
                            "log4j_remote_config"),
                        new ServerActionParameter<>(
                            PublishCidsServerMessageAction.ParameterType.USER.toString(),
                            userKey));
        } catch (final Exception ex) {
            LOG.fatal("Fehler beim Ausführen der Action.", ex);
            final ErrorInfo errorInfo = new ErrorInfo(
                    "Fehler",
                    "Fehler beim Ausführen der Action.",
                    null,
                    null,
                    ex,
                    Level.ALL,
                    null);
            JXErrorPane.showDialog(null, errorInfo);
        }
    } //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        final RemoteLog4JConfigChangerDialog dialog = new RemoteLog4JConfigChangerDialog(
                                new JFrame(),
                                true);
//                        final String callServerURL = args[0];
//                        final String domain = args[1];
                        final String callServerURL = "rmi://localhost/callServer";
                        final String domain = "WUNDA_BLAU";
                        dialog.login(callServerURL, domain, false);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                                @Override
                                public void windowClosing(final java.awt.event.WindowEvent e) {
                                    System.exit(0);
                                }
                            });
                        StaticSwingTools.showDialog(dialog);
                        System.exit(0);
                    } catch (final Exception ex) {
                        LOG.fatal("Fehler beim Starten des RemoteDebugConfigTester.", ex);
                        final ErrorInfo errorInfo = new ErrorInfo(
                                "Fehler",
                                "Fehler beim Starten des Password-RemoteDebugConfigTester.",
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
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     */
    private void login(final String callServerURL, final String domain, final boolean compressionEnabled) {
        final CidsAuthentification cidsAuth = new CidsAuthentification(
                callServerURL,
                domain,
                RemoteLog4JConfigChangerDialog.class.getSimpleName(),
                compressionEnabled,
                getConnectionContext());
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

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
