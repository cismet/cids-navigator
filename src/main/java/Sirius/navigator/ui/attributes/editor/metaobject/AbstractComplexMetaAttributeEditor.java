/*
 * AbstractComplexMetaAttributeEditor.java
 *
 * Created on 26. August 2004, 14:17
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import java.util.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;
import Sirius.navigator.ui.attributes.editor.*;
import Sirius.navigator.connection.*;
import Sirius.server.localserver.attribute.ObjectAttribute;


/**
 *
 * @author  pascal
 */
public abstract class AbstractComplexMetaAttributeEditor extends AbstractComplexEditor
{
    protected String PROPERTY_SHOW_ONLY_VISIBLE_ATTRIBUTES = "showOnlyVisibleAttributes";//NOI18N
    
    protected boolean showOnlyVisibleAttributes = false;
    
    // setValue() --------------------------------------------------------------
    
    protected void setValue(Object value)
    {
        if(logger.isDebugEnabled())logger.debug("setValue(" + this + "):" + value);//NOI18N
        if (this.getValue() != null && this.getValue() instanceof Attribute && (value == null || !(value instanceof Attribute)))
        {
            if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting new value of existing meta attribute");//NOI18N
            ((Attribute)this.getValue()).setValue(value);
        }
        else if(value != null && value instanceof MetaObject)
        {
            if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting new meta object (this must be the root editor)");//NOI18N
            super.setValue(value);
        }
        else if(value != null && value instanceof Attribute)
        {
            if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting new meta attribute");//NOI18N
            MetaObject MetaObject = this.getMetaObject(value);
            
            if(MetaObject == null)
            {
                if(logger.isDebugEnabled())logger.warn("setValue(" + this + ") the value of this attribute is null, creating new empty meta object");//NOI18N
                if (((Attribute)value).isArray()) {
                    String domain=((Attribute)value).getClassKey().split("@")[1];//NOI18N
                    try {
                        MetaObject = new DefaultMetaObject(new Sirius.server.localserver.object.DefaultObject(-1,((ObjectAttribute)value).getClassID()),domain);
                        MetaObject.setDummy(true);
                    }
                    catch (Exception e) {
                        logger.error("Error while creating a ArrayDummyObjektes",e);//NOI18N
                    }
                }else {
                    MetaObject = this.getMetaObjectInstance(((Attribute)value).getClassKey());
                }
                ((Attribute)value).setValue(MetaObject);
            }
            
            super.setValue(value);
        }
        else
        {
            logger.error("setValue(" + this + ") old value or new value is not of type Attribute or MetaObject (" + value + ")");//NOI18N
        }
    }
    
    public java.lang.Object getValue(java.lang.Object key)
    {
        Map metaAttributes = this.getMetaObjectAttributes(this.getValue());
        if(metaAttributes != null && metaAttributes.containsKey(key))
        {
            return metaAttributes.get(key);
        }
        else
        {
            logger.error("getValue(" + this + ") no meta attributes in this meta object found, or unknown attribute key: '" + key + "'");//NOI18N
        }
        
        return null;
    }
    
    public void setValue(java.lang.Object key, java.lang.Object value)
    {
        Map metaAttributes = this.getMetaObjectAttributes(this.getValue());
        if(metaAttributes != null)
        {
            if(metaAttributes.containsKey(key))
            {
                Attribute metaAttribute = (Attribute)metaAttributes.get(key);
                Object attributeValue = this.getAttributeValue(value);
                
                if(logger.isDebugEnabled())logger.debug("setValue(" + this + ") setting attribute '" + key + "' value: " + value);//NOI18N
                if(this.showOnlyVisibleAttributes && !metaAttribute.isVisible())
                {
                    logger.warn("setting the value of an invisible attribute");//NOI18N
                }
                
                metaAttribute.setValue(attributeValue);
                metaAttribute.setChanged(true);
            }
            else if(value != null && value instanceof Attribute)
            {
                if(logger.isDebugEnabled())logger.warn("adding new attribute '" + key + "' to the list of attributes");//NOI18N
                metaAttributes.put(key, value);
            }
            else
            {
                logger.error("setValue(" + this + ") attribute '" + key + "' not found in map of attributes (" + value + ")");//NOI18N
            }
        }
        else
        {
            logger.error("getValue(" + this + ") no meta attributes in this meta object found ("+ key + ")");//NOI18N
        }
    }
    
    public void setValueChanged(boolean valueChanged)
    {
         logger.debug("setValueChanged");//NOI18N
        super.setValueChanged(valueChanged);
        if(this.getValue() instanceof Attribute)
        {
            ((Attribute)this.getValue()).setChanged(((Attribute)this.getValue()).isChanged() | valueChanged);
        }
        
        MetaObject MetaObject = this.getMetaObject(this.getValue());
        MetaObject.setChanged(MetaObject.isChanged() | valueChanged);
        if(MetaObject.isChanged() &&(!MetaObject.isDummy()))
        {
            if (logger.isDebugEnabled()) {
                logger.debug("Object: "+MetaObject+" was modified and has the state "+MetaObject.getStatus());//NOI18N
            }
            if (MetaObject.getStatus() == MetaObject.NO_STATUS ||MetaObject.getStatus() == MetaObject.MODIFIED) { 
                MetaObject.setStatus(MetaObject.MODIFIED);
            }
            else {
                MetaObject.setStatus(MetaObject.NEW);
            }
        }
        
        
//        else if (MetaObject.isDummy()) // frickeldasgeht
//        {
//            ObjectAttribute[] as = MetaObject.getAttribs();
//            
//            for(int i=0;i<as.length;i++)
//            {
//               MetaObject mo =(MetaObject)as[i].getValue();
//               
//               if(mo.getStatus()== MetaObject.NO_STATUS)
//                   mo.setStatus(MetaObject.NEW);
//            
//            }
//            
//            
//            
//        }
    }
    
    // Properties --------------------------------------------------------------
    
    public boolean setProperty(String key, Object value)
    {
        if(key.equalsIgnoreCase(PROPERTY_SHOW_ONLY_VISIBLE_ATTRIBUTES) && value instanceof Boolean)
        {
            this.showOnlyVisibleAttributes = ((Boolean)value).booleanValue();
            return true;
        }
        else
        {
            return super.setProperty(key,value);
        }
    }
    
    public Object getProperty(String key)
    {
        if(key.equalsIgnoreCase(PROPERTY_SHOW_ONLY_VISIBLE_ATTRIBUTES))
        {
            return new Boolean(this.showOnlyVisibleAttributes);
        }
        else
        {
            return super.getProperty(key);
        }
    }
    
    // Hilfsmethoden -----------------------------------------------------------
    
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
    
    /**
     * Liefert den Wert eines Attributes, wenn es sich bei dem Argument um ein
     * MetaAttribut handelt.
     */
    protected MetaObject getMetaObject(Object value)
    {
        if(value != null)
        {
            if(value instanceof MetaObject)
            {
                return (MetaObject)value;
            }
            else if(value instanceof Attribute)
            {
                Object attributeValue = ((Attribute)value).getValue();
                if(attributeValue == null || (attributeValue != null && attributeValue instanceof MetaObject))
                {
                    return (MetaObject)attributeValue;
                }
                else
                {
                    logger.error("getMetaObject(" + this + ") value of Attribute '" + ((Attribute)value).getName() + "' is not of type MetaObject (" + attributeValue.getClass().getName() + ")");//NOI18N
                }
            }
            else
            {
                logger.error("getMetaObject(" + this + ") value is not of type Attribute or MetaObject (" + value.getClass().getName() + ")");//NOI18N
            }
        }
        
        return null;
    }
    
    /**
     * Created a new instance of a Meta Object
     *
     * @param classKey Class of the Meta Object
     * @return new instnace of MetaObject of Type classKey or null
     */
    protected MetaObject getMetaObjectInstance(Object classKey)
    {
        try
        {
            if(logger.isDebugEnabled())logger.debug("getMetaObjectInstance(): try to retrive ObjectTemplate for Class '" + classKey + "'");//NOI18N
            MetaClass metaClass = SessionManager.getProxy().getMetaClass(classKey.toString());
            
            MetaObject MetaObject = SessionManager.getProxy().getInstance(metaClass);
            MetaObject.setStatus(MetaObject.NEW);
            return MetaObject;
        }
        catch(Throwable t)
        {
            logger.error("setValue(" + this + ") could not create new empty meta object of type '" + classKey + "'", t);//NOI18N
            return null;
        }
    }
    
    protected Map getMetaObjectAttributes(Object value)
    {
        MetaObject MetaObject = this.getMetaObject(value);
        if(MetaObject != null)
        {
            return MetaObject.getAttributes();
        }
        
        return null;
    }
    
    protected String getName(Object value)
    {
        if(value != null)
        {
            if(value instanceof Attribute)
            {
                return ((Attribute)value).getName();
            }
            else if(value instanceof MetaObject)
            {
                return ((MetaObject)value).getName();
            }
        }
        
        return null;
    }
}
