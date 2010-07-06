package Sirius.navigator.connection;

import Sirius.navigator.exception.ConnectionException;
/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.enviromatics.net
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTW
 * University of Applied Sciences
 * Goebenstr. 40
 * 66117 Saarbruecken, Germany
 *
 * Programmers	:	Pascal <pascal@enviromatics.net>
 *
 * Project		:	Sirius
 * Version		:	1.0
 * Purpose		:
 * Created		:	12/20/2002
 * History		:
 *
 *******************************************************************************/
//import Sirius.server.search.*;
//import Sirius.server.search.query.*;
//import Sirius.Translation.*;
import java.util.*;
import javax.swing.Icon;

import Sirius.server.localserver.method.MethodMap;
import Sirius.server.middleware.types.*;
import Sirius.server.newuser.*;
import Sirius.server.newuser.UserException;
import Sirius.util.image.ImageHashMap;
import Sirius.server.search.*;
import Sirius.server.search.store.*;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public interface Connection {
    // Connection --------------------------------------------------------------

    public boolean connect(String callserverURL) throws ConnectionException;

    public boolean connect(String callserverURL, String username, String password) throws ConnectionException;

    public boolean reconnect() throws ConnectionException;

    public void disconnect();

    public boolean isConnected();

    // Default -----------------------------------------------------------------
    public String[] getDomains() throws ConnectionException;

    public ImageHashMap getDefaultIcons() throws ConnectionException;

    public Icon getDefaultIcon(String name) throws ConnectionException;

    // User --------------------------------------------------------------------
    public User getUser(String userGroupLsName, String userGroupName, String userLsName, String userName, String password) throws ConnectionException, UserException;

    public Vector getUserGroupNames() throws ConnectionException;

    public Vector getUserGroupNames(String username, String domain) throws ConnectionException, UserException;

    public boolean changePassword(User user, String oldPassword, String newPassword) throws ConnectionException, UserException;

    // Node --------------------------------------------------------------------
    public Node[] getRoots(User user) throws ConnectionException;

    public Node[] getRoots(User user, String domain) throws ConnectionException;

    // public Node[] getChildren(User user, int nodeID, String domain) throws ConnectionException;
    public Node[] getChildren(Node node, User user) throws ConnectionException;

    //  public Node[] getChildren(User user, int nodeID, String domain, String sortBy) throws ConnectionException;
    // public Node[] getParents(User user, int nodeID, String domain) throws ConnectionException;
    public Node getNode(User user, int nodeID, String domain) throws ConnectionException;

    // .........................................................................
    public Node addNode(Node node, Link parent, User user) throws ConnectionException;

    public boolean deleteNode(Node node, User user) throws ConnectionException;

    public boolean addLink(Node from, Node to, User user) throws ConnectionException;

    public boolean deleteLink(Node from, Node to, User user) throws ConnectionException;

    // public boolean copySubTree(Node root, User user) throws ConnectionException;
    // .........................................................................
    public Node[] getClassTreeNodes(User user) throws ConnectionException;

    // Classes & Objects -------------------------------------------------------
    public MetaClass getMetaClass(User user, int classID, String domain) throws ConnectionException;

    public MetaClass[] getClasses(User user, String domain) throws ConnectionException;

    public MetaObject[] getMetaObject(User usr, Query query) throws ConnectionException;

    public MetaObject getMetaObject(User user, int objectID, int classID, String domain) throws ConnectionException;

    public MetaObject[] getMetaObjectByQuery(User user, String query) throws ConnectionException;

    public MetaObject insertMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    public int insertMetaObject(User user, Query query, String domain) throws ConnectionException;

    public int updateMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    public int deleteMetaObject(User user, MetaObject MetaObject, String domain) throws ConnectionException;

    public MetaObject getInstance(User user, MetaClass c) throws ConnectionException;

    // Dynmaic Search ----------------------------------------------------------
    public HashMap getSearchOptions(User user) throws ConnectionException;

    public HashMap getSearchOptions(User user, String domain) throws ConnectionException;

    public SearchResult search(User user, String[] classIds, SearchOption[] searchOptions) throws ConnectionException;

    /**
     * add single query root and leaf returns a query_id
     */
    public int addQuery(User user, String name, String description, String statement, int resultType, char isUpdate, char isRoot, char isUnion, char isBatch) throws ConnectionException;

    public int addQuery(User user, String name, String description, String statement) throws ConnectionException;

    public boolean addQueryParameter(User user, int queryId, int typeId, String paramkey, String description, char isQueryResult, int queryPosition) throws ConnectionException;

    /**
     * position set in order of the addition
     */
    public boolean addQueryParameter(User user, int queryId, String paramkey, String description) throws ConnectionException;

    // QueryData ---------------------------------------------------------------
    public boolean deleteQueryData(int queryDataId, String domain) throws ConnectionException;

    public boolean storeQueryData(User user, QueryData data) throws ConnectionException;

    public QueryData getQueryData(int id, String domain) throws ConnectionException;

    public Info[] getUserGroupQueryInfos(UserGroup userGroup) throws ConnectionException;

    public Info[] getUserQueryInfos(User user) throws ConnectionException;

    // Methods -----------------------------------------------------------------
    public MethodMap getMethods(User user) throws ConnectionException;

    public MethodMap getMethods(User user, String domain) throws ConnectionException;

    // .........................................................................
    //---!!!
    public MetaObject[] getAllLightweightMetaObjectsForClass(int classId, User user, String[] representationFields, String representationPattern) throws ConnectionException;

    public MetaObject[] getAllLightweightMetaObjectsForClass(int classId, User user, String[] representationFields, AbstractAttributeRepresentationFormater formater) throws ConnectionException;

    public MetaObject[] getLightweightMetaObjectsByQuery(int classId, User user, String query, String[] representationFields, String representationPattern) throws ConnectionException;

    public MetaObject[] getLightweightMetaObjectsByQuery(int classId, User user, String query, String[] representationFields, AbstractAttributeRepresentationFormater formater) throws ConnectionException;
}
