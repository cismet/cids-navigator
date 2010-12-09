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
 * Created                      :       11.09.2000
 * History                      :
 *
 *******************************************************************************/
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.tree.*;

import Sirius.server.middleware.types.Node;
import Sirius.server.newuser.UserGroup;
import Sirius.server.search.store.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.zip.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class QueryResultProfileManager extends ProfileManager {

    //~ Instance fields --------------------------------------------------------

    protected SearchResultsTree searchTree;
    protected boolean newNodesLoaded = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * protected JMenu searchMenu;
     *
     * @param  dialog       DOCUMENT ME!
     * @param  searchTree   DOCUMENT ME!
     * @param  profileType  DOCUMENT ME!
     */
    public QueryResultProfileManager(final JDialog dialog, final SearchResultsTree searchTree, final int profileType) {
        super(dialog, profileType);
        this.searchTree = searchTree;
        this.setTitle(org.openide.util.NbBundle.getMessage(
                QueryResultProfileManager.class,
                "QueryResultProfileManager.title")); // NOI18N

        this.initQueryResultProfileManager();
    }

    /**
     * Creates a new QueryResultProfileManager object.
     *
     * @param  frame        DOCUMENT ME!
     * @param  searchTree   DOCUMENT ME!
     * @param  profileType  DOCUMENT ME!
     */
    public QueryResultProfileManager(final JFrame frame, final SearchResultsTree searchTree, final int profileType) {
        super(frame, profileType);
        this.searchTree = searchTree;
        this.setTitle(org.openide.util.NbBundle.getMessage(
                QueryResultProfileManager.class,
                "QueryResultProfileManager.title")); // NOI18N

        this.initQueryResultProfileManager();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initQueryResultProfileManager() {
        // JMenuBar menuBar = new JMenuBar();
        // searchMenu = new JMenu(this.getTitle());
        // menuBar.add(searchMenu);

        // this.setJMenuBar(menuBar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  searchTree  DOCUMENT ME!
     */
    public void setSearchTree(final SearchResultsTree searchTree) {
        this.searchTree = searchTree;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean newNodesLoaded() {
        return newNodesLoaded;
    }

    /**
     * DOCUMENT ME!
     */
    public void updateQueryResultProfileManager() {
        try {
            userInfo = this.getQueryInfos();
            if (logger.isDebugEnabled()) {
                logger.debug("updateQueryResultProfileManager(): userInfo.length: " + userInfo.length);          // NOI18N
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.updateQueryResultProfileManager().searchResultsWarning.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.updateQueryResultProfileManager().searchResultsWarning.title"),   // NOI18N
                JOptionPane.WARNING_MESSAGE);

            logger.error("could not load user info", exp); // NOI18N
        }

        updateProfileTree();
        // updateSearchMenu();

        buttonSave.setEnabled(canStoreQueryResult());
        buttonDelete.setEnabled(canStoreQueryResult());

        newNodesLoaded = false;
    }

    /**
     * private void updateSearchMenu() { if(userInfo != null & searchMenu != null) { this.searchMenu.removeAll();
     * for(int i = 0; i < userInfo.length; i++) { JMenuItem searchItem = new JMenuItem(userInfo[i].toString());
     * this.searchMenu.add(searchItem); } } }.
     *
     * @return  DOCUMENT ME!
     */
    protected boolean canStoreQueryResult() {
        // logger.debug("searchFlag: " + SessionManager.getSession().getUser().getUserGroup().getSearchFlag());
        // logger.debug("STOREQUERYRESULT: " + UserGroup.STOREQUERYRESULT); logger.debug("searchFlag & STOREUSERPROFILE:
        // " + (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() & UserGroup.STOREQUERYRESULT));

        // return true ? (SessionManager.getSession().getUser().getUserGroup().getSearchFlag() &
        // UserGroup.STOREQUERYRESULT) != 0 : false;

        // TODO implement this
        return true;
    }

    @Override
    public void show() {
        updateQueryResultProfileManager();
        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filename  DOCUMENT ME!
     */
    public void loadSearchResults(final String filename) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("loading query result'" + filename + "'"); // NOI18N
            }
            final String path = PropertyManager.getManager().getProfilesPath();

            final File inputFile = new File(path, filename);

            final FileInputStream fileInputStream = new FileInputStream(inputFile);

            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fileInputStream));
            searchTree.setResultNodes((Node[])ois.readObject());
            ois.close();

            updateQueryResultProfileManager();
            if (logger.isDebugEnabled()) {
                logger.debug("<SEARCH PROFILE> Load QueryResult"); // NOI18N
            }

            newNodesLoaded = true;
        } catch (Throwable t) {
            logger.error("Error while loading profiles:", t);                                        // NOI18N
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.loadSearchResults().loadSearchResultsError.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.loadSearchResults().loadSearchResultsError.title"),   // NOI18N
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final QueryInfo selectedQueryResultInfo = (QueryInfo)getSelectedInfo();
        if (logger.isDebugEnabled()) {
            logger.debug("selectedQueryResultInfo: '" + getSelectedInfo() + "'"); // NOI18N
        }

        if (e.getActionCommand().equals("close"))                                                                   // NOI18N
        {
            dispose();
        } else if (e.getActionCommand().equals("save"))                                                             // NOI18N
        {
            try {
                if (searchTree.getResultNodes() == null) {
                    JOptionPane.showMessageDialog(
                        this,
                        org.openide.util.NbBundle.getMessage(
                            QueryResultProfileManager.class,
                            "QueryResultProfileManager.actionPerformed(ActionEvent).searchResultsWarning.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            QueryResultProfileManager.class,
                            "QueryResultProfileManager.actionPerformed(ActionEvent).searchResultsWarning.title"),   // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (selectedQueryResultInfo != null) {
                    profileSaveDialog.show(selectedQueryResultInfo.getID(),
                        selectedQueryResultInfo.getName(),
                        false,
                        false);
                } else {
                    profileSaveDialog.show(false, false);
                }

                if (profileSaveDialog.isAccepted()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("saving query result as '" + this.profileSaveDialog.getSelectedProfileName()
                                    + "'"); // NOI18N
                    }
                    final File profilesPath = new File(PropertyManager.getManager().getProfilesPath());
                    if (profilesPath.exists() || profilesPath.mkdirs()) {
                        final File outputFile = new File(PropertyManager.getManager().getProfilesPath(),
                                this.profileSaveDialog.getSelectedProfileName());
                        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                        final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                                    fileOutputStream));
                        oos.writeObject(searchTree.getResultNodes());
                        oos.close();

                        updateQueryResultProfileManager();
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            org.openide.util.NbBundle.getMessage(
                                QueryResultProfileManager.class,
                                "QueryResultProfileManager.actionPerformed(ActionEvent).noDirError.message"), // NOI18N
                            org.openide.util.NbBundle.getMessage(
                                QueryResultProfileManager.class,
                                "QueryResultProfileManager.actionPerformed(ActionEvent).noDirError.title"), // NOI18N
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Throwable t) {
                // TODO display error dialog
                logger.error("Error while saving profiles:", t); // NOI18N
            }
        } else if (selectedQueryResultInfo != null) {
            if (e.getActionCommand().equals("load"))             // NOI18N
            {
                loadSearchResults(selectedQueryResultInfo.getFileName());
            } else if (e.getActionCommand().equals("delete"))    // NOI18N
            {
                if (logger.isDebugEnabled()) {
                    logger.debug("deleting query result'" + selectedQueryResultInfo.getFileName() + "'"); // NOI18N
                }
                try {
                    final File inputFile = new File(PropertyManager.getManager().getProfilesPath(),
                            selectedQueryResultInfo.getFileName());
                    inputFile.delete();
                } catch (Throwable t) {
                    logger.error("Error while deleting profiles:", t); // NOI18N
                    JOptionPane.showMessageDialog(
                        this,
                        org.openide.util.NbBundle.getMessage(
                            QueryResultProfileManager.class,
                            "QueryResultProfileManager.actionPerformed(ActionEvent).deleteError.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            QueryResultProfileManager.class,
                            "QueryResultProfileManager.actionPerformed(ActionEvent).deleteError.title"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                }

                updateQueryResultProfileManager();
            }
        } else {
            // _TA_JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst ein "+profileTypeName+" aus.", "Keine
            // Selektion", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.actionPerformed(ActionEvent).noProfileWarning.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    QueryResultProfileManager.class,
                    "QueryResultProfileManager.actionPerformed(ActionEvent).noProfileWarning.title"), // NOI18N
                JOptionPane.WARNING_MESSAGE);
        }
    }
    /**
     * FIXME quich hack, handle better later.
     *
     * @return  DOCUMENT ME!
     */
    private QueryInfo[] getQueryInfos() {
        try {
            final File profilesDir = new File(PropertyManager.getManager().getProfilesPath());
            final File[] profiles = profilesDir.listFiles(new FileFilter() {

                        @Override
                        public boolean accept(final File pathname) {
                            return !pathname.isDirectory();
                        }
                    });

            if (profiles != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(profiles.length + " profiles found in '"
                                + PropertyManager.getManager().getProfilesPath() + "'"); // NOI18N
                }
                final QueryInfo[] infos = new QueryInfo[profiles.length];
                for (int i = 0; i < profiles.length; i++) {
                    // omg
                    if (logger.isDebugEnabled()) {
                        logger.debug("filename: '" + profiles[i].getName() + "'"); // NOI18N
                    }
                    final QueryInfo info = new QueryInfo(
                            i,
                            profiles[i].getName(),
                            profiles[i].getName(),
                            profiles[i].getName());
                    infos[i] = info;
                }

                return infos;
            } else {
                logger.warn("no profiles found!");              // NOI18N
            }
        } catch (Exception exp) {
            logger.error("could not load search results", exp); // NOI18N
        }

        return new QueryInfo[0];
    }
}
