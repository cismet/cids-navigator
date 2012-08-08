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

import java.beans.*;

import java.io.Serializable;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class NewBean implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private String sampleProperty;

    private PropertyChangeSupport propertySupport;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NewBean object.
     */
    public NewBean() {
        propertySupport = new PropertyChangeSupport(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSampleProperty() {
        return sampleProperty;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     */
    public void setSampleProperty(final String value) {
        final String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
