package Sirius.navigator.ui;

import java.awt.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.navigator.*;
//import Sirius.navigator.search.*;
import Sirius.navigator.search.dynamic.*;
import Sirius.navigator.search.dynamic.profile.*;
import Sirius.navigator.ui.dialog.*;
import Sirius.navigator.ui.widget.*;
import Sirius.navigator.ui.tree.*;
import Sirius.navigator.ui.attributes.*;
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.attributes.*;
import Sirius.navigator.ui.attributes.editor.*;
import Sirius.navigator.plugin.ui.manager.*;

/**
 *
 * @author  pascal
 */
public class ComponentRegistry {

    public final static String CATALOGUE_TREE = MetaCatalogueTree.class.getName();
    public final static String SEARCHRESULTS_TREE = SearchResultsTree.class.getName();
    public final static String ATTRIBUTE_VIEWER = AttributeViewer.class.getName();
    public final static String ATTRIBUTE_EDITOR = AttributeEditor.class.getName();
    public final static String DESCRIPTION_PANE = DescriptionPane.class.getName();
    private static ComponentRegistry registry = null;
    private static boolean registred;
    //private final Logger logger;
    /** Holds value of property registred. */
    /** Holds value of property coordinateChooser. */
    private CoordinateChooser coordinateChooser = null;
    /** Holds value of property navigator. */
    private Navigator navigator = null;
    /** Holds value of property passwordDialog. */
    private PasswordDialog passwordDialog = null;
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
    /** Holds value of property aboutDialog. */
    private AboutDialog aboutDialog;
    private DescriptionPane descriptionPane = null;
    /**
     * Holds value of property attributeEditor.
     */
    private AttributeEditor attributeEditor;

    /** Creates a new instance of UIRegistry */
    private ComponentRegistry(Navigator navigator, GUIContainer guiContainer, MutableMenuBar mutableMenuBar, MutableToolBar mutableToolBar, MutablePopupMenu mutablePopupMenu, MetaCatalogueTree catalogueTree, SearchResultsTree searchResultsTree, AttributeViewer attributeViewer, AttributeEditor attributeEditor, SearchDialog searchDialog, DescriptionPane descriptionPane) throws Exception {
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
    }
    private final static Object blocker = new Object();

    public final static ComponentRegistry getRegistry() throws RuntimeException {
        synchronized (blocker) {
            if (isRegistred()) {
                return registry;
            } else {
                throw new RuntimeException("unexpected call to getRegistry(): ComponentRegistry not yet initialized");//NOI18N
            }
        }
    }

    public final static void registerComponents(Navigator navigator, GUIContainer mutableContainer, MutableMenuBar mutableMenuBar, MutableToolBar mutableToolBar, MutablePopupMenu mutablePopupMenu, MetaCatalogueTree catalogueTree, SearchResultsTree searchResultsTree, AttributeViewer attributeViewer, AttributeEditor attributeEditor, SearchDialog searchDialog, DescriptionPane descriptionPane) throws Exception {
        synchronized (blocker) {
            if (!isRegistred()) {
                //Logger.getLogger(ComponentRegistry.class).debug("creating singelton ComponentRegistry instance");
                registry = new ComponentRegistry(navigator, mutableContainer, mutableMenuBar, mutableToolBar, mutablePopupMenu, catalogueTree, searchResultsTree, attributeViewer, attributeEditor, searchDialog, descriptionPane);
                registry.registred = true;
            }
        }
    }

    public final static void destroy() {
        synchronized (blocker) {
            Logger.getLogger(ComponentRegistry.class).warn("destroying singelton ComponentRegistry instance");//NOI18N
            registred = false;
            registry = null;
        }
    }

    // -------------------------------------------------------------------------
    public MetaCatalogueTree getActiveCatalogue() {
        if (catalogueTree.isShowing()) {
            return catalogueTree;
        } else {
            return searchResultsTree;
        }
    }

    public void showComponent(String id) {
        guiContainer.select(id);
    }

    public JFrame getMainWindow() {
        return navigator;
    }

    public JFrame getWindowFor(Component component) {
        Window window = SwingUtilities.windowForComponent(component);
        if (window != null && window instanceof JFrame) {
            return (JFrame) window;
        } else {
            return this.getMainWindow();
        }
    }

    // -------------------------------------------------------------------------
    /** Getter for property registred.
     * @return Value of property registred.
     *
     */
    public static boolean isRegistred() {
        return registred;
    }

    /** Getter for property coordinateChooser.
     * @return Value of property coordinateChooser.
     *
     */
    public CoordinateChooser getCoordinateChooser() {
        if (this.coordinateChooser == null) {
            this.coordinateChooser = new CoordinateChooser(this.getMainWindow());
        }

        return this.coordinateChooser;
    }

    /** Getter for property navigator.
     * @return Value of property navigator.
     *
     */
    public Navigator getNavigator() {
        return this.navigator;
    }

    /** Getter for property passwordDialog.
     * @return Value of property passwordDialog.
     *
     */
    public PasswordDialog getPasswordDialog() {
        if (this.passwordDialog == null) {
            this.passwordDialog = new PasswordDialog(this.getMainWindow());
        }

        return this.passwordDialog;
    }

    /** Getter for property searchDialog.
     * @return Value of property searchDialog.
     *
     */
    public SearchDialog getSearchDialog() {
        return this.searchDialog;
    }

    /** Getter for property searchProgressDialog.
     * @return Value of property searchProgressDialog.
     *
     */
    /*public SearchProgressDialog getSearchProgressDialog()
    {
    return this.getSearchDialog().getSearchProgressDialog();
    }*/
    /** Getter for property searchResultsTree.
     * @return Value of property searchResultsTree.
     *
     */
    public SearchResultsTree getSearchResultsTree() {
        return this.searchResultsTree;
    }

    /** Getter for property queryResultProfileManager.
     * @return Value of property queryResultProfileManager.
     *
     */
    public Sirius.navigator.search.dynamic.profile.QueryResultProfileManager getQueryResultProfileManager() {
        if (this.queryResultProfileManager == null) {
            queryResultProfileManager = new Sirius.navigator.search.dynamic.profile.QueryResultProfileManager(this.getMainWindow(), this.searchResultsTree, QueryResultProfileManager.QUERY_RESULT_PROFILE);
        }

        return this.queryResultProfileManager;
    }

    /** Getter for property mutableContainer.
     * @return Value of property mutableContainer.
     *
     */
    public GUIContainer getGUIContainer() {
        return this.guiContainer;
    }

    /** Getter for property catalogueTree.
     * @return Value of property catalogueTree.
     *
     */
    public MetaCatalogueTree getCatalogueTree() {
        return this.catalogueTree;
    }

    public AttributeViewer getAttributeViewer() {
        return this.attributeViewer;
    }

    /** Getter for property mutableMenuBar.
     * @return Value of property mutableMenuBar.
     *
     */
    public MutableMenuBar getMutableMenuBar() {
        return this.mutableMenuBar;
    }

    /** Getter for property mutableToolBar.
     * @return Value of property mutableToolBar.
     *
     */
    public MutableToolBar getMutableToolBar() {
        return this.mutableToolBar;
    }

    /** Getter for property mutablePopupMenu.
     * @return Value of property mutablePopupMenu.
     *
     */
    public MutablePopupMenu getMutablePopupMenu() {
        return this.mutablePopupMenu;
    }

    /** Getter for property pluginManager.
     * @return Value of property pluginManager.
     *
     */
    public PluginManager getPluginManager() {
        if (this.pluginManager == null) {
            this.pluginManager = new PluginManager(this.getMainWindow());
        }

        return this.pluginManager;
    }

    /** Getter for property aboutDialog.
     * @return Value of property aboutDialog.
     *
     */
    public AboutDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new AboutDialog(this.getMainWindow());
        }

        return this.aboutDialog;
    }

    /**
     * Getter for property attributeEditor.
     * @return Value of property attributeEditor.
     */
    public AttributeEditor getAttributeEditor() {
        return this.attributeEditor;
    }
    
    
    public DescriptionPane getDescriptionPane() {
        return this.descriptionPane;
    }
}
