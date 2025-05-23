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

import Sirius.server.localserver.method.MethodMap;
import Sirius.server.middleware.types.Link;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.Node;
import Sirius.server.newuser.User;

import Sirius.util.image.ImageHashMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * Default implementation of the connection proxy interface.
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public class DefaultConnectionProxyHandler extends ConnectionProxyHandler {

    //~ Instance fields --------------------------------------------------------

    protected ProxyInterface proxyHandler;
    protected ImageHashMap iconCache = null;
    protected ClassAndMethodCache classAndMethodCache = null;
    protected HashMap objectCache = new HashMap();
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultConnectionProxyHandler object.
     *
     * @param  connectionSession  DOCUMENT ME!
     */
    public DefaultConnectionProxyHandler(final ConnectionSession connectionSession) {
        super(connectionSession);
        proxyHandler = new DefaultConnectionProxyHandler.DefaultConnectionProxy();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HashMap getClassHash() {
        return classAndMethodCache.getClassHash();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            if (method.getDeclaringClass().equals(Sirius.navigator.connection.Connection.class)) {
                if (method.getName().equals("getDefaultIcons")) {             // NOI18N
                    if (iconCache == null) {
                        if (log.isInfoEnabled()) {
                            log.info("[ConnectionProxy] filling icon cache"); // NOI18N
                        }
                        iconCache = (ImageHashMap)method.invoke(connection, args);
                    }

                    return iconCache;
                } else {
                    return method.invoke(connection, args);
                }
            } else if (method.getDeclaringClass().equals(Sirius.navigator.connection.proxy.ProxyInterface.class)) {
                return method.invoke(proxyHandler, args);
            } else {
                log.error("[ConnectionProxy] undeclared method '" + method.getName() + "'");                  // NOI18N
                throw new RuntimeException("[ConnectionProxy] undeclared method '" + method.getName() + "'"); // NOI18N
            }
        } catch (InvocationTargetException itex) {
            // ok, no need to worry about
            throw itex.getTargetException();
        } catch (Exception ex) {
            log.error("[ConnectionProxy] unexpected invocation exception' " + ex.getMessage() + "'", ex);              // NOI18N
            throw new RuntimeException("[ConnectionProxy] unexpected invocation exception' " + ex.getMessage() + "'"); // NOI18N
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ClassAndMethodCache {

        //~ Instance fields ----------------------------------------------------

        private HashMap classHash = null;
        private HashMap methodHash = null;
        private List lsNames = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Konstruiert einen neuen leeren ClassCache. Der ClassCache wird beim erstmaligen Ladens einer
         * Sirius.Middleware.Types.Class gefuellt.
         */
        public ClassAndMethodCache()    // MetaService metaService)
        {
            classHash = new HashMap(25, 0.5f);
            methodHash = new HashMap(25, 0.5f);
            lsNames = new ArrayList(5);
            // metaServiceRef = metaService;
        }

        /**
         * Konstruiert einen neuen ClassCache, der mit den Classes eines bestimmten Lokalservers gefuellt wird.
         *
         * @param  user              User
         * @param  localServerNames  DOCUMENT ME!
         * @param  context           DOCUMENT ME!
         */
        public ClassAndMethodCache(final User user, final String[] localServerNames, final ConnectionContext context) {
            classHash = new HashMap(50, 0.5f);
            methodHash = new HashMap(25, 0.5f);
            lsNames = new ArrayList((localServerNames.length + 1));
            // metaServiceRef = metaService;

            for (int i = 0; i < localServerNames.length; i++) {
                try {
                    final MetaClass[] tmpClasses = connection.getClasses(
                            user,
                            localServerNames[i],
                            context); // .getClasses(user, localServerNames[i]);

                    if (tmpClasses != null) {
                        putClasses(tmpClasses, localServerNames[i]);
                    }
                } catch (Exception e) {
                    log.fatal("Ausnahme im ClassAndMethodCache beim Aufruf von remoteNodeRef.getClasses(...): ", e); // NOI18N
                }
            }
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public HashMap getClassHash() {
            return classHash;
        }

        /**
         * Laedt eine Class aus dem Cache bzw. vom Server.<br>
         * Ist die Class noch nicht im Cache enthalten wird sie vom Server geladen, wurden von diesem LocalServer noch
         * keine Classes geladen, so werden alle Classes dieses LocalServers gecacht.
         *
         * @param   user             User.
         * @param   classID          Die ID der zu ladenden Class.
         * @param   localServerName  Der LocalServer des Users.
         * @param   context          DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  ConnectionException  DOCUMENT ME!
         */
        public MetaClass getCachedClass(final User user,
                final int classID,
                final String localServerName,
                final ConnectionContext context) throws ConnectionException {
            final String key = new String(localServerName + classID);
            // Falls noch keine Class von diesem LocalServer geladen wurde,
            // -> alle Classes des LocalServer cachen
            if (!lsNames.contains(localServerName)) {
                final MetaClass[] tmpClasses = connection.getClasses(user, localServerName, context);
                this.putClasses(tmpClasses, localServerName);
                if (log.isDebugEnabled()) {
                    log.debug("<CC> Classes von neuem LocalServer " + localServerName + " gecacht");
                }
            }

            // Falls die Class nicht im Cache enthalten ist
            // -> Class vom Server laden
            if (!classHash.containsKey(key)) {
                final MetaClass tmpClass = connection.getMetaClass(
                        user,
                        classID,
                        localServerName,
                        context);
                this.putClass(tmpClass, localServerName);
                return tmpClass;
            } else {
                return (MetaClass)classHash.get(key);
            }
        }

        /**
         * Liefert alle Classes, die sich im Cache befinden.
         *
         * @return  Ein Array von Type Sirius.Middleware.Types.Class oder null.
         */
        public MetaClass[] getAllCachedClasses() {
            final List classVector = new ArrayList(classHash.values());

            if (classVector == null) {
                return null;
            }

            return (MetaClass[])classVector.toArray(new MetaClass[classVector.size()]);
        }

        /**
         * Fuegt eine Class zum ClassCache hinzu.
         *
         * @param  cls   Die zu cachende Class.
         * @param  lsID  LocalServer ID, bildet zusammen mit der Class ID einen einduetigen Schluessel fuer die
         *               Hashtable
         */
        protected void putClass(final MetaClass cls, final String lsID) {
            final String key = String.valueOf(lsID + cls.getID());
            if (!classHash.containsKey(key)) {
                classHash.put(key, cls);
            }
        }

        /**
         * Fuegt ein Array von Classes zum ClassCache hinzu.
         *
         * @param  classes          Ein Array von cachenden Classes.
         * @param  localServerName  DOCUMENT ME!
         */
        protected void putClasses(final MetaClass[] classes, final String localServerName) {
            lsNames.add(localServerName);
            for (int i = 0; i < classes.length; i++) {
                final String key = localServerName + classes[i].getID();
                if (!classHash.containsKey(key)) {
                    classHash.put(key, classes[i]);
                }

                if (log.isDebugEnabled()) {
                    log.debug("<CMC> Class gecacht: " + classes[i].getName() + " " + classes[i].getID() + " "
                                + classes[i].getDomain());
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("<CMC> " + classes.length + " Classes von LocalServer " + localServerName + " gecacht.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   methodKey  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Sirius.server.localserver.method.Method getCachedMethod(final String methodKey) {
            return (Sirius.server.localserver.method.Method)methodHash.get(methodKey);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  method           DOCUMENT ME!
         * @param  localServerName  DOCUMENT ME!
         */
        protected void putMethod(final Sirius.server.localserver.method.Method method, final String localServerName) {
            final String key = String.valueOf(localServerName + method.getID());
            if (!methodHash.containsKey(key)) {
                methodHash.put(key, method);
                if (log.isDebugEnabled()) {
                    log.debug("<CMC> method '" + key + "' gecacht.");
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  methodMap  DOCUMENT ME!
         */
        protected void putMethods(final MethodMap methodMap) {
            if (methodMap != null) {
                methodHash.putAll(methodMap);
                if (log.isDebugEnabled()) {
                    final Iterator iterator = methodMap.keySet().iterator();
                    if (iterator.hasNext()) {
                        if (log.isDebugEnabled()) {
                            log.debug("<CMC> method '" + iterator.next() + " gecacht."); // NOI18N
                        }
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class DefaultConnectionProxy implements ProxyInterface {

        //~ Methods ------------------------------------------------------------

        @Override
        public void setProperty(final String name, final String value) {
            if (log.isDebugEnabled()) {
                log.debug("[ProxyInterface] setting propety '" + name + "' to '" + value + "'");
            }
        }

        @Override
        public ConnectionSession getSession() {
            return session;
        }

        @Deprecated
        @Override
        public Node[] getRoots() throws ConnectionException {
            return getRoots(ConnectionContext.createDeprecated());
        }

        @Override
        public Node[] getRoots(final ConnectionContext context) throws ConnectionException {
            return connection.getRoots(session.getUser(), context);
        }

        @Deprecated
        @Override
        public Node[] getRoots(final String domain) throws ConnectionException {
            return getRoots(domain, ConnectionContext.createDeprecated());
        }

        @Override
        public Node[] getRoots(final String domain, final ConnectionContext context) throws ConnectionException {
            return connection.getRoots(session.getUser(), domain, context);
        }

        @Deprecated
        @Override
        public Node[] getChildren(final Node node) throws ConnectionException {
            return getChildren(node, ConnectionContext.createDeprecated());
        }

        @Override
        public Node[] getChildren(final Node node, final ConnectionContext context) throws ConnectionException {
            final Node[] c = connection.getChildren(node, session.getUser(), context);

            if ((node.getDynamicChildrenStatement() != null) && node.isSqlSort()) {
                return c;
            }

            return Sirius.navigator.tools.NodeSorter.sortNodes(c);
        }

        @Deprecated
        @Override
        public Node getNode(final int nodeID, final String domain) throws ConnectionException {
            return getNode(nodeID, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public Node getNode(final int nodeID, final String domain, final ConnectionContext context)
                throws ConnectionException {
            return connection.getNode(session.getUser(), nodeID, domain, context);
        }

        @Deprecated
        @Override
        public Node addNode(final Node node, final Link parent) throws ConnectionException {
            return addNode(node, parent, ConnectionContext.createDeprecated());
        }

        @Override
        public Node addNode(final Node node, final Link parent, final ConnectionContext context)
                throws ConnectionException {
            return connection.addNode(node, parent, session.getUser(), context);
        }

        @Deprecated
        @Override
        public boolean deleteNode(final Node node) throws ConnectionException {
            return deleteNode(node, ConnectionContext.createDeprecated());
        }

        @Override
        public boolean deleteNode(final Node node, final ConnectionContext context) throws ConnectionException {
            return connection.deleteNode(node, session.getUser(), context);
        }

        @Override
        @Deprecated
        public boolean addLink(final Node from, final Node to) throws ConnectionException {
            return addLink(from, to, ConnectionContext.createDeprecated());
        }

        @Override
        public boolean addLink(final Node from, final Node to, final ConnectionContext context)
                throws ConnectionException {
            return connection.addLink(from, to, session.getUser(), context);
        }

        @Deprecated
        @Override
        public boolean deleteLink(final Node from, final Node to) throws ConnectionException {
            return deleteLink(from, to, ConnectionContext.createDeprecated());
        }

        @Override
        public boolean deleteLink(final Node from, final Node to, final ConnectionContext context)
                throws ConnectionException {
            return connection.deleteLink(from, to, session.getUser(), context);
        }

        @Deprecated
        @Override
        public Node[] getClassTreeNodes() throws ConnectionException {
            return getClassTreeNodes(ConnectionContext.createDeprecated());
        }

        @Override
        public Node[] getClassTreeNodes(final ConnectionContext context) throws ConnectionException {
            return connection.getClassTreeNodes(session.getUser(), context);
        }

        @Deprecated
        @Override
        public MetaClass[] getClasses() throws ConnectionException {
            return getClasses(ConnectionContext.createDeprecated());
        }

        @Override
        public MetaClass[] getClasses(final ConnectionContext context) throws ConnectionException {
            final String[] domains = connection.getDomains(context);
            final ArrayList classes = new ArrayList();

            for (int i = 0; i < domains.length; i++) {
                MetaClass[] classArray = new MetaClass[0];
                try {
                    classArray = this.getClasses(domains[i], context);
                } catch (Exception t) {
                    log.error("Fehler im DefaultConnectionProxyHandler bei getClasses", t);
                }

                for (int j = 0; j < classArray.length; j++) {
                    classes.add(classArray[j]);
                }
            }

            return (MetaClass[])classes.toArray(new MetaClass[classes.size()]);
        }

        @Deprecated
        @Override
        public MetaClass[] getClasses(final String domain) throws ConnectionException {
            return getClasses(domain, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaClass[] getClasses(final String domain, final ConnectionContext context) throws ConnectionException {
            return connection.getClasses(session.getUser(), domain, context);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   context  DOCUMENT ME!
         *
         * @throws  ConnectionException  DOCUMENT ME!
         */
        public void initClassAndMethodCache(final ConnectionContext context) throws ConnectionException {
            classAndMethodCache = new ClassAndMethodCache(session.getUser(),
                    connection.getDomains(context), context);
        }

        @Deprecated
        @Override
        public MetaClass getMetaClass(final int classID, final String domain) throws ConnectionException {
            return getMetaClass(classID, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaClass getMetaClass(final int classID, final String domain, final ConnectionContext context)
                throws ConnectionException {
            if (classAndMethodCache == null) {
                if (log.isInfoEnabled()) {
                    log.info("[ConnectionProxy] filling meta class cache"); // NOI18N
                }
                classAndMethodCache = new ClassAndMethodCache(session.getUser(),
                        connection.getDomains(context),
                        context);
            }

            final MetaClass metaClass = classAndMethodCache.getCachedClass(session.getUser(), classID, domain, context);
            if (log.isDebugEnabled()) {
                log.debug("getgetMetaClass(): classID=" + classID + ", domain=" + domain);
                log.debug("MetaClass: " + metaClass + "\nMetaClass.getName(): " + metaClass.getName()
                            + "\nMetaClass.getEditor(): " + metaClass.getEditor() + "\nMetaClass.getComplexEditor(): "
                            + metaClass.getComplexEditor());
            }

            return metaClass;
        }

        @Deprecated
        @Override
        public MetaClass getMetaClass(final String classKey) throws ConnectionException {
            return getMetaClass(classKey, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaClass getMetaClass(final String classKey, final ConnectionContext context)
                throws ConnectionException {
            try {
                final StringTokenizer tokenizer = new StringTokenizer(classKey, "@"); // NOI18N
                final int classID = Integer.valueOf(tokenizer.nextToken()).intValue();
                final String domain = tokenizer.nextToken();

                return this.getMetaClass(classID, domain, context);
            } catch (ConnectionException cexp) {
                throw cexp;
            } catch (Exception t) {
                log.error("malformed classKey: '" + classKey + "' (classId@domain expected)"); // NOI18N
                throw new ConnectionException("malformed class key: '" + classKey + "' (classId@domain expected)",
                    ConnectionException.ERROR,
                    t);                                                                        // NOI18N
            }
        }

        @Deprecated
        @Override
        public Sirius.server.localserver.method.Method getMethod(final String methodKey) throws ConnectionException {
            return getMethod(methodKey, ConnectionContext.createDeprecated());
        }

        @Override
        public Sirius.server.localserver.method.Method getMethod(final String methodKey,
                final ConnectionContext context) throws ConnectionException {
            if (classAndMethodCache == null) {
                if (log.isInfoEnabled()) {
                    log.info("[ConnectionProxy] filling meta class cache"); // NOI18N
                }
                classAndMethodCache = new ClassAndMethodCache(session.getUser(),
                        connection.getDomains(context),
                        context);
            }

            return classAndMethodCache.getCachedMethod(methodKey);
        }

        @Override
        @Deprecated
        public MetaObject getMetaObject(final int objectID, final int classID, final String domain)
                throws ConnectionException {
            return getMetaObject(objectID, classID, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaObject getMetaObject(final int objectID,
                final int classID,
                final String domain,
                final ConnectionContext context) throws ConnectionException {
            if (classAndMethodCache == null) {
                initClassAndMethodCache(context);
            }

            if (log.isDebugEnabled()) {
                log.debug("getMetaObject(): objectID=" + objectID + ", classID=" + classID + ", domain=" + domain); // NOI18N
            }

            final MetaObject metaObject = connection.getMetaObject(session.getUser(),
                    objectID,
                    classID,
                    domain,
                    context);
            if (metaObject != null) {
                if (log.isDebugEnabled()) {
                    log.debug(" MetaObject: " + metaObject + " MetaObject.getName(): " + metaObject.getName()
                                + " MetaObject.getEditor(): " + metaObject.getEditor()
                                + " MetaObject.getComplexEditor(): " + metaObject.getComplexEditor());
                }

                // set Classes in SubObjects as well
                metaObject.setAllClasses(classAndMethodCache.getClassHash());
            }

            return metaObject;
        }

        @Override
        @Deprecated
        public MetaObject getMetaObject(final String objectId) throws ConnectionException {
            return getMetaObject(objectId, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaObject getMetaObject(final String objectId, final ConnectionContext context)
                throws ConnectionException {
            try {
                final StringTokenizer tokenizer = new StringTokenizer(objectId, "@"); // NOI18N
                final int objectID = Integer.valueOf(tokenizer.nextToken()).intValue();
                final int classID = Integer.valueOf(tokenizer.nextToken()).intValue();
                final String domain = tokenizer.nextToken();

                return this.getMetaObject(objectID, classID, domain, context);
            } catch (ConnectionException cexp) {
                throw cexp;
            } catch (Exception t) {
                log.error("malformed object id: '" + objectId + "' (objectID@classID@domain expected)"); // NOI18N
                throw new ConnectionException("malformed object id: '" + objectId
                            + "' (objectID@classID@domain expected)",
                    ConnectionException.ERROR,
                    t);                                                                                  // NOI18N
            }
        }

        @Override
        @Deprecated
        public MetaObject[] getMetaObjectByQuery(final String query, final int sig) throws ConnectionException {
            return getMetaObjectByQuery(query, sig, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaObject[] getMetaObjectByQuery(final String query, final int sig, final ConnectionContext context)
                throws ConnectionException {
            if (classAndMethodCache == null) {
                initClassAndMethodCache(context);
            }

            if (log.isDebugEnabled()) {
                log.debug("getMetaObjectByQuery"); // NOI18N
            }

            try {
                final MetaObject[] obs = connection.getMetaObjectByQuery(session.getUser(), query, context);

                for (int i = 0; i < obs.length; i++) {
                    if (obs[i] != null) {
                        obs[i].setAllClasses(classAndMethodCache.getClassHash());
                    }
                }

                return obs;
            } catch (Exception t) {
                log.warn("Fehler in getMetaObjectByQuery", t);
            }

            return null;
        }

        @Deprecated
        @Override
        public MetaObject insertMetaObject(final MetaObject MetaObject, final String domain)
                throws ConnectionException {
            return insertMetaObject(MetaObject, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaObject insertMetaObject(final MetaObject MetaObject,
                final String domain,
                final ConnectionContext context) throws ConnectionException {
            return connection.insertMetaObject(session.getUser(), MetaObject, domain, context);
        }

        @Deprecated
        @Override
        public int updateMetaObject(final MetaObject MetaObject, final String domain) throws ConnectionException {
            return updateMetaObject(MetaObject, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public int updateMetaObject(final MetaObject MetaObject, final String domain, final ConnectionContext context)
                throws ConnectionException {
            return connection.updateMetaObject(session.getUser(), MetaObject, domain, context);
        }

        @Deprecated
        @Override
        public int deleteMetaObject(final MetaObject MetaObject, final String domain) throws ConnectionException {
            return deleteMetaObject(MetaObject, domain, ConnectionContext.createDeprecated());
        }

        @Override
        public int deleteMetaObject(final MetaObject MetaObject, final String domain, final ConnectionContext context)
                throws ConnectionException {
            return connection.deleteMetaObject(session.getUser(), MetaObject, domain, context);
        }

        @Deprecated
        @Override
        public MetaObject getInstance(final MetaClass c) throws ConnectionException {
            return getInstance(c, ConnectionContext.createDeprecated());
        }

        @Override
        public MetaObject getInstance(final MetaClass c, final ConnectionContext context) throws ConnectionException {
            MetaObject MetaObject = null;

            MetaObject = connection.getInstance(session.getUser(), c, context);
            MetaObject.setAllClasses(classAndMethodCache.getClassHash());

            return MetaObject;
        }

        @Deprecated
        @Override
        public Collection customServerSearch(final CidsServerSearch serverSearch) throws ConnectionException {
            return customServerSearch(serverSearch, ConnectionContext.createDeprecated());
        }

        @Override
        public Collection customServerSearch(final CidsServerSearch serverSearch, final ConnectionContext context)
                throws ConnectionException {
            return connection.customServerSearch(session.getUser(), serverSearch, context);
        }

        @Override
        @Deprecated
        public Object executeTask(final String taskname,
                final String taskdomain,
                final Object body,
                final ServerActionParameter... params) throws ConnectionException {
            return executeTask(taskname, taskdomain, body, ConnectionContext.createDeprecated(), params);
        }

        @Override
        public Object executeTask(final String taskname,
                final String taskdomain,
                final Object body,
                final ConnectionContext context,
                final ServerActionParameter... params) throws ConnectionException {
            return connection.executeTask(session.getUser(), taskname, taskdomain, body, context, params);
        }

        @Override
        public Object executeTask(final String taskname,
                final String taskdomain,
                final Object body,
                final ConnectionContext context,
                final boolean resolvePreparedAsyncByteAction,
                final ServerActionParameter... params) throws ConnectionException {
            return connection.executeTask(session.getUser(),
                    taskname,
                    taskdomain,
                    body,
                    context,
                    resolvePreparedAsyncByteAction,
                    params);
        }
    }
}
