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

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.Action;
import javax.swing.SwingWorker;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class QuerySearchResultsActionPanel extends javax.swing.JPanel implements QuerySearchResultsExecutor {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(QuerySearchResultsActionPanel.class);

    //~ Instance fields --------------------------------------------------------

    private final QuerySearch querySearch = new QuerySearch(true);
    private final Action closeAction;

    private final CidsBeansTableActionPanel actionsPanel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.cismet.cids.search.CidsBeansTableActionPanel beansColumnsPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private de.cismet.tools.gui.PaginationPanel paginationPanel1;
    private de.cismet.cids.search.QuerySearch querySearch1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     */
    public QuerySearchResultsActionPanel() {
        this((Action)null);
    }

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     *
     * @param  closeAction  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final Action closeAction) {
        this((QuerySearchResultsAction)null, closeAction);
    }

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     *
     * @param  action  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final QuerySearchResultsAction action) {
        this(action, (Action)null);
    }

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     *
     * @param  actions  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final List<QuerySearchResultsAction> actions) {
        this(actions, (Action)null);
    }

    /**
     * Creates a new QuerySearchResultsActionPanel object.
     *
     * @param  action       DOCUMENT ME!
     * @param  closeAction  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final QuerySearchResultsAction action, final Action closeAction) {
        this(Arrays.asList(action), closeAction);
    }
    /**
     * Creates new form QuerySearchResultsActionPanel.
     *
     * @param  actions      DOCUMENT ME!
     * @param  closeAction  DOCUMENT ME!
     */
    public QuerySearchResultsActionPanel(final List<QuerySearchResultsAction> actions,
            final Action closeAction) {
        this.closeAction = closeAction;

        actionsPanel = new CidsBeansTableActionPanel(actions, false);

        querySearch.initWithConnectionContext(ConnectionContext.createDeprecated());

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
                                    final List<CidsBean> entities = new ArrayList<>();
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
                                        LOG.warn("exeption while building search result treeset", ex);
                                    }
                                    setSearchResults(results);
                                }
                            }.execute();
                        }
                    }
                }
            });

        if (actions != null) {
            for (final QuerySearchResultsAction action : actions) {
                action.setExecutor(this);
            }
        }
        actionsPanel.refreshSplitPane();
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
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  dateFormat  DOCUMENT ME!
     */
    public void setDateFormat(final String dateFormat) {
        this.actionsPanel.setDateFormat(dateFormat);
    }

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
        jComboBox1 = actionsPanel.getChooseActionComboBox();
        jButton2 = actionsPanel.getExecuteActionButton();
        jButton1 = new javax.swing.JButton();
        beansColumnsPanel1 = actionsPanel;
        jPanel3 = new javax.swing.JPanel();
        paginationPanel1 = querySearch.getPanginationPanel();
        jToggleButton1 = actionsPanel.getColumnsSelectorToggleButton();

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel5.add(jComboBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(jButton2, gridBagConstraints);

        if (closeAction != null) {
            jButton1.setAction(closeAction);
            org.openide.awt.Mnemonics.setLocalizedText(
                jButton1,
                org.openide.util.NbBundle.getMessage(
                    QuerySearchResultsActionPanel.class,
                    "QuerySearchResultsActionPanel.jButton1.text")); // NOI18N
            jButton1.setMaximumSize(new java.awt.Dimension(126, 25));
            jButton1.setMinimumSize(new java.awt.Dimension(126, 25));
            jButton1.setPreferredSize(new java.awt.Dimension(126, 25));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
            gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
            jPanel5.add(jButton1, gridBagConstraints);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 10, 5);
        jPanel9.add(jPanel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(beansColumnsPanel1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(paginationPanel1, gridBagConstraints);
        jPanel3.add(jToggleButton1, new java.awt.GridBagConstraints());

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
     * @param  metaClass              DOCUMENT ME!
     * @param  defaultAttributeOrder  DOCUMENT ME!
     */
    public void setDefaultAttributeOrder(final MetaClass metaClass, final List<String> defaultAttributeOrder) {
        this.actionsPanel.setDefaultAttributeOrder(metaClass, defaultAttributeOrder);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultAttributeNames  DOCUMENT ME!
     */
    public void addDefaultAttributeNames(final HashMap<String, String> defaultAttributeNames) {
        actionsPanel.addDefaultAttributeNames(defaultAttributeNames);
    }
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
     * @return  DOCUMENT ME!
     */
    @Override
    public final MetaClass getMetaClass() {
        return querySearch.getMetaClass();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  metaClass  DOCUMENT ME!
     */
    private void metaClassChanged(final MetaClass metaClass) {
        actionsPanel.setMetaClass(metaClass);
        if (metaClass != null) {
            paginationPanel1.setTotal(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  results  DOCUMENT ME!
     */
    public void setSearchResults(final List<CidsBean> results) {
        actionsPanel.setCidsBeans(results);
    }

    @Override
    public HashMap<String, String> getAttributeNames() {
        final HashMap<String, String> attributeNamesToExport = new HashMap<>();
        for (final String attributeKey
                    : (Collection<String>)actionsPanel.getAttributesToDisplay().get(getMetaClass())) {
            attributeNamesToExport.put(attributeKey, actionsPanel.getAttributeNames().get(attributeKey));
        }
        return attributeNamesToExport;
    }

    @Override
    public List<String> getAttributeKeys() {
        return (List)actionsPanel.getAttributesToDisplay().get(getMetaClass());
    }

    @Override
    public String getWhereCause() {
        return querySearch.getWhereCause();
    }
}
