package Sirius.navigator.types.iterator;



/**
 *
 * @author  pascal
 */
public interface Restriction
{
    public final static long PURE = 1;
    public final static long OBJECT = 2;
    public final static long CLASS = 4;
    
    public final static int TRUE = 1;
    public final static int FALSE = 0;
    public final static int IGNORE = -1;
    
    public long getTypeRestriction();
}
