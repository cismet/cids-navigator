/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import java.text.MessageFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author thorsten
 */
public class DefaultBindableDateChooser extends JXDatePicker implements Bindable {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultBindableDateChooser.class);

    public DefaultBindableDateChooser() {
        super();
        inint();
    }

    public void inint() {
        setBorder(new EmptyBorder(1, 1, 1, 1));
        setEditable(true);
        setFormats(new String[]{"dd.MM.yyyy"});
        setLinkFormat(new MessageFormat("Heute ist der {0,date}"));
    }

    public String getBindingProperty() {
        return "date";
    }

    public Converter getConverter() {
        return new Converter<java.sql.Date, java.util.Date>() {

            @Override
            public java.util.Date convertForward(java.sql.Date value) {
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
            public java.sql.Date convertReverse(java.util.Date value) {
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

    public Validator getValidator() {
        throw new UnsupportedOperationException("Not supported yet.");
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
