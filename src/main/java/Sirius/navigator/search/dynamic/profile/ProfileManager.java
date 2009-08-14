package Sirius.navigator.search.dynamic.profile;

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
 * Created			:	05.09.2000
 * History			:
 *
 *******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;

import Sirius.server.newuser.*;
import Sirius.server.search.store.QueryInfo;
import Sirius.navigator.resource.*;

public abstract class ProfileManager extends JDialog implements ActionListener
{
    protected Logger logger;
    
    public final static int PROFILE = 0;
    public final static int QUERY_PROFILE = 1;
    public final static int QUERY_RESULT_PROFILE = 2;
    
    protected QueryInfo[] userGroupInfo = null;
    protected QueryInfo[] userInfo = null;
    
    protected DefaultMutableTreeNode rootNode, userNode, userGroupNode;
    protected JTree profileTree;
    protected JTextField entryField;
    protected JButton  buttonLoad, buttonSave,  buttonDelete, buttonClose;
    
    protected ProfileSaveDialog profileSaveDialog;
    
    protected int profileType = 0;
    
    public ProfileManager (JDialog dialog, int profileType)
    {
        super (dialog, true);
        
        this.logger = Logger.getLogger (this.getClass ());
        this.profileType = profileType;
        
        initProfileManager ();
    }
    
    public ProfileManager (JFrame frame, int profileType)
    {
        super (frame, true);
        
        this.logger = Logger.getLogger (this.getClass ());
        this.profileType = profileType;
        
        initProfileManager ();
    }
    
    protected void initProfileManager ()
    {
        String profileTypeName = ResourceManager.getManager ().getString ("dialog.profile.title");
        
        switch (profileType)
        {
            case 1:     profileTypeName = ResourceManager.getManager ().getString ("dialog.profile.search.title");
            break;
            
            case 2:     profileTypeName = ResourceManager.getManager ().getString ("dialog.profile.search.results.title");
            break;
        }
        
        this.profileSaveDialog = new ProfileSaveDialog (profileTypeName);
        
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        profileSaveDialog.setLocationRelativeTo (this);
        JPanel contentPane = new JPanel (new GridBagLayout ());
        contentPane.setBorder (new EmptyBorder (10,10,8,10));
        contentPane.setPreferredSize (new Dimension (400, 400));
        GridBagConstraints gbc = new GridBagConstraints ();
        
        // INFO LABEL ==========================================================
        gbc.insets = new Insets (0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        //_TA_JLabel infoLabel = new JLabel("Verwaltung der " + profileTypeName);
        JLabel infoLabel = new JLabel (ResourceManager.getManager ().getString ("dialog.profile.admin") + ' ' + profileTypeName + '.');
        infoLabel.setVerticalAlignment (JLabel.CENTER);
        infoLabel.setHorizontalAlignment (JLabel.CENTER);
        infoLabel.setBorder (new EmptyBorder (5, 5, 5, 5));
        contentPane.add (infoLabel, gbc);
        
        // PROFILE TREE ========================================================
        
        if(profileType == QUERY_RESULT_PROFILE)
        {
            userNode = new DefaultMutableTreeNode (profileTypeName);
            profileTree = new JTree (userNode, false);
        }
        else
        {
            rootNode = new DefaultMutableTreeNode (profileTypeName);
            
            rootNode.add (userGroupNode = new DefaultMutableTreeNode (ResourceManager.getManager ().getString ("dialog.profile.usergroup")));
            rootNode.add (userNode = new DefaultMutableTreeNode (ResourceManager.getManager ().getString ("dialog.profile.user")));
            
            profileTree = new JTree (rootNode, false);
        }
        
        gbc.insets = new Insets (0, 10, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.gridy++;
        gbc.weighty = 1.0;
        
        profileTree.getSelectionModel ().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
        profileTree.putClientProperty ("JTree.lineStyle", "Angled");
        profileTree.setShowsRootHandles (true);
        profileTree.setEditable (false);
        contentPane.add (new JScrollPane (profileTree), gbc);
        
        // BUTTONS =============================================================
        gbc.insets = new Insets (0, 0, 10, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridx++;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        
        buttonLoad = new JButton (ResourceManager.getManager ().getButtonText ("load"));
        buttonLoad.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("load"));
        buttonLoad.setActionCommand ("load");
        buttonLoad.addActionListener (this);
        contentPane.add (buttonLoad, gbc);
        
        gbc.gridy++;
        buttonSave = new JButton (ResourceManager.getManager ().getButtonText ("save"));
        buttonSave.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("save"));
        buttonSave.setActionCommand ("save");
        buttonSave.addActionListener (this);
        contentPane.add (buttonSave, gbc);
        
        gbc.gridy++;
        buttonDelete = new JButton (ResourceManager.getManager ().getButtonText ("delete"));
        buttonDelete.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("delete"));
        buttonDelete.setActionCommand ("delete");
        buttonDelete.addActionListener (this);
        contentPane.add (buttonDelete, gbc);
        
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy++;
        gbc.weighty = 1.0;
        
        buttonClose = new JButton (ResourceManager.getManager ().getButtonText ("close"));
        buttonClose.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("close"));
        buttonClose.setActionCommand ("close");
        buttonClose.addActionListener (this);
        contentPane.add (buttonClose, gbc);
        
        this.setContentPane (contentPane);
        this.pack ();
    }
    
    public void dispose ()
    {
        userGroupInfo = null;
        userInfo = null;
        
        if(userGroupNode != null)
            userGroupNode.removeAllChildren ();
        
        if(userNode != null)
            userNode.removeAllChildren ();
        
        System.gc ();
        super.dispose ();
    }
    
    protected void updateProfileTree ()
    {
        DefaultTreeModel treeModel = (DefaultTreeModel)profileTree.getModel ();
        
        if(userGroupInfo != null && userGroupNode != null)
        {
            userGroupNode.removeAllChildren ();
            
            for(int i = 0; i < userGroupInfo.length; i++)
                userGroupNode.add (new DefaultMutableTreeNode (userGroupInfo[i]));
            
            treeModel.nodeStructureChanged (userGroupNode);
        }
        
        if(userInfo != null && userNode != null)
        {
            userNode.removeAllChildren ();
            
            for(int i = 0; i < userInfo.length; i++)
                userNode.add (new DefaultMutableTreeNode (userInfo[i]));
            
            treeModel.nodeStructureChanged (userNode);
        }
    }
    
    public QueryInfo[] getUserInfos()
    {
        return this.userInfo;
    }
    
    public  QueryInfo[] getUserGroupInfos()
    {
        return this.userGroupInfo;
    }
    
    protected QueryInfo getSelectedInfo ()
    {
        TreePath selectedPath =  profileTree.getSelectionPath ();
        java.lang.Object userObject = null;
        QueryInfo selectedQueryInfo = null;
        
        if(selectedPath != null)
        {
            userObject = ((DefaultMutableTreeNode)selectedPath.getLastPathComponent ()).getUserObject ();
            
            if(userObject!= null && userObject instanceof QueryInfo)
                return (QueryInfo)userObject;
        }
        
        return null;
    }
    
    // #########################################################################
    
    /**
     * Profile save dialog
     */
    class ProfileSaveDialog extends JDialog implements ActionListener
    {
        protected String selectedProfileName = null;
        protected int selectedProfileID = -1;
        
        protected boolean accepted = false;
        protected boolean groupProfile = false;
        
        protected JRadioButton optionUserGroup, optionUser;
        protected JTextField entryField;
        protected JButton buttonSave, buttonCancel;
        protected String profileTypeName;
        
        public ProfileSaveDialog (String profileTypeName)
        {
            super (ProfileManager.this, profileTypeName + ' ' + ResourceManager.getManager ().getString ("dialog.profile.save.title") + '.', true);
            
            this.profileTypeName = profileTypeName;
            
            initProfileSaveDialog ();
        }
        
        protected void initProfileSaveDialog ()
        {
            this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
            JPanel contentPane = new JPanel (new GridBagLayout ());
            contentPane.setBorder (new EmptyBorder (10,10,8,10));
            GridBagConstraints gbc = new GridBagConstraints ();
            
            // INFO LABEL ======================================================
            gbc.insets = new Insets (0, 0, 10, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridheight = 1;
            gbc.gridwidth = 2;
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            //_TA_JLabel infoLabel = new JLabel("<html><p>Bitte geben Sie einen Namen ein, </p><p>unter dem Sie die "+profileTypeName+" speichern wollen.");
            JLabel infoLabel = new JLabel (ResourceManager.getManager ().getString ("dialog.profile.save.name.1") + ' ' + profileTypeName + ' ' + ResourceManager.getManager ().getString ("dialog.profile.save.name.2"));
            infoLabel.setVerticalAlignment (JLabel.CENTER);
            infoLabel.setHorizontalAlignment (JLabel.CENTER);
            infoLabel.setBorder (new EmptyBorder (5, 5, 5, 5));
            contentPane.add (infoLabel, gbc);
            
            // RADIO BUTTONS =======================================================
            ButtonGroup buttonGroup = new ButtonGroup ();
            JPanel optionsPanel = new JPanel (new GridLayout (2,1));
            optionsPanel.setBorder (new CompoundBorder (new TitledBorder (null, ResourceManager.getManager ().getString ("dialog.profile.save.message"), TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder (5,5,5,5)));
            
            
            
            optionUserGroup = new JRadioButton (ResourceManager.getManager ().getString ("dialog.profile.usergroup"));
            optionUserGroup.setActionCommand ("userGroupProfile");
            buttonGroup.add (optionUserGroup);
            optionsPanel.add (optionUserGroup);
            
            gbc.gridy++;
            optionUser = new JRadioButton (ResourceManager.getManager ().getString ("dialog.profile.user"));
            optionUser.setSelected (true);
            buttonGroup.add (optionUser);
            optionsPanel.add (optionUser);
            
            gbc.insets = new Insets (0, 10, 15, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridy++;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            contentPane.add (optionsPanel, gbc);
            
            // TEXT FIELD ======================================================
            gbc.gridy++;
            entryField = new JTextField (12);
            entryField.requestFocus ();
            contentPane.add (entryField, gbc);
            
            // BUTTONS =========================================================
            gbc.insets = new Insets (0, 0, 0, 20);
            gbc.gridwidth = 1;
            gbc.gridy++;
            buttonSave = new JButton (ResourceManager.getManager ().getButtonText ("save"));
            buttonSave.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("save"));
            buttonSave.setActionCommand ("save");
            buttonSave.addActionListener (this);
            contentPane.add (buttonSave, gbc);
            
            gbc.insets = new Insets (0, 0, 0, 0);
            gbc.gridx++;
            buttonCancel = new JButton (ResourceManager.getManager ().getButtonText ("cancel"));
            buttonCancel.setMnemonic (ResourceManager.getManager ().getButtonMnemonic ("cancel"));
            buttonCancel.setActionCommand ("cancel");
            buttonCancel.addActionListener (this);
            contentPane.add (buttonCancel, gbc);
            
            this.setContentPane (contentPane);
            this.pack ();
        }
        
        protected boolean confirmOverwrite (String entry)
        {
            String message = ResourceManager.getManager ().getString ("dialog.profile.save.overwrite.1") + "' " +  entry +  "' " + ResourceManager.getManager ().getString ("dialog.profile.save.overwrite.1");
            int result = JOptionPane.showConfirmDialog (this, message, ResourceManager.getManager ().getString ("dialog.profile.save.overwrite"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if(result == JOptionPane.YES_OPTION)
            {
                return true;
            }
            
            return false;
        }
        
        public String getSelectedProfileName ()
        {
            return selectedProfileName;
        }
        
        public int getSelectedProfileID ()
        {
            return selectedProfileID;
        }
        
        public boolean isAccepted ()
        {
            if(selectedProfileName == null)
                return false;
            else
                return accepted;
        }
        
        public boolean isGroupProfile ()
        {
            return groupProfile;
        }
        
        public void show ()
        {
            this.show (false, false);
        }
        
        public void show (boolean groupProfileEnabled, boolean groupProfileSelected)
        {
            selectedProfileID = -1;
            selectedProfileName = null;
            groupProfile = groupProfileEnabled;
            accepted = false;
            
            entryField.setText ("");
            entryField.requestFocus ();
            optionUserGroup.setEnabled (groupProfileEnabled);
            optionUserGroup.setSelected (groupProfileEnabled & groupProfileSelected);
            
            super.show ();
        }
        
        public void show (int selectedProfileID, String selectedProfileName, boolean groupProfileEnabled, boolean groupProfileSelected)
        {
            this.selectedProfileName = null;
            this.selectedProfileID = -1;
            
            groupProfile = groupProfileEnabled & groupProfileSelected;
            accepted = false;
            
            entryField.setText (selectedProfileName);
            optionUserGroup.setEnabled (groupProfileEnabled);
            optionUserGroup.setSelected (groupProfileEnabled & groupProfileSelected);
            
            super.show ();
        }
        
        // FIXME inner class
        public void actionPerformed (ActionEvent e)
        {
            if(e.getActionCommand ().equals ("save"))
            {
                //NavigatorLogger.printMessage("<ProfileManager> SAVE:" + entryField.getText());
                
                selectedProfileName = entryField.getText ();
                
                if(selectedProfileName.length () > 0)
                {
                    accepted = true;
                    groupProfile = optionUserGroup.isSelected ();
                    
                    if(groupProfile && userGroupInfo != null && userGroupInfo.length > 0)
                    {
                        for(int i = 0; i < userGroupInfo.length; i++)
                        {
                            //NavigatorLogger.printMessage("userGroupInfo[i].getName()" + userInfo[i].getName() + " selectedProfileName: " + selectedProfileName);
                            if(userGroupInfo[i].getName ().equals (selectedProfileName) && confirmOverwrite (selectedProfileName) == true)
                            {
                                accepted = true;
                                selectedProfileID = userGroupInfo[i].getID ();
                            }
                        }
                    }
                    else if(!groupProfile && userInfo != null && userInfo.length > 0)
                    {
                        for(int i = 0; i < userInfo.length; i++)
                        {
                            //NavigatorLogger.printMessage("userInfo[i].getName()" + userInfo[i].getName() + " selectedProfileName: " + selectedProfileName);
                            if(userInfo[i].getName ().equals (selectedProfileName) && confirmOverwrite (selectedProfileName) == true)
                            {
                                accepted = true;
                                selectedProfileID = userInfo[i].getID ();
                            }
                        }
                    }
                    else
                    {
                        //NavigatorLogger.printMessage("<ProfileManager> userInfo: " + userInfo);
                    }
                    
                    if(accepted)
                        this.dispose ();
                    else
                        selectedProfileName = null;
                }
                else
                {
                    JOptionPane.showMessageDialog (this, ResourceManager.getManager ().getString ("dialog.profile.save.error.message"), ResourceManager.getManager ().getString ("dialog.profile.save.error"), JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(e.getActionCommand ().equals ("cancel"))
            {
                selectedProfileName = null;
                selectedProfileID = -1;
                accepted = false;
                
                this.dispose ();
            }
        }
    }
    
}
