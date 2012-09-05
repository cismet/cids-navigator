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
// no {@link Event} since it is not (necessarily} propagated in the EDT
public final class MetaObjectChangeEvent {

    //~ Instance fields --------------------------------------------------------

    private final Object source;
    private final MetaObject oldMetaObject;
    private final MetaObject newMetaObject;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaObjectChangeEvent object.
     *
     * @param  source  DOCUMENT ME!
     */
    public MetaObjectChangeEvent(final Object source) {
        this.source = source;
        oldMetaObject = null;
        newMetaObject = null;
    }

    /**
     * Creates a new MetaObjectChangeEvent object.
     *
     * @param  source         DOCUMENT ME!
     * @param  oldMetaObject  DOCUMENT ME!
     * @param  newMetaObject  DOCUMENT ME!
     */
    public MetaObjectChangeEvent(final Object source, final MetaObject oldMetaObject, final MetaObject newMetaObject) {
        this.source = source;
        this.oldMetaObject = oldMetaObject;
        this.newMetaObject = newMetaObject;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getSource() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject getNewMetaObject() {
        return newMetaObject;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject getOldMetaObject() {
        return oldMetaObject;
    }
}
