/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public final class TreeNodeIterator {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(TreeNodeIterator.class);

    //~ Instance fields --------------------------------------------------------

    private Iterator iterator;
    private final TreeNodeRestriction restriction;

    private DefaultMetaTreeNode nextElement = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreeNodeIterator object.
     *
     * @param  collection  DOCUMENT ME!
     */
    public TreeNodeIterator(final Collection collection) {
        this(collection, new TreeNodeRestriction());
    }

    /**
     * Creates a new TreeNodeIterator object.
     *
     * @param  enumeration  DOCUMENT ME!
     */
    public TreeNodeIterator(final Enumeration enumeration) {
        this(enumeration, new TreeNodeRestriction());
    }

    /**
     * Creates a new TreeNodeIterator object.
     *
     * @param  restriction  DOCUMENT ME!
     */
    public TreeNodeIterator(final TreeNodeRestriction restriction) {
        this.restriction = restriction;
        this.iterator = null;
    }

    /**
     * Creates a new instance of MetaIterator.
     *
     * @param  collection   DOCUMENT ME!
     * @param  restriction  DOCUMENT ME!
     */
    public TreeNodeIterator(final Collection collection, final TreeNodeRestriction restriction) {
        this.restriction = restriction;
        this.init(collection);
    }

    /**
     * Creates a new TreeNodeIterator object.
     *
     * @param  enumeration  DOCUMENT ME!
     * @param  restriction  DOCUMENT ME!
     */
    public TreeNodeIterator(final Enumeration enumeration, final TreeNodeRestriction restriction) {
        this.restriction = restriction;
        this.init(enumeration);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   collection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final Collection collection) {
        this.nextElement = null;
        if ((collection != null) && (collection.size() > 0)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(" init collection size: " + collection.size()); // NOI18N
            }
            this.iterator = collection.iterator();
            return true;
        } else {
            LOG.warn("could not create iterator");                        // NOI18N
            this.iterator = null;
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   enumeration  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final Enumeration enumeration) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" init enumeration hasMoreElements: " + enumeration.hasMoreElements()); // NOI18N
        }
        this.nextElement = null;
        if ((enumeration != null) && enumeration.hasMoreElements()) {
            this.iterator = new EnumerationIterator(enumeration);
            return true;
        } else {
            LOG.warn("could not create iterator");                                            // NOI18N
            this.iterator = null;
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasNext() {
        if (nextElement != null) {
            return true;
        } else if ((iterator != null) && iterator.hasNext()) {
            while (iterator.hasNext() && ((nextElement = restriction.applyRestriction(iterator.next())) == null)) {
                // noop
            }

            return (nextElement != null) ? true : false;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoSuchElementException  DOCUMENT ME!
     */
    public DefaultMetaTreeNode next() throws NoSuchElementException {
        if (this.hasNext()) {
            final DefaultMetaTreeNode next = nextElement;
            nextElement = null;

            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class EnumerationIterator implements Iterator {

        //~ Instance fields ----------------------------------------------------

        private final Enumeration enumeration;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new EnumerationIterator object.
         *
         * @param  enumeration  DOCUMENT ME!
         */
        private EnumerationIterator(final Enumeration enumeration) {
            this.enumeration = enumeration;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        @Override
        public Object next() {
            return this.enumeration.nextElement();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("this method is not implemented"); // NOI18N
        }
    }
}
