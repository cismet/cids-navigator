/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.utils.multibean;

import java.beans.PropertyChangeListener;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface MultiBeanHelperListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void refillAllEqualsMapStarted();

    /**
     * DOCUMENT ME!
     */
    void refillAllEqualsMapDone();

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  allEquals     DOCUMENT ME!
     */
    void allEqualsChanged(final String propertyName, final boolean allEquals);
}
