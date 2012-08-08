/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleIntegerMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 12:27
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import org.apache.log4j.Logger;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Ein Editor f\u00FCr Integer Attribute.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class SimpleIntegerMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of SimpleIntegerMetaAttributeEditor.
     */
    public SimpleIntegerMetaAttributeEditor() {
        super();
        this.logger = Logger.getLogger(this.getClass());
        this.simpleValueField.setDocument(new SimpleIntegerMetaAttributeEditor.IntegerDocument());
        this.readOnly = false;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Object getComponentValue() {
        try {
            return Integer.valueOf(this.simpleValueField.getText());
        } catch (NumberFormatException nfe) {
            logger.warn("string '" + this.simpleValueField.getText() + "' is no valid integer", nfe); // NOI18N
            final Integer integer = new Integer(0);
            this.setComponentValue(integer);
            return integer;
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Document, das nur bestimme Werte (z.B. Integer) akzeptiert
     *
     * @version  $Revision$, $Date$
     */
    protected class IntegerDocument extends PlainDocument {

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertString(final int i, final String s, final AttributeSet attributes)
                throws BadLocationException {
            super.insertString(i, s, attributes);
            if ((s != null) && (!s.equals("-") || (i != 0) || (s.length() >= 2))) // NOI18N
            {
                try {
                    Integer.parseInt(getText(0, getLength()));
                } catch (NumberFormatException e) {
                    remove(i, s.length());
                    SimpleIntegerMetaAttributeEditor.this.getToolkit().beep();
                }
            }
        }
    }
}
