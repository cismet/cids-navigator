/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.interfaces;

import Sirius.navigator.method.MultithreadedMethod;
import Sirius.navigator.plugin.context.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class MultithreadedPluginMethod extends MultithreadedMethod implements PluginMethod {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginMultithreadedMethod.
     *
     * @param  progressObserver  DOCUMENT ME!
     */
    public MultithreadedPluginMethod(final PluginProgressObserver progressObserver) {
        super(progressObserver);
    }
}
