/*
 * ComplexEditor.java
 *
 * Created on 10. August 2004, 10:31
 */

package Sirius.navigator.ui.attributes.editor;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

/**
 * This interface defines the method any object that would like to be a complex editor 
 * of meta objects for components needs to implement.
 *
 * @author  pascal
 */
public interface ComplexEditor extends BasicEditor, ComplexContainer
{
    /**
     * Initialisiert die Komponente (z.B. ein JPanel) um das komplexe Object
     * (value) zu bearbeiten und gibt dieses zur\u00FCck.<p>
     *
     * @param id die eindeutige id des zu bearbeitenden Objekts
     * @param value das zu bearbeitende Object
     */   
    public Component getEditorComponent(BasicContainer parentContainer, Object id, Object value);
}
