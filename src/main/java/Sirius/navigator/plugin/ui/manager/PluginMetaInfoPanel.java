/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginMetaInfoPanel.java
 *
 * Created on 11. Mai 2003, 10:52
 */
package Sirius.navigator.plugin.ui.manager;

import Sirius.navigator.plugin.*;
import Sirius.navigator.resource.*;

import java.awt.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   Peter Alzheimer
 * @version  $Revision$, $Date$
 */
public class PluginMetaInfoPanel extends javax.swing.JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authorField;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JTextField companyField;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JTextField contactField;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JTextField copyrightField;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextField homepageField;
    private javax.swing.JLabel homepageLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField versionField;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form PluginMetaInfoPanel.
     */
    public PluginMetaInfoPanel() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  metaInfo  DOCUMENT ME!
     */
    public void setPluginDescription(final PluginMetaInfo metaInfo) {
        titleLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.titleLabel.defaulttext")); // NOI18N

        nameField.setText(metaInfo.getName());
        nameField.setEnabled(true);
        nameLabel.setEnabled(true);
        versionField.setText(metaInfo.getVersion());
        versionField.setEnabled(true);
        versionLabel.setEnabled(true);
        authorField.setText(metaInfo.getAuthor());
        authorField.setEnabled(true);
        authorLabel.setEnabled(true);
        copyrightField.setText(metaInfo.getCopyright());
        copyrightField.setEnabled(true);
        copyrightLabel.setEnabled(true);
        companyField.setText(metaInfo.getCompany());
        companyField.setEnabled(true);
        companyLabel.setEnabled(true);
        contactField.setText(metaInfo.getContact());
        contactField.setEnabled(true);
        contactLabel.setEnabled(true);
        homepageField.setText(metaInfo.getHomepage());
        homepageField.setEnabled(true);
        homepageLabel.setEnabled(true);

        // System.out.println(metaInfo.getDescription().trim());
        descriptionArea.setText(metaInfo.getDescription().trim());
        descriptionArea.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  methodDescriptor  DOCUMENT ME!
     */
    public void setMethodDescription(final PluginMethodDescriptor methodDescriptor) {
        this.clear();
        titleLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.setMethodDescription(PluginMethodDescriptor).titleLabel.text")); // NOI18N

        nameField.setText(methodDescriptor.getName());
        nameField.setEnabled(true);
        nameLabel.setEnabled(true);

        descriptionArea.setText(methodDescriptor.getDescription().trim());
        descriptionArea.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     */
    protected void clear() {
        titleLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.titleLabel.defaulttext")); // NOI18N

        nameField.setText(null);
        nameField.setEnabled(false);
        nameLabel.setEnabled(false);
        versionField.setText(null);
        versionField.setEnabled(false);
        versionLabel.setEnabled(false);
        authorField.setText(null);
        authorField.setEnabled(false);
        authorLabel.setEnabled(false);
        copyrightField.setText(null);
        copyrightField.setEnabled(false);
        copyrightLabel.setEnabled(false);
        companyField.setText(null);
        companyField.setEnabled(false);
        companyLabel.setEnabled(false);
        contactField.setText(null);
        contactField.setEnabled(false);
        contactLabel.setEnabled(false);
        homepageField.setText(null);
        homepageField.setEnabled(false);
        homepageLabel.setEnabled(false);

        descriptionArea.setText(null);
        descriptionArea.setEnabled(false);
    }

    /*public static void main(String args[])
     * { PluginMetaInfoPanel pmip = new  PluginMetaInfoPanel(); JFrame jf = new JFrame("PluginMetaInfo");
     * jf.getContentPane().setLayout(new GridLayout(1,1)); jf.getContentPane().add(pmip); //jf.setSize(500,600);
     * jf.pack(); jf.setVisible(true);}*/

    // #########################################################################

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titleLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        companyLabel = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        homepageLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        versionField = new javax.swing.JTextField();
        authorField = new javax.swing.JTextField();
        copyrightField = new javax.swing.JTextField();
        companyField = new javax.swing.JTextField();
        contactField = new javax.swing.JTextField();
        homepageField = new javax.swing.JTextField();
        descriptionPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        titleLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.titleLabel.defaulttext")); // NOI18N
        titleLabel.setAlignmentX(0.5F);
        titleLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(titleLabel, gridBagConstraints);

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nameLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.nameLabel.text")); // NOI18N
        nameLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(nameLabel, gridBagConstraints);

        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        versionLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.versionLabel.text")); // NOI18N
        versionLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(versionLabel, gridBagConstraints);

        authorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        authorLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.authorLabel.text")); // NOI18N
        authorLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(authorLabel, gridBagConstraints);

        copyrightLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        copyrightLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.copyrightLabel.text")); // NOI18N
        copyrightLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(copyrightLabel, gridBagConstraints);

        companyLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        companyLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.companyLabel.text")); // NOI18N
        companyLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(companyLabel, gridBagConstraints);

        contactLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        contactLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.contactLabel.text")); // NOI18N
        contactLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(contactLabel, gridBagConstraints);

        homepageLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        homepageLabel.setText(org.openide.util.NbBundle.getMessage(
                PluginMetaInfoPanel.class,
                "PluginMetaInfoPanel.homepageLabel.text")); // NOI18N
        homepageLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(homepageLabel, gridBagConstraints);

        nameField.setEnabled(false);
        nameField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(nameField, gridBagConstraints);

        versionField.setEnabled(false);
        versionField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(versionField, gridBagConstraints);

        authorField.setEnabled(false);
        authorField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(authorField, gridBagConstraints);

        copyrightField.setEnabled(false);
        copyrightField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(copyrightField, gridBagConstraints);

        companyField.setEnabled(false);
        companyField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(companyField, gridBagConstraints);

        contactField.setEnabled(false);
        contactField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(contactField, gridBagConstraints);

        homepageField.setEnabled(false);
        homepageField.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        add(homepageField, gridBagConstraints);

        descriptionPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    org.openide.util.NbBundle.getMessage(
                        PluginMetaInfoPanel.class,
                        "PluginMetaInfoPanel.descriptionPanel.title")),
                javax.swing.BorderFactory.createEmptyBorder(1, 2, 2, 2))); // NOI18N
        descriptionPanel.setPreferredSize(new java.awt.Dimension(450, 125));
        descriptionPanel.setLayout(new java.awt.GridLayout(1, 1));

        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMargin(new java.awt.Insets(2, 2, 2, 2));
        scrollPane.setViewportView(descriptionArea);

        descriptionPanel.add(scrollPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(descriptionPanel, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents
}
