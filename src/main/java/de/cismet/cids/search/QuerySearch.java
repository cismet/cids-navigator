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
package de.cismet.cids.search;

import Sirius.navigator.actiontag.ActionTagProtected;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.search.dynamic.SearchControlPanel;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.search.CidsServerSearch;
import de.cismet.cids.server.search.MetaObjectNodeServerSearch;
import de.cismet.cids.server.search.builtin.DistinctValuesSearch;
import de.cismet.cids.server.search.builtin.QueryEditorSearch;

import de.cismet.cids.tools.search.clientstuff.CidsWindowSearch;
import de.cismet.cids.tools.search.clientstuff.CidsWindowSearchWithMenuEntry;

import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.features.FeatureServiceFeature;
import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.featureservice.FeatureServiceAttribute;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.layerwidget.ZoomToLayerWorker;
import de.cismet.cismap.commons.interaction.CismapBroker;
import de.cismet.cismap.commons.interaction.DefaultQueryButtonAction;
import de.cismet.cismap.commons.rasterservice.MapService;

import de.cismet.tools.gui.PaginationPanel;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsWindowSearch.class)
public class QuerySearch extends javax.swing.JPanel implements CidsWindowSearchWithMenuEntry,
    ActionTagProtected,
    ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static transient Logger LOG = Logger.getLogger(QuerySearch.class);
    public static final String PROP_ATTRIBUTES = "attributes"; // NOI18N
    public static final String PROP_METACLASS = "metaclass";
    public static final String PROP_VALUES = "values";         // NOI18N
    public static final String PROP_SELECT_COMMAND = "selectCommand";
    public static final String PROP_COUNT = "count";
    public static final String ACTION_TAG = "navigator.querybuilder.searchdialogue@";

    //~ Instance fields --------------------------------------------------------

    ExecutorService threadPool = Executors.newCachedThreadPool();
    private List<? extends Object> attributes;
    private List<Object> values;
    private String selectCommand;
    private List<MetaClass> classes;
    private List<AbstractFeatureService> services;
    private List<Object> layers;
    private Set<String> queryableValues;
    private int count = 0;
    private ActiveLayerModel model;
    private ImageIcon iconSearch;
    private ImageIcon iconCancel;
    private String[] methodList;
    private QuerySearchMethod[] additionalMethods;
    private String currentlyExpandedAttribute;
    private String searchButtonName = org.openide.util.NbBundle.getMessage(
            QuerySearch.class,
            "SearchControlPanel.btnSearchCancel.text");
    private MetaClass metaClass;

    private final boolean paginationEnabled;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearchCancel;
    private javax.swing.JList jAttributesLi;
    private javax.swing.JLabel jCommandLb;
    private javax.swing.JLabel jGeheZuLb;
    private javax.swing.JButton jGetValuesBn;
    private javax.swing.JComboBox jLayerCB;
    private javax.swing.JLabel jLayerLb;
    private javax.swing.JComboBox jMethodCB;
    private javax.swing.JLabel jMethodLb;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelTasten;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList jValuesLi;
    private javax.swing.JLabel jlEinzelwerteAnzeigen;
    private org.jdesktop.swingx.JXBusyLabel lblBusyIcon;
    private org.jdesktop.swingx.JXBusyLabel lblBusyValueIcon;
    private javax.swing.JPanel panCommand;
    private de.cismet.tools.gui.PaginationPanel panPagination;
    private javax.swing.Box.Filler strGap;
    private javax.swing.JTextArea taQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearch object.
     */
    public QuerySearch() {
        this(false);
    }

    /**
     * Creates new form StandaloneStart.
     *
     * @param  paginationEnabled  DOCUMENT ME!
     */
    public QuerySearch(final boolean paginationEnabled) {
        this(null, new String[] { "de.cismet.cids.search.SearchQuerySearchMethod" }, paginationEnabled);
    }

    /**
     * Creates a new QuerySearch object.
     *
     * @param  model       DOCUMENT ME!
     * @param  methodList  DOCUMENT ME!
     */
    public QuerySearch(final ActiveLayerModel model, final String[] methodList) {
        this(model, methodList, false);
    }

    /**
     * Creates a new QuerySearch object.
     *
     * @param  model          DOCUMENT ME!
     * @param  methodList     DOCUMENT ME!
     * @param  choosenLayers  DOCUMENT ME!
     */
    public QuerySearch(final ActiveLayerModel model,
            final String[] methodList,
            final AbstractFeatureService[] choosenLayers) {
        this(model, methodList, choosenLayers, null, false);
    }

    /**
     * Creates new form StandaloneStart.
     *
     * @param  model              the layer model to be use
     * @param  methodList         only the method of this list can be used, if they can be found by the lookup. If the
     *                            methodList == null, all methods can be used
     * @param  paginationEnabled  DOCUMENT ME!
     */
    public QuerySearch(final ActiveLayerModel model, final String[] methodList, final boolean paginationEnabled) {
        this(model, methodList, null, paginationEnabled);
    }

    /**
     * Creates a new QuerySearch object.
     *
     * @param  model              the layer model to be use
     * @param  methodList         only the method of this list can be used, if they can be found by the lookup. If the
     *                            methodList == null, all methods can be used
     * @param  choosenLayers      if choosenLayers is not null, only the layers from this list will be used
     * @param  additionalMethods  additional methods, which can also be used
     */
    public QuerySearch(final ActiveLayerModel model,
            final String[] methodList,
            final AbstractFeatureService[] choosenLayers,
            final QuerySearchMethod[] additionalMethods) {
        this(model, methodList, choosenLayers, additionalMethods, false);
    }

    /**
     * Creates new form StandaloneStart.
     *
     * @param  model              the layer model to be use
     * @param  methodList         only the method of this list can be used, if they can be found by the lookup. If the
     *                            methodList == null, all methods can be used
     * @param  choosenLayers      the available layers. If null, all layers from the model and all configured cids layer
     * @param  paginationEnabled  will be available
     */
    public QuerySearch(final ActiveLayerModel model,
            final String[] methodList,
            final AbstractFeatureService[] choosenLayers,
            final boolean paginationEnabled) {
        this(model, methodList, choosenLayers, null, paginationEnabled);
    }

    /**
     * Creates new form StandaloneStart.
     *
     * @param  model              the layer model to be use
     * @param  methodList         only the method of this list can be used, if they can be found by the lookup. If the
     *                            methodList == null, all methods can be used
     * @param  choosenLayers      the available layers. If null, all layers from the model and all configured cids layer
     *                            will be available
     * @param  additionalMethods  additional methods that should be used
     * @param  paginationEnabled  DOCUMENT ME!
     */
    public QuerySearch(final ActiveLayerModel model,
            final String[] methodList,
            final AbstractFeatureService[] choosenLayers,
            final QuerySearchMethod[] additionalMethods,
            final boolean paginationEnabled) {
        this.model = model;
        this.methodList = methodList;
        this.additionalMethods = additionalMethods;
        this.paginationEnabled = paginationEnabled;

        if (choosenLayers == null) {
            services = getFeatureServices(model);
            classes = GetClasses();
            layers = new ArrayList<Object>(services);
            layers.addAll(classes);
        } else {
            layers = new ArrayList<Object>(Arrays.asList(choosenLayers));
            services = new ArrayList<AbstractFeatureService>();
            services.addAll(Arrays.asList(choosenLayers));
            classes = new ArrayList<MetaClass>();
        }
        initComponents();
        jMethodCB.setVisible((jMethodCB.getModel().getSize() > 1));
        jMethodLb.setVisible((jMethodCB.getModel().getSize() > 1));
        jGeheZuLb.setVisible(false);
        jTextField1.setVisible(false);
        jAttributesLi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jAttributesLi.addMouseListener(new MouseAdapterImpl());
        jValuesLi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jValuesLi.addMouseListener(new MouseAdapterImpl());

        jGetValuesBn.setEnabled(false);
        jAttributesLi.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    if (!((attributes == null) || (jAttributesLi.getSelectedIndex() == -1))) {
                        final Object attributeObject = attributes.get(jAttributesLi.getSelectedIndex());

                        if (attributeObject instanceof MemberAttributeInfo) {
                            final MemberAttributeInfo attributeInfo = (MemberAttributeInfo)attributeObject;
                            if (queryableValues.contains(attributeInfo.getFieldName())) {
                                jGetValuesBn.setEnabled(true);
                                return;
                            }
                        } else if (attributeObject instanceof FeatureServiceAttribute) {
                            jGetValuesBn.setEnabled(true);
                            return;
                        }
                    }
                    jGetValuesBn.setEnabled(false);
                }
            });

        jAttributesLi.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList<?> list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final Component component = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus); // To change body of generated methods, choose Tools | Templates.
                    final Object attributeObject = attributes.get(index);

                    if (attributeObject instanceof MemberAttributeInfo) {
                        final MemberAttributeInfo mai = (MemberAttributeInfo)attributeObject;
                        ((JLabel)component).setText(mai.getFieldName());
                        ((JLabel)component).setToolTipText(mai.getName());
                    }
                    return component;
                }
            });
        if (classes.size() > 0) {
            jLayerCB.setSelectedIndex(0);
        }
        if (getMethods().size() > 0) {
            jMethodCB.setSelectedIndex(0);
        }

        jValuesLi.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final Component c = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus);

                    if ((value instanceof MetaObject) && (c instanceof JLabel)) {
                        final MetaObject mo = (MetaObject)value;
                        ((JLabel)c).setText(mo.getID() + " - " + mo.toString());
                    }

                    return c;
                }
            });

        final URL iconSearchUrl = getClass().getResource(
                "/Sirius/navigator/search/dynamic/SearchControlPanel_btnSearchCancel.png");
        if (iconSearchUrl != null) {
            this.iconSearch = new ImageIcon(iconSearchUrl);
        } else {
            this.iconSearch = new ImageIcon();
        }

        final URL iconCancelUrl = getClass().getResource(
                "/Sirius/navigator/search/dynamic/SearchControlPanel_btnSearchCancel_cancel.png");
        if (iconCancelUrl != null) {
            this.iconCancel = new ImageIcon(iconCancelUrl);
        } else {
            this.iconCancel = new ImageIcon();
        }

        jLayerCBActionPerformed(null);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Enables line wrap in the query text area.
     *
     * @param  enabled  true or false, if the line wrap should be enabled or disabled
     */
    public void enableLineWrap(final boolean enabled) {
        taQuery.setLineWrap(enabled);
        taQuery.setWrapStyleWord(enabled);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(panPagination)) {
            performSearch();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PaginationPanel getPanginationPanel() {
        return panPagination;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * fills the buttons panel with the buttons from the seleced service.
     */
    private void fillButtonPanel() {
        final Object layer = jLayerCB.getSelectedItem();
        List<DefaultQueryButtonAction> queryButtons;
        int x = 0;
        int y = 0;
        jPanelTasten.removeAll();

        if (layer instanceof AbstractFeatureService) {
            queryButtons = ((AbstractFeatureService)layer).getQueryButtons();
        } else {
            queryButtons = AbstractFeatureService.SQL_QUERY_BUTTONS;
        }

        for (final DefaultQueryButtonAction buttonAction : queryButtons) {
            final JButton button = new JButton(buttonAction.getText());
            button.addActionListener(buttonAction);
            final GridBagConstraints constraint = new GridBagConstraints(
                    x,
                    y,
                    buttonAction.getWidth(),
                    1,
                    1,
                    0,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL,
                    new Insets(2, 2, 2, 2),
                    0,
                    0);
            jPanelTasten.add(button, constraint);
            buttonAction.setQueryTextArea(taQuery);
            x += buttonAction.getWidth();
            if (x > 5) {
                x = 0;
                ++y;
            }
        }

        jPanelTasten.invalidate();
        jPanelTasten.revalidate();
        jPanelTasten.repaint();
    }

    /**
     * Retrieves all configured cids layer classes.
     *
     * @return  DOCUMENT ME!
     */
    private static List<MetaClass> GetClasses() {
        final List<MetaClass> metaClassesWithAttribute = new LinkedList<MetaClass>();

        try {
            final MetaClass[] metaClass = SessionManager.getProxy()
                        .getClasses(SessionManager.getSession().getUser().getDomain());
            for (final MetaClass mClass : metaClass) {
                if (!mClass.getAttributeByName("Queryable").isEmpty()) {
                    metaClassesWithAttribute.add(mClass);
                }
            }
        } catch (Exception e) {
            LOG.error("Error while retrieving classes", e);
        }
        return metaClassesWithAttribute;
    }

    /**
     * Retrieves all feature services from the model.
     *
     * @param   model  DOCUMENT ME!
     *
     * @return  all feature services from the model
     */
    private List<AbstractFeatureService> getFeatureServices(final ActiveLayerModel model) {
        final List<AbstractFeatureService> list = new ArrayList<AbstractFeatureService>();

        if (model != null) {
            final TreeMap<Integer, MapService> map = model.getMapServices();

            for (final Integer key : map.keySet()) {
                final MapService service = map.get(key);

                if (service instanceof AbstractFeatureService) {
                    list.add((AbstractFeatureService)service);
                }
            }
        }

        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static List<MemberAttributeInfo> getAttributesFromClass(final MetaClass metaClass) {
        final HashMap map = metaClass.getMemberAttributeInfos();
        final List<MemberAttributeInfo> attributes = new ArrayList<MemberAttributeInfo>(map.size());
        for (final Object o : map.entrySet()) {
            final MemberAttributeInfo mai = (MemberAttributeInfo)((java.util.Map.Entry)o).getValue();
            if (!mai.isExtensionAttribute()) {
                attributes.add(mai);
            }
        }
        return attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     * @param   attribute  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static List<Object> GetValuesFromAttribute(final MetaClass metaClass, final MemberAttributeInfo attribute) {
        final List<Object> values = new ArrayList<Object>();

        try {
            if (attribute.isForeignKey() && !attribute.isArray()) {
                final MetaClass foreignClass = ClassCacheMultiple.getMetaClass(metaClass.getDomain(),
                        attribute.getForeignKeyClassId());
                final String query = "select " + foreignClass.getID() + ", " + foreignClass.getPrimaryKey()
                            + " from "
                            + foreignClass.getTableName();
                final MetaObject[] mos = SessionManager.getProxy()
                            .getMetaObjectByQuery(SessionManager.getSession().getUser(), query);

                if (mos != null) {
                    values.addAll(Arrays.asList(mos));
                }
            } else {
                final CidsServerSearch search = new DistinctValuesSearch(SessionManager.getSession().getUser()
                                .getDomain(),
                        metaClass.getTableName(),
                        attribute.getFieldName());
                final Collection resultCollection = SessionManager.getProxy()
                            .customServerSearch(SessionManager.getSession().getUser(), search);

                final ArrayList<ArrayList> resultArray = (ArrayList<ArrayList>)resultCollection;

                if ((resultArray == null) || resultArray.isEmpty() || (resultArray.get(0).size() != 1)) {
                    return null;
                }
                for (final ArrayList attributes : resultArray) {
                    values.add(String.valueOf(attributes.get(0)));
                }
            }
        } catch (ConnectionException e) {
            Exceptions.printStackTrace(e);
        }
        return values;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Vector<QuerySearchMethod> getMethods() {
        final Collection<? extends QuerySearchMethod> searchMethods = Lookup.getDefault()
                    .lookupAll(QuerySearchMethod.class);

        final Vector<QuerySearchMethod> methods = new Vector<QuerySearchMethod>();
        if (methodList != null) {
            Arrays.sort(methodList);
        }

        for (final QuerySearchMethod method : searchMethods) {
            if ((methodList == null) || (Arrays.binarySearch(methodList, method.getClass().getName()) >= 0)) {
                method.setQuerySearch(this);
                methods.add(method);
            }
        }

        if (additionalMethods != null) {
            for (final QuerySearchMethod method : additionalMethods) {
                method.setQuerySearch(this);
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass    DOCUMENT ME!
     * @param   whereClause  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int GetCount(final MetaClass metaClass, final String whereClause) {
        // int values = -1;
        try {
            final CidsServerSearch search = new QueryEditorSearch(SessionManager.getSession().getUser().getDomain(),
                    metaClass.getTableName(),
                    whereClause,
                    metaClass.getId());
            final Collection resultCollection = SessionManager.getProxy()
                        .customServerSearch(SessionManager.getSession().getUser(), search);
            final ArrayList<ArrayList> resultArray = (ArrayList<ArrayList>)resultCollection;

            if (resultArray.size() != 1) {
                return -1;
            } else {
                if (resultArray.get(0).size() != 1) {
                    return -1;
                } else {
                    return (Integer)resultArray.get(0).get(0);
                }
            } /*
               * if (resultArray == null || resultArray.size() == 0 || resultArray.get(0).size() != 1) { return 0; } for
               * (ArrayList attributes : resultArray) { values.add(String.valueOf(attributes.get(0)));}*/
        } catch (ConnectionException e) {
            Exceptions.printStackTrace(e);
        }
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<? extends Object> getAttributes() {
        return attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSelectCommand() {
        return selectCommand;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(QuerySearch.class, "QuerySearch.getName()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     */
    private void AppendString(String str) {
        // jTextArea1.append(str + " ");
        if ((taQuery.getText() != null) && !taQuery.getText().isEmpty()) {
            try {
                if (!taQuery.getText(taQuery.getCaretPosition() - 1, 1).contains("(")) {
                    str = " " + str;
                }
            } catch (BadLocationException ex) {
                LOG.error("Error while appending string", ex);
                str = " " + str;
            }
        }
        taQuery.insert(str, taQuery.getCaretPosition());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jLayerLb = new javax.swing.JLabel();
        jLayerCB = new javax.swing.JComboBox();
        jMethodLb = new javax.swing.JLabel();
        jMethodCB = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jAttributesLi = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jValuesLi = new javax.swing.JList();
        jPanelTasten = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taQuery = new javax.swing.JTextArea();
        jCommandLb = new javax.swing.JLabel();
        jGetValuesBn = new javax.swing.JButton();
        jGeheZuLb = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        panCommand = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblBusyIcon = new org.jdesktop.swingx.JXBusyLabel(new java.awt.Dimension(20, 20));
        strGap = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0),
                new java.awt.Dimension(5, 25),
                new java.awt.Dimension(5, 32767));
        btnSearchCancel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblBusyValueIcon = new org.jdesktop.swingx.JXBusyLabel(new java.awt.Dimension(20, 20));
        jlEinzelwerteAnzeigen = new javax.swing.JLabel();
        panPagination = new de.cismet.tools.gui.PaginationPanel(this);

        setLayout(new java.awt.GridBagLayout());

        jLayerLb.setText(org.openide.util.NbBundle.getMessage(QuerySearch.class, "QuerySearch.jLayerLb.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 5, 5);
        add(jLayerLb, gridBagConstraints);

        jLayerCB.setModel(new javax.swing.DefaultComboBoxModel(layers.toArray()));
        jLayerCB.addItemListener(new java.awt.event.ItemListener() {

                @Override
                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    jLayerCBItemStateChanged(evt);
                }
            });
        jLayerCB.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jLayerCBActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLayerCB, gridBagConstraints);

        jMethodLb.setText(org.openide.util.NbBundle.getMessage(QuerySearch.class, "QuerySearch.jMethodLb.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 5, 5);
        add(jMethodLb, gridBagConstraints);

        jMethodCB.setModel(new javax.swing.DefaultComboBoxModel(getMethods()));
        jMethodCB.addItemListener(new java.awt.event.ItemListener() {

                @Override
                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    jMethodCBItemStateChanged(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.75;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jMethodCB, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(258, 40));

        jAttributesLi.setModel(new javax.swing.AbstractListModel() {

                Object[] objects = {};

                @Override
                public int getSize() {
                    return objects.length;
                }
                @Override
                public Object getElementAt(final int i) {
                    return objects[i];
                }
            });
        jAttributesLi.setVisibleRowCount(0);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${attributes}");
        org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings
                    .createJListBinding(
                        org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                        this,
                        eLProperty,
                        jAttributesLi,
                        "");
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        jListBinding.setSourceNullValue(null);
        jListBinding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(jListBinding);

        jScrollPane1.setViewportView(jAttributesLi);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(258, 40));

        jValuesLi.setModel(new javax.swing.AbstractListModel() {

                Object[] objects = {};

                @Override
                public int getSize() {
                    return objects.length;
                }
                @Override
                public Object getElementAt(final int i) {
                    return objects[i];
                }
            });
        jValuesLi.setVisibleRowCount(0);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${values}");
        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                eLProperty,
                jValuesLi);
        bindingGroup.addBinding(jListBinding);

        jScrollPane2.setViewportView(jValuesLi);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane2, gridBagConstraints);

        jPanelTasten.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelTasten, gridBagConstraints);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(262, 87));

        taQuery.setColumns(20);
        taQuery.setRows(5);
        jScrollPane3.setViewportView(taQuery);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane3, gridBagConstraints);

        final org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${selectCommand}"),
                jCommandLb,
                org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCommandLb, gridBagConstraints);

        jGetValuesBn.setText(org.openide.util.NbBundle.getMessage(QuerySearch.class, "QuerySearch.jGetValuesBn.text")); // NOI18N
        jGetValuesBn.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jGetValuesBnActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jGetValuesBn, gridBagConstraints);

        jGeheZuLb.setText(org.openide.util.NbBundle.getMessage(QuerySearch.class, "QuerySearch.jGeheZuLb.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jGeheZuLb, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jTextField1, gridBagConstraints);

        panCommand.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(125, 25));
        jPanel2.setPreferredSize(new java.awt.Dimension(185, 25));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblBusyIcon.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(lblBusyIcon, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(strGap, gridBagConstraints);

        btnSearchCancel.setText(org.openide.util.NbBundle.getMessage(
                QuerySearch.class,
                "SearchControlPanel.btnSearchCancel.text"));        // NOI18N
        btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearch.class,
                "SearchControlPanel.btnSearchCancel.toolTipText")); // NOI18N
        btnSearchCancel.setMaximumSize(new java.awt.Dimension(100, 25));
        btnSearchCancel.setMinimumSize(new java.awt.Dimension(100, 25));
        btnSearchCancel.setPreferredSize(new java.awt.Dimension(100, 25));
        btnSearchCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnSearchCancelActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel2.add(btnSearchCancel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCommand.add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panCommand.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(panCommand, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblBusyValueIcon.setEnabled(false);
        jPanel1.add(lblBusyValueIcon);
        jPanel1.add(jlEinzelwerteAnzeigen);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel1, gridBagConstraints);

        if (paginationEnabled) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 9;
            gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
            add(panPagination, gridBagConstraints);
        }

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  metaClass  DOCUMENT ME!
     */
    private void setMetaClass(final MetaClass metaClass) {
        final MetaClass old = this.metaClass;
        this.metaClass = metaClass;
        firePropertyChange(PROP_METACLASS, old, this.metaClass);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jLayerCBActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jLayerCBActionPerformed
        setMetaClass(null);
        if (jLayerCB.getSelectedItem() instanceof MetaClass) {
            setMetaClass((MetaClass)jLayerCB.getSelectedItem());

            threadPool.submit(new Runnable() {

                    @Override
                    public void run() {
                        final ClassAttribute[] classAttributes = metaClass.getAttribs();
                        for (final ClassAttribute attribute : classAttributes) {
                            if ("Queryable".equals(attribute.getName())) {
                                queryableValues = attribute.getOptions().keySet();
                                break;
                            }
                        }
                        final List<MemberAttributeInfo> newAttributes = getAttributesFromClass(metaClass);
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    final List<? extends Object> old = attributes;
                                    attributes = newAttributes;
                                    if (attributes != old) {
                                        firePropertyChange(PROP_ATTRIBUTES, old, attributes);
                                    }
                                    final String old2 = selectCommand;
                                    selectCommand = String.format(
                                            "SELECT * FROM %s WHERE",
                                            jLayerCB.getSelectedItem().toString());
                                    firePropertyChange(PROP_SELECT_COMMAND, old2, selectCommand);
                                }
                            });
                    }
                });
        } else if (jLayerCB.getSelectedItem() instanceof AbstractFeatureService) {
            final AbstractFeatureService afs = (AbstractFeatureService)jLayerCB.getSelectedItem();
            final Map<String, FeatureServiceAttribute> newAttribMap = afs.getFeatureServiceAttributes();
            final List<FeatureServiceAttribute> newAttributes = new ArrayList<FeatureServiceAttribute>();

            for (final String attr : (List<String>)afs.getOrderedFeatureServiceAttributes()) {
                final FeatureServiceAttribute fsa = newAttribMap.get(attr);

                if (attr != null) {
                    newAttributes.add(fsa);
                }
            }

            if (afs.getCalculatedAttributes() != null) {
                for (final String attrName : afs.getCalculatedAttributes()) {
                    for (int i = 0; i < newAttributes.size(); ++i) {
                        if (newAttributes.get(i).getName().equals(attrName)) {
                            newAttributes.remove(i);
                            break;
                        }
                    }
                }
            }

            final List<? extends Object> old = attributes;
            attributes = newAttributes;

            if (attributes != old) {
                firePropertyChange(PROP_ATTRIBUTES, old, attributes);
            }
        }

        jlEinzelwerteAnzeigen.setText("");
        final List<Object> old = values;
        values = new LinkedList<Object>();
        firePropertyChange(PROP_VALUES, old, values);

        fillButtonPanel();
    } //GEN-LAST:event_jLayerCBActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jGetValuesBnActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jGetValuesBnActionPerformed
        if (jAttributesLi.getSelectedValue() == null) {
            return;
        }

        final Object attributeObject = attributes.get(jAttributesLi.getSelectedIndex());
        currentlyExpandedAttribute = "";

        if (attributeObject instanceof MemberAttributeInfo) {
            final MetaClass metaClass = (MetaClass)jLayerCB.getSelectedItem();
            final MemberAttributeInfo attributeInfo = (MemberAttributeInfo)attributeObject;

            threadPool.submit(new Runnable() {

                    @Override
                    public void run() {
                        lblBusyValueIcon.setEnabled(true);
                        lblBusyValueIcon.setBusy(true);
                        final List<Object> newValues = GetValuesFromAttribute(metaClass, attributeInfo);

                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    final List<Object> old = values;
                                    values = newValues;
                                    lblBusyValueIcon.setEnabled(false);
                                    lblBusyValueIcon.setBusy(false);
                                    firePropertyChange(PROP_VALUES, old, values);
                                }
                            });
                    }
                });

            currentlyExpandedAttribute = attributeInfo.getName();
        } else if (attributeObject instanceof FeatureServiceAttribute) {
            lblBusyValueIcon.setEnabled(true);
            lblBusyValueIcon.setBusy(true);
            final AbstractFeatureService afs = (AbstractFeatureService)jLayerCB.getSelectedItem();
            final FeatureServiceAttribute attributeInfo = (FeatureServiceAttribute)attributeObject;
            jGetValuesBn.setEnabled(false);

            threadPool.submit(new Runnable() {

                    @Override
                    public void run() {
                        List allFeatures;
                        try {
                            final Geometry g = ZoomToLayerWorker.getServiceBounds(afs);
                            XBoundingBox bounds = null;

                            if (g != null) {
                                bounds = new XBoundingBox(g);
                                String crs;

                                if (model != null) {
                                    crs = model.getSrs().getCode();
                                } else {
                                    crs = CismapBroker.getInstance().getSrs().getCode();
                                }

                                final CrsTransformer trans = new CrsTransformer(crs);
                                bounds = trans.transformBoundingBox(bounds);
                            }
                            allFeatures = afs.getFeatureFactory()
                                        .createFeatures(afs.getQuery(), bounds, null, 0, 0, null);
                        } catch (Exception e) {
                            allFeatures = afs.getFeatureFactory().getLastCreatedFeatures();
                        }

                        final TreeSet set = new TreeSet();
                        for (final Object tmp : allFeatures) {
                            final FeatureServiceFeature tmpFeature = (FeatureServiceFeature)tmp;
                            final Object attrValue = tmpFeature.getProperty(attributeInfo.getName());

                            if (attrValue != null) {
                                set.add(attrValue);
                            }
                        }

                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    final List<Object> old = values;
                                    values = new ArrayList<Object>(set);
                                    lblBusyValueIcon.setEnabled(false);
                                    lblBusyValueIcon.setBusy(false);
                                    jGetValuesBn.setEnabled(true);
                                    firePropertyChange(PROP_VALUES, old, values);
                                }
                            });
                    }
                });

            currentlyExpandedAttribute = attributeInfo.getName();
        }

        jlEinzelwerteAnzeigen.setText(NbBundle.getMessage(
                QuerySearch.class,
                "QuerySearch.jGetValuesBnActionPerformed().jlEinzelwerteAnzeigen.text",
                currentlyExpandedAttribute));
    } //GEN-LAST:event_jGetValuesBnActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnSearchCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnSearchCancelActionPerformed
        if (panPagination.getParent() != null) {
            panPagination.reset();
        }
        performSearch();
    }                                                                                   //GEN-LAST:event_btnSearchCancelActionPerformed

    /**
     * DOCUMENT ME!
     */
    private void performSearch() {
        final QuerySearchMethod method = getSelectedMethod();
        final Object layer = jLayerCB.getSelectedItem();
        String query = taQuery.getText();

        if (layer instanceof AbstractFeatureService) {
            if (((AbstractFeatureService)layer).decorateLater()) {
                query = ((AbstractFeatureService)layer).decorateQuery(query);
            }
        }

        method.actionPerformed(jLayerCB.getSelectedItem(), query);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jMethodCBItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_jMethodCBItemStateChanged
    }                                                                            //GEN-LAST:event_jMethodCBItemStateChanged

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jLayerCBItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_jLayerCBItemStateChanged
        taQuery.setText("");
    }                                                                           //GEN-LAST:event_jLayerCBItemStateChanged

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QuerySearchMethod getSelectedMethod() {
        return (QuerySearchMethod)jMethodCB.getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  busy  DOCUMENT ME!
     */
    public void setBusy(final boolean busy) {
        lblBusyIcon.setEnabled(busy);
        lblBusyIcon.setBusy(busy);
    }

    @Override
    public JComponent getSearchWindowComponent() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getWhereCause() {
        return taQuery.getText();
    }

    @Override
    public MetaObjectNodeServerSearch getServerSearch() {
        final MetaClass metaClass = (MetaClass)jLayerCB.getSelectedItem();

        if (panPagination.getParent() != null) {
            return new QueryEditorSearch(SessionManager.getSession().getUser().getDomain(),
                    metaClass.getTableName(),
                    getWhereCause(),
                    metaClass.getId(),
                    panPagination.getPageSize(),
                    (panPagination.getPage() - 1)
                            * panPagination.getPageSize());
        } else {
            return new QueryEditorSearch(SessionManager.getSession().getUser().getDomain(),
                    metaClass.getTableName(),
                    taQuery.getText(),
                    metaClass.getId());
        }
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(this.getClass().getResource("/de/cismet/cids/search/binocular.png"));
    }

    @Override
    public boolean checkActionTag() {
        boolean result;
        try {
            result = SessionManager.getConnection()
                        .getConfigAttr(SessionManager.getSession().getUser(),
                                ACTION_TAG
                                + SessionManager.getSession().getUser().getDomain())
                        != null;
        } catch (ConnectionException ex) {
            LOG.error("Can not check ActionTag!", ex);
            result = false;
        }
        return result;
    }

    /**
     * Set the name of the search button.
     *
     * @param  newName  the new name of the search button
     */
    public void setSearchButtonName(final String newName) {
        searchButtonName = newName;
        btnSearchCancel.setText(newName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  searching  DOCUMENT ME!
     */
    public void setControlsAccordingToState(final boolean searching) {
        if (searching) {
            btnSearchCancel.setText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel_cancel.text"));        // NOI18N
            btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel_cancel.toolTipText")); // NOI18N
            btnSearchCancel.setIcon(iconCancel);
            lblBusyIcon.setEnabled(true);
            lblBusyIcon.setBusy(true);
            panPagination.setEnabled(false);
        } else {
            btnSearchCancel.setText(searchButtonName);                         // NOI18N
            btnSearchCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                    SearchControlPanel.class,
                    "SearchControlPanel.btnSearchCancel.toolTipText"));        // NOI18N
            btnSearchCancel.setIcon(iconSearch);
            lblBusyIcon.setEnabled(false);
            lblBusyIcon.setBusy(false);
            panPagination.setEnabled(true);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MouseAdapterImpl extends MouseAdapter {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MouseAdapterImpl object.
         */
        public MouseAdapterImpl() {
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() == 2) {
                final JList source = ((JList)e.getSource());
                Object selectedObject = source.getSelectedValue();
                String value;

                if (e.getSource() == jAttributesLi) {
                    selectedObject = attributes.get(jAttributesLi.getSelectedIndex());
                } // TODO remove hack

                if (selectedObject instanceof MemberAttributeInfo) {
                    if (((MemberAttributeInfo)selectedObject).getJavaclassname().equals(
                                    com.vividsolutions.jts.geom.Geometry.class.getName())) {
                        value = "'" + ((MemberAttributeInfo)selectedObject).getFieldName() + "'";
                    } else {
                        value = ((MemberAttributeInfo)selectedObject).getFieldName();
                    }
                } else {
                    final Object layer = jLayerCB.getSelectedItem();

                    if (layer instanceof AbstractFeatureService) {
                        if (source == jAttributesLi) {
                            final String v = ((FeatureServiceAttribute)selectedObject).getName();
                            if (((AbstractFeatureService)layer).decorateLater()) {
                                value = v;
                            } else {
                                value = ((AbstractFeatureService)layer).decoratePropertyName(v);
                            }
                        } else {
                            value = ((AbstractFeatureService)layer).decoratePropertyValue(
                                    currentlyExpandedAttribute,
                                    selectedObject.toString());
                        }
                    } else {
                        if (source == jAttributesLi) {
                            value = ((FeatureServiceAttribute)selectedObject).getName();
                        } else {
                            if (selectedObject instanceof MetaObject) {
                                value = String.valueOf(((MetaObject)selectedObject).getID());
                            } else {
                                value = "'" + selectedObject.toString() + "'";
                            }
                        }
                    }
                }
                AppendString(value);
            }
        }
    }
}
