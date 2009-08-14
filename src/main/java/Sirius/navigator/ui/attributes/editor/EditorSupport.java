/*
 * EditorSupport.java
 *
 * Created on 10. August 2004, 17:02
 */

package Sirius.navigator.ui.attributes.editor;

/**
 *
 * @author  pascal
 */
public interface EditorSupport
{
    
    /**
     * Getter for property complexEditor.
     * @return Value of property complexEditor.
     */
    public ComplexEditor getComplexEditor();
    
    /**
     * Getter for property complexEditorSupported.
     * @return Value of property complexEditorSupported.
     */
    public boolean isComplexEditorSupported();
    
    /**
     * Getter for property simpleEditor.
     * @return Value of property simpleEditor.
     */
    public SimpleEditor getSimpleEditor();
    
    /**
     * Getter for property simpleEditorSupported.
     * @return Value of property simpleEditorSupported.
     */
    public boolean isSimpleEditorSupported();
}
