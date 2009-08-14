package Sirius.navigator.plugin.ui;

import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.*;

import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.method.*;
import Sirius.server.localserver.method.*;
import Sirius.navigator.resource.ResourceManager;

/**
 *
 * @author  pascal
 */
public class PluginMenuItem extends JMenuItem
{
    protected PluginMethod pluginMethod = null;
    protected Method method = null;
    
    /** Holds value of property availability. */
    private long availability = 0;
    
    public PluginMenuItem(long availability)
    {
        this.availability = availability;
        this.addActionListener(new PluginMethodInvoker());
    }
    
    /** Creates a new instance of PluginMenuItem */
    public PluginMenuItem(PluginMethod pluginMethod, long availability)
    {
        this(availability);
        this.pluginMethod = pluginMethod;
    }
    
    public PluginMenuItem(PluginMethod pluginMethod, Method method)
    {
        this.pluginMethod = pluginMethod;
        this.method = method;
        
        this.availability = MethodManager.PURE_NODE + MethodManager.OBJECT_NODE + MethodManager.CLASS_NODE;
        if(method.isMultiple())
        {
            this.availability += MethodManager.MULTIPLE;
        }
        if(method.isClassMultiple())
        {
            this.availability += MethodManager.CLASS_MULTIPLE;
        }
        
        this.addActionListener(new PluginMethodInvoker());
    }
    
    public String getId()
    {
        return this.pluginMethod.getId();
    }
    
    /** Getter for property availability.
     * @return Value of property availability.
     *
     */
    public long getAvailability()
    {
        return this.availability;
    }
    
    
    /*public void setVisible(long availability)
    {
        if((this.availability & availability) > 0 )
        {
            this.setVisible(true);
        }
        else
        {
            this.setVisible(false);
        }
    }*/
    
    private class PluginMethodInvoker implements ActionListener
    {
        /**
         * Invoked when an method occurs.
         */
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                Logger.getLogger(this.getClass()).debug("invoking method " + PluginMenuItem.this.getName());
                PluginMenuItem.this.pluginMethod.invoke();
            }
            catch(Throwable t)
            {
                Logger.getLogger(this.getClass()).error("invocation of plugin method '" + PluginMenuItem.this.getId() + "' failed", t);
                
                // XXX i18n
                JOptionPane.showMessageDialog(PluginMenuItem.this,
                        ResourceManager.getManager().getString("Sirius.navigator.plugin.ui.PluginMenuItem.actionPerformed.ErrorMessage1") + t.getMessage() +
                        ResourceManager.getManager().getString("Sirius.navigator.plugin.ui.PluginMenuItem.actionPerformed.ErrorMessage2"),
                        ResourceManager.getManager().getString("Sirius.navigator.plugin.ui.PluginMenuItem.actionPerformed.ErrorTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Getter for property method.
     * @return Value of property method.
     */
    public Sirius.server.localserver.method.Method getMethod()
    {
        return this.method;
    }
    
    /**
     * Setter for property method.
     * @param method New value of property method.
     */
    public void setMethod(Sirius.server.localserver.method.Method method)
    {
        this.method = method;
    }
}
