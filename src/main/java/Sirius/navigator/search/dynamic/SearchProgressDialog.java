/*
 * SearchProgressDialog.java
 *
 * Created on 18. November 2003, 15:00
 */

package Sirius.navigator.search.dynamic;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

import org.apache.log4j.Logger;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.SearchResult;
import Sirius.navigator.resource.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.ui.tree.*;
import Sirius.navigator.ui.status.*;
import Sirius.navigator.ui.widget.*;
import Sirius.navigator.connection.*;
import de.cismet.tools.CismetThreadPool;

/**
 *
 * @author  pascal
 */
public class SearchProgressDialog extends javax.swing.JDialog
{
    
    private final Logger logger;

    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    
    private final ResourceManager resources;
    private final DefaultStatusChangeSupport statusChangeSupport;
    private final MutableImageLabel animationLabel;
    
    private SearchThread searchThread;
        
     /** Holds value of property canceld. */
    private boolean canceld;    
    
     /** Holds value of property resultNodes. */
    //private Node[] resultNodes;    
    
     /** Holds value of property searchResult. */
    private SearchResult searchResult;    

    
    
    /** Creates new form SearchProgressDialog */
    public SearchProgressDialog(JDialog parent, DefaultStatusChangeSupport statusChangeSupport)
    {
        super(parent, 
                I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.title"),
                true);
        
        this.logger = Logger.getLogger(this.getClass());
        
        this.resources = ResourceManager.getManager();
        this.statusChangeSupport = statusChangeSupport;
        
        initComponents();
        
        this.animationLabel = new MutableImageLabel(
                resources.getIcon(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.animationLabel.imageOff")),
                resources.getIcon(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.animationLabel.imageOn")));
        this.iconPanel.add(this.animationLabel, BorderLayout.CENTER);        
        this.cancelButton.addActionListener(new ButtonListener());
        
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
    
    // .........................................................................
    
    public void show(Collection classNodeKeys, Collection searchOptions)
    {
        if(this.searchThread != null && this.searchThread.isAlive())
        {
            logger.warn("search thread is still running");
            
            try
            {
                // TODO display warning message
                this.searchThread.join();
                logger.debug("searchThread.join() successfull");
            }
            catch(InterruptedException iexp)
            {
                if(logger.isDebugEnabled())logger.warn(iexp.getMessage(), iexp);
            }
        }
        
        this.statusChangeSupport.fireStatusChange(
                I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.show().status.running"),
                Status.MESSAGE_POSITION_2, Status.ICON_IGNORE, Status.ICON_BLINKING);
        this.animationLabel.switchOn(true);
        //this.setResultNodes(null);
        this.setSearchResult(null);
        this.setCanceld(false);

        searchThread = new SearchThread(classNodeKeys, searchOptions);
//        searchThread.start();
        CismetThreadPool.execute(searchThread);
        
        if(logger.isDebugEnabled())logger.debug("waiting for search thread to finish");
        //this.pack();
        super.show();
    }
    
    // .........................................................................
    
    
    /** Getter for property canceld.
     * @return Value of property canceld.
     *
     */
    public synchronized boolean isCanceld()
    {
        return this.canceld;
    }
    
    /** Setter for property canceld.
     * @param canceld New value of property canceld.
     *
     */
    private synchronized void setCanceld(boolean canceld)
    {
        this.canceld = canceld;
    }
    
    /** Getter for property resultNodes.
     * @return Value of property resultNodes.
     *
     */
    /*public synchronized Node[] getResultNodes()
    {
        return this.resultNodes;
    }*/
    
    /** Setter for property resultNodes.
     * @param resultNodes New value of property resultNodes.
     *
     */
    /*private synchronized void setResultNodes(Node[] resultNodes)
    {
        this.resultNodes = resultNodes;
    }*/
    
    // -------------------------------------------------------------------------
    
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(
                    I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.ButtonListener.actionPerformed().status.canceled"),
                    Status.MESSAGE_POSITION_2, Status.ICON_DEACTIVATED, Status.ICON_DEACTIVATED);
            SearchProgressDialog.this.animationLabel.switchOff(true);
            SearchProgressDialog.this.setCanceld(true);
            //SearchProgressDialog.this.setResultNodes(null);
            SearchProgressDialog.this.setSearchResult(null);

            SearchProgressDialog.this.dispose();
        }  
    }
    
    // -------------------------------------------------------------------------
    
    
    /**
     *
     *
     */
    private final class SearchThread extends Thread
    {
        private final Logger logger;
        private final Collection classNodeKeys;
        private final Collection searchOptions;
        
        //private Node[] resultNodes = null;
        private SearchResult searchResult = null;
        
        private SearchThread(Collection classNodeKeys, Collection searchOptions)
        {
            super("SearchThread");
            
            this.logger = Logger.getLogger(this.getClass());
            
            this.classNodeKeys = classNodeKeys;
            this.searchOptions = searchOptions;
        }
        
        public void run()
        {
            logger.info("starting new search with ");
            if(logger.isDebugEnabled())logger.debug("# classNodeKeys: " + classNodeKeys.size() + ", #  searchOptions: " + searchOptions.size());
            
            if(!SearchProgressDialog.this.isCanceld())
            {
                try
                {
                    if(this.classNodeKeys != null && this.classNodeKeys.size() > 0)
                    {
                        logger.debug("performing search with class ids");
                        this.searchResult = SessionManager.getProxy().search(this.classNodeKeys, this.searchOptions);
                    }
                    else
                    {
                        logger.debug("performing search without class ids");
                        this.searchResult = SessionManager.getProxy().search(this.searchOptions);
                    }
                    
                    if(!SearchProgressDialog.this.isCanceld())
                    {
                        SearchProgressDialog.this.setSearchResult(this.searchResult);
                        
                        if(this.searchResult.isNode() && this.searchResult.getNodes() != null && this.searchResult.getNodes().length > 0)
                        {
                            logger.info(this.searchResult.getNodes().length + " nodes found");
                            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(this.searchResult.getNodes().length +
                                    I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.SearchThread.run().status.results"),
                                    Status.MESSAGE_POSITION_2, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
                        }
                        else if(this.searchResult.isObject())
                        {
                            logger.info(this.searchResult.getObjects().length + " meta objects found");
                        }
                        else if(this.searchResult.isSearchParameter())
                        {
                            logger.debug("searchParameter found");
                            SearchProgressDialog.this.statusChangeSupport.fireStatusChange("", Status.MESSAGE_POSITION_2, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
                        }
                        else if(logger.isDebugEnabled())
                        {
                            logger.warn("no search results found: " + this.searchResult.getResult() + "(" + this.searchResult.getResult().getClass() + ")");                            
                            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(
                                    I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.SearchThread.run().status.noresults"),
                                    Status.MESSAGE_POSITION_2, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
                        }
                        
                        SearchProgressDialog.this.dispose();
                    }
                }
                catch(Throwable t)
                {
                    logger.error("could not perform search", t);
                    SearchProgressDialog.this.setSearchResult(null);
                    SearchProgressDialog.this.animationLabel.switchOff(true);

                    if(!SearchProgressDialog.this.isCanceld())
                    {
                        SearchProgressDialog.this.statusChangeSupport.fireStatusChange(
                                I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.SearchThread.run().status.error"),
                                Status.MESSAGE_POSITION_2, Status.ICON_DEACTIVATED, Status.ICON_ACTIVATED);
                        ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR, resources.getExceptionName("sx02"), resources.getExceptionMessage("sx02"), t);
                        SearchProgressDialog.this.dispose();
                    }    
                }
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JPanel contentPanel = new javax.swing.JPanel();
        iconPanel = new javax.swing.JPanel();
        javax.swing.JLabel infoLabel = new javax.swing.JLabel();
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        contentPanel.setLayout(new java.awt.GridBagLayout());

        iconPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        iconPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        contentPanel.add(iconPanel, gridBagConstraints);

        infoLabel.setText(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.infoLabel.text")); // NOI18N
        infoLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        contentPanel.add(infoLabel, gridBagConstraints);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new java.awt.BorderLayout());

        cancelButton.setMnemonic(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.cancelButton.mnemonic").charAt(0));
        cancelButton.setText(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.cancelButton.text")); // NOI18N
        cancelButton.setToolTipText(I18N.getString("Sirius.navigator.search.dynamic.SearchProgressDialog.cancelButton.tooltip")); // NOI18N
        buttonPanel.add(cancelButton, java.awt.BorderLayout.CENTER);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    /** Getter for property searchResult.
     * @return Value of property searchResult.
     *
     */
    public SearchResult getSearchResult()
    {
        return this.searchResult;
    }    
 
    /** Setter for property searchResult.
     * @param searchResult New value of property searchResult.
     *
     */
    public void setSearchResult(SearchResult searchResult)
    {
        this.searchResult = searchResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel iconPanel;
    // End of variables declaration//GEN-END:variables

   
   // ##########################################################################
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[])
    {
        //new SearchProgressDialog(new javax.swing.JFrame(), true).show();
        System.exit(0);
    }*/
}
