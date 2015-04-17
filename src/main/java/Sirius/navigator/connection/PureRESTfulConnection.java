/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import java.awt.GraphicsEnvironment;

import de.cismet.cids.server.CallServerService;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

/**
 * The PureRESTfulConnection allows the cids navigator to use the new cids Pure REST API while providing backwards
 * compatibility with the old Connection interface.
 *
 * <p>This class extends the 'java-objects-over-http' RESTfulConnection and internally uses a new CallServerService
 * Implementation (PureRESTfulReconnector and RESTfulInterfaceConnector, respectively) that connects to the cids server
 * Pure REST API.</p>
 *
 * @author   Pascal Dihé <pascal.dihe@cismet.de>
 * @version  1.0 2015/04/17
 */
public class PureRESTfulConnection extends RESTfulConnection {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PureRESTfulConnection object.
     */
    public PureRESTfulConnection() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Overriden to return a PureRESTfulReconnector that internally uses a RESTfulInterfaceConnector to interact with
     * the cids server Pure REST API.
     *
     * @param   callserverURL  DOCUMENT ME!
     * @param   proxy          DOCUMENT ME!
     *
     * @return  PureRESTfulReconnector
     */
    @Override
    protected Reconnector<CallServerService> createReconnector(final String callserverURL, final Proxy proxy) {
        reconnector = new RESTfulReconnector(CallServerService.class, callserverURL, proxy);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }
}
