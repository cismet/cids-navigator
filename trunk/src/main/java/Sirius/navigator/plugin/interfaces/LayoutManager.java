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
package Sirius.navigator.plugin.interfaces;

import java.awt.Component;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public interface LayoutManager {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  parent  DOCUMENT ME!
     */
    void saveCurrentLayout(Component parent);
    /**
     * DOCUMENT ME!
     *
     * @param  parent  DOCUMENT ME!
     */
    void loadLayout(Component parent);
    /**
     * DOCUMENT ME!
     */
    void resetLayout();
}
