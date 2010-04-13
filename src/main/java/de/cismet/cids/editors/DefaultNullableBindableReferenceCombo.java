/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;


/**
 *
 * @author thorsten
 */
public class DefaultNullableBindableReferenceCombo extends DefaultBindableReferenceCombo{
    public DefaultNullableBindableReferenceCombo() {
        super();
        super.setNullable(true);
        super.setNullValueRepresentation(org.openide.util.NbBundle.getMessage(DefaultNullableBindableReferenceCombo.class, "DefaultNullableBindableReferenceCombo.nullValueRepresentation"));//NOI18N
    }
}
