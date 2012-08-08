/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector;

import java.awt.Component;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ReconnectorEvent {

    //~ Instance fields --------------------------------------------------------

    private final Component component;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReconnectorEvent object.
     *
     * @param  panel  DOCUMENT ME!
     */
    public ReconnectorEvent(final Component panel) {
        this.component = panel;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Component getComponent() {
        return component;
    }
}
