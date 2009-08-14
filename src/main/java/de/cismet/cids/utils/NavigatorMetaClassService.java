/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.utils;

import Sirius.server.middleware.types.MetaClass;
import java.util.Hashtable;

/**
 *
 * @author thorsten
 */
@org.openide.util.lookup.ServiceProvider(service=MetaClassCacheService.class)
public class NavigatorMetaClassService implements MetaClassCacheService{
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    public NavigatorMetaClassService(){
        log.fatal("inited");
    }

    public Hashtable getAllClasses(String domain) {
        return ClassCacheMultiple.getClassKeyHashtableOfClassesForOneDomain(domain);
    }

    public MetaClass getMetaClass(String domain, String tableName) {
        return ClassCacheMultiple.getMetaClass(domain, tableName);
    }

    public MetaClass getMetaClass(String domain, int classId) {
        return ClassCacheMultiple.getMetaClass(domain, classId);
    }



}
