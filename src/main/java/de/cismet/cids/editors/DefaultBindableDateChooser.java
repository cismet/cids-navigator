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
import org.jdesktop.swingx.JXDatePicker;

import java.text.MessageFormat;

import java.util.Date;

import javax.swing.border.EmptyBorder;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableDateChooser extends JXDatePicker implements Bindable {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            DefaultBindableDateChooser.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableDateChooser object.
     */
    public DefaultBindableDateChooser() {
        super();
        inint();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void inint() {
        setBorder(new EmptyBorder(1, 1, 1, 1));
        setEditable(true);
        setFormats(
            new String[] {
                org.openide.util.NbBundle.getMessage(
                    DefaultBindableDateChooser.class,
                    "DefaultBindableDateChooser.inint().Formatstring")
            });                                                            // NOI18N
        setLinkFormat(new MessageFormat(
                org.openide.util.NbBundle.getMessage(
                    DefaultBindableDateChooser.class,
                    "DefaultBindableDateChooser.inint().Messageformat"))); // NOI18N
    }

    @Override
    public String getBindingProperty() {
        return "date"; // NOI18N
    }

    @Override
    public Converter getConverter() {
        return new Converter<java.sql.Date, java.util.Date>() {

                @Override
                public java.util.Date convertForward(final java.sql.Date value) {
                    try {
                        if (value != null) {
                            return new java.util.Date(value.getTime());
                        } else {
                            return null;
                        }
                    } catch (Exception ex) {
                        log.fatal(ex);
                        return new java.util.Date(System.currentTimeMillis());
                    }
                }

                @Override
                public java.sql.Date convertReverse(final java.util.Date value) {
                    try {
                        if (value != null) {
                            return new java.sql.Date(value.getTime());
                        } else {
                            return null;
                        }
                    } catch (Exception ex) {
                        log.fatal(ex);
                        return new java.sql.Date(System.currentTimeMillis());
                    }
                }
            };
    }

    @Override
    public Validator getValidator() {
        throw new UnsupportedOperationException("Not supported yet."); // NOI18N
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }
//    @Override
//    public Date getDate() {
//        log.fatal("getDate returns: " + super.getDate());
//        return super.getDate();
//    }
//    @Override
//    public void setDate(Date date) {
//        log.fatal("setDate to: " + date);
//        super.setDate(date);
//    }
}
