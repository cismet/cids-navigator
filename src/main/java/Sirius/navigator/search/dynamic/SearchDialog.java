/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchDialog.java
 *
 * Created on 18. November 2003, 10:18
 */
package Sirius.navigator.search.dynamic;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.*;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.resource.*;
import Sirius.navigator.search.dynamic.profile.QueryProfileManager;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.status.*;
import Sirius.navigator.ui.tree.*;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SearchDialog extends javax.swing.JDialog implements StatusChangeSupport {

    //~ Instance fields --------------------------------------------------------

    private final Logger logger;

    private final SearchSelectionTree searchSelectionTree;
    private final SearchFormManager searchFormManager;

    private final SearchProgressDialog searchProgressDialog;
    private final QueryProfileManager queryProfileManager;

    private final DefaultStatusChangeSupport statusChangeSupport;
    private final ResourceManager resources;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem appendResultsItem;
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenuItem manageProfilesItem;
    private javax.swing.JPanel managerPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchDialog.
     *
     * @param  parent            DOCUMENT ME!
     * @param  searchOptionsMap  DOCUMENT ME!
     * @param  classNodes        DOCUMENT ME!
     */
    public SearchDialog(final java.awt.Frame parent, final Map searchOptionsMap, final Node[] classNodes) {
        // super(parent, ResourceManager.getManager().getString("search.dialog.title"), true);

        // nicht modal
        super(parent, org.openide.util.NbBundle.getMessage(SearchDialog.class, "SearchDialog.title"), false); // NOI18N

        this.logger = Logger.getLogger(this.getClass());
        if (logger.isInfoEnabled()) {
            logger.info("creating SearchDialog with " + searchOptionsMap.size() + " queries & " + classNodes.length
                        + " class nodes"); // NOI18N
        }
        this.statusChangeSupport = new DefaultStatusChangeSupport(this);
        this.resources = ResourceManager.getManager();

        this.searchSelectionTree = new SearchSelectionTree(classNodes);
        this.searchFormManager = new SearchFormManager(searchOptionsMap);

        this.queryProfileManager = new QueryProfileManager(this);
        this.queryProfileManager.setSearchDialog(this);
        this.searchProgressDialog = new SearchProgressDialog(this, this.statusChangeSupport);

        initComponents(); // ...................................................
        // this.searchSelectionTree.setPreferredSize(new Dimension(195,320));
        // this.searchFormManager.setPreferredSize(new Dimension(400,320));
        // this.searchSelectionTree.setMaximumSize(new Dimension(195,320));
        // this.splitPane.setLeftComponent(new JScrollPane(this.searchSelectionTree));
        // this.splitPane.setRightComponent(this.searchFormManager);
        this.treeScrollPane.setViewportView(this.searchSelectionTree);
        this.managerPanel.add(this.searchFormManager, BorderLayout.CENTER);
        // .....................................................................

        this.searchFormManager.addSearchFormSelectionListener(new SearchFormSelectionListener());
        this.searchSelectionTree.addMouseListener(new SearchSelectionListener());

        final ActionListener actionListener = new ButtonListener();
        this.searchButton.addActionListener(actionListener);
        this.resetButton.addActionListener(actionListener);
        this.cancelButton.addActionListener(actionListener);
        this.manageProfilesItem.addActionListener(actionListener);
        // this.loadProfileItem.addActionListener(actionListener);

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // dynamic search form initialization
        this.loadSearchForms(); // .............................................
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * .........................................................................
     *
     * @param  formDataBean  DOCUMENT ME!
     */
    public void search(final FormDataBean formDataBean) {
        this.search(formDataBean, this, this.appendResultsItem.isSelected());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  formDataBean         DOCUMENT ME!
     * @param  owner                DOCUMENT ME!
     * @param  appendSearchResults  DOCUMENT ME!
     */
    public void search(final FormDataBean formDataBean, final Component owner, final boolean appendSearchResults) {
        try {
            final SearchResult searchResult = this.getSearchResult(formDataBean, owner);

            if ((searchResult != null) && searchResult.isNode() && (searchResult.getNodes().length > 0)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("showing search results in search results tree"); // NOI18N
                }
                // XXX event w\u00E4re besser ...
                MethodManager.getManager().showSearchResults(searchResult.getNodes(), appendSearchResults);
                SearchDialog.this.dispose();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.warn("could not show search results in search results tree: no result available"); // NOI18N
                }
                JOptionPane.showMessageDialog(
                    this,
                    org.openide.util.NbBundle.getMessage(
                        SearchDialog.class,
                        "SearchDialog.search(FormDataBean,Component,boolean).noresultsErrorDialog.message"),  // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        SearchDialog.class,
                        "SearchDialog.search(FormDataBean,Component,boolean).noresultsErrorDialog.title"),    // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exp) {
            logger.fatal("could not show search results", exp);                                               // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(
                            SearchDialog.class,
                            "SearchDialog.search(FormDataBean,Component,boolean).name"),                      // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            SearchDialog.class,
                            "SearchDialog.search(FormDataBean,Component,boolean).message"),                   // NOI18N
                        exp);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classNodeKeys   DOCUMENT ME!
     * @param  searchFormData  DOCUMENT ME!
     */
    public void search(final Collection classNodeKeys, final Collection searchFormData) {
        this.search(classNodeKeys, searchFormData, this, this.appendResultsItem.isSelected());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classNodeKeys        DOCUMENT ME!
     * @param  searchFormData       DOCUMENT ME!
     * @param  owner                DOCUMENT ME!
     * @param  appendSearchResults  DOCUMENT ME!
     */
    public void search(final Collection classNodeKeys,
            final Collection searchFormData,
            final Component owner,
            final boolean appendSearchResults) {
        try {
            final SearchResult searchResult = this.getSearchResult(classNodeKeys, searchFormData, owner);

            if ((searchResult != null) && searchResult.isNode() && (searchResult.getNodes().length > 0)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("showing search results in search results tree"); // NOI18N
                }
                // XXX event w\u00E4re besser ...
                MethodManager.getManager().showSearchResults(searchResult.getNodes(), appendSearchResults);
                SearchDialog.this.dispose();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.warn("could not show search results in search results tree: no result available");         // NOI18N
                }
                JOptionPane.showMessageDialog(
                    this,
                    org.openide.util.NbBundle.getMessage(
                        SearchDialog.class,
                        "SearchDialog.search(Collection,Collection,Component,boolean).noresultsErrorDialog.message"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        SearchDialog.class,
                        "SearchDialog.search(Collection,Collection,Component,boolean).noresultsErrorDialog.title"),   // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exp) {
            logger.fatal("could not show search results", exp);                                                       // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(
                            SearchDialog.class,
                            "SearchDialog.search(Collection,Collection,Component,boolean).name"),                     // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            SearchDialog.class,
                            "SearchDialog.search(Collection,Collection,Component,boolean).message"),                  // NOI18N
                        exp);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   classNodeKeys   DOCUMENT ME!
     * @param   searchFormData  DOCUMENT ME!
     * @param   owner           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchResult getSearchResult(final Collection classNodeKeys,
            final Collection searchFormData,
            final Component owner) {
        try {
            final java.util.List searchOptions = this.fillSearchOptions(searchFormData);

            if (this.checkCompleteness(classNodeKeys, searchOptions)) {
                this.searchProgressDialog.pack();
                this.searchProgressDialog.setLocationRelativeTo(owner);
                this.searchProgressDialog.show(classNodeKeys, searchOptions);

                if (!this.searchProgressDialog.isCanceld()) {
                    return this.searchProgressDialog.getSearchResult();
                } else if (logger.isDebugEnabled()) {
                    logger.debug("search canceld, don't do anything"); // NOI18N
                }
            } else {
                logger.warn("could not perform search: incomplete data"); // NOI18N
            }
        } catch (FormValidationException fvexp) {
            this.handleFormValidationException(fvexp);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   formDataBean  DOCUMENT ME!
     * @param   owner         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchResult getSearchResult(final FormDataBean formDataBean, final Component owner) {
        try {
            final LinkedList classNodeKeys = new LinkedList();
            final LinkedList searchFormData = new LinkedList();

            searchFormData.add(formDataBean);
            final Collection searchOptions = this.fillSearchOptions(searchFormData);

            if (this.checkCompleteness(searchOptions)) {
                this.searchProgressDialog.pack();
                this.searchProgressDialog.setLocationRelativeTo(owner);
                this.searchProgressDialog.show(classNodeKeys, searchOptions);

                if (!this.searchProgressDialog.isCanceld()) {
                    return this.searchProgressDialog.getSearchResult();
                } else if (logger.isDebugEnabled()) {
                    logger.debug("search canceld, don't do anything"); // NOI18N
                }
            } else {
                logger.warn("could not perform search: incomplete data"); // NOI18N
            }
        } catch (FormValidationException fvexp) {
            this.handleFormValidationException(fvexp);
        }

        return null;
    }

    /*public Node[] search(java.util.List classNodeKeys, java.util.List searchFormData, Component owner, boolean
     * appendSearchResults) { try {     java.util.List searchOptions = this.fillSearchOptions(searchFormData);
     * if(this.checkCompleteness(classNodeKeys, searchOptions))     {
     * this.searchProgressDialog.setLocationRelativeTo(owner);         this.searchProgressDialog.show(classNodeKeys,
     * searchOptions);
     *
     * if(!this.searchProgressDialog.isCanceld())         { if(logger.isDebugEnabled())logger.debug("returning search
     * result nodes");             return this.searchProgressDialog.getResultNodes(); SearchDialog.this.dispose();  }
     *  else if(logger.isDebugEnabled())         { logger.debug("search canceld, don't do anything"); } }     else  {
     *      logger.warn("could not perform search: incomplete data");     } }
     * catch(FormValidationException fvexp) {     this.handleFormValidationException(fvexp); }  return null;}*/

    /**
     * Copies data from form data beans into search options (query parameters).
     *
     * @param   searchFormData  DOCUMENT ME!
     *
     * @return  a list with all selected search options (queries)
     *
     * @throws  FormValidationException  DOCUMENT ME!
     *
     * @thows   FormValidationException by <code>getSelectedFormData()</code>
     */
    private java.util.List fillSearchOptions(final Collection searchFormData) throws FormValidationException {
        // java.util.List searchFormData = this.getSelectedFormData();
        final LinkedList searchOptionsList = new LinkedList();

        if (logger.isDebugEnabled()) {
            logger.debug("filling " + searchFormData.size() + " search option objects with data"); // NOI18N
        }
        final Iterator iterator = searchFormData.iterator();
        while (iterator.hasNext()) {
            final FormDataBean dataBean = (FormDataBean)iterator.next();
            // SearchOption searchOption = (SearchOption)this.searchOptionsMap.get();

            if (logger.isDebugEnabled()) {
                logger.info("filling search option '" + dataBean.getQueryId() + "' with data"); // NOI18N
                logger.debug(dataBean.toString());
            }

            final SearchOption searchOption = this.searchFormManager.getSearchOption(dataBean.getQueryId());
            if (searchOption == null) {
                throw new FormValidationException(dataBean.getFormId(),
                    org.openide.util.NbBundle.getMessage(
                        SearchDialog.class,
                        "SearchDialog.fillSearchOptions(Collection).FormValidationException.message", // NOI18N
                        new Object[] { dataBean.getQueryId() }));
            }

            try {
                final Iterator parameterIterator = searchOption.getParameterNames();
                while (parameterIterator.hasNext()) {
                    final String parameterName = parameterIterator.next().toString();
                    final Object parameterValue = dataBean.getQueryParameter(parameterName);

                    if (logger.isDebugEnabled()) {
                        logger.debug("parameterName: '" + parameterName + "' parameterValue: '" + parameterValue + "'"); // NOI18N
                    }
                    searchOption.setDefaultSearchParameter(parameterName, parameterValue);
                }

                searchOptionsList.add(searchOption);
            } catch (FormValidationException fexp) {
                throw fexp;
            } catch (Exception exp) {
                logger.error("could not set query parameters", exp); // NOI18N

                // TODO more info
                throw new FormValidationException("could not set query parameters", exp.getMessage(), exp.getMessage()); // NOI18N
            }
        }

        return searchOptionsList;
    }

    /**
     * .........................................................................
     */
    private void loadSearchForms() {
        logger.info("loading dynamic search categories & forms ..."); // NOI18N

        final SearchContext searchContext = new SearchContext(this);
        final SearchFormFactory formFactory = new SearchFormFactory();

        try {
            final java.util.List searchCategories = formFactory.createSearchForms(PropertyManager.getManager()
                            .getSearchFormPath(),
                    "search.xml",
                    searchContext);                                                                                      // NOI18N
            logger.info(searchCategories.size() + " search categories loaded");                                          // NOI18N
            this.searchFormManager.setSearchFormContainers(searchCategories);
        } catch (Exception exp) {
            logger.fatal("could create dynmaic search categories & forms", exp);                                         // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(SearchDialog.class, "SearchDialog.loadSearchForms().name"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            SearchDialog.class,
                            "SearchDialog.loadSearchForms().message"),                                                   // NOI18N
                        exp);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void showQueryProfilesManager() {
        if (!this.isShowing()) {
            this.show();
        }

        this.queryProfileManager.setLocationRelativeTo(SearchDialog.this);
        this.queryProfileManager.show();
        this.queryProfileManager.toFront();
    }

    /**
     * Load search properties und update the dialog.
     *
     * @param  searchProperties  DOCUMENT ME!
     */
    public void setSearchProperties(final SearchPropertiesBean searchProperties) {
        logger.info("loading search properties"); // NOI18N
        final Collection userGroups = new LinkedList();
        userGroups.add(SessionManager.getSession().getUser().getUserGroup().getKey());

        this.searchSelectionTree.setSelectedClassNodeKeys(searchProperties.getClassNodeKeys());
        this.searchFormManager.resetAllForms();
        this.searchFormManager.setSelectedFormData(searchProperties.getFormDataBeans());
        this.appendResultsItem.setSelected(searchProperties.isAppendSearchResults());
        this.searchFormManager.setSearchFormsEnabled(searchProperties.getClassNodeKeys(), userGroups);

        this.validate();
        this.repaint();
    }

    /**
     * Save all properties.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  FormValidationException  DOCUMENT ME!
     */
    public SearchPropertiesBean getSearchProperties() throws FormValidationException {
        logger.info("saving search properties"); // NOI18N

        final SearchPropertiesBean searchProperties = new SearchPropertiesBean();
        searchProperties.setFormDataBeans(this.searchFormManager.getSelectedFormData());
        searchProperties.setClassNodeKeys(this.searchSelectionTree.getSelectedClassNodeKeys());
        searchProperties.setAppendSearchResults(this.appendResultsItem.isSelected());

        return searchProperties;
    }

    /**
     * -------------------------------------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public SearchProgressDialog getSearchProgressDialog() {
        return this.searchProgressDialog;
    }

    @Override
    public void addStatusChangeListener(final StatusChangeListener listener) {
        this.statusChangeSupport.addStatusChangeListener(listener);
    }

    @Override
    public void removeStatusChangeListener(final StatusChangeListener listener) {
        this.statusChangeSupport.removeStatusChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchFormManager getSearchFormManager() {
        return this.searchFormManager;
    }

    /**
     * =========================================================================.
     *
     * @param  formValidationException  DOCUMENT ME!
     */
    private void handleFormValidationException(final FormValidationException formValidationException) {
        logger.error(formValidationException.getMessage(), formValidationException);
        JOptionPane.showMessageDialog(
            this,
            formValidationException.getMessage(),
            formValidationException.getFormName(),
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   selectedSearchOptions  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean checkCompleteness(final Collection selectedSearchOptions) {
        if ((selectedSearchOptions != null) && (selectedSearchOptions.size() == 0)) {
            // XXX show error dialog
            logger.warn("incomplete search data: no formData available");                // NOI18N
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    SearchDialog.class,
                    "SearchDialog.checkCompleteness(Collection).messageDialog.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    SearchDialog.class,
                    "SearchDialog.checkCompleteness(Collection).messageDialog.title"),   // NOI18N
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   selectedClassNodeKeys  DOCUMENT ME!
     * @param   selectedSearchOptions  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean checkCompleteness(final Collection selectedClassNodeKeys, final Collection selectedSearchOptions) {
        if ((selectedClassNodeKeys != null) && (selectedClassNodeKeys.size() == 0)) {
            logger.warn("incomplete search data: no class nodes selected");                         // NOI18N
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(
                    SearchDialog.class,
                    "SearchDialog.checkCompleteness(Collection,Collection).messageDialog.message"), // NOI18N
                org.openide.util.NbBundle.getMessage(
                    SearchDialog.class,
                    "SearchDialog.checkCompleteness(Collection,Collection).messageDialog.title"),   // NOI18N
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return this.checkCompleteness(selectedSearchOptions);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JPanel contentPanel = new javax.swing.JPanel();
        final javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane();
        treeScrollPane = new javax.swing.JScrollPane();
        managerPanel = new javax.swing.JPanel();
        final javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        final javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        final javax.swing.JMenu profilesMenu = new javax.swing.JMenu();
        manageProfilesItem = new javax.swing.JMenuItem();
        final javax.swing.JMenu optionsMenu = new javax.swing.JMenu();
        appendResultsItem = new javax.swing.JCheckBoxMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    closeDialog(evt);
                }
            });

        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        contentPanel.setLayout(new java.awt.BorderLayout());

        treeScrollPane.setPreferredSize(new java.awt.Dimension(200, 320));
        splitPane.setLeftComponent(treeScrollPane);

        managerPanel.setPreferredSize(new java.awt.Dimension(400, 320));
        managerPanel.setLayout(new java.awt.BorderLayout());
        splitPane.setRightComponent(managerPanel);

        contentPanel.add(splitPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        searchButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.searchButton.mnemonic").charAt(0));
        searchButton.setText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.searchButton.text"));    // NOI18N
        searchButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.searchButton.tooltip")); // NOI18N
        searchButton.setActionCommand("search");       // NOI18N
        buttonPanel.add(searchButton);

        cancelButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.cancelButton.mnemonic").charAt(0));
        cancelButton.setText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.cancelButton.text"));    // NOI18N
        cancelButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.cancelButton.tooltip")); // NOI18N
        cancelButton.setActionCommand("cancel");       // NOI18N
        buttonPanel.add(cancelButton);

        resetButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.resetButton.mnemonic").charAt(0));
        resetButton.setText(org.openide.util.NbBundle.getMessage(SearchDialog.class, "SearchDialog.resetButton.text")); // NOI18N
        resetButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.resetButton.tooltip"));                                                                   // NOI18N
        resetButton.setActionCommand("reset");                                                                          // NOI18N
        buttonPanel.add(resetButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        profilesMenu.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.profilesMenu.mnemonic").charAt(0));
        profilesMenu.setText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.profilesMenu.text")); // NOI18N

        manageProfilesItem.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.profilesMenu.manageProfilesItem.mnemonic").charAt(0));
        manageProfilesItem.setText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.profilesMenu.manageProfilesItem.text")); // NOI18N
        manageProfilesItem.setActionCommand("manageProfiles");
        profilesMenu.add(manageProfilesItem);

        menuBar.add(profilesMenu);

        optionsMenu.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.optionsMenu.mnemonic").charAt(0));
        optionsMenu.setText(org.openide.util.NbBundle.getMessage(SearchDialog.class, "SearchDialog.optionsMenu.text")); // NOI18N

        appendResultsItem.setMnemonic(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.optionsMenu.appendResultsItem.mnemonic").charAt(0));
        appendResultsItem.setText(org.openide.util.NbBundle.getMessage(
                SearchDialog.class,
                "SearchDialog.optionsMenu.appendResultsItem.text")); // NOI18N
        optionsMenu.add(appendResultsItem);

        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog.
     *
     * @param  evt  DOCUMENT ME!
     */
    private void closeDialog(final java.awt.event.WindowEvent evt) //GEN-FIRST:event_closeDialog
    {
        setVisible(false);
        dispose();
    }                                                              //GEN-LAST:event_closeDialog

    //~ Inner Classes ----------------------------------------------------------

    /**
     * =========================================================================.
     *
     * @version  $Revision$, $Date$
     */
    private class ButtonListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing action '" + e.getActionCommand() + "'"); // NOI18N
            }

            if (e.getActionCommand().equals("search")) // NOI18N
            {
                try {
                    final java.util.List selectedClassNodeKeys = SearchDialog.this.searchSelectionTree
                                .getSelectedClassNodeKeys();
                    final java.util.List selectedSearchFormData = SearchDialog.this.searchFormManager
                                .getSelectedFormData();
                    // java.util.List selectedSearchOptions =
                    // SearchDialog.this.searchFormManager.getSelectedSearchOptions();

                    SearchDialog.this.search(selectedClassNodeKeys, selectedSearchFormData);
                } catch (FormValidationException fvexp) {
                    SearchDialog.this.handleFormValidationException(fvexp);
                }
            } else if (e.getActionCommand().equals("reset")) // NOI18N
            {
                // rest dialog

                SearchDialog.this.searchSelectionTree.deselectAllNodes();

                SearchDialog.this.searchFormManager.resetAllForms();
            } else if (e.getActionCommand().equals("cancel")) // NOI18N
            {
                // close dialog
                SearchDialog.this.dispose();
            } else if (e.getActionCommand().equals("manageProfiles")) // NOI18N
            {
                // show profiles manager
                SearchDialog.this.showQueryProfilesManager();

                /*try
                 * { SearchPropertiesBean searchPropertiesBean = SearchDialog.this.getSearchProperties();
                 * if(SearchDialog.this.checkCompleteness(searchPropertiesBean.getClassNodeKeys(),
                 * searchPropertiesBean.getFormDataBeans())) {     logger.info("saving search properties bean ...");
                 * SearchDialog.this.queryProfileManager.setLocationRelativeTo(SearchDialog.this);
                 * SearchDialog.this.queryProfileManager.show(); } } catch(FormValidationException fvexp) {
                 * SearchDialog.this.handleFormValidationException(fvexp);}*/

            }
            /*else if(e.getActionCommand().equals("loadProfile"))
             * { SearchDialog.this.queryProfileManager.setLocationRelativeTo(SearchDialog.this);
             * SearchDialog.this.queryProfileManager.show();  SearchPropertiesBean searchProperties =
             * SearchDialog.this.queryProfileManager.getSearchPropertiesBean(); if(searchProperties != null) {
             * SearchDialog.this.setSearchProperties(searchProperties); } else if(logger.isDebugEnabled()) {
             * logger.debug("no search properties bean loaded"); }}*/
        }
    }

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    private class SearchSelectionListener extends MouseAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() == 1) {
                final TreePath path = searchSelectionTree.getPathForLocation(e.getX(), e.getY());

                if (path != null) {
                    final DefaultMetaTreeNode node = (DefaultMetaTreeNode)path.getLastPathComponent();
                    if (logger.isDebugEnabled()) {
                        logger.debug("selecting theme (and all subthemes) '" + node + "'"); // NOI18N
                    }
                    // logger.debug("node.isSelected(): " + node + "  " + node.isSelected());
                    node.selectSubtree(!node.isSelected());
                    // logger.debug("node.isSelected(): " + node + "  " + node.isSelected());
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting search forms enabled"); // NOI18N
                    }
                    final Collection userGroups = new LinkedList();
                    userGroups.add(SessionManager.getSession().getUser().getUserGroup().getKey());

                    SearchDialog.this.searchFormManager.setSearchFormsEnabled(SearchDialog.this.searchSelectionTree
                                .getSelectedClassNodeKeys(),
                        userGroups);

                    // searchSelectionTree.revalidate();
                    // searchSelectionTree.repaint();

                    SearchDialog.this.validate();
                    SearchDialog.this.repaint();
                } else if (logger.isDebugEnabled()) {
                    logger.debug("no class tree nodes selected"); // NOI18N
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SearchFormSelectionListener implements ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem() instanceof SearchForm) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting search form enabled"); // NOI18N
                    }
                    final Collection userGroups = new LinkedList();
                    userGroups.add(SessionManager.getSession().getUser().getUserGroup().getKey());

                    SearchDialog.this.searchFormManager.setSearchFormEnabled((SearchForm)e.getItem(),
                        SearchDialog.this.searchSelectionTree.getSelectedClassNodeKeys(),
                        userGroups);
                }
            }
        }
    }

    // #########################################################################

    // =========================================================================

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[])
     * {
     * org.apache.log4j.PropertyConfigurator.configure(ClassLoader.getSystemResource("Sirius/Navigator/resource/cfg/log4j.debug.properties"));
     * //PropertyManager.getManager().configure(System.getProperty("user.home") + "\\.navigator\\",
     * "D:\\work\\web\\Sirius\\navigator\\plugins\\", "D:\\work\\web\\Sirius\\navigator\\search\\", null,
     * System.getProperty("user.home") + "\\.navigator\\navigator.cfg");
     * PropertyManager.getManager().configure(System.getProperty("user.home") + "\\.navigator\\navigator.cfg",
     * System.getProperty("user.home") + "\\.navigator\\", "D:\\cids\\web\\navigator\\plugins\\",
     * "D:\\cids\\web\\navigator\\search\\", null);  SearchDialog searchDialog = new SearchDialog(new
     * javax.swing.JFrame(), new HashMap(), new Node[0]); searchDialog.pack(); searchDialog.setLocationRelativeTo(null);
     * searchDialog .show(); System.exit(0);}*/
}
