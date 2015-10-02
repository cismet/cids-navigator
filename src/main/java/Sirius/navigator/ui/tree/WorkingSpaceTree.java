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
public class WorkingSpaceTree extends SearchResultsTree {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WorkingSpaceTree object.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree() throws Exception {
    }

    /**
     * Creates a new WorkingSpaceTree object.
     *
     * @param   useThread       DOCUMENT ME!
     * @param   maxThreadCount  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree(final boolean useThread, final int maxThreadCount) throws Exception {
        super(useThread, maxThreadCount);
    }
}
