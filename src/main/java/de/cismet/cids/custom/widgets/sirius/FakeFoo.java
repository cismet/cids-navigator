/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.widgets.sirius;

import Sirius.navigator.ui.DashBoardWidget;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cids.custom.widgets.AbstractDashBoardWidget;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = DashBoardWidget.class)
public class FakeFoo extends AbstractDashBoardWidget {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form FakeFoo2.
     */
    public FakeFoo() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(FakeFoo.class, "FakeFoo.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(FakeFoo.class, "FakeFoo.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(FakeFoo.class, "FakeFoo.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(FakeFoo.class, "FakeFoo.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jToggleButton1.setText(org.openide.util.NbBundle.getMessage(FakeFoo.class, "FakeFoo.jToggleButton1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jToggleButton1, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                FakeFoo.class,
                "FakeFoo.jPanel1.TabConstraints.tabTitle"),
            jPanel1); // NOI18N

        jPanel2.setOpaque(false);

        final javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                242,
                Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                226,
                Short.MAX_VALUE));

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                FakeFoo.class,
                "FakeFoo.jPanel2.TabConstraints.tabTitle"),
            jPanel2); // NOI18N

        jPanel3.setOpaque(false);

        final javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                242,
                Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                226,
                Short.MAX_VALUE));

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                FakeFoo.class,
                "FakeFoo.jPanel3.TabConstraints.tabTitle"),
            jPanel3); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jTabbedPane1, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public void init() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public boolean isHeaderWidget() {
        return false;
    }

    @Override
    public int getX() {
        return 1;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getTitle() {
        return "Test Foo";
    }
}
