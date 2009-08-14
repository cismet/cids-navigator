package Sirius.navigator.plugin;


import Sirius.navigator.plugin.interfaces.*;
/**
 *
 * @author  pascal
 */
public class PluginMethodDescriptor
{
    /** Holds value of property id. */
    private final String id;
    
    /** Holds value of property name. */
    private final String name;
    
    /** Holds value of property description. */
    private final String description;
    
    /** Holds value of property method. */
    private final PluginMethod method;
    
    /** Holds value of property multithreaded. */
    private boolean multithreaded;
    
    /** Holds value of property availability. */
    private long availability;
    
    /** Creates a new instance of PluginMethodDescriptor */
    public PluginMethodDescriptor(String id, String name, String description, boolean multithreaded, long availability, PluginMethod method)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.multithreaded = multithreaded;
        this.availability = availability;
        this.method = method;
    }
    
    public PluginMethodDescriptor(String id, String name, String description, PluginMethod method)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.multithreaded = false;
        this.availability = Long.MAX_VALUE;
        this.method = method;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName()
    {
        return this.name;
    }
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /** Getter for property method.
     * @return Value of property method.
     *
     */
    public PluginMethod getMethod()
    {
        return this.method;
    }   
    
    /** Getter for property multithreaded.
     * @return Value of property multithreaded.
     *
     */
    public boolean isMultithreaded()
    {
        return this.multithreaded;
    }
        
    /** Getter for property availability.
     * @return Value of property availability.
     *
     */
    public long getAvailability()
    {
        return this.availability;
    }    
}
