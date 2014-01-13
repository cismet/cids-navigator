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
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface QuerySearchMethod {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    void setQuerySearch(QuerySearch search);

    /**
     * DOCUMENT ME!
     *
     * @param  layer  DOCUMENT ME!
     * @param  query  DOCUMENT ME!
     */
    void actionPerformed(Object layer, String query);
}
