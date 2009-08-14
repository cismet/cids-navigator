package Sirius.navigator.event;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import Sirius.navigator.ui.DescriptionPane;
import Sirius.navigator.ui.attributes.AttributeViewer;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 *
 * @author  pascal
 */
public class CatalogueSelectionListener implements TreeSelectionListener {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final AttributeViewer attributeViewer;
    private final DescriptionPane descriptionPane;
    //private final SingleAttributeIterator classAttributeIterator;
    //private final SingleAttributeIterator objectAttributeIterator;
    
    /** Creates a new instance of CatalogueSelectionListener */
    public CatalogueSelectionListener(AttributeViewer attributeViewer, DescriptionPane descriptionPane) {
        this.attributeViewer = attributeViewer;
        this.descriptionPane = descriptionPane;
        // class attributes only
        //this.classAttributeIterator = new SingleAttributeIterator(new SimpleAttributeRestriction(AttributeRestriction.CLASS, AttributeRestriction.TRUE, AttributeRestriction.IGNORE, null, null), false);
        // object attributes only
        //this.objectAttributeIterator = new SingleAttributeIterator();
    }
    
    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     *
     */
    public void valueChanged(final TreeSelectionEvent e) {
        Thread t=new Thread() {
            public void run() {
                
                Object object = null;
//                TreePath[] treePaths = e.getPaths();
                JTree t=(JTree)e.getSource();
                TreePath[] treePaths = t.getSelectionPaths();
                
                Vector objects=new Vector();
                if (treePaths.length==1) {
                    object = treePaths[0].getLastPathComponent();
                } else {
                    object=null;
                    for(int i = 0; i < treePaths.length; i++) {
                        objects.add(treePaths[i].getLastPathComponent());
                    }
                    
                }
                
                
                if(object != null) {
                    CatalogueSelectionListener.this.attributeViewer.setTreeNode(object);
                    CatalogueSelectionListener.this.descriptionPane.setNodeDescription(object);
                } else {
                    log.debug("more than 1"+objects);
                    CatalogueSelectionListener.this.attributeViewer.clear();
                    CatalogueSelectionListener.this.descriptionPane.setNodesDescription(objects);
                    //CatalogueSelectionListener.this.descriptionPane.clear();
                }
            }
        };
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
}
