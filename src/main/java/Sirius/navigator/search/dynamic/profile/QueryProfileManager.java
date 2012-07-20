/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic.profile;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
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
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       05.09.2000
 * History                      :
 *
 *******************************************************************************/

//import Sirius.navigator.NavigatorLogger;
import Sirius.navigator.connection.*;
//import Sirius.navigator.Views.Tree.SearchTree;
//import Sirius.navigator.tools.*;
//import Sirius.navigator.ui.dialog.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.search.dynamic.*;


//import Sirius.server.search.query.*;
import Sirius.server.search.store.*;

import java.awt.event.*;

import java.beans.*;

import java.io.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class QueryProfileManager extends ProfileManager {

    //~ Instance fields --------------------------------------------------------

    // protected SearchDialog searchDialog;
    protected boolean groupFlag = false;
    protected boolean saveFlag = false;

    /** Holds value of property searchDialog. */
    private SearchDialog searchDialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * Holds value of property searchPropertiesBean.
     *
     * @param  dialog  DOCUMENT ME!
     */
    // private SearchPropertiesBean searchPropertiesBean;

    public QueryProfileManager(final JDialog dialog) {
        super(dialog, QUERY_PROFILE);

        // this.logger = Logger.getLogger(this.getClass());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * public QueryProfileManager() { super(new JFrame(), QUERY_PROFILE); }. public QueryProfileManager(JDialog dialog,
     * SearchDialog searchDialog, String profileTypeName) { super(dialog, profileTypeName); this.searchDialog =
     * searchDialog; } public QueryProfileManager(JFrame frame, SearchDialog searchDialog, String profileTypeName) {
     * super(frame, profileTypeName); this.searchDialog = searchDialog; }
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void updateQueryProfileManager() throws Exception {
        userInfo = (QueryInfo[])SessionManager.getProxy().getUserQueryInfos(SessionManager.getSession().getUser());
        userGroupInfo = (QueryInfo[])SessionManager.getProxy()
                    .getUserGroupQueryInfos(SessionManager.getSession().getUser().getUserGroup());

        updateProfileTree();

        // if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> User SearchFlag: " +
        // SessionManager.getSession().getUser().getUserGroup().getSearchFlag() + " User can store UserProfile: " +
        // canStoreUserProfile() + " User can store UserGroupProfile: " + canStoreUserGroupProfile());
        // if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> userGroupInfo: " + userGroupInfo);
        // if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> userInfo: " + userGroupInfo);

        // buttonSave.setEnabled(canStoreUserProfile());
        // buttonDelete.setEnabled(canStoreUserGroupProfile());

        buttonSave.setEnabled(canStoreUserProfile(userInfo) || canStoreUserGroupProfile(userGroupInfo));
        buttonDelete.setEnabled(canStoreUserProfile(userInfo) || canStoreUserGroupProfile(userGroupInfo));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   queryInfo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean canStoreUserProfile(final QueryInfo[] queryInfo) {
        // if(logger.isDebugEnabled())logger.debug("searchFlag: " +
        // SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        // if(logger.isDebugEnabled())logger.debug("STOREUSERPROFILE: " + UserGroup.STOREUSERPROFILE);
        // if(logger.isDebugEnabled())logger.debug("searchFlag & STOREUSERPROFILE: " +
        // (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREUSERPROFILE));

        // return SessionManager.getSession().getUser().getUserGroup().getSearchFlag() == UserGroup.STOREUSERPROFILE;

        /*if(queryInfo != null && queryInfo.length > 0)
         * { return queryInfo[0].getUserGroups().contains(SessionManager.getSession().getUser().getUserGroup()); }
         *
         *return false;*/

        // TODO implement this
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   queryInfo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean canStoreUserGroupProfile(final QueryInfo[] queryInfo) {
        // if(logger.isDebugEnabled())logger.debug("searchFlag: " +
        // SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        // if(logger.isDebugEnabled())logger.debug("STOREUSERPROFILE: " + UserGroup.STOREUSERPROFILE);
        // if(logger.isDebugEnabled())logger.debug("searchFlag & STOREUSERPROFILE: " +
        // (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREUSERPROFILE));

        // return true ? (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() &
        // UserGroup.STOREUSERGROUPPROFILE) != 0 : false;

        /*if(queryInfo != null && queryInfo.length > 0)
         * { return queryInfo[0].getUserGroups().contains(SessionManager.getSession().getUser().getUserGroup()); }
         *
         *return false;*/

        // TODO implement this
        return true;
    }

    @Override
    public void show() {
        try {
            updateQueryProfileManager();
            // NOTE: This call can not be substituted by StaticSwingTools.showDialog(this) because
            // show() method overwrites JDialog.show(). StaticSwingTools.showDialog() calls
            // setVisible(true) which internally calls JDialog show() -> endless recursion if
            // StaticSwingTools.showDialog() is called here
            super.show();
        } catch (Exception exp) {
            logger.error(exp.getMessage(), exp);
            ExceptionManager.getManager()
                    .showExceptionDialog(this, ExceptionManager.ERROR, "Exception", exp.getMessage(), exp); // NOI18N
        }
    }

    /*public void show(SearchPropertiesBean searchPropertiesBean)
     * { try {     this.setSearchPropertiesBean(searchPropertiesBean);          updateQueryProfileManager();
     * super.show(); } catch(Exception exp) {     logger.error(exp.getMessage(), exp);
     * ExceptionManager.getManager().showExceptionDialog(this, ExceptionManager.ERROR, "Exception", exp.getMessage(),
     * exp); }}*/

    // FIXME inner class
    @Override
    public void actionPerformed(final ActionEvent e) {
        final QueryInfo selectedQueryInfo = (QueryInfo)getSelectedInfo();

        if (e.getActionCommand().equals("close"))       // NOI18N
        {
            dispose();
        } else if (e.getActionCommand().equals("save")) // NOI18N
        {
            try {
                // searchDialog.checkInput();
                // searchDialog.updateSearchModel();

                if (selectedQueryInfo != null) {
                    profileSaveDialog.show(selectedQueryInfo.getID(),
                        selectedQueryInfo.getName(),
                        canStoreUserGroupProfile(new QueryInfo[] { selectedQueryInfo }),
                        selectedQueryInfo.getUserGroups().size()
                                > 0);
                } else {
                    profileSaveDialog.show(canStoreUserGroupProfile(userInfo), canStoreUserGroupProfile(userGroupInfo));
                }

                if (profileSaveDialog.isAccepted()) {
                    // System.out.println("profileSaveDialog.isAccepted(): " + profileSaveDialog.isAccepted());
                    // SiriusGZIPOutputStream gzos = new SiriusGZIPOutputStream(baos); GZIPOutputStream gzos = new
                    // GZIPOutputStream(baos); ObjectOutputStream oos = new ObjectOutputStream(gzos);

                    // ObjectOutputStream oos = new ObjectOutputStream(baos);
                    // oos.writeObject(searchDialog.getModel());

                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    final XMLEncoder xmlEncoder = new XMLEncoder(baos);
                    // xmlEncoder.writeObject(this.getSearchPropertiesBean());
                    xmlEncoder.writeObject(this.getSearchDialog().getSearchProperties());
                    xmlEncoder.close();

                    // Query query = new Query(profileSaveDialog.getSelectedProfileID(),
                    // SessionManager.getSession().getUser().getLocalServerName(),
                    // profileSaveDialog.getSelectedProfile(), baos.toByteArray(), !profileSaveDialog.isGroupProfile());

                    final QueryData queryData = new QueryData(profileSaveDialog.getSelectedProfileID(),
                            SessionManager.getSession().getUser().getDomain(),
                            profileSaveDialog.getSelectedProfileName(),
                            baos.toByteArray());

                    // = new QueryData(profileSaveDialog.getSelectedProfileID(),
                    // SessionManager.getSession().getUser().getLocalServerName(),
                    // profileSaveDialog.getSelectedProfile(), baos.toByteArray(), !profileSaveDialog.isGroupProfile());

                    // System.out.println("Query: " + query);

                    if (profileSaveDialog.isGroupProfile()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("<SEARCH PROFILE> Save UserGroupQuery key: "
                                        + SessionManager.getSession().getUser().getUserGroup().getKey().toString()); // NOI18N
                        }
                        queryData.addUserGroup(SessionManager.getSession().getUser().getUserGroup().getKey()
                                    .toString());
                    } else if (logger.isDebugEnabled()) {
                        logger.debug("<SEARCH PROFILE> Save UserQuery: " + queryData);                               // NOI18N
                    }

                    SessionManager.getProxy().storeQueryData(SessionManager.getSession().getUser(), queryData);
                    updateQueryProfileManager();

                    /*if(profileSaveDialog.getSelectedProfileID() == -1)
                     * { } else { if(profileSaveDialog.isGroupProfile()) {
                     * if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Save UserGroupQuery: Overwrite " +
                     * query);     SessionManager.getProxy().updateQuery(query); } else {
                     * if(logger.isDebugEnabled())logger.debug("<SEARCH PROFILE> Save UserQuery: Overwrite " + query);
                     *   SessionManager.getProxy().updateQuery(query); }}*/
                } else if (logger.isDebugEnabled()) {
                    logger.debug("<SEARCH PROFILE> Saving canceled"); // NOI18N
                }
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
                ExceptionManager.getManager()
                        .showExceptionDialog(this, ExceptionManager.ERROR, "Exception", t.getMessage(), t); // NOI18N
            }
        } else if (selectedQueryInfo != null) {
            if (e.getActionCommand().equals("load"))                  // NOI18N
            {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("loading query data, id: " + selectedQueryInfo.getID() + ", domain: "
                                    + selectedQueryInfo.getDomain()); // NOI18N
                    }
                    final QueryData queryData = SessionManager.getProxy()
                                .getQueryData(selectedQueryInfo.getID(), selectedQueryInfo.getDomain());

                    final XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(queryData.getData()));
                    final Object result = xmlDecoder.readObject();
                    xmlDecoder.close();

                    if (logger.isDebugEnabled()) {
                        logger.debug("<SEARCH PROFILE> Load Query: " + result + " GroupQueryInfo: "
                                    + selectedQueryInfo.getUserGroups().size() + "(" + selectedQueryInfo + ")"); // NOI18N
                    }

                    // this.setSearchPropertiesBean((SearchPropertiesBean)result);

                    this.getSearchDialog().setSearchProperties((SearchPropertiesBean)result);

                    // SiriusGZIPInputStream gzos = new SiriusGZIPInputStream(baos);
                    // GZIPInputStream gzos = new GZIPInputStream(baos);
                    // ObjectInputStream oos = new ObjectInputStream(gzos);
                    // ObjectInputStream oos = new ObjectInputStream(baos);

                    // searchDialog.setModel((SearchModel)oos.readObject());

                } catch (Throwable t) {
                    logger.error("error loading stored beand data:", t);                                        // NOI18N
                    ExceptionManager.getManager()
                            .showExceptionDialog(this, ExceptionManager.ERROR, "Exception", t.getMessage(), t); // NOI18N
                }

                this.dispose();
            } else if (e.getActionCommand().equals("delete")) // NOI18N
            {
                if ((selectedQueryInfo.getUserGroups().size() > 0)
                            && !canStoreUserGroupProfile(new QueryInfo[] { selectedQueryInfo })) {
                    // _TA_JOptionPane.showMessageDialog(this, "Sie haben nicht die Berechtigung ein
                    // Benutzergruppenprofil zu loeschen", "Profil loeschen", JOptionPane.WARNING_MESSAGE);
                    JOptionPane.showMessageDialog(
                        this,
                        org.openide.util.NbBundle.getMessage(
                            QueryProfileManager.class,
                            "QueryProfileManager.actionPerformed().noDeletePermissionErrorMessage.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            QueryProfileManager.class,
                            "QueryProfileManager.actionPerformed().noDeletePermissionErrorMessage.title"), // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug("<SEARCH PROFILE> Delete Query: " + selectedQueryInfo + " GroupQueryInfo: "
                                        + selectedQueryInfo.getUserGroups().size());                       // NOI18N
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("deleting query data, id: " + selectedQueryInfo.getID() + ", domain: "
                                        + selectedQueryInfo.getDomain()); // NOI18N
                        }
                        SessionManager.getProxy()
                                .deleteQueryData(selectedQueryInfo.getID(), selectedQueryInfo.getDomain());

                        updateQueryProfileManager();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }
        } else {
            // _TA_JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst ein "+profileTypeName+" aus.", "Keine
            // Selektion", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    QueryProfileManager.class,
                    "QueryProfileManager.actionPerformed().noProfileSelectedErrorMessage.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    QueryProfileManager.class,
                    "QueryProfileManager.actionPerformed().noProfileSelectedErrorMessage.title"), // NOI18N
                JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Setter for property searchDialog.
     *
     * @param  searchDialog  New value of property searchDialog.
     */
    public void setSearchDialog(final SearchDialog searchDialog) {
        this.searchDialog = searchDialog;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchDialog getSearchDialog() {
        return this.searchDialog;
    }

    /** Getter for property searchPropertiesBean.
     * @return Value of property searchPropertiesBean.
     *
     */
    /*public SearchPropertiesBean getSearchPropertiesBean()
     * { return this.searchPropertiesBean;}*/

    /** Setter for property searchPropertiesBean.
     * @param searchPropertiesBean New value of property searchPropertiesBean.
     *
     */
    /*protected void setSearchPropertiesBean(SearchPropertiesBean searchPropertiesBean)
     * { this.searchPropertiesBean = searchPropertiesBean;}*/

}
