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

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsAuthentification extends LoginService {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CidsAuthentification.class);

    public static final String CONNECTION_PROXY_CLASS =
        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler";
    public static final String CONNECTION_CLASS = "Sirius.navigator.connection.RMIConnection";

    //~ Instance fields --------------------------------------------------------

    private final String callServerURL;
    private final String domain;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsAuthentification object.
     *
     * @param  callServerURL  DOCUMENT ME!
     * @param  domain         DOCUMENT ME!
     */
    public CidsAuthentification(final String callServerURL, final String domain) {
        this.callServerURL = callServerURL;
        this.domain = domain;
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

        final String connectionclass = "Sirius.navigator.connection.RMIConnection";

        try {
            final Connection connection = ConnectionFactory.getFactory()
                        .createConnection(connectionclass, callServerURL);
            final ConnectionInfo connectionInfo = new ConnectionInfo();
            connectionInfo.setCallserverURL(callServerURL);
            connectionInfo.setPassword(new String(password));
            connectionInfo.setUserDomain(domain);
            connectionInfo.setUsergroup(group);
            connectionInfo.setUsergroupDomain(domain);
            connectionInfo.setUsername(user);
            final ConnectionSession session = ConnectionFactory.getFactory()
                        .createSession(connection, connectionInfo, true);
            final ConnectionProxy proxy = ConnectionFactory.getFactory().createProxy(CONNECTION_PROXY_CLASS, session);
            SessionManager.init(proxy);

            ClassCacheMultiple.setInstance(domain);
            return true;
        } catch (Throwable t) {
            LOG.error("Fehler beim Anmelden", t);
            return false;
        }
    }
}
