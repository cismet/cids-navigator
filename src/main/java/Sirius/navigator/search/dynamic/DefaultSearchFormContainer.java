/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchCategoryPanel.java
 *
 * Created on 25. September 2003, 17:36
 */
package Sirius.navigator.search.dynamic;

import Sirius.navigator.ui.widget.MutablePanel;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultSearchFormContainer extends javax.swing.JPanel implements SearchFormContainer {

    //~ Instance fields --------------------------------------------------------

    protected final Logger logger;
    protected final LinkedHashMap searchFormsMap;
    protected final MutablePanel container;

    protected SearchForm globalSearchForm = null;
    protected ResourceBundle resourceBundle = null;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel globalSearchFormContainer;
    private javax.swing.JLabel noFormLabel;
    private javax.swing.JPanel noFormPanel;
    private javax.swing.JPanel searchFormContainer;
    private javax.swing.JComboBox searchFormSelectionBox;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultSearchFormContainer object.
     */
    public DefaultSearchFormContainer() {
        this.logger = Logger.getLogger(this.getClass());
        this.searchFormsMap = new LinkedHashMap();
        this.container = new MutablePanel(
                this,
                org.openide.util.NbBundle.getMessage(
                    DefaultSearchFormContainer.class,
                    "DefaultSearchFormContainer.container.disabledMessage")); // NOI18N

        initComponents();

        this.globalSearchFormContainer.setPreferredSize(new Dimension(0, 0));
        this.globalSearchFormContainer.setVisible(false);
        this.addSearchFormSelectionListener(new SearchFormSelectionListener());
    }

    /**
     * Creates new form SearchCategoryPanel.
     *
     * @param  name              DOCUMENT ME!
     * @param  searchForms       DOCUMENT ME!
     * @param  globalSearchForm  DOCUMENT ME!
     * @param  locale            DOCUMENT ME!
     */
    public DefaultSearchFormContainer(final String name,
            final Collection searchForms,
            final SearchForm globalSearchForm,
            final Locale locale) {
        this();

        // this.logger = Logger.getLogger(this.getClass());
        // this.searchFormsMap = new LinkedHashMap();
        // this.container = new MutablePanel(this, "n/a");

        // initComponents();

        this.setName(name);
        this.setSearchForms(searchForms);
        this.setGlobalSearchForm(globalSearchForm);
        this.setLocale(locale);

        // this.addSearchFormSelectionListener(new SearchFormSelectionListener());
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void intFormContainer() throws FormInitializationException {
        if (logger.isDebugEnabled()) {
            logger.debug("initalizing searchFormContainer '" + this.getName() + "'"); // NOI18N
        }
        if (this.getResourceBundle() != null) {
            try {
                this.internationalize(this.getResourceBundle());

                // this.searchFormContainer.add(noFormPanel, noFormPanel.getName());
            } catch (MissingResourceException mrexp) {
                // TODO more info
                throw new FormInitializationException();
            }
        } else {
            logger.warn("i18n not supported by form container '" + this.getName() + "'"); // NOI18N
        }
    }

    @Override
    public SearchForm getGlobalSearchForm() {
        return this.globalSearchForm;
    }

    @Override
    public void setGlobalSearchForm(final SearchForm globalSearchForm) {
        this.globalSearchForm = globalSearchForm;
        this.globalSearchFormContainer.removeAll();

        if (globalSearchForm != null) {
            this.globalSearchFormContainer.add(globalSearchForm.getForm());
            this.globalSearchFormContainer.setVisible(true);
        } else {
            this.globalSearchFormContainer.setPreferredSize(new Dimension(0, 0));
            this.globalSearchFormContainer.setVisible(false);
        }
    }

    @Override
    public SearchForm getSearchForm(final String formId) {
        if (this.searchFormsMap.containsKey(formId)) {
            return (SearchForm)this.searchFormsMap.get(formId);
        } else {
            logger.warn("search form '" + formId + "' not found"); // NOI18N
            return null;
        }
    }

    @Override
    public void setSearchForms(final Collection searchForms) {
        this.searchFormsMap.clear();
        this.searchFormSelectionBox.removeAllItems();
        this.searchFormContainer.removeAll();

        this.searchFormContainer.add(this.noFormPanel.getName(), this.noFormPanel);
        this.searchFormSelectionBox.addItem(this.noFormPanel.getName());

        if (searchForms != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("adding " + searchForms.size() + " SearchForms"); // NOI18N
            }

            final Iterator iterator = searchForms.iterator();
            while (iterator.hasNext()) {
                final SearchForm searchForm = (SearchForm)iterator.next();
                if (searchForm.isVisible()) {
                    this.addSearchForm(searchForm);
                } else if (logger.isDebugEnabled()) {
                    logger.warn("ignoring invisible search form '" + searchForm.getName() + "'"); // NOI18N
                }
            }
        }

        this.searchFormSelectionBox.setSelectedIndex(0);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.container.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    @Override
    public boolean isSearchFormSelected() {
        return this.searchFormSelectionBox.getSelectedIndex() > 0;
    }

    @Override
    public SearchForm getSelectedSearchForm() {
        if (this.isSearchFormSelected()) {
            return (SearchForm)this.searchFormSelectionBox.getSelectedItem();
        } else {
            logger.warn("no search form selected (" + this.searchFormSelectionBox.getSelectedIndex() + ")"); // NOI18N
            return null;
        }
    }

    @Override
    public int getSelectedSearchFormIndex() {
        return this.searchFormSelectionBox.getSelectedIndex();
    }

    @Override
    public boolean setSelectedSearchForm(final String formId) {
        if (this.searchFormsMap.containsKey(formId)) {
            this.searchFormSelectionBox.setSelectedItem(this.searchFormsMap.get(formId));
            return true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("search form '" + formId + "' not found"); // NOI18N
            }
            return false;
        }
    }

    @Override
    public boolean setSelectedSearchFormIndex(final int index) {
        if (index < this.searchFormSelectionBox.getItemCount()) {
            this.searchFormSelectionBox.setSelectedIndex(index);
            return true;
        }

        return false;
    }

    @Override
    public Map getSearchFormsMap() {
        return this.searchFormsMap;
    }

    @Override
    public Collection getSearchForms() {
        return this.searchFormsMap.values();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       resourceBundle  DOCUMENT ME!
     *
     * @throws      MissingResourceException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    protected void internationalize(final ResourceBundle resourceBundle) throws MissingResourceException {
        if (this.resourceBundle != null) {
            try {
                this.setName(resourceBundle.getString("container.name"));
            } catch (Throwable t) { /*egal*/
            }
            this.noFormPanel.setName(resourceBundle.getString("noform.title") + this.getName());
            this.noFormLabel.setText(resourceBundle.getString("noform.message") + this.getName());
        } else {
            if (logger.isDebugEnabled()) {
                logger.error("i18n falied: property 'resourceBundle' is null"); // NOI18N
            }
            throw new MissingResourceException(
                "i18n falied: property 'resourceBundle' is null",
                this.getClass().getName(),
                "resourceBundle");  // NOI18N
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public javax.swing.JComponent getFormContainer() {
        return this.container;
    }

    @Override
    public void addSearchFormSelectionListener(final ItemListener searchFormSelectionListener) {
        this.searchFormSelectionBox.addItemListener(searchFormSelectionListener);
    }

    /**
     * .........................................................................
     *
     * @param  searchForm  DOCUMENT ME!
     */
    protected void addSearchForm(final SearchForm searchForm) {
        if (logger.isDebugEnabled()) {
            logger.debug("initializing & adding SearchForm '" + searchForm.getName() + "' (formId: "
                        + searchForm.getFormId() + " queryId: " + searchForm.getQueryId() + ")"); // NOI18N
        }

        try {
            searchForm.initForm();

            this.searchFormsMap.put(searchForm.getFormId(), searchForm);
            this.searchFormSelectionBox.addItem(searchForm);
            this.searchFormContainer.add(searchForm.getName(), searchForm.getForm());
        } catch (FormInitializationException fiexp) {
            logger.error("could not initialize form '" + searchForm.getName() + "', form not added", fiexp); // NOI18N
        }
    }

    @Override
    public java.util.ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    @Override
    public void setResourceBundle(final java.util.ResourceBundle resourceBundle) {
        if (logger.isDebugEnabled()) {
            logger.debug("setting resource bundle for locale locale to '" + resourceBundle.getLocale().toString()
                        + "'"); // NOI18N
        }
        this.resourceBundle = resourceBundle;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        noFormPanel = new javax.swing.JPanel();
        noFormLabel = new javax.swing.JLabel();
        searchFormSelectionBox = new javax.swing.JComboBox();
        searchFormContainer = new javax.swing.JPanel();
        globalSearchFormContainer = new javax.swing.JPanel();

        noFormPanel.setName(""); // NOI18N
        noFormPanel.setLayout(new java.awt.BorderLayout());

        noFormLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noFormLabel.setText(org.openide.util.NbBundle.getMessage(
                DefaultSearchFormContainer.class,
                "DefaultSearchFormContainer.noFormLabel.text")); // NOI18N
        noFormPanel.add(noFormLabel, java.awt.BorderLayout.CENTER);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new java.awt.Dimension(40, 73));
        setLayout(new java.awt.BorderLayout(0, 5));
        add(searchFormSelectionBox, java.awt.BorderLayout.NORTH);

        searchFormContainer.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEtchedBorder(),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchFormContainer.setLayout(new java.awt.CardLayout());
        add(searchFormContainer, java.awt.BorderLayout.CENTER);

        globalSearchFormContainer.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEtchedBorder(),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        globalSearchFormContainer.setEnabled(false);
        globalSearchFormContainer.setMinimumSize(new java.awt.Dimension(0, 0));
        globalSearchFormContainer.setLayout(new java.awt.GridLayout(1, 1));
        add(globalSearchFormContainer, java.awt.BorderLayout.SOUTH);
    } // </editor-fold>//GEN-END:initComponents

    //~ Inner Classes ----------------------------------------------------------

    /**
     * -------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    protected class SearchFormSelectionListener implements ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (logger.isDebugEnabled()) {
                    logger.debug("search form '" + e.getItem().toString() + "' deselected"); // NOI18N
                }
                if (e.getItem() instanceof SearchForm) {
                    ((SearchForm)e.getItem()).setSelected(false);
                }
            } else if (e.getStateChange() == ItemEvent.SELECTED) {
                if (logger.isDebugEnabled()) {
                    logger.debug("search form '" + e.getItem().toString() + "' selected ["
                                + searchFormSelectionBox.getSelectedIndex() + "]");          // NOI18N
                }
                if ((searchFormSelectionBox.getSelectedIndex() > 0) && (e.getItem() instanceof SearchForm)) {
                    ((SearchForm)e.getItem()).setSelected(true);
                }

                ((CardLayout)DefaultSearchFormContainer.this.searchFormContainer.getLayout()).show(
                    DefaultSearchFormContainer.this.searchFormContainer,
                    e.getItem().toString());
            }
        }
    }

    /*protected class GERMAN extends ListResourceBundle
     * { protected Object[][] contents = {     {"defaultName", "DefaultSearchFormContainer"},     {"noFormTitle",
     * "DefaultSearchFormContainer"},     {"noFormMessage", "DefaultSearchFormContainer"}     };  protected Object[][]
     * getContents() {     return this.contents; }    }*/

    // #########################################################################

    /*public static void main(String args[])
     * { org.apache.log4j.BasicConfigurator.configure();  DefaultSearchFormContainer dsf = new
     * DefaultSearchFormContainer();
     *
     * JFrame jf = new JFrame("DefaultSearchFormContainer"); jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
     * jf.setLocationRelativeTo(null); jf.getContentPane().setLayout(new BorderLayout());
     * jf.getContentPane().add(dsf.getFormContainer(), BorderLayout.CENTER);   jf.pack();  Collection searchForms = new
     * LinkedList(); searchForms.add(new DefaultSearchForm(new Query())); dsf.setSearchForms(searchForms);
     * //jf.setSize(320,240); jf.setVisible(true);}*/
}
