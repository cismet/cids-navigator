/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                                http://www.htw-saarland.de/eig
                                                Prof. Dr. Reiner Guettler
                                                Prof. Dr. Ralf Denzer

                                                HTWdS
                                                Hochschule fuer Technik und Wirtschaft des Saarlandes
                                                Goebenstr. 40
                                                66117 Saarbruecken
                                                Germany

        Programmers             :       Pascal

        Project                 :       WuNDA 2
        Version                 :       1.0
        Purpose                 :
        Created                 :       01.11.1999
        History                 :

*******************************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
/* IMPORT STRINGLOADER */

/**
 * Eine JComboBox mit JCheckBoxes als Listeneintr\u00E4ge. Ueber die CheckBox "Alle" koennen alle Eintraege an- bzw.
 * abgewaehlt werden.
 *
 * @author   Pascal Dih&eacute;
 * @version  1.0
 * @see      javax.swing.JCheckBox;
 * @see      javax.swing.JComboBox;
 */
public class CheckBoxComboBox extends JComboBox implements ActionListener {

    //~ Instance fields --------------------------------------------------------

    // protected boolean allSelected = false;
    protected boolean modelChanged = false;
    protected CheckBoxModel model;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CheckBoxComboBox object.
     */
    public CheckBoxComboBox() {
        super();
        model = new CheckBoxModel();
        this.setModel(model);
        this.initCheckBoxComboBox();
    }

    /**
     * Konstruiert eine neue CheckBoxComboBox "Alle" koennen alle Eintraege an- bzw. abgewaehlt werden.
     *
     * @param  themes     Ein String mit den Namen fuer die Checkboxes.
     * @param  selectAll  Wenn true werden alle Eintrage zu Beginn selektiert.
     */
    public CheckBoxComboBox(final String[] themes, final boolean selectAll) {
        super();
        model = new CheckBoxModel(themes, selectAll);
        model.allSelected = selectAll;
        this.setModel(model);
        this.initCheckBoxComboBox();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initCheckBoxComboBox() {
        modelChanged = false;
        this.setRenderer(new CheckBoxListCellRenderer());
        this.addActionListener(this);
        // Damit das PopupWindow richtig angezeigt wird, wenn die
        // CheckBoxComboBox direkt zu einer HeavyWight Komponente, einen
        // Swing Toplevel Komponente oder einem LayPanel hinzugefuegt wird.
        this.setLightWeightPopupEnabled(false);
    }

    /**
     * Liefert ein String-Array mit den Namen aller ausgewaehlten CheckBoxes.
     *
     * @return  DOCUMENT ME!
     */
    public synchronized String[] getSelectedItems() {
        int selected = 0;
        final String[] tmp = new String[(this.getItemCount() - 1)];
        // Die Ausgewaehlten Elemente (ausser "Alle") in ein
        // String Array kopieren
        for (int i = 1; i < this.getItemCount(); i++) {
            final JCheckBox checkBox = (JCheckBox)getItemAt(i);
            if (checkBox.isSelected()) {
                tmp[selected] = checkBox.getText();
                selected++;
            }
        }

        if (selected == 0) {
            return new String[0];
        }

        final String[] result = new String[selected];
        System.arraycopy(tmp, 0, result, 0, selected);
        return result;
    }

    /**
     * Liefert ein int-Array mit den positionen aller ausgewaehlten CheckBoxes.
     *
     * @return  DOCUMENT ME!
     */
    public synchronized int[] getSelectedIndices() {
        int selected = 0;
        final int[] tmp = new int[(this.getItemCount() - 1)];
        // Die Ausgewaehlten Elemente (ausser "Alle") in ein
        // int Array kopieren
        for (int i = 1; i < this.getItemCount(); i++) {
            final JCheckBox checkBox = (JCheckBox)getItemAt(i);
            if (checkBox.isSelected()) {
                tmp[selected] = i - 1;
                selected++;
                // NavigatorLogger.printMessage(tmp[selected]);
            }
        }

        if (selected == 0) {
            return new int[0];
        }

        final int[] result = new int[selected];
        System.arraycopy(tmp, 0, result, 0, selected);
        return result;
    }

    /**
     * Musste uerbschrieben werden, da sonst bei jeder Aenderung des Models auch ein ActionEvent ausgeloest wird.
     *
     * @param  model  DOCUMENT ME!
     */
    public void setModel(final CheckBoxModel model) {
        this.model = model;
        modelChanged = true;

        super.setModel(model);
        this.validate();
    }

    /**
     * Das erste selektierte Objekt (CheckBox) wird angezeigt.
     */
    public void showSelectedIndex() {
        final JCheckBox checkBox = (JCheckBox)model.getElementAt(model.firstSelectedIndex);

        if ((checkBox != null) && checkBox.isSelected()) {
            setSelectedIndex(model.firstSelectedIndex);
            setSelectedIndex(model.firstSelectedIndex);
        }
    }

    // ACTION LISTENER =========================================================
    @Override
    public void actionPerformed(final ActionEvent e) {
        JCheckBox selectedCheckBox = (JCheckBox)getSelectedItem();
        JCheckBox checkBox;

        if (selectedCheckBox == null) {
            return;
        }

        if (modelChanged == true) {
            modelChanged = false;
            return;
        }

        // Wenn CheckBox "Alle" markiert wird, alle anderen Eintraege
        // anwaehlen, bzw. abwaehlen
        if (selectedCheckBox.getText().equals("Alle")) {
            for (int i = 0; i < getItemCount(); i++) {
                checkBox = (JCheckBox)getItemAt(i);
                checkBox.setSelected(!model.allSelected);
            }
            model.allSelected = !model.allSelected;
        } else {
            selectedCheckBox.doClick();

            int j = 1;
            for (int i = 1; i < this.getItemCount(); i++) {
                checkBox = (JCheckBox)getItemAt(i);
                if (checkBox.isSelected()) {
                    j++;
                }
            }

            // NavigatorLogger.printMessage(j);
            // NavigatorLogger.printMessage("getItemCount()" + this.getItemCount());
            if (j == this.getItemCount()) {
                checkBox = (JCheckBox)getItemAt(0);
                checkBox.setSelected(true);
                model.allSelected = true;
            } else {
                // Die CheckBox "Alle" zuruecksetzen
                selectedCheckBox = (JCheckBox)getItemAt(0);
                selectedCheckBox.setSelected(false);
                model.allSelected = false;
            }
        }

        // String[] st = getSelectedItems();
        // for(int i = 0; i < st.length; i++)
        // NavigatorLogger.printMessage(st[i]);

        // L\u00F6st einen neuen ItemEvent aus
        fireItemStateChanged(new ItemEvent(this, 0, selectedCheckBox, ItemEvent.SELECTED));
    }
}

/**
 * Ein neuer ListCellRenderer und die JCheckBoxes in der JList anzuzeigen.<br>
 * Wird benoetigt, da standardmaessig nur eine String-Representation des Objekts in der JList angezeigt wird.
 *
 * @version  $Revision$, $Date$
 */
class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CheckBoxListCellRenderer object.
     */
    public CheckBoxListCellRenderer() {
        super();
        this.setOpaque(true);
    }

    //~ Methods ----------------------------------------------------------------

    /*
     * getListCellRendererComponent(...) gibt nun eine Referenz auf die JCheckBox in der JList zurueck.
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        if ((value == null) || !(value instanceof JCheckBox)) {
            // _TA_return new JLabel("nicht verfuegbar");
            // return new JLabel(StringLoader.getString("STL@notAvailable"));
            return new JLabel();
        }
        final JCheckBox checkBox = (JCheckBox)value;
        list.setToolTipText(checkBox.getToolTipText());
        // NavigatorLogger.printMessage("CheckBoxToolTip: " + checkBox.getToolTipText());

        if (isSelected) {
            checkBox.setBackground(list.getSelectionBackground());
            checkBox.setForeground(list.getSelectionForeground());
        } else {
            checkBox.setBackground(list.getBackground());
            checkBox.setForeground(list.getForeground());
        }

        return checkBox;
    }
}
