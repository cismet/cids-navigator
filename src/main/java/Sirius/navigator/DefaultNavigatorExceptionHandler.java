/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class DefaultNavigatorExceptionHandler implements Thread.UncaughtExceptionHandler {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DefaultNavigatorExceptionHandler.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public void uncaughtException(final Thread thread, final Throwable error) {
        if (error instanceof ThreadDeath) {
            final StackTraceElement[] ste = error.getStackTrace();
            if (LOG.isDebugEnabled() && (ste.length == 2) && ste[1].getClassName().startsWith("calpa.html.Cal")) { // NOI18N
                // downgrade calpa html's thread death to debug
                LOG.debug("uncaught exception in thread: " + thread, error); // NOI18N

                return;
            }
        }

        if (error instanceof Error) {
            LOG.fatal("uncaught error in thread: " + thread, error);     // NOI18N
        } else {
            LOG.error("uncaught exception in thread: " + thread, error); // NOI18N
        }
    }
}
