/*
 * TreeNodeRestriction.java
 *
 * Created on 24. April 2003, 12:45
 */

package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import org.apache.log4j.Logger;


/**
 *
 * @author  pascal
 */
public class TreeNodeRestriction implements Restriction
{
    private final Logger logger = Logger.getLogger(TreeNodeRestriction.class);
    
    private final long typeRestriction;
    private final String domainRestriction;
    
    public TreeNodeRestriction()
    {
        this(PURE + OBJECT + CLASS, null);
    }
    
    /** Creates a new instance of TreeNodeRestriction */
    public TreeNodeRestriction(long typeRestriction)
    {
        this(typeRestriction, null);
    }
    
    public TreeNodeRestriction(long typeRestriction, String domainRestriction)
    {
        this.typeRestriction = typeRestriction;
        this.domainRestriction = domainRestriction;
    }
    
    public DefaultMetaTreeNode applyRestriction(Object object)
    {
        if(logger.isDebugEnabled())logger.debug("apply restriction on '" + object.toString() + "' (" + object.getClass().getName() + ")");//NOI18N
        
        DefaultMetaTreeNode node;
        if(!(object instanceof DefaultMetaTreeNode))
        {
            return null;
        }
        else
        {
            node = (DefaultMetaTreeNode)object;
        }
        
        if(node.isRootNode() || node.isWaitNode())
        {
            return null;
        }
        else if(
        ((node.isPureNode() && (PURE & typeRestriction) != 0)) ||
        ((node.isObjectNode() && (OBJECT & typeRestriction) != 0)) ||
        ((node.isClassNode() && (CLASS & typeRestriction) != 0)))
        {
            if(this.domainRestriction == null || this.domainRestriction.equals(node.getDomain()))
            {
                return node;
            }
        }
        
        return null;
    }
    
    public long getTypeRestriction()
    {
        return this.typeRestriction;
    }
    
    /*public final static void main(String args[])
    {
        TreeNodeRestriction treeNodeRestriction = new TreeNodeRestriction();
        System.out.println(treeNodeRestriction.typeRestriction);
        System.out.println(treeNodeRestriction.CLASS & treeNodeRestriction.typeRestriction);
    }*/
}
