/*
 * AttributeNode.java
 *
 * Created on 4. Mai 2004, 17:52
 */

package Sirius.navigator.ui.attributes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import Sirius.navigator.types.iterator.AttributeIterator;
import Sirius.navigator.types.iterator.AttributeRestriction;
import Sirius.navigator.types.iterator.SimpleAttributeRestriction;
import Sirius.navigator.types.iterator.SingleAttributeIterator;
import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.connection.*;
import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;


/**
 *
 * @author  pascal
 */
public abstract class AttributeNode extends DefaultMutableTreeNode
{
    protected final Logger logger;
    
    protected final static AttributeRestriction objectAttributeRestriction = new SimpleAttributeRestriction(AttributeRestriction.OBJECT, AttributeRestriction.TRUE, null, null, MetaObject.class);
    protected final static AttributeRestriction classAttributeRestriction = new SimpleAttributeRestriction(AttributeRestriction.CLASS, AttributeRestriction.TRUE, null, null, MetaObject.class);
    
    protected Object attributeKey = null;
    
    /**
     * Anzeigen der Struktur komplexer Objekte.<p>
     * Bei true wird die Struktur angezeigt, bei false wird die Sturktur aufgel\u00F6st
     * und nur das flache Objekt angezeigt.
     */
    protected boolean ignoreSubstitute = true;
    
    /**
     * Anzeigen der Array Hilfsobjekte.<p>
     * Bei true wird das Hilfsobjekt nicht angezeigt
     */
    protected boolean ignoreArrayHelperObjects = true;
    
    
    protected boolean ignoreInvisibleAttributes = false;
    
    public AttributeNode(String name, boolean ignoreSubstitute, boolean ignoreArrayHelperObjects, boolean ignoreInvisibleAttributes, Object attributeKey)
    {
        super(name);
        
        this.logger = Logger.getLogger(this.getClass());
        this.attributeKey = attributeKey;
        this.ignoreSubstitute = ignoreSubstitute;
        this.ignoreArrayHelperObjects =  ignoreArrayHelperObjects;
        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }
    
    public AttributeNode(String name, boolean ignoreSubstitute,  boolean ignoreArrayHelperObjects, boolean ignoreInvisibleAttributes)
    {
        this(name, ignoreSubstitute,  ignoreArrayHelperObjects, ignoreInvisibleAttributes, name);
        if(logger.isDebugEnabled())logger.debug("AttributeNode(): this must be the root node (" + name + ")");//NOI18N
    }
    
    public AttributeNode(String name)
    {
        this(name, true,  true, false, name);
    }
    
    protected void addAttributeNodes(AttributeIterator attributeIterator)
    {
        while(attributeIterator.hasNext())
        {
            Attribute metaAttribute = attributeIterator.next();
            
            // ignorieren (isSubstitute)
            if((this.ignoreSubstitute || !metaAttribute.isSubstitute()) && (this.ignoreInvisibleAttributes || metaAttribute.isVisible()))
            {
                if(logger.isDebugEnabled())logger.debug("adding new complex object '" + metaAttribute.getName() + "'");//NOI18N
                MetaObject childMetaObject = (MetaObject)metaAttribute.getValue();
                this.add(new ObjectAttributeNode(metaAttribute.getName(), this.ignoreSubstitute, this.ignoreArrayHelperObjects, this.ignoreInvisibleAttributes, metaAttribute.getKey(), childMetaObject));
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("ignoring complex object '" + metaAttribute.getName() + "' (isSubstitute or !isVisible)");//NOI18N
            }
        }
    }
        
    /**
     * Konstruiert den anzuzeigenden Attributnamen
     */
    protected String getName(Attribute metaAttribute, MetaObject MetaObject)
    {
        if(metaAttribute.referencesObject() && metaAttribute.isArray())
        {
            StringBuffer name = new StringBuffer();
            name.append(metaAttribute.getName()).append(' ');
            name.append('(').append(MetaObject.getName()).append(')');
            return name.toString();
        }
        else
        {
            return metaAttribute.getName();
        }
    }
    
    public Object getAttributeKey()
    {
        return this.attributeKey;
    }
    
    public abstract java.util.Collection getAttributes();
    
    public abstract Icon getIcon();
}
