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
package de.cismet.cids.navigatorstartuphooks;

import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cids.servermessage.CidsServerMessageNotifier;
import de.cismet.cids.servermessage.CidsServerMessageNotifierListener;
import de.cismet.cids.servermessage.CidsServerMessageNotifierListenerEvent;

import de.cismet.tools.configuration.StartupHook;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = StartupHook.class)
public class CidsServerMessageStartUpHook implements StartupHook {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CidsServerMessageStartUpHook.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public void applicationStarted() {
        // logger
        CidsServerMessageNotifier.getInstance().subscribe(new CidsServerMessageNotifierListener() {

                @Override
                public void messageRetrieved(final CidsServerMessageNotifierListenerEvent event) {
                    if ((event != null) && (event.getMessage() != null)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                event.getMessage().getId()
                                        + " ("
                                        + event.getMessage().getCategory()
                                        + "): "
                                        + event.getMessage().getMessage());
                        }
                    }
                }
            }, null);
        CidsServerMessageNotifier.getInstance().start();
    }
}
