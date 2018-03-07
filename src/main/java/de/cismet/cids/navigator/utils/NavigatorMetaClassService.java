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
import de.cismet.connectioncontext.AbstractConnectionContext.Category;
import de.cismet.connectioncontext.ConnectionContextStore;

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
public class NavigatorMetaClassService implements MetaClassCacheService, ConnectionContextStore {

    //~ Instance fields --------------------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NavigatorMetaClassService.class);

    //~ Constructors -----------------------------------------------------------

    private ConnectionContext connectionContext = ConnectionContext.createDummy();
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
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }
    
    @Override
    public HashMap getAllClasses(final String domain) {
        return ClassCacheMultiple.getClassKeyHashtableOfClassesForOneDomain(domain, getConnectionContext());
    }

    @Override
    public MetaClass getMetaClass(final String domain, final String tableName) {
        return ClassCacheMultiple.getMetaClass(domain, tableName, getConnectionContext());
    }

    @Override
    public MetaClass getMetaClass(final String domain, final int classId) {
        return ClassCacheMultiple.getMetaClass(domain, classId, getConnectionContext());
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
