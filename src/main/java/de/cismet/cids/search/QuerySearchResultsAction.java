/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class QuerySearchResultsAction {

    //~ Instance fields --------------------------------------------------------

    private QuerySearchResultsExecutor executor;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  executor  DOCUMENT ME!
     */
    public void setExecutor(final QuerySearchResultsExecutor executor) {
        this.executor = executor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QuerySearchResultsExecutor getExecutor() {
        return executor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getName();
    /**
     * DOCUMENT ME!
     */
    public abstract void doAction();
}
