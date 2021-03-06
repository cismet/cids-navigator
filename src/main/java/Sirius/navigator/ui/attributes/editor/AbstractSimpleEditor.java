/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AbstractSimpleEditor.java
 *
 * Created on 18. August 2004, 11:46
 */
package Sirius.navigator.ui.attributes.editor;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.io.Serializable;

import java.util.*;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * Die abstrakte Implementierung eines einfachen Editors.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class AbstractSimpleEditor extends JPanel implements SimpleEditor {

    //~ Instance fields --------------------------------------------------------

    /** Der Logger des Editors. */
    protected Logger logger;

    /** implementiert add- und removeComplexEditor Methoden. */
    protected AbstractEditorActivationDelegate editorActivationDelegate = null;

    /** implementiert show- und hideEditorUI Methoden. */
    protected EditorUIDelegate editorUIDelegate = null;

    /** true, wenn sich der Wert neu ist. */
    protected boolean valueNew = false;

    /** Lister der children des Editors. */
    protected java.util.Map childrenMap;

    /** parent Container dieses Editors. */
    protected BasicContainer parentContainer = null;

    /** Der Editor darf nur einmal initialisiert werden. */
    protected boolean init = false;

    /** Gibt an, ob seit dem Erzeugen dieses Editors sich ein Wert ge\u00E4ndert hat. */
    // protected boolean changedSinceCreation = false;

    // EditorEvent Kram ........................................................
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList;

    // Properties ..............................................................

    /** Eigenschaft f\u00FCr den Klassenenamen eines komplexen Editors. */
    protected Class complexEditorClass = null;

    /**
     * Gibt an, ob dieser Editor nur zum Anzeigen verwendet werden soll, bzw. nur um einen komplexen Editor aufzurufen
     */
    protected boolean readOnly = false;

    /** Die eindeutige id des Editors. */
    private Object id = null;

    /** Der aktuelle Wert des Editors. */
    private Object value = null;

    /** true, wenn sich der Wert ge\u00E4ndert hat. */
    private boolean valueChanged = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractSimpleEditor object.
     */
    public AbstractSimpleEditor() {
        this.logger = Logger.getLogger(AbstractSimpleEditor.class);

        this.childrenMap = new LinkedHashMap();
        this.listenerList = new EventListenerList();
    }

    //~ Methods ----------------------------------------------------------------

    // Basic Editor Methoden ....................................................

    @Override
    public Object getId() {
        return this.id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    protected void setId(final Object id) {
        if (logger.isDebugEnabled()) {
            logger.debug("setId(" + this + "): setting id of editor '" + id + "'"); // NOI18N
        }
        this.id = id;
    }

    // changed Methoden ........................................................

    @Override
    public boolean isValueChanged() {
        return this.valueChanged;
    }

    @Override
    public void setValueChanged(final boolean valueChanged) {
        if (logger.isDebugEnabled()) {
            logger.debug("setValueChanged(" + this.getId() + ") " + valueChanged); // NOI18N
        }
        this.valueChanged = valueChanged;
    }

    @Override
    public boolean isValueNew() {
        return this.valueNew;
    }

    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    /**
     * Gibt das bearbeitete Objekt zur\u00FCck, das eine Kopie (falls m\u00F6glich / n\u00F6tig) des urspr\u00FCglichen
     * Objekts ist.
     *
     * <p>Diese Methode wird normalerweise von einem Listener des Parent Containers dieses Editors aufgerufen, nachdem
     * dieser Editor stopEditing() aufgerufen hat.</p>
     *
     * @return  der ver\u00E4nderte Wert
     *
     * @see     setValue()
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /**
     * Setzt den Wert, den der Editor darstellen soll.
     *
     * <p>Es ist wird dringend empfohlen, den Wert zu kopieren, da sonst ein undo mit cancelEditing nicht m\u00F6glich
     * ist.</p>
     *
     * @param  value  der neue Wert
     *
     * @see    CloneHelper
     */
    protected void setValue(final Object value) {
        this.value = value;
    }

    // UI Methoden .............................................................

    /**
     * Initialisiert das UI dieses Editors.
     *
     * <p>Hier wird z.B. die initComponents() Methode des NetBeans FormsEditors aufgerufen.</p>
     */
    protected abstract void initUI();
    /*{
     *  this.initComponents();}*/

    /**
     * Setzt den Wert im Editor UI.
     *
     * @param  value  der Wert, der im Editor UI gesetzt wird.
     */
    protected abstract void setComponentValue(Object value);
    /*{
     *  this.textField.setText(value.toString();}*/

    /**
     * Liefert den Wert, der im Editor UI gesetzt wurde.
     *
     * @return  der Wert, der im Editor UI gesetzt wurde.
     */
    protected abstract Object getComponentValue();
    /*{
     *  if(((JTextField)this.componentDelegate).getText() != null) {     return this.textField.getText(); }}*/

    // EditorComponent Methoden ................................................

    /**
     * Das wird vom editorUIDelegate erledigt.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Component getComponent() {
        return this.editorUIDelegate.getComponent();
    }

    @Override
    public Component getEditorComponent(final BasicContainer parentContainer,
            final ComplexEditor complexChildEditor,
            final Object id,
            final Object value) {
        if (logger.isDebugEnabled()) {
            if (complexChildEditor != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getEditorComponent(" + this + "): initializing simple editor component for value '"
                                + id + "' with complex child editor support");    // NOI18N
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("getEditorComponent(" + this + "): initializing simple editor component for value '"
                                + id + "' without complex child editor support"); // NOI18N
                }
            }
        }

        // neuen Wert kopieren!
        if (value != null) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("getEditorComponent(" + this + "): cloning & setting new value: '"
                                + ((value != null) ? value.toString() : "null") + "'");         // NOI18N
                }
                this.setValue(Sirius.navigator.tools.CloneHelper.clone(value));
            } catch (Throwable t) {
                if (logger.isDebugEnabled()) {
                    logger.warn("getEditorComponent(" + this + "): cloning not sucessfull", t); // NOI18N
                }
                this.setValue(value);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("getEditorComponent(" + this + "): value is null");                // NOI18N
            }
            this.setValue(null);
        }

        // assing new id
        this.setId(id);

        // parent container zuweisen
        this.parentContainer = parentContainer;

        // init user interface
        this.initUI();

        // prepare complex child editor
        if (complexChildEditor != null) {
            this.addComplexEditor(complexChildEditor);
        }

        // show new value in UI
        this.setValueChanged(false);
        this.setComponentValue(this.getValue());

        this.init = true;

        return this;
    }

    @Override
    public Component getEditorComponent(final BasicContainer parentContainer, final Object id, final Object value) {
        return this.getEditorComponent(parentContainer, null, id, value);
    }

    /**
     * Gibt an, ob dieser Editor bereit ist, ein Objekt zu bearbeiten.
     *
     * <p>Die Methode getSimpleEditorComponent() sollte vom Container dieses Editors nur dann aufgerufen werden, wenn
     * diese MEtohde true zur\u00FCckliefert.</p>
     *
     * @param   anEvent  Anhand dieses events kann die Bereitschaft definiert werden (MouseEvent: Doppelclick, etc.)
     *
     * @return  true, falls dieser Editor bereit ist
     *
     * @see     getSimpleEditorComponent()
     */
    @Override
    public abstract boolean isEditable(java.util.EventObject anEvent);

    @Override
    public void uiChanged() {
        if (this.getParentContainer() != null) {
            this.parentContainer.uiChanged();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("isEditable(" + this + "): this is the root container, fire uiChanged()"); // NOI18N
            }
            this.fireUIChanged();
        }
    }

    // Edit Methoden  ..........................................................

    /**
     * Bricht den Bearbeitungsvorgang ab.
     */
    @Override
    public void cancelEditing() {
        if (logger.isDebugEnabled()) {
            logger.debug("cancelEditing(" + this.getId() + ")"); // NOI18N
        }
        if (this.getChildEditors().size() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("cancelEditing(" + this.getId() + "): calling cancelEditing() on children of this editor ("
                            + this.getId() + ")");               // NOI18N
            }
            final Iterator iterator = this.getChildEditors().values().iterator();
            while (iterator.hasNext()) {
                ((BasicEditor)iterator.next()).cancelEditing();
            }
        }

        if ((this.editorActivationDelegate != null) && this.editorActivationDelegate.isChildEditorVisible()) {
            logger.warn("cancelEditing(" + this.getId() + "): child editor still visible"); // NOI18N
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }

        this.fireEditingCanceled();
    }

    /**
     * Diese Methode *mu\u00DF* \u00FCberschrieben werden, um den neuen Wert im Editor UI dem alten Wert im Editor
     * zuzuweisen.<pd> Der R\u00FCckgabewert dieser Methode kann durchaus false sein, obwohl sich der Wert eines child
     * Editos ge\u00E4ndert hat. Das h\u00E4ngt mit der synchronen Event- Verarbeitung zusammen.
     *
     * @return  true, wenn die Zuweisung erfolgreich war (hinweis beachten).
     */
    @Override
    public boolean stopEditing() {
        if (logger.isDebugEnabled()) {
            logger.debug("stopEditing(" + this.getId() + ")"); // NOI18N
        }
        if (this.getChildEditors().size() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("stopEditing(" + this.getId() + "): calling stopEditing() on children of this editor ("
                            + this.getId() + ")");             // NOI18N
            }
            final Iterator iterator = this.getChildEditors().values().iterator();
            while (iterator.hasNext()) {
                // hier wird nur ein Event ausgel\u00F6st, alles weitere l\u00E4uft dann
                // im EditorListener ab, der an diesem Objekt registriert ist.
                ((BasicEditor)iterator.next()).stopEditing();
            }
        }

        if ((this.editorActivationDelegate != null) && this.editorActivationDelegate.isChildEditorVisible()) {
            if (logger.isDebugEnabled()) {
                logger.warn("stopEditing(" + this.getId() + "): child editor still visible, hiding child editor"); // NOI18N
            }
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }

        // Der Wert wurde nicht durch einen child editor ver\u00E4ndert
        // XXX was ist, wenn der komplexe Editor nicht modal ist?
        /*if(this.valueChanged && this.getChildEditors().size() == 0)
         * { if(logger.isDebugEnabled())logger.debug("stopEditing(" + this.getId() + "): value changed, setting new
         * value");  Object newValue = this.getComponentValue(); this.setValue(newValue); this.valueChanged = true; }
         * else if(logger.isDebugEnabled()) { logger.debug("stopEditing(" + this.getId() + "): this object value not
         * changed directly, ignoring new value");}*/

        if (this.valueChanged) {
            if (logger.isDebugEnabled()) {
                logger.debug("stopEditing(" + this.getId() + "): value changed, setting new value"); // NOI18N
            }

            final Object newValue = this.getComponentValue();
            this.setValue(newValue);
        } else if (logger.isDebugEnabled()) {
            logger.debug("stopEditing(" + this.getId() + "): object value not changed"); // NOI18N
        }

        this.fireEditingStopped();

        //
        return this.valueChanged;
    }

    // Properties ..............................................................

    /**
     * Diese Implementierung gibt nur die Eigenschaft 'PROPERTY_LOCALE' und 'PROPERTY_COMLPEX_EDTIOR' zur\u00FCck.
     *
     * @param   key  PROPERTY_LOCALE
     *
     * @return  Ein locale Objekt, oder null
     */
    @Override
    public Object getProperty(final String key) {
        /*if(key.equalsIgnoreCase(PROPERTY_LOCALE) && this.resources != null)
         * { return this.resources.getLocale();}*/

        if (key.equalsIgnoreCase(PROPERTY_COMLPEX_EDTIOR)) {
            return this.complexEditorClass;
        } else if (key.equalsIgnoreCase(PROPERTY_READ_ONLY)) {
            return new Boolean(this.readOnly);
        } else {
            return null;
        }
    }

    /**
     * Diese Implementierung setzt nur die Eigenschaften 'PROPERTY_LOCALE', 'PROPERTY_COMLPEX_EDTIOR' und
     * 'PROPERTY_READ_ONLY'.
     *
     * <p>value mu\u00DF ein Objekt vom Type java.util.Locale oder jaca.langClass sein. Es wird ein entsprechendes
     * ResourceBundle f\u00FCr internationalisierte Strings dieses Editors gesucht.</p>
     *
     * @param   key    PROPERTY_LOCALE, PROPERTY_COMLPEX_EDTIOR, PROPERTY_READ_ONLY
     * @param   value  Ein entsprechndes Objekt
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean setProperty(final String key, final Object value) {
        /*if(key.equalsIgnoreCase(PROPERTY_LOCALE) && value instanceof Locale)
         * { try {     this.resources = ResourceBundle.getBundle(this.getClass().getName(), (Locale)value);     return
         * true; } catch(Throwable t) {     logger.error("setProperty(" + this.getId() + "): could not load resource
         * bundle for locale '" + value.toString() + "'", t); }}*/

        if (key.equalsIgnoreCase(PROPERTY_COMLPEX_EDTIOR) && (value instanceof Class)) {
            this.complexEditorClass = (Class)value;
            return true;
        } else if (key.equalsIgnoreCase(PROPERTY_READ_ONLY) && (value instanceof Boolean)) {
            this.readOnly = ((Boolean)value).booleanValue();
            return true;
        }

        return false;
    }

    // Editor Listener Methods .................................................

    /**
     * Adds a <code>CellEditorListener</code> to the listener list.
     *
     * @param  l  the new listener to be added
     */
    @Override
    public void addEditorListener(final EditorListener l) {
        if (logger.isDebugEnabled()) {
            logger.debug("addEditorListener(" + this + ") called"); // NOI18N
        }
        listenerList.add(EditorListener.class, l);
    }

    /**
     * Removes a <code>EditorListener</code> from the listener list.
     *
     * @param  l  the listener to be removed
     */
    @Override
    public void removeEditorListener(final EditorListener l) {
        if (logger.isDebugEnabled()) {
            logger.debug("removeEditorListener(" + this + ") called"); // NOI18N
        }
        listenerList.remove(EditorListener.class, l);
    }

    /**
     * Returns an array of all the <code>EditorListener</code>s added to this AbstractEditor with addEditorListener().
     *
     * @return  all of the <code>EditorListener</code>s added or an empty array if no listeners have been added
     *
     * @since   1.4
     */
    public EditorListener[] getEditorListeners() {
        return (EditorListener[])listenerList.getListeners(
                EditorListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * created lazily.
     *
     * @see  EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((EditorListener)listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * created lazily.
     *
     * @see  EventListenerList
     */
    protected void fireUIChanged() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((EditorListener)listeners[i + 1]).uiChanged(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * created lazily.
     *
     * @see  EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((EditorListener)listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    // BasicContainer Methoden ................................................

    @Override
    public boolean addComplexEditor(final ComplexEditor complexEditor) {
        if (this.editorActivationDelegate.isChildEditorVisible()) {
            logger.warn("addComplexEditor(" + this.getId() + "): complex child editor still visible"); // NOI18N
            this.editorActivationDelegate.getChildEditor().cancelEditing();
            this.editorActivationDelegate.hideComplexEditorChildComponentUI();
        }

        if (this.editorActivationDelegate.isChildEditorRegistered()) {
            if (this.editorActivationDelegate.getChildEditor() != complexEditor) {
                this.editorActivationDelegate.getChildEditor().cancelEditing();
                logger.warn("addComplexEditor(" + this.getId() + "): another complex child editor is still registered"); // NOI18N
                this.editorActivationDelegate.removeComplexEditor();
                return this.editorActivationDelegate.addComplexEditor(complexEditor);
            } else if (logger.isDebugEnabled()) {
                logger.warn("addComplexEditor(" + this.getId() + "): this complex child editor is already registered");  // NOI18N
                return false;
            }
        } else {
            return this.editorActivationDelegate.addComplexEditor(complexEditor);
        }

        return false;
    }

    @Override
    public boolean removeComplexEditor(final ComplexEditor complexEditor) {
        if (this.editorActivationDelegate != null) {
            if (this.editorActivationDelegate.isChildEditorVisible()) {
                logger.warn("removeComplexEditor(" + this.getId() + "): complex child editor still visible"); // NOI18N
                this.editorActivationDelegate.hideComplexEditorChildComponentUI();
            }

            return this.editorActivationDelegate.removeComplexEditor(complexEditor);
        } else {
            logger.error("removeComplexEditor(" + this.getId() + "): this.editorActivationDelegate not initialized"); // NOI18N
            return false;
        }
    }

    @Override
    public boolean showComplexEditorComponentUI(final Component complexChildEditorComponent,
            final Object complexChildEditorId) {
        if (this.editorUIDelegate != null) {
            return this.editorUIDelegate.showComplexEditorComponentUI(
                    complexChildEditorComponent,
                    complexChildEditorId);
        } else {
            logger.error("showComplexEditorComponentUI(" + this.getId() + "): this.editorUIDelegate not initialized"); // NOI18N
            return false;
        }
    }

    @Override
    public boolean hideComplexEditorComponentUI(final Component complexChildEditorComponent,
            final Object complexChildEditorId) {
        if (this.editorUIDelegate != null) {
            return this.editorUIDelegate.hideComplexEditorComponentUI(
                    complexChildEditorComponent,
                    complexChildEditorId);
        } else {
            logger.error("hideComplexEditorComponentUI(" + this.getId() + "): this.editorUIDelegate not initialized"); // NOI18N
            return false;
        }
    }

    @Override
    public BasicContainer getParentContainer() {
        return this.parentContainer;
    }

    @Override
    public java.util.Map getChildEditors() {
        return this.childrenMap;
    }

    // child editor Methoden ...................................................

    @Override
    public java.util.LinkedList getActiveChildEditorTree(java.util.LinkedList activeChildEditorTree) {
        if (activeChildEditorTree == null) {
            if (this.getParentContainer() != null) {
                logger.error("getActiveChildEditorTree(" + this.getId()
                            + "): activeChildEditorTree should not be null this parent container is not null)"); // NOI18N
            }

            activeChildEditorTree = new LinkedList();
        }

        // nur bei komplexen Editoren!
        // activeChildEditorTree.addLast(this.getId());

        final Object activeChildEditorId = this.getActiveChildEditorId();
        if (activeChildEditorId != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("getActiveChildEditorTree(" + this.getId() + "): no child editor in this editor'"
                            + this.getId() + "' found");                            // NOI18N
            }
            if (this.getChildEditors().containsKey(activeChildEditorId)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getActiveChildEditorTree(" + this.getId() + "): child editor '" + activeChildEditorId
                                + "' found in this editor '" + this.getId() + "'"); // NOI18N
                }
                final BasicContainer activeChildEditor = (BasicContainer)this.getChildEditors()
                            .get(activeChildEditorId);

                return activeChildEditor.getActiveChildEditorTree(activeChildEditorTree);
            } else {
                logger.error("getActiveChildEditorTree(" + this.getId()
                            + "):  active child editor not found in list of this child editors"); // NOI18N
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("getActiveChildEditorTree(" + this.getId() + "):  no active child editor in '" + this.getId()
                        + "'");                                                                   // NOI18N
        }

        return activeChildEditorTree;
    }

    @Override
    public boolean setActiveChildEditorTree(final java.util.LinkedList activeChildEditorTree) {
        if (activeChildEditorTree.size() > 0) {
            final Object newActiveChildEditorId = activeChildEditorTree.getFirst();
            if (this.getChildEditors().containsKey(newActiveChildEditorId)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this + "):  new active child editor '"
                                + newActiveChildEditorId + "' is a registered child of this editor");         // NOI18N
                }
                final Object oldActiveChildEditorId = this.getActiveChildEditorId();
                if (oldActiveChildEditorId != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setActiveChildEditorTree(" + this
                                    + "): there is still an old active child editor '" + oldActiveChildEditorId
                                    + " registered");                                                         // NOI18N
                    }
                    if (this.getChildEditors().containsKey(oldActiveChildEditorId)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("setActiveChildEditorTree(" + this + "): old active (visible) child editor '"
                                        + oldActiveChildEditorId + "' is a registered child of this editor"); // NOI18N
                        }
                        final BasicEditor oldActiveChildEditor = (BasicEditor)this.getChildEditors()
                                    .get(oldActiveChildEditorId);
                        if (oldActiveChildEditorId.equals(newActiveChildEditorId)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '"
                                            + newActiveChildEditorId
                                            + " is the active (visible) child of this editor!");              // NOI18N
                            }
                            return oldActiveChildEditor.setActiveChildEditorTree(activeChildEditorTree);
                        } else {
                            // oder cancel?
                            if (logger.isDebugEnabled()) {
                                logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '"
                                            + newActiveChildEditorId
                                            + " is NOT the active (visible) child of this editor, removing old editor ("
                                            + oldActiveChildEditorId + ")"); // NOI18N
                            }
                            oldActiveChildEditor.stopEditing();
                        }
                    } else {
                        logger.error("setActiveChildEditorTree(" + this + "): old active child editor '"
                                    + oldActiveChildEditorId + "' not found in list of this child editors"); // NOI18N
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this
                                + "): there is NO old active child editor registered"); // NOI18N
                }

                final BasicEditor newActiveChildEditor = (BasicEditor)this.getChildEditors()
                            .get(newActiveChildEditorId);
                if (ComplexEditor.class.isAssignableFrom(newActiveChildEditor.getClass())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setActiveChildEditorTree(" + this + "): adding new active child editor '"
                                    + newActiveChildEditorId + "' to this container"); // NOI18N
                    }
                    this.addComplexEditor((ComplexEditor)newActiveChildEditor);
                } else if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '"
                                + newActiveChildEditorId + "' is no complex editor");  // NOI18N
                }

                if (activeChildEditorTree.size() > 0) {
                    return newActiveChildEditor.setActiveChildEditorTree(activeChildEditorTree);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setActiveChildEditorTree(" + this + "): this editor(" + this
                                    + ") is the leaf editor"); // NOI18N
                    }
                    return true;
                }
            } else if (this.editorActivationDelegate.lazyChildEditor != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this + "):  new active child editor '"
                                + newActiveChildEditorId
                                + "' is not yet registered with this editor, but is a lazy editor. Showing the UI now"); // NOI18N
                }
                this.editorActivationDelegate.showComplexEditorChildComponentUI();

                if (this.editorActivationDelegate.getChildEditor().getId().equals(newActiveChildEditorId)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setActiveChildEditorTree(" + this + "): new active child editor '"
                                    + newActiveChildEditorId + " is NOW the active (visible) child of this editor!"); // NOI18N
                    }
                    if (activeChildEditorTree.size() > 0) {
                        return this.editorActivationDelegate.getChildEditor()
                                    .setActiveChildEditorTree(activeChildEditorTree);
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("setActiveChildEditorTree(" + this + "): this editor(" + this
                                        + ") is the leaf editor");                                                    // NOI18N
                        }
                        return true;
                    }
                } else {
                    logger.fatal("setActiveChildEditorTree(" + this + "): what a mess: lazy child editor id '"
                                + this.editorActivationDelegate.getChildEditor().getId()
                                + " does not match new child editor id '" + newActiveChildEditorId + "'");            // NOI18N
                }
            } else {
                logger.error("setActiveChildEditorTree(" + this + "): new active child editor '"
                            + newActiveChildEditorId + "' not found in this editor (" + this + ")");                  // NOI18N
                /*if(logger.isDebugEnabled())
                 * { Iterator iterator = this.getChildEditors().keySet().iterator(); while(iterator.hasNext()) {
                 * logger.debug("setActiveChildEditorTree(" + this + "): " + iterator.next()); }}*/
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("setActiveChildEditorTree(" + this + "): this editor (" + this
                            + ") must be the is the leaf editor");                                    // NOI18N
            }
            final Object oldActiveChildEditorId = this.getActiveChildEditorId();
            if (oldActiveChildEditorId != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this + "): there is still an old active child editor '"
                                + oldActiveChildEditorId + " registered");                            // NOI18N
                }
                if (this.getChildEditors().containsKey(oldActiveChildEditorId)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setActiveChildEditorTree(" + this + "): old active (visible) child editor '"
                                    + oldActiveChildEditorId
                                    + "' is a registered child of this editor, removing old editor"); // NOI18N
                    }
                    final BasicEditor oldActiveChildEditor = (BasicEditor)this.getChildEditors()
                                .get(oldActiveChildEditorId);

                    // XXX oder cancel?
                    oldActiveChildEditor.stopEditing();

                    return true;
                } else {
                    logger.error("setActiveChildEditorTree(" + this + "): old active child editor '"
                                + oldActiveChildEditorId + "' not found in list of this child editors"); // NOI18N
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("setActiveChildEditorTree(" + this + "): this editor (" + this
                                + ") IS the is the leaf editor!");                                       // NOI18N
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public Object getActiveChildEditorId() {
        return this.editorUIDelegate.getActiveChildEditorId();
    }

    // TableEditor Methoden ....................................................

    @Override
    public java.awt.Component getTableCellEditorComponent(final JTable table,
            final Object value,
            final boolean isSelected,
            final int row,
            final int column) {
        logger.error("getTableCellEditorComponent(" + this.getId()
                    + "): getTableCellEditorComponent should never be called");       // NOI18N
        return new JLabel(org.openide.util.NbBundle.getMessage(
                    AbstractSimpleEditor.class,
                    "AbstractSimpleEditor.getTableCellEditorComponent.JLabel.text")); // NOI18N
    }

    @Override
    public boolean stopCellEditing() {
        return this.stopEditing();
    }

    @Override
    public void cancelCellEditing() {
        this.cancelEditing();
    }

    @Override
    public void addCellEditorListener(final javax.swing.event.CellEditorListener l) {
        if (l instanceof EditorListener) {
            this.addEditorListener((EditorListener)l);
        } else {
            logger.warn("addCellEditorListener(" + this.getId() + "): listener not of type 'EditorListener'"); // NOI18N
        }
    }

    @Override
    public void removeCellEditorListener(final javax.swing.event.CellEditorListener l) {
        if (l instanceof EditorListener) {
            this.removeEditorListener((EditorListener)l);
        } else {
            logger.warn("removeCellEditorListener(" + this.getId() + "): listener not of type 'EditorListener'"); // NOI18N
        }
    }

    @Override
    public boolean shouldSelectCell(final java.util.EventObject anEvent) {
        return true;
    }

    @Override
    public boolean isCellEditable(final java.util.EventObject anEvent) {
        return this.isEditable(anEvent);
    }

    @Override
    public Object getCellEditorValue() {
        return this.getValue();
    }

    // Hilfsklassen ============================================================

    /**
     * Adds a PropertyChangeListener to the listener list.
     *
     * @param  l  The listener to add.
     */
    @Override
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.editorActivationDelegate.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     *
     * @param  l  The listener to remove.
     */
    @Override
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.editorActivationDelegate.removePropertyChangeListener(l);
    }

    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return (this.getId() != null) ? this.getId().toString() : null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Standardimplementierung eines.
     *
     * @version  $Revision$, $Date$
     */
    protected class SimpleEditorActivationDelegate extends AbstractEditorActivationDelegate {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new instance of ComplexEditorActivator.
         */
        public SimpleEditorActivationDelegate() {
            this.logger = Logger.getLogger(this.getClass());

            this.thisEditor = AbstractSimpleEditor.this;
            this.thisContainer = AbstractSimpleEditor.this;

            this.propertyChangeSupport = new SwingPropertyChangeSupport(AbstractSimpleEditor.this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected EditorListener createEditorListener() {
            return new SimpleEditorHandler();
        }

        /**
         * einfacher Editor -> parent container.
         *
         * @return  DOCUMENT ME!
         */
        @Override
        protected BasicContainer getParentContainerForUI() {
            return this.thisContainer.getParentContainer();
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Rudiment\u00E4re Dialogbox zum Anzeigen eines komplexen Editors (child) in einem simplen Editor (this).
     *
     * <p>Normalerweise sollte das UI eines komplexen Editors, der aus einem einfachen Editor heraus aufgerufen wird, im
     * UI des parent Containers des simplen Editors angezeigt werden.<br>
     * Diese Hilfsklasse erm\u00F6glicht es, das UI eines komplexen Editor mit Hilfe einer Dialogbox 'im' simplen Editor
     * anzuzeigen.</p>
     *
     * @version  $Revision$, $Date$
     */
    protected class SimpleEditorUIDelegate extends JDialog implements EditorUIDelegate {

        //~ Instance fields ----------------------------------------------------

        protected Object complexChildEditorId = null;
        Logger logger = Logger.getLogger(SimpleEditorUIDelegate.class);

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SimpleEditorUIDelegate object.
         */
        public SimpleEditorUIDelegate() {
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean showComplexEditorComponentUI(final Component complexChildEditorComponent,
                final Object complexChildEditorId) {
            if ((this.complexChildEditorId == null) && (this.getContentPane() == null)) {
                if ((complexChildEditorComponent != null) && (complexChildEditorId != null)) {
                    if ((AbstractSimpleEditor.this.editorActivationDelegate != null)
                                && (AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor() != null)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                        + "): showing complex editor UI '"
                                        + complexChildEditorComponent.getClass().getName() + "' ("
                                        + complexChildEditorId + ") in simple editor UI"); // NOI18N
                        }
                        this.setModal(true);
                        this.setName(AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId()
                                    .toString());

                        this.complexChildEditorId = complexChildEditorId;
                        // XXX \u00DCberpr\u00FCfung ...
                        this.setContentPane((JComponent)complexChildEditorComponent);

                        this.pack();
                        StaticSwingTools.showDialog(this);

                        this.uiChanged();
                        return true;
                    } else {
                        logger.error("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                    + "): no corresponding editor for component registered");          // NOI18N
                    }
                } else {
                    logger.warn("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                + "): showComplexEditorComponentUI(): child component or id is null"); // NOI18N
                }
            } else {
                logger.error("showComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                            + "): unexpected call to showComplexEditorComponentUI(): A Container can show only one complex editor at the same time ("
                            + this.complexChildEditorId + ")");                                        // NOI18N
            }

            return false;
        }

        @Override
        public boolean hideComplexEditorComponentUI(final Component complexChildEditorComponent,
                final Object complexChildEditorId) {
            if ((this.complexChildEditorId != null) && (this.getContentPane() != null)) {
                if ((complexChildEditorComponent != null) && (complexChildEditorId != null)) {
                    if (this.complexChildEditorId.equals(complexChildEditorId)) {
                        if ((AbstractSimpleEditor.this.editorActivationDelegate != null)
                                    && (AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor() != null)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                            + "): hiding complex editor UI"); // NOI18N
                            }

                            this.hide();
                            this.dispose();
                            this.setContentPane(null);
                            this.complexChildEditorId = null;

                            this.uiChanged();
                            return true;
                        } else {
                            logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                        + "): no corresponfing editor for component registered"); // NOI18N
                        }
                    } else {
                        logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                    + "): removed id '" + complexChildEditorId
                                    + "' does not match current active child editor id '" + this.complexChildEditorId
                                    + "'");                                                       // NOI18N
                    }
                } else {
                    logger.warn("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                                + "): child component or id is null");                            // NOI18N
                }
            } else {
                logger.error("hideComplexEditorComponentUI(" + AbstractSimpleEditor.this.getId()
                            + "): unexpected call to hideComplexEditorComponentUIr(): no complex editor shown ("
                            + complexChildEditorId + ")");                                        // NOI18N
            }

            return false;
        }

        @Override
        public java.awt.Component getComponent() {
            return this;
        }

        @Override
        public Object getActiveChildEditorId() {
            if ((this.complexChildEditorId == null)
                        && AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorRegistered()) {
                return AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId();
            }

            return this.complexChildEditorId;
        }

        /**
         * wird nicht delegiert!!!!
         */
        @Override
        public void uiChanged() {
            AbstractSimpleEditor.this.uiChanged();
        }
    }

    // PropertyChange Events ---------------------------------------------------

    /**
     * Rudiment\u00E4rer EditorListener, der den Wert des komplexen (child) Editors dem simplen Editor zuweist, von dem
     * aus er aufgerufen wurde (this).
     *
     * <p>Der Event wird ausgel\u00F6st, wenn auf dem komplexen (child) Editor cancel- oder stopEditing() aufgerufen
     * wird.</p>
     *
     * @version  $Revision$, $Date$
     */
    protected class SimpleEditorHandler implements EditorListener {

        //~ Instance fields ----------------------------------------------------

        protected Logger logger;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SimpleEditorHandler object.
         */
        public SimpleEditorHandler() {
            this.logger = Logger.getLogger(SimpleEditorHandler.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void editingCanceled(final ChangeEvent e) {
            final BasicEditor basicEditor = (BasicEditor)e.getSource();
            if (logger.isDebugEnabled()) {
                logger.debug("editingCanceled(" + AbstractSimpleEditor.this.getId()
                            + "): cancelEditing() on child editor '" + basicEditor.getId()
                            + "' called, ignoring input"); // NOI18N
            }

            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);
        }

        @Override
        public void editingStopped(final ChangeEvent e) {
            final BasicEditor basicEditor = (BasicEditor)e.getSource();
            if (logger.isDebugEnabled()) {
                logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId()
                            + "): stopEditing() on child editor '" + basicEditor.getId() + "' called, saving input"); // NOI18N
            }

            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);

            if (basicEditor.isValueChanged()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId()
                                + "): changes in complex child editor '" + basicEditor.getId()
                                + "', setting new value"); // NOI18N
                }
                // der neue Wert aus dem komplexen Editor ...
                final Object value = basicEditor.getValue();

                // den Wert diesem Editor UI zuweisen, damit er angezeigt wird (z.B. als String)
                AbstractSimpleEditor.this.setValue(value);

                // \u00C4nderung anzeigen!
                AbstractSimpleEditor.this.valueChanged = true;

                // den Wert diesem Editor zuweisen
                AbstractSimpleEditor.this.setComponentValue(value);

                fireEditingStopped();
            } else if (logger.isDebugEnabled()) {
                logger.debug("editingStopped(" + AbstractSimpleEditor.this.getId()
                            + "): no changes in complex child editor '" + basicEditor.getId() + "'"); // NOI18N
            }
        }

        /**
         * Wenn der komplexe Child Editor noch an diesem komplexen Editor registriert ist, wird er nun entfernt.
         *
         * @param  basicEditor  DOCUMENT ME!
         */
        protected void checkComplexChildRegistration(final BasicEditor basicEditor) {
            if (ComplexEditor.class.isAssignableFrom(basicEditor.getClass())) {
                if (AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorRegistered()
                            && AbstractSimpleEditor.this.editorActivationDelegate.getChildEditor().getId().equals(
                                basicEditor.getId())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("checkComplexChildRegistration(" + AbstractSimpleEditor.this.getId()
                                    + "): unregistering complex child editor '" + basicEditor.getId() + "'"); // NOI18N
                    }
                    AbstractSimpleEditor.this.removeComplexEditor((ComplexEditor)basicEditor);
                } else if (logger.isDebugEnabled()) {
                    logger.debug("checkComplexChildRegistration(" + AbstractSimpleEditor.this.getId()
                                + "): complex child editor '" + basicEditor.getId()
                                + "' not unregistered, possibly registered a simple child editor");           // NOI18N
                }
            }
        }

        /**
         * Wenn das komplexe Child Editor UI noch in diesem komplexen Editor angezeigt wird, wird es nun versteckt.
         *
         * @param  basicEditor  DOCUMENT ME!
         */
        protected void checkComplexChildVisibility(final BasicEditor basicEditor) {
            if (ComplexEditor.class.isAssignableFrom(basicEditor.getClass())) {
                if (AbstractSimpleEditor.this.editorActivationDelegate.isChildEditorVisible()
                            && (AbstractSimpleEditor.this.editorActivationDelegate.getChildEditorComponent()
                                == ((EditorUIDelegate)basicEditor).getComponent())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("checkComplexChildVisibility(" + AbstractSimpleEditor.this.getId()
                                    + "): hiding complex child editor ui '" + basicEditor.getId() + "' ("
                                    + AbstractSimpleEditor.this.editorActivationDelegate.getChildEditorComponent()
                                    .getClass().getName() + ")");                           // NOI18N
                    }
                    AbstractSimpleEditor.this.editorActivationDelegate.hideComplexEditorChildComponentUI();
                } else if (logger.isDebugEnabled()) {
                    logger.debug("checkComplexChildVisibility(" + AbstractSimpleEditor.this.getId()
                                + "): complex child editor '" + basicEditor.getId()
                                + "' not hidden, possibly shown in complex parent editor"); // NOI18N
                }
            }
        }

        @Override
        public void uiChanged(final ChangeEvent e) {
            // ignore
        }
    }
}
