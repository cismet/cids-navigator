/*
 * SearchContainer.java
 *
 * Created on 9. Oktober 2003, 10:09
 */

package Sirius.navigator.search.dynamic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;


import org.apache.log4j.Logger;

import Sirius.server.search.*;
import Sirius.navigator.search.*;

/**
 *
 * @author  pascal
 */
public class SearchFormManager extends javax.swing.JPanel
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    protected final Logger logger;

    protected final Map searchOptionsMap;
    protected final Map searchFormsMap;
    
    protected ItemListener searchFormSelectionListener;
    
    
    protected java.util.List searchFormContainers;
    
    /** Creates new form SearchContainer */
    public SearchFormManager(Map searchOptionsMap)
    {
        this.logger = Logger.getLogger(this.getClass());
        
       
        this.searchOptionsMap = searchOptionsMap;
        
        logger.debug("SearchFormManager initilized with searchOptions :" +searchOptionsMap);
        
        this.searchFormsMap = new HashMap();
        
        /*if(logger.isDebugEnabled())
        {
            logger.info("searchOptionsMap entries (" + this.searchOptionsMap.size() + "): ");

            Iterator iterator = this.searchOptionsMap.keySet().iterator();
            while(iterator.hasNext())
            {
                String queryId = iterator.next().toString();
                StringBuffer buffer = new StringBuffer("\nquery id: ").append(queryId);
                
                SearchOption searchOption = this.getSearchOption(queryId);
                Iterator parameterIterator = searchOption.getParameterNames();
                while(parameterIterator.hasNext())
                {
                    buffer.append("\nparameter name: ");
                    buffer.append(parameterIterator.next());
                }
                
                buffer.append('\n');
                logger.debug(buffer);
            } 
        }*/
        
        initComponents();
    }
    
    /** Creates new form SearchContainer */
    public SearchFormManager(Map searchOptionsMap, java.util.List searchFormContainers)
    {
        this(searchOptionsMap);
        
        this.setSearchFormContainers(searchFormContainers);
    }
    
    protected void setSearchFormContainers(java.util.List searchFormContainers)
    {
        if(this.searchFormContainers != null && this.searchFormContainers.size() > 0)
        {
            if(logger.isDebugEnabled())logger.debug("removing " + this.searchFormContainerPane.getTabCount() + " search categories");
            
            this.searchFormContainers.clear();
            this.searchFormsMap.clear();
            this.searchFormContainerPane.removeAll();
        }
        
        this.searchFormContainers = searchFormContainers;
        //this.searchFormContainers.addAll(searchFormContainers);

        logger.info("adding " + this.searchFormContainers.size() + " searchFormContainer");
        Iterator iterator = searchFormContainers.iterator();
        while(iterator.hasNext())
        {
            SearchFormContainer searchFormContainer = (SearchFormContainer)iterator.next();
            if(searchFormContainer.isVisible())
            {
                if(this.searchFormSelectionListener != null)
                {
                    searchFormContainer.addSearchFormSelectionListener(this.searchFormSelectionListener);
                }

                this.searchFormsMap.putAll(searchFormContainer.getSearchFormsMap()); 
                this.searchFormContainerPane.addTab(searchFormContainer.getName(), searchFormContainer.getFormContainer()); 
            } 
            else if(logger.isDebugEnabled())
            {
                logger.warn("ignoring invisible search form container '" + searchFormContainer.getName() + "'");
            }
        }
    }
    
    /**
     * 
     */
    public java.util.List getSearchFormContainers()
    {
        return this.searchFormContainers;
    }
    

    public SearchFormContainer getActiveSearchFormContainer()
    {
        return (SearchFormContainer)this.searchFormContainers.get(this.searchFormContainerPane.getSelectedIndex());
    }
    
    private void setStatus(final String message, final boolean error)
    {
        if(SwingUtilities.isEventDispatchThread())
        {
            this.statusLabel.setForeground(error ? Color.RED : Color.BLUE);
            this.statusLabel.setText(message);
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    setStatus(message, error);
                }
            });
        }
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Reset all forms to default values
     */
    public void resetAllForms()
    {
        Iterator iterator = this.getSearchForms().iterator();
        while(iterator.hasNext())
        {
            ((SearchForm)iterator.next()).resetForm();
        }
        
        iterator = this.getSearchFormContainers().iterator();
        while(iterator.hasNext())
        {
            ((SearchFormContainer)iterator.next()).setSelectedSearchFormIndex(0);
        }
        
        this.searchFormContainerPane.setSelectedIndex(0);
        
    }
    
    public SearchForm getSearchForm(String formId)
    {
        if(this.searchFormsMap.containsKey(formId))
        {
            return (SearchForm)this.searchFormsMap.get(formId);
        }
        else
        {
            logger.error("search form id '" + formId + "' not found in search forms map");
            return null;
        }
    }
    
    public Collection getSearchForms()
    {
        return this.searchFormsMap.values();
    }
    
    public SearchOption getSearchOption(String queryId)
    {
        if(this.searchOptionsMap.containsKey(queryId))
        {
            return (SearchOption)this.searchOptionsMap.get(queryId);
        }
        else
        {
            logger.error("search query id '" + queryId + "' not found in search options map");
            return null;
        }
    }
    
    public Collection getSearchOptions()
    {
        return this.searchOptionsMap.values();
    }
    
    /**
     * Creates and return a *new* hashmap of all available form data beans.
     */
    public HashMap getFormDataBeans()
    {
        HashMap dataBeans = new HashMap(this.searchFormsMap.size());
        Iterator iterator = this.getSearchForms().iterator();
        
        while(iterator.hasNext())
        {
            FormDataBean dataBean = ((SearchForm)iterator.next()).getDataBean();
            
            try
            {
                dataBeans.put(dataBean.getQueryId(), dataBean.clone());
            }
            catch(CloneNotSupportedException cnsexp)
            {
                logger.warn("could not clone form data bean '" + dataBean.getQueryId() + "'", cnsexp);
            }
        }
        
        return dataBeans;
    }
    
    /**
     * Returns a list of all selected search forms (enabled and disabled).<p>
     * Warning: global search forms are ignored.
     *
     * @return a list of all selected search forms
     */
    protected java.util.List getSelectedSearchForms()
    {
        LinkedList selectedSearchForms = new LinkedList();
        
        /*Iterator iterator = this.getSearchFormContainers().iterator();
        while(iterator.hasNext())
        {
            SearchForm searchForm = ((SearchFormContainer)(SearchFormContainer)iterator.next()).getSelectedSearchForm();
            
            if(searchForm != null)
            {
                selectedSearchForms.add(searchForm);
            }
        }*/
        
        Iterator iterator = this.getSearchForms().iterator();
        while(iterator.hasNext())
        {
            SearchForm searchForm = (SearchForm)iterator.next();
            if(searchForm.isSelected())
            {
                selectedSearchForms.add(searchForm);
            }
        }
        
        if(logger.isDebugEnabled())logger.debug("returning " + selectedSearchForms.size() + " search forms");
        return selectedSearchForms;
    }
    
    /**
     * Returns a list of form data object of all selected search forms that are
     * enabled.<p>
     * Before returning the data, this method synchronizes the data in the form 
     * with the data stored in the data bean object with a call to 
     * <code>writeFormParameters()</code>.<br>
     * Warning: global search forms are ignored.
     *
     * @return a list of FormDataBean objects
     * @thows FormValidationException by <code>writeFormParameters()</code>
     */
    protected java.util.List getSelectedFormData() throws FormValidationException
    {
        LinkedList formDataList = new LinkedList();
        
        Iterator iterator = this.getSelectedSearchForms().iterator();
        while(iterator.hasNext())
        {
            SearchForm searchForm = (SearchForm)iterator.next();
            if(searchForm.isEnabled())
            {
                if(logger.isDebugEnabled())logger.debug("reading data of form '" + searchForm.getName() + "'");
                
                searchForm.writeFormParameters();
                formDataList.add(searchForm.getDataBean());
            }
            else if(logger.isDebugEnabled())
            {
                logger.debug(searchForm.getName() + " is disabled");
            }         
        }
        
        return formDataList;
    }

    protected void setSelectedFormData(java.util.List formDataList) //throws FormValidationException
    {
        if(logger.isDebugEnabled())logger.debug("loading data for " + formDataList.size() + " forms");
        
        Iterator iterator = formDataList.iterator();
        while(iterator.hasNext())
        {
            FormDataBean dataBean = (FormDataBean)iterator.next();
            SearchForm searchForm = this.getSearchForm(dataBean.getFormId());
            
            if(searchForm != null)
            {
                searchForm.setDataBean(dataBean);
                searchForm.readBeanParameters();
                
                // select the search form (find the right container first)
                Iterator sfcIterator = this.getSearchFormContainers().iterator();
                while(sfcIterator.hasNext())
                {
                    SearchFormContainer searchFormContainer = (SearchFormContainer)sfcIterator.next();
                    if(searchFormContainer.setSelectedSearchForm(searchForm.getFormId()))
                    {
                        this.searchFormContainerPane.setSelectedIndex(this.searchFormContainerPane.indexOfTab(searchFormContainer.getName()));
                        break;
                    }
                }
            }
        }
    }
    
    
    protected void setSearchFormsEnabled(Collection classNodeKeys, Collection userGroups)
    {
        Iterator iterator = this.getSearchForms().iterator();
        while(iterator.hasNext())
        {
            SearchForm searchForm = (SearchForm)iterator.next();
            this.setSearchFormEnabled(searchForm, classNodeKeys, userGroups);
        }
    }
    
    protected void setSearchFormEnabled(SearchForm searchForm, Collection classNodeKeys, Collection userGroups)
    {
        SearchOption searchOption = this.getSearchOption(searchForm.getQueryId());
        
        if(searchOption != null)
        {
            searchForm.setEnabled(searchOption.isSelectable(classNodeKeys, userGroups));
        }
        else
        {
            logger.warn("no search option found, disabling search form '" + searchForm.getName() + "'");
            searchForm.setEnabled(false);
        }
    }
    
    protected void addSearchFormSelectionListener(ItemListener searchFormSelectionListener)
    {
        this.searchFormSelectionListener = searchFormSelectionListener;
        
        if(this.getSearchFormContainers() != null && this.getSearchFormContainers().size() > 0)
        {
            Iterator iterator = this.getSearchFormContainers().iterator();
        
            while(iterator.hasNext())
            {
                SearchFormContainer searchFormContainer = (SearchFormContainer)iterator.next();
                searchFormContainer.addSearchFormSelectionListener(searchFormSelectionListener);
            }
        } 
    }
  
    // =========================================================================
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusLabel = new javax.swing.JLabel();
        searchFormContainerPane = new javax.swing.JTabbedPane();

        statusLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText(I18N.getString("Sirius.navigator.search.dynamic.SearchFormManager.statuslabel.text")); // NOI18N
        statusLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 2)));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout(5, 5));

        searchFormContainerPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        add(searchFormContainerPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane searchFormContainerPane;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
    
    /*public static void main(String args[])
    {
        try
        {
            //org.apache.log4j.BasicConfigurator.configure();
            org.apache.log4j.PropertyConfigurator.configure(ClassLoader.getSystemResource("Sirius/Navigator/resource/cfg/log4j.debug.properties"));

            SearchFormFactory sff = new SearchFormFactory();
            java.util.List searchFormsContainers = sff.createSearchForms("D:\\work\\web\\Sirius\\navigator\\search\\", "search.xml", new SearchContext());


            final SearchFormManager sfcm = new SearchFormManager(new HashMap(), searchFormsContainers);

            JFrame jf = new JFrame("SearchFormContainerManager");
            jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
            jf.setLocationRelativeTo(null);
            jf.getContentPane().setLayout(new BorderLayout());
            jf.getContentPane().add(sfcm, BorderLayout.CENTER);

            jf.pack();
            
            JButton jb = new JButton("Suche");
            jb.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        java.util.List formData = sfcm.getSelectedFormData();  
                        XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("c:\\formData.xml")));
                        xmlEncoder.writeObject(formData);
                        xmlEncoder.close();
                    }
                    catch(FormValidationException fve)
                    {
                        sfcm.logger.error(fve.getMessage(), fve);
                        JOptionPane.showMessageDialog(sfcm, fve.getMessage(), fve.getFormName(), JOptionPane.ERROR_MESSAGE); 
                    }
                    catch(Exception exp)
                    {
                        sfcm.logger.error(exp);
                    }
                }
            });
            
            jf.getContentPane().add(jb, BorderLayout.NORTH);*/

            /*DefaultSearchFormContainer dsfc = new DefaultSearchFormContainer();
            Collection searchForms = new LinkedList();

            LinkedHashMap parameterMap = new LinkedHashMap();
            parameterMap.put("y0x0", "rechtsoben");
            parameterMap.put("y1x2", "linksunten");
            FormDataBean formDataBean = new DefaultFormDataBean(parameterMap);
            formDataBean.setBeanParameter("rechtsoben", "200");
            formDataBean.setBeanParameter("linksunten", "400");

            SearchForm searchForm = new DefaultSearchForm("Planquadrat", formDataBean, Locale.getDefault());

            try
            {
                searchForm.readBeanParameters();
            }
            catch(Exception exp)
            {
                sfcm.logger.fatal(exp.getMessage(), exp);
            }

            //searchForms.add(new DefaultSearchForm(new Query()));
            searchForms.add(searchForm);

            dsfc.setSearchForms(searchForms);
            LinkedList searchFormContainers = new LinkedList();
            searchFormContainers.add(dsfc);
            sfcm.setSearchFormContainers(searchFormContainers);*/

    /*
            jf.pack();
            jf.setVisible(true);
        }
        catch(Exception exp)
        {
            Logger.getLogger(SearchFormManager.class).fatal(exp.getMessage(), exp);
            System.exit(1);
        }
    }  */
}
