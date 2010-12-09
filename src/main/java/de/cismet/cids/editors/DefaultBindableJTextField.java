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

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableJTextField extends JTextField implements Bindable {

    //~ Instance fields --------------------------------------------------------

    Converter converter = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableJTextField object.
     */
    public DefaultBindableJTextField() {
    }

    /**
     * Creates a new DefaultBindableJTextField object.
     *
     * @param  columns  DOCUMENT ME!
     */
    public DefaultBindableJTextField(final int columns) {
        super(columns);
    }

    /**
     * Creates a new DefaultBindableJTextField object.
     *
     * @param  text  DOCUMENT ME!
     */
    public DefaultBindableJTextField(final String text) {
        super(text);
    }

    /**
     * Creates a new DefaultBindableJTextField object.
     *
     * @param  text     DOCUMENT ME!
     * @param  columns  DOCUMENT ME!
     */
    public DefaultBindableJTextField(final String text, final int columns) {
        super(text, columns);
    }

    /**
     * Creates a new DefaultBindableJTextField object.
     *
     * @param  doc      DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  columns  DOCUMENT ME!
     */
    public DefaultBindableJTextField(final Document doc, final String text, final int columns) {
        super(doc, text, columns);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getBindingProperty() {
        return "text"; // NOI18N
    }

    @Override
    public Converter getConverter() {
        return converter;
    }
    /**
     * DOCUMENT ME!
     *
     * @param  converter  DOCUMENT ME!
     */
    public void setConverter(final Converter converter) {
        this.converter = converter;
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public Object getNullSourceValue() {
        return "";
    }

    @Override
    public Object getErrorSourceValue() {
        return "";
    }
}
