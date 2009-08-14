/*
 * ClassAttributeNode.java
 *
 * Created on 3. Juni 2004, 15:06
 */

package Sirius.navigator.ui.attributes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

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
public class ClassAttributeNode extends AttributeNode
{
    private final MetaClass metaClass;
    private final Icon icon;
    
    private final SingleAttributeIterator attributeIterator;
    
    /** Creates a new instance of ObjectAttributeNode */
    public ClassAttributeNode(String name, boolean ignoreSubstitute, boolean ignoreArrayHelperObjects, boolean ignoreInvisibleAttributes, MetaClass metaClass)
    {
        super(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes);
        
        this.metaClass =  metaClass;
        this.attributeIterator = new SingleAttributeIterator(this.classAttributeRestriction, false);
        
        // load class icon ...
        if (this.metaClass.getIconData().length > 0)
        {
            this.icon =  new ImageIcon(this.metaClass.getIconData());
        }
        else
        {
            this.icon = ResourceManager.getManager().getIcon("ClassNodeIcon.gif");
        }
        
        // load attributes ...
        this.attributeIterator.init(metaClass.getAttributes());
        this.addAttributeNodes(this.attributeIterator);
    }
    
    public MetaClass getMetaClass()
    {
        return this.metaClass;
    }
    
    public Icon getIcon()
    {
        return this.icon;
    }
    
    public java.util.Collection getAttributes()
    {
        return this.metaClass.getAttributes();
    }
}
