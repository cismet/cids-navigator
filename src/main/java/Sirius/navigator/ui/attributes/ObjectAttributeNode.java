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
import Sirius.navigator.types.iterator.SingleAttributeIterator;

import Sirius.server.middleware.types.*;

import java.util.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ObjectAttributeNode extends AttributeNode implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager RESOURCE = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final MetaObject MetaObject;
    private final Icon icon;

    private final SingleAttributeIterator attributeIterator;

    private final ConnectionContext connectionContext = ConnectionContext.createDummy();

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
     * @param  metaObject                 DOCUMENT ME!
     */
    public ObjectAttributeNode(final String name,
            final boolean ignoreSubstitute,
            final boolean ignoreArrayHelperObjects,
            final boolean ignoreInvisibleAttributes,
            final Object attributeId,
            final MetaObject metaObject) {
        super(name, ignoreSubstitute, ignoreArrayHelperObjects, ignoreInvisibleAttributes, attributeId);

        this.MetaObject = metaObject;
        this.attributeIterator = new SingleAttributeIterator(this.objectAttributeRestriction, false);

        MetaClass tempClass = null;
        Collection attributeValues = null;

        // load class icon ...
        try {
            tempClass = SessionManager.getProxy()
                        .getMetaClass(metaObject.getClassID(), metaObject.getDomain(), getConnectionContext());
        } catch (Exception exp) {
            logger.error("could not load class for Object :" + metaObject, exp); // NOI18N
        }

        // logger.fatal(name + " isArrayHelperObject: " + tempClass.isArrayElementLink());
        if ((tempClass != null) && (tempClass.getIconData().length > 0)) {
            this.icon = new ImageIcon(tempClass.getIconData());
        } else {
            this.icon = RESOURCE.getIcon("ClassNodeIcon.gif"); // NOI18N
        }

        // ignore array attribute nodes
        final LinkedHashMap attributes = metaObject.getAttributes();
        if (attributes != null) {
            if ((tempClass != null) && this.ignoreArrayHelperObjects && tempClass.isArrayElementLink()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("addArrayAttributeNodes(): ignoring array helper objects '" + metaObject.getName()
                                + "'"); // NOI18N
                }
                final SingleAttributeIterator arrayAttributeIterator = new SingleAttributeIterator(
                        this.objectAttributeRestriction,
                        false);
                arrayAttributeIterator.init(attributes.values());
                attributeValues = new LinkedList();

                while (arrayAttributeIterator.hasNext()) {
                    attributeValues.addAll(((MetaObject)arrayAttributeIterator.next().getValue()).getAttributes()
                                .values());
                }
            } else {
                attributeValues = attributes.values();
            }
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

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
