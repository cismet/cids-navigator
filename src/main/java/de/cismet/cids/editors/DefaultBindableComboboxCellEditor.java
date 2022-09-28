/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;

import java.awt.Component;
import java.awt.event.MouseEvent;

import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * A table cell editor that shows a bindable combobox.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultBindableComboboxCellEditor extends AbstractCellEditor implements TableCellEditor {

    //~ Instance fields --------------------------------------------------------

    private final DefaultBindableReferenceCombo comboBox;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableComboboxCellEditor object.
     *
     * @param  metaClass  DOCUMENT ME!
     */
    public DefaultBindableComboboxCellEditor(final MetaClass metaClass) {
        comboBox = new DefaultBindableScrollableComboBox(metaClass);
    }

    /**
     * Creates a new DefaultBindableComboboxCellEditor object.
     *
     * @param  metaClass  DOCUMENT ME!
     * @param  nullable   DOCUMENT ME!
     * @param  onlyUsed   DOCUMENT ME!
     */
    public DefaultBindableComboboxCellEditor(final MetaClass metaClass,
            final boolean nullable,
            final boolean onlyUsed) {
        comboBox = new DefaultBindableScrollableComboBox(metaClass, nullable, onlyUsed);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isCellEditable(final EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= 2;
        }
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(final JTable table,
            final Object value,
            final boolean isSelected,
            final int row,
            final int column) {
        comboBox.setSelectedItem(value);

        return comboBox;
    }
}
