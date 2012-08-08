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
import javax.swing.text.Document;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanStore;

/**
 * de.cismet.ammunition.cids.BildTestBindable.
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class BildTestBindable extends JTextField implements Bindable, CidsBeanStore {

    //~ Instance fields --------------------------------------------------------

    private CidsBean cidsBean = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BildTestBindable object.
     */
    public BildTestBindable() {
        super();
    }

    /**
     * Creates a new BildTestBindable object.
     *
     * @param  columns  DOCUMENT ME!
     */
    public BildTestBindable(final int columns) {
        super(columns);
    }

    /**
     * Creates a new BildTestBindable object.
     *
     * @param  text  DOCUMENT ME!
     */
    public BildTestBindable(final String text) {
        super(text);
    }

    /**
     * Creates a new BildTestBindable object.
     *
     * @param  text     DOCUMENT ME!
     * @param  columns  DOCUMENT ME!
     */
    public BildTestBindable(final String text, final int columns) {
        super(text, columns);
    }

    /**
     * Creates a new BildTestBindable object.
     *
     * @param  doc      DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  columns  DOCUMENT ME!
     */
    public BildTestBindable(final Document doc, final String text, final int columns) {
        super(doc, text, columns);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getBindingProperty() {
        return "text"; // NOI18N
    }

    @Override
    public Converter getConverter() {
        return new Converter<CidsBean, String>() {

                @Override
                public String convertForward(final CidsBean value) {
                    try {
                        if (value != null) {
                            cidsBean = value;
                            return (String)value.getProperty("url"); // NOI18N
                        }
                    } catch (Exception e) {
                    }
                    return null;
                }

                @Override
                public CidsBean convertReverse(final String value) {
                    try {
                        if (cidsBean != null) {
                            cidsBean.setProperty("url", value); // NOI18N
                        }
                    } catch (Exception exception) {
                    }
                    return cidsBean;
                }
            };
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        this.cidsBean = cidsBean;
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }
}
