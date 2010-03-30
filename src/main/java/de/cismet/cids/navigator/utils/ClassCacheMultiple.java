/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.server.middleware.types.MetaClass;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author thorsten
 */
public class ClassCacheMultiple {

    private static HashMap<String, Hashtable> allClassCaches = new HashMap<String, Hashtable>();
    private static HashMap<String, Hashtable> allTableNameClassCaches = new HashMap<String, Hashtable>();
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClassCacheMultiple.class);

    public static Hashtable getClassKeyHashtableOfClassesForOneDomain(String domain) {
        Hashtable ret = allClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain);
                ret = allClassCaches.get(domain);
            } catch (Exception e) {
                log.debug("Fehler in setInstance vom ClassCacheMultiple", e);
            }
        }
        return ret;
    }

    public static Hashtable getTableNameHashtableOfClassesForOneDomain(String domain) {
        Hashtable ret = allTableNameClassCaches.get(domain);
        if (ret == null) {
            try {
                addInstance(domain);
                ret = allTableNameClassCaches.get(domain);
            } catch (Exception e) {
                log.debug("Fehler in setInstance vom ClassCacheMultiple", e);
            }
        }
        return ret;
    }

    public static MetaClass getMetaClass(String domain, String tableName) {
        try {
            final Hashtable ht = getTableNameHashtableOfClassesForOneDomain(domain);
            return (MetaClass) ht.get(tableName.toLowerCase());
        } catch (Exception e) {
            log.warn("Couldn't get Class for Table " + tableName + "@" + domain, e);
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
            log.error("Fehler in setInstance vom ClassCacheMultiple", connectionException);
        }
    }

    public static void addInstance(String domain) {
        try {
            MetaClass[] mcArr = SessionManager.getConnection().getClasses(SessionManager.getSession().getUser(), domain);
            allClassCaches.put(domain, getClassHashtable(mcArr, domain));
            allTableNameClassCaches.put(domain, getClassByTableNameHashtable(mcArr));
        } catch (ConnectionException connectionException) {
            log.error("Fehler in setInstance vom ClassCacheMultiple", connectionException);
        }
    }

    private static Hashtable getClassHashtable(MetaClass[] classes, String localServerName) {
        Hashtable classHash = new Hashtable();
        for (int i = 0; i < classes.length; i++) {
            String key = new String(localServerName + classes[i].getID());
            if (!classHash.containsKey(key)) {
                classHash.put(key, classes[i]);
            }
        }
        return classHash;
    }

    private static Hashtable getClassByTableNameHashtable(MetaClass[] classes) {
        Hashtable classHash = new Hashtable();
        for (MetaClass mc : classes) {
            String key = mc.getTableName().toLowerCase();
            if (!classHash.containsKey(key)) {
                classHash.put(key, mc);
            }
        }
        return classHash;
    }
}
