/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.server.middleware.types.MetaClass;
import java.util.HashMap;

/**
 *
 * @author thorsten
 */
public class ClassCacheMultiple {

    private static HashMap<String, HashMap> allClassCaches = new HashMap<String, HashMap>();
    private static HashMap<String, HashMap> allTableNameClassCaches = new HashMap<String, HashMap>();
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClassCacheMultiple.class);

    public static HashMap getClassKeyHashtableOfClassesForOneDomain(String domain) {
        HashMap ret = allClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain);
                ret = allClassCaches.get(domain);
            } catch (Exception e) {
                log.debug("Error in setInstance of ClassCacheMultiple", e);//NOI18N
            }
        }
        return ret;
    }

    public static HashMap getTableNameHashtableOfClassesForOneDomain(String domain) {
        HashMap ret = allTableNameClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain);
                ret = allTableNameClassCaches.get(domain);
            } catch (Exception e) {
                log.debug("Error in setInstance of ClassCacheMultiple", e);//NOI18N
            }
        }
        return ret;
    }

    public static MetaClass getMetaClass(String domain, String tableName) {
        try {
            final HashMap ht = getTableNameHashtableOfClassesForOneDomain(domain);
            return (MetaClass) ht.get(tableName.toLowerCase());
        } catch (Exception e) {
            log.warn("Couldn't get Class for Table " + tableName + "@" + domain, e);//NOI18N
            return null;
        }
    }

    public static MetaClass getMetaClass(String domain, int classId) {
        return (MetaClass) ClassCacheMultiple.getClassKeyHashtableOfClassesForOneDomain(domain).get(domain + classId);
    }

    public static void setInstance(String domain) {
        try {
            MetaClass[] mcArr = SessionManager.getConnection().getClasses(SessionManager.getSession().getUser(), domain);
            allClassCaches.put(domain, getClassHashtable(mcArr, domain));
            allTableNameClassCaches.put(domain, getClassByTableNameHashtable(mcArr));
        } catch (ConnectionException connectionException) {
            log.error("Error in setInstance of ClassCacheMultiple", connectionException);//NOI18N
        }
    }

    public static void addInstance(String domain) {
        try {
            MetaClass[] mcArr = SessionManager.getConnection().getClasses(SessionManager.getSession().getUser(), domain);
            allClassCaches.put(domain, getClassHashtable(mcArr, domain));
            allTableNameClassCaches.put(domain, getClassByTableNameHashtable(mcArr));
        } catch (ConnectionException connectionException) {
            log.error("Error in setInstance of ClassCacheMultiple", connectionException);//NOI18N
        }
    }

    private static HashMap getClassHashtable(MetaClass[] classes, String localServerName) {
        HashMap classHash = new HashMap();
        for (int i = 0; i < classes.length; i++) {
            String key = localServerName + classes[i].getID();
            if (!classHash.containsKey(key)) {
                classHash.put(key, classes[i]);
            }
        }
        return classHash;
    }

    private static HashMap getClassByTableNameHashtable(MetaClass[] classes) {
        HashMap classHash = new HashMap();
        for (MetaClass mc : classes) {
            String key = mc.getTableName().toLowerCase();
            if (!classHash.containsKey(key)) {
                classHash.put(key, mc);
            }
        }
        return classHash;
    }
}
