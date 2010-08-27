package de.cismet.reconnector;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author jruiz
 */
public abstract class Reconnector<S extends Object> {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Reconnector.class);

    private Class serviceClass;
    private ConnectorWorker connectorWorker;
    private JDialog reconnectorDialog;
    private JFrame dialogOwner;
    private S service;
    private boolean isReconnecting = true;

    private final ReconnectorListener dispatcher = new ListenerDispatcher();
    private List<ReconnectorListener> listeners = new LinkedList<ReconnectorListener>();
    private List<ReconnectorPanel> reconnectorPanels = new LinkedList<ReconnectorPanel>();
    //private List<ReconnectorWrapperPanel> wrapperPanels = new LinkedList<ReconnectorWrapperPanel>();

    protected Reconnector(final Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    protected abstract S connectService() throws ReconnectorException;

    protected abstract ReconnectorException getReconnectorException(final Throwable throwable) throws Throwable;

    /*
     * Abbrechen
     */
    public void doAbbort() {
        reconnectorDialog.setVisible(false);
        isReconnecting = false;
    }

    /*
     * Verbindungsaufbau über Swingworker
     */
    public void doReconnect() {
        // Worker schon vorhanden?
        if (connectorWorker != null) {
            // worker anhalten
            connectorWorker.cancel(false);
            connectorWorker = null;
        }
        if (isReconnecting) {
            // neuen Worker erzeugen und starten
            connectorWorker = new ConnectorWorker();
            connectorWorker.execute();
        }
    }

    /*
     * Verbindungsaufbau abbrechen
     */
    public void doCancel() {
        if (connectorWorker != null && !connectorWorker.isDone()) {
            connectorWorker.cancel(true);
            connectorWorker = null;
            log.debug("Verbindungsvorgang abgebrochen");
        } else {
            log.debug("nichts zum Abbrechen");

        }
    }

    public void addListener(ReconnectorListener listener) {
        listeners.add(listener);
    }

    /*
     * Service Proxy
     */
    public S getProxy() {
        return (S) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] { serviceClass },
                new ReconnectorInvocationHandler()
        );
    }

    void pack() {
        reconnectorDialog.pack();
    }

    public void useDialog(final boolean useDialog, final JFrame parentFrame) {
        if (useDialog) {
            if (parentFrame != null) {
                dialogOwner = parentFrame;
            } else {
                dialogOwner = null;
            }

            reconnectorDialog = new JDialog(dialogOwner, "Verbindungsfehler", true);
            reconnectorDialog.setResizable(false);
            reconnectorDialog.setContentPane(createReconnectorPanel());
            reconnectorDialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent we) {
                    doAbbort();
                }

            });
            reconnectorDialog.setLocationRelativeTo(null);
            reconnectorDialog.setAlwaysOnTop(true);
        } else {
            //TODO reconnectorPanel des Dialogs aus der Liste entfernen
            if (reconnectorDialog != null) {
                ReconnectorPanel panel = (ReconnectorPanel) reconnectorDialog.getContentPane();
                reconnectorPanels.remove(panel);
                listeners.remove(panel);
            }
            dialogOwner = null;
            reconnectorDialog = null;
        }
    }

//    public JPanel registerFrame(final JPanel panel) {
//        final ReconnectorWrapperPanel reconnectorWrapperPanel = new ReconnectorWrapperPanel(panel, createReconnectorPanel());
//        addListener(reconnectorWrapperPanel);
//        //wrapperPanels.add(reconnectorWrapperPanel);
//        return reconnectorWrapperPanel;
//    }

    /*
     * Die Nutzung des services ist Fehlgeschlagen.
     *
     * Je nach Modus (attended / unattend) wird die Verbindung entweder sofort
     * wieder hergestellt, oder es wird eine Fehlermeldung angezeigt und
     * darauf gewartet, dass der User die Verbindung wieder anstößt.
     */
    private void serviceFailed(final ReconnectorException exception) {
        Component component = exception.getComponent();
        if (isUnattended()) {
            doReconnect();
        } else {
            dispatcher.connectionFailed(new ReconnectorEvent(component));
        }
    }

    /*
     * mit GUI oder ohne?
     */
    private boolean isUnattended() {
        return (reconnectorDialog == null/* && wrapperPanels.isEmpty()*/);
    }

    private ReconnectorPanel createReconnectorPanel() {
        final ReconnectorPanel reconnectorPanel = new ReconnectorPanel(this);

        reconnectorPanels.add(reconnectorPanel);
        addListener(reconnectorPanel);
        return reconnectorPanel;
    }

    class ReconnectorInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
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
                    log.debug("Exception while invocation", ex);
                    final Throwable targetEx = ex.getTargetException();
                    serviceFailed(getReconnectorException(targetEx));
                }
            }
            isReconnecting = true;
            throw new AbortException();
        }
    }

    class ConnectorWorker extends SwingWorker<S, Void> {

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

    class ListenerDispatcher implements ReconnectorListener {

        @Override
        public void connecting() {
            for (final ReconnectorListener listener : listeners) {
                listener.connecting();
            }

            if (reconnectorDialog != null) {
                reconnectorDialog.pack();
                if (!reconnectorDialog.isVisible()) {
                    reconnectorDialog.setLocationRelativeTo(dialogOwner);
                    reconnectorDialog.setVisible(true);
                }
            }
        }

        @Override
        public void connectionFailed(ReconnectorEvent event) {
            for (final ReconnectorListener listener : listeners) {
                listener.connectionFailed(event);
            }

            if (reconnectorDialog != null) {
                reconnectorDialog.pack();
                if (!reconnectorDialog.isVisible()) {
                    reconnectorDialog.setLocationRelativeTo(dialogOwner);
                    reconnectorDialog.setVisible(true);
                }
            }
        }

        @Override
        public void connectionCompleted() {
            for (final ReconnectorListener listener : listeners) {
                listener.connectionCompleted();
            }

            if (reconnectorDialog != null) {
                if (reconnectorDialog.isVisible()) {
                    reconnectorDialog.setVisible(false);
                }
            }
        }

        @Override
        public void connectionCanceled() {
            for (final ReconnectorListener listener : listeners) {
                listener.connectionCanceled();
            }

            if (reconnectorDialog != null) {
                reconnectorDialog.pack();
                if (!reconnectorDialog.isVisible()) {
                    reconnectorDialog.setLocationRelativeTo(dialogOwner);
                    reconnectorDialog.setVisible(true);
                }
            }
        }
    }
}
