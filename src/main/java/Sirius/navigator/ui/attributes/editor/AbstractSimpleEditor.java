  /*
   * AbstractSimpleEditor.java
   *
   * Created on 18. August 2004, 11:46
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
 * Die abstrakte Implementierung eines einfachen Editors.
 *
 * @author  pascal
 */
public abstract class AbstractSimpleEditor extends JPanel implements SimpleEditor
{
    /**
     * ResourceBundle f\u00FCr internationalisierte Strings
     */
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    /**
     * Der Logger des Editors
     */
    protected Logger logger;
    
    /**
     * Die eindeutige id des Editors
     */
    private Object id = null;
    
    /**
     * Der aktuelle Wert des Editors
     */
    private Object value = null;
    
    /**
     * implementiert add- und removeComplexEditor Methoden
     */
    protected AbstractEditorActivationDelegate editorActivationDelegate = null;
    
    /**
     * implementiert show- und hideEditorUI Methoden
     */
    protected EditorUIDelegate editorUIDelegate = null;
    
    /**
     * true, wenn sich der Wert ge\u00E4ndert hat
     */
    private boolean valueChanged = false;
    
    /**
     * true, wenn sich der Wert neu ist
     */
    protected boolean valueNew = false;
    
    /**
     * Lister der children des Editors
     */
    protected java.util.Map childrenMap;
    
    /**
     * parent Container dieses Editors
     */
    protected BasicContainer parentContainer = null;
    
    /**
     * Der Editor darf nur einmal initialisiert werden
     */
    protected boolean init = false;
    
    /**
     * Gibt an, ob seit dem Erzeugen dieses Editors sich ein Wert ge\u00E4ndert hat
     */
    //protected boolean changedSinceCreation = false;
    
    // EditorEvent Kram ........................................................
    transient protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList;
    
    
    // Properties ..............................................................
    
    
    /**
     * Eigenschaft f\u00FCr den Klassenenamen eines komplexen Editors
     */
    protected Class complexEditorClass = null;
    
    /**
     * Gibt an, ob dieser Editor nur zum Anzeigen verwendet werden soll, bzw.
     * nur um einen komplexen Editor aufzurufen
     */
    protected boolean readOnly = false;
    
    public AbstractSimpleEditor()
    {
        this.logger = Logger.getLogger(AbstractSimpleEditor.class);
        
        this.childrenMap = new LinkedHashMap();
        this.listenerList = new EventListenerList();
        
    }
    
    
    // Basic Editor Methoden ....................................................
    
    public Object getId()
    {
        return this.id;
    }
    
    protected void setId(Object id)
    {
        if(logger.isDebugEnabled())logger.debug("setId(" + this + "): setting id of editor '" + id + "'");
        this.id = id;
    }
    
    // changed Methoden ........................................................
    
    public boolean isValueChanged()
    {
        return this.valueChanged;
    }
    
    public void setValueChanged(boolean valueChanged)
    {
        if(logger.isDebugEnabled())logger.debug("setValueChanged(" + this.getId() + ") " + valueChanged);
        this.valueChanged = valueChanged;
    }
    
    public boolean isValueNew()
    {
        return this.valueNew;
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    /**
     * Gibt das bearbeitete Objekt zur\u00FCck, das eine Kopie (falls m\u00F6glich / n\u00F6tig)
     * des urspr\u00FCglichen Objekts ist.<p>
     * Diese Methode wird normalerweise von einem Listener des Parent Containers
     * dieses Editors aufgerufen, nachdem dieser Editor stopEditing() aufgerufen
     * hat.
     *
     * @return der ver\u00E4nderte Wert
     * @see setValue()
     */
    public Object getValue()
    {
        return this.value;
    }
    
    /**
     * Setzt den Wert, den der Editor darstellen soll.<p>
     * Es ist wird dringend empfohlen, den Wert zu kopieren, da sonst ein undo
     * mit cancelEditing nicht m\u00F6glich ist.
     *
     * @param value der neue Wert
     * @see CloneHelper
     */
    protected void setValue(Object value)
    {
        this.value = value;
    }
    
    // UI Methoden .............................................................
    
    /**
     * Initialisiert das UI dieses Editors.<p>
     * Hier wird z.B. die initComponents() Methode des NetBeans FormsEditors
     * aufgerufen.
     */
    protected abstract void initUI();
    /*{
        this.initComponents();
    }*/
    
    
    /**
     * Setzt den Wert im Editor UI
     *
     * @param value der Wert, der im Editor UI gesetzt wird.
     */
    protected abstract void setComponentValue(Object value);
    /*{
        this.textField.setText(value.toString();
    }*/
    
    /**
     * Liefert den Wert, der im Editor UI gesetzt wurde.
     *
     * @return der Wert, der im Editor UI gesetzt wurde.
     */
    protected abstract Object getComponentValue();
    /*{
        if(((JTextField)this.componentDelegate).getText() != null)
        {
            return this.textField.getText();
        }
    }*/
    
    // EditorComponent Methoden ................................................
    
    /**
     * Das wird vom editorUIDelegate erledigt.
     */
    public Component getComponent()
    {
        return this.editorUIDelegate.getComponent();
    }
    
    public Component getEditorComponent(BasicContainer parentContainer, ComplexEditor complexChildEditor, Object id, Object value)
    {
        if(logger.isDebugEnabled())
        {
            if(complexChildEditor != null)
            {
                logger.debug("getEditorComponent(" + this + "): initializing simple editor component for value '" + id + "' with complex child editor support");
            }
            else
            {
                logger.debug("getEditorComponent(" + this + "): initializing simple editor component for value '" + id + "' without complex child editor support");
            }
        }
        
        // neuen Wert kopieren!
        if(value != null)
        {
            try
            {
                if(logger.isDebugEnabled())logger.debug("getEditorComponent(" + this + "): cloning & setting new value: '" + (value != null ? value.toString() : "null") + "'");
                this.setValue(Sirius.navigator.tools.CloneHelper.clone(value));
            }
            catch(Throwable t)
            {
                if(logger.isDebugEnabled())logger.warn("getEditorComponent(" + this + "): cloning not sucessfull", t);
                this.setValue(value);
            }
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("getEditorComponent(" + this + "): value is null");
            this.setValue(null);
        }
        
        // assing new id
        this.setId(id);
        
        // parent container zuweisen
        this.parentContainer = parentContainer;
        
        // init user interface
        this.initUI();
        
        
        // prepare complex child editor
        if(complexChildEditor != null)
        {
            this.addComplexEditor(complexChildEditor);
        }
        
        // show new value in UI
        this.setValueChanged(false);
        this.setComponentValue(this.getValue());
        
        this.init = true;
        
        return this;
    }
    
    public Component getEditorComponent(BasicContainer parentContainer, Object id, Object value)
    {
        return this.getEditorComponent(parentContainer, null, id, value);
    }
    
    /**
     * Gibt an, ob dieser Editor bereit ist, ein Objekt zu bearbeiten.<p>
     * Die Methode getSimpleEditorComponent() sollte vom Container dieses Editors
     * nur dann aufgerufen werden, wenn diese MEtohde true zur\u00FCckliefert.
     *
     * @param anEvent Anhand dieses events kann die Bereitschaft definiert werden (MouseEvent: Doppelclick, etc.)
     * @return true, falls dieser Editor bereit ist
     * @see getSimpleEditorComponent()
     */
    public abstract boolean isEditable(java.util.EventObject anEvent);
    
    public void uiChanged()
    {
        if(this.getParentContainer() != null)
        {
            this.parentContainer.uiChanged();
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("isEditable(" + this + "): this is the root container, fire uiChanged()");
            this.fireUIChanged();
        }
    }
    
    // Edit Methoden  ..........................................................
    
    /**
     * Bricht den Bearbeitungsvorgang ab.
     */
    public void cancelEditing()
    {
        if(logger.isDebugEnabled())logger.debug("cancelEditing(" + this.getId() + ")");
        if(this.getChildEditors().size() != 0)
        {
            if(logger.isDebugEnabled())logger.debug("cancelEditing(" + this.getId() + "): calling cancelEditing() on children of this editor (" + this.getId() + ")");
            Iterator iterator = this.getChildEditors().values().iterator();
            while(iterator.hasNext())
            {
                ((BasicEditor)iterator.next()).cancelEditing();
            }
        }
        
        if(this.editorActivationDelegate != null && this.editorActivationDelegate.isChildEditorVisible())
        {
            logger.warn("cancelEditing(" + this.getId() + "): child editor still visible");
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }
        
        this.fireEditingCanceled();
    }
    
    /**
     *
     * Diese Methode *mu\u00DF* \u00FCberschrieben werden, um den neuen Wert im Editor UI
     * dem alten Wert im Editor zuzuweisen.<pd>
     * Der R\u00FCckgabewert dieser Methode kann durchaus false sein, obwohl sich der
     * Wert eines child Editos ge\u00E4ndert hat. Das h\u00E4ngt mit der synchronen Event-
     * Verarbeitung zusammen.
     *
     * @return true, wenn die Zuweisung erfolgreich war (hinweis beachten).
     */
    public boolean stopEditing()
    {
        if(logger.isDebugEnabled())logger.debug("stopEditing(" + this.getId() + ")");
        if(this.getChildEditors().size() != 0)
        {
            if(logger.isDebugEnabled())logger.debug("stopEditing(" + this.getId() + "): calling stopEditing() on children of this editor (" + this.getId() + ")");
            Iterator iterator = this.getChildEditors().values().iterator();
            while(iterator.hasNext())
            {
                // hier wird nur ein Event ausgel\u00F6st, alles weitere l\u00E4uft dann
                // im EditorListener ab, der an diesem Objekt registriert ist.
                ((BasicEditor)iterator.next()).stopEditing();
            }
        }
        
        if(this.editorActivationDelegate != null && this.editorActivationDelegate.isChildEditorVisible())
        {
            if(logger.isDebugEnabled())logger.warn("stopEditing(" + this.getId() + "): child editor still visible, hiding child editor");
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }
        
        // Der Wert wurde nicht durch einen child editor ver\u00E4ndert
        // XXX was ist, wenn der komplexe Editor nicht modal ist?
        /*if(this.valueChanged && this.getChildEditors().size() == 0)
        {
            if(logger.isDebugEnabled())logger.debug("stopEditing(" + this.getId() + "): value changed, setting new value");
            
            Object newValue = this.getComponentValue();
            this.setValue(newValue);
            this.valueChanged = true;
        }
        else if(logger.isDebugEnabled())
        {
            logger.debug("stopEditing(" + this.getId() + "): this object value not changed directly, ignoring new value");
        }*/
        
        if(this.valueChanged)
        {
            if(logger.isDebugEnabled())logger.debug("stopEditing(" + this.getId() + "): value changed, setting new value");
            
            Object newValue = this.getComponentValue();
            this.setValue(newValue);
        }
        else if(logger.isDebugEnabled())
        {
            logger.debug("stopEditing(" + this.getId() + "): object value not changed");
        }
        
        this.fireEditingStopped();
        
        //
        return this.valueChanged;
    }
    
    // Properties ..............................................................
    
    /**
     * Diese Implementierung gibt nur die Eigenschaft 'PROPERTY_LOCALE' und
     * 'PROPERTY_COMLPEX_EDTIOR' zur\u00FCck.
     *
     * @param key PROPERTY_LOCALE
     * @return Ein locale Objekt, oder null
     */
    public Object getProperty(String key)
    {
        /*if(key.equalsIgnoreCase(PROPERTY_LOCALE) && this.resources != null)
        {
            return this.resources.getLocale();
        }*/
        
        if(key.equalsIgnoreCase(PROPERTY_COMLPEX_EDTIOR))
        {
            return this.complexEditorClass;
        }
        else if(key.equalsIgnoreCase(PROPERTY_READ_ONLY))
        {
            return new Boolean(this.readOnly);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Diese Implementierung setzt nur die Eigenschaften 'PROPERTY_LOCALE',
     * 'PROPERTY_COMLPEX_EDTIOR' und 'PROPERTY_READ_ONLY'.<p>
     * value mu\u00DF ein Objekt vom Type java.util.Locale oder jaca.langClass sein.
     * Es wird ein entsprechendes ResourceBundle f\u00FCr internationalisierte Strings
     * dieses Editors gesucht.
     *
     * @param key PROPERTY_LOCALE, PROPERTY_COMLPEX_EDTIOR, PROPERTY_READ_ONLY
     * @param value Ein entsprechndes Objekt
     */
    public boolean setProperty(String key, Object value)
    {
        /*if(key.equalsIgnoreCase(PROPERTY_LOCALE) && value instanceof Locale)
        {
            try
            {
                this.resources = ResourceBundle.getBundle(this.getClass().getName(), (Locale)value);
                return true;
            }
            catch(Throwable t)
            {
                logger.error("setProperty(" + this.getId() + "): could not load resource bundle for locale '" + value.toString() + "'", t);
            }
        }*/
         
        if(key.equalsIgnoreCase(PROPERTY_COMLPEX_EDTIOR) && value instanceof Class)
        {
            this.complexEditorClass = (Class)value;
            return true;
        }
        else if(key.equalsIgnoreCase(PROPERTY_READ_ONLY) && value instanceof Boolean)
        {
            this.readOnly = ((Boolean)value).booleanValue();
            return true;
        }
        
        return false;
    }
    
    
    // Editor Listener Methods .................................................
    
    /**
     * Adds a <code>CellEditorListener</code> to the listener list.
     * @param l  the new listener to be added
     */
    public void addEditorListener(EditorListener l)
    {
        if(logger.isDebugEnabled())logger.debug("addEditorListener(" + this + ") called");
        listenerList.add(EditorListener.class, l);
    }
    
    /**
     * Removes a <code>EditorListener</code> from the listener list.
     * @param l  the listener to be removed
     */
    public void removeEditorListener(EditorListener l)
    {
        if(logger.isDebugEnabled())logger.debug("removeEditorListener(" + this + ") called");
        listenerList.remove(EditorListener.class, l);
    }
    
    /**
     * Returns an array of all the <code>EditorListener</code>s added
     * to this AbstractEditor with addEditorListener().
     *
     * @return all of the <code>EditorListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public EditorListener[] getEditorListeners()
    {
        return (EditorListener[])listenerList.getListeners(
        EditorListener.class);
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingStopped()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2)
        {
            if (listeners[i]==EditorListener.class)
            {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((EditorListener)listeners[i+1]).editingStopped(changeEvent);
            }
        }
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireUIChanged()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2)
        {
            if (listeners[i]==EditorListener.class)
            {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((EditorListener)listeners[i+1]).uiChanged(changeEvent);
            }
        }
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingCanceled()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2)
        {
            if (listeners[i]==EditorListener.class)
            {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((EditorListener)listeners[i+1]).editingCanceled(changeEvent);
            }
        }
    }
    
    // BasicContainer Methoden ................................................
    
    public boolean addComplexEditor(ComplexEditor complexEditor)
    {
        
        if(this.editorActivationDelegate.isChildEditorVisible())
        {
            logger.warn("addComplexEditor(" + this.getId() + "): complex child editor still visible");
            this.editorActivationDelegate.getChildEditor().cancelEditing();
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }
        
        if(this.editorActivationDelegate.isChildEditorRegistered())
        {
            if(this.editorActivationDelegate.getChildEditor() != complexEditor)
            {
                this.editorActivationDelegate.getChildEditor().cancelEditing();
                logger.warn("addComplexEditor(" + this.getId() + "): another complex child editor is still registered");
                this.editorActivationDelegate.removeComplexEditor();
                return this.editorActivationDelegate.addComplexEditor(complexEditor);
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("addComplexEditor(" + this.getId() + "): this complex child editor is already registered");
                return false;
            }
        }
        else
        {
            return this.editorActivationDelegate.addComplexEditor(complexEditor);
        }
        
        return false;
    }
    
    public boolean removeComplexEditor(ComplexEditor complexEditor)
    {
        if(this.editorActivationDelegate != null)
        {
            if(this.editorActivationDelegate.isChildEditorVisible())
            {
                logger.warn("removeComplexEditor(" + this.getId() + "): complex child editor still visible");
                this.editorActivationDelegate.hideComplexEditorChildComponentUI();
            }
            
            return this.editorActivationDelegate.removeComplexEditor(complexEditor);
        }
        else
        {
            logger.error("removeComplexEditor(" + this.getId() + "): this.editorActivationDelegate not initialized");
            return false;
        }
    }
    
    public boolean showComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
    {
        if(this.editorUIDelegate != null)
        {
            return this.editorUIDelegate.showComplexEditorComponentUI(complexChildEditorComponent, complexChildEditorId);
        }
        else
        {
            logger.error("showComplexEditorComponentUI(" + this.getId() + "): this.editorUIDelegate not initialized");
            return false;
        }
    }
    
    public boolean hideComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
    {
        if(this.editorUIDelegate != null)
        {
            return this.editorUIDelegate.hideComplexEditorComponentUI(complexChildEditorComponent, complexChildEditorId);
        }
        else
        {
            logger.error("hideComplexEditorComponentUI(" + this.getId() + "): this.editorUIDelegate not initialized");
            return false;
        }
    }
    
    public BasicContainer getParentContainer()
    {
        return this.parentContainer;
    }
    
    
    
    public java.util.Map getChildEditors()
    {
        return this.childrenMap;
    }
    
    // child editor Methoden ...................................................
    
    public java.util.LinkedList getActiveChildEditorTree(java.util.LinkedList activeChildEditorTree)
    {
        if(activeChildEditorTree == null)
        {
            if(this.getParentContainer() != null)
            {
                logger.error("getActiveChildEditorTree(" + this.getId() + "): activeChildEditorTree should not be null this parent container is not null)");
            }
            
            activeChildEditorTree = new LinkedList();
        }
        
        // nur bei komplexen Editoren!
        //activeChildEditorTree.addLast(this.getId());
        
        Object activeChildEditorId = this.getActiveChildEditorId();
        if(activeChildEditorId != null)
        {
            if(logger.isDebugEnabled())logger.debug("getActiveChildEditorTree(" + this.getId() + "): no child editor in this editor'" + this.getId() + "' found");
            if(this.getChildEditors().containsKey(activeChildEditorId))
            {
                logger.debug("getActiveChildEditorTree(" + this.getId() + "): child editor '" + activeChildEditorId + "' found in this editor '" + this.getId() + "'");
                BasicContainer activeChildEditor = (BasicContainer)this.getChildEditors().get(activeChildEditorId);
                
                return activeChildEditor.getActiveChildEditorTree(activeChildEditorTree);
            }
            else
            {
                logger.error("getActiveChildEditorTree(" + this.getId() + "):  active child editor not found in list of this child editors");
            }
        }
        else if(logger.isDebugEnabled())logger.debug("getActiveChildEditorTree(" + this.getId() + "):  no active child editor in '" + this.getId() + "'");
        
        
        return activeChildEditorTree;
    }
    
    public boolean setActiveChildEditorTree(java.util.LinkedList activeChildEditorTree)
    {
        if(activeChildEditorTree.size() > 0)
        {
            Object newActiveChildEditorId = activeChildEditorTree.getFirst();
            if(this.getChildEditors().containsKey(newActiveChildEditorId))
            {
                if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "):  new active child editor '" + newActiveChildEditorId + "' is a registered child of this editor");
                Object oldActiveChildEditorId = this.getActiveChildEditorId();
                if(oldActiveChildEditorId != null)
                {
                    if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): there is still an old active child editor '" + oldActiveChildEditorId + " registered");
                    if(this.getChildEditors().containsKey(oldActiveChildEditorId))
                    {
                        if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): old active (visible) child editor '" + oldActiveChildEditorId + "' is a registered child of this editor");
                        BasicEditor oldActiveChildEditor = (BasicEditor)this.getChildEditors().get(oldActiveChildEditorId);
                        if(oldActiveChildEditorId.equals(newActiveChildEditorId))
                        {
                            if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '" + newActiveChildEditorId + " is the active (visible) child of this editor!");
                            return oldActiveChildEditor.setActiveChildEditorTree(activeChildEditorTree);
                        }
                        else
                        {
                            // oder cancel?
                            if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '" + newActiveChildEditorId + " is NOT the active (visible) child of this editor, removing old editor (" + oldActiveChildEditorId + ")");
                            oldActiveChildEditor.stopEditing();  
                        }
                    }
                    else
                    {
                        logger.error("setActiveChildEditorTree(" + this + "): old active child editor '" + oldActiveChildEditorId  + "' not found in list of this child editors");
                    }
                }
                else if(logger.isDebugEnabled())
                {
                    logger.debug("setActiveChildEditorTree(" + this + "): there is NO old active child editor registered");
                }
               
                BasicEditor newActiveChildEditor = (BasicEditor)this.getChildEditors().get(newActiveChildEditorId);
                if(ComplexEditor.class.isAssignableFrom(newActiveChildEditor.getClass()))
                {
                    if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): adding new active child editor '" + newActiveChildEditorId + "' to this container");
                    this.addComplexEditor((ComplexEditor)newActiveChildEditor);
                }
                else if(logger.isDebugEnabled())
                {
                    logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '" + newActiveChildEditorId + "' is no complex editor");
                }   
                
                if(activeChildEditorTree.size() > 0)
                {
                    return newActiveChildEditor.setActiveChildEditorTree(activeChildEditorTree);
                }
                else
                {
                    if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): this editor(" + this + ") is the leaf editor");
                    return true;
                }
            }
            else if(this.editorActivationDelegate.lazyChildEditor != null)
            {
                if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "):  new active child editor '" + newActiveChildEditorId + "' is not yet registered with this editor, but is a lazy editor. Showing the UI now");
                this.editorActivationDelegate.showComplexEditorChildComponentUI();
                
                if(this.editorActivationDelegate.getChildEditor().getId().equals(newActiveChildEditorId))
                {
                    if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '" + newActiveChildEditorId + " is NOW the active (visible) child of this editor!");
                    if(activeChildEditorTree.size() > 0)
                    {
                        return this.editorActivationDelegate.getChildEditor().setActiveChildEditorTree(activeChildEditorTree);
                    }
                    else
                    {
                        if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): this editor(" + this + ") is the leaf editor");
                        return true;
                    }
                }
                else
                {
                    logger.fatal("setActiveChildEditorTree(" + this + "): what a mess: lazy child editor id '" + this.editorActivationDelegate.getChildEditor().getId() + " does not match new child editor id '" + newActiveChildEditorId + "'");
                }
            }
            else
            {
                logger.error("setActiveChildEditorTree(" + this + "): new active child editor '" + newActiveChildEditorId + "' not found in this editor (" + this + ")");
                /*if(logger.isDebugEnabled())
                {
                    Iterator iterator = this.getChildEditors().keySet().iterator();
                    while(iterator.hasNext())
                    {
                        logger.debug("setActiveChildEditorTree(" + this + "): " + iterator.next());
                    }
                }*/
            }
        }
        else if(logger.isDebugEnabled())
        {
            if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): this editor (" + this + ") must be the is the leaf editor");
            Object oldActiveChildEditorId = this.getActiveChildEditorId();
            if(oldActiveChildEditorId != null)
            {
                if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): there is still an old active child editor '" + oldActiveChildEditorId + " registered");
                if(this.getChildEditors().containsKey(oldActiveChildEditorId))
                {
                    if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): old active (visible) child editor '" + oldActiveChildEditorId + "' is a registered child of this editor, removing old editor");
                    BasicEditor oldActiveChildEditor = (BasicEditor)this.getChildEditors().get(oldActiveChildEditorId);
                    
                    // XXX oder cancel?
                    oldActiveChildEditor.stopEditing();
                    
                    return true;
                }
                else
                {
                    logger.error("setActiveChildEditorTree(" + this + "): old active child editor '" + oldActiveChildEditorId  + "' not found in list of this child editors");
                }
            }
            else
            {
                if(logger.isDebugEnabled())logger.debug("setActiveChildEditorTree(" + this + "): this editor (" + this + ") IS the is the leaf editor!");
                return true;
            } 
        }
        
        return false;
    }
    
    public Object getActiveChildEditorId()
    {
        return this.editorUIDelegate.getActiveChildEditorId();
    }
    
    // TableEditor Methoden ....................................................
    
    public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        logger.error("getTableCellEditorComponent(" + this.getId() + "): getTableCellEditorComponent should never be called");
        return new JLabel(I18N.getString("Sirius.navigator.ui.attributes.editor.AbstractSimpleEditor.getTableCellEditorComponent.JLabel.text"));
    }
    
    public boolean stopCellEditing()
    {
        return this.stopEditing();
    }
    
    public void cancelCellEditing()
    {
        this.cancelEditing();
    }
    
    public void addCellEditorListener(javax.swing.event.CellEditorListener l)
    {
        if(l instanceof EditorListener)
        {
            this.addEditorListener((EditorListener)l);
        }
        else
        {
            logger.warn("addCellEditorListener(" + this.getId() + "): listener not of type 'EditorListener'");
        }
    }
    
    public void removeCellEditorListener(javax.swing.event.CellEditorListener l)
    {
        if(l instanceof EditorListener)
        {
            this.removeEditorListener((EditorListener)l);
        }
        else
        {
            logger.warn("removeCellEditorListener(" + this.getId() + "): listener not of type 'EditorListener'");
        }
    }
    
    public boolean shouldSelectCell(java.util.EventObject anEvent)
    {
        return true;
    }
    
    public boolean isCellEditable(java.util.EventObject anEvent)
    {
        return this.isEditable(anEvent);
    }
    
    public Object getCellEditorValue()
    {
        return this.getValue();
    }
    
    // Hilfsklassen ============================================================
    
    /**
     * Standardimplementierung eines
     */
    protected class SimpleEditorActivationDelegate extends AbstractEditorActivationDelegate
    {
        /** Creates a new instance of ComplexEditorActivator */
        public SimpleEditorActivationDelegate()
        {
            this.logger = Logger.getLogger(this.getClass());
            
            this.thisEditor = AbstractSimpleEditor.this;
            this.thisContainer = AbstractSimpleEditor.this;
            
            this.propertyChangeSupport =  new SwingPropertyChangeSupport(AbstractSimpleEditor.this);
        }
        
        protected EditorListener createEditorListener()
        {
            return new SimpleEditorHandler();
        }
        
        /**
         * einfacher Editor -> parent container
         */
        protected BasicContainer getParentContainerForUI()
        {
            return this.thisContainer.getParentContainer();
        }
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Rudiment\u00E4re Dialogbox zum Anzeigen eines komplexen Editors (child) in einem
     * simplen Editor (this).<p>
     * Normalerweise sollte das UI eines komplexen Editors, der aus einem einfachen
     * Editor heraus aufgerufen wird, im UI des parent Containers des
     * simplen Editors angezeigt werden.<br>
     * Diese Hilfsklasse erm\u00F6glicht es, das UI eines komplexen Editor mit Hilfe
     * einer Dialogbox 'im' simplen Editor anzuzeigen.
     */
    protected class SimpleEditorUIDelegate extends JDialog implements EditorUIDelegate
    {
        Logger logger = Logger.getLogger(SimpleEditorUIDelegate.class);
        
        protected Object complexChildEditorId = null;
        
        public SimpleEditorUIDelegate(){}
        
        public boolean showComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
        {
            if(this.complexChildEditorId == null && this.getContentPane() == null)
            {
                if(complexChildEditorComponent != null && complexChildEditorId != null)
                {
                    if(AbstractSimpleEditor.this.editorActivationDelegate != null && AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor() != null)
                    {
                        if(logger.isDebugEnabled())logger.debug("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): showing complex editor UI '" + complexChildEditorComponent.getClass().getName() + "' (" + complexChildEditorId + ") in simple editor UI");
                        this.setModal(true);
                        this.setName(AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId().toString());
                        
                        this.complexChildEditorId = complexChildEditorId;
                        // XXX \u00DCberpr\u00FCfung ...
                        this.setContentPane((JComponent)complexChildEditorComponent);
                        
                        this.pack();
                        this.setLocationRelativeTo(null);
                        this.show();
                        
                        this.uiChanged();
                        return true;
                    }
                    else
                    {
                        logger.error("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): no corresponding editor for component registered");
                    }
                }
                else
                {
                    logger.warn("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): showComplexEditorComponentUI(): child component or id is null");
                }
            }
            else
            {
                logger.error("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): unexpected call to showComplexEditorComponentUI(): A Container can show only one complex editor at the same time (" + this.complexChildEditorId + ")");
            }
            
            return false;
        }
        
        public boolean hideComplexEditorComponentUI(Component complexChildEditorComponent, Object complexChildEditorId)
        {
            if(this.complexChildEditorId != null && this.getContentPane() != null)
            {
                if(complexChildEditorComponent != null && complexChildEditorId != null)
                {
                    if(this.complexChildEditorId.equals(complexChildEditorId))
                    {
                        if(AbstractSimpleEditor.this.editorActivationDelegate != null && AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor() != null)
                        {
                            if(logger.isDebugEnabled())logger.debug("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): hiding complex editor UI");
                            
                            this.hide();
                            this.dispose();
                            this.setContentPane(null);
                            this.complexChildEditorId = null;
                            
                            this.uiChanged();
                            return true;
                        }
                        else
                        {
                            logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): no corresponfing editor for component registered");
                        }
                    }
                    else
                    {
                        logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): removed id '" + complexChildEditorId + "' does not match current active child editor id '" + this.complexChildEditorId + "'");
                    }
                }
                else
                {
                    logger.warn("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): child component or id is null");
                }
            }
            else
            {
                logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId() + "): unexpected call to hideComplexEditorComponentUIr(): no complex editor shown (" + complexChildEditorId + ")");
            }
            
            return false;
        }
        
        public java.awt.Component getComponent()
        {
            return this;
        }
        
        public Object getActiveChildEditorId()
        {
            if(this.complexChildEditorId == null && AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorRegistered())
            {
                return AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId();
            }
            
            return this.complexChildEditorId;
        }
        
        /**
         * wird nicht delegiert!!!!
         */
        public void uiChanged()
        {
            AbstractSimpleEditor.this.uiChanged();
        }
    }
    
    // PropertyChange Events ---------------------------------------------------
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        this.editorActivationDelegate.addPropertyChangeListener(l);
    }
    
    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        this.editorActivationDelegate.removePropertyChangeListener(l);
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Rudiment\u00E4rer EditorListener, der den Wert des komplexen (child) Editors
     * dem simplen Editor zuweist, von dem aus er aufgerufen wurde (this).<p>
     * Der Event wird ausgel\u00F6st, wenn auf dem komplexen (child) Editor cancel-
     * oder stopEditing() aufgerufen wird.
     */
    protected class SimpleEditorHandler implements EditorListener
    {
        protected Logger logger;
        
        public SimpleEditorHandler()
        {
            this.logger = Logger.getLogger(SimpleEditorHandler.class);
        }
        
        public void editingCanceled(ChangeEvent e)
        {
            BasicEditor basicEditor = (BasicEditor)e.getSource();
            if(logger.isDebugEnabled())logger.debug("editingCanceled(" + AbstractSimpleEditor.this.getId() + "): cancelEditing() on child editor '" + basicEditor.getId() + "' called, ignoring input");
            
            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);
        }
        
        public void editingStopped(ChangeEvent e)
        {
            BasicEditor basicEditor = (BasicEditor)e.getSource();
            if(logger.isDebugEnabled())logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId() + "): stopEditing() on child editor '" + basicEditor.getId() + "' called, saving input");
            
            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);
            
            if(basicEditor.isValueChanged())
            {
                if(logger.isDebugEnabled())logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId() + "): changes in complex child editor '" + basicEditor.getId() + "', setting new value");
                // der neue Wert aus dem komplexen Editor ...
                Object value = basicEditor.getValue();
                
                // den Wert diesem Editor UI zuweisen, damit er angezeigt wird (z.B. als String)
                AbstractSimpleEditor.this.setValue(value);
                
                // \u00C4nderung anzeigen!
                AbstractSimpleEditor.this.valueChanged = true;
                
                // den Wert diesem Editor zuweisen
                AbstractSimpleEditor.this.setComponentValue(value);
                
                fireEditingStopped();
            }
            else if(logger.isDebugEnabled())
            {
                logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId() + "): no changes in complex child editor '" + basicEditor.getId() + "'");
            }
        }
        
        /**
         * Wenn der komplexe Child Editor noch an diesem komplexen Editor registriert
         * ist, wird er nun entfernt.
         */
        protected void checkComplexChildRegistration(BasicEditor basicEditor)
        {
            if(ComplexEditor.class.isAssignableFrom(basicEditor.getClass()))
            {
                if(AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorRegistered() && AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId().equals(basicEditor.getId()))
                {
                    if(logger.isDebugEnabled())logger.debug("checkComplexChildRegistration(" + AbstractSimpleEditor.this.getId() + "): unregistering complex child editor '" + basicEditor.getId() + "'");
                    AbstractSimpleEditor.this.removeComplexEditor((ComplexEditor)basicEditor);
                }
                else if(logger.isDebugEnabled())
                {
                    logger.debug("checkComplexChildRegistration(" + AbstractSimpleEditor.this.getId() + "): complex child editor '" + basicEditor.getId() + "' not unregistered, possibly registered a simple child editor");
                }
            }
        }
        
        /**
         * Wenn das komplexe Child Editor UI noch in diesem komplexen Editor
         * angezeigt wird, wird es nun versteckt.
         */
        protected void checkComplexChildVisibility(BasicEditor basicEditor)
        {
            if(ComplexEditor.class.isAssignableFrom(basicEditor.getClass()))
            {
                if(AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorVisible() && AbstractSimpleEditor.this.editorActivationDelegate.getChildEditorComponent() == ((EditorUIDelegate)basicEditor).getComponent())
                {
                    if(logger.isDebugEnabled())logger.debug("checkComplexChildVisibility(" + AbstractSimpleEditor.this.getId() + "): hiding complex child editor ui '" + basicEditor.getId() + "' (" + AbstractSimpleEditor.this.editorActivationDelegate.getChildEditorComponent().getClass().getName() + ")");
                    AbstractSimpleEditor.this.editorActivationDelegate.hideComplexEditorChildComponentUI();
                }
                else if(logger.isDebugEnabled())
                {
                    logger.debug("checkComplexChildVisibility(" + AbstractSimpleEditor.this.getId() + "): complex child editor '" + basicEditor.getId() + "' not hidden, possibly shown in complex parent editor");
                }
            }
        }
        
        public void uiChanged(ChangeEvent e)
        {
            // ignore
        }
        
    }
    
    public String toString()
    {
        return this.getId() != null ? this.getId().toString() : null;
    }
}