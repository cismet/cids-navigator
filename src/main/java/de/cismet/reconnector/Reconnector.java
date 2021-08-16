/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector;

import java.awt.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import de.cismet.cids.server.ws.rest.SSLInitializationException;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class Reconnector<S> {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Reconnector.class);

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static enum ReconnectorState {

        //~ Enum constants -----------------------------------------------------

        CONNECTING, CANCELED, COMPLETED, FAILED
    }

    //~ Instance fields --------------------------------------------------------

    private final Class serviceClass;
    private ConnectorWorker connectorWorker;
    private ReconnectorDialog reconnectorDialog;
    private S service;
    private boolean isReconnecting = false;

    private final ReconnectorListener dispatcher = new ListenerDispatcher();
    private final List<ReconnectorListener> listeners = new LinkedList<>();
    // private final List<ReconnectorWrapperPanel> wrapperPanels = new LinkedList<>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Reconnector object.
     *
     * @param  serviceClass  DOCUMENT ME!
     */
    protected Reconnector(final Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ReconnectorException  DOCUMENT ME!
     */
    protected abstract S connectService() throws ReconnectorException;

    /**
     * DOCUMENT ME!
     *
     * @param   throwable  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Throwable  DOCUMENT ME!
     */
    protected abstract ReconnectorException getReconnectorException(final Throwable throwable) throws Throwable;

    /**
     * Abbrechen.
     */
    public void doAbort() {
        isReconnecting = false;
    }

    /**
     * Verbindungsaufbau über Swingworker.
     */
    public void doReconnect() {
        LOG.fatal("reconnecting", new Exception());
        // Worker schon vorhanden?
        if (connectorWorker != null) {
            // worker anhalten
            connectorWorker.cancel(true);
            connectorWorker = null;
        }
        if (isReconnecting) {
            // neuen Worker erzeugen und starten
            connectorWorker = new ConnectorWorker();
            connectorWorker.execute();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void addListener(final ReconnectorListener listener) {
        listeners.add(listener);
    }

    /**
     * Service Proxy.
     *
     * @return  DOCUMENT ME!
     */
    public S getProxy() {
        return (S)Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] { serviceClass },
                new ReconnectorInvocationHandler());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  useDialog    DOCUMENT ME!
     * @param  parentFrame  DOCUMENT ME!
     */
    public void useDialog(final boolean useDialog, final JFrame parentFrame) {
        if (useDialog) {
            if (reconnectorDialog != null) {
                listeners.remove(reconnectorDialog);
            }
            reconnectorDialog = new ReconnectorDialog(parentFrame, this);
            addListener(reconnectorDialog);
        } else {
            // TODO reconnectorPanel des Dialogs aus der Liste entfernen
            if (reconnectorDialog != null) {
                listeners.remove(reconnectorDialog);
            }
            reconnectorDialog = null;
        }
    }

    /**
     * public JPanel registerFrame(final JPanel panel) { final ReconnectorWrapperPanel reconnectorWrapperPanel = new
     * ReconnectorWrapperPanel(panel, createReconnectorPanel()); addListener(reconnectorWrapperPanel);
     * //wrapperPanels.add(reconnectorWrapperPanel); return reconnectorWrapperPanel; } Die Nutzung des services ist
     * Fehlgeschlagen. Je nach Modus (attended / unattend) wird die Verbindung entweder sofort wieder hergestellt, oder
     * es wird eine Fehlermeldung angezeigt und darauf gewartet, dass der User die Verbindung wieder anstößt.
     *
     * @param  exception  DOCUMENT ME!
     */
    private void serviceFailed(final ReconnectorException exception) {
        final Component component = exception.getComponent();
        if (isUnattended()) {
            doReconnect();
        } else {
            dispatcher.connectionFailed(new ReconnectorEvent(component));
        }
    }

    /**
     * mit GUI oder ohne?
     *
     * @return  DOCUMENT ME!
     */
    private boolean isUnattended() {
        return (reconnectorDialog == null /* && wrapperPanels.isEmpty()*/);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ReconnectorInvocationHandler implements InvocationHandler {

        //~ Methods ------------------------------------------------------------

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            Throwable targetEx = null;
            isReconnecting = true;
            while (isReconnecting) {
                try {
                    // service wieder verbinden
                    if (service == null) {
                        service = connectService();
                    }

                    // Methodenaufruf durchreichen
                    return method.invoke(service, args);

                    // wieder verbinden fehlgeschlagen
                } catch (final ReconnectorException ex) {
                    // Fehler anzeigen und neuverbindung anfragen
                    serviceFailed(ex);
                } catch (final InvocationTargetException ex) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exception while invocation", ex);        // NOI18N
                    }
                    targetEx = ex.getTargetException();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Wrapped Exception", targetEx);           // NOI18N
                    }
                    serviceFailed(getReconnectorException(targetEx));
                } catch (final SSLInitializationException sslInitEx) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exception while invocation", sslInitEx); // NOI18N
                    }
                    targetEx = sslInitEx.getCause();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Wrapped Exception", targetEx);           // NOI18N
                    }
                    serviceFailed(getReconnectorException(targetEx));
                }
            }
            throw targetEx;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ConnectorWorker extends SwingWorker<S, Void> {

        //~ Methods ------------------------------------------------------------

        @Override
        protected S doInBackground() throws Exception {
            dispatcher.connecting();
            // alten service verwerfen
            service = null;
            // Verbindungs-Prozess starten
            try {
                return connectService();
            } catch (ReconnectorException exception) {
                serviceFailed(exception);
                throw new ExecutionException(exception);
            }
        }

        @Override
        protected void done() {
            // abgebrochen ?
            if (isCancelled()) {
                dispatcher.connectionCanceled();
                // abgeschlossen ?
            } else {
                try {
                    // neuen service setzen
                    service = get();
                    // panels verstecken
                    dispatcher.connectionCompleted();

                    // Fehler beim Verbinden?
                } catch (final Exception ex) {
                    // Neuverbindung in Gang setzen
                    if (ex instanceof ExecutionException) {
                    } else {
                        serviceFailed(new ReconnectorException(ex.getClass().getCanonicalName()));
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ListenerDispatcher implements ReconnectorListener {

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  state  DOCUMENT ME!
         * @param  evt    DOCUMENT ME!
         */
        private void updateAndNotifyAboutState(final ReconnectorState state, final ReconnectorEvent evt) {
            if (reconnectorDialog != null) {
                switch (state) {
                    case CONNECTING: {
                        for (final ReconnectorListener listener : listeners) {
                            listener.connecting();
                        }
                        break;
                    }

                    case FAILED: {
                        for (final ReconnectorListener listener : listeners) {
                            listener.connectionFailed(evt);
                        }
                        break;
                    }
                    case COMPLETED: {
                        for (final ReconnectorListener listener : listeners) {
                            listener.connectionCompleted();
                        }
                        break;
                    }
                    case CANCELED: {
                        for (final ReconnectorListener listener : listeners) {
                            listener.connectionCanceled();
                        }
                        break;
                    }
                }
            }
        }

        @Override
        public void connecting() {
            this.updateAndNotifyAboutState(ReconnectorState.CONNECTING, null);
        }

        @Override
        public void connectionFailed(final ReconnectorEvent event) {
            this.updateAndNotifyAboutState(ReconnectorState.FAILED, event);
        }

        @Override
        public void connectionCompleted() {
            this.updateAndNotifyAboutState(ReconnectorState.COMPLETED, null);
        }

        @Override
        public void connectionCanceled() {
            this.updateAndNotifyAboutState(ReconnectorState.CANCELED, null);
        }
    }
}
