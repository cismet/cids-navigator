/*
 * SimpleCharacterMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 15:26
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

/**
 * Ein Editor f\u00FCr Character Attribute.
 *
 * @author  Pascal
 */
public class SimpleCharacterMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor
{
    public SimpleCharacterMetaAttributeEditor()
    {
        super();
        this.logger = Logger.getLogger(this.getClass());
        this.simpleValueField.setDocument(new ShortDocument());
        //this.readOnly = true;
    }
    
    protected Object getComponentValue()
    {
        if(this.simpleValueField.getText().length() == 1)
        {
            return new Character(this.simpleValueField.getText().charAt(0));
        }
        else
        { 
            this.simpleValueField.setText(null);
            return null;
        }
    }
    
    /**
     * Document, das nur bestimme Werte (z.B. Integer) akzeptiert
     */
    protected class ShortDocument extends PlainDocument
    {
        public void insertString(final int i, final String s, final AttributeSet attributes) throws BadLocationException
        {  
            if(s.length() < 2 && i == 0)
            {
                super.insertString(i, s, attributes);
            }
            else
            {
                getToolkit().beep();
            }
        } 
    } 
}
