package Sirius.navigator.plugin.ui;


import javax.swing.*;

import Sirius.navigator.ui.widget.*;

/**
 *
 * @author  pascal
 */
public class PluginFloatingFrameConfigurator extends FloatingFrameConfigurator
{
    
    /** Holds value of property toolBarAvailable. */
    private boolean toolBarAvailable;
    
    /** Holds value of property floatingEventsEnabled. */
    private boolean floatingEventsEnabled;
    
    /** Holds value of property menuBarAvailable. */
    private boolean menuBarAvailable;
    
    /** Creates a new instance of FloatingFrameProperties */
    public PluginFloatingFrameConfigurator()
    {
    }
    
    /** Getter for property toolBarAvailable.
     * @return Value of property toolBarAvailable.
     *
     */
    public boolean isToolBarAvailable()
    {
        return this.toolBarAvailable;
    }
    
    /** Setter for property toolBarAvailable.
     * @param toolBarAvailable New value of property toolBarAvailable.
     *
     */
    public void setToolBarAvailable(boolean toolBarAvailable)
    {
        this.toolBarAvailable = toolBarAvailable;
    }
    
    /** Getter for property floatingEventsEnabled.
     * @return Value of property floatingEventsEnabled.
     *
     */
    public boolean isFloatingEventsEnabled()
    {
        return this.floatingEventsEnabled;
    }
    
    /** Setter for property floatingEventsEnabled.
     * @param floatingEventsEnabled New value of property floatingEventsEnabled.
     *
     */
    public void setFloatingEventsEnabled(boolean floatingEventsEnabled)
    {
        this.floatingEventsEnabled = floatingEventsEnabled;
    }
    
    /** Getter for property menuBarAvailable.
     * @return Value of property menuBarAvailable.
     *
     */
    public boolean isMenuBarAvailable()
    {
        return this.menuBarAvailable;
    }
    
    /** Setter for property menuBarAvailable.
     * @param menuBarAvailable New value of property menuBarAvailable.
     *
     */
    public void setMenuBarAvailable(boolean menuBarAvailable)
    {
        this.menuBarAvailable = menuBarAvailable;
    }
    
}
