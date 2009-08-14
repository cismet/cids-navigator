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
 *
 * @author thorsten
 */
public interface CidsTreeObjectIconFactory {
    public Icon getClosedPureNodeIcon(PureTreeNode ptn);
    public Icon getOpenPureNodeIcon(PureTreeNode ptn);
    public Icon getLeafPureNodeIcon(PureTreeNode ptn);


    public Icon getOpenObjectNodeIcon(ObjectTreeNode otn);
    public Icon getClosedObjectNodeIcon(ObjectTreeNode otn);
    public Icon getLeafObjectNodeIcon(ObjectTreeNode otn);

    
    public Icon getClassNodeIcon(ClassTreeNode dmtn);
}
