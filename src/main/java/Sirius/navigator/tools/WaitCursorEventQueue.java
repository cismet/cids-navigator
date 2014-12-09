/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       01.10.1999
 * History                      :
 *
 *******************************************************************************/

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import javax.swing.SwingUtilities;

import de.cismet.tools.CismetThreadPool;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class WaitCursorEventQueue extends EventQueue {

    //~ Instance fields --------------------------------------------------------

    // private final static Logger logger = Logger.getLogger(WaitCursorEventQueue.class);

    private final int delay;
    private final WaitCursorTimer waitTimer;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WaitCursorEventQueue object.
     *
     * @param  delay  DOCUMENT ME!
     */
    public WaitCursorEventQueue(final int delay) {
        this.delay = delay;
        waitTimer = new WaitCursorTimer();
        waitTimer.setDaemon(true);

        CismetThreadPool.execute(waitTimer);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void dispatchEvent(final AWTEvent event) {
        try {
            waitTimer.startTimer(event.getSource());
            super.dispatchEvent(event);
        } catch (Exception exp) {
            exp.printStackTrace();
            // logger.fatal("exception during event dispatching", exp);
        } finally {
            waitTimer.stopTimer();
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class WaitCursorTimer extends Thread {

        //~ Instance fields ----------------------------------------------------

        private Object source;
        private Component parent;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new WaitCursorTimer object.
         */
        public WaitCursorTimer() {
            super("WaitCursorTimer");
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  source  DOCUMENT ME!
         */
        synchronized void startTimer(final Object source) {
            this.source = source;
            notify();
        }

        /**
         * DOCUMENT ME!
         */
        synchronized void stopTimer() {
            if (parent == null) {
                interrupt();
            } else if (parent.getCursor().getType() == Cursor.WAIT_CURSOR) {
                // if(logger.isDebugEnabled())logger.debug("hiding wait cursor");
                parent.setCursor(null);
                parent = null;
            }
        }

        @Override
        public synchronized void run() {
            while (true) {
                try {
                    // wait for notification from startTimer()
                    wait();

                    // wait for event processing to reach the threshold, or
                    // interruption from stopTimer()

                    // Bei sehr schnellen Systemen erfolgt der naechste Event
                    // bereits bevor dieser Code abgearbeitet ist: 2x notify():
                    // Der WaitCursor wird angezeigt, obwohl delay noch nicht
                    // vorbei. Daher 100 ms Toleranz.
                    wait(250);
                    wait(delay);

                    // if(logger.isDebugEnabled())logger.debug("showing wait cursor");
                    if (source instanceof Component) {
                        parent = SwingUtilities.getRoot((Component)source);
                    } else if (source instanceof MenuComponent) {
                        final MenuContainer mParent = ((MenuComponent)source).getParent();

                        if (mParent instanceof Component) {
                            parent = SwingUtilities.getRoot((Component)mParent);
                        }
                    }

                    if ((parent != null) && parent.isShowing()
                                && (parent.getCursor().getType() == Cursor.DEFAULT_CURSOR)) {
                        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                } catch (InterruptedException iexp) {
                    // logger.warn("wait cursor thread interrupted", iexp);
                }
            }
        }
    }
}
