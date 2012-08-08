/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorListener.java
 *
 * Created on 11. August 2004, 14:13
 */
package Sirius.navigator.ui.attributes.editor;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 * Ein Listener f\u00FCr simple und komplexe Editoren.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 * @see      javax.swing.event.CellEditorListener
 */
public interface EditorListener extends CellEditorListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Wird ausgel\u00F6st, wenn sich das UI des Editors ge\u00E4ndert hat.
     *
     * <p>Achtung: Aus Performancegr\u00FCnden l\u00F6st ausschlieslich der Root Container diesen event aus. Ein
     * entsprechender Listener mu\u00DF also nur am Root Container registiert werden.</p>
     *
     * @param  e  DOCUMENT ME!
     */
    void uiChanged(ChangeEvent e);
}
