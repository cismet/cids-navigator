/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * BasicEditor.java
 *
 * Created on 11. August 2004, 14:00
 */
package Sirius.navigator.ui.attributes.editor;

/**
 * Basisklasse aller simplen und komplexen Editoren.
 *
 * <p>Die Methoden stopEditing() und cancelEditing sollten<br>
 * a) rekursiv auf allen untergeordneten Editoren aufgerufen werden<br>
 * und<br>
 * b) jeweils ein stopEditing bzw. cancelEditing Ereigniss ausl\u00F6sen.<br>
 * Ein geeignter Listener sollte auf diese Ereginisse reagieren und dementsprechend die Werte der untergeordneten
 * Objekte setzten.</p>
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public interface BasicEditor extends BasicContainer {

    //~ Methods ----------------------------------------------------------------

    /**
     * Eigenschaft f\u00FCr die Spracheinstellung (Locale Objekt).
     *
     * @return  DOCUMENT ME!
     */
    // public final static String PROPERTY_LOCALE = "locale";

    /**
     * Returns the value contained in the editor. Returns null if no value has been set.
     *
     * @return  the value contained in the editor
     */
    Object getValue();

    /**
     * Returns the id of the edited object or null if no value has been set.
     *
     * @return  the id of the edited object
     */
    Object getId();

    /**
     * Asks the editor if it can start editing using <code>anEvent</code>. <code>anEvent</code> is in the invoking
     * component coordinate system. The editor can not assume the Component returned by <code>
     * getCellEditorComponent</code> is installed. This method is intended for the use of client to avoid the cost of
     * setting up and installing the editor component if editing is not possible. If editing can be started this method
     * returns true.
     *
     * @param   anEvent  the event the editor should use to consider whether to begin editing or not
     *
     * @return  true if editing can be started
     *
     * @see     #shouldSelectCell
     */
    boolean isEditable(java.util.EventObject anEvent);

    /**
     * Tells the editor to stop editing and accept any partially edited value as the value of the editor. The editor
     * returns false if editing was not stopped; this is useful for editors that validate and can not accept invalid
     * entries.
     *
     * @return  true if editing was stopped; false otherwise
     */
    boolean stopEditing();

    /**
     * Tells the editor to cancel editing and not accept any partially edited value.
     */
    void cancelEditing();

    /**
     * Adds a listener to the list that's notified when the editor stops, or cancels editing.
     *
     * @param  l  the CellEditorListener
     */
    void addEditorListener(EditorListener l);

    /**
     * Removes a listener from the list that's notified.
     *
     * @param  l  the CellEditorListener
     */
    void removeEditorListener(EditorListener l);

    /**
     * Setzt eine Eigenschaft des Editors, z.B. die Sprache.
     *
     * <p>Implementierende Klassen sollten zuerst super.setProperty() aufrufen, der return Wert gibt dann an, ob die
     * Eigenschaft bereits der Superklasse bekannt war und schon gesetzt wurde.</p>
     *
     * @param   key    Name der Eigenschaft
     * @param   value  Wert der Eigenschaft
     *
     * @return  true, wenn es sich um eine bekannte Eigenschaft gehandelt hat.
     */
    boolean setProperty(String key, Object value);

    /**
     * Fragt eine Eigenschaft des Editors ab.
     *
     * <p>z.B. die Sprache</p>
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  Wert der Eigenschaft, oder null
     *
     * @key     Name der Eigenschaft
     */
    Object getProperty(String key);

    /**
     * Gibt an, ob der Wert durch den Editor ver\u00E4ndert wurde.
     *
     * <p>Nach dem Aufruf dieser Methode, solle das changed flag automatisch auf false zur\u00FCckgesetzt werden.<br>
     * . Das changed flag sollte in der Methode stopEditing() auf true gesetzt werden, wenn getComponentValue() einen
     * neuen Wert liefert.</p>
     *
     * @return  true, wenn sich der Wert ge\u00E4ndert hat
     */
    boolean isValueChanged();

    /**
     * Gibt an, ob der Wert durch den Editor ver\u00E4ndert wurde.
     *
     * <p>Das changed flag sollte in der Methode stopEditing() auf true gesetzt werden, wenn getComponentValue() einen
     * neuen Wert liefert. Das changed flag sollte durch den parent editor automatisch auf false gesetzt werden (im
     * editor Listener).<br>
     * .</p>
     *
     * @param  valueChanged  DOCUMENT ME!
     */
    void setValueChanged(boolean valueChanged);

    /**
     * Gibt an, da\u00DF ein neuer Wert durch den Editor hinzugef\u00FCgt wurde.
     *
     * <p>Wird nur abgefragt, wenn isChanged() true liefert. Der parent Editor sollte das alte Objekt zur id des child
     * Editors entfernen, das neue Objekt unter einer neuen negativen is hinzuf\u00FCgen und dem Editor diese neue id
     * zuweisen.<br>
     * Auch hier sollte das new flag zur\u00FCckgesetzt werden.</p>
     *
     * <p>return true, wenn sich der Wert neu ist</p>
     *
     * @return  DOCUMENT ME!
     */
    boolean isValueNew();
}
