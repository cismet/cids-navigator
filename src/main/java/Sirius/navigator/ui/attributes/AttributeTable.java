/*
 * AttributeTable.java
 *
 * Created on 3. Juni 2004, 11:55
 */

package Sirius.navigator.ui.attributes;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.net.*;

import org.apache.log4j.Logger;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.attributes.renderer.*;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author  pascal
 */
public class AttributeTable extends JXTable //implements ComplexContainer
{
    private final Logger logger;
    private final HashMap cellEditors;
    
    HyperlinkRenderer hyperlinkRenderer = new HyperlinkRenderer();
    
    //protected ComplexContainer complexContainerDelegate;
    
    public AttributeTable()
    {
        this(ResourceManager.getManager().getString("attribute.table.name"), ResourceManager.getManager().getString("attribute.table.value"));
    }
    
    
    public AttributeTable(String nameColumnName, String valueColumnName)
    {
        super(new AttributeTableModel(new String[]
        {nameColumnName, valueColumnName}));
        
        this.logger = Logger.getLogger(this.getClass());
        this.cellEditors = new HashMap();
        
        // XXX
        //this.setDefaultRenderer(Object.class, new HyperlinkRenderer());
        //this.setDefaultEditor(Integer.class, new HyperlinkRenderer());
        //this.setDefaultEditor(Boolean.class, new HyperlinkRenderer());
        //this.setDefaultEditor(Double.class, new HyperlinkRenderer());
        //this.setDefaultEditor(Float.class, new HyperlinkRenderer());
        //this.setDefaultRenderer(String.class, new HyperlinkRenderer());
        
        this.addMouseListener(new CellRendererMouseListener());
        
        if(PropertyManager.getManager().isAdvancedLayout())
        {
            this.setTableHeader(null);
        }
    }
    
    public void setAttributes(Collection metaAtributes)
    {
        this.getAttributeTableModel().setAttributes(metaAtributes);
    }
    
    public void clear()
    {
        //System.out.println("clear attribute table");
        if(this.cellEditor != null)
        {
            this.cellEditor.cancelCellEditing();
        }
        
        this.getAttributeTableModel().clear();
        this.clearSelection();
    }
    
    protected AttributeTableModel getAttributeTableModel()
    {
        return (AttributeTableModel)super.getModel();
    }
    
    public TableCellEditor getCellEditor(int row, int column)
    {
        TableCellEditor editor = null;
        
        if(column == 1)
        {
            editor = this.getAttributeTableModel().getCellEditor(row, column);
            
            if(editor == null)
            {
                editor = getDefaultEditor(this.getAttributeTableModel().getClassAt(row, column));
            }
        }
        
        return editor;
    }
    
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        TableCellRenderer renderer = null;
        
        if(column == 1)
        {
            /*renderer = this.getAttributeTableModel().getCellRenderer(row, column);
             
            if(renderer == null)
            {
                renderer = getDefaultEditor(this.getAttributeTableModel().getClassAt(row, column));
            }*/
            
            if(this.getAttributeTableModel().getValueAt(row, column) != null)
            {
                Attribute attribute = ((Attribute)this.getAttributeTableModel().getValueAt(row, column));
                if(attribute.referencesObject() && attribute.getValue() != null)
                {
                    try
                    {
                        new URL(attribute.getValue().toString());
                        return hyperlinkRenderer;
                        
                    }
                    catch(Throwable exp)
                    {
                        logger.debug("getCellRenderer() attribute " + attribute.getName() + " is no valid url");
                    }
                }
            }
        }
        
        return super.getCellRenderer(row, column);
    }
    
    /**
     * Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isEditable()
    {
        return this.getAttributeTableModel().isEditable();
    }
    
    /**
     * Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setEditable(boolean editable)
    {
        this.getAttributeTableModel().setEditable(editable);
        
        if(!editable && this.cellEditor != null)
        {
            this.cellEditor.cancelCellEditing();
        }
    }
    
    public String getToolTipText(MouseEvent e)
    {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realColumnIndex = convertColumnIndexToModel(colIndex);
        
        Object value = getValueAt(rowIndex, colIndex);
        if(value != null)
        {
            tip = value.toString();
        }
        
        return tip;
    }
    
    // #########################################################################
    
    /**
     * Gibt MouseEvents an Renderer weiter
     */
    protected class CellRendererMouseListener extends MouseAdapter
    {
        
        public void mouseClicked(MouseEvent event)
        {
           logger.debug("mouseClick");
            this.translateMouseEvent(event);
        }
        
        public void mousePressed(MouseEvent event)
        {
            this.translateMouseEvent(event);
        }
        
        public void mouseReleased(MouseEvent event)
        {
            this.translateMouseEvent(event);
        }
        
        protected void translateMouseEvent(MouseEvent event)
        {
            Point point = event.getPoint();
            int column = columnAtPoint(point);
            
            if(column == 1)
            {
                int row = rowAtPoint(point);
                TableCellRenderer renderer = getCellRenderer(row, column);
                
                if(renderer instanceof HyperlinkRenderer)
                {
                    //Component component = prepareRenderer(renderer, row, column);
                    Component component = ((HyperlinkRenderer)renderer).getComponent();
                    Rectangle cellRect = getCellRect(row, column, false);
                    point.translate(-cellRect.x, -cellRect.y);
                    
                    //MouseEvent newEvent = new MouseEvent(component, event.getID(), event.getWhen(), event.getModifiers(), point.x, point.y, event.getClickCount(), event.isPopupTrigger());
                    MouseEvent newEvent = SwingUtilities.convertMouseEvent(AttributeTable.this, event, component);
                    component.dispatchEvent(newEvent);
                }
            }
        }
    }
    
    // ComplexContainer implementation
    /*public final boolean addComplexEditor(ComplexEditor complexChildEditor)
    {
        this.complexContainerDelegate.addComplexEditor(complexChildEditor);
    }
     
    public final java.util.Map getChildEditors()
    {
        return this.complexContainerDelegate.getChildEditors();
    }
     
    public final java.awt.Component getComponent()
    {
        return this.complexContainerDelegate.getComponent();
    }
     
    public final BasicContainer getParentContainer()
    {
        return this.complexContainerDelegate.getParentContainer();
    }
     
    public final java.lang.Object getValue(java.lang.Object key)
    {
        return this.complexContainerDelegate.getValue(key);
    }
     
    public final boolean hideComplexEditorComponentUI(Component complexChildEditorComponent)
    {
        return this.complexContainerDelegate.hideComplexEditorComponentUI(complexChildEditorComponent);
    }
     
    public final boolean removeComplexEditor(ComplexEditor complexChildEditor)
    {
        return this.complexContainerDelegate.removeComplexEditor(complexChildEditor);
    }
     
    public final void setValue(java.lang.Object key, java.lang.Object value)
    {
        this.complexContainerDelegate.setValue(key, value);
    }
     
    public final boolean showComplexEditorComponentUI(Component complexChildEditorComponent)
    {
        return this.complexContainerDelegate.showComplexEditorComponentUI(complexChildEditorComponent);
    }*/
    
    /**
     * Delegate -> keine Mehrfachvererbung! :-(
     */
    /*protected class ComplexTableEditorDelegate extends AbstractComplexEditor
    {
     
        public java.lang.Object getValue(java.lang.Object key)
        {
            //return AttributeTable.this.g
            return null;
        }
     
        public boolean isEditable(java.util.EventObject anEvent)
        {
            AttributeTable.this.isEditable();
        }
     
        public void setValue(java.lang.Object key, java.lang.Object value)
        {
     
        }
    }*/
}
