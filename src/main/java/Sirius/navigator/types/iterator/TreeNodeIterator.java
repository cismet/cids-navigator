package Sirius.navigator.types.iterator;

import java.util.*;

import org.apache.log4j.Logger;

import Sirius.navigator.types.treenode.*;

/**
 *
 * @author  pascal
 */
public class TreeNodeIterator
{
    private final Logger logger = Logger.getLogger(TreeNodeIterator.class);
    
    private Iterator iterator;
    private final TreeNodeRestriction restriction;
    
    private DefaultMetaTreeNode nextElement = null;
    
    public TreeNodeIterator(Collection collection)
    {
        this(collection, new TreeNodeRestriction());
    }
    
    public TreeNodeIterator(Enumeration enumeration)
    {
        this(enumeration, new TreeNodeRestriction());
    }
    
    public TreeNodeIterator(TreeNodeRestriction restriction)
    {
        this.restriction = restriction;
        this.iterator = null;
    }
    
    /** Creates a new instance of MetaIterator */
    public TreeNodeIterator(Collection collection, TreeNodeRestriction restriction)
    {
        this.restriction = restriction;
        this.init(collection);
    }
    
    public TreeNodeIterator(Enumeration enumeration, TreeNodeRestriction restriction)
    {
        this.restriction = restriction;
        this.init(enumeration);
    }
    
    public boolean init(Collection collection)
    { 
        this.nextElement = null;
        if(collection != null && collection.size() > 0)
        {
            logger.debug(" init collection size: " + collection.size());
            this.iterator = collection.iterator();
            return true;
        }
        else
        {
            logger.warn("could not create iterator");
            this.iterator = null;
            return false;
        }
    }
       
    public boolean init(Enumeration enumeration)
    {
        logger.debug(" init enumeration hasMoreElements: " + enumeration.hasMoreElements());
        
        this.nextElement = null;
        if(enumeration != null && enumeration.hasMoreElements())
        {
            this.iterator = new EnumerationIterator(enumeration);
            return true;
        }
        else
        {
            logger.warn("could not create iterator");
            this.iterator = null;
            return false;
        }
    }
    
    public boolean hasNext()
    {
        if(nextElement != null)
        {
            return true;
        }
        else if(iterator != null && iterator.hasNext())
        {
            while(iterator.hasNext() && (nextElement = restriction.applyRestriction(iterator.next())) == null);
            
            return nextElement != null ? true : false;
        }
        
        return false;
    }
    
    public DefaultMetaTreeNode next() throws NoSuchElementException
    {
        if(this.hasNext())
        {
            DefaultMetaTreeNode next = nextElement;
            nextElement = null;
            return next;
        }
        else
        {
            throw new NoSuchElementException();
        }
    }
    
    private final class EnumerationIterator implements Iterator
    {
        private final Enumeration enumeration;
        
        private EnumerationIterator(Enumeration enumeration)
        {
            this.enumeration = enumeration;
        }
        
        public boolean hasNext()
        {
            return this.enumeration.hasMoreElements();
        }
        
        public Object next()
        {
            return this.enumeration.nextElement();
        }
        
        public void remove()
        {
            throw new UnsupportedOperationException("this method is not implemented");
        }
        
    }
}
