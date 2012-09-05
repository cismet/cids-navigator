/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

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
     * DOCUMENT ME!
     *
     * @param  moce  DOCUMENT ME!
     */
    void metaObjectAdded(MetaObjectChangeEvent moce);

    /**
     * DOCUMENT ME!
     *
     * @param  moce  DOCUMENT ME!
     */
    void metaObjectChanged(MetaObjectChangeEvent moce);

    /**
     * DOCUMENT ME!
     *
     * @param  moce  DOCUMENT ME!
     */
    void metaObjectRemoved(MetaObjectChangeEvent moce);
}
