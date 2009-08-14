/*
 * NodeSorter.java
 *
 * Created on 26. Juni 2007, 15:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Sirius.navigator.tools;

import Sirius.server.middleware.types.*;
import Sirius.util.NodeComparator;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author schlob
 */
public class NodeSorter
{
    
    protected final static Logger logger = Logger.getLogger(NodeSorter.class);
    
    public final static boolean ASCENDING=true;
    
    public final static boolean DESCENDING=false;
    
    public final static String DEFAULT_COMPARATOR="Sirius.util.NodeComparator";
    
    
    
    private NodeSorter()
    {
    }
    
    
    public static Node[] sortNodes(Node[] nodes)
    {
        Comparator comparator = new NodeComparator();
        Arrays.sort(nodes, comparator);
        return nodes;
        
    }
    
    public static Node[] sortNodes(Node[] nodes, String comparatorClass, boolean ascending) throws Throwable
    {
        
        
        
        if(comparatorClass == null|| comparatorClass.length() == 0)
            comparatorClass = DEFAULT_COMPARATOR;
//
//            if(comparatorCache.containsKey(comparatorClass))
//            {
//                comparator = (Comparator)comparatorCache.get(comparatorClass);
//            }
//            else
//            {
        
        Comparator comparator = null;
        try
        {
            logger.info("creating new comparator instance: comparatorClass");
            comparator = (Comparator)Class.forName(comparatorClass).newInstance();
        }
        catch(Throwable t)
        {
            logger.error("could not create comparator class '" + comparatorClass + "', sorting aborted");
            return nodes;
        }
//            }
        
        
        if(logger.isDebugEnabled())logger.debug("sorting nodes ascending by comparator '" + comparatorClass + "'");
        
        Arrays.sort(nodes, comparator);
        
        if(ascending)
        {
            return nodes;
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("sorting nodes descending by comparator '" + comparatorClass + "'");
            
            // Arrays.sort(nodes, comparator);
            Node[] tempNodes = new Node[nodes.length];
            
            for (int i = 0; i < tempNodes.length; i++)
            {
                tempNodes[i] = nodes[tempNodes.length - i - 1];
            }
            
            return tempNodes;
        }
    }
}
