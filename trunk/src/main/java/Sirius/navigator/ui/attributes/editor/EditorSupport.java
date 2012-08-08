/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorSupport.java
 *
 * Created on 10. August 2004, 17:02
 */
package Sirius.navigator.ui.attributes.editor;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface EditorSupport {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property complexEditor.
     *
     * @return  Value of property complexEditor.
     */
    ComplexEditor getComplexEditor();

    /**
     * Getter for property complexEditorSupported.
     *
     * @return  Value of property complexEditorSupported.
     */
    boolean isComplexEditorSupported();

    /**
     * Getter for property simpleEditor.
     *
     * @return  Value of property simpleEditor.
     */
    SimpleEditor getSimpleEditor();

    /**
     * Getter for property simpleEditorSupported.
     *
     * @return  Value of property simpleEditorSupported.
     */
    boolean isSimpleEditorSupported();
}
