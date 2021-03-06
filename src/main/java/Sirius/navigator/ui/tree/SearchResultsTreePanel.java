/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.method.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.tree.postfilter.PostFilterGUI;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.Collection;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.*;

import de.cismet.tools.gui.GUIWindow;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = GUIWindow.class)
public class SearchResultsTreePanel extends JPanel implements ResultNodeListener, GUIWindow {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchResultsTreePanel.class);

    //~ Instance fields --------------------------------------------------------

    private SearchResultsTree searchResultsTree;
    private JToolBar toolBar;
    private JButton removeButton;
    private JButton clearButton;
    private JCheckBox showDirectlyInMap;
    private JCheckBox showDirectlyInRenderer;
    private JToggleButton tbnSort;
    private JSplitPane splitPane;
    private JPanel treePanel;
    private JPanel postFilterPanel;
    private JTabbedPane tabFilters = new JTabbedPane();
    private JToggleButton tbnFilteredResults;
    private JLabel numResultsLabel;

    //~ Constructors -----------------------------------------------------------

    /**
     * This default constructor will be used by the lookup. If the object was created with this constructor, the method
     * <code>init(SearchResultsTree, boolean)</code> should be invoked, to initialise this component
     */
    public SearchResultsTreePanel() {
        super(new BorderLayout());
    }

    /**
     * Creates a new SearchResultsTreePanel object.
     *
     * @param  searchResultsTree  DOCUMENT ME!
     */
    public SearchResultsTreePanel(final SearchResultsTree searchResultsTree) {
        this(searchResultsTree, false);
    }

    /**
     * Creates a new SearchResultsTreePanel object.
     *
     * @param  searchResultsTree  DOCUMENT ME!
     * @param  advancedLayout     DOCUMENT ME!
     */
    public SearchResultsTreePanel(final SearchResultsTree searchResultsTree, final boolean advancedLayout) {
        super(new BorderLayout());
        init(searchResultsTree, advancedLayout);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Should be invoked, if the default constructor was used.
     *
     * @param  searchResultsTree  DOCUMENT ME!
     * @param  advancedLayout     DOCUMENT ME!
     */
    public void init(final SearchResultsTree searchResultsTree, final boolean advancedLayout) {
        this.searchResultsTree = searchResultsTree;
        this.toolBar = new JToolBar(org.openide.util.NbBundle.getMessage(
                    SearchResultsTreePanel.class,
                    "SearchResultsTreePanel.toolbar.name"), // NOI18N
                JToolBar.HORIZONTAL);
        this.toolBar.setRollover(advancedLayout);
        this.toolBar.setFloatable(advancedLayout);
        this.init();
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        this.createDefaultButtons();
        if (searchResultsTree instanceof PostfilterEnabledSearchResultsTree) {
            treePanel = new JPanel(new BorderLayout());
            postFilterPanel = new JPanel(new BorderLayout());
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePanel, postFilterPanel);
            treePanel.add(toolBar, BorderLayout.SOUTH);
            treePanel.add(new JScrollPane(searchResultsTree), BorderLayout.CENTER);
            this.add(splitPane, BorderLayout.CENTER);
            postFilterPanel.add(tabFilters);
        } else {
            this.add(toolBar, BorderLayout.SOUTH);
            this.add(new JScrollPane(searchResultsTree), BorderLayout.CENTER);
        }
        searchResultsTree.addResultNodeListener(this);
        this.setButtonsEnabled();
        final ButtonEnablingListener buttonEnablingListener = new ButtonEnablingListener();
        this.searchResultsTree.addTreeSelectionListener(buttonEnablingListener);
        this.searchResultsTree.addPropertyChangeListener("browse", buttonEnablingListener); // NOI18N
        this.addComponentListener(new ComponentEventForwarder());
    }

    /**
     * DOCUMENT ME!
     */
    private void createDefaultButtons() {
        final ResourceManager resources = ResourceManager.getManager();
        final ActionListener toolBarListener = new ToolBarListener();
        tbnFilteredResults = new JToggleButton(resources.getIcon("funnel.png"));
        tbnFilteredResults.setEnabled(false);
        tbnSort = new JToggleButton(resources.getIcon("sort_ascending_16.png"));
        tbnSort.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.tbnSort.tooltip"));
        tbnSort.setMargin(new Insets(4, 4, 4, 4));
        tbnSort.setActionCommand("sort");
        tbnSort.addActionListener(toolBarListener);
        toolBar.add(tbnSort);

        removeButton = new JButton(resources.getIcon("remove24.gif")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.removeButton.tooltip"));       // NOI18N
        removeButton.setActionCommand("remove");                       // NOI18N
        removeButton.setMargin(new Insets(4, 4, 4, 4));
        removeButton.addActionListener(toolBarListener);
        toolBar.add(removeButton);
        // toolBar.addSeparator();

        clearButton = new JButton(resources.getIcon("delete24.gif")); // NOI18N
        clearButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.clearButton.tooltip"));       // NOI18N
        clearButton.setActionCommand("clear");                        // NOI18N
        clearButton.setMargin(new Insets(4, 4, 4, 4));
        clearButton.addActionListener(toolBarListener);
        toolBar.add(clearButton);

        showDirectlyInMap = new JCheckBox();
        showDirectlyInMap.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    searchResultsTree.setSyncWithMap(showDirectlyInMap.isSelected());
                }
            });
        showDirectlyInMap.setSelected(false);
        toolBar.add(showDirectlyInMap);
        final JLabel showDirectlyInMapLabel = new JLabel(resources.getIcon("map.png")); // NOI18N
        showDirectlyInMapLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        searchResultsTree.syncWithMap(true);
                        if (searchResultsTree.isSyncWithRenderer()) {
                            // Because in this case the map is not brought to front by default
                            ComponentRegistry.getRegistry().showComponent("map");
//                            PluginRegistry.getRegistry()
//                                    .getPluginDescriptor("cismap")
//                                    .getUIDescriptor("cismap")
//                                    .getView()
//                                    .makeVisible();
                        }
                    }
                }
            });

        showDirectlyInMapLabel.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.showDirectInMapLabel.tooltipText")); // NOI18N
        toolBar.add(showDirectlyInMapLabel);

        showDirectlyInRenderer = new JCheckBox();
        showDirectlyInRenderer.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    searchResultsTree.setSyncWithRenderer(showDirectlyInRenderer.isSelected());
                }
            });
        showDirectlyInRenderer.setSelected(false);
        toolBar.add(showDirectlyInRenderer);
        final JLabel showDirectlyInRendererLabel = new JLabel(new javax.swing.ImageIcon(
                    getClass().getResource("/Sirius/navigator/resource/imgx/descriptionpane_icon.gif"))); // NOI18N
        showDirectlyInRendererLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        searchResultsTree.syncWithRenderer(true);
                        if (searchResultsTree.isSyncWithMap()) {
                            // Because in this case the renderer is not brought to front by default
                            ComponentRegistry.getRegistry().showComponent(ComponentRegistry.DESCRIPTION_PANE);
                        }
                    }
                }
            });

        showDirectlyInRendererLabel.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.showDirectInRendererLabel.tooltipText")); // NOI18N
        toolBar.add(showDirectlyInRendererLabel);

        if (isPostFiltersEnabled()) {
            // Postfilterpecific Buttons

//        tbnResetFilteredResults.setToolTipText(org.openide.util.NbBundle.getMessage(
//                SearchResultsTreePanel.class,
//                "SearchResultsTreePanel.tbnSort.tooltip"));
            tbnFilteredResults.setMargin(new Insets(4, 4, 4, 4));
            tbnFilteredResults.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (!tbnFilteredResults.isSelected()) {
                            ((PostfilterEnabledSearchResultsTree)getSearchResultsTree()).clearFilter();
                        }
                    }
                });
            toolBar.add(Box.createHorizontalGlue());
            toolBar.add(tbnFilteredResults);
        }

        numResultsLabel = new JLabel();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(numResultsLabel);
    }

    @Override
    public void resultNodesChanged() {
        if (searchResultsTree instanceof PostfilterEnabledSearchResultsTree) {
            final Collection resultNodes = Collections.unmodifiableCollection(searchResultsTree.resultNodes);
            Component tabFiltersVisComp = tabFilters.getSelectedComponent();
            tbnFilteredResults.setSelected(false);
            tbnFilteredResults.setEnabled(false);
            tabFilters.setVisible(true);
            final java.util.List<PostFilterGUI> availablePostFilterGUIs =
                ((PostfilterEnabledSearchResultsTree)searchResultsTree).getAvailablePostFilterGUIs();
            if (LOG.isDebugEnabled()) {
                LOG.debug("initializing " + availablePostFilterGUIs.size() + " PostFilterGUIs");
            }
            for (final PostFilterGUI pfg : availablePostFilterGUIs) {
                if (pfg.canHandle(resultNodes)) {
                    pfg.initializeFilter(Collections.unmodifiableCollection(resultNodes));
                    final Component pfgGUI = pfg.getGUI();
                    tabFilters.add(pfg.getTitle(), pfgGUI);
                    if (pfg.getIcon() != null) {
                        tabFilters.setIconAt(tabFilters.indexOfComponent(pfg.getGUI()), pfg.getIcon());
                    }
                    if (pfg.isSelected()) {
                        tabFiltersVisComp = pfgGUI;
                    }
                    pfg.addPostFilterListener((PostfilterEnabledSearchResultsTree)searchResultsTree);
                } else {
                    pfg.removePostFilterListener((PostfilterEnabledSearchResultsTree)searchResultsTree);
                    tabFilters.remove(pfg.getGUI());
                }
            }
            try {
                if (tabFiltersVisComp != null) {
                    tabFilters.setSelectedComponent(tabFiltersVisComp);
                }
            } catch (Exception skip) {
                LOG.error(skip.getMessage(), skip);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(tabFilters.getComponents().length + " post filter GUIs of "
                            + availablePostFilterGUIs.size() + " post filters enabled and initialized");
            }
        }

        refreshNumResultsLabel();
    }

    /**
     * DOCUMENT ME!
     */
    private void refreshNumResultsLabel() {
        final int numResoultsCount = ((searchResultsTree != null) && (searchResultsTree.resultNodes != null))
            ? searchResultsTree.resultNodes.size() : 0;
        final String templateForSingle = org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.numResultsLabel.templateForSingle");
        final String templateForOther = org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.numResultsLabel.templateForOther");
        if (numResoultsCount == 0) {
            numResultsLabel.setText(null);
        } else if (numResoultsCount == 1) {
            numResultsLabel.setText("  " + String.format(templateForSingle) + "  ");
        } else {
            numResultsLabel.setText("  " + String.format(templateForOther, numResoultsCount) + "  ");
        }
    }

    @Override
    public void resultNodesFiltered() {
        if (searchResultsTree instanceof PostfilterEnabledSearchResultsTree) {
            tbnFilteredResults.setSelected(((PostfilterEnabledSearchResultsTree)searchResultsTree).isFiltered());
            tbnFilteredResults.setEnabled(((PostfilterEnabledSearchResultsTree)searchResultsTree).isFiltered());
            final Collection resultNodes = Collections.unmodifiableCollection(searchResultsTree.resultNodes);
            if (LOG.isDebugEnabled()) {
                LOG.debug("adjusting " + tabFilters.getComponents() + " PostFilterGUIs");
            }
            for (final Component c : tabFilters.getComponents()) {
                if (c instanceof PostFilterGUI) {
                    ((PostFilterGUI)c).adjustFilter(resultNodes);
                }
            }
        }
    }

    @Override
    public void resultNodesCleared() {
        if (searchResultsTree instanceof PostfilterEnabledSearchResultsTree) {
            tabFilters.setVisible(false);
        }
        refreshNumResultsLabel();
    }

    /**
     * The functionality of the buttons hidden with this method is broken. Therefore that functionality needs to be
     * fixed before the items can become visible again.
     *
     * @param  button  menuItem
     */
    private void doNotShowThisButtonAsItsFunctionalityIsBroken(final JButton button) {
        button.setVisible(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void setButtonsEnabled() {
        tbnSort.setEnabled(!searchResultsTree.isEmpty());
        removeButton.setEnabled(!searchResultsTree.isEmpty() && (searchResultsTree.getSelectedNodeCount() > 0));
        clearButton.setEnabled(!searchResultsTree.isEmpty());

        // saveAllButton.setEnabled(!searchResultsTree.isEmpty());
        // saveAllButton.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPostFiltersEnabled() {
        return (searchResultsTree instanceof PostfilterEnabledSearchResultsTree);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JToolBar getToolBar() {
        return this.toolBar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SearchResultsTree getSearchResultsTree() {
        return this.searchResultsTree;
    }

    @Override
    public JComponent getGuiComponent() {
        return this;
    }

    @Override
    public String getPermissionString() {
        return GUIWindow.NO_PERMISSION;
    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public Icon getViewIcon() {
        return null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ComponentEventForwarder extends ComponentAdapter {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when the component has been made invisible.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentHidden(final ComponentEvent e) {
            searchResultsTree.dispatchEvent(e);
        }

        /**
         * Invoked when the component has been made visible.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentShown(final ComponentEvent e) {
            searchResultsTree.dispatchEvent(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ToolBarListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when an action occurs.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("remove"))         // NOI18N
            {
                searchResultsTree.removeSelectedResultNodes();
            } else if (e.getActionCommand().equals("clear"))   // NOI18N
            {
                searchResultsTree.clear();
            } else if (e.getActionCommand().equals("save"))    // NOI18N
            {
                // logger.warn("command 'save' not implemented");
            } else if (e.getActionCommand().equals("saveall")) // NOI18N
            {
                MethodManager.getManager().showQueryResultProfileManager();
            } else if (e.getActionCommand().equals("sort")) {
                if (tbnSort.isSelected()) {
                    tbnSort.setIcon(ResourceManager.getManager().getIcon("sort_descending_16.png"));
                    tbnSort.setToolTipText(org.openide.util.NbBundle.getMessage(
                            SearchResultsTreePanel.class,
                            "SearchResultsTreePanel.tbnSort.selected.tooltip"));
                } else {
                    tbnSort.setIcon(ResourceManager.getManager().getIcon("sort_ascending_16.png"));
                    tbnSort.setToolTipText(org.openide.util.NbBundle.getMessage(
                            SearchResultsTreePanel.class,
                            "SearchResultsTreePanel.tbnSort.tooltip"));
                }
                searchResultsTree.sort(!tbnSort.isSelected());
            }

            SearchResultsTreePanel.this.setButtonsEnabled();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ButtonEnablingListener implements PropertyChangeListener, TreeSelectionListener {

        //~ Methods ------------------------------------------------------------

        /**
         * This method gets called when a bound property is changed.
         *
         * @param  e  evt A PropertyChangeEvent object describing the event source and the property that has changed.
         */
        @Override
        public void propertyChange(final PropertyChangeEvent e) {
            SearchResultsTreePanel.this.setButtonsEnabled();
        }

        /**
         * Called whenever the value of the selection changes.
         *
         * @param  e  the event that characterizes the change.
         */
        @Override
        public void valueChanged(final TreeSelectionEvent e) {
            SearchResultsTreePanel.this.setButtonsEnabled();
        }
    }
}
