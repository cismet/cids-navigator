/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;


import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.tools.metaobjectrenderer.Titled;
import java.awt.LayoutManager;
import java.util.HashMap;
import javax.swing.JPanel;
import org.jdesktop.beansbinding.BindingGroup;

/**
 *
 * @author thorsten
 */
public class DefaultCidsEditor extends JPanel implements AutoBindableCidsEditor,Titled {
    private HashMap<String, Bindable> controls = new HashMap<String, Bindable>();
    private CidsBean cidsBean = null;
    private BindingGroup bindingGroup = new BindingGroup();
    private String customTitle=null;

    private void init(){
        setOpaque(false);
    }

    public DefaultCidsEditor() {
        init();
    }

    public DefaultCidsEditor(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    public DefaultCidsEditor(LayoutManager layout) {
        super(layout);
        init();
    }

    public DefaultCidsEditor(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    public void addControlInformation(String name, Bindable component) {
        controls.put(name, component);
    }

    public Bindable getControlByName(String name) {
        return controls.get(name);
    }

    public HashMap<String, Bindable> getAllControls() {
        return controls;
    }

    public BindingGroup getBindingGroup() {
        return bindingGroup;
    }

    public CidsBean getCidsBean() {
        return cidsBean;
    }

    public void setCidsBean(CidsBean cidsBean) {
        this.cidsBean = cidsBean;
    }


    public String getTitle() {
        if (customTitle!=null){
            return customTitle;
        }
        else if (cidsBean!=null&&cidsBean.getMetaObject()!=null){
            return cidsBean.getMetaObject().getMetaClass().getName();
        }
        else {
            return org.openide.util.NbBundle.getMessage(DefaultCidsEditor.class, "DefaultCidsEditor.getTitle().defaultTitle");//NOI18N
        }
    }

    public void setTitle(String title) {
        customTitle=title;
    }
}
