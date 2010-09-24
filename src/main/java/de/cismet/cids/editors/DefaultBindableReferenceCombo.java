/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;
import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.tools.CismetThreadPool;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingWorker;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author thorsten
 */
public class DefaultBindableReferenceCombo extends JComboBox implements Bindable, MetaClassStore, Serializable {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultBindableReferenceCombo.class);
    private CidsBean cidsBean = null;
    private MetaClass metaClass = null;
//    private String fieldname = null;
    private boolean fakeModel = false;
    private boolean nullable = false;
    private boolean onlyUsed = false;
    private String nullValueRepresentation = null;
    private static final Comparator<CidsBean> beanToStringComparator;

    static {
        beanToStringComparator = new Comparator<CidsBean>() {

            @Override
            public final int compare(CidsBean o1, CidsBean o2) {
                return (String.valueOf(o1)).compareToIgnoreCase(String.valueOf(o2));//NOI18N
            }
        };
    }

    public DefaultBindableReferenceCombo(final MetaClass mc) {
        this();
        init(mc);
    }

    public DefaultBindableReferenceCombo(final MetaClass mc, boolean nullable, boolean onlyUsed) {
        this();
        this.nullable = nullable;
        this.onlyUsed = onlyUsed;
        init(mc);
    }

    public DefaultBindableReferenceCombo() {
        String[] s = new String[]{null};

        setModel(new DefaultComboBoxModel(s));
        final DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
        setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component ret = dlcr.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    ((JLabel) ret).setText(getNullValueRepresentation());
                }
                return ret;
            }
        });
    }

    private void init(final MetaClass mc) {
        if (!isFakeModel()) {
            CismetThreadPool.execute(new SwingWorker<DefaultComboBoxModel, Void>() {

                @Override
                protected DefaultComboBoxModel doInBackground() throws Exception {
                    return getModelByMetaClass(mc, nullable, onlyUsed);
                }

                @Override
                protected void done() {

                    try {
                        setModel(get());
                        setSelectedItem(cidsBean);
                    } catch (InterruptedException interruptedException) {
                    } catch (ExecutionException executionException) {
                        log.error("Error while initializing the model of a referenceCombo", executionException);//NOI18N
                    }


                }
            });
        } else {
        }
    }

    @Override
    public String getBindingProperty() {
        return "selectedItem";//NOI18N
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public void setSelectedItem(final Object anObject) {
        if (isFakeModel()) {
            setModel(new DefaultComboBoxModel(new Object[]{anObject}));
        }
        super.setSelectedItem(anObject);
        cidsBean = (CidsBean) anObject;

    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
        init(metaClass);
    }

    public boolean isFakeModel() {
        return fakeModel;
    }

    public void setFakeModel(boolean fakeModel) {
        this.fakeModel = fakeModel;
    }

    public String getNullValueRepresentation() {
        return nullValueRepresentation;
    }

    public void setNullValueRepresentation(String nullValueRepresentation) {
        this.nullValueRepresentation = nullValueRepresentation;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setOnlyUsed(boolean onlyUsed) {
        this.onlyUsed = onlyUsed;
    }

    public boolean isOnlyUsed() {
        return onlyUsed;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }

    public static DefaultComboBoxModel getModelByMetaClass(MetaClass mc, boolean nullable, boolean onlyUsed) throws Exception {
        ClassAttribute ca = mc.getClassAttribute("sortingColumn");//NOI18N
        String orderBy = "";//NOI18N
        if (ca != null) {
            String value = ca.getValue().toString();
            orderBy = " order by " + value;//NOI18N
        }
        String query = "select " + mc.getID() + "," + mc.getPrimaryKey() + " from " + mc.getTableName();//NOI18N
        if (onlyUsed) {
            query += " where used is true";//NOI18N
        }
        query += orderBy;
        final MetaObject[] MetaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
        final List<CidsBean> cbv = new ArrayList<CidsBean>(MetaObjects.length);
        if (nullable) {
            cbv.add(null);
        }
        for (MetaObject mo : MetaObjects) {
            cbv.add(mo.getBean());
        }
        if (ca == null) {
            //Sorts the model using String comparison on the bean's toString()
            Collections.sort(cbv, beanToStringComparator);
        }
        return new DefaultComboBoxModel(cbv.toArray());
    }

    public static DefaultComboBoxModel getModelByMetaClass(MetaClass mc, boolean nullable) throws Exception {
        return getModelByMetaClass(mc, nullable, false);
    }
}
