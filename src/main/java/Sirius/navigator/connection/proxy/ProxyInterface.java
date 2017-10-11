/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection.proxy;

import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.Link;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.Node;

import java.util.Collection;

import de.cismet.cids.server.connectioncontext.ConnectionContext;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.search.CidsServerSearch;

/**
 * Default implementation of the connection proxy interface.
 *
 * @author   Pascal test
 * @version  1.0 12/22/2002
 */
public interface ProxyInterface {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  name   DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    void setProperty(String name, String value);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ConnectionSession getSession();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getRoots(String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getChildren(Node node) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   nodeID  DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node getNode(int nodeID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node    DOCUMENT ME!
     * @param   parent  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node addNode(Node node, Link parent) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteNode(Node node) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     * @param   to    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean addLink(Node from, Node to) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     * @param   to    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    boolean deleteLink(Node from, Node to) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   methodKey  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Sirius.server.localserver.method.Method getMethod(String methodKey) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Node[] getClassTreeNodes() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass[] getClasses(String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass[] getClasses() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classID  DOCUMENT ME!
     * @param   domain   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass getMetaClass(int classID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   classKey  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaClass getMetaClass(String classKey) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   objectID  DOCUMENT ME!
     * @param   classID   DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    MetaObject getMetaObject(int objectID, int classID, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   objectID  DOCUMENT ME!
     * @param   classID   DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     * @param   context   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getMetaObject(int objectID, int classID, String domain, ConnectionContext context)
            throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   objectId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getMetaObject(String objectId) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   objectId  DOCUMENT ME!
     * @param   context   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getMetaObject(String objectId, ConnectionContext context) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     * @param   sig    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getMetaObjectByQuery(String query, int sig) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   query    DOCUMENT ME!
     * @param   sig      DOCUMENT ME!
     * @param   context  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject[] getMetaObjectByQuery(String query, int sig, ConnectionContext context) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject insertMetaObject(MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int updateMetaObject(MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   MetaObject  DOCUMENT ME!
     * @param   domain      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    int deleteMetaObject(MetaObject MetaObject, String domain) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    MetaObject getInstance(MetaClass c) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   serverSearch  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    Collection customServerSearch(CidsServerSearch serverSearch) throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @param   taskname    DOCUMENT ME!
     * @param   taskdomain  DOCUMENT ME!
     * @param   body        DOCUMENT ME!
     * @param   params      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Deprecated
    Object executeTask(String taskname, String taskdomain, Object body, ServerActionParameter... params)
            throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
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
    Object executeTask(String taskname,
            String taskdomain,
            ConnectionContext context,
            Object body,
            ServerActionParameter... params) throws ConnectionException;
}
