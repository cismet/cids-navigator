/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import com.jgoodies.looks.Options;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.metal.MetalScrollBarUI;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class PlasticScrollableComboPopup extends BasicComboPopup {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PlasticScrollableComboPopup object.
     *
     * @param  combo  DOCUMENT ME!
     */
    public PlasticScrollableComboPopup(final JComboBox combo) {
        super(combo);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void configureList() {
        super.configureList();
        list.setForeground(UIManager.getColor("MenuItem.foreground"));
        list.setBackground(UIManager.getColor("MenuItem.background"));
    }

    @Override
    protected JScrollPane createScroller() {
        final JScrollPane sp = new JScrollPane(
                list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return sp;
    }

    /**
     * Configures the JScrollPane created by #createScroller().
     */
    @Override
    protected void configureScroller() {
        super.configureScroller();
        scroller.getVerticalScrollBar().putClientProperty(
            MetalScrollBarUI.FREE_STANDING_PROP,
            Boolean.FALSE);
        scroller.getHorizontalScrollBar().putClientProperty(
            MetalScrollBarUI.FREE_STANDING_PROP,
            Boolean.FALSE);
    }

    /**
     * Calculates the placement and size of the popup portion of the combo box based on the combo box location and the
     * enclosing screen bounds. If no transformations are required, then the returned rectangle will have the same
     * values as the parameters.
     *
     * <p>In addition to the superclass behavior, this class offers to use the combo's popup prototype display value to
     * compute the popup menu width. This is an optional feature of the JGoodies Plastic L&amp;fs implemented via a
     * client property key.</p>
     *
     * <p>If a prototype is set, the popup width is the maximum of the combobox width and the prototype based popup
     * width. For the latter the renderer is used to render the prototype. The prototype based popup width is the
     * prototype's width plus the scrollbar width - if any. The scrollbar test checks if there are more items than the
     * combo's maximum row count.</p>
     *
     * @param   px  starting x location
     * @param   py  starting y location
     * @param   pw  starting width
     * @param   ph  starting height
     *
     * @return  a rectangle which represents the placement and size of the popup
     *
     * @see     Options#COMBO_POPUP_PROTOTYPE_DISPLAY_VALUE_KEY
     * @see     JComboBox#getMaximumRowCount()
     */
    @Override
    protected Rectangle computePopupBounds(final int px, final int py, int pw, final int ph) {
        final Rectangle defaultBounds = super.computePopupBounds(px, py, pw, ph);
        final Object popupPrototypeDisplayValue = comboBox.getClientProperty(
                Options.COMBO_POPUP_PROTOTYPE_DISPLAY_VALUE_KEY);
        if (popupPrototypeDisplayValue == null) {
            return defaultBounds;
        }

        final ListCellRenderer renderer = list.getCellRenderer();
        final Component c = renderer.getListCellRendererComponent(
                list,
                popupPrototypeDisplayValue,
                -1,
                true,
                true);
        pw = c.getPreferredSize().width;
        final boolean hasVerticalScrollBar = comboBox.getItemCount() > comboBox.getMaximumRowCount();
        if (hasVerticalScrollBar) {
            // Add the scrollbar width.
            final JScrollBar verticalBar = scroller.getVerticalScrollBar();
            pw += verticalBar.getPreferredSize().width;
        }
        final Rectangle prototypeBasedBounds = super.computePopupBounds(px, py, pw, ph);
        return (prototypeBasedBounds.width > defaultBounds.width) ? prototypeBasedBounds : defaultBounds;
    }
}
