/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.exception.SqlConnectionException;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.object.FieldNotFoundException;
import Sirius.server.localserver.user.PasswordCheckException;
import Sirius.server.middleware.interfaces.proxy.MetaService;
import Sirius.server.middleware.types.AbstractAttributeRepresentationFormater;
import Sirius.server.middleware.types.HistoryObject;
import Sirius.server.middleware.types.LightweightMetaObject;
import Sirius.server.middleware.types.Link;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.Node;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;
import Sirius.server.search.SearchRuntimeException;

import Sirius.util.image.ImageHashMap;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.awt.GraphicsEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;

import javax.swing.Icon;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.actions.PreparedAsyncByteAction;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.cids.utils.ErrorUtils;

import de.cismet.cidsx.server.api.types.GenericResourceWithContentType;

import de.cismet.commons.security.WebDavClient;

import de.cismet.connectioncontext.AbstractConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;

import de.cismet.reconnector.Reconnector;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public class RESTfulConnection implements Connection, Reconnectable<CallServerService>, ProxyHandler.Listener {

    //~ Static fields/initializers ---------------------------------------------

    protected static final transient Logger LOG = Logger.getLogger(RESTfulConnection.class);
    private static final String DISABLE_MO_FILENAME = "cids_disable_lwmo"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private transient Reconnector<CallServerService> reconnector;
    private String connectionName = RESTfulConnection.class.getSimpleName();

    private final transient boolean isLWMOEnabled;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RESTfulConnection object.
     */
    public RESTfulConnection() {
        final String uHome = System.getProperty("user.home");                                       // NOI18N
        if (uHome != null) {
            final File homeDir = new File(uHome);
            final File disableIndicator = new File(homeDir, DISABLE_MO_FILENAME);
            isLWMOEnabled = !disableIndicator.isFile();
            if (!isLWMOEnabled) {
                LOG.warn("LIGHTWIGHTMETAOBJECT CODE IS DISABLED! FOUND FILE: " + disableIndicator); // NOI18N
            }
        } else {
            isLWMOEnabled = true;
        }

        ProxyHandler.getInstance().addListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void proxyChanged(final ProxyHandler.Event event) {
        getReconnector().setProxy(event.getNewProxy());
        new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        reconnect();
                    } catch (final Exception ex) {
                        LOG.error(ex, ex);
                    }
                }
            }).start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  reconnector  DOCUMENT ME!
     */
    protected void setReconnector(final Reconnector<CallServerService> reconnector) {
        this.reconnector = reconnector;
    }

    @Override
    public RESTfulReconnector getReconnector() {
        return (RESTfulReconnector)reconnector;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected CallServerService getConnector() {
        return (reconnector != null) ? reconnector.getCallserver() : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   proxy               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Reconnector<CallServerService> createReconnector(final String callserverURL,
            final Proxy proxy,
            final boolean compressionEnabled) {
        final RESTfulReconnector reconnector = new RESTfulReconnector(
                CallServerService.class,
                callserverURL,
                proxy,
                getConnectionName(),
                compressionEnabled);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    @Deprecated
    public boolean connect(final String callserverURL) throws ConnectionException {
        return connect(callserverURL, false, ConnectionContext.createDeprecated());
    }

    @Override
    @Deprecated
    public boolean connect(final String callserverURL, final boolean compressionEnabled) throws ConnectionException {
        return connect(callserverURL, compressionEnabled, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean connect(final String callserverURL,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws ConnectionException {
        return connect(callserverURL, null, compressionEnabled, connectionContext);
    }

    @Override
    @Deprecated
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        return connect(callserverURL, proxy, false);
    }

    @Override
    @Deprecated
    public boolean connect(final String callserverURL, final Proxy proxy, final boolean compressionEnabled)
            throws ConnectionException {
        return connect(callserverURL, proxy, compressionEnabled, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private boolean testConnection(final ConnectionContext connectionContext) throws ConnectionException {
        try {
            getConnector().getDomains(connectionContext);
        } catch (final Exception e) {
            final String message = "no connection to restful interface"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }

        return true;
    }

    @Override
    public boolean connect(final String callserverURL,
            final Proxy proxy,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws ConnectionException {
        setReconnector(createReconnector(callserverURL, proxy, compressionEnabled));

        return testConnection(connectionContext);
    }

    @Override
    public boolean reconnect() throws ConnectionException {
        if (reconnector instanceof RESTfulReconnector) {
            ((RESTfulReconnector)reconnector).doReconnect();
            return testConnection(ConnectionContext.create(
                        AbstractConnectionContext.Category.OPTIONS,
                        RESTfulConnection.class.getSimpleName()));
        } else {
            return false;
        }
    }

    @Override
    public void disconnect() {
        if (reconnector != null) {
            reconnector.dispose();
        }
        reconnector = null;
        ProxyHandler.getInstance().removeListener(this);
    }

    @Override
    public boolean isConnected() {
        return reconnector != null;
    }

    @Deprecated
    @Override
    public String[] getDomains() throws ConnectionException {
        return getDomains(ConnectionContext.createDeprecated());
    }

    @Override
    public String[] getDomains(final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getDomains(connectionContext);
        } catch (final Exception e) {
            final String message = "cannot get domains"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    @Deprecated
    public ImageHashMap getDefaultIcons() throws ConnectionException {
        return getDefaultIcons(ConnectionContext.createDeprecated());
    }

    @Override
    public ImageHashMap getDefaultIcons(final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return new ImageHashMap(getConnector().getDefaultIcons(connectionContext));
        } catch (final Exception e) {
            final String message = "cannot get default icons"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    @Deprecated
    public Icon getDefaultIcon(final String name) throws ConnectionException {
        return getDefaultIcon(name, ConnectionContext.createDeprecated());
    }

    @Override
    public Icon getDefaultIcon(final String name, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getDefaultIcons(connectionContext).get(name);
        } catch (final Exception e) {
            final String message = "cannot get default icon with name: " + name; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public User getUser(final String userGroupLsName,
            final String userGroupName,
            final String userLsName,
            final String userName,
            final String password) throws ConnectionException, UserException {
        return getUser(
                userGroupLsName,
                userGroupName,
                userLsName,
                userName,
                password,
                ConnectionContext.createDeprecated());
    }

    @Override
    public User getUser(final String userGroupLsName,
            final String userGroupName,
            final String userLsName,
            final String userName,
            final String password,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        try {
            return getConnector().getUser(
                    userGroupLsName,
                    userGroupName,
                    userLsName,
                    userName,
                    password,
                    connectionContext);
        } catch (final UserException e) {
            final String message = "cannot get user: " // NOI18N
                        + userGroupLsName
                        + " :: "                       // NOI18N
                        + userGroupName
                        + " :: "                       // NOI18N
                        + userLsName
                        + " :: "                       // NOI18N
                        + userName
                        + " :: ****";
            LOG.error(message, e);
            throw e;
        } catch (final Exception e) {
            final String message = "cannot get user: " // NOI18N
                        + userGroupLsName
                        + " :: "                       // NOI18N
                        + userGroupName
                        + " :: "                       // NOI18N
                        + userLsName
                        + " :: "                       // NOI18N
                        + userName
                        + " :: ****";                  // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Vector getUserGroupNames() throws ConnectionException {
        return getUserGroupNames(ConnectionContext.createDeprecated());
    }

    @Override
    public Vector getUserGroupNames(final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getUserGroupNames(connectionContext);
        } catch (final Exception e) {
            final String message = "could not get usergroup names"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Vector getUserGroupNames(final String username, final String domain) throws ConnectionException,
        UserException {
        return getUserGroupNames(username, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public Vector getUserGroupNames(final String username,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException, UserException {
        try {
            return getConnector().getUserGroupNames(username, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get usergroup names by username,domain: " + username + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean changePassword(final User user, final String oldPassword, final String newPassword)
            throws ConnectionException, UserException, PasswordCheckException {
        return changePassword(user, oldPassword, newPassword, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean changePassword(final User user,
            final String oldPassword,
            final String newPassword,
            final ConnectionContext connectionContext) throws ConnectionException,
        UserException,
        PasswordCheckException {
        try {
            return getConnector().changePassword(user, oldPassword, newPassword, connectionContext);
        } catch (final Exception e) {
            final String message = "could not change password: " + user + " :: " + oldPassword + " :: " + newPassword; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getRoots(final User user) throws ConnectionException {
        return getRoots(user, ConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getRoots(final User user, final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getRoots(user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getRoots(final User user, final String domain) throws ConnectionException {
        return getRoots(user, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getRoots(final User user, final String domain, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getRoots(user, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getChildren(final Node node, final User user) throws ConnectionException {
        return getChildren(node, user, ConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getChildren(final Node node, final User user, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getChildren(node, user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get children for node and user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node getNode(final User user, final int nodeID, final String domain) throws ConnectionException {
        return getNode(user, nodeID, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public Node getNode(final User user,
            final int nodeID,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getMetaObjectNode(user, nodeID, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get node for user, nodeID, domain: " // NOI18N
                        + user
                        + "@"                                                      // NOI18N
                        + domain
                        + " :: "                                                   // NOI18N
                        + nodeID;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node addNode(final Node node, final Link parent, final User user) throws ConnectionException {
        return addNode(node, parent, user, ConnectionContext.createDeprecated());
    }

    @Override
    public Node addNode(final Node node,
            final Link parent,
            final User user,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().addNode(node, parent, user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not add node with parent and user: " + node + " :: " + parent + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean deleteNode(final Node node, final User user) throws ConnectionException {
        return deleteNode(node, user, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean deleteNode(final Node node, final User user, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().deleteNode(node, user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not delete node for user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean addLink(final Node from, final Node to, final User user) throws ConnectionException {
        return addLink(from, to, user, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean addLink(final Node from,
            final Node to,
            final User user,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().addLink(from, to, user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not add link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean deleteLink(final Node from, final Node to, final User user) throws ConnectionException {
        return deleteLink(from, to, user, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean deleteLink(final Node from,
            final Node to,
            final User user,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().deleteLink(from, to, user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not delete link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getClassTreeNodes(final User user) throws ConnectionException {
        return getClassTreeNodes(user, ConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getClassTreeNodes(final User user, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getClassTreeNodes(user, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get classtree nodes for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaClass getMetaClass(final User user, final int classID, final String domain) throws ConnectionException {
        return getMetaClass(user, classID, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass getMetaClass(final User user,
            final int classID,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getClass(user, classID, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get metaclass for user, domain, classid " // NOI18N
                        + user
                        + "@"                                                           // NOI18N
                        + domain
                        + " :: "                                                        // NOI18N
                        + classID;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaClass[] getClasses(final User user, final String domain) throws ConnectionException {
        return getClasses(user, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass[] getClasses(final User user, final String domain, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getClasses(user, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get classes for user, doamin: " + user + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    @Deprecated
    public MetaObject getMetaObject(final User user, final int objectID, final int classID, final String domain)
            throws ConnectionException {
        return getMetaObject(user, objectID, classID, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject getMetaObject(final User user,
            final int objectID,
            final int classID,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getMetaObject(user, objectID, classID, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get metaobject for user, objectid, classid, domain: " // NOI18N
                        + user
                        + "@"                                                                       // NOI18N
                        + domain
                        + " :: "                                                                    // NOI18N
                        + objectID
                        + " :: "                                                                    // NOI18N
                        + classID;
            LOG.error(message, e);

            if (e.getCause() instanceof FieldNotFoundException) {
                final FieldNotFoundException ex = (FieldNotFoundException)e.getCause();

                if (!ErrorUtils.exceptionAlreadyShown(ex)) {
                    ErrorUtils.addThrownException(ex);
                    ExceptionManager.getManager()
                            .showExceptionDialog(
                                ExceptionManager.ERROR,
                                "FieldNotFound",
                                NbBundle.getMessage(
                                    RESTfulConnection.class,
                                    "RESTfulConnection.getMetaObject.fieldNotFound",
                                    ex.getFieldName(),
                                    ex.getTableName()),
                                e);
                }
            }

            throw new ConnectionException(message, e);
        }
    }

    @Override
    @Deprecated
    public MetaObject[] getMetaObjectByQuery(final User user, final String query) throws ConnectionException {
        return getMetaObjectByQuery(user, query, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user,
            final String query,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getMetaObject(user, query, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get metaobject for user, query: " + user + " :: " + query; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject[] getMetaObjectByQuery(final User user, final String query, final String domain)
            throws ConnectionException {
        return getMetaObjectByQuery(user, query, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user,
            final String query,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getMetaObject(user, query, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get metaobject for user, query, domain: " + user + " :: " // NOI18N
                        + query
                        + " :: "                                                                        // NOI18N
                        + domain;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject insertMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        return insertMetaObject(user, metaObject, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject insertMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().insertMetaObject(user, metaObject, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not insert metaobject for user, metaobject, domain: " // NOI18N
                        + user
                        + "@"                                                                   // NOI18N
                        + domain
                        + " :: "                                                                // NOI18N
                        + metaObject;
            LOG.error(message, e);
            /*
             *if the top level cause was an SQL Exception, we throw an instance of SqlConnectionException which are
             * visualised with a custom error dialog by the MethodManager
             */
            final Throwable initialCause = getTopInitialCause(e);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException(message, e);
            }
        }
    }

    @Deprecated
    @Override
    public int updateMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        return updateMetaObject(user, metaObject, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public int updateMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().updateMetaObject(user, metaObject, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not update metaobject for user, metaobject, domain: " // NOI18N
                        + user
                        + "@"                                                                   // NOI18N
                        + domain
                        + " :: "                                                                // NOI18N
                        + metaObject;
            LOG.error(message, e);
            /*
             *if the top level cause was an SQL Exception, we throw an instance of SqlConnectionException which are
             * visualised with a custom error dialog by the MethodManager
             */
            final Throwable initialCause = getTopInitialCause(e);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException(message, e);
            }
        }
    }

    @Deprecated
    @Override
    public int deleteMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        return deleteMetaObject(user, metaObject, domain, ConnectionContext.createDeprecated());
    }

    @Override
    public int deleteMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().deleteMetaObject(user, metaObject, domain, connectionContext);
        } catch (final Exception e) {
            final String message = "could not delete metaobject for user, metaobject, domain: " // NOI18N
                        + user
                        + "@"                                                                   // NOI18N
                        + domain
                        + " :: "                                                                // NOI18N
                        + metaObject;
            LOG.error(message, e);
            /*
             *if the top level cause was an SQL Exception, we throw an instance of SqlConnectionException which are
             * visualised with a custom error dialog by the MethodManager
             */
            final Throwable initialCause = getTopInitialCause(e);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException(message, e);
            }
        }
    }

    @Deprecated
    @Override
    public MetaObject getInstance(final User user, final MetaClass c) throws ConnectionException {
        return getInstance(user, c, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject getInstance(final User user, final MetaClass c, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getInstance(user, c, connectionContext);
        } catch (final Exception e) {
            final String message = "could not get instance for user, metaclass: " + user + " :: " + c; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        return getAllLightweightMetaObjectsForClass(
                classId,
                user,
                representationFields,
                representationPattern,
                ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final String representationPattern,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmos = getConnector().getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        representationPattern,
                        connectionContext);
                return initLWMOs(lwmos, null, connectionContext);
            } else {
                return getLWMOFallback(classId, user, connectionContext);
            }
        } catch (final Exception e) {
            final String message = "could not get all lightweight MOs for classid, user, fields, pattern: " // NOI18N
                        + classId
                        + " :: "                                                                            // NOI18N
                        + user
                        + " :: "                                                                            // NOI18N
                        + representationFields
                        + " :: "                                                                            // NOI18N
                        + representationPattern;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        return getAllLightweightMetaObjectsForClass(
                classId,
                user,
                representationFields,
                formater,
                ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = getConnector().getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        connectionContext);
                return initLWMOs(lwmo, formater, connectionContext);
            } else {
                return getLWMOFallback(classId, user, connectionContext);
            }
        } catch (final Exception e) {
            final String message = "could not get all lightweight MOs for classid, user, fields: " // NOI18N
                        + classId
                        + " :: "                                                                   // NOI18N
                        + user
                        + " :: "                                                                   // NOI18N
                        + representationFields;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        return getLightweightMetaObjectsByQuery(
                classId,
                user,
                query,
                representationFields,
                representationPattern,
                ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final String representationPattern,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = getConnector().getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        representationPattern,
                        connectionContext);
                return initLWMOs(lwmo, null, connectionContext);
            } else {
                return getLWMOFallback(classId, user, connectionContext);
            }
        } catch (final Exception e) {
            final String message = "could not get all lightweight MOs for classid, user, fields, pattern: " // NOI18N
                        + classId
                        + " :: "                                                                            // NOI18N
                        + user
                        + " :: "                                                                            // NOI18N
                        + representationFields
                        + " :: "                                                                            // NOI18N
                        + representationPattern;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        return getLightweightMetaObjectsByQuery(
                classId,
                user,
                query,
                representationFields,
                formater,
                ConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = getConnector().getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        connectionContext);
                return initLWMOs(lwmo, formater, connectionContext);
            } else {
                return getLWMOFallback(classId, user, connectionContext);
            }
        } catch (final Exception e) {
            final String message = "could not get all lightweight MOs for classid, user, fields: " // NOI18N
                        + classId
                        + " :: "                                                                   // NOI18N
                        + user
                        + " :: "                                                                   // NOI18N
                        + representationFields;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Collection customServerSearch(final User user, final CidsServerSearch serverSearch)
            throws ConnectionException {
        return customServerSearch(user, serverSearch, ConnectionContext.createDeprecated());
    }

    @Override
    public Collection customServerSearch(final User user,
            final CidsServerSearch serverSearch,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().customServerSearch(user, serverSearch, connectionContext);
        } catch (SearchRuntimeException e) {
            throw e;
        } catch (final Exception e) {
            final String message = "error during custom search";
            throw new ConnectionException(message, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   e  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Throwable getTopInitialCause(final Exception e) {
        Throwable initialCause = e.getCause();

        if (initialCause != null) {
            while (initialCause.getCause() != null) {
                initialCause = initialCause.getCause();
            }
        }

        return initialCause;
    }

    /**
     * Initializes LWMOs with the appropriate metaservice and string formatter.
     *
     * @param   lwmos              DOCUMENT ME!
     * @param   formater           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaObject[] initLWMOs(final LightweightMetaObject[] lwmos,
            final AbstractAttributeRepresentationFormater formater,
            final ConnectionContext connectionContext) {
        if (lwmos != null) {
            final MetaService msServer = getConnector();
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
                    lwmo.initWithConnectionContext(connectionContext);
                    if (formater != null) {
                        lwmo.setFormater(formater);
                    }
                }
            }
        }

        return lwmos;
    }

    /**
     * !For debugging purpose only. Do not use!
     *
     * @param   classId            DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private MetaObject[] getLWMOFallback(final int classId,
            final User user,
            final ConnectionContext connectionContext) throws ConnectionException {
        final MetaClass mc = ClassCacheMultiple.getMetaClass(user.getDomain(), classId, connectionContext);
        final ClassAttribute ca = mc.getClassAttribute("sortingColumn"); // NOI18N
        final String orderBy;

        if (ca == null) {
            orderBy = "";          // NOI18N
        } else {
            orderBy = " order by " // NOI18N
                        + ca.getValue().toString();
        }

        final String query = "select " // NOI18N
                    + mc.getID()
                    + ","              // NOI18N
                    + mc.getPrimaryKey()
                    + " from "         // NOI18N
                    + mc.getTableName()
                    + orderBy;

        return getMetaObjectByQuery(user, query, connectionContext);
    }

    @Deprecated
    @Override
    public String getConfigAttr(final User user, final String key) throws ConnectionException {
        return getConfigAttr(user, key, ConnectionContext.createDeprecated());
    }

    @Override
    public String getConfigAttr(final User user, final String key, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().getConfigAttr(user, key, connectionContext);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get config attr for user: " + user, e); // NOI18N
        }
    }

    @Deprecated
    @Override
    public boolean hasConfigAttr(final User user, final String key) throws ConnectionException {
        return hasConfigAttr(user, key, ConnectionContext.createDeprecated());
    }

    @Override
    public boolean hasConfigAttr(final User user, final String key, final ConnectionContext connectionContext)
            throws ConnectionException {
        try {
            return getConnector().hasConfigAttr(user, key, connectionContext);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not check config attr for user: " + user, e); // NOI18N
        }
    }

    @Deprecated
    @Override
    public HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements) throws ConnectionException {
        return getHistory(classId, objectId, domain, user, elements, ConnectionContext.createDeprecated());
    }

    @Override
    public HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements,
            final ConnectionContext connectionContext) throws ConnectionException {
        try {
            return getConnector().getHistory(classId, objectId, domain, user, elements, connectionContext);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get history: classId: " + classId + " || objectId: " // NOI18N
                        + objectId
                        + " || domain: " + domain + " || user: " + user + " || elements: " + elements, // NOI18N
                e);
        }
    }

    @Override
    @Deprecated
    public Object executeTask(final User user,
            final String taskname,
            final String taskdomain,
            final Object body,
            final ServerActionParameter... params) throws ConnectionException {
        return executeTask(user, taskname, taskdomain, body, ConnectionContext.createDeprecated(), params);
    }

    @Override
    public Object executeTask(final User user,
            final String taskname,
            final String taskdomain,
            final Object body,
            final ConnectionContext connectionContext,
            final ServerActionParameter... params) throws ConnectionException {
        return executeTask(user, taskname, taskdomain, body, connectionContext, true, params);
    }

    @Override
    public Object executeTask(final User user,
            final String taskname,
            final String taskdomain,
            final Object body,
            final ConnectionContext connectionContext,
            final boolean resolvePreparedAsyncByteAction,
            final ServerActionParameter... params) throws ConnectionException {
        try {
            // FIXME: workaround for legacy clients that do not support GenericResourceWithContentType
            final Object taskResult = getConnector().executeTask(
                    user,
                    taskname,
                    taskdomain,
                    body,
                    connectionContext,
                    params);
            if ((taskResult != null) && GenericResourceWithContentType.class.isAssignableFrom(taskResult.getClass())) {
                LOG.warn("REST Action  '" + taskname + "' completed, GenericResourceWithContentType with type '"
                            + ((GenericResourceWithContentType)taskResult).getContentType() + "' generated.");
                return ((GenericResourceWithContentType)taskResult).getRes();
            } else {
                if (resolvePreparedAsyncByteAction && (taskResult instanceof PreparedAsyncByteAction)) {
                    final PreparedAsyncByteAction preparedTaskResult = (PreparedAsyncByteAction)taskResult;

                    final String server = preparedTaskResult.getUrl();
                    final WebDavClient webDavClient = new WebDavClient(ProxyHandler.getInstance().getProxy(),
                            "",
                            "");

                    try {
                        final InputStream iStream = webDavClient.getInputStream(server);
                        final long length = preparedTaskResult.getLength();
                        final byte[] tmp = new byte[1024];

                        if (length > 0) {
                            final byte[] result = new byte[(int)length];
                            int resCounter = 0;
                            int counter;

                            // iStream.read(result) does sometimes not read the whole stream

                            while ((counter = iStream.read(tmp)) != -1) {
                                System.arraycopy(tmp, 0, result, resCounter, counter);
                                resCounter += counter;
                            }

                            return result;
                        } else {
                            final ByteArrayOutputStream result = new ByteArrayOutputStream();
                            int counter;

                            // iStream.read(result) does sometimes not read the whole stream

                            while ((counter = iStream.read(tmp)) != -1) {
                                result.write(tmp, 0, counter);
                            }

                            return result.toByteArray();
                        }
                    } catch (Exception e) {
                        LOG.error("Error while download action result", e);

                        return null;
                    }
                } else {
                    return taskResult;
                }
            }
        } catch (final RemoteException e) {
            throw new ConnectionException("could not executeTask: taskname: " + taskname + " || body: " + body
                        + " || taskdomain: " + taskdomain
                        + " || user: " + user,
                e);
        }
    }

    @Override
    public CallServerService getCallServerService() {
        return reconnector.getCallserver();
    }

    @Override
    public void setConnectionName(final String connectionName) {
        this.connectionName = connectionName;
    }

    @Override
    public String getConnectionName() {
        return connectionName;
    }
}
