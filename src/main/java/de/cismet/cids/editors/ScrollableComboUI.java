/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import com.jgoodies.looks.plastic.PlasticComboBoxUI;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.ComboPopup;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ScrollableComboUI extends PlasticComboBoxUI {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScrollableComboUI object.
     */
    public ScrollableComboUI() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ComponentUI createUI(final JComponent b) {
        PlasticComboBoxUI.createUI(b);
        return new ScrollableComboUI();
    }

    @Override
    protected ComboPopup createPopup() {
        return new PlasticScrollableComboPopup(comboBox);
    }
}
