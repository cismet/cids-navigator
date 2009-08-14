package Sirius.navigator.types.iterator;

import java.util.*;

import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;

/**
 *
 * @author  pascal
 */
public class MultipleAttributeIterator  implements AttributeIterator
{  
    private final SingleAttributeIterator attributeIterator;
    private final HashSet hashSet;
   
    private TreeNodeIterator treeNodeIterator = null;  
    private Sirius.server.localserver.attribute.Attribute nextElement = null;
    
    public MultipleAttributeIterator(TreeNodeIterator treeNodeIterator)
    {
        this(treeNodeIterator, new SimpleAttributeRestriction(), false);
    }
    
    public MultipleAttributeIterator(AttributeRestriction restriction, boolean distinct)
    {
        this(null, restriction, distinct);
    }
    
    public MultipleAttributeIterator(TreeNodeIterator treeNodeIterator, AttributeRestriction restriction, boolean distinct)
    {
        this.attributeIterator = new SingleAttributeIterator(restriction, false);
        if(distinct)
        {
            this.hashSet = new HashSet();
        }
        else
        {
            this.hashSet = null;   
        }
        
        this.init(treeNodeIterator);
    }
    
    public boolean init(Object object)
    {
        this.treeNodeIterator = null;
        this.nextElement = null;
        
        if(object != null && object instanceof TreeNodeIterator)
        {
            return this.init((TreeNodeIterator)object);
        }
        
        return false;
    }
    
    public boolean init(TreeNodeIterator treeNodeIterator)
    {
        this.treeNodeIterator = treeNodeIterator;
        this.nextElement = null;
        
        if(treeNodeIterator != null && treeNodeIterator.hasNext())
        {
            attributeIterator.init(treeNodeIterator.next());
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
        if(treeNodeIterator == null)
        {
            return false;   
        }
        
        if(nextElement == null && attributeIterator.hasNext())
        {
            nextElement = attributeIterator.next();
            //return true;
        }
        
        if(nextElement == null && treeNodeIterator.hasNext())
        {
            attributeIterator.init(treeNodeIterator.next());
            this.hasNext();
        }
        
        if(nextElement != null && this.isDistinct() && !hashSet.add(nextElement))
        {
            nextElement = null;
            this.hasNext();
        }
        
        return nextElement != null ? true : false;
    }
    
    public Sirius.server.localserver.attribute.Attribute next() throws NoSuchElementException
    {
        if(this.hasNext())
        {
            Sirius.server.localserver.attribute.Attribute next = nextElement;
            nextElement = null;
            return next;
        }
        else 
        {
            throw new NoSuchElementException();
        }
    }
    
    public boolean isDistinct()
    {
        return hashSet != null ? true : false;   
    }
}
