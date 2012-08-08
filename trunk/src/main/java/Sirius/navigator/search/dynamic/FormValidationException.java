/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * FormValidationException.java
 *
 * Created on 13. Oktober 2003, 11:27
 */
package Sirius.navigator.search.dynamic;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class FormValidationException extends Exception {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property formName. */
    private String formName;

    /** Holds value of property expectedType. */
    private String expectedType;

    /** Holds value of property parameterName. */
    private String parameterName;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FormValidationException object.
     *
     * @param  formName  DOCUMENT ME!
     * @param  message   DOCUMENT ME!
     */
    public FormValidationException(final String formName, final String message) {
        super(makeCustomMessage(message));

        this.setFormName(formName);
    }

    // .........................................................................
    /**
     * Creates a new instance of FormValidationException.
     *
     * @param  formName       DOCUMENT ME!
     * @param  parameterName  DOCUMENT ME!
     * @param  expectedType   DOCUMENT ME!
     */

    public FormValidationException(final String formName, final String parameterName, final String expectedType) {
        super(makeDefaultMessage(formName, parameterName, expectedType));

        this.setFormName(formName);
        this.setParameterName(parameterName);
        this.setExpectedType(expectedType);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    public String getFormName() {
        return this.formName;
    }

    /**
     * Setter for property name.
     *
     * @param  formName  name New value of property name.
     */
    public void setFormName(final String formName) {
        this.formName = formName;
    }

    /**
     * Getter for property expectedType.
     *
     * @return  Value of property expectedType.
     */
    public String getExpectedType() {
        return this.expectedType;
    }

    /**
     * Setter for property expectedType.
     *
     * @param  expectedType  New value of property expectedType.
     */
    public void setExpectedType(final String expectedType) {
        this.expectedType = expectedType;
    }

    /**
     * Getter for property parameter.
     *
     * @return  Value of property parameter.
     */
    public String getParameterName() {
        return this.parameterName;
    }

    /**
     * Setter for property parameter.
     *
     * @param  parameterName  New value of property parameter.
     */
    public void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * -------------------------------------------------------------------------
     *
     * @param   formName       DOCUMENT ME!
     * @param   parameterName  DOCUMENT ME!
     * @param   expectedType   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected static String makeDefaultMessage(final String formName,
            final String parameterName,
            final String expectedType) {
        return org.openide.util.NbBundle.getMessage(
                FormValidationException.class,
                "FormValidationException.makeDefaultMessage().defaultMessage",
                new Object[] { formName, parameterName, expectedType }); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   message  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected static String makeCustomMessage(final String message) {
        return org.openide.util.NbBundle.getMessage(
                FormValidationException.class,
                "FormValidationException.makeCustomMessage().customMessage",
                new Object[] { message }); // NOI18N
    }
}
