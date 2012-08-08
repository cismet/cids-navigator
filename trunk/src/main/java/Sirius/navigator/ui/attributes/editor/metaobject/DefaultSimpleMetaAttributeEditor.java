/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultSimpleMetaAttributeEditor.java
 *
 * Created on 27. August 2004, 09:24
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import org.apache.log4j.Logger;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultSimpleMetaAttributeEditor extends AbstractSimpleMetaAttributeEditor // javax.swing.JPanel
{

    //~ Instance fields --------------------------------------------------------

    protected ValueChangeListener valueChangeListener;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField simpleValueField;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DefaultSimpleMetaAttributeEditor.
     */
    public DefaultSimpleMetaAttributeEditor() {
        this.logger = Logger.getLogger(this.getClass());

        this.editorActivationDelegate = new SimpleEditorActivationDelegate();
        this.editorUIDelegate = new SimpleEditorUIDelegate();
        valueChangeListener = this.getValueChangeListener();

        this.initComponents();

        this.simpleValueField.addFocusListener(valueChangeListener);
        this.simpleValueField.addActionListener(valueChangeListener);

        final InputMap inputMap = this.simpleValueField.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape"); // NOI18N

        final ActionMap actionMap = this.simpleValueField.getActionMap();
        actionMap.put("escape", new AbstractAction() // NOI18N
            {

                @Override
                public void actionPerformed(final ActionEvent ae) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("resetting text field"); // NOI18N
                    }
                    DefaultSimpleMetaAttributeEditor.this.setComponentValue(
                        DefaultSimpleMetaAttributeEditor.this.getValue());
                }
            });

        this.readOnly = false;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        simpleValueField = new javax.swing.JTextField();
        final DropTarget dropTarget = new DropTarget(this.simpleValueField, new DefaultDropTargetListener());

        setLayout(new java.awt.GridBagLayout());

        simpleValueField.setColumns(12);
        simpleValueField.setDragEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(simpleValueField, gridBagConstraints);
    }
    // </editor-fold>//GEN-END:initComponents

    @Override
    protected void initUI() {
        this.simpleValueField.setEnabled(this.isEditable(null));
        this.simpleValueField.setEditable(this.isEditable(null));
    }

    @Override
    protected Object getComponentValue() {
        return this.simpleValueField.getText();
    }

    /**
     * Setzt den Wert, der angezeigt werden soll.
     *
     * @param  value  ein Objekt vom Typ Attribut
     */
    @Override
    protected void setComponentValue(final Object value) {
        if (value != null) {
            this.simpleValueField.setText(value.toString());
        } else {
            this.simpleValueField.setText(null);
        }
    }

    @Override
    protected Sirius.navigator.ui.attributes.editor.metaobject.AbstractSimpleMetaAttributeEditor.ValueChangeListener
    getValueChangeListener() {
        return new DefaultSimpleValueChangeListener();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Speichert den Wert des Editors, wenn das Textfeld den Focus verliert oder ENTER gedr\u00FCckt wird.
     *
     * @version  $Revision$, $Date$
     */
    protected class DefaultSimpleValueChangeListener extends ValueChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        protected Object getNewValue() {
            return DefaultSimpleMetaAttributeEditor.this.simpleValueField.getText();
        }
    }

    /**
     * A Simple TransferHandler that exports the data as a String, and imports the data from the String clipboard. This
     * is only used if the UI hasn't supplied one, which would only happen if someone hasn't subclassed Basic.
     *
     * @version  $Revision$, $Date$
     */
    protected class DefaultDropTargetListener implements DropTargetListener {

        //~ Instance fields ----------------------------------------------------

        private Logger logger;
        private DataFlavor[] supportedDataFlavours;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DefaultDropTargetListener object.
         */
        public DefaultDropTargetListener() {
            this.logger = Logger.getLogger(this.getClass());

            this.supportedDataFlavours = new DataFlavor[1];

            // String
            this.supportedDataFlavours[0] = DataFlavor.stringFlavor;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   flavors  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private DataFlavor getFlavor(final DataFlavor[] flavors) {
            if (flavors != null) {
                for (int i = 0; i < flavors.length; i++) {
                    for (int j = 0; i < this.supportedDataFlavours.length; j++) {
                        if (flavors[i].equals(this.supportedDataFlavours[j])) {
                            return this.supportedDataFlavours[j];
                        }
                    }
                }
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   t       DOCUMENT ME!
         * @param   action  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private boolean importData(final Transferable t, final int action) {
            final DataFlavor dataFlavor = getFlavor(t.getTransferDataFlavors());

            try {
                if (dataFlavor != null) {
                    // String
                    if (dataFlavor.equals(this.supportedDataFlavours[0])) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("importData() importing String Data"); // NOI18N
                        }

                        final String data = (String)t.getTransferData(dataFlavor);
                        DefaultSimpleMetaAttributeEditor.this.simpleValueField.setText(data);
                        DefaultSimpleMetaAttributeEditor.this.valueChangeListener.actionPerformed();

                        return true;
                    }

                    // XXX i18n
                    JOptionPane.showMessageDialog(
                        DefaultSimpleMetaAttributeEditor.this,
                        org.openide.util.NbBundle.getMessage(
                            DefaultSimpleMetaAttributeEditor.class,
                            "DefaultSimpleMetaAttributeEditor.DefaultDropTargetListener.importData().errorOptionPane.ErrorMessage"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            DefaultSimpleMetaAttributeEditor.class,
                            "DefaultSimpleMetaAttributeEditor.DefaultDropTargetListener.importData().errorOptionPane.ErrorTitle"), // NOI18N
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (Throwable th) {
                logger.error("importData():  data import failed", th);                                                             // NOI18N
            }

            return false;
        }

        @Override
        public void dragEnter(final DropTargetDragEvent dtde) {
            if (this.getFlavor(dtde.getCurrentDataFlavors()) == null) {
                dtde.rejectDrag();
            }
        }

        @Override
        public void drop(final DropTargetDropEvent dtde) {
            if (!this.importData(dtde.getTransferable(), dtde.getDropAction())) {
                dtde.rejectDrop();
            }
        }

        @Override
        public void dragExit(final DropTargetEvent dte) {
            // if(logger.isDebugEnabled())logger.debug("dragExit()");
        }

        @Override
        public void dragOver(final DropTargetDragEvent dtde) {
            // if(logger.isDebugEnabled())logger.debug("dragOver()");
        }

        @Override
        public void dropActionChanged(final DropTargetDragEvent dtde) {
            // if(logger.isDebugEnabled())logger.debug("dropActionChangedr()");
        }
    }
}
