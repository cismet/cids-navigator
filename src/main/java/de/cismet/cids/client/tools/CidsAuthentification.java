/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.auth.LoginService;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsAuthentification extends LoginService implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CidsAuthentification.class);

    public static final String CONNECTION_PROXY_CLASS =
        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler";
    public static final String CONNECTION_CLASS = "Sirius.navigator.connection.RESTfulConnection";

    //~ Instance fields --------------------------------------------------------

    private final String connectionClass;
    private final String callServerURL;
    private final String connectionName;
    private final String domain;
    private final boolean compressionEnabled;
    private final ConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsAuthentification object.
     *
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     */
    @Deprecated
    public CidsAuthentification(final String callServerURL,
            final String domain,
            final boolean compressionEnabled) {
        this(callServerURL, domain, null, compressionEnabled, ConnectionContext.createDeprecated());
    }

    /**
     * Creates a new CidsAuthentification object.
     *
     * @param  connectionClass     DOCUMENT ME!
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     */
    @Deprecated
    public CidsAuthentification(final String connectionClass,
            final String callServerURL,
            final String domain,
            final boolean compressionEnabled) {
        this(connectionClass, callServerURL, domain, compressionEnabled, ConnectionContext.createDeprecated());
    }

    /**
     * Creates a new CidsAuthentification object.
     *
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  connectionName      DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     * @param  connectionContext   DOCUMENT ME!
     */
    public CidsAuthentification(final String callServerURL,
            final String domain,
            final String connectionName,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) {
        this.connectionClass = CONNECTION_CLASS;
        this.callServerURL = callServerURL;
        this.connectionName = connectionName;
        this.domain = domain;
        this.compressionEnabled = compressionEnabled;
        this.connectionContext = connectionContext;
    }

    /**
     * Creates a new CidsAuthentification object.
     *
     * @param  connectionClass     DOCUMENT ME!
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  connectionName      DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     * @param  connectionContext   DOCUMENT ME!
     */
    public CidsAuthentification(final String connectionClass,
            final String callServerURL,
            final String domain,
            final String connectionName,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) {
        this.connectionClass = connectionClass;
        this.connectionName = connectionName;
        this.callServerURL = callServerURL;
        this.domain = domain;
        this.compressionEnabled = compressionEnabled;
        this.connectionContext = connectionContext;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   name      DOCUMENT ME!
     * @param   password  DOCUMENT ME!
     * @param   server    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Override
    public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
        System.setProperty("sun.rmi.transport.connectionTimeout", "15");
        final String[] split = name.split("@");
        final String user = (split.length > 1) ? split[0] : name;
        final String group = (split.length > 1) ? split[1] : null;

        try {
            final Connection connection = ConnectionFactory.getFactory()
                        .createConnection(
                            connectionClass,
                            callServerURL,
                            connectionName,
                            compressionEnabled,
                            getConnectionContext());
            final ConnectionInfo connectionInfo = new ConnectionInfo();
            connectionInfo.setCallserverURL(callServerURL);
            connectionInfo.setPassword(new String(password));
            connectionInfo.setUserDomain(domain);
            connectionInfo.setUsergroup(group);
            connectionInfo.setUsergroupDomain(domain);
            connectionInfo.setUsername(user);
            final ConnectionSession session = ConnectionFactory.getFactory()
                        .createSession(connection, connectionInfo, true, getConnectionContext());
            final ConnectionProxy proxy = ConnectionFactory.getFactory()
                        .createProxy(CONNECTION_PROXY_CLASS, session, getConnectionContext());
            SessionManager.init(proxy);

            ClassCacheMultiple.setInstance(domain, getConnectionContext());
            return true;
        } catch (Throwable t) {
            LOG.error("Fehler beim Anmelden", t);
            return false;
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
