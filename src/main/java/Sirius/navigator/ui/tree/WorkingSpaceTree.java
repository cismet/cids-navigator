/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import de.cismet.connectioncontext.ConnectionContext;
import Sirius.navigator.resource.PropertyManager;

import javax.swing.JComponent;

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
        if (PropertyManager.getManager().isWorkingSpaceEnabled()) {
            return GUIWindow.NO_PERMISSION;
        } else {
            return "WorkingSpaceTreeEnabled";
        }
    }
}
