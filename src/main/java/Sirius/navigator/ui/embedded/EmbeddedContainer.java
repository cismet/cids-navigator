package Sirius.navigator.ui.embedded;

import java.util.*;
import javax.swing.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public abstract class EmbeddedContainer implements EmbeddedComponent
{
    protected final static Logger logger = Logger.getLogger(EmbeddedContainer.class);
    
    private final String id ;
    private final Collection components;
    private boolean enabled = false;
    private boolean visible = false;
    
    /** Holds value of property name. */
    private String name;
    
    public EmbeddedContainer(String id, Collection components)
    {
        this.id = id;
        this.components = components;
    }
    
    protected abstract void addComponents();
    
    protected abstract void removeComponents();
    
    protected ComponentIterator iterator()
    {
        return new ComponentIterator(components.iterator());
    }
    
    public void setEnabled(boolean enabled)
    {
        ComponentIterator iterator = this.iterator();
        while(iterator.hasNext())
        {
            JComponent component = iterator.next();
            if(component != null)
            {
                component.setEnabled(enabled);
            }
        }
        
        this.enabled = enabled;
    }
    
    public boolean isEnabled()
    {
        return this.enabled;
    }
    
    public void setVisible(boolean visible)
    {
        if(this.isVisible() != visible)
        {
            ComponentIterator iterator = this.iterator();
            while(iterator.hasNext())
            {
                JComponent component = iterator.next();
                if(component != null)
                {
                    component.setVisible(visible);
                }
            }
            
            this.visible = visible;
        }
        else
        {
            this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'");//NOI18N
        }
    }
    
    public boolean isVisible()
    {
        return this.visible;
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
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     */
   /*public void setId(String id)
    {
        this.id = id;
    }*/
    
    protected final class ComponentIterator
    {
        Iterator iterator;
        
        private ComponentIterator(Iterator iterator)
        {
            this.iterator = iterator;
        }
        
        public boolean hasNext()
        {
            return iterator.hasNext();
        }
        
        public JComponent next()
        {
            Object next = iterator.next();
            
            if(JComponent.class.isAssignableFrom(next.getClass()))
            {
                return (JComponent)next;
            }
            else
            {
                logger.error("object '" + next  + "' is not of type 'javax.swing.JComponent' but '" + next.getClass().getName() + "'");//NOI18N
                iterator.remove();
                return null;
            }
        }
    }
}
