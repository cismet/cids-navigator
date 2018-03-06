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
package de.cismet.cids.client.tools;

import java.awt.Component;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ClientConnectionContextUtils {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   component  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ClientConnectionContext getFirstParentClientConnectionContext(final Component component) {
        if (component == null) {
            return null;
        }
        Component child = component;
        Component parent;
        while ((parent = child.getParent()) != null) {
            if (parent instanceof ConnectionContextProvider) {
                final ConnectionContext context = ((ConnectionContextProvider)parent).getConnectionContext();
                if (context instanceof ClientConnectionContext) {
                    return (ClientConnectionContext)context;
                }
            }
            child = parent;
        }
        return ClientConnectionContext.createDeprecated();
    }
}
