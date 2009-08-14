/*
 * SimpleTextEditor.java
 *
 * Created on 24. August 2004, 09:53
 */

package Sirius.navigator.ui.attributes.editor.primitive;

import Sirius.navigator.ui.attributes.editor.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class SimpleTextEditor extends DefaultSimpleEditor
{
    protected void initUI()
    {
        super.initUI();
        if(this.complexEditorButton != null)
        {
            this.complexEditorButton.removeActionListener(this.editorActivationDelegate);
            this.remove(this.complexEditorButton);
            
            this.complexEditorButton =  null;
            this.editorActivationDelegate = null;  
        }
    }
}
