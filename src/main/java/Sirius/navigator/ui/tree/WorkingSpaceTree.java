/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.WorkingSpaceHandler;
import Sirius.navigator.resource.PropertyManager;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.tools.gui.GUIWindow;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = GUIWindow.class)
public class WorkingSpaceTree extends SearchResultsTree implements GUIWindow {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WorkingSpaceTree object. This constructor will be used by the lookup
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree() throws Exception {
        super(ConnectionContext.createDummy());
    }

    /**
     * Creates a new WorkingSpaceTree object.
     *
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public WorkingSpaceTree(final ConnectionContext connectionContext) throws Exception {
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
            final ConnectionContext connectionContext) throws Exception {
        super(useThread, maxThreadCount, connectionContext);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getPermissionString() {
        if (WorkingSpaceHandler.getInstance().isEnabled()) {
            return GUIWindow.NO_PERMISSION;
        } else {
            return "WorkingSpaceTreeEnabled";
        }
    }
}
