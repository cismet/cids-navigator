package Sirius.navigator.plugin;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.*;
import Sirius.navigator.ui.widget.*;
import Sirius.navigator.plugin.ui.*;
import Sirius.navigator.plugin.interfaces.*;

/**
 *
 * @author  pascal
 */
public final class PluginUIDescriptor extends MutableConstraints implements PropertyChangeListener
{
    private PluginUI pluginUI = null;
    
    /** Holds value of property pluginComponentEventsEnabled. */
    private boolean pluginComponentEventsEnabled = false;
    
    /** Holds value of property iconName. */
    private String iconName = null;
    
    
    public void addAsComponent(PluginUI pluginUI)
    {
        this.pluginUI = pluginUI;
        this.setContainerType(this.NONE);
        this.addAsComponent(pluginUI.getComponent());
        this.setComponentEventsEnabled(pluginComponentEventsEnabled);
    }
    
    public void addAsPanel(PluginUI pluginUI, String layout)
    {
        this.pluginUI = pluginUI;
        this.setContainerType(this.PANEL);
        super.addAsPanel(pluginUI.getComponent(), pluginComponentEventsEnabled, layout);
    }
        
    public void addAsScrollPane(PluginUI pluginUI)
    {
        this.pluginUI = pluginUI;
        this.setContainerType(this.SCROLLPANE);
        super.addAsScrollPane(pluginUI.getComponent(), pluginComponentEventsEnabled);
    }
        
    public void addAsFloatingFrame(FloatingPluginUI pluginUI, PluginFloatingFrameConfigurator configurator)
    {
        this.pluginUI = pluginUI;
        this.setContainerType(this.FLOATINGFRAME);
        
        configurator.setId(this.getId());
        configurator.setName(this.getName());
        configurator.setIcon(this.getIcon());
        
        if(configurator.isToolBarAvailable())
        {
            configurator.setButtons(pluginUI.getButtons());
        }
        
        if(configurator.isMenuBarAvailable())
        {
            configurator.setMenues(pluginUI.getMenus());
        }
        
        super.addAsFloatingFrame(pluginUI.getComponent(), pluginComponentEventsEnabled, configurator, configurator.isFloatingEventsEnabled());
    }
    
    
    
    // -------------------------------------------------------------------------
        
    /** Getter for property pluginComponentEventsEnabled.
     * @return Value of property pluginComponentEventsEnabled.
     *
     */
    public boolean isPluginComponentEventsEnabled()
    {
        return pluginComponentEventsEnabled;
    }
    
    /** Setter for property pluginComponentEventsEnabled.
     * @param pluginComponentEventsEnabled New value of property pluginComponentEventsEnabled.
     *
     */
    public void setPluginComponentEventsEnabled(boolean pluginComponentEventsEnabled)
    {
        this.pluginComponentEventsEnabled = pluginComponentEventsEnabled;
    }   
    
    /** Getter for property pluginIconId.
     * @return Value of property pluginIconId.
     *
     */
    public String getIconName()
    {
        return this.iconName;
    }
    
    /** Setter for property pluginIconId.
     * @param pluginIconId New value of property pluginIconId.
     *
     */
    public void setIconName(String iconName)
    {
        this.iconName = iconName;
    }
    
    // EventHandler ------------------------------------------------------------
    
    /** Invoked when the component has been made invisible.
     *
     */
    public void componentHidden(ComponentEvent e)
    {
        pluginUI.hidden();
    }
    
    /** Invoked when the component's position changes.
     *
     */
    public void componentMoved(ComponentEvent e)
    {
        pluginUI.moved();
    }
    
    /** Invoked when the component's size changes.
     *
     */
    public void componentResized(ComponentEvent e)
    {
        pluginUI.resized();
    }
    
    /** Invoked when the component has been made visible.
     *
     */
    public void componentShown(ComponentEvent e)
    {
        pluginUI.shown();
    }
     
    /** This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *   	and the property that has changed.
     *
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        //if(evt.getPropertyName().equals(FloatingFrame.FLOATING))
        //{
            if(((Boolean)evt.getNewValue()).booleanValue())
            {
                ((FloatingPluginUI)pluginUI).floatingStarted();
            }
            else
            {
                ((FloatingPluginUI)pluginUI).floatingStopped();
            }    
        //}  
    }  
}
