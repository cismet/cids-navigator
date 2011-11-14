/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MetaObjectCache {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(MetaObjectCache.class);
    private static final MetaObjectCache INSTANCE = new MetaObjectCache();

    //~ Instance fields --------------------------------------------------------

    private final List<String> processingQueries = Collections.synchronizedList(new ArrayList<String>());
    private Map<String, MetaObject[]> cache = Collections.synchronizedMap(new HashMap<String, MetaObject[]>());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaSearchCache object.
     */
    private MetaObjectCache() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaObjectCache getInstance() {
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject[] get(final String key) {
        return cache.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  key    DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    public void put(final String key, final MetaObject[] value) {
        cache.put(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject[] getMetaObjectByQuery(final String query) {
        MetaObject[] result = cache.get(query);

        if (result == null) {
            if (processingQueries.contains(query)) {
                while ((cache.get(query) == null) && processingQueries.contains(query)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        // nothing to do
                    }
                }

                return getMetaObjectByQuery(query);
            } else {
                try {
                    processingQueries.add(query);
                    result = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
                    cache.put(query, result);
                    processingQueries.remove(query);
                    return result;
                } catch (ConnectionException e) {
                    LOG.error("Error while loading the objects with query: " + query, e); // NOI18N
                    return new MetaObject[0];
                }
            }
        } else {
            return result;
        }
    }
}
