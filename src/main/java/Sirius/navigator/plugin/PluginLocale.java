/*
 * PluginLocale.java
 *
 * Created on 17. Juni 2003, 09:26
 */

package Sirius.navigator.plugin;

/**
 *
 * @author  pascal
 */
public class PluginLocale
{   
    /** Holds value of property locale. */
    private final java.util.Locale locale;
    
    /** Holds value of property resourceFile. */
    private final String resourceFile;
    
    /** Holds value of property name. */
    private final String name;
    
    /** Creates a new instance of PluginLocale */
    public PluginLocale(String name, String language, String country, String resourceFile)
    {
        this.locale = new java.util.Locale(language, country);
        this.name = name;
        this.resourceFile = resourceFile;
    }
        
    /** Getter for property locale.
     * @return Value of property locale.
     *
     */
    public java.util.Locale getLocale()
    {
        return this.locale;
    }
    
    
    /** Getter for property resourceFile.
     * @return Value of property resourceFile.
     *
     */
    public String getResourceFile()
    {
        return this.resourceFile;
    }
        
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName()
    {
        return this.name;
    }   
}
