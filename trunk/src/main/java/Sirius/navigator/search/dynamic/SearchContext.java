/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchContext.java
 *
 * Created on 17. November 2003, 15:24
 */
package Sirius.navigator.search.dynamic;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SearchContext {

    //~ Instance fields --------------------------------------------------------

    private final SearchDialog seachDialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * TODO remove.
     *
     * @param  searchDialog  DOCUMENT ME!
     */
    protected SearchContext(final SearchDialog searchDialog) {
        this.seachDialog = searchDialog;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchDialog getSearchDialog() {
        return this.seachDialog;
    }
}
