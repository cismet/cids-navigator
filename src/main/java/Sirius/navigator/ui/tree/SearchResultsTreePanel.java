/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

import Sirius.navigator.method.*;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.resource.*;
import Sirius.navigator.search.dynamic.profile.QueryResultProfileManager;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.search.store.QueryInfo;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

import de.cismet.tools.gui.JPopupMenuButton;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SearchResultsTreePanel extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchResultsTreePanel.class);

    //~ Instance fields --------------------------------------------------------

    private final SearchResultsTree searchResultsTree;
    private final JToolBar toolBar;
    private JButton removeButton;
    private JButton clearButton;
    private JPopupMenuButton saveAllButton;
    private JCheckBox showDirectlyInMap;
    private JCheckBox showDirectlyInRenderer;
    private JToggleButton tbnSort;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchResultsTreePanel object.
     *
     * @param  searchResultsTree  DOCUMENT ME!
     */
    public SearchResultsTreePanel(final SearchResultsTree searchResultsTree) {
        this(searchResultsTree, false);
    }

    /**
     * Creates a new instance of SearchResultsTreePanel.
     *
     * @param  searchResultsTree  DOCUMENT ME!
     * @param  advancedLayout     DOCUMENT ME!
     */
    public SearchResultsTreePanel(final SearchResultsTree searchResultsTree, final boolean advancedLayout) {
        super(new BorderLayout());
        this.searchResultsTree = searchResultsTree;
        this.toolBar = new JToolBar(org.openide.util.NbBundle.getMessage(
                    SearchResultsTreePanel.class,
                    "SearchResultsTreePanel.toolbar.name"), // NOI18N
                JToolBar.HORIZONTAL);
        this.toolBar.setRollover(advancedLayout);
        this.toolBar.setFloatable(advancedLayout);

        this.init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        this.createDefaultButtons();
        this.add(toolBar, BorderLayout.SOUTH);
        this.add(new JScrollPane(searchResultsTree), BorderLayout.CENTER);
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
        toolBar.addSeparator();

        // saveAllButton = new JButton(resources.getIcon("saveall24.gif"));
        saveAllButton = new JPopupMenuButton();
        saveAllButton.setPopupMenu(new HistoryPopupMenu());
        saveAllButton.setIcon(resources.getIcon("saveall24.gif")); // NOI18N
        saveAllButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.saveAllButton.tooltip"));  // NOI18N
        saveAllButton.setActionCommand("saveall");                 // NOI18N
        saveAllButton.setMargin(new Insets(4, 4, 4, 4));
        saveAllButton.addActionListener(toolBarListener);
        doNotShowThisButtonAsItsFunctionalityIsBroken(saveAllButton);
        toolBar.add(saveAllButton);

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
                            PluginRegistry.getRegistry()
                                    .getPluginDescriptor("cismap")
                                    .getUIDescriptor("cismap")
                                    .getView()
                                    .makeVisible();
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
                            ComponentRegistry.getRegistry()
                                    .getGUIContainer()
                                    .select(ComponentRegistry.DESCRIPTION_PANE);
                        }
                    }
                }
            });

        showDirectlyInRendererLabel.setToolTipText(org.openide.util.NbBundle.getMessage(
                SearchResultsTreePanel.class,
                "SearchResultsTreePanel.showDirectInRendererLabel.tooltipText")); // NOI18N
        toolBar.add(showDirectlyInRendererLabel);
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
        saveAllButton.setEnabled(true);
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

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class HistoryPopupMenu extends JPopupMenu implements PopupMenuListener, ActionListener {

        //~ Instance fields ----------------------------------------------------

        private QueryResultProfileManager queryResultProfileManager = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new HistoryPopupMenu object.
         */
        public HistoryPopupMenu() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("HistoryPopupMenu(): creating new instance"); // NOI18N
            }
            this.addPopupMenuListener(this);

            // ugly workaround
            this.add(new JMenuItem("shouldnotseeme")); // NOI18N
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("popupMenuCanceled()"); // NOI18N
            }

            // ugly workaround
            this.add(new JMenuItem("shouldnotseeme")); // NOI18N
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("popupMenuWillBecomeInvisible()"); // NOI18N
            }

            // ugly workaround
            this.add(new JMenuItem("shouldnotseeme")); // NOI18N
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("popupMenuWillBecomeVisible(): showing popup meu"); // NOI18N
            }

            if (this.queryResultProfileManager == null) {
                this.queryResultProfileManager = ComponentRegistry.getRegistry().getQueryResultProfileManager();
            }

            if ((this.queryResultProfileManager.getUserInfos() == null)
                        || (this.queryResultProfileManager.getUserInfos().length == 0)) {
                this.queryResultProfileManager.updateQueryResultProfileManager();
            }

            this.removeAll();

            final QueryInfo[] userInfo = this.queryResultProfileManager.getUserInfos();
            if ((userInfo != null) && (userInfo.length > 0)) {
                for (int i = 0; i < userInfo.length; i++) {
                    final JMenuItem menuItem = new JMenuItem(userInfo[i].getName());
                    menuItem.setActionCommand(userInfo[i].getFileName());
                    menuItem.addActionListener(this);

                    this.add(menuItem);
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.warn("HistoryPopupMenu: no query result profiles found"); // NOI18N
            }
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("HistoryPopupMenu: loading query result profile '" + e.getActionCommand() + "'"); // NOI18N
            }
            this.queryResultProfileManager.loadSearchResults(e.getActionCommand());
        }
    }
}
