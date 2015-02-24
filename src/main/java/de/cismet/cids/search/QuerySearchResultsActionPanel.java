/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.tree.SearchResultsTree;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class QuerySearchResultsActionPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(QuerySearchResultsActionPanel.class);

    //~ Instance fields --------------------------------------------------------

    private final List<QuerySearchResultsAction> actions = new ArrayList<QuerySearchResultsAction>();
    private QuerySearchResultsAction selectedAction;

    private final HashMap<MemberAttributeInfo, String> attrNames = new HashMap<MemberAttributeInfo, String>();
    private final Multimap<MetaClass, MemberAttributeInfo> maisToDisplay = ArrayListMultimap.create();

    private final QuerySearch querySearch = new QuerySearch(true);
    private final MyAttrToHideTableModel attrToHideTableModel = new MyAttrToHideTableModel();
    private final MyAttrToDisplayTableModel attrToDisplayTableModel = new MyAttrToDisplayTableModel();
    private final MyTableModel tableModel = new MyTableModel();

    private int dividerLocation = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private de.cismet.tools.gui.PaginationPanel paginationPanel1;
    private de.cismet.cids.search.QuerySearch querySearch1;
    private javax.swing.JTable tblToDisplay;
    private javax.swing.JTable tblToHide;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     */
    public QuerySearchResultsActionPanel() {
        initComponents();
    }

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     *
     * @param  action  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final QuerySearchResultsAction action) {
        this(Arrays.asList(action));
    }

    /**
     * Creates new form QuerySearchResultsActionPanel.
     *
     * @param  actions  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final List<QuerySearchResultsAction> actions) {
        initComponents();

        final SearchResultsTree searchResultsTree = ComponentRegistry.getRegistry().getSearchResultsTree();
        searchResultsTree.addPropertyChangeListener("browse", new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if ((querySearch.getSelectedMethod() instanceof SearchQuerySearchMethod)
                                && ((SearchQuerySearchMethod)querySearch.getSelectedMethod()).isSearching()) {
                        final List<Node> nodes = searchResultsTree.getResultNodes();
                        if (nodes != null) {
                            new SwingWorker<List<CidsBean>, Void>() {

                                @Override
                                protected List<CidsBean> doInBackground() throws Exception {
                                    final List<CidsBean> entities = new ArrayList<CidsBean>();
                                    for (final Node node : nodes) {
                                        if ((node != null) && (node instanceof MetaObjectNode)) {
                                            final MetaObjectNode moNode = (MetaObjectNode)node;
                                            final MetaObject mo = moNode.getObject();
                                            if (mo != null) {
                                                final CidsBean bean = mo.getBean();
                                                entities.add(bean);
                                            }
                                        }
                                    }
                                    return entities;
                                }

                                @Override
                                protected void done() {
                                    List<CidsBean> results = null;
                                    try {
                                        results = get();
                                    } catch (final Exception ex) {
                                        LOG.warn("exeption whil building search result treeset", ex);
                                    }
                                    setSearchResults(results);
                                }
                            }.execute();
                        }
                    }
                }
            });

        final MyAttrTableCellRenderer cellRenderer = new MyAttrTableCellRenderer();
        tblToDisplay.setDefaultRenderer(MemberAttributeInfo.class, cellRenderer);
        tblToHide.setDefaultRenderer(MemberAttributeInfo.class, cellRenderer);

        final MyAttrTableCellEditor cellEditor = new MyAttrTableCellEditor();
        tblToDisplay.setDefaultEditor(MemberAttributeInfo.class, cellEditor);
        tblToHide.setDefaultEditor(MemberAttributeInfo.class, cellEditor);

        jToggleButton1ActionPerformed(null);
        metaClassChanged(getMetaClass());

        querySearch.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if (evt.getSource().equals(querySearch)
                                && evt.getPropertyName().equals(QuerySearch.PROP_METACLASS)) {
                        final MetaClass metaClass = (MetaClass)evt.getNewValue();
                        metaClassChanged(metaClass);
                    }
                }
            });

        jComboBox1.setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final Component comp = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus);
                    if (value == null) {
                        ((JLabel)comp).setText(
                            org.openide.util.NbBundle.getMessage(
                                QuerySearchResultsActionPanel.class,
                                "QuerySearchResultsActionPanel.jComboBox1.null"));
                    } else if (value instanceof QuerySearchResultsAction) {
                        final QuerySearchResultsAction action = (QuerySearchResultsAction)value;
                        ((JLabel)comp).setText(action.getName());
                    }
                    return comp;
                }
            });

        this.actions.addAll(actions);
        jComboBox1.addItem(null);
        for (final QuerySearchResultsAction action : actions) {
            jComboBox1.addItem(action);
        }
        if (actions.size() == 1) {
            jComboBox1.setVisible(false);
        }
        if (!actions.isEmpty()) {
            jComboBox1.setSelectedItem(actions.get(0));
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        querySearch1 = querySearch;
        jPanel9 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblToDisplay = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblToHide = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        paginationPanel1 = querySearch.getPanginationPanel();
        jToggleButton1 = new javax.swing.JToggleButton();

        setLayout(new java.awt.GridBagLayout());

        jSplitPane2.setBorder(null);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 5, 0);
        jPanel1.add(querySearch1, gridBagConstraints);

        jSplitPane2.setLeftComponent(jPanel1);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton1,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton1.text")); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(126, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(126, 25));
        jButton1.setPreferredSize(new java.awt.Dimension(126, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel5.add(jButton1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton7,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton7.text")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(jButton7, gridBagConstraints);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jComboBox1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel5.add(jComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 10, 5);
        jPanel9.add(jPanel5, gridBagConstraints);

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(320);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(tableModel);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jScrollPane1, gridBagConstraints);

        jSplitPane1.setBottomComponent(jPanel8);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel12.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButton4.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-curve-000-left.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton4,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton4.text"));                             // NOI18N
        jButton4.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton4.toolTipText"));                      // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel4.add(jButton4, gridBagConstraints);

        jButton5.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-curve-180.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton5,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton5.text"));                        // NOI18N
        jButton5.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton5.toolTipText"));                 // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jButton5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel10, gridBagConstraints);

        jButton6.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-curve-000-double.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton6,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton6.text"));                               // NOI18N
        jButton6.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton6.toolTipText"));                        // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton6ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jButton6, gridBagConstraints);

        jButton9.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-curve-180-double.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton9,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton9.text"));                               // NOI18N
        jButton9.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton9.toolTipText"));                        // NOI18N
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton9ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel4.add(jButton9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel11, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        jPanel12.add(jPanel4, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel14, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/search/arrow-090.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton2,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton2.text"));                                                     // NOI18N
        jButton2.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton2.toolTipText"));                                              // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButton2, gridBagConstraints);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/search/arrow-270.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton3,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton3.text"));                                                     // NOI18N
        jButton3.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton3.toolTipText"));                                              // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButton3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel18, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel19, gridBagConstraints);

        jButton8.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-stop-090.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton8,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton8.text"));                       // NOI18N
        jButton8.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton8.toolTipText"));                // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton8ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel2.add(jButton8, gridBagConstraints);

        jButton10.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/search/arrow-stop-270.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton10,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton10.text"));                      // NOI18N
        jButton10.setToolTipText(org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jButton10.toolTipText"));               // NOI18N
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton10ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel2.add(jButton10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanel12.add(jPanel2, gridBagConstraints);

        tblToDisplay.setModel(attrToDisplayTableModel);
        jScrollPane2.setViewportView(tblToDisplay);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel12.add(jScrollPane2, gridBagConstraints);

        tblToHide.setModel(attrToHideTableModel);
        jScrollPane3.setViewportView(tblToHide);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel12.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jPanel12, gridBagConstraints);

        jSplitPane1.setTopComponent(jPanel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(jSplitPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(paginationPanel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jToggleButton1,
            org.openide.util.NbBundle.getMessage(
                QuerySearchResultsActionPanel.class,
                "QuerySearchResultsActionPanel.jToggleButton1.text")); // NOI18N
        jToggleButton1.setMaximumSize(new java.awt.Dimension(150, 25));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(150, 25));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(150, 25));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jToggleButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        jPanel9.add(jPanel3, gridBagConstraints);

        jSplitPane2.setRightComponent(jPanel9);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jSplitPane2, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public QuerySearch getQuerySearch() {
        return querySearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton7ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton7ActionPerformed
        if (selectedAction != null) {
            final QuerySearchResultsAction action = selectedAction;
            action.setMetaClass(getMetaClass());
            action.setWhereCause(querySearch.getWhereCause());
            final HashMap<String, String> maiNames = new HashMap<String, String>();
            for (final MemberAttributeInfo mai : maisToDisplay.get(getMetaClass())) {
                maiNames.put(mai.getFieldName(), attrNames.get(mai));
            }
            action.setMais((List<MemberAttributeInfo>)maisToDisplay.get(getMetaClass()));
            action.setMaiNames(maiNames);
            action.doAction();
        }
    }                                                                            //GEN-LAST:event_jButton7ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton4ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton4ActionPerformed
        int firstIndex = -1;
        final Collection<MemberAttributeInfo> toAdd = new ArrayList<MemberAttributeInfo>();
        for (final int selectedIndex : tblToHide.getSelectedRows()) {
            if (firstIndex < 0) {
                firstIndex = selectedIndex;
            }
            final MemberAttributeInfo selectedMai = attrToHideTableModel.getElementAt(selectedIndex);
            toAdd.add(selectedMai);
        }
        maisToDisplay.get(getMetaClass()).addAll(toAdd);

        attrToHideTableModel.refresh();
        attrToDisplayTableModel.refresh();

        if (firstIndex > -1) {
            tblToHide.setRowSelectionInterval(firstIndex - 1, firstIndex - 1);
            tblToHide.scrollRectToVisible(new Rectangle(tblToHide.getCellRect(tblToHide.getSelectedRow(), 0, true)));
        }

        tblToHide.clearSelection();

        tableModel.refresh();
    } //GEN-LAST:event_jButton4ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton5ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton5ActionPerformed
        int firstIndex = -1;
        final Collection<MemberAttributeInfo> toRemove = new ArrayList<MemberAttributeInfo>();
        for (final int selectedIndex : tblToDisplay.getSelectedRows()) {
            if (firstIndex < 0) {
                firstIndex = selectedIndex;
            }
            final MemberAttributeInfo selectedMai = attrToDisplayTableModel.getElementAt(
                    selectedIndex);
            toRemove.add(selectedMai);
        }
        maisToDisplay.get(getMetaClass()).removeAll(toRemove);

        attrToHideTableModel.refresh();
        attrToDisplayTableModel.refresh();

        tblToHide.clearSelection();
        if (firstIndex > -1) {
            tblToDisplay.setRowSelectionInterval(firstIndex - 1, firstIndex - 1);
            tblToDisplay.scrollRectToVisible(new Rectangle(
                    tblToDisplay.getCellRect(tblToDisplay.getSelectedRow(), 0, true)));
        }

        tableModel.refresh();
    } //GEN-LAST:event_jButton5ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton6ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton6ActionPerformed
        maisToDisplay.get(getMetaClass()).addAll(attrToHideTableModel.getAllElements());

        attrToHideTableModel.refresh();
        attrToDisplayTableModel.refresh();

        tblToHide.clearSelection();
        tblToDisplay.clearSelection();

        tableModel.refresh();
    } //GEN-LAST:event_jButton6ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton9ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton9ActionPerformed
        maisToDisplay.get(getMetaClass()).removeAll(attrToDisplayTableModel.getAllElements());
        attrToHideTableModel.refresh();
        attrToDisplayTableModel.refresh();

        tblToHide.clearSelection();
        tblToDisplay.clearSelection();

        tableModel.refresh();
    } //GEN-LAST:event_jButton9ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        final int[] selectedIndices = tblToDisplay.getSelectedRows();
        if ((selectedIndices.length > 0) && (selectedIndices[0] > 0)) {
            final int[] newSelectedIndices = new int[selectedIndices.length];
            for (int index = 0; index < selectedIndices.length; index++) {
                final int selectedIndex = selectedIndices[index];
                final int newIndex = selectedIndex - 1;
                newSelectedIndices[index] = newIndex;
                Collections.swap(attrToDisplayTableModel.getAllElements(),
                    selectedIndex,
                    newIndex);
            }

            attrToDisplayTableModel.refresh();

            final Rectangle rect = new Rectangle();
            for (final int newSelectedIndex : newSelectedIndices) {
                tblToDisplay.addRowSelectionInterval(newSelectedIndex, newSelectedIndex);
                rect.add(tblToDisplay.getCellRect(newSelectedIndex, 0, true));
            }
            tblToDisplay.scrollRectToVisible(rect);

            tableModel.refresh();
        }
    } //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton3ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton3ActionPerformed
        final int[] selectedIndices = tblToDisplay.getSelectedRows();
        final int[] newSelectedIndices = new int[selectedIndices.length];
        if ((selectedIndices.length > 0)
                    && (selectedIndices[selectedIndices.length - 1] < (attrToDisplayTableModel.getSize() - 1))) {
            for (int index = selectedIndices.length - 1; index >= 0; index--) {
                final int selectedIndex = selectedIndices[index];
                final int newIndex = selectedIndex + 1;
                newSelectedIndices[index] = newIndex;
                Collections.swap(attrToDisplayTableModel.getAllElements(),
                    selectedIndex,
                    newIndex);
            }

            attrToDisplayTableModel.refresh();

            final Rectangle rect = new Rectangle();
            for (final int newSelectedIndex : newSelectedIndices) {
                tblToDisplay.addRowSelectionInterval(newSelectedIndex, newSelectedIndex);
                rect.add(tblToDisplay.getCellRect(newSelectedIndex, 0, true));
            }
            tblToDisplay.scrollRectToVisible(rect);

            tableModel.refresh();
        }
    } //GEN-LAST:event_jButton3ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton8ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton8ActionPerformed
        final int[] selectedIndices = tblToDisplay.getSelectedRows();
        if ((selectedIndices.length > 0) && (selectedIndices[0] > 0)) {
            final int[] newSelectedIndices = new int[selectedIndices.length];
            for (int index = 0; index < selectedIndices.length; index++) {
                final int selectedIndex = selectedIndices[index];
                final int newIndex = index;
                newSelectedIndices[index] = newIndex;
                Collections.swap(attrToDisplayTableModel.getAllElements(),
                    selectedIndex,
                    newIndex);
            }

            attrToDisplayTableModel.refresh();

            final Rectangle rect = new Rectangle();
            for (final int newSelectedIndex : newSelectedIndices) {
                tblToDisplay.addRowSelectionInterval(newSelectedIndex, newSelectedIndex);
                rect.add(tblToDisplay.getCellRect(newSelectedIndex, 0, true));
            }
            tblToDisplay.scrollRectToVisible(rect);

            tableModel.refresh();
        }
    } //GEN-LAST:event_jButton8ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton10ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton10ActionPerformed
        final int[] selectedIndices = tblToDisplay.getSelectedRows();
        final Collection<MemberAttributeInfo> toMove = new ArrayList<MemberAttributeInfo>();
        if ((selectedIndices.length > 0)
                    && (selectedIndices[selectedIndices.length - 1] < (attrToDisplayTableModel.getSize() - 1))) {
            for (int index = selectedIndices.length - 1; index >= 0; index--) {
                final int selectedIndex = selectedIndices[index];
                final MemberAttributeInfo selectedMai = attrToDisplayTableModel.getElementAt(
                        selectedIndex);
                toMove.add(selectedMai);
            }
            maisToDisplay.get(getMetaClass()).removeAll(toMove);
            maisToDisplay.get(getMetaClass()).addAll(toMove);

            attrToDisplayTableModel.refresh();

            final Rectangle rect = new Rectangle();
            for (int index = 0; index < toMove.size(); index++) {
                final int newSelectedIndex = attrToDisplayTableModel.getSize() - 1 - index;
                tblToDisplay.addRowSelectionInterval(newSelectedIndex, newSelectedIndex);
                rect.add(tblToDisplay.getCellRect(newSelectedIndex, 0, true));
            }
            tblToDisplay.scrollRectToVisible(rect);

            tableModel.refresh();
        }
    } //GEN-LAST:event_jButton10ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton1ActionPerformed
        if (jToggleButton1.isSelected()) {
            jSplitPane1.getTopComponent().setVisible(true);
            jSplitPane1.setDividerLocation(dividerLocation);
            jSplitPane1.setDividerSize((Integer)UIManager.get("SplitPane.dividerSize"));
        } else {
            dividerLocation = jSplitPane1.getDividerLocation();
            jSplitPane1.setDividerSize(0);
            jSplitPane1.getTopComponent().setVisible(false);
        }
    }                                                                                  //GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jComboBox1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedItem() != null) {
            selectedAction = (QuerySearchResultsAction)jComboBox1.getSelectedItem();
            jButton7.setText(selectedAction.getName());
            jComboBox1.setSelectedItem(null);
        }
    }                                                                              //GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  metaClass  DOCUMENT ME!
     */
    private void metaClassChanged(final MetaClass metaClass) {
        if (metaClass != null) {
            final HashMap map = metaClass.getMemberAttributeInfos();
//        final List<MemberAttributeInfo> attributes = new ArrayList<MemberAttributeInfo>(map.size());

            final boolean addToDisplay = maisToDisplay.get(metaClass).isEmpty();
            for (final Object o : map.entrySet()) {
                final MemberAttributeInfo mai = (MemberAttributeInfo)((java.util.Map.Entry)o).getValue();
//                final String field_Name = mai.getFieldName();
                final String name = mai.getName();
                if (attrNames.get(mai) == null) {
                    attrNames.put(mai, name);
                    if (addToDisplay) {
                        maisToDisplay.put(metaClass, mai);
                    }
                }
            }
            tableModel.setMetaClass(metaClass);
            tableModel.setCidsBeans(null);
            paginationPanel1.setTotal(0);
            tableModel.refresh();
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setMinWidth(50);
            }
            jTable1.getTableHeader().setResizingAllowed(true);

            attrToHideTableModel.refresh();
            attrToDisplayTableModel.refresh();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  results  DOCUMENT ME!
     */
    public void setSearchResults(final List<CidsBean> results) {
        tableModel.setCidsBeans(results);
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaClass getMetaClass() {
        return querySearch.getMetaClass();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MyTableModel extends AbstractTableModel {

        //~ Instance fields ----------------------------------------------------

        private MetaClass metaClass;
        private List<CidsBean> cidsBeans;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CidsBeanTableModel object.
         */
        protected MyTableModel() {
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  metaClass  DOCUMENT ME!
         */
        public void setMetaClass(final MetaClass metaClass) {
            this.metaClass = metaClass;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public int getColumnCount() {
            return getMais().size();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   column  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public String getColumnName(final int column) {
            return attrNames.get(getMai(column));
        }

        /**
         * DOCUMENT ME!
         *
         * @param   column  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public Class getColumnClass(final int column) {
            return String.class;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public List<CidsBean> getCidsBeans() {
            return cidsBeans;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  cidsBeans  DOCUMENT ME!
         */
        public void setCidsBeans(final List<CidsBean> cidsBeans) {
            this.cidsBeans = cidsBeans;
            fireTableDataChanged();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public int getRowCount() {
            if (cidsBeans == null) {
                return 0;
            }
            return cidsBeans.size();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   modelIndices  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Collection<CidsBean> getCidsBeansByIndices(final int[] modelIndices) {
            final Collection<CidsBean> cidsBeansByIndices = new ArrayList<CidsBean>();
            for (int i = 0; i < modelIndices.length; i++) {
                cidsBeansByIndices.add(getCidsBeanByIndex(modelIndices[i]));
            }
            return cidsBeansByIndices;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   modelIndex  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public CidsBean getCidsBeanByIndex(final int modelIndex) {
            if (cidsBeans == null) {
                return null;
            }
            try {
                return (CidsBean)cidsBeans.get(modelIndex);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("CidsBean at index " + modelIndex + " not found. will return null", e);
                }
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   cidsBean  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getIndexByCidsBean(final CidsBean cidsBean) {
            if (cidsBeans == null) {
                return -1;
            }
            try {
                return cidsBeans.indexOf(cidsBean);
            } catch (Exception e) {
                LOG.error("error in getIndexByCidsBean(). will return -1", e);
                return -1;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  cidsBean  DOCUMENT ME!
         */
        public void addCidsBean(final CidsBean cidsBean) {
            if (cidsBeans != null) {
                cidsBeans.add(cidsBean);
                fireTableDataChanged();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   rowIndex     DOCUMENT ME!
         * @param   columnIndex  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            final CidsBean cidsBean = getCidsBeanByIndex(rowIndex);
            if (cidsBean == null) {
                return null;
            }
            if ((columnIndex < 0) || (columnIndex >= getMais().size())) {
                return null;
            }

            final MemberAttributeInfo mai = getMai(columnIndex);
            final Object o = cidsBeans.get(rowIndex).getProperty(mai.getFieldName().toLowerCase());
            if (o != null) {
                return o.toString();
            } else {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private List<MemberAttributeInfo> getMais() {
            return (List<MemberAttributeInfo>)maisToDisplay.get(metaClass);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   index  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private MemberAttributeInfo getMai(final int index) {
            return getMais().get(index);
        }

        /**
         * DOCUMENT ME!
         */
        private void refresh() {
            fireTableStructureChanged();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MyAttrToDisplayTableModel extends DefaultTableModel {

        //~ Methods ------------------------------------------------------------

        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return MemberAttributeInfo.class;
        }

        @Override
        public String getColumnName(final int column) {
            return org.openide.util.NbBundle.getMessage(
                    QuerySearchResultsActionPanel.class,
                    "QuerySearchResultsActionPanel.tblToDisplay.column.name");
        }
        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public List<MemberAttributeInfo> getAllElements() {
            return (List<MemberAttributeInfo>)maisToDisplay.get(getMetaClass());
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            return getElementAt(row);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   index  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public MemberAttributeInfo getElementAt(final int index) {
            if ((index < 0) || (index > getSize())) {
                return null;
            }
            return maisToDisplay.get(getMetaClass()).toArray(new MemberAttributeInfo[0])[index];
        }

        @Override
        public void setValueAt(final Object aValue, final int row, final int column) {
            final MemberAttributeInfo mai = getElementAt(row);
            final String newName = (String)aValue;
            attrNames.put(mai, newName);
            refresh();
            tableModel.refresh();
        }

        @Override
        public int getRowCount() {
            return getSize();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getSize() {
            return maisToDisplay.get(getMetaClass()).size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        /**
         * DOCUMENT ME!
         */
        public void refresh() {
            fireTableDataChanged();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MyAttrToHideTableModel extends DefaultTableModel {

        //~ Methods ------------------------------------------------------------

        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return MemberAttributeInfo.class;
        }

        @Override
        public String getColumnName(final int column) {
            return org.openide.util.NbBundle.getMessage(
                    QuerySearchResultsActionPanel.class,
                    "QuerySearchResultsActionPanel.tblToHide.column.name");
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public List<MemberAttributeInfo> getAllElements() {
            if (getMetaClass() == null) {
                return new ArrayList<MemberAttributeInfo>();
            } else {
                final List<MemberAttributeInfo> allElements = new ArrayList<MemberAttributeInfo>(getMetaClass()
                                .getMemberAttributeInfos().values());
                allElements.removeAll(maisToDisplay.get(getMetaClass()));
                return allElements;
            }
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            return getElementAt(row);
        }

        @Override
        public void setValueAt(final Object aValue, final int row, final int column) {
            final MemberAttributeInfo mai = getElementAt(row);
            final String newName = (String)aValue;
            attrNames.put(mai, newName);
            refresh();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   index  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public MemberAttributeInfo getElementAt(final int index) {
            return getAllElements().get(index);
        }

        @Override
        public int getRowCount() {
            return getSize();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getSize() {
            return getAllElements().size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        /**
         * DOCUMENT ME!
         */
        public void refresh() {
            fireTableDataChanged();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MyAttrTableCellRenderer extends DefaultTableCellRenderer {

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getTableCellRendererComponent(final JTable table,
                final Object value,
                final boolean isSelected,
                final boolean hasFocus,
                final int row,
                final int column) {
            final Component component = super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column);

            if (value instanceof MemberAttributeInfo) {
                final String name = attrNames.get((MemberAttributeInfo)value);
                final String fieldName = ((MemberAttributeInfo)value).getFieldName();
                ((JLabel)component).setText(name);
                ((JLabel)component).setToolTipText(fieldName);
            }

            return component;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class MyAttrTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        //~ Instance fields ----------------------------------------------------

        private final JTextField component = new JTextField();

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getTableCellEditorComponent(final JTable table,
                final Object value,
                final boolean isSelected,
                final int rowIndex,
                final int vColIndex) {
            final String name = attrNames.get((MemberAttributeInfo)value);
            ((JTextField)component).setText(name);
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            final String name = ((JTextField)component).getText();
            return name;
        }

        @Override
        public boolean isCellEditable(final EventObject eventObject) {
            if (eventObject instanceof MouseEvent) {
                return ((MouseEvent)eventObject).getClickCount() >= 2;
            }
            return false;
        }
    }
}
