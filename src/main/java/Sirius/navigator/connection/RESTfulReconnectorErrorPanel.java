/*
 *  Copyright (C) 2010 jruiz
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * RESTfulReconnectorErrorPanel.java
 *
 * Created on 25.08.2010, 12:55:35
 */

package Sirius.navigator.connection;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;
import de.cismet.security.Proxy;
import java.awt.BorderLayout;
import javax.swing.UIManager;

/**
 *
 * @author jruiz
 */
public class RESTfulReconnectorErrorPanel extends javax.swing.JPanel {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RESTfulReconnector.class);

    private ProxyOptionsPanel panProxyOptions;
    private RESTfulReconnector reconnector;

    /** Creates new form RESTfulReconnectorErrorPanel */
    public RESTfulReconnectorErrorPanel(ProxyOptionsPanel panProxyOptions, RESTfulReconnector reconnector) {
        initComponents();
        this.reconnector = reconnector;
        this.panProxyOptions = panProxyOptions;
        panProxyOptionsWrapper.add(panProxyOptions, BorderLayout.CENTER);
        panProxyOptionsWrapper.setVisible(false);
    }

    public void setErrorMessage(String message) {
        labError.setText(message);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labError = new javax.swing.JLabel();
        panProxyOptionsWrapper = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        cbProxy = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        labIcon = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        labError.setText(org.openide.util.NbBundle.getMessage(RESTfulReconnectorErrorPanel.class, "RESTfulReconnectorErrorPanel.labError.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(labError, gridBagConstraints);

        panProxyOptionsWrapper.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText(org.openide.util.NbBundle.getMessage(RESTfulReconnectorErrorPanel.class, "RESTfulReconnectorErrorPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        panProxyOptionsWrapper.add(jPanel1, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(panProxyOptionsWrapper, gridBagConstraints);

        cbProxy.setText(org.openide.util.NbBundle.getMessage(RESTfulReconnectorErrorPanel.class, "RESTfulReconnectorErrorPanel.cbProxy.text")); // NOI18N
        cbProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProxyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(cbProxy, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        add(jSeparator1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        add(jSeparator2, gridBagConstraints);

        labIcon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        labIcon.setText(org.openide.util.NbBundle.getMessage(RESTfulReconnectorErrorPanel.class, "RESTfulReconnectorErrorPanel.labIcon.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(labIcon, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cbProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProxyActionPerformed
        panProxyOptionsWrapper.setVisible(cbProxy.isSelected());
    }//GEN-LAST:event_cbProxyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Proxy proxy = panProxyOptions.getProxy();
        if (!proxy.isEnabled()) {
            proxy = null;
        }
        reconnector.setProxy(proxy);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbProxy;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labError;
    private javax.swing.JLabel labIcon;
    private javax.swing.JPanel panProxyOptionsWrapper;
    // End of variables declaration//GEN-END:variables

}
