/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

/**
 * All methods, which should be used in the QueryŚearch, must implement this interface.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface QuerySearchMethod {

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the QuerySearch object, that uses this QuerySearchMethod object.
     *
     * @param  search  the QuerySearch object, that uses this QuerySearchMethod object
     */
    void setQuerySearch(QuerySearch search);

    /**
     * starts this QuerySearchMethod with the given parameter.
     *
     * @param  layer  the layer that should be used. This is either an AbstractFeatureService or a cids meta object
     * @param  query  the query as string
     */
    void actionPerformed(Object layer, String query);
}
