/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import Sirius.server.middleware.types.LightweightMetaObject;
import Sirius.server.middleware.types.MetaObjectNode;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cids.servermessage.CidsServerMessageNotifier;
import de.cismet.cids.servermessage.CidsServerMessageNotifierListener;
import de.cismet.cids.servermessage.CidsServerMessageNotifierListenerEvent;

import de.cismet.tools.configuration.StartupHook;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = StartupHook.class)
public class LwmoCacheInvalidationStartupHook implements StartupHook {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void applicationStarted() {
        CidsServerMessageNotifier.getInstance()
                .subscribe(new CidsServerMessageNotifierListener() {

                        @Override
                        public void messageRetrieved(final CidsServerMessageNotifierListenerEvent event) {
                            final MetaObjectNode mon = (MetaObjectNode)event.getMessage().getContent();
                            if (mon != null) {
                                LightweightMetaObject.invalidateCacheFor(
                                    mon.getDomain(),
                                    mon.getClassId(),
                                    mon.getObjectId());
                            }
                        }
                    }, LightweightMetaObject.CACHE_INVALIDATION_MESSAGE);
    }
}
