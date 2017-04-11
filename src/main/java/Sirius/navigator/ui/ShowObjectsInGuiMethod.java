/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;

import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface ShowObjectsInGuiMethod {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void init();

    /**
     * Performs the show objects action.
     *
     * @param   nodes     DOCUMENT ME!
     * @param   editable  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void invoke(final Collection<DefaultMetaTreeNode> nodes, final boolean editable) throws Exception;
}
