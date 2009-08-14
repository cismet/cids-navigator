/*
 * PluginProperties.java
 *
 * Created on 21. September 2004, 10:40
 */

package Sirius.navigator.plugin.interfaces;

import java.beans.*;

/**
 *
 * @author  pascal
 */
public interface PluginProperties
{
    public void setProperty(String propertyName, Object value);
    
    public Object getProperty(String propertyName);
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    
    public void removePropertyChangeListener(PropertyChangeListener listener);
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
