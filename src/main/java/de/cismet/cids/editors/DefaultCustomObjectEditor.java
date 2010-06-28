/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultCustomObjectEditor.java
 *
 * Created on 11.03.2009, 16:56:02
 */
package de.cismet.cids.editors;

import Sirius.server.localserver.attribute.ObjectAttribute;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.DisposableCidsBeanStore;
import java.awt.Component;
import java.lang.reflect.Field;
import java.util.List;
import javax.swing.JComponent;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.el.impl.ValueExpressionImpl;
import org.openide.util.Exceptions;

/**
 *
 * @author thorsten
 */
public class DefaultCustomObjectEditor extends javax.swing.JPanel implements DisposableCidsBeanStore {

    protected CidsBean cidsBean;
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultCustomObjectEditor.class);

    /** Creates new form DefaultCustomObjectEditor */
    public DefaultCustomObjectEditor() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    synchronized public void setCidsBean(CidsBean cidsBean) {
        this.cidsBean = cidsBean;
        try {
            BindingGroup bindingGroup = getBindingGroupFormChildClass();
            setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(bindingGroup, cidsBean);


            bindingGroup.unbind();
            bindingGroup.bind();
            boundAndReadyNotify();
        } catch (Exception e) {
            throw new RuntimeException("Bindingproblems occur", e);
        }
    }

    private BindingGroup getBindingGroupFormChildClass() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        BindingGroup bindingGroup = null;
        if (this instanceof BindingGroupStore) {
            bindingGroup = ((BindingGroupStore) this).getBindingGroup();
        } else {

            Field bindingGroupField = getClass().getDeclaredField("bindingGroup");

            bindingGroupField.setAccessible(true);

            bindingGroup = (BindingGroup) bindingGroupField.get(this);
        }
        return bindingGroup;
    }

    public static void setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(BindingGroup bindingGroup, CidsBean cidsBean) throws Exception {
        List<Binding> bindings = bindingGroup.getBindings();
        for (Binding binding : bindings) {
            if (binding.getTargetObject() instanceof MetaClassStore) {
                //log.fatal("MetaClassStores gefunden");
                String fieldname = null;
                try {
                    MetaClassStore mcs = (MetaClassStore) binding.getTargetObject();
                    ELProperty p = (ELProperty) binding.getSourceProperty();
                    String expr = getPropertyStringOutOfELProperty(p);
                    expr = expr.substring(expr.indexOf(".") + 1);
                    expr = expr.substring(0, expr.length() - 1);

                    //in expr steckt in den allermeisten faellen ein feldname
                    //es kann aber auch sein, dass ein zusammengesetzter feldname vorkommt: subobject.fieldname




                    fieldname = expr;




                    ObjectAttribute oa = cidsBean.getMetaObject().getAttributeByFieldName(fieldname);
                    String domain = cidsBean.getMetaObject().getDomain();
                    int foreignClassId = oa.getMai().getForeignKeyClassId();

                    MetaClass foreignClass = CidsObjectEditorFactory.getMetaClass(domain, foreignClassId);
                    mcs.setMetaClass(foreignClass);
                } catch (Exception e) {
                    throw new RuntimeException("Error during Bind: " + fieldname + " of " + cidsBean.getMetaObject().getMetaClass(), e);
                }

            }
        }

    }

    private static String getPropertyStringOutOfELProperty(ELProperty p) throws Exception {
        Field expressionField = p.getClass().getDeclaredField("expression");
        expressionField.setAccessible(true);

        ValueExpressionImpl valueExpression = (ValueExpressionImpl) expressionField.get(p);
        return valueExpression.getExpressionString();

    }

    public void boundAndReadyNotify() {
    }

    @Override
    public void dispose() {
        try {
            BindingGroup bindingGroup = getBindingGroupFormChildClass();
            bindingGroup.unbind();
        } catch (Exception ex) {
            throw new RuntimeException("Binding Problem!", ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
