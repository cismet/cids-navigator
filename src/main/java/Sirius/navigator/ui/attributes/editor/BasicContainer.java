/*
 * EditorContainer.java
 *
 * Created on 10. August 2004, 14:12
 */

package Sirius.navigator.ui.attributes.editor;

import javax.swing.*;

/** 
 * Ein einfacher Container f\u00FCr einen komplexen Editor.
 * @author  pascal
 */
public interface BasicContainer extends EditorActivationDelegate, EditorUIDelegate
{
    /**
     * Liefert den parent Container dieses Containers
     *
     * @return der parent Container dieses Containers
     */
    public BasicContainer getParentContainer();
    
    /**
     * Liefert eine Collection aller einfachen sowie komplexen Editoren
     * dieses Containers.<p>
     *
     * @return Liste aller child Editoren, Objekte vom Typ BasicEditor
     */
    public java.util.Map getChildEditors();
    
    public java.util.LinkedList getActiveChildEditorTree(java.util.LinkedList activeChildEditorTree);
    
    public boolean setActiveChildEditorTree(java.util.LinkedList activeChildEditorTree); 
}
