/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors.converters;

import java.sql.Date;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author thorsten
 */
public class SqlDateToStringConverter extends Converter<java.sql.Date,String>{

    @Override
    public String convertForward(Date value) {
        
        if (value!=null){
            return value.toString();
        }
        else {
            return null;
        }
    }

    @Override
    public Date convertReverse(String value) {
        return Date.valueOf(value);
    }
    

}
