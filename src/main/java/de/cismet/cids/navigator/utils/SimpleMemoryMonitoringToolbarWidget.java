/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.navigator.utils;

import org.apache.commons.lang.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

import de.cismet.tools.StaticDebuggingTools;

import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class SimpleMemoryMonitoringToolbarWidget extends AbstractAction implements CidsClientToolbarItem, CidsUiAction {

    //~ Static fields/initializers ---------------------------------------------

    static boolean visible = StaticDebuggingTools.checkHomeForFile("cismetMemoryMonitoring");

    //~ Instance fields --------------------------------------------------------

    private final Timer timer = new Timer(300, new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final Runtime runtime = Runtime.getRuntime();

                    final long memory = (long)(runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                    final long totmem = (long)runtime.totalMemory() / 1024 / 1024;
                    SimpleMemoryMonitoringToolbarWidget.this.putValue(
                        Action.NAME,
                        StringUtils.leftPad(Long.toString(memory), 4, '0')
                                + "MB/"
                                + StringUtils.leftPad(Long.toString(totmem), 4, '0')
                                + "MB");
                }
            });

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MemoryToolbar object.
     */
    public SimpleMemoryMonitoringToolbarWidget() {
        this.putValue(Action.NAME, "Memory");

        if (isVisible()) {
            timer.start();
        }
        putValue(CidsUiAction.CIDS_ACTION_KEY, "SimpleMemoryMonitoringToolbarWidget");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        new Thread() {

                @Override
                public void run() {
                    System.gc();
                }
            }.start();
    }

    @Override
    public String getSorterString() {
        return "ZZZ";
    }

    /**
     * DOCUMENT ME!
     */
    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    @Override
    public final boolean isVisible() {
        return visible;
    }
}
