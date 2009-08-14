package Sirius.navigator.ui.widget;

import javax.swing.*;

/**
 *
 * @author  pascal
 */
public class FloatingFrameConfigurator
{
    
    /** Holds value of property name. */
    private String name = null;
    
    /** Holds value of property swapMenuBar. */
    private boolean swapMenuBar = false;
    
    /** Holds value of property swapToolBar. */
    private boolean swapToolBar = false;
    
    /** Holds value of property disableToolBar. */
    private boolean disableToolBar = false;
    
    /** Holds value of property disableMenuBar. */
    private boolean disableMenuBar = false;
    
    /** Holds value of property buttons. */
    private java.util.Collection buttons = null;
    
    /** Holds value of property menues. */
    private java.util.Collection menues = null;
    
    /** Holds value of property icon. */
    private ImageIcon icon = null;
    
    /** Holds value of property id. */
    private String id = null;
    
    /** Holds value of property advancedLayout. */
    private boolean advancedLayout;
    
    /**
     * Holds value of property titleBarEnabled.
     */
    private boolean titleBarEnabled = true;
    
    /** Creates a new instance of FloatingFrameProperties */
    public FloatingFrameConfigurator()
    {
        this.id = Sirius.navigator.tools.NavigatorToolkit.getToolkit().generateId();
    }
    
    /** Creates a new instance of FloatingFrameProperties */
    public FloatingFrameConfigurator(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName()
    {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    /** Getter for property swapMenuBar.
     * @return Value of property swapMenuBar.
     *
     */
    public boolean isSwapMenuBar()
    {
        if(this.getMenues() != null)
        {
            return this.swapMenuBar;
        }
        else
        {
            return false;
        }
    }
    
    /** Setter for property swapMenuBar.
     * @param swapMenuBar New value of property swapMenuBar.
     *
     */
    public void setSwapMenuBar(boolean swapMenuBar)
    {
        this.swapMenuBar = swapMenuBar;
    }
    
    /** Getter for property swapToolBar.
     * @return Value of property swapToolBar.
     *
     */
    public boolean isSwapToolBar()
    {
        if(this.getButtons() != null)
        {
            return this.swapToolBar;
        }
        else
        {
            return false;
        }
    }
    
    /** Setter for property swapToolBar.
     * @param swapToolBar New value of property swapToolBar.
     *
     */
    public void setSwapToolBar(boolean swapToolBar)
    {
        this.swapToolBar = swapToolBar;
    }
    
    /** Getter for property disableToolBar.
     * @return Value of property disableToolBar.
     *
     */
    public boolean isDisableToolBar()
    {
        if(this.getButtons() != null)
        {
            return this.disableToolBar;
        }
        else
        {
            return false;
        }
    }
    
    /** Setter for property disableToolBar.
     * @param disableToolBar New value of property disableToolBar.
     *
     */
    public void setDisableToolBar(boolean disableToolBar)
    {
        this.disableToolBar = disableToolBar;
    }
    
    /** Getter for property disableMenuBar.
     * @return Value of property disableMenuBar.
     *
     */
    public boolean isDisableMenuBar()
    {
        if(this.getMenues() != null)
        {
            return this.disableMenuBar;
        }
        else
        {
            return false;
        }
    }
    
    /** Setter for property disableMenuBar.
     * @param disableMenuBar New value of property disableMenuBar.
     *
     */
    public void setDisableMenuBar(boolean disableMenuBar)
    {
        this.disableMenuBar = disableMenuBar;
    }
    
    /** Getter for property buttons.
     * @return Value of property buttons.
     *
     */
    public java.util.Collection getButtons()
    {
        return this.buttons;
    }
    
    /** Setter for property buttons.
     * @param buttons New value of property buttons.
     *
     */
    public void setButtons(java.util.Collection buttons)
    {
        this.buttons = buttons;
    }
    
    /** Getter for property menues.
     * @return Value of property menues.
     *
     */
    public java.util.Collection getMenues()
    {
        return this.menues;
    }
    
    /** Setter for property menues.
     * @param menues New value of property menues.
     *
     */
    public void setMenues(java.util.Collection menues)
    {
        this.menues = menues;
    }
    
    /** Getter for property icon.
     * @return Value of property icon.
     *
     */
    public ImageIcon getIcon()
    {
        return this.icon;
    }
    
    /** Setter for property icon.
     * @param icon New value of property icon.
     *
     */
    public void setIcon(ImageIcon icon)
    {
        this.icon = icon;
    }  
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Getter for property advancedLayout.
     * @return Value of property advancedLayout.
     *
     */
    public boolean isAdvancedLayout()
    {
        return this.advancedLayout;
    }
    
    /** Setter for property advancedLayout.
     * @param advancedLayout New value of property advancedLayout.
     *
     */
    public void setAdvancedLayout(boolean advancedLayout)
    {
        this.advancedLayout = advancedLayout;
    }
    
    /**
     * Getter for property titleBarEnabled.
     * @return Value of property titleBarEnabled.
     */
    public boolean isTitleBarEnabled()
    {
        return this.titleBarEnabled;
    }
    
    /**
     * Setter for property titleBarEnabled.
     * @param titleBarEnabled New value of property titleBarEnabled.
     */
    public void setTitleBarEnabled(boolean titleBarEnabled)
    {
        this.titleBarEnabled = titleBarEnabled;
    }
    
}
