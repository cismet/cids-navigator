package Sirius.navigator.plugin.listener;

import java.util.*;

/**
 *
 * @author  pascal
 */
public abstract class MetaAttributeSelectionListener
{
    
    /** Creates a new instance of Sirius.server.localserver.attribute.AttributeSelectionListener */
    public MetaAttributeSelectionListener()
    {
    }
    
    protected abstract void attributeSelectionChanged(Collection nodeSelection);    
    
}
