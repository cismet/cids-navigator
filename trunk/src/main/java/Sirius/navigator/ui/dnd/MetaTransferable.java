/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MetaTransferable.java
 *
 * Created on 15. September 2004, 10:40
 */
package Sirius.navigator.ui.dnd;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface MetaTransferable extends java.awt.datatransfer.Transferable {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property tansferAction.
     *
     * @return  Value of property tansferAction.
     */
    int getTransferAction();

    /**
     * Setter for property tansferAction.
     *
     * @param  tansferAction  New value of property tansferAction.
     */
    void setTransferAction(int tansferAction);
}
