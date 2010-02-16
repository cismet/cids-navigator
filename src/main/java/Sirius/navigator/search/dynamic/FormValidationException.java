/*
 * FormValidationException.java
 *
 * Created on 13. Oktober 2003, 11:27
 */

package Sirius.navigator.search.dynamic;

import java.util.ResourceBundle;

/**
 *
 * @author  pascal
 */
public class FormValidationException extends Exception
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

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
        StringBuffer buffer = new StringBuffer("<html>");
        
        buffer.append(I18N.getString("Sirius.navigator.search.dynamic.FormValidationException.makeDefaultMessage().defaultMessage"));
        buffer.append("<p>");
        buffer.append(I18N.getString("Sirius.navigator.search.dynamic.FormValidationException.makeDefaultMessage().form"));
        buffer.append(' ');
        buffer.append(formName);
        buffer.append("</p>");
        buffer.append("<p>");
        buffer.append(I18N.getString("Sirius.navigator.search.dynamic.FormValidationException.makeDefaultMessage().parameter"));
        buffer.append(' ');
        buffer.append(parameterName);
        buffer.append("</p>");
        buffer.append("<p>");
        buffer.append(I18N.getString("Sirius.navigator.search.dynamic.FormValidationException.makeDefaultMessage().type"));
        buffer.append(' ');
        buffer.append(expectedType);
        buffer.append("</p>");
        buffer.append("</html>");
        
        return buffer.toString();
    }
    
    protected static String makeCustomMessage(String message)
    {
        StringBuffer buffer = new StringBuffer("<html>");
        
        buffer.append(I18N.getString("Sirius.navigator.search.dynamic.FormValidationException.makeCustomMessage().customMessage"));
        buffer.append("<p>");
        buffer.append(message);
        buffer.append("</p>");
        buffer.append("</html>");
        
        return buffer.toString();
    }
}
