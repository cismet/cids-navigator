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

import Sirius.server.newuser.User;
import Sirius.server.newuser.UserGroup;

import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.tools.configuration.ConfigAttrProvider;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = de.cismet.tools.configuration.ConfigAttrProvider.class)
public final class DefaultNavigatorConfigAttrProviderImpl implements ConfigAttrProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DefaultNavigatorConfigAttrProviderImpl.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getUserConfigAttr(final String key) {
        final ConnectionSession session = SessionManager.getSession();
        if (session == null) {
            throw new IllegalStateException("no session available"); // NOI18N
        }

        try {
            return session.getConnection().getConfigAttr(session.getUser(), key);
        } catch (final ConnectionException ex) {
            LOG.error("could not get user config attr for key: " + key, ex); // NOI18N
            return null;
        }
    }

    @Override
    public String getGroupConfigAttr(final String key) {
        final ConnectionSession session = SessionManager.getSession();
        if (session == null) {
            throw new IllegalStateException("no session available"); // NOI18N
        }

        try {
            final User user = session.getUser();
            final User queryUser = new User(-1, "", user.getDomain(), user.getUserGroup()); // NOI18N

            return session.getConnection().getConfigAttr(queryUser, key);
        } catch (final ConnectionException e) {
            LOG.error("could not get group config attr for key: " + key, e); // NOI18N
            return null;
        }
    }

    @Override
    public String getDomainConfigAttr(final String key) {
        final ConnectionSession session = SessionManager.getSession();
        if (session == null) {
            throw new IllegalStateException("no session available"); // NOI18N
        }

        try {
            final User user = session.getUser();
            final UserGroup queryUg = new UserGroup(-1, "", user.getUserGroup().getDomain()); // NOI18N
            final User queryUser = new User(-1, "", user.getDomain(), queryUg);               // NOI18N

            return session.getConnection().getConfigAttr(queryUser, key);
        } catch (final ConnectionException e) {
            LOG.error("could not get domain config attr for key: " + key, e); // NOI18N
            return null;
        }
    }
}
