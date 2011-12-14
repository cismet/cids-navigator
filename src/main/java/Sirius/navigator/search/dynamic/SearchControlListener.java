/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.CidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public interface SearchControlListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    CidsServerSearch assembleSearch();
    /**
     * DOCUMENT ME!
     */
    void searchStarted();
    /**
     * DOCUMENT ME!
     *
     * @param  result  DOCUMENT ME!
     */
    void searchDone(final Node[] result);
    /**
     * DOCUMENT ME!
     */
    void searchCancelled();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean displaysEmptyResultMessage();
}
