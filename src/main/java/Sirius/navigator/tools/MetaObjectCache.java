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

import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import java.lang.ref.SoftReference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
    private final transient Map<Integer, SoftReference<MetaObject[]>> cache;
    private final transient Map<Integer, ReentrantReadWriteLock> locks;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaSearchCache object.
     */
    private MetaObjectCache() {
        cache = new HashMap<>();
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
     * DOCUMENT ME!
     *
     * @param       query  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use {@link #getMetaObjectsByQuery(java.lang.String)} instead
     */
    public MetaObject[] get(final String query) {
        try {
            getLock(query.intern().hashCode()).readLock().lock();

            final SoftReference<MetaObject[]> objs = cache.get(query.intern().hashCode());

            return (objs == null) ? null : objs.get();
        } finally {
            getLock(query.intern().hashCode()).readLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param       query  DOCUMENT ME!
     * @param       value  DOCUMENT ME!
     *
     * @deprecated  altering cache entries manually is highly discouraged
     */
    public void put(final String query, final MetaObject[] value) {
        try {
            getLock(query.intern().hashCode()).writeLock().lock();

            cache.put(query.intern().hashCode(), new SoftReference<MetaObject[]>(value));
        } finally {
            getLock(query.intern().hashCode()).writeLock().unlock();
        }
    }

    /**
     * Completely wipes the cache content.
     */
    public synchronized void clearCache() {
        try {
            final Collection<ReentrantReadWriteLock> c = getAllLocks();

            for (final ReentrantReadWriteLock tmp : c) {
                tmp.writeLock().lock();
            }

            cache.clear();
        } finally {
            final Collection<ReentrantReadWriteLock> c = getAllLocks();

            for (final ReentrantReadWriteLock tmp : c) {
                tmp.writeLock().unlock();
            }
        }
    }

    /**
     * Wipes the cache content for the given query and returns the cache's original content.
     *
     * @param   query  the query for which the cache shall be cleared
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject[] clearCache(final String query) {
        try {
            getLock(query.intern().hashCode()).writeLock().lock();

            final SoftReference<MetaObject[]> objs = cache.put(query.intern().hashCode(), null);

            return (objs == null) ? null : objs.get();
        } finally {
            getLock(query.intern().hashCode()).writeLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param       query  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use {@link #getMetaObjectsByQuery(java.lang.String)} instead
     */
    public MetaObject[] getMetaObjectByQuery(final String query) {
        try {
            return getMetaObjectsByQuery(query, false);
        } catch (final CacheException e) {
            // this is only to maintain compliance with the previous API behaviour
            LOG.warn("exception in cache, returning empty array", e); // NOI18N

            return new MetaObject[0];
        }
    }

    /**
     * Get {@link MetaObject}s by query using the very same query string as one would request them from the server. This
     * operation simply calls {@link #getMetaObjectsByQuery(java.lang.String, boolean)} with the given query and <code>
     * false</code> for force reload. It throws an exception to indicate any errors so that it can maintain compliance
     * with a call to {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} with regards to its return
     * value.
     *
     * @param   query  the query to get the <code>MetaObject</code>s for
     *
     * @return  an array of <code>MetaObject</code>s as they would have been returned from
     *          ConnectionProxy#getMetaObjectByQuery(java.lang.String, int) if used directly
     *
     * @throws  CacheException  if any error occurs, e.g. the server is not reachable if the cache is empty
     *
     * @see     #getMetaObjectsByQuery(java.lang.String, boolean)
     */
    @Deprecated
    public MetaObject[] getMetaObjectsByQuery(final String query) throws CacheException {
        return getMetaObjectsByQuery(query, null, false);
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
    public MetaObject[] getMetaObjectsByQuery(final String query, final String domain) throws CacheException {
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
        return getMetaObjectsByQuery(query, domain, false);
    }

    /**
     * Get {@link MetaObject}s by query using the very same query string as one would request them from the server. This
     * operation supports forcing of a reload so that callers are assured to receive a current result as it would have
     * been returned from {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)}. It throws an exception to
     * indicate any errors so that it can maintain compliance with a call to
     * {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} with regards to its return value.
     *
     * @param   query        the query to get the <code>MetaObject</code>s for
     * @param   forceReload  force a reload of the <code>MetaObject</code>s if they have already been cached
     *
     * @return  an array of <code>MetaObject</code>s as they would have been returned from
     *          {@link ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)} if used directly
     *
     * @throws  CacheException  if any error occurs, e.g. the server is not reachable if the cache is empty or was
     *                          forced to reload
     *
     * @see     ConnectionProxy#getMetaObjectByQuery(java.lang.String, int)
     */
    @Deprecated
    public MetaObject[] getMetaObjectsByQuery(final String query, final boolean forceReload) throws CacheException {
        return getMetaObjectsByQuery(query, null, forceReload);
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
     * @param   domain             the domain to get the <code>MetaObject</code>s from
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
            final String domain,
            final boolean forceReload,
            final ConnectionContext connectionContext) throws CacheException {
        if (query == null) {
            return null;
        }

        final String iQuery = query.intern();
        final Integer qHash = (iQuery + ((domain != null) ? ("@" + domain) : "")).hashCode();
        MetaObject[] cachedObjects = null;
        Lock lock = null;
        try {
            lock = getLock(qHash).readLock();
            lock.lock();

            SoftReference<MetaObject[]> objs = cache.get(qHash);
            cachedObjects = (objs == null) ? null : objs.get();

            if ((cachedObjects == null) || forceReload) {
                lock.unlock();
                lock = getLock(qHash).writeLock();
                lock.lock();

                final boolean wasEmpty = cachedObjects == null;

                // somebody may have aquired the write lock in the meantime and we're late
                objs = cache.get(qHash);
                cachedObjects = (objs == null) ? null : objs.get();

                // if this is the case we truly have to load it because there are no objects or there have already been
                // objects in the cache but a reload is forced. in case of the write lock was aquired in the mean time
                // and the cache has been filled by another thread and the forceReload is true, we don't want to reload
                // (because it has already been done by the other thread)
                if ((cachedObjects == null) || (!wasEmpty && forceReload)) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("loading metaobjects: " // NOI18N
                                        + "[query=" + iQuery // NOI18N
                                        + "|qHash=" + qHash // NOI18N
                                        + "|cachedObjects=" + cachedObjects // NOI18N
                                        + "|wasEmpty=" + wasEmpty // NOI18N
                                        + "|forceReload=" + forceReload // NOI18N
                                        + "]");         // NOI18N
                        }

                        if (domain != null) {
                            cachedObjects = SessionManager.getProxy()
                                        .getMetaObjectByQuery(SessionManager.getSession().getUser(),
                                                query,
                                                domain,
                                                connectionContext);
                        } else {
                            cachedObjects = SessionManager.getProxy()
                                        .getMetaObjectByQuery(iQuery, 0, connectionContext);
                        }
                        cache.put(qHash, new SoftReference<MetaObject[]>(cachedObjects));
                    } catch (final ConnectionException ex) {
                        final String message = "cannot fetch meta objects for query: " + iQuery; // NOI18N
                        LOG.error(message, ex);
                        throw new CacheException(iQuery, message, ex);
                    }
                }
            }
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

        return cachedObjects;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   hash  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private synchronized ReentrantReadWriteLock getLock(final int hash) {
        ReentrantReadWriteLock lock = locks.get(hash);

        if (lock == null) {
            lock = new ReentrantReadWriteLock();
            locks.put(hash, lock);
        }

        return lock;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private synchronized Collection<ReentrantReadWriteLock> getAllLocks() {
        return locks.values();
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
