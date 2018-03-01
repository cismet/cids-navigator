/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;

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
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree(final ClientConnectionContext connectionContext) throws Exception {
        super(connectionContext);
    }

    /**
     * Creates a new WorkingSpaceTree object.
     *
     * @param   useThread          DOCUMENT ME!
     * @param   maxThreadCount     DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree(final boolean useThread,
            final int maxThreadCount,
            final ClientConnectionContext connectionContext) throws Exception {
        super(useThread, maxThreadCount, connectionContext);
    }
}
