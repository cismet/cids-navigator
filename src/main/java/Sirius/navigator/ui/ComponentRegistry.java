/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.Navigator;
import Sirius.navigator.plugin.ui.manager.PluginManager;
import Sirius.navigator.search.dynamic.SearchDialog;
import Sirius.navigator.search.dynamic.profile.QueryResultProfileManager;
import Sirius.navigator.ui.attributes.AttributeViewer;
import Sirius.navigator.ui.attributes.editor.AttributeEditor;
import Sirius.navigator.ui.dialog.CoordinateChooser;
import Sirius.navigator.ui.tree.MetaCatalogueTree;
import Sirius.navigator.ui.tree.SearchResultsTree;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ComponentRegistry {

    //~ Static fields/initializers ---------------------------------------------

    public static final String CATALOGUE_TREE = MetaCatalogueTree.class.getName();
    public static final String SEARCHRESULTS_TREE = SearchResultsTree.class.getName();
    public static final String ATTRIBUTE_VIEWER = AttributeViewer.class.getName();
    public static final String ATTRIBUTE_EDITOR = AttributeEditor.class.getName();
    public static final String DESCRIPTION_PANE = DescriptionPane.class.getName();
    private static ComponentRegistry registry = null;
    private static boolean registred;
    // private final Logger logger;
    private static final Object blocker = new Object();

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property registred. */
    /** Holds value of property coordinateChooser. */
    private CoordinateChooser coordinateChooser = null;
    /** Holds value of property navigator. */
    private Navigator navigator = null;
    /** Holds value of property searchDialog. */
    private SearchDialog searchDialog = null;
    /** Holds value of property searchResultsTree. */
    private SearchResultsTree searchResultsTree = null;
    /** Holds value of property queryResultProfileManager. */
    private Sirius.navigator.search.dynamic.profile.QueryResultProfileManager queryResultProfileManager = null;
    /** Holds value of property mutableContainer. */
    private GUIContainer guiContainer = null;
    /** Holds value of property catalogueTree. */
    private MetaCatalogueTree catalogueTree = null;
    private AttributeViewer attributeViewer = null;
    /** Holds value of property mutableMenuBar. */
    private MutableMenuBar mutableMenuBar = null;
    /** Holds value of property mutableToolBar. */
    private MutableToolBar mutableToolBar = null;
    /** Holds value of property mutablePopupMenu. */
    private MutablePopupMenu mutablePopupMenu = null;
    /** Holds value of property pluginManager. */
    private PluginManager pluginManager = null;
    private DescriptionPane descriptionPane = null;
    /** Holds value of property attributeEditor. */
    private AttributeEditor attributeEditor;
    private DashBoard dashBoard;
    private de.cismet.lookupoptions.gui.OptionsDialog optionsDialog = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of UIRegistry.
     *
     * @param   navigator          DOCUMENT ME!
     * @param   guiContainer       DOCUMENT ME!
     * @param   mutableMenuBar     DOCUMENT ME!
     * @param   mutableToolBar     DOCUMENT ME!
     * @param   mutablePopupMenu   DOCUMENT ME!
     * @param   catalogueTree      DOCUMENT ME!
     * @param   searchResultsTree  DOCUMENT ME!
     * @param   attributeViewer    DOCUMENT ME!
     * @param   attributeEditor    DOCUMENT ME!
     * @param   searchDialog       DOCUMENT ME!
     * @param   descriptionPane    DOCUMENT ME!
     * @param   dashBoard          DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private ComponentRegistry(final Navigator navigator,
            final GUIContainer guiContainer,
            final MutableMenuBar mutableMenuBar,
            final MutableToolBar mutableToolBar,
            final MutablePopupMenu mutablePopupMenu,
            final MetaCatalogueTree catalogueTree,
            final SearchResultsTree searchResultsTree,
            final AttributeViewer attributeViewer,
            final AttributeEditor attributeEditor,
            final SearchDialog searchDialog,
            final DescriptionPane descriptionPane,
            final DashBoard dashBoard) throws Exception {
        this.navigator = navigator;
        this.guiContainer = guiContainer;
        this.mutableMenuBar = mutableMenuBar;
        this.mutableToolBar = mutableToolBar;
        this.mutablePopupMenu = mutablePopupMenu;
        this.catalogueTree = catalogueTree;
        this.searchResultsTree = searchResultsTree;
        this.attributeViewer = attributeViewer;
        this.attributeEditor = attributeEditor;
        this.searchDialog = searchDialog;
        this.descriptionPane = descriptionPane;
        this.dashBoard = dashBoard;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public static final ComponentRegistry getRegistry() throws RuntimeException {
        synchronized (blocker) {
            if (isRegistred()) {
                return registry;
            } else {
                throw new RuntimeException("unexpected call to getRegistry(): ComponentRegistry not yet initialized"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   navigator          DOCUMENT ME!
     * @param   mutableContainer   DOCUMENT ME!
     * @param   mutableMenuBar     DOCUMENT ME!
     * @param   mutableToolBar     DOCUMENT ME!
     * @param   mutablePopupMenu   DOCUMENT ME!
     * @param   catalogueTree      DOCUMENT ME!
     * @param   searchResultsTree  DOCUMENT ME!
     * @param   attributeViewer    DOCUMENT ME!
     * @param   attributeEditor    DOCUMENT ME!
     * @param   searchDialog       DOCUMENT ME!
     * @param   descriptionPane    DOCUMENT ME!
     * @param   dashBoard          DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static final void registerComponents(final Navigator navigator,
            final GUIContainer mutableContainer,
            final MutableMenuBar mutableMenuBar,
            final MutableToolBar mutableToolBar,
            final MutablePopupMenu mutablePopupMenu,
            final MetaCatalogueTree catalogueTree,
            final SearchResultsTree searchResultsTree,
            final AttributeViewer attributeViewer,
            final AttributeEditor attributeEditor,
            final SearchDialog searchDialog,
            final DescriptionPane descriptionPane,
            final DashBoard dashBoard) throws Exception {
        synchronized (blocker) {
            if (!isRegistred()) {
                // Logger.getLogger(ComponentRegistry.class).debug("creating singelton ComponentRegistry instance");
                registry = new ComponentRegistry(
                        navigator,
                        mutableContainer,
                        mutableMenuBar,
                        mutableToolBar,
                        mutablePopupMenu,
                        catalogueTree,
                        searchResultsTree,
                        attributeViewer,
                        attributeEditor,
                        searchDialog,
                        descriptionPane,
                        dashBoard);
                registry.registred = true;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static final void destroy() {
        synchronized (blocker) {
            Logger.getLogger(ComponentRegistry.class).warn("destroying singelton ComponentRegistry instance"); // NOI18N
            registred = false;
            registry = null;
        }
    }

    /**
     * -------------------------------------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public MetaCatalogueTree getActiveCatalogue() {
        if (catalogueTree.isShowing() && searchResultsTree.isShowing()) {
            if (catalogueTree.getSelectedNodeCount() >= searchResultsTree.getSelectedNodeCount()) {
                return catalogueTree;
            } else {
                return searchResultsTree;
            }
        } else if (catalogueTree.isShowing()) {
            return catalogueTree;
        } else {
            return searchResultsTree;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void showComponent(final String id) {
        guiContainer.select(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JFrame getMainWindow() {
        return navigator;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   component  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JFrame getWindowFor(final Component component) {
        final Window window = SwingUtilities.windowForComponent(component);
        if ((window != null) && (window instanceof JFrame)) {
            return (JFrame)window;
        } else {
            return this.getMainWindow();
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Getter for property registred.
     *
     * @return  Value of property registred.
     */
    public static boolean isRegistred() {
        return registred;
    }

    /**
     * Getter for property coordinateChooser.
     *
     * @return  Value of property coordinateChooser.
     */
    public CoordinateChooser getCoordinateChooser() {
        if (this.coordinateChooser == null) {
            this.coordinateChooser = new CoordinateChooser(this.getMainWindow());
        }

        return this.coordinateChooser;
    }

    /**
     * Getter for property navigator.
     *
     * @return  Value of property navigator.
     */
    public Navigator getNavigator() {
        return this.navigator;
    }

    /**
     * Getter for property searchDialog.
     *
     * @return  Value of property searchDialog.
     */
    public SearchDialog getSearchDialog() {
        return this.searchDialog;
    }

    /**
     * Getter for property searchProgressDialog.
     *
     * @return  Value of property searchProgressDialog.
     */
    /*public SearchProgressDialog getSearchProgressDialog()
     * { return this.getSearchDialog().getSearchProgressDialog();}*/
    /**
     * Getter for property searchResultsTree.
     *
     * @return  Value of property searchResultsTree.
     */
    public SearchResultsTree getSearchResultsTree() {
        return this.searchResultsTree;
    }

    /**
     * Getter for property queryResultProfileManager.
     *
     * @return  Value of property queryResultProfileManager.
     */
    public Sirius.navigator.search.dynamic.profile.QueryResultProfileManager getQueryResultProfileManager() {
        if (this.queryResultProfileManager == null) {
            queryResultProfileManager = new Sirius.navigator.search.dynamic.profile.QueryResultProfileManager(this
                            .getMainWindow(),
                    this.searchResultsTree,
                    QueryResultProfileManager.QUERY_RESULT_PROFILE);
        }

        return this.queryResultProfileManager;
    }

    /**
     * Getter for property mutableContainer.
     *
     * @return  Value of property mutableContainer.
     */
    public GUIContainer getGUIContainer() {
        return this.guiContainer;
    }

    /**
     * Getter for property catalogueTree.
     *
     * @return  Value of property catalogueTree.
     */
    public MetaCatalogueTree getCatalogueTree() {
        return this.catalogueTree;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AttributeViewer getAttributeViewer() {
        return this.attributeViewer;
    }

    /**
     * Getter for property mutableMenuBar.
     *
     * @return  Value of property mutableMenuBar.
     */
    public MutableMenuBar getMutableMenuBar() {
        return this.mutableMenuBar;
    }

    /**
     * Getter for property mutableToolBar.
     *
     * @return  Value of property mutableToolBar.
     */
    public MutableToolBar getMutableToolBar() {
        return this.mutableToolBar;
    }

    /**
     * Getter for property mutablePopupMenu.
     *
     * @return  Value of property mutablePopupMenu.
     */
    public MutablePopupMenu getMutablePopupMenu() {
        return this.mutablePopupMenu;
    }

    /**
     * Getter for property pluginManager.
     *
     * @return  Value of property pluginManager.
     */
    public PluginManager getPluginManager() {
        if (this.pluginManager == null) {
            this.pluginManager = new PluginManager(this.getMainWindow());
        }

        return this.pluginManager;
    }

    /**
     * Getter for property optionsDialog.
     *
     * @return  Value of property optionsDialog.
     */
    public de.cismet.lookupoptions.gui.OptionsDialog getOptionsDialog() {
        if (this.optionsDialog == null) {
            this.optionsDialog = new de.cismet.lookupoptions.gui.OptionsDialog(this.getMainWindow(), true);
            this.optionsDialog.addWindowListener(this.optionsDialog);
        }

        return this.optionsDialog;
    }

    /**
     * Getter for property attributeEditor.
     *
     * @return  Value of property attributeEditor.
     */
    public AttributeEditor getAttributeEditor() {
        return this.attributeEditor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DescriptionPane getDescriptionPane() {
        return this.descriptionPane;
    }
}
