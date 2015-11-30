/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.navigator.ui.tree.SearchResultsTree;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsServerSearchProtocolStepResultsTree extends SearchResultsTree {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolResultsTree object.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public CidsServerSearchProtocolStepResultsTree() throws Exception {
    }

    /**
     * Creates a new WorkingSpaceTree object.
     *
     * @param   useThread       DOCUMENT ME!
     * @param   maxThreadCount  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public CidsServerSearchProtocolStepResultsTree(final boolean useThread, final int maxThreadCount) throws Exception {
        super(useThread, maxThreadCount);
    }
}
