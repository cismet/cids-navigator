/*
 * MetaTreeNodeTransferable.java
 *
 * Created on 15. September 2004, 10:42
 */

package Sirius.navigator.ui.dnd;

import java.awt.datatransfer.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.tree.*;
import Sirius.navigator.types.treenode.*;

/**
 *
 * @author  pascal
 */
public class MetaTreeNodeTransferable implements MetaTransferable
{
    static DataFlavor[] dataFlavors = null;
    private MetaCatalogueTree metaCatalogueTree;
    
    /**
     * Holds value of property transferAction.
     */
    private int transferAction;
    
    /** Creates a new instance of MetaTreeNodeTransferable */
    public MetaTreeNodeTransferable(MetaCatalogueTree metaCatalogueTree)
    {
        this.metaCatalogueTree =  metaCatalogueTree;
    }
    
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException
    {
        if(getTransferDataFlavors().length > 0)
        {
            if(getTransferDataFlavors()[0].equals(flavor))
            {
                //Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): MetaTreeNode");
                
                return this.metaCatalogueTree.getSelectedNode();
                
            }
            else if (getTransferDataFlavors()[1].equals(flavor))
            {
                //Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): Collection of nodes");
                return this.metaCatalogueTree.getSelectedNodes();
            }
            else
            {
                Logger.getLogger(MetaTreeNodeTransferable.class).warn("getTransferData(): UnsupportedFlavorException");//NOI18N
                throw new UnsupportedFlavorException(flavor);
            }
        }
        
        //Logger.getLogger(MetaTreeNodeTransferable.class).debug("getTransferData(): no flavours available");
        return null;
    }
    
    public DataFlavor[] getTransferDataFlavors()
    {
        // lazily construct flavors
        if(dataFlavors == null)
        {
            dataFlavors = new DataFlavor[2];
            
            try
            {
                // MetaTreeNode
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + DefaultMetaTreeNode.class.getName();//NOI18N
                DataFlavor dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName("a DefaultMetaTreeNode");//NOI18N
                dataFlavors[0] = dataFlavor;
                
                // Collection of nodes
                mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + java.util.Collection.class.getName();//NOI18N
                dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName("a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects");//NOI18N
                dataFlavors[1] = dataFlavor;
            }
            catch (ClassNotFoundException cnfe)
            {
                Logger.getLogger(MetaTreeNodeTransferable.class).error("getTransferDataFlavors() could not create DnD data flavours", cnfe);//NOI18N
                dataFlavors = new DataFlavor[0];
            }
        }
        
        return dataFlavors;
    }
    
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        if(getTransferDataFlavors().length > 0 && (dataFlavors[0].equals(flavor) || dataFlavors[1].equals(flavor)))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Getter for property transferAction.
     * @return Value of property transferAction.
     */
    public int getTransferAction()
    {
        return this.transferAction;
    }
    
    /**
     * Setter for property transferAction.
     * @param transferAction New value of property transferAction.
     */
    public void setTransferAction(int transferAction)
    {
        this.transferAction = transferAction;
    }
}
