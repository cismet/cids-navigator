/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * IconCheckBox.java
 *
 * Created on 12. M\u00E4rz 2004, 14:33
 */
package Sirius.navigator.ui.widget;

import java.awt.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class IconCheckBox extends javax.swing.JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBox;
    private javax.swing.JLabel iconLabel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form IconCheckBox.
     */
    public IconCheckBox() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSelected() {
        return checkBox.isSelected();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  b  DOCUMENT ME!
     */
    public void setSelected(final boolean b) {
        checkBox.setSelected(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getText() {
        return checkBox.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     */
    public void setText(final String s) {
        checkBox.setText(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  i  DOCUMENT ME!
     */
    public void setIcon(final Icon i) {
        iconLabel.setIcon(i);
    }

    @Override
    public void setFont(final Font font) {
        super.setFont(font);

        if ((iconLabel != null) && (checkBox != null) && (font != null)) {
            iconLabel.setFont(font);
            checkBox.setFont(font);
        }
    }

    /*public void setBackground(Color color)
     * { super.setBackground(color);  if(iconLabel != null && checkBox != null && color != null) {
     * iconLabel.setBackground(color);     checkBox.setBackground(color); } }
     *
     * public void setForeground(Color color) { super.setForeground(color);  if(iconLabel != null && checkBox != null &&
     * color != null) {     iconLabel.setForeground(color);     checkBox.setForeground(color); }}*/

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        iconLabel.setEnabled(enabled);
        checkBox.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    private void initComponents() //GEN-BEGIN:initComponents
    {
        java.awt.GridBagConstraints gridBagConstraints;

        iconLabel = new javax.swing.JLabel();
        checkBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(21, 100));
        setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(iconLabel, gridBagConstraints);

        checkBox.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(checkBox, gridBagConstraints);
    } //GEN-END:initComponents
}
