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

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.cismet.cids.dynamics.Disposable;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class DisposableAgent {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DisposableAgent.class);
    private static final int CANCEL_AFTER_MS = 75000;

    //~ Instance fields --------------------------------------------------------

    private final Map<Disposable, Timer> disposableToTimerMap = new HashMap<>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DisposableAgent object.
     */
    private DisposableAgent() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDisposableCount() {
        return disposableToTimerMap.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  disposable  DOCUMENT ME!
     */
    public void register(final Disposable disposable) {
        final Timer timer = new Timer();
        disposableToTimerMap.put(disposable, timer);
        timer.schedule(new DisposableTimerTask(disposable), CANCEL_AFTER_MS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   disposable  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isRegistered(final Disposable disposable) {
        return disposableToTimerMap.containsKey(disposable);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  disposable  DOCUMENT ME!
     */
    public void unregister(final Disposable disposable) {
        if (isRegistered(disposable)) {
            final Timer timer = disposableToTimerMap.remove(disposable);
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DisposableAgent getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  disposable  DOCUMENT ME!
     */
    public void dispose(final Disposable disposable) {
        disposable.dispose();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final DisposableAgent INSTANCE = new DisposableAgent();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class DisposableTimerTask extends TimerTask {

        //~ Instance fields ----------------------------------------------------

        private final Disposable diposable;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DisposableTimerTask object.
         *
         * @param  diposable  DOCUMENT ME!
         */
        private DisposableTimerTask(final Disposable diposable) {
            this.diposable = diposable;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            try {
                dispose(diposable);
            } catch (final Exception ex) {
                LOG.warn("error while diposing", ex);
            } finally {
                unregister(diposable);
            }
        }
    }
}
