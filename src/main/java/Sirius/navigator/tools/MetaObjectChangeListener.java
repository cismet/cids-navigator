/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import Sirius.server.middleware.types.MetaObject;

import java.util.EventListener;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */

/* this is only an eventlistener so that it can be used with common listener factilites,
 * it won't propagate {@link Event}s or {@link AWTEvent}s
 */
public interface MetaObjectChangeListener extends EventListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Shall be invoked if a {@link MetaObject} is added. It is not guaranteed that the call is made on the EDT. Callers
     * should obey the semantics of an added-call: This means that the given event object should contain the source
     * object and the new object.
     *
     * @param  moce  the event object related to the add
     */
    void metaObjectAdded(MetaObjectChangeEvent moce);

    /**
     * Shall be invoked if a {@link MetaObject} is changed. It is not guaranteed that the call is made on the EDT.
     * Callers should obey the semantics of a changed-call: This means that the given event object should contain the
     * source object and the old and the new object.
     *
     * @param  moce  the event object related to the change
     */
    void metaObjectChanged(MetaObjectChangeEvent moce);

    /**
     * Shall be invoked if a {@link MetaObject} is removed. It is not guaranteed that the call is made on the EDT.
     * Callers should obey the semantics of a removed-call: This means that the given event object should contain the
     * source object and the old object.
     *
     * @param  moce  the event object related to the change
     */
    void metaObjectRemoved(MetaObjectChangeEvent moce);
}
