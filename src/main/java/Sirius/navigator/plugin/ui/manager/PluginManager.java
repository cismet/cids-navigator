/*
 * PluginManager.java
 *
 * Created on 11. Mai 2003, 15:46
 */


package Sirius.navigator.plugin.ui.manager;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import Sirius.navigator.resource.*;
import Sirius.navigator.plugin.*;
import Sirius.navigator.method.*;
import Sirius.navigator.ui.progress.*;
import Sirius.navigator.exception.ExceptionManager;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author  Peter Alzheimer
 */
public class PluginManager extends javax.swing.JDialog 
{
    protected final Logger logger;
    private final PluginMetaInfoPanel metaInfoPanel;
    private final PluginTree pluginTree;
    
    /** Creates new form PluginManager */
    public PluginManager(java.awt.Frame parent) 
    {
        super(parent, org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.title"), true);  // NOI18N
        
        this.logger = Logger.getLogger(this.getClass());
        this.pluginTree = new PluginTree();
        this.metaInfoPanel = new PluginMetaInfoPanel();
        
        this.initComponents();   
        
        this.pluginTree.addTreeSelectionListener(new PluginTreeListener());
        ActionListener actionListener = new ButtonListener();
        this.loadButton.addActionListener(actionListener);
        this.unloadButton.addActionListener(actionListener);
        this.activateButton.addActionListener(actionListener);
        this.deactivateButton.addActionListener(actionListener);
        this.closeButton.addActionListener(actionListener);
    }
    
    public void show()
    {
        if(!pluginTree.isInitialized())
        {
            pluginTree.init();
        }
        
        this.setButtonsEnabled(pluginTree.getSelectedNode());
        super.show();   
    }
    
    // .........................................................................
    
    /*public static void main(String args[])
    {
        PluginManager pm = new  PluginManager(new JFrame(), false);
        pm.pack();
        pm.setVisible(true);
    }*/
    
    
    // #########################################################################
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JPanel treePanel = new javax.swing.JPanel();
        javax.swing.JScrollPane scrollPane = new JScrollPane(pluginTree);
        infoPanel = new javax.swing.JPanel();
        infoPanel.add(metaInfoPanel);
        buttonPanel = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        unloadButton = new javax.swing.JButton();
        activateButton = new javax.swing.JButton();
        deactivateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setModal(true);
        setName("pluginManagerDialog"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        treePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        treePanel.setPreferredSize(new java.awt.Dimension(200, 300));
        treePanel.setLayout(new java.awt.GridLayout(1, 1));

        scrollPane.setPreferredSize(new java.awt.Dimension(0, 0));
        treePanel.add(scrollPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 3);
        getContentPane().add(treePanel, gridBagConstraints);

        infoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        infoPanel.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 3, 5);
        getContentPane().add(infoPanel, gridBagConstraints);

        buttonPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 3)));
        buttonPanel.setLayout(new java.awt.GridLayout(1, 5, 5, 0));

        loadButton.setMnemonic(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.loadButton.mnemonics").charAt(0));
        loadButton.setText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.loadButton.text")); // NOI18N
        loadButton.setToolTipText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.loadButton.tooltip")); // NOI18N
        loadButton.setActionCommand("load");
        loadButton.setEnabled(false);
        buttonPanel.add(loadButton);

        unloadButton.setMnemonic(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.unloadButton.mnemonics").charAt(0));
        unloadButton.setText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.unloadButton.text")); // NOI18N
        unloadButton.setToolTipText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.unloadButton.tooltip")); // NOI18N
        unloadButton.setActionCommand("unload");
        unloadButton.setEnabled(false);
        buttonPanel.add(unloadButton);

        activateButton.setMnemonic(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.activateButton.mnemonics").charAt(0));
        activateButton.setText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.activateButton.text")); // NOI18N
        activateButton.setToolTipText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.activateButton.tooltip")); // NOI18N
        activateButton.setActionCommand("activate");
        activateButton.setEnabled(false);
        buttonPanel.add(activateButton);

        deactivateButton.setMnemonic(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.deactivateButton.mnemonics").charAt(0));
        deactivateButton.setText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.deactivateButton.text")); // NOI18N
        deactivateButton.setToolTipText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.deactivateButton.tooltip")); // NOI18N
        deactivateButton.setActionCommand("deactivate");
        deactivateButton.setEnabled(false);
        buttonPanel.add(deactivateButton);

        closeButton.setMnemonic(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.closeButton.mnemonics").charAt(0));
        closeButton.setText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.closeButton.text")); // NOI18N
        closeButton.setToolTipText(org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.closeButton.tooltip")); // NOI18N
        closeButton.setActionCommand("close");
        buttonPanel.add(closeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        getContentPane().add(buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        //dispose();
    }//GEN-LAST:event_closeDialog
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activateButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton deactivateButton;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton unloadButton;
    // End of variables declaration//GEN-END:variables
    
    
    private void setButtonsEnabled(PluginTree.PluginTreeNode pluginNode)
    {
        if(pluginNode == null || pluginNode.isPluginMethodNode() || pluginNode.getPluginDescriptor() == null)
        {
            loadButton.setEnabled(false);
            unloadButton.setEnabled(false);
            activateButton.setEnabled(false);
            deactivateButton.setEnabled(false);
        }
        else
        {
            Sirius.navigator.plugin.PluginDescriptor descriptor = pluginNode.getPluginDescriptor();
            
            loadButton.setEnabled(!descriptor.isLoaded());
            unloadButton.setEnabled(descriptor.isUnloadable() & descriptor.isLoaded());
            activateButton.setEnabled(descriptor.isLoaded() & !descriptor.isActivated());
            deactivateButton.setEnabled(descriptor.isDeactivateable() & descriptor.isActivated());
        }
    }
    
    private class PluginTreeListener implements TreeSelectionListener
    {
        
        public void valueChanged(TreeSelectionEvent e)
        {
            Object object = e.getPath().getLastPathComponent();
            if(object != null && object instanceof PluginTree.PluginTreeNode)
            {
                PluginTree.PluginTreeNode pluginNode = (PluginTree.PluginTreeNode)object;
                PluginManager.this.setButtonsEnabled(pluginNode);
                
                if(pluginNode.isPluginNode())
                {
                    metaInfoPanel.setPluginDescription(pluginNode.getPluginDescriptor().getMetaInfo());
                }
                else if(pluginNode.isPluginMethodNode())
                {
                    metaInfoPanel.setMethodDescription(pluginNode.getPluginMethodDescriptor());
                }
                else
                {
                    metaInfoPanel.clear();
                }
            }
            else
            {
               PluginManager.this.setButtonsEnabled(null);
            }
        } 
    }
    
    private final class ButtonListener implements ActionListener
    {
        private final ProgressObserver progressObserver;
        private final PluginManagerMethod pluginManagerMethod;
        private final ProgressDialog progressDialog;

        private ButtonListener()
        {
            this.progressObserver = new ProgressObserver(1000, 500);
            this.progressObserver.setIndeterminate(true);
            this.pluginManagerMethod = new PluginManagerMethod(progressObserver);
            this.progressDialog = new ProgressDialog(PluginManager.this);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            PluginTree.PluginTreeNode pluginNode = PluginManager.this.pluginTree.getSelectedNode();
            if(e.getActionCommand().equals("close"))  // NOI18N
            {
                PluginManager.this.setVisible(false);
                PluginManager.this.dispose();
            } 
            else if(pluginNode != null && pluginNode.isPluginNode())
            {
                //try
                //{
                    if(logger.isDebugEnabled())logger.debug("performing plugin action");  // NOI18N
                    if(e.getActionCommand().equals("load") && pluginNode.getPluginDescriptor().isProgressObservable())  // NOI18N
                    {
                        ProgressObserver pluginProgressObserver = pluginNode.getPluginDescriptor().getContext().getEnvironment().getProgressObserver();
                        if(logger.isDebugEnabled())logger.debug("using plugin progress observer '" + pluginProgressObserver.getName() + "'");  // NOI18N
                        this.pluginManagerMethod.setProgressObserver(pluginProgressObserver);
                    }
                    else
                    {
                        this.pluginManagerMethod.setProgressObserver(this.progressObserver);
                    }

                    String[] strings = new String[]{e.getActionCommand(), pluginNode.getPluginDescriptor().getId(), pluginNode.getPluginDescriptor().getName()};
                    this.progressDialog.setLocationRelativeTo(PluginManager.this);
                    this.progressDialog.show(this.pluginManagerMethod, strings);
                    
                    if(logger.isDebugEnabled())logger.debug("plugin action performed");  // NOI18N
                    PluginManager.this.setButtonsEnabled(pluginNode);
                /*}
                catch(Exception exp)
                {
                    logger.error("could not load / activate plugin '" + pluginNode.getPluginDescriptor().getName() + "'", t);
                    ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR, ResourceManager.getManager().getExceptionName("px01"), ResourceManager.getManager().getExceptionMessage("px01"), t);
                    pluginNode.getPluginDescriptor().setLoaded(false);
                    pluginNode.getPluginDescriptor().setActivated(false);
                }*/
            }
        }  
    }
    
    // .........................................................................
    
    private final class PluginManagerMethod extends MultithreadedMethod
    {
        private String pluginId = null;
        private String actionCommand = null;
        
        public PluginManagerMethod(ProgressObserver progressObserver)
        {
            super(progressObserver);
        }
        
        private synchronized void setProgressObserver(ProgressObserver progressObserver) 
        {
            this.progressObserver = progressObserver;
        }
        
        protected void init(Object object)
        {
            String[] strings = (String[])object;
            this.actionCommand = strings[0];
            this.pluginId = strings[1];
            
            this.progressObserver.reset();
            this.progressObserver.setName(strings[2]);
            try{this.progressObserver.setMessage(
                    org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.progressObserver.message.default"));}  // NOI18N
            catch(InterruptedException iexp){}
        }
        
        protected void doInvoke()
        {        
            if(actionCommand.equals("load"))  // NOI18N
            {
               try
               {
                   this.progressObserver.setMessage(
                           org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.progressObserver.message.load"));  // NOI18N
                   PluginRegistry.getRegistry().loadPlugin(pluginId);
               }
               catch(Throwable t)
               {
                    logger.error("could not load plugin '" + pluginId + "'", t);  // NOI18N
                    ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR,
                            org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon1.name"),  // NOI18N
                            org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon1.message"),  // NOI18N
                            t);
               }
               finally
               {
                   try{this.progressObserver.setFinished(true);}
                   catch(InterruptedException iexp)
                   {
                    logger.error("could not load plugin", iexp);  // NOI18N
                   }
               }
            }
            else if(actionCommand.equals("unload"))  // NOI18N
            {
                logger.fatal("method not implemented");  // NOI18N
                throw new RuntimeException("method not implemented");  // NOI18N
                //this.progressObserver.setMessage(ResourceManager.getManager().getString("plugin.progress.unload"));
            }
            else if(actionCommand.equals("activate"))  // NOI18N
            {
                try
                {
                    this.progressObserver.setMessage(
                            org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.progressObserver.message.activate"));  // NOI18N
                    Thread.currentThread().sleep(1000);
                    PluginRegistry.getRegistry().activatePlugin(pluginId);
                    Thread.currentThread().sleep(1000);
                }
                catch(Throwable t)
                {
                   logger.error("could not activate plugin '" + pluginId + "'", t);  // NOI18N
                   ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR,
                           org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon2.name"),  // NOI18N
                           org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon2.message"),  // NOI18N
                           t);
                }
                finally
                {
                   try{this.progressObserver.setFinished(true);}
                   catch(InterruptedException iexp)
                   {
                    logger.error("could not activate plugin", iexp);  // NOI18N
                   }
                }
            }
            else if(actionCommand.equals("deactivate"))  // NOI18N
            {
                try
                {
                    this.progressObserver.setMessage(
                            org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.progressObserver.message.deactivate"));  // NOI18N
                    Thread.currentThread().sleep(1000);
                    PluginRegistry.getRegistry().deactivatePlugin(pluginId);
                    Thread.currentThread().sleep(1000);
                }
                catch(Throwable t)
                {
                   logger.error("could not deactivate plugin '" + pluginId + "'", t);  // NOI18N
                   ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR,
                           org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon3.name"),  // NOI18N
                           org.openide.util.NbBundle.getMessage(PluginManager.class, "PluginManager.doInvoke().ExceptionManager_anon3.message"),  // NOI18N
                           t);
                }
                finally
                {
                   try{this.progressObserver.setFinished(true);}
                   catch(InterruptedException iexp)
                   {
                    logger.error("could not deactivate plugin", iexp);  // NOI18N
                   }
                }
            }
        }
    }
}
