package Sirius.navigator.types.iterator;

import java.util.*;

import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;

/**
 *
 * @author  pascal
 */
public class AttributeIteratorIterator
{
    private TreeNodeIterator treeNodeIterator = null;   
    private final SingleAttributeIterator attributeIterator;
    
    public AttributeIteratorIterator(TreeNodeIterator treeNodeIterator)
    {
        //this.treeNodeIterator = treeNodeIterator;
        //this.attributeIterator = new SingleAttributeIterator();
        this(treeNodeIterator, new SimpleAttributeRestriction(), false);
    }
    
    public AttributeIteratorIterator(AttributeRestriction restriction, boolean distinct)
    {
        this(null, restriction, distinct);
    }
    
    public AttributeIteratorIterator(TreeNodeIterator treeNodeIterator, AttributeRestriction restriction, boolean distinct)
    {
        //this.treeNodeIterator = treeNodeIterator;
        this.attributeIterator = new SingleAttributeIterator(restriction, distinct);
        this.init(treeNodeIterator);
    }
    
    public boolean init(Object object)
    {
        this.treeNodeIterator = null;
        
        if(object != null && object instanceof TreeNodeIterator)
        {
            return this.init((TreeNodeIterator)object);
        }
        
        return false;
    }
    
    public boolean init(TreeNodeIterator treeNodeIterator)
    {
        this.treeNodeIterator = treeNodeIterator;
        
        if(treeNodeIterator != null && treeNodeIterator.hasNext())
        {
            return true;
        }
        else
        {
            treeNodeIterator = null;
            return false;
        } 
    }

    public boolean hasNext()
    {
        if(treeNodeIterator != null)
        {
            return treeNodeIterator.hasNext();
        }
        else
        {
            return false;
        }
    }
    
    public AttributeIterator next() throws NoSuchElementException
    {
        if(this.hasNext())
        {
            attributeIterator.init(treeNodeIterator.next());
            return attributeIterator;
        }
        else
        {
            throw new NoSuchElementException();
        }
    }
    
    public boolean isDistinct()
    {
        return attributeIterator.isDistinct();
    }
}
