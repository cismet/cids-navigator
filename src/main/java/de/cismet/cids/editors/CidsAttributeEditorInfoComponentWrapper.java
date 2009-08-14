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
public class CidsAttributeEditorInfoComponentWrapper implements CidsAttributeEditorInfo {
    private String bindingProperty=null;
    private JComponent component=null;
    private Converter converter=null;
    private Validator validator=null;

    public CidsAttributeEditorInfoComponentWrapper(JComponent component,String bindingProperty,Converter converter,Validator validator){
        this.component=component;
        this.bindingProperty=bindingProperty;
        this.converter=converter;
        this.validator=validator;
    }
    public CidsAttributeEditorInfoComponentWrapper(JComponent component){
        this(component,null,null,null);
    }
    public CidsAttributeEditorInfoComponentWrapper(JComponent component,String bindingProperty){
        this(component, bindingProperty,null,null);
    }
    public CidsAttributeEditorInfoComponentWrapper(JComponent component,String bindingProperty,Converter converter){
        this(component, bindingProperty, converter,null);
    }
    public CidsAttributeEditorInfoComponentWrapper(JComponent component,String bindingProperty,Validator validator){
        this(component, bindingProperty, null,validator);
    }


    
    

    public String getBindingProperty() {
        return bindingProperty;
    }

    public JComponent getComponent() {
        return component;
    }

    public Converter getConverter() {
        return converter;
    }

    public Validator getValidator() {
        return validator;
    }

}
