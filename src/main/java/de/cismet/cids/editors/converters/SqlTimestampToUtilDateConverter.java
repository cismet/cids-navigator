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
public class SqlTimestampToUtilDateConverter extends Converter<java.sql.Timestamp, java.util.Date> {
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    @Override
    public java.util.Date convertForward(java.sql.Timestamp value) {
        //log.fatal("forward:"+value);
        if (value != null) {
            return new java.util.Date(value.getTime());
        } else {
            return null;
        }
    }

    @Override
    public java.sql.Timestamp convertReverse(java.util.Date value) {
        //log.fatal("reverse:"+value);
        if (value != null) {
            return new java.sql.Timestamp(value.getTime());
        } else {
            return null;
        }
    }
}
