/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AttributeTableModel.java
 *
 * Created on 24. Juni 2004, 16:53
 */
package Sirius.navigator.ui.attributes;

import org.apache.log4j.Logger;

import java.util.*;

import javax.swing.table.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
class AttributeTableModel extends AbstractTableModel {

    //~ Static fields/initializers ---------------------------------------------

    private static final int MIN_ROWS = 0;

    //~ Instance fields --------------------------------------------------------

    private final Logger logger = Logger.getLogger(this.getClass());
    private final LinkedList metaAttributes = new LinkedList();

    private final String[] columnNames;

    /** Holds value of property editable. */
    private boolean editable;

    /** Utility field used by bound properties. */
    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /** Holds value of property showInvisibleAttributes. */
    private boolean showInvisibleAttributes;

    //~ Constructors -----------------------------------------------------------

    /**
     * Konstruktor. ruft den Konstruktor der Superklasse auf.
     *
     * @param  columnNames  DOCUMENT ME!
     */
    public AttributeTableModel(final String[] columnNames) {
        this.columnNames = (columnNames.length > 1) ? columnNames : new String[2];
        this.showInvisibleAttributes = false;
    }

    //~ Methods ----------------------------------------------------------------

    /*
     * Ueberschreibt boolen isCellEditable(), damit die Zellen der Tabelle nict editiert werden koennen.
     */
    @Override
    public boolean isCellEditable(final int row, final int column) {
//        if(this.editable && column == 1 && row < this.metaAttributes.size())
//        {
//            return true;
//        }

        return false;
    }

    /**
     * Loescht den Inhalt der Tabelle. Die aktuelle Anzahl der Zeilen bleibt erhalten.
     */
    public void clear() {
        // if(logger.isDebugEnabled())logger.debug("clear attribute table model");

        this.metaAttributes.clear();
        this.fireTableDataChanged();
    }

    @Override
    public Class getColumnClass(final int column) {
        return java.lang.Object.class;

        /*if(column == 0)
         * { return java.lang.String.class; } else { return java.lang.Object.class;}*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param   row     DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Class getClassAt(final int row, final int column) {
        if (row < this.metaAttributes.size()) {
            if (column == 0) {
                return String.class;
            } else {
                /*Sirius.server.localserver.attribute.Attribute metaAttribute =
                 * (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
                 * Object value = metaAttribute.getValue(); if(value!= null) { return value.getClass();}*/

                return Sirius.server.localserver.attribute.ObjectAttribute.class;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   row     DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TableCellEditor getCellEditor(final int row, final int column) {
        return null;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(final int column) {
        return this.columnNames[column];
    }

    /**
     * Liefert einen gefakten row c.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getRowCount() {
        return (this.metaAttributes.size() < MIN_ROWS) ? MIN_ROWS : this.metaAttributes.size();
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        if ((row >= 0) && (row < this.metaAttributes.size())) {
            final Sirius.server.localserver.attribute.Attribute metaAttribute =
                (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
            /*if(this.isShowInvisibleAttributes() || metaAttribute.isVisible())
             * { if(column == 0) {     return metaAttribute.getName(); } else {     return metaAttribute; } } else
             * if(logger.isDebugEnabled()) { logger.warn("ignoring invisible attribute '" + metaAttribute.getName() +
             * "'");}*/

            if (column == 0) {
                return metaAttribute.getName();
            } else {
                return metaAttribute;
            }
        }

        return null;
    }

    @Override
    public void setValueAt(final Object aValue, final int row, final int column) {
        if ((column == 1) && (row < this.metaAttributes.size())) {
            final Sirius.server.localserver.attribute.Attribute metaAttribute =
                (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
            metaAttribute.setValue(aValue);

            // propertyChangeSupport.firePropertyChange("changed", new Boolean(false), new Boolean(true));
            this.fireTableCellUpdated(row, column);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  metaAttributes  DOCUMENT ME!
     */
    public void setAttributes(final Collection metaAttributes) {
        this.metaAttributes.clear();

        if (this.showInvisibleAttributes) {
            logger.warn("showing invisible attributes");  // NOI18N
            this.metaAttributes.addAll(metaAttributes);
        } else {
            logger.warn("ignoring invisible attributes"); // NOI18N
            final Iterator iterator = metaAttributes.iterator();
            while (iterator.hasNext()) {
                final Sirius.server.localserver.attribute.Attribute attribute =
                    (Sirius.server.localserver.attribute.Attribute)iterator.next();
                if (attribute.isVisible()) {
                    this.metaAttributes.add(attribute);
                }
            }
        }
        this.fireTableDataChanged();
    }

    /**
     * Liefert den Editor, der in MetaAttribute definiert ist.
     *
     * @param   row     DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Class getCellRendererClass(final int row, final int column) {
        if ((column == 1) && (row < this.metaAttributes.size())) {
            // return Sirius.server.localserver.attribute.ObjectAttribute.class;
            // Sirius.server.localserver.attribute.Attribute metaAttribute =
            // (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);
        }

        return null;
    }

    /**
     * Liefert den Renderer, der in MetaAttribute definiert ist.
     *
     * @param   row     DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    /*public Class getCellEditorClass(int row, int column)
     * { if(this.isMetaAttribute(row, column) {  } else if(logger.isDebug)   if(column == 1 && row <
     * this.metaAttributes.size()) {     Sirius.server.localserver.attribute.Attribute metaAttribute =
     * (Sirius.server.localserver.attribute.Attribute)this.metaAttributes.get(row);  }  return null;}*/

    /**
     * Returns true, if the Object at the selected position is Meta Attribute.
     *
     * @param   row     DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isMetaAttribute(final int row, final int column) {
        return (column == 1) & (row < this.metaAttributes.size());
    }

    /**
     * Getter for property editable.
     *
     * @return  Value of property editable.
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Setter for property editable.
     *
     * @param  editable  New value of property editable.
     */
    public void setEditable(final boolean editable) {
        this.editable = editable;

        // switched to editing mode: reset changed to false
        // this.changed = this.editable ? false : this.changed;
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     *
     * @param  l  The listener to add.
     */
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     *
     * @param  l  The listener to remove.
     */
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Getter for property showInvisibleAttributes.
     *
     * @return  Value of property showInvisibleAttributes.
     */
    public boolean isShowInvisibleAttributes() {
        return this.showInvisibleAttributes;
    }

    /**
     * Setter for property showInvisibleAttributes.
     *
     * @param  showInvisibleAttributes  New value of property showInvisibleAttributes.
     */
    public void setShowInvisibleAttributes(final boolean showInvisibleAttributes) {
        this.showInvisibleAttributes = showInvisibleAttributes;
    }
}
