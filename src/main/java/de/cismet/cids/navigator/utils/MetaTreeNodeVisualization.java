/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;

import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.Lookup.Template;

import java.util.Collection;

import de.cismet.cids.utils.interfaces.DefaultMetaTreeNodeVisualizationService;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class MetaTreeNodeVisualization implements DefaultMetaTreeNodeVisualizationService {

    //~ Static fields/initializers ---------------------------------------------

    private static MetaTreeNodeVisualization instance = null;

    //~ Instance fields --------------------------------------------------------

    private Lookup visLookup;
    private Collection<DefaultMetaTreeNodeVisualizationService> visServices;
    private Template visTemplate;
    private Result visResults;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaTreeNodeVisualization object.
     */
    private MetaTreeNodeVisualization() {
        visLookup = Lookup.getDefault();
        visTemplate = new Template(DefaultMetaTreeNodeVisualizationService.class);
        visResults = visLookup.lookup(visTemplate);
        visServices = visResults.allInstances();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaTreeNodeVisualization getInstance() {
        if (instance == null) {
            instance = new MetaTreeNodeVisualization();
        }
        return instance;
    }

    @Override
    public void addVisualization(final DefaultMetaTreeNode dmtn) throws Exception {
        for (final DefaultMetaTreeNodeVisualizationService service : visServices) {
            service.addVisualization(dmtn);
        }
    }

    @Override
    public void addVisualization(final Collection<DefaultMetaTreeNode> c) throws Exception {
        for (final DefaultMetaTreeNodeVisualizationService service : visServices) {
            service.addVisualization(c);
        }
    }

    @Override
    public void removeVisualization(final DefaultMetaTreeNode mon) throws Exception {
        for (final DefaultMetaTreeNodeVisualizationService service : visServices) {
            service.removeVisualization(mon);
        }
    }

    @Override
    public void removeVisualization(final Collection<DefaultMetaTreeNode> c) throws Exception {
        for (final DefaultMetaTreeNodeVisualizationService service : visServices) {
            service.removeVisualization(c);
        }
    }
}
