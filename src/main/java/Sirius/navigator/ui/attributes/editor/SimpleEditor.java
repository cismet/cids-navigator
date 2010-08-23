/*
 * SimpleEditor.java
 *
 * Created on 10. August 2004, 17:05
 */

package Sirius.navigator.ui.attributes.editor;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;


/**
 * Ein einfacher Editor zum editieren von primitiven Datententypen oder Objekten
 * als String (toString / fromString).<p>
 * Abgeleitet von TableCellEditor um auch in einer JTable verwendet werden zu k\u00F6nnen.<br>
 * Handelt es sich beim zu bearbeitenden Objekt um ein komplexes Objekt (d.h. ein 
 * zusammengesetztes Objekt, eine Collection, eine Java Bean, etc.) kann aus 
 * diesem einfachen Editor heraus ein komplexer Editor aufgerufen werden
 * siehe Methode getEditorComponent(ComplexContainer, ComplexEditor complexEditor, 
 * Object, Object)) .
 *
 * @author  Pascal
 * @see ComplexEditor
 */
public interface SimpleEditor extends BasicEditor, TableCellEditor
{   
    /**
     * Eigenschaft f\u00FCr den Klassenenamen eines komplexen Editors
     */
    public final static String PROPERTY_COMLPEX_EDTIOR = "complexEditor";//NOI18N
    
    /**
     * Eigenschaft f\u00FCr den Klassenenamen eines komplexen Editors
     */
    public final static String PROPERTY_READ_ONLY = "readOnly";//NOI18N
    
    /**
     * Initialisiert die Komponente (z.B. ein JPanel) um das einfache Object
     * (value) zu bearbeiten und gibt dieses zur\u00FCck.<p>
     * 
     * @param id die eindeutige id des zu bearbeitenden Objekts
     * @param value das zu bearbeitende Object
     */
    public Component getEditorComponent(BasicContainer parentContainer, Object id, Object value);  
    
    /**
     * Diese Methode sollte addComlplexEditor() des Superinterfaces ComplexContainer 
     * aufrufen, um das Bearbeiten des komplexen Objekts im \u00FCbergebenen komplexen 
     * Editor zu erm\u00F6glichen.<br>
     * Dabei sollte standardm\u00E4\u00DFig das aktuelle Editor UI durch das UI des neuen
     * komplexen Editors ersetzt werden. Alternativ kann auch ein *modaler* Dialog
     * erzeugt werden, der das neue Editor UI enth\u00E4lt.<br>
     * Dadurch wird garantier15t, da\u00DF die Ereigniskette zum Speichern / Abbrechen
     * aller untergeordneten Editoren korrekt durchlaufen wird!
     * 
     * @param parentContainer der Container in dem dieser Komplexe Editor dargestellt werden soll
     * @param complexChildEditor der komplexe Editor der aus deisem Editor heraus aufgerufen werden kann
     * @param id die eindeutige id des zu bearbeitenden Objekts
     * @param value das zu bearbeitende Object
     */
    public Component getEditorComponent(BasicContainer parentContainer, ComplexEditor complexChildEditor, Object id, Object value);     
}
