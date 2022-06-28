/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.tools.CacheException;
import Sirius.navigator.tools.MetaObjectCache;
import Sirius.navigator.tools.MetaObjectChangeEvent;
import Sirius.navigator.tools.MetaObjectChangeListener;
import Sirius.navigator.tools.MetaObjectChangeSupport;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;

import com.jgoodies.looks.plastic.PlasticComboBoxUI;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.text.JTextComponent;

import de.cismet.cids.client.tools.ConnectionContextUtils;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.CismetThreadPool;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableReferenceCombo extends JComboBox implements Bindable,
    MetaClassStore,
    Serializable,
    EditorAndRendererComponent,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DefaultBindableReferenceCombo.class);
    private static final String MESSAGE_LOADING_ITEM = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.loading");
    private static final String MESSAGE_NULLEABLE_ITEM_EDITOR = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.item.nullable");
    private static final String MESSAGE_NULLEABLE_ITEM_RENDERER = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.label.nullable");
    private static final String MESSAGE_MANAGEABLE_ITEM = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.item.manageable");
    private static final String MESSAGE_CREATEITEMDIALOG_TITLE = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.createitemdialog.title");

    private static final Comparator<CidsBean> BEAN_TOSTRING_COMPARATOR = new BeanToStringComparator();

    //~ Instance fields --------------------------------------------------------

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private CidsBean cidsBean;
    @Getter private MetaClass metaClass;
    @Getter @Setter private boolean fakeModel;
    @Getter @Setter private boolean nullable;
    @Getter @Setter private String nullValueRepresentation;
    @Getter @Setter private String nullValueRepresentationInRenderer;
    @Getter @Setter private boolean manageable;
    @Getter @Setter private String manageableItemRepresentation;
    @Getter @Setter private String manageableProperty;
    @Getter @Setter private String where;
    @Getter @Setter private boolean alwaysReload;
    @Getter @Setter private String sortingColumn;
    @Getter @Setter private Comparator<CidsBean> comparator;
    @Getter @Setter private boolean categorised;
    @Getter @Setter private String categorySplitBy;
    @Getter @Setter private boolean categorySelfStructuring;

    private boolean actingAsRenderer;

    private boolean explicitlyEnabledOrDisabled;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     */
    public DefaultBindableReferenceCombo() {
        this((Option)null);
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  nullable  DOCUMENT ME!
     */
    public DefaultBindableReferenceCombo(final boolean nullable) {
        this(nullable ? new NullableOption() : (Option)null);
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  comparator  DOCUMENT ME!
     */
    public DefaultBindableReferenceCombo(final Comparator<CidsBean> comparator) {
        this(new ComparatorOption(comparator));
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  metaClass  DOCUMENT ME!
     */
    public DefaultBindableReferenceCombo(final MetaClass metaClass) {
        this(new MetaClassOption(metaClass));
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  options  DOCUMENT ME!
     */
    public DefaultBindableReferenceCombo(final Option... options) {
        MetaClass metaClass = null;
        boolean fakeModel = false;
        boolean nullable = false;
        String nullValueRepresentationInEditor = MESSAGE_NULLEABLE_ITEM_EDITOR;
        String nullValueRepresentationInRenderer = MESSAGE_NULLEABLE_ITEM_RENDERER;
        boolean manageable = false;
        String manageableItemRepresentation = MESSAGE_MANAGEABLE_ITEM;
        String manageableProperty = null;
        String where = null;
        boolean alwaysReload = false;
        String sortingColumn = null;
        Comparator<CidsBean> comparator = BEAN_TOSTRING_COMPARATOR;
        boolean categorised = false;
        boolean categorySelfstructuring = true;
        String categorySplitBy = " - ";

        if (options != null) {
            for (final Option option : options) {
                if (option != null) {
                    if (option.getClass().equals(MetaClassOption.class)) {
                        metaClass = ((MetaClassOption)option).getMetaClass();
                    } else if (option.getClass().equals(FakeModelOption.class)) {
                        fakeModel = true;
                    } else if (option.getClass().equals(AlwaysReloadOption.class)) {
                        alwaysReload = true;
                    } else if (option.getClass().equals(NullableOption.class)) {
                        nullable = true;
                        final String valueInEditor = ((NullableOption)option).getRepresentationinEditor();
                        if (valueInEditor != null) {
                            nullValueRepresentationInEditor = valueInEditor;
                        }
                        final String valueInRenderer = ((NullableOption)option).getRepresentationInRenderer();
                        if (valueInRenderer != null) {
                            nullValueRepresentationInRenderer = valueInRenderer;
                        }
                    } else if (option.getClass().equals(ManageableOption.class)) {
                        manageable = true;
                        final String property = ((ManageableOption)option).getProperty();
                        if (property != null) {
                            manageableProperty = property;
                        }
                        final String representation = ((ManageableOption)option).getRepresentation();
                        if (representation != null) {
                            manageableItemRepresentation = representation;
                        }
                    } else if (option.getClass().equals(WhereOption.class)) {
                        where = ((WhereOption)option).getWhere();
                    } else if (option.getClass().equals(ComparatorOption.class)) {
                        comparator = ((ComparatorOption)option).getComparator();
                    } else if (option.getClass().equals(SortingColumnOption.class)) {
                        sortingColumn = ((SortingColumnOption)option).getColumn();
                    } else if (option.getClass().equals(CategorisedOption.class)) {
                        categorised = true;
                        categorySelfstructuring = !Boolean.FALSE.equals(((CategorisedOption)option)
                                        .getSelfStructuring());
                        categorySplitBy = ((CategorisedOption)option).getSplitBy();
                    }
                }
            }
        }

        setModel(new DefaultComboBoxModel(new Object[] { new LoadingItem(MESSAGE_LOADING_ITEM) }));
        setRenderer(new DefaultBindableReferenceComboRenderer());

        final MetaObjectChangeSupport mocSupport = MetaObjectChangeSupport.getDefault();
        final MetaObjectChangeListener moChangeListener = new MetaObjectChangeListenerImpl();
        mocSupport.addMetaObjectChangeListener(WeakListeners.create(
                MetaObjectChangeListener.class,
                moChangeListener,
                mocSupport));

        setFakeModel(fakeModel);
        setNullable(nullable);
        setNullValueRepresentation(nullValueRepresentationInEditor);
        setNullValueRepresentationInRenderer(nullValueRepresentationInRenderer);
        setManageable(manageable);
        setManageableItemRepresentation(manageableItemRepresentation);
        setManageableProperty(manageableProperty);
        setWhere(where);
        setAlwaysReload(alwaysReload);
        setSortingColumn(sortingColumn);
        setComparator(comparator);
        setCategorised(categorised);
        setCategorySelfStructuring(categorySelfstructuring);
        setCategorySplitBy(categorySplitBy);

        // refresh is also happening here !
        setMetaClass(metaClass);
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  metaClass      mc DOCUMENT ME!
     * @param  sortingcolumn  DOCUMENT ME!
     */
    public DefaultBindableReferenceCombo(final MetaClass metaClass, final String sortingcolumn) {
        this(
            new MetaClassOption(metaClass),
            new SortingColumnOption(sortingcolumn),
            null);
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  metaClass  mc DOCUMENT ME!
     * @param  nullable   DOCUMENT ME!
     * @param  onlyUsed   DOCUMENT ME!
     */
    @Deprecated
    public DefaultBindableReferenceCombo(final MetaClass metaClass, final boolean nullable, final boolean onlyUsed) {
        this(
            new MetaClassOption(metaClass),
            nullable ? new NullableOption() : (Option)null,
            onlyUsed ? new WhereOption("used IS TRUE") : (Option)null,
            null);
    }

    /**
     * Creates a new DefaultBindableReferenceCombo object.
     *
     * @param  metaClass   mc DOCUMENT ME!
     * @param  nullable    DOCUMENT ME!
     * @param  onlyUsed    DOCUMENT ME!
     * @param  comparator  DOCUMENT ME!
     */
    @Deprecated
    public DefaultBindableReferenceCombo(final MetaClass metaClass,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator) {
        this(
            new MetaClassOption(metaClass),
            nullable ? new NullableOption() : (Option)null,
            onlyUsed ? new WhereOption("used IS TRUE") : (Option)null,
            (comparator != null) ? new ComparatorOption(comparator) : (Option)null,
            null);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isCategory(final Object value) {
        return value instanceof String;
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
            if ((o instanceof CidsBean) && (((DefaultComboBoxModel)m).getIndexOf(o) == -1)) {
                return ((DefaultComboBoxModel)m).getIndexOf(((CidsBean)o).getMetaObject());
            } else {
                return ((DefaultComboBoxModel)m).getIndexOf(o);
            }
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

    /**
     * DOCUMENT ME!
     *
     * @param  data  DOCUMENT ME!
     */
    private void initCategorisedRenderer(final List<List> data) {
        // show the categories
        setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    boolean isSel = isCategory(value) ? false : isSelected;

                    if ((value instanceof MetaObject) && (cidsBean != null)) {
                        isSel = (((MetaObject)value).getBean().equals(cidsBean) ? true : isSel);
                    }
                    final Component ret = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSel,
                            cellHasFocus);
                    final String text;
                    if (value == null) {
                        text = getNullValueRepresentation();
                    } else {
                        final int lastIndex = (value.toString() != null)
                            ? value.toString().lastIndexOf(getCategorySplitBy()) : -1;
                        final String name = (lastIndex < 0)
                            ? value.toString() : value.toString().substring(lastIndex + getCategorySplitBy().length());
                        text = spaces(getCategoryLevel(value)) + name;
                    }
                    ((JLabel)ret).setText(((text != null) && !text.trim().isEmpty()) ? text : " ");
                    if (isCategory(value)) {
                        final JLabel label = (JLabel)ret;
                        final Font boldLabelFont = new Font(
                                label.getFont().getName(),
                                Font.BOLD,
                                label.getFont().getSize());

                        label.setFont(boldLabelFont);
                    }

                    return ret;
                }

                private String spaces(final int i) {
                    if (i == 0) {
                        return "";
                    }
                    final StringBuffer spaces = new StringBuffer(i * 2);

                    for (int n = 0; n < i; ++n) {
                        spaces.append("  ");
                    }

                    return spaces.toString();
                }

                private int getCategoryLevel(final Object value) {
                    if (isCategory(value)) {
                        for (final List tmp : data) {
                            for (int i = 0; i < (tmp.size() - 1); ++i) {
                                final Object o = tmp.get(i);
                                if ((o != null) && o.equals(value)) {
                                    return i;
                                }
                            }
                        }
                    } else {
                        for (final List tmp : data) {
                            if (tmp.get(tmp.size() - 1).equals(value)) {
                                for (int i = 0; i < tmp.size(); ++i) {
                                    if (tmp.get(i) == null) {
                                        return i;
                                    }
                                }
                                return tmp.size();
                            }
                        }
                    }

                    return 0;
                }
            });

        // prevent the selection of a category
        addItemListener(new ItemListener() {

                private int lastIndex = -1;

                @Override
                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        lastIndex = getIndexOf(e.getItem());
                    } else if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (isCategory(e.getItem())) {
                            final int index = DefaultBindableReferenceCombo.this.getIndexOf(e.getItem());
                            final Object nextSelection = getNextItem(
                                    e.getItem(),
                                    ((lastIndex != -1) && (lastIndex > index)));

                            if (nextSelection != null) {
                                EventQueue.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            setSelectedItem(nextSelection);
                                        }
                                    });
                            }
                        }
                    }
                }

                private Object getNextItem(final Object value, final boolean rev) {
                    Object lastObject = null;

                    if (isCategory(value)) {
                        for (final List tmp : data) {
                            for (int i = 0; i < (tmp.size() - 1); ++i) {
                                if ((tmp.get(i) != null) && tmp.get(i).equals(value)) {
                                    if (rev && (lastObject != null)) {
                                        return lastObject;
                                    } else {
                                        return tmp.get(tmp.size() - 1);
                                    }
                                }
                            }
                            lastObject = tmp.get(tmp.size() - 1);
                        }
                    }

                    return null;
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isExplicitlyEnabledOrDisabled() {
        return explicitlyEnabledOrDisabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  explicitlyEnabledOrDisabled  DOCUMENT ME!
     */
    private void setExplicitlyEnabledOrDisabled(final boolean explicitlyEnabledOrDisabled) {
        this.explicitlyEnabledOrDisabled = explicitlyEnabledOrDisabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        setExplicitlyEnabledOrDisabled(true);
        super.setEnabled(enabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   data  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Object[] createCategorisedModelData(final List<List> data) {
        final List<String> categories = new ArrayList<>();
        final List<Object> dataArray = new ArrayList<>();

        if (isNullable()) {
            dataArray.add(null);
        }

        for (int i = 0; i < data.size(); ++i) {
            for (int n = 0; n < (data.get(i).size() - 1); ++n) {
                final String category = (String)data.get(i).get(n);

                if ((category != null) && !category.isEmpty() && !categories.contains(category)) {
                    dataArray.add(category);
                    categories.add(category);
                }
            }
            dataArray.add(data.get(i).get(data.get(i).size() - 1));
        }

        initCategorisedRenderer(data);
        return dataArray.toArray(new Object[dataArray.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  forceReload  DOCUMENT ME!
     */
    public void reload(final boolean forceReload) {
        final MetaClass metaClass = getMetaClass();
        if (!isFakeModel() && (metaClass != null)) {
            CismetThreadPool.execute(new SwingWorker<DefaultComboBoxModel, Void>() {

                    @Override
                    protected DefaultComboBoxModel doInBackground() throws Exception {
                        Thread.currentThread().setName("DefaultBindableReferenceCombo init()");
                        final DefaultComboBoxModel model = getModelByMetaClass(
                                metaClass,
                                isNullable() ? new NullableItem(getNullValueRepresentation()) : null,
                                isManageable() ? new ManageableItem(getManageableItemRepresentation()) : null,
                                getWhere(),
                                getSortingColumn(),
                                getComparator(),
                                forceReload,
                                getConnectionContext());

                        if (isCategorised()) {
                            if (model == null) {
                                return null;
                            }

                            final List<List> data = new ArrayList<>();

                            int maxElements = 0;

                            // determine the max number of sub categories
                            for (int moIndex = 0; moIndex <= model.getSize(); moIndex++) {
                                final Object element = model.getElementAt(moIndex);
                                if (element instanceof CidsBean) {
                                    final CidsBean bean = (CidsBean)element;
                                    if (bean.toString().split(getCategorySplitBy()).length > maxElements) {
                                        maxElements = bean.toString().split(getCategorySplitBy()).length - 1;
                                    }
                                }
                            }

                            for (int moIndex = 0; moIndex <= model.getSize(); moIndex++) {
                                final Object element = model.getElementAt(moIndex);
                                if (element instanceof CidsBean) {
                                    final CidsBean bean = (CidsBean)element;
                                    final String[] splitted = bean.toString().split(getCategorySplitBy());
                                    final List sub = new ArrayList(maxElements);
                                    for (int splitIndex = 0; splitIndex < maxElements; splitIndex++) {
                                        sub.add((splitIndex < (splitted.length - 1)) ? splitted[splitIndex] : null);
                                    }

                                    sub.add(bean);
                                    data.add(sub);
                                }
                            }
                            return new DefaultComboBoxModel(createCategorisedModelData(data));
                        } else {
                            return model;
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            final DefaultComboBoxModel tmp = get();
                            tmp.setSelectedItem(getCidsBean());
                            setModel(tmp);

                            if (!isExplicitlyEnabledOrDisabled()) {
                                final boolean actingAsRenderer = isActingAsRenderer();
                                setEditable(actingAsRenderer);

                                synchronized (DefaultBindableReferenceCombo.this) {
                                    setUI(actingAsRenderer ? createRendererUI() : createEditorUI());
                                }
                                DefaultBindableReferenceCombo.super.setEnabled(!actingAsRenderer);
                                setOpaque(!actingAsRenderer);

                                final Border editorBorder;
                                final Color editorDisabledTextColor;
                                if (actingAsRenderer) {
                                    editorBorder = null;
                                    editorDisabledTextColor = Color.BLACK;
                                } else {
                                    final JComboBox dummyCombobox = new DefaultBindableReferenceCombo();
                                    editorBorder = ((JTextComponent)dummyCombobox.getEditor().getEditorComponent())
                                                .getBorder();
                                    editorDisabledTextColor =
                                        ((JTextComponent)dummyCombobox.getEditor().getEditorComponent())
                                                .getDisabledTextColor();
                                }

                                ((JTextComponent)getEditor().getEditorComponent()).setOpaque(!actingAsRenderer);
                                ((JTextComponent)getEditor().getEditorComponent()).setBorder(editorBorder);
                                ((JTextComponent)getEditor().getEditorComponent()).setDisabledTextColor(
                                    editorDisabledTextColor);

                                if (actingAsRenderer && (getSelectedItem() == null)) {
                                    ((JTextComponent)getEditor().getEditorComponent()).setText(
                                        getNullValueRepresentationInRenderer());
                                }
                            }
                        } catch (final InterruptedException interruptedException) {
                        } catch (final ExecutionException executionException) {
                            LOG.error("Error while initializing the model of a referenceCombo", executionException); // NOI18N
                        }
                    }
                });
        }
    }

    @Override
    public Object getSelectedItem() {
        final Object object = super.getSelectedItem();
        if (object instanceof Item) {
            return null;
        } else {
            return object;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public void reload() {
        if (getMetaClass() == null) {
            throw new IllegalStateException("the metaclass has not been set yet"); // NOI18N
        }

        reload(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isActingAsRenderer() {
        return actingAsRenderer;
    }

    @Override
    public void setActingAsRenderer(final boolean actingAsRenderer) {
        this.actingAsRenderer = actingAsRenderer;
        reload(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ComboBoxUI createRendererUI() {
        return new PlasticComboBoxUI() {

                @Override
                protected JButton createArrowButton() {
                    return null;
                }
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ComboBoxUI createEditorUI() {
        return new JComboBox().getUI();
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
    public void setSelectedItem(final Object item) {
        synchronized (this) {
            CidsBean selectedBean = null;
            if (isFakeModel()) {
                setModel(new DefaultComboBoxModel(new Object[] { item }));
            } else {
                if (item instanceof CidsBean) {
                    selectedBean = (CidsBean)item;
                } else if (item instanceof ManageableItem) {
                    try {
                        final CidsBean newBean = createNewInstance();
                        if (newBean != null) {
                            selectedBean = newBean.persist(getConnectionContext());
                            reload();
                        }
                    } catch (final Exception ex) {
                        LOG.error(ex, ex);
                    }
                }
            }
            super.setSelectedItem(selectedBean);
            setCidsBean(selectedBean);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private CidsBean createNewInstance() throws Exception {
        final MetaClass metaClass = getMetaClass();
        if (metaClass != null) {
            final String manageableProperty = getManageableProperty();
            final String property = (manageableProperty != null) ? manageableProperty : "name";
            String name = null;
            for (final Object value : metaClass.getMemberAttributeInfos().values()) {
                if (value instanceof MemberAttributeInfo) {
                    final MemberAttributeInfo mai = (MemberAttributeInfo)value;
                    if (property.equals(mai.getFieldName())) {
                        name = mai.getName();
                        break;
                    }
                }
            }
            final String input = JOptionPane.showInputDialog(
                    this,
                    String.format("%s:", name),
                    String.format(MESSAGE_CREATEITEMDIALOG_TITLE, metaClass.getName()),
                    JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                final MetaObject metaObject = metaClass.getEmptyInstance(getConnectionContext());
                final CidsBean cidsBean = metaObject.getBean();
                cidsBean.setProperty(property, input);
                return cidsBean;
            }
        }
        return null;
    }

    @Override
    public final void setMetaClass(final MetaClass metaClass) {
        this.metaClass = metaClass;
        reload(isAlwaysReload());
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
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     * @param   orderBy    DOCUMENT ME!
     * @param   where      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String createQuery(final MetaClass metaClass, final String orderBy, final String where) {
        final String template = "SELECT %d, %s FROM %s WHERE %s ORDER BY %s";
        final String query = String.format(
                template,
                metaClass.getID(),
                metaClass.getPrimaryKey(),
                metaClass.getTableName(),
                (where != null) ? where : "TRUE",
                (orderBy != null) ? orderBy : metaClass.getPrimaryKey());
        return query;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass          DOCUMENT ME!
     * @param   nullable           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass metaClass,
            final boolean nullable,
            final ConnectionContext connectionContext) throws Exception {
        return getModelByMetaClass(
                metaClass,
                nullable ? new NullableItem(MESSAGE_NULLEABLE_ITEM_EDITOR) : null,
                null,
                null,
                null,
                BEAN_TOSTRING_COMPARATOR,
                false,
                connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass          DOCUMENT ME!
     * @param   nullableIteam      DOCUMENT ME!
     * @param   manageableItem     DOCUMENT ME!
     * @param   where              onlyUsed DOCUMENT ME!
     * @param   sortingColumn      DOCUMENT ME!
     * @param   comparator         DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static DefaultComboBoxModel getModelByMetaClass(final MetaClass metaClass,
            final NullableItem nullableIteam,
            final ManageableItem manageableItem,
            final String where,
            final String sortingColumn,
            final Comparator<CidsBean> comparator,
            final boolean forceReload,
            final ConnectionContext connectionContext) {
        if (metaClass != null) {
            final ClassAttribute ca = metaClass.getClassAttribute("sortingColumn");
            final String orderBy = (sortingColumn != null) ? sortingColumn
                                                           : ((ca != null) ? ca.getValue().toString() : null);
            final String query = createQuery(metaClass, orderBy, where);
            return getModelByMetaClass(
                    query,
                    metaClass,
                    (orderBy == null) ? comparator : null,
                    nullableIteam,
                    metaClass.getPermissions().hasWritePermission(SessionManager.getSession().getUser())
                        ? manageableItem : null,
                    forceReload,
                    connectionContext);
        } else {
            return new DefaultComboBoxModel();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   query              DOCUMENT ME!
     * @param   metaClass          domain DOCUMENT ME!
     * @param   comparator         DOCUMENT ME!
     * @param   nullableIteam      DOCUMENT ME!
     * @param   manageableItem     DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static DefaultComboBoxModel getModelByMetaClass(
            final String query,
            final MetaClass metaClass,
            final Comparator<CidsBean> comparator,
            final NullableItem nullableIteam,
            final ManageableItem manageableItem,
            final boolean forceReload,
            final ConnectionContext connectionContext) {
        if ((query != null) && (metaClass != null)) {
            MetaObject[] metaObjects;
            try {
                metaObjects = MetaObjectCache.getInstance()
                            .getMetaObjectsByQuery(query, metaClass, forceReload, connectionContext);
            } catch (final CacheException ex) {
                LOG.warn("cache could not come up with appropriate objects", ex); // NOI18N
                metaObjects = new MetaObject[0];
            }

            final List<CidsBean> beans = new ArrayList(metaObjects.length);
            for (final MetaObject mo : metaObjects) {
                beans.add(mo.getBean());
            }
            if (comparator != null) {
                Collections.sort(beans, comparator);
            }

            int size = beans.size();
            if (nullableIteam != null) {
                size++;
            }
            if (manageableItem != null) {
                size++;
            }
            final List items = new ArrayList(size);
            if (nullableIteam != null) {
                items.add(nullableIteam);
            }
            items.addAll(beans);
            if (manageableItem != null) {
                items.add(manageableItem);
            }
            return new DefaultComboBoxModel(items.toArray());
        } else {
            return new DefaultComboBoxModel();
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return ConnectionContextUtils.getFirstParentClientConnectionContext(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  onlyUsed  DOCUMENT ME!
     */
    @Deprecated
    public void setOnlyUsed(final boolean onlyUsed) {
        setWhere((onlyUsed) ? "used IS TRUE" : null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public boolean isOnlyUsed() {
        return "used IS TRUE".equals(getWhere());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc                 DOCUMENT ME!
     * @param   nullable           DOCUMENT ME!
     * @param   onlyUsed           DOCUMENT ME!
     * @param   comparator         DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator,
            final ConnectionContext connectionContext) throws Exception {
        return getModelByMetaClass(mc, nullable, onlyUsed, comparator, false, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc           DOCUMENT ME!
     * @param   nullable     DOCUMENT ME!
     * @param   onlyUsed     DOCUMENT ME!
     * @param   comparator   DOCUMENT ME!
     * @param   forceReload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator,
            final boolean forceReload) {
        return getModelByMetaClass(
                mc,
                nullable,
                onlyUsed,
                comparator,
                forceReload,
                ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc                 DOCUMENT ME!
     * @param   nullable           DOCUMENT ME!
     * @param   onlyUsed           DOCUMENT ME!
     * @param   comparator         DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator,
            final boolean forceReload,
            final ConnectionContext connectionContext) {
        return getModelByMetaClass(
                mc,
                nullable ? new NullableItem() : null,
                null,
                onlyUsed ? "used IS TRUE" : null,
                null,
                comparator,
                forceReload,
                connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc                 DOCUMENT ME!
     * @param   nullableItem       DOCUMENT ME!
     * @param   onlyUsed           DOCUMENT ME!
     * @param   comparator         DOCUMENT ME!
     * @param   forceReload        DOCUMENT ME!
     * @param   sortingColumn      DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final NullableItem nullableItem,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator,
            final boolean forceReload,
            final String sortingColumn,
            final ConnectionContext connectionContext) {
        return getModelByMetaClass(
                mc,
                nullableItem,
                null,
                onlyUsed ? "used IS TRUE" : null,
                sortingColumn,
                comparator,
                forceReload,
                connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc          DOCUMENT ME!
     * @param   nullable    DOCUMENT ME!
     * @param   onlyUsed    DOCUMENT ME!
     * @param   comparator  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator) throws Exception {
        return getModelByMetaClass(mc, nullable, onlyUsed, comparator, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc        DOCUMENT ME!
     * @param   nullable  DOCUMENT ME!
     * @param   onlyUsed  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed) throws Exception {
        return getModelByMetaClass(mc, nullable, onlyUsed, ConnectionContext.createDeprecated());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc                 DOCUMENT ME!
     * @param   nullable           DOCUMENT ME!
     * @param   onlyUsed           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final ConnectionContext connectionContext) throws Exception {
        return getModelByMetaClass(mc, nullable, onlyUsed, BEAN_TOSTRING_COMPARATOR, false, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc        DOCUMENT ME!
     * @param   nullable  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static DefaultComboBoxModel getModelByMetaClass(final MetaClass mc, final boolean nullable)
            throws Exception {
        return getModelByMetaClass(mc, nullable, ConnectionContext.createDeprecated());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public abstract static class Option {
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class MetaClassOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final MetaClass metaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class NullableOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String representationinEditor;
        private final String representationInRenderer;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NullableOption object.
         */
        public NullableOption() {
            this(null, null);
        }

        /**
         * Creates a new NullableOption object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public NullableOption(final String representation) {
            this(representation, representation);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class ManageableOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String property;
        private final String representation;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ManageableOption object.
         */
        public ManageableOption() {
            this(null, null);
        }

        /**
         * Creates a new ManageableOption object.
         *
         * @param  property  DOCUMENT ME!
         */
        public ManageableOption(final String property) {
            this(property, null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class AlwaysReloadOption extends Option {
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class FakeModelOption extends Option {
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class WhereOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String where;
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class ComparatorOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final Comparator<CidsBean> comparator;
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class SortingColumnOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String column;
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @AllArgsConstructor
    public static class CategorisedOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String splitBy;
        private final Boolean selfStructuring;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CategorisedOption object.
         *
         * @param  splitBy  DOCUMENT ME!
         */
        public CategorisedOption(final String splitBy) {
            this(splitBy, null);
        }

        /**
         * Creates a new CategorisedOption object.
         *
         * @param  selfStructuring  DOCUMENT ME!
         */
        public CategorisedOption(final Boolean selfStructuring) {
            this(null, selfStructuring);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private abstract static class Item {

        //~ Instance fields ----------------------------------------------------

        @Getter private final String representation;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Item object.
         */
        public Item() {
            this(null);
        }

        /**
         * Creates a new Item object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public Item(final String representation) {
            this.representation = representation;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class LoadingItem extends Item {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadingItem object.
         */
        public LoadingItem() {
        }

        /**
         * Creates a new LoadingItem object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public LoadingItem(final String representation) {
            super(representation);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class ManageableItem extends Item {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ManageableItem object.
         */
        public ManageableItem() {
        }

        /**
         * Creates a new ManageableItem object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public ManageableItem(final String representation) {
            super(representation);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class NullableItem extends Item {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NullableItem object.
         */
        public NullableItem() {
        }

        /**
         * Creates a new NullableItem object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public NullableItem(final String representation) {
            super(representation);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class BeanToStringComparator implements Comparator<CidsBean> {

        //~ Methods ------------------------------------------------------------

        @Override
        public int compare(final CidsBean o1, final CidsBean o2) {
            final String s1 = (o1 == null) ? "" : o1.toString(); // NOI18N
            final String s2 = (o2 == null) ? "" : o2.toString(); // NOI18N

            return (s1).compareToIgnoreCase(s2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class MetaObjectChangeListenerImpl implements MetaObjectChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void metaObjectAdded(final MetaObjectChangeEvent moce) {
            // we're only registered to the DefaultMetaObjectChangeSupport that asserts of proper initialisation of
            // events
            final MetaClass metaClass = getMetaClass();
            if ((metaClass != null) && metaClass.equals(moce.getNewMetaObject().getMetaClass())) {
                reload(true);
            }
        }

        @Override
        public void metaObjectChanged(final MetaObjectChangeEvent moce) {
            // we're only registered to the DefaultMetaObjectChangeSupport that asserts of proper initialisation of
            // events
            final MetaClass metaClass = getMetaClass();
            if ((metaClass != null) && metaClass.equals(moce.getNewMetaObject().getMetaClass())) {
                reload(true);
            }
        }

        @Override
        public void metaObjectRemoved(final MetaObjectChangeEvent moce) {
            // we're only registered to the DefaultMetaObjectChangeSupport that asserts of proper initialisation of
            // events
            final MetaClass metaClass = getMetaClass();
            if ((metaClass != null) && metaClass.equals(moce.getOldMetaObject().getMetaClass())) {
                reload(true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class DefaultBindableReferenceComboRenderer extends DefaultListCellRenderer {

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            final Component component = super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus);
            if (component instanceof JLabel) {
                final JLabel label = (JLabel)component;
                if (value instanceof Item) {
                    final Item item = (Item)value;
                    final String representation = item.getRepresentation();
                    label.setText(((representation != null) && !representation.trim().isEmpty()) ? representation
                                                                                                 : " ");
                }
            }

            return component;
        }
    }
}
