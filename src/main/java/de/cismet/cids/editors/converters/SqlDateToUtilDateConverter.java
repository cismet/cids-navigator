/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors.converters;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author thorsten
 */
public class SqlDateToUtilDateConverter extends Converter<java.sql.Date, java.util.Date> {
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    @Override
    public java.util.Date convertForward(java.sql.Date value) {
        //log.fatal("forward:"+value);
        if (value != null) {
            return new java.util.Date(value.getTime());
        } else {
            return null;
        }
    }

    @Override
    public java.sql.Date convertReverse(java.util.Date value) {
        //log.fatal("reverse:"+value);
        if (value != null) {
            return new java.sql.Date(value.getTime());
        } else {
            return null;
        }
    }
}
