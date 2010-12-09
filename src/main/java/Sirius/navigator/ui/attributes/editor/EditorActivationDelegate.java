/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorActivatorDelegate.java
 *
 * Created on 19. August 2004, 16:49
 */
package Sirius.navigator.ui.attributes.editor;

/**
 * Delegate zum Aktivieren / Deaktivieren eines komplexen Editors innerhalb eines simplen / komplexen Editors.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public interface EditorActivationDelegate {

    //~ Instance fields --------------------------------------------------------

    String ACTIVE_CHILD_EDITOR_TREE = "activeChildEditorTree"; // NOI18N

    //~ Methods ----------------------------------------------------------------

    /**
     * Setzt den komplexen Editor dieses einfachen Editors.
     *
     * <p>Diese Methode ist nur sinnvoll, wenn das zu bearbeitende Objekt ein komplexes Objekt ist.<br>
     * Diese Methode *mu\u00DF* einen EditorListener am \u00FCbergebenen komplexen Editor registrieren. Ist der Editor,
     * der dieses Interface implemetiert ein einfacher Editor, sollte die Methode setValue() bei einem stopEditing()
     * Event des komplexen Editors aufgerufen werden. Weiterhin *mu\u00DF* in der stopEditing() Methode dieses editors,
     * die stopEditing() Methode des komplexen Editors aufgerufen werden (das gleiche gilt f\u00FCr cancelEditing())</p>
     *
     * @param   complexChildEditor  der Komplexe Editor zum Berabeiten eines komplexen Objekts
     *
     * @return  true, wenn der Vorgang erfolgreich war
     */
    boolean addComplexEditor(ComplexEditor complexChildEditor);

    /**
     * Entfernt den komplexen Editor dieses einfachen Editors.
     *
     * <p>Analog zu addComplexEditor() *mu\u00DF* der EditorListener deregistriert werden.</p>
     *
     * @param   complexChildEditor  DOCUMENT ME!
     *
     * @return  true, wenn der Vorgang erfolgreich war
     */
    boolean removeComplexEditor(ComplexEditor complexChildEditor);

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    void addPropertyChangeListener(java.beans.PropertyChangeListener l);

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    void removePropertyChangeListener(java.beans.PropertyChangeListener l);
}
