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
package de.cismet.cids.custom.objectrenderer.sirius;

import java.awt.Component;

/**
 * DOCUMENT ME!
 *
 * @author   bfriedrich
 * @version  $Revision$, $Date$
 */
public interface NoDescriptionWidget {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void init();
    /**
     * DOCUMENT ME!
     */
    void refresh();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getWidget();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isHeaderWidget();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getX();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getY();
    /**
     * DOCUMENT ME!
     */
    void dispose();
}
