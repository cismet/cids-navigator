/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author thorsten
 */
public class DefaultBindableJCheckBox extends JCheckBox implements Bindable {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultBindableJCheckBox.class);
    private static final Converter<Boolean, Boolean> NULL_CONVERTER = new Converter<Boolean, Boolean>() {

        @Override
        public Boolean convertForward(Boolean s) {
            if (s == null) {
                return Boolean.FALSE;
            } else {
                return s;
            }
        }

        @Override
        public Boolean convertReverse(Boolean t) {
            if (t == null) {
                return Boolean.FALSE;
            } else {
                return t;
            }
        }
    };

    public DefaultBindableJCheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }

    public DefaultBindableJCheckBox(String text, Icon icon) {
        super(text, icon);
    }

    public DefaultBindableJCheckBox(String text, boolean selected) {
        super(text, selected);
    }

    public DefaultBindableJCheckBox(Action a) {
        super(a);
    }

    public DefaultBindableJCheckBox(String text) {
        super(text);
    }

    public DefaultBindableJCheckBox(Icon icon, boolean selected) {
        super(icon, selected);
    }

    public DefaultBindableJCheckBox(Icon icon) {
        super(icon);
    }

    public DefaultBindableJCheckBox() {
        super();
        setOpaque(false);
    }

    public String getBindingProperty() {
        return "selected";//NOI18N
    }

    public Converter getConverter() {
        return NULL_CONVERTER;
    }

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
