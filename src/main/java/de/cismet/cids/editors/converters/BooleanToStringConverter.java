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

    //~ Instance fields --------------------------------------------------------

    private final String yes;
    private final String no;
    private final String nul;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BooleanToStringConverter object.
     */
    public BooleanToStringConverter() {
        this(Boolean.TRUE.toString(), Boolean.FALSE.toString(), null);
    }

    /**
     * Creates a new BooleanToStringConverter object.
     *
     * @param  yes  DOCUMENT ME!
     * @param  no   DOCUMENT ME!
     */
    public BooleanToStringConverter(final String yes, final String no) {
        this(yes, no, null);
    }

    /**
     * Creates a new BooleanToStringConverter object.
     *
     * @param  yes  DOCUMENT ME!
     * @param  no   DOCUMENT ME!
     * @param  nul  DOCUMENT ME!
     */
    public BooleanToStringConverter(final String yes, final String no, final String nul) {
        this.yes = yes;
        this.no = no;
        this.nul = nul;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String convertForward(final Boolean value) {
        if (value == null) {
            return nul;
        } else {
            return value ? yes : no;
        }
    }

    @Override
    public Boolean convertReverse(final String value) {
        try {
            if ((value == null) || value.equals(nul)) {
                return null;
            } else if (value.equals(yes)) {
                return true;
            } else if (value.equals(no)) {
                return false;
            } else {
                return null;
            }
        } catch (final Exception e) {
            return null;
        }
    }
}
