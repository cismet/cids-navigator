package Sirius.navigator.ui.dialog;

/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
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
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Version			:	1.0
 * Purpose			:
 * Created			:	01.02.2000
 * History			:
 *
 *******************************************************************************/
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.prefs.*;

import org.apache.log4j.Logger;

import Sirius.server.newuser.UserException;
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.exception.*;


//import Sirius.server.user.UserException;
//import Sirius.navigator.*;
//import Sirius.navigator.connection.*;
/**
 * Der Login Dialog in dem Benutzername, Passwort und Localserver angegeben
 * werden muessen. Der Dialog ist modal, ein Klick auf 'Abbrechen' beendet
 * das Programm sofort.
 *
 * @author Pascal Dih&eacute;
 * @version 1.0
 */
public class LoginDialog extends JDialog {

    private final static String PREF_NAME = "username";
    private final static String PREF_DOMAIN = "domain";
    private final static String PREF_USERGROUP = "usergroup";
    private final static Logger logger = Logger.getLogger(LoginDialog.class);
    private final ResourceManager resources;
    private UserGroupChooser userGroupChooser;
    private String[][] userGroupLSNames;
    private JTextField tf_name;
    private JPasswordField pf_pass;
    private JComboBox cb_userGroup,  cb_srv;
    //private JButton btn_userGroup;
    private JButton btn_ok,  btn_cancel;
    private Preferences preferences;

    /**
     * Konstruiert einen neuen LoginDialog mit dem Titel 'Login'.
     *
     * @param navigator Der LoginDialog wird relativ zu diesem Window zentriert
     * @param iCom Die InitialisationConnection (fuer User, LocalServer + LoginIcon)
     */
    public LoginDialog(JFrame owner) {
        super(owner, ResourceManager.getManager().getString("login.title"), true);
        this.resources = ResourceManager.getManager();
        this.preferences = Preferences.userNodeForPackage(this.getClass());
        this.setAlwaysOnTop(true);
        init();

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                LoginDialog.this.requestFocus();
                LoginDialog.this.toFront();
                tf_name.requestFocus();
            }
        });
    }

    /**
     * Initialisierungsmethode.<br>
     * Wird nur von den Konstruktoren aufgerufen.
     */
    protected void init() {
        ActionListener loginListener = new LoginListener();

        // So kann der Dialog nich ueber |X| geschlossen werden!
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());

        userGroupChooser = new UserGroupChooser(this, resources.getString("login.usergroup.title"), resources.getString("login.usergroup.message"));
        userGroupChooser.setLocationRelativeTo(this);

        JLabel lbl_img = new JLabel(resources.getIcon("login_icon.gif"));
        lbl_img.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(20, 20, 20, 20)));

        JLabel lbl_info = new JLabel(resources.getString("login.message"));
        JLabel lbl_name = new JLabel(resources.getString("login.username"));
        JLabel lbl_pass = new JLabel(resources.getString("login.password"));
        JLabel lbl_usr = new JLabel(resources.getString("login.usergroup"));
        JLabel lbl_srv = new JLabel(resources.getString("login.domain"));

        tf_name = new JTextField(12);
        tf_name.setActionCommand("ok");
        tf_name.addActionListener(loginListener);
        tf_name.addFocusListener(new NameListener());

        pf_pass = new JPasswordField(12);
        pf_pass.setActionCommand("ok");
        pf_pass.addActionListener(loginListener);

        cb_srv = new JComboBox();
        cb_srv.setLightWeightPopupEnabled(false);
        cb_srv.addItemListener(new DomainListener());

        cb_userGroup = new JComboBox();
        cb_userGroup.setLightWeightPopupEnabled(false);

        btn_ok = new JButton(resources.getButtonText("ok"));
        btn_ok.setMnemonic(resources.getButtonMnemonic("ok"));
        btn_ok.setToolTipText(resources.getButtonTooltip("ok"));
        btn_ok.setActionCommand("ok");
        btn_ok.addActionListener(loginListener);

        btn_cancel = new JButton(resources.getButtonText("cancel"));
        btn_cancel.setMnemonic(resources.getButtonMnemonic("cancel"));
        btn_cancel.setToolTipText(resources.getButtonTooltip("cancel"));
        btn_cancel.setActionCommand("cancel");
        btn_cancel.addActionListener(loginListener);

        //_TA_Muss wohl nicht ersetzt werden
        //btn_userGroup = new JButton("?");
        //btn_userGroup.setMnemonic('?');
        //btn_userGroup.setMargin(new Insets(0, 2, 0, 2));
        //btn_userGroup.setActionCommand("userGroup");
        //btn_userGroup.addActionListener(loginListener);


        EmptyBorder border = new EmptyBorder(new Insets(0, 3, 0, 0));
        GridBagConstraints c = new GridBagConstraints();

        // Info Text ======================================
        c.insets = new Insets(20, 10, 20, 10);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 4;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0;
        this.getContentPane().add(lbl_info, c);

        // Icon	===========================================
        c.insets = new Insets(8, 20, 8, 20);
        //c.anchor = GridBagConstraints.CENTER;
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
        //c.gridx = 1;
        lbl_pass.setBorder(border);
        this.getContentPane().add(lbl_pass, c);


        // LocalServer
        c.gridy++;
        //c.gridx = 1;
        lbl_srv.setBorder(border);
        this.getContentPane().add(lbl_srv, c);

        // UserGroup@LocalServer
        c.gridy++;
        //c.gridx = 1;
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
        //c.gridx = 2;
        this.getContentPane().add(pf_pass, c);

        // Server ComboBox
        c.gridy++;
        //c.gridx = 2;
        this.getContentPane().add(cb_srv, c);


        // Usergroup ComboBox
        //c.insets = new Insets(8, 10, 8, 5);
        //c.gridwidth = 1;
        c.gridy++;
        //c.gridx = 2;
        this.getContentPane().add(cb_userGroup, c);

        // Usergroup Button
        //c.fill = GridBagConstraints.BOTH;
        //c.insets = new Insets(8, 0, 8, 10);
        //c.gridx++;
        //c.weightx = 0.0;
        //this.getContentPane().add(btn_userGroup, c);


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
        //c.gridy = 4;
        c.gridx = 2;
        this.getContentPane().add(btn_cancel, c);
        this.pack();
        this.setResizable(false);
    }

    public void reset() {
        tf_name.setText("");
        pf_pass.setText("");
        cb_userGroup.setSelectedIndex(0);
        cb_srv.setSelectedIndex(0);
    }

    public void show() {
        try {
            cb_userGroup.setModel(new DefaultComboBoxModel());
            cb_srv.setModel(new DefaultComboBoxModel());

            String[] domains = SessionManager.getProxy().getDomains();

            for (int i = 0; i < domains.length; i++) {
                cb_srv.addItem(domains[i]);
            }

            String name = this.preferences.get(this.PREF_NAME, null);
            if (name != null && name.length() > 0) {
                tf_name.setText(name);
            }

            String domain = this.preferences.get(this.PREF_DOMAIN, null);
            if (domain != null && domain.length() > 0) {
                cb_srv.setSelectedItem(domain);
            }

            this.updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());

            String usergroup = this.preferences.get(this.PREF_USERGROUP, null);
            if (usergroup != null && usergroup.length() > 0) {
                this.cb_userGroup.setSelectedItem(usergroup);
            }

            if (name != null && name.length() > 0) {
                this.pf_pass.requestFocus();
            } else {
                this.tf_name.requestFocus();
            }
        } catch (Throwable t) {
            logger.fatal("fatal error during login", t);
            ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.FATAL, "lx01", t);
            System.exit(1);
        }

        this.pack();
        super.show();
        this.toFront();
    }

    protected void updateUserGroups(String user, String domain) throws ConnectionException, UserException {
        cb_userGroup.removeAllItems();

        if (user == null || user.length() == 0 || domain == null || domain.length() == 0) {
            Vector tmpVector = SessionManager.getProxy().getUserGroupNames();
            userGroupLSNames = (String[][]) tmpVector.toArray(new String[tmpVector.size()][2]);
        } else {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("retrieving usergroups for user '" + user + "' @ domain '" + domain + "'");
                }

                Vector tmpVector = SessionManager.getProxy().getUserGroupNames(user, domain);
                userGroupLSNames = (String[][]) tmpVector.toArray(new String[tmpVector.size()][2]);
            } catch (UserException ue) {
                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.username"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                tf_name.setText("");
            }
        }

        for (int i = 0; i < userGroupLSNames.length; i++) {
            cb_userGroup.addItem(userGroupLSNames[i][0] + "@" + userGroupLSNames[i][1].trim());
        }
    }

    private class NameListener implements FocusListener {

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            try {
                String name = tf_name.getText();
                if (name != null && name.length() > 0 && cb_srv.getSelectedIndex() >= 0) {
                    updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
                }
            } catch (Throwable t) {
                logger.fatal("fatal error during login", t);
                ExceptionManager.getManager().showExceptionDialog(LoginDialog.this, ExceptionManager.FATAL, "lx01", t);
                System.exit(1);
            }
        }
    }

    private class DomainListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    String name = tf_name.getText();
                    if (name != null && name.length() > 0) {
                        updateUserGroups(tf_name.getText(), cb_srv.getSelectedItem().toString());
                    }
                } catch (Throwable t) {
                    logger.fatal("fatal error during login", t);
                    ExceptionManager.getManager().showExceptionDialog(LoginDialog.this, ExceptionManager.FATAL, "lx01", t);
                    System.exit(1);
                }
            }
        }
    }

    private class LoginListener implements ActionListener {

        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            try {
                /*if(e.getActionCommand().equals("userGroup"))
                {
                if(tf_name.getText().length() <= 0)
                {
                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.missing.username"), resources.getString("login.missing.input"), JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                try
                {
                if(logger.isDebugEnabled())logger.debug("retrieving usergroups for user '" + tf_name.getText() + "' @ domain '" + cb_srv.getSelectedItem().toString() + "'");
                Vector tmpVector = SessionManager.getProxy().getUserGroupNames(tf_name.getText(), cb_srv.getSelectedItem().toString());
                userGroupChooser.setTitle(resources.getString("login.usergroup.title") + " '" + tf_name.getText() + "'");
                userGroupChooser.show((String[][])tmpVector.toArray(new String[tmpVector.size()][2]));
                String[] tmpArray = SessionManager.getProxy().getDomains();
                if(userGroupChooser.isUserGroupAccepted())
                {
                if(logger.isDebugEnabled())logger.debug("selecting usergroup '" + userGroupChooser.getSelectedUserGroup() + "'");
                cb_userGroup.setSelectedItem(userGroupChooser.getSelectedUserGroup());
                }
                }
                catch(UserException ue)
                {
                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.username"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                tf_name.setText("");
                }
                }
                }
                else*/
                if (e.getActionCommand().equals("ok")) {
                    if (tf_name.getText().length() <= 0) {
                        JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.missing.username"), resources.getString("login.missing.input"), JOptionPane.WARNING_MESSAGE);
                    } else if (pf_pass.getPassword().length <= 0) {
                        JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.missing.password"), resources.getString("login.missing.input"), JOptionPane.WARNING_MESSAGE);
                    } else if (cb_userGroup.getSelectedIndex() < 0) {
                        JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.missing.usergroup"), resources.getString("login.missing.input"), JOptionPane.WARNING_MESSAGE);
                    } else if (cb_srv.getSelectedIndex() < 0) {
                        JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.missing.domain"), resources.getString("login.missing.input"), JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            SessionManager.getSession().login(userGroupLSNames[cb_userGroup.getSelectedIndex()][1], userGroupLSNames[cb_userGroup.getSelectedIndex()][0], cb_srv.getSelectedItem().toString(), tf_name.getText(), new String(pf_pass.getPassword()));

                            LoginDialog.this.preferences.put(LoginDialog.this.PREF_NAME, tf_name.getText());
                            LoginDialog.this.preferences.put(LoginDialog.this.PREF_DOMAIN, cb_srv.getSelectedItem().toString());
                            LoginDialog.this.preferences.put(LoginDialog.this.PREF_USERGROUP, cb_userGroup.getSelectedItem().toString());

                            dispose();
                        } catch (UserException u) {
                            if (u.wrongUserName()) {
                                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.username"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                                tf_name.setText("");
                            } else if (u.wrongPassword()) {
                                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.password"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                                pf_pass.setText("");
                            } else if (u.wrongUserGroup()) {
                                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.usergroup"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                                cb_userGroup.setSelectedIndex(0);
                            } else if (u.wrongLocalServer()) {
                                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.wrong.domain"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                                cb_srv.setSelectedIndex(0);
                            } else {
                                JOptionPane.showMessageDialog(LoginDialog.this, resources.getString("login.failed"), resources.getString("login.wrong.input"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else if (e.getActionCommand().equals("cancel")) {
                    if (ExceptionManager.getManager().showExitDialog(LoginDialog.this)) {
                        logger.info("close program");
                        System.exit(0);
                    }
                }
            } catch (ConnectionException cexp) {
                ExceptionManager.getManager().showExceptionDialog(LoginDialog.this, cexp);
            }
        }
    }

    private class UserGroupChooser extends StringChooser {

        protected String[][] userGroups = null;

        public UserGroupChooser(JDialog dialog, String title) {
            super(dialog, title);
        }

        public UserGroupChooser(JDialog dialog, String title, String infoString) {
            super(dialog, title, infoString);
        }

        public void show(String[][] userGroups) {
            this.userGroups = userGroups;
            if (userGroups != null && userGroups.length > 0) {
                String[] tmpStrArray = new String[userGroups.length];
                for (int i = 0; i < userGroups.length; i++) {
                    tmpStrArray[i] = userGroups[i][0];

                    if (userGroups[i][1] != null && userGroups[i][1].length() > 0) {
                        tmpStrArray[i] += ("@" + userGroups[i][1]);
                    }
                }

                super.show(tmpStrArray);
            } else {
                super.show(); //null);
            }
        }

        public boolean isUserGroupAccepted() {
            return isSelectionAccepted();
        }

        public String getSelectedUserGroup() {
            return getSelectedString();
        }
    }
}
