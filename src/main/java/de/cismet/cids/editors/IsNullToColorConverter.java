/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;

import org.jdesktop.beansbinding.Converter;
import java.awt.Color;

/**
 *
 * @author thorsten
 */
public class IsNullToColorConverter extends Converter<Boolean,Color> {
    private static final Color isNullColor=Color.LIGHT_GRAY;
    private static final Color isNotNullColor=Color.WHITE;

    @Override
    public Color convertForward(Boolean isnull) {
        if (isnull){
            return isNullColor;
        }
        else{
            return isNotNullColor;
        }
    }

    @Override
    public Boolean convertReverse(Color value) {
        //wird nie aufgerufen
        return false;
    }



}
