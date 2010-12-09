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
public class SqlTimestampToUtilDateConverter extends Converter<java.sql.Timestamp, java.util.Date> {

    //~ Instance fields --------------------------------------------------------

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Methods ----------------------------------------------------------------

    @Override
    public java.util.Date convertForward(final java.sql.Timestamp value) {
        // log.fatal("forward:"+value);
        if (value != null) {
            return new java.util.Date(value.getTime());
        } else {
            return null;
        }
    }

    @Override
    public java.sql.Timestamp convertReverse(final java.util.Date value) {
        // log.fatal("reverse:"+value);
        if (value != null) {
            return new java.sql.Timestamp(value.getTime());
        } else {
            return null;
        }
    }
}
