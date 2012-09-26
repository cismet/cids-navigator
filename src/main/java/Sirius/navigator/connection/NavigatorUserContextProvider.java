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
package Sirius.navigator.connection;

import Sirius.server.newuser.User;
import Sirius.server.newuser.UserContextProvider;

import org.openide.util.lookup.ServiceProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = UserContextProvider.class)
public class NavigatorUserContextProvider implements UserContextProvider {

    //~ Methods ----------------------------------------------------------------

    @Override
    public User getUser() {
        return SessionManager.getSession().getUser();
    }
}
