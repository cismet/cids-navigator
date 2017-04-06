/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Lagis.java
 *
 * Created on 16. M\u00E4rz 2007, 12:10
 */
package Sirius.navigator;

import Sirius.navigator.actiontag.ActionTagProtected;
import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.event.CatalogueActivationListener;
import Sirius.navigator.event.CatalogueSelectionListener;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.ui.ActionConfiguration;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.DescriptionPane;
import Sirius.navigator.ui.DescriptionPaneCalpa;
import Sirius.navigator.ui.DescriptionPaneFS;
import Sirius.navigator.ui.DescriptionPaneFX;
import Sirius.navigator.ui.LAFManager;
import Sirius.navigator.ui.LayoutedContainer;
import Sirius.navigator.ui.MutablePopupMenu;
import Sirius.navigator.ui.NavigatorStatusBar;
import Sirius.navigator.ui.attributes.AttributeViewer;
import Sirius.navigator.ui.dialog.LoginDialog;
import Sirius.navigator.ui.dnd.MetaTreeNodeDnDHandler;
import Sirius.navigator.ui.progress.ProgressObserver;
import Sirius.navigator.ui.tree.MetaCatalogueTree;
import Sirius.navigator.ui.tree.PostfilterEnabledSearchResultsTree;
import Sirius.navigator.ui.tree.SearchResultsTree;
import Sirius.navigator.ui.tree.SearchResultsTreePanel;
import Sirius.navigator.ui.tree.WorkingSpace;
import Sirius.navigator.ui.tree.WorkingSpaceTree;
import Sirius.navigator.ui.widget.FloatingFrame;
import Sirius.navigator.ui.widget.FloatingFrameConfigurator;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;
import Sirius.server.newuser.UserGroup;
import Sirius.server.newuser.permission.Permission;
import Sirius.server.newuser.permission.PermissionHolder;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.title.DockingWindowTitleProvider;
import net.infonode.docking.title.SimpleDockingWindowTitleProvider;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.StringViewMap;
import net.infonode.gui.componentpainter.AlphaGradientComponentPainter;
import net.infonode.util.Direction;

import org.apache.commons.collections.MultiHashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.jdom.Element;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;

import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;

import de.cismet.cids.editors.NavigatorAttributeEditorGui;

import de.cismet.cids.tools.search.clientstuff.CidsWindowSearch;

import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.drophandler.MappingComponentDropHandler;
import de.cismet.cismap.commons.drophandler.MappingComponentDropHandlerRegistry;
import de.cismet.cismap.commons.features.Feature;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.capabilitywidget.CapabilityWidget;
import de.cismet.cismap.commons.gui.featurecontrolwidget.FeatureControl;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.layerwidget.LayerWidget;
import de.cismet.cismap.commons.gui.layerwidget.LayerWidgetProvider;
import de.cismet.cismap.commons.gui.options.CapabilityWidgetOptionsPanel;
import de.cismet.cismap.commons.gui.overviewwidget.OverviewComponent;
import de.cismet.cismap.commons.gui.shapeexport.ShapeExport;
import de.cismet.cismap.commons.interaction.CismapBroker;
import de.cismet.cismap.commons.interaction.MapDnDListener;
import de.cismet.cismap.commons.interaction.events.MapDnDEvent;
import de.cismet.cismap.commons.util.DnDUtils;
import de.cismet.cismap.commons.wfsforms.AbstractWFSForm;
import de.cismet.cismap.commons.wfsforms.WFSFormFactory;

import de.cismet.commons.gui.protocol.ProtocolHandler;
import de.cismet.commons.gui.protocol.ProtocolPanel;

import de.cismet.ext.CExtContext;

import de.cismet.lookupoptions.gui.OptionsClient;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;

import de.cismet.netutil.Proxy;

import de.cismet.remote.RESTRemoteControlStarter;

import de.cismet.tools.CismetThreadPool;
import de.cismet.tools.JnlpSystemPropertyHelper;
import de.cismet.tools.JnlpTools;
import de.cismet.tools.Static2DTools;
import de.cismet.tools.StaticDebuggingTools;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.ConfigurationManager;
import de.cismet.tools.configuration.ShutdownHook;
import de.cismet.tools.configuration.StartupHook;
import de.cismet.tools.configuration.TakeoffHook;

import de.cismet.tools.gui.CheckThreadViolationRepaintManager;
import de.cismet.tools.gui.DefaultPopupMenuListener;
import de.cismet.tools.gui.EventDispatchThreadHangMonitor;
import de.cismet.tools.gui.GUIWindow;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import de.cismet.tools.gui.menu.CidsUiAction;
import de.cismet.tools.gui.menu.ConfiguredToolBar;

import static java.awt.Frame.MAXIMIZED_BOTH;

/**
 * DOCUMENT ME!
 *
 * @author   Puhl
 * @version  $Revision$, $Date$
 */
public class NavigatorX extends javax.swing.JFrame {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(NavigatorX.class);
    private static final ResourceManager resourceManager = ResourceManager.getManager();
    public static final String NAVIGATOR_HOME_DIR = ".navigator"
                + JnlpSystemPropertyHelper.getProperty("directory.extension", "");
    public static final String NAVIGATOR_HOME = System.getProperty("user.home") + System.getProperty("file.separator")
                + NAVIGATOR_HOME_DIR + System.getProperty("file.separator");
    public static final String DEFAULT_LAYOUT = NAVIGATOR_HOME + "navigator.layout"; // NOI18N
    private static volatile boolean startupFinished = false;

    //~ Instance fields --------------------------------------------------------

    private final PropertyManager propertyManager;
    private final ConfigurationManager configurationManager = new ConfigurationManager();
    private final ConfigurationManager cismapConfigurationManager = new ConfigurationManager();
    private final ExceptionManager exceptionManager;
    private final ProgressObserver progressObserver;
    private LoginDialog loginDialog;
//    private MutableStatusBar statusBar;
    private NavigatorStatusBar statusBar;
    private MutablePopupMenu popupMenu;
    private Preferences preferences;
    /** Holds value of property disposed. */
    private boolean disposed = false;    // InfoNode
    // Panels
    private NavigatorSplashScreen splashScreen;
    private String title;
    private RootWindow rootWindow;
    private StringViewMap viewMap = new StringViewMap();
    private Properties titleNames = new Properties();
    private int currentId = 0;
    private MappingComponent mapC = null;
    private WFSFormFactory wfsFormFactory;
    private String home = System.getProperty("user.home");    // NOI18N
    private String fs = System.getProperty("file.separator"); // NOI18N
    private String cismapDirectory = home + fs + ".cismap";   // NOI18N
    private LayerWidget layers = null;
    private final Map<DefaultMetaTreeNode, Feature> featuresInMap = new HashMap<DefaultMetaTreeNode, Feature>();
    private final Map<Feature, DefaultMetaTreeNode> featuresInMapReverse = new HashMap<Feature, DefaultMetaTreeNode>();
    private List<ConfiguredToolBar> toolbars;
    private Map<String, Action> windowActions = new HashMap<String, Action>();
    private int progress = 200;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel toolbarPanel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorX object.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public NavigatorX() throws Exception {
        this(new ProgressObserver());
    }

    /**
     * Creates a new instance of NavigatorX.
     *
     * @param   progressObserver  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public NavigatorX(final ProgressObserver progressObserver) throws Exception {
        this(progressObserver, null);
    }

    /**
     * Creates new form NavigatorX.
     *
     * @param   progressObserver  DOCUMENT ME!
     * @param   splashScreen      DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public NavigatorX(final ProgressObserver progressObserver, final NavigatorSplashScreen splashScreen)
            throws Exception {
        this.progressObserver = progressObserver;
        this.splashScreen = splashScreen;

        initComponents();
        this.propertyManager = PropertyManager.getManager();

        try {
            titleNames.load(getClass().getResourceAsStream("/Sirius/navigator/titleNames.properties"));
        } catch (Exception e) {
            LOG.warn("Cannot load titles property file", e);
        }

        this.preferences = Preferences.userNodeForPackage(this.getClass());

        this.exceptionManager = ExceptionManager.getManager();
        StaticSwingTools.tweakUI();
        this.init();
        mapC.setReadOnly(false);
        mapC.unlock();
        CismapBroker.getInstance().addMapDnDListener(new CustomMapDnDListener());
//        CismapBroker.getInstance().addStatusListener(this);

        startupFinished = true;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void init() throws Exception {
        if (StaticDebuggingTools.checkHomeForFile("cismetDebuggingInitEventDispatchThreadHangMonitor")) { // NOI18N
            EventDispatchThreadHangMonitor.initMonitoring();
        }
        if (StaticDebuggingTools.checkHomeForFile("cismetBeansbindingDebuggingOn")) {                     // NOI18N
            System.setProperty("cismet.beansdebugging", "true");                                          // NOI18N
        }
        if (StaticDebuggingTools.checkHomeForFile("cismetCheckForEDThreadVialoation")) {                  // NOI18N
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        }

        initTakeoffHooks();

        final ProxyOptionsPanel proxyOptions = new ProxyOptionsPanel();
        proxyOptions.setProxy(Proxy.fromPreferences());

        final String heavyComps = System.getProperty("contains.heavyweight.comps"); // NOI18N
        if ((heavyComps != null) && heavyComps.equals("true")) {                    // NOI18N
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        }

        // splashscreen gesetzt?
        if (splashScreen != null) {
            // ProxyOptions panel soll im SplashScreen integriert werden
            // panel übergeben
            splashScreen.setProxyOptionsPanel(proxyOptions);
            // panel noch nicht anzeigen
            splashScreen.setProxyOptionsVisible(false);

            // auf Anwenden-Button horchen
            splashScreen.addApplyButtonActionListener(new ActionListener() {

                    // Anwenden wurde gedrückt
                    @Override
                    public void actionPerformed(final ActionEvent ae) {
                        // Proxy in den Preferences setzen
                        proxyOptions.getProxy().toPreferences();
                        // Panel wieder verstecken
                        splashScreen.setProxyOptionsVisible(false);
                    }
                });
        }

        initConnection(Proxy.fromPreferences());

        try {
            checkNavigatorHome();
            initConfigurationManager();
            initCismapConfigurationManager();
            initUI();
            initWidgets();
            addWfsForms();
            initStatusBar();
            initWindow();
            initSearch();

            configurationManager.addConfigurable(OptionsClient.getInstance());
            if (PropertyManager.getManager().isProtocolEnabled()) {
                configurationManager.addConfigurable(ProtocolHandler.getInstance());
            }
            configurationManager.configure();
            cismapConfigurationManager.addConfigurable(new ShapeExport());
            cismapConfigurationManager.configure();
            statusBar.initialize();
            initMenuAndToolbar();

            SwingUtilities.invokeLater(new Runnable() {

                    // UGLY WINNING
                    @Override
                    public void run() {
                        loadLayout(DEFAULT_LAYOUT, true);
                    }
                });
            if (!StaticDebuggingTools.checkHomeForFile("cismetTurnOffInternalWebserver")) { // NOI18N
                initHttpServer();
            }

            initStartupHooks();
        } catch (final InterruptedException iexp) {
            LOG.error("navigator start interrupted: " + iexp.getMessage() + "\n disconnecting from server"); // NOI18N
            SessionManager.getSession().logout();
            SessionManager.getConnection().disconnect();
            this.progressObserver.reset();
        }

        // From Hell
        final KeyStroke configLoggerKeyStroke = KeyStroke.getKeyStroke(
                'L',
                InputEvent.CTRL_DOWN_MASK
                        + InputEvent.SHIFT_DOWN_MASK);
        final Action configAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                Log4JQuickConfig.getSingletonInstance().setVisible(true);
                            }
                        });
                }
            };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(configLoggerKeyStroke, "CONFIGLOGGING"); // NOI18N
        getRootPane().getActionMap().put("CONFIGLOGGING", configAction);                                          // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    private void initUI() throws InterruptedException {
        progressObserver.setProgress(
            100,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_100")); // NOI18N
        popupMenu = new MutablePopupMenu();
        this.setContentPane(new JPanel(new BorderLayout(), true));
        this.setJMenuBar(menuBar);
        rootWindow = DockingUtil.createRootWindow(viewMap, true);

        final DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(
            theme.getRootWindowProperties());
        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);
        final AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(
                java.awt.SystemColor.inactiveCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.inactiveCaptionText);
        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setTabAreaOrientation(Direction.UP);
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setPaintTabAreaShadow(true);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setShadowSize(10);
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setShadowStrength(0.8f);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getMinimizeButtonProperties().setVisible(false);
        this.getContentPane().add(rootWindow, BorderLayout.CENTER);
//        jPanel1.add(rootWindow, BorderLayout.CENTER);
        rootWindow.getRootWindowProperties().getDockingWindowProperties().setTitleProvider(new CustomTitleProvider());
    }

    /**
     * DOCUMENT ME!
     */
    private void initStatusBar() {
        statusBar = new NavigatorStatusBar();
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void initMenuAndToolbar() throws Exception {
        progress = 850;
        try {
            progressObserver.setProgress(
                progress,
                org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_850")); // NOI18N
        } catch (InterruptedException ex) {
            // nothing to do
        }
        final ActionConfiguration config = new ActionConfiguration("/Sirius/navigator/MenuConfig.json", windowActions);

        config.configureMainMenu(menuBar);
        toolbars = config.getToolbars();

        if ((toolbars != null) && !toolbars.isEmpty()) {
            toolbarPanel.setLayout(new GridLayout(toolbars.size(), 1));

            for (final ConfiguredToolBar toolbar : toolbars) {
                toolbarPanel.add(toolbar.getToolbar());
            }
            this.getContentPane().add(toolbarPanel, BorderLayout.NORTH);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void initWidgets() throws Exception {
        progressObserver.setProgress(
            progress,
            org.openide.util.NbBundle.getMessage(NavigatorX.class,
                "NavigatorX.progressObserver.loadWindow")); // NOI18N
        final Collection<? extends GUIWindow> windows = Lookup.getDefault().lookupAll(GUIWindow.class);
        LOG.error("windows found: " + windows.size());
        MetaCatalogueTree catalogueTree = null;
        SearchResultsTree searchResultsTree = null;
        WorkingSpaceTree workingSpaceTree = null;
        AttributeViewer attributeViewer = null;
        NavigatorAttributeEditorGui editorGui = null;
        DescriptionPane description = null;
        ProtocolPanel protocol = null;
        View view = null;
        String viewId = null;

        for (final GUIWindow window : windows) {
            if (window.getPermissionString().equals(GUIWindow.NO_PERMISSION)) {
                progress = ((progress < 500) ? (progress + 30) : progress);
                progressObserver.setProgress(
                    progress,
                    org.openide.util.NbBundle.getMessage(NavigatorX.class,
                        "NavigatorX.progressObserver.loadWindow")); // NOI18N
                final String titleName = ((window.getViewTitle() != null) ? window.getViewTitle() : "");
                if (window instanceof MetaCatalogueTree) {
                    final RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());
                    ((MetaCatalogueTree)window).init(
                        rootTreeNode,
                        PropertyManager.getManager().isEditable(),
                        true,
                        propertyManager.getMaxConnections());
                    // this handler registers itself at the given tree
                    new MetaTreeNodeDnDHandler((MetaCatalogueTree)window);
                    final JPanel panel = wrapInScrollPane(window.getGuiComponent());
                    viewId = ComponentRegistry.CATALOGUE_TREE;
                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.metaCatalogueTree.name"),
                            resourceManager.getIcon("catalogue_tree_icon.gif"),
                            panel);
                    catalogueTree = (MetaCatalogueTree)window;
                    viewMap.addView(ComponentRegistry.CATALOGUE_TREE, view);
                } else if (window instanceof SearchResultsTreePanel) {
                    if (PropertyManager.getManager().isPostfilterEnabled()) {
                        searchResultsTree = new PostfilterEnabledSearchResultsTree();
                    } else {
                        searchResultsTree = new SearchResultsTree();
                    }
                    ((SearchResultsTreePanel)window).init(searchResultsTree, propertyManager.isAdvancedLayout());
                    viewId = ComponentRegistry.SEARCHRESULTS_TREE;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.searchResultsTreePanel.name"),
                            resourceManager.getIcon("searchresults_tree_icon.gif"),
                            window.getGuiComponent());
                    new MetaTreeNodeDnDHandler(searchResultsTree);
                    viewMap.addView(ComponentRegistry.SEARCHRESULTS_TREE, view);
                } else if (window instanceof WorkingSpaceTree) {
                    final WorkingSpace workingSpace = new WorkingSpace((WorkingSpaceTree)window,
                            propertyManager.isAdvancedLayout());
                    new MetaTreeNodeDnDHandler((WorkingSpaceTree)window);
                    viewId = ComponentRegistry.WORKINGSPACE_TREE;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.WorkingSpaceTreePanel.name"),
                            resourceManager.getIcon("clipboard-list.png"),
                            workingSpace);
                    workingSpaceTree = (WorkingSpaceTree)window;
                    viewMap.addView(ComponentRegistry.WORKINGSPACE_TREE, view);
                } else if (window instanceof AttributeViewer) {
                    final FloatingFrameConfigurator configurator = new FloatingFrameConfigurator(
                            ComponentRegistry.ATTRIBUTE_VIEWER,
                            org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.initWidgets().configurator.name.attributeViewer")); // NOI18N
                    configurator.setTitleBarEnabled(false);
                    final JComponent comp = wrapInFloatingFrame(window.getGuiComponent(), configurator);
                    viewId = ComponentRegistry.ATTRIBUTE_VIEWER;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.attributeviewer.name"),
                            resourceManager.getIcon("attributetable_icon.gif"),
                            comp);
                    attributeViewer = (AttributeViewer)window;
                    viewMap.addView(ComponentRegistry.ATTRIBUTE_VIEWER,
                        view);
                } else if (window instanceof NavigatorAttributeEditorGui) {
                    final FloatingFrameConfigurator configurator = new FloatingFrameConfigurator(
                            ComponentRegistry.ATTRIBUTE_EDITOR,
                            org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.initWidgets().configurator.name.attributeEditor")); // NOI18N
                    configurator.setTitleBarEnabled(false);
                    final JComponent comp = wrapInFloatingFrame(window.getGuiComponent(), configurator);
                    viewId = ComponentRegistry.ATTRIBUTE_EDITOR;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.attributeeditor.name"),
                            resourceManager.getIcon("attributetable_icon.gif"),
                            comp);
                    editorGui = (NavigatorAttributeEditorGui)window;
                    viewMap.addView(ComponentRegistry.ATTRIBUTE_EDITOR, view);
                } else if ((window instanceof DescriptionPaneFS) || (window instanceof DescriptionPaneFX)
                            || (window instanceof DescriptionPaneCalpa)) {
                    final FloatingFrameConfigurator configurator = new FloatingFrameConfigurator(
                            ComponentRegistry.DESCRIPTION_PANE,
                            org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.initWidgets().configurator.name.descriptionPane")); // NOI18N
                    final JComponent comp = wrapInFloatingFrame(window.getGuiComponent(), configurator);
                    viewId = ComponentRegistry.DESCRIPTION_PANE;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.descriptionpane.name"),
                            resourceManager.getIcon("descriptionpane_icon.gif"),
                            comp);
                    description = (DescriptionPane)window;
                    viewMap.addView(ComponentRegistry.DESCRIPTION_PANE, view);
                } else if (window instanceof ProtocolPanel) {
                    if (!PropertyManager.getManager().isProtocolEnabled()) {
                        continue;
                    }
                    viewId = ComponentRegistry.PROTOCOL_PANEL;

                    view = new View(org.openide.util.NbBundle.getMessage(
                                NavigatorX.class,
                                "NavigatorX.protocolpanel.name"),
                            resourceManager.getIcon("protocolpane_icon.png"),
                            window.getGuiComponent());
                    protocol = (ProtocolPanel)window;
                    viewMap.addView(ComponentRegistry.PROTOCOL_PANEL, view);
                    // todo: wo wird das genutzt
                } else if (window.getGuiComponent() instanceof MappingComponent) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = "Map";
                    viewMap.addView("Map", view);
                    mapC = (MappingComponent)window.getGuiComponent();
                    mapC.setMainMappingComponent(true);
                    wfsFormFactory = WFSFormFactory.getInstance(mapC);
                    CismapBroker.getInstance().addCrsChangeListener(mapC);
                    CismapBroker.getInstance().setMappingComponent(mapC);
                } else if (window.getGuiComponent() instanceof LayerWidget) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    layers = (LayerWidget)window.getGuiComponent();
                    layers.init(mapC);
                    layers.setPreferredSize(new Dimension(100, 120));
                    mapC.setMappingModel(layers.getMappingModel());
                } else if (window.getGuiComponent() instanceof CapabilityWidget) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    final CapabilityWidget caps = (CapabilityWidget)window.getGuiComponent();
                    CapabilityWidgetOptionsPanel.setCapabilityWidget(caps);
                    CismapBroker.getInstance().addMapBoundsListener(caps);
                } else if (window.getGuiComponent() instanceof FeatureControl) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    final FeatureControl fc = (FeatureControl)window.getGuiComponent();
                    fc.init(mapC);
                    mapC.getFeatureCollection().addFeatureCollectionListener(fc);
                    CismapBroker.getInstance().addMapBoundsListener(fc);
                } else if (window.getGuiComponent() instanceof OverviewComponent) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    final OverviewComponent oMap = (OverviewComponent)window.getGuiComponent();
                    oMap.setMasterMap(mapC);
                } else {
                    viewId = getUniqueId(window.getClass().getName());
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewMap.addView(viewId, view);
                }

                if (window.getGuiComponent() instanceof Configurable) {
                    cismapConfigurationManager.addConfigurable((Configurable)window.getGuiComponent());
                }

                if (window.getGuiComponent() instanceof EmbededControlBar) {
                    ((EmbededControlBar)window.getGuiComponent()).setControlBarVisible(false);
                    final Vector<AbstractButton> customButtons = ((EmbededControlBar)window.getGuiComponent())
                                .getControlBarButtons();
                    if (customButtons != null) {
                        for (final AbstractButton currentButton : customButtons) {
                            view.getCustomTabComponents().add(currentButton);
                        }
                    }
                }

                final WindowAction windowAction = new WindowAction(viewId);
                windowActions.put((String)windowAction.getValue(CidsUiAction.CIDS_ACTION_KEY), windowAction);
            }
        }

        progress = ((progress > 550) ? progress : 550);
        progressObserver.setProgress(
            progress,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_550")); // NOI18N
        ComponentRegistry.registerComponents(
            this,
            null,
            null,
            null,
            popupMenu,
            catalogueTree,
            searchResultsTree,
            workingSpaceTree,
            attributeViewer,
            editorGui,
            description);

        final CatalogueSelectionListener catalogueSelectionListener = new CatalogueSelectionListener(
                attributeViewer,
                description);
        catalogueTree.addTreeSelectionListener(catalogueSelectionListener);
        searchResultsTree.addTreeSelectionListener(catalogueSelectionListener);

        catalogueTree.addComponentListener(new CatalogueActivationListener(
                catalogueTree,
                attributeViewer,
                description));
        searchResultsTree.addComponentListener(new CatalogueActivationListener(
                searchResultsTree,
                attributeViewer,
                description));

        final DefaultPopupMenuListener cataloguePopupMenuListener = new DefaultPopupMenuListener(popupMenu);
        catalogueTree.addMouseListener(cataloguePopupMenuListener);
        searchResultsTree.addMouseListener(cataloguePopupMenuListener);

        if (workingSpaceTree != null) {
            workingSpaceTree.addTreeSelectionListener(catalogueSelectionListener);
            workingSpaceTree.addComponentListener(new CatalogueActivationListener(
                    workingSpaceTree,
                    attributeViewer,
                    description));
            workingSpaceTree.addMouseListener(cataloguePopupMenuListener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getUniqueId(final String title) {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   component  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JPanel wrapInScrollPane(final JComponent component) {
        final JPanel container = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(component);

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {
            container.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
            component.setBorder(null);
            scrollPane.setBorder(null);
            scrollPane.setViewportBorder(null);
        }

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   component     DOCUMENT ME!
     * @param   configurator  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JComponent wrapInFloatingFrame(final JComponent component, final FloatingFrameConfigurator configurator) {
        final FloatingFrame container = new FloatingFrame(component, configurator);

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {
            container.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
            component.setBorder(null);
        }
        container.setTileBarVisible(false);
        return container;
    }

    /**
     * DOCUMENT ME!
     */
    private void addWfsForms() {
        final Set<String> keySet = wfsFormFactory.getForms().keySet();
//        final JMenu wfsFormsMenu = new JMenu(org.openide.util.NbBundle.getMessage(
//                    CismapPlugin.class,
//                    "CismapPlugin.CismapPlugin(PluginContext).wfsFormMenu.title")); // NOI18N
        cismapConfigurationManager.configure(wfsFormFactory);

        for (final String key : keySet) {
            // View
            final AbstractWFSForm form = wfsFormFactory.getForms().get(key);
            form.setMappingComponent(mapC);
            if (LOG.isDebugEnabled()) {
                LOG.debug("WFSForms: key,form" + key + "," + form); // NOI18N
            }

            final View formView = new View(form.getTitle(),
                    Static2DTools.borderIcon(form.getIcon(), 0, 3, 0, 1),
                    form);
            if (LOG.isDebugEnabled()) {
                LOG.debug("WFSForms: formView" + formView); // NOI18N
            }
            viewMap.addView(form.getId(), formView);

            // Menu
            final WindowAction action = new WindowAction(form.getId());
            windowActions.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  v  DOCUMENT ME!
     */
    private void showOrHideView(final View v) {
        ///irgendwas besser als Closable ??
        // Problem wenn floating --> close -> open  (muss zweimal open)
        if (v.isClosable()) {
            v.close();
        } else {
            v.restore();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void select(final String id) {
        final View v = viewMap.getView(id);

        if (v != null) {
            if (!v.isClosable()) {
                v.restore();
            }

            v.restoreFocus();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void initCismapPlugin() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    private void initWindow() throws InterruptedException {
        progress = 650;
        progressObserver.setProgress(
            progress,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_950")); // NOI18N
        this.title = org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.title");                // NOI18N
        this.setTitle(title);
        this.setIconImage(resourceManager.getIcon("navigator_icon.gif").getImage());                            // NOI18N
        this.restoreWindowState();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new NavigatorX.ClosingListener());
        final KeyStroke showLayoutKeyStroke = KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK);
        final Action showLayoutAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DeveloperUtil.createWindowLayoutFrame(
                                        org.openide.util.NbBundle.getMessage(
                                            LayoutedContainer.class,
                                            "LayoutedContainer.doConfigKeystrokes.rootWindow.title"), // NOI18N
                                        rootWindow)
                                        .setVisible(true);
                            }
                        });
                }
            };
        rootWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(showLayoutKeyStroke, "SHOW_LAYOUT"); // NOI18N
        rootWindow.getActionMap().put("SHOW_LAYOUT", showLayoutAction); // NOI18N
        // rootWindow.registerKeyboardAction(showLayoutAction,showLayoutKeyStroke,JComponent.WHEN_FOCUSED);
    }

    /**
     * Search for window searches and integrates them into the navigator.
     */
    private void initSearch() {
        progress = 750;
        try {
            progressObserver.setProgress(
                progress,
                org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_750")); // NOI18N
        } catch (InterruptedException ex) {
            // nothing to do
        }
        final Collection<? extends CidsWindowSearch> windowSearches = Lookup.getDefault()
                    .lookupAll(CidsWindowSearch.class);

        if (!windowSearches.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing " + windowSearches.size() + " window searches.");
            }
            for (final CidsWindowSearch windowSearch : windowSearches) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Initializing window search '" + windowSearch.getName() + "'.");
                }

                if (checkActionTag(windowSearch)) {
                    final View view = new View(windowSearch.getName(),
                            windowSearch.getIcon(),
                            windowSearch.getSearchWindowComponent());
                    viewMap.addView(windowSearch.getClass().getName(), view);

                    final WindowAction action = new WindowAction(windowSearch.getClass().getName());
                    windowActions.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not initialize window search '" + windowSearch.getName()
                                    + "' due to restricted permissions.");
                    }
                }
            }
        }
    }

    /**
     * Checks, if the action tag for the given object is set.
     *
     * @param   toCheck  DOCUMENT ME!
     *
     * @return  return true, iff the component should be enabled
     */
    private boolean checkActionTag(final Object toCheck) {
        if (toCheck instanceof ActionTagProtected) {
            final ActionTagProtected atp = (ActionTagProtected)toCheck;
            return atp.checkActionTag();
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    private void initStartupHooks() throws InterruptedException {
        progressObserver.setProgress(
            980,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_980")); // NOI18N

        final Collection<? extends StartupHook> hooks = Lookup.getDefault().lookupAll(StartupHook.class);

        for (final StartupHook hook : hooks) {
            hook.applicationStarted();
        }

        progressObserver.setProgress(
            1000,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_1000")); // NOI18N
    }

    /**
     * DOCUMENT ME!
     */
    private void initHttpServer() {
        try {
            RESTRemoteControlStarter.initRestRemoteControlMethods(propertyManager.getHttpInterfacePort());
        } catch (Throwable e) {
            LOG.error("Error during initializion of remote control server", e);
        }

        // this is the HTTP interface from cismap plugin
        try {
            final Thread http = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);                             // Bugfix Try Deadlock
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Http Interface initialisieren"); // NOI18N
                                }

                                final Server server = new Server();
                                final Connector connector = new SelectChannelConnector();
                                connector.setPort(9098);
                                server.setConnectors(new Connector[] { connector });

                                final Handler param = new AbstractHandler() {

                                        @Override
                                        public void handle(final String target,
                                                final HttpServletRequest request,
                                                final HttpServletResponse response,
                                                final int dispatch) throws IOException, ServletException {
                                            final Request base_request = (request instanceof Request)
                                                ? (Request)request : HttpConnection.getCurrentConnection().getRequest();
                                            base_request.setHandled(true);
                                            response.setContentType("text/html");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // NOI18N
                                            response.setStatus(HttpServletResponse.SC_ACCEPTED);
                                            response.getWriter()
                                                    .println(
                                                        "<html><head><title>HTTP interface</title></head><body><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\"><tr><td width=\"30%\" align=\"center\" valign=\"middle\"><img border=\"0\" src=\"http://www.cismet.de/images/cismetLogo250M.png\" ><br></td><td width=\"%\">&nbsp;</td><td width=\"50%\" align=\"left\" valign=\"middle\"><font face=\"Arial\" size=\"3\" color=\"#1c449c\">... and <b><font face=\"Arial\" size=\"3\" color=\"#1c449c\">http://</font></b> just works</font><br><br><br></td></tr></table></body></html>"); // NOI18N
                                        }
                                    };

                                final Handler hello = new AbstractHandler() {

                                        @Override
                                        public void handle(final String target,
                                                final HttpServletRequest request,
                                                final HttpServletResponse response,
                                                final int dispatch) throws IOException, ServletException {
                                            try {
                                                if (request.getLocalAddr().equals(request.getRemoteAddr())) {
                                                    LOG.info("HttpInterface connected"); // NOI18N

                                                    if (target.equalsIgnoreCase("/gotoBoundingBox")) { // NOI18N

                                                        final String x1 = request.getParameter("x1"); // NOI18N
                                                        final String y1 = request.getParameter("y1"); // NOI18N
                                                        final String x2 = request.getParameter("x2"); // NOI18N
                                                        final String y2 = request.getParameter("y2"); // NOI18N

                                                        try {
                                                            final BoundingBox bb = new BoundingBox(
                                                                    new Double(x1),
                                                                    new Double(y1),
                                                                    new Double(x2),
                                                                    new Double(y2));
                                                            mapC.gotoBoundingBoxWithHistory(bb);
                                                        } catch (Exception e) {
                                                            LOG.warn("gotoBoundingBox failed", e); // NOI18N
                                                        }
                                                    }

                                                    if (target.equalsIgnoreCase("/gotoScale")) { // NOI18N

                                                        final String x1 = request.getParameter("x1"); // NOI18N
                                                        final String y1 = request.getParameter("y1"); // NOI18N
                                                        final String scaleDenominator = request.getParameter(
                                                                "scaleDenominator");                  // NOI18N

                                                        try {
                                                            final BoundingBox bb = new BoundingBox(
                                                                    new Double(x1),
                                                                    new Double(y1),
                                                                    new Double(x1),
                                                                    new Double(y1));

                                                            mapC.gotoBoundingBoxWithHistory(
                                                                mapC.getScaledBoundingBox(
                                                                    new Double(scaleDenominator).doubleValue(),
                                                                    bb));
                                                        } catch (Exception e) {
                                                            LOG.warn("gotoBoundingBox failed", e); // NOI18N
                                                        }
                                                    }

                                                    if (target.equalsIgnoreCase("/centerOnPoint")) { // NOI18N

                                                        final String x1 = request.getParameter("x1"); // NOI18N
                                                        final String y1 = request.getParameter("y1"); // NOI18N

                                                        try {
                                                            final BoundingBox bb = new BoundingBox(
                                                                    new Double(x1),
                                                                    new Double(y1),
                                                                    new Double(x1),
                                                                    new Double(y1));
                                                            mapC.gotoBoundingBoxWithHistory(bb);
                                                        } catch (Exception e) {
                                                            LOG.warn("centerOnPoint failed", e); // NOI18N
                                                        }
                                                    } else {
                                                        LOG.warn("Unknown target: " + target);   // NOI18N
                                                    }
                                                } else {
                                                    LOG.warn(
                                                        "Someone tries to access the http interface from an other computer. Access denied."); // NOI18N
                                                }
                                            } catch (Throwable t) {
                                                LOG.error("Error while handle http requests", t); // NOI18N
                                            }
                                        }
                                    };

                                final HandlerCollection handlers = new HandlerCollection();
                                handlers.setHandlers(new Handler[] { param, hello });
                                server.setHandler(handlers);

                                server.start();
                                server.join();
                            } catch (Throwable t) {
                                LOG.error("Error in the HttpInterface of cismap", t); // NOI18N
                            }
                        }
                    });
            http.start();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initialise HTTP interface");                               // NOI18N
            }
        } catch (Throwable t) {
            LOG.fatal("Nothing at all", t);                                           // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    private void initTakeoffHooks() throws InterruptedException {
        final Collection<? extends TakeoffHook> hooks = Lookup.getDefault().lookupAll(TakeoffHook.class);

        for (final TakeoffHook hook : hooks) {
            hook.applicationTakeoff();
        }
    }

    /**
     * Initialises the configuration manager.
     */
    private void initConfigurationManager() {
        // TODO: Put in method which modifies progress
        String cismapconfig = null;
        String fallBackConfig = null;

        // Default
        if (cismapconfig == null) {
            cismapconfig = "defaultNavigatorProperties.xml"; // NOI18N
        }

        if (fallBackConfig == null) {
            fallBackConfig = "defaultNavigatorProperties.xml"; // NOI18N
        }

        configurationManager.setDefaultFileName(cismapconfig);
        configurationManager.setFallBackFileName(fallBackConfig);

        configurationManager.setFileName("configuration.xml");
        configurationManager.setClassPathFolder("/");
        configurationManager.setFolder(NAVIGATOR_HOME_DIR);
    }

    /**
     * Initialises the configuration manager.
     */
    private void initCismapConfigurationManager() {
        String cismapconfig = null;
        String fallBackConfig = null;
        String dirExtension = "";
//        PluginContext context;
        try {
            final String ext = JnlpSystemPropertyHelper.getProperty("directory.extension"); // NOI18N

            System.out.println("SystemdirExtension=:" + ext); // NOI18N

            if (ext != null) {
                dirExtension = ext;
                cismapDirectory += ext;
            }
        } catch (final Exception e) {
            LOG.warn("Error while adding DirectoryExtension"); // NOI18N
        }

        try {
            final User user = SessionManager.getSession().getUser();
            final UserGroup userGroup = user.getUserGroup();

            final String prefix = "cismapconfig:"; // NOI18N
            final String username = user.getName();
            Collection<UserGroup> groups;
            if (userGroup != null) {
                final ArrayList<UserGroup> onlyOne = new ArrayList<UserGroup>();
                onlyOne.add(userGroup);
                groups = onlyOne;
            } else {
                groups = user.getPotentialUserGroups();
            }

            // First try: cismapconfig:username@usergroup@domainserver
// if (cismapconfig == null) {
// for (final UserGroup group : groups) {
// cismapconfig = context.getEnvironment()
// .getParameter(prefix + username + "@" + group.getName() + "@" // NOI18N
// + group.getDomain());
// if (cismapconfig != null) {
// break;
// }
// }
// }

            // Second try: cismapconfig:*@usergroup@domainserver
// if (cismapconfig == null) {
// for (final UserGroup group : groups) {
// cismapconfig = context.getEnvironment()
// .getParameter(prefix + "*" + "@" + group.getName() + "@" // NOI18N
// + group.getDomain());
// if (cismapconfig != null) {
// break;
// }
// }
// }

            // Third try: cismapconfig:*@*@domainserver//NOI18N
// if (cismapconfig == null) {
// for (final UserGroup group : groups) {
// cismapconfig = context.getEnvironment()
// .getParameter(prefix + "*" + "@" + "*" + "@" + group.getDomain()); // NOI18N
// if (cismapconfig != null) {
// break;
// }
// }
// }
            // Default from pluginXML
// if (cismapconfig == null) {
// cismapconfig = context.getEnvironment().getParameter(prefix + "default"); // NOI18N
// }
//
// fallBackConfig = context.getEnvironment().getParameter(prefix + "default"); // NOI18N
        } catch (final Exception e) {
            LOG.info("cismap started standalone", e); // NOI18N
        }

        if (cismapconfig == null) {
            cismapconfig = "defaultCismapProperties.xml"; // NOI18N
        }

        if (fallBackConfig == null) {
            fallBackConfig = "defaultCismapProperties.xml";  // NOI18N
        }
        LOG.info("cismap ServerConfigFile=" + cismapconfig); // NOI18N
        cismapConfigurationManager.setDefaultFileName(cismapconfig);
        cismapConfigurationManager.setFallBackFileName(fallBackConfig);

        cismapConfigurationManager.setFileName("configurationPlugin.xml"); // NOI18N
// cismapConfigurationManager.addConfigurable(metaSearchComponentFactory);

        cismapConfigurationManager.setClassPathFolder("/");             // NOI18N
        cismapConfigurationManager.setFolder(".cismap" + dirExtension); // NOI18N
    }

    /**
     * DOCUMENT ME!
     */
    private void checkNavigatorHome() {
        try {
            final File file = new File(NAVIGATOR_HOME);
            if (file.exists()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Navigator Directory exists.");                     // NOI18N
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Navigator Directory does not exist --> creating"); // NOI18N
                }
                file.mkdir();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Navigator Directory successfully created");        // NOI18N
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while checking/creating Navigator home directory", ex); // NOI18N
        }
    }

    /**
     * #########################################################################
     *
     * @param   proxyConfig  DOCUMENT ME!
     *
     * @throws  ConnectionException   DOCUMENT ME!
     * @throws  InterruptedException  DOCUMENT ME!
     */
    private void initConnection(final Proxy proxyConfig) throws ConnectionException, InterruptedException {
        progressObserver.setProgress(
            25,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_25")); // NOI18N
        if (LOG.isDebugEnabled()) {
            LOG.debug("initialising connection using proxy: " + proxyConfig);
        }
        final Connection connection = ConnectionFactory.getFactory()
                    .createConnection(propertyManager.getConnectionClass(),
                        propertyManager.getConnectionInfo().getCallserverURL(),
                        proxyConfig);
        ConnectionSession session = null;
        ConnectionProxy proxy = null;

        progressObserver.setProgress(
            50,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_50")); // NOI18N
        // autologin
        if (propertyManager.isAutoLogin()) {
            if (LOG.isInfoEnabled()) {
                LOG.info("performing autologin of user '" + propertyManager.getConnectionInfo().getUsername() + "'"); // NOI18N
            }
            try {
                session = ConnectionFactory.getFactory()
                            .createSession(connection, propertyManager.getConnectionInfo(), true);
                proxy = ConnectionFactory.getFactory().createProxy(propertyManager.getConnectionProxyClass(), session);
                SessionManager.init(proxy);
            } catch (UserException uexp) {
                LOG.error("autologin failed", uexp);                                                                  // NOI18N
                session = null;
            }
        }

        // autologin = false || autologin failed
        if (!propertyManager.isAutoLogin() || (session == null)) {
            if (LOG.isInfoEnabled()) {
                LOG.info("performing login"); // NOI18N
            }
            try {
                session = ConnectionFactory.getFactory()
                            .createSession(connection, propertyManager.getConnectionInfo(), false);
            } catch (UserException uexp) {
            }                                 // should never happen
            proxy = ConnectionFactory.getFactory().createProxy(propertyManager.getConnectionProxyClass(), session);
            SessionManager.init(proxy);

            loginDialog = new LoginDialog(this);
            StaticSwingTools.showDialog(loginDialog);
        }

        PropertyManager.getManager()
                .setEditable(this.hasPermission(
                        SessionManager.getProxy().getClasses(),
                        PermissionHolder.WRITEPERMISSION));
        // PropertyManager.getManager().setEditable(true);
        if (LOG.isInfoEnabled()) {
            LOG.info("initConnection(): navigator editor enabled: " + PropertyManager.getManager().isEditable()); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   classes     DOCUMENT ME!
     * @param   permission  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean hasPermission(final MetaClass[] classes, final Permission permission) {
        final User user = SessionManager.getSession().getUser();
        final UserGroup userGroup = user.getUserGroup();
        if (userGroup != null) {
            return hasPermission(classes, permission, userGroup);
        } else {
            for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                if (hasPermission(classes, permission, potentialUserGroup)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   classes     DOCUMENT ME!
     * @param   permission  DOCUMENT ME!
     * @param   userGroup   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean hasPermission(final MetaClass[] classes, final Permission permission, final UserGroup userGroup) {
        final String key = userGroup.getKey().toString();

        for (int i = 0; i < classes.length; i++) {
            try {
                // falsch aufgerufen schlob SessionManager.getSession().getUser().getUserGroup().getKey()
                final PermissionHolder perm = classes[i].getPermissions();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(" usergroup can edit ?? " + key + " permissions :: " + perm);               // NOI18N          //logger.debug(perm +" \n" +key);
                }
                if ((perm != null) && perm.hasPermission(key, permission))                                // xxxxxxxxxxxxxxxxxxxxxx user????
                {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("permission '" + permission + "' found in class '" + classes[i] + "'"); // NOI18N
                    }
                    return true;
                }
            } catch (final Exception exp) {
                LOG.error("hasPermission(): could not check permissions", exp);                           // NOI18N
            }
        }

        LOG.warn("permission '" + permission + "' not found, disabling editor"); // NOI18N
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        toolbarPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();

        toolbarPanel.setLayout(new java.awt.GridLayout(1, 0));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.title")); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setJMenuBar(menuBar);

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     */
    private void restoreWindowState() {
        if (LOG.isInfoEnabled()) {
            LOG.info("restoring window state ..."); // NOI18N
        }

        final int windowHeight = this.preferences.getInt("windowHeight", PropertyManager.getManager().getHeight()); // NOI18N
        final int windowWidth = this.preferences.getInt("windowWidth", PropertyManager.getManager().getWidth());    // NOI18N
        final int windowX = this.preferences.getInt("windowX", 0);                                                  // NOI18N
        final int windowY = this.preferences.getInt("windowY", 0);                                                  // NOI18N

        final boolean windowMaximised;
        // issue #8: osx does to correctly determine the maximised state of a window, thus we ignore that on osx
        final String osName = System.getProperty("os.name"); // NOI18N
        if (osName.startsWith("Mac")) {
            windowMaximised = false;
        } else {
            windowMaximised = this.preferences.getBoolean(
                    "windowMaximised",                       // NOI18N
                    propertyManager.isMaximizeWindow());
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("restoring window state: \nwindowHeight=" + windowHeight + ", windowWidth=" + windowWidth
                        + ", windowX=" + windowX + ", windowY=" + windowY + ", windowMaximised=" + windowMaximised); // NOI18N
        }

        this.setSize(windowWidth, windowHeight);
        this.setLocation(windowX, windowY);

        if (windowMaximised) {
            this.setExtendedState(MAXIMIZED_BOTH);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  file    DOCUMENT ME!
     * @param  isInit  DOCUMENT ME!
     */
    public void loadLayout(final String file, final boolean isInit) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Load Layout.. from " + file);
        }
        final File layoutFile = new File(file);

        if (layoutFile.exists()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Layout File exists");
            }
            try {
                final FileInputStream layoutInput = new FileInputStream(layoutFile);
                final ObjectInputStream in = new ObjectInputStream(layoutInput);
                rootWindow.read(in);
                in.close();
                rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
                rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
                if (isInit) {
                    final int count = viewMap.getViewCount();
                    for (int i = 0; i < count; i++) {
                        final View current = viewMap.getViewAtIndex(i);
                        if (current.isUndocked()) {
                            current.dock();
                        }
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading Layout successfull");
                }
            } catch (IOException ex) {
                LOG.error("Layout File IO Exception --> loading default Layout", ex);
                if (isInit) {
                    JOptionPane.showMessageDialog(
                        this,
                        "W\u00E4hrend dem Laden des Layouts ist ein Fehler aufgetreten.\n Das Layout wird zur\u00FCckgesetzt.",
                        "Fehler",
                        JOptionPane.INFORMATION_MESSAGE);
                    doLayoutInfoNode();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "W\u00E4hrend dem Laden des Layouts ist ein Fehler aufgetreten.\n Das Layout wird zur\u00FCckgesetzt.",
                        "Fehler",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            if (isInit) {
                LOG.warn("Datei exitstiert nicht --> default layout (init)");
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // UGLY WINNING --> Gefixed durch IDW Version 1.5
                            // setupDefaultLayout();
                            // DeveloperUtil.createWindowLayoutFrame("nach setup1",rootWindow).setVisible(true);
                            doLayoutInfoNode();
                            // DeveloperUtil.createWindowLayoutFrame("nach setup2",rootWindow).setVisible(true);
                        }
                    });
            } else {
                LOG.warn("Datei exitstiert nicht)");
                JOptionPane.showMessageDialog(
                    this,
                    "Das angegebene Layout konnte nicht gefunden werden.",
                    "Fehler",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void resetToDefaultLayout() {
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayoutInfoNode() {
        ObjectInputStream in = null;
        InputStream layoutInput = null;

        try {
            layoutInput = NavigatorX.class.getResourceAsStream("/Sirius/navigator/defaultLayout.layout");
            in = new ObjectInputStream(layoutInput);
            rootWindow.read(in);
            in.close();
        } catch (Exception e) {
            LOG.error("cannot load default layout", e);
        } finally {
            if (layoutInput != null) {
                try {
                    layoutInput.close();
                } catch (IOException ex) {
                    LOG.error("Cannot close layout stream", ex);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    LOG.error("Cannot close layout stream", ex);
                }
            }
        }
    }

    /**
     * Getter for property disposed.
     *
     * @return  Value of property disposed.
     */
    public boolean isDisposed() {
        return this.disposed;
    }

    /**
     * Setter for property disposed.
     *
     * @param  disposed  New value of property disposed.
     */
    private synchronized void setDisposed(final boolean disposed) {
        this.disposed = disposed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  file    DOCUMENT ME!
     * @param  parent  DOCUMENT ME!
     */
    public void saveLayout(final String file, final Component parent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving Layout.. to " + file);                                                              // NOI18N
        }
        final File layoutFile = new File(file);
        try {
            if (!layoutFile.exists()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Saving Layout.. File does not exit");                                              // NOI18N
                }
                layoutFile.createNewFile();
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Saving Layout.. File does exit");                                                  // NOI18N
                }
            }
            final FileOutputStream layoutOutput = new FileOutputStream(layoutFile);
            final ObjectOutputStream out = new ObjectOutputStream(layoutOutput);
            rootWindow.write(out);
            out.flush();
            out.close();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Saving Layout.. to " + file + " successfull");                                         // NOI18N
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                org.openide.util.NbBundle.getMessage(LayoutedContainer.class, "NavigatorX.saveLayout().message"), // NOI18N
                org.openide.util.NbBundle.getMessage(LayoutedContainer.class, "NavigatorX.saveLayout().title"),   // NOI18N
                JOptionPane.INFORMATION_MESSAGE);
            LOG.error("A failure occured during writing the layout file", ex);                                    // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void dispose() {
        if (LOG.isInfoEnabled()) {
            LOG.info("dispose() called"); // NOI18N
            LOG.info("saving Layout");    // NOI18N
        }
        saveLayout(LayoutedContainer.DEFAULT_LAYOUT, this);
        saveWindowState();

        configurationManager.writeConfiguration();
        cismapConfigurationManager.writeConfiguration();

        PluginRegistry.destroy();

        SessionManager.getConnection().disconnect();
        SessionManager.destroy();
        MethodManager.destroy();
        ComponentRegistry.destroy();

        if (!isDisposed()) {
            super.dispose();
            setDisposed(true);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void saveWindowState() {
        final int windowHeight = this.getHeight();
        final int windowWidth = this.getWidth();
        final int windowX = (int)this.getLocation().getX();
        final int windowY = (int)this.getLocation().getY();
        final boolean windowMaximised = (this.getExtendedState() == MAXIMIZED_BOTH);

        if (LOG.isInfoEnabled()) {
            LOG.info("saving window state: \nwindowHeight=" + windowHeight + ", windowWidth=" + windowWidth
                        + ", windowX=" + windowX + ", windowY=" + windowY + ", windowMaximised=" + windowMaximised); // NOI18N
        }

        this.preferences.putInt("windowHeight", windowHeight);           // NOI18N
        this.preferences.putInt("windowWidth", windowWidth);             // NOI18N
        this.preferences.putInt("windowX", windowX);                     // NOI18N
        this.preferences.putInt("windowY", windowY);                     // NOI18N
        this.preferences.putBoolean("windowMaximised", windowMaximised); // NOI18N

        if (LOG.isInfoEnabled()) {
            LOG.info("saved window state"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new NavigatorX.NavigatorShutdown());
        Thread.setDefaultUncaughtExceptionHandler(DefaultNavigatorExceptionHandler.getInstance());

        // There is no way to adjust the Locale using the Jnlp file.
        try {
            JnlpTools.adjustDefaultLocale();

            System.out.println("Using default Locale: " + Locale.getDefault());
        } catch (final SecurityException e) {
            System.err.println("You have insufficient rights to set the default locale."); // NOI18N
        }

        try {
            // cmdline arguments ...............................................

            if (args.length < 5) {
                // FIXME: use correct error string
                final String errorString = new String(
                        "\nusage: navigator %1 %2 %3 %4 %5 %6(%5)\n%1 = navigator config file \n%2 = navigator working directory \n%3 = plugin base directory \n%4 = navigator search forms base directory \n%5 navigator search profile store (optional, default: %1/profiles \n\nexample: java Sirius.navigator.NavigatorX c:\\programme\\cids\\navigator\\navigator.cfg c:\\programme\\cids\\navigator\\ c:\\programme\\cids\\navigator\\plugins\\ c:\\programme\\cids\\navigator\\search\\ c:\\programme\\cids\\navigator\\search\\profiles\\"); // NOI18N
                System.out.println(errorString);

                throw new Exception(errorString);
            } else {
                System.out.println("-------------------------------------------------------"); // NOI18N
                System.out.println("C I D S   N A V I G A T 0 R   C O N F I G U R A T I 0 N"); // NOI18N
                System.out.println("-------------------------------------------------------"); // NOI18N
                System.out.println("log4j.properties = " + args[0]);                           // NOI18N
                System.out.println("navigator.cfg    = " + args[1]);                           // NOI18N
                System.out.println("basedir          = " + args[2]);                           // NOI18N
                System.out.println("plugindir        = " + args[3]);                           // NOI18N
                System.out.println("-------------------------------------------------------"); // NOI18N

                // log4j configuration .....................................
                final Properties properties = new Properties();
                boolean l4jinited = false;
                try {
                    final URL log4jPropertiesURL = new URL(args[0]);
                    properties.load(log4jPropertiesURL.openStream());

                    l4jinited = true;
                } catch (final Exception e) {
                    System.err.println("could not lode log4jproperties will try to load it from file" // NOI18N
                                + e.getMessage());
                    e.printStackTrace();
                }

                try {
                    if (!l4jinited) {
                        properties.load(new BufferedInputStream(new FileInputStream(new File(args[0]))));
                    }
                } catch (Exception e) {
                    System.err.println("could not lode log4jproperties " + e.getMessage()); // NOI18N
                    e.printStackTrace();
                }

                PropertyConfigurator.configure(properties);

                // log4j configuration .....................................
                PropertyManager.getManager()
                        .configure(args[1], args[2], args[3], null, ((args.length > 5) ? args[5] : null));
            }

            // configuration ...................................................
            // look and feel ...................................................
            LAFManager.getManager().changeLookAndFeel(PropertyManager.getManager().getLookAndFeel());

            final NavigatorSplashScreen navigatorSplashScreen = new NavigatorSplashScreen(PropertyManager.getManager()
                            .getSharedProgressObserver(),
                    // FIXME: illegal icon
                    resourceManager.getIcon("wundaLogo.png"));

            navigatorSplashScreen.pack();
            navigatorSplashScreen.setLocationRelativeTo(null);
            navigatorSplashScreen.toFront();
            navigatorSplashScreen.show();
            // run .............................................................
        } catch (final Throwable t) {
            // error .............................................................
            Logger.getLogger(NavigatorX.class).fatal("could not create navigator instance", t); // NOI18N
            ExceptionManager.getManager()
                    .showExceptionDialog(
                        ExceptionManager.FATAL,
                        org.openide.util.NbBundle.getMessage(
                            NavigatorX.class,
                            "NavigatorX.main(String[]).ExceptionManager_anon.name"),            // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            NavigatorX.class,
                            "NavigatorX.main(String[]).ExceptionManager_anon.message"),
                        t);                                                                     // NOI18N

            System.exit(1);
            // error .............................................................
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the configurationManager
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the cismapConfigurationManager
     */
    public ConfigurationManager getCismapConfigurationManager() {
        return cismapConfigurationManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the cismapDirectory
     */
    public String getCismapDirectory() {
        return cismapDirectory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
// @Override
    public void masterConfigure(final Element e) {
//        final Element prefs = e.getChild("cismapPluginUIPreferences"); // NOI18N
//        cismapPluginUIPreferences = prefs;
//        activateLineRef();
//
//        try {
//            final Element httpInterfacePortElement = prefs.getChild("httpInterfacePort"); // NOI18N
//
//            try {
//                httpInterfacePort = new Integer(httpInterfacePortElement.getText());
//            } catch (Throwable t) {
//                LOG.warn("httpInterface was not configured. Set default value: " + httpInterfacePort, t); // NOI18N
//            }
//        } catch (Throwable t) {
//            LOG.error("Error while loading the help urls (" + prefs.getChildren() + "), disabling menu items", t); // NOI18N
//        }
//
//        try {
//            final List<JMenuItem> serverProfileItems = new ArrayList<JMenuItem>();
//
//            final Element serverprofiles = e.getChild("serverProfiles");                   // NOI18N
//            final Iterator<Element> it = serverprofiles.getChildren("profile").iterator(); // NOI18N
//
//            while (it.hasNext()) {
//                final Element next = it.next();
//                final String id = next.getAttributeValue("id");                                 // NOI18N
//                final String sorter = next.getAttributeValue("sorter");                         // NOI18N
//                final String name = next.getAttributeValue("name");                             // NOI18N
//                final String path = next.getAttributeValue("path");                             // NOI18N
//                final String icon = next.getAttributeValue("icon");                             // NOI18N
//                final String descr = next.getAttributeValue("descr");                           // NOI18N
//                final String descrWidth = next.getAttributeValue("descrwidth");                 // NOI18N
//                final String complexDescriptionText = next.getTextTrim();
//                final String complexDescriptionSwitch = next.getAttributeValue("complexdescr"); // NOI18N
//
//                final JMenuItem serverProfileMenuItem = new JMenuItem();
//                serverProfileMenuItem.setText(name);
//                serverProfileMenuItem.addActionListener(new ActionListener() {
//
//                        @Override
//                        public void actionPerformed(final ActionEvent e) {
//                            try {
//                                ((ActiveLayerModel)mapC.getMappingModel()).removeAllLayers();
//                                configurationManager.configureFromClasspath(path, null);
//                                setButtonSelectionAccordingToMappingComponent();
//                            } catch (Throwable ex) {
//                                log.fatal("No ServerProfile", ex); // NOI18N
//                            }
//                        }
//                    });
//                serverProfileMenuItem.setName("ServerProfile:" + sorter + ":" + name); // NOI18N
//
//                if ((complexDescriptionSwitch != null) && complexDescriptionSwitch.equalsIgnoreCase("true") // NOI18N
//                            && (complexDescriptionText != null)) {
//                    serverProfileMenuItem.setToolTipText(complexDescriptionText);
//                } else if (descrWidth != null) {
//                    serverProfileMenuItem.setToolTipText("<html><table width=\"" + descrWidth               // NOI18N
//                                + "\" border=\"0\"><tr><td>" + descr + "</p></td></tr></table></html>");    // NOI18N
//                } else {
//                    serverProfileMenuItem.setToolTipText(descr);
//                }
//
//                try {
//                    serverProfileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource(icon)));
//                } catch (Exception iconE) {
//                    log.warn("Could not create Icon for ServerProfile.", iconE); // NOI18N
//                }
//
//                serverProfileItems.add(serverProfileMenuItem);
//            }
//
//            Collections.sort(serverProfileItems, new Comparator<JMenuItem>() {
//
//                    @Override
//                    public int compare(final JMenuItem o1, final JMenuItem o2) {
//                        if ((o1.getName() != null) && (o2.getName() != null)) {
//                            return o1.getName().compareTo(o2.getName());
//                        } else {
//                            return 0;
//                        }
//                    }
//                });
//
//            menFile.removeAll();
//
//            for (final Component c : before) {
//                menFile.add(c);
//            }
//
//            for (final JMenuItem jmi : serverProfileItems) {
//                menFile.add(jmi);
//            }
//
//            for (final Component c : after) {
//                menFile.add(c);
//            }
//        } catch (Exception x) {
//            log.info("No server profile available, or error while cerating analysis.", x); // NOI18N
//        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class NavigatorShutdown extends Thread {

        //~ Static fields/initializers -----------------------------------------

        private static final transient Logger LOG = Logger.getLogger(NavigatorX.NavigatorShutdown.class);

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NavigatorShutdown object.
         */
        public NavigatorShutdown() {
            super("NavigatorShutdown");
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        @Override
        public void run() {
            if (startupFinished) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Shutting down Navigator..."); // NOI18N
                }

                final Collection<? extends ShutdownHook> hooks = Lookup.getDefault().lookupAll(ShutdownHook.class);

                for (final ShutdownHook hook : hooks) {
                    hook.applicationFinished();
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Shutting down Navigator... FINISHED"); // NOI18N
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ClosingListener extends WindowAdapter {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when the user attempts to close the window from the window's system menu. If the program does not
         * explicitly hide or dispose the window while processing this event, the window close operation will be
         * cancelled.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void windowClosing(final WindowEvent e) {
            if (exceptionManager.showExitDialog(NavigatorX.this)) {
                dispose();
                if (LOG.isInfoEnabled()) {
                    LOG.info("closing navigator"); // NOI18N
                }
                System.exit(0);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class CustomTitleProvider implements DockingWindowTitleProvider {

        //~ Methods ------------------------------------------------------------

        @Override
        public String getTitle(final DockingWindow window) {
            if (window.getChildWindowCount() > 0) {
                final List<String> names = new ArrayList<String>();

                for (int i = 0; i < window.getChildWindowCount(); ++i) {
                    names.addAll(getTitleNames(window.getChildWindow(i)));
                }

                Collections.sort(names);

                StringBuilder builder = null;
                for (final String name : names) {
                    if (builder == null) {
                        builder = new StringBuilder();
                    } else {
                        builder.append("_");
                    }
                    builder.append(name);
                }

                if (builder != null) {
                    return " " + getTitleByKey(builder.toString());
                } else {
                    // should never happen
                    return " " + SimpleDockingWindowTitleProvider.INSTANCE.getTitle(window);
                }
            } else {
                return " " + SimpleDockingWindowTitleProvider.INSTANCE.getTitle(window);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   window  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private List<String> getTitleNames(final DockingWindow window) {
            final List<String> names = new ArrayList<String>();

            if (window.getChildWindowCount() > 0) {
                for (int i = 0; i < window.getChildWindowCount(); ++i) {
                    names.addAll(getTitleNames(window.getChildWindow(i)));
                }

                return names;
            } else {
                return Collections.nCopies(1, SimpleDockingWindowTitleProvider.INSTANCE.getTitle(window));
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   key  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private String getTitleByKey(final String key) {
            if (titleNames != null) {
                return titleNames.getProperty(key, key);
            } else {
                return key;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class CustomMapDnDListener implements MapDnDListener {

        //~ Instance fields ----------------------------------------------------

// private ShowObjectsWaitDialog showObjectsWaitDialog = new ShowObjectsWaitDialog(NavigatorX.this, false);
        DataFlavor fromCapabilityWidget = new DataFlavor(
                DataFlavor.javaJVMLocalObjectMimeType,
                "SelectionAndCapabilities");                                                                  // NOI18N
        DataFlavor fromNavigatorNode = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class="       // NOI18N
                        + DefaultMetaTreeNode.class.getName(),
                "a DefaultMetaTreeNode");                                                                     // NOI18N
        DataFlavor fromNavigatorCollection = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" // NOI18N
                        + java.util.Collection.class.getName(),
                "a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects");     // NOI18N

        //~ Methods ------------------------------------------------------------

        @Override
        public void dropOnMap(final MapDnDEvent mde) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("drop on map"); // NOI18N
            }

            if (mde.getDte() instanceof DropTargetDropEvent) {
                final DropTargetDropEvent dtde = (DropTargetDropEvent)mde.getDte();

                if (dtde.getTransferable().isDataFlavorSupported(fromCapabilityWidget)) {
                    layers.drop(dtde);
                } else if (dtde.getTransferable().isDataFlavorSupported(fromNavigatorNode)
                            && dtde.getTransferable().isDataFlavorSupported(fromNavigatorCollection)) {
                    // Drop von MetaObjects
                    try {
                        final Object object = dtde.getTransferable().getTransferData(fromNavigatorCollection);

                        if (object instanceof Collection) {
                            final Collection c = (Collection)object;
                            invoke(c, false);
                        }
                    } catch (Throwable t) {
                        LOG.fatal("Error on drop", t); // NOI18N
                    }
                } else if (DnDUtils.isFilesOrUriList(dtde)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        final List<File> data = DnDUtils.getFilesFrom(dtde);

                        final MultiHashMap filesPerDropHandlerMap = new MultiHashMap();
                        if (data != null) {
                            for (final File file : data) {
                                final MappingComponentDropHandler dropHandler = MappingComponentDropHandlerRegistry
                                            .getInstance().getDropHandler(file);
                                if (dropHandler != null) {
                                    filesPerDropHandlerMap.put(dropHandler, file);
                                    if (dropHandler instanceof LayerWidgetProvider) {
                                        ((LayerWidgetProvider)dropHandler).setLayerWidget(layers);
                                    }
                                }
                            }
                        }
                        if (!filesPerDropHandlerMap.isEmpty()) {
                            for (final MappingComponentDropHandler dropHandler
                                        : (Set<MappingComponentDropHandler>)filesPerDropHandlerMap.keySet()) {
                                final Collection<File> files = filesPerDropHandlerMap.getCollection(dropHandler);
                                new SwingWorker<Void, Void>() {

                                        @Override
                                        protected Void doInBackground() throws Exception {
                                            dropHandler.dropFiles(files);
                                            return null;
                                        }
                                    }.execute();
                            }
                        }
                    } catch (final Exception ex) {
                        LOG.error("Failure during drag & drop opertation", ex); // NOI18N
                    }
                } else {
                    JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(mapC),
                        org.openide.util.NbBundle.getMessage(
                            NavigatorX.class,
                            "CismapPlugin.dropOnMap(MapDnDEvent).JOptionPane.message")); // NOI18N
                    LOG.error("Unable to process the datatype." + dtde.getTransferable().getTransferDataFlavors()[0]); // NOI18N
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   nodes     DOCUMENT ME!
         * @param   editable  DOCUMENT ME!
         *
         * @throws  Exception  DOCUMENT ME!
         */
        public synchronized void invoke(final Collection<DefaultMetaTreeNode> nodes, final boolean editable)
                throws Exception {
            LOG.info("invoke shows objects in the map"); // NOI18N

            final Runnable showWaitRunnable = new Runnable() {

                    @Override
                    public void run() {
//                        StaticSwingTools.showDialog(showObjectsWaitDialog);

                        final SwingWorker<List<Feature>, Void> addToMapWorker = new SwingWorker<List<Feature>, Void>() {

                                private Map<DefaultMetaTreeNode, Feature> tmpFeaturesInMap = null;
                                private Map<Feature, DefaultMetaTreeNode> tmpFeaturesInMapReverse = null;

                                @Override
                                protected List<Feature> doInBackground() throws Exception {
                                    Thread.currentThread().setName("ShowObjectsMethod addToMapWorker");
                                    final Iterator<DefaultMetaTreeNode> mapIter = featuresInMap.keySet().iterator();

                                    while (mapIter.hasNext()) {
                                        final DefaultMetaTreeNode node = mapIter.next();
                                        final Feature f = featuresInMap.get(node);

                                        if (!mapC.getFeatureCollection().isHoldFeature(f)) {
                                            mapIter.remove();
                                            featuresInMapReverse.remove(f);
                                        }
                                    }

                                    final List<Feature> features = new ArrayList<Feature>();

                                    for (final DefaultMetaTreeNode node : nodes) {
                                        final MetaObjectNode mon = ((ObjectTreeNode)node).getMetaObjectNode();
                                        // TODO: Check4CashedGeomAndLightweightJson
                                        MetaObject mo = mon.getObject();

                                        if (mo == null) {
                                            mo = ((ObjectTreeNode)node).getMetaObject();
                                        }

                                        final CExtContext context = new CExtContext(
                                                CExtContext.CTX_REFERENCE,
                                                mo.getBean());
                                        // there always is a default final MapVisualisationProvider mvp =
                                        // CExtManager.getInstance() .getExtension(MapVisualisationProvider.class,
                                        // context);
                                        //
                                        // final Feature feature = mvp.getFeature(mo.getBean()); if (feature == null) {
                                        // // no map visualisation available, ignore continue; }
                                        //
                                        // feature.setEditable(editable);
                                        //
                                        // final List<Feature> allFeaturesToAdd; if (feature instanceof FeatureGroup) {
                                        // final FeatureGroup fg = (FeatureGroup)feature; allFeaturesToAdd = new
                                        // ArrayList<Feature>(FeatureGroups.expandAll(fg)); } else { allFeaturesToAdd
                                        // = Arrays.asList(feature); }
                                        //
                                        // if (LOG.isDebugEnabled()) { LOG.debug("allFeaturesToAdd:" +
                                        // allFeaturesToAdd); // NOI18N }
                                        //
                                        // if (!(featuresInMap.containsValue(feature))) {
                                        // features.addAll(allFeaturesToAdd);
                                        //
                                        // node -> masterfeature featuresInMap.put(node, feature);
                                        //
                                        // for (final Feature f : allFeaturesToAdd) { // master and all subfeatures ->
                                        // node featuresInMapReverse.put(f, node); } if (LOG.isDebugEnabled()) {
                                        // LOG.debug("featuresInMap.put(node,cidsFeature):" + node + "," // NOI18Ns +
                                        // feature); } }
                                    }
                                    tmpFeaturesInMap = new HashMap<DefaultMetaTreeNode, Feature>(featuresInMap);
                                    tmpFeaturesInMapReverse = new HashMap<Feature, DefaultMetaTreeNode>(
                                            featuresInMapReverse);

                                    return features;
                                }

                                @Override
                                protected void done() {
                                    try {
//                                        showObjectsWaitDialog.setVisible(false);
                                        final List<Feature> features = get();

                                        mapC.getFeatureLayer().setVisible(true);
                                        mapC.getFeatureCollection().substituteFeatures(features);
                                        featuresInMap.clear();
                                        featuresInMap.putAll(tmpFeaturesInMap);
                                        featuresInMapReverse.clear();
                                        featuresInMapReverse.putAll(tmpFeaturesInMapReverse);

                                        if (!mapC.isFixedMapExtent()) {
                                            mapC.zoomToFeatureCollection(mapC.isFixedMapScale());
                                        }
                                    } catch (final InterruptedException e) {
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug(e, e);
                                        }
                                    } catch (final Exception e) {
                                        LOG.error("Error while displaying objects:", e); // NOI18N
                                    }
                                }
                            };
                        CismetThreadPool.execute(addToMapWorker);
                    }
                };

            if (EventQueue.isDispatchThread()) {
                showWaitRunnable.run();
            } else {
                EventQueue.invokeLater(showWaitRunnable);
            }
        }

        @Override
        public void dragOverMap(final MapDnDEvent mde) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class WindowAction extends AbstractAction implements CidsUiAction {

        //~ Instance fields ----------------------------------------------------

        private final String viewId;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new WindowAction object.
         *
         * @param  viewId  DOCUMENT ME!
         */
        public WindowAction(final String viewId) {
            this.viewId = viewId;

            if (viewMap.getView(viewId).getIcon() != null) {
                putValue(SMALL_ICON, viewMap.getView(viewId).getIcon());
            }
            putValue(NAME, viewMap.getView(viewId).getTitle());
            putValue(CidsUiAction.CIDS_ACTION_KEY, viewId + "WindowAction");
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            select(viewId);
        }
    }
}
