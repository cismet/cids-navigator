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
public class ReconnectorException extends Exception {

    //~ Instance fields --------------------------------------------------------

    private Component component;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReconnectorException object.
     *
     * @param  errorMsg  DOCUMENT ME!
     */
    public ReconnectorException(final String errorMsg) {
        component = new DefaultReconnectorErrorPanel(errorMsg, this);
    }

    /**
     * Creates a new ReconnectorException object.
     *
     * @param  component  DOCUMENT ME!
     */
    public ReconnectorException(final Component component) {
        this.component = component;
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
