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
import Sirius.server.localserver.method.MethodMap;
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
import Sirius.server.newuser.UserGroup;
import Sirius.server.search.SearchOption;
import Sirius.server.search.SearchResult;
import Sirius.server.search.store.Info;
import Sirius.server.search.store.QueryData;

import Sirius.util.image.ImageHashMap;

import org.apache.log4j.Logger;

import java.awt.GraphicsEnvironment;

import java.io.File;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Icon;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.actions.ServerActionParameter;
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
    @Override
    public String[] getDomains() throws ConnectionException {
        try {
            return ((MetaService)callserver).getDomains();
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
    @Override
    public User getUser(final String usergroupLocalserver,
            final String usergroup,
            final String userLocalserver,
            final String username,
            final String password) throws ConnectionException, UserException {
        try {
            return ((UserService)callserver).getUser(
                    usergroupLocalserver,
                    usergroup,
                    userLocalserver,
                    username,
                    password);
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

    @Override
    public Vector getUserGroupNames() throws ConnectionException {
        try {
            return ((UserService)callserver).getUserGroupNames();
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the usergroup names", re);
            throw new ConnectionException("[ServerError] could not retrieve the usergroup names: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
        }
    }

    @Override
    public Vector getUserGroupNames(final String username, final String domain) throws ConnectionException,
        UserException {
        try {
            return ((UserService)callserver).getUserGroupNames(username, domain);
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

    @Override
    public boolean changePassword(final User user, final String oldPassword, final String newPassword)
            throws ConnectionException, UserException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("changing user password");
            }
            return ((UserService)callserver).changePassword(user, oldPassword, newPassword);
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
    @Override
    public Node[] getRoots(final User user, final String domain) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).getRoots(user, domain);
        } catch (RemoteException re) {
            LOG.fatal("[ServerError] could not retrieve the top nodes of domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not  retrieve the top nodes of domain '" + domain + "': "
                        + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
        }
    }

    @Override
    public Node[] getRoots(final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).getRoots(user);
        } catch (RemoteException re) {
            LOG.fatal("[CatalogueService] could not retrieve the top nodes", re);
            throw new ConnectionException("[CatalogueService] could not retrieve the top nodes: " + re.getMessage(),
                ConnectionException.FATAL,
                re.getCause());
        }
    }

    @Override
    public Node[] getChildren(final Node node, final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).getChildren(node, user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve children of node '" + node, re);
            throw new ConnectionException("[ServerError] could not retrieve children of node '" + node
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public Node getNode(final User user, final int nodeID, final String domain) throws ConnectionException {
        try {
            return ((MetaService)callserver).getMetaObjectNode(user, nodeID, domain);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain
                        + "': " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public Node addNode(final Node node, final Link parent, final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).addNode(node, parent, user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] addNode() could not add node '" + node + "'", re);
            throw new ConnectionException("[ServerError] addNode() could not add node '" + node + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public boolean deleteNode(final Node node, final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).deleteNode(node, user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] deleteNode() could not delete node '" + node + "'", re);
            throw new ConnectionException("[ServerError] deleteNode() could not delete node '" + node + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public boolean addLink(final Node from, final Node to, final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).addLink(from, to, user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] addLink() could not add Link", re);
            throw new ConnectionException("[ServerError] addLink() could not add Link: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public boolean deleteLink(final Node from, final Node to, final User user) throws ConnectionException {
        try {
            return ((CatalogueService)callserver).deleteLink(from, to, user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] deleteLink() could not delete Link", re);
            throw new ConnectionException("[ServerError] deleteLink() could not delete Link: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public Node[] getClassTreeNodes(final User user) throws ConnectionException {
        try {
            return ((MetaService)callserver).getClassTreeNodes(user);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve the class tree nodes", re);
            throw new ConnectionException("[ServerError] could not retrieve the class tree nodes: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    // Classes and Objects -----------------------------------------------------
    @Override
    public MetaClass getMetaClass(final User user, final int classID, final String domain) throws ConnectionException {
        try {
            return ((MetaService)callserver).getClass(user, classID, domain);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve meta class '" + classID + "' from domain '" + domain + "'",
                re);
            throw new ConnectionException("[ServerError] could not retrieve meta class '" + classID + "' from domain '"
                        + domain + "': " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public MetaClass[] getClasses(final User user, final String domain) throws ConnectionException {
        try {
            return ((MetaService)callserver).getClasses(user, domain);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve the classes from domain '" + domain + "'", re);
            throw new ConnectionException("[ServerError] could not retrieve the classes from domain '" + domain + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user, final String query) throws ConnectionException {
        try {
            return ((MetaService)callserver).getMetaObject(user, query);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve MetaObject", re);
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user, final String query, final String domain)
            throws ConnectionException {
        try {
            return ((MetaService)callserver).getMetaObject(user, query, domain);
        } catch (RemoteException re) {
            LOG.error("[ServerError] could not retrieve MetaObject", re);
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public MetaObject getMetaObject(final User user, final int objectID, final int classID, final String domain)
            throws ConnectionException {
        try {
            return ((MetaService)callserver).getMetaObject(user, objectID, classID, domain);
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

    @Override
    public MetaObject insertMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        try {
            return ((MetaService)callserver).insertMetaObject(user, MetaObject, domain);
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

    @Override
    public int updateMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        try {
            return ((MetaService)callserver).updateMetaObject(user, MetaObject, domain);
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

    @Override
    public int deleteMetaObject(final User user, final MetaObject MetaObject, final String domain)
            throws ConnectionException {
        try {
            return ((MetaService)callserver).deleteMetaObject(user, MetaObject, domain);
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

    @Override
    public MetaObject getInstance(final User user, final MetaClass c) throws ConnectionException {
        try {
            try {
                final MetaObject mo = (MetaObject)CloneHelper.clone(((MetaService)callserver).getInstance(user, c));
                return mo;
            } catch (CloneNotSupportedException ce) {
                LOG.warn("could not clone MetaObject", ce);
                return ((MetaService)callserver).getInstance(user, c);
            }
        } catch (RemoteException re) {
            LOG.error("[ServerError] getInstance(): could not get instance of class '" + c + "'", re);
            throw new ConnectionException("[[ServerError] getInstance(): could not get instance of class '" + c + "': "
                        + re.getMessage(),
                ConnectionException.ERROR,
                re.getCause());
        }
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        representationPattern);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
        }
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
        }
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        representationPattern);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
        }
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService)callserver).getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId,
                ex);
        }
    }

    @Override
    public Collection customServerSearch(final User user, final CidsServerSearch serverSearch)
            throws ConnectionException {
        try {
            return ((SearchService)callserver).customServerSearch(user, serverSearch);
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
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private MetaObject[] getLightweightMetaObjectsFallback(final int classId, final User user)
            throws ConnectionException {
        final MetaClass mc = ClassCacheMultiple.getMetaClass(user.getDomain(), classId);
        final ClassAttribute ca = mc.getClassAttribute("sortingColumn");                                                 // NOI18N
        String orderBy = "";                                                                                             // NOI18N
        if (ca != null) {
            final String value = ca.getValue().toString();
            orderBy = " order by " + value;                                                                              // NOI18N
        }
        final String query = "select " + mc.getID() + "," + mc.getPrimaryKey() + " from " + mc.getTableName() + orderBy; // NOI18N

        return getMetaObjectByQuery(user, query);
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

    @Override
    public String getConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            final UserService service = (UserService)callserver;
            return service.getConfigAttr(user, key);
        } catch (final RemoteException ex) {
            throw new ConnectionException("could not get config attr for user: " + user, ex); // NOI18N
        }
    }

    @Override
    public boolean hasConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            final UserService service = (UserService)callserver;
            return service.hasConfigAttr(user, key);
        } catch (final RemoteException ex) {
            throw new ConnectionException("could not check config attr for user: " + user, ex); // NOI18N
        }
    }

    @Override
    public HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements) throws ConnectionException {
        try {
            final MetaService service = (MetaService)callserver;

            return service.getHistory(classId, objectId, domain, user, elements);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get history: classId: " + classId + " || objectId: " // NOI18N
                        + objectId
                        + " || domain: " + domain + " || user: " + user + " || elements: " + elements, // NOI18N
                e);
        }
    }

    @Override
    public Object executeTask(final User user,
            final String taskname,
            final String taskdomain,
            final Object body,
            final ServerActionParameter... params) throws ConnectionException {
        try {
            return ((ActionService)callserver).executeTask(user, taskname, taskdomain, body, params);
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
