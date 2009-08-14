/*
 * DefaultComparator.java
 *
 * Created on 20. Oktober 2005, 14:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package Sirius.navigator.connection.proxy;

import Sirius.server.middleware.types.Node;

/**
 *
 * @author pascal
 */
public class DefaultComparator implements java.util.Comparator
{
    
    /** Creates a new instance of DefaultComparator */
    public DefaultComparator()
    {
        
    }
    
    public int compare(Object o1, Object o2)
    {
        return ((Node)o1).getName().compareTo(((Node)o2).getName());
    }
    
    public boolean equals(Object obj)
    {
        return false;
    }   
}
