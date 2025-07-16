/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.resource.PropertyManager;

import Sirius.server.newuser.LoginDeactivatedUserException;
import Sirius.server.newuser.LoginRestrictionUserException;
import Sirius.server.newuser.UserException;

import org.apache.log4j.Logger;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * Der Login Dialog in dem Benutzername, Passwort und Localserver angegeben werden muessen. Der Dialog ist modal, ein
 * Klick auf 'Abbrechen' beendet das Programm sofort.
 *
 * @author   Pascal Dih&eacute;
 * @version  1.0
 */
public class LoginDialog extends JDialog implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final String PREF_NAME = "username";       // NOI18N
    private static final String PREF_DOMAIN = "domain";       // NOI18N
    private static final String PREF_USERGROUP = "usergroup"; // NOI18N

    private static final Logger LOG = Logger.getLogger(LoginDialog.class);

    //~ Instance fields --------------------------------------------------------

    private String[][] userGroupLSNames;
    private Preferences preferences;
    private boolean userGroupIsOptional = PropertyManager.getManager().getPermissionModus()
                == PropertyManager.PermissionModus.OPTIONAL;
    private boolean userGroupIsForbidden = PropertyManager.getManager().getPermissionModus()
                == PropertyManager.PermissionModus.FORBIDDEN;

    private final ConnectionContext connectionContext;
    private String startName = null;
    private String startGroup = null;
    private String startUserDomain = null;
    private boolean sorted = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_ok;
    private javax.swing.JComboBox cb_srv;
    private javax.swing.JComboBox cb_userGroup;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JLabel lbl_info;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_pass;
    private javax.swing.JLabel lbl_srv;
    private javax.swing.JLabel lbl_usr;
    private javax.swing.JPasswordField pf_pass;
    private javax.swing.JTextField tf_name;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Konstruiert einen neuen LoginDialog mit dem Titel 'Login'.
     *
     * @param  owner  navigator Der LoginDialog wird relativ zu diesem Window zentriert
     */
    public LoginDialog(final java.awt.Frame owner) {
        this(owner, false, false);
    }

    /**
     * Konstruiert einen neuen LoginDialog mit dem Titel 'Login'.
     *
     * @param  owner         navigator Der LoginDialog wird relativ zu diesem Window zentriert
     * @param  withoutGroup  DOCUMENT ME!
     * @param  sorted        DOCUMENT ME!
     */
    public LoginDialog(final java.awt.Frame owner, final boolean withoutGroup, final boolean sorted) {
        super(owner, true);
        if (owner instanceof ConnectionContextProvider) {
            connectionContext = (ConnectionContext)((ConnectionContextProvider)owner).getConnectionContext();
        } else {
            connectionContext = ConnectionContext.createDummy();
        }

        preferences = Preferences.userNodeForPackage(getClass());

        setAlwaysOnTop(true);

        // So kann der Dialog nich ueber |X| geschlossen werden!
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        if (withoutGroup) {
            userGroupIsOptional = true;
            userGroupIsForbidden = true;
        }
        this.sorted = sorted;

        initComponents();

        lbl_usr.setVisible(!userGroupIsForbidden);
        cb_userGroup.setVisible(!userGroupIsForbidden);

        pack();
        setResizable(false);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setVisible(final boolean b) {
        super.setVisible(b);

        if (b) {
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        requestFocus();
                        toFront();
                        tf_name.requestFocus();
                    }
                });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tf_name = new javax.swing.JTextField();
        pf_pass = new javax.swing.JPasswordField();
        cb_userGroup = new javax.swing.JComboBox();
        cb_srv = new javax.swing.JComboBox();
        btn_ok = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        lbl_img = new javax.swing.JLabel();
        lbl_info = new javax.swing.JLabel();
        lbl_name = new javax.swing.JLabel();
        lbl_pass = new javax.swing.JLabel();
        lbl_srv = new javax.swing.JLabel();
        lbl_usr = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tf_name.setColumns(12);
        tf_name.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.tf_name.text")); // NOI18N
        tf_name.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tf_nameActionPerformed(evt);
                }
            });
        tf_name.addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusLost(final java.awt.event.FocusEvent evt) {
                    tf_nameFocusLost(evt);
                }
            });
        tf_name.setActionCommand("ok"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(tf_name, gridBagConstraints);

        pf_pass.setColumns(12);
        pf_pass.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.pf_pass.text")); // NOI18N
        pf_pass.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    pf_passActionPerformed(evt);
                }
            });
        pf_pass.setActionCommand("ok"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(pf_pass, gridBagConstraints);

        cb_userGroup.setLightWeightPopupEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(cb_userGroup, gridBagConstraints);

        cb_srv.setLightWeightPopupEnabled(false);
        cb_srv.addItemListener(new java.awt.event.ItemListener() {

                @Override
                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    cb_srvItemStateChanged(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(cb_srv, gridBagConstraints);

        btn_ok.setMnemonic(java.util.ResourceBundle.getBundle("Sirius/navigator/ui/dialog/Bundle").getString(
                "LoginDialog.btn_ok.mnemonic").charAt(0));
        btn_ok.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.btn_ok.text")); // NOI18N
        btn_ok.setToolTipText(org.openide.util.NbBundle.getMessage(
                LoginDialog.class,
                "LoginDialog.btn_ok.toolTipText"));                                                         // NOI18N
        btn_ok.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btn_okActionPerformed(evt);
                }
            });
        btn_ok.setActionCommand("ok"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(btn_ok, gridBagConstraints);

        btn_cancel.setMnemonic(java.util.ResourceBundle.getBundle("Sirius/navigator/ui/dialog/Bundle").getString(
                "LoginDialog.btn_cancel.mnemonic").charAt(0));
        btn_cancel.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.btn_cancel.text")); // NOI18N
        btn_cancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                LoginDialog.class,
                "LoginDialog.btn_cancel.toolTipText"));                                                             // NOI18N
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btn_cancelActionPerformed(evt);
                }
            });
        btn_cancel.setActionCommand("cancel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(btn_cancel, gridBagConstraints);

        lbl_img.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/Sirius/navigator/resource/img/login_icon.gif")));                    // NOI18N
        lbl_img.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_img.text")); // NOI18N
        lbl_img.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED),
                javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 8, 20);
        getContentPane().add(lbl_img, gridBagConstraints);

        lbl_info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_info.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_info.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 20, 10);
        getContentPane().add(lbl_info, gridBagConstraints);

        lbl_name.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_name.text")); // NOI18N
        lbl_name.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(lbl_name, gridBagConstraints);

        lbl_pass.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_pass.text")); // NOI18N
        lbl_pass.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(lbl_pass, gridBagConstraints);

        lbl_srv.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_srv.text")); // NOI18N
        lbl_srv.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(lbl_srv, gridBagConstraints);

        lbl_usr.setText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.lbl_usr.text")); // NOI18N
        lbl_usr.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 10);
        getContentPane().add(lbl_usr, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tf_nameActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_tf_nameActionPerformed
        refreshLogin(evt);
    }                                                                           //GEN-LAST:event_tf_nameActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void pf_passActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_pf_passActionPerformed
        refreshLogin(evt);
    }                                                                           //GEN-LAST:event_pf_passActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btn_okActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btn_okActionPerformed
        refreshLogin(evt);
    }                                                                          //GEN-LAST:event_btn_okActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btn_cancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btn_cancelActionPerformed
        refreshLogin(evt);
    }                                                                              //GEN-LAST:event_btn_cancelActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tf_nameFocusLost(final java.awt.event.FocusEvent evt) { //GEN-FIRST:event_tf_nameFocusLost
        refreshFocus(evt);
    }                                                                    //GEN-LAST:event_tf_nameFocusLost

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cb_srvItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_cb_srvItemStateChanged
        domainRefresh(evt);
    }                                                                         //GEN-LAST:event_cb_srvItemStateChanged

    /**
     * Set the default user, usergroup and domain, if they should not be taken from the preferences. If a parameter is
     * null, the value from the preferences will be used.
     *
     * @param  name        DOCUMENT ME!
     * @param  userGroup   DOCUMENT ME!
     * @param  userDomain  domain DOCUMENT ME!
     */
    public void setDefaultValues(final String name, final String userGroup, final String userDomain) {
        this.startName = name;
        this.startGroup = userGroup;
        this.startUserDomain = userDomain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void refreshFocus(final java.awt.event.FocusEvent evt) {
        try {
            final String name = tf_name.getText();
            if ((name != null) && (name.length() > 0) && (cb_srv.getSelectedIndex() >= 0)) {
                updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
            }
        } catch (Throwable t) {
            LOG.fatal("fatal error during login", t);                                  // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        LoginDialog.this,
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.NameListener.focusLost(FocusEvent).name"),    // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.NameListener.focusLost(FocusEvent).message"), // NOI18N
                        t);
            System.exit(1);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final LoginDialog dialog = new LoginDialog(new javax.swing.JFrame());
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                            @Override
                            public void windowClosing(final java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                    dialog.setVisible(true);
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        tf_name.setText(""); // NOI18N
        pf_pass.setText(""); // NOI18N
        cb_userGroup.setSelectedIndex(0);
        cb_srv.setSelectedIndex(0);
    }

    @Override
    public void show() {
        try {
            cb_userGroup.setModel(new DefaultComboBoxModel());
            cb_srv.setModel(new DefaultComboBoxModel());

            final String[] domains = SessionManager.getProxy().getDomains(getConnectionContext());

            if (sorted) {
                Arrays.sort(domains, new Comparator<String>() {

                        @Override
                        public int compare(final String o1, final String o2) {
                            return -1 * o1.compareTo(o2);
                        }
                    });
            }

            for (int i = 0; i < domains.length; i++) {
                cb_srv.addItem(domains[i]);
            }

            final String name = ((startName == null) ? preferences.get(PREF_NAME, null) : startName);
            if ((name != null) && (name.length() > 0)) {
                tf_name.setText(name);
            }

            final String domain = ((startUserDomain == null) ? preferences.get(PREF_DOMAIN, null) : startUserDomain);
            if ((domain != null) && (domain.length() > 0)) {
                cb_srv.setSelectedItem(domain);
            }

            updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());

            final String usergroup = ((startGroup == null) ? preferences.get(PREF_USERGROUP, null) : startGroup);
            if ((usergroup != null) && (usergroup.length() > 0)) {
                cb_userGroup.setSelectedItem(usergroup);
            }

            if ((name != null) && (name.length() > 0)) {
                pf_pass.requestFocus();
            } else {
                tf_name.requestFocus();
            }
        } catch (Throwable t) {
            LOG.fatal("fatal error during login", t);                                                          // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        this,
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.show().name"),    // NOI18N
                        org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.show().message"), // NOI18N
                        t);                                                                                    // NOI18N
            System.exit(1);
        }

        pack();

        // NOTE: This call can not be substituted by StaticSwingTools.showDialog(this) because
        // show() method overwrites JDialog.show(). StaticSwingTools.showDialog() calls
        // setVisible(true) which internally calls JDialog show() -> endless recursion if
        // StaticSwingTools.showDialog() is called here
        super.show();
        toFront();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    private void refreshLogin(final ActionEvent e) {
        try {
            /*if(e.getActionCommand().equals("userGroup"))
             * { if(tf_name.getText().length() <= 0) { JOptionPane.showMessageDialog(LoginDialog.this,
             * resources.getString("login.missing.username"), resources.getString("login.missing.input"),
             * JOptionPane.WARNING_MESSAGE); } else { try { if(logger.isDebugEnabled())logger.debug("retrieving
             * usergroups for user '" + tf_name.getText() + "' @ domain '" + cb_srv.getSelectedItem().toString() + "'");
             * Vector tmpVector = SessionManager.getProxy().getUserGroupNames(tf_name.getText(),
             * cb_srv.getSelectedItem().toString());
             * userGroupChooser.setTitle(resources.getString("login.usergroup.title") + " '" + tf_name.getText() + "'");
             * userGroupChooser.show((String[][])tmpVector.toArray(new String[tmpVector.size()][2])); String[] tmpArray
             * = SessionManager.getProxy().getDomains(); if(userGroupChooser.isUserGroupAccepted()) {
             * if(logger.isDebugEnabled())logger.debug("selecting usergroup '" + userGroupChooser.getSelectedUserGroup()
             * + "'"); cb_userGroup.setSelectedItem(userGroupChooser.getSelectedUserGroup()); } } catch(UserException
             * ue) { JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.username"),
             * resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE); tf_name.setText(""); } }
             * }else*/
            if (e.getActionCommand().equals("ok")) {                                                                      // NOI18N
                if (tf_name.getText().length() <= 0) {
                    JOptionPane.showMessageDialog(
                        LoginDialog.this,
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingUsernameOptionPane.message"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingUsernameOptionPane.title"),    // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                } else if (pf_pass.getPassword().length <= 0) {
                    JOptionPane.showMessageDialog(
                        LoginDialog.this,
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingPasswordOptionPane.message"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingPasswordOptionPane.title"),    // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                } else if (!userGroupIsForbidden && (cb_userGroup.getSelectedIndex() < 0)) {
                    JOptionPane.showMessageDialog(
                        LoginDialog.this,
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingUsergroupOptionPane.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingUsergroupOptionPane.title"),   // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                } else if (cb_srv.getSelectedIndex() < 0) {
                    JOptionPane.showMessageDialog(
                        LoginDialog.this,
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingDomainOptionPane.message"),    // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LoginDialog.class,
                            "LoginDialog.LoginListener.actionPerformed(ActionEvent).missingDomainOptionPane.title"),      // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        final int selectedUserGroupIndex = cb_userGroup.getSelectedIndex();
                        if (userGroupIsForbidden || (userGroupIsOptional && (selectedUserGroupIndex == 0))) {             // usergroup == null;
                            SessionManager.getSession()
                                    .login(
                                        cb_srv.getSelectedItem().toString(),
                                        tf_name.getText(),
                                        new String(pf_pass.getPassword()));
                        } else {
                            final int userGroupLSNameIndex = (userGroupIsOptional) ? (selectedUserGroupIndex - 1)
                                                                                   : selectedUserGroupIndex;
                            SessionManager.getSession()
                                    .login(
                                        userGroupLSNames[userGroupLSNameIndex][1],
                                        userGroupLSNames[userGroupLSNameIndex][0],
                                        cb_srv.getSelectedItem().toString(),
                                        tf_name.getText(),
                                        new String(pf_pass.getPassword()));
                        }

                        preferences.put(LoginDialog.PREF_NAME, tf_name.getText());
                        preferences.put(
                            PREF_DOMAIN,
                            cb_srv.getSelectedItem().toString());
                        if (cb_userGroup.getSelectedItem() != null) {
                            preferences.put(LoginDialog.PREF_USERGROUP, cb_userGroup.getSelectedItem().toString());
                        } else {
                            preferences.put(LoginDialog.PREF_USERGROUP, "");
                        }

                        dispose();
                    } catch (UserException u) {
                        if (u instanceof LoginDeactivatedUserException) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                u.getMessage(),
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginRestrictedOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                        } else if (u instanceof LoginRestrictionUserException) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginRestrictedOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginRestrictedOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                            pf_pass.setText("");
                        } else if (u.wrongUserName()) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsernameOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsernameOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                            tf_name.setText(""); // NOI18N
                        } else if (u.wrongPassword()) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongPasswordOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongPasswordOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                            pf_pass.setText(""); // NOI18N
                        } else if (u.wrongUserGroup()) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsergroupOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsergroupOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                            cb_userGroup.setSelectedIndex(0);
                        } else if (u.wrongLocalServer()) {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongDomainOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongDomainOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                            cb_srv.setSelectedIndex(0);
                        } else {
                            JOptionPane.showMessageDialog(
                                LoginDialog.this,
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginFailedOptionPane.message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LoginDialog.class,
                                    "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginFailedOptionPane.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else if (e.getActionCommand().equals("cancel")) { // NOI18N
                if (ExceptionManager.getManager().showExitDialog(LoginDialog.this)) {
                    LOG.info("close program"); // NOI18N
                    System.exit(0);
                }
            }
        } catch (ConnectionException cexp) {
            ExceptionManager.getManager().showExceptionDialog(LoginDialog.this, cexp);
        } catch (final Exception ex) {
            LOG.fatal("bla", ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   user    DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    protected void updateUserGroups(final String user, final String domain) throws ConnectionException, UserException {
        cb_userGroup.removeAllItems();

        if ((user == null) || (user.length() == 0) || (domain == null) || (domain.length() == 0)) {
            final List tmpVector = SessionManager.getProxy().getUserGroupNames(getConnectionContext());
            userGroupLSNames = (String[][])tmpVector.toArray(new String[tmpVector.size()][2]);
        } else {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("retrieving usergroups for user '" + user + "' @ domain '" + domain + "'"); // NOI18N
                }

                final List tmpVector = SessionManager.getProxy()
                            .getUserGroupNames(user, domain, getConnectionContext());
                userGroupLSNames = (String[][])tmpVector.toArray(new String[tmpVector.size()][2]);
            } catch (UserException ue) {
                JOptionPane.showMessageDialog(
                    LoginDialog.this,
                    org.openide.util.NbBundle.getMessage(
                        LoginDialog.class,
                        "LoginDialog.updateUserGroups().errorOptionPane.message"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        LoginDialog.class,
                        "LoginDialog.updateUserGroups().errorOptionPane.title"), // NOI18N
                    JOptionPane.ERROR_MESSAGE);
                tf_name.setText("");                                             // NOI18N
            }
        }

        if (!userGroupIsForbidden) {
            if (userGroupIsOptional) {
                cb_userGroup.addItem("[keine]");
            }

            for (int i = 0; i < userGroupLSNames.length; i++) {
                cb_userGroup.addItem(userGroupLSNames[i][0] + "@" + userGroupLSNames[i][1].trim()); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    private void domainRefresh(final ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            try {
                final String name = tf_name.getText();
                if ((name != null) && (name.length() > 0)) {
                    updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
                }
            } catch (Throwable t) {
                LOG.fatal("fatal error during login", t);                                          // NOI18N
                ExceptionManager.getManager()
                        .showExceptionDialog(
                            LoginDialog.this,
                            ExceptionManager.FATAL,
                            org.openide.util.NbBundle.getMessage(
                                LoginDialog.class,
                                "LoginDialog.DomainListener.itemStateChanged(ItemEvent).name"),    // NOI18N
                            org.openide.util.NbBundle.getMessage(
                                LoginDialog.class,
                                "LoginDialog.DomainListener.itemStateChanged(ItemEvent).message"), // NOI18N
                            t);
                System.exit(1);
            }
        }
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
