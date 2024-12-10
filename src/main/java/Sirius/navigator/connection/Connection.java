/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;

import Sirius.server.localserver.user.PasswordCheckException;
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
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.connectioncontext.ConnectionContext;

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
    @Deprecated
    boolean connect(String callserverURL, boolean compressionEnabled) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   connectionContext   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean connect(String callserverURL, boolean compressionEnabled, ConnectionContext connectionContext)
            throws ConnectionException;

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
    @Deprecated
    boolean connect(String callserverURL, Proxy proxy, boolean compressionEnabled) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   proxy               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   connectionContext   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean connect(String callserverURL,
            Proxy proxy,
            boolean compressionEnabled,
            ConnectionContext connectionContext) throws ConnectionException;

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
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    String[] getDomains() throws ConnectionException;

    /**
     * Default -----------------------------------------------------------------
     *
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    String[] getDomains(ConnectionContext connectionContext) throws ConnectionException;

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
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    ImageHashMap getDefaultIcons(ConnectionContext connectionContext) throws ConnectionException;

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
     * DOCUMENT ME!
     *
     * @param   name               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Icon getDefaultIcon(String name, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
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
    @Deprecated
    User getUser(String userGroupLsName, String userGroupName, String userLsName, String userName, String password)
            throws ConnectionException, UserException;

    /**
     * User --------------------------------------------------------------------
     *
     * @param   userGroupLsName    DOCUMENT ME!
     * @param   userGroupName      DOCUMENT ME!
     * @param   userLsName         DOCUMENT ME!
     * @param   userName           DOCUMENT ME!
     * @param   password           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    User getUser(String userGroupLsName,
            String userGroupName,
            String userLsName,
            String userName,
            String password,
            ConnectionContext connectionContext) throws ConnectionException, UserException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Vector getUserGroupNames() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Vector getUserGroupNames(ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Vector getUserGroupNames(String username, String domain) throws ConnectionException, UserException;

    /**
     * DOCUMENT ME!
     *
     * @param   username           DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    Vector getUserGroupNames(String username, String domain, ConnectionContext connectionContext)
            throws ConnectionException, UserException;

    /**
     * DOCUMENT ME!
     *
     * @param   user         DOCUMENT ME!
     * @param   oldPassword  DOCUMENT ME!
     * @param   newPassword  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException     DOCUMENT ME!
     * @throws  UserException           DOCUMENT ME!
     * @throws  PasswordCheckException  DOCUMENT ME!
     */
    @Deprecated
    boolean changePassword(User user, String oldPassword, String newPassword) throws ConnectionException,
        UserException,
        PasswordCheckException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   oldPassword        DOCUMENT ME!
     * @param   newPassword        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException     DOCUMENT ME!
     * @throws  UserException           DOCUMENT ME!
     * @throws  PasswordCheckException  DOCUMENT ME!
     */
    boolean changePassword(User user, String oldPassword, String newPassword, ConnectionContext connectionContext)
            throws ConnectionException, UserException, PasswordCheckException;

    /**
     * DOCUMENT ME!
     *
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Node[] getRoots(User user) throws ConnectionException;

    /**
     * Node --------------------------------------------------------------------
     *
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots(User user, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Node[] getRoots(User user, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots(User user, String domain, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Node[] getChildren(Node node, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node               DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getChildren(Node node, User user, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Node getNode(User user, int nodeID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   nodeID             DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node getNode(User user, int nodeID, String domain, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Node addNode(Node node, Link parent, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node               DOCUMENT ME!
     * @param   parent             DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node addNode(Node node, Link parent, User user, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    boolean deleteNode(Node node, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node               DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteNode(Node node, User user, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    boolean addLink(Node from, Node to, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from               DOCUMENT ME!
     * @param   to                 DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean addLink(Node from, Node to, User user, ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    boolean deleteLink(Node from, Node to, User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from               DOCUMENT ME!
     * @param   to                 DOCUMENT ME!
     * @param   user               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteLink(Node from, Node to, User user, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Node[] getClassTreeNodes(User user) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       user               DOCUMENT ME!
     * @param       connectionContext  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    Node[] getClassTreeNodes(User user, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user     DOCUMENT ME!
     * @param   classID  DOCUMENT ME!
     * @param   domain   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaClass getMetaClass(User user, int classID, String domain) throws ConnectionException;

    /**
     * Classes & Objects -------------------------------------------------------
     *
     * @param   user               DOCUMENT ME!
     * @param   classID            DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass getMetaClass(User user, int classID, String domain, ConnectionContext connectionContext)
            throws ConnectionException;

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
    @Deprecated
    MetaClass[] getClasses(User user, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass[] getClasses(User user, String domain, ConnectionContext connectionContext) throws ConnectionException;

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
     * @param   user               DOCUMENT ME!
     * @param   objectID           DOCUMENT ME!
     * @param   classID            DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getMetaObject(User user,
            int objectID,
            int classID,
            String domain,
            ConnectionContext connectionContext) throws ConnectionException;

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
     * @param       user               DOCUMENT ME!
     * @param       query              DOCUMENT ME!
     * @param       connectionContext  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @Deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query, ConnectionContext connectionContext)
            throws ConnectionException;

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
     * @param       user               DOCUMENT ME!
     * @param       query              DOCUMENT ME!
     * @param       domain             DOCUMENT ME!
     * @param       connectionContext  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @throws      ConnectionException  DOCUMENT ME!
     *
     * @Deprecated  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getMetaObjectByQuery(User user, String query, String domain, ConnectionContext connectionContext)
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
    @Deprecated
    MetaObject insertMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   MetaObject         DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject insertMetaObject(User user,
            MetaObject MetaObject,
            String domain,
            ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    int updateMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   MetaObject         DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int updateMetaObject(User user, MetaObject MetaObject, String domain, ConnectionContext connectionContext)
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
    @Deprecated
    int deleteMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   MetaObject         DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int deleteMetaObject(User user, MetaObject MetaObject, String domain, ConnectionContext connectionContext)
            throws ConnectionException;

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
    @Deprecated
    MetaObject getInstance(User user, MetaClass c) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   c                  DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getInstance(User user, MetaClass c, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
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
    @Deprecated
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            String representationPattern) throws ConnectionException;
    /**
     * /** DOCUMENT ME!
     *
     * @param   classId                DOCUMENT ME!
     * @param   user                   DOCUMENT ME!
     * @param   representationFields   DOCUMENT ME!
     * @param   representationPattern  DOCUMENT ME!
     * @param   connectionContext      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            String representationPattern,
            ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            AbstractAttributeRepresentationFormater formater) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classId               DOCUMENT ME!
     * @param   user                  DOCUMENT ME!
     * @param   representationFields  DOCUMENT ME!
     * @param   formater              DOCUMENT ME!
     * @param   connectionContext     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getAllLightweightMetaObjectsForClass(int classId,
            User user,
            String[] representationFields,
            AbstractAttributeRepresentationFormater formater,
            ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classId                DOCUMENT ME!
     * @param   user                   DOCUMENT ME!
     * @param   query                  DOCUMENT ME!
     * @param   representationFields   DOCUMENT ME!
     * @param   representationPattern  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
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
     * @param       classId                DOCUMENT ME!
     * @param       user                   DOCUMENT ME!
     * @param       query                  DOCUMENT ME!
     * @param       representationFields   DOCUMENT ME!
     * @param       representationPattern  DOCUMENT ME!
     * @param       connectionContext      DOCUMENT ME!
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
            String representationPattern,
            ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classId               DOCUMENT ME!
     * @param   user                  DOCUMENT ME!
     * @param   query                 DOCUMENT ME!
     * @param   representationFields  DOCUMENT ME!
     * @param   formater              DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaObject[] getLightweightMetaObjectsByQuery(int classId,
            User user,
            String query,
            String[] representationFields,
            AbstractAttributeRepresentationFormater formater) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       classId               DOCUMENT ME!
     * @param       user                  DOCUMENT ME!
     * @param       query                 DOCUMENT ME!
     * @param       representationFields  DOCUMENT ME!
     * @param       formater              DOCUMENT ME!
     * @param       connectionContext     DOCUMENT ME!
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
            AbstractAttributeRepresentationFormater formater,
            ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user  DOCUMENT ME!
     * @param   key   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    String getConfigAttr(User user, String key) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   key                DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     *
     * @see     Sirius.server.middleware.interfaces.proxy.UserService#getConfigAttr(Sirius.server.newuser.User, java.lang.String)
     */
    String getConfigAttr(User user, String key, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user  DOCUMENT ME!
     * @param   key   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    boolean hasConfigAttr(User user, String key) throws ConnectionException;

    /**
     * @see  Sirius.server.middleware.interfaces.proxy.UserService#hasConfigAttr(Sirius.server.newuser.User, java.lang.String)
     */
    boolean hasConfigAttr(User user, String key, ConnectionContext connectionContext) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classId   DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   elements  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    HistoryObject[] getHistory(int classId,
            int objectId,
            String domain,
            User user,
            int elements) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param       classId            DOCUMENT ME!
     * @param       objectId           DOCUMENT ME!
     * @param       domain             DOCUMENT ME!
     * @param       user               DOCUMENT ME!
     * @param       elements           DOCUMENT ME!
     * @param       connectionContext  DOCUMENT ME!
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
    HistoryObject[] getHistory(int classId,
            int objectId,
            String domain,
            User user,
            int elements,
            ConnectionContext connectionContext) throws ConnectionException;

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
    @Deprecated
    Collection customServerSearch(User user, CidsServerSearch serverSearch) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user               DOCUMENT ME!
     * @param   serverSearch       DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Collection customServerSearch(User user, CidsServerSearch serverSearch, ConnectionContext connectionContext)
            throws ConnectionException;

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
     * @param   user               DOCUMENT ME!
     * @param   taskname           DOCUMENT ME!
     * @param   taskdomain         DOCUMENT ME!
     * @param   body               DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     * @param   params             DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Object executeTask(User user,
            String taskname,
            String taskdomain,
            Object body,
            ConnectionContext connectionContext,
            ServerActionParameter... params) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   user                            DOCUMENT ME!
     * @param   taskname                        DOCUMENT ME!
     * @param   taskdomain                      DOCUMENT ME!
     * @param   body                            DOCUMENT ME!
     * @param   connectionContext               DOCUMENT ME!
     * @param   resolvePreparedAsyncByteAction  DOCUMENT ME!
     * @param   params                          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Object executeTask(User user,
            String taskname,
            String taskdomain,
            Object body,
            ConnectionContext connectionContext,
            boolean resolvePreparedAsyncByteAction,
            ServerActionParameter... params) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    CallServerService getCallServerService();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getConnectionName();

    /**
     * DOCUMENT ME!
     *
     * @param  connectionName  DOCUMENT ME!
     */
    void setConnectionName(String connectionName);
}
