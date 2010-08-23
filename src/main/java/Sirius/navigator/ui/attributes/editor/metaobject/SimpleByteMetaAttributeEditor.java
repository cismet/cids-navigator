/*
 * SimpleByteAttributeEditor.java
 *
 * Created on 29. August 2004, 15:21
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

/**
 * Ein Editor f\u00FCr Byte Attribute.
 *
 * @author  Pascal
 */
public class SimpleByteMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor
{
    public SimpleByteMetaAttributeEditor()
    {
        super();
        this.logger = Logger.getLogger(this.getClass());
        this.simpleValueField.setDocument(new ShortDocument());
        this.readOnly = false;
    }
    
    protected Object getComponentValue()
    {
        try
        {
            return Byte.valueOf(this.simpleValueField.getText());
        }
        catch(NumberFormatException nfe)
        {
            logger.warn("string '" + this.simpleValueField.getText() + "' is no valid Byte", nfe);//NOI18N
            Byte byteObject = new Byte((byte)0);
            this.setComponentValue(byteObject);
            return byteObject;
        }
    }
    
    /**
     * Document, das nur bestimme Werte (z.B. Integer) akzeptiert
     */
    protected class ShortDocument extends PlainDocument
    {
        public void insertString(final int i, final String s, final AttributeSet attributes) throws BadLocationException
        {  
            super.insertString(i, s, attributes);
            if (s != null && (!s.equals("-") || i != 0 || s.length() >= 2))//NOI18N
            {
                try
                {
                    Byte.parseByte(getText(0, getLength()));
                }
                catch (NumberFormatException e)
                {
                    remove(i, s.length());
                    getToolkit().beep();
                }
            }
        } 
    } 
}
