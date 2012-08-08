/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ClassAttributeNode.java
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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ClassAttributeNode extends AttributeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final MetaClass metaClass;
    private final Icon icon;

    private final SingleAttributeIterator attributeIterator;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ObjectAttributeNode.
     *
     * @param  name                       DOCUMENT ME!
     * @param  ignoreSubstitute           DOCUMENT ME!
     * @param  ignoreArrayHelperObjects   DOCUMENT ME!
     * @param  ignoreInvisibleAttributes  DOCUMENT ME!
     * @param  metaClass                  DOCUMENT ME!
     */
    public ClassAttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes,
            final MetaClass metaClass) {
        super(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes);

        this.metaClass = metaClass;
        this.attributeIterator = new SingleAttributeIterator(this.classAttributeRestriction, false);

        // load class icon ...
        if (this.metaClass.getIconData().length > 0) {
            this.icon = new ImageIcon(this.metaClass.getIconData());
        } else {
            this.icon = resource.getIcon("ClassNodeIcon.gif");
        }

        // load attributes ...
        this.attributeIterator.init(metaClass.getAttributes());
        this.addAttributeNodes(this.attributeIterator);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaClass getMetaClass() {
        return this.metaClass;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public java.util.Collection getAttributes() {
        return this.metaClass.getAttributes();
    }
}
