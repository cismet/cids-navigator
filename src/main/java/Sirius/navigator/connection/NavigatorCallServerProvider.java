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

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.CallServerServiceProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = CallServerServiceProvider.class)
public class NavigatorCallServerProvider implements CallServerServiceProvider {

    //~ Methods ----------------------------------------------------------------

    @Override
    public CallServerService getCallServerService() {
        return SessionManager.getSession().getConnection().getCallServerService();
    }
}
