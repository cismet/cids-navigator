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
package de.cismet.cids.custom.widgets.sirius;

import Sirius.navigator.ui.DashBoardWidget;

import org.openide.util.lookup.ServiceProvider;

import java.awt.Component;

import java.io.File;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * DOCUMENT ME!
 *
 * @author   bfriedrich
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = DashBoardWidget.class)
public class FakeClock implements DashBoardWidget {

    //~ Instance fields --------------------------------------------------------

    private final JLabel lblClock;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FakeClock object.
     */
    public FakeClock() {
        System.err.println("CONSTRUCTOR");
        this.lblClock = new JLabel(new ImageIcon(
                    "/home/bfriedrich/git-netbeans-project/cids-navigator/src/main/java/de/cismet/cids/custom/objectrenderer/sirius/widgets/fake_clock.png"));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void init() {
        System.err.println("INIT");
    }

    @Override
    public void refresh() {
        System.err.println("REFRESH");
    }

    @Override
    public Component getWidget() {
        return this.lblClock;
    }

    @Override
    public boolean isHeaderWidget() {
        return false;
    }

    @Override
    public int getX() {
        return 1;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void dispose() {
    }
}
