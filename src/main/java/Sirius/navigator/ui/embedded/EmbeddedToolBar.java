package Sirius.navigator.ui.embedded;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class EmbeddedToolBar extends JToolBar implements EmbeddedComponent
{
    protected final static Logger logger = Logger.getLogger(EmbeddedToolBar.class);
    protected final HashSet enabledComponents = new HashSet();
    
    protected String id;
    
    public EmbeddedToolBar(String id)
    {
        this.id = id;
    }
    
    public EmbeddedToolBar(String id, Collection buttons)
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
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;;
    }
    
    public void setEnabled(boolean enabled)
    {
        if(this.isEnabled() != enabled)
        {
            Component[] components = this.getComponents();
            if(components != null && components.length > 0);
            {
                // disablen: status merken
                if(!enabled)
                {
                    this.enabledComponents.clear();
                    for(int i = 0; i < components.length; i++)
                    {
                        if(components[i].isEnabled())
                        {
                            enabledComponents.add(components[i]);
                        }

                        components[i].setEnabled(false);
                    }   
                }
                // enablen: status setzten
                else
                {
                    for(int i = 0; i < components.length; i++)
                    {
                        if(this.enabledComponents.contains(components[i]))
                        {
                            components[i].setEnabled(true);
                        }
                    }   
                }
            }
            
            super.setEnabled(enabled);
        }   
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
        if(logger.isDebugEnabled())logger.debug("adding '" + components.size() + "' toolbar buttons");//NOI18N
        Iterator iterator = components.iterator();
        
        while(iterator.hasNext())
        {
            this.addButton(iterator.next());
        }
    }
    
    public void addButton(Object button)
    {
        if(button instanceof JComponent)
        {
            /*if(((JComponent)button).isEnabled())
            {
                this.enabledComponents.add(button);
            }*/
            
            if(button instanceof JButton)
            {
                ((JButton)button).setMargin(new Insets(0,0,0,0));
                this.add((JButton)button);
            }
            else if(button instanceof JToggleButton)
            {
                ((JToggleButton)button).setMargin(new Insets(0,0,0,0));
                this.add((JToggleButton)button);
            }
            else if (button instanceof JSeparator)
            {
                this.addSeparator();
            }
            else
            {
                logger.warn("button type '" + button.getClass().getName() + "' found, 'javax.swing.JButton' or 'javax.swing.JSeparator' preferred");//NOI18N
                this.add((JComponent)button);
            }
        }
        else
        {
            logger.error("invalid button type '" + button.getClass().getName() + "', 'javax.swing.JComponent' expected");//NOI18N
        }
    }
}
