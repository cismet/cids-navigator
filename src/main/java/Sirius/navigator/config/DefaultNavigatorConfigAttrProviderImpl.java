/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.config;

import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;


import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.tools.configuration.ConfigAttrProvider;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = de.cismet.tools.configuration.ConfigAttrProvider.class)
public final class DefaultNavigatorConfigAttrProviderImpl implements ConfigAttrProvider, ConnectionContextStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DefaultNavigatorConfigAttrProviderImpl.class);

    //~ Instance fields --------------------------------------------------------

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }

    @Override
    public String getUserConfigAttr(final String key) {
        final ConnectionSession session = SessionManager.getSession();
        if (session == null) {
            throw new IllegalStateException("no session available"); // NOI18N
        }

        try {
            return session.getConnection().getConfigAttr(session.getUser(), key, getConnectionContext());
        } catch (final ConnectionException ex) {
            LOG.error("could not get user config attr for key: " + key, ex); // NOI18N
            return null;
        }
    }

    @Override
    public String getGroupConfigAttr(final String key) {
        // there was more code here before, cycling through the potentialUsergroups
        // and checking each group individually. This made no sense, as the server
        // is already doing this (more efficiently)
        return getUserConfigAttr(key);
    }

    @Override
    public String getDomainConfigAttr(final String key) {
        // there was more code here before, cycling through the potentialUsergroups
        // and checking each group individually. This made no sense, as the server
        // is already doing this (more efficiently)
        return getUserConfigAttr(key);
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
