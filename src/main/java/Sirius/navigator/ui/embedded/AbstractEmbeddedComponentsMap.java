package Sirius.navigator.ui.embedded;

import java.util.*;
import javax.swing.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public abstract class AbstractEmbeddedComponentsMap extends HashMap implements EmbeddedComponentsMap
{
    protected Logger logger = Logger.getLogger(AbstractEmbeddedComponentsMap.class);
    
    /** Creates a new instance of AbstractEmbeddedComponentsMap */
    public AbstractEmbeddedComponentsMap()
    {
        super();
    }
    
    public synchronized void add(final EmbeddedComponent component)
    {
        if(logger.isDebugEnabled())logger.debug("adding new component '" + component.getName() + "' : '" + component.getId() + "' (" + component.getClass().getName() + ")");
        
        if(!this.isAvailable(component.getId()))
        {
            this.put(component.getId(), component);

            if(SwingUtilities.isEventDispatchThread())
            {
                doAdd(component);
            }
            else
            {
                logger.debug("add(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doAdd(component);
                    }
                });
            }
        }
        else
        {
            logger.warn("add(): component '" + component.getId() + "' already in map");
        }
    }
    
    protected abstract void doAdd(EmbeddedComponent component);

    
    /** Getter for property name.
     * @return Value of property name.
     *
     *
     */
    public String getName(String id)
    {
        if(isAvailable(id))
        {
            return this.get(id).getName();
        }
        else
        {
            logger.warn("getName(): component '" + id + "' not found");
            return null;
        }
    }
    
    /** Getter for property enabled.
     * @return Value of property enabled.
     *
     *
     */
    public boolean isEnabled(String id)
    {
        if(isAvailable(id))
        {
            return this.get(id).isEnabled();
        }
        else
        {
            logger.warn("isEnabled(): component '" + id + "' not found");
            return false;
        }
    }
    
    /** Getter for property visible.
     * @return Value of property visible.
     *
     *
     */
    public boolean isVisible(String id)
    {
        if(isAvailable(id))
        {
            return this.get(id).isVisible();
        }
        else
        {
            logger.warn("isVisible(): component '" + id + "' not found");
            return false;
        }
    }
    
    public synchronized void remove(String id)
    {
        if(logger.isDebugEnabled())logger.debug("removing component '" + id + "'");
        
        if(this.isAvailable(id))
        { 
            final EmbeddedComponent component = (EmbeddedComponent)super.remove(id);
            if(SwingUtilities.isEventDispatchThread())
            {
                doRemove(component);
            }
            else
            {
                logger.debug("remove(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doRemove(component);
                    }
                });
            }
        }
        else
        {
            logger.warn("remove(): component '" + id + "' not found");
        }
    }
    
    protected abstract void doRemove(EmbeddedComponent component);
  
    
    /** Setter for property enabled.
     * @param enabled New value of property enabled.
     *
     *
     */
    public synchronized void setEnabled(final String id, final boolean enabled)
    {
        if(isAvailable(id))
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                doSetEnabled(this.get(id), enabled);
            }
            else
            {
                logger.debug("setEnabled(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doSetEnabled(get(id), enabled);
                    }
                });
            }
        }
        else
        {
            logger.warn("setEnabled(): component '" + id + "' not found");
        }
    }
    
    protected void doSetEnabled(EmbeddedComponent component, boolean enabled)
    {
        component.setEnabled(enabled);
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     *
     */
    /*public synchronized void setName(final String id, final String name)
    {
        if(isAvailable(id))
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                doSetName(this.get(id), name);
            }
            else
            {
                logger.debug("setName(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doSetName(get(id), name);
                    }
                });
            }
        }
        else
        {
            logger.warn("setName(): component '" + id + "' not found");
        }
    }
    
    protected void doSetName(EmbeddedComponent component, String name)
    {
        component.setName(name);
    }*/
    
    /** Setter for property visible.
     * @param visible New value of property visible.
     *
     *
     */
    public synchronized void setVisible(final String id, final boolean visible)
    {
        if(isAvailable(id))
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                doSetVisible(this.get(id), visible);
            }
            else
            {
                logger.debug("setVisible(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doSetVisible(get(id), visible);
                    }
                });
            }
        }
        else
        {
            logger.warn("setVisible(): component '" + id + "' not found");
        }
    }
    
    protected void doSetVisible(EmbeddedComponent component, boolean visible)
    {
        component.setVisible(visible);
    }
    
    public EmbeddedComponent get(String id)
    {
        if(logger.isDebugEnabled())logger.debug("retrieving component: '" + id + "'");
        
        if(isAvailable(id))
        {
            return (EmbeddedComponent)super.get(id);
        }
        else
        {
             logger.warn("get(): component '" + id + "' not found");
             return null;
        }
    }
    
    public boolean isAvailable(String id)
    {
        return this.containsKey(id);
    }
    
    public Iterator getEmbeddedComponents()
    {
        return this.values().iterator();
    }
}
