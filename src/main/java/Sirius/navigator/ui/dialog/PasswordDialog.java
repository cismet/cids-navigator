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


public class PasswordDialog extends JDialog
{
    private JButton btn_change, btn_cancel;
    private JPasswordField password_old, password_new, password_again;
    private static final ResourceManager resources = ResourceManager.getManager();

    public PasswordDialog(JFrame parent)
    {
        //TA_super(navigator, "Passwort aendern", true);
        super(parent, org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.title") , true);//NOI18N
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
        JLabel passwordIcon = new JLabel(resources.getIcon("password_icon.gif"));//NOI18N
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
        contentPane.add(new JLabel(
                org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.initPasswordDialog().contentPane.oldPWLabel.text")), constraints);//NOI18N
        
        //constraints.gridheight = 1;
        //constraints.gridwidth = 1;
        //constraints.weightx = 0.0;
        //constraints.weighty = 0.0;
        constraints.gridy++;
        //constraints.gridx;
        //_TA_contentPane.add(new JLabel("Neues Passwort:"), constraints);
        contentPane.add(new JLabel(
                org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.initPasswordDialog().contentPane.newPWLabel.text")), constraints);//NOI18N
        
        constraints.gridy++;
        //_TA_contentPane.add(new JLabel("Bestaetigung:"), constraints);
        contentPane.add(new JLabel(
                org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.initPasswordDialog().contentPane.repeatPWLabel.text")), constraints);//NOI18N
        
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
        btn_change = new JButton(org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.btn_change.text"));//NOI18N
        //_TA_btn_change.setMnemonic('A');
        btn_change.setMnemonic(org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.btn_change.mnemonic").charAt(0));//NOI18N
        btn_change.setActionCommand("btn_change");//NOI18N
        btn_change.addActionListener(passwordActionListener);
        contentPane.add(btn_change, constraints);
        
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx++;
        //_TA_btn_cancel = new JButton("Abbrechen");
        btn_cancel = new JButton(org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.btn_cancel.text"));//NOI18N
        //_TA_btn_cancel.setMnemonic('b');
        btn_cancel.setMnemonic(org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.btn_cancel.mnemonic").charAt(0));//NOI18N
        btn_cancel.setActionCommand("btn_cancel");//NOI18N
        btn_cancel.addActionListener(passwordActionListener);
        contentPane.add(btn_cancel, constraints);
        
        this.setContentPane(contentPane);
        this.pack();
        
        
    }
    
    class PasswordActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("btn_change"))//NOI18N
            {
                if(password_old.getPassword().length < 1 || password_new.getPassword().length < 1 || password_new.getPassword().length < 1)
                {
                    //_TA_JOptionPane.showMessageDialog(null, "Bitte fuellen Sie alle Felder aus.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.missingInputError.message"),//NOI18N
                            org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.missingInputError.title"),//NOI18N
                            JOptionPane.ERROR_MESSAGE);
                }
                else if(!new String(password_new.getPassword()).equals(new String(password_again.getPassword())))
                {
                    //_TA_JOptionPane.showMessageDialog(null, "Das neue Kennwort stimmt nicht mit dem Bestaetigungskennwort ueberein.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.passwordsDifferentError.message"),//NOI18N
                            org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.passwordsDifferentError.title"),//NOI18N
                            JOptionPane.ERROR_MESSAGE);
                    password_new.setText("");//NOI18N
                    password_again.setText("");//NOI18N
                }
                else
                {
                    try
                    {
                        if(!SessionManager.getProxy().changePassword(SessionManager.getSession().getUser(), new String(password_old.getPassword()), new String(password_new.getPassword())))
                        {
                            //_TA_JOptionPane.showMessageDialog(null, "Ihr Kennwort konnte nicht geaendert werden.", "Kennwort aendern", JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(null,
                                    org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.changePasswordError.message"),//NOI18N
                                    org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.changePasswordError.title"),//NOI18N
                                    JOptionPane.ERROR_MESSAGE);
                            password_old.setText("");//NOI18N
                            password_new.setText("");//NOI18N
                            password_again.setText("");//NOI18N
                            
                        }
                        else
                        {
                            //_TA_JOptionPane.showMessageDialog(null, "Ihr Kennwort wurde geaendert.", "Kennwort aendern", JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null,
                                    org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.passwordOK.message"),//NOI18N
                                    org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.passwordOK.title"),//NOI18N
                                    JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    }
                    catch(Exception exp)
                    {
                        exp.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                exp.getMessage(),
                                org.openide.util.NbBundle.getMessage(PasswordDialog.class, "PasswordDialog.PasswordActionListener.error.title"),//NOI18N
                                JOptionPane.ERROR_MESSAGE);
                        dispose();
                    }
                }
            }
            else if(e.getActionCommand().equals("btn_cancel"))//NOI18N
            {
                dispose();
            }
        }
    }
}
