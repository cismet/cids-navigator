/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.event;

import Sirius.navigator.types.iterator.*;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.attributes.*;
import Sirius.navigator.ui.tree.*;

import org.apache.log4j.Logger;

import java.awt.event.*;

import javax.swing.event.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class CatalogueActivationListener extends ComponentAdapter {

    //~ Instance fields --------------------------------------------------------

    private final MetaCatalogueTree catalogueTree;
    private final AttributeViewer attributeViewer;
    private final DescriptionPane descriptionPane;
    // private final SingleAttributeIterator iterator;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of CatalogueActivationListener.
     *
     * @param  catalogueTree    DOCUMENT ME!
     * @param  attributeViewer  DOCUMENT ME!
     * @param  descriptionPane  DOCUMENT ME!
     */
    public CatalogueActivationListener(final MetaCatalogueTree catalogueTree,
            final AttributeViewer attributeViewer,
            final DescriptionPane descriptionPane) {
        this.catalogueTree = catalogueTree;
        this.attributeViewer = attributeViewer;
        this.descriptionPane = descriptionPane;
        // this.iterator = new SingleAttributeIterator();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void componentShown(final ComponentEvent ce) {
        if (catalogueTree.getSelectedNodeCount() == 1) {
            final Object object = catalogueTree.getLastSelectedPathComponent();
            this.attributeViewer.setTreeNode(object);
            this.descriptionPane.setNodeDescription(object);
        } else {
            this.attributeViewer.clear();
            this.descriptionPane.clear();
        }
    }
    /*public void componentHidden(ComponentEvent ce)
     * { System.out.println(catalogueTree.getClass().getName() + " isVisible: " + catalogueTree.isVisible());
     * System.out.println(catalogueTree.getClass().getName() + " isDisplayable: " + catalogueTree.isDisplayable());
     * System.out.println(catalogueTree.getClass().getName() + " isFocusable: " + catalogueTree.isFocusable());
     * System.out.println(catalogueTree.getClass().getName() + " isShowing: " + catalogueTree.isShowing());}*/
}
