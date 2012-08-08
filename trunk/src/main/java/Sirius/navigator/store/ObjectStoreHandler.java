/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.store;

import java.io.Serializable;

/**
 * ObjectStoreHandler implementations handle the storage and recovery of their managed object
 * such as storing and/or restoring the layout of a GUI component. 
 * 
 * They are usually managed by the {@link ObjectStoreManager}.
 *
 * NOTE: The data managed by the ObjectStoreHandlers has to be assigned to 
 * one group specified in {@link Group}.
 * 
 * @author   Benjamin Friedrich (benjamin.friedrich@cismet.de)
 * @version  $Revision$, $Date$
 */
public interface ObjectStoreHandler {

    //~ Enums ------------------------------------------------------------------

    /**
     * Data group to which a ObjectStoreHandler has to belong to.
     *
     * @version  $Revision$, $Date$
     */
    public static enum Group {

        //~ Enum constants -----------------------------------------------------

        LAYOUT, OTHER
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Return data group to which the handler belongs to
     *
     * @return  data group
     */
    Group getGroup();
    
    /**
     * Returns id of the handler.
     * 
     * <b>NOTE:</b> This id has to be always the same!!! Data returned by {@link ObjectStoreHandler#getObjectToBeSaved() }
     * is associated with this id when it is persisted. Accordingly, recovered data is passed to the right handler
     * by identification with this id.
     *
     * @return  id of the handler
     */
    Integer getId();
    
    /**
     * Returns data which shall be saved
     *
     * @return  data to be saved
     */
    Serializable getObjectToBeSaved();
    
    /**
     * Passes recovered data (which should be the same as returned by {@link ObjectStoreHandler#getObjectToBeSaved() }).
     *
     * @param  in  recovered data
     */
    void notifyAboutLoadedObject(Serializable in);
}
