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
public class DefaultBindableJCheckBox extends JCheckBox implements Bindable{

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
        return null;
    }

    public Validator getValidator() {
        return null;
    }

}
