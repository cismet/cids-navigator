/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.server.middleware.types.AbstractAttributeRepresentationFormater;

import com.jgoodies.looks.plastic.PlasticComboBoxUI;

import javax.swing.plaf.ComboBoxUI;

import de.cismet.cidsx.server.search.builtin.legacy.LightweightMetaObjectsSearch;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FastBindableScrollableComboBox extends FastBindableReferenceCombo {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FastBindableScrollableComboBox object.
     */
    public FastBindableScrollableComboBox() {
        super();
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new FastBindableScrollableComboBox object.
     *
     * @param  representation        DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableScrollableComboBox(final String representation, final String[] representationFields) {
        super(representation, representationFields);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new FastBindableScrollableComboBox object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableScrollableComboBox(final LightweightMetaObjectsSearch lwmoSearch,
            final String representation,
            final String[] representationFields) {
        super(lwmoSearch, representation, representationFields);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new FastBindableScrollableComboBox object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableScrollableComboBox(final LightweightMetaObjectsSearch lwmoSearch,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        super(lwmoSearch, formater, representationFields);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setUI(final ComboBoxUI ui) {
        super.setUI(ui);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }
}
