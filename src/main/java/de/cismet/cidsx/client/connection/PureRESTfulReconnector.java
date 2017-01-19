/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cidsx.client.connection;

import Sirius.navigator.connection.RESTfulReconnector;

import org.openide.util.Lookup;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;

import de.cismet.cidsx.client.connector.RESTfulInterfaceConnector;

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("connection to cids pure REST service '" + callserverURL + "'");
        }

        final SSLConfigProvider sslConfigProvider = Lookup.getDefault().lookup(SSLConfigProvider.class);
        final SSLConfig sslConfig = (sslConfigProvider == null) ? null : sslConfigProvider.getSSLConfig();

        return (R)new RESTfulInterfaceConnector(callserverURL, proxy, sslConfig);
    }
}
