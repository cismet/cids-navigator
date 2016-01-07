/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.event;

import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.DescriptionPane;
import Sirius.navigator.ui.attributes.AttributeViewer;
import Sirius.navigator.ui.tree.MetaCatalogueTree;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class CatalogueSelectionListener implements TreeSelectionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(CatalogueSelectionListener.class);

    private static final int SELECTION_CADENCE_TIME = 300;

    //~ Instance fields --------------------------------------------------------

    private final AttributeViewer attributeViewer;
    private final DescriptionPane descriptionPane;
    private final Timer timer;
    private final SelectionActionListener timerListener;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of CatalogueSelectionListener.
     *
     * @param  attributeViewer  DOCUMENT ME!
     * @param  descriptionPane  DOCUMENT ME!
     */
    public CatalogueSelectionListener(final AttributeViewer attributeViewer, final DescriptionPane descriptionPane) {
        this.attributeViewer = attributeViewer;
        this.descriptionPane = descriptionPane;
        timerListener = new SelectionActionListener();
        timer = new Timer(SELECTION_CADENCE_TIME, timerListener);
        timer.setRepeats(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    private void performValueChanged(final TreeSelectionEvent e) {
        final JTree t = (JTree)e.getSource();

        final MetaCatalogueTree catalogue = ComponentRegistry.getRegistry().getCatalogueTree();
        final MetaCatalogueTree searchResults = ComponentRegistry.getRegistry().getSearchResultsTree();
        final MetaCatalogueTree workingArea = ComponentRegistry.getRegistry().getWorkingSpaceTree();

        if (t == catalogue) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("catalogue tree changed");
            }
            tryToClearSelection(searchResults);
            tryToClearSelection(workingArea);
        } else if (t == searchResults) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("searchresults tree changed");
            }
            tryToClearSelection(catalogue);
            tryToClearSelection(workingArea);
            catalogue.removeTreeSelectionListener(this);
            catalogue.clearSelection();
            catalogue.addTreeSelectionListener(this);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("working area tree changed");
            }
            tryToClearSelection(catalogue);
            tryToClearSelection(searchResults);
        }

        final TreePath[] treePaths = t.getSelectionPaths();

        final List<Object> objects = new ArrayList<Object>();
        if (treePaths != null) {
            for (int i = 0; i < treePaths.length; i++) {
                objects.add(treePaths[i].getLastPathComponent());
            }
        }
        CatalogueSelectionListener.this.attributeViewer.setTreeNodes(objects);
        CatalogueSelectionListener.this.descriptionPane.setNodesDescriptions(objects);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tree  DOCUMENT ME!
     */
    private void tryToClearSelection(final MetaCatalogueTree tree) {
        if (tree != null) {
            tree.removeTreeSelectionListener(this);
            tree.clearSelection();
            tree.addTreeSelectionListener(this);
        }
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param  e  the event that characterizes the change.
     */
    @Override
    public void valueChanged(final TreeSelectionEvent e) {
        timer.stop();
        descriptionPane.prepareValueChanged();
        timerListener.setEvt(e);
        timer.start();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    final class SelectionActionListener extends AbstractAction {

        //~ Instance fields ----------------------------------------------------

        private TreeSelectionEvent evt;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (evt != null) {
                performValueChanged(evt);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the evt
         */
        public TreeSelectionEvent getEvt() {
            return evt;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  evt  the evt to set
         */
        public void setEvt(final TreeSelectionEvent evt) {
            this.evt = evt;
        }
    }
}
