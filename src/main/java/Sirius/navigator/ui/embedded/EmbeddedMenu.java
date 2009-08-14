package Sirius.navigator.ui.embedded;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import org.apache.log4j.Logger;


/**
 *
 * @author  pascal
 */
public class EmbeddedMenu extends JMenu implements EmbeddedComponent
{
    protected final static Logger logger = Logger.getLogger(EmbeddedMenu.class);
    
    protected final String id;
    
    public EmbeddedMenu(String id)
    {
        this.id = id;
    }
    
    public EmbeddedMenu(String id, Collection buttons)
    {
        this.id = id;
        this.add(buttons);
    }
    
    /** Getter for property id.
     * @return Value of property id.
     *
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     *
     */
    /*public void setId(String id)
    {
        this.id = id;
    }*/
    
    public void setEnabled(boolean enabled)
    {
         Component[] components = this.getComponents();
         if(components != null && components.length > 0);
         for(int i = 0; i < components.length; i++)
         {
            components[i].setEnabled(enabled);
         }
         
         super.setEnabled(enabled);
    }
    
    public void setVisible(boolean visible)
    {
         Component[] components = this.getComponents();
         if(components != null && components.length > 0);
         for(int i = 0; i < components.length; i++)
         {
            components[i].setVisible(visible);
         }
         
         super.setVisible(visible);
    }
    
    protected void add(Collection components)
    {
        if(logger.isDebugEnabled())logger.debug("adding '" + components.size() + "' menu items");
        Iterator iterator = components.iterator();
        
        while(iterator.hasNext())
        {
            this.addItem(iterator.next()); 
        }
    }   
    
    public void addItem(Object item)
    {
        if(item instanceof JComponent)
        {
            if(item instanceof JMenuItem)
            {
                this.add((JMenuItem)item);
            }
            else if (item instanceof JSeparator)
            {
                this.addSeparator();
            }
            else
            {
                logger.warn("item type '" + item.getClass().getName() + "' found, 'javax.swing.JMenuItem' or 'javax.swing.JSeparator' preferred");
                this.add((JComponent)item);
            }
        }
        else
        {
            logger.error("invalid item type '" + item.getClass().getName() + "', 'javax.swing.JComponent' expected");
        }
    }
}
