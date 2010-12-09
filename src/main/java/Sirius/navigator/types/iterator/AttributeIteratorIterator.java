/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class AttributeIteratorIterator {

    //~ Instance fields --------------------------------------------------------

    private TreeNodeIterator treeNodeIterator = null;
    private final SingleAttributeIterator attributeIterator;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AttributeIteratorIterator object.
     *
     * @param  treeNodeIterator  DOCUMENT ME!
     */
    public AttributeIteratorIterator(final TreeNodeIterator treeNodeIterator) {
        // this.treeNodeIterator = treeNodeIterator;
        // this.attributeIterator = new SingleAttributeIterator();
        this(treeNodeIterator, new SimpleAttributeRestriction(), false);
    }

    /**
     * Creates a new AttributeIteratorIterator object.
     *
     * @param  restriction  DOCUMENT ME!
     * @param  distinct     DOCUMENT ME!
     */
    public AttributeIteratorIterator(final AttributeRestriction restriction, final boolean distinct) {
        this(null, restriction, distinct);
    }

    /**
     * Creates a new AttributeIteratorIterator object.
     *
     * @param  treeNodeIterator  DOCUMENT ME!
     * @param  restriction       DOCUMENT ME!
     * @param  distinct          DOCUMENT ME!
     */
    public AttributeIteratorIterator(final TreeNodeIterator treeNodeIterator,
            final AttributeRestriction restriction,
            final boolean distinct) {
        // this.treeNodeIterator = treeNodeIterator;
        this.attributeIterator = new SingleAttributeIterator(restriction, distinct);
        this.init(treeNodeIterator);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   object  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final Object object) {
        this.treeNodeIterator = null;

        if ((object != null) && (object instanceof TreeNodeIterator)) {
            return this.init((TreeNodeIterator)object);
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   treeNodeIterator  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(TreeNodeIterator treeNodeIterator) {
        this.treeNodeIterator = treeNodeIterator;

        if ((treeNodeIterator != null) && treeNodeIterator.hasNext()) {
            return true;
        } else {
            treeNodeIterator = null;
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasNext() {
        if (treeNodeIterator != null) {
            return treeNodeIterator.hasNext();
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoSuchElementException  DOCUMENT ME!
     */
    public AttributeIterator next() throws NoSuchElementException {
        if (this.hasNext()) {
            attributeIterator.init(treeNodeIterator.next());
            return attributeIterator;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isDistinct() {
        return attributeIterator.isDistinct();
    }
}
