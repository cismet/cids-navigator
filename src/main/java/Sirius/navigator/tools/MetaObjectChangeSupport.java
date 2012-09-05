/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class MetaObjectChangeSupport {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private enum Change {

        //~ Enum constants -----------------------------------------------------

        ADDED, CHANGED, REMOVED
    }

    //~ Instance fields --------------------------------------------------------

    private final transient Set<MetaObjectChangeListener> listeners;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaObjectChangeSupport object.
     */
    public MetaObjectChangeSupport() {
        listeners = new HashSet<MetaObjectChangeListener>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  mocL  DOCUMENT ME!
     */
    public void addMetaObjectChangeListener(final MetaObjectChangeListener mocL) {
        synchronized (listeners) {
            listeners.add(mocL);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mocL  DOCUMENT ME!
     */
    public void removeMetaObjectChangeListener(final MetaObjectChangeListener mocL) {
        synchronized (listeners) {
            listeners.remove(mocL);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   moce  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void fireMetaObjectChanged(final MetaObjectChangeEvent moce) {
        if (moce.getOldMetaObject() == null) {
            throw new IllegalArgumentException(
                "fireMetaObjectChanged invocation with MetaObjectChangeEvent object without old metaobject illegal: " // NOI18N
                        + moce);
        } else if (moce.getNewMetaObject() == null) {
            throw new IllegalArgumentException(
                "fireMetaObjectChanged invocation with MetaObjectChangeEvent object without new metaobject illegal: " // NOI18N
                        + moce);
        }

        doFireMetaObjectChanged(moce, Change.ADDED);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   moce  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void fireMetaObjectAdded(final MetaObjectChangeEvent moce) {
        if (moce.getNewMetaObject() == null) {
            throw new IllegalArgumentException(
                "fireMetaObjectAdded invocation with MetaObjectChangeEvent object without new metaobject illegal: " // NOI18N
                        + moce);
        }

        doFireMetaObjectChanged(moce, Change.CHANGED);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   moce  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void fireMetaObjectRemoved(final MetaObjectChangeEvent moce) {
        if (moce.getOldMetaObject() == null) {
            throw new IllegalArgumentException(
                "fireMetaObjectRemoved invocation with MetaObjectChangeEvent object without old metaobject illegal: " // NOI18N
                        + moce);
        }

        doFireMetaObjectChanged(moce, Change.REMOVED);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   moce    DOCUMENT ME!
     * @param   change  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private void doFireMetaObjectChanged(final MetaObjectChangeEvent moce, final Change change) {
        if (moce.getSource() == null) {
            throw new IllegalArgumentException("MetaObjectChangeEvent objects without source are illegal"); // NOI18N
        }

        final Iterator<MetaObjectChangeListener> it;

        synchronized (listeners) {
            it = new HashSet<MetaObjectChangeListener>(listeners).iterator();
        }

        while (it.hasNext()) {
            if (Change.ADDED == change) {
                it.next().metaObjectAdded(moce);
            } else if (Change.CHANGED == change) {
                it.next().metaObjectChanged(moce);
            } else if (Change.REMOVED == change) {
                it.next().metaObjectRemoved(moce);
            } else {
                throw new IllegalArgumentException("unknown change: " + change); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaObjectChangeSupport getDefault() {
        return LazyInitialiser.INSTANCE;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final MetaObjectChangeSupport INSTANCE = new MetaObjectChangeSupport();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
