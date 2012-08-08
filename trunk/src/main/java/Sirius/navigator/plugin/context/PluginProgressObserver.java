/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginProgressObserver.java
 *
 * Created on 12. Mai 2003, 13:56
 */
package Sirius.navigator.plugin.context;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginProgressObserver extends Sirius.navigator.ui.progress.ProgressObserver {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginProgressObserver.
     *
     * @param  name  DOCUMENT ME!
     */

    public PluginProgressObserver(final String name) {
        super();
        this.setName(name);
    }
}
