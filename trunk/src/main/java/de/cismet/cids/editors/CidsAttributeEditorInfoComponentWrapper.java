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

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CidsAttributeEditorInfoComponentWrapper implements CidsAttributeEditorInfo {

    //~ Instance fields --------------------------------------------------------

    private String bindingProperty = null;
    private JComponent component = null;
    private Converter converter = null;
    private Validator validator = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsAttributeEditorInfoComponentWrapper object.
     *
     * @param  component  DOCUMENT ME!
     */
    public CidsAttributeEditorInfoComponentWrapper(final JComponent component) {
        this(component, null, null, null);
    }
    /**
     * Creates a new CidsAttributeEditorInfoComponentWrapper object.
     *
     * @param  component        DOCUMENT ME!
     * @param  bindingProperty  DOCUMENT ME!
     */
    public CidsAttributeEditorInfoComponentWrapper(final JComponent component, final String bindingProperty) {
        this(component, bindingProperty, null, null);
    }
    /**
     * Creates a new CidsAttributeEditorInfoComponentWrapper object.
     *
     * @param  component        DOCUMENT ME!
     * @param  bindingProperty  DOCUMENT ME!
     * @param  converter        DOCUMENT ME!
     */
    public CidsAttributeEditorInfoComponentWrapper(final JComponent component,
            final String bindingProperty,
            final Converter converter) {
        this(component, bindingProperty, converter, null);
    }
    /**
     * Creates a new CidsAttributeEditorInfoComponentWrapper object.
     *
     * @param  component        DOCUMENT ME!
     * @param  bindingProperty  DOCUMENT ME!
     * @param  validator        DOCUMENT ME!
     */
    public CidsAttributeEditorInfoComponentWrapper(final JComponent component,
            final String bindingProperty,
            final Validator validator) {
        this(component, bindingProperty, null, validator);
    }

    /**
     * Creates a new CidsAttributeEditorInfoComponentWrapper object.
     *
     * @param  component        DOCUMENT ME!
     * @param  bindingProperty  DOCUMENT ME!
     * @param  converter        DOCUMENT ME!
     * @param  validator        DOCUMENT ME!
     */
    public CidsAttributeEditorInfoComponentWrapper(final JComponent component,
            final String bindingProperty,
            final Converter converter,
            final Validator validator) {
        this.component = component;
        this.bindingProperty = bindingProperty;
        this.converter = converter;
        this.validator = validator;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getBindingProperty() {
        return bindingProperty;
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
