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

import Sirius.server.middleware.types.MetaClass;

import java.util.HashMap;

import de.cismet.cids.utils.MetaClassCacheService;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(
    service = MetaClassCacheService.class,
    position = 100
)
public class NavigatorMetaClassService implements MetaClassCacheService {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            NavigatorMetaClassService.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorMetaClassService object.
     */
    public NavigatorMetaClassService() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("inited"); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    @Deprecated
    public HashMap getAllClasses(final String domain) {
        return getAllClasses(domain, ConnectionContext.createDeprecated());
    }

    @Override
    public HashMap getAllClasses(final String domain, final ConnectionContext connectionContext) {
        return ClassCacheMultiple.getClassKeyHashtableOfClassesForOneDomain(domain, connectionContext);
    }

    @Override
    @Deprecated
    public MetaClass getMetaClass(final String domain, final String tableName) {
        return getMetaClass(domain, tableName, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass getMetaClass(final String domain,
            final String tableName,
            final ConnectionContext connectionContext) {
        return ClassCacheMultiple.getMetaClass(domain, tableName, connectionContext);
    }

    @Override
    @Deprecated
    public MetaClass getMetaClass(final String domain, final int classId) {
        return getMetaClass(domain, classId, ConnectionContext.createDeprecated());
    }

    @Override
    public MetaClass getMetaClass(final String domain, final int classId, final ConnectionContext connectionContext) {
        return ClassCacheMultiple.getMetaClass(domain, classId, connectionContext);
    }
}
