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
 * Ein einfacher Container f\u00FCr einen komplexen Editor.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface BasicContainer extends EditorActivationDelegate, EditorUIDelegate {

    //~ Methods ----------------------------------------------------------------

    /**
     * Liefert den parent Container dieses Containers.
     *
     * @return  der parent Container dieses Containers
     */
    BasicContainer getParentContainer();

    /**
     * Liefert eine Collection aller einfachen sowie komplexen Editoren dieses Containers.
     *
     * @return  Liste aller child Editoren, Objekte vom Typ BasicEditor
     */
    java.util.Map getChildEditors();

    /**
     * DOCUMENT ME!
     *
     * @param   activeChildEditorTree  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    java.util.LinkedList getActiveChildEditorTree(java.util.LinkedList activeChildEditorTree);

    /**
     * DOCUMENT ME!
     *
     * @param   activeChildEditorTree  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean setActiveChildEditorTree(java.util.LinkedList activeChildEditorTree);
}
