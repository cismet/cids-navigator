/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.tools.BrowserLauncher;

import de.cismet.tools.configuration.StartupHook;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = StartupHook.class)
public class DefaultStartupHook implements StartupHook {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void applicationStarted() {
        BrowserLauncher.initializeDesktop();
    }
}
