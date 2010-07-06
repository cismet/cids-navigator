/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanStore;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 * de.cismet.ammunition.cids.BildTestBindable
 * @author thorsten
 */
public class BildTestBindable extends JTextField implements Bindable, CidsBeanStore {

    private CidsBean cidsBean = null;

    public BildTestBindable() {
        super();
    }

    public BildTestBindable(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public BildTestBindable(String text, int columns) {
        super(text, columns);
    }

    public BildTestBindable(int columns) {
        super(columns);
    }

    public BildTestBindable(String text) {
        super(text);
    }

    public String getBindingProperty() {
        return "text";//NOI18N
    }

    public Converter getConverter() {
        return new Converter<CidsBean, String>() {

            @Override
            public String convertForward(CidsBean value) {
                try {
                    if (value != null) {
                        cidsBean = value;
                        return (String) value.getProperty("url");//NOI18N
                    }
                } catch (Exception e) {
                }
                return null;

            }

            @Override
            public CidsBean convertReverse(String value) {
                try {
                    if (cidsBean != null) {
                        cidsBean.setProperty("url", value);//NOI18N
                    }
                } catch (Exception exception) {
                }
                return cidsBean;
            }
        };

    }

    public Validator getValidator() {
        return null;
    }

    public CidsBean getCidsBean() {
        return cidsBean;
    }

    public void setCidsBean(CidsBean cidsBean) {
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
