/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultSearchType.java
 *
 * Created on 8. Oktober 2003, 15:06
 */
package Sirius.navigator.search.dynamic;

import Sirius.navigator.ui.widget.MutablePanel;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultSearchForm extends AbstractSearchForm {

    //~ Instance fields --------------------------------------------------------

    protected HashMap inputFields = new HashMap();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultSearchForm object.
     */
    public DefaultSearchForm() {
        super();

        super.logger = Logger.getLogger(DefaultSearchForm.class);
        this.inputFields = new HashMap();
    }

    /**
     * Creates a new instance of DefaultSearchType.
     *
     * @param  name      DOCUMENT ME!
     * @param  dataBean  DOCUMENT ME!
     * @param  locale    DOCUMENT ME!
     */
    public DefaultSearchForm(final String name, final FormDataBean dataBean, final Locale locale) {
        // super(name, dataBean, locale);
        // super.logger = Logger.getLogger(this.getClass());
        this();

        this.setName(name);
        this.setDataBean(dataBean);
        this.setLocale(locale);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initForm() throws FormInitializationException {
        if (this.getDataBean() != null) {
            this.initComponents(this.getDataBean().getBeanParameterNames());
        } else {
            throw new FormInitializationException();
        }

//        if(this.getResourceBundle() != null)
//        {
//            try
//            {
//                super.internationalize(this.getResourceBundle());
//            }
//            catch(MissingResourceException mrexp)
//            {
//                throw new FormInitializationException();
//            }
//        }
//        else
//        {
//            logger.debug("i18n not supported by form '" + this.getName() + "'");//NOI18N
//        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  beanParameterNames  DOCUMENT ME!
     */
    protected void initComponents(final Collection beanParameterNames) {
        // this.setLayout(new GridLayout(beanParameterNames.size(), 2, 5, 5));

        this.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(20, 20, 20, 20)));
        this.setLayout(new GridBagLayout());

        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = -1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weighty = 0.0;

        final Iterator iterator = beanParameterNames.iterator();
        while (iterator.hasNext()) {
            final String name = iterator.next().toString();
            final JTextField textField = new JTextField();
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
     * {  super.setDataBean(dataBean); }*/

    // deprecated
    /*public Object validateFormParameter(String name, Object value) throws FormValidationException
     * { if(value != null && value.toString().length() > 0) {     return value; } else {     logger.warn("value of
     * parameter '" + name + "' is null");     throw new FormValidationException(); }}*/

    @Override
    public Object getFormParameter(final String name) throws FormValidationException {
        final Object inputField = this.inputFields.get(name);

        if (inputField != null) {
            final String text = ((JTextField)inputField).getText();
            if ((text != null) && (text.length() > 0)) {
                return text;
            } else {
                throw new FormValidationException(this.getName(), name, "java.lang.String"); // NOI18N
            }
        } else {
            logger.error("getParameter() failed: input field '" + name + "' not found");     // NOI18N
            throw new FormValidationException(this.getName(), name, "unknown parameter");    // NOI18N
        }
    }

    @Override
    public void setFormParameter(final String name, final Object value) // throws FormValidationException
    {
        final Object inputField = this.inputFields.get(name);

        if (inputField != null) {
            if (value != null) {
                ((JTextField)inputField).setText(value.toString());
            } else {
                ((JTextField)inputField).setText(null);
            }
        } else {
            logger.warn("setParameter() failed: input field '" + name + "' not found"); // NOI18N
        }
    }

    @Override
    public void resetForm() {
        if (logger.isDebugEnabled()) {
            logger.debug("resetting " + this.inputFields.size() + " input fields"); // NOI18N
        }

        final Iterator iterator = this.inputFields.values().iterator();
        while (iterator.hasNext()) {
            ((JTextField)iterator.next()).setText(null);
        }
    }
}
