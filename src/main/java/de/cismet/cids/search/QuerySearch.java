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
import Sirius.navigator.search.dynamic.SearchControlListener;
import Sirius.navigator.search.dynamic.SearchControlPanel;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
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

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsWindowSearch.class)
public class QuerySearch extends javax.swing.JPanel implements CidsWindowSearchWithMenuEntry,
    SearchControlListener,
    ActionTagProtected {

    //~ Static fields/initializers ---------------------------------------------

    private static transient Logger LOG = Logger.getLogger(QuerySearch.class);
    public static final String PROP_ATTRIBUTES = "attributes"; // NOI18N
    public static final String PROP_VALUES = "values";         // NOI18N
    public static final String PROP_SELECT_COMMAND = "selectCommand";
    public static final String PROP_COUNT = "count";
    public static final String ACTION_TAG = "navigator.querybuilder.searchdialogue@";

    //~ Instance fields --------------------------------------------------------

    ExecutorService threadPool = Executors.newCachedThreadPool();
    private List<MemberAttributeInfo> attributes;
    private List<Object> values;
    private String selectCommand;
    private List<MetaClass> classes;
    private Set<String> queryableValues;
    private int count = 0;

    private SearchControlPanel pnlSearchCancel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jAttributesLi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jCommandLb;
    private javax.swing.JLabel jGeheZuLb;
    private javax.swing.JButton jGetValuesBn;
    private javax.swing.JComboBox jLayerCB;
    private javax.swing.JLabel jLayerLb;
    private javax.swing.JComboBox jMethodCB;
    private javax.swing.JLabel jMethodLb;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelTasten;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList jValuesLi;
    private javax.swing.JLabel jlEinzelwerteAnzeigen;
    private org.jdesktop.swingx.JXBusyLabel lblBusyValueIcon;
    private javax.swing.JPanel panCommand;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form StandaloneStart.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public QuerySearch() throws Exception {
        classes = GetClasses();
        initComponents();
        jMethodCB.setVisible(false);
        jMethodLb.setVisible(false);
        jGeheZuLb.setVisible(false);
        jTextField1.setVisible(false);
        jAttributesLi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jAttributesLi.addMouseListener(new MouseAdapterImpl());
        jValuesLi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jValuesLi.addMouseListener(new MouseAdapterImpl());
        jButton1.addActionListener(new ActionListenerImpl());
        jButton2.addActionListener(new ActionListenerImpl());
        jButton3.addActionListener(new ActionListenerImpl());
        jButton4.addActionListener(new ActionListenerImpl());
        jButton5.addActionListener(new ActionListenerImpl());
        jButton6.addActionListener(new ActionListenerImpl());
        jButton7.addActionListener(new ActionListenerImpl());
        jButton8.addActionListener(new ActionListenerImpl());
        jButton9.addActionListener(new ActionListenerImpl());
        jButton10.addActionListener(new ActionListenerImpl());
        jButton11.addActionListener(new ActionListenerImpl());
        jButton12.addActionListener(new ActionListenerImpl((short)-1) {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (jTextArea1.getSelectionEnd() == 0) {
                        super.actionPerformed(e);
                    } else {
                        final int start = jTextArea1.getSelectionStart();
                        final int end = jTextArea1.getSelectionEnd();
                        jTextArea1.insert("(", start);
                        jTextArea1.insert(")", end + 1);
                        // jTextArea1.setCaretPosition(end + 2);
                        if (start == end) {
                            CorrectCarret(posCorrection);
                        } else {
                            CorrectCarret((short)2);
                        }
                    }
                }
            });
        jButton13.addActionListener(new ActionListenerImpl());
        jButton14.addActionListener(new ActionListenerImpl());
        jGetValuesBn.setEnabled(false);
        jAttributesLi.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    if (!((attributes == null) || (jAttributesLi.getSelectedIndex() == -1))) {
                        final MemberAttributeInfo attributeInfo = attributes.get(jAttributesLi.getSelectedIndex());
                        if (queryableValues.contains(attributeInfo.getFieldName())) {
                            jGetValuesBn.setEnabled(true);
                            return;
                        }
                    }
                    jGetValuesBn.setEnabled(false);
                }
            });
        if (classes.size() > 0) {
            jLayerCB.setSelectedIndex(0);
        }
        if (GetMethods().size() > 0) {
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

        pnlSearchCancel = new SearchControlPanel(this);
        final Dimension max = pnlSearchCancel.getMaximumSize();
        final Dimension min = pnlSearchCancel.getMinimumSize();
        final Dimension pre = pnlSearchCancel.getPreferredSize();
        pnlSearchCancel.setMaximumSize(new java.awt.Dimension(
                new Double(max.getWidth()).intValue(),
                new Double(max.getHeight() + 5).intValue()));
        pnlSearchCancel.setMinimumSize(new java.awt.Dimension(
                new Double(min.getWidth()).intValue(),
                new Double(min.getHeight() + 5).intValue()));
        pnlSearchCancel.setPreferredSize(new java.awt.Dimension(
                new Double(pre.getWidth() + 6).intValue(),
                new Double(pre.getHeight() + 5).intValue()));
        panCommand.add(pnlSearchCancel);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private static List<MetaClass> GetClasses() throws Exception {
        // DevelopmentTools.initSessionManagerFromRMIConnectionOnLocalhost(DOMAIN, GROUP, USER, PASS);
        final MetaClass[] metaClass = SessionManager.getProxy()
                    .getClasses(SessionManager.getSession().getUser().getDomain());
        final List<MetaClass> metaClassesWithAttribute = new LinkedList<MetaClass>();
        for (final MetaClass mClass : metaClass) {
            if (!mClass.getAttributeByName("Queryable").isEmpty()) {
                metaClassesWithAttribute.add(mClass);
            }
        }

        return metaClassesWithAttribute;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static List<MemberAttributeInfo> GetAttributesFromClass(final MetaClass metaClass) {
        final HashMap map = metaClass.getMemberAttributeInfos();
        final List<MemberAttributeInfo> attributes = new ArrayList<MemberAttributeInfo>(map.size());
        for (final Object o : map.entrySet()) {
            attributes.add((MemberAttributeInfo)((java.util.Map.Entry)o).getValue());
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
    private static Vector<String> GetMethods() {
        final Vector<String> methods = new Vector<String>();
        // methods.add(null);
        methods.add("In Suchergebnissen anzeigen");
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
    public List<MemberAttributeInfo> getAttributes() {
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
        if ((jTextArea1.getText() != null) && !jTextArea1.getText().isEmpty()) {
            try {
                if (!jTextArea1.getText(jTextArea1.getCaretPosition() - 1, 1).contains("(")) {
                    str = " " + str;
                }
            } catch (BadLocationException ex) {
                LOG.fatal("Fatal", ex);
                str = " " + str;
            }
        }
        jTextArea1.insert(str, jTextArea1.getCaretPosition());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     */
    private void WriteOver(final String str) {
        String text1 = jTextArea1.getText().substring(0, jTextArea1.getSelectionStart());
        String text2 = jTextArea1.getText().substring(jTextArea1.getSelectionEnd());
        if (text1.length() >= 1) {
            switch (text1.charAt(text1.length() - 1)) {
                case ' ':
                case '(': {
                    text1 = text1 + " ";
                    break;
                }
            }
        }
        if (text2.length() >= 1) {
            switch (text2.charAt(0)) {
                case ' ':
                case '(': {
                    text2 = " " + text2;
                    break;
                }
            }
        }
        jTextArea1.setText(text1 + str + text2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  change  DOCUMENT ME!
     */
    private void CorrectCarret(final short change) {
        if (change != 0) {
            jTextArea1.setCaretPosition(jTextArea1.getCaretPosition() + change);
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jCommandLb = new javax.swing.JLabel();
        jGetValuesBn = new javax.swing.JButton();
        jGeheZuLb = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        panCommand = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblBusyValueIcon = new org.jdesktop.swingx.JXBusyLabel(new java.awt.Dimension(20, 20));
        jlEinzelwerteAnzeigen = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jLayerLb.setText(org.openide.util.NbBundle.getMessage(QuerySearch.class, "QuerySearch.jLayerLb.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 5, 5);
        add(jLayerLb, gridBagConstraints);

        jLayerCB.setModel(new javax.swing.DefaultComboBoxModel(classes.toArray()));
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

        jMethodCB.setModel(new javax.swing.DefaultComboBoxModel(GetMethods()));
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

        jButton1.setText("=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton1, gridBagConstraints);

        jButton2.setText("<>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton2, gridBagConstraints);

        jButton3.setText("Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton3, gridBagConstraints);

        jButton4.setText(">");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton4, gridBagConstraints);

        jButton5.setText(">=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton5, gridBagConstraints);

        jButton6.setText("And");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton6, gridBagConstraints);

        jButton7.setText("<");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton7, gridBagConstraints);

        jButton8.setText("<=");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton8, gridBagConstraints);

        jButton9.setText("Or");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton9, gridBagConstraints);

        jButton10.setText("_");
        jButton10.setMargin(new java.awt.Insets(2, 4, 2, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 1);
        jPanelTasten.add(jButton10, gridBagConstraints);

        jButton11.setText("%");
        jButton11.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        jPanelTasten.add(jButton11, gridBagConstraints);

        jButton12.setText("()");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton12, gridBagConstraints);

        jButton13.setText("Not");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton13, gridBagConstraints);

        jButton14.setText("Is");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTasten.add(jButton14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelTasten, gridBagConstraints);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(262, 87));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setMinimumSize(new java.awt.Dimension(20, 22));
        jScrollPane3.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
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

        panCommand.setLayout(new java.awt.FlowLayout(2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(panCommand, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(0));

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

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jLayerCBActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jLayerCBActionPerformed
        final MetaClass metaClass = (MetaClass)jLayerCB.getSelectedItem();

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
                    final List<MemberAttributeInfo> newAttributes = GetAttributesFromClass(metaClass);
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                final List<MemberAttributeInfo> old = attributes;
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

        jlEinzelwerteAnzeigen.setText("");
        final List<Object> old = values;
        values = new LinkedList<Object>();
        firePropertyChange(PROP_VALUES, old, values);
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

        final MetaClass metaClass = (MetaClass)jLayerCB.getSelectedItem();
        final MemberAttributeInfo attributeInfo = /*(MemberAttributeInfo)jAttributesLi.getSelectedValue();//*/ attributes
                    .get(jAttributesLi.getSelectedIndex());

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

        jlEinzelwerteAnzeigen.setText(NbBundle.getMessage(
                QuerySearch.class,
                "QuerySearch.jGetValuesBnActionPerformed().jlEinzelwerteAnzeigen.text",
                attributeInfo.getName()));
    } //GEN-LAST:event_jGetValuesBnActionPerformed

    @Override
    public JComponent getSearchWindowComponent() {
        return this;
    }

    @Override
    public MetaObjectNodeServerSearch getServerSearch() {
        final MetaClass metaClass = (MetaClass)jLayerCB.getSelectedItem();

        return new QueryEditorSearch(SessionManager.getSession().getUser().getDomain(),
                metaClass.getTableName(),
                jTextArea1.getText(),
                metaClass.getId());
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(this.getClass().getResource("/de/cismet/cids/search/binocular.png"));
    }

    @Override
    public MetaObjectNodeServerSearch assembleSearch() {
        return getServerSearch();
    }

    @Override
    public void searchStarted() {
    }

    @Override
    public void searchDone(final int numberOfResults) {
    }

    @Override
    public void searchCanceled() {
    }

    @Override
    public boolean suppressEmptyResultMessage() {
        return false;
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

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ActionListenerImpl implements ActionListener {

        //~ Instance fields ----------------------------------------------------

        protected short posCorrection = 0;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ActionListenerImpl object.
         */
        public ActionListenerImpl() {
        }

        /**
         * Creates a new ActionListenerImpl object.
         *
         * @param  posCorrection  DOCUMENT ME!
         */
        public ActionListenerImpl(final short posCorrection) {
            this.posCorrection = posCorrection;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (jTextArea1.getSelectionEnd() == jTextArea1.getSelectionStart()) {
                AppendString(((JButton)e.getSource()).getText());
                CorrectCarret(posCorrection);
            } else {
                WriteOver(((JButton)e.getSource()).getText());
                CorrectCarret(posCorrection);
            }
        }
    }

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
                String value = String.valueOf(selectedObject);

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
                    if (e.getSource() == jValuesLi) {
                        if (selectedObject instanceof MetaObject) {
                            value = String.valueOf(((MetaObject)selectedObject).getID());
                        } else {
                            value = "'" + (String)selectedObject + "'";
                        }
                    }
                }
                AppendString(value);
            }
        }
    }
}
