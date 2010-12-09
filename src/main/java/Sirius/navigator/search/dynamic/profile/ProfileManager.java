/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic.profile;

import Sirius.server.search.store.QueryInfo;

import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class ProfileManager extends JDialog implements ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final int PROFILE = 0;
    public static final int QUERY_PROFILE = 1;
    public static final int QUERY_RESULT_PROFILE = 2;

    //~ Instance fields --------------------------------------------------------

    protected Logger logger;

    protected QueryInfo[] userGroupInfo = null;
    protected QueryInfo[] userInfo = null;

    protected DefaultMutableTreeNode rootNode;
    protected DefaultMutableTreeNode userNode;
    protected DefaultMutableTreeNode userGroupNode;
    protected JTree profileTree;
    protected JTextField entryField;
    protected JButton buttonLoad;
    protected JButton buttonSave;
    protected JButton buttonDelete;
    protected JButton buttonClose;

    protected ProfileSaveDialog profileSaveDialog;

    protected int profileType = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProfileManager object.
     *
     * @param  dialog       DOCUMENT ME!
     * @param  profileType  DOCUMENT ME!
     */
    public ProfileManager(final JDialog dialog, final int profileType) {
        super(dialog, true);

        this.logger = Logger.getLogger(this.getClass());
        this.profileType = profileType;

        initProfileManager();
    }

    /**
     * Creates a new ProfileManager object.
     *
     * @param  frame        DOCUMENT ME!
     * @param  profileType  DOCUMENT ME!
     */
    public ProfileManager(final JFrame frame, final int profileType) {
        super(frame, true);

        this.logger = Logger.getLogger(this.getClass());
        this.profileType = profileType;

        initProfileManager();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initProfileManager() {
        String profileTypeName = org.openide.util.NbBundle.getMessage(
                ProfileManager.class,
                "ProfileManager.initProfileManager().profileTypeName.profile"); // NOI18N

        switch (profileType) {
            case 1: {
                profileTypeName = org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.initProfileManager().profileTypeName.search"); // NOI18N
                break;
            }

            case 2: {
                profileTypeName = org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.initProfileManager().profileTypeName.searchresult"); // NOI18N
                break;
            }
        }

        this.profileSaveDialog = new ProfileSaveDialog(profileTypeName);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        profileSaveDialog.setLocationRelativeTo(this);
        final JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 8, 10));
        contentPane.setPreferredSize(new Dimension(400, 400));
        final GridBagConstraints gbc = new GridBagConstraints();

        // INFO LABEL ==========================================================
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        // _TA_JLabel infoLabel = new JLabel("Verwaltung der " + profileTypeName);
        final JLabel infoLabel = new JLabel(
                org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.initProfileManager().infoLabel.text",
                    new Object[] { profileTypeName })); // NOI18N
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(infoLabel, gbc);

        // PROFILE TREE ========================================================

        if (profileType == QUERY_RESULT_PROFILE) {
            userNode = new DefaultMutableTreeNode(profileTypeName);
            profileTree = new JTree(userNode, false);
        } else {
            rootNode = new DefaultMutableTreeNode(profileTypeName);

            rootNode.add(userGroupNode = new DefaultMutableTreeNode(
                        org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.initProfileManager().userGroupNode.userObject"))); // NOI18N
            rootNode.add(userNode = new DefaultMutableTreeNode(
                        org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.initProfileManager().userNode.userObject")));      // NOI18N

            profileTree = new JTree(rootNode, false);
        }

        gbc.insets = new Insets(0, 10, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.gridy++;
        gbc.weighty = 1.0;

        profileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        profileTree.putClientProperty("JTree.lineStyle", "Angled"); // NOI18N
        profileTree.setShowsRootHandles(true);
        profileTree.setEditable(false);
        contentPane.add(new JScrollPane(profileTree), gbc);

        // BUTTONS =============================================================
        gbc.insets = new Insets(0, 0, 10, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.gridx++;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        buttonLoad = new JButton(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.buttonLoad.text"));           // NOI18N
        buttonLoad.setMnemonic(org.openide.util.NbBundle.getMessage(
                ProfileManager.class,
                "ProfileManager.buttonLoad.mnemonic").charAt(0)); // NOI18N
        buttonLoad.setActionCommand("load");                      // NOI18N
        buttonLoad.addActionListener(this);
        contentPane.add(buttonLoad, gbc);

        gbc.gridy++;
        buttonSave = new JButton(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.buttonSave.text"));           // NOI18N
        buttonSave.setMnemonic(org.openide.util.NbBundle.getMessage(
                ProfileManager.class,
                "ProfileManager.buttonSave.mnemonic").charAt(0)); // NOI18N
        buttonSave.setActionCommand("save");                      // NOI18N
        buttonSave.addActionListener(this);
        contentPane.add(buttonSave, gbc);

        gbc.gridy++;
        buttonDelete = new JButton(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.buttonDelete.text"));           // NOI18N
        buttonDelete.setMnemonic(org.openide.util.NbBundle.getMessage(
                ProfileManager.class,
                "ProfileManager.buttonDelete.mnemonic").charAt(0)); // NOI18N
        buttonDelete.setActionCommand("delete");                    // NOI18N
        buttonDelete.addActionListener(this);
        contentPane.add(buttonDelete, gbc);

        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy++;
        gbc.weighty = 1.0;

        buttonClose = new JButton(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.buttonClose.text"));           // NOI18N
        buttonClose.setMnemonic(org.openide.util.NbBundle.getMessage(
                ProfileManager.class,
                "ProfileManager.buttonClose.mnemonic").charAt(0)); // NOI18N
        buttonClose.setActionCommand("close");                     // NOI18N
        buttonClose.addActionListener(this);
        contentPane.add(buttonClose, gbc);

        this.setContentPane(contentPane);
        this.pack();
    }

    @Override
    public void dispose() {
        userGroupInfo = null;
        userInfo = null;

        if (userGroupNode != null) {
            userGroupNode.removeAllChildren();
        }

        if (userNode != null) {
            userNode.removeAllChildren();
        }

        System.gc();
        super.dispose();
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateProfileTree() {
        final DefaultTreeModel treeModel = (DefaultTreeModel)profileTree.getModel();

        if ((userGroupInfo != null) && (userGroupNode != null)) {
            userGroupNode.removeAllChildren();

            for (int i = 0; i < userGroupInfo.length; i++) {
                userGroupNode.add(new DefaultMutableTreeNode(userGroupInfo[i]));
            }

            treeModel.nodeStructureChanged(userGroupNode);
        }

        if ((userInfo != null) && (userNode != null)) {
            userNode.removeAllChildren();

            for (int i = 0; i < userInfo.length; i++) {
                userNode.add(new DefaultMutableTreeNode(userInfo[i]));
            }

            treeModel.nodeStructureChanged(userNode);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QueryInfo[] getUserInfos() {
        return this.userInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QueryInfo[] getUserGroupInfos() {
        return this.userGroupInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected QueryInfo getSelectedInfo() {
        final TreePath selectedPath = profileTree.getSelectionPath();
        java.lang.Object userObject = null;
        final QueryInfo selectedQueryInfo = null;

        if (selectedPath != null) {
            userObject = ((DefaultMutableTreeNode)selectedPath.getLastPathComponent()).getUserObject();

            if ((userObject != null) && (userObject instanceof QueryInfo)) {
                return (QueryInfo)userObject;
            }
        }

        return null;
    }

    // #########################################################################

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Profile save dialog.
     *
     * @version  $Revision$, $Date$
     */
    class ProfileSaveDialog extends JDialog implements ActionListener {

        //~ Instance fields ----------------------------------------------------

        protected String selectedProfileName = null;
        protected int selectedProfileID = -1;

        protected boolean accepted = false;
        protected boolean groupProfile = false;

        protected JRadioButton optionUserGroup;
        protected JRadioButton optionUser;
        protected JTextField entryField;
        protected JButton buttonSave;
        protected JButton buttonCancel;
        protected String profileTypeName;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProfileSaveDialog object.
         *
         * @param  profileTypeName  DOCUMENT ME!
         */
        public ProfileSaveDialog(final String profileTypeName) {
            super(
                ProfileManager.this,
                profileTypeName
                        + ' '
                        + org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.ProfileSaveDialog.title"),
                true); // NOI18N

            this.profileTypeName = profileTypeName;

            initProfileSaveDialog();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        protected void initProfileSaveDialog() {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            final JPanel contentPane = new JPanel(new GridBagLayout());
            contentPane.setBorder(new EmptyBorder(10, 10, 8, 10));
            final GridBagConstraints gbc = new GridBagConstraints();

            // INFO LABEL ======================================================
            gbc.insets = new Insets(0, 0, 10, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridheight = 1;
            gbc.gridwidth = 2;
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            // _TA_JLabel infoLabel = new JLabel("<html><p>Bitte geben Sie einen Namen ein, </p><p>unter dem Sie die
            // "+profileTypeName+" speichern wollen.");
            final JLabel infoLabel = new JLabel(
                    org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.initProfileSaveDialog().infoLabel.text",
                        new Object[] { profileTypeName })); // NOI18N
            infoLabel.setVerticalAlignment(JLabel.CENTER);
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPane.add(infoLabel, gbc);

            // RADIO BUTTONS =======================================================
            final ButtonGroup buttonGroup = new ButtonGroup();
            final JPanel optionsPanel = new JPanel(new GridLayout(2, 1));
            optionsPanel.setBorder(new CompoundBorder(
                    new TitledBorder(
                        null,
                        org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.ProfileSaveDialog.initProfileSaveDialog().optionsPanel.border.title"), // NOI18N
                        TitledBorder.LEFT,
                        TitledBorder.TOP),
                    new EmptyBorder(5, 5, 5, 5)));

            optionUserGroup = new JRadioButton(
                    org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.initProfileSaveDialog().optionUserGroup.text")); // NOI18N
            optionUserGroup.setActionCommand("userGroupProfile");                                          // NOI18N
            buttonGroup.add(optionUserGroup);
            optionsPanel.add(optionUserGroup);

            gbc.gridy++;
            optionUser = new JRadioButton(
                    org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.initProfileSaveDialog().optionUser.text")); // NOI18N
            optionUser.setSelected(true);
            buttonGroup.add(optionUser);
            optionsPanel.add(optionUser);

            gbc.insets = new Insets(0, 10, 15, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridy++;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            contentPane.add(optionsPanel, gbc);

            // TEXT FIELD ======================================================
            gbc.gridy++;
            entryField = new JTextField(12);
            entryField.requestFocus();
            contentPane.add(entryField, gbc);

            // BUTTONS =========================================================
            gbc.insets = new Insets(0, 0, 0, 20);
            gbc.gridwidth = 1;
            gbc.gridy++;
            buttonSave = new JButton(org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.buttonSave.text"));           // NOI18N
            buttonSave.setMnemonic(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.ProfileSaveDialog.buttonSave.mnemonic").charAt(0)); // NOI18N
            buttonSave.setActionCommand("save");                                        // NOI18N
            buttonSave.addActionListener(this);
            contentPane.add(buttonSave, gbc);

            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridx++;
            buttonCancel = new JButton(org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.buttonCancel.text"));           // NOI18N
            buttonCancel.setMnemonic(org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.ProfileSaveDialog.buttonCancel.mnemonic").charAt(0)); // NOI18N
            buttonCancel.setActionCommand("cancel");                                      // NOI18N
            buttonCancel.addActionListener(this);
            contentPane.add(buttonCancel, gbc);

            this.setContentPane(contentPane);
            this.pack();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   entry  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected boolean confirmOverwrite(final String entry) {
            final String message = org.openide.util.NbBundle.getMessage(
                    ProfileManager.class,
                    "ProfileManager.ProfileSaveDialog.confirmOverwrite().confirmOptionPane.message",
                    new Object[] { entry });                                                            // NOI18N
            final int result = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    org.openide.util.NbBundle.getMessage(
                        ProfileManager.class,
                        "ProfileManager.ProfileSaveDialog.confirmOverwrite().confirmOptionPane.title"), // NOI18N
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                return true;
            }

            return false;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getSelectedProfileName() {
            return selectedProfileName;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getSelectedProfileID() {
            return selectedProfileID;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isAccepted() {
            if (selectedProfileName == null) {
                return false;
            } else {
                return accepted;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isGroupProfile() {
            return groupProfile;
        }

        @Override
        public void show() {
            this.show(false, false);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  groupProfileEnabled   DOCUMENT ME!
         * @param  groupProfileSelected  DOCUMENT ME!
         */
        public void show(final boolean groupProfileEnabled, final boolean groupProfileSelected) {
            selectedProfileID = -1;
            selectedProfileName = null;
            groupProfile = groupProfileEnabled;
            accepted = false;

            entryField.setText(""); // NOI18N
            entryField.requestFocus();
            optionUserGroup.setEnabled(groupProfileEnabled);
            optionUserGroup.setSelected(groupProfileEnabled & groupProfileSelected);

            super.show();
        }

        /**
         * DOCUMENT ME!
         *
         * @param  selectedProfileID     DOCUMENT ME!
         * @param  selectedProfileName   DOCUMENT ME!
         * @param  groupProfileEnabled   DOCUMENT ME!
         * @param  groupProfileSelected  DOCUMENT ME!
         */
        public void show(final int selectedProfileID,
                final String selectedProfileName,
                final boolean groupProfileEnabled,
                final boolean groupProfileSelected) {
            this.selectedProfileName = null;
            this.selectedProfileID = -1;

            groupProfile = groupProfileEnabled & groupProfileSelected;
            accepted = false;

            entryField.setText(selectedProfileName);
            optionUserGroup.setEnabled(groupProfileEnabled);
            optionUserGroup.setSelected(groupProfileEnabled & groupProfileSelected);

            super.show();
        }

        // FIXME inner class
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("save")) { // NOI18N
                selectedProfileName = entryField.getText();

                if (selectedProfileName.length() > 0) {
                    accepted = true;
                    groupProfile = optionUserGroup.isSelected();

                    if (groupProfile && (userGroupInfo != null) && (userGroupInfo.length > 0)) {
                        for (int i = 0; i < userGroupInfo.length; i++) {
                            if (userGroupInfo[i].getName().equals(selectedProfileName)
                                        && (confirmOverwrite(selectedProfileName) == true)) {
                                accepted = true;
                                selectedProfileID = userGroupInfo[i].getID();
                            }
                        }
                    } else if (!groupProfile && (userInfo != null) && (userInfo.length > 0)) {
                        for (int i = 0; i < userInfo.length; i++) {
                            if (userInfo[i].getName().equals(selectedProfileName)
                                        && (confirmOverwrite(selectedProfileName) == true)) {
                                accepted = true;
                                selectedProfileID = userInfo[i].getID();
                            }
                        }
                    }

                    if (accepted) {
                        this.dispose();
                    } else {
                        selectedProfileName = null;
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.ProfileSaveDialog.actionPerformed().saveErrorOptionPane.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            ProfileManager.class,
                            "ProfileManager.ProfileSaveDialog.actionPerformed().saveErrorOptionPane.title"), // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                }
            } else if (e.getActionCommand().equals("cancel")) {                                              // NOI18N
                selectedProfileName = null;
                selectedProfileID = -1;
                accepted = false;

                this.dispose();
            }
        }
    }
}
