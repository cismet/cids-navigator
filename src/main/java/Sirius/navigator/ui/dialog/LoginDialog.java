/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       01.02.2000
 * History                      :
 *
 *******************************************************************************/
import Sirius.navigator.connection.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.resource.*;

import Sirius.server.newuser.UserException;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.border.*;

//import Sirius.server.user.UserException;
//import Sirius.navigator.*;
//import Sirius.navigator.connection.*;
/**
 * Der Login Dialog in dem Benutzername, Passwort und Localserver angegeben werden muessen. Der Dialog ist modal, ein
 * Klick auf 'Abbrechen' beendet das Programm sofort.
 *
 * @author   Pascal Dih&eacute;
 * @version  1.0
 */
public class LoginDialog extends JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final String PREF_NAME = "username";       // NOI18N
    private static final String PREF_DOMAIN = "domain";       // NOI18N
    private static final String PREF_USERGROUP = "usergroup"; // NOI18N
    private static final Logger logger = Logger.getLogger(LoginDialog.class);

    //~ Instance fields --------------------------------------------------------

    private final ResourceManager resources;
    private UserGroupChooser userGroupChooser;
    private String[][] userGroupLSNames;
    private JTextField tf_name;
    private JPasswordField pf_pass;
    private JComboBox cb_userGroup;
    private JComboBox cb_srv;
    // private JButton btn_userGroup;
    private JButton btn_ok;
    private JButton btn_cancel;
    private Preferences preferences;

    //~ Constructors -----------------------------------------------------------

    /**
     * Konstruiert einen neuen LoginDialog mit dem Titel 'Login'.
     *
     * @param  owner  navigator Der LoginDialog wird relativ zu diesem Window zentriert
     */
    public LoginDialog(final JFrame owner) {
        super(owner, org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.title"), true); // NOI18N
        this.resources = ResourceManager.getManager();
        this.preferences = Preferences.userNodeForPackage(this.getClass());
        this.setAlwaysOnTop(true);
        init();

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    LoginDialog.this.requestFocus();
                    LoginDialog.this.toFront();
                    tf_name.requestFocus();
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Initialisierungsmethode.<br>
     * Wird nur von den Konstruktoren aufgerufen.
     */
    protected void init() {
        final ActionListener loginListener = new LoginListener();

        // So kann der Dialog nich ueber |X| geschlossen werden!
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());

        userGroupChooser = new UserGroupChooser(
                this,
                org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.userGroupChooser.title"), // NOI18N
                org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.userGroupChooser.infoString")); // NOI18N
        userGroupChooser.setLocationRelativeTo(this);

        final JLabel lbl_img = new JLabel(resources.getIcon("login_icon.gif")); // NOI18N
        lbl_img.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.LOWERED),
                new EmptyBorder(20, 20, 20, 20)));

        final JLabel lbl_info = new JLabel(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.userGroupChooser.lbl_info.text")); // NOI18N
        final JLabel lbl_name = new JLabel(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.userGroupChooser.lbl_name.text")); // NOI18N
        final JLabel lbl_pass = new JLabel(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.userGroupChooser.lbl_pass.text")); // NOI18N
        final JLabel lbl_usr = new JLabel(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.userGroupChooser.lbl_usr.text"));  // NOI18N
        final JLabel lbl_srv = new JLabel(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.userGroupChooser.lbl_srv.text"));  // NOI18N

        tf_name = new JTextField(12);
        tf_name.setActionCommand("ok"); // NOI18N
        tf_name.addActionListener(loginListener);
        tf_name.addFocusListener(new NameListener());

        pf_pass = new JPasswordField(12);
        pf_pass.setActionCommand("ok"); // NOI18N
        pf_pass.addActionListener(loginListener);

        cb_srv = new JComboBox();
        cb_srv.setLightWeightPopupEnabled(false);
        cb_srv.addItemListener(new DomainListener());

        cb_userGroup = new JComboBox();
        cb_userGroup.setLightWeightPopupEnabled(false);

        btn_ok = new JButton(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.btn_ok.text"));     // NOI18N
        btn_ok.setMnemonic(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.btn_ok.mnemonic")
                    .charAt(0));                                                                                      // NOI18N
        btn_ok.setToolTipText(org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.btn_ok.tooltip")); // NOI18N
        btn_ok.setActionCommand("ok");                                                                                // NOI18N
        btn_ok.addActionListener(loginListener);

        btn_cancel = new JButton(org.openide.util.NbBundle.getMessage(
                    LoginDialog.class,
                    "LoginDialog.btn_cancel.text"));           // NOI18N
        btn_cancel.setMnemonic(org.openide.util.NbBundle.getMessage(
                LoginDialog.class,
                "LoginDialog.btn_cancel.mnemonic").charAt(0)); // NOI18N
        btn_cancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                LoginDialog.class,
                "LoginDialog.btn_cancel.tooltip"));            // NOI18N
        btn_cancel.setActionCommand("cancel");                 // NOI18N
        btn_cancel.addActionListener(loginListener);

        // _TA_Muss wohl nicht ersetzt werden
        // btn_userGroup = new JButton("?");
        // btn_userGroup.setMnemonic('?');
        // btn_userGroup.setMargin(new Insets(0, 2, 0, 2));
        // btn_userGroup.setActionCommand("userGroup");
        // btn_userGroup.addActionListener(loginListener);

        final EmptyBorder border = new EmptyBorder(new Insets(0, 3, 0, 0));
        final GridBagConstraints c = new GridBagConstraints();

        // Info Text ======================================
        c.insets = new Insets(20, 10, 20, 10);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 4;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0;
        this.getContentPane().add(lbl_info, c);

        // Icon ===========================================
        c.insets = new Insets(8, 20, 8, 20);
        // c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridheight = 4;
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0.0;
        this.getContentPane().add(lbl_img, c);

        // Labels =========================================
        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = 0.0;

        // Name
        c.gridheight = 1;
        c.gridwidth = 1;
        lbl_name.setBorder(border);
        this.getContentPane().add(lbl_name, c);

        // Passwort
        c.gridy++;
        // c.gridx = 1;
        lbl_pass.setBorder(border);
        this.getContentPane().add(lbl_pass, c);

        // LocalServer
        c.gridy++;
        // c.gridx = 1;
        lbl_srv.setBorder(border);
        this.getContentPane().add(lbl_srv, c);

        // UserGroup@LocalServer
        c.gridy++;
        // c.gridx = 1;
        lbl_usr.setBorder(border);
        this.getContentPane().add(lbl_usr, c);

        // Eingabefelder ==================================
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;
        c.weightx = 1.0;

        // Name
        c.gridy = 1;
        c.gridx = 2;
        this.getContentPane().add(tf_name, c);

        // Passwort
        c.gridy++;
        // c.gridx = 2;
        this.getContentPane().add(pf_pass, c);

        // Server ComboBox
        c.gridy++;
        // c.gridx = 2;
        this.getContentPane().add(cb_srv, c);

        // Usergroup ComboBox
        // c.insets = new Insets(8, 10, 8, 5);
        // c.gridwidth = 1;
        c.gridy++;
        // c.gridx = 2;
        this.getContentPane().add(cb_userGroup, c);

        // Usergroup Button
        // c.fill = GridBagConstraints.BOTH;
        // c.insets = new Insets(8, 0, 8, 10);
        // c.gridx++;
        // c.weightx = 0.0;
        // this.getContentPane().add(btn_userGroup, c);

        // Buttons =======================================

        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        // OK
        c.weightx = 0.0;
        c.gridy++;
        c.gridx = 1;
        this.getContentPane().add(btn_ok, c);

        // Abbrechen
        c.gridwidth = 2;
        c.weightx = 1.0;
        // c.gridy = 4;
        c.gridx = 2;
        this.getContentPane().add(btn_cancel, c);
        this.pack();
        this.setResizable(false);
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

            final String[] domains = SessionManager.getProxy().getDomains();

            for (int i = 0; i < domains.length; i++) {
                cb_srv.addItem(domains[i]);
            }

            final String name = this.preferences.get(this.PREF_NAME, null);
            if ((name != null) && (name.length() > 0)) {
                tf_name.setText(name);
            }

            final String domain = this.preferences.get(this.PREF_DOMAIN, null);
            if ((domain != null) && (domain.length() > 0)) {
                cb_srv.setSelectedItem(domain);
            }

            this.updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());

            final String usergroup = this.preferences.get(this.PREF_USERGROUP, null);
            if ((usergroup != null) && (usergroup.length() > 0)) {
                this.cb_userGroup.setSelectedItem(usergroup);
            }

            if ((name != null) && (name.length() > 0)) {
                this.pf_pass.requestFocus();
            } else {
                this.tf_name.requestFocus();
            }
        } catch (Throwable t) {
            logger.fatal("fatal error during login", t);                                                       // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        this,
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.show().name"),    // NOI18N
                        org.openide.util.NbBundle.getMessage(LoginDialog.class, "LoginDialog.show().message"), // NOI18N
                        t);                                                                                    // NOI18N
            System.exit(1);
        }

        this.pack();
        super.show();
        this.toFront();
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
            final Vector tmpVector = SessionManager.getProxy().getUserGroupNames();
            userGroupLSNames = (String[][])tmpVector.toArray(new String[tmpVector.size()][2]);
        } else {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("retrieving usergroups for user '" + user + "' @ domain '" + domain + "'"); // NOI18N
                }

                final Vector tmpVector = SessionManager.getProxy().getUserGroupNames(user, domain);
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

        for (int i = 0; i < userGroupLSNames.length; i++) {
            cb_userGroup.addItem(userGroupLSNames[i][0] + "@" + userGroupLSNames[i][1].trim()); // NOI18N
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class NameListener implements FocusListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void focusGained(final FocusEvent e) {
        }

        @Override
        public void focusLost(final FocusEvent e) {
            try {
                final String name = tf_name.getText();
                if ((name != null) && (name.length() > 0) && (cb_srv.getSelectedIndex() >= 0)) {
                    updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
                }
            } catch (Throwable t) {
                logger.fatal("fatal error during login", t);                               // NOI18N
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
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class DomainListener implements ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    final String name = tf_name.getText();
                    if ((name != null) && (name.length() > 0)) {
                        updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
                    }
                } catch (Throwable t) {
                    logger.fatal("fatal error during login", t);                                       // NOI18N
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
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class LoginListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when an action occurs.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                /*if(e.getActionCommand().equals("userGroup"))
                 * { if(tf_name.getText().length() <= 0) { JOptionPane.showMessageDialog(LoginDialog.this,
                 * resources.getString("login.missing.username"), resources.getString("login.missing.input"),
                 * JOptionPane.WARNING_MESSAGE); } else { try { if(logger.isDebugEnabled())logger.debug("retrieving
                 * usergroups for user '" + tf_name.getText() + "' @ domain '" + cb_srv.getSelectedItem().toString() +
                 * "'"); Vector tmpVector = SessionManager.getProxy().getUserGroupNames(tf_name.getText(),
                 * cb_srv.getSelectedItem().toString());
                 * userGroupChooser.setTitle(resources.getString("login.usergroup.title") + " '" + tf_name.getText() +
                 * "'"); userGroupChooser.show((String[][])tmpVector.toArray(new String[tmpVector.size()][2])); String[]
                 * tmpArray = SessionManager.getProxy().getDomains(); if(userGroupChooser.isUserGroupAccepted()) {
                 * if(logger.isDebugEnabled())logger.debug("selecting usergroup '" +
                 * userGroupChooser.getSelectedUserGroup() + "'");
                 * cb_userGroup.setSelectedItem(userGroupChooser.getSelectedUserGroup()); } } catch(UserException ue) {
                 * JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.username"),
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
                    } else if (cb_userGroup.getSelectedIndex() < 0) {
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
                            SessionManager.getSession()
                                    .login(
                                        userGroupLSNames[cb_userGroup.getSelectedIndex()][1],
                                        userGroupLSNames[cb_userGroup.getSelectedIndex()][0],
                                        cb_srv.getSelectedItem().toString(),
                                        tf_name.getText(),
                                        new String(pf_pass.getPassword()));

                            LoginDialog.this.preferences.put(LoginDialog.this.PREF_NAME, tf_name.getText());
                            LoginDialog.this.preferences.put(
                                LoginDialog.this.PREF_DOMAIN,
                                cb_srv.getSelectedItem().toString());
                            LoginDialog.this.preferences.put(
                                LoginDialog.this.PREF_USERGROUP,
                                cb_userGroup.getSelectedItem().toString());

                            dispose();
                        } catch (UserException u) {
                            if (u.wrongUserName()) {
                                JOptionPane.showMessageDialog(
                                    LoginDialog.this,
                                    org.openide.util.NbBundle.getMessage(
                                        LoginDialog.class,
                                        "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsernameOptionPane.message"), // NOI18N
                                    org.openide.util.NbBundle.getMessage(
                                        LoginDialog.class,
                                        "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongUsernameOptionPane.title"), // NOI18N
                                    JOptionPane.ERROR_MESSAGE);
                                tf_name.setText("");                                                                             // NOI18N
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
                                pf_pass.setText("");                                                                             // NOI18N
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
                                        "LoginDialog.LoginListener.actionPerformed(ActionEvent).wrongDomainOptionPane.title"),   // NOI18N
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
                                        "LoginDialog.LoginListener.actionPerformed(ActionEvent).loginFailedOptionPane.title"),   // NOI18N
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else if (e.getActionCommand().equals("cancel")) {                                                              // NOI18N
                    if (ExceptionManager.getManager().showExitDialog(LoginDialog.this)) {
                        logger.info("close program");                                                                            // NOI18N
                        System.exit(0);
                    }
                }
            } catch (ConnectionException cexp) {
                ExceptionManager.getManager().showExceptionDialog(LoginDialog.this, cexp);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class UserGroupChooser extends StringChooser {

        //~ Instance fields ----------------------------------------------------

        protected String[][] userGroups = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new UserGroupChooser object.
         *
         * @param  dialog  DOCUMENT ME!
         * @param  title   DOCUMENT ME!
         */
        public UserGroupChooser(final JDialog dialog, final String title) {
            super(dialog, title);
        }

        /**
         * Creates a new UserGroupChooser object.
         *
         * @param  dialog      DOCUMENT ME!
         * @param  title       DOCUMENT ME!
         * @param  infoString  DOCUMENT ME!
         */
        public UserGroupChooser(final JDialog dialog, final String title, final String infoString) {
            super(dialog, title, infoString);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  userGroups  DOCUMENT ME!
         */
        public void show(final String[][] userGroups) {
            this.userGroups = userGroups;
            if ((userGroups != null) && (userGroups.length > 0)) {
                final String[] tmpStrArray = new String[userGroups.length];
                for (int i = 0; i < userGroups.length; i++) {
                    tmpStrArray[i] = userGroups[i][0];

                    if ((userGroups[i][1] != null) && (userGroups[i][1].length() > 0)) {
                        tmpStrArray[i] += ("@" + userGroups[i][1]); // NOI18N
                    }
                }

                super.show(tmpStrArray);
            } else {
                super.show(); // null);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isUserGroupAccepted() {
            return isSelectionAccepted();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getSelectedUserGroup() {
            return getSelectedString();
        }
    }
}
