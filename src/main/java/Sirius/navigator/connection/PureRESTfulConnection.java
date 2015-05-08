/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
package Sirius.navigator.connection;

import static Sirius.navigator.connection.RESTfulConnection.LOG;
import Sirius.navigator.exception.ConnectionException;
import Sirius.server.newuser.UserException;
import Sirius.util.image.ImageHashMap;
import java.awt.GraphicsEnvironment;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import javax.swing.Icon;
import javax.ws.rs.core.UriBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * The PureRESTfulConnection allows the cids navigator to use the new cids Pure
 * REST API while providing backwards compatibility with the old Connection
 * interface.
 *
 * <p>
 * This class extends the 'java-objects-over-http' RESTfulConnection and
 * internally uses a new CallServerService Implementation
 * (PureRESTfulReconnector and RESTfulInterfaceConnector, respectively) that
 * connects to the cids server Pure REST API.</p>
 *
 * @author Pascal Dihé <pascal.dihe@cismet.de>
 * @version 1.0 2015/04/17
 */
public class PureRESTfulConnection extends RESTfulConnection {

    /**
     * FIXME: legacyConnector is used for operations that are currently not
     * implemented by cids REST service
     */
    private transient CallServerService legacyConnector;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new PureRESTfulConnection object.
     */
    public PureRESTfulConnection() {
        super();
    }

    //~ Methods ----------------------------------------------------------------
    private CallServerService createLegacyConnector(final String restServerURL, final Proxy proxy) throws ConnectionException {
        final SSLConfigProvider sslConfigProvider = Lookup.getDefault().lookup(SSLConfigProvider.class);
        final SSLConfig sslConfig = (sslConfigProvider == null) ? null : sslConfigProvider.getSSLConfig();

        try {
            final UriBuilder uriBuilder = UriBuilder.fromUri(restServerURL);

            // FIXME:
            LOG.debug("building legacy callServerURL from restServerURL '" + restServerURL + "', assuming default values for path (callserver/binary) and port (9986)");
            final URI callServerURI = uriBuilder.port(9986).replacePath("callserver/binary").build();
            LOG.warn("creating additional legacy connection to service '" + callServerURI + "'");
            return new RESTfulSerialInterfaceConnector(callServerURI.toString(), proxy, sslConfig);
        } catch (Exception ex) {
            final String message = "could n ot build legacy callServerURL from restServerURL '" + restServerURL + "': " + ex.getMessage();
            LOG.error(message, ex);
            throw new ConnectionException(message, ex);
        }
    }

    /**
     * Overridden to return a PureRESTfulReconnector that internally uses a
     * RESTfulInterfaceConnector to interact with the cids server Pure REST API.
     *
     * @param callserverURL DOCUMENT ME!
     * @param proxy DOCUMENT ME!
     *
     * @return PureRESTfulReconnector
     */
    @Override
    protected Reconnector<CallServerService> createReconnector(final String callserverURL, final Proxy proxy) {
        reconnector = new PureRESTfulReconnector(CallServerService.class, callserverURL, proxy);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        this.connector = createReconnector(callserverURL, proxy).getProxy();

        // FIXME: remove when all methods implemented in pure RESTful Service
        this.legacyConnector = createLegacyConnector(callserverURL, proxy);

        try {
            this.getDomains();
        } catch (final Exception e) {
            final String message = "Could not connect cids PURE REST Service at '" + callserverURL + "' (proxy: " + proxy + ")"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }

        return true;
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement
     * in pure RESTful Service
     *
     * @return
     * @throws ConnectionException
     */
    @Override
    public String[] getDomains() throws ConnectionException {
        try {
            LOG.warn("delegating getDomains() to legacy REST Connection");
            return this.legacyConnector.getDomains();
        } catch (final Exception e) {
            final String message = "cannot get domains: " + e.getMessage(); // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement
     * in pure RESTful Service
     *
     * @return
     * @throws ConnectionException
     */
    @Override
    public Vector getUserGroupNames() throws ConnectionException {
        try {
            LOG.warn("delegating getUserGroupNames() to legacy REST Connection");
            return this.legacyConnector.getUserGroupNames();
        } catch (final Exception e) {
            final String message = "could not get usergroup names: " + e.getMessage(); // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement
     * in pure RESTful Service
     * 
     * @param username
     * @param domain
     * @return
     * @throws ConnectionException
     * @throws UserException 
     */
    @Override
    public Vector getUserGroupNames(final String username, final String domain) throws ConnectionException,
            UserException {
        try {
            LOG.warn("delegating getUserGroupNames(" + username + ", " + domain + ") to legacy REST Connection");
            return this.legacyConnector.getUserGroupNames(username, domain);
        } catch (final Exception e) {
            final String message = "could not get usergroup names by username, domain: " + username + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement
     * in pure RESTful Service
     * 
     * @return
     * @throws ConnectionException 
     */
    @Override
    public ImageHashMap getDefaultIcons() throws ConnectionException {
        try {
            LOG.warn("delegating getDefaultIcons() to legacy REST Connection");
            return new ImageHashMap(this.legacyConnector.getDefaultIcons());
        } catch (final Exception e) {
            final String message = "cannot get default icons from legacy REST Server"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement
     * in pure RESTful Service
     * 
     * @param name
     * @return
     * @throws ConnectionException 
     */
    @Override
    public Icon getDefaultIcon(final String name) throws ConnectionException {
        try {
            LOG.warn("delegating getDefaultIcon("+name+") to legacy REST Connection");
            return getDefaultIcons().get(name);
        } catch (final Exception e) {
            final String message = "cannot get default icon with name '" + name + "' from from legacy REST Server"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }
}
