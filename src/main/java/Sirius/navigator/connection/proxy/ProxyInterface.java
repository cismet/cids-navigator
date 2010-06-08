package Sirius.navigator.connection.proxy;

/*******************************************************************************

 	Copyright (c)	:	EIG (Environmental Informatics Group)
				http://www.enviromatics.net
				Prof. Dr. Reiner Guettler
				Prof. Dr. Ralf Denzer

				HTW
				University of Applied Sciences
				Goebenstr. 40
 				66117 Saarbruecken, Germany

	Programmers	:	Pascal <pascal@enviromatics.net>

 	Project		:	Sirius
	Version		:	1.0
 	Purpose		:
	Created		:	12/20/2002
	History		:

*******************************************************************************/


import java.util.*;

import Sirius.server.search.*;
import Sirius.server.middleware.types.*;
import Sirius.navigator.connection.*;
import Sirius.navigator.exception.ConnectionException;

/**
 * Default implementation of the connection proxy interface.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 * test
 */
public interface ProxyInterface
{
    public void setProperty(String name, String value);
    
    public ConnectionSession getSession();
    
    public Node[] getRoots() throws ConnectionException;
    
    public Node[] getRoots(String domain) throws ConnectionException;
    
    public Node[] getChildren(Node node) throws ConnectionException;
    
  //  public Node[] getChildren(int nodeID, String domain, String sortBy) throws ConnectionException;
    
    //public Node[] getParents(int nodeID, String domain) throws ConnectionException;
    
    public Node getNode(int nodeID, String domain) throws ConnectionException;

    // .........................................................................
    
    public Node addNode(Node node, Link parent) throws ConnectionException;
        
    public boolean deleteNode(Node node) throws ConnectionException;

    public boolean addLink(Node from, Node to) throws ConnectionException;

    public boolean deleteLink(Node from, Node to) throws ConnectionException;

   // public boolean copySubTree(Node root) throws ConnectionException;
        
    // .........................................................................
    
    
    public Sirius.server.localserver.method.Method getMethod(String methodKey) throws ConnectionException;
    
    public Node[] getClassTreeNodes() throws ConnectionException;
    
    // .........................................................................
    
    public MetaClass[] getClasses(String domain) throws ConnectionException;
    
    public MetaClass[] getClasses() throws ConnectionException;
     
    public MetaClass getMetaClass(int classID, String domain) throws ConnectionException;
    
    public MetaClass getMetaClass(String classKey) throws ConnectionException;
    
    // .........................................................................
    
    public MetaObject getMetaObject(int objectID, int classID, String domain) throws ConnectionException;
    
    public MetaObject getMetaObject(String objectId) throws ConnectionException;
    
    public MetaObject[] getMetaObject(Query query) throws ConnectionException;
    
    public MetaObject[] getMetaObjectByQuery(String query,int sig) throws ConnectionException;
   
    public MetaObject insertMetaObject(MetaObject MetaObject, String domain) throws ConnectionException;
    
    public int insertMetaObject(Query query, String domain) throws ConnectionException;
    

    public int updateMetaObject(MetaObject MetaObject, String domain)	throws ConnectionException;  
    
    public int deleteMetaObject(MetaObject MetaObject, String domain) throws ConnectionException;
    
    
    public MetaObject getInstance(MetaClass c) throws ConnectionException;
    
    // .........................................................................
    
    public HashMap getSearchOptions() throws ConnectionException;
    
    public SearchResult search(Collection classIds, Collection searchOptions)  throws ConnectionException;
    
    public SearchResult search(Collection searchOptions)  throws ConnectionException;
    
    public int addQuery(String name,String description,String statement,int resultType,char isUpdate,char isRoot,char isUnion, char isBatch) throws ConnectionException;
    
    public int addQuery(String name,String description,String statement) throws ConnectionException;
    
    public boolean addQueryParameter(int queryId,int typeId,String paramkey,String description,char isQueryResult,int queryPosition) throws ConnectionException;
    
    public boolean addQueryParameter(int queryId,String paramkey,String description) throws ConnectionException;
        
    // .........................................................................
}
