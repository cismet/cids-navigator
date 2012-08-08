/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleFloatAttributeEditor.java
 *
 * Created on 29. August 2004, 15:19
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import org.apache.log4j.Logger;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Ein Editor f\u00FCr Float Attribute.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class SimpleFloatMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleFloatMetaAttributeEditor object.
     */
    public SimpleFloatMetaAttributeEditor() {
        super();
        this.logger = Logger.getLogger(this.getClass());
        this.simpleValueField.setDocument(new ShortDocument());
        this.readOnly = false;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Object getComponentValue() {
        try {
            return Float.valueOf(this.simpleValueField.getText());
        } catch (NumberFormatException nfe) {
            logger.warn("string '" + this.simpleValueField.getText() + "' is no valid Float", nfe); // NOI18N
            final Float floatObject = new Float(0);
            this.setComponentValue(floatObject);
            return floatObject;
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Document, das nur bestimme Werte (z.B. Integer) akzeptiert
     *
     * @version  $Revision$, $Date$
     */
    protected class ShortDocument extends PlainDocument {

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertString(final int i, final String s, final AttributeSet attributes)
                throws BadLocationException {
            super.insertString(i, s, attributes);
            if ((s != null) && (!s.equals("-") || (i != 0) || (s.length() >= 2))) // NOI18N
            {
                try {
                    Float.parseFloat(getText(0, getLength()));
                } catch (NumberFormatException e) {
                    remove(i, s.length());
                    getToolkit().beep();
                }
            }
        }
    }
}
