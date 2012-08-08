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
package Sirius.navigator.ui.tree;

import Sirius.navigator.types.treenode.ClassTreeNode;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.types.treenode.PureTreeNode;

import javax.swing.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface CidsTreeObjectIconFactory {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ptn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getClosedPureNodeIcon(PureTreeNode ptn);
    /**
     * DOCUMENT ME!
     *
     * @param   ptn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getOpenPureNodeIcon(PureTreeNode ptn);
    /**
     * DOCUMENT ME!
     *
     * @param   ptn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getLeafPureNodeIcon(PureTreeNode ptn);

    /**
     * DOCUMENT ME!
     *
     * @param   otn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getOpenObjectNodeIcon(ObjectTreeNode otn);
    /**
     * DOCUMENT ME!
     *
     * @param   otn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getClosedObjectNodeIcon(ObjectTreeNode otn);
    /**
     * DOCUMENT ME!
     *
     * @param   otn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getLeafObjectNodeIcon(ObjectTreeNode otn);

    /**
     * DOCUMENT ME!
     *
     * @param   dmtn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getClassNodeIcon(ClassTreeNode dmtn);
}
