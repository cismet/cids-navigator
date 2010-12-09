/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

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
public abstract class SwingWorker {

    //~ Instance fields --------------------------------------------------------

    private Object value; // see getValue(), setValue()
    private Thread thread;

    private ThreadVar threadVar;

    //~ Constructors -----------------------------------------------------------

    /**
     * Start a thread that will call the <code>construct</code> method and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {

                @Override
                public void run() {
                    finished();
                }
            };

        final Runnable doConstruct = new Runnable() {

                @Override
                public void run() {
                    try {
                        setValue(construct());
                    } finally {
                        threadVar.clear();
                    }

                    SwingUtilities.invokeLater(doFinished);
                }
            };

        final Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Get the value produced by the worker thread, or null if it hasn't been constructed yet.
     *
     * @return  DOCUMENT ME!
     */
    protected synchronized Object getValue() {
        return value;
    }

    /**
     * Set the value produced by worker thread.
     *
     * @param  x  DOCUMENT ME!
     */
    private synchronized void setValue(final Object x) {
        value = x;
    }

    /**
     * Compute the value to be returned by the <code>get</code> method.
     *
     * @return  DOCUMENT ME!
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread) after the <code>construct</code> method has
     * returned.
     */
    public void finished() {
    }

    /**
     * A new method that interrupts the worker thread. Call this method to force the worker to stop what it's doing.
     */
    public void interrupt() {
        final Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method. Returns null if either the constructing thread or
     * the current thread was interrupted before a value was produced.
     *
     * @return  the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {
            final Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        final Thread t = threadVar.get();
        if (t != null) {
            CismetThreadPool.execute(t);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Class to maintain reference to current worker thread under separate synchronization control.
     *
     * @version  $Revision$, $Date$
     */
    private static class ThreadVar {

        //~ Instance fields ----------------------------------------------------

        private Thread thread;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ThreadVar object.
         *
         * @param  t  DOCUMENT ME!
         */
        ThreadVar(final Thread t) {
            thread = t;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
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
    }
}

// old

/**
 * An abstract class that you subclass to perform
 * GUI-related work in a dedicated thread.
 * For instructions on using this class, see
 * http://java.sun.com/products/jfc/swingdoc-current/threads2.html
 */
/*public abstract class SwingWorker {
    private Object value;  // see getValue(), setValue()
    private Thread thread;

    /**
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
/*private static class ThreadVar {
 *  private Thread thread; ThreadVar(Thread t) { thread = t; } synchronized Thread get() { return thread; } synchronized
 * void clear() { thread = null; } }
 *
 * private ThreadVar threadVar;
 *
 * /** Get the value produced by the worker thread, or null if it hasn't been constructed yet.
 */
/* protected synchronized Object getValue() {
 * return value; }
 *
 * /** Set the value produced by worker thread
 */
/* private synchronized void setValue(Object x) {
 * value = x; }
 *
 * /** Compute the value to be returned by the <code>get</code> method.
 */
/* public abstract Object construct();
 *
 * /** Called on the event dispatching thread (not on the worker thread) after the <code>construct</code> method has
 * returned.
 */
/* public void finished() {
 * }
 *
 * /** A new method that interrupts the worker thread.  Call this method to force the worker to abort what it's doing.
 */
/* public void interrupt() {
 *  Thread t = threadVar.get(); if (t != null) { t.interrupt(); } threadVar.clear(); }
 *
 * /** Return the value created by the <code>construct</code> method. Returns null if either the constructing thread or
 * the current thread was interrupted before a value was produced.
 *
 * @return the value created by the <code>construct</code> method
 */
/* public Object get() {
 *  while (true) {     Thread t = threadVar.get();     if (t == null) {         return getValue();     }     try {
 *   t.join();     }     catch (InterruptedException e) {         Thread.currentThread().interrupt(); // propagate
 *   return null;     } } }
 *
 *
 * /** Start a thread that will call the <code>construct</code> method and then exit.
 */
/* public SwingWorker() {
 *  final Runnable doFinished = new Runnable() {    public void run() { finished(); } };
 *
 * Runnable doConstruct = new Runnable() {     public void run() {         try {             setValue(construct());
 *  }         finally {             threadVar.clear();         }
 *
 *       SwingUtilities.invokeLater(doFinished);     } };
 *
 * Thread t = new Thread(doConstruct); threadVar = new ThreadVar(t); t.start(); }}*/
