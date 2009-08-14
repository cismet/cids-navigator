/*
 * DefaultSearchType.java
 *
 * Created on 8. Oktober 2003, 15:06
 */

package Sirius.navigator.search.dynamic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.text.*;
import java.beans.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.MutablePanel;

/**
 *
 * @author  pascal
 */
public class DefaultSearchForm extends AbstractSearchForm
{
    protected HashMap inputFields = new HashMap();    
    
    public DefaultSearchForm()
    {
        super();
        
        super.logger = Logger.getLogger(DefaultSearchForm.class);
        this.inputFields = new HashMap();
    }
    
    /** Creates a new instance of DefaultSearchType */
    public DefaultSearchForm(String name, FormDataBean dataBean, Locale locale)
    {
        //super(name, dataBean, locale);
        //super.logger = Logger.getLogger(this.getClass());
        this();
        
        this.setName(name);
        this.setDataBean(dataBean);
        this.setLocale(locale); 
    }
    
    public void initForm() throws FormInitializationException
    {
        if(this.getDataBean() != null)
        {
            this.initComponents(this.getDataBean().getBeanParameterNames());
        }
        else
        {
            throw new FormInitializationException();
        }
        
        if(this.getResourceBundle() != null)
        {
            try
            {
                super.internationalize(this.getResourceBundle());
            }
            catch(MissingResourceException mrexp)
            {
                throw new FormInitializationException();
            }
        }
        else
        {
            logger.debug("i18n not supported by form '" + this.getName() + "'");
        }
    }
    
    protected void initComponents(Collection beanParameterNames)
    {
        //this.setLayout(new GridLayout(beanParameterNames.size(), 2, 5, 5));
        
        this.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(20, 20, 20, 20)));
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = -1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weighty = 0.0;
        
        Iterator iterator = beanParameterNames.iterator();
        while(iterator.hasNext())
        {
            String name = iterator.next().toString();
            JTextField textField = new JTextField();
            textField.setName(name);
            
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weightx = 1.0;
            this.add(new JLabel(name), gridBagConstraints);
            
            gridBagConstraints.gridx = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.weightx = 0.5;
            this.add(textField, gridBagConstraints);
            
            this.inputFields.put(name, textField);
        }
    }
    
   /* public void setDataBean(FormDataBean dataBean)
    {
        super.setDataBean(dataBean);
    }*/
    
    
    // deprecated
    /*public Object validateFormParameter(String name, Object value) throws FormValidationException
    {
        if(value != null && value.toString().length() > 0)
        {
            return value;
        }
        else
        {
            logger.warn("value of parameter '" + name + "' is null");
            throw new FormValidationException();
        }
    }*/
    
    public Object getFormParameter(String name) throws FormValidationException
    {
        Object inputField = this.inputFields.get(name);
        
        if(inputField != null)
        {
            String text = ((JTextField)inputField).getText();
            if(text != null && text.length() > 0)
            {
                return text;
            }
            else
            {
                throw new FormValidationException(this.getName(), name, "java.lang.String");
            } 
        }
        else
        {
            logger.error("getParameter() failed: input field '" + name + "' not found");
            throw new FormValidationException(this.getName(), name, "unknown parameter");
        }
    }
    
    public void setFormParameter(String name, Object value) //throws FormValidationException
    {
        Object inputField = this.inputFields.get(name);
        
        if(inputField != null)
        {
            if(value != null)
            {
                ((JTextField)inputField).setText(value.toString());
            }
            else
            {
                ((JTextField)inputField).setText(null);
            }   
        }
        else
        {
            logger.warn("setParameter() failed: input field '" + name + "' not found");
        }
    }
    
    public void resetForm()
    {
        if(logger.isDebugEnabled())logger.debug("resetting " + this.inputFields.size() + " input fields");
        
        Iterator iterator = this.inputFields.values().iterator();
        while(iterator.hasNext())
        {
            ((JTextField)iterator.next()).setText(null);
        }
    }   
}
