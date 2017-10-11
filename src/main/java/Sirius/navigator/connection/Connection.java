/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.AbstractAttributeRepresentationFormater;
import Sirius.server.middleware.types.HistoryObject;
import Sirius.server.middleware.types.Link;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.Node;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;

import Sirius.util.image.ImageHashMap;

import java.util.Collection;
import java.util.Vector;

import javax.swing.Icon;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.connectioncontext.ConnectionContext;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.netutil.Proxy;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public interface Connection {

    //~ Methods ----------------------------------------------------------------

    // Connection --------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    boolean connect(String callserverURL) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean connect(String callserverURL, final boolean compressionEnabled) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL  DOCUMENT ME!
     * @param   proxy          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    boolean connect(String callserverURL, Proxy proxy) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   proxy               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean connect(String callserverURL, Proxy proxy, final boolean compressionEnabled) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean reconnect() throws ConnectionException;

    /**
     * DOCUMENT ME!
     */
    void disconnect();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isConnected();
    /**
     * Default -----------------------------------------------------------------
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    String[] getDomains() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    ImageHashMap getDefaultIcons() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       name  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    Icon getDefaultIcon(String name) throws ConnectionException;
    /**
     * User --------------------------------------------------------------------
     *
     * @param   userGroupLsName  DOCUMENT ME!
     * @param   userGroupName    DOCUMENT ME!
     * @param   userLsName       DOCUMENT ME!
     * @param   userName         DOCUMENT ME!
     * @param   password         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    User getUser(String userGroupLsName, String userGroupName, String userLsName, String userName, String password)
            throws ConnectionException, UserException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Vector getUserGroupNames() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   username  DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    Vector getUserGroupNames(String username, String domain) throws ConnectionException, UserException;

    /**
     * DOCUMENT ME!
     *
     * @param   user         DOCUMENT ME!
     * @param   oldPassword  DOCUMENT ME!
     * @param   newPassword  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    boolean changePassword(User user, String oldPassword, String newPassword) throws ConnectionException, UserException;
    /**
     * Node --------------------------------------------------------------------
     *
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots(User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user    DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots(User user, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getChildren(Node node, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user    DOCUMENT ME!
     * @param   nodeID  DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node getNode(User user, int nodeID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node    DOCUMENT ME!
     * @param   parent  DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node addNode(Node node, Link parent, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteNode(Node node, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     * @param   to    DOCUMENT ME!
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean addLink(Node from, Node to, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     * @param   to    DOCUMENT ME!
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteLink(Node from, Node to, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       user  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    Node[] getClassTreeNodes(User user) throws ConnectionException;
    /**
     * Classes & Objects -------------------------------------------------------
     *
     * @param   user     DOCUMENT ME!
     * @param   classID  DOCUMENT ME!
     * @param   domain   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass getMetaClass(User user, int classID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user    DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass[] getClasses(User user, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user      DOCUMENT ME!
     * @param   objectID  DOCUMENT ME!
     * @param   classID   DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaObject getMetaObject(User user, int objectID, int classID, String domain) throws ConnectionException;
    /**
     * DOCUMENT ME!
     *
     * @param   user      DOCUMENT ME!
     * @param   objectID  DOCUMENT ME!
     * @param   classID   DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     * @param   context   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getMetaObject(User user, int objectID, int classID, String domain, ConnectionContext context)
            throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user   DOCUMENT ME!
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       user     DOCUMENT ME!
     * @param       query    DOCUMENT ME!
     * @param       context  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @Deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query, ConnectionContext context) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user    DOCUMENT ME!
     * @param   query   DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query, String domain) throws ConnectionException;
    /**
     * DOCUMENT ME!
     *
     * @param       user     DOCUMENT ME!
     * @param       query    DOCUMENT ME!
     * @param       domain   DOCUMENT ME!
     * @param       context  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @Deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query, String domain, ConnectionContext context)
            throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user        DOCUMENT ME!
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject insertMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user        DOCUMENT ME!
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int updateMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user        DOCUMENT ME!
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int deleteMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user  DOCUMENT ME!
     * @param   c     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getInstance(User user, MetaClass c) throws ConnectionException;
    /**
     * /** DOCUMENT ME!
     *
     * @param   classId                DOCUMENT ME!
     * @param   user                   DOCUMENT ME!
     * @param   representationFields   DOCUMENT ME!
     * @param   representationPattern  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            String representationPattern) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classId               DOCUMENT ME!
     * @param   user                  DOCUMENT ME!
     * @param   representationFields  DOCUMENT ME!
     * @param   formater              DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            AbstractAttributeRepresentationFormater formater) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       classId                DOCUMENT ME!
     * @param       user                   DOCUMENT ME!
     * @param       query                  DOCUMENT ME!
     * @param       representationFields   DOCUMENT ME!
     * @param       representationPattern  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @Deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getLightweightMetaObjectsByQuery(int classId,
            User user,
            String query,
            String[] representationFields,
            String representationPattern) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       classId               DOCUMENT ME!
     * @param       user                  DOCUMENT ME!
     * @param       query                 DOCUMENT ME!
     * @param       representationFields  DOCUMENT ME!
     * @param       formater              DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getLightweightMetaObjectsByQuery(int classId,
            User user,
            String query,
            String[] representationFields,
            AbstractAttributeRepresentationFormater formater) throws ConnectionException;

    /**
     * @see  Sirius.server.middleware.interfaces.proxy.UserService#getConfigAttr(Sirius.server.newuser.User, java.lang.String)
     */
    String getConfigAttr(final User user, final String key) throws ConnectionException;

    /**
     * @see  Sirius.server.middleware.interfaces.proxy.UserService#hasConfigAttr(Sirius.server.newuser.User, java.lang.String)
     */
    boolean hasConfigAttr(final User user, final String key) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       classId   DOCUMENT ME!
     * @param       objectId  DOCUMENT ME!
     * @param       domain    DOCUMENT ME!
     * @param       user      DOCUMENT ME!
     * @param       elements  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @see         Sirius.server.middleware.interfaces.proxy.MetaService#getHistory(int, int, java.lang.String,
     *              Sirius.server.newuser.User, int)
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    HistoryObject[] getHistory(final int classId,
            final int objectId,
            final String domain,
            final User user,
            final int elements) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user          DOCUMENT ME!
     * @param   serverSearch  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Collection customServerSearch(User user, CidsServerSearch serverSearch) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user        DOCUMENT ME!
     * @param   taskname    DOCUMENT ME!
     * @param   taskdomain  DOCUMENT ME!
     * @param   body        json DOCUMENT ME!
     * @param   params      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Object executeTask(User user, String taskname, String taskdomain, Object body, ServerActionParameter... params)
            throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user        DOCUMENT ME!
     * @param   taskname    DOCUMENT ME!
     * @param   taskdomain  DOCUMENT ME!
     * @param   context     DOCUMENT ME!
     * @param   body        DOCUMENT ME!
     * @param   params      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Object executeTask(User user,
            String taskname,
            String taskdomain,
            final ConnectionContext context,
            Object body,
            ServerActionParameter... params) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    CallServerService getCallServerService();
}
