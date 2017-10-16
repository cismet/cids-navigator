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
import Sirius.navigator.tools.CloneHelper;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.middleware.interfaces.proxy.*;
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
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

import de.cismet.reconnector.rmi.RmiReconnector;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public final class RMIConnection implements Connection, Reconnectable<CallServerService> {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(RMIConnection.class);
    private static final boolean IS_LEIGHTWEIGHT_MO_CODE_ENABLED;
    private static final String DISABLE_MO_FILENAME = "cids_disable_lwmo"; // NOI18N

    static {
        final String uHome = System.getProperty("user.home"); // NOI18N
        if (uHome != null) {
            final File homeDir = new File(uHome);
            final File disableIndicator = new File(homeDir, DISABLE_MO_FILENAME);
            IS_LEIGHTWEIGHT_MO_CODE_ENABLED = !disableIndicator.isFile();
            if (!IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                LOG.warn("LIGHTWIGHTMETAOBJECT CODE IS DISABLED! FOUND FILE: " + disableIndicator);
            }
        } else {
            IS_LEIGHTWEIGHT_MO_CODE_ENABLED = true;
        }
    }

    //~ Instance fields --------------------------------------------------------

    protected String callserverURL = null;
    protected boolean connected = false;
    protected java.lang.Object callserver;
    protected Reconnector<CallServerService> reconnector;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Reconnector<CallServerService> getReconnector() {
        return reconnector;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Reconnector<CallServerService> createReconnector(final String callserverURL) {
        reconnector = new RmiReconnector<CallServerService>(CallServerService.class, callserverURL);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    public boolean connect(final String callserverURL) throws ConnectionException {
        return connect(callserverURL, false);
    }

    @Override
    public boolean connect(final String callserverURL, final boolean compressionEnabled) throws ConnectionException {
        this.callserverURL = null;
        this.connected = false;

        LOG.info("creating network connection to callserver '" + callserverURL + "'");
        callserver = createReconnector(callserverURL).getProxy();

        if (LOG.isDebugEnabled()) {
            final StringBuffer buffer = new StringBuffer("remote interfaces of '").append(callserver.getClass()
                            .getName())
                        .append("': ");
            final Class[] interfaces = callserver.getClass().getInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                buffer.append('\n');
                buffer.append(interfaces[i].getName());
            }

            LOG.debug(buffer);
        }

        this.callserverURL = callserverURL;
        this.connected = true;

        return this.connected;
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        return connect(callserverURL, proxy, false);
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy, final boolean compressionEnabled)
            throws ConnectionException {
        if (proxy != null) {
            LOG.warn("RMI over proxy not supported yet"); // NOI18N
        }

        return connect(callserverURL);
    }

    @Override
    public boolean reconnect() throws ConnectionException {
        if (callserverURL != null) {
            return connect(callserverURL);
        } else {
            LOG.error("can't reconnect - no connection informations from previous connection found");
            throw new ConnectionException(
                "can't reconnect - no connection informations from previous connection found",
                ConnectionException.ERROR);
        }
    }

    @Override
    public void disconnect() {
        this.connected = false;
        this.callserverURL = null;
        this.callserver = null;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    // Default -----------------------------------------------------------------

    @Deprecated
    @Override
    public String[] getDomains() throws ConnectionException {
        return getDomains(ClientConnectionContext.createDeprecated());
    }

    @Override
    public String[] getDomains(final ClientConnectionContext context) throws ConnectionException {
        try {
            return ((MetaService)callserver).getDomains(context);
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the local server names", re);
            throw new ConnectionException("[ServerError] could not retrieve the local server names: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
        }
    }

    @Override
    public ImageHashMap getDefaultIcons() throws ConnectionException {
        try {
            return new ImageHashMap(((SystemService)callserver).getDefaultIcons());
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the default icons", re);
            throw new ConnectionException("[ServerError] could not retrieve the default icons: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
        }
    }

    @Override
    public Icon getDefaultIcon(final String name) throws ConnectionException {
        try {
            // proxy should implement caching here
            return this.getDefaultIcons().get(name);
        } catch (ConnectionException ce) {
            LOG.fatal("[ServerError] could not retrieve the default icon for '" + name + "'");
            throw ce;
        }
    }

    // User ---------------------------------------------------------

    @Deprecated
    @Override
    public User getUser(final String usergroupLocalserver,
            final String usergroup,
            final String userLocalserver,
            final String username,
            final String password) throws ConnectionException, UserException {
        return getUser(
                usergroupLocalserver,
                usergroup,
                userLocalserver,
                username,
                password,
                ClientConnectionContext.createDeprecated());
    }

    @Override
    public User getUser(final String usergroupLocalserver,
            final String usergroup,
            final String userLocalserver,
            final String username,
            final String password,
            final ClientConnectionContext context) throws ConnectionException, UserException {
        try {
            return ((UserService)callserver).getUser(
                    usergroupLocalserver,
                    usergroup,
                    userLocalserver,
                    username,
                    password,
                    context);
        } catch (UserException ue) {
            LOG.warn("can't login: wrong user informations", ue);
            throw ue;
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] can't login", re);
            throw new ConnectionException("[ServerError] can't login: " + re.getMessage(),
                ConnectionException.FATAL,
                re);
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
            return ((UserService)callserver).getUserGroupNames(context);
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the usergroup names", re);
            throw new ConnectionException("[ServerError] could not retrieve the usergroup names: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
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
            return ((UserService)callserver).getUserGroupNames(username, domain, context);
        } catch (RemoteException re) {
            if (re.getMessage().indexOf("UserGroupException") != -1) {
                LOG.warn("[ServerError] could not retrieve the usergroup names for user '" + username + "'", re);
                throw new UserException(re.getMessage());
            } else {
                LOG.fatal("[ServerError] could not retrieve the usergroup names for user '" + username
                            + "' on localserver '" + domain + "'",
                    re);
                throw new ConnectionException("[ServerError] could not retrieve the usergroup names for user '"
                            + username + "' on localserver '" + domain + "': " + re.getMessage(),
                    ConnectionException.FATAL,
                    re.getCause());
            }
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("changing user password");
            }
            return ((UserService)callserver).changePassword(user, oldPassword, newPassword, context);
        } catch (UserException ue) {
            LOG.warn("could not change password");
            throw ue;
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not change user password", re);
            throw new ConnectionException("[ServerError] could not change user password: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    // Node ---------------------------------------------------------

    @Deprecated
    @Override
    public Node[] getRoots(final User user, final String domain) throws ConnectionException {
        return getRoots(user, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public Node[] getRoots(final User user, final String domain, final ClientConnectionContext context)
            throws ConnectionException {
        try {
            return ((CatalogueService)callserver).getRoots(user, domain, context);
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the top nodes of domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not  retrieve the top nodes of domain '" + domain + "': "
                        + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
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
            return ((CatalogueService)callserver).getRoots(user, context);
        } catch (RemoteException re) {
            LOG.fatal("[CatalogueService] could not retrieve the top nodes", re);
            throw new ConnectionException("[CatalogueService] could not retrieve the top nodes: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
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
            return ((CatalogueService)callserver).getChildren(node, user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve children of node '" + node, re);
            throw new ConnectionException("[ServerError] could not retrieve children of node '" + node
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((MetaService)callserver).getMetaObjectNode(user, nodeID, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain
                        + "': " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((CatalogueService)callserver).addNode(node, parent, user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] addNode() could not add node '" + node + "'", re);
            throw new ConnectionException("[ServerError] addNode() could not add node '" + node + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((CatalogueService)callserver).deleteNode(node, user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] deleteNode() could not delete node '" + node + "'", re);
            throw new ConnectionException("[ServerError] deleteNode() could not delete node '" + node + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((CatalogueService)callserver).addLink(from, to, user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] addLink() could not add Link", re);
            throw new ConnectionException("[ServerError] addLink() could not add Link: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((CatalogueService)callserver).deleteLink(from, to, user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] deleteLink() could not delete Link", re);
            throw new ConnectionException("[ServerError] deleteLink() could not delete Link: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((MetaService)callserver).getClassTreeNodes(user, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve the class tree nodes", re);
            throw new ConnectionException("[ServerError] could not retrieve the class tree nodes: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    // Classes and Objects -----------------------------------------------------

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
            return ((MetaService)callserver).getClass(user, classID, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve meta class '" + classID + "' from domain '" + domain + "'",
                re);
            throw new ConnectionException("[ServerError] could not retrieve meta class '" + classID + "' from domain '"
                        + domain + "': " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((MetaService)callserver).getClasses(user, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve the classes from domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not retrieve the classes from domain '" + domain + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((MetaService)callserver).getMetaObject(user, query, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve MetaObject", re);
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    @Deprecated
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
            return ((MetaService)callserver).getMetaObject(user, query, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve MetaObject", re);
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            return ((MetaService)callserver).getMetaObject(user, objectID, classID, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve MetaObject '" + objectID + '@' + classID + '@' + domain
                        + '\'',
                re);
            throw new ConnectionException("[ServerError] could not retrieve the classes from domain '" + domain + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Deprecated
    @Override
    public MetaObject insertMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        return insertMetaObject(user, MetaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public MetaObject insertMetaObject(final User user,
            final MetaObject MetaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return ((MetaService)callserver).insertMetaObject(user, MetaObject, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not insert MetaObject '" + MetaObject + "'", re);
            final Throwable initialCause = getTopInitialCause(re);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException("[[ServerError] could not insert MetaObject '" + MetaObject + "': "
                            + re.getMessage(),
                    ConnectionException.ERROR,
                    re.getCause());
            }
        }
    }

    @Deprecated
    @Override
    public int updateMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        return updateMetaObject(user, MetaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public int updateMetaObject(final User user,
            final MetaObject MetaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return ((MetaService)callserver).updateMetaObject(user, MetaObject, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not update MetaObject '" + MetaObject + "'", re);
            final Throwable initialCause = getTopInitialCause(re);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException("[[ServerError] could not update MetaObject '" + MetaObject + "': "
                            + re.getMessage(),
                    ConnectionException.ERROR,
                    re.getCause());
            }
        }
    }

    @Deprecated
    @Override
    public int deleteMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        return deleteMetaObject(user, MetaObject, domain, ClientConnectionContext.createDeprecated());
    }

    @Override
    public int deleteMetaObject(final User user,
            final MetaObject MetaObject,
            final String domain,
            final ClientConnectionContext context) throws ConnectionException {
        try {
            return ((MetaService)callserver).deleteMetaObject(user, MetaObject, domain, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] deleteMetaObject(): could not delete MetaObject '" + MetaObject + "'", re);
            /*
             *if the top level cause was an SQL Exception, we throw an instance of SqlConnectionException which are
             * visualised with a custom error dialog by the MethodManager
             */
            final Throwable initialCause = getTopInitialCause(re);
            if (initialCause instanceof SQLException) {
                throw new SqlConnectionException(initialCause.getMessage(),
                    initialCause);
            } else {
                throw new ConnectionException("[[ServerError] deleteMetaObject(): could not delete MetaObject '"
                            + MetaObject + "': " + re.getMessage(),
                    ConnectionException.ERROR,
                    re.getCause());
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
            try {
                final MetaObject mo = (MetaObject)CloneHelper.clone(((MetaService)callserver).getInstance(
                            user,
                            c,
                            context));
                return mo;
            } catch (CloneNotSupportedException ce) {
                LOG.warn("could not clone MetaObject", ce);
                return ((MetaService)callserver).getInstance(user, c, context);
            }
        } catch (RemoteException re) {
            LOG.error("[ServerError] getInstance(): could not get instance of class '" + c + "'", re);
            throw new ConnectionException("[[ServerError] getInstance(): could not get instance of class '" + c + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        representationPattern,
                        context);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user, context);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
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
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        context);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user, context);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
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
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        representationPattern,
                        context);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user, context);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
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
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        context);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user, context);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
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
            return ((SearchService)callserver).customServerSearch(user, serverSearch, context);
        } catch (RemoteException re) {
            LOG.error("[ServerError] error during custom search ", re);
            throw new ConnectionException("[ServerError] [ServerError] error during custom search "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
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
     * !For debugging purpose only. Do not use!
     *
     * @param   classId  DOCUMENT ME!
     * @param   user     DOCUMENT ME!
     * @param   context  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private MetaObject[] getLightweightMetaObjectsFallback(final int classId,
            final User user,
            final ClientConnectionContext context) throws ConnectionException {
        final MetaClass mc = ClassCacheMultiple.getMetaClass(user.getDomain(), classId);
        final ClassAttribute ca = mc.getClassAttribute("sortingColumn");                                                 // NOI18N
        String orderBy = "";                                                                                             // NOI18N
        if (ca != null) {
            final String value = ca.getValue().toString();
            orderBy = " order by " + value;                                                                              // NOI18N
        }
        final String query = "select " + mc.getID() + "," + mc.getPrimaryKey() + " from " + mc.getTableName() + orderBy; // NOI18N

        return getMetaObjectByQuery(user, query, context);
    }

    /**
     * Initializes LWMOs with the appropriate metaservice.
     *
     * @param   lwmos  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaObject[] initLightweightMetaObjectsWithMetaService(final LightweightMetaObject[] lwmos) {
        if (lwmos != null) {
            final MetaService msServer = (MetaService)callserver;
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
                }
            }
        }
        return lwmos;
    }

    /**
     * Initializes LWMOs with the appropriate metaservice and string formatter.
     *
     * @param   lwmos     DOCUMENT ME!
     * @param   formater  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaObject[] initLightweightMetaObjectsWithMetaServiceAndFormater(final LightweightMetaObject[] lwmos,
            final AbstractAttributeRepresentationFormater formater) {
        if (lwmos != null) {
            final MetaService msServer = (MetaService)callserver;
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
                    lwmo.setFormater(formater);
                }
            }
        }
        return lwmos;
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
            final UserService service = (UserService)callserver;
            return service.getConfigAttr(user, key, context);
        } catch (final RemoteException ex) {
            throw new ConnectionException("could not get config attr for user: " + user, ex); // NOI18N
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
            final UserService service = (UserService)callserver;
            return service.hasConfigAttr(user, key, context);
        } catch (final RemoteException ex) {
            throw new ConnectionException("could not check config attr for user: " + user, ex); // NOI18N
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
            final MetaService service = (MetaService)callserver;

            return service.getHistory(classId, objectId, domain, user, elements, context);
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
            return ((ActionService)callserver).executeTask(user, taskname, taskdomain, context, body, params);
        } catch (final RemoteException e) {
            throw new ConnectionException("could executeTask: taskname: " + taskname + " || taskdomain: " + taskdomain
                        + " || user: " + user,
                e);
        }
    }

    @Override
    public CallServerService getCallServerService() {
        return (CallServerService)callserver;
    }
}
