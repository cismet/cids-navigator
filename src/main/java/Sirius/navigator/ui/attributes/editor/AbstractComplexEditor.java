/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AbstractComplexEditor.java
 *
 * Created on 10. August 2004, 11:38
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

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class AbstractComplexEditor extends AbstractSimpleEditor implements ComplexEditor {

    //~ Instance fields --------------------------------------------------------

    /** Enth\u00E4lt alle Editoren der Kinder dieses Objekts. */
    protected EditorLocator editorLocator = null;

    /** DOCUMENT ME! */
    protected EditorListener editorHandler;

    //~ Methods ----------------------------------------------------------------

    // Abstrakte AbstractSimpleEditor Methoden .................................
    /*protected abstract Object getComponentValue();
     *
     * protected abstract void initUI();
     *
     * public abstract boolean isEditable(java.util.EventObject anEvent);
     *
     *protected abstract void setComponentValue(Object value);*/

    // \u00DCberschriebene AbstractSimpleEditor Methodn ..............................

    /**
     * Macht das gleiche wie setValue().
     *
     * <p>Diese Methode ist bei einem komplexen Editor normalerweise nicht notwendig, da es hier idr mehr als eine UI
     * Komponenten gibt.</p>
     *
     * @param  value  der Wert, der im Editor gesetzt wird.
     */
    @Override
    protected void setComponentValue(final Object value) {
        this.setValue(value);
    }

    /**
     * Macht das gleiche wie getValue().
     *
     * <p>Diese Methode ist bei einem komplexen Editor normalerweise nicht notwendig, da es hier idr mehr als eine UI
     * Komponenten gibt. werden sollte.</p>
     *
     * @return  der Wert, der im Editor gesetzt wurde.
     */
    @Override
    protected Object getComponentValue() {
        return this.getValue();
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    protected void initUI() {
        if (this.getValue() != null) {
            if ((this.getChildEditors() != null) && (this.getChildEditors().size() > 0)
                        && (this.editorHandler != null)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("initUI(" + this.getId() + "): removing editor listeners from previous session"); // NOI18N
                }
                final Iterator iterator = this.getChildEditors().values().iterator();
                while (iterator.hasNext()) {
                    ((BasicEditor)iterator.next()).removeEditorListener(this.editorHandler);
                }
            }

            // Objekt nach editoren untersuchen
            this.childrenMap = this.editorLocator.getEditors(this.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("initUI(): " + this.childrenMap.size() + " child editors initialized"); // NOI18N
            }
        } else {
            logger.warn("initUI(): value is null, no child editors available");                      // NOI18N
            this.childrenMap = new HashMap();
        }
    }

    // EditorComponent Methoden ................................................

    @Override
    public abstract Object getValue(java.lang.Object key);
    @Override
    public abstract void setValue(java.lang.Object key, java.lang.Object value);

    /**
     * DOCUMENT ME!
     *
     * @param   parentContainer  DOCUMENT ME!
     * @param   id               DOCUMENT ME!
     * @param   value            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Component getEditorComponent(final BasicContainer parentContainer, final Object id, final Object value) {
        this.getEditorComponent(parentContainer, null, id, value);

        return this.editorUIDelegate.getComponent();
    }

    @Override
    public java.util.LinkedList getActiveChildEditorTree(final java.util.LinkedList activeChildEditorTree) {
        // XXX
        // um die Konsitenz zu wahren: (Complex(id:1) <-> Simple(id:2) <-> Complex(id:2) == id:1 <-> id:2
        final LinkedList activeComplexChildEditorTree = super.getActiveChildEditorTree(activeChildEditorTree);
        activeComplexChildEditorTree.addLast(this.getId());

        return activeComplexChildEditorTree;
    }

    @Override
    public boolean setActiveChildEditorTree(final java.util.LinkedList activeChildEditorTree) {
        // XXX
        // um die Konsitenz zu wahren: (Complex(id:1) <-> Simple(id:2) <-> Complex(id:2) == id:1 <-> id:2
        if (activeChildEditorTree.size() > 0) {
            activeChildEditorTree.removeFirst();
            return super.setActiveChildEditorTree(activeChildEditorTree);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("setActiveChildEditorTree(" + this + "):  this must be the leaf editor"); // NOI18N
            }
            return true;
        }
    }

    // -------------------------------------------------------------------------

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Hilfsklassen ===========================================================.
     *
     * @version  $Revision$, $Date$
     */
    protected class ComplexEditorActivationDelegate extends AbstractEditorActivationDelegate {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new instance of ComplexEditorActivator.
         */
        public ComplexEditorActivationDelegate() {
            this.logger = Logger.getLogger(this.getClass());

            this.thisEditor = AbstractComplexEditor.this;
            this.thisContainer = AbstractComplexEditor.this;

            this.propertyChangeSupport = new SwingPropertyChangeSupport(AbstractComplexEditor.this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected EditorListener createEditorListener() {
            return new ComplexEditorHandler();
        }

        /**
         * komplexer Editor -> this container.
         *
         * @return  DOCUMENT ME!
         */
        @Override
        protected BasicContainer getParentContainerForUI() {
            return this.thisContainer;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class ComplexEditorUIDelegate extends JPanel implements EditorUIDelegate {

        //~ Static fields/initializers -----------------------------------------

        public static final String PARENT_EDITOR = "rootComplexEditorUI"; // NOI18N
        public static final String CHILD_EDITOR = "childComplexEditorUI"; // NOI18N

        //~ Instance fields ----------------------------------------------------

        protected Logger logger;
        protected Object complexChildEditorId = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ComplexEditorUIDelegate object.
         */
        public ComplexEditorUIDelegate() {
            super(new CardLayout());

            this.logger = Logger.getLogger(ComplexEditorUIDelegate.class);
            this.add(PARENT_EDITOR, AbstractComplexEditor.this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean showComplexEditorComponentUI(final Component complexChildEditorComponent,
                final Object complexChildEditorId) {
            // logger.debug("components in complex editor: " + this.getComponentCount());
            if ((this.complexChildEditorId == null) && (this.getComponentCount() == 1)) {
                if ((complexChildEditorComponent != null) && (complexChildEditorId != null)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                    + "): showing complex editor UI '"
                                    + complexChildEditorComponent.getClass().getName() + "' (" + complexChildEditorId
                                    + ") in complex editor UI"); // NOI18N
                    }

                    if (AbstractComplexEditor.this.getChildEditors().containsKey(complexChildEditorId)) {
                        this.add(CHILD_EDITOR, complexChildEditorComponent);
                        ((CardLayout)this.getLayout()).show(this, CHILD_EDITOR);
                        this.complexChildEditorId = complexChildEditorId;

                        this.uiChanged();
                        return true;
                    } else {
                        logger.warn("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                    + "): complex child editor to be shown is no child of this editor"); // NOI18N
                    }
                } else {
                    logger.warn("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                + "): schild component or id is null");                                  // NOI18N
                }
            } else {
                logger.error("showComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                            + "): unexpected call to showComplexEditorComponentUI(): A Container can show only one complex editor at the same time ("
                            + this.complexChildEditorId + ")");                                          // NOI18N
            }

            return false;
        }

        @Override
        public boolean hideComplexEditorComponentUI(final Component complexChildEditorComponent,
                final Object complexChildEditorId) {
            if ((this.complexChildEditorId != null) && (this.getComponentCount() == 2)) {
                if ((complexChildEditorComponent != null) && (complexChildEditorId != null)) {
                    if (this.complexChildEditorId.equals(complexChildEditorId)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                        + "): hiding complex editor UI '"
                                        + complexChildEditorComponent.getClass().getName() + "' in complex editor UI"); // NOI18N
                        }
                        this.remove(complexChildEditorComponent);

                        if (this.getComponentCount() == 1) {
                            this.complexChildEditorId = null;

                            this.uiChanged();
                            return true;
                        } else {
                            logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                        + "): removal of complex child editor UI'"
                                        + complexChildEditorComponent.getClass().getName() + "' not sucessfull"); // NOI18N
                        }
                    } else {
                        logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                    + "): removed id '" + complexChildEditorId
                                    + "' does not match current active child editor id '" + this.complexChildEditorId
                                    + "'");                                                                       // NOI18N
                    }
                } else {
                    logger.warn("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                                + "): showComplexEditorComponentUI(): child component or id is null");            // NOI18N
                }
            } else {
                logger.error("hideComplexEditorComponentUI(" + AbstractComplexEditor.this.getId()
                            + "): unexpected call to hideComplexEditorComponentUIr(): no complex editor shown ("
                            + complexChildEditorId + ")");                                                        // NOI18N
            }

            return false;
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public Object getActiveChildEditorId() {
            return this.complexChildEditorId;
        }

        @Override
        public void uiChanged() {
            AbstractComplexEditor.this.uiChanged();
        }
    }

    /**
     * todo.
     *
     * @version  $Revision$, $Date$
     */
    protected class ComplexEditorHandler extends SimpleEditorHandler {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ComplexEditorHandler object.
         */
        public ComplexEditorHandler() {
            this.logger = Logger.getLogger(ComplexEditorHandler.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void editingCanceled(final ChangeEvent e) {
            final BasicEditor basicEditor = (BasicEditor)e.getSource();
            if (logger.isDebugEnabled()) {
                logger.debug("editingCanceled(" + AbstractComplexEditor.this.getId()
                            + "): cancelEditing() on child editor '" + basicEditor.getId()
                            + "' called, ignoring input"); // NOI18N
            }

            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);
        }

        @Override
        public void editingStopped(final ChangeEvent e) {
            final BasicEditor basicEditor = (BasicEditor)e.getSource();
            // if(logger.isDebugEnabled())logger.debug("stopEditing() on child editor '" + basicEditor.getId() + "'
            // called, saving input");

            this.checkComplexChildVisibility(basicEditor);
            this.checkComplexChildRegistration(basicEditor);

            if (basicEditor.isValueChanged()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("editingStopped(" + AbstractComplexEditor.this.getId() + "): changes in child editor '"
                                + basicEditor.getId() + "', setting new value"); // NOI18N
                }
                // der neue Wert aus dem Editor ...
                final Object value = basicEditor.getValue();

                // den Wert diesem Editor zuweisen
                AbstractComplexEditor.this.setValue(basicEditor.getId(), basicEditor.getValue());

                // zur\u00FCcksetzen
                basicEditor.setValueChanged(false);
                // \u00C4nderung anzeigen!
                AbstractComplexEditor.this.setValueChanged(true);
            } else if (logger.isDebugEnabled()) {
                logger.debug("editingStopped(" + AbstractComplexEditor.this.getId() + "): no changes in child editor '"
                            + basicEditor.getId() + "' detected"); // NOI18N
            }
        }
    }
}
