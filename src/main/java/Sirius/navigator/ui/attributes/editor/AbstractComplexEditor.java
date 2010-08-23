/*
 * AbstractComplexEditor.java
 *
 * Created on 10. August 2004, 11:38
 */

package Sirius.navigator.ui.attributes.editor;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.EventObject;
import java.io.Serializable;
import java.util.*;
import java.awt.event.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public abstract class AbstractComplexEditor extends AbstractSimpleEditor implements ComplexEditor
{  
    /**
     * Enth\u00E4lt alle Editoren der Kinder dieses Objekts
     */
    protected EditorLocator editorLocator = null;
    
    /**
     * 
     */
    protected EditorListener editorHandler;
    
    // Abstrakte AbstractSimpleEditor Methoden .................................
    /*protected abstract Object getComponentValue();
    
    protected abstract void initUI();
    
    public abstract boolean isEditable(java.util.EventObject anEvent);
    
    protected abstract void setComponentValue(Object value);*/
    
    // \u00DCberschriebene AbstractSimpleEditor Methodn ..............................
    
    /**
     * Macht das gleiche wie setValue().<p>
     * Diese Methode ist bei einem komplexen Editor normalerweise nicht 
     * notwendig, da es hier idr mehr als eine UI Komponenten gibt.
     *
     * @param value der Wert, der im Editor gesetzt wird.
     */
    protected void setComponentValue(Object value)
    {
        this.setValue(value);
    }
    
    /**
     * Macht das gleiche wie getValue().<p>
     * Diese Methode ist bei einem komplexen Editor normalerweise nicht 
     * notwendig, da es hier idr mehr als eine UI Komponenten gibt.
     * werden sollte.
     * @return der Wert, der im Editor gesetzt wurde.
     */
    protected Object getComponentValue()
    {
        return this.getValue();
    }

    /**
     *
     */
    protected void initUI()
    {
        if(this.getValue() != null)
        {
            if(this.getChildEditors() != null && this.getChildEditors().size() > 0 && this.editorHandler != null)
            {
                if(logger.isDebugEnabled())logger.debug("initUI(" + this.getId() + "): removing editor listeners from previous session");//NOI18N
                Iterator iterator = this.getChildEditors().values().iterator();
                while(iterator.hasNext())
                {
                    ((BasicEditor)iterator.next()).removeEditorListener(this.editorHandler);
                }
            }

            // Objekt nach editoren untersuchen
            this.childrenMap = this.editorLocator.getEditors(this.getValue());
            if(logger.isDebugEnabled())logger.debug("initUI(): " + this.childrenMap.size() + " child editors initialized");//NOI18N
        }
        else
        {
            logger.warn("initUI(): value is null, no child editors available");//NOI18N
            this.childrenMap = new HashMap();
        }
    }
    
    // EditorComponent Methoden ................................................
     
    public abstract Object getValue(java.lang.Object key);

    public abstract void setValue(java.lang.Object key, java.lang.Object value);

    // Hilfsklassen  ===========================================================
    
    protected class ComplexEditorActivationDelegate extends AbstractEditorActivationDelegate
    {
        /** Creates a new instance of ComplexEditorActivator */
        public ComplexEditorActivationDelegate()
        {
            this.logger = Logger.getLogger(this.getClass());
            
            this.thisEditor = AbstractComplexEditor.this;
            this.thisContainer = AbstractComplexEditor.this;
            
            this.propertyChangeSupport =  new SwingPropertyChangeSupport(AbstractComplexEditor.this);
        }
        
        protected EditorListener createEditorListener()
        {
            return new ComplexEditorHandler();
        }
        
        /**
         * komplexer Editor -> this container
         */
        protected BasicContainer getParentContainerForUI()
        {
            return this.thisContainer;
        }
    }
    
    // -------------------------------------------------------------------------
    
    /**
     *
     */
    public Component getEditorComponent(BasicContainer parentContainer, Object id, Object value)
    {
        this.getEditorComponent(parentContainer, null, id, value);
        
        return this.editorUIDelegate.getComponent();
    }
    
    /**
     * 
     */
    protected class ComplexEditorUIDelegate extends JPanel implements EditorUIDelegate
    {
        public final static String PARENT_EDITOR = "rootComplexEditorUI";//NOI18N
        public final static String CHILD_EDITOR = "childComplexEditorUI";//NOI18N
        
        protected Logger logger;
        protected Object complexChildEditorId = null;
        
        public ComplexEditorUIDelegate()
        {
            super(new CardLayout());
            
            this.logger = Logger.getLogger(ComplexEditorUIDelegate.class);
            this.add(PARENT_EDITOR, AbstractComplexEditor.this);
        }
        
        public boolean showComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
        {
            //logger.debug("components in complex editor: " + this.getComponentCount());
            if(this.complexChildEditorId == null && this.getComponentCount() == 1)
            {
                if(complexChildEditorComponent != null && complexChildEditorId != null)
                {
                    if(logger.isDebugEnabled())logger.debug("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): showing complex editor UI '" + complexChildEditorComponent.getClass().getName() + "' (" + complexChildEditorId + ") in complex editor UI");//NOI18N
                    
                    if(AbstractComplexEditor.this.getChildEditors().containsKey(complexChildEditorId))
                    {
                        this.add(CHILD_EDITOR, complexChildEditorComponent);
                        ((CardLayout)this.getLayout()).show(this, CHILD_EDITOR);
                        this.complexChildEditorId = complexChildEditorId;
                        
                        this.uiChanged();    
                        return true;
                    }
                    else
                    {
                        logger.warn("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): complex child editor to be shown is no child of this editor");//NOI18N
                    }
                }
                else
                {
                    logger.warn("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): schild component or id is null");//NOI18N
                }
            }
            else
            {
                logger.error("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): unexpected call to showComplexEditorComponentUI(): A Container can show only one complex editor at the same time (" + this.complexChildEditorId + ")");//NOI18N
            }
            
            return false;
        }
        
        public boolean hideComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
        {
            if(this.complexChildEditorId != null && this.getComponentCount() == 2)
            {
                if(complexChildEditorComponent != null && complexChildEditorId != null)
                {
                    if(this.complexChildEditorId.equals(complexChildEditorId))
                    {
                        if(logger.isDebugEnabled())logger.debug("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): hiding complex editor UI '" + complexChildEditorComponent.getClass().getName() + "' in complex editor UI");//NOI18N
                        this.remove(complexChildEditorComponent);

                        if(this.getComponentCount() == 1)
                        {
                            this.complexChildEditorId = null;
                            
                            this.uiChanged();
                            return true;
                        }
                        else
                        {
                            logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): removal of complex child editor UI'" + complexChildEditorComponent.getClass().getName() + "' not sucessfull");//NOI18N
                        }  
                    }
                    else
                    {
                        logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): removed id '" + complexChildEditorId + "' does not match current active child editor id '" + this.complexChildEditorId + "'");//NOI18N
                    }
                }
                else
                {
                    logger.warn("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): showComplexEditorComponentUI(): child component or id is null");//NOI18N
                }
            }
            else
            {
                logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId() + "): unexpected call to hideComplexEditorComponentUIr(): no complex editor shown (" + complexChildEditorId + ")");//NOI18N
            }
            
            return false;
        }
        
        public Component getComponent()
        {
            return this;
        }
        
        public Object getActiveChildEditorId()
        {
            return this.complexChildEditorId;
        }
        
        public void uiChanged()
        {
            AbstractComplexEditor.this.uiChanged();
        }
        
    }
    
    public java.util.LinkedList getActiveChildEditorTree(java.util.LinkedList activeChildEditorTree)
    {
        // XXX
        // um die Konsitenz zu wahren: (Complex(id:1) <-> Simple(id:2) <-> Complex(id:2) == id:1 <-> id:2
        LinkedList activeComplexChildEditorTree = super.getActiveChildEditorTree(activeChildEditorTree);
        activeComplexChildEditorTree.addLast(this.getId());
        
        return activeComplexChildEditorTree;
    }
    
    public boolean setActiveChildEditorTree(java.util.LinkedList activeChildEditorTree)
    {
        // XXX
        // um die Konsitenz zu wahren: (Complex(id:1) <-> Simple(id:2) <-> Complex(id:2) == id:1 <-> id:2
        if(activeChildEditorTree.size() > 0)
        {
            activeChildEditorTree.removeFirst();
            return super.setActiveChildEditorTree(activeChildEditorTree);
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "):  this must be the leaf editor");//NOI18N
            return true;
        }
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * todo
     */
    protected class ComplexEditorHandler extends SimpleEditorHandler
    {        
        public ComplexEditorHandler()
        {
            this.logger = Logger.getLogger(ComplexEditorHandler.class);
        }
        
        public void editingCanceled(ChangeEvent e)
        {
            BasicEditor basicEditor = (BasicEditor)e.getSource();
            if(logger.isDebugEnabled())logger.debug("editingCanceled(" + AbstractComplexEditor.this.getId() + "): cancelEditing() on child editor '" + basicEditor.getId() + "' called, ignoring input");//NOI18N
            
            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor); 
        }
        
        public void editingStopped(ChangeEvent e)
        {
            BasicEditor basicEditor = (BasicEditor)e.getSource();
            //if(logger.isDebugEnabled())logger.debug("stopEditing() on child editor '" + basicEditor.getId() + "' called, saving input");
            
            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor); 
            
            if(basicEditor.isValueChanged())
            {
                if(logger.isDebugEnabled())logger.debug("editingStopped(" + AbstractComplexEditor.this.getId() + "): changes in child editor '" + basicEditor.getId() + "', setting new value");//NOI18N
                // der neue Wert aus dem Editor ...
                Object value = basicEditor.getValue();
                
                // den Wert diesem Editor zuweisen
                AbstractComplexEditor.this.setValue(basicEditor.getId(), basicEditor.getValue());
                
                // zur\u00FCcksetzen
                basicEditor.setValueChanged(false);
                // \u00C4nderung anzeigen!
                AbstractComplexEditor.this.setValueChanged(true);
            }
            else if(logger.isDebugEnabled())
            {
                logger.debug("editingStopped(" + AbstractComplexEditor.this.getId() + "): no changes in child editor '" + basicEditor.getId() + "' detected");//NOI18N
            }
        } 
    }
}