package Sirius.navigator.types.treenode;

import javax.swing.tree.*;
import java.io.*;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
 */

public interface TreeNodeLoader extends Serializable
{
    public boolean addChildren(DefaultMetaTreeNode node) throws Exception;
    
    public boolean addChildren(DefaultMetaTreeNode node, Sirius.server.middleware.types.Node[] children) throws Exception;
}
