/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaClass;

import java.util.HashMap;

import de.cismet.cids.utils.MetaClassUtils;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class ClassCacheMultiple {

    //~ Static fields/initializers ---------------------------------------------

    private static HashMap<String, HashMap> allClassCaches = new HashMap<String, HashMap>();
    private static HashMap<String, HashMap> allTableNameClassCaches = new HashMap<String, HashMap>();
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClassCacheMultiple.class);

    //~ Methods ----------------------------------------------------------------

    @Deprecated
    public static HashMap getClassKeyHashtableOfClassesForOneDomain(final String domain) {
        return getClassKeyHashtableOfClassesForOneDomain(domain, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static HashMap getClassKeyHashtableOfClassesForOneDomain(final String domain, final ConnectionContext connectionContext) {
        HashMap ret = allClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain, connectionContext);
                ret = allClassCaches.get(domain);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error in setInstance of ClassCacheMultiple", e); // NOI18N
                }
            }
        }
        return ret;
    }

    @Deprecated
    public static HashMap getTableNameHashtableOfClassesForOneDomain(final String domain) {
        return getTableNameHashtableOfClassesForOneDomain(domain, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static HashMap getTableNameHashtableOfClassesForOneDomain(final String domain, final ConnectionContext connectionContext) {
        HashMap ret = allTableNameClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain, connectionContext);
                ret = allTableNameClassCaches.get(domain);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error in setInstance of ClassCacheMultiple", e); // NOI18N
                }
            }
        }
        return ret;
    }

    @Deprecated
    public static MetaClass getMetaClass(final String domain, final String tableName) {
        return getMetaClass(domain, tableName, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   domain     DOCUMENT ME!
     * @param   tableName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaClass getMetaClass(final String domain, final String tableName, final ConnectionContext connectionContext) {
        try {
            final HashMap ht = getTableNameHashtableOfClassesForOneDomain(domain, connectionContext);
            return (MetaClass)ht.get(tableName.toLowerCase());
        } catch (Exception e) {
            log.warn("Couldn't get Class for Table " + tableName + "@" + domain, e); // NOI18N
            return null;
        }
    }

    @Deprecated
    public static MetaClass getMetaClass(final String domain, final int classId) {
        return getMetaClass(domain, classId, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   domain   DOCUMENT ME!
     * @param   classId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaClass getMetaClass(final String domain, final int classId, final ConnectionContext connectionContext) {
        return (MetaClass)ClassCacheMultiple.getClassKeyHashtableOfClassesForOneDomain(domain, connectionContext).get(domain + classId);
    }

    public static void setInstance(final String domain) {    
        setInstance(domain, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param  domain  DOCUMENT ME!
     */
    public static void setInstance(final String domain, final ConnectionContext connectionContext) {
        try {
            final MetaClass[] mcArr = SessionManager.getConnection()
                        .getClasses(SessionManager.getSession().getUser(), domain, connectionContext);
            allClassCaches.put(domain, MetaClassUtils.getClassHashtable(mcArr, domain));
            allTableNameClassCaches.put(domain, MetaClassUtils.getClassByTableNameHashtable(mcArr));
        } catch (ConnectionException connectionException) {
            log.error("Error in setInstance of ClassCacheMultiple", connectionException); // NOI18N
        }
    }

    @Deprecated
    public static void addInstance(final String domain) {
        addInstance(domain, ConnectionContext.createDeprecated());
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param  domain  DOCUMENT ME!
     */
    public static void addInstance(final String domain, final ConnectionContext connectionContext) {
        try {
            final MetaClass[] mcArr = SessionManager.getConnection()
                        .getClasses(SessionManager.getSession().getUser(), domain, connectionContext);
            allClassCaches.put(domain, MetaClassUtils.getClassHashtable(mcArr, domain));
            allTableNameClassCaches.put(domain, MetaClassUtils.getClassByTableNameHashtable(mcArr));
        } catch (ConnectionException connectionException) {
            log.error("Error in setInstance of ClassCacheMultiple", connectionException); // NOI18N
        }
    }

}
