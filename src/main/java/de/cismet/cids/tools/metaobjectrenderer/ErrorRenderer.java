/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.navigator.ui.RequestsFullSizeComponent;

import Sirius.server.middleware.types.MetaObject;

import java.awt.BorderLayout;

import javax.swing.JLabel;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ErrorRenderer extends javax.swing.JPanel implements RequestsFullSizeComponent {

    //~ Static fields/initializers ---------------------------------------------

    private static final String NEW_LINE = System.getProperty("line.separator");

    //~ Instance fields --------------------------------------------------------

    private final DefaultMetaObjectRenderer defaultComponent;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDefaultRenderer;
    private javax.swing.JButton btnShowProblem;
    private javax.swing.JPanel defaultrendererComponent;
    private javax.swing.JPanel footerComponent;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOopsIcon;
    private javax.swing.JLabel lblOopsText;
    private javax.swing.JPanel panOpps;
    private javax.swing.JScrollPane spDefaultRenderer;
    private javax.swing.JPanel stracktraceComponent;
    private javax.swing.JTextPane tpErrorMessage;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ErrorRenderer.
     *
     * @param  throwable  DOCUMENT ME!
     * @param  mo         DOCUMENT ME!
     * @param  title      DOCUMENT ME!
     */
    public ErrorRenderer(final Throwable throwable, final MetaObject mo, final String title) {
        initComponents();
        defaultComponent = (DefaultMetaObjectRenderer)new DefaultMetaObjectRenderer().getSingleRenderer(mo, title);
        spDefaultRenderer.setViewportView(defaultComponent);
        jLabel3.setText(mo.getBean().toString());

        final StringBuilder result = new StringBuilder(throwable.toString()).append(NEW_LINE);

        // add each element of the stack trace
        for (final StackTraceElement element : throwable.getStackTrace()) {
            result.append("\t").append(element).append(NEW_LINE);
        }
        tpErrorMessage.setText(result.toString());
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

        stracktraceComponent = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tpErrorMessage = new javax.swing.JTextPane();
        defaultrendererComponent = new javax.swing.JPanel();
        spDefaultRenderer = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        footerComponent = new javax.swing.JPanel();
        btnShowProblem = new javax.swing.JButton();
        btnDefaultRenderer = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        panOpps = new javax.swing.JPanel();
        lblOopsText = new javax.swing.JLabel();
        lblOopsIcon = new javax.swing.JLabel();

        stracktraceComponent.setOpaque(false);
        stracktraceComponent.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setOpaque(false);

        tpErrorMessage.setEditable(false);
        jScrollPane1.setViewportView(tpErrorMessage);

        stracktraceComponent.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        defaultrendererComponent.setOpaque(false);
        defaultrendererComponent.setLayout(new java.awt.BorderLayout());

        spDefaultRenderer.setOpaque(false);
        defaultrendererComponent.add(spDefaultRenderer, java.awt.BorderLayout.CENTER);

        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel3.setForeground(new java.awt.Color(127, 127, 127));
        jLabel3.setText(org.openide.util.NbBundle.getMessage(ErrorRenderer.class, "ErrorRenderer.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jPanel3.add(jPanel2, java.awt.BorderLayout.NORTH);

        footerComponent.setOpaque(false);
        footerComponent.setLayout(new java.awt.GridBagLayout());

        btnShowProblem.setText(org.openide.util.NbBundle.getMessage(
                ErrorRenderer.class,
                "ErrorRenderer.btnShowProblem.text")); // NOI18N
        btnShowProblem.setMaximumSize(new java.awt.Dimension(230, 25));
        btnShowProblem.setMinimumSize(new java.awt.Dimension(230, 25));
        btnShowProblem.setPreferredSize(new java.awt.Dimension(230, 25));
        btnShowProblem.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnShowProblemActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        footerComponent.add(btnShowProblem, gridBagConstraints);

        btnDefaultRenderer.setText(org.openide.util.NbBundle.getMessage(
                ErrorRenderer.class,
                "ErrorRenderer.btnDefaultRenderer.text")); // NOI18N
        btnDefaultRenderer.setMaximumSize(new java.awt.Dimension(230, 25));
        btnDefaultRenderer.setMinimumSize(new java.awt.Dimension(230, 25));
        btnDefaultRenderer.setPreferredSize(new java.awt.Dimension(230, 25));
        btnDefaultRenderer.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnDefaultRendererActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        footerComponent.add(btnDefaultRenderer, gridBagConstraints);

        jPanel3.add(footerComponent, java.awt.BorderLayout.SOUTH);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        panOpps.setOpaque(false);
        panOpps.setLayout(new java.awt.GridBagLayout());

        lblOopsText.setFont(new java.awt.Font("Ubuntu", 1, 18));                                     // NOI18N
        lblOopsText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOopsText.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/tools/metaobjectrenderer/error_text.png"))); // NOI18N
        lblOopsText.setText(org.openide.util.NbBundle.getMessage(
                ErrorRenderer.class,
                "ErrorRenderer.lblOopsText.text"));                                                  // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
        panOpps.add(lblOopsText, gridBagConstraints);

        lblOopsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOopsIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/tools/metaobjectrenderer/dialog_error.png"))); // NOI18N
        lblOopsIcon.setText(org.openide.util.NbBundle.getMessage(
                ErrorRenderer.class,
                "ErrorRenderer.lblOopsIcon.text"));                                                    // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        panOpps.add(lblOopsIcon, gridBagConstraints);

        jPanel1.add(panOpps, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel3, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnDefaultRendererActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnDefaultRendererActionPerformed
        jPanel1.removeAll();
        jPanel1.add(defaultrendererComponent, BorderLayout.CENTER);
        validate();
        repaint();
    }                                                                                      //GEN-LAST:event_btnDefaultRendererActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnShowProblemActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnShowProblemActionPerformed
        jPanel1.removeAll();
        jPanel1.add(stracktraceComponent);
        validate();
        repaint();
    }                                                                                  //GEN-LAST:event_btnShowProblemActionPerformed
}
