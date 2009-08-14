/*
 * PluginException.java
 *
 * Created on 18. Februar 2003, 12:03
 */

package Sirius.navigator.plugin.exceptions;

import Sirius.navigator.exception.*;

/**
 *
 * @author  pascal
 */
public class PluginException extends NavigatorException
{
    
    /** Creates a new instance of PluginException */
    public PluginException(int level, String errorcode, String[] values, Throwable cause) 
    {
        super(level, errorcode, values, cause);
    }
}
