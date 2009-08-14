/*
 * PluginUIException.java
 *
 * Created on 18. Februar 2003, 12:04
 */

package Sirius.navigator.plugin.exceptions;

/**
 *
 * @author  pascal
 */
public class PluginUIException extends PluginException
{
    
    /** Creates a new instance of PluginUIException */
    public PluginUIException(int level, String errorcode, String[] values, Throwable cause)
    {
        super(level,errorcode, values, cause);
    }
    
}
