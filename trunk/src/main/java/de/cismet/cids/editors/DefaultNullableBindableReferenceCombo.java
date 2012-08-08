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

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultNullableBindableReferenceCombo extends DefaultBindableReferenceCombo {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultNullableBindableReferenceCombo object.
     */
    public DefaultNullableBindableReferenceCombo() {
        super();
        super.setNullable(true);
        super.setNullValueRepresentation(org.openide.util.NbBundle.getMessage(
                DefaultNullableBindableReferenceCombo.class,
                "DefaultNullableBindableReferenceCombo.nullValueRepresentation")); // NOI18N
    }
}
