/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import java.lang.ref.SoftReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MetaObjectCache {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(MetaObjectCache.class);

    //~ Instance fields --------------------------------------------------------

    // we're soft-referencing the whole array so that we can be sure that the whole array will be collected and not only
    // single items of the array. if there is a need for additional cache access methods we'll have to change the data
    // store anyway
    private final transient Map<String, SoftReference<MetaObject[]>> cache;
    private final transient Map<String, Set<String>> queriesPerTable;
    private final transient Map<String, ReentrantReadWriteLock> locks;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaSearchCache object.
     */
    private MetaObjectCache() {
        cache = new HashMap<>();
        queriesPerTable = new HashMap<>();
        locks = new HashMap<>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaObjectCache getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    /**
     * Completely wipes the cache content.
     */
    public synchronized void clearCache() {
        final Collection<Lock> locks = new ArrayList<>();
        try {
            for (final String query : cache.keySet()) {
                locks.add(createWriteLock(query));
            }
            cache.clear();
            queriesPerTable.clear();
        } finally {
            for (final Lock lock : locks) {
                lock.unlock();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject[][] clearCache(final MetaClass metaClass) {
        final String tableName = (metaClass != null) ? metaClass.getTableName() : null;
        if (tableName != null) {
            final String tableNameIntern = tableName.toLowerCase().intern();
            final Set<String> queries = queriesPerTable.get(tableNameIntern);
            if (queries != null) {
                final Collection<MetaObject[]> mos = new ArrayList<>(queries.size());
                for (final String query : queries) {
                    if (query != null) {
                        final String queryIntern = query.intern();
                        final Lock lock = createWriteLock(queryIntern);
                        try {
                            final SoftReference<MetaObject[]> objs = cache.put(queryIntern, null);
                            if (objs != null) {
                                mos.add(objs.get());
                            }
                        } finally {
                            lock.unlock();
                        }
                    }
                }
                return (MetaObject[][])mos.toArray(new MetaObject[0][]);
            }
            queriesPerTable.put(tableNameIntern, null);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Lock createWriteLock(final String query) {
        final Lock lock = getLock(query).writeLock();
        lock.lock();
        return lock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Lock createReadLock(final String query) {
        final Lock lock = getLock(query).readLock();
        lock.lock();
        return lock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getTableName(final String query) {
        if (query != null) {
            for (final Map.Entry<String, Set<String>> entry : queriesPerTable.entrySet()) {
                final Set<String> queryCodes = entry.getValue();
                if ((queryCodes != null) && queryCodes.contains(query)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public MetaObject[] clearCache(final String query) {
        return clearCache(query, null);
    }

    /**
     * Wipes the cache content for the given query and returns the cache's original content.
     *
     * @param   query   the query for which the cache shall be cleared
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject[] clearCache(final String query, final String domain) {
        if (query != null) {
            final String queryIntern = query.intern();
            final String tableName = getTableName(queryIntern);

            if (tableName != null) {
                final Lock lock = createWriteLock(queryIntern);
                try {
                    final SoftReference<MetaObject[]> objs = cache.put(queryIntern, null);
                    queriesPerTable.remove(tableName, queryIntern);
                    return (objs == null) ? null : objs.get();
                } finally {
                    lock.unlock();
                }
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query   DOCUMENT ME!
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    @Deprecated
    public MetaObject[] getMetaObjectsByQuery(final String query,
            final String domain) throws CacheException {
        return getMetaObjectsByQuery(query, domain, ConnectionContext.createDeprecated());
    }

    /**
     * Get {@link MetaObject}s by query using the very same query string as one would request them from the server. This
     * operation simply calls {@link #getMetaObjectsByQuery(java.lang.String, boolean)} with the given query and <code>
     * false</code> for force reload. It throws an exception to indicate any errors so that it can maintain compliance
     * with a call to {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} with regards to its return
     * value.
     *
     * @param   query              the query to get the <code>MetaObject</code>s for
     * @param   domain             the domai to get the <code>MetaObject</code>s from
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  an array of <code>MetaObject</code>s as they would have been returned from
     *          ConnectionProxy#getMetaObjectByQuery(java.lang.String, int) if used directly
     *
     * @throws  CacheException  if any error occurs, e.g. the server is not reachable if the cache is empty
     *
     * @see     #getMetaObjectsByQuery(java.lang.String, boolean)
     */
    public MetaObject[] getMetaObjectsByQuery(final String query,
            final String domain,
            final ConnectionContext connectionContext) throws CacheException {
        return getMetaObjectsByQuery(query, domain, false, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query        DOCUMENT ME!
     * @param   domain       DOCUMENT ME!
     * @param   forceReload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    @Deprecated
    public MetaObject[] getMetaObjectsByQuery(final String query, final String domain, final boolean forceReload)
            throws CacheException {
        return getMetaObjectsByQuery(query, domain, forceReload, ConnectionContext.createDeprecated());
    }

    /**
     * Get {@link MetaObject}s by query using the very same query string as one would request them from the server. This
     * operation supports forcing of a reload so that callers are assured to receive a current result as it would have
     * been returned from {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)}. It throws an exception to
     * indicate any errors so that it can maintain compliance with a call to
     * {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} with regards to its return value.
     *
     * @param   query              the query to get the <code>MetaObject</code>s for
     * @param   metaClass          DOCUMENT ME!
     * @param   forceReload        force a reload of the <code>MetaObject</code>s if they have already been cached
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  an array of <code>MetaObject</code>s as they would have been returned from
     *          {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} if used directly
     *
     * @throws  CacheException  if any error occurs, e.g. the server is not reachable if the cache is empty or was
     *                          forced to reload
     *
     * @see     ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)
     */
    public MetaObject[] getMetaObjectsByQuery(final String query,
            final MetaClass metaClass,
            final boolean forceReload,
            final ConnectionContext connectionContext) throws CacheException {
        return getMetaObjectsByQuery(
                query,
                (metaClass != null) ? metaClass.getTableName() : null,
                (metaClass != null) ? metaClass.getDomain() : null,
                forceReload,
                connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query              DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    @Deprecated
    public MetaObject[] getMetaObjectsByQuery(final String query,
            final String domain,
            final boolean forceReload,
            final ConnectionContext connectionContext) throws CacheException {
        return getMetaObjectsByQuery(query, null, domain, forceReload, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query              DOCUMENT ME!
     * @param   tableName          DOCUMENT ME!
     * @param   domain             DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    private MetaObject[] getMetaObjectsByQuery(final String query,
            final String tableName,
            final String domain,
            final boolean forceReload,
            final ConnectionContext connectionContext) throws CacheException {
        if (query == null) {
            return null;
        }

        final String queryIntern = query.intern();
        MetaObject[] cachedObjects = null;
        if (!forceReload) {
            final Lock readLock = createReadLock(queryIntern);
            try {
                final SoftReference<MetaObject[]> objs = cache.get(queryIntern);
                cachedObjects = (objs == null) ? null : objs.get();
            } finally {
                readLock.unlock();
            }
        }

        final boolean wasEmpty = cachedObjects == null;
        if (wasEmpty) { // is always empty if forceReload
            final Lock writeLock = createWriteLock(queryIntern);
            try {
                if (!forceReload) {
                    // somebody may have aquired the write lock in the meantime and we're late
                    final SoftReference<MetaObject[]> objs = cache.get(queryIntern);
                    cachedObjects = (objs == null) ? null : objs.get();
                }

                // if this is the case we truly have to load it because there are no objects or there have already been
                // objects in the cache but a reload is forced.
                final boolean stillEmpty = cachedObjects == null;
                if (stillEmpty) {                                           // is always empty if forceReload
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("loading metaobjects: "               // NOI18N
                                        + "[query=" + queryIntern           // NOI18N
                                        + "|cachedObjects=" + cachedObjects // NOI18N
                                        + "|wasEmpty=" + wasEmpty           // NOI18N
                                        + "|forceReload=" + forceReload     // NOI18N
                                        + "]");                             // NOI18N
                        }

                        if (domain != null) {
                            cachedObjects = SessionManager.getProxy()
                                        .getMetaObjectByQuery(SessionManager.getSession().getUser(),
                                                queryIntern,
                                                domain,
                                                connectionContext);
                        } else {
                            cachedObjects = SessionManager.getProxy()
                                        .getMetaObjectByQuery(queryIntern, 0, connectionContext);
                        }

                        final String tableNameIntern = (tableName != null) ? tableName.toLowerCase().intern() : null;
                        Set<String> queries = queriesPerTable.get(tableNameIntern);
                        if (queries == null) {
                            queries = new HashSet<>();
                            queriesPerTable.put(tableNameIntern, queries);
                        }
                        queries.add(queryIntern);

                        cache.put(queryIntern, new SoftReference<>(cachedObjects));
                    } catch (final ConnectionException ex) {
                        final String message = "cannot fetch meta objects for query: " + queryIntern; // NOI18N
                        LOG.error(message, ex);
                        throw new CacheException(queryIntern, message, ex);
                    }
                }
            } finally {
                writeLock.unlock();
            }
        }

        return cachedObjects;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   queryIntern  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private synchronized ReentrantReadWriteLock getLock(final String queryIntern) {
        ReentrantReadWriteLock lock = locks.get(queryIntern);

        if (lock == null) {
            lock = new ReentrantReadWriteLock();
            locks.put(queryIntern, lock);
        }

        return lock;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final MetaObjectCache INSTANCE = new MetaObjectCache();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
