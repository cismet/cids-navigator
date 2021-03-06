/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.attributes.editor;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Abstrakte Implementierung zum Aktivieren / Deaktivieren eines komplexen Editors innerhalb eines simplen / komplexen
 * Editors.
 *
 * <p>Diese Klasse mu\u00DF von keinem konkreten simplen / komplexen Editor implementiert werden.</p>
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 * @see      AbstractSimpleEditor
 * @see      SimpleEditorActivationDelegate
 * @see      AbstractComplexEditor
 * @see      ComplexEditorActivationDelegate
 */
public abstract class AbstractEditorActivationDelegate implements EditorActivationDelegate, ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final String START_EDIT_COMMAND = "startEdit";   // NOI18N
    public static final String STOP_EDIT_COMMAND = "stopEdit";     // NOI18N
    public static final String CANCEL_EDIT_COMMAND = "cancelEdit"; // NOI18N
    public static final String SHOW_UI_COMMAND = "showUI";         // NOI18N
    public static final String HIDE_UI_COMMAND = "hideUI";         // NOI18N

    //~ Instance fields --------------------------------------------------------

    protected Logger logger = null;

    protected BasicEditor thisEditor = null;
    protected BasicContainer thisContainer = null;
    // protected BasicContainer parentContainer = null;

    /** Der egistrierte Child Editor. */
    protected ComplexEditor childEditor = null;

    /** Der zur Pegistrierung vorgesehene Child Editor. */
    protected ComplexEditor lazyChildEditor = null;

    protected Component editorComponent = null;

    protected EditorListener editorListener = null;

    /** Utility field used by bound properties. */
    protected SwingPropertyChangeSupport propertyChangeSupport;

    /** Holds value of property propertyChangeEnabled. */
    private boolean propertyChangeEnabled = false;

    //~ Methods ----------------------------------------------------------------

    /**
     * Kann \u00FCberschrieben werden, um eine interne Implementierung eines EditorListeners zu erzeugen.
     *
     * @return  DOCUMENT ME!
     */
    protected abstract EditorListener createEditorListener();

    /**
     * Liefert den parent Container, in dem das UI des komplexen child Editors angezeigt werden soll.
     *
     * <p>Bei einem komplexen Editor, ist dies normalerweise das UI des komplexen Editors selbst (also this), bei einem
     * simplen Editor ist dies normalerweise das UI in dem der simple Editor angezeigt wird (also this.parent), es sei
     * denn, das UI des komplexen child Editors soll in einem *modalen* Dialog angezeigt werden.</p>
     *
     * @return  der parent Container, in dem das UI des komplexen child Editors angezeigt werden soll
     */
    protected abstract BasicContainer getParentContainerForUI();
    /*{
     *  // komplexer Editor -> this container return this.thisContainer;  // einfacher Editor -> parent container return
     * this.thisContainer.getParent();}*/

    /**
     * DOCUMENT ME!
     *
     * @param   child  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected BasicContainer getRoot(final BasicContainer child) {
        final BasicContainer parent = child.getParentContainer();
        if (parent != null) {
            return this.getRoot(parent);
        } else {
            return child;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean addLazyComplexEditor() {
        final boolean ret = this.addComplexEditor(this.lazyChildEditor);

        // hm ...
        return ret;
    }

    @Override
    public boolean addComplexEditor(final ComplexEditor childEditor) {
        if (childEditor != null) {
            if (!this.isChildEditorAvailable() && !this.isChildEditorRegistered()) {
                if (childEditor.getId() != null) {
                    LinkedList oldActiveChildEditorTree = null;
                    BasicContainer rootContainer = null;
                    if (this.isPropertyChangeEnabled()
                                && (this.propertyChangeSupport.getPropertyChangeListeners().length > 0)) {
                        // System.exit(1);
                        if (logger.isDebugEnabled()) {
                            logger.debug("addComplexEditor(" + thisEditor + "): fire property change event"); // NOI18N
                        }
                        rootContainer = this.getRoot(this.thisContainer);
                        oldActiveChildEditorTree = rootContainer.getActiveChildEditorTree(new LinkedList());
                    }

                    this.setChildEditor(childEditor);
                    this.thisContainer.getChildEditors().put(childEditor.getId(), childEditor);

                    if (logger.isDebugEnabled()) {
                        logger.debug("addComplexEditor(" + thisEditor
                                    + "): preparing editor for complex edit action of complex editor '"
                                    + childEditor.getId() + "'"); // NOI18N
                    }

                    // neue Listener erzeugen
                    this.editorListener = this.createEditorListener();

                    // \u00C4nderungen implements komplexen Editor (child) -> \u00C4nderungen implements simplen
                    // Editor (this)
                    childEditor.addEditorListener(this.editorListener);

                    if ((oldActiveChildEditorTree != null) && (rootContainer != null)) {
                        final LinkedList newActiveChildEditorTree = rootContainer.getActiveChildEditorTree(
                                new LinkedList());
                        this.propertyChangeSupport.firePropertyChange(
                            ACTIVE_CHILD_EDITOR_TREE,
                            newActiveChildEditorTree,
                            oldActiveChildEditorTree);
                    }

                    return true;
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("addComplexEditor(" + thisEditor + "): id of complex child editor '"
                                    + childEditor.getClass().getName() + "' is null -> adding editor lazily"); // NOI18N
                    }
                    this.lazyChildEditor = childEditor;
                }
            } else {
                logger.error("addComplexEditor(" + thisEditor
                            + "): unexpected call to addComplexEditor(): A Container can hold only one complex editor at the same time"); // NOI18N
            }
        } else {
            logger.warn("addComplexEditor(" + thisEditor + "): addComplexEditor(): child editor is null");     // NOI18N
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean removeComplexEditor() {
        return this.removeComplexEditor(this.childEditor);
    }

    @Override
    public boolean removeComplexEditor(final ComplexEditor childEditor) {
        if (childEditor != null) {
            if (this.isChildEditorAvailable()
                        && (this.thisContainer.getChildEditors().remove(childEditor.getId()) != null)
                        && (this.editorListener != null)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("removeComplexEditor(" + thisEditor
                                + "): stopping complex edit action of complex editor '" + childEditor.getId() + "'"); // NOI18N
                }

                LinkedList oldActiveChildEditorTree = null;
                BasicContainer rootContainer = null;
                if (this.isPropertyChangeEnabled()
                            && (this.propertyChangeSupport.getPropertyChangeListeners().length > 0)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("removeComplexEditor(" + thisEditor + "): fire property change event"); // NOI18N
                    }
                    rootContainer = this.getRoot(this.thisContainer);
                    oldActiveChildEditorTree = rootContainer.getActiveChildEditorTree(new LinkedList());
                }

                childEditor.removeEditorListener(this.editorListener);

                this.editorListener = null;
                this.editorComponent = null;
                this.setChildEditor(null);

                if ((oldActiveChildEditorTree != null) && (rootContainer != null)) {
                    final LinkedList newActiveChildEditorTree = rootContainer.getActiveChildEditorTree(new LinkedList());
                    this.propertyChangeSupport.firePropertyChange(
                        ACTIVE_CHILD_EDITOR_TREE,
                        newActiveChildEditorTree,
                        oldActiveChildEditorTree);
                }

                return true;
            } else if (logger.isDebugEnabled()) {
                logger.error("removeComplexEditor(" + thisEditor
                            + "): unexpected call to removeComplexEditor(): no complex editor registered: "
                            + childEditor.getId()); // NOI18N
            }
        } else {
            logger.warn("removeComplexEditor(" + thisEditor + "): child editor is null"); // NOI18N
        }

        return false;
    }

    /**
     * Ruft showComplexEditorComponentUI auf dem parent Container auf.
     *
     * <p>UI des komplexen Editors anzeigen im komplexen Container -> intern (CardLayout)<br>
     * UI des komplexen Editors anzeigen im simplen Container -> Dialogbox</p>
     *
     * @return  DOCUMENT ME!
     */
    public boolean showComplexEditorChildComponentUI() {
        if (logger.isDebugEnabled()) {
            logger.debug("showComplexEditorChildComponentUI(" + thisEditor + "): called");                        // NOI18N
        }
        if ((this.childEditor != null) || (this.lazyChildEditor != null)) {
            if (this.isChildEditorVisible()) {
                logger.warn("showComplexEditorChildComponentUI(" + thisEditor + "): child editor still visible"); // NOI18N
                if (!this.hideComplexEditorChildComponentUI()) {
                    return false;
                }
            }

            if (this.childEditor == null) {
                // child editor UI erzeugen
                // thisContainer wird als parent container \u00FCbergeben

                this.editorComponent = this.lazyChildEditor.getEditorComponent(
                        this.thisContainer,
                        this.thisEditor.getId(),
                        this.thisEditor.getValue());
                if (logger.isDebugEnabled()) {
                    logger.debug("showComplexEditorChildComponentUI(" + thisEditor
                                + "): adding now lazily added child editor"); // NOI18N
                }
                this.addLazyComplexEditor();
            } else {
                // child editor UI erzeugen
                // thisContainer wird als parent container \u00FCbergeben
                this.editorComponent = this.childEditor.getEditorComponent(
                        this.thisContainer,
                        this.thisEditor.getId(),
                        this.thisEditor.getValue());
            }

            return this.getParentContainerForUI()
                        .showComplexEditorComponentUI(this.editorComponent, this.childEditor.getId());
        } else {
            logger.warn("showComplexEditorChildComponentUI(" + thisEditor
                        + "): child editor and lazy cild editor are null"); // NOI18N
        }

        return false;
    }

    /**
     * Ruft hideComplexEditorComponentUI auf dem parent Container auf.
     *
     * <p>UI des komplexen Editors entfernen aus komplexem Container -> intern (CardLayout)<br>
     * UI des komplexen Editors entfernen aus simplen Container -> Dialogbox</p>
     *
     * @return  DOCUMENT ME!
     */
    public boolean hideComplexEditorChildComponentUI() {
        if (logger.isDebugEnabled()) {
            logger.debug("hideComplexEditorChildComponentUI(" + thisEditor + "): called");                 // NOI18N
        }
        if (this.editorComponent != null) {
            if (this.getParentContainerForUI().hideComplexEditorComponentUI(
                            this.editorComponent,
                            this.childEditor.getId())) {
                this.editorComponent = null;
                return true;
            }
        } else {
            logger.warn("hideComplexEditorChildComponentUI(" + thisEditor + "): editorComponent is null"); // NOI18N
        }

        return false;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals(START_EDIT_COMMAND)) {
            this.addLazyComplexEditor();
        } else if (e.getActionCommand().equals(STOP_EDIT_COMMAND)) {
            this.removeComplexEditor();
        } else if (e.getActionCommand().equals(SHOW_UI_COMMAND)) {
            this.showComplexEditorChildComponentUI();
        } else if (e.getActionCommand().equals(HIDE_UI_COMMAND)) {
            this.hideComplexEditorChildComponentUI();
        } else if (logger.isDebugEnabled()) {
            logger.warn("actionPerformed(" + thisEditor + "): unrecognized action command '" + e.getActionCommand()
                        + "'"); // NOI18N
        }
    }

    /**
     * Getter for property childEditor.
     *
     * @return  Value of property childEditor.
     */
    protected ComplexEditor getChildEditor() {
        return this.childEditor;
    }

    /**
     * Setter for property childEditor.
     *
     * @param  childEditor  New value of property childEditor.
     */
    protected void setChildEditor(final ComplexEditor childEditor) {
        this.childEditor = childEditor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isChildEditorAvailable() {
        return this.childEditor != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isChildEditorRegistered() {
        if (this.isChildEditorAvailable()) {
            return this.thisContainer.getChildEditors().containsKey(this.getChildEditor().getId());
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isChildEditorVisible() {
        return this.getChildEditorComponent() != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Component getChildEditorComponent() {
        return this.editorComponent;
    }

    @Override
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Getter for property propertyChangeEnabled.
     *
     * @return  Value of property propertyChangeEnabled.
     */
    public boolean isPropertyChangeEnabled() {
        return this.propertyChangeEnabled;
    }

    /**
     * Setter for property propertyChangeEnabled.
     *
     * @param  propertyChangeEnabled  New value of property propertyChangeEnabled.
     */
    public void setPropertyChangeEnabled(final boolean propertyChangeEnabled) {
        this.propertyChangeEnabled = propertyChangeEnabled;
    }
}
