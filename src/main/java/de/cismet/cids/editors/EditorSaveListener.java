/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public interface EditorSaveListener {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum EditorSaveStatus {

        //~ Enum constants -----------------------------------------------------

        CANCELED, SAVE_SUCCESS, SAVE_ERROR
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  event  status DOCUMENT ME!
     */
    void editorClosed(EditorClosedEvent event);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean prepareForSave();
}
