/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.newuser.User;

import lombok.Getter;

import org.apache.log4j.Logger;

import de.cismet.connectioncontext.AbstractConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class WorkingSpaceHandler implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(WorkingSpaceHandler.class);
    private static final String CONF_ATTR = "navigator.workingspace.enabled";

    //~ Instance fields --------------------------------------------------------

    @Getter private final boolean enabled;
    @Getter private final ConnectionContext connectionContext = ConnectionContext.create(
            AbstractConnectionContext.Category.STATIC,
            this.getClass().getSimpleName());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WorkingSpaceHandler object.
     */
    private WorkingSpaceHandler() {
        boolean enabled = false;
        try {
            final User user = SessionManager.getSession().getUser();
            enabled = SessionManager.getProxy().hasConfigAttr(user, CONF_ATTR, getConnectionContext());
        } catch (final ConnectionException ex) {
            LOG.error(String.format("error while checking conf_attr %s working space disabled", CONF_ATTR), ex);
        }
        this.enabled = enabled;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static WorkingSpaceHandler getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final WorkingSpaceHandler INSTANCE = new WorkingSpaceHandler();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
