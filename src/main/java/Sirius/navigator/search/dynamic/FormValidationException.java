/*
 * FormValidationException.java
 *
 * Created on 13. Oktober 2003, 11:27
 */

package Sirius.navigator.search.dynamic;


/**
 *
 * @author  pascal
 */
public class FormValidationException extends Exception
{
    /** Holds value of property formName. */
    private String formName;
    
    /** Holds value of property expectedType. */
    private String expectedType;
    
    /** Holds value of property parameterName. */
    private String parameterName;
    /** Creates a new instance of FormValidationException */

    public FormValidationException(String formName, String parameterName, String expectedType)
    {
        super(makeDefaultMessage(formName, parameterName, expectedType));
        
        this.setFormName(formName);
        this.setParameterName(parameterName);
        this.setExpectedType(expectedType);  
    }
    
    public FormValidationException(String formName, String message)
    {
        super(makeCustomMessage(message));
        
        this.setFormName(formName);
    }
    
    // .........................................................................
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getFormName()
    {
        return this.formName;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setFormName(String formName)
    {
        this.formName = formName;
    }
    
    /** Getter for property expectedType.
     * @return Value of property expectedType.
     *
     */
    public String getExpectedType()
    {
        return this.expectedType;
    }
    
    /** Setter for property expectedType.
     * @param expectedType New value of property expectedType.
     *
     */
    public void setExpectedType(String expectedType)
    {
        this.expectedType = expectedType;
    }
    
    /** Getter for property parameter.
     * @return Value of property parameter.
     *
     */
    public String getParameterName()
    {
        return this.parameterName;
    }
    
    /** Setter for property parameter.
     * @param parameter New value of property parameter.
     *
     */
    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }  
    
    // -------------------------------------------------------------------------
    
    protected static String makeDefaultMessage(String formName, String parameterName, String expectedType)
    {
        return org.openide.util.NbBundle.getMessage(FormValidationException.class,"FormValidationException.makeDefaultMessage().defaultMessage", new Object[]{formName, parameterName, expectedType});//NOI18N
    }
    
    protected static String makeCustomMessage(String message)
    {
        return org.openide.util.NbBundle.getMessage(FormValidationException.class, "FormValidationException.makeCustomMessage().customMessage", new Object[]{message});//NOI18N
    }
}
