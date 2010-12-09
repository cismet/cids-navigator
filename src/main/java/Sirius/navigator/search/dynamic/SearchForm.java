/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchForm.java
 *
 * Created on 25. September 2003, 11:45
 */
package Sirius.navigator.search.dynamic;

/**
 * This interface repesents a genric input form for search parameter values.
 *
 * @author   Pascal
 * @version  0.4
 */
public interface SearchForm {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property enabled.
     *
     * <p>Should return true, if the form allows input of parameter values. Should return false otherwise.</p>
     *
     * @return  Value of property enabled.
     */
    boolean isEnabled();

    /**
     * Setter for property enabled.
     *
     * <p>If enabled is false, the form must not allow any input of parameter values (e.g. disable any input field ot
     * 'lock' the entire form).</p>
     *
     * @param  enabled  New value of property enabled.
     */
    void setEnabled(boolean enabled);

    /**
     * Getter for property selected.
     *
     * <p>Returns true, if the form is selected. False otherwise.</p>
     *
     * @return  Value of property selected.
     */
    boolean isSelected();

    /**
     * Setter for property selected.
     *
     * <p>If the form is marked as selected, all parameter values will be used by the search.</p>
     *
     * @param  selected  New value of property selected.
     */
    void setSelected(boolean selected);

    /**
     * Sets a parameter value from outside.
     *
     * <p>The value is only set in the <b>form ui</b>! To store <b>all</b> new values in the dataBean object, <code>
     * writeParameters()</code> must be invoked.<br>
     * To set a parameter in the dataBean object directly, call <code>getDataBean.setBeanParameter()</code> (not
     * recommended).<br>
     * </p>
     *
     * @param  name   name of the parameter
     * @param  value  value of the parameter //@throws FormValidationException if one or more parameter is invalid or
     *                missing
     */
    void setFormParameter(String name, Object value);                                                         // throws FormValidationException;

    /**
     * Returns the value of a parameter stored in the <b>form ui</b>.
     *
     * <p>To retrieve a parameter from the dataBean object, call <code>getDataBean.getBeanParameter()</code> (not
     * recommended).</p>
     *
     * @param   name  of the parameter
     *
     * @return  the parameter value (can be null)
     *
     * @throws  FormValidationException  DOCUMENT ME!
     */
    Object getFormParameter(String name) throws FormValidationException;

    /**
     * Getter for property dataBean.
     *
     * <p>Returns the dataBean object of this form where all parameter values are stored.</p>
     *
     * @return  Value of property dataBean.
     */
    FormDataBean getDataBean();

    /**
     * Setter for property dataBean.
     *
     * <p>If this method is called, the method readBeanParameters() should be invoked. This will update the form ui with
     * the new vales stored in the data bean.</p>
     *
     * @param  dataBean  New value of property dataBean.
     */
    void setDataBean(FormDataBean dataBean);

    /**
     * Getter for property locale.
     *
     * <p>Returns the active Locale of this SearchForm.</p>
     *
     * @return  Value of property locale.
     */
    // public java.util.Locale getLocale();

    /**
     * Setter for property locale.
     *
     * <p>Sets the active Locale of this SearchForm. Each SearchForm is responsible for proper internationalization.<br>
     * Ignore this method, if i18n is not supported by this Form.</p>
     *
     * @return  DOCUMENT ME!
     */
    // public void setLocale(java.util.Locale locale);

    /**
     * Getter for property name.
     *
     * <p>Returns the (internationalized) display name of this form.</p>
     *
     * @return  Value of property name.
     */
    String getName();

    /**
     * Getter for property queryId.
     *
     * @return  Value of property queryId.
     */
    String getQueryId();

    /**
     * Setter for property queryId.
     *
     * @param  queryId  New value of property queryId.
     */
    void setQueryId(String queryId);

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    void setName(String name);

    /**
     * Getter for property component.
     *
     * <p>Returns this form's user interface component, that will be displayed in the form container. In general, this
     * method returns <code>this</code>.</p>
     *
     * @return  Value of property component.
     */
    javax.swing.JComponent getForm();

    /**
     * Set form properties.
     *
     * @param  formProperties  a Map with name/value property pairs
     */
    void setFormProperties(java.util.Map formProperties);

    // .........................................................................

    /**
     * Validates a form parameter parameters.
     *
     * @throws  FormValidationException  if the parameter value is invalid or null //@deprecated use getFormParameter()
     */
    // public Object validateFormParameter(String name, Object value) throws FormValidationException;

    /**
     * Store all parameter values in the dataBean object.
     *
     * <p>This method should validate all form parameters before storing the values in the data bean.</p>
     *
     * @throws  FormValidationException  if one or more parameter is invalid or missing
     */
    void writeFormParameters() throws FormValidationException;

    /**
     * Read all parameter values stored in the Query object an show them in the Form ui.
     *
     * <p>This method assumes that the parameters stored in the data bean are all valid form parameters. If a parameter
     * is missing or invalid no exception should be thrown but the form value (e.g. the input field) should be reset to
     * a default value (e.g. null).</p>
     */
    void readBeanParameters();     // throws FormValidationException;

    /**
     * Performs post form construction initilaization tasks (ui creation, ...).
     *
     * <p>This method will be called</p>
     *
     * @throws  FormInitializationException  if the initialization of the form fails.
     */
    void initForm() throws FormInitializationException;

    /**
     * This will reset the form (e.g. clear all input fields)
     */
    void resetForm();

    // .........................................................................

    /**
     * This method should always return the name of this form.
     *
     * @return  same value as <code>getName()</code>
     */
    @Override
    String toString();

    /**
     * Getter for property searchContext.
     *
     * @return  Value of property searchContext.
     */
    SearchContext getSearchContext();

    /**
     * Setter for property searchContext.
     *
     * @param  searchContext  New value of property searchContext.
     */
    void setSearchContext(SearchContext searchContext);

    /**
     * Getter for property resourceBundle.
     *
     * @return  Value of property resourceBundle.
     */
    java.util.ResourceBundle getResourceBundle();

    /**
     * Setter for property resourceBundle.
     *
     * @param  resourceBundle  New value of property resourceBundle.
     */
    void setResourceBundle(java.util.ResourceBundle resourceBundle);

    /**
     * Getter for property formId.
     *
     * @return  Value of property formId.
     */
    String getFormId();

    /**
     * Setter for property formId.
     *
     * @param  formId  New value of property formId.
     */
    void setFormId(String formId);

    /**
     * Getter for property visible.
     *
     * @return  Value of property visible.
     */
    boolean isVisible();

    /**
     * Setter for property visible.
     *
     * @param  visible  New value of property visible.
     */
    void setVisible(boolean visible);

    /** Getter for property resourceFileName.
     * @return Value of property resourceFileName.
     *
     */
    // public String getResourceFileName();

    /**
     * Setter for property resourceFileName.<p>
     * The base name of the i18n resource bundle of this form.
     *
     * @param resourceFileName New value of property resourceFileName.
     */
    // public void setResourceFileName(String resourceFileName);

}
