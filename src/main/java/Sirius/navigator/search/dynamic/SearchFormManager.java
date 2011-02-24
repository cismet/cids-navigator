/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic;

import Sirius.server.search.SearchOption;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.event.ItemListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SearchFormManager extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    protected final Logger logger;

    protected final Map searchOptionsMap;
    protected final Map searchFormsMap;

    protected ItemListener searchFormSelectionListener;

    protected java.util.List searchFormContainers;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane searchFormContainerPane;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchContainer.
     *
     * @param  searchOptionsMap  DOCUMENT ME!
     */
    public SearchFormManager(final Map searchOptionsMap) {
        this.logger = Logger.getLogger(this.getClass());

        this.searchOptionsMap = searchOptionsMap;
        if (logger.isDebugEnabled()) {
            logger.debug("SearchFormManager initilized with searchOptions :" + searchOptionsMap); // NOI18N
        }

        this.searchFormsMap = new HashMap();

        initComponents();
    }

    /**
     * Creates new form SearchContainer.
     *
     * @param  searchOptionsMap      DOCUMENT ME!
     * @param  searchFormContainers  DOCUMENT ME!
     */
    public SearchFormManager(final Map searchOptionsMap, final java.util.List searchFormContainers) {
        this(searchOptionsMap);

        this.setSearchFormContainers(searchFormContainers);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  searchFormContainers  DOCUMENT ME!
     */
    protected void setSearchFormContainers(final java.util.List searchFormContainers) {
        if ((this.searchFormContainers != null) && (this.searchFormContainers.size() > 0)) {
            if (logger.isDebugEnabled()) {
                logger.debug("removing " + this.searchFormContainerPane.getTabCount() + " search categories"); // NOI18N
            }

            this.searchFormContainers.clear();
            this.searchFormsMap.clear();
            this.searchFormContainerPane.removeAll();
        }

        this.searchFormContainers = searchFormContainers;

        if (logger.isInfoEnabled()) {
            logger.info("adding " + this.searchFormContainers.size() + " searchFormContainer"); // NOI18N
        }
        final Iterator iterator = searchFormContainers.iterator();
        while (iterator.hasNext()) {
            final SearchFormContainer searchFormContainer = (SearchFormContainer)iterator.next();
            if (searchFormContainer.isVisible()) {
                if (this.searchFormSelectionListener != null) {
                    searchFormContainer.addSearchFormSelectionListener(this.searchFormSelectionListener);
                }

                this.searchFormsMap.putAll(searchFormContainer.getSearchFormsMap());
                this.searchFormContainerPane.addTab(searchFormContainer.getName(),
                    searchFormContainer.getFormContainer());
            } else if (logger.isDebugEnabled()) {
                logger.warn("ignoring invisible search form container '" + searchFormContainer.getName() + "'"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public java.util.List getSearchFormContainers() {
        return this.searchFormContainers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchFormContainer getActiveSearchFormContainer() {
        return (SearchFormContainer)this.searchFormContainers.get(this.searchFormContainerPane.getSelectedIndex());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     * @param  error    DOCUMENT ME!
     */
    private void setStatus(final String message, final boolean error) {
        if (SwingUtilities.isEventDispatchThread()) {
            this.statusLabel.setForeground(error ? Color.RED : Color.BLUE);
            this.statusLabel.setText(message);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        setStatus(message, error);
                    }
                });
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Reset all forms to default values.
     */
    public void resetAllForms() {
        Iterator iterator = this.getSearchForms().iterator();
        while (iterator.hasNext()) {
            ((SearchForm)iterator.next()).resetForm();
        }

        iterator = this.getSearchFormContainers().iterator();
        while (iterator.hasNext()) {
            ((SearchFormContainer)iterator.next()).setSelectedSearchFormIndex(0);
        }

        this.searchFormContainerPane.setSelectedIndex(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   formId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchForm getSearchForm(final String formId) {
        if (this.searchFormsMap.containsKey(formId)) {
            return (SearchForm)this.searchFormsMap.get(formId);
        } else {
            logger.error("search form id '" + formId + "' not found in search forms map"); // NOI18N
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Collection getSearchForms() {
        return this.searchFormsMap.values();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   queryId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchOption getSearchOption(final String queryId) {
        if (this.searchOptionsMap.containsKey(queryId)) {
            return (SearchOption)this.searchOptionsMap.get(queryId);
        } else {
            logger.error("search query id '" + queryId + "' not found in search options map"); // NOI18N
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Collection getSearchOptions() {
        return this.searchOptionsMap.values();
    }

    /**
     * Creates and return a *new* hashmap of all available form data beans.
     *
     * @return  DOCUMENT ME!
     */
    public HashMap getFormDataBeans() {
        final HashMap dataBeans = new HashMap(this.searchFormsMap.size());
        final Iterator iterator = this.getSearchForms().iterator();

        while (iterator.hasNext()) {
            final FormDataBean dataBean = ((SearchForm)iterator.next()).getDataBean();

            try {
                dataBeans.put(dataBean.getQueryId(), dataBean.clone());
            } catch (CloneNotSupportedException cnsexp) {
                logger.warn("could not clone form data bean '" + dataBean.getQueryId() + "'", cnsexp); // NOI18N
            }
        }

        return dataBeans;
    }

    /**
     * Returns a list of all selected search forms (enabled and disabled).
     *
     * <p>Warning: global search forms are ignored.</p>
     *
     * @return  a list of all selected search forms
     */
    protected java.util.List getSelectedSearchForms() {
        final LinkedList selectedSearchForms = new LinkedList();

        final Iterator iterator = this.getSearchForms().iterator();
        while (iterator.hasNext()) {
            final SearchForm searchForm = (SearchForm)iterator.next();
            if (searchForm.isSelected()) {
                selectedSearchForms.add(searchForm);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("returning " + selectedSearchForms.size() + " search forms"); // NOI18N
        }
        return selectedSearchForms;
    }

    /**
     * Returns a list of form data object of all selected search forms that are enabled.
     *
     * <p>Before returning the data, this method synchronizes the data in the form with the data stored in the data bean
     * object with a call to <code>writeFormParameters()</code>.<br>
     * Warning: global search forms are ignored.</p>
     *
     * @return  a list of FormDataBean objects
     *
     * @throws  FormValidationException  DOCUMENT ME!
     *
     * @thows   FormValidationException by <code>writeFormParameters()</code>
     */
    protected java.util.List getSelectedFormData() throws FormValidationException {
        final LinkedList formDataList = new LinkedList();

        final Iterator iterator = this.getSelectedSearchForms().iterator();
        while (iterator.hasNext()) {
            final SearchForm searchForm = (SearchForm)iterator.next();
            if (searchForm.isEnabled()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("reading data of form '" + searchForm.getName() + "'"); // NOI18N
                }

                searchForm.writeFormParameters();
                formDataList.add(searchForm.getDataBean());
            } else if (logger.isDebugEnabled()) {
                logger.debug(searchForm.getName() + " is disabled"); // NOI18N
            }
        }

        return formDataList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  formDataList  DOCUMENT ME!
     */
    protected void setSelectedFormData(final java.util.List formDataList)       // throws FormValidationException
    {
        if (logger.isDebugEnabled()) {
            logger.debug("loading data for " + formDataList.size() + " forms"); // NOI18N
        }

        final Iterator iterator = formDataList.iterator();
        while (iterator.hasNext()) {
            final FormDataBean dataBean = (FormDataBean)iterator.next();
            final SearchForm searchForm = this.getSearchForm(dataBean.getFormId());

            if (searchForm != null) {
                searchForm.setDataBean(dataBean);
                searchForm.readBeanParameters();

                // select the search form (find the right container first)
                final Iterator sfcIterator = this.getSearchFormContainers().iterator();
                while (sfcIterator.hasNext()) {
                    final SearchFormContainer searchFormContainer = (SearchFormContainer)sfcIterator.next();
                    if (searchFormContainer.setSelectedSearchForm(searchForm.getFormId())) {
                        this.searchFormContainerPane.setSelectedIndex(this.searchFormContainerPane.indexOfTab(
                                searchFormContainer.getName()));
                        break;
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classNodeKeys  DOCUMENT ME!
     * @param  userGroups     DOCUMENT ME!
     */
    protected void setSearchFormsEnabled(final Collection classNodeKeys, final Collection userGroups) {
        final Iterator iterator = this.getSearchForms().iterator();
        while (iterator.hasNext()) {
            final SearchForm searchForm = (SearchForm)iterator.next();
            this.setSearchFormEnabled(searchForm, classNodeKeys, userGroups);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  searchForm     DOCUMENT ME!
     * @param  classNodeKeys  DOCUMENT ME!
     * @param  userGroups     DOCUMENT ME!
     */
    protected void setSearchFormEnabled(final SearchForm searchForm,
            final Collection classNodeKeys,
            final Collection userGroups) {
        final SearchOption searchOption = this.getSearchOption(searchForm.getQueryId());

        if (searchOption != null) {
            searchForm.setEnabled(searchOption.isSelectable(classNodeKeys, userGroups));
        } else {
            logger.warn("no search option found, disabling search form '" + searchForm.getName() + "'"); // NOI18N
            searchForm.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  searchFormSelectionListener  DOCUMENT ME!
     */
    protected void addSearchFormSelectionListener(final ItemListener searchFormSelectionListener) {
        this.searchFormSelectionListener = searchFormSelectionListener;

        if ((this.getSearchFormContainers() != null) && (this.getSearchFormContainers().size() > 0)) {
            final Iterator iterator = this.getSearchFormContainers().iterator();

            while (iterator.hasNext()) {
                final SearchFormContainer searchFormContainer = (SearchFormContainer)iterator.next();
                searchFormContainer.addSearchFormSelectionListener(searchFormSelectionListener);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        statusLabel = new javax.swing.JLabel();
        searchFormContainerPane = new javax.swing.JTabbedPane();

        statusLabel.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText(org.openide.util.NbBundle.getMessage(
                SearchFormManager.class,
                "SearchFormManager.statuslabel.text"));          // NOI18N
        statusLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED),
                javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 2)));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout(5, 5));

        searchFormContainerPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        add(searchFormContainerPane, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
}
