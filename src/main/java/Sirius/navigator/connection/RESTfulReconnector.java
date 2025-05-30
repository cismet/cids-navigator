/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import lombok.Getter;
import lombok.Setter;

import org.openide.util.Lookup;

import java.rmi.RemoteException;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;
import de.cismet.reconnector.ReconnectorException;

/**
 * DOCUMENT ME!
 *
 * @param    <R>
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class RESTfulReconnector<R extends CallServerService> extends Reconnector<R> {

    //~ Static fields/initializers ---------------------------------------------

    protected static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RESTfulReconnector.class);

    //~ Instance fields --------------------------------------------------------

    private final RESTfulReconnectorErrorPanel errorPanel;
    private final ProxyOptionsPanel proxyOptionsPanel;

    @Getter private String callserverURL;
    @Getter private final boolean compressionEnabled;
    @Getter private final String connectionName;
    @Getter @Setter private Proxy proxy;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RESTfulReconnector object.
     *
     * @param  serviceClass   DOCUMENT ME!
     * @param  callserverURL  DOCUMENT ME!
     * @param  proxy          DOCUMENT ME!
     * @param  clientName     DOCUMENT ME!
     */
    public RESTfulReconnector(final Class serviceClass,
            final String callserverURL,
            final Proxy proxy,
            final String clientName) {
        this(serviceClass, callserverURL, proxy, clientName, false);
    }

    /**
     * Creates a new RESTfulReconnector object.
     *
     * @param  serviceClass        DOCUMENT ME!
     * @param  callserverURL       DOCUMENT ME!
     * @param  proxy               DOCUMENT ME!
     * @param  connectionName      DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     */
    public RESTfulReconnector(final Class serviceClass,
            final String callserverURL,
            final Proxy proxy,
            final String connectionName,
            final boolean compressionEnabled) {
        super(serviceClass);

        this.callserverURL = callserverURL;
        this.proxy = proxy;
        this.connectionName = connectionName;
        this.compressionEnabled = compressionEnabled;

        this.proxyOptionsPanel = new ProxyOptionsPanel();
        this.errorPanel = new RESTfulReconnectorErrorPanel(proxyOptionsPanel);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ReconnectorException getReconnectorException(final Throwable e) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getReconnectorException(exception) invoked", e);
        }

        final Throwable exception;
        if ((e instanceof RemoteException) && (e.getCause() != null)) {
            exception = e.getCause();
        } else {
            exception = e;
        }

        boolean error = false;
        if (exception instanceof UniformInterfaceException) {
            final int status = ((UniformInterfaceException)exception).getResponse().getStatus();
            error = (status == 502) || (status == 503) || (status == 407);
        } else if (exception instanceof IllegalArgumentException) {
            error = true;
        } else if (exception instanceof ClientHandlerException) {
            error = true;
        }

        if (error) {
            if (firstInvocation && isConnectionReset(exception)) {
                firstInvocation = false;
                return null;
            } else {
                final String message = org.openide.util.NbBundle.getMessage(
                        RESTfulReconnector.class,
                        "RESTfulReconnector.errormessage");
                if (LOG.isDebugEnabled()) {
                    LOG.debug(message);
                }
                errorPanel.setError(message, exception);
                return new ReconnectorException(errorPanel);
            }
        }

        throw exception;
    }

    @Override
    public void dispose() {
        super.dispose();
        proxyOptionsPanel.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   e  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isConnectionReset(final Throwable e) {
        int currentDeep = 0;
        Throwable ex = e;

        while ((ex != null) && (currentDeep < 20)) {
            ++currentDeep;
            if ((ex.getMessage() != null) && ex.getMessage().toLowerCase().contains("connection reset")) {
                return true;
            }

            ex = ex.getCause();
        }

        return false;
    }

    @Override
    protected R connectService() throws ReconnectorException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("connection to service '" + getCallserverURL() + "'");
        }

        final SSLConfigProvider sslConfigProvider = Lookup.getDefault().lookup(SSLConfigProvider.class);
        final SSLConfig sslConfig = (sslConfigProvider == null) ? null : sslConfigProvider.getSSLConfig();

        return (R)new RESTfulSerialInterfaceConnector(
                getCallserverURL(),
                getProxy(),
                sslConfig,
                getConnectionName(),
                isCompressionEnabled());
    }
}
