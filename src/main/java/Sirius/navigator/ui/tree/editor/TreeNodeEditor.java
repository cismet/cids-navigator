/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MetaClassChooser.java
 *
 * Created on 30. August 2004, 11:29
 */
package Sirius.navigator.ui.tree.editor;

import Sirius.navigator.connection.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.*;

import Sirius.server.middleware.types.*;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserGroup;
import Sirius.server.newuser.permission.*;

import org.apache.log4j.Logger;

import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * Dialog zum Ausw\u00E4hlen einer Klasse.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class TreeNodeEditor extends javax.swing.JDialog implements ConnectionContextProvider {

    //~ Instance fields --------------------------------------------------------

    private DefaultMetaTreeNode metaTreeNode = null;
    private Logger logger;

    private final ClientConnectionContext connectionContext = ClientConnectionContext.create(getClass()
                    .getSimpleName());

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox classBox;
    private javax.swing.JLabel classLabel;
    private javax.swing.JRadioButton classNodeRadioButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JRadioButton objectNodeRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton pureNodeRadioButton;
    private javax.swing.ButtonGroup typeButtonGroup;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form MetaClassChooser.
     *
     * @param  parent  DOCUMENT ME!
     * @param  modal   DOCUMENT ME!
     */
    public TreeNodeEditor(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);

        this.logger = Logger.getLogger(this.getClass());

        initComponents();
        getRootPane().setDefaultButton(okButton);
        final ActionListener actionListener = new ButtonListener();
        this.okButton.addActionListener(actionListener);
        this.cancelButton.addActionListener(actionListener);
        this.pureNodeRadioButton.addActionListener(actionListener);
        this.classNodeRadioButton.addActionListener(actionListener);
        this.objectNodeRadioButton.addActionListener(actionListener);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
        final javax.swing.JList classList = new javax.swing.JList();
        typeButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel infoLabel = new javax.swing.JLabel();
        final javax.swing.JPanel editorPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        typeLabel = new javax.swing.JLabel();
        pureNodeRadioButton = new javax.swing.JRadioButton();
        classNodeRadioButton = new javax.swing.JRadioButton();
        objectNodeRadioButton = new javax.swing.JRadioButton();
        classLabel = new javax.swing.JLabel();
        classBox = new javax.swing.JComboBox();
        final javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        classList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(classList);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(TreeNodeEditor.class, "TreeNodeEditor.title")); // NOI18N

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText(org.openide.util.NbBundle.getMessage(TreeNodeEditor.class, "TreeNodeEditor.infoLabel.text")); // NOI18N
        infoLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));
        getContentPane().add(infoLabel, java.awt.BorderLayout.NORTH);

        editorPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10),
                javax.swing.BorderFactory.createEtchedBorder()));
        editorPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setText(org.openide.util.NbBundle.getMessage(TreeNodeEditor.class, "TreeNodeEditor.nameLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(25, 25, 5, 10);
        editorPanel.add(nameLabel, gridBagConstraints);

        nameField.setMinimumSize(new java.awt.Dimension(200, 20));
        nameField.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 5, 25);
        editorPanel.add(nameField, gridBagConstraints);

        typeLabel.setText(org.openide.util.NbBundle.getMessage(TreeNodeEditor.class, "TreeNodeEditor.typeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 10);
        editorPanel.add(typeLabel, gridBagConstraints);

        typeButtonGroup.add(pureNodeRadioButton);
        pureNodeRadioButton.setText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.pureNodeRadioButton.text")); // NOI18N
        pureNodeRadioButton.setActionCommand("pure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 25);
        editorPanel.add(pureNodeRadioButton, gridBagConstraints);

        typeButtonGroup.add(classNodeRadioButton);
        classNodeRadioButton.setText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.classNodeRadioButton.text")); // NOI18N
        classNodeRadioButton.setActionCommand("class");
        classNodeRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 25);
        editorPanel.add(classNodeRadioButton, gridBagConstraints);

        typeButtonGroup.add(objectNodeRadioButton);
        objectNodeRadioButton.setSelected(true);
        objectNodeRadioButton.setText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.objectNodeRadioButton.text")); // NOI18N
        objectNodeRadioButton.setActionCommand("object");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 25);
        editorPanel.add(objectNodeRadioButton, gridBagConstraints);

        classLabel.setText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.classLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 25, 10);
        editorPanel.add(classLabel, gridBagConstraints);

        classBox.setEnabled(false);
        classBox.setMinimumSize(new java.awt.Dimension(200, 19));
        classBox.setPreferredSize(new java.awt.Dimension(200, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 25, 25);
        editorPanel.add(classBox, gridBagConstraints);

        getContentPane().add(editorPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 10, 10));
        buttonPanel.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        okButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.okButton.mnemonic").charAt(0));
        okButton.setText(org.openide.util.NbBundle.getMessage(TreeNodeEditor.class, "TreeNodeEditor.okButton.text")); // NOI18N
        okButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.okButton.tooltip"));                                                                  // NOI18N
        okButton.setActionCommand("ok");
        buttonPanel.add(okButton);

        cancelButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.cancelButton.mnemonic").charAt(0));
        cancelButton.setText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.cancelButton.text"));    // NOI18N
        cancelButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                TreeNodeEditor.class,
                "TreeNodeEditor.cancelButton.tooltip")); // NOI18N
        cancelButton.setActionCommand("cancel");
        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  filtered   DOCUMENT ME!
     * @param  cs         DOCUMENT ME!
     * @param  userGroup  DOCUMENT ME!
     */
    private void addToFiltered(final ArrayList filtered, final MetaClass cs, final UserGroup userGroup) {
        try {
            final String key = userGroup.getKey().toString();
            if (cs.getPermissions().hasPermission(key, PermissionHolder.WRITEPERMISSION)) {
                filtered.add(cs);
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("filter for " + cs); // NOI18N
            }
        }
    }

    @Override
    public void show() {
        if (this.classBox.getModel().getSize() == 0) {
            try {
                final MetaClass[] cs = SessionManager.getProxy().getClasses(getConnectionContext());
                final ArrayList filtered = new ArrayList();

                final User user = SessionManager.getSession().getUser();
                final UserGroup userGroup = user.getUserGroup();

                // Permission perm = SessionManager.getSession().getWritePermission();

                // filtering
                for (int i = 0; i < cs.length; i++) {
                    if (userGroup != null) {
                        addToFiltered(filtered, cs[i], userGroup);
                    } else {
                        for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                            addToFiltered(filtered, cs[i], potentialUserGroup);
                        }
                    }
                }

                final MetaClass[] csFiltered = (MetaClass[])filtered.toArray(new MetaClass[filtered.size()]);

                this.classBox.setModel(new DefaultComboBoxModel(csFiltered));
                if (this.classBox.getModel().getSize() > 0) {
                    this.classBox.setSelectedIndex(0);
                }
            } catch (ConnectionException cexp) {
                logger.error("could not load class nodes", cexp); // NOI18N
            }

            this.pack();
        }

        // NOTE: This call can not be substituted by StaticSwingTools.showDialog(this) because
        // show() method overwrites JDialog.show(). StaticSwingTools.showDialog() calls
        // setVisible(true) which internally calls JDialog show() -> endless recursion if
        // StaticSwingTools.showDialog() is called here
        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultMetaTreeNode createTreeNode() {
        this.metaTreeNode = null;
        this.pureNodeRadioButton.setEnabled(true);
        this.objectNodeRadioButton.setSelected(true);
        this.objectNodeRadioButton.setEnabled(true);
        this.classBox.setEnabled(true);

        StaticSwingTools.showDialog(this);
        getRootPane().setDefaultButton(okButton);
        return this.metaTreeNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaTreeNode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultMetaTreeNode editTreeNode(final DefaultMetaTreeNode metaTreeNode) {
        this.metaTreeNode = metaTreeNode;

        this.nameField.setText(metaTreeNode.toString());
        this.pureNodeRadioButton.setEnabled(false);
        this.classNodeRadioButton.setEnabled(false);
        this.objectNodeRadioButton.setEnabled(false);
        this.classBox.setEnabled(false);

        StaticSwingTools.showDialog(this);
        getRootPane().setDefaultButton(okButton);
        return this.metaTreeNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultMetaTreeNode getMetaTreeNode() {
        return this.metaTreeNode;
    }

    @Override
    public final ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class ButtonListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("ok")) { // NOI18N
                if (TreeNodeEditor.this.nameField.getText().length() > 0) {
                    // create
                    if (metaTreeNode == null) {
                        // new pure node
                        if (TreeNodeEditor.this.pureNodeRadioButton.isSelected()) {
                            final MetaNode metaNode = new MetaNode(
                                    -1,
                                    SessionManager.getSession().getUser().getDomain(),
                                    TreeNodeEditor.this.nameField.getText(),
                                    null,
                                    true,
                                    Policy.createWIKIPolicy(),
                                    -1,
                                    null,
                                    false,
                                    -1);
                            TreeNodeEditor.this.metaTreeNode = new PureTreeNode(metaNode, getConnectionContext());

                            TreeNodeEditor.this.dispose();
                        } else if (TreeNodeEditor.this.classNodeRadioButton.isSelected()) {
                            // XXX
                        } else if (TreeNodeEditor.this.objectNodeRadioButton.isSelected()) {
                            if (TreeNodeEditor.this.classBox.getSelectedIndex() >= 0) {
                                try {
                                    final MetaClass metaClass = (MetaClass)TreeNodeEditor.this.classBox
                                                .getSelectedItem();

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("actionPerformed(): creating new meta object node of type "
                                                    + metaClass); // NOI18N
                                    }

                                    ComponentRegistry.getRegistry()
                                            .getMainWindow()
                                            .setCursor(java.awt.Cursor.getPredefinedCursor(
                                                    java.awt.Cursor.WAIT_CURSOR));
                                    final MetaObject metaObject = SessionManager.getProxy()
                                                .getInstance(metaClass, getConnectionContext());
                                    ComponentRegistry.getRegistry()
                                            .getMainWindow()
                                            .setCursor(java.awt.Cursor.getPredefinedCursor(
                                                    java.awt.Cursor.DEFAULT_CURSOR));

                                    final MetaObjectNode MetaObjectNode = new MetaObjectNode(
                                            -1,
                                            SessionManager.getSession().getUser().getDomain(),
                                            metaObject,
                                            TreeNodeEditor.this.nameField.getText(),
                                            null,
                                            true,
                                            Policy.createWIKIPolicy(),
                                            -1,
                                            null,
                                            false);
                                    TreeNodeEditor.this.metaTreeNode = new ObjectTreeNode(
                                            MetaObjectNode,
                                            getConnectionContext());

                                    TreeNodeEditor.this.dispose();
                                } catch (Throwable t) {
                                    logger.error("actionPerformed(): could not create new empty meta object", t); // NOI18N
                                    ComponentRegistry.getRegistry()
                                            .getMainWindow()
                                            .setCursor(java.awt.Cursor.getPredefinedCursor(
                                                    java.awt.Cursor.DEFAULT_CURSOR));

                                    JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                                        org.openide.util.NbBundle.getMessage(
                                            TreeNodeEditor.class,
                                            "TreeNodeEditor.ButtonListener.actionPerformed().createMetaObjectError.message",
                                            new Object[] {
                                                TreeNodeEditor.this.classBox.getSelectedItem(),
                                                t.getMessage()
                                            }),                                                                             // NOI18N
                                        org.openide.util.NbBundle.getMessage(
                                            TreeNodeEditor.class,
                                            "TreeNodeEditor.ButtonListener.actionPerformed().createMetaObjectError.title"), // NOI18N
                                        JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                logger.warn("actionPerformed() no class");                                                  // NOI18N
                                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                                    org.openide.util.NbBundle.getMessage(
                                        TreeNodeEditor.class,
                                        "TreeNodeEditor.ButtonListener.actionPerformed().noClassInfo.message"),             // NOI18N
                                    org.openide.util.NbBundle.getMessage(
                                        TreeNodeEditor.class,
                                        "TreeNodeEditor.ButtonListener.actionPerformed().noClassInfo.title"),               // NOI18N
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else {
                        metaTreeNode.getNode().setName(nameField.getText());
                        metaTreeNode.setChanged(true);

                        TreeNodeEditor.this.dispose();
                    }
                } else {
                    logger.warn("actionPerformed() no name");                                      // NOI18N
                    JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                        org.openide.util.NbBundle.getMessage(
                            TreeNodeEditor.class,
                            "TreeNodeEditor.ButtonListener.actionPerformed().noNameInfo.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            TreeNodeEditor.class,
                            "TreeNodeEditor.ButtonListener.actionPerformed().noNameInfo.title"),   // NOI18N
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (e.getActionCommand().equals("cancel")) {                                    // NOI18N
                TreeNodeEditor.this.metaTreeNode = null;
                TreeNodeEditor.this.dispose();
            } else if (e.getActionCommand().equals("pure")) {                                      // NOI18N
                TreeNodeEditor.this.classBox.setEnabled(false);
            } else if (e.getActionCommand().equals("class")) {                                     // NOI18N
                TreeNodeEditor.this.classBox.setEnabled(true);
            } else if (e.getActionCommand().equals("object")) {                                    // NOI18N
                TreeNodeEditor.this.classBox.setEnabled(true);
            }
        }
    }
}
