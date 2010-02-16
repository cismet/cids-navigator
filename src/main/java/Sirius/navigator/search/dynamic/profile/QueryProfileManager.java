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
 * Created			:	05.09.2000
 * History			:
 *
 *******************************************************************************/
import java.io.*;
import java.util.zip.*;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.beans.*;

//import Sirius.server.search.query.*;
import Sirius.server.newuser.UserGroup;

//import Sirius.navigator.NavigatorLogger;
import Sirius.navigator.connection.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.search.dynamic.*;
import Sirius.navigator.connection.proxy.*;
//import Sirius.navigator.Views.Tree.SearchTree;
//import Sirius.navigator.tools.*;
//import Sirius.navigator.ui.dialog.*;

import Sirius.server.search.store.*;


import Sirius.navigator.resource.*;

public class QueryProfileManager extends ProfileManager
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    //protected SearchDialog searchDialog;
    protected boolean groupFlag = false;
    protected boolean saveFlag = false;
    
    /** Holds value of property searchDialog. */
    private SearchDialog searchDialog;
    
    /** Holds value of property searchPropertiesBean. */
    //private SearchPropertiesBean searchPropertiesBean;
    
    public QueryProfileManager(JDialog dialog)
    {
        super(dialog, QUERY_PROFILE);
        
        //this.logger = Logger.getLogger(this.getClass());
    }
    
    /*public QueryProfileManager()
    {
        super(new JFrame(), QUERY_PROFILE);
    }
     
    public QueryProfileManager(JDialog dialog, SearchDialog searchDialog, String profileTypeName)
    {
        super(dialog, profileTypeName);
        this.searchDialog = searchDialog;
    }
     
    public QueryProfileManager(JFrame frame, SearchDialog searchDialog, String profileTypeName)
    {
        super(frame, profileTypeName);
        this.searchDialog = searchDialog;
    }*/
    
    protected void updateQueryProfileManager() throws Exception
    {
        userInfo = (QueryInfo[])SessionManager.getProxy().getUserQueryInfos(SessionManager.getSession().getUser());
        userGroupInfo = (QueryInfo[])SessionManager.getProxy().getUserGroupQueryInfos(SessionManager.getSession().getUser().getUserGroup());
        
        updateProfileTree();
        
        //if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> User SearchFlag: " + SessionManager.getSession().getUser().getUserGroup().getSearchFlag() + " User can store UserProfile: " + canStoreUserProfile() + " User can store UserGroupProfile: " + canStoreUserGroupProfile());
        //if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> userGroupInfo: " + userGroupInfo);
        //if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> userInfo: " + userGroupInfo);
        
        
        //buttonSave.setEnabled(canStoreUserProfile());
        //buttonDelete.setEnabled(canStoreUserGroupProfile());
        
        buttonSave.setEnabled(canStoreUserProfile(userInfo) || canStoreUserGroupProfile(userGroupInfo));
        buttonDelete.setEnabled(canStoreUserProfile(userInfo) || canStoreUserGroupProfile(userGroupInfo));
    }
    
    protected boolean canStoreUserProfile(QueryInfo[] queryInfo)
    {
        //if(logger.isDebugEnabled())logger.debug("searchFlag: " + SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        //if(logger.isDebugEnabled())logger.debug("STOREUSERPROFILE: " + UserGroup.STOREUSERPROFILE);
        //if(logger.isDebugEnabled())logger.debug("searchFlag & STOREUSERPROFILE: " + (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREUSERPROFILE));
        
        //return SessionManager.getSession().getUser().getUserGroup().getSearchFlag() == UserGroup.STOREUSERPROFILE;
        
        /*if(queryInfo != null && queryInfo.length > 0)
        {
            return queryInfo[0].getUserGroups().contains(SessionManager.getSession().getUser().getUserGroup());
        }
        
        return false;*/
        
        // TODO implement this
        return true;
    }
    
    protected boolean canStoreUserGroupProfile(QueryInfo[] queryInfo)
    {
        //if(logger.isDebugEnabled())logger.debug("searchFlag: " + SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        //if(logger.isDebugEnabled())logger.debug("STOREUSERPROFILE: " + UserGroup.STOREUSERPROFILE);
        //if(logger.isDebugEnabled())logger.debug("searchFlag & STOREUSERPROFILE: " + (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREUSERPROFILE));
        
        //return true ? (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREUSERGROUPPROFILE) != 0 : false;
        
        /*if(queryInfo != null && queryInfo.length > 0)
        {
            return queryInfo[0].getUserGroups().contains(SessionManager.getSession().getUser().getUserGroup());
        }
        
        return false;*/
        
        // TODO implement this
        return true;
    }
    
    public void show()
    {
        //this.show(null);
        try
        {
            updateQueryProfileManager();
            super.show();
        }
        catch(Exception exp)
        {
            logger.error(exp.getMessage(), exp);
            ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.ERROR, "Exception", exp.getMessage(), exp);
        }
    }
    
    /*public void show(SearchPropertiesBean searchPropertiesBean)
    {
        try
        {
            this.setSearchPropertiesBean(searchPropertiesBean);
            
            updateQueryProfileManager();
            super.show();
        }
        catch(Exception exp)
        {
            logger.error(exp.getMessage(), exp);
            ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.ERROR, "Exception", exp.getMessage(), exp);
        }
    }*/
    
    // FIXME inner class
    public void actionPerformed(ActionEvent e)
    {
        QueryInfo selectedQueryInfo = (QueryInfo)getSelectedInfo();
        
        if(e.getActionCommand().equals("close"))
        {
            dispose();
        }
        else if(e.getActionCommand().equals("save"))
        {
            try
            {
                //searchDialog.checkInput();
                //searchDialog.updateSearchModel();
                
                if(selectedQueryInfo != null)
                {
                    profileSaveDialog.show(selectedQueryInfo.getID(), selectedQueryInfo.getName(), canStoreUserGroupProfile(new QueryInfo[]{selectedQueryInfo}), selectedQueryInfo.getUserGroups().size() > 0);
                }
                else
                {
                    profileSaveDialog.show(canStoreUserGroupProfile(userInfo), canStoreUserGroupProfile(userGroupInfo));
                }
                
                if(profileSaveDialog.isAccepted())
                {
                    //System.out.println("profileSaveDialog.isAccepted(): " + profileSaveDialog.isAccepted());
                    //SiriusGZIPOutputStream gzos = new SiriusGZIPOutputStream(baos);
                    //GZIPOutputStream gzos = new GZIPOutputStream(baos);
                    //ObjectOutputStream oos = new ObjectOutputStream(gzos);
                    
                    //ObjectOutputStream oos = new ObjectOutputStream(baos);
                    //oos.writeObject(searchDialog.getModel());
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    
                    XMLEncoder xmlEncoder = new XMLEncoder(baos);
                    //xmlEncoder.writeObject(this.getSearchPropertiesBean());
                    xmlEncoder.writeObject(this.getSearchDialog().getSearchProperties());
                    xmlEncoder.close();
                    
                    //Query query = new Query(profileSaveDialog.getSelectedProfileID(), SessionManager.getSession().getUser().getLocalServerName(), profileSaveDialog.getSelectedProfile(), baos.toByteArray(), !profileSaveDialog.isGroupProfile());

                    QueryData queryData = new QueryData(profileSaveDialog.getSelectedProfileID(), SessionManager.getSession().getUser().getDomain(), profileSaveDialog.getSelectedProfileName(), baos.toByteArray());
                    
                    
                    //= new QueryData(profileSaveDialog.getSelectedProfileID(), SessionManager.getSession().getUser().getLocalServerName(), profileSaveDialog.getSelectedProfile(), baos.toByteArray(), !profileSaveDialog.isGroupProfile());
                    
                    //System.out.println("Query: " + query);
                    
                    
                    if(profileSaveDialog.isGroupProfile())
                    {
                        if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Save UserGroupQuery key: " + SessionManager.getSession().getUser().getUserGroup().getKey().toString());
                        queryData.addUserGroup(SessionManager.getSession().getUser().getUserGroup().getKey().toString());     
                    }
                    else if(logger.isDebugEnabled())
                    {
                        logger.debug("<SEARCH PROFILE> Save UserQuery: " + queryData);
                    }
                    
                    SessionManager.getProxy().storeQueryData(SessionManager.getSession().getUser(), queryData);
                    updateQueryProfileManager();
                    
                    /*if(profileSaveDialog.getSelectedProfileID() == -1)
                    {
                     
                    }
                    else
                    {
                        if(profileSaveDialog.isGroupProfile())
                        {
                            if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Save UserGroupQuery: Overwrite " + query);
                            SessionManager.getProxy().updateQuery(query);
                        }
                        else
                        {
                            if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Save UserQuery: Overwrite " + query);
                            SessionManager.getProxy().updateQuery(query);
                        }
                    }*/
                    
                    
                }
                else if(logger.isDebugEnabled())
                {
                    logger.debug("<SEARCH PROFILE> Saving canceled");
                }
            }
            catch(Throwable t)
            {
                logger.error(t.getMessage(), t);
                ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.ERROR, "Exception", t.getMessage(), t);
            }
        }
        else if(selectedQueryInfo != null)
        {
            if(e.getActionCommand().equals("load"))
            {
                try
                {
                    if(logger.isDebugEnabled())logger.debug("loading query data, id: " + selectedQueryInfo.getID() + ", domain: " + selectedQueryInfo.getDomain());
                    QueryData queryData = SessionManager.getProxy().getQueryData(selectedQueryInfo.getID(), selectedQueryInfo.getDomain());
                    
                    XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(queryData.getData()));
                    Object result = xmlDecoder.readObject();
                    xmlDecoder.close();
                    
                    if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Load Query: " + result + " GroupQueryInfo: " + selectedQueryInfo.getUserGroups().size() + "("+selectedQueryInfo+")");
                    
                    //this.setSearchPropertiesBean((SearchPropertiesBean)result);
                    
                    this.getSearchDialog().setSearchProperties((SearchPropertiesBean)result);
                    
                    //SiriusGZIPInputStream gzos = new SiriusGZIPInputStream(baos);
                    //GZIPInputStream gzos = new GZIPInputStream(baos);
                    //ObjectInputStream oos = new ObjectInputStream(gzos);
                    //ObjectInputStream oos = new ObjectInputStream(baos);
                    
                    
                    //searchDialog.setModel((SearchModel)oos.readObject());
                    
                }
                catch(Throwable t)
                {
                    logger.error("error loading stored beand data:", t);
                    ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.ERROR, "Exception", t.getMessage(), t);
                }
                
                this.dispose();
            }
            else if(e.getActionCommand().equals("delete"))
            {
                if(selectedQueryInfo.getUserGroups().size() > 0 && !canStoreUserGroupProfile(new QueryInfo[]{selectedQueryInfo}))
                {
                    //_TA_JOptionPane.showMessageDialog(this, "Sie haben nicht die Berechtigung ein Benutzergruppenprofil zu loeschen", "Profil loeschen", JOptionPane.WARNING_MESSAGE);
                    JOptionPane.showMessageDialog(this,
                            I18N.getString("Sirius.navigator.search.dynamic.profile.QueryProfileManager.actionPerformed().noDeletePermissionErrorMessage.message"),
                            I18N.getString("Sirius.navigator.search.dynamic.profile.QueryProfileManager.actionPerformed().noDeletePermissionErrorMessage.title"),
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    try
                    {
                        if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Delete Query: " + selectedQueryInfo + " GroupQueryInfo: " + selectedQueryInfo.getUserGroups().size());
                        
                        if(logger.isDebugEnabled())logger.debug("deleting query data, id: " + selectedQueryInfo.getID() + ", domain: " + selectedQueryInfo.getDomain());
                        SessionManager.getProxy().deleteQueryData(selectedQueryInfo.getID(), selectedQueryInfo.getDomain());
                        
                        updateQueryProfileManager();
                    }
                    catch(Exception exp)
                    {
                        exp.printStackTrace();
                    }
                }
            }
        }
        else
        {
            //_TA_JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst ein "+profileTypeName+" aus.", "Keine Selektion", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    I18N.getString("Sirius.navigator.search.dynamic.profile.QueryProfileManager.actionPerformed().noProfileSelectedErrorMessage.message"),
                    I18N.getString("Sirius.navigator.search.dynamic.profile.QueryProfileManager.actionPerformed().noProfileSelectedErrorMessage.title"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /** Setter for property searchDialog.
     * @param searchDialog New value of property searchDialog.
     *
     */
    public void setSearchDialog(SearchDialog searchDialog)
    {
        this.searchDialog = searchDialog;
    }
    
    public SearchDialog getSearchDialog()
    {
        return this.searchDialog;
    }
    
    /** Getter for property searchPropertiesBean.
     * @return Value of property searchPropertiesBean.
     *
     */
    /*public SearchPropertiesBean getSearchPropertiesBean()
    {
        return this.searchPropertiesBean;
    }*/
    
    /** Setter for property searchPropertiesBean.
     * @param searchPropertiesBean New value of property searchPropertiesBean.
     *
     */
    /*protected void setSearchPropertiesBean(SearchPropertiesBean searchPropertiesBean)
    {
        this.searchPropertiesBean = searchPropertiesBean;
    }*/
    
}
