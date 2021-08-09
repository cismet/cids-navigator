/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class EditorClosedEvent extends EditorSavedEvent {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EditorClosedEvent object.
     *
     * @param  status  DOCUMENT ME!
     */
    public EditorClosedEvent(final EditorSaveListener.EditorSaveStatus status) {
        this(status, null);
    }

    /**
     * Creates a new EditorClosedEvent object.
     *
     * @param  status     DOCUMENT ME!
     * @param  savedBean  DOCUMENT ME!
     */
    public EditorClosedEvent(final EditorSaveListener.EditorSaveStatus status, final CidsBean savedBean) {
        super(status, savedBean);
    }
}
