/*
 * SimpleBooleanMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 15:48
 */

package Sirius.navigator.ui.attributes.editor.metaobject;
import java.awt.event.ItemEvent;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;

/**
 * 
 *
 * @author  Pascal
 */
public abstract class SimpleObjectSelectionMetaAttributeEditor extends AbstractSimpleMetaAttributeEditor
{
    protected Map selectionValues;
    
    /** Creates new form SimpleBooleanMetaAttributeEditor */
    public SimpleObjectSelectionMetaAttributeEditor()
    {
        this.logger = Logger.getLogger(this.getClass());
        this.selectionValues = this.getSelectionValues();
        
        this.editorActivationDelegate = new SimpleEditorActivationDelegate();
        this.editorUIDelegate = new SimpleEditorUIDelegate();
        this.readOnly = false;
        
        this.initComponents();
        
        this.stringComboBox.setModel((new DefaultComboBoxModel((Object[])selectionValues.keySet().toArray(new Object[selectionValues.size()]))));
    }
    
    protected abstract Map getSelectionValues();
    
    /*protected Map getSelectionValues()
    {
         HashMap selectionValues = new HashMap();
         
         selectionValues.put("Wert 1", null);
         selectionValues.put("Wert 2", new Boolean(true));
         selectionValues.put("Wert 3", "w3");
         
         return selectionValues;
    }*/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()                          
    {
        java.awt.GridBagConstraints gridBagConstraints;

        stringComboBox = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        stringComboBox.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                stringComboBoxItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(stringComboBox, gridBagConstraints);

    }                        

    private void stringComboBoxItemStateChanged(java.awt.event.ItemEvent evt)                                                
    {                                                    
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            if(logger.isDebugEnabled())logger.debug("stringComboBoxItemStateChanged() item selected: " + evt.getItem());
            this.setValueChanged(true);
            this.stopEditing();
        }
    }                                               
    
    
    // Variables declaration - do not modify                     
    private javax.swing.JComboBox stringComboBox;
    // End of variables declaration                   
    
    protected void initUI()
    {
         this.stringComboBox.setEnabled(this.isEditable(null));
    }
    
    public boolean isEditable(java.util.EventObject anEvent)
    {
        return true;
    }
    
    protected Object getComponentValue()
    {
        Object value = this.selectionValues.get(this.stringComboBox.getSelectedItem());
        
        if(value == null && logger.isDebugEnabled())
        {
            logger.debug("value of item '" + this.stringComboBox.getSelectedItem() + "' is null, return item");
        }
        
        //if(logger.isDebugEnabled())logger.debug("value of item '" + this.stringComboBox.getSelectedItem() + "' is null, return item");
        //return this.stringComboBox.getSelectedItem();
        
        return value;
    }
    
    // value ist immer ein Attribut!!!
    protected void setComponentValue(Object value)
    { 
        if(logger.isDebugEnabled())logger.debug("setComponentValue(): setting value: " + value);
        if(value != null && this.getAttributeValue(value) != null)
        {
            Object attributeValue = this.getAttributeValue(value);
            if(this.selectionValues.values().contains(attributeValue))
            {
                if(logger.isDebugEnabled())logger.debug("setComponentValue(value): this.stringComboBox.setSelectedItem(value): " + value);
                Iterator iterator = this.selectionValues.keySet().iterator();
                while(iterator.hasNext())
                {
                    Object key = iterator.next();
                    if(this.selectionValues.get(key) != null && this.selectionValues.get(key).equals(attributeValue))
                    {
                        this.stringComboBox.setSelectedItem(key);
                        break;
                    }
                }
            }
            else if(this.selectionValues.keySet().contains(attributeValue))
            {
                if(logger.isDebugEnabled())logger.debug("setComponentValue(key): this.stringComboBox.setSelectedItem(value): " + value);
                this.stringComboBox.setSelectedItem(attributeValue);
            }
            else
            {
                logger.warn("setComponentValue(): new value (" + value + ") is not in the list of allowed selection values");
                this.stringComboBox.setSelectedIndex(0);
                
                /*logger.debug(value.getClass());
                Iterator iterator = this.selectionValues.iterator();
                while(iterator.hasNext())
                {
                    logger.debug(iterator.next());
                }*/
            }
        }
        else
        {
            // standardm\u00E4\u00DFig auf 0 setzen
            this.stringComboBox.setSelectedIndex(0);
            if(logger.isDebugEnabled())logger.debug("setComponentValue(): value is null, setting index 0 to: " + this.getComponentValue());
            this.setValue(this.getComponentValue());
            this.setValueChanged(true);
        } 
    }
    
    protected Sirius.navigator.ui.attributes.editor.metaobject.AbstractSimpleMetaAttributeEditor.ValueChangeListener getValueChangeListener()
    {
        return null;
    }  
}
