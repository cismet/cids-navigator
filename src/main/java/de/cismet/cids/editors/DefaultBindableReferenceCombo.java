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
import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;

import java.awt.Color;
import java.awt.Component;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        String nullValueRepresentationInEditor = null;
        String nullValueRepresentationInRenderer = null;
        boolean manageable = false;
        String manageableItemRepresentation = null;
        String manageableProperty = null;
        String where = null;
        boolean alwaysReload = false;
        String sortingColumn = null;
        Comparator<CidsBean> comparator = BEAN_TOSTRING_COMPARATOR;

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
                        nullValueRepresentationInEditor = (valueInEditor != null) ? String.valueOf(valueInEditor)
                                                                                  : MESSAGE_NULLEABLE_ITEM_EDITOR;
                        final String valueInRenderer = ((NullableOption)option).getRepresentationInRenderer();
                        nullValueRepresentationInRenderer = (valueInRenderer != null) ? String.valueOf(valueInRenderer)
                                                                                      : MESSAGE_NULLEABLE_ITEM_RENDERER;
                    } else if (option.getClass().equals(ManageableOption.class)) {
                        manageable = true;
                        final String property = ((ManageableOption)option).getProperty();
                        final String representation = ((ManageableOption)option).getRepresentation();
                        manageableProperty = (property != null) ? property : "name";
                        manageableItemRepresentation = (representation != null) ? representation
                                                                                : MESSAGE_MANAGEABLE_ITEM;
                    } else if (option.getClass().equals(WhereOption.class)) {
                        where = ((WhereOption)option).getWhere();
                    } else if (option.getClass().equals(ComparatorOption.class)) {
                        comparator = ((ComparatorOption)option).getComparator();
                    } else if (option.getClass().equals(SortingColumnOption.class)) {
                        sortingColumn = ((SortingColumnOption)option).getColumn();
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
     * @param  forceReload  DOCUMENT ME!
     */
    public void reload(final boolean forceReload) {
        final MetaClass metaClass = getMetaClass();
        if (!isFakeModel() && (metaClass != null)) {
            CismetThreadPool.execute(new SwingWorker<DefaultComboBoxModel, Void>() {

                    @Override
                    protected DefaultComboBoxModel doInBackground() throws Exception {
                        Thread.currentThread().setName("DefaultBindableReferenceCombo init()");
                        return getModelByMetaClass(
                                metaClass,
                                isNullable() ? new NullableItem(getNullValueRepresentation()) : null,
                                isManageable() ? new ManageableItem(getManageableItemRepresentation()) : null,
                                getWhere(),
                                getSortingColumn(),
                                getComparator(),
                                forceReload,
                                getConnectionContext());
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

                                setUI(actingAsRenderer ? createRendererUI() : createEditorUI());

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
     * @return  DOCUMENT ME!
     */
    private String createQuery() {
        return createQuery(getMetaClass(), getSortingColumn(), getWhere());
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
    public static class MetaClassOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final MetaClass metaClass;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MetaClassOption object.
         *
         * @param  metaClass  DOCUMENT ME!
         */
        public MetaClassOption(final MetaClass metaClass) {
            this.metaClass = metaClass;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public MetaClass getMetaClass() {
            return metaClass;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    public static class NullableOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String representationinEditor;
        private final String representationInRenderer;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NullableOption object.
         */
        public NullableOption() {
            this(null);
        }

        /**
         * Creates a new NullableOption object.
         *
         * @param  representation  DOCUMENT ME!
         */
        public NullableOption(final String representation) {
            this(representation, representation);
        }

        /**
         * Creates a new NullableOption object.
         *
         * @param  representationinEditor    DOCUMENT ME!
         * @param  representationInRenderer  DOCUMENT ME!
         */
        public NullableOption(final String representationinEditor, final String representationInRenderer) {
            this.representationinEditor = representationinEditor;
            this.representationInRenderer = representationInRenderer;
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
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

        /**
         * Creates a new ManageableOption object.
         *
         * @param  property        DOCUMENT ME!
         * @param  representation  DOCUMENT ME!
         */
        public ManageableOption(final String property, final String representation) {
            this.property = property;
            this.representation = representation;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getProperty() {
            return property;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getRepresentation() {
            return representation;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class AlwaysReloadOption extends Option {
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class FakeModelOption extends Option {
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class WhereOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String where;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new WhereOption object.
         *
         * @param  where  DOCUMENT ME!
         */
        public WhereOption(final String where) {
            this.where = where;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getWhere() {
            return where;
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class ComparatorOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final Comparator<CidsBean> comparator;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ComparatorOption object.
         *
         * @param  comparator  DOCUMENT ME!
         */
        public ComparatorOption(final Comparator<CidsBean> comparator) {
            this.comparator = comparator;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Comparator<CidsBean> getComparator() {
            return comparator;
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class SortingColumnOption extends Option {

        //~ Instance fields ----------------------------------------------------

        private final String column;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SortingColumnOption object.
         *
         * @param  column  DOCUMENT ME!
         */
        public SortingColumnOption(final String column) {
            this.column = column;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getColumn() {
            return column;
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
