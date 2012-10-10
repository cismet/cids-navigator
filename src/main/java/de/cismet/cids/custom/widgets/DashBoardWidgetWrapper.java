/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.widgets;

import Sirius.navigator.ui.DashBoardWidget;
import Sirius.navigator.ui.WrappedDashBoardWidget;

import org.apache.log4j.Logger;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import de.cismet.tools.gui.ComponentWrapper;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DashBoardWidgetWrapper implements ComponentWrapper {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DashBoardWidgetWrapper.class);
    private static DashBoardWidgetWrapper INSTANCE = new DashBoardWidgetWrapper();

    //~ Methods ----------------------------------------------------------------

    @Override
    public WrappedDashBoardWidget wrapComponent(final JComponent component) {
        component.setBorder(new EmptyBorder(10, 10, 10, 10));
        if (component instanceof DashBoardWidget) {
            final AbstractDashBoardWidget dashBoardWidget = (AbstractDashBoardWidget)component;
            final WrappedDashBoardWidget wdbw = new WrappedDashBoardWidget(dashBoardWidget);
            return wdbw;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DashBoardWidgetWrapper getInstance() {
        return INSTANCE;
    }
}
