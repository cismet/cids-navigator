package de.cismet.cids.editors.converters;

import java.awt.Color;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author jruiz
 */
public class ColorToStringConverter extends Converter<String, Color> {
    private org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(this.getClass());
    
    @Override
    public String convertReverse(Color color) {
        if (color != null) {            
            return "#" + Integer.toHexString(color.getRGB() & 0x00ffffff);
        }
        return null;
    }

    @Override
    public Color convertForward(String string) {
        if (string != null) {
            try {
                return Color.decode(string);
            } catch (Exception e) {
                LOG.debug("error while converting string to color", e);
            }
        }
        return null;
    }
}
