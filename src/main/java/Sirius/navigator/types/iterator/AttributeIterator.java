package Sirius.navigator.types.iterator;

/**
 *
 * @author  pascal
 */
public interface AttributeIterator
{
    public boolean hasNext();
    
    public Sirius.server.localserver.attribute.Attribute next() throws java.util.NoSuchElementException;
    
    public boolean isDistinct();
}
