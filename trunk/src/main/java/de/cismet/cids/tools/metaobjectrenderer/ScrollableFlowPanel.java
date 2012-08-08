/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ScrollableFlowPanel.java
 *
 * Created on 23. Mai 2007, 16:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ScrollableFlowPanel extends JPanel implements Scrollable {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, getParent().getWidth(), height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getPreferredHeight());
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        final int hundredth =
            ((orientation == SwingConstants.VERTICAL) ? getParent().getHeight() : getParent().getWidth())
                    / 100;
        return ((hundredth == 0) ? 1 : hundredth);
    }

    @Override
    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return (orientation == SwingConstants.VERTICAL) ? getParent().getHeight() : getParent().getWidth();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getPreferredHeight() {
        int rv = 0;
        for (int k = 0, count = getComponentCount(); k < count; k++) {
            final Component comp = getComponent(k);
            final Rectangle r = comp.getBounds();
            final int height = r.y
                        + r.height;
            if (height > rv) {
                rv = height;
            }
        }
        rv += ((FlowLayout)getLayout()).getVgap();
        return rv;
    }
}
