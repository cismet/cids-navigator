/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ScrollablePanel extends JPanel implements Scrollable, SwingConstants {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ScrollableSizeHint {

        //~ Enum constants -----------------------------------------------------

        NONE, FIT, STRETCH;
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum IncrementType {

        //~ Enum constants -----------------------------------------------------

        PERCENT, PIXELS;
    }

    //~ Instance fields --------------------------------------------------------

    private ScrollableSizeHint scrollableHeight = ScrollableSizeHint.NONE;
    private ScrollableSizeHint scrollableWidth = ScrollableSizeHint.NONE;

    private IncrementInfo horizontalBlock;
    private IncrementInfo horizontalUnit;
    private IncrementInfo verticalBlock;
    private IncrementInfo verticalUnit;

    //~ Constructors -----------------------------------------------------------

    /**
     * Default constructor that uses a FlowLayout.
     */
    public ScrollablePanel() {
        this(new FlowLayout());
    }

    /**
     * Constuctor for specifying the LayoutManager of the panel.
     *
     * @param  layout  the LayountManger for the panel
     */
    public ScrollablePanel(final LayoutManager layout) {
        super(layout);

        final IncrementInfo block = new IncrementInfo(IncrementType.PERCENT, 100);
        final IncrementInfo unit = new IncrementInfo(IncrementType.PERCENT, 10);

        setScrollableBlockIncrement(HORIZONTAL, block);
        setScrollableBlockIncrement(VERTICAL, block);
        setScrollableUnitIncrement(HORIZONTAL, unit);
        setScrollableUnitIncrement(VERTICAL, unit);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Get the height ScrollableSizeHint enum.
     *
     * @return  the ScrollableSizeHint enum for the height
     */
    public ScrollableSizeHint getScrollableHeight() {
        return scrollableHeight;
    }

    /**
     * Set the ScrollableSizeHint enum for the height. The enum is used to determine the boolean value that is returned
     * by the getScrollableTracksViewportHeight() method. The valid values are:
     *
     * <p>ScrollableSizeHint.NONE - return "false", which causes the height of the panel to be used when laying out the
     * children ScrollableSizeHint.FIT - return "true", which causes the height of the viewport to be used when laying
     * out the children ScrollableSizeHint.STRETCH - return "true" when the viewport height is greater than the height
     * of the panel, "false" otherwise.</p>
     *
     * @param  scrollableHeight  as represented by the ScrollableSizeHint enum.
     */
    public void setScrollableHeight(final ScrollableSizeHint scrollableHeight) {
        this.scrollableHeight = scrollableHeight;
        revalidate();
    }

    /**
     * Get the width ScrollableSizeHint enum.
     *
     * @return  the ScrollableSizeHint enum for the width
     */
    public ScrollableSizeHint getScrollableWidth() {
        return scrollableWidth;
    }

    /**
     * Set the ScrollableSizeHint enum for the width. The enum is used to determine the boolean value that is returned
     * by the getScrollableTracksViewportWidth() method. The valid values are:
     *
     * <p>ScrollableSizeHint.NONE - return "false", which causes the width of the panel to be used when laying out the
     * children ScrollableSizeHint.FIT - return "true", which causes the width of the viewport to be used when laying
     * out the children ScrollableSizeHint.STRETCH - return "true" when the viewport width is greater than the width of
     * the panel, "false" otherwise.</p>
     *
     * @param  scrollableWidth  as represented by the ScrollableSizeHint enum.
     */
    public void setScrollableWidth(final ScrollableSizeHint scrollableWidth) {
        this.scrollableWidth = scrollableWidth;
        revalidate();
    }

    /**
     * Get the block IncrementInfo for the specified orientation.
     *
     * @param   orientation  DOCUMENT ME!
     *
     * @return  the block IncrementInfo for the specified orientation
     */
    public IncrementInfo getScrollableBlockIncrement(final int orientation) {
        return (orientation == SwingConstants.HORIZONTAL) ? horizontalBlock : verticalBlock;
    }

    /**
     * Specify the information needed to do block scrolling.
     *
     * @param  orientation  specify the scrolling orientation. Must be either: SwingContants.HORIZONTAL or
     *                      SwingContants.VERTICAL.
     * @param  type         DOCUMENT ME!
     * @param  amount       a value used with the IncrementType to determine the scrollable amount
     *
     * @paran  type specify how the amount parameter in the calculation of the scrollable amount. Valid values are:
     *         IncrementType.PERCENT - treat the amount as a % of the viewport size IncrementType.PIXEL - treat the
     *         amount as the scrollable amount
     */
    public void setScrollableBlockIncrement(final int orientation, final IncrementType type, final int amount) {
        final IncrementInfo info = new IncrementInfo(type, amount);
        setScrollableBlockIncrement(orientation, info);
    }

    /**
     * Specify the information needed to do block scrolling.
     *
     * @param   orientation  specify the scrolling orientation. Must be either: SwingContants.HORIZONTAL or
     *                       SwingContants.VERTICAL.
     * @param   info         An IncrementInfo object containing information of how to calculate the scrollable amount.
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void setScrollableBlockIncrement(final int orientation, final IncrementInfo info) {
        switch (orientation) {
            case SwingConstants.HORIZONTAL: {
                horizontalBlock = info;
                break;
            }
            case SwingConstants.VERTICAL: {
                verticalBlock = info;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
            }
        }
    }

    /**
     * Get the unit IncrementInfo for the specified orientation.
     *
     * @param   orientation  DOCUMENT ME!
     *
     * @return  the unit IncrementInfo for the specified orientation
     */
    public IncrementInfo getScrollableUnitIncrement(final int orientation) {
        return (orientation == SwingConstants.HORIZONTAL) ? horizontalUnit : verticalUnit;
    }

    /**
     * Specify the information needed to do unit scrolling.
     *
     * @param  orientation  specify the scrolling orientation. Must be either: SwingContants.HORIZONTAL or
     *                      SwingContants.VERTICAL.
     * @param  type         DOCUMENT ME!
     * @param  amount       a value used with the IncrementType to determine the scrollable amount
     *
     * @paran  type specify how the amount parameter in the calculation of the scrollable amount. Valid values are:
     *         IncrementType.PERCENT - treat the amount as a % of the viewport size IncrementType.PIXEL - treat the
     *         amount as the scrollable amount
     */
    public void setScrollableUnitIncrement(final int orientation, final IncrementType type, final int amount) {
        final IncrementInfo info = new IncrementInfo(type, amount);
        setScrollableUnitIncrement(orientation, info);
    }

    /**
     * Specify the information needed to do unit scrolling.
     *
     * @param   orientation  specify the scrolling orientation. Must be either: SwingContants.HORIZONTAL or
     *                       SwingContants.VERTICAL.
     * @param   info         An IncrementInfo object containing information of how to calculate the scrollable amount.
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void setScrollableUnitIncrement(final int orientation, final IncrementInfo info) {
        switch (orientation) {
            case SwingConstants.HORIZONTAL: {
                horizontalUnit = info;
                break;
            }
            case SwingConstants.VERTICAL: {
                verticalUnit = info;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
            }
        }
    }

//  Implement Scrollable interface

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(final Rectangle visible, final int orientation, final int direction) {
        switch (orientation) {
            case SwingConstants.HORIZONTAL: {
                return getScrollableIncrement(horizontalUnit, visible.width);
            }
            case SwingConstants.VERTICAL: {
                return getScrollableIncrement(verticalUnit, visible.height);
            }
            default: {
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
            }
        }
    }

    @Override
    public int getScrollableBlockIncrement(final Rectangle visible, final int orientation, final int direction) {
        switch (orientation) {
            case SwingConstants.HORIZONTAL: {
                return getScrollableIncrement(horizontalBlock, visible.width);
            }
            case SwingConstants.VERTICAL: {
                return getScrollableIncrement(verticalBlock, visible.height);
            }
            default: {
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   info      DOCUMENT ME!
     * @param   distance  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected int getScrollableIncrement(final IncrementInfo info, final int distance) {
        if (info.getIncrement() == IncrementType.PIXELS) {
            return info.getAmount();
        } else {
            return distance * info.getAmount() / 100;
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (scrollableWidth == ScrollableSizeHint.NONE) { // scrollableWidth = ScrollableSizeHint.STRETCH
            return false;
        }

        if (scrollableWidth == ScrollableSizeHint.FIT) {
            return true;
        }

        // STRETCH sizing, use the greater of the panel or viewport width

        if (getParent() instanceof JViewport) {
            return (((JViewport)getParent()).getWidth() > getPreferredSize().width);
        }

        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        if (scrollableHeight == ScrollableSizeHint.NONE) {
            return false;
        }

        if (scrollableHeight == ScrollableSizeHint.FIT) {
            return true;
        }

        // STRETCH sizing, use the greater of the panel or viewport height

        if (getParent() instanceof JViewport) {
            return (((JViewport)getParent()).getHeight() > getPreferredSize().height);
        }

        return false;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Helper class to hold the information required to calculate the scroll amount.
     *
     * @version  $Revision$, $Date$
     */
    static class IncrementInfo {

        //~ Instance fields ----------------------------------------------------

        private IncrementType type;
        private int amount;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new IncrementInfo object.
         *
         * @param  type    DOCUMENT ME!
         * @param  amount  DOCUMENT ME!
         */
        public IncrementInfo(final IncrementType type, final int amount) {
            this.type = type;
            this.amount = amount;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public IncrementType getIncrement() {
            return type;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "ScrollablePanel["
                        + type + ", "
                        + amount + "]";
        }
    }
}
