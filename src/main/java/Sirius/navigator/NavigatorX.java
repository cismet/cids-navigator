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
import Sirius.navigator.config.TitleOrientationConfiguration;
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
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
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
import Sirius.navigator.ui.ShowObjectsInGuiMethod;
import Sirius.navigator.ui.Windows;
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
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;
import Sirius.server.newuser.UserGroup;
import Sirius.server.newuser.permission.Permission;
import Sirius.server.newuser.permission.PermissionHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.title.DockingWindowTitleProvider;
import net.infonode.docking.title.SimpleDockingWindowTitleProvider;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.PropertiesUtil;
import net.infonode.docking.util.StringViewMap;
import net.infonode.gui.componentpainter.AlphaGradientComponentPainter;
import net.infonode.gui.mouse.MouseButtonListener;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapListener;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.types.DirectionProperty;
import net.infonode.tabbedpanel.TabAreaVisiblePolicy;
import net.infonode.tabbedpanel.TabLayoutPolicy;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.util.Direction;
import net.infonode.util.ValueChange;

import org.apache.commons.collections.MultiHashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;

import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.prefs.Preferences;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
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
import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.attributetable.AttributeTable;
import de.cismet.cismap.commons.gui.attributetable.AttributeTableFactory;
import de.cismet.cismap.commons.gui.attributetable.AttributeTableListener;
import de.cismet.cismap.commons.gui.capabilitywidget.CapabilityWidget;
import de.cismet.cismap.commons.gui.featurecontrolwidget.FeatureControl;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.layerwidget.LayerWidget;
import de.cismet.cismap.commons.gui.layerwidget.LayerWidgetProvider;
import de.cismet.cismap.commons.gui.layerwidget.ThemeLayerWidget;
import de.cismet.cismap.commons.gui.options.CapabilityWidgetOptionsPanel;
import de.cismet.cismap.commons.gui.overviewwidget.OverviewComponent;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.SelectionListener;
import de.cismet.cismap.commons.gui.shapeexport.ShapeExport;
import de.cismet.cismap.commons.interaction.CismapBroker;
import de.cismet.cismap.commons.interaction.MapDnDListener;
import de.cismet.cismap.commons.interaction.events.MapDnDEvent;
import de.cismet.cismap.commons.rasterservice.georeferencing.RasterGeoReferencingBackend;
import de.cismet.cismap.commons.util.DnDUtils;
import de.cismet.cismap.commons.util.SelectionManager;
import de.cismet.cismap.commons.wfsforms.AbstractWFSForm;
import de.cismet.cismap.commons.wfsforms.WFSFormFactory;

import de.cismet.commons.gui.protocol.ProtocolHandler;
import de.cismet.commons.gui.protocol.ProtocolPanel;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.layout.WrapLayout;

import de.cismet.lookupoptions.gui.OptionsClient;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;

import de.cismet.netutil.Proxy;

import de.cismet.remote.RESTRemoteControlStarter;

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
import de.cismet.tools.gui.WaitingDialogThread;
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
public class NavigatorX extends javax.swing.JFrame implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(NavigatorX.class);
    private static final ResourceManager resourceManager = ResourceManager.getManager();
    public static final String NAVIGATOR_HOME_DIR = ".navigator"
                + JnlpSystemPropertyHelper.getProperty("directory.extension", "");
    public static final String NAVIGATOR_HOME = System.getProperty("user.home") + System.getProperty("file.separator")
                + NAVIGATOR_HOME_DIR + System.getProperty("file.separator");
    public static final String DEFAULT_LAYOUT = NAVIGATOR_HOME + "navigatorx.layout"; // NOI18N
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
    private NavigatorXSplashScreen splashScreen;
    private String title;
    private RootWindow rootWindow;
    private StringViewMap viewMap = new StringViewMap();
    private Properties titleNames = new Properties();
    private int currentId = 0;
    private MappingComponent mapC = null;
    private FeatureControl featureControl = null;
    private WFSFormFactory wfsFormFactory;
    private String home = System.getProperty("user.home");    // NOI18N
    private String fs = System.getProperty("file.separator"); // NOI18N
    private String cismapDirectory = home + fs + ".cismap";   // NOI18N
    private LayerWidget layers = null;
    private final Map<DefaultMetaTreeNode, Feature> featuresInMap = new HashMap<DefaultMetaTreeNode, Feature>();
    private final Map<Feature, DefaultMetaTreeNode> featuresInMapReverse = new HashMap<Feature, DefaultMetaTreeNode>();
    private List<ConfiguredToolBar> toolbars;
    private Map<String, Action> windowActions = new HashMap<String, Action>();
    private ShowObjectsInGuiMethod showObjectMethod = null;
    private HashMap<String, View> attributeTableMap = new HashMap<String, View>();
    private int progress = 200;
    private final ConnectionContext connectionContext = ConnectionContext.create(
            ConnectionContext.Category.OTHER,
            getClass().getSimpleName());
    private Windows windowsConfig = null;
    private String[] windowsPriority = null;
    private String applicationKey = "";
    private Map<TabWindow, TreeListener> tabListeners = new WeakHashMap<TabWindow, TreeListener>();
    private TitleOrientationConfiguration titleOrientation = new TitleOrientationConfiguration();

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
    public NavigatorX(final ProgressObserver progressObserver, final NavigatorXSplashScreen splashScreen)
            throws Exception {
        this.progressObserver = progressObserver;
        this.splashScreen = splashScreen;

        initComponents();
        this.propertyManager = PropertyManager.getManager();
        this.applicationKey = System.getProperty("jnlp.applicationKey", "");

        this.preferences = Preferences.userNodeForPackage(this.getClass());

        this.exceptionManager = ExceptionManager.getManager();
        StaticSwingTools.tweakUI();
        this.init();
        CismapBroker.getInstance().setUseInternalDb(true);
        mapC.setReadOnly(false);
        mapC.unlock();
        CismapBroker.getInstance().addMapDnDListener(new CustomMapDnDListener());
        CismapBroker.getInstance()
                .addActiveLayerListener(RasterGeoReferencingBackend.getInstance().getActiveLayerListenerHandler());

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
            initWindowConfig();
            initUI();
            initWidgets();
            addWfsForms();
            initStatusBar();
            initWindow();
            initSearch();

            final Collection<? extends ShowObjectsInGuiMethod> showObjectMethods = Lookup.getDefault()
                        .lookupAll(ShowObjectsInGuiMethod.class);

            if ((showObjectMethods != null) && (showObjectMethods.size() > 0)) {
                showObjectMethod = showObjectMethods.iterator().next();
                showObjectMethod.init();
            }
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
        initAttributeTable();
        final SelectionListener sl = (SelectionListener)mapC.getInputEventListener().get(MappingComponent.SELECT);

        if (sl != null) {
            sl.setFeaturesFromServicesSelectable(true);
        }
    }

    /**
     * Loads the window configuration from the corresponding JSon file.
     */
    private void initWindowConfig() {
        try {
            final User usr = SessionManager.getSession().getUser();
            String allowedWindowNamesFile = SessionManager.getProxy()
                        .getConfigAttr(usr, "navigatorx." + applicationKey + ".allowedWindows", getConnectionContext());

            if ((allowedWindowNamesFile == null) || allowedWindowNamesFile.equals("")) {
                allowedWindowNamesFile = System.getProperty(
                        "jnlp.allowedWindows",
                        "/Sirius/navigator/windows.json");
            }

            if ((allowedWindowNamesFile != null) && !allowedWindowNamesFile.equals("")) {
                final ObjectMapper mapper = new ObjectMapper();

                BufferedReader reader = null;
                final InputStream is = this.getClass().getResourceAsStream(allowedWindowNamesFile);

                if (is != null) {
                    try {
                        reader = new BufferedReader(new InputStreamReader(is));
                        windowsConfig = mapper.readValue(reader, Windows.class);
                        final String[] windowArray = windowsConfig.getAllowedWindows();
                        windowsPriority = new String[windowArray.length];
                        System.arraycopy(windowArray, 0, windowsPriority, 0, windowArray.length);

                        if ((windowsConfig != null) && (windowsConfig.getAllowedWindows() != null)) {
                            Arrays.sort(windowsConfig.getAllowedWindows());
                        }
                    } finally {
                        is.close();

                        if (reader != null) {
                            reader.close();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        NbBundle.getMessage(
                            NavigatorX.class,
                            "NavigatorX.initWindowConfig.fileNotFound.message",
                            allowedWindowNamesFile),
                        NbBundle.getMessage(NavigatorX.class, "NavigatorX.initWindowConfig.fileNotFound.title"),
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            LOG.warn("Cannot load windows property file", e);
        }

        try {
            final User usr = SessionManager.getSession().getUser();
            String titleNamesFile = SessionManager.getProxy()
                        .getConfigAttr(usr, "navigatorx." + applicationKey + ".titleName", getConnectionContext());

            if ((titleNamesFile == null) || titleNamesFile.equals("")) {
                titleNamesFile = System.getProperty(
                        "jnlp.titleName",
                        "/Sirius/navigator/titleNames.properties");
            }

            final InputStream is = this.getClass().getResourceAsStream(titleNamesFile);

            if (is != null) {
                titleNames.load(is);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    NbBundle.getMessage(
                        NavigatorX.class,
                        "NavigatorX.initWindowConfig.fileNotFound.message",
                        titleNamesFile),
                    NbBundle.getMessage(NavigatorX.class, "NavigatorX.initWindowConfig.fileNotFound.title"),
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (Exception e) {
            LOG.warn("Cannot load titles property file", e);
        }
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
        rootWindow.addListener(new DockingWindowAdapter() {

                @Override
                public void windowAdded(final DockingWindow addedToWindow, final DockingWindow addedWindow) {
                    if (addedWindow instanceof TabWindow) {
                        final TabWindow tab = (TabWindow)addedWindow;
                        TreeListener li = tabListeners.get(tab);

                        if (li == null) {
                            li = new TreeListener(tab);
                            tabListeners.put(tab, li);
                        }

                        tab.getTabWindowProperties().getTabbedPanelProperties().getMap().removeTreeListener(li);
                        tab.getTabWindowProperties().getTabbedPanelProperties().getMap().addTreeListener(li);
                    }
                }

                @Override
                public void windowRemoved(final DockingWindow removedFromWindow, final DockingWindow removedWindow) {
                    if (removedWindow instanceof TabWindow) {
                        final TabWindow tab = (TabWindow)removedWindow;
                        final TreeListener li = tabListeners.get(tab);

                        if (li != null) {
                            tab.getTabWindowProperties().getTabbedPanelProperties().getMap().removeTreeListener(li);
                            tabListeners.remove(tab);
                        }
                    }
                }
            });

        final DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(
            theme.getRootWindowProperties());
//        final RootWindowProperties titleBarStyleProperties = PropertiesUtil.createTitleBarStyleRootWindowProperties();
//        rootWindow.getRootWindowProperties().addSuperObject(
//            titleBarStyleProperties);
        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);
//        final AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(
//                java.awt.SystemColor.inactiveCaptionText,
//                java.awt.SystemColor.activeCaptionText,
//                java.awt.SystemColor.activeCaptionText,
//                java.awt.SystemColor.inactiveCaptionText);
//        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);
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
    private void initAttributeTable() {
        AttributeTableFactory.getInstance().setMappingComponent(mapC);
        AttributeTableFactory.getInstance().setAttributeTableListener(new AttributeTableListener() {

                @Override
                public void showAttributeTable(final AttributeTable table,
                        final String id,
                        final String name,
                        final String tooltip) {
                    View view = attributeTableMap.get(id);

                    table.setExportEnabled(true);

                    if (view != null) {
                        view.restore();
                        view.requestFocusInWindow();
                        final Object parentWindow = view.getWindowParent();

                        if (parentWindow instanceof TabWindow) {
                            final TabWindow tab = (TabWindow)parentWindow;
                            final int tabIndex = tab.getChildWindowIndex(view);

                            if (tabIndex != -1) {
                                tab.setSelectedTab(tabIndex);
                            }
                        }
                    } else {
                        view = new View(name, null, table);
                        addAttributeTableWindowListener(view, table);
                        viewMap.addView(id, view);
                        attributeTableMap.put(id, view);
                        final TabWindow tw = (TabWindow)viewMap.getView("Map").getWindowParent();
                        tw.addTab(view, tw.getChildWindowCount());
                        view.restore();
                        SelectionManager.getInstance().addConsideredAttributeTable(table);
                    }
                }

                @Override
                public void changeName(final String id, final String name) {
                    final View view = attributeTableMap.get(id);

                    if (view != null) {
                        view.getViewProperties().setTitle(name);
                    }
                }

                @Override
                public void processingModeChanged(final AbstractFeatureService service, final boolean active) {
                    SelectionManager.getInstance().switchProcessingMode(service);
                }

                @Override
                public void closeAttributeTable(final AbstractFeatureService service) {
                    final View attributeTableView = attributeTableMap.remove(AttributeTableFactory.createId(service));

                    if (attributeTableView != null) {
                        attributeTableView.close();
                    }
                }

                @Override
                public AttributeTable getAttributeTable(final String id) {
                    final View view = attributeTableMap.get(id);

                    if (view != null) {
                        final Component c = view.getComponent();

                        if (c instanceof AttributeTable) {
                            return (AttributeTable)c;
                        }
                    }

                    return null;
                }

                @Override
                public void switchProcessingMode(final AbstractFeatureService service, final String id) {
                    if (!NavigatorX.this.switchProcessingMode(service, false)) {
//                        setTabWindow();
                        final int index = -1;

//                        if ((tabWindow != null) && (tabWindow.getSelectedWindow() != null)) {
//                            index = tabWindow.getChildWindowIndex(tabWindow.getSelectedWindow());
//                        }

                        AttributeTableFactory.getInstance().showAttributeTable(service);

//                        if ((index != -1) && (index < tabWindow.getChildWindowCount())) {
//                            tabWindow.setSelectedTab(index);
//                        }

                        final WaitingDialogThread<Void> wdt = new WaitingDialogThread<Void>(
                                NavigatorX.this,
                                true,
                                "Starte Edit mode",
//                                NbBundle.getMessage(
//                                    CismapPlugin.class,
//                                    "WatergisApp.EditModeMenuItem.actionPerformed().wait"),
                                null,
                                200) {

                                @Override
                                protected Void doInBackground() throws Exception {
                                    final View view = attributeTableMap.get(id);

                                    if (view != null) {
                                        final Component c = view.getComponent();

                                        if (c instanceof AttributeTable) {
                                            final AttributeTable attrTable = (AttributeTable)c;

                                            while (attrTable.isLoading()) {
                                                Thread.sleep(100);
                                            }
                                        }
                                    }

                                    return null;
                                }

                                @Override
                                protected void done() {
                                    NavigatorX.this.switchProcessingMode(service, false);
                                }
                            };

                        wdt.start();
                    }
                }
            });
    }

    /**
     * Adds the window listener to the given view.
     *
     * @param  view   the view to add the listener
     * @param  table  the AttributeTable that is used inside the view
     */
    private void addAttributeTableWindowListener(final View view, final AttributeTable table) {
        view.addListener(new DockingWindowAdapter() {

                @Override
                public void windowClosing(final DockingWindow window) throws OperationAbortedException {
                    final boolean disposeCompleted = table.dispose();

                    if (!disposeCompleted) {
                        throw new OperationAbortedException();
                    }
                }

                @Override
                public void windowClosed(final DockingWindow window) {
                    disposeTable();
                }

                private void disposeTable() {
                    view.removeListener(this);
                    if (view.getParent() != null) {
                        view.getParent().remove(view);
                    }
                    viewMap.removeView("Attributtabelle " + table.getFeatureService().getName());
                    attributeTableMap.remove(AttributeTableFactory.createId(table.getFeatureService()));

                    SelectionManager.getInstance().removeConsideredAttributeTable(table);

                    // The view is not removed from the root window and this will cause that the layout cannot be saved
                    // when the application will be closed. So rootWindow.removeView(view) must be invoked. But without
                    // the invocation of view.close(), the invocation of rootWindow.removeView(view) will do nothing To
                    // avoid an infinite loop, view.removeListener(this) must be invoked before view.close();
                    view.close();
                    rootWindow.removeView(view);
                }
            });
    }

    /**
     * switches the processing mode of the given service.
     *
     * @param   service    DOCUMENT ME!
     * @param   forceSave  if true, the changed data will be saved without confirmation
     *
     * @return  true, if the processing mode was switched
     */
    public boolean switchProcessingMode(final AbstractFeatureService service, final boolean forceSave) {
        final View view = attributeTableMap.get(AttributeTableFactory.createId(service));

        if (view != null) {
            final Component c = view.getComponent();

            if (c instanceof AttributeTable) {
                final AttributeTable attrTable = (AttributeTable)c;

                attrTable.changeProcessingMode(forceSave);
                return true;
            }
        }

        return false;
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
        final User usr = SessionManager.getSession().getUser();
        String configFile = SessionManager.getProxy()
                    .getConfigAttr(usr, "navigatorx." + applicationKey + ".menuConfigFile", getConnectionContext());

        if ((configFile == null) || configFile.equals("")) {
            configFile = System.getProperty("jnlp.menuConfigFile", "/Sirius/navigator/MenuConfig.json");
        }

        final InputStream is = this.getClass().getResourceAsStream(configFile);

        if (is == null) {
            JOptionPane.showMessageDialog(
                this,
                NbBundle.getMessage(NavigatorX.class, "NavigatorX.initWindowConfig.fileNotFound.message", configFile),
                NbBundle.getMessage(NavigatorX.class, "NavigatorX.initWindowConfig.fileNotFound.title"),
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        final ActionConfiguration config = new ActionConfiguration(is, windowActions, getConnectionContext());

        config.configureMainMenu(menuBar);
        toolbars = config.getToolbars();
        double maxHeight = 0;

        if ((toolbars != null) && !toolbars.isEmpty()) {
            toolbarPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 1, 0));

            for (final ConfiguredToolBar toolbar : toolbars) {
                if (maxHeight < toolbar.getToolbar().getPreferredSize().getHeight()) {
                    maxHeight = toolbar.getToolbar().getPreferredSize().getHeight();
                }
            }

            final double finalHeight = maxHeight;
            for (final ConfiguredToolBar toolbar : toolbars) {
                toolbar.getToolbar().layout();
                toolbar.getToolbar()
                        .setMinimumSize(new Dimension(
                                (int)toolbar.getToolbar().getMinimumSize().getWidth(),
                                (int)maxHeight));
                toolbar.getToolbar().setFloatable(false);
                toolbarPanel.add(toolbar.getToolbar());
                toolbar.getToolbar().addComponentListener(new ComponentAdapter() {

                        boolean resize = false;

                        @Override
                        public void componentResized(final ComponentEvent e) {
                            if (!resize && ((int)((JToolBar)e.getSource()).getSize().getHeight() != (int)finalHeight)) {
                                resize = true;
                                ((JToolBar)e.getSource()).setSize(
                                    (int)((JToolBar)e.getSource()).getSize().getWidth(),
                                    (int)finalHeight);
                                ((JToolBar)e.getSource()).setPreferredSize(
                                    new Dimension(
                                        (int)((JToolBar)e.getSource()).getSize().getWidth(),
                                        (int)finalHeight));
                            }
                            resize = false;
                        }
                    });
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
            if ((windowsConfig != null) && (windowsConfig.getAllowedWindows() != null)
                        && (Arrays.binarySearch(windowsConfig.getAllowedWindows(), window.getClass().getName()) < 0)) {
                continue;
            }
            if (window instanceof ConnectionContextStore) {
                ((ConnectionContextStore)window).initWithConnectionContext(getConnectionContext());
            }
            if (window.getPermissionString().equals(GUIWindow.NO_PERMISSION)) {
                progress = ((progress < 500) ? (progress + 30) : progress);
                progressObserver.setProgress(
                    progress,
                    org.openide.util.NbBundle.getMessage(NavigatorX.class,
                        "NavigatorX.progressObserver.loadWindow")); // NOI18N
                final String titleName = ((window.getViewTitle() != null) ? window.getViewTitle() : "");
                if (window instanceof MetaCatalogueTree) {
                    final RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots(),
                            getConnectionContext());
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
                        searchResultsTree = new PostfilterEnabledSearchResultsTree(getConnectionContext());
                    } else {
                        searchResultsTree = new SearchResultsTree(getConnectionContext());
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
                    SelectionManager.getInstance().init();
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
                    featureControl = (FeatureControl)window.getGuiComponent();
                    featureControl.init(mapC);
                    mapC.getFeatureCollection().addFeatureCollectionListener(featureControl);
                    CismapBroker.getInstance().addMapBoundsListener(featureControl);
                } else if (window.getGuiComponent() instanceof OverviewComponent) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    final OverviewComponent oMap = (OverviewComponent)window.getGuiComponent();
                    cismapConfigurationManager.addConfigurable(oMap);
                    oMap.setMasterMap(mapC);
                    oMap.getOverviewMap().unlock();
                } else if (window.getGuiComponent() instanceof ThemeLayerWidget) {
                    view = new View(window.getViewTitle(),
                            window.getViewIcon(),
                            window.getGuiComponent());
                    viewId = getUniqueId(window.getClass().getName());
                    viewMap.addView(viewId, view);
                    final ThemeLayerWidget layerWidget = (ThemeLayerWidget)window.getGuiComponent();
                    layerWidget.setMappingModel((ActiveLayerModel)mapC.getMappingModel());
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
        final DefaultPopupMenuListener cataloguePopupMenuListener = new DefaultPopupMenuListener(popupMenu);
        if (catalogueTree != null) {
            catalogueTree.addTreeSelectionListener(catalogueSelectionListener);
            catalogueTree.addMouseListener(cataloguePopupMenuListener);
            catalogueTree.addComponentListener(new CatalogueActivationListener(
                    catalogueTree,
                    attributeViewer,
                    description));
        }
        if (searchResultsTree != null) {
            searchResultsTree.addTreeSelectionListener(catalogueSelectionListener);
            searchResultsTree.addMouseListener(cataloguePopupMenuListener);
            searchResultsTree.addComponentListener(new CatalogueActivationListener(
                    searchResultsTree,
                    attributeViewer,
                    description));
        }
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

        cismapConfigurationManager.configure(wfsFormFactory);

        for (final String key : keySet) {
            // View
            if ((windowsConfig != null) && (windowsConfig.getAllowedWindows() != null)
                        && (Arrays.binarySearch(windowsConfig.getAllowedWindows(), key) < 0)) {
                continue;
            }
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
     * @param  id  DOCUMENT ME!
     */
    public void select(final String id) {
        final View v = viewMap.getView(id);

        if (v != null) {
            if (!v.isClosable()) {
                if (v.isRestorable()) {
                    v.restore();
                } else {
                    final TabWindow tabWindow = getTabWindowForNewView(rootWindow.getWindow());

                    if (tabWindow == null) {
                        LOG.error("No suitable tab window found");
                    } else {
                        tabWindow.addTab(v);
                        v.restore();
                    }
                }
            }
            v.restoreFocus();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   window  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private TabWindow getTabWindowForNewView(final DockingWindow window) {
        if (window instanceof TabWindow) {
            return (TabWindow)window;
        } else if (window instanceof SplitWindow) {
            final TabWindow windowLeft = getTabWindowForNewView(((SplitWindow)window).getLeftWindow());
            final TabWindow windowRight = getTabWindowForNewView(((SplitWindow)window).getRightWindow());

            if (windowLeft != null) {
                return windowLeft;
            } else if (windowRight != null) {
                return windowRight;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void showOrHide(final String id) {
        final View v = viewMap.getView(id);

        if (v != null) {
            if (v.isClosable()) {
                v.close();
            } else {
                v.restore();
            }
//            v.restoreFocus();
        }
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
                if (windowSearch instanceof ConnectionContextStore) {
                    ((ConnectionContextStore)windowSearch).initWithConnectionContext(getConnectionContext());
                }
                if ((windowsConfig != null) && (windowsConfig.getAllowedWindows() != null)
                            && (Arrays.binarySearch(
                                    windowsConfig.getAllowedWindows(),
                                    windowSearch.getClass().getName()) < 0)) {
                    continue;
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

            final String prefix = "cismapconfig:";    // NOI18N
            final String username = user.getName();
            Collection<UserGroup> groups;
            if (userGroup != null) {
                final ArrayList<UserGroup> onlyOne = new ArrayList<UserGroup>();
                onlyOne.add(userGroup);
                groups = onlyOne;
            } else {
                groups = user.getPotentialUserGroups();
            }
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
                        proxyConfig,
                        propertyManager.isCompressionEnabled());
        ConnectionSession session = null;
        ConnectionProxy proxy = null;

        progressObserver.setProgress(
            50,
            org.openide.util.NbBundle.getMessage(NavigatorX.class, "NavigatorX.progressObserver.message_50")); // NOI18N

        // autologin with jnlp parameter
        final String username = System.getProperty("jnlp.username", null);
        final String password = System.getProperty("jnlp.password", null);
        final String userDomain = System.getProperty("jnlp.userDomain", null);
        final String userGroupDomain = System.getProperty("jnlp.userGroupDomain", null);
        final String usergroup = System.getProperty("jnlp.userGroup", null);
        boolean autoLogin = false;

        if (username != null) {
            propertyManager.getConnectionInfo().setUsername(username);
        }
        if (userDomain != null) {
            propertyManager.getConnectionInfo().setUserDomain(userDomain);
        }
        propertyManager.getConnectionInfo().setUsergroupDomain(userGroupDomain);
        propertyManager.getConnectionInfo().setUsergroup(usergroup);

        if ((username != null) && (password != null) && (userDomain != null)) {
            try {
                propertyManager.getConnectionInfo().setPassword(password);
                session = ConnectionFactory.getFactory()
                            .createSession(
                                    connection,
                                    propertyManager.getConnectionInfo(),
                                    true,
                                    getConnectionContext());
                proxy = ConnectionFactory.getFactory()
                            .createProxy(propertyManager.getConnectionProxyClass(), session, getConnectionContext());
                SessionManager.init(proxy);
                autoLogin = true;
            } catch (UserException uexp) {
                LOG.error("login from jnlp parameters failed", uexp); // NOI18N
                session = null;
            }
        }

        // autologin = false || autologin failed
        if ((!autoLogin && !propertyManager.isAutoLogin()) || (session == null)) {
            String userGroupWithDomain = null;

            if ((usergroup != null) && (userGroupDomain != null)) {
                userGroupWithDomain = usergroup + "@" + userGroupDomain;
            } else if ((usergroup != null) && (userDomain != null)) {
                // if the user group domain is not set, it will be assumed that the user domain is also the domain of
                // the group
                userGroupWithDomain = usergroup + "@" + userDomain;
            }

            if (LOG.isInfoEnabled()) {
                LOG.info("performing login"); // NOI18N
            }
            try {
                session = ConnectionFactory.getFactory()
                            .createSession(
                                    connection,
                                    propertyManager.getConnectionInfo(),
                                    false,
                                    getConnectionContext());
            } catch (UserException uexp) {
            }                                 // should never happen
            proxy = ConnectionFactory.getFactory()
                        .createProxy(propertyManager.getConnectionProxyClass(), session, getConnectionContext());
            SessionManager.init(proxy);
            loginDialog = new LoginDialog(this);
            loginDialog.setDefaultValues(username, userGroupWithDomain, userDomain);
            StaticSwingTools.showDialog(loginDialog);
        }

        PropertyManager.getManager()
                .setEditable(this.hasPermission(
                        SessionManager.getProxy().getClasses(getConnectionContext()),
                        PermissionHolder.WRITEPERMISSION));
        // PropertyManager.getManager().setEditable(true);
        if (LOG.isInfoEnabled()) {
            LOG.info("initConnection(): navigator editor enabled: " + PropertyManager.getManager().isEditable()); // NOI18N
        }
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
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
     * @param  nodes     DOCUMENT ME!
     * @param  editable  DOCUMENT ME!
     */
    public void showObjectInGui(final Collection<DefaultMetaTreeNode> nodes, final boolean editable) {
        try {
            showObjectMethod.invoke(nodes, editable);
        } catch (Exception e) {
            LOG.error("Cannot show object in gui", e);
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
                rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
                rootWindow.getWindowBar(Direction.UP).setEnabled(true);
                rootWindow.invalidate();
                rootWindow.updateUI();
                rootWindow.revalidate();
                rootWindow.repaint();
                rootWindow.doLayout();
                addTabbedPanelListener(rootWindow);
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
     * Show the view title bar dependent on the tab orientation.
     *
     * @param  window  DOCUMENT ME!
     */
    private void addTabbedPanelListener(final DockingWindow window) {
        if (window instanceof TabWindow) {
            final TabWindow tab = (TabWindow)window;
            TreeListener li = tabListeners.get(tab);

            if (li == null) {
                li = new TreeListener(tab);
                tabListeners.put(tab, li);
            }

            final Direction d = tab.getTabWindowProperties().getTabbedPanelProperties().getTabAreaOrientation();
            if (titleOrientation.showTitleForDirection(d)) {
                setupTitleBarStyleProperties(tab, true);
            } else {
                setupTitleBarStyleProperties(tab, false);
            }
            tab.getTabWindowProperties().getTabbedPanelProperties().getMap().removeTreeListener(li);
            tab.getTabWindowProperties().getTabbedPanelProperties().getMap().addTreeListener(li);
        }
        if (window.getChildWindowCount() > 0) {
            for (int i = 0; i < window.getChildWindowCount(); ++i) {
                addTabbedPanelListener(window.getChildWindow(i));
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
            final String layoutFile = System.getProperty(
                    "jnlp.defaultLayout",
                    "/Sirius/navigator/defaultLayout.layout");
            layoutInput = NavigatorX.class.getResourceAsStream(layoutFile);
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
        saveLayout(DEFAULT_LAYOUT, this);
        saveWindowState();

        configurationManager.writeConfiguration();
        cismapConfigurationManager.writeConfiguration();

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

            final NavigatorXSplashScreen navigatorSplashScreen = new NavigatorXSplashScreen(PropertyManager.getManager()
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
     * activate or deactivate the title bar of the child views.
     *
     * @param  window   DOCUMENT ME!
     * @param  visible  true, if the titel bar should be activated
     */
    private void setupTitleBarStyleProperties(final TabWindow window, final boolean visible) {
        for (int i = 0; i < window.getChildWindowCount(); ++i) {
            if (window.getChildWindow(i) instanceof View) {
                ((View)window.getChildWindow(i)).getViewProperties().getViewTitleBarProperties().setVisible(visible);
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class TreeListener implements PropertyMapTreeListener {

        //~ Instance fields ----------------------------------------------------

        TabWindow window;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TreeListener object.
         *
         * @param  window  DOCUMENT ME!
         */
        public TreeListener(final TabWindow window) {
            this.window = window;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyValuesChanged(final Map changes) {
            for (final Object key : changes.keySet()) {
                final Object o = changes.get(key);

                for (final Object prop : ((Map)o).keySet()) {
                    if (prop instanceof DirectionProperty) {
                        final Object valObject = ((Map)o).get(prop);

                        if (valObject instanceof ValueChange) {
                            final ValueChange value = (ValueChange)valObject;
                            final Object newVal = value.getNewValue();

                            if (newVal instanceof Direction) {
                                if (titleOrientation.showTitleForDirection((Direction)newVal)) {
                                    setupTitleBarStyleProperties(window, true);
                                } else {
                                    setupTitleBarStyleProperties(window, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

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
                    String title = getTitleByKey(builder.toString());
                    final String tooltip = createToolTipString(window);
                    setTooltipText(window, tooltip);

                    if (title.equals(builder.toString()) && (window.getChildWindowCount() > 1)) {
                        // no name was found in the titles files
                        title = getDefaultTitle(window);
                    }

                    return " " + title;
                } else {
                    // should never happen
                    final String title = SimpleDockingWindowTitleProvider.INSTANCE.getTitle(window);
                    setTooltipText(window, title);
                    return " " + title;
                }
            } else {
                final String title = SimpleDockingWindowTitleProvider.INSTANCE.getTitle(window);
                setTooltipText(window, title);
                return " " + title;
            }
        }

        /**
         * Set the tooltip text for the given window.
         *
         * @param  window  DOCUMENT ME!
         * @param  text    DOCUMENT ME!
         */
        private void setTooltipText(final DockingWindow window, final String text) {
            window.getWindowProperties()
                    .getTabProperties()
                    .getTitledTabProperties()
                    .getNormalProperties()
                    .setToolTipText(text);
        }

        /**
         * Creates the default title for the given window.
         *
         * @param   window  key DOCUMENT ME!
         *
         * @return  the default title
         */
        private String createToolTipString(final DockingWindow window) {
            final List<DockingWindow> windows = getWindows(window);
            Collections.sort(windows, new WindowComparator());
            final StringBuilder titleString = new StringBuilder("<html>");

            for (int i = 0; i < windows.size(); i++) {
                if (i == 0) {
                    titleString.append(windows.get(i).getTitle());
                } else {
                    titleString.append("<br>").append(windows.get(i).getTitle());
                }
            }

            titleString.append("</html>");
            return titleString.toString();
        }

        /**
         * Creates a list with all window titles.
         *
         * @param   window  DOCUMENT ME!
         *
         * @return  a list with all window titles
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
         * Creates a list with all sub windows.
         *
         * @param   window  DOCUMENT ME!
         *
         * @return  a list with all sub windows
         */
        private List<DockingWindow> getWindows(final DockingWindow window) {
            final List<DockingWindow> names = new ArrayList<DockingWindow>();

            if (window.getChildWindowCount() > 0) {
                for (int i = 0; i < window.getChildWindowCount(); ++i) {
                    names.addAll(getWindows(window.getChildWindow(i)));
                }

                return names;
            } else {
                return Collections.nCopies(1, window);
            }
        }

        /**
         * Get the predefined title from the titles file.
         *
         * @param   key  DOCUMENT ME!
         *
         * @return  the predefined title from the titles file or the given key, if no predefined title exists
         */
        private String getTitleByKey(final String key) {
            if (titleNames != null) {
                final List<String> allTokenFromKey = toList(key);
                String propertyKey = null;
                int hitsForPopertyKes = 0;

                for (final String titleName : titleNames.stringPropertyNames()) {
                    final List<String> allTokenFromPossibleTitle = toList(titleName);
                    int hits = 0;

                    for (final String token : allTokenFromKey) {
                        if (allTokenFromPossibleTitle.contains(token)) {
                            ++hits;
                        }
                    }
                    if ((hits == allTokenFromPossibleTitle.size()) && (hits > hitsForPopertyKes)) {
                        hitsForPopertyKes = hits;
                        propertyKey = titleName;
                    }
                }

                if (propertyKey != null) {
                    return titleNames.getProperty(propertyKey, key);
                } else {
                    return key;
                }
            } else {
                return key;
            }
        }

        /**
         * Creates the default title for the given window.
         *
         * @param   window  key DOCUMENT ME!
         *
         * @return  the default title
         */
        private String getDefaultTitle(final DockingWindow window) {
            final List<DockingWindow> windows = getWindows(window);
            Collections.sort(windows, new WindowComparator());
            final StringBuilder titleString = new StringBuilder();

            for (int i = 0; i < windows.size(); i++) {
                if (i == 0) {
                    String title = windows.get(i).getTitle();
                    if (title.startsWith(" ")) {
                        title = title.substring(1);
                    }
                    titleString.append(title);
                } else if (i == 1) {
                    titleString.append(",").append(windows.get(i).getTitle());
                } else {
                    titleString.append(", ");

                    if (windows.size() > 3) {
                        titleString.append(NbBundle.getMessage(
                                CustomTitleProvider.class,
                                "NavigatorX.CustomTitleProvider.getDefaultTitle",
                                (windows.size() - 2)));
                    } else {
                        titleString.append(NbBundle.getMessage(
                                CustomTitleProvider.class,
                                "NavigatorX.CustomTitleProvider.getDefaultTitle.single",
                                (windows.size() - 2)));
                    }
                    break;
                }
            }

            return titleString.toString();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   title  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private List<String> toList(final String title) {
            final StringTokenizer tokenizer = new StringTokenizer(title, "_");
            final List<String> allToken = new ArrayList<String>();

            while (tokenizer.hasMoreTokens()) {
                allToken.add(tokenizer.nextToken());
            }

            return allToken;
        }

        //~ Inner Classes ------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @version  $Revision$, $Date$
         */
        class WindowComparator implements Comparator<DockingWindow> {

            //~ Methods --------------------------------------------------------

            @Override
            public int compare(final DockingWindow o1, final DockingWindow o2) {
                final Integer index1 = getIndex(o1);
                final Integer index2 = getIndex(o2);

                return index1.compareTo(index2);
            }

            /**
             * DOCUMENT ME!
             *
             * @param   window  DOCUMENT ME!
             *
             * @return  DOCUMENT ME!
             */
            private int getIndex(final DockingWindow window) {
                if (windowsPriority != null) {
                    for (int i = 0; i < windowsPriority.length; ++i) {
                        if (window instanceof View) {
                            final Component c = ((View)window).getComponent();

                            if (c instanceof AbstractWFSForm) {
                                final AbstractWFSForm form = (AbstractWFSForm)c;

                                if (form.getId().equals(windowsPriority[i])) {
                                    return i;
                                }
                            } else if (c.getClass().getName().equals(windowsPriority[i])) {
                                return i;
                            }
                        }
                        if (window.getClass().getName().equals(windowsPriority[i])) {
                            return i;
                        }
                    }
                }

                // this can only happen, when the given window does not
                // exist in the windows file. And this should be impossible.
                // (If the window does not exist in the windows file,
                // then it should not be used in the navigator and so it
                // cannot be asked for the index of this window)
                LOG.error("window not found");
                return 0;
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
                            showObjectMethod.invoke(c, false);
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
