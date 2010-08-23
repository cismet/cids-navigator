/*
 * SimpleLongAttributeEditor.java
 *
 * Created on 29. August 2004, 15:14
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

/**
 * Ein Editor f\u00FCr Long Attribute.
 *
 * @author  Pascal
 */
public class SimpleLongMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor
{
    public SimpleLongMetaAttributeEditor()
    {
        super();
        this.logger = Logger.getLogger(this.getClass());
        this.simpleValueField.setDocument(new LongDocument());
        this.readOnly = false;
    }
    
    protected Object getComponentValue()
    {
        try
        {
            return Long.valueOf(this.simpleValueField.getText());
        }
        catch(NumberFormatException nfe)
        {
            logger.warn("string '" + this.simpleValueField.getText() + "' is no valid long", nfe);//NOI18N
            Long longObject = new Long(0);
            this.setComponentValue(longObject);
            return longObject;
        }
    }
    
    /**
     * Document, das nur bestimme Werte (z.B. Integer) akzeptiert
     */
    protected class LongDocument extends PlainDocument
    {
        public void insertString(final int i, final String s, final AttributeSet attributes) throws BadLocationException
        {  
            super.insertString(i, s, attributes);
            if (s != null && (!s.equals("-") || i != 0 || s.length() >= 2))//NOI18N
            {
                try
                {
                    Long.parseLong(getText(0, getLength()));
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
