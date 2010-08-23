/*
 * AttributeTableModel.java
 *
 * Created on 24. Juni 2004, 16:53
 */

package Sirius.navigator.ui.attributes;

import java.util.*;
import javax.swing.table.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
class AttributeTableModel extends AbstractTableModel
{
    private final Logger logger = Logger.getLogger(this.getClass());
    
    private final static int MIN_ROWS = 0;
    private final LinkedList metaAttributes = new LinkedList();
    
    private final String[] columnNames;
    
    /**
     * Holds value of property editable.
     */
    private boolean editable;
    
    /**
     * Utility field used by bound properties.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport =  new java.beans.PropertyChangeSupport(this);
    
        /*
         * Konstruktor. ruft den Konstruktor der Superklasse auf.
         */
    public AttributeTableModel(String[] columnNames)
    {
        this.columnNames = columnNames.length > 1 ? columnNames : new String[2];
        this.showInvisibleAttributes = false;
    }
    
        /*
         * Ueberschreibt boolen isCellEditable(),
         * damit die Zellen der Tabelle nict editiert werden koennen.
         */
    public boolean isCellEditable(int row, int column)
    {
//        if(this.editable && column == 1 && row < this.metaAttributes.size())
//        {
//            return true;
//        }
        
        return false;
    }
    
    /**
     * Loescht den Inhalt der Tabelle. Die aktuelle Anzahl der Zeilen
     * bleibt erhalten.
     */
    public void clear()
    {
        //if(logger.isDebugEnabled())logger.debug("clear attribute table model");
        
        this.metaAttributes.clear();
        this.fireTableDataChanged();
    }
    
    public Class getColumnClass(int column)
    {
        return java.lang.Object.class;
        
        /*if(column == 0)
        {
            return java.lang.String.class;
        }
        else
        {
            return java.lang.Object.class;
        }*/
    }
    
    public Class getClassAt(int row, int column)
    {
        if(row < this.metaAttributes.size())
        {
            if(column == 0)
            {
                return String.class;
            }
            else
            {
                /*Sirius.server.localserver.attribute.Attribute metaAttribute = (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
                Object value = metaAttribute.getValue();
                 
                if(value!= null)
                {
                    return value.getClass();
                }*/
                
                return Sirius.server.localserver.attribute.ObjectAttribute.class;
            }
        }
        
        return null;
    }
    
    public TableCellEditor getCellEditor(int row, int column)
    {
        return null;
    }
    
    public int getColumnCount()
    {
        return 2;
    }
    
    public String getColumnName(int column)
    {
        return this.columnNames[column];
    }
    
    /**
     * Liefert einen gefakten row c
     */
    public int getRowCount()
    {
        return this.metaAttributes.size() < MIN_ROWS ? MIN_ROWS : this.metaAttributes.size();
    }
    
    public Object getValueAt(int row, int column)
    {
        if(row < this.metaAttributes.size())
        {
            Sirius.server.localserver.attribute.Attribute metaAttribute = (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
            /*if(this.isShowInvisibleAttributes() || metaAttribute.isVisible())
            {
                if(column == 0)
                {
                    return metaAttribute.getName();
                }
                else
                {
                    return metaAttribute;
                }
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("ignoring invisible attribute '" + metaAttribute.getName() + "'");
            }*/
            
            if(column == 0)
            {
                return metaAttribute.getName();
            }
            else
            {
                return metaAttribute;
            }
        }

        return null;
    }
    
    public void setValueAt(Object aValue, int row, int column)
    {
        if(column == 1 && row < this.metaAttributes.size())
        {
            Sirius.server.localserver.attribute.Attribute metaAttribute = (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
            metaAttribute.setValue(aValue);
            
            //propertyChangeSupport.firePropertyChange("changed", new Boolean(false), new Boolean(true));
            this.fireTableCellUpdated(row, column);
        }
    }
    
    public void setAttributes(Collection metaAttributes)
    {
        this.metaAttributes.clear();
        
        if(this.showInvisibleAttributes)
        {
            logger.warn("showing invisible attributes");//NOI18N
            this.metaAttributes.addAll(metaAttributes);
        }
        else
        {
            logger.warn("ignoring invisible attributes");//NOI18N
            Iterator iterator = metaAttributes.iterator();
            while(iterator.hasNext())
            {
                Sirius.server.localserver.attribute.Attribute attribute = (Sirius.server.localserver.attribute.Attribute)iterator.next();
                if(attribute.isVisible())
                {
                    this.metaAttributes.add(attribute);
                }
            }
        }
        this.fireTableDataChanged();
    }
    
    
    /**
     * Liefert den Editor, der in MetaAttribute definiert ist.
     */
    public Class getCellRendererClass(int row, int column)
    {
        if(column == 1 && row < this.metaAttributes.size())
        {
            //return Sirius.server.localserver.attribute.ObjectAttribute.class;
            //Sirius.server.localserver.attribute.Attribute metaAttribute = (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
        }
        
        return null;
    }
    
    /**
     * Liefert den Renderer, der in MetaAttribute definiert ist.
     */
    /*public Class getCellEditorClass(int row, int column)
    {
        if(this.isMetaAttribute(row, column)
        {
     
        }
        else if(logger.isDebug)
     
     
        if(column == 1 && row < this.metaAttributes.size())
        {
            Sirius.server.localserver.attribute.Attribute metaAttribute = (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
     
        }
     
        return null;
    }*/
    
    /**
     * Returns true, if the Object at the selected position is Meta Attribute
     */
    protected boolean isMetaAttribute(int row, int column)
    {
        return column == 1 & row < this.metaAttributes.size();
    }
    
    
    /**
     * Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isEditable()
    {
        return this.editable;
    }
    
    /**
     * Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
        
        // switched to editing mode: reset changed to false
        //this.changed = this.editable ? false : this.changed;
    }
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    /**
     * Holds value of property showInvisibleAttributes.
     */
    private boolean showInvisibleAttributes;
    
    /**
     * Getter for property showInvisibleAttributes.
     * @return Value of property showInvisibleAttributes.
     */
    public boolean isShowInvisibleAttributes()
    {
        
        return this.showInvisibleAttributes;
    }
    
    /**
     * Setter for property showInvisibleAttributes.
     * @param showInvisibleAttributes New value of property showInvisibleAttributes.
     */
    public void setShowInvisibleAttributes(boolean showInvisibleAttributes)
    {
        
        this.showInvisibleAttributes = showInvisibleAttributes;
    }
}

