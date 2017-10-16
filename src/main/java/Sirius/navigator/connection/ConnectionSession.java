/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                http://www.enviromatics.net
                                Prof. Dr. Reiner Guettler
                                Prof. Dr. Ralf Denzer

                                HTW
                                University of Applied Sciences
                                Goebenstr. 40
                                66117 Saarbruecken, Germany

        Programmers     :       Pascal <pascal@enviromatics.net>

        Project         :       Sirius
        Version         :       1.0
        Purpose         :
        Created         :       12/20/2002
        History         :

*******************************************************************************/

import Sirius.navigator.exception.ConnectionException;

import Sirius.server.newuser.*;
import Sirius.server.newuser.permission.*;

import org.apache.log4j.*;

import java.rmi.*;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;

/**
 * Stores a <code>Connection</code> and a <code>User</code> Object.
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public class ConnectionSession implements ClientConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(ConnectionSession.class);

    //~ Instance fields --------------------------------------------------------

    private final Connection connection;
    private final ConnectionInfo connectionInfo;
    // private final Permission writePermission;

    private boolean loggedin = false;
    private User user;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ConnectionSession.
     *
     * @param   connection  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    protected ConnectionSession(final Connection connection) throws ConnectionException, UserException {
        this(connection, new ConnectionInfo(), false);
    }

    /**
     * Creates a new instance of ConnectionSession.
     *
     * @param   connection      DOCUMENT ME!
     * @param   connectionInfo  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    protected ConnectionSession(final Connection connection, final ConnectionInfo connectionInfo)
            throws ConnectionException, UserException {
        this(connection, connectionInfo, true);
    }

    /**
     * Creates a new ConnectionSession object.
     *
     * @param   connection      DOCUMENT ME!
     * @param   connectionInfo  DOCUMENT ME!
     * @param   autoLogin       DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    protected ConnectionSession(final Connection connection,
            final ConnectionInfo connectionInfo,
            final boolean autoLogin) throws ConnectionException, UserException {
        if (logger.isDebugEnabled()) {
            logger.debug("creating new connection session"); // NOI18N
        }

        this.connection = connection;
        this.connectionInfo = connectionInfo;
        // this.writePermission = new Permission(PermissionHolder.WRITE, "write", "accessExplicit");

        if (autoLogin) {
            loggedin = login();
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public User getUser() {
        return this.user;
    }

    /**
     * public Permission getWritePermission() { return this.writePermission; }.
     *
     * @param   usergroupDomain  DOCUMENT ME!
     * @param   usergroup        DOCUMENT ME!
     * @param   userDomain       DOCUMENT ME!
     * @param   username         DOCUMENT ME!
     * @param   password         DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public void login(final String usergroupDomain,
            final String usergroup,
            final String userDomain,
            final String username,
            final String password) throws ConnectionException, UserException {
        if (loggedin && (user != null) && connectionInfo.getUsergroupDomain().equals(usergroupDomain)
                    && connectionInfo.getUsergroup().equals(usergroup)
                    && connectionInfo.getUserDomain().equals(userDomain)
                    && connectionInfo.getUsername().equals(username) && connectionInfo.getPassword().equals(password)) {
            logger.warn("can't perform login: this user '" + connectionInfo.getUsername() + "' is already logged in"); // NOI18N
        } else {
            if (loggedin && (user != null)) {
                if (logger.isInfoEnabled()) {
                    logger.info("logging out user '" + connectionInfo.getUsername() + "'");                            // NOI18N
                }
            }

            connectionInfo.setUsername(username);
            connectionInfo.setPassword(password);
            connectionInfo.setUsergroup(usergroup);
            connectionInfo.setUserDomain(userDomain);
            connectionInfo.setUsergroupDomain(usergroupDomain);

            loggedin = login();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   userDomain  DOCUMENT ME!
     * @param   username    DOCUMENT ME!
     * @param   password    DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    public void login(final String userDomain,
            final String username,
            final String password) throws ConnectionException, UserException {
        if (loggedin && (user != null)
                    && connectionInfo.getUserDomain().equals(userDomain)
                    && connectionInfo.getUsername().equals(username) && connectionInfo.getPassword().equals(password)) {
            logger.warn("can't perform login: this user '" + connectionInfo.getUsername() + "' is already logged in"); // NOI18N
        } else {
            if (loggedin && (user != null)) {
                if (logger.isInfoEnabled()) {
                    logger.info("logging out user '" + connectionInfo.getUsername() + "'");                            // NOI18N
                }
            }

            connectionInfo.setUsername(username);
            connectionInfo.setPassword(password);
            connectionInfo.setUsergroup(null);
            connectionInfo.setUserDomain(userDomain);
            connectionInfo.setUsergroupDomain(null);

            loggedin = login();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    private boolean login() throws ConnectionException, UserException {
        if (!connection.isConnected()) {
            logger.error("can't login: no connection established");                                             // NOI18N
            throw new ConnectionException("can't login: no connection established", ConnectionException.ERROR); // NOI18N
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("logging in user '" + connectionInfo.getUsergroupDomain() + "' '"
                            + connectionInfo.getUsergroup() + "' '" + connectionInfo.getUserDomain() + "' '"
                            + connectionInfo.getUsername() + "' '" /*+ connectionInfo.getPassword() + "'"*/); // NOI18N
            }
            this.user = connection.getUser(connectionInfo.getUsergroupDomain(),
                    connectionInfo.getUsergroup(),
                    connectionInfo.getUserDomain(),
                    connectionInfo.getUsername(),
                    connectionInfo.getPassword(),
                    getClientConnectionContext());
        } catch (UserException ue) {
            logger.warn("can't login: wrong user informations", ue); // NOI18N
            throw ue;
        } catch (ConnectionException ce) {
            logger.fatal("[ServerError] can't login"); // NOI18N
            // throw new ConnectionException("[ServerError] can't login", ConnectionException.FATAL, re);
            throw ce;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void logout() {
        this.loggedin = false;
        this.user = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isLoggedin() {
        return this.loggedin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isConnected() {
        try {
            return this.connection.isConnected();
        } catch (Exception ex) {
            logger.fatal("An unexpected exception occoured in method 'Connection.isConnected()'", ex); // NOI18N
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConnectionInfo getConnectionInfo() {
        return this.connectionInfo;
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }
}
