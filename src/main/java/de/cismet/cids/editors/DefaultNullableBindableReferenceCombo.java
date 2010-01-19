/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;

import java.util.ResourceBundle;

/**
 *
 * @author thorsten
 */
public class DefaultNullableBindableReferenceCombo extends DefaultBindableReferenceCombo{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    public DefaultNullableBindableReferenceCombo() {
        super();
        super.setNullable(true);
        super.setNullValueRepresentation(I18N.getString("de.cismet.cids.editors.DefaultNullableBindableReferenceCombo.nullValueRepresentation"));
    }

}
