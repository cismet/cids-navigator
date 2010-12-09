/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleFromStringMetaAttributeEditor.java
 *
 * Created on 3. Dezember 2004, 13:22
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import Sirius.server.localserver.attribute.Attribute;

import org.apache.log4j.lf5.util.Resource;

import java.awt.image.RescaleOp;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SimpleFromStringMetaAttributeEditor extends SimpleStringMetaAttributeEditor {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of SimpleFromStringMetaAttributeEditor.
     */
    public SimpleFromStringMetaAttributeEditor() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void initUI() {
        super.initUI();
        this.simpleValueField.setEnabled(this.isStringCreateable((Attribute)this.getValue()));
    }

    @Override
    protected ValueChangeListener getValueChangeListener() {
        return new SimpleFromStringValueChangeListener();
    }

    /**
     * Der Wert wurde schon im ValueChanged Listener ver\u00E4ndert.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected Object getComponentValue() {
        return this.getValue();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class SimpleFromStringValueChangeListener
            extends SimpleStringMetaAttributeEditor.DefaultSimpleValueChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        protected void actionPerformed() {
            SimpleFromStringMetaAttributeEditor.this.setValueChanged(SimpleFromStringMetaAttributeEditor.this
                        .isValueChanged() | this.isChanged());
            if (SimpleFromStringMetaAttributeEditor.this.isValueChanged()) {
                try {
                    final Sirius.server.localserver.attribute.Attribute attribute =
                        (Sirius.server.localserver.attribute.Attribute)getValue();
                    if (SimpleFromStringMetaAttributeEditor.this.isStringCreateable(attribute)) {
                        SimpleFromStringMetaAttributeEditor.this.setValueFromString(
                            attribute,
                            this.getNewValue().toString());
                        if (logger.isDebugEnabled()) {
                            logger.debug("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId()
                                        + "): speichere neue Eingabe");          // NOI18N
                        }
                        SimpleFromStringMetaAttributeEditor.this.stopEditing();
                    } else {
                        logger.error("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId()
                                    + "): value is not from String createable"); // NOI18N
                    }
                } catch (Throwable t) {
                    logger.error("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId()
                                + "): from String creation " + this.getNewValue() + " failed",
                        t);                                                      // NOI18N

                    // XXX i18n
                    javax.swing.JOptionPane.showMessageDialog(
                        SimpleFromStringMetaAttributeEditor.this,
                        org.openide.util.NbBundle.getMessage(
                            SimpleFromStringMetaAttributeEditor.class,
                            "SimpleFromStringMetaAttributeEditor.actionPerformed.ErrorMessage",
                            new Object[] { t.getMessage() }), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            SimpleFromStringMetaAttributeEditor.class,
                            "SimpleFromStringMetaAttributeEditor.actionPerformed.ErrorTitle"),
                        javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N

                    // reset
                    setComponentValue(getValue());
                }
            }
        }
    }
}
