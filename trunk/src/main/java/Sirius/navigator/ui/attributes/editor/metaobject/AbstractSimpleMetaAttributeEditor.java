/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AbstractMetaAttributeEditor.java
 *
 * Created on 26. August 2004, 13:35
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import Sirius.navigator.ui.attributes.editor.*;

import Sirius.server.localserver.attribute.Attribute;

import java.awt.event.*;

import de.cismet.cids.tools.fromstring.StringCreateable;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class AbstractSimpleMetaAttributeEditor extends AbstractSimpleEditor {

    //~ Methods ----------------------------------------------------------------

    // setValue() --------------------------------------------------------------
    @Override
    protected void setValue(final Object value) {
        if ((this.getValue() != null) && (this.getValue() instanceof Attribute)
                    && ((value == null) || !(value instanceof Attribute))) {
            if (logger.isDebugEnabled()) {
                logger.debug("setValue(" + this + ") setting new value of existing meta attribute"); // NOI18N
            }
            ((Attribute)this.getValue()).setValue(value);
        } else if ((value != null) && (value instanceof Attribute)) {
            if (logger.isDebugEnabled()) {
                logger.debug("setValue(" + this + ") setting new Meta Attribute no null value");     // NOI18N
            }
            super.setValue(value);
        } else {
            logger.error("setValue(" + this
                        + ") old value or new value is not of type Attribute, null values are not permitted in this editor ("
                        + value + ")");                                                              // NOI18N
        }
    }

    @Override
    public boolean isEditable(final java.util.EventObject anEvent) {
        // String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();

        try {
            // klasse besorgen xxx MetaClass metaClass =
            // SessionManager.getProxy().getMetaClass(this.getMetaObject(this.getValue()).getClassKey());

            return !this.readOnly
                        & !((Attribute)this.getValue()).isPrimaryKey()                                     /* & ((Attribute)this.getValue()).getPermissions().hasPermission(key,Sirius.navigator.connection.SessionManager.getSession().getWritePermission())*/;
        } catch (Exception exp) {
            logger.error("isEditable() could not check permissions of attribute " + this.getValue(), exp); // NOI18N
        }

        return false;
    }

    @Override
    public void setValueChanged(final boolean valueChanged) {
        super.setValueChanged(valueChanged);
        if (this.getValue() instanceof Attribute) {
            ((Attribute)this.getValue()).setChanged(((Attribute)this.getValue()).isChanged() | valueChanged);
        }
    }

    /**
     * Liefert den Wert eines Attributes, wenn es sich bei dem Argument um ein MetaAttribut handelt.
     *
     * @param   value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Object getAttributeValue(final Object value) {
        if ((value != null) && (value instanceof Attribute)) {
            return ((Attribute)value).getValue();
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   attribute  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isStringCreateable(final Attribute attribute) {
        if (attribute != null) {
            return StringCreateable.class.isAssignableFrom(attribute.getClass())
                        & ((StringCreateable)attribute).isStringCreateable();
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   attribute  DOCUMENT ME!
     * @param   newValue   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected boolean setValueFromString(final Attribute attribute, final String newValue) throws Exception {
        if (this.isStringCreateable(attribute)) {
            if (logger.isDebugEnabled()) {
                logger.debug("setValueFromString(): setting value from string " + newValue); // NOI18N
            }
            final Object newerValue = ((StringCreateable)attribute).fromString(newValue, attribute);

            this.setValue(newerValue);
            this.setComponentValue(newerValue);

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected abstract ValueChangeListener getValueChangeListener();

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Speichert den Wert des Editors, wenn das Textfeld den Focus verliert oder ENTER gedr\u00FCckt wird.
     *
     * @version  $Revision$, $Date$
     */
    protected abstract class ValueChangeListener implements FocusListener, ActionListener {

        //~ Instance fields ----------------------------------------------------

        private Object oldValue = null;

        //~ Methods ------------------------------------------------------------

        @Override
        public void focusGained(final FocusEvent e) {
            if (!e.isTemporary()) {
                this.oldValue = this.getNewValue();
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            this.actionPerformed();
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            this.actionPerformed();
            this.oldValue = this.getNewValue();
        }

        /**
         * DOCUMENT ME!
         */
        protected void actionPerformed() {
            AbstractSimpleMetaAttributeEditor.this.setValueChanged(AbstractSimpleMetaAttributeEditor.this
                        .isValueChanged() | this.isChanged());
            if (AbstractSimpleMetaAttributeEditor.this.isValueChanged()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("actionPerformed(" + AbstractSimpleMetaAttributeEditor.this.getId()
                                + "): save new input"); // NOI18N
                }
                AbstractSimpleMetaAttributeEditor.this.stopEditing();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected boolean isChanged() {
            if (this.oldValue != null) {
                return !this.oldValue.equals(this.getNewValue());
            } else {
                return true;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected abstract Object getNewValue();
        /*{
         *  return AbstractSimpleMetaAttributeEditor.this.simpleValueField.getText();}*/
    }
}
