/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.method;

import Sirius.navigator.ui.progress.*;

import javax.swing.SwingUtilities;

import de.cismet.tools.CismetThreadPool;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you subclass to perform
 * GUI-related work in a dedicated thread. For instructions on using this class, see:
 *
 * <p>http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html</p>
 *
 * <p>Note that the API changed slightly in the 3rd version: You must now invoke start() on the SwingWorker after
 * creating it.</p>
 *
 * @version  $Revision$, $Date$
 */
public abstract class MultithreadedMethod {

    //~ Instance fields --------------------------------------------------------

    protected ProgressObserver progressObserver;

    private ThreadVar threadVar;

    //~ Constructors -----------------------------------------------------------

    /**
     * Start a thread that will call the <code>construct</code> method and then exit.
     */
    public MultithreadedMethod() {
        this(null);
    }

    /**
     * Creates a new MultithreadedMethod object.
     *
     * @param  progressObserver  DOCUMENT ME!
     */
    public MultithreadedMethod(final ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
        this.threadVar = new ThreadVar();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final ProgressObserver getProgressObserver() {
        return this.progressObserver;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final boolean isProgressObservable() {
        return (this.progressObserver != null) ? true : false;
    }

    /**
     * Start the worker thread.
     *
     * @param  object  DOCUMENT ME!
     */
    public final void invoke(final Object object) {
        if (threadVar.get() == null) {
            threadVar.set(new Thread(new DoInvokeThread(), "MultithreadedMethod"));
            this.init(object);
            CismetThreadPool.execute(threadVar.get());
        }
    }

    /**
     * A new method that interrupts the worker thread. Call this method to force the worker to stop what it's doing.
     */
    public final void interrupt() {
        final Thread thread = threadVar.get();

        if (thread != null) {
            thread.interrupt();
        }

        if (this.isProgressObservable()) {
            progressObserver.setInterrupted(true);
        }

        threadVar.clear();
    }

    // #########################################################################
    /**
     * Place your code that should be executed before the thread starts here.
     *
     * @param  object  DOCUMENT ME!
     */
    protected void init(final Object object) {
    }

    // .........................................................................
    /**
     * Place your code that should be executed in a new thread here.
     */
    protected abstract void doInvoke();

    // .........................................................................
    /**
     * Place your code that should be executed after the thread has finished here. This method is called on the event
     * dispatching thread.
     */
    protected void finish() {
    }

    //~ Inner Classes ----------------------------------------------------------

    // #########################################################################
    /**
     * Class to maintain reference to current worker thread under separate synchronization control.
     *
     * @version  $Revision$, $Date$
     */
    private static final class ThreadVar {

        //~ Instance fields ----------------------------------------------------

        private Thread thread = null;

        //~ Methods ------------------------------------------------------------

        /**
         * ThreadVar(Thread thread) { this.thread = thread; }.
         *
         * @return  DOCUMENT ME!
         */
        synchronized Thread get() {
            return thread;
        }

        /**
         * DOCUMENT ME!
         */
        synchronized void clear() {
            thread = null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  thread  DOCUMENT ME!
         */
        synchronized void set(final Thread thread) {
            if (this.thread == null) {
                this.thread = thread;
            }
        }
    }
    /**
     * #########################################################################.
     *
     * @version  $Revision$, $Date$
     */
    private final class DoInvokeThread implements Runnable {

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            try {
                doInvoke();
            } finally {
                threadVar.clear();
            }

            SwingUtilities.invokeLater(new UpdateUIThread());
        }

        //~ Inner Classes ------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @version  $Revision$, $Date$
         */
        private final class UpdateUIThread implements Runnable {

            //~ Methods --------------------------------------------------------

            @Override
            public void run() {
                finish();
            }
        }
    }
}
