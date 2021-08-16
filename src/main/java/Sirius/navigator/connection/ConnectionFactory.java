/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.connection.proxy.ConnectionProxyHandler;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.newuser.UserException;

import org.apache.log4j.Logger;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.Proxy;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @author   Pascal
 * @author   martin.scholl@cismet.de
 * @version  1.0 12/22/2002
 */
public class ConnectionFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ConnectionFactory.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionFactory object.
     */
    private ConnectionFactory() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating singleton shared ConnectionManager instance"); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ConnectionFactory getFactory() {
        return LazyInitialiser.INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public Connection createConnection(final String connectionClassName, final String callserverURL)
            throws ConnectionException {
        return createConnection(connectionClassName, callserverURL, false, ConnectionContext.createDeprecated());
    }

    /**
     * Creates and initializes a new shared connection instance.
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     * @param   compressionEnabled   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public Connection createConnection(final String connectionClassName,
            final String callserverURL,
            final boolean compressionEnabled) throws ConnectionException {
        return createConnection(
                connectionClassName,
                callserverURL,
                compressionEnabled,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     * @param   compressionEnabled   DOCUMENT ME!
     * @param   connectionContext    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    public Connection createConnection(final String connectionClassName,
            final String callserverURL,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws ConnectionException {
        final Connection connection = createConnection(connectionClassName);
        connection.connect(callserverURL, compressionEnabled, connectionContext);

        return connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     * @param   proxy                DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public Connection createConnection(final String connectionClassName, final String callserverURL, final Proxy proxy)
            throws ConnectionException {
        return createConnection(connectionClassName, callserverURL, proxy, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     * @param   proxy                DOCUMENT ME!
     * @param   compressionEnabled   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public Connection createConnection(final String connectionClassName,
            final String callserverURL,
            final Proxy proxy,
            final boolean compressionEnabled) throws ConnectionException {
        return createConnection(
                connectionClassName,
                callserverURL,
                proxy,
                compressionEnabled,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     * @param   callserverURL        DOCUMENT ME!
     * @param   proxy                DOCUMENT ME!
     * @param   compressionEnabled   DOCUMENT ME!
     * @param   connectionContext    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    public Connection createConnection(final String connectionClassName,
            final String callserverURL,
            final Proxy proxy,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws ConnectionException {
        final Connection connection = createConnection(connectionClassName);
        connection.connect(callserverURL, proxy, compressionEnabled, connectionContext);

        return connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private Connection createConnection(final String connectionClassName) throws ConnectionException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("creating connection class instance '" + connectionClassName + "'"); // NOI18N
            }

            return (Connection)Class.forName(connectionClassName).newInstance();
        } catch (ClassNotFoundException cne) {
            final String message = "connection class '" + connectionClassName + "' not found";             // NOI18N
            LOG.fatal(message, cne);
            throw new ConnectionException(message, cne);
        } catch (final InstantiationException ie) {
            final String message = "could not instantiate connection class '" + connectionClassName + "'"; // NOI18N
            LOG.fatal(message, ie);
            throw new ConnectionException(message, ie);
        } catch (IllegalAccessException iae) {
            final String message = "could not instantiate connection class '" + connectionClassName + "'"; // NOI18N
            LOG.fatal(message, iae);
            throw new ConnectionException(message, iae);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public ConnectionSession createSession(final Connection connection) throws ConnectionException {
        return createSession(connection, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection         DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  RuntimeException     DOCUMENT ME!
     */
    public ConnectionSession createSession(final Connection connection, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return new ConnectionSession(connection, connectionContext);
        } catch (final UserException ue) {
            final String message = "could not create connection session for connection"; // NOI18N
            LOG.fatal(message, ue);
            throw new RuntimeException(message, ue);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection      DOCUMENT ME!
     * @param   connectionInfo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionSession createSession(final Connection connection, final ConnectionInfo connectionInfo)
            throws ConnectionException, UserException {
        return createSession(connection, connectionInfo, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection         DOCUMENT ME!
     * @param   connectionInfo     DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public ConnectionSession createSession(final Connection connection,
            final ConnectionInfo connectionInfo,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        return new ConnectionSession(connection, connectionInfo, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection      DOCUMENT ME!
     * @param   connectionInfo  DOCUMENT ME!
     * @param   autoLogin       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionSession createSession(final Connection connection,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin) throws ConnectionException, UserException {
        return createSession(connection, connectionInfo, autoLogin, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection         DOCUMENT ME!
     * @param   connectionInfo     DOCUMENT ME!
     * @param   autoLogin          DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public ConnectionSession createSession(final Connection connection,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        return new ConnectionSession(connection, connectionInfo, autoLogin, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection       DOCUMENT ME!
     * @param   usergroupDomain  DOCUMENT ME!
     * @param   usergroup        DOCUMENT ME!
     * @param   userDomain       DOCUMENT ME!
     * @param   username         DOCUMENT ME!
     * @param   password         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionSession createSession(final Connection connection,
            final String usergroupDomain,
            final String usergroup,
            final String userDomain,
            final String username,
            final String password) throws ConnectionException, UserException {
        return createSession(
                connection,
                usergroupDomain,
                usergroup,
                userDomain,
                username,
                password,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connection         DOCUMENT ME!
     * @param   usergroupDomain    DOCUMENT ME!
     * @param   usergroup          DOCUMENT ME!
     * @param   userDomain         DOCUMENT ME!
     * @param   username           DOCUMENT ME!
     * @param   password           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public ConnectionSession createSession(final Connection connection,
            final String usergroupDomain,
            final String usergroup,
            final String userDomain,
            final String username,
            final String password,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        final ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUsername(username);
        connectionInfo.setPassword(password);
        connectionInfo.setUsergroup(usergroup);
        connectionInfo.setUserDomain(userDomain);
        connectionInfo.setUsergroupDomain(usergroupDomain);

        return new ConnectionSession(connection, connectionInfo, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionSession                DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    public ConnectionProxy createProxy(final String connectionProxyHandlerClassName,
            final ConnectionSession connectionSession) throws ConnectionException {
        return createProxy(connectionProxyHandlerClassName,
                connectionSession,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionSession                DOCUMENT ME!
     * @param   connectionContext                DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    public ConnectionProxy createProxy(final String connectionProxyHandlerClassName,
            final ConnectionSession connectionSession,
            final ConnectionContext connectionContext) throws ConnectionException {
        final ConnectionProxyHandler connectionProxyHandler;

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("creating connection proxy handler instance '" + connectionProxyHandlerClassName + "'");       // NOI18N
            }
            connectionProxyHandler = (ConnectionProxyHandler)Class.forName(connectionProxyHandlerClassName)
                        .getConstructor(new Class[] { ConnectionSession.class })
                        .newInstance(new Object[] { connectionSession });
        } catch (final ClassNotFoundException cne) {
            final String message = "connection proxy handler class '" + connectionProxyHandlerClassName + "' not found"; // NOI18N
            LOG.fatal(message, cne);
            throw new ConnectionException(message, cne);
        } catch (final Exception e) {
            final String message = "could not instantiate connection proxy handler class '"                              // NOI18N
                        + connectionProxyHandlerClassName + "'";                                                         // NOI18N
            LOG.fatal(message, e);
            throw new ConnectionException(message, e);
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("creating the connection proxy"); // NOI18N
            }

            return (ConnectionProxy)java.lang.reflect.Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(),
                    new Class[] { ConnectionProxy.class },
                    connectionProxyHandler);
        } catch (final Exception e) {
            final String message = "could not create connection proxy"; // NOI18N
            LOG.fatal(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName              DOCUMENT ME!
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionInfo                   DOCUMENT ME!
     * @param   autoLogin                        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionProxy createProxy(final String connectionClassName,
            final String connectionProxyHandlerClassName,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin) throws ConnectionException, UserException {
        return createProxy(
                connectionClassName,
                connectionProxyHandlerClassName,
                connectionInfo,
                autoLogin,
                false,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName              DOCUMENT ME!
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionInfo                   DOCUMENT ME!
     * @param   autoLogin                        DOCUMENT ME!
     * @param   compressionEnabled               DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionProxy createProxy(final String connectionClassName,
            final String connectionProxyHandlerClassName,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin,
            final boolean compressionEnabled) throws ConnectionException, UserException {
        return createProxy(
                connectionClassName,
                connectionProxyHandlerClassName,
                connectionInfo,
                autoLogin,
                compressionEnabled,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName              DOCUMENT ME!
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionInfo                   DOCUMENT ME!
     * @param   autoLogin                        DOCUMENT ME!
     * @param   compressionEnabled               DOCUMENT ME!
     * @param   connectionContext                DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public ConnectionProxy createProxy(final String connectionClassName,
            final String connectionProxyHandlerClassName,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        final Connection connection = createConnection(
                connectionClassName,
                connectionInfo.getCallserverURL(),
                compressionEnabled,
                connectionContext);
        final ConnectionSession connectionSession = createSession(
                connection,
                connectionInfo,
                autoLogin,
                connectionContext);

        return createProxy(connectionProxyHandlerClassName, connectionSession, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionClassName              DOCUMENT ME!
     * @param   connectionProxyHandlerClassName  DOCUMENT ME!
     * @param   connectionInfo                   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Deprecated
    public ConnectionProxy createProxy(final String connectionClassName,
            final String connectionProxyHandlerClassName,
            final ConnectionInfo connectionInfo) throws ConnectionException, UserException {
        return createProxy(
                connectionClassName,
                connectionProxyHandlerClassName,
                connectionInfo,
                true,
                false,
                ConnectionContext.createDeprecated());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final ConnectionFactory INSTANCE = new ConnectionFactory();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
