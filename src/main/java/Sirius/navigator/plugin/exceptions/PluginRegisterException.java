/*
 * PluginRegisterException.java
 *
 * Created on 18. Juni 2003, 09:45
 */

package Sirius.navigator.plugin.exceptions;

/**
 *
 * @author  pascal
 */
public class PluginRegisterException extends PluginException
{
    
    /** Creates a new instance of PluginRegisterException */
    public PluginRegisterException(String errorcode, String[] values, Throwable cause) 
    {
        super(PluginException.ERROR, errorcode, values, cause);
    }
    
}
