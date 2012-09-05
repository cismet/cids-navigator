/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import Sirius.server.middleware.types.MetaObject;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
// no {@link Event} since it is not (necessarily) propagated in the EDT
public final class MetaObjectChangeEvent {

    //~ Instance fields --------------------------------------------------------

    private final Object source;
    private final MetaObject oldMetaObject;
    private final MetaObject newMetaObject;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaObjectChangeEvent object.
     *
     * @param  source  the source object where the change happened, should not be <code>null</code>
     */
    public MetaObjectChangeEvent(final Object source) {
        this.source = source;
        oldMetaObject = null;
        newMetaObject = null;
    }

    /**
     * Creates a new MetaObjectChangeEvent object.
     *
     * @param  source         the source object where the change happened, should not be <code>null</code>
     * @param  oldMetaObject  the old state of the object that changed, if applicable
     * @param  newMetaObject  the new state of the object, if applicable
     */
    public MetaObjectChangeEvent(final Object source, final MetaObject oldMetaObject, final MetaObject newMetaObject) {
        this.source = source;
        this.oldMetaObject = oldMetaObject;
        this.newMetaObject = newMetaObject;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * The source where the change happened.
     *
     * @return  the source
     */
    public Object getSource() {
        return source;
    }

    /**
     * The new state of the changed object.
     *
     * @return  the new state of the changed object
     */
    public MetaObject getNewMetaObject() {
        return newMetaObject;
    }

    /**
     * The old state of the changed object.
     *
     * @return  the old state of the changed object
     */
    public MetaObject getOldMetaObject() {
        return oldMetaObject;
    }
}
