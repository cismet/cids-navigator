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
public class RESTfulConnection implements Connection, Reconnectable<CallServerService> {

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
     *
     * @return  DOCUMENT ME!
     */
    protected Reconnector<CallServerService> createReconnector(final String callserverURL, final Proxy proxy) {
        reconnector = new RESTfulReconnector(CallServerService.class, callserverURL, proxy);
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
        connector = createReconnector(callserverURL, proxy).getProxy();

        try {
            connector.getDomains();
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

    @Override
    public String[] getDomains() throws ConnectionException {
        try {
            return connector.getDomains();
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

    @Override
    public User getUser(final String userGroupLsName,
            final String userGroupName,
            final String userLsName,
            final String userName,
            final String password) throws ConnectionException, UserException {
        try {
            return connector.getUser(userGroupLsName, userGroupName, userLsName, userName, password);
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

    @Override
    public Vector getUserGroupNames() throws ConnectionException {
        try {
            return connector.getUserGroupNames();
        } catch (final Exception e) {
            final String message = "could not get usergroup names"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Vector getUserGroupNames(final String username, final String domain) throws ConnectionException,
        UserException {
        try {
            return connector.getUserGroupNames(username, domain);
        } catch (final Exception e) {
            final String message = "could not get usergroup names by username,domain: " + username + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public boolean changePassword(final User user, final String oldPassword, final String newPassword)
            throws ConnectionException, UserException {
        try {
            return connector.changePassword(user, oldPassword, newPassword);
        } catch (final Exception e) {
            final String message = "could not change password: " + user + " :: " + oldPassword + " :: " + newPassword; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Node[] getRoots(final User user) throws ConnectionException {
        try {
            return connector.getRoots(user);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Node[] getRoots(final User user, final String domain) throws ConnectionException {
        try {
            return connector.getRoots(user, domain);
        } catch (final Exception e) {
            final String message = "could not get roots for user: " + user + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Node[] getChildren(final Node node, final User user) throws ConnectionException {
        try {
            return connector.getChildren(node, user);
        } catch (final Exception e) {
            final String message = "could not get children for node and user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Node getNode(final User user, final int nodeID, final String domain) throws ConnectionException {
        try {
            return connector.getMetaObjectNode(user, nodeID, domain);
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

    @Override
    public Node addNode(final Node node, final Link parent, final User user) throws ConnectionException {
        try {
            return connector.addNode(node, parent, user);
        } catch (final Exception e) {
            final String message = "could not add node with parent and user: " + node + " :: " + parent + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public boolean deleteNode(final Node node, final User user) throws ConnectionException {
        try {
            return connector.deleteNode(node, user);
        } catch (final Exception e) {
            final String message = "could not delete node for user: " + node + " :: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public boolean addLink(final Node from, final Node to, final User user) throws ConnectionException {
        try {
            return connector.addLink(from, to, user);
        } catch (final Exception e) {
            final String message = "could not add link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public boolean deleteLink(final Node from, final Node to, final User user) throws ConnectionException {
        try {
            return connector.deleteLink(from, to, user);
        } catch (final Exception e) {
            final String message = "could not delete link, node from, to, user: " + from + " :: " + to + ":: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public Node[] getClassTreeNodes(final User user) throws ConnectionException {
        try {
            return connector.getClassTreeNodes(user);
        } catch (final Exception e) {
            final String message = "could not get classtree nodes for user: " + user; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public MetaClass getMetaClass(final User user, final int classID, final String domain) throws ConnectionException {
        try {
            return connector.getClass(user, classID, domain);
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

    @Override
    public MetaClass[] getClasses(final User user, final String domain) throws ConnectionException {
        try {
            return connector.getClasses(user, domain);
        } catch (final Exception e) {
            final String message = "could not get classes for user, doamin: " + user + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public MetaObject getMetaObject(final User user, final int objectID, final int classID, final String domain)
            throws ConnectionException {
        try {
            return connector.getMetaObject(user, objectID, classID, domain);
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
    public MetaObject[] getMetaObjectByQuery(final User user, final String query) throws ConnectionException {
        try {
            return connector.getMetaObject(user, query);
        } catch (final Exception e) {
            final String message = "could not get metaobject for user, query: " + user + " :: " + query; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public MetaObject[] getMetaObjectByQuery(final User user, final String query, final String domain)
            throws ConnectionException {
        try {
            return connector.getMetaObject(user, query, domain);
        } catch (final Exception e) {
            final String message = "could not get metaobject for user, query, domain: " + user + " :: " // NOI18N
                        + query
                        + " :: "                                                                        // NOI18N
                        + domain;
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public MetaObject insertMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        try {
            return connector.insertMetaObject(user, metaObject, domain);
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

    @Override
    public int updateMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        try {
            return connector.updateMetaObject(user, metaObject, domain);
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

    @Override
    public int deleteMetaObject(final User user, final MetaObject metaObject, final String domain)
            throws ConnectionException {
        try {
            return connector.deleteMetaObject(user, metaObject, domain);
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

    @Override
    public MetaObject getInstance(final User user, final MetaClass c) throws ConnectionException {
        try {
            return connector.getInstance(user, c);
        } catch (final Exception e) {
            final String message = "could not get instance for user, metaclass: " + user + " :: " + c; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmos = connector.getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields,
                        representationPattern);
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

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(final int classId,
            final User user,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getAllLightweightMetaObjectsForClass(
                        classId,
                        user,
                        representationFields);
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

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final String representationPattern) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields,
                        representationPattern);
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

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(final int classId,
            final User user,
            final String query,
            final String[] representationFields,
            final AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (isLWMOEnabled) {
                final LightweightMetaObject[] lwmo = connector.getLightweightMetaObjectsByQuery(
                        classId,
                        user,
                        query,
                        representationFields);
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

    @Override
    public Collection customServerSearch(final User user, final CidsServerSearch serverSearch)
            throws ConnectionException {
        try {
            return connector.customServerSearch(user, serverSearch);
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

    @Override
    public String getConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            return connector.getConfigAttr(user, key);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get config attr for user: " + user, e); // NOI18N
        }
    }

    @Override
    public boolean hasConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            return connector.hasConfigAttr(user, key);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not check config attr for user: " + user, e); // NOI18N
        }
    }

    @Override
    public HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements) throws ConnectionException {
        try {
            return connector.getHistory(classId, objectId, domain, user, elements);
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
            // FIXME: workaround for legacy clients that do not support GenericResourceWithContentType
            final Object taskResult = connector.executeTask(user, taskname, taskdomain, body, params);
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
}
