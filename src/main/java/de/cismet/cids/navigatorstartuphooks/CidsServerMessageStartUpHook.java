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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import java.io.IOException;

import de.cismet.cids.client.tools.RemoteLog4JConfig;

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
                                        + event.getMessage().getContent());
                        }
                    }
                }
            }, null);

        CidsServerMessageNotifier.getInstance()
                .subscribe(new CidsServerMessageNotifierListener() {

                        @Override
                        public void messageRetrieved(final CidsServerMessageNotifierListenerEvent event) {
                            try {
                                final RemoteLog4JConfig remoteConfig =
                                    new ObjectMapper().readValue(
                                        (String)event.getMessage().getContent(),
                                        RemoteLog4JConfig.class);

                                Log4JQuickConfig.configure4LumbermillOn(
                                    remoteConfig.getRemoteHost(),
                                    remoteConfig.getRemotePort(),
                                    remoteConfig.getLogLevel());
                            } catch (IOException ex) {
                                LOG.warn(ex, ex);
                            }
                        }
                    }, "log4j_remote_config");

        CidsServerMessageNotifier.getInstance().start();
    }
}
