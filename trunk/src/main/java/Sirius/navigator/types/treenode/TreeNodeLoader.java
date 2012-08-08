/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import Sirius.server.middleware.types.Node;

import java.io.Serializable;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public interface TreeNodeLoader extends Serializable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    boolean addChildren(DefaultMetaTreeNode node) throws Exception;

    /**
     * DOCUMENT ME!
     *
     * @param   node      DOCUMENT ME!
     * @param   children  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    boolean addChildren(DefaultMetaTreeNode node, Node[] children) throws Exception;
}
