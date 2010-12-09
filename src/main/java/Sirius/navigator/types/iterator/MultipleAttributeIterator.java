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
public class MultipleAttributeIterator implements AttributeIterator {

    //~ Instance fields --------------------------------------------------------

    private final SingleAttributeIterator attributeIterator;
    private final HashSet hashSet;

    private TreeNodeIterator treeNodeIterator = null;
    private Sirius.server.localserver.attribute.Attribute nextElement = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultipleAttributeIterator object.
     *
     * @param  treeNodeIterator  DOCUMENT ME!
     */
    public MultipleAttributeIterator(final TreeNodeIterator treeNodeIterator) {
        this(treeNodeIterator, new SimpleAttributeRestriction(), false);
    }

    /**
     * Creates a new MultipleAttributeIterator object.
     *
     * @param  restriction  DOCUMENT ME!
     * @param  distinct     DOCUMENT ME!
     */
    public MultipleAttributeIterator(final AttributeRestriction restriction, final boolean distinct) {
        this(null, restriction, distinct);
    }

    /**
     * Creates a new MultipleAttributeIterator object.
     *
     * @param  treeNodeIterator  DOCUMENT ME!
     * @param  restriction       DOCUMENT ME!
     * @param  distinct          DOCUMENT ME!
     */
    public MultipleAttributeIterator(final TreeNodeIterator treeNodeIterator,
            final AttributeRestriction restriction,
            final boolean distinct) {
        this.attributeIterator = new SingleAttributeIterator(restriction, false);
        if (distinct) {
            this.hashSet = new HashSet();
        } else {
            this.hashSet = null;
        }

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
        this.nextElement = null;

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
        this.nextElement = null;

        if ((treeNodeIterator != null) && treeNodeIterator.hasNext()) {
            attributeIterator.init(treeNodeIterator.next());
            return true;
        } else {
            treeNodeIterator = null;
            return false;
        }
    }

    @Override
    public boolean hasNext() {
        if (treeNodeIterator == null) {
            return false;
        }

        if ((nextElement == null) && attributeIterator.hasNext()) {
            nextElement = attributeIterator.next();
            // return true;
        }

        if ((nextElement == null) && treeNodeIterator.hasNext()) {
            attributeIterator.init(treeNodeIterator.next());
            this.hasNext();
        }

        if ((nextElement != null) && this.isDistinct() && !hashSet.add(nextElement)) {
            nextElement = null;
            this.hasNext();
        }

        return (nextElement != null) ? true : false;
    }

    @Override
    public Sirius.server.localserver.attribute.Attribute next() throws NoSuchElementException {
        if (this.hasNext()) {
            final Sirius.server.localserver.attribute.Attribute next = nextElement;
            nextElement = null;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean isDistinct() {
        return (hashSet != null) ? true : false;
    }
}
