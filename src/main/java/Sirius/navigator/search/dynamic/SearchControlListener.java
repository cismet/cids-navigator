/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

/**
 * A listener which is used by SearchControlPanel to customize the search process.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public interface SearchControlListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Has to assemble a AbstractCidsServerSearch which corresponds to the current user input.
     *
     * @return  A AbstractCidsServerSearch object reflecting the user input.
     */
    MetaObjectNodeServerSearch assembleSearch();
    /**
     * Is invoked by SearchControlPanel as soon as the user clicks the search button.
     */
    void searchStarted();
    /**
     * Is invoked by SearchControlPanel when the search is done. This means whether the search is done and didn't
     * provide a result or the SearchResultsTree is updated.
     *
     * @param  numberOfResults  Count of result objects.
     */
    void searchDone(int numberOfResults);
    /**
     * Invoked by SearchControlPanel when the user canceled the search.
     */
    void searchCanceled();
    /**
     * Tells the SearchControlPanel to suppress or display a message if no result was found.
     *
     * @return  <code>false</code> if SearchControlPanel should display a message if the search didn't provide a result.
     */
    boolean suppressEmptyResultMessage();
}
