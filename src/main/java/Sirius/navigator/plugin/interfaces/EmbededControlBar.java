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

import java.util.Vector;

import javax.swing.AbstractButton;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public interface EmbededControlBar {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  isVisible  DOCUMENT ME!
     */
    void setControlBarVisible(boolean isVisible);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Vector<AbstractButton> getControlBarButtons();
}
