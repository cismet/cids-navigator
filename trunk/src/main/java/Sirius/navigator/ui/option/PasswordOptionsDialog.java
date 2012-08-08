/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.option;

import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.dialog.PasswordDialog;

import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.cismet.lookupoptions.AbstractOptionsPanel;
import de.cismet.lookupoptions.OptionsPanelController;

import de.cismet.lookupoptions.options.SecurityOptionsCategory;

/**
 * The PasswordOptionsDialog is intended to provide the user with means for changing her password. It is displayed in
 * the menu "Extras -> Optionen -> Sicherheit->Kennword ändern" and replaces {@link PasswordDialog}.
 *
 * @author   Benjamin Friedrich (benjamin.friedrich@cismet.de)
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsPanelController.class)
public class PasswordOptionsDialog extends AbstractOptionsPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(PasswordOptionsDialog.class);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePassword;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lblDialogDescription;
    private javax.swing.JLabel lblNewPassword;
    private javax.swing.JLabel lblOldPassword;
    private javax.swing.JLabel lblPasswordAgain;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField pwdNewPassword;
    private javax.swing.JPasswordField pwdOldPassword;
    private javax.swing.JPasswordField pwdPasswordAgain;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form PasswordOptionsDialog.
     */
    public PasswordOptionsDialog() {
        super(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.title"), // NOI18N,
            SecurityOptionsCategory.class);

        initComponents();

        this.txtUser.setText(SessionManager.getSession().getUser().getName());
        this.clearPwdFields();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void clearPwdFields() {
        this.pwdNewPassword.setText("");
        this.pwdOldPassword.setText("");
        this.pwdPasswordAgain.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblUser = new javax.swing.JLabel();
        lblOldPassword = new javax.swing.JLabel();
        lblNewPassword = new javax.swing.JLabel();
        lblPasswordAgain = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        pwdNewPassword = new javax.swing.JPasswordField();
        pwdOldPassword = new javax.swing.JPasswordField();
        pwdPasswordAgain = new javax.swing.JPasswordField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        btnChangePassword = new javax.swing.JButton();
        lblDialogDescription = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMaximumSize(new java.awt.Dimension(264, 177));
        setMinimumSize(new java.awt.Dimension(100, 177));
        setLayout(new java.awt.GridBagLayout());

        lblUser.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.lblUser.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(lblUser, gridBagConstraints);

        lblOldPassword.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.lblOldPassword.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(lblOldPassword, gridBagConstraints);

        lblNewPassword.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.lblNewPassword.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(lblNewPassword, gridBagConstraints);

        lblPasswordAgain.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.lblPasswordAgain.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(lblPasswordAgain, gridBagConstraints);

        txtUser.setEditable(false);
        txtUser.setEnabled(false);
        txtUser.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(txtUser, gridBagConstraints);

        pwdNewPassword.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(pwdNewPassword, gridBagConstraints);

        pwdOldPassword.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(pwdOldPassword, gridBagConstraints);

        pwdPasswordAgain.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(pwdPasswordAgain, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        add(filler1, gridBagConstraints);

        btnChangePassword.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.btnChangePassword.text"));    // NOI18N
        btnChangePassword.setToolTipText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.btnChangePassword.tooltip")); // NOI18N
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnChangePasswordActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(btnChangePassword, gridBagConstraints);

        lblDialogDescription.setText(org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.lblDialogDescription.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 10, 4);
        add(lblDialogDescription, gridBagConstraints);
    }                                                                // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnChangePasswordActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnChangePasswordActionPerformed
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final JFrame mainWindow = ComponentRegistry.getRegistry().getMainWindow();

                    final char[] pwdOld = pwdOldPassword.getPassword();
                    final char[] pwdNew = pwdNewPassword.getPassword();
                    final char[] pwdAgain = pwdPasswordAgain.getPassword();

                    if ((pwdOld.length == 0)
                                || (pwdNew.length == 0)
                                || (pwdAgain.length == 0)) {
                        JOptionPane.showMessageDialog(
                            mainWindow,
                            org.openide.util.NbBundle.getMessage(
                                PasswordOptionsDialog.class,
                                "PasswordOptionsDialog.btnChangePasswordActionPerformed().missingInputError.message"), // NOI18N
                            org.openide.util.NbBundle.getMessage(
                                PasswordOptionsDialog.class,
                                "PasswordOptionsDialog.btnChangePasswordActionPerformed().missingInputError.title"), // NOI18N
                            JOptionPane.ERROR_MESSAGE);
                    } else if (!Arrays.equals(pwdNew, pwdAgain)) {
                        JOptionPane.showMessageDialog(
                            mainWindow,
                            org.openide.util.NbBundle.getMessage(
                                PasswordOptionsDialog.class,
                                "PasswordOptionsDialog.btnChangePasswordActionPerformed().passwordsDifferentError.message"), // NOI18N
                            org.openide.util.NbBundle.getMessage(
                                PasswordOptionsDialog.class,
                                "PasswordOptionsDialog.btnChangePasswordActionPerformed().passwordsDifferentError.title"), // NOI18N
                            JOptionPane.ERROR_MESSAGE);

                        pwdNewPassword.setText("");
                        pwdPasswordAgain.setText("");
                    } else {
                        try {
                            final ConnectionProxy proxy = SessionManager.getProxy();
                            final ConnectionSession session = SessionManager.getSession();

                            final boolean success = proxy.changePassword(
                                    session.getUser(),
                                    String.valueOf(pwdOld),
                                    String.valueOf(pwdNew));

                            if (success) {
                                JOptionPane.showMessageDialog(
                                    mainWindow,
                                    org.openide.util.NbBundle.getMessage(
                                        PasswordOptionsDialog.class,
                                        "PasswordOptionsDialog.btnChangePasswordActionPerformed().passwordOK.message"), // NOI18N
                                    org.openide.util.NbBundle.getMessage(
                                        PasswordOptionsDialog.class,
                                        "PasswordOptionsDialog.btnChangePasswordActionPerformed().passwordOK.title"), // NOI18N
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(
                                    mainWindow,
                                    org.openide.util.NbBundle.getMessage(
                                        PasswordOptionsDialog.class,
                                        "PasswordOptionsDialog.btnChangePasswordActionPerformed().changePasswordError.message"), // NOI18N
                                    org.openide.util.NbBundle.getMessage(
                                        PasswordOptionsDialog.class,
                                        "PasswordOptionsDialog.btnChangePasswordActionPerformed().changePasswordError.title"), // NOI18N
                                    JOptionPane.ERROR_MESSAGE);
                            }

                            clearPwdFields();
                        } catch (final Exception exp) {
                            LOG.error("an error occurred while changing the password", exp);
                            JOptionPane.showMessageDialog(
                                mainWindow,
                                exp.getMessage(),
                                org.openide.util.NbBundle.getMessage(
                                    PasswordOptionsDialog.class,
                                    "PasswordOptionsDialog.btnChangePasswordActionPerformed().error.title"), // NOI18N
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
    }                             //GEN-LAST:event_btnChangePasswordActionPerformed

    @Override
    public void update() {
        this.clearPwdFields();
    }

    /**
     * Returns tooltip text.
     *
     * @return  tooltip text
     */
    @Override
    public String getTooltip() {
        return org.openide.util.NbBundle.getMessage(
                PasswordOptionsDialog.class,
                "PasswordOptionsDialog.tooltip"); // NOI18N
    }
}
