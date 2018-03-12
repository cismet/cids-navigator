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
        final ConnectionSession session = SessionManager.getSession();
        if (session == null) {
            throw new IllegalStateException("no session available"); // NOI18N
        }

        final User user = session.getUser();
        final UserGroup userGroup = user.getUserGroup();
        if (userGroup != null) {
            return getGroupConfigAttr(key, user.getDomain(), userGroup);
        } else {
            for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                final String confAttr = getGroupConfigAttr(key, user.getDomain(), potentialUserGroup);
                if (confAttr != null) {
                    return confAttr;
                }
            }
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key         DOCUMENT ME!
     * @param   userDomain  DOCUMENT ME!
     * @param   userGroup   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getGroupConfigAttr(final String key, final String userDomain, final UserGroup userGroup) {
        final ConnectionSession session = SessionManager.getSession();
        try {
            final User queryUser = new User(-1, "", userDomain, userGroup); // NOI18N

            return session.getConnection().getConfigAttr(queryUser, key, getConnectionContext());
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

        final User user = session.getUser();
        final UserGroup userGroup = user.getUserGroup();
        if (userGroup != null) {
            return getDomainConfigAttr(key, user.getDomain(), userGroup);
        } else {
            for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                final String confAttr = getDomainConfigAttr(key, user.getDomain(), potentialUserGroup);
                if (confAttr != null) {
                    return confAttr;
                }
            }
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key         DOCUMENT ME!
     * @param   userDomain  DOCUMENT ME!
     * @param   userGroup   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getDomainConfigAttr(final String key, final String userDomain, final UserGroup userGroup) {
        final ConnectionSession session = SessionManager.getSession();
        try {
            final UserGroup queryUg = new UserGroup(-1, "", userGroup.getDomain()); // NOI18N
            final User queryUser = new User(-1, "", userDomain, queryUg);           // NOI18N

            return session.getConnection().getConfigAttr(queryUser, key, getConnectionContext());
        } catch (final ConnectionException e) {
            LOG.error("could not get domain config attr for key: " + key, e); // NOI18N
            return null;
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
