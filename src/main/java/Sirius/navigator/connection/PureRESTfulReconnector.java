/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import org.openide.util.Lookup;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

/**
 * Overrides RESTfulReconnector and uses the RESTfulInterfaceConnector CallServerService implementation to interact with
 * the cids server Pure REST API.
 *
 * @author   Pascal Dihé <pascal.dihe@cismet.de>
 * @version  1.0 2015/04/17
 */
public class PureRESTfulReconnector<R extends CallServerService> extends RESTfulReconnector<R> {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PureRESTfulReconnector object.
     *
     * @param  serviceClass   DOCUMENT ME!
     * @param  callserverURL  DOCUMENT ME!
     * @param  proxy          DOCUMENT ME!
     */
    public PureRESTfulReconnector(final Class serviceClass, final String callserverURL, final Proxy proxy) {
        super(serviceClass, callserverURL, proxy);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns a RESTfulInterfaceConnector CallServerService implementation to interact with the cids server Pure REST
     * API.
     *
     * @return  RESTfulInterfaceConnector instance as implementation of the
     */
    @Override
    protected R connectService() {
        final SSLConfigProvider sslConfigProvider = Lookup.getDefault().lookup(SSLConfigProvider.class);
        final SSLConfig sslConfig = (sslConfigProvider == null) ? null : sslConfigProvider.getSSLConfig();

        // TODO: replace by RESTfulInterfaceConnector!
        return (R)new RESTfulSerialInterfaceConnector(callserverURL, proxy, sslConfig);
    }
}
