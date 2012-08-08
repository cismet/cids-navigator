/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ObjectAttributeNode.java
 *
 * Created on 3. Juni 2004, 15:06
 */
package Sirius.navigator.ui.attributes;

import Sirius.navigator.connection.*;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.iterator.AttributeIterator;
import Sirius.navigator.types.iterator.AttributeRestriction;
import Sirius.navigator.types.iterator.SimpleAttributeRestriction;
import Sirius.navigator.types.iterator.SingleAttributeIterator;
import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;

import java.util.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ObjectAttributeNode extends AttributeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final MetaObject MetaObject;
    private final Icon icon;

    private final SingleAttributeIterator attributeIterator;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ObjectAttributeNode object.
     *
     * @param  name                       DOCUMENT ME!
     * @param  ignoreSubstitute           DOCUMENT ME!
     * @param  ignoreArrayHelperObjects   DOCUMENT ME!
     * @param  ignoreInvisibleAttributes  DOCUMENT ME!
     * @param  MetaObject                 DOCUMENT ME!
     */
    public ObjectAttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes,
            final MetaObject MetaObject) {
        this(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, name, MetaObject);
    }

    /**
     * Creates a new instance of ObjectAttributeNode.
     *
     * @param  name                       DOCUMENT ME!
     * @param  ignoreSubstitute           DOCUMENT ME!
     * @param  ignoreArrayHelperObjects   DOCUMENT ME!
     * @param  ignoreInvisibleAttributes  DOCUMENT ME!
     * @param  attributeId                DOCUMENT ME!
     * @param  MetaObject                 DOCUMENT ME!
     */
    public ObjectAttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes,
            final Object attributeId,
            final MetaObject MetaObject) {
        super(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, attributeId);

        this.MetaObject = MetaObject;
        this.attributeIterator = new SingleAttributeIterator(this.objectAttributeRestriction, false);

        MetaClass tempClass = null;
        Collection attributeValues = null;

        // load class icon ...
        try {
            tempClass = SessionManager.getProxy().getMetaClass(MetaObject.getClassID(), MetaObject.getDomain());
        } catch (Exception exp) {
            logger.error("could not load class for Object :" + MetaObject, exp); // NOI18N
        }

        // logger.fatal(name + " isArrayHelperObject: " + tempClass.isArrayElementLink());
        if ((tempClass != null) && (tempClass.getIconData().length > 0)) {
            this.icon = new ImageIcon(tempClass.getIconData());
        } else {
            this.icon = resource.getIcon("ClassNodeIcon.gif"); // NOI18N
        }

        // ignore array attribute nodes
        if ((tempClass != null) && this.ignoreArrayHelperObjects && tempClass.isArrayElementLink()) {
            if (logger.isDebugEnabled()) {
                logger.debug("addArrayAttributeNodes(): ignoring array helper objects '" + MetaObject.getName() + "'"); // NOI18N
            }
            final SingleAttributeIterator arrayAttributeIterator = new SingleAttributeIterator(
                    this.objectAttributeRestriction,
                    false);
            arrayAttributeIterator.init(MetaObject.getAttributes().values());
            attributeValues = new LinkedList();

            while (arrayAttributeIterator.hasNext()) {
                attributeValues.addAll(((MetaObject)arrayAttributeIterator.next().getValue()).getAttributes().values());
            }
        } else {
            attributeValues = MetaObject.getAttributes().values();
        }

        // load attributes ...
        if ((attributeValues != null) && !this.attributeIterator.init(attributeValues)) {
            logger.error("could not initialize attribute iterator"); // NOI18N
        }

        this.addAttributeNodes(this.attributeIterator);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject getMetaObject() {
        return this.MetaObject;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public java.util.Collection getAttributes() {
        return this.MetaObject.getAttributes().values();
    }
}
