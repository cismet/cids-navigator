/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import Sirius.server.middleware.types.MetaObject;

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
     * Adds the given {@link MetaObjectChangeListener} to the set of listeners.
     *
     * @param  mocL  the listener to add
     */
    public void addMetaObjectChangeListener(final MetaObjectChangeListener mocL) {
        synchronized (listeners) {
            listeners.add(mocL);
        }
    }

    /**
     * Removes the given {@link MetaObjectChangeListener} from the set of listeners.
     *
     * @param  mocL  the listener to remove
     */
    public void removeMetaObjectChangeListener(final MetaObjectChangeListener mocL) {
        synchronized (listeners) {
            listeners.remove(mocL);
        }
    }

    /**
     * Fires the given {@link MetaObjectChangeEvent} to all registered listeners. This operation assures that the <code>
     * MetaObjectChangeEvent</code> is properly initialised. In case of removed there has to be the source object and
     * the old and the new {@link MetaObject}.
     *
     * @param   moce  the <code>MetaObjectChangeEvent</code> to be fired
     *
     * @throws  IllegalArgumentException  if the given event does not have a source object or an old or a new <code>
     *                                    MetaObject</code>
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
     * Fires the given {@link MetaObjectChangeEvent} to all registered listeners. This operation assures that the <code>
     * MetaObjectChangeEvent</code> is properly initialised. In case of added there has to be the source object and the
     * new {@link MetaObject}.
     *
     * @param   moce  the <code>MetaObjectChangeEvent</code> to be fired
     *
     * @throws  IllegalArgumentException  if the given event does not have a source object or a new <code>
     *                                    MetaObject</code>
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
     * Fires the given {@link MetaObjectChangeEvent} to all registered listeners. This operation assures that the <code>
     * MetaObjectChangeEvent</code> is properly initialised. In case of removed there has to be the source object and
     * the old {@link MetaObject}.
     *
     * @param   moce  the <code>MetaObjectChangeEvent</code> to be fired
     *
     * @throws  IllegalArgumentException  if the given event does not have a source object or an old <code>
     *                                    MetaObject</code>
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
     * Assures that the source of the {@link MetaObjectChangeEvent} has been set and fires an appropriate change to all
     * listeners.
     *
     * @param   moce    DOCUMENT ME!
     * @param   change  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  if the <code>MetaObjectChangeEvent</code> does not have a source object or the
     *                                    give change object is unknown (which only happens if somebody alters the enum
     *                                    and does not add another case here).
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
            // no switch, if is faster
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
     * Returns the default instance of the <code>MetaObjectChangeSupport</code> which should be used by default
     * components to register their interest in meta object changes.
     *
     * @return  the default instance of <code>MetaObjectChangeSupport</code>
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
