/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import java.io.*;

import javax.swing.tree.*;

/**
 * // header - edit "Data/yourJavaHeader" to customize // contents - edit "EventHandlers/Java file/onCreate" to
 * customize //.
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
    boolean addChildren(DefaultMetaTreeNode node, Sirius.server.middleware.types.Node[] children) throws Exception;
}
