/*
 * SimpleAttributeRestriction.java
 *
 * Created on 4. Mai 2004, 18:00
 */

package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;

import java.util.Collection;

/**
 *
 * @author  pascal
 */
public class SimpleAttributeRestriction implements AttributeRestriction
{
    private final long typeRestriction;
    
    private final int visible;
    private final String id;
    private final String name;
    private final Class instance;
        
    /**
     * Restrict to any visible object attributes
     */
    public SimpleAttributeRestriction()
    {
        this(OBJECT, TRUE, null, null, null);
    } 
    
    public SimpleAttributeRestriction(long typeRestriction, int visible)
    {
        this(typeRestriction, visible, null, null, null);
    }
    
    public SimpleAttributeRestriction(long typeRestriction, int visible, String id, String name, Class instance)
    {
        this.typeRestriction = typeRestriction;
        this.visible = visible;
        this.id = id;
        this.name = name;
        this.instance = instance;
    }
    
    
    /*public AttributeRestriction(long typeRestriction, int visible, int coordinate, int id, String name)
    {
        this.typeRestriction = typeRestriction;
        this.visible = visible;
        this.coordinate = coordinate;
        this.id = id;
        this.name = name;
    }*/

    /*public Sirius.server.localserver.attribute.Attribute applyRestriction(Sirius.server.localserver.attribute.Attribute attribute)
    {
        if( ((this.visible == IGNORE) || (this.visible == FALSE && !attribute.isVisible()) || (this.visible == TRUE && attribute.isVisible())) &&
            ((this.coordinate == IGNORE) || (this.coordinate == FALSE && !attribute.isCoordinate()) || (this.coordinate == TRUE && attribute.isCoordinate())) &&
            ((this.id == IGNORE) || this.id == attribute.getID()) &&
            ((this.name == null) || this.name.equalsIgnoreCase(attribute.getName())))
        {
            return attribute;
        }
        else
        {
            return null;
        }
    }*/
    
    public Sirius.server.localserver.attribute.Attribute applyRestriction(Sirius.server.localserver.attribute.Attribute attribute)
    {
        if( ((this.visible == IGNORE) || (this.visible == FALSE && !attribute.isVisible()) || (this.visible == TRUE && attribute.isVisible())) && ((this.id == null) || this.id.equals(attribute.getID())))
        {
            if(name == null || name.equalsIgnoreCase(attribute.getName()))
            {
                if(instance == null || (attribute.getValue() != null && instance.isAssignableFrom(attribute.getValue().getClass())))
                {
                    return attribute;
                }
            }
            else if(name == null && (instance == null || (attribute.getValue() != null && instance.isAssignableFrom(attribute.getValue().getClass()))))
            {
                return attribute;
            }
            /*else
            {
                
                for(int i = 0; i < names.length; i++)
                {
                    if(attribute.getName().trim().equalsIgnoreCase((names[i])))
                    {
                        return attribute;
                    }
                }
            }*/
        }
        
        return null;
    }
    
    public long getTypeRestriction()
    {
        return this.typeRestriction;
    } 
}
