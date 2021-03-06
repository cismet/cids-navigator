/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.navigator.ui.tree.SearchResultsTree;

import de.cismet.connectioncontext.ConnectionContext;

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
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public CidsServerSearchProtocolStepResultsTree(final ConnectionContext connectionContext) throws Exception {
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
    public CidsServerSearchProtocolStepResultsTree(final boolean useThread,
            final int maxThreadCount,
            final ConnectionContext connectionContext) throws Exception {
        super(useThread, maxThreadCount, connectionContext);
    }
}
