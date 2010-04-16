/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;
import Sirius.server.middleware.types.AbstractAttributeRepresentationFormater;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import java.awt.Component;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author srichter
 */
public class FastBindableReferenceCombo extends JComboBox implements Bindable, MetaClassStore, Serializable {

    private static final Comparator<MetaObject> LWMO_COMP = new Comparator<MetaObject>() {

        @Override
        public final int compare(MetaObject o1, MetaObject o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1 != null ? o1.toString() : "", o2 != null ? o2.toString() : "");
        }
    };
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastBindableReferenceCombo.class);
    private final AbstractAttributeRepresentationFormater representationFormater;
    private final String representation;
    private final String query;
    private boolean nullable = true;
    private boolean sorted = false;
    private String[] representationFields;
    private CidsBean cidsBean = null;
    private MetaClass metaClass = null;
    private String nullValueRepresentation = "-";

    public FastBindableReferenceCombo(String representation, String[] representationFields) {
        this("", representation, representationFields);
    }

    public FastBindableReferenceCombo(String query, String representation, String[] representationFields) {
        this(query, representation, null, representationFields);
    }

    public FastBindableReferenceCombo(String query, AbstractAttributeRepresentationFormater formater, String[] representationFields) {
        this(query, null, formater, representationFields);
    }

    private FastBindableReferenceCombo(String query, String representation, AbstractAttributeRepresentationFormater formater, String[] representationFields) {
        final String[] s = new String[]{null};
        this.query = query;
        this.metaClass = null;
        this.representation = representation;
        this.representationFormater = formater;
        this.representationFields = representationFields;
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

    private void init() {
        try {
            final DefaultComboBoxModel mod = createModelForMetaClass(nullable);
            setModel(mod);
            setSelectedItem(cidsBean);
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

    public int getIndexOf(Object o) {
        final ComboBoxModel m = getModel();
        if (m instanceof DefaultComboBoxModel) {
            return ((DefaultComboBoxModel) m).getIndexOf(o);
        } else if (m != null) {
            for (int i = 0; i < m.getSize(); ++i) {
                Object cur = m.getElementAt(i);
                if (o == cur || cur != null && cur.equals(o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public String getBindingProperty() {
        return "selectedItem";
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
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }

    /**
     * DANGER: could be buggy :(
     * @return
     */
    @Override
    public Object getSelectedItem() {
        return cidsBean;
    }

    /**
     * DANGER: could be buggy :(
     * @param anObject
     */
    @Override
    public void setSelectedItem(Object anObject) {
        if (anObject != null) {
            if (anObject instanceof CidsBean) {
                cidsBean = (CidsBean) anObject;
            } else if (anObject instanceof MetaObject) {
                try {
                    cidsBean = ((MetaObject) anObject).getBean();
                } catch (Exception ex) {
                    cidsBean = null;
                    log.error(ex, ex);
                }
            }
        } else {
            cidsBean = null;
        }
        //just to notify listeners
        super.setSelectedItem(anObject);
    }

    /**
     * DANGER: could be buggy :(
     * @return
     */
    @Override
    public int getSelectedIndex() {
        return getIndexOf(cidsBean);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
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

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void refreshModel() {
//        if (EventQueue.isDispatchThread()) {
//            init();
//        } else {
//            EventQueue.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
        init();
//                }
//            });
//        }
    }

    public final MetaObject[] receiveLightweightMetaObjects() throws Exception {
        final MetaClass mc = metaClass;
        if (mc == null) {
            log.error("MetaClass is null!", new Exception());
            return new MetaObject[0];
        }
        MetaObject[] lwmos;
        if (query != null && query.trim().length() > 1) {
            if (representationFormater != null) {
                lwmos = SessionManager.getProxy().getLightweightMetaObjectsByQuery(mc.getID(), SessionManager.getSession().getUser(), query, getRepresentationFields(), representationFormater);
            } else {
                lwmos = SessionManager.getProxy().getLightweightMetaObjectsByQuery(mc.getID(), SessionManager.getSession().getUser(), query, getRepresentationFields(), getRepresentation());
            }
        } else {
            if (representationFormater != null) {
                lwmos = SessionManager.getProxy().getAllLightweightMetaObjectsForClass(mc.getID(), SessionManager.getSession().getUser(), getRepresentationFields(), representationFormater);
            } else {
                lwmos = SessionManager.getProxy().getAllLightweightMetaObjectsForClass(mc.getID(), SessionManager.getSession().getUser(), getRepresentationFields(), getRepresentation());
            }
        }
        if (sorted) {
            Arrays.sort(lwmos, LWMO_COMP);
        }
        if (nullable) {
            final MetaObject[] withNull = new MetaObject[lwmos.length + 1];
            System.arraycopy(lwmos, 0, withNull, 1, lwmos.length);
            lwmos = withNull;
        }
        return lwmos;
    }

    public DefaultComboBoxModel createModelForMetaClass(boolean nullable) throws Exception {
        MetaObject[] lwmos = receiveLightweightMetaObjects();
//        final DefaultComboBoxModel model = new DefaultComboBoxModel(lwmos);
        final DefaultComboBoxModel model = new MetaObjectComboBoxModel(lwmos);
        return model;
    }

    /**
     * @return the sorted
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * @param sorted the sorted to set
     */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return the representation
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * @return the representationFields
     */
    public String[] getRepresentationFields() {
        return representationFields;
    }

    /**
     * @param representationFields the representationFields to set
     */
    public void setRepresentationFields(String[] representationFields) {
        this.representationFields = representationFields;
    }

    public void setMetaClassFromTableName(final String domain, final String tabname) {
//        if (EventQueue.isDispatchThread()) {
        setMetaClassFromTableNameImpl(domain, tabname);
//        } else {
//            EventQueue.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
//                    setMetaClassFromTableNameImpl(domain, tabname);
//                }
//            });
//        }
    }

    private final void setMetaClassFromTableNameImpl(final String domain, final String tabname) {
        if (tabname != null && tabname.length() > 0) {
            this.metaClass = ClassCacheMultiple.getMetaClass(domain, tabname);
            if (metaClass == null) {
                log.error("Could not find MetaClass for Table " + tabname + " in domain " + domain);
            }
            init();
        }
    }

    public MetaClass getCurrentMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(final MetaClass metaClass) {
//        if (EventQueue.isDispatchThread()) {
        setMetaClassImpl(metaClass);
//        } else {
//            EventQueue.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
//                    setMetaClassImpl(metaClass);
//
//                }
//            });
//        }
    }

    private final void setMetaClassImpl(final MetaClass metaClass) {
        this.metaClass = metaClass;
        init();
    }
}

final class MetaObjectComboBoxModel extends DefaultComboBoxModel {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetaObjectComboBoxModel.class);

    public MetaObjectComboBoxModel() {
        super();
    }

    public MetaObjectComboBoxModel(Vector<?> v) {
        super(checkTypes(v.toArray()));
    }

    public MetaObjectComboBoxModel(Object[] items) {
        super(checkTypes(items));
    }

    private static final Object[] checkTypes(final Object[] items) {
        for (final Object item : items) {
            checkType(item);
        }
        return items;
    }

    private static final Object checkType(final Object item) {
//        if (!(item instanceof MetaObject || item == null)) {
//            log.fatal("!", new Exception());
//            throw new IllegalStateException("Can not fill CidsBeanComboBoxModel with item " + item + " of class " + item.getClass() +
//                    "!. Model supports only instances of CidsBean and MetaObject!");
//        }

//        if(item != null) {
//            log.fatal("type: "+item.getClass());
//        } else {
//            log.fatal("type is null");
//        }

        return item;
    }

    @Override
    public void addElement(Object anObject) {
        super.addElement(checkType(anObject));
    }

    @Override
    public void setSelectedItem(Object anObject) {
//        log.fatal("setSelectedItem: " + anObject, new Exception());
        if (anObject instanceof CidsBean) {
            anObject = ((CidsBean) anObject).getMetaObject();
        }
        super.setSelectedItem(checkType(anObject));
    }

    @Override
    public Object getSelectedItem() {
        Object o = super.getSelectedItem();
//        if (o instanceof MetaObject) {
//            o = ((MetaObject) o).getBean();
//        }
//        log.fatal("getSelectedItem: " + o, new Exception());
        return o;
    }

    @Override
    public int getIndexOf(Object anObject) {
        if (anObject instanceof MetaObject) {
            return findIndexForMetaObject((MetaObject) anObject);
        } else if (anObject instanceof CidsBean) {
            return findIndexForCidsBean((CidsBean) anObject);
        }
//        log.fatal("getIndexOf: " + anObject +" as "+super.getIndexOf(anObject), new Exception());
        return super.getIndexOf(anObject);
    }

    final int findIndexForMetaObject(final MetaObject mo) {
        if (mo != null) {
            int objID = mo.getID();
            int classID = mo.getClassID();
            //search for MO
            for (int i = 0; i < getSize(); ++i) {
                final Object item = getElementAt(i);
                if (item instanceof MetaObject) {
                    final MetaObject curMo = (MetaObject) item;
                    if (curMo.getID() == objID && curMo.getClassID() == classID) {
                        return i;
                    }
                }
            }
        } else {
            //search for null
            for (int i = 0; i < getSize(); ++i) {
                final Object item = getElementAt(i);
                if (item == null) {
                    return i;
                }
            }
        }

        //nothing found
        return -1;
    }

    final int findIndexForCidsBean(final CidsBean bean) {
        if (bean != null) {
            return findIndexForMetaObject(bean.getMetaObject());
        } else {
            return findIndexForMetaObject(null);
        }
    }
}



