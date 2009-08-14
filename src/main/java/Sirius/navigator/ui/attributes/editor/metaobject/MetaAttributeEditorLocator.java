/*
 * DefaultEditorLocatorDelegate.java
 *
 * Created on 20. August 2004, 16:34
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import java.util.*;
import java.net.*;

import org.apache.log4j.Logger;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.ui.attributes.editor.*;
import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.plugin.*;


/**
 * Standardimplementierung des EditorLocator Interfaces, die eine Liste von
 * Standard Editoren f\u00FCr bestimmte Klassen bereith\u00E4lt.<p>
 *
 * @author  Pascal
 */
public class MetaAttributeEditorLocator implements EditorLocator
{
    public final static String EDITOR_JAR_PATH = "/editor/editor.jar";
    public final static Class DEFAULT_COMPLEX_EDITOR = DefaultComplexMetaAttributeEditor.class;
    public final static Class DEFAULT_COMPLEX_ARRAY_EDITOR = DefaultComplexMetaAttributeArrayEditor.class;
    public final static Class DEFAULT_READONLY_EDITOR = ReadOnlyMetaAttributeEditor.class;
    
    protected Logger logger;
    protected static ClassLoader classLoader;
    
    /** Creates a new instance of DefaultEditorLocatorDelegate */
    public MetaAttributeEditorLocator()
    {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.ignoreInvisibleAttributes = false;
        
        if(classLoader == null)
        {
            String editorJARPath = ResourceManager.getManager().pathToIURIString(PropertyManager.getManager().getBasePath()  + EDITOR_JAR_PATH);
            
            try
            {
                logger.info("MetaAttributeEditorLocator(): initializing and loading editor jar: "  + editorJARPath);
                URL[] urls = new URL[]{new URL(editorJARPath)};
                this.classLoader = new PluginClassLoader(urls, this.getClass().getClassLoader());
            }
            catch(Throwable t)
            {
                logger.error("MetaAttributeEditorLocator(): could not load editor jar: " + editorJARPath);
                this.classLoader = this.getClass().getClassLoader();
            }
        }
    }
    
    public Map getEditors(Object value)
    {
        if(value instanceof Attribute)
        {
            if(logger.isDebugEnabled())logger.debug("getEditors(value) value is a meta attribute, retrieving meta object");
            Attribute metaAttribute = (Attribute)value;
            return this.getEditors(metaAttribute.getValue());
        }
        else if(value instanceof MetaObject)
        {
            MetaObject MetaObject = (MetaObject)value;
            if(logger.isDebugEnabled())logger.debug("getEditors(value) value is a meta object (" + MetaObject.getName() + ")");
            
            return this.getEditors(MetaObject.getAttributes());
        }
        else
        {
            logger.error("getEditor(value): " + value.getClass().getName() + " is not supported by this editor locator");
            return new HashMap();
        }
    }
    
    public Map getEditors(java.util.Map metaAttributes)
    {
        LinkedHashMap editorsMap = new LinkedHashMap();
        Iterator iterator = metaAttributes.keySet().iterator();
        
        while(iterator.hasNext())
        {
            Object key = iterator.next();
            Attribute attribute = (Attribute)metaAttributes.get(key);
            
            if(logger.isDebugEnabled())logger.debug("hash map key: " + key + " | attribute key: " + attribute.getKey());
            BasicEditor editor = this.getEditor(attribute);
            
            if(editor != null)
            {
                if(logger.isDebugEnabled())logger.debug("getEditors(MetaObject): adding new editor with id " + key);
                editorsMap.put(key, editor);
            }
            else
            {
                logger.warn("no editor found for '" + attribute.getName() + "' using default read only editor");
                BasicEditor readOnlyEditor = this.createEditor(ReadOnlyMetaAttributeEditor.class);
                editorsMap.put(key, readOnlyEditor);
            }
        }
        
        if(editorsMap.size() == 0)logger.warn("getEditors(metaAttributes): no attributes / editors found");
        return editorsMap;
    }
    
    protected Class createEditorClass(String editorClassName)
    {
        if(logger.isDebugEnabled())logger.debug("createEditorClass(): creating new editor '" + editorClassName + "'");
        if(editorClassName != null)
        {
            try
            {
                Class editorClass = classLoader.loadClass(editorClassName);
                return editorClass;
            }
            catch(Throwable t)
            {
                logger.error("createEditorClass(): could not create editor '" + editorClassName + "' instance", t);
            }
        }
        
        return null;
    }
    
    protected BasicEditor createEditor(Class editorClass)
    {
        if(editorClass != null)
        {
            try
            {
                Object instance = editorClass.newInstance();
                return (BasicEditor)instance;
            }
            catch(Throwable t)
            {
                logger.error("createEditor(): could not create editor '" + editorClass.getName() + "' instance", t);
            }
        }
        
        return null;
    }
    
    public BasicEditor getEditor(Object value)
    {
        if(value instanceof Attribute)
        {
            Sirius.server.localserver.attribute.ObjectAttribute objectAttribute = (Sirius.server.localserver.attribute.ObjectAttribute)value;
            logger.info("getEditor(): searching editor for meta attribute '" + objectAttribute.getName() + "' (" + objectAttribute.getID() + ")");
            
            logger.info("objectAttribute.getName() .is Visible: " + objectAttribute.isVisible());
            if(objectAttribute.isVisible() || !this.isIgnoreInvisibleAttributes())
            {
                BasicEditor simpleEditor = this.createEditor(this.createEditorClass(objectAttribute.getSimpleEditor()));
                if(simpleEditor != null && objectAttribute.referencesObject())// && objectAttribute.isSubstitute())
                {
                    if(logger.isDebugEnabled())logger.debug("getEditor(): attribute '" + objectAttribute.getName() + "' is complex (references object)");
                    Class complexEditorClass = null;
                    
                    // xxx
                    if(objectAttribute.isArray())
                    {
                        if(logger.isDebugEnabled())logger.debug("getEditor(): attribute is an array, using " + MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR);
                        
                        /*try
                        {
                            MetaClass metaClass = SessionManager.getProxy().getMetaClass(objectAttribute.getClassKey());
                            if(metaClass.isArrayElementLink())
                            {
                                if(logger.isDebugEnabled())logger.debug("getEditor(): attribute is an array helper object, using default editor");
                                complexEditorClass = this.createEditorClass(objectAttribute.getComplexEditor());
                            }
                            else
                            {
                                complexEditorClass = MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR;
                            }
                        }
                        catch(Throwable t)
                        {
                            logger.error("getEditor(): could not load meta class", t);
                            complexEditorClass = this.createEditorClass(objectAttribute.getComplexEditor());
                        }*/
                        
                        complexEditorClass = MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR;
                    }
                    else
                    {
                        complexEditorClass = this.createEditorClass(objectAttribute.getComplexEditor());
                    }
                    
                    if(complexEditorClass == null || !ComplexEditor.class.isAssignableFrom(complexEditorClass))
                    {
                        logger.error(complexEditorClass + " is no complax editor, selecting DefaultComplexEditor");
                        complexEditorClass = DEFAULT_COMPLEX_EDITOR;
                    }
                    
                    simpleEditor.setProperty(SimpleEditor.PROPERTY_COMLPEX_EDTIOR, complexEditorClass);
                }
                
                return simpleEditor;
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("getEditor(): attribute '" + objectAttribute.getName() + "' is invisible, ignoring attribute");
            }
        }
        else if(value instanceof MetaObject)
        {
            
            MetaObject MetaObject = (MetaObject)value;
            logger.info("getEditor(): searching editor for meta object editorname=" + MetaObject.getComplexEditor() + " ");
            /*BasicEditor simpleEditor = this.createEditor(this.createEditorClass(MetaObject.getSimpleEditor()));
            Class complexEditor = this.createEditorClass(MetaObject.getComplexEditor());
             
            if(simpleEditor != null && complexEditor != null)
            {
                simpleEditor.setProperty(editor.PROPERTY_COMLPEX_EDTIOR, complexEditor);
            }*/
            if (MetaObject.getComplexEditor()!=null) {
                BasicEditor editor = this.createEditor(this.createEditorClass(MetaObject.getComplexEditor()));
                return editor;
            }
            else {
                try {
                    BasicEditor editor = this.createEditor(this.createEditorClass(SessionManager.getProxy().getMetaClass(MetaObject.getClassKey()).getComplexEditor()));
                    return editor;
                }
                catch (Exception e) {
                    logger.fatal("MetaObjectEditorsuche Exception",e);
                    return null;
                }
                
            }
            
        }
        else
        {
            logger.error("getEditor(): " + value.getClass().getName() + " is not supported by this editor locator");
        }
        
        return null;
    }
    
    public java.util.Map getEditors(java.util.Collection collection)
    {
        logger.error("the method 'getEditors(java.util.Collection collection)' ist not supported by this implementation");
        return new java.util.HashMap();
    }

    /**
     * Holds value of property ignoreInvisibleAttributes.
     */
    private boolean ignoreInvisibleAttributes = false;

    /**
     * Getter for property ignoreInvisibleAttributes.
     * @return Value of property ignoreInvisibleAttributes.
     */
    public boolean isIgnoreInvisibleAttributes()
    {

        return this.ignoreInvisibleAttributes;
    }

    /**
     * Setter for property ignoreInvisibleAttributes.
     * @param ignoreInvisibleAttributes New value of property ignoreInvisibleAttributes.
     */
    public void setIgnoreInvisibleAttributes(boolean ignoreInvisibleAttributes)
    {

        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }
}
