/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.newuser.User;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.utils.CidsBeanPersistService;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsBeanPersistService.class)
public class NavigatorCidsBeanPersistService implements CidsBeanPersistService, ConnectionContextStore {

    //~ Instance fields --------------------------------------------------------

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Methods ----------------------------------------------------------------

    @Override
    public CidsBean persistCidsBean(final CidsBean cidsBean) throws Exception {
        final MetaObject metaObject = cidsBean.getMetaObject();
        final String domain = metaObject.getDomain();
        final User user = SessionManager.getSession().getUser();

        if (metaObject.getStatus() == MetaObject.MODIFIED) {
            SessionManager.getConnection().updateMetaObject(user, metaObject, domain, getConnectionContext());

            return SessionManager.getConnection()
                        .getMetaObject(
                                user,
                                metaObject.getID(),
                                metaObject.getClassID(),
                                domain,
                                getConnectionContext())
                        .getBean();
        } else if (metaObject.getStatus() == MetaObject.TO_DELETE) {
            SessionManager.getConnection().deleteMetaObject(user, metaObject, domain, getConnectionContext());

            return null;
        } else if (metaObject.getStatus() == MetaObject.NEW) {
            final MetaObject mo = SessionManager.getConnection()
                        .insertMetaObject(user, metaObject, domain, getConnectionContext());

            // mo == null shall never occur
            assert mo != null : "illegal state: insert metaobject returned null"; // NOI18N

            return mo.getBean();
        } else {
            // [issue:2585] nothing to do, persist was called on a bean that has not been modified
            return cidsBean;
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    @Override
    public void initWithConnectionContext(final ConnectionContext clientconnectionContext) {
        this.connectionContext = clientconnectionContext;
    }
}
