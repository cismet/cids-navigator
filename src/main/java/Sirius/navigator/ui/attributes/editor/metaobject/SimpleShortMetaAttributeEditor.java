/*
 * SimpleShortMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 15:08
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

/**
 * Ein Editor f\u00FCr Short Attribute.
 *
 * @author  Pascal
 */
public class SimpleShortMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor
{
    public SimpleShortMetaAttributeEditor()
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
            return Short.valueOf(this.simpleValueField.getText());
        }
        catch(NumberFormatException nfe)
        {
            logger.warn("string '" + this.simpleValueField.getText() + "' is no valid short", nfe);//NOI18N
            Short shortObject = new Short((short)0);
            this.setComponentValue(shortObject);
            return shortObject;
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
                    Short.parseShort(getText(0, getLength()));
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
