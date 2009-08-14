/*
 * SearchCategoryPanel.java
 *
 * Created on 25. September 2003, 17:36
 */

package Sirius.navigator.search.dynamic;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.MutablePanel;
import Sirius.navigator.resource.ResourceManager;

/**
 *
 *
 * @author  pascal
 */
public class DefaultSearchFormContainer extends javax.swing.JPanel implements SearchFormContainer
{
    protected final Logger logger;
    protected final LinkedHashMap searchFormsMap;
    protected final MutablePanel container;

    protected SearchForm globalSearchForm = null;
    protected ResourceBundle resourceBundle = null;
      
    public DefaultSearchFormContainer()
    {
        this.logger = Logger.getLogger(this.getClass());
        this.searchFormsMap = new LinkedHashMap();
        this.container = new MutablePanel(this, "n/a");
        
        initComponents();
        
        this.globalSearchFormContainer.setPreferredSize(new Dimension(0,0));
        this.globalSearchFormContainer.setVisible(false);
        this.addSearchFormSelectionListener(new SearchFormSelectionListener());
    }
    
    /** Creates new form SearchCategoryPanel */
    public DefaultSearchFormContainer(String name, Collection searchForms, SearchForm globalSearchForm, Locale locale)
    {
        this();
        
        //this.logger = Logger.getLogger(this.getClass());
        //this.searchFormsMap = new LinkedHashMap();
        //this.container = new MutablePanel(this, "n/a");
         
        //initComponents();
        
        this.setName(name);
        this.setSearchForms(searchForms);
        this.setGlobalSearchForm(globalSearchForm);
        this.setLocale(locale);
        
        //this.addSearchFormSelectionListener(new SearchFormSelectionListener());
    }
    
    public void intFormContainer() throws FormInitializationException
    {
        logger.debug("initalizing searchFormContainer '" + this.getName() + "'");
        if(this.getResourceBundle() != null)
        {
            try
            {
                this.internationalize(this.getResourceBundle());
                
                //this.searchFormContainer.add(noFormPanel, noFormPanel.getName());
            }
            catch(MissingResourceException mrexp)
            {
                // TODO more info
                throw new FormInitializationException();
            }
            
            
        }
        else
        {
            logger.warn("i18n not supported by form container '" + this.getName() + "'");
        }
    }    

    public SearchForm getGlobalSearchForm()
    {
        return this.globalSearchForm;
    }
    
    public void setGlobalSearchForm(SearchForm globalSearchForm)
    {
        this.globalSearchForm = globalSearchForm;
        this.globalSearchFormContainer.removeAll();

        if(globalSearchForm != null)
        {
            this.globalSearchFormContainer.add(globalSearchForm.getForm());
            this.globalSearchFormContainer.setVisible(true);
        }
        else
        {
            this.globalSearchFormContainer.setPreferredSize(new Dimension(0,0));
            this.globalSearchFormContainer.setVisible(false);
        }
    }
    
    public SearchForm getSearchForm(String formId)
    {
        if(this.searchFormsMap.containsKey(formId))
        {
            return (SearchForm)this.searchFormsMap.get(formId);
        }
        else
        {
            logger.warn("search form '" + formId + "' not found");
            return null;
        }
    }   
    
    public void setSearchForms(Collection searchForms)
    { 
        this.searchFormsMap.clear();
        this.searchFormSelectionBox.removeAllItems();
        this.searchFormContainer.removeAll();

        this.searchFormContainer.add(this.noFormPanel.getName(), this.noFormPanel);
        this.searchFormSelectionBox.addItem(this.noFormPanel.getName());

        if(searchForms != null)
        {
            if(logger.isDebugEnabled())logger.debug("adding " + searchForms.size() + " SearchForms");

            Iterator iterator = searchForms.iterator();
            while(iterator.hasNext())
            {
                SearchForm searchForm = (SearchForm)iterator.next();
                if(searchForm.isVisible())
                {
                    this.addSearchForm(searchForm);
                }
                else if(logger.isDebugEnabled())
                {
                    logger.warn("ignoring invisible search form '" + searchForm.getName() + "'");
                }
            }
        }

        this.searchFormSelectionBox.setSelectedIndex(0);
    }
    
    public boolean isEnabled()
    {
        return super.isEnabled();
    }
    
    public void setEnabled(boolean enabled)
    {
        this.container.setEnabled(enabled);
        super.setEnabled(enabled);
    }
    
    public String getName()
    {
        return super.getName();
    }
    
    public void setName(String name)
    {
        super.setName(name);
    }
    
    public boolean isSearchFormSelected()
    {
        return this.searchFormSelectionBox.getSelectedIndex() > 0;
    }

    public SearchForm getSelectedSearchForm()
    {
        if(this.isSearchFormSelected())
        {
            return (SearchForm)this.searchFormSelectionBox.getSelectedItem();
        }
        else
        {
            logger.warn("no search form selected (" + this.searchFormSelectionBox.getSelectedIndex() + ")");
            return null;
        }
    }    
    
    public int getSelectedSearchFormIndex()
    {
        return this.searchFormSelectionBox.getSelectedIndex();
    }    
    
    public boolean setSelectedSearchForm(String formId)
    {
        if(this.searchFormsMap.containsKey(formId))
        {
            this.searchFormSelectionBox.setSelectedItem(this.searchFormsMap.get(formId));
            return true;
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("search form '" + formId + "' not found");
            return false;
        }
    }
    
    public boolean setSelectedSearchFormIndex(int index)
    {
        if(index < this.searchFormSelectionBox.getItemCount())
        {
            this.searchFormSelectionBox.setSelectedIndex(index);
            return true;
        }
        
        return false;
    }
    
    public Map getSearchFormsMap()
    {
        return this.searchFormsMap;
    }  
    
    public Collection getSearchForms()
    {
        return this.searchFormsMap.values();
    } 

    protected void internationalize(ResourceBundle resourceBundle) throws MissingResourceException
    {
        if(this.resourceBundle != null)
        {
            try{this.setName(resourceBundle.getString("container.name"));}catch(Throwable t){/*egal*/}
            this.noFormPanel.setName(resourceBundle.getString("noform.title") + this.getName());
            this.noFormLabel.setText(resourceBundle.getString("noform.message") + this.getName());
        }
        else       
        {
            if(logger.isDebugEnabled())logger.error("i18n falied: property 'resourceBundle' is null");
            throw new MissingResourceException("i18n falied: property 'resourceBundle' is null", this.getClass().getName(), "resourceBundle");
        }
        
    }
    
    public String toString()
    {
        return this.getName();
    }
    
    public javax.swing.JComponent getFormContainer()
    {
        return this.container;
    }
    
    public void addSearchFormSelectionListener(ItemListener searchFormSelectionListener)
    {
        this.searchFormSelectionBox.addItemListener(searchFormSelectionListener);
    }
    
    // .........................................................................
    
    protected void addSearchForm(SearchForm searchForm)
    {
        if(logger.isDebugEnabled())logger.debug("initializing & adding SearchForm '" + searchForm.getName() + "' (formId: " + searchForm.getFormId() + " queryId: " + searchForm.getQueryId() + ")");
        
        try
        {
            searchForm.initForm();
            
            this.searchFormsMap.put(searchForm.getFormId(), searchForm);
            this.searchFormSelectionBox.addItem(searchForm);
            this.searchFormContainer.add(searchForm.getName(), searchForm.getForm());
        }
        catch(FormInitializationException fiexp)
        {
            logger.error("could not initialize form '" + searchForm.getName() + "', form not added", fiexp);
        }
    }
    
    public java.util.ResourceBundle getResourceBundle()
    {
        return this.resourceBundle;
    }
    
    public void setResourceBundle(java.util.ResourceBundle resourceBundle)
    {
        if(logger.isDebugEnabled())logger.debug("setting resource bundle for locale locale to '" + resourceBundle.getLocale().toString() + "'");
        this.resourceBundle = resourceBundle;
    }    
    
    // -------------------------------------------------------------------------
    
    protected class SearchFormSelectionListener implements ItemListener 
    {
        public void itemStateChanged(ItemEvent e)
        {
            if(e.getStateChange() == ItemEvent.DESELECTED)
            {
                if(logger.isDebugEnabled())logger.debug("search form '" + e.getItem().toString() + "' deselected");
                if(e.getItem() instanceof SearchForm)
                {
                    ((SearchForm)e.getItem()).setSelected(false);
                }
            }
            else if(e.getStateChange() == ItemEvent.SELECTED)
            {
                if(logger.isDebugEnabled())logger.debug("search form '" + e.getItem().toString() + "' selected ["+searchFormSelectionBox.getSelectedIndex()+ "]");
                if(searchFormSelectionBox.getSelectedIndex() > 0 && e.getItem() instanceof SearchForm)
                {
                    ((SearchForm)e.getItem()).setSelected(true);
                }
                
                ((CardLayout)DefaultSearchFormContainer.this.searchFormContainer.getLayout()).show(DefaultSearchFormContainer.this.searchFormContainer, e.getItem().toString());
            }
        } 
    }
    
    /*protected class GERMAN extends ListResourceBundle
    {
        protected Object[][] contents =
        {
            {"defaultName", "DefaultSearchFormContainer"},
            {"noFormTitle", "DefaultSearchFormContainer"},
            {"noFormMessage", "DefaultSearchFormContainer"}    
        };
        
        protected Object[][] getContents()
        {
            return this.contents;
        }    
    }*/
    
    // #########################################################################
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        noFormPanel = new javax.swing.JPanel();
        noFormLabel = new javax.swing.JLabel();
        searchFormSelectionBox = new javax.swing.JComboBox();
        searchFormContainer = new javax.swing.JPanel();
        globalSearchFormContainer = new javax.swing.JPanel();

        noFormPanel.setLayout(new java.awt.BorderLayout());

        noFormPanel.setName("");
        noFormLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noFormLabel.setText("no form selected");
        noFormPanel.add(noFormLabel, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout(0, 5));

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        setPreferredSize(new java.awt.Dimension(40, 73));
        add(searchFormSelectionBox, java.awt.BorderLayout.NORTH);

        searchFormContainer.setLayout(new java.awt.CardLayout());

        searchFormContainer.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5))));
        add(searchFormContainer, java.awt.BorderLayout.CENTER);

        globalSearchFormContainer.setLayout(new java.awt.GridLayout(1, 1));

        globalSearchFormContainer.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5))));
        globalSearchFormContainer.setMinimumSize(new java.awt.Dimension(0, 0));
        globalSearchFormContainer.setEnabled(false);
        add(globalSearchFormContainer, java.awt.BorderLayout.SOUTH);

    }
    // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel globalSearchFormContainer;
    private javax.swing.JLabel noFormLabel;
    private javax.swing.JPanel noFormPanel;
    private javax.swing.JPanel searchFormContainer;
    private javax.swing.JComboBox searchFormSelectionBox;
    // End of variables declaration//GEN-END:variables
    
    
    /*public static void main(String args[])
    {
        org.apache.log4j.BasicConfigurator.configure();
        
        DefaultSearchFormContainer dsf = new DefaultSearchFormContainer();

        JFrame jf = new JFrame("DefaultSearchFormContainer");
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(dsf.getFormContainer(), BorderLayout.CENTER);
         
        jf.pack();
        
        Collection searchForms = new LinkedList();
        searchForms.add(new DefaultSearchForm(new Query()));
        dsf.setSearchForms(searchForms);
        
        //jf.setSize(320,240);
        jf.setVisible(true);
    }*/
}
