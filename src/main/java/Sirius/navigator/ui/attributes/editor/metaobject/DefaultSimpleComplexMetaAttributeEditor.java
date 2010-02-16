/*
 * DefaultSimpleMetaAttributeEditor.java
 *
 * Created on 26. August 2004, 15:49
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

import org.apache.log4j.Logger;


import Sirius.navigator.ui.attributes.editor.*;
import Sirius.navigator.types.treenode.*;
import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;
import Sirius.navigator.ui.attributes.*;
import Sirius.navigator.ui.dnd.MetaTransferable;
import Sirius.navigator.resource.ResourceManager;
import de.cismet.cids.tools.fromstring.StringCreateable;



/**
 * Ein einfacher Standard Editor f\u00FCr komplexe Meta Attribute.<p>
 * Unterst\u00FCtzt Drag & Drop von ObjectTreeNodes und String, falls das Meta
 * Attribut dies unterst\u00FCtzt.
 *
 * @author  Pascal
 */
public class DefaultSimpleComplexMetaAttributeEditor extends AbstractSimpleMetaAttributeEditor //javax.swing.JPanel
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    private static final ResourceManager resource = ResourceManager.getManager();
    /**
     * Gibt an, ob dieses komplexe Object als String ver\u00E4ndert wurde.
     */
    //protected boolean isStringEdited = false;
    //protected ValueChangeListener valueChangeListener;
    
    protected ValueChangeListener valueChangeListener;
    
    /** Creates new form DefaultSimpleMetaAttributeEditor */
    public DefaultSimpleComplexMetaAttributeEditor()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.editorActivationDelegate = new SimpleEditorActivationDelegate();
        this.editorUIDelegate = new SimpleEditorUIDelegate();
        this.valueChangeListener = this.getValueChangeListener();
        
        this.initComponents();
        
        // Beim Dr\u00FCcken auf den [...]-Button sollte sich der komplexe Editor \u00F6ffnen
        // (oder auch nicht ...);
        this.complexEditorButton.addActionListener(this.editorActivationDelegate);
        
        this.simpleValueField.addFocusListener(valueChangeListener);
        this.simpleValueField.addActionListener(valueChangeListener);
        
        this.complexEditorButton.setPreferredSize(new Dimension(this.simpleValueField.getPreferredSize().height, this.complexEditorButton.getPreferredSize().width));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        linkLabel = new javax.swing.JLabel();
        simpleValueField = new javax.swing.JTextField();
        DropTarget dropTarget = new DropTarget(this.simpleValueField, new MetaAttributeDropTargetListener());
        complexEditorButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        linkLabel.setMaximumSize(new java.awt.Dimension(18, 18));
        linkLabel.setMinimumSize(new java.awt.Dimension(18, 18));
        linkLabel.setPreferredSize(new java.awt.Dimension(18, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(linkLabel, gridBagConstraints);

        simpleValueField.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(simpleValueField, gridBagConstraints);

        complexEditorButton.setText(I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.complexEditorButton.text")); // NOI18N
        complexEditorButton.setActionCommand(AbstractSimpleEditor.SimpleEditorActivationDelegate.SHOW_UI_COMMAND);
        complexEditorButton.setEnabled(false);
        complexEditorButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        complexEditorButton.setMaximumSize(new java.awt.Dimension(0, 0));
        complexEditorButton.setMinimumSize(new java.awt.Dimension(15, 20));
        complexEditorButton.setPreferredSize(new java.awt.Dimension(15, 20));
        complexEditorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                complexEditorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(complexEditorButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void complexEditorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_complexEditorButtonActionPerformed
    {//GEN-HEADEREND:event_complexEditorButtonActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_complexEditorButtonActionPerformed
    
    /**
     * Der Wert wurde schon im ValueChanged Listener ver\u00E4ndert
     */
    protected Object getComponentValue()
    {
        return this.getValue();
    }
    
    protected void setComponentValue(Object value)
    {
        MetaObject MetaObject = this.getMetaObject(value);
        if(MetaObject != null)
        {
            this.simpleValueField.setText(MetaObject.toString());
        }
        else
        {
            this.simpleValueField.setText(null);
        }
    }
    
    /**
     * Der [...]-Button interessiert uns nur, wenn ein komplexer child editor verf\u00FCgbar ist ...
     */
    public Component getEditorComponent(BasicContainer parentContainer, ComplexEditor complexChildEditor, Object id, Object value)
    {
        Component editorComponent = super.getEditorComponent(parentContainer, complexChildEditor, id, value);
        if(this.complexEditorButton != null)
        {
            this.complexEditorButton.setEnabled(complexChildEditor != null);
        }
        
        return editorComponent;
    }
    
    protected void initUI()
    {
        this.simpleValueField.setEnabled(this.isStringCreateable((Attribute)this.getValue()));
        this.simpleValueField.setEditable(this.isEditable(null));
        
        if(this.getValue() != null && this.getMetaObject(this.getValue()) != null && this.getMetaObject(this.getValue()).getID() != -1)
        {
            this.linkLabel.setIcon(resource.getIcon(
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.linkLabel.linkIcon")));
        }
        else
        {
            this.linkLabel.setIcon(resource.getIcon(
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.linkLabel.copyIcon")));
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton complexEditorButton;
    private javax.swing.JLabel linkLabel;
    protected javax.swing.JTextField simpleValueField;
    // End of variables declaration//GEN-END:variables
    
    // Hilfsmethoden ...........................................................
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
                    logger.error("getMetaObject(" + this + ") value of Attribute '" + ((Attribute)value).getName() + "' is not of type MetaObject (" + attributeValue.getClass().getName() + ")");
                }
            }
            else
            {
                logger.error("getMetaObject(" + this + ") value is not of type Attribute or MetaObject (" + value.getClass().getName() + ")");
            }
        }
        
        return null;
    }
    
    protected boolean setValueFromString(Object object, String newValue) throws Exception
    {
        if(object != null)
        {
            if(logger.isDebugEnabled())logger.debug("setValueFromString(): setting value from string " + newValue);
            //Object object = ((StringCreateable)MetaObject).fromString(newValue, MetaObject);
            Object newObject = ((StringCreateable)object).fromString(newValue, this.getMetaObject(object));
            this.setComponentValue(newObject);
            this.setValue(newObject);
            return true;
        }
        
        return false;
    }
    
    protected boolean setValueFromDragAndDrop(MetaObject oldMetaObject, MetaObject newMetaObject, boolean link)
    {
        if(oldMetaObject.getClassKey().equals(newMetaObject.getClassKey()))
        {
            // Kopie!!!
            this.setValue(newMetaObject);
            
            // Kopie ver\u00E4ndern
            //this.getMetaObject(this.getValue()).setChanged(true);
            
            if(!link)
            {
                if(logger.isDebugEnabled())logger.debug("setValueFromDragAndDrop() creating a copy of the selected meta object");
                this.getMetaObject(this.getValue()).setPrimaryKey(new Integer(-1));
                this.linkLabel.setIcon(resource.getIcon(
                        I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.linkLabel.copyIcon")));
            }
            else
            {
                if(logger.isDebugEnabled())logger.debug("setValueFromDragAndDrop() creating a link to the selected meta object");
                this.linkLabel.setIcon(resource.getIcon(
                        I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.linkLabel.linkIcon")));
            }
            
            this.setComponentValue(this.getValue());
            this.setValueChanged(true);
            return this.stopEditing();
        }
        else
        {
            String oldClassName = oldMetaObject.getClassKey();
            String newClassName = newMetaObject.getClassKey();
            
            try
            {
                oldClassName = Sirius.navigator.connection.SessionManager.getProxy().getMetaClass(oldClassName).getName();
                newClassName = Sirius.navigator.connection.SessionManager.getProxy().getMetaClass(newClassName).getName();
            }
            catch(Throwable t)
            {
                logger.warn("setValueFromDragAndDrop(): could not retrieve class names", t);
            }
            
            // XXX i18n
            JOptionPane.showMessageDialog(DefaultSimpleComplexMetaAttributeEditor.this,
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.setValueFromDragAndDrop().ErrorMessage1") + oldClassName +
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.setValueFromDragAndDrop().ErrorMessage2") + newClassName +
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.setValueFromDragAndDrop().ErrorMessage3"),
                    I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.setValueFromDragAndDrop().ErrorTitle"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    
    protected Sirius.navigator.ui.attributes.editor.metaobject.AbstractSimpleMetaAttributeEditor.ValueChangeListener getValueChangeListener()
    {
        return new DefaultSimpleComplexValueChangeListener();
    }
    
    /**
     * Speichert den Wert des Editors, wenn das Textfeld den Focus verliert oder
     * ENTER gedr\u00FCckt wird.
     */
    protected class DefaultSimpleComplexValueChangeListener extends ValueChangeListener
    {
        protected Object getNewValue()
        {
            return DefaultSimpleComplexMetaAttributeEditor.this.simpleValueField.getText();
        }
        
        protected void actionPerformed()
        {
            DefaultSimpleComplexMetaAttributeEditor.this.setValueChanged(DefaultSimpleComplexMetaAttributeEditor.this.isValueChanged() | this.isChanged());
            if(DefaultSimpleComplexMetaAttributeEditor.this.isValueChanged())
            {
                try
                {
                    //MetaObject MetaObject = getMetaObject(getValue());
                    if(isStringCreateable((Attribute)getValue()) && DefaultSimpleComplexMetaAttributeEditor.this.setValueFromString(getValue(), this.getNewValue().toString()))
                    {
                        if(logger.isDebugEnabled())logger.debug("actionPerformed(" + DefaultSimpleComplexMetaAttributeEditor.this.getId() + "): saves new input");
                        DefaultSimpleComplexMetaAttributeEditor.this.stopEditing();
                    }
                    else
                    {
                        logger.error("actionPerformed(" + DefaultSimpleComplexMetaAttributeEditor.this.getId() + "): value is not from String createable");
                    }
                    
                }
                catch(Throwable t)
                {
                    logger.error("actionPerformed(" + DefaultSimpleComplexMetaAttributeEditor.this.getId() + "): from String creation ('" + this.getNewValue() + "' failed", t);
                    
                    // XXX i18n
                    JOptionPane.showMessageDialog(DefaultSimpleComplexMetaAttributeEditor.this,
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.actionPerformed().ErrorMessage"),
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.actionPerformed().ErrorTitle"), JOptionPane.ERROR_MESSAGE);
                    
                    // reset
                    setComponentValue(getValue());
                }
            }
        }
    }
    
    /**
     * A Simple TransferHandler that exports the data as a String, and
     * imports the data from the String clipboard.  This is only used
     * if the UI hasn't supplied one, which would only happen if someone
     * hasn't subclassed Basic.
     */
    private class MetaAttributeDropTargetListener implements DropTargetListener
    {
        private Logger logger;
        private DataFlavor[] supportedDataFlavours;
        
        public MetaAttributeDropTargetListener()
        {
            this.logger = Logger.getLogger(this.getClass());
            
            try
            {
                this.supportedDataFlavours = new DataFlavor[3];
                
                // String
                this.supportedDataFlavours[0] = DataFlavor.stringFlavor;
                // MetaTreeNode
                this.supportedDataFlavours[1] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + DefaultMetaTreeNode.class.getName());
                // ObjectTreeNode
                this.supportedDataFlavours[2] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ObjectAttributeNode.class.getName());
            }
            catch (ClassNotFoundException cnfe)
            {
                logger.error("getTransferDataFlavors() could not create DnD data flavours", cnfe);
                this.supportedDataFlavours = new DataFlavor[0];
            }
        }
        
        private boolean isLink(Transferable t)
        {
            if(Sirius.navigator.ui.dnd.MetaTransferable.class.isAssignableFrom(t.getClass()))
            {
                logger.fatal("((MetaTransferable)t).getTransferAction(): " + ((Sirius.navigator.ui.dnd.MetaTransferable)t).getTransferAction());
                return (((MetaTransferable)t).getTransferAction() & java.awt.dnd.DnDConstants.ACTION_LINK) != 0;
            }
            
            logger.fatal("((MetaTransferable)t).getTransferAction(): " + ((Sirius.navigator.ui.dnd.MetaTransferable)t).getTransferAction());
            
            return false;
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
                            logger.debug(flavors[i]);
                            return this.supportedDataFlavours[j];
                        }
                    }
                }
            }
            
            return null;
        }
        
        private boolean isLink(int action)
        {
            return (action & java.awt.dnd.DnDConstants.ACTION_LINK) != 0;
        }
        
        private boolean importData(Transferable t, int action)
        {
            DataFlavor dataFlavor = getFlavor(t.getTransferDataFlavors());
            try
            {
                if(dataFlavor != null)
                {
                    if(dataFlavor.equals(this.supportedDataFlavours[0]))
                    {
                        if(logger.isDebugEnabled())logger.debug("importData() importing String Data");
                        
                        String data = (String)t.getTransferData(dataFlavor);
                        DefaultSimpleComplexMetaAttributeEditor.this.simpleValueField.setText(data);
                        DefaultSimpleComplexMetaAttributeEditor.this.valueChangeListener.actionPerformed();
                        
                        return true;
                    }
                    else if(dataFlavor.equals(this.supportedDataFlavours[1]))
                    {
                        if(logger.isDebugEnabled())logger.debug("importData() importing ObjectTreeNode Data");
                        Object object = t.getTransferData(dataFlavor);
                        
                        if(object != null && object instanceof ObjectTreeNode)
                        {
                            MetaObject oldMetaObject = DefaultSimpleComplexMetaAttributeEditor.this.getMetaObject(DefaultSimpleComplexMetaAttributeEditor.this.getValue());
                            MetaObject newMetaObject = ((ObjectTreeNode)object).getMetaObject();
                            
                            return DefaultSimpleComplexMetaAttributeEditor.this.setValueFromDragAndDrop(oldMetaObject, newMetaObject,  this.isLink(action));
                        }
                        else if(logger.isDebugEnabled())
                        {
                            logger.warn("not supported MetaTreeNode: " + object);
                        }
                    }
                    else if(dataFlavor.equals(this.supportedDataFlavours[2]))
                    {
                        if(logger.isDebugEnabled())logger.debug("importData() importing ObjectAttributeNode Data");
                        Object object = t.getTransferData(dataFlavor);
                        
                        if(object != null && object instanceof ObjectAttributeNode)
                        {
                            MetaObject oldMetaObject = DefaultSimpleComplexMetaAttributeEditor.this.getMetaObject(DefaultSimpleComplexMetaAttributeEditor.this.getValue());
                            MetaObject newMetaObject = ((ObjectAttributeNode)object).getMetaObject();
                            
                            return DefaultSimpleComplexMetaAttributeEditor.this.setValueFromDragAndDrop(oldMetaObject, newMetaObject, this.isLink(action));
                        }
                        else if(logger.isDebugEnabled())
                        {
                            logger.warn("not supported AttributeTreeNode: " + object);
                        }
                    }
                    
                    // XXX i18n
                    JOptionPane.showMessageDialog(DefaultSimpleComplexMetaAttributeEditor.this,
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.importData().ErrorMessage"),
                            I18N.getString("Sirius.navigator.ui.attributes.editor.metaobject.DefaultSimpleComplexMetaAttributeEditor.importData().ErrorTitle"), JOptionPane.WARNING_MESSAGE);
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
            if(logger.isDebugEnabled())logger.debug("dragEnter()");
            
            if(this.getFlavor(dtde.getCurrentDataFlavors()) == null)
            {
                dtde.rejectDrag();
            }
        }
        
        public void drop(DropTargetDropEvent dtde)
        {
            if(logger.isDebugEnabled())logger.debug("drop()");
            
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
