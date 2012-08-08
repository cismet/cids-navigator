/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface ReconnectorListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void connecting();

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void connectionFailed(ReconnectorEvent event);

    /**
     * DOCUMENT ME!
     */
    void connectionCanceled();

    /**
     * DOCUMENT ME!
     */
    void connectionCompleted();
}
