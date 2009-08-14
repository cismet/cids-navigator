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
public class BooleanToStringConverter extends Converter<Boolean,String>{

    @Override
    public String convertForward(Boolean value) {
        if (value!=null){
            return value.toString();
        }
        else {
            return null;
        }
    }

    @Override
    public Boolean convertReverse(String value) {
        try {
            return new Boolean(value);
        }
        catch (Exception e){
            return null;
        }
    }

}
