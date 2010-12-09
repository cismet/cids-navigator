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
 * Created on 8. Oktober 2003, 14:43
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
public abstract class AbstractSearchForm extends JPanel implements SearchForm {

    //~ Instance fields --------------------------------------------------------

    protected Logger logger;
    protected final MutablePanel formPanel;
    protected final HashMap formProperties;

    // protected LinkedHashMap values;
    protected String queryId = null;
    protected String formId = null;
    protected boolean selected = false;
    protected ResourceBundle resourceBundle = null;
    protected SearchContext searchContext = null;

    protected FormDataBean dataBean = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractSearchForm object.
     */
    public AbstractSearchForm() {
        // System.out.println(this.getClass());

        // warning: this.getClass() does not work! (reflection)
        // this.logger = Logger.getLogger(this.getClass());
        this.logger = Logger.getLogger(AbstractSearchForm.class);

        this.formPanel = new MutablePanel(
                this,
                org.openide.util.NbBundle.getMessage(
                    AbstractSearchForm.class,
                    "AbstractSearchForm.formPanel.disabledMessage")); // NOI18N
        this.formProperties = new HashMap();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates a new instance of DefaultSearchType.
     *
     * @return  DOCUMENT ME!
     */
    /*public AbstractSearchForm(String name, FormDataBean dataBean, Locale locale)
     * { this();  this.setDataBean(dataBean); this.setLocale(locale); this.setName(name);}*/

    @Override
    public javax.swing.JComponent getForm() {
        return this.formPanel;
    }

    /*public Object getValue(Object name)
     * { return values.get(name);}*/

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    /*public void setValue(Object name, Object value)
     * { if(this.values.containsValue(name)) {     //Object oldValue = this.values.put(name, value);
     * //super.firePropertyChange(name.toString(), oldValue, value);          this.values.put(name, value); } else {
     * logger.warn("could not set value: unknown element '" + name + "'"); }}*/

    @Override
    public void setEnabled(final boolean enabled) {
        this.formPanel.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
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
    public String getQueryId() {
        return this.queryId;
    }

    @Override
    public void setQueryId(final String queryId) {
        this.queryId = queryId;
    }

    @Override
    public String getFormId() {
        return this.formId;
    }

    @Override
    public void setFormId(final String formId) {
        this.formId = formId;
    }

    @Override
    public FormDataBean getDataBean() {
        return this.dataBean;
    }

    @Override
    public void setDataBean(final FormDataBean dataBean) {
        // keep ParameterNamesMap to avoid and detect class version conflicts
        // if the new dataBean contains xml deserialized values
        if ((this.dataBean != null) && (dataBean != null)) {
            // 'copy' ParameterNamesMap (only initialized once)
            dataBean.setParameterNamesMap(this.dataBean.getParameterNamesMap());
        }

        this.dataBean = dataBean;
    }

    @Override
    public SearchContext getSearchContext() {
        return this.searchContext;
    }

    @Override
    public void setSearchContext(final SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  This method should not be used. Instead the netbeans I18N API should be used.
     */
    @Override
    public java.util.ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param       resourceBundle  DOCUMENT ME!
     *
     * @deprecated  This method should not be used. Instead the netbeans I18N API should be used.
     */
    @Override
    public void setResourceBundle(final java.util.ResourceBundle resourceBundle) {
        if (logger.isDebugEnabled()) {
            logger.debug("setting resource bundle for locale locale to '" + resourceBundle.getLocale().toString()
                        + "'"); // NOI18N
        }
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void setFormProperties(final Map formProperties) {
        this.formProperties.clear();
        this.formProperties.putAll(formProperties);
    }

    @Override
    public void readBeanParameters()                                    // throws FormValidationException
    {
        if (this.getDataBean() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("showing " + this.getDataBean().getBeanParameterNames().size()
                            + " parameter(s) in data bean in form ui"); // NOI18N
            }

            final Iterator iterator = this.getDataBean().getBeanParameterNames().iterator();
            while (iterator.hasNext()) {
                final String name = iterator.next().toString();
                this.setFormParameter(name, this.getDataBean().getBeanParameter(name));
            }
        }
    }

    @Override
    public void writeFormParameters() throws FormValidationException {
        if (this.getDataBean() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("storing " + this.getDataBean().getBeanParameterNames().size()
                            + " parameter(s) from form ui in data bean"); // NOI18N
            }

            this.getDataBean().clear();
            final Iterator iterator = this.getDataBean().getBeanParameterNames().iterator();

            while (iterator.hasNext()) {
                final String name = iterator.next().toString();
                this.getDataBean().setBeanParameter(name, this.getFormParameter(name));
            }
        }
    }

    // .........................................................................

    @Override
    public abstract Object getFormParameter(String name) throws FormValidationException;
    @Override
    public abstract void setFormParameter(String name, Object value); // throws FormValidationException;
    @Override
    public abstract void initForm() throws FormInitializationException;
    @Override
    public abstract void resetForm();

    // deprecated
    // public abstract Object validateFormParameter(String name, Object value) throws FormValidationException;

    @Override
    public String toString() {
        return this.getName();
    }

    // .........................................................................

    /**
     * DOCUMENT ME!
     *
     * @param       resourceBundle  the resource bundle that contains the i18n strings
     *
     * @throws      MissingResourceException  if a key is not in the bundle
     *
     * @deprecated  This method should not be used. Instead the netbeans I18N way should be used.
     *
     *              <p>Should be called in the init() method.</p>
     *
     *              <p>Override this method, if your form supports i18n!</p>
     */
    protected void internationalize(final ResourceBundle resourceBundle) throws MissingResourceException {
        if (this.resourceBundle != null) {
            try {
                this.setName(resourceBundle.getString("form.name"));
            } catch (Throwable t) { /*egal*/
            }
            this.formPanel.setDisabledMessage(resourceBundle.getString("disabled.message"));
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
}
