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
        super(parent, ResourceManager.getManager().getString("search.dialog.progress.title"), true);
        
        this.logger = Logger.getLogger(this.getClass());
        
        this.resources = ResourceManager.getManager();
        this.statusChangeSupport = statusChangeSupport;
        
        initComponents();
        
        this.animationLabel = new MutableImageLabel(ResourceManager.getManager().getIcon("SearchIcon01.gif"),  ResourceManager.getManager().getIcon("SearchIcon02.gif"));
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
        
        this.statusChangeSupport.fireStatusChange(SearchProgressDialog.this.resources.getString("search.dialog.progress.status.running"), Status.MESSAGE_POSITION_2, Status.ICON_IGNORE, Status.ICON_BLINKING);
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
            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(SearchProgressDialog.this.resources.getString("search.dialog.progress.status.canceld"), Status.MESSAGE_POSITION_2, Status.ICON_DEACTIVATED, Status.ICON_DEACTIVATED);
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
                            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(this.searchResult.getNodes().length + SearchProgressDialog.this.resources.getString("search.dialog.progress.status.results"), Status.MESSAGE_POSITION_2, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
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
                            SearchProgressDialog.this.statusChangeSupport.fireStatusChange(SearchProgressDialog.this.resources.getString("search.dialog.progress.status.noresults"), Status.MESSAGE_POSITION_2, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
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
                        SearchProgressDialog.this.statusChangeSupport.fireStatusChange(SearchProgressDialog.this.resources.getString("search.dialog.progress.status.error"), Status.MESSAGE_POSITION_2, Status.ICON_DEACTIVATED, Status.ICON_ACTIVATED);
                        ExceptionManager.getManager().showExceptionDialog(ExceptionManager.ERROR, ResourceManager.getManager().getExceptionName("sx02"), ResourceManager.getManager().getExceptionMessage("sx02"), t);
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        javax.swing.JPanel buttonPanel;
        javax.swing.JPanel contentPanel;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel infoLabel;

        contentPanel = new javax.swing.JPanel();
        iconPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        contentPanel.setLayout(new java.awt.GridBagLayout());

        contentPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 0, 5)));
        iconPanel.setLayout(new java.awt.BorderLayout());

        iconPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(25, 25, 25, 25))));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        contentPanel.add(iconPanel, gridBagConstraints);

        infoLabel.setText(resources.getString("search.dialog.progress.message"));
        infoLabel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(25, 25, 25, 25))));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        contentPanel.add(infoLabel, gridBagConstraints);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.BorderLayout());

        buttonPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        cancelButton.setMnemonic(resources.getButtonMnemonic("cancel"));
        cancelButton.setText(resources.getButtonText("cancel"));
        cancelButton.setToolTipText(resources.getButtonTooltip("cancel"));
        buttonPanel.add(cancelButton, java.awt.BorderLayout.CENTER);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

    }
    // </editor-fold>//GEN-END:initComponents
    
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
