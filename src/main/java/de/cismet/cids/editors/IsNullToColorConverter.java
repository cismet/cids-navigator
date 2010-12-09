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
package de.cismet.cids.editors;

import org.jdesktop.beansbinding.Converter;

import java.awt.Color;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class IsNullToColorConverter extends Converter<Boolean, Color> {

    //~ Static fields/initializers ---------------------------------------------

    private static final Color isNullColor = Color.LIGHT_GRAY;
    private static final Color isNotNullColor = Color.WHITE;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Color convertForward(final Boolean isnull) {
        if (isnull) {
            return isNullColor;
        } else {
            return isNotNullColor;
        }
    }

    @Override
    public Boolean convertReverse(final Color value) {
        // wird nie aufgerufen
        return false;
    }
}
