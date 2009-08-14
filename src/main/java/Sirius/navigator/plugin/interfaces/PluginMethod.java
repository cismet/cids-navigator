package Sirius.navigator.plugin.interfaces;

/**
 *
 * @author  pascal
 */
public interface PluginMethod
{
    /**
     * Returns the unique id of this action
     */
    public String getId();
    
    /**
     * Performs the plugin action
     */
    public void invoke() throws Exception;   
}
