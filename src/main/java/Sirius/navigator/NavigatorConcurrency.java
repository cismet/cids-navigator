/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class NavigatorConcurrency {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient ThreadGroup NAVIGATOR_THREAD_GROUP;

    static {
        final SecurityManager s = System.getSecurityManager();
        final ThreadGroup parent = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

        NAVIGATOR_THREAD_GROUP = new ThreadGroup(parent, "Navigator"); // NOI18N
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SudplanConcurrency object.
     */
    private NavigatorConcurrency() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   prefix  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ThreadFactory createThreadFactory(final String prefix) {
        return createThreadFactory(prefix, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   prefix      DOCUMENT ME!
     * @param   excHandler  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ThreadFactory createThreadFactory(final String prefix,
            final Thread.UncaughtExceptionHandler excHandler) {
        return new NavigatorThreadFactory(prefix, excHandler);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Very similar to the {@link Executors#defaultThreadFactory()} implementation.
     *
     * @version  $Revision$, $Date$
     */
    private static final class NavigatorThreadFactory implements ThreadFactory {

        //~ Static fields/initializers -----------------------------------------

        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

        //~ Instance fields ----------------------------------------------------

        private final transient String prefix;
        private final transient AtomicInteger createCount;
        private final transient Thread.UncaughtExceptionHandler excHandler;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SudplanThreadFactory object.
         *
         * @param  prefix      DOCUMENT ME!
         * @param  excHandler  DOCUMENT ME!
         */
        NavigatorThreadFactory(final String prefix, final Thread.UncaughtExceptionHandler excHandler) {
            this.prefix = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-"; // NOI18N
            this.createCount = new AtomicInteger(1);
            this.excHandler = excHandler;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Thread newThread(final Runnable r) {
            final Thread t = new Thread(NAVIGATOR_THREAD_GROUP, r, prefix + createCount.getAndIncrement(), 0);

            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            t.setUncaughtExceptionHandler(excHandler);

            return t;
        }
    }
}
