/*
 * ObjectAttributeNode.java
 *
 * Created on 3. Juni 2004, 15:06
 */

package Sirius.navigator.ui.attributes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.iterator.AttributeIterator;
import Sirius.navigator.types.iterator.AttributeRestriction;
import Sirius.navigator.types.iterator.SimpleAttributeRestriction;
import Sirius.navigator.types.iterator.SingleAttributeIterator;
import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.connection.*;
import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;

/**
 *
 * @author  pascal
 */
public class ObjectAttributeNode extends AttributeNode
{
    private static final ResourceManager resource = ResourceManager.getManager();
    private final MetaObject MetaObject;
    private final Icon icon;
    
    private final SingleAttributeIterator attributeIterator;
    
    public ObjectAttributeNode(String name, boolean ignoreSubstitute, boolean ignoreArrayHelperObjects, boolean ignoreInvisibleAttributes, MetaObject MetaObject)
    {
        this(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, name, MetaObject);
    }
    
    /** Creates a new instance of ObjectAttributeNode */
    public ObjectAttributeNode(String name, boolean ignoreSubstitute, boolean ignoreArrayHelperObjects, boolean ignoreInvisibleAttributes, Object attributeId, MetaObject MetaObject )
    {
        super(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, attributeId);
        
        this.MetaObject =  MetaObject;
        this.attributeIterator = new SingleAttributeIterator(this.objectAttributeRestriction, false);
        
        
        MetaClass tempClass = null;
        Collection attributeValues = null;
        
        // load class icon ...
        try
        {
            
            tempClass = SessionManager.getProxy().getMetaClass(MetaObject.getClassID(), MetaObject.getDomain());
        }
        catch(Exception exp)
        {
            logger.error("could not load class for Object :"+MetaObject, exp);
        }
        
        //logger.fatal(name + " isArrayHelperObject: " + tempClass.isArrayElementLink());
        if (tempClass != null && tempClass.getIconData().length > 0)
        {
            this.icon =  new ImageIcon(tempClass.getIconData());
        }
        else
        {
            this.icon = resource.getIcon(resource.getString("Sirius.navigator.ui.attributes.ObjectAttributeNode.icon"));
        }
        
        // ignore array attribute nodes
        if (tempClass != null && this.ignoreArrayHelperObjects && tempClass.isArrayElementLink())
        {
            if(logger.isDebugEnabled())logger.debug("addArrayAttributeNodes(): ignoring array helper objects '" + MetaObject.getName() + "'");
            SingleAttributeIterator arrayAttributeIterator = new SingleAttributeIterator(this.objectAttributeRestriction, false);
            arrayAttributeIterator.init(MetaObject.getAttributes().values());
            attributeValues = new LinkedList();
            
            while(arrayAttributeIterator.hasNext())
            {
                attributeValues.addAll(((MetaObject)arrayAttributeIterator.next().getValue()).getAttributes().values());
            }
        }
        else
        {
            attributeValues = MetaObject.getAttributes().values();
        }
        
        // load attributes ...
        if(attributeValues != null && !this.attributeIterator.init(attributeValues))
        {
            logger.error("could not initialize attribute iterator");
        }
        
        
        this.addAttributeNodes(this.attributeIterator);
    }
    
    public MetaObject getMetaObject()
    {
        return this.MetaObject;
    }
    
    public Icon getIcon()
    {
        return this.icon;
    }
    
    public java.util.Collection getAttributes()
    {
        return this.MetaObject.getAttributes().values();
    }
}
