/*
 * DefaultEditorLocatorDelegate.java
 *
 * Created on 20. August 2004, 16:34
 */

package Sirius.navigator.ui.attributes.editor;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * Standardimplementierung des EditorLocator Interfaces, die eine Liste von
 * Standard Editoren f\u00FCr bestimmte Klassen bereith\u00E4lt.<p>
 *
 * @author  Pascal
 */
public class DefaultEditorLocator implements EditorLocator
{
    protected Logger logger;
    
    /**
     *
     */
    protected final HashMap standardEditorClasses;
    
    /** Creates a new instance of DefaultEditorLocatorDelegate */
    public DefaultEditorLocator()
    {
        this.logger = Logger.getLogger(DefaultEditorLocator.class);
        
        this.standardEditorClasses = new HashMap();
        
        // einfache Editoren
        this.standardEditorClasses.put(Object.class, DefaultSimpleEditor.class);
        this.standardEditorClasses.put(String.class, Sirius.navigator.ui.attributes.editor.primitive.SimpleStringEditor.class);
        this.standardEditorClasses.put(Boolean.class, Sirius.navigator.ui.attributes.editor.primitive.SimpleBooleanEditor.class);
        
        // komplexe Editoren
        this.standardEditorClasses.put(List.class, DefaultComplexEditor.class);
        this.standardEditorClasses.put(Map.class, DefaultComplexEditor.class);
    }
    
    public Map getEditors(java.util.Collection collection)
    {
        LinkedHashMap editorsMap = new LinkedHashMap();
        if(logger.isDebugEnabled())logger.debug("value is a list, inspecting elements");
        
        int i = 0;
        Iterator iterator = collection.iterator();
        while(iterator.hasNext())
        {
            BasicEditor basicEditor = this.getEditor(iterator.next());
            if(basicEditor != null)
            {
                if(logger.isDebugEnabled())logger.debug("editor '" + basicEditor.getClass().getName() +  "' found for object '" + i + "'");
                editorsMap.put(new Integer(i), basicEditor);
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("no editor found for object '" + i + "'");
            }
            
            i++;
        }
        
        return editorsMap;
    }
    
    public Map getEditors(java.util.Map map)
    {
        LinkedHashMap editorsMap = new LinkedHashMap();
        if(logger.isDebugEnabled())logger.debug("value is a map, inspecting elements");
        
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext())
        {
            Object key = iterator.next();
            BasicEditor basicEditor = this.getEditor(map.get(key));
            if(basicEditor != null)
            {
                if(logger.isDebugEnabled())logger.debug("editor '" + basicEditor.getClass().getName() +  "' found for object '" + key + "'");
                editorsMap.put(key, basicEditor);
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("no editor found for object '" + key + "'");
            }
        }
        
        return editorsMap;
    }
    
    public Map getEditors(Object value)
    {
        if(java.util.List.class.isAssignableFrom(value.getClass()))
        {
            return this.getEditors((List)value);
        }
        else if(java.util.Map.class.isAssignableFrom(value.getClass()))
        {
            return this.getEditors((Map)value);
        }
        else
        {
            LinkedHashMap editorsMap = new LinkedHashMap();
            BasicEditor basicEditor = this.getEditor(value);
            
            if(basicEditor != null)
            {
                editorsMap.put(value.toString(), basicEditor);
            } 
            
            return editorsMap;
        }  
    }
    
    public BasicEditor getEditor(Object value)
    {
        Class editorClass = this.getEditorClass(value.getClass());
        if(editorClass != null)
        {
            try
            {
                return (BasicEditor)editorClass.newInstance();
            }
            catch(Throwable t)
            {
                logger.error("could not create editor instance of '" + editorClass.getName() + "'", t);
            }
        }
        
        return null;
    }
    
    protected Class getEditorClass(Class objectClass)
    {
        //if(logger.isDebugEnabled())logger.debug("searching editor for class " + objectClass);
        if(objectClass != null)
        {
            if(this.standardEditorClasses.containsKey(objectClass))
            {
                return (Class)this.standardEditorClasses.get(objectClass);
            }
            else
            {
                Class[] interfaces = objectClass.getInterfaces();
                for(int i = 0; i < interfaces.length ; i++)
                {
                    Class editorClass = this.getEditorClass(interfaces[i]);
                    if(editorClass != null)
                    {
                        return editorClass;
                    }
                }
                
                return this.getEditorClass(objectClass.getSuperclass());
            }
        }
        
        return null;
    }
    
    /*public static void main(String args[])
    {
        org.apache.log4j.BasicConfigurator.configure();
     
        ArrayList value = new ArrayList();
        value.add(new String("ein String"));
        value.add(new Boolean(true));
        value.add(new Integer(1));
        value.add(new Double(6.66));
     
        HashMap map = new HashMap();
        map.put("String.class", new String("noch ein String"));
        map.put("Boolean.class", new Boolean(false));
        map.put("Integer.class", new Integer(2));
        map.put("Double.class", new Double(4711));
     
        value.add(map);
     
        DefaultEditorLocator def = new DefaultEditorLocator();
     
        //System.out.println(def.getEditorClass(String.class));
     
        def.getEditors(value);
    }*/
}
