/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import org.apache.log4j.Logger;

import java.util.HashSet;

import de.cismet.cids.server.actions.UncaughtClientExceptionServerAction;

import de.cismet.connectioncontext.AbstractConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.security.PrivacyClientHandler;

import de.cismet.tools.gui.exceptionnotification.DefaultExceptionHandlerListener;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class DefaultNavigatorExceptionHandler implements Thread.UncaughtExceptionHandler,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DefaultNavigatorExceptionHandler.class);
    private static final DefaultNavigatorExceptionHandler INSTANCE = new DefaultNavigatorExceptionHandler();

    //~ Instance fields --------------------------------------------------------

    private final HashSet<DefaultExceptionHandlerListener> listeners = new HashSet<>();
    private ConnectionContext connectionContext = ConnectionContext.create(
            AbstractConnectionContext.Category.STATIC,
            DefaultNavigatorExceptionHandler.class.getCanonicalName());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultNavigatorExceptionHandler object.
     */
    private DefaultNavigatorExceptionHandler() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DefaultNavigatorExceptionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable error) {
        if (isDisposedFxSceneUpdate(error)) {
            if (LOG.isDebugEnabled()) {
                // Harmless JavaFX race, still unfixed as of JavaFX 25: every scene attach makes
                // JFXPanel.setEmbeddedScene() call EmbeddedScene.setPixelScaleFactors(), which defers an
                // updateSceneState() via Platform.runLater(). If the JFXPanel leaves the Swing hierarchy before that
                // lambda runs, GlassScene.dispose() has already nulled sceneState and the lambda throws. The scene is
                // gone, so the skipped update is a no-op -- log it instead of alarming the user.
                LOG.debug("ignoring updateSceneState() on an already disposed JavaFX scene", error); // NOI18N
            }

            return;
        }

        for (final DefaultExceptionHandlerListener listener : listeners) {
            listener.uncaughtException(thread, error);
        }

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

        try {
            if (PrivacyClientHandler.getInstance().isSendUncaughtExceptions()) {
                SessionManager.getProxy()
                        .executeTask(
                            UncaughtClientExceptionServerAction.TASK_NAME,
                            SessionManager.getSession().getUser().getDomain(),
                            error,
                            getConnectionContext());
            }
        } catch (final ConnectionException ex) {
            LOG.error(ex, ex);
        }
    }

    /**
     * Checks whether the given throwable is the NullPointerException JavaFX raises when a deferred updateSceneState()
     * runs against an EmbeddedScene that has meanwhile been disposed.
     *
     * <p>Deliberately narrow: it matches only a NullPointerException whose innermost frame is
     * GlassScene.updateSceneState() and whose stack contains the deferring EmbeddedScene.setPixelScaleFactors() lambda.
     * Any other NullPointerException, including one from anywhere else in JavaFX, is still reported.</p>
     *
     * @param   error  the throwable to inspect, may be null
     *
     * @return  true if the throwable is that specific JavaFX race, false otherwise
     */
    private static boolean isDisposedFxSceneUpdate(final Throwable error) {
        if (!(error instanceof NullPointerException)) {
            return false;
        }

        final StackTraceElement[] trace = error.getStackTrace();

        if ((trace == null) || (trace.length == 0)
                    || !"com.sun.javafx.tk.quantum.GlassScene".equals(trace[0].getClassName()) // NOI18N
                    || !"updateSceneState".equals(trace[0].getMethodName())) {                 // NOI18N
            return false;
        }

        for (final StackTraceElement element : trace) {
            if ("com.sun.javafx.tk.quantum.EmbeddedScene".equals(element.getClassName())         // NOI18N
                        && element.getMethodName().startsWith("lambda$setPixelScaleFactors$")) { // NOI18N
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void addListener(final DefaultExceptionHandlerListener listener) {
        listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void removeListener(final DefaultExceptionHandlerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
