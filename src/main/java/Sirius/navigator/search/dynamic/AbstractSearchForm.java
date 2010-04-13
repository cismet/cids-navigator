/*
 * DefaultSearchType.java
 *
 * Created on 8. Oktober 2003, 14:43
 */

package Sirius.navigator.search.dynamic;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.MutablePanel;

/**
 *
 * @author  pascal
 */
public abstract class AbstractSearchForm extends JPanel implements SearchForm
{
    protected Logger logger;
    protected final MutablePanel formPanel;
    protected final HashMap formProperties;
    
    //protected LinkedHashMap values;
    protected String queryId = null;
    protected String formId = null;
    protected boolean selected = false;
    protected ResourceBundle resourceBundle = null;
    protected SearchContext searchContext = null;
    
    protected FormDataBean dataBean = null;
    
    public AbstractSearchForm()
    {
        //System.out.println(this.getClass());
        
        // warning: this.getClass() does not work! (reflection) 
        //this.logger = Logger.getLogger(this.getClass());
        this.logger = Logger.getLogger(AbstractSearchForm.class);
        
        this.formPanel = new MutablePanel(this, org.openide.util.NbBundle.getMessage(AbstractSearchForm.class, "AbstractSearchForm.formPanel.disabledMessage"));//NOI18N
        this.formProperties = new HashMap();
    }
    
    /** Creates a new instance of DefaultSearchType */
    /*public AbstractSearchForm(String name, FormDataBean dataBean, Locale locale)
    {
        this();
        
        this.setDataBean(dataBean);
        this.setLocale(locale);
        this.setName(name);
    }*/
  
    public javax.swing.JComponent getForm()
    {
        return this.formPanel;
    }
    
    /*public Object getValue(Object name)
    {
        return values.get(name);
    }*/
    
    public boolean isSelected()
    {
        return this.selected;
    }
    
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    /*public void setValue(Object name, Object value)
    {
        if(this.values.containsValue(name))
        {
            //Object oldValue = this.values.put(name, value);
            //super.firePropertyChange(name.toString(), oldValue, value);
            
            this.values.put(name, value);
        }
        else
        {
            logger.warn("could not set value: unknown element '" + name + "'");
        }
    }*/
    
    public void setEnabled(boolean enabled)
    {
        this.formPanel.setEnabled(enabled);
        super.setEnabled(enabled);
    } 
    
    public boolean isEnabled()
    {
        return super.isEnabled();
    }
    
    public String getName()
    {
        return super.getName();
    }
    
    public void setName(String name)
    {
        super.setName(name);
    }

    public String getQueryId()
    {
        return this.queryId;
    }
    
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }
    
    public String getFormId()
    {
        return this.formId;
    }
    
    public void setFormId(String formId)
    {
        this.formId = formId;
    }
    
    public FormDataBean getDataBean()
    {
        return this.dataBean;
    }
    
    public void setDataBean(FormDataBean dataBean)
    {
        // keep ParameterNamesMap to avoid and detect class version conflicts
        // if the new dataBean contains xml deserialized values
        if(this.dataBean != null && dataBean != null)
        {
            // 'copy' ParameterNamesMap (only initialized once)
            dataBean.setParameterNamesMap(this.dataBean.getParameterNamesMap());
        }
        
        this.dataBean = dataBean;
    }

    public SearchContext getSearchContext()
    {
        return this.searchContext;
    }
    
    public void setSearchContext(SearchContext searchContext)
    {
        this.searchContext = searchContext;
    }
    
    /**
     * @deprecated
     * This method should not be used. Instead the netbeans I18N API should be used.
     */
    public java.util.ResourceBundle getResourceBundle()
    {
        return this.resourceBundle;
    }

    /**
     * @deprecated
     * This method should not be used. Instead the netbeans I18N API should be used.
     */
    public void setResourceBundle(java.util.ResourceBundle resourceBundle)
    {
        if(logger.isDebugEnabled())logger.debug("setting resource bundle for locale locale to '" + resourceBundle.getLocale().toString() + "'");//NOI18N
        this.resourceBundle = resourceBundle;
    }
    
    public void setFormProperties(Map formProperties)
    {
        this.formProperties.clear();
        this.formProperties.putAll(formProperties);
    }
    
    public void readBeanParameters() //throws FormValidationException
    {
        if(this.getDataBean() != null)
        {
            if(logger.isDebugEnabled())logger.debug("showing " + this.getDataBean().getBeanParameterNames().size() + " parameter(s) in data bean in form ui");//NOI18N

            Iterator iterator = this.getDataBean().getBeanParameterNames().iterator();
            while(iterator.hasNext())
            {
                String name = iterator.next().toString();
                this.setFormParameter(name, this.getDataBean().getBeanParameter(name));
            }
        }
    }
        
    public void writeFormParameters() throws FormValidationException
    {
        if(this.getDataBean() != null)
        {
            if(logger.isDebugEnabled())logger.debug("storing " + this.getDataBean().getBeanParameterNames().size() + " parameter(s) from form ui in data bean");//NOI18N
        
            this.getDataBean().clear();
            Iterator iterator = this.getDataBean().getBeanParameterNames().iterator();
            
            while(iterator.hasNext())
            {
                String name = iterator.next().toString();
                this.getDataBean().setBeanParameter(name, this.getFormParameter(name));
            }
        }
    }
    
    // .........................................................................

    public abstract Object getFormParameter(String name) throws FormValidationException;
    
    public abstract void setFormParameter(String name, Object value); //throws FormValidationException;
    
    public abstract void initForm() throws FormInitializationException;
    
    public abstract void resetForm();
    
    // deprecated
    //public abstract Object validateFormParameter(String name, Object value) throws FormValidationException; 
    
    public String toString()
    {
        return this.getName();
    }
    
    // .........................................................................
    
    /**
     * @deprecated
     * This method should not be used. Instead the netbeans I18N way should be used.
     *
     * Should be called in the init() method.<p>
     * Override this method, if your form supports i18n!<p>
     *
     * @param resourceBundle the resource bundle that contains the i18n strings
     * @throws MissingResourceException if a key is not in the bundle
     */
    protected void internationalize(ResourceBundle resourceBundle) throws MissingResourceException
    {
        if(this.resourceBundle != null)
        {
            try{this.setName(resourceBundle.getString("form.name"));}catch(Throwable t){/*egal*/}            
            this.formPanel.setDisabledMessage(resourceBundle.getString("disabled.message"));
        }
        else
        {
            if(logger.isDebugEnabled())logger.error("i18n falied: property 'resourceBundle' is null");//NOI18N
            throw new MissingResourceException("i18n falied: property 'resourceBundle' is null", this.getClass().getName(), "resourceBundle");//NOI18N
        }
    }  
    
}
