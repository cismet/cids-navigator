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
public class BooleanToStringConverter extends Converter<Boolean, String> {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertForward(final Boolean value) {
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public Boolean convertReverse(final String value) {
        try {
            return new Boolean(value);
        } catch (Exception e) {
            return null;
        }
    }
}
