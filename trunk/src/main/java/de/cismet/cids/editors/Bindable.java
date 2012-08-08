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

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface Bindable {

    //~ Methods ----------------------------------------------------------------

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
    Validator getValidator();
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
    Object getNullSourceValue();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getErrorSourceValue();
}
