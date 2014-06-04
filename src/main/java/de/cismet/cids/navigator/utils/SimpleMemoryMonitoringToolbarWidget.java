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

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class SimpleMemoryMonitoringToolbarWidget extends AbstractAction implements CidsClientToolbarItem {

    //~ Static fields/initializers ---------------------------------------------

    static final boolean visible = StaticDebuggingTools.checkHomeForFile("cismetMemoryMonitoring");

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MemoryToolbar object.
     */
    public SimpleMemoryMonitoringToolbarWidget() {
        this.putValue(Action.NAME, "Memory");
        if (isVisible()) {
            new Timer(300, new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (isVisible()) {
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
                    }
                }).start();
        }
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

    @Override
    public boolean isVisible() {
        return visible;
    }
}
