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
 * Filename		:
 * Version			:	1.0
 * Purpose			:
 * Created			:	23.05.2000
 * History			:
 *
 *******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import Sirius.navigator.*;
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;
import Sirius.navigator.resource.*;
import java.util.ResourceBundle;


public class PasswordDialog extends JDialog
{
    private JButton btn_change, btn_cancel;
    private JPasswordField password_old, password_new, password_again;

    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    private static final ResourceManager resources = ResourceManager.getManager();

    public PasswordDialog(JFrame parent)
    {
        //TA_super(navigator, "Passwort aendern", true);
        super(parent, I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.title") , true);
        initPasswordDialog();
    }
    
    protected void initPasswordDialog()
    {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        GridBagConstraints constraints = new GridBagConstraints();
        PasswordActionListener passwordActionListener = new PasswordActionListener();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        
        constraints.insets = new Insets(0, 0, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridy = 0;
        constraints.gridx = 0;
        JLabel passwordIcon = new JLabel(resources.getIcon(
                I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.initPasswordDialog().passwordIcon.icon")));
        //JLabel passwordIcon = new JLabel(ConnectionHandler.getDefaultIcon("animated.gif"));
        passwordIcon.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(10,10,10,10)));
        contentPane.add(passwordIcon, constraints);
        
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.gridy = 0;
        constraints.gridx++;
        //_TA_contentPane.add(new JLabel("Altes Passwort:"), constraints);
        contentPane.add(new JLabel(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.initPasswordDialog().contentPane.oldPWLabel.text")), constraints);
        
        //constraints.gridheight = 1;
        //constraints.gridwidth = 1;
        //constraints.weightx = 0.0;
        //constraints.weighty = 0.0;
        constraints.gridy++;
        //constraints.gridx;
        //_TA_contentPane.add(new JLabel("Neues Passwort:"), constraints);
        contentPane.add(new JLabel(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.initPasswordDialog().contentPane.newPWLabel.text")), constraints);
        
        constraints.gridy++;
        //_TA_contentPane.add(new JLabel("Bestaetigung:"), constraints);
        contentPane.add(new JLabel(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.initPasswordDialog().contentPane.repeatPWLabel.text")), constraints);
        
        constraints.insets = new Insets(0, 0, 10, 0);
        constraints.gridx++;
        constraints.gridy = 0;
        password_old = new JPasswordField();
        password_old.setColumns(10);
        contentPane.add(password_old, constraints);
        
        constraints.gridy++;
        password_new = new JPasswordField();
        password_new.setColumns(10);
        contentPane.add(password_new, constraints);
        
        constraints.gridy++;
        password_again = new JPasswordField();
        password_again.setColumns(10);
        contentPane.add(password_again, constraints);
        
        constraints.insets = new Insets(0, 0, 0, 10);
        constraints.gridx--;
        constraints.gridy++;
        //_TA_btn_change = new JButton("Aendern");
        btn_change = new JButton(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.btn_change.text"));
        //_TA_btn_change.setMnemonic('A');
        btn_change.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.btn_change.mnemonic").charAt(0));
        btn_change.setActionCommand("btn_change");
        btn_change.addActionListener(passwordActionListener);
        contentPane.add(btn_change, constraints);
        
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx++;
        //_TA_btn_cancel = new JButton("Abbrechen");
        btn_cancel = new JButton(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.btn_cancel.text"));
        //_TA_btn_cancel.setMnemonic('b');
        btn_cancel.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.btn_cancel.mnemonic").charAt(0));
        btn_cancel.setActionCommand("btn_cancel");
        btn_cancel.addActionListener(passwordActionListener);
        contentPane.add(btn_cancel, constraints);
        
        this.setContentPane(contentPane);
        this.pack();
        
        
    }
    
    class PasswordActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("btn_change"))
            {
                if(password_old.getPassword().length < 1 || password_new.getPassword().length < 1 || password_new.getPassword().length < 1)
                {
                    //_TA_JOptionPane.showMessageDialog(null, "Bitte fuellen Sie alle Felder aus.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.missingInputError.message"),
                            I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.missingInputError.title"),
                            JOptionPane.ERROR_MESSAGE);
                }
                else if(!new String(password_new.getPassword()).equals(new String(password_again.getPassword())))
                {
                    //_TA_JOptionPane.showMessageDialog(null, "Das neue Kennwort stimmt nicht mit dem Bestaetigungskennwort ueberein.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.passwordsDifferentError.message"),
                            I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.passwordsDifferentError.title"),
                            JOptionPane.ERROR_MESSAGE);
                    password_new.setText("");
                    password_again.setText("");
                }
                else
                {
                    try
                    {
                        if(!SessionManager.getProxy().changePassword(SessionManager.getSession().getUser(), new String(password_old.getPassword()), new String(password_new.getPassword())))
                        {
                            //_TA_JOptionPane.showMessageDialog(null, "Ihr Kennwort konnte nicht geaendert werden.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(null,
                                    I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.changePasswordError.message"),
                                    I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.changePasswordError.title"),
                                    JOptionPane.ERROR_MESSAGE);
                            password_old.setText("");
                            password_new.setText("");
                            password_again.setText("");
                            
                        }
                        else
                        {
                            //_TA_JOptionPane.showMessageDialog(null, "Ihr Kennwort wurde geaendert.", "Kennwort aendern", JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null,
                                    I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.passwordOK.message"),
                                    I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.passwordOK.title"),
                                    JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    }
                    catch(Exception exp)
                    {
                        exp.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                exp.getMessage(),
                                I18N.getString("Sirius.navigator.ui.dialog.PasswordDialog.PasswordActionListener.error.title"),
                                JOptionPane.ERROR_MESSAGE);
                        dispose();
                    }
                }
            }
            else if(e.getActionCommand().equals("btn_cancel"))
            {
                dispose();
            }
        }
    }
}
