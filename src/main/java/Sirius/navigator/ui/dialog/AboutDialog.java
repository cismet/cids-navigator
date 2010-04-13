package Sirius.navigator.ui.dialog;

import java.io.*;

import Sirius.navigator.resource.*;

/**
 *
 * @author  pascal
 */
public class AboutDialog extends javax.swing.JDialog
{
    /** Creates new form AboutDialog */
    public AboutDialog(java.awt.Frame parent)
    {
        super(parent, true);
        initComponents();
        
        try
        {
            StringBuffer buffer = new StringBuffer();
            String string = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceManager.getManager().getNavigatorResourceAsStream(
                    "doc/about_de_DE.html")));//NOI18N
        
            while((string = reader.readLine()) != null)
            {
                buffer.append(string);
            }
            
            this.aboutLabel.setText(buffer.toString());
        }
        catch(IOException ioexp)
        {
            //logger.warn("could not load html about file", ioexp);
            this.aboutLabel.setText(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.aboutLabel.defaulttext"));//NOI18N
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutPanel = new javax.swing.JPanel();
        aboutLabel = new javax.swing.JLabel();
        closePanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setName("AboutDialog"); // NOI18N
        setResizable(false);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        aboutPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        aboutPanel.setLayout(new java.awt.GridLayout(1, 1));

        aboutLabel.setBackground(new java.awt.Color(255, 255, 240));
        aboutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aboutLabel.setToolTipText(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.aboutLabel.tooltip")); // NOI18N
        aboutLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        aboutLabel.setOpaque(true);
        aboutLabel.setPreferredSize(null);
        aboutPanel.add(aboutLabel);

        getContentPane().add(aboutPanel, java.awt.BorderLayout.CENTER);

        closePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 5, 5));
        closePanel.setLayout(new java.awt.GridLayout(1, 0));

        closeButton.setMnemonic(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.closeButton.mnemonic").charAt(0));
        closeButton.setText(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.closeButton.text")); // NOI18N
        closeButton.setToolTipText(org.openide.util.NbBundle.getMessage(AboutDialog.class, "AboutDialog.closeButton.tooltip")); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        closePanel.add(closeButton);

        getContentPane().add(closePanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed
        // Add your handling code here:
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[])
    {
        AboutDialog aboutDialog = new AboutDialog(new javax.swing.JFrame());
        aboutDialog.setLocationRelativeTo(null);
        aboutDialog.pack();
        aboutDialog.show();
    }*/
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutLabel;
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel closePanel;
    // End of variables declaration//GEN-END:variables
    
}
