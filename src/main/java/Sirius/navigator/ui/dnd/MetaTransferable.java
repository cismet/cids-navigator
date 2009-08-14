/*
 * MetaTransferable.java
 *
 * Created on 15. September 2004, 10:40
 */

package Sirius.navigator.ui.dnd;

/**
 *
 * @author  pascal
 */
public interface MetaTransferable extends java.awt.datatransfer.Transferable
{

    /**
     * Getter for property tansferAction.
     * @return Value of property tansferAction.
     */
    public int getTransferAction();
    
    /**
     * Setter for property tansferAction.
     * @param tansferAction New value of property tansferAction.
     */
    public void setTransferAction(int tansferAction);
    
}
