package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.tools.CloneHelper;
/*******************************************************************************
 *
 * Copyright (c)    :    EIG (Environmental Informatics Group)
 * http://www.enviromatics.net
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTW
 * University of Applied Sciences
 * Goebenstr. 40
 * 66117 Saarbruecken, Germany
 *
 * Programmers    :    Pascal <pascal@enviromatics.net>
 *
 * Project        :    Sirius
 * Version        :    1.0
 * Purpose        :
 * Created        :    12/20/2002
 * History        :
 *
 *******************************************************************************/
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Icon;

import org.apache.log4j.Logger;

//import Sirius.Translation.*;
import Sirius.util.image.ImageHashMap;
import Sirius.server.localserver.method.MethodMap;
import Sirius.server.middleware.interfaces.proxy.*;
import Sirius.server.middleware.types.*;
import Sirius.server.search.SearchOption;
import Sirius.server.search.SearchResult;
import Sirius.server.search.store.Info;
import Sirius.server.search.store.QueryData;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;
import Sirius.server.newuser.UserGroup;
import Sirius.server.dataretrieval.*;
import Sirius.server.localserver.attribute.ClassAttribute;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import java.io.File;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public final class RMIConnection implements Connection {
    // log4j

    protected static final Logger logger = Logger.getLogger(RMIConnection.class);
    protected String callserverURL = null;
    protected boolean connected = false;
    protected java.lang.Object callserver;
    private static final boolean IS_LEIGHTWEIGHT_MO_CODE_ENABLED;
    private static final String DISABLE_MO_FILENAME = "cids_disable_lwmo";  // NOI18N

    static {
        final String uHome = System.getProperty("user.home");  // NOI18N
        if (uHome != null) {
            final File homeDir = new File(uHome);
            final File disableIndicator = new File(homeDir, DISABLE_MO_FILENAME);
            IS_LEIGHTWEIGHT_MO_CODE_ENABLED = !disableIndicator.isFile();
            if (!IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                logger.warn("LIGHTWIGHTMETAOBJECT CODE IS DISABLED! FOUND FILE: " + disableIndicator);  // NOI18N
            }
        } else {
            IS_LEIGHTWEIGHT_MO_CODE_ENABLED = true;
        }

    }

    public boolean connect(String callserverURL) throws ConnectionException {
        this.callserverURL = null;
        this.connected = false;

        try {
            if(logger.isInfoEnabled())
                logger.info("creating network connection to callserver '" + callserverURL + "'");  // NOI18N
            callserver = Naming.lookup(callserverURL);
        } catch (MalformedURLException mue) {
            logger.fatal("'" + callserverURL + "' is not a valid URL", mue);  // NOI18N
            throw new ConnectionException("'" + callserverURL + "' is not a valid URL", mue);  // NOI18N
        } catch (NotBoundException nbe) {
            logger.fatal("[NetworkError] could not connect to '" + callserverURL + "'", nbe);  // NOI18N
            throw new ConnectionException("[NetworkError] could not connect to '" + callserverURL + "'", nbe);  // NOI18N
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not connect to '" + callserverURL + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not connect to '" + callserverURL + "'", re);  // NOI18N
        }

        if (logger.isDebugEnabled()) {
            StringBuffer buffer = new StringBuffer("remote interfaces of '").append(callserver.getClass().getName()).append("': ");  // NOI18N
            Class[] interfaces = callserver.getClass().getInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                buffer.append('\n');
                buffer.append(interfaces[i].getName());
            }

            logger.debug(buffer);
        }

        this.callserverURL = callserverURL;
        this.connected = true;

        return this.connected;
    }

    public boolean connect(String callserverURL, String username, String password) throws ConnectionException {
        return connect(callserverURL);
    }

    public boolean reconnect() throws ConnectionException {
        if (callserverURL != null) {
            return connect(callserverURL);
        } else {
            logger.error("can't reconnect - no connection informations from previous connection found");  // NOI18N
            throw new ConnectionException("can't reconnect - no connection informations from previous connection found", ConnectionException.ERROR);  // NOI18N
        }
    }

    public void disconnect() {
        this.connected = false;
        this.callserverURL = null;
        this.callserver = null;
    }

    public boolean isConnected() {
        return this.connected;
    }

    // #########################################################################
    // Default -----------------------------------------------------------------
    public String[] getDomains() throws ConnectionException {
        try {
            return ((MetaService) callserver).getDomains();
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve the local server names", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the local server names: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public ImageHashMap getDefaultIcons() throws ConnectionException {
        try {
            return new ImageHashMap(((SystemService) callserver).getDefaultIcons());
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve the default icons", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the default icons: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public Icon getDefaultIcon(String name) throws ConnectionException {
        try {
            // proxy should implement caching here
            return this.getDefaultIcons().get(name);
        } catch (ConnectionException ce) {
            logger.fatal("[ServerError] could not retrieve the default icon for '" + name + "'");  // NOI18N
            //throw new ConnectionException("[ServerError] could not retrieve the default icon for '" + name + "'", ConnectionException.FATAL);
            throw ce;
        }
    }

    // User ---------------------------------------------------------
    public User getUser(String usergroupLocalserver, String usergroup, String userLocalserver, String username, String password) throws ConnectionException, UserException {
        try {
            return ((UserService) callserver).getUser(usergroupLocalserver, usergroup, userLocalserver, username, password);
        } catch (UserException ue) {
            logger.warn("can't login: wrong user informations", ue);  // NOI18N
            throw ue;
        } catch (RemoteException re) {
            logger.fatal("[ServerError] can't login", re);  // NOI18N
            throw new ConnectionException("[ServerError] can't login: " + re.getMessage(), ConnectionException.FATAL, re);  // NOI18N
        }
    }

    public Vector getUserGroupNames() throws ConnectionException {
        try {
            return ((UserService) callserver).getUserGroupNames();
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve the usergroup names", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the usergroup names: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public Vector getUserGroupNames(String username, String domain) throws ConnectionException, UserException {
        try {
            return ((UserService) callserver).getUserGroupNames(username, domain);
        } catch (RemoteException re) {
            if (re.getMessage().indexOf("UserGroupException") != -1) {  // NOI18N
                logger.warn("[ServerError] could not retrieve the usergroup names for user '" + username + "'", re);  // NOI18N
                throw new UserException(re.getMessage());
            } else {
                logger.fatal("[ServerError] could not retrieve the usergroup names for user '" + username + "' on localserver '" + domain + "'", re);  // NOI18N
                throw new ConnectionException("[ServerError] could not retrieve the usergroup names for user '" + username + "' on localserver '" + domain + "': " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
            }
        }
    }

    public boolean changePassword(User user, String oldPassword, String newPassword) throws ConnectionException, UserException {
        try {
            if(logger.isDebugEnabled())
                logger.debug("changing user password");   // NOI18N
            return ((UserService) callserver).changePassword(user, oldPassword, newPassword);
        } catch (UserException ue) {
            logger.warn("could not change password");  // NOI18N
            throw ue;
        } catch (RemoteException re) {
            logger.error("[ServerError] could not change user password", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not change user password: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    // Node ---------------------------------------------------------
    public Node[] getRoots(User user, String domain) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).getRoots(user, domain);
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve the top nodes of domain '" + domain + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not  retrieve the top nodes of domain '" + domain + "': " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public Node[] getRoots(User user) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).getRoots(user);
        } catch (RemoteException re) {
            logger.fatal("[CatalogueService] could not retrieve the top nodes", re);  // NOI18N
            throw new ConnectionException("[CatalogueService] could not retrieve the top nodes: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }
    //yxc
//    public Node[] getParents(User user, int nodeID, String domain) throws ConnectionException
//    {
//        try
//        {
//            return ((CatalogueService)callserver).getParents(user, nodeID, domain);
//        }
//        catch(RemoteException re)
//        {
//            logger.error("[ServerError] could not retrieve children of node '" + nodeID + "' of domain '" + domain + "'", re);
//            throw new ConnectionException("[ServerError] could not retrieve children of node '" + nodeID + "' of domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);
//        }
//    }
//   
//    public Node[] getChildren(User user, int nodeID, String domain) throws ConnectionException
//    {
//        try
//        {
//            return ((CatalogueService)callserver).getChildren(user, nodeID, domain);
//        }
//        catch(RemoteException re)
//        {
//            logger.error("[ServerError] could not retrieve children of node '" + nodeID + "' of domain '" + domain + "'", re);
//            throw new ConnectionException("[ServerError] could not retrieve children of node '" + nodeID + "' of domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);
//        }
//    }

//    public Node[] getChildren(User user, int nodeID, String domain, String sortBy) throws ConnectionException
//    {
//        logger.warn("can not sort nodes, sorting should be implemented by the proxy");
//        return this.getChildren(user, nodeID, domain);
//    }
//    
    public Node[] getChildren(Node node, User user) throws ConnectionException {
        try {
            Node[] n = ((CatalogueService) callserver).getChildren(node, user);

            if (node.isDynamic() && node.isSqlSort()) {
                return n;
            }

            return Sirius.navigator.tools.NodeSorter.sortNodes(n);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve children of node '" + node, re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve children of node '" + node + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public Node getNode(User user, int nodeID, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).getMetaObjectNode(user, nodeID, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve node '" + nodeID + "' of domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    // .........................................................................
    public Node addNode(Node node, Link parent, User user) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).addNode(node, parent, user);
        } catch (RemoteException re) {
            logger.error("[ServerError] addNode() could not add node '" + node + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] addNode() could not add node '" + node + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public boolean deleteNode(Node node, User user) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).deleteNode(node, user);
        } catch (RemoteException re) {
            logger.error("[ServerError] deleteNode() could not delete node '" + node + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] deleteNode() could not delete node '" + node + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public boolean addLink(Node from, Node to, User user) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).addLink(from, to, user);
        } catch (RemoteException re) {
            logger.error("[ServerError] addLink() could not add Link", re);  // NOI18N
            throw new ConnectionException("[ServerError] addLink() could not add Link: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public boolean deleteLink(Node from, Node to, User user) throws ConnectionException {
        try {
            return ((CatalogueService) callserver).deleteLink(from, to, user);
        } catch (RemoteException re) {
            logger.error("[ServerError] deleteLink() could not delete Link", re);  // NOI18N
            throw new ConnectionException("[ServerError] deleteLink() could not delete Link: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

//    public boolean copySubTree(Node root, User user) throws ConnectionException
//    {
//        try
//        {
//            return ((CatalogueService)callserver).copySubTree(root, user);
//        }
//        catch(RemoteException re)
//        {
//            logger.error("[ServerError] copySubTree() could not copy subtree", re);
//            throw new ConnectionException("[ServerError] copySubTree() could not copy subtree: " + re.getMessage(), ConnectionException.ERROR);
//        }
//    }
    // .........................................................................
    public Node[] getClassTreeNodes(User user) throws ConnectionException {
        try {
            return ((MetaService) callserver).getClassTreeNodes(user);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve the class tree nodes", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the class tree nodes: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    // Classes and Objects -----------------------------------------------------
    public MetaClass getMetaClass(User user, int classID, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).getClass(user, classID, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve meta class '" + classID + "' from domain '" + domain + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve meta class '" + classID + "' from domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaClass[] getClasses(User user, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).getClasses(user, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve the classes from domain '" + domain + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the classes from domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaObject[] getMetaObjectByQuery(User user, String query) throws ConnectionException {
        try {
            return ((MetaService) callserver).getMetaObject(user, query);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve MetaObject", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaObject[] getMetaObject(User user, Sirius.server.search.Query query) throws ConnectionException {
        try {

            return ((MetaService) callserver).getMetaObject(user, query);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve MetaObject", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve MetaObject: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaObject getMetaObject(User user, int objectID, int classID, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).getMetaObject(user, objectID, classID, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve MetaObject '" + objectID + '@' + classID + '@' + domain + '\'', re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve the classes from domain '" + domain + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public int insertMetaObject(User user, Sirius.server.search.Query query, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).insertMetaObject(user, query, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not insert / update MetaObject '" + query + "'", re);  // NOI18N
            throw new ConnectionException("[[ServerError] could not insert / update MetaObject '" + query + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaObject insertMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).insertMetaObject(user, MetaObject, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not insert MetaObject '" + MetaObject + "'", re);  // NOI18N
            throw new ConnectionException("[[ServerError] could not insert MetaObject '" + MetaObject + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public int updateMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).updateMetaObject(user, MetaObject, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not update MetaObject '" + MetaObject + "'", re);  // NOI18N
            throw new ConnectionException("[[ServerError] could not update MetaObject '" + MetaObject + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public int deleteMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).deleteMetaObject(user, MetaObject, domain);
        } catch (RemoteException re) {
            logger.error("[ServerError] deleteMetaObject(): could not delete MetaObject '" + MetaObject + "'", re);  // NOI18N
            throw new ConnectionException("[[ServerError] deleteMetaObject(): could not delete MetaObject '" + MetaObject + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public MetaObject getInstance(User user, MetaClass c) throws ConnectionException {
        try {
            try {
                MetaObject mo = (MetaObject) CloneHelper.clone(((MetaService) callserver).getInstance(user, c));
//                this.
//                mo.setMetaClass(c);
                return mo;
            } catch (CloneNotSupportedException ce) {
                logger.warn("could not clone MetaObject", ce);  // NOI18N
                return ((MetaService) callserver).getInstance(user, c);
            }
        } catch (RemoteException re) {
            logger.error("[ServerError] getInstance(): could not get instance of class '" + c + "'", re);  // NOI18N
            throw new ConnectionException("[[ServerError] getInstance(): could not get instance of class '" + c + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    // Dynmaic Search ----------------------------------------------------------
    public HashMap getSearchOptions(User user) throws ConnectionException {
        try {
            return ((Sirius.server.middleware.interfaces.proxy.SearchService) callserver).getSearchOptions(user);
        } catch (RemoteException re) {
            logger.fatal("[SearchService] getSearchOptions() failed ...", re);  // NOI18N
            throw new ConnectionException("[SearchService] getSearchOptions() failed: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public HashMap getSearchOptions(User user, String domain) throws ConnectionException {
        try {
            return ((Sirius.server.middleware.interfaces.proxy.SearchService) callserver).getSearchOptions(user, domain);
        } catch (RemoteException re) {
            logger.fatal("[SearchService] getSearchOptions() failed ...", re);  // NOI18N
            throw new ConnectionException("[SearchService] getSearchOptions() failed: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public SearchResult search(User user, String[] classIds, SearchOption[] searchOptions) throws ConnectionException {
        try {
            return ((Sirius.server.middleware.interfaces.proxy.SearchService) callserver).search(user, classIds, searchOptions);
        } catch (RemoteException re) {
            logger.fatal("[SearchService] search failed ...", re);  // NOI18N
            throw new ConnectionException("[SearchService] search failed: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    // QueryData ---------------------------------------------------------------
    public Info[] getUserQueryInfos(User user) throws ConnectionException {
        try {
            return ((QueryStore) callserver).getQueryInfos(user);
        } catch (RemoteException re) {
            logger.fatal("[ServerError] getUserGroupQueryInfos(UserGroup userGroup)", re);  // NOI18N
            throw new ConnectionException("[ServerError] getUserGroupQueryInfos(UserGroup userGroup): " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public Info[] getUserGroupQueryInfos(UserGroup userGroup) throws ConnectionException {
        try {
            return ((QueryStore) callserver).getQueryInfos(userGroup);
        } catch (RemoteException re) {
            logger.fatal("[QueryStore] getUserGroupQueryInfos(UserGroup userGroup)", re);  // NOI18N
            throw new ConnectionException("[QueryStore] getUserGroupQueryInfos(UserGroup userGroup): " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public QueryData getQueryData(int id, String domain) throws ConnectionException {
        try {
            return ((QueryStore) callserver).getQuery(id, domain);
        } catch (RemoteException re) {
            logger.fatal("[QueryStore] getQuery(QueryInfo queryInfo)", re);  // NOI18N
            throw new ConnectionException("[QueryStore] getQuery(QueryInfo queryInfo): " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public boolean storeQueryData(User user, QueryData data) throws ConnectionException {
        try {
            return ((QueryStore) callserver).storeQuery(user, data);
        } catch (RemoteException re) {
            logger.fatal("[ueryStore] storeUserQuery(User user, Query query)", re);  // NOI18N
            throw new ConnectionException("[ueryStore] storeUserQuery(User user, Query query): " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public boolean deleteQueryData(int queryDataId, String domain) throws ConnectionException {
        try {
            return ((QueryStore) callserver).delete(queryDataId, domain);
        } catch (RemoteException re) {
            logger.fatal("[QueryStore] deleteQuery(QueryInfo queryInfo)", re);  // NOI18N
            throw new ConnectionException("[QueryStore] deleteQuery(QueryInfo queryInfo): " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    // Methods -----------------------------------------------------------------
    public MethodMap getMethods(User user) throws ConnectionException {
        try {
            return ((MetaService) callserver).getMethods(user);
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve methods", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve methods: " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    public MethodMap getMethods(User user, String domain) throws ConnectionException {
        try {
            return ((MetaService) callserver).getMethods(user, domain);
        } catch (RemoteException re) {
            logger.fatal("[ServerError] could not retrieve methods from domain " + domain + "", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not retrieve methods from domain " + domain + ": " + re.getMessage(), ConnectionException.FATAL);  // NOI18N
        }
    }

    // DataRetrieval -----------------------------------------------------------
    public DataObject[] getDataObject(User user, Sirius.server.search.Query query) throws ConnectionException, DataRetrievalException {
        try {
            return ((DataService) callserver).getDataObject(user, query);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve data objects", re);  // NOI18N
            throw new ConnectionException("[[ServerError] could not retrieve data objects: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public DataObject getDataObject(User user, MetaObject MetaObject) throws ConnectionException, DataRetrievalException {
        try {
            return ((DataService) callserver).getDataObject(user, MetaObject);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not retrieve data object", re);  // NOI18N
            throw new ConnectionException("[[ServerError] could not retrieve data object: " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public int addQuery(User user, String name, String description, String statement) throws ConnectionException {
        try {
            return ((SearchService) callserver).addQuery(user, name, description, statement);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not add query '" + name + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not add query '" + name + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public int addQuery(User user, String name, String description, String statement, int resultType, char isUpdate, char isRoot, char isUnion, char isBatch) throws ConnectionException {
        try {
            return ((SearchService) callserver).addQuery(user, name, description, statement, resultType, isUpdate, isRoot, isUnion, isBatch);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not add query '" + name + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not add query '" + name + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public boolean addQueryParameter(User user, int queryId, String paramkey, String description) throws ConnectionException {
        try {
            return ((SearchService) callserver).addQueryParameter(user, queryId, paramkey, description);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not add query parameter '" + queryId + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not add query parameter '" + queryId + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    public boolean addQueryParameter(User user, int queryId, int typeId, String paramkey, String description, char isQueryResult, int queryPosition) throws ConnectionException {
        try {
            return ((SearchService) callserver).addQueryParameter(user, queryId, paramkey, description);
        } catch (RemoteException re) {
            logger.error("[ServerError] could not add query parameter '" + queryId + "'", re);  // NOI18N
            throw new ConnectionException("[ServerError] could not add query parameter '" + queryId + "': " + re.getMessage(), ConnectionException.ERROR);  // NOI18N
        }
    }

    // .........................................................................
    //---!!!
    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(int classId, User user, String[] representationFields, String representationPattern) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService) callserver).getAllLightweightMetaObjectsForClass(classId, user, representationFields, representationPattern);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId, ex);  // NOI18N
        }
    }

    @Override
    public MetaObject[] getAllLightweightMetaObjectsForClass(int classId, User user, String[] representationFields, AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService) callserver).getAllLightweightMetaObjectsForClass(classId, user, representationFields);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId, ex);  // NOI18N
        }
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(int classId, User user, String query, String[] representationFields, String representationPattern) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService) callserver).getLightweightMetaObjectsByQuery(classId, user, query, representationFields, representationPattern);
                return initLightweightMetaObjectsWithMetaService(lwmos);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId, ex);  // NOI18N
        }
    }

    @Override
    public MetaObject[] getLightweightMetaObjectsByQuery(int classId, User user, String query, String[] representationFields, AbstractAttributeRepresentationFormater formater) throws ConnectionException {
        try {
            if (IS_LEIGHTWEIGHT_MO_CODE_ENABLED) {
                final LightweightMetaObject[] lwmos = ((MetaService) callserver).getLightweightMetaObjectsByQuery(classId, user, query, representationFields);
                return initLightweightMetaObjectsWithMetaServiceAndFormater(lwmos, formater);
            } else {
                return getLightweightMetaObjectsFallback(classId, user);
            }
        } catch (RemoteException ex) {
            throw new ConnectionException("[ServerError] could not get all LightweightMetaObjects for class " + classId, ex);  // NOI18N
        }
    }

    /**
     * !For debugging purpose only. Do not use!
     * @param classId
     * @param user
     * @return
     * @throws ConnectionException
     */
    private final MetaObject[] getLightweightMetaObjectsFallback(int classId, User user) throws ConnectionException {
        final MetaClass mc = ClassCacheMultiple.getMetaClass(user.getDomain(), classId);
        final ClassAttribute ca = mc.getClassAttribute("sortingColumn");  // NOI18N
        String orderBy = "";  // NOI18N
        if (ca != null) {
            String value = ca.getValue().toString();
            orderBy = " order by " + value;  // NOI18N
        }
        final String query = "select " + mc.getID() + "," + mc.getPrimaryKey() + " from " + mc.getTableName() + orderBy;  // NOI18N

        return getMetaObjectByQuery(user, query);
    }

    /**
     * Initializes LWMOs with the appropriate metaservice
     * @param lwmos
     * @return
     */
    private final MetaObject[] initLightweightMetaObjectsWithMetaService(final LightweightMetaObject[] lwmos) {
        if (lwmos != null) {
            final MetaService msServer = (MetaService) callserver;
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
                }
            }
        }
        return lwmos;
    }

    /**
     * Initializes LWMOs with the appropriate metaservice and string formatter
     * @param lwmos
     * @param formater
     * @return
     */
    private final MetaObject[] initLightweightMetaObjectsWithMetaServiceAndFormater(final LightweightMetaObject[] lwmos, final AbstractAttributeRepresentationFormater formater) {
        if (lwmos != null) {
            final MetaService msServer = (MetaService) callserver;
            for (final LightweightMetaObject lwmo : lwmos) {
                if (lwmo != null) {
                    lwmo.setMetaService(msServer);
                    lwmo.setFormater(formater);
                }
            }
        }
        return lwmos;
    }
}

