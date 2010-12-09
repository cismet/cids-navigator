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
import org.jdesktop.beansbinding.Validator;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableJCheckBox extends JCheckBox implements Bindable {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            DefaultBindableJCheckBox.class);
    private static final Converter<Boolean, Boolean> NULL_CONVERTER = new Converter<Boolean, Boolean>() {

            @Override
            public Boolean convertForward(final Boolean s) {
                if (s == null) {
                    return Boolean.FALSE;
                } else {
                    return s;
                }
            }

            @Override
            public Boolean convertReverse(final Boolean t) {
                if (t == null) {
                    return Boolean.FALSE;
                } else {
                    return t;
                }
            }
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableJCheckBox object.
     */
    public DefaultBindableJCheckBox() {
        super();
        setOpaque(false);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  a  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final Action a) {
        super(a);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  text  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final String text) {
        super(text);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  icon  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final Icon icon) {
        super(icon);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  text  DOCUMENT ME!
     * @param  icon  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final String text, final Icon icon) {
        super(text, icon);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  text      DOCUMENT ME!
     * @param  selected  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final String text, final boolean selected) {
        super(text, selected);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  icon      DOCUMENT ME!
     * @param  selected  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final Icon icon, final boolean selected) {
        super(icon, selected);
    }

    /**
     * Creates a new DefaultBindableJCheckBox object.
     *
     * @param  text      DOCUMENT ME!
     * @param  icon      DOCUMENT ME!
     * @param  selected  DOCUMENT ME!
     */
    public DefaultBindableJCheckBox(final String text, final Icon icon, final boolean selected) {
        super(text, icon, selected);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getBindingProperty() {
        return "selected"; // NOI18N
    }

    @Override
    public Converter getConverter() {
        return NULL_CONVERTER;
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public Object getNullSourceValue() {
        return Boolean.FALSE;
    }

    @Override
    public Object getErrorSourceValue() {
        return Boolean.FALSE;
    }
}
