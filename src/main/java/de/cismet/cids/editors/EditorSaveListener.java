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
 * @author      stefan
 * @version     $Revision$, $Date$
 * @deprecated  use SavePreparingEditor, EditorCloseListener and SaveVetoable instead
 */
@Deprecated
public interface EditorSaveListener {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Deprecated
    public enum EditorSaveStatus {

        //~ Enum constants -----------------------------------------------------

        CANCELED, SAVE_SUCCESS, SAVE_ERROR
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use BeforeSavingHook.beforeSavig and SaveVetoable.isOkForSaving() instead
     */
    @Deprecated
    boolean prepareForSave();

    /**
     * DOCUMENT ME!
     *
     * @param       event  status DOCUMENT ME!
     *
     * @deprecated  use AfterSavingHook.afterSaving() and AfterClosingHool.afterClosing() instead
     */
    @Deprecated
    void editorClosed(EditorClosedEvent event);
}
