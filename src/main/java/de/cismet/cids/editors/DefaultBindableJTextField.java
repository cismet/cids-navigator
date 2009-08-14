/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author thorsten
 */
public class DefaultBindableJTextField extends JTextField implements Bindable{
    Converter converter=null;

    public DefaultBindableJTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public DefaultBindableJTextField(String text, int columns) {
        super(text, columns);
    }

    public DefaultBindableJTextField(int columns) {
        super(columns);
    }

    public DefaultBindableJTextField(String text) {
        super(text);
    }

    public DefaultBindableJTextField() {
    }

    public String getBindingProperty() {
        return "text";
    }

    public Converter getConverter() {
        return converter;
    }
    public void setConverter(Converter converter) {
        this.converter=converter;
    }

    public Validator getValidator() {
        return null;
    }


}
