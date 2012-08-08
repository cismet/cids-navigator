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
package de.cismet.cids.editors;

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BindingInformationProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  name       DOCUMENT ME!
     * @param  component  DOCUMENT ME!
     */
    void addControlInformation(String name, Bindable component);

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Bindable getControlByName(String name);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    HashMap<String, Bindable> getAllControls();
}
