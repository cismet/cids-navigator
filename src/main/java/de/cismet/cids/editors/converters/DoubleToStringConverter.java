/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors.converters;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.Converter;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DoubleToStringConverter extends Converter<Double, String> {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DoubleToStringConverter.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertForward(final Double value) {
        return value.toString().replace('.', ',');
    }

    @Override
    public Double convertReverse(final String value) {
        try {
            if (value == null) {
                return null;
            }

            return new Double(value.replace(',', '.'));
        } catch (final NumberFormatException e) {
            LOG.warn("No valid number: " + value, e); // NOI18N

            // this is for convenience
            if (value.trim().startsWith("kein")) {
                return 0D;
            }

            return null;
        }
    }
}
