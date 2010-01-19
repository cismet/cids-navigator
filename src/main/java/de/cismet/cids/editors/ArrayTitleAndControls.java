/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ArrayTitleAndControls.java
 *
 * Created on 25.02.2009, 12:19:03
 */
package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;
import de.cismet.cids.dynamics.CidsBean;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JList;
import org.jdesktop.observablecollections.ObservableList;

/**
 *
 * @author thorsten
 */
public class ArrayTitleAndControls extends javax.swing.JPanel {
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    private MetaClass detailClass;
    private CidsBean cidsBean;
    private String arrayProperty;
    private JList jList;

    public ArrayTitleAndControls(String title, MetaClass detailClass, String arrayProperty, JList jList) {
        super();
        this.jList = jList;
        initComponents();
        this.detailClass = detailClass;
        this.arrayProperty = arrayProperty;
        lblTitle.setText(title);
    }

    public MetaClass getDetailClass() {
        return detailClass;
    }

    public JList getJList() {
        return jList;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 2));

        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/editors/edit_add_mini.png"))); // NOI18N
        cmdAdd.setBorderPainted(false);
        cmdAdd.setMinimumSize(new java.awt.Dimension(16, 16));
        cmdAdd.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel1.add(cmdAdd);

        cmdRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/editors/edit_remove_mini.png"))); // NOI18N
        cmdRemove.setBorderPainted(false);
        cmdRemove.setMinimumSize(new java.awt.Dimension(16, 16));
        cmdRemove.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jPanel1.add(cmdRemove);

        jPanel2.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        lblTitle.setText(I18N.getString("de.cismet.cids.editors.ArrayTitleAndControls.lblTitle.text")); // NOI18N
        jPanel3.add(lblTitle, java.awt.BorderLayout.EAST);

        jLabel1.setText("           ");
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        Object o = jList.getSelectedValue();
        if (o instanceof CidsBean && o != null) {
            try {
                int index = jList.getSelectedIndex();
                ((CidsBean) o).delete();

                if (jList.getModel().getSize() == 0) {
                    jList.setSelectedValue(null, false);
                } else if (index <= jList.getModel().getSize() - 1) {
                    jList.setSelectedIndex(index);
                } else {
                    jList.setSelectedIndex(jList.getModel().getSize() - 1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        try {
            CidsBean newOne = detailClass.getEmptyInstance().getBean();



            CidsBean sourcebean = (CidsBean) jList.getClientProperty(CidsObjectEditorFactory.CIDS_BEAN);
            JList sourcelist = (JList) jList.getClientProperty(CidsObjectEditorFactory.SOURCE_LIST);
            if (sourcebean != null) {
                ((List) (sourcebean.getProperty(arrayProperty))).add(newOne);
            } else if (sourcelist!=null && sourcelist.getSelectedValue()!=null) {
               CidsBean localSourceBean=(CidsBean)sourcelist.getSelectedValue();
               //Entferne den String bis zum ersten []
               String subElement=arrayProperty.substring(arrayProperty.lastIndexOf("[]")+2);
               ((List) (localSourceBean.getProperty(arrayProperty))).add(newOne);
            }
            jList.setSelectedValue(newOne, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
}//GEN-LAST:event_cmdAddActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}
