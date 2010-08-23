/*
 * AttributeNodeTransferable.java
 *
 * Created on 15. September 2004, 11:03
 */

package Sirius.navigator.ui.dnd;

import java.awt.datatransfer.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.attributes.*;

/**
 *
 * @author  pascal
 */
public class AttributeNodeTransferable implements MetaTransferable
{
    static DataFlavor[] dataFlavors = null;
    private ObjectAttributeNode objectAttributeNode;
    
    /**
     * Holds value of property transferAction.
     */
    private int transferAction;
    
    /** Creates a new instance of AttributeNodeTransferable */
    public AttributeNodeTransferable(ObjectAttributeNode objectAttributeNode)
    {
        this.objectAttributeNode = objectAttributeNode;
    }
    
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException
    {
        if(getTransferDataFlavors().length > 0)
        {
            if(getTransferDataFlavors()[0].equals(flavor))
            {
                return this.objectAttributeNode;
            }
            else
            {
                throw new UnsupportedFlavorException(flavor);
            }
        }
        
        return null;
    }
    
    public DataFlavor[] getTransferDataFlavors()
    {
        // lazily construct flavors
        if(dataFlavors == null)
        {
            dataFlavors = new DataFlavor[1];
            
            try
            {
                // ObjectAttributeNode
                String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ObjectAttributeNode.class.getName();//NOI18N
                DataFlavor dataFlavor = new DataFlavor(mimeType);
                dataFlavor.setHumanPresentableName("a ObjectAttributeNode");//NOI18N
                dataFlavors[0] = dataFlavor;
            }
            catch (ClassNotFoundException cnfe)
            {
                Logger.getLogger(AttributeNodeTransferable.class).error("getTransferDataFlavors() could not create DnD data flavours", cnfe);//NOI18N
                dataFlavors = new DataFlavor[0];
            }
        }
        
        return dataFlavors;
    }
    
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        if(getTransferDataFlavors().length > 0 && dataFlavors[0].equals(flavor))
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
