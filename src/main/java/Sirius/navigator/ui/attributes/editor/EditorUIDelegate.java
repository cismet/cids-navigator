/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorUIDelegate.java
 *
 * Created on 20. August 2004, 11:31
 */
package Sirius.navigator.ui.attributes.editor;

import java.awt.*;

/**
 * Delegiert die Methoden zum Anzeigen / Verstecken eines komplexen / einfachen Editors im UI eines komplexen Editors,
 * bzw. Containers.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface EditorUIDelegate {

    //~ Instance fields --------------------------------------------------------

    String ACTIVE_CHILD_EDITOR = "ActiveChildEditor"; // NOI18N

    //~ Methods ----------------------------------------------------------------

    /**
     * Zeigt das komplexe Editor UI in diesem Editor an.
     *
     * <p>Entweder wird das aktuelle Editor UI durch das komplexe Editor UI ersetzt (nur sinnvoll, wenn das aktuelle
     * Editor UI ein komplexes Editor UI ist), oder es \u00F6ffnet sich eine (modale) Dialogbox, in der das komplexe
     * Editor UI angezeigt wird (sinvoll, wenn das aktuelle Editor UI ein simples Editor UI ist).</p>
     *
     * @param   complexChildEditorComponent  die komponenten die das UI des komplexen Editors repr\u00E4sentiert
     * @param   complexChildEditorId         DOCUMENT ME!
     *
     * @return  true, wenn der Vorgang erfolgreich war
     */
    boolean showComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId);

    /**
     * Blendet das komplexe Editor UI in diesem Editor an.
     *
     * @param   complexChildEditorComponent  die komponenten die das UI des komplexen Editors repr\u00E4sentiert
     * @param   complexChildEditorId         DOCUMENT ME!
     *
     * @return  true, wenn der Vorgang erfolgreich war
     */
    boolean hideComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId);

    /**
     * Liefert die Komponenten dieses Delagates, \u00FCblicherwiese ist das this.
     *
     * @return  this
     */
    java.awt.Component getComponent();

    /**
     * Liefert die id des aktiven child Editors, d.h. des Editors, dessen UI gerade angezeigt wird.
     *
     * @return  die id des aktiven child Editors oder null
     */
    Object getActiveChildEditorId();

    /**
     * Wird von allen Containern bis an den root container heruntergereicht, der dann einen PropertyChangeEVent
     * ausl\u00F6st;
     */
    void uiChanged();
}
