package Sirius.navigator.plugin.ui;

import java.awt.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.embedded.*;
import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.method.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public class PluginMenu extends EmbeddedMenu
{   
    /** Holds value of property hideUnavailableItems. */
    private boolean hideUnavailableItems = false;
    
    /** Holds value of property disableUnavailableItems. */
    private boolean disableUnavailableItems = true;
    
    public PluginMenu(String id)
    {
        super(id);
    }
    
    public PluginMenu(String id, String name)
    {
        this(id);
        this.setText(name);
    }
    
    /** Getter for property hideUnavailableItems.
     * @return Value of property hideUnavailableItems.
     *
     */
    public boolean isHideUnavailableItems()
    {
        return this.hideUnavailableItems;
    }
    
    /** Setter for property hideUnavailableItems.
     * @param hideUnavailableItems New value of property hideUnavailableItems.
     *
     */
    public void setHideUnavailableItems(boolean hideUnavailableItems)
    {
        this.hideUnavailableItems = hideUnavailableItems;
    }
    
    /** Getter for property disableUnavailableItems.
     * @return Value of property disableUnavailableItems.
     *
     */
    public boolean isDisableUnavailableItems()
    {
        return this.disableUnavailableItems;
    }
    
    /** Setter for property disableUnavailableItems.
     * @param disableUnavailableItems New value of property disableUnavailableItems.
     *
     */
    public void setDisableUnavailableItems(boolean disableUnavailableItems)
    {
        this.disableUnavailableItems = disableUnavailableItems;
    }
    
    /** Setter for property availability.
     * @param availability New value of property availability.
     *
     */
    public void setAvailability(MethodAvailability methodAvailability)
    {
        if(logger.isDebugEnabled())logger.debug("setting plugin menu items availability '" + methodAvailability.getAvailability() + "' of '" + this.getMenuComponentCount() + "' components");  // NOI18N
        Component[] components = this.getMenuComponents();
        
        for(int i = 0; i < components.length; i++)
        {
            if(components[i] instanceof PluginMenuItem)
            {
                PluginMenuItem pluginMenuItem = (PluginMenuItem)components[i];
                //if(logger.isDebugEnabled())logger.debug("changing availability '" + pluginMenuItem.getAvailability() + "' of '" + pluginMenuItem.getText() + "' to '" + availability + "': '"  + (pluginMenuItem.getAvailability() & availability) + "'");
                
                boolean available = (pluginMenuItem.getAvailability() & methodAvailability.getAvailability()) > 0;
                if(logger.isDebugEnabled())logger.debug(pluginMenuItem.getText() + " is available: " + available + " (" + pluginMenuItem.getAvailability() + " & " + methodAvailability.getAvailability() + " > 0)");  // NOI18N
                
                if(pluginMenuItem.getMethod() != null && available)
                {
                    available = methodAvailability.containsClasses(pluginMenuItem.getMethod().getClassKeys());
                    if(logger.isDebugEnabled())logger.debug(pluginMenuItem.getText() + " is available for selected classes: " + available);  // NOI18N
                    
                    Iterator iterator = methodAvailability.getClassKeys().iterator();
                    while(iterator.hasNext())
                    {
                        if(logger.isDebugEnabled())
                            logger.debug("class key of selected nodes: " + iterator.next());  // NOI18N
                    }
                    
                    iterator = pluginMenuItem.getMethod().getClassKeys().iterator();
                    while(iterator.hasNext())
                    {
                        if(logger.isDebugEnabled())
                            logger.debug("class key of selected method: " + iterator.next());  // NOI18N
                    }
                }
                
                if(disableUnavailableItems)
                {
                    pluginMenuItem.setEnabled(available);
                }
                else if (hideUnavailableItems)
                {
                    pluginMenuItem.setVisible(available);
                }
            }
            else if(!(components[i] instanceof JSeparator))
            {
                logger.warn("could not set availability of component '" + components[i].getClass().getName() + "'");  // NOI18N
            }
        }
    }   
}
