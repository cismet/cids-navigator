/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface ResultNodeListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void resultNodesChanged();
    /**
     * DOCUMENT ME!
     */
    void resultNodesCleared();

    /**
     * DOCUMENT ME!
     */
    void resultNodesFiltered();
}
