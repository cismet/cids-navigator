/*
 * AbstractMetaAttributeEditor.java
 *
 * Created on 26. August 2004, 13:35
 */

package Sirius.navigator.ui.attributes.editor.metaobject;


import java.awt.event.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.navigator.ui.attributes.editor.*;
import de.cismet.cids.tools.fromstring.StringCreateable;

/**
 *
 * @author  pascal
 */
public abstract class AbstractSimpleMetaAttributeEditor extends AbstractSimpleEditor
{
    // setValue() --------------------------------------------------------------
    protected void setValue(Object value)
    {
        if (this.getValue() != null && this.getValue() instanceof Attribute && (value == null || !(value instanceof Attribute)))
        {
            if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting new value of existing meta attribute");//NOI18N
            ((Attribute)this.getValue()).setValue(value);
        }
        else if(value != null && value instanceof Attribute)
        {
            if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting new Meta Attribute no null value");//NOI18N
            super.setValue(value);
        }
        else
        {
            logger.error("setValue(" + this + ") old value or new value is not of type Attribute, null values are not permitted in this editor (" + value + ")");//NOI18N
        }
    }
    
    public boolean isEditable(java.util.EventObject anEvent)
    {
        //String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();
        
        try
        {
            // klasse besorgen xxx
            // MetaClass metaClass = SessionManager.getProxy().getMetaClass(this.getMetaObject(this.getValue()).getClassKey());
            
            return !this.readOnly & !((Attribute)this.getValue()).isPrimaryKey() /* & ((Attribute)this.getValue()).getPermissions().hasPermission(key,Sirius.navigator.connection.SessionManager.getSession().getWritePermission())*/;
        }
        catch(Exception exp)
        {
            logger.error("isEditable() could not check permissions of attribute " + this.getValue(), exp);//NOI18N
        }
        
        return false;
    }
    
    public void setValueChanged(boolean valueChanged)
    {
        super.setValueChanged(valueChanged);
        if(this.getValue() instanceof Attribute)
        {
            ((Attribute)this.getValue()).setChanged(((Attribute)this.getValue()).isChanged() | valueChanged);
        }
    }
    
    /**
     * Liefert den Wert eines Attributes, wenn es sich bei dem Argument um ein
     * MetaAttribut handelt.
     */
    protected Object getAttributeValue(Object value)
    {
        if(value != null && value instanceof Attribute)
        {
            return ((Attribute)value).getValue();
        }
        
        return value;
    }
    
    protected boolean isStringCreateable(Attribute attribute)
    {
        if(attribute != null)
        {
            return StringCreateable.class.isAssignableFrom(attribute.getClass()) & ((StringCreateable)attribute).isStringCreateable();
        }
        
        return false;
    }
    
    protected boolean setValueFromString(Attribute attribute, String newValue) throws Exception
    {
        if(this.isStringCreateable(attribute))
        {
            if(logger.isDebugEnabled())logger.debug("setValueFromString(): setting value from string " + newValue);//NOI18N
            Object newerValue = ((StringCreateable)attribute).fromString(newValue, attribute);
            
            this.setValue(newerValue);
            this.setComponentValue(newerValue);
            
            return true;
        }
        
        return false;
    }
    
    protected abstract ValueChangeListener getValueChangeListener();
    
    /**
     * Speichert den Wert des Editors, wenn das Textfeld den Focus verliert oder
     * ENTER gedr\u00FCckt wird.
     */
    protected abstract class ValueChangeListener implements FocusListener, ActionListener
    {
        private Object oldValue = null;
        
        public void focusGained(FocusEvent e)
        {
            if(!e.isTemporary())
            {
                this.oldValue = this.getNewValue();
            }
        }
        
        public void focusLost(FocusEvent e)
        {
            this.actionPerformed();
        }
        
        public void actionPerformed(ActionEvent e)
        {
            this.actionPerformed();
            this.oldValue = this.getNewValue();
        }
        
        protected void actionPerformed()
        {
            AbstractSimpleMetaAttributeEditor.this.setValueChanged(AbstractSimpleMetaAttributeEditor.this.isValueChanged() | this.isChanged());
            if(AbstractSimpleMetaAttributeEditor.this.isValueChanged())
            {
                if(logger.isDebugEnabled())logger.debug("actionPerformed(" + AbstractSimpleMetaAttributeEditor.this.getId() + "): save new input");//NOI18N
                AbstractSimpleMetaAttributeEditor.this.stopEditing();
            }
        }
        
        protected boolean isChanged()
        {
            if(this.oldValue != null)
            {
                return !this.oldValue.equals(this.getNewValue());
            }
            else
            {
                return true;
            }
        }
        
        protected abstract Object getNewValue();
        /*{
            return AbstractSimpleMetaAttributeEditor.this.simpleValueField.getText();
        }*/
    }
}
