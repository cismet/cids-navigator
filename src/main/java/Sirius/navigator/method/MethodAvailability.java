/*
 * Availability.java
 *
 * Created on 11. August 2005, 15:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package Sirius.navigator.method;

import java.util.*;

/**
 *
 * @author pascal
 */
public class MethodAvailability
{
    private final HashSet classKeys;
    private final long availability;
    
    /** Creates a new instance of Availability */
    public MethodAvailability(HashSet classKeys, long availability)
    {
        this.classKeys = classKeys;
        this.availability = availability;
    }

    /**
     * Getter for property availability.
     * @return Value of property availability.
     */
    public long getAvailability()
    {
        return this.availability;
    }   
    
    public HashSet getClassKeys()
    {
        return this.classKeys;
    }
    
    public boolean containsClasses(Collection classKeys)
    {
        Iterator iterator = this.classKeys.iterator();
        if(!iterator.hasNext())
        {
            return false;
        }
        
        while(iterator.hasNext())
        {
            if(!classKeys.contains(iterator.next()))
            {
                return false;
            }
        }
        
        return true;
    }
}
