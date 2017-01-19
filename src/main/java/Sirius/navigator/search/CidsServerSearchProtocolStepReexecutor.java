/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface CidsServerSearchProtocolStepReexecutor {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void reExecuteSearch();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isReExecuteSearchEnabled();
}
