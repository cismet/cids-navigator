/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author thorsten
 */
public interface Bindable {
    public String getBindingProperty();
    public Validator getValidator();
    public Converter getConverter();
    public Object getNullSourceValue();
    public Object getErrorSourceValue();
}
