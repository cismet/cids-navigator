/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors.converters;

import org.jdesktop.beansbinding.Converter;

import java.awt.Color;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ColorToStringConverter extends Converter<String, Color> {

    //~ Instance fields --------------------------------------------------------

    private org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertReverse(final Color color) {
        if (color != null) {
            return "#" + Integer.toHexString(color.getRGB() & 0x00ffffff);
        }
        return null;
    }

    @Override
    public Color convertForward(final String string) {
        if (string != null) {
            try {
                return Color.decode(string);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("error while converting string to color", e);
                }
            }
        }
        return null;
    }
}
