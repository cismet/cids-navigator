package Sirius.navigator.plugin;

import javax.swing.*;

import Sirius.navigator.plugin.*;

/**
 *
 * @author  pascal
 */
public class PluginActionDescriptor
{
    
    /** Holds value of property name. */
    private String name;
    
    /** Holds value of property mnemonic. */
    private char mnemonic;
    
    /** Holds value of property tooltip. */
    private String tooltip;
    
    /** Holds value of property iconName. */
    private String iconName;
    
    /** Holds value of property methodId. */
    private String methodId;
    
    /** Holds value of property accelerator. */
    private String accelerator;
    
    /** Holds value of property availability. */
    private byte availability;
    
    /** Holds value of property floatable. */
    private boolean floatable;
    
    /** Holds value of property separator. */
    private boolean separator;
    
    /** Creates a new instance of PluginActionDescriptor */
    public PluginActionDescriptor()
    {
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
    
    /** Getter for property mnemonic.
     * @return Value of property mnemonic.
     *
     */
    public char getMnemonic()
    {
        return this.mnemonic;
    }
    
    /** Setter for property mnemonic.
     * @param mnemonic New value of property mnemonic.
     *
     */
    public void setMnemonic(String mnemonic)
    {
        if(mnemonic != null && mnemonic.length() > 0)
        {
            this.mnemonic = mnemonic.charAt(0);
        }
    }
    
    /** Getter for property tooltip.
     * @return Value of property tooltip.
     *
     */
    public String getTooltip()
    {
        return this.tooltip;
    }
    
    /** Setter for property tooltip.
     * @param tooltip New value of property tooltip.
     *
     */
    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }
    
    /** Getter for property iconId.
     * @return Value of property iconId.
     *
     */
    public String getIconName()
    {
        return this.iconName;
    }
    
    /** Setter for property iconId.
     * @param iconId New value of property iconId.
     *
     */
    public void setIconName(String iconName)
    {
        this.iconName = iconName;
    }
    
    /** Getter for property methodId.
     * @return Value of property methodId.
     *
     */
    public String getMethodId()
    {
        return this.methodId;
    }
    
    /** Setter for property methodId.
     * @param methodId New value of property methodId.
     *
     */
    public void setMethodId(String methodId)
    {
        this.methodId = methodId;
    }
    
    /** Getter for property accelerator.
     * @return Value of property accelerator.
     *
     */
    public KeyStroke getAccelerator()
    {
        return KeyStroke.getKeyStroke(accelerator);
    }
    
    /** Setter for property accelerator.
     * @param accelerator New value of property accelerator.
     *
     */
    public void setAccelerator(String accelerator)
    {
        this.accelerator = accelerator;
    }
    
    /** Getter for property availability.
     * @return Value of property availability.
     *
     */
    public byte getAvailability()
    {
        return this.availability;
    }
    
    /** Setter for property availability.
     * @param availability New value of property availability.
     *
     */
    public void setAvailability(byte availability)
    {
        this.availability = availability;
    }
    
    /** Getter for property floatable.
     * @return Value of property floatable.
     *
     */
    public boolean isFloatable()
    {
        return this.floatable;
    }
    
    /** Setter for property floatable.
     * @param floatable New value of property floatable.
     *
     */
    public void setFloatable(boolean floatable)
    {
        this.floatable = floatable;
    }
    
    /** Getter for property separator.
     * @return Value of property separator.
     *
     */
    public boolean isSeparator()
    {
        return this.separator;
    }
    
    /** Setter for property separator.
     * @param separator New value of property separator.
     *
     */
    public void setSeparator(boolean separator)
    {
        this.separator = separator;
    }
    
    /** Setter for property separator.
     * @param separator New value of property separator.
     *
     */
    public void setSeparator(Boolean separator)
    {
        this.separator = separator.booleanValue();
    }
    
}
