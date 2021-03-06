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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

import org.jdesktop.beansbinding.Converter;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class GeometryToStringConverter extends Converter<Geometry, String> {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertForward(final Geometry value) {
        if (value == null) {
            return null;
        } else {
            return value.toText();
        }
    }

    @Override
    public Geometry convertReverse(final String value) {
        try {
            return new WKTReader(new GeometryFactory()).read(value);
        } catch (Exception e) {
            return null;
        }
    }
}
