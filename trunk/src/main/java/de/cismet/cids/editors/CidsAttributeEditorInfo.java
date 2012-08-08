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

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface CidsAttributeEditorInfo {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JComponent getComponent();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getBindingProperty();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Converter getConverter();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Validator getValidator();
}
