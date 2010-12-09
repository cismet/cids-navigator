/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.listener;

import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class MetaNodeSelectionListener implements TreeSelectionListener {

    //~ Constructors -----------------------------------------------------------

    // maby later public final static int CATALOGUE_TREE = 0; public final static int SEARCH_RESULTS_TREE = 1;

    // private final int

    /**
     * Creates a new instance of MetaNodeSelectionListener.
     */
    public MetaNodeSelectionListener() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Called whenever the value of the selection changes.
     *
     * @param  e  the event that characterizes the change.
     */
    @Override
    public void valueChanged(final TreeSelectionEvent e) {
        final ArrayList nodeSelection;
        final TreePath[] selectedPaths = e.getPaths();

        if ((selectedPaths != null) && (selectedPaths.length > 0)) {
            nodeSelection = new ArrayList(selectedPaths.length);
            for (int i = 0; i < selectedPaths.length; i++) {
                nodeSelection.add(selectedPaths[i].getLastPathComponent());
            }

            this.nodeSelectionChanged(nodeSelection);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodeSelection  DOCUMENT ME!
     */
    protected abstract void nodeSelectionChanged(Collection nodeSelection);
}
