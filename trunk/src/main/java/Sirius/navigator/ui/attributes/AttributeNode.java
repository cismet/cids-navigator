/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeNode.java
 *
 * Created on 4. Mai 2004, 17:52
 */
package Sirius.navigator.ui.attributes;

import Sirius.navigator.connection.*;
import Sirius.navigator.types.iterator.AttributeIterator;
import Sirius.navigator.types.iterator.AttributeRestriction;
import Sirius.navigator.types.iterator.SimpleAttributeRestriction;
import Sirius.navigator.types.iterator.SingleAttributeIterator;
import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class AttributeNode extends DefaultMutableTreeNode {

    //~ Static fields/initializers ---------------------------------------------

    protected static final AttributeRestriction objectAttributeRestriction = new SimpleAttributeRestriction(
            AttributeRestriction.OBJECT,
            AttributeRestriction.TRUE,
            null,
            null,
            MetaObject.class);
    protected static final AttributeRestriction classAttributeRestriction = new SimpleAttributeRestriction(
            AttributeRestriction.CLASS,
            AttributeRestriction.TRUE,
            null,
            null,
            MetaObject.class);

    //~ Instance fields --------------------------------------------------------

    protected final Logger logger;

    protected Object attributeKey = null;

    /**
     * Anzeigen der Struktur komplexer Objekte.
     *
     * <p>Bei true wird die Struktur angezeigt, bei false wird die Sturktur aufgel\u00F6st und nur das flache Objekt
     * angezeigt.</p>
     */
    protected boolean ignoreSubstitute = true;

    /**
     * Anzeigen der Array Hilfsobjekte.
     *
     * <p>Bei true wird das Hilfsobjekt nicht angezeigt</p>
     */
    protected boolean ignoreArrayHelperObjects = true;

    protected boolean ignoreInvisibleAttributes = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AttributeNode object.
     *
     * @param  name  DOCUMENT ME!
     */
    public AttributeNode(final String name) {
        this(name, true, true, false, name);
    }

    /**
     * Creates a new AttributeNode object.
     *
     * @param  name                       DOCUMENT ME!
     * @param  ignoreSubstitute           DOCUMENT ME!
     * @param  ignoreArrayHelperObjects   DOCUMENT ME!
     * @param  ignoreInvisibleAttributes  DOCUMENT ME!
     */
    public AttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes) {
        this(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, name);
        if (logger.isDebugEnabled()) {
            logger.debug("AttributeNode(): this must be the root node (" + name + ")"); // NOI18N
        }
    }

    /**
     * Creates a new AttributeNode object.
     *
     * @param  name                       DOCUMENT ME!
     * @param  ignoreSubstitute           DOCUMENT ME!
     * @param  ignoreArrayHelperObjects   DOCUMENT ME!
     * @param  ignoreInvisibleAttributes  DOCUMENT ME!
     * @param  attributeKey               DOCUMENT ME!
     */
    public AttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes,
            final Object attributeKey) {
        super(name);

        this.logger = Logger.getLogger(this.getClass());
        this.attributeKey = attributeKey;
        this.ignoreSubstitute = ignoreSubstitute;
        this.ignoreArrayHelperObjects = ignoreArrayHelperObjects;
        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  attributeIterator  DOCUMENT ME!
     */
    protected void addAttributeNodes(final AttributeIterator attributeIterator) {
        while (attributeIterator.hasNext()) {
            final Attribute metaAttribute = attributeIterator.next();

            // ignorieren (isSubstitute)
            if ((this.ignoreSubstitute || !metaAttribute.isSubstitute())
                        && (this.ignoreInvisibleAttributes || metaAttribute.isVisible())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("adding new complex object '" + metaAttribute.getName() + "'");                       // NOI18N
                }
                final MetaObject childMetaObject = (MetaObject)metaAttribute.getValue();
                this.add(new ObjectAttributeNode(
                        metaAttribute.getName(),
                        this.ignoreSubstitute,
                        this.ignoreArrayHelperObjects,
                        this.ignoreInvisibleAttributes,
                        metaAttribute.getKey(),
                        childMetaObject));
            } else if (logger.isDebugEnabled()) {
                logger.warn("ignoring complex object '" + metaAttribute.getName() + "' (isSubstitute or !isVisible)"); // NOI18N
            }
        }
    }

    /**
     * Konstruiert den anzuzeigenden Attributnamen.
     *
     * @param   metaAttribute  DOCUMENT ME!
     * @param   MetaObject     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String getName(final Attribute metaAttribute, final MetaObject MetaObject) {
        if (metaAttribute.referencesObject() && metaAttribute.isArray()) {
            final StringBuffer name = new StringBuffer();
            name.append(metaAttribute.getName()).append(' ');
            name.append('(').append(MetaObject.getName()).append(')');
            return name.toString();
        } else {
            return metaAttribute.getName();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getAttributeKey() {
        return this.attributeKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract java.util.Collection getAttributes();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract Icon getIcon();
}
