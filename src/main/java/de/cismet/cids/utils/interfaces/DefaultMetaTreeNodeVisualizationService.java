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
package de.cismet.cids.utils.interfaces;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;

import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface DefaultMetaTreeNodeVisualizationService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   dmtn  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void removeVisualization(DefaultMetaTreeNode dmtn) throws Exception;
    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void removeVisualization(Collection<DefaultMetaTreeNode> c) throws Exception;
    /**
     * DOCUMENT ME!
     *
     * @param   DefaultMetaTreeNode  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void addVisualization(DefaultMetaTreeNode DefaultMetaTreeNode) throws Exception;
    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void addVisualization(Collection<DefaultMetaTreeNode> c) throws Exception;
}
