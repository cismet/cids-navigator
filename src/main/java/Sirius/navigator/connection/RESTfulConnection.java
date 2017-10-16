/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.SqlConnectionException;

import Sirius.server.localserver.attribute.ClassAttribute;
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

import Sirius.util.image.ImageHashMap;

import org.apache.log4j.Logger;

import java.awt.GraphicsEnvironment;

import java.io.File;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;

import javax.swing.Icon;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.cidsx.server.api.types.GenericResourceWithContentType;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public class RESTfulConnection implements Connection,
    Reconnectable<CallServerService>,
    ClientConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    protected static final transient Logger LOG = Logger.getLogger(RESTfulConnection.class);
    private static final String DISABLE_MO_FILENAME = "cids_disable_lwmo"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    protected Reconnector<CallServerService> reconnector;

    protected transient CallServerService connector;

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
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Reconnector<CallServerService> getReconnector() {
        return reconnector;
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
        reconnector = new RESTfulReconnector(CallServerService.class, callserverURL, proxy, compressionEnabled);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    public boolean connect(final String callserverURL) throws ConnectionException {
        return connect(callserverURL, false);
    }

    @Override
    public boolean connect(final String callserverURL, final boolean compressionEnabled) throws ConnectionException {
        return connect(callserverURL, null, compressionEnabled);
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        return connect(callserverURL, proxy, false);
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy, final boolean compressionEnabled)
            throws ConnectionException {
        connector = createReconnector(callserverURL, proxy, compressionEnabled).getProxy();

        try {
            connector.getDomains(getClientConnectionContext());
        } catch (final Exception e) {
            final String message = "no connection to restful interface"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }

        return true;
    }

    @Override
    public boolean reconnect() throws ConnectionException {
        if (LOG.isInfoEnabled()) {
            LOG.info("reconnect not necessary for RESTful connector"); // NOI18N
        }

        return true;
    }

    @Override
    public void disconnect() {
        connector = null;
    }

    @Override
    public boolean isConnected() {
        return connector != null;
    }

    @Deprecated
    @Override
    public String[] getDomains() throws ConnectionException {
        return getDomains(ClientConnectionContext.createDeprecated());
    }

    @Override
    public String[] getDomains(final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getDomains(context);
        } catch (final Exception e) {
            final String message = "cannot get domains"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public ImageHashMap getDefaultIcons() throws ConnectionException {
        try {
            return new ImageHashMap(connector.getDefaultIcons());
        } catch (final Exception e) {
            final String message = "cannot get default icons"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Icon getDefaultIcon(final String name) throws ConnectionException {
        try {
            return getDefaultIcons().get(name);
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
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public User getUser(final String userGroupLsName,
            final String userGroupName,
            final String userLsName,
            final String userName,
            final String password,
            final ClientConnectionContext context) throws ConnectionException, UserException {
        try {
            return connector.getUser(userGroupLsName, userGroupName, userLsName, userName, password, context);
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
        return getUserGroupNames(ClientConnectionContext.createDeprecated());
    }

    @Override
    public Vector getUserGroupNames(final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getUserGroupNames(context);
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
        return getUserGroupNames(username, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Vector getUserGroupNames(final String username, final String domain, final ClientConnectionContext context)
            throws ConnectionException, UserException {
        try {
            return connector.getUserGroupNames(username, domain, context);
        } catch (final Exception e) {
            final String message = "could not get usergroup names by username,domain: " + username + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean changePassword(final User user, final String oldPassword, final String newPassword)
            throws ConnectionException, UserException {
        return changePassword(user, oldPassword, newPassword, ClientConnectionContext.createDeprecated());
    }

    @Override
    public boolean changePassword(final User user,
            final String oldPassword,
            final String newPassword,
            final ClientConnectionContext context) throws ConnectionException, UserException {
        try {
            return connector.changePassword(user, oldPassword, newPassword, context);
        } catch (final Exception e) {
            final String message = "could not change password: " + user + " :: " + oldPassword + " :: " + newPassword; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getRoots(final User user) throws ConnectionException {
        return getRoots(user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getRoots(final User user, final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getRoots(user, context);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getRoots(final User user, final String domain) throws ConnectionException {
        return getRoots(user, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getRoots(final User user, final String domain, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getRoots(user, domain, context);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getChildren(final Node node, final User user) throws ConnectionException {
        return getChildren(node, user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getChildren(final Node node, final User user, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getChildren(node, user, context);
        } catch (final Exception e) {
            final String message = "could not get children for node and user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node getNode(final User user, final int nodeID, final String domain) throws ConnectionException {
        return getNode(user, nodeID, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node getNode(final User user, final int nodeID, final String domain, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getMetaObjectNode(user, nodeID, domain, context);
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
        return addNode(node, parent, user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node addNode(final Node node, final Link parent, final User user, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.addNode(node, parent, user, context);
        } catch (final Exception e) {
            final String message = "could not add node with parent and user: " + node + " :: " + parent + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean deleteNode(final Node node, final User user) throws ConnectionException {
        return deleteNode(node, user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public boolean deleteNode(final Node node, final User user, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.deleteNode(node, user, context);
        } catch (final Exception e) {
            final String message = "could not delete node for user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean addLink(final Node from, final Node to, final User user) throws ConnectionException {
        return addLink(from, to, user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public boolean addLink(final Node from, final Node to, final User user, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.addLink(from, to, user, context);
        } catch (final Exception e) {
            final String message = "could not add link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public boolean deleteLink(final Node from, final Node to, final User user) throws ConnectionException {
        return deleteLink(from, to, user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public boolean deleteLink(final Node from, final Node to, final User user, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.deleteLink(from, to, user, context);
        } catch (final Exception e) {
            final String message = "could not delete link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public Node[] getClassTreeNodes(final User user) throws ConnectionException {
        return getClassTreeNodes(user, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getClassTreeNodes(final User user, final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getClassTreeNodes(user, context);
        } catch (final Exception e) {
            final String message = "could not get classtree nodes for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Deprecated
    @Override
    public MetaClass getMetaClass(final User user, final int classID, final String domain) throws ConnectionException {
        return getMetaClass(user, classID, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass getMetaClass(final User user,
            final int classID,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getClass(user, classID, domain, context);
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
        return getClasses(user, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass[] getClasses(final User user, final String domain, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getClasses(user, domain, context);
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
        return getMetaObject(user, objectID, classID, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject getMetaObject(final User user,
            final int objectID,
            final int classID,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getMetaObject(user, objectID, classID, domain, context);
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
            throw new ConnectionException(message, e);
        }
    }

    @Override
    @Deprecated
    public MetaObject[] getMetaObjectByQuery(final User user, final String query) throws ConnectionException {
        return getMetaObjectByQuery(user, query, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user, final String query, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getMetaObject(user, query, context);
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
        return getMetaObjectByQuery(user, query, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user,
            final String query,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getMetaObject(user, query, domain, context);
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
        return insertMetaObject(user, metaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject insertMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.insertMetaObject(user, metaObject, domain, context);
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
        return updateMetaObject(user, metaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public int updateMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.updateMetaObject(user, metaObject, domain, context);
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
        return deleteMetaObject(user, metaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public int deleteMetaObject(final User user,
            final MetaObject metaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.deleteMetaObject(user, metaObject, domain, context);
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
        return getInstance(user, c, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject getInstance(final User user, final MetaClass c, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getInstance(user, c, context);
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
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final String representationPattern,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmos = connector.getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        representationPattern,
                        context);
                return initLWMOs(lwmos, null);
            } else {
                return getLWMOFallback(classId, user);
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
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        context);
                return initLWMOs(lwmo, formater);
            } else {
                return getLWMOFallback(classId, user);
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
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final String representationPattern,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        representationPattern,
                        context);
                return initLWMOs(lwmo, null);
            } else {
                return getLWMOFallback(classId, user);
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
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        context);
                return initLWMOs(lwmo, formater);
            } else {
                return getLWMOFallback(classId, user);
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
        return customServerSearch(user, serverSearch, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Collection customServerSearch(final User user,
            final CidsServerSearch serverSearch,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.customServerSearch(user, serverSearch, context);
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
        while (initialCause.getCause() != null) {
            initialCause = initialCause.getCause();
        }
        return initialCause;
    }

    /**
     * Initializes LWMOs with the appropriate metaservice and string formatter.
     *
     * @param   lwmos     DOCUMENT ME!
     * @param   formater  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaObject[] initLWMOs(final LightweightMetaObject[] lwmos,
            final AbstractAttributeRepresentationFormater formater) {
        if (lwmos != null) {
            final MetaService msServer = (MetaService)connector;
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
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
     * @param   classId  DOCUMENT ME!
     * @param   user     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private MetaObject[] getLWMOFallback(final int classId, final User user) throws ConnectionException {
        final MetaClass mc = ClassCacheMultiple.getMetaClass(user.getDomain(), classId);
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

        return getMetaObjectByQuery(user, query);
    }

    @Deprecated
    @Override
    public String getConfigAttr(final User user, final String key) throws ConnectionException {
        return getConfigAttr(user, key, ClientConnectionContext.createDeprecated());
    }

    @Override
    public String getConfigAttr(final User user, final String key, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.getConfigAttr(user, key, context);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get config attr for user: " + user, e); // NOI18N
        }
    }

    @Deprecated
    @Override
    public boolean hasConfigAttr(final User user, final String key) throws ConnectionException {
        return hasConfigAttr(user, key, ClientConnectionContext.createDeprecated());
    }

    @Override
    public boolean hasConfigAttr(final User user, final String key, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return connector.hasConfigAttr(user, key, context);
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
        return getHistory(classId, objectId, domain, user, elements, ClientConnectionContext.createDeprecated());
    }

    @Override
    public HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return connector.getHistory(classId, objectId, domain, user, elements, context);
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
        return executeTask(user, taskname, taskdomain, ClientConnectionContext.createDeprecated(), body, params);
    }

    @Override
    public Object executeTask(final User user,
            final String taskname,
            final String taskdomain,
            final ClientConnectionContext context,
            final Object body,
            final ServerActionParameter... params) throws ConnectionException {
        try {
            // FIXME: workaround for legacy clients that do not support GenericResourceWithContentType
            final Object taskResult = connector.executeTask(user, taskname, taskdomain, context, body, params);
            if ((taskResult != null) && GenericResourceWithContentType.class.isAssignableFrom(taskResult.getClass())) {
                LOG.warn("REST Action  '" + taskname + "' completed, GenericResourceWithContentType with type '"
                            + ((GenericResourceWithContentType)taskResult).getContentType() + "' generated.");
                return ((GenericResourceWithContentType)taskResult).getRes();
            } else {
                return taskResult;
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
        return (CallServerService)connector;
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(RESTfulConnection.class.getSimpleName());
    }
}
