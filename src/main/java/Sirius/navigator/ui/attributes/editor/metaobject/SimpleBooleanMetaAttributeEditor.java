/*
 * SimpleBooleanMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 15:48
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.MetaObject;
import Sirius.navigator.ui.attributes.editor.*;

import org.apache.log4j.Logger;

/**
 * Ein Editor f\u00FCr Boolean Attribute.
 *
 * @author  Pascal
 */
public class SimpleBooleanMetaAttributeEditor extends AbstractSimpleMetaAttributeEditor
{
    
    /** Creates new form SimpleBooleanMetaAttributeEditor */
    public SimpleBooleanMetaAttributeEditor()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.editorActivationDelegate = new SimpleEditorActivationDelegate();
        this.editorUIDelegate = new SimpleEditorUIDelegate();
        this.readOnly = false;
        
        this.initComponents();
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

        booleanCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        booleanCheckBox.setMargin(new java.awt.Insets(2, 0, 2, 2));
        booleanCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                booleanCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(booleanCheckBox, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents

    private void booleanCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_booleanCheckBoxActionPerformed
    {//GEN-HEADEREND:event_booleanCheckBoxActionPerformed
        this.setValueChanged(true);
        this.stopEditing();
    }//GEN-LAST:event_booleanCheckBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JCheckBox booleanCheckBox;
    // End of variables declaration//GEN-END:variables
    
    protected void initUI()
    {
         this.booleanCheckBox.setEnabled(this.isEditable(null));
    }
    
    public boolean isEditable(java.util.EventObject anEvent)
    {
        return true;
    }
    
    protected Object getComponentValue()
    {
        return new Boolean(this.booleanCheckBox.isSelected());
    }
    
    protected void setComponentValue(Object value)
    { 
        if(logger.isDebugEnabled())logger.debug("setting boolean value: " + value);

        if(value != null)
        {
            Object attributeValue = this.getAttributeValue(value);
            if(attributeValue != null && attributeValue instanceof Boolean)
            {
                if(logger.isDebugEnabled())logger.debug("this.booleanCheckBox.setSelected(((Boolean)value).booleanValue()): " + ((Boolean)attributeValue).booleanValue());
                this.booleanCheckBox.setSelected(((Boolean)value).booleanValue());
            }
            else
            {
                logger.warn("new value (" + attributeValue + ") is null or not of type Boolean, setting value to 'FALSE'");
                this.setValue(new Boolean(false));
                this.booleanCheckBox.setSelected(false);
            }
        }
        else
        {
            // standardm\u00E4\u00DFig auf false setzen
            this.booleanCheckBox.setSelected(false);
            this.setValue(new Boolean(false));
            this.setValueChanged(true);
        } 
    }
    
    protected Sirius.navigator.ui.attributes.editor.metaobject.AbstractSimpleMetaAttributeEditor.ValueChangeListener getValueChangeListener()
    {
        return null;
    }
    
}
