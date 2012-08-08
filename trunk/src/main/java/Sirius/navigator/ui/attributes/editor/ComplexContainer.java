/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorContainer.java
 *
 * Created on 10. August 2004, 14:12
 */
package Sirius.navigator.ui.attributes.editor;

import javax.swing.*;

/**
 * Ein komplexer Container f\u00FCr einen komplexen Editor und beliebig viele simple Editoren.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface ComplexContainer extends BasicContainer {

    //~ Methods ----------------------------------------------------------------

    /**
     * Setzt den Wert eines Kindes.
     *
     * @param  key    key des Kindes
     * @param  value  neuer Wert des Kindes
     */
    void setValue(java.lang.Object key, java.lang.Object value);

    /**
     * Liefert den Wert eines Kindes.
     *
     * @param   key  eindeutiger Key des Kindes
     *
     * @return  value Wert des Kindes
     */
    java.lang.Object getValue(java.lang.Object key);

    /**
     * Entfernt ein Kind.
     *
     * @param   key  eindeutiger Key des Kindes
     *
     * @return  value Wert des entfernten Kindes
     */
    java.lang.Object removeValue(java.lang.Object key);

    /**
     * F\u00FCgt ein neues Kind hinzu.
     *
     * @param  key    key des Kindes
     * @param  value  neuer Wert des Kindes
     */
    void addValue(java.lang.Object key, java.lang.Object value);
}
