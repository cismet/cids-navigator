package Sirius.navigator.event;

import java.awt.event.*;
import javax.swing.event.*;


import Sirius.navigator.types.treenode.*;
import Sirius.navigator.types.iterator.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.attributes.*;
import Sirius.navigator.ui.tree.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class CatalogueActivationListener extends ComponentAdapter {

    private final MetaCatalogueTree catalogueTree;
    private final AttributeViewer attributeViewer;
    private final DescriptionPane descriptionPane;
    //private final SingleAttributeIterator iterator;

    /** Creates a new instance of CatalogueActivationListener */
    public CatalogueActivationListener(MetaCatalogueTree catalogueTree, AttributeViewer attributeViewer, DescriptionPane descriptionPane) {
        this.catalogueTree = catalogueTree;
        this.attributeViewer = attributeViewer;
        this.descriptionPane = descriptionPane;
        //this.iterator = new SingleAttributeIterator();
    }

    public void componentShown(ComponentEvent ce) {
        if (catalogueTree.getSelectedNodeCount() == 1) {
            Object object = catalogueTree.getLastSelectedPathComponent();            
            this.attributeViewer.setTreeNode(object);
            this.descriptionPane.setNodeDescription(object);
        } else {
            this.attributeViewer.clear();
            this.descriptionPane.clear();
        }
    }
    /*public void componentHidden(ComponentEvent ce)
    {
    System.out.println(catalogueTree.getClass().getName() + " isVisible: " + catalogueTree.isVisible());
    System.out.println(catalogueTree.getClass().getName() + " isDisplayable: " + catalogueTree.isDisplayable());
    System.out.println(catalogueTree.getClass().getName() + " isFocusable: " + catalogueTree.isFocusable());
    System.out.println(catalogueTree.getClass().getName() + " isShowing: " + catalogueTree.isShowing());
    }*/
}
