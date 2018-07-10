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
package Sirius.navigator.ui;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public interface GUIContainer {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param       id  DOCUMENT ME!
     *
     * @deprecated  does not work with the new navigator. Use ComponentRegistry.getRegistry().showComponent(String id).
     */
    void select(String id);
    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    void remove(String id);
    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    void add(MutableConstraints constraints);
}
