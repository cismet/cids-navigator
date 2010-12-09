/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CheckBoxModel extends DefaultComboBoxModel {

    //~ Instance fields --------------------------------------------------------

    protected boolean allSelected = false;
    protected int firstSelectedIndex = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CheckBoxModel object.
     */
    public CheckBoxModel() {
        super();
    }

    /**
     * Creates a new CheckBoxModel object.
     *
     * @param  names      DOCUMENT ME!
     * @param  selectAll  DOCUMENT ME!
     */
    public CheckBoxModel(final String[] names, final boolean selectAll) {
        final JCheckBox checkBox = new JCheckBox("all", selectAll);
        this.addElement(checkBox);
        for (int i = 0; i < names.length; i++) {
            this.addElement(new JCheckBox(names[i], selectAll));
        }
    }
}
