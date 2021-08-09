/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.EditorSaveListener.EditorSaveStatus;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class EditorSavedEvent {

    //~ Instance fields --------------------------------------------------------

    private EditorSaveListener.EditorSaveStatus status;
    private CidsBean savedBean;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EditorClosedEvent object.
     *
     * @param  status  DOCUMENT ME!
     */
    public EditorSavedEvent(final EditorSaveListener.EditorSaveStatus status) {
        this(status, null);
    }

    /**
     * Creates a new EditorClosedEvent object.
     *
     * @param  status     DOCUMENT ME!
     * @param  savedBean  DOCUMENT ME!
     */
    public EditorSavedEvent(final EditorSaveListener.EditorSaveStatus status, final CidsBean savedBean) {
        this.status = status;
        this.savedBean = savedBean;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBean getSavedBean() {
        return savedBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public EditorSaveStatus getStatus() {
        return status;
    }
}
