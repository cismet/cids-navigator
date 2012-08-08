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

import java.sql.Date;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SqlDateToStringConverter extends Converter<java.sql.Date, String> {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertForward(final Date value) {
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public Date convertReverse(final String value) {
        return Date.valueOf(value);
    }
}
