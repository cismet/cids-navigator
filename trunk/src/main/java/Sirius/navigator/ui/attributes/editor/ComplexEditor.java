/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ComplexEditor.java
 *
 * Created on 10. August 2004, 10:31
 */
package Sirius.navigator.ui.attributes.editor;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * This interface defines the method any object that would like to be a complex editor of meta objects for components
 * needs to implement.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface ComplexEditor extends BasicEditor, ComplexContainer {

    //~ Methods ----------------------------------------------------------------

    /**
     * Initialisiert die Komponente (z.B. ein JPanel) um das komplexe Object (value) zu bearbeiten und gibt dieses
     * zur\u00FCck.
     *
     * @param   parentContainer  DOCUMENT ME!
     * @param   id               die eindeutige id des zu bearbeitenden Objekts
     * @param   value            das zu bearbeitende Object
     *
     * @return  DOCUMENT ME!
     */
    Component getEditorComponent(BasicContainer parentContainer, Object id, Object value);
}
