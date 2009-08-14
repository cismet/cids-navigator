/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.editors;

import javax.swing.JComponent;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author thorsten
 */
public interface CidsAttributeEditorInfo {
    public JComponent getComponent();
    public String getBindingProperty();
    public Converter getConverter();
    public Validator getValidator();
}
