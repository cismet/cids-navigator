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
package de.cismet.cids.editors.converters;

import org.jdesktop.beansbinding.Converter;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SqlDateToUtilDateConverter extends Converter<java.sql.Date, java.util.Date> {

    //~ Instance fields --------------------------------------------------------

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Methods ----------------------------------------------------------------

    @Override
    public java.util.Date convertForward(final java.sql.Date value) {
        // log.fatal("forward:"+value);
        if (value != null) {
            return new java.util.Date(value.getTime());
        } else {
            return null;
        }
    }

    @Override
    public java.sql.Date convertReverse(final java.util.Date value) {
        // log.fatal("reverse:"+value);
        if (value != null) {
            return new java.sql.Date(value.getTime());
        } else {
            return null;
        }
    }
}
