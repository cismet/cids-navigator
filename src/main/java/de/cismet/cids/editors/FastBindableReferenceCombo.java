/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;

import Sirius.server.middleware.types.AbstractAttributeRepresentationFormater;
import Sirius.server.middleware.types.LightweightMetaObject;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import java.awt.Component;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.cismet.cids.client.tools.ConnectionContextUtils;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cidsx.server.search.builtin.legacy.LightweightMetaObjectsByQuerySearch;
import de.cismet.cidsx.server.search.builtin.legacy.LightweightMetaObjectsSearch;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class FastBindableReferenceCombo extends JComboBox implements Bindable,
    MetaClassStore,
    Serializable,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Comparator<MetaObject> LWMO_COMP = new Comparator<MetaObject>() {

            @Override
            public final int compare(final MetaObject o1, final MetaObject o2) {
                return String.CASE_INSENSITIVE_ORDER.compare((o1 != null) ? o1.toString() : "",
                        (o2 != null) ? o2.toString() : ""); // NOI18N
            }
        };

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            FastBindableReferenceCombo.class);

    //~ Instance fields --------------------------------------------------------

    private final AbstractAttributeRepresentationFormater representationFormater;
    private final String representation;
    @Deprecated private final String query;
    private final LightweightMetaObjectsSearch lwmoSearch;
    private boolean nullable = true;
    private boolean sorted = false;
    private String[] representationFields;
    private CidsBean cidsBean = null;
    private MetaClass metaClass = null;
    private String nullValueRepresentation = "-"; // NOI18N

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FastBindableReferenceCombo object.
     */
    public FastBindableReferenceCombo() {
        this("%1$2s", new String[] { "NAME" });
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  representation        DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableReferenceCombo(final String representation, final String[] representationFields) {
        this((LightweightMetaObjectsSearch)null, representation, representationFields); // NOI18N
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  query                 DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    @Deprecated
    public FastBindableReferenceCombo(final String query,
            final String representation,
            final String[] representationFields) {
        this(query, representation, null, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableReferenceCombo(final LightweightMetaObjectsSearch lwmoSearch,
            final String representation,
            final String[] representationFields) {
        this(lwmoSearch, representation, null, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  query                 DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    @Deprecated
    public FastBindableReferenceCombo(final String query,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        this(query, null, formater, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    public FastBindableReferenceCombo(final LightweightMetaObjectsSearch lwmoSearch,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        this(lwmoSearch, null, formater, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  query                 DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    @Deprecated
    private FastBindableReferenceCombo(final String query,
            final String representation,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        this(null, query, representation, formater, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    private FastBindableReferenceCombo(final LightweightMetaObjectsSearch lwmoSearch,
            final String representation,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        this(lwmoSearch, null, representation, formater, representationFields);
    }

    /**
     * Creates a new FastBindableReferenceCombo object.
     *
     * @param  lwmoSearch            DOCUMENT ME!
     * @param  query                 DOCUMENT ME!
     * @param  representation        DOCUMENT ME!
     * @param  formater              DOCUMENT ME!
     * @param  representationFields  DOCUMENT ME!
     */
    @Deprecated
    private FastBindableReferenceCombo(final LightweightMetaObjectsSearch lwmoSearch,
            final String query,
            final String representation,
            final AbstractAttributeRepresentationFormater formater,
            final String[] representationFields) {
        final String[] s = new String[] { null };
        this.lwmoSearch = lwmoSearch;
        this.query = query;
        this.metaClass = null;
        this.representation = representation;
        this.representationFormater = formater;
        this.representationFields = representationFields;
        setModel(new DefaultComboBoxModel(s));
        final DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
        setRenderer(new ListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final Component ret = dlcr.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus);
                    if (value == null) {
                        ((JLabel)ret).setText(getNullValueRepresentation());
                    }
                    return ret;
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public ConnectionContext getConnectionContext() {
        return ConnectionContextUtils.getFirstParentClientConnectionContext(this);
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        try {
            final DefaultComboBoxModel mod = createModelForMetaClass(nullable);
            setModel(mod);
            setSelectedItem(cidsBean);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIndexOf(final Object o) {
        final ComboBoxModel m = getModel();
        if (m instanceof DefaultComboBoxModel) {
            return ((DefaultComboBoxModel)m).getIndexOf(o);
        } else if (m != null) {
            for (int i = 0; i < m.getSize(); ++i) {
                final Object cur = m.getElementAt(i);
                if ((o == cur) || ((cur != null) && cur.equals(o))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public String getBindingProperty() {
        return "selectedItem"; // NOI18N
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
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object getSelectedItem() {
        return cidsBean;
    }

    /**
     * DANGER: could be buggy :(
     *
     * @param  anObject  DOCUMENT ME!
     */
    @Override
    public void setSelectedItem(final Object anObject) {
        if (anObject != null) {
            if (anObject instanceof CidsBean) {
                cidsBean = (CidsBean)anObject;
            } else if (anObject instanceof MetaObject) {
                try {
                    cidsBean = ((MetaObject)anObject).getBean();
                } catch (Exception ex) {
                    cidsBean = null;
                    LOG.error(ex, ex);
                }
            }
        } else {
            cidsBean = null;
        }
        // just to notify listeners
        super.setSelectedItem(anObject);
    }

    /**
     * DANGER: could be buggy :(
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getSelectedIndex() {
        return getIndexOf(cidsBean);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getNullValueRepresentation() {
        return nullValueRepresentation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nullValueRepresentation  DOCUMENT ME!
     */
    public void setNullValueRepresentation(final String nullValueRepresentation) {
        this.nullValueRepresentation = nullValueRepresentation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nullable  DOCUMENT ME!
     */
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * DOCUMENT ME!
     */
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

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public final MetaObject[] receiveLightweightMetaObjects() throws Exception {
        final MetaClass mc = metaClass;
        if (mc == null) {
            LOG.error("MetaClass is null!", new Exception()); // NOI18N
            return new MetaObject[0];
        }
        MetaObject[] lwmos;

        final LightweightMetaObjectsSearch search;

        if ((query != null) && (query.trim().length() > 1)) {
            search = new LightweightMetaObjectsByQuerySearch();
            ((LightweightMetaObjectsByQuerySearch)search).initWithConnectionContext(getConnectionContext());
            ((LightweightMetaObjectsByQuerySearch)search).setDomain(mc.getDomain());
            ((LightweightMetaObjectsByQuerySearch)search).setClassId(mc.getID());
            ((LightweightMetaObjectsByQuerySearch)search).setQuery(query);
        } else {
            search = this.lwmoSearch;
        }

        if (search != null) {
            search.setRepresentationFields(getRepresentationFields());
            if (representationFormater == null) {
                search.setRepresentationPattern(getRepresentation());
            }
            final Collection<LightweightMetaObject> results = SessionManager.getProxy()
                        .customServerSearch(SessionManager.getSession().getUser(),
                            search,
                            getConnectionContext());
            if (representationFormater != null) {
                for (final LightweightMetaObject result : results) {
                    result.setFormater(representationFormater);
                }
            }
            lwmos = results.toArray(new MetaObject[0]);
        } else {
            if (representationFormater != null) {
                lwmos = SessionManager.getProxy()
                            .getAllLightweightMetaObjectsForClass(mc.getID(),
                                    SessionManager.getSession().getUser(),
                                    getRepresentationFields(),
                                    representationFormater,
                                    getConnectionContext());
            } else {
                lwmos = SessionManager.getProxy()
                            .getAllLightweightMetaObjectsForClass(mc.getID(),
                                    SessionManager.getSession().getUser(),
                                    getRepresentationFields(),
                                    getRepresentation(),
                                    getConnectionContext());
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

    /**
     * DOCUMENT ME!
     *
     * @param   nullable  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public DefaultComboBoxModel createModelForMetaClass(final boolean nullable) throws Exception {
        final MetaObject[] lwmos = receiveLightweightMetaObjects();
//        final DefaultComboBoxModel model = new DefaultComboBoxModel(lwmos);
        final DefaultComboBoxModel model = new MetaObjectComboBoxModel(lwmos);
        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the sorted
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sorted  the sorted to set
     */
    public void setSorted(final boolean sorted) {
        this.sorted = sorted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the query
     */
    @Deprecated
    public String getQuery() {
        return query;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the representation
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the representationFields
     */
    public String[] getRepresentationFields() {
        return representationFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  representationFields  the representationFields to set
     */
    public void setRepresentationFields(final String[] representationFields) {
        this.representationFields = representationFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  domain   DOCUMENT ME!
     * @param  tabname  DOCUMENT ME!
     */
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

    /**
     * DOCUMENT ME!
     *
     * @param  domain   DOCUMENT ME!
     * @param  tabname  DOCUMENT ME!
     */
    private void setMetaClassFromTableNameImpl(final String domain, final String tabname) {
        if ((tabname != null) && (tabname.length() > 0)) {
            this.metaClass = ClassCacheMultiple.getMetaClass(domain, tabname, getConnectionContext());
            if (metaClass == null) {
                LOG.error("Could not find MetaClass for Table " + tabname + " in domain " + domain); // NOI18N
            }
            init();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
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

    /**
     * DOCUMENT ME!
     *
     * @param  metaClass  DOCUMENT ME!
     */
    private void setMetaClassImpl(final MetaClass metaClass) {
        this.metaClass = metaClass;
        init();
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
final class MetaObjectComboBoxModel extends DefaultComboBoxModel {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetaObjectComboBoxModel.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaObjectComboBoxModel object.
     */
    public MetaObjectComboBoxModel() {
        super();
    }

    /**
     * Creates a new MetaObjectComboBoxModel object.
     *
     * @param  v  DOCUMENT ME!
     */
    public MetaObjectComboBoxModel(final Vector<?> v) {
        super(checkTypes(v.toArray()));
    }

    /**
     * Creates a new MetaObjectComboBoxModel object.
     *
     * @param  items  DOCUMENT ME!
     */
    public MetaObjectComboBoxModel(final Object[] items) {
        super(checkTypes(items));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   items  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Object[] checkTypes(final Object[] items) {
        for (final Object item : items) {
            checkType(item);
        }
        return items;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   item  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Object checkType(final Object item) {
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
    public void addElement(final Object anObject) {
        super.addElement(checkType(anObject));
    }

    @Override
    public void setSelectedItem(Object anObject) {
//        log.fatal("setSelectedItem: " + anObject, new Exception());
        if (anObject instanceof CidsBean) {
            anObject = ((CidsBean)anObject).getMetaObject();
        }
        super.setSelectedItem(checkType(anObject));
    }

    @Override
    public Object getSelectedItem() {
        final Object o = super.getSelectedItem();
//        if (o instanceof MetaObject) {
//            o = ((MetaObject) o).getBean();
//        }
//        log.fatal("getSelectedItem: " + o, new Exception());
        return o;
    }

    @Override
    public int getIndexOf(final Object anObject) {
        if (anObject instanceof MetaObject) {
            return findIndexForMetaObject((MetaObject)anObject);
        } else if (anObject instanceof CidsBean) {
            return findIndexForCidsBean((CidsBean)anObject);
        }
//        log.fatal("getIndexOf: " + anObject +" as "+super.getIndexOf(anObject), new Exception());
        return super.getIndexOf(anObject);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int findIndexForMetaObject(final MetaObject mo) {
        if (mo != null) {
            final int objID = mo.getID();
            final int classID = mo.getClassID();
            // search for MO
            for (int i = 0; i < getSize(); ++i) {
                final Object item = getElementAt(i);
                if (item instanceof MetaObject) {
                    final MetaObject curMo = (MetaObject)item;
                    if ((curMo.getID() == objID) && (curMo.getClassID() == classID)) {
                        return i;
                    }
                }
            }
        } else {
            // search for null
            for (int i = 0; i < getSize(); ++i) {
                final Object item = getElementAt(i);
                if (item == null) {
                    return i;
                }
            }
        }

        // nothing found
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   bean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int findIndexForCidsBean(final CidsBean bean) {
        if (bean != null) {
            return findIndexForMetaObject(bean.getMetaObject());
        } else {
            return findIndexForMetaObject(null);
        }
    }
}
