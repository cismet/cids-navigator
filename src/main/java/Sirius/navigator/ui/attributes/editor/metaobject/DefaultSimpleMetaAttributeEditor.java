/*
 * DefaultSimpleMetaAttributeEditor.java
 *
 * Created on 27. August 2004, 09:24
 */

package Sirius.navigator.ui.attributes.editor.metaobject;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.MetaObject;
import Sirius.navigator.ui.attributes.editor.*;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.Resource;

/**
 *
 * @author  pascal
 */
public class DefaultSimpleMetaAttributeEditor extends AbstractSimpleMetaAttributeEditor //javax.swing.JPanel
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    protected ValueChangeListener valueChangeListener;
    
    /** Creates new form DefaultSimpleMetaAttributeEditor */
    public DefaultSimpleMetaAttributeEditor()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.editorActivationDelegate = new SimpleEditorActivationDelegate();
        this.editorUIDelegate = new SimpleEditorUIDelegate();
        valueChangeListener = this.getValueChangeListener();
        
        this.initComponents();
        
        this.simpleValueField.addFocusListener(valueChangeListener);
        this.simpleValueField.addActionListener(valueChangeListener);
        
        InputMap inputMap = this.simpleValueField.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        
        ActionMap actionMap = this.simpleValueField.getActionMap();
        actionMap.put("escape", new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                if(logger.isDebugEnabled())logger.debug("resetting text field");
                DefaultSimpleMetaAttributeEditor.this.setComponentValue(DefaultSimpleMetaAttributeEditor.this.getValue());
            }
        });
        
        this.readOnly = false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        simpleValueField = new javax.swing.JTextField();
        DropTarget dropTarget = new DropTarget(this.simpleValueField, new DefaultDropTargetListener());

        setLayout(new java.awt.GridBagLayout());

        simpleValueField.setColumns(12);
        simpleValueField.setDragEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(simpleValueField, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    protected void initUI()
    {
        this.simpleValueField.setEnabled(this.isEditable(null));
        this.simpleValueField.setEditable(this.isEditable(null));
    }

    protected Object getComponentValue()
    {
        return this.simpleValueField.getText();
    }
    
    /**
     * Setzt den Wert, der angezeigt werden soll.
     *
     * @param value ein Objekt vom Typ Attribut
     */
    protected void setComponentValue(Object value)
    {
        if(value != null)
        {
            this.simpleValueField.setText(value.toString());
        }
        else
        {
            this.simpleValueField.setText(null);
        }
    }

    protected Sirius.navigator.ui.attributes.editor.metaobject.AbstractSimpleMetaAttributeEditor.ValueChangeListener getValueChangeListener()
    {
        return new DefaultSimpleValueChangeListener();
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField simpleValueField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Speichert den Wert des Editors, wenn das Textfeld den Focus verliert oder
     * ENTER gedr\u00FCckt wird.
     */
    protected class DefaultSimpleValueChangeListener extends ValueChangeListener
    {
        protected Object getNewValue()
        {
            return DefaultSimpleMetaAttributeEditor.this.simpleValueField.getText();
        }
    }
    
    /**
     * A Simple TransferHandler that exports the data as a String, and
     * imports the data from the String clipboard.  This is only used
     * if the UI hasn't supplied one, which would only happen if someone
     * hasn't subclassed Basic.
     */
    protected class DefaultDropTargetListener implements DropTargetListener
    {
        private Logger logger;
        private DataFlavor[] supportedDataFlavours;
        
        public DefaultDropTargetListener()
        {
            this.logger = Logger.getLogger(this.getClass());
            
            this.supportedDataFlavours = new DataFlavor[1];
                
            // String
            this.supportedDataFlavours[0] = DataFlavor.stringFlavor;
        }
        
        private DataFlavor getFlavor(DataFlavor[] flavors)
        {
            if (flavors != null)
            {
                for(int i = 0; i < flavors.length; i++)
                {
                    for(int j = 0; i < this.supportedDataFlavours.length; j++)
                    {
                        if(flavors[i].equals(this.supportedDataFlavours[j]))
                        {
                            return this.supportedDataFlavours[j];
                        }
                    }
                }
            }
            
            return null;
        }
        
        private boolean importData(Transferable t, int action)
        {
            DataFlavor dataFlavor = getFlavor(t.getTransferDataFlavors());
            
            try
            {
                if(dataFlavor != null)
                {
                    // String
                    if(dataFlavor.equals(this.supportedDataFlavours[0]))
                    {
                        if(logger.isDebugEnabled())logger.debug("importData() importing String Data");

                        String data = (String)t.getTransferData(dataFlavor);
                        DefaultSimpleMetaAttributeEditor.this.simpleValueField.setText(data);
                        DefaultSimpleMetaAttributeEditor.this.valueChangeListener.actionPerformed();

                        return true;
                    }
                    
                    // XXX i18n
                    JOptionPane.showMessageDialog(DefaultSimpleMetaAttributeEditor.this,
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleMetaAttributeEditor.DefaultDropTargetListener.importData().errorOptionPane.ErrorMessage"),
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleMetaAttributeEditor.DefaultDropTargetListener.importData().errorOptionPane.ErrorTitle"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
            catch (Throwable th)
            {
                logger.error("importData():  data import failed", th);
            }   
            
            return false;
        }
        
        public void dragEnter(DropTargetDragEvent dtde)
        {
            if(this.getFlavor(dtde.getCurrentDataFlavors()) == null)
            {
                dtde.rejectDrag();
            }
        }
        
        public void drop(DropTargetDropEvent dtde)
        {
            if(!this.importData(dtde.getTransferable(), dtde.getDropAction()))
            {
                dtde.rejectDrop();
            }
        }
        
        
        public void dragExit(DropTargetEvent dte)
        {
           // if(logger.isDebugEnabled())logger.debug("dragExit()");
        }
        
        public void dragOver(DropTargetDragEvent dtde)
        {
            //if(logger.isDebugEnabled())logger.debug("dragOver()");
        }
        
        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            // if(logger.isDebugEnabled())logger.debug("dropActionChangedr()");
        }
    }
}
