package Sirius.navigator.event;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import Sirius.navigator.ui.DescriptionPane;
import Sirius.navigator.ui.attributes.AttributeViewer;
import de.cismet.tools.collections.TypeSafeCollections;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.*;

/**
 *
 * @author  pascal
 */
public class CatalogueSelectionListener implements TreeSelectionListener {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final int SELECTION_CADENCE_TIME = 300;
    private final AttributeViewer attributeViewer;
    private final DescriptionPane descriptionPane;

    /** Creates a new instance of CatalogueSelectionListener */
    public CatalogueSelectionListener(AttributeViewer attributeViewer, DescriptionPane descriptionPane) {
        this.attributeViewer = attributeViewer;
        this.descriptionPane = descriptionPane;
        timerListener = new SelectionActionListener();
        timer = new Timer(SELECTION_CADENCE_TIME, timerListener);
        timer.setRepeats(false);
        // class attributes only
        //this.classAttributeIterator = new SingleAttributeIterator(new SimpleAttributeRestriction(AttributeRestriction.CLASS, AttributeRestriction.TRUE, AttributeRestriction.IGNORE, null, null), false);
        // object attributes only
        //this.objectAttributeIterator = new SingleAttributeIterator();
    }
    private final Timer timer;
    private final SelectionActionListener timerListener;

    private final void performValueChanged(final TreeSelectionEvent e) {
//        final Runnable r = new Runnable() {
//
//            @Override
//            public void run() {
        final JTree t = (JTree) e.getSource();
        final TreePath[] treePaths = t.getSelectionPaths();

        final List<Object> objects = TypeSafeCollections.newArrayList(treePaths.length);
        for (int i = 0; i < treePaths.length; i++) {
            objects.add(treePaths[i].getLastPathComponent());
        }
        CatalogueSelectionListener.this.attributeViewer.setTreeNodes(objects);
        CatalogueSelectionListener.this.descriptionPane.setNodesDescriptions(objects);

//            }
//        };
//        ApplicationThreadPool.execute(r);
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     *
     */
    @Override
    public void valueChanged(final TreeSelectionEvent e) {
        timer.stop();
        descriptionPane.prepareValueChanged();
        timerListener.setEvt(e);
        timer.start();
    }

    final class SelectionActionListener extends AbstractAction {

        private TreeSelectionEvent evt;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (evt != null) {
                performValueChanged(evt);
            }

        }

        /**
         * @return the evt
         */
        public TreeSelectionEvent getEvt() {
            return evt;
        }

        /**
         * @param evt the evt to set
         */
        public void setEvt(TreeSelectionEvent evt) {
            this.evt = evt;
        }
    }
}
