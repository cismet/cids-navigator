/*
 * ComplexAttributeRestriction.java
 *
 * Created on 5. Mai 2004, 09:33
 */

package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;

import java.util.Collection;
/**
 *
 * @author  pascal
 */
public class ComplexAttributeRestriction implements AttributeRestriction
{
    
    private final long typeRestriction;
    
    private final int visible;
    private final Collection ids;
    private final Collection names;
    private final Collection classes;
        
    /**
     * Restrict to any visible object attributes
     */
    public ComplexAttributeRestriction()
    {
        this(OBJECT, TRUE, null, null, null);
    } 
    
    public ComplexAttributeRestriction(long typeRestriction, int visible)
    {
        this(typeRestriction, visible, null, null, null);
    }
    
    public ComplexAttributeRestriction(long typeRestriction, int visible, Collection ids, Collection names, Collection classes)
    {
        this.typeRestriction = typeRestriction;
        this.visible = visible;
        this.ids = ids;
        this.names = names;
        this.classes = classes;
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
        if(((this.visible == IGNORE) || (this.visible == FALSE && !attribute.isVisible()) || (this.visible == TRUE && attribute.isVisible())))  
        {
            if(this.ids == null || this.ids.contains(attribute.getID()))
            {
                if(names == null || names.size() == 0 || names.contains(attribute.getName()))
                {
                    if(classes == null || (attribute.getValue() != null && classes.contains(attribute.getValue().getClass())))
                    {
                        return attribute;
                    }
                }
            }
            else if(names == null || names.size() == 0 || names.contains(attribute.getName()))
            {
                // XXX 
                // classes.contains ..  aweia!
                if(classes == null || (attribute.getValue() != null && classes.contains(attribute.getValue().getClass())))
                {
                    return attribute;
                }
            }
            else if(classes == null || (attribute.getValue() != null && classes.contains(attribute.getValue().getClass())))
            {
                return attribute;
            }  
        }
        
        return null;
    }
    
    public long getTypeRestriction()
    {
        return this.typeRestriction;
    } 
    
}
