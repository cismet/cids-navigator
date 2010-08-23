package Sirius.navigator.search.dynamic.profile;

/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Filename		:
 * Version			:	1.0
 * Purpose			:
 * Created			:	11.09.2000
 * History			:
 *
 *******************************************************************************/
import java.io.*;
import java.util.zip.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;

import Sirius.server.search.store.*;
import Sirius.server.newuser.UserGroup;
import Sirius.server.middleware.types.Node;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.ui.tree.*;
import Sirius.navigator.resource.*;



public class QueryResultProfileManager extends ProfileManager
{
    protected SearchResultsTree searchTree;
    protected boolean newNodesLoaded = false;
    
    //protected JMenu searchMenu;
    
    public QueryResultProfileManager(JDialog dialog, SearchResultsTree searchTree, int profileType)
    {
        super(dialog, profileType);
        this.searchTree = searchTree;
        this.setTitle(org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.title"));//NOI18N
        
        this.initQueryResultProfileManager();
    }
    
    public QueryResultProfileManager(JFrame frame, SearchResultsTree searchTree, int profileType)
    {
        super(frame, profileType);
        this.searchTree = searchTree;
        this.setTitle(org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.title"));//NOI18N
        
        this.initQueryResultProfileManager();
    }
    
    protected void initQueryResultProfileManager()
    {
        //JMenuBar menuBar = new JMenuBar();
        //searchMenu = new JMenu(this.getTitle());
        //menuBar.add(searchMenu);
        
        //this.setJMenuBar(menuBar);
    }
    
    public void setSearchTree(SearchResultsTree searchTree)
    {
        this.searchTree = searchTree;
    }
    
    public boolean newNodesLoaded()
    {
        return newNodesLoaded;
    }
    
    public void updateQueryResultProfileManager()
    {
        try
        {
            userInfo = this.getQueryInfos();
            logger.debug("updateQueryResultProfileManager(): userInfo.length: " + userInfo.length);//NOI18N
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.updateQueryResultProfileManager().searchResultsWarning.message") ,//NOI18N
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.updateQueryResultProfileManager().searchResultsWarning.title"),//NOI18N
                    JOptionPane.WARNING_MESSAGE);
            
            logger.error("could not load user info", exp);//NOI18N
        }
        
        updateProfileTree();
        //updateSearchMenu();
        
        buttonSave.setEnabled(canStoreQueryResult());
        buttonDelete.setEnabled(canStoreQueryResult());
        
        newNodesLoaded = false;
    }
    
    /*private void updateSearchMenu()
    {
        if(userInfo != null & searchMenu != null)
        {
            this.searchMenu.removeAll();
            for(int i = 0; i < userInfo.length; i++)
            {
                JMenuItem searchItem = new JMenuItem(userInfo[i].toString());
                this.searchMenu.add(searchItem);
            }
        }
    }*/
    
    protected boolean canStoreQueryResult()
    {
        //logger.debug("searchFlag: " + SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        //logger.debug("STOREQUERYRESULT: " + UserGroup.STOREQUERYRESULT);
        //logger.debug("searchFlag & STOREUSERPROFILE: " + (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREQUERYRESULT));
        
        //return true ? (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREQUERYRESULT) != 0 : false;
        
        // TODO implement this
        return true;
    }
    
    
    public void show()
    {
        updateQueryResultProfileManager();
        super.show();
    }
    
    public void loadSearchResults(String filename)
    {
        try
        {
            if (logger.isDebugEnabled()) {
                logger.debug("loading query result'" + filename + "'");//NOI18N
            }
            String path=PropertyManager.getManager().getProfilesPath();
           
            File inputFile = new File(path, filename);
            
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fileInputStream));
            searchTree.setResultNodes((Node[])ois.readObject());
            ois.close();
            
            updateQueryResultProfileManager();
            logger.debug("<SEARCH PROFILE> Load QueryResult");//NOI18N
            
            newNodesLoaded = true;
            
        }
        catch(Throwable t)
        {
            logger.error("Error while loading profiles:", t);//NOI18N
            JOptionPane.showMessageDialog(this,
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.loadSearchResults().loadSearchResultsError.message"),//NOI18N
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.loadSearchResults().loadSearchResultsError.title"),//NOI18N
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        QueryInfo selectedQueryResultInfo = (QueryInfo)getSelectedInfo();
        if(logger.isDebugEnabled())logger.debug("selectedQueryResultInfo: '" + getSelectedInfo() + "'");//NOI18N
        
        if(e.getActionCommand().equals("close"))//NOI18N
        {
            dispose();
        }
        else if(e.getActionCommand().equals("save"))//NOI18N
        {
            try
            {
                if(searchTree.getResultNodes() == null)
                {
                    JOptionPane.showMessageDialog(this,
                            org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).searchResultsWarning.message"), //NOI18N
                            org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).searchResultsWarning.title"),//NOI18N
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                
                if(selectedQueryResultInfo != null)
                {
                    profileSaveDialog.show(selectedQueryResultInfo.getID(), selectedQueryResultInfo.getName(), false, false);
                }
                else
                {
                    profileSaveDialog.show(false, false);
                }
                
                if(profileSaveDialog.isAccepted())
                {
                    if (logger.isDebugEnabled()) {
                        logger.debug("saving query result as '" + this.profileSaveDialog.getSelectedProfileName() + "'");//NOI18N
                    }
                    File profilesPath = new File(PropertyManager.getManager().getProfilesPath());
                    if(profilesPath.exists() || profilesPath.mkdirs())
                    {
                        File outputFile = new File(PropertyManager.getManager().getProfilesPath(), this.profileSaveDialog.getSelectedProfileName());
                        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                        
                        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fileOutputStream));
                        oos.writeObject(searchTree.getResultNodes());
                        oos.close();
                        
                        updateQueryResultProfileManager();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this,
                                org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).noDirError.message"),//NOI18N
                                org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).noDirError.title"),//NOI18N
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch(Throwable t)
            {
                // TODO display error dialog
                logger.error("Error while saving profiles:", t);//NOI18N
            }
        }
        else if(selectedQueryResultInfo != null)
        {
            if(e.getActionCommand().equals("load"))//NOI18N
            {
                loadSearchResults(selectedQueryResultInfo.getFileName());
            }
            else if(e.getActionCommand().equals("delete"))//NOI18N
            {
                if (logger.isDebugEnabled()) {
                    logger.debug("deleting query result'" + selectedQueryResultInfo.getFileName() + "'");//NOI18N
                }
                try
                {
                    File inputFile = new File(PropertyManager.getManager().getProfilesPath(), selectedQueryResultInfo.getFileName());
                    inputFile.delete();
                }
                catch(Throwable t)
                {
                    logger.error("Error while deleting profiles:", t);//NOI18N
                    JOptionPane.showMessageDialog(this,
                            org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).deleteError.message"),//NOI18N
                            org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).deleteError.title"),//NOI18N
                            JOptionPane.ERROR_MESSAGE);
                }
                
                updateQueryResultProfileManager();
            }
        }
        else
        {
            //_TA_JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst ein "+profileTypeName+" aus.", "Keine Selektion", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).noProfileWarning.message"),//NOI18N
                    org.openide.util.NbBundle.getMessage(QueryResultProfileManager.class, "QueryResultProfileManager.actionPerformed(ActionEvent).noProfileWarning.title"),//NOI18N
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // FIXME quich hack, handle better later
    private QueryInfo[] getQueryInfos()
    {
        try
        {
            File profilesDir = new File(PropertyManager.getManager().getProfilesPath());
            File[] profiles = profilesDir.listFiles(new FileFilter()
            {
                public boolean 	accept(File pathname)
                {
                    return !pathname.isDirectory();
                }
            });
            
            if(profiles != null)
            {
                if (logger.isDebugEnabled()) {
                    logger.debug(profiles.length + " profiles found in '" + PropertyManager.getManager().getProfilesPath() + "'");//NOI18N
                }
                QueryInfo[] infos = new QueryInfo[profiles.length];
                for(int i = 0; i < profiles.length; i++)
                {
                    // omg
                    if (logger.isDebugEnabled()) {
                        logger.debug("filename: '" + profiles[i].getName() + "'");//NOI18N
                    }
                    QueryInfo info = new QueryInfo(i,profiles[i].getName(),profiles[i].getName(),profiles[i].getName());
                    infos[i] = info;
                }
                
                return infos;
            }
            else
            {
                logger.warn("no profiles found!");//NOI18N
            }
        }
        catch(Exception exp)
        {
            logger.error("could not load search results", exp);//NOI18N
        }
        
        return new QueryInfo[0];
    }
}
