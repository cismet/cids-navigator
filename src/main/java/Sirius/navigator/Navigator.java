package Sirius.navigator;

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.event.CatalogueActivationListener;
import Sirius.navigator.event.CataloguePopupMenuListener;
import Sirius.navigator.event.CatalogueSelectionListener;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.search.dynamic.FormDataBean;
import Sirius.navigator.search.dynamic.SearchDialog;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.dnd.MetaTreeNodeDnDHandler;
import Sirius.navigator.ui.attributes.AttributeViewer;
import Sirius.navigator.ui.attributes.editor.AttributeEditor;
import Sirius.navigator.ui.dialog.LoginDialog;
import Sirius.navigator.ui.progress.ProgressObserver;
import Sirius.navigator.ui.status.MutableStatusBar;
import Sirius.navigator.ui.status.StatusChangeListener;
import Sirius.navigator.ui.tree.MetaCatalogueTree;
import Sirius.navigator.ui.tree.SearchResultsTree;
import Sirius.navigator.ui.tree.SearchResultsTreePanel;
import Sirius.navigator.ui.widget.FloatingFrameConfigurator;
import Sirius.server.newuser.UserException;
import Sirius.server.newuser.permission.*;
import Sirius.server.middleware.types.*;
import de.cismet.cids.editors.NavigatorAttributeEditorGui;
import de.cismet.cids.tools.StaticCidsUtilities;
import de.cismet.tools.CismetThreadPool;
import de.cismet.tools.StaticDebuggingTools;
import de.cismet.tools.gui.CheckThreadViolationRepaintManager;
import de.cismet.tools.gui.EventDispatchThreadHangMonitor;
import de.cismet.tools.gui.Static2DTools;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.*;
import java.util.prefs.*;

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

/**
 *
 * @author  pascal
 */
public class Navigator extends JFrame {

    private final Logger logger;// = Logger.getLogger(Navigator.class);
    private final PropertyManager propertyManager;
    private static final ResourceManager resourceManager = ResourceManager.getManager();
    private final ExceptionManager exceptionManager;
    private final ProgressObserver progressObserver;
    private LoginDialog loginDialog;
    private LayoutedContainer container;
    private MutableMenuBar menuBar;
    private MutableToolBar toolBar;
    private MutableStatusBar statusBar;
    private MutablePopupMenu popupMenu;
    private MetaCatalogueTree metaCatalogueTree;
    private SearchResultsTree searchResultsTree;
    private AttributeViewer attributeViewer;
    private AttributeEditor attributeEditor;
    private SearchDialog searchDialog;
    private Preferences preferences;
    /**
     * Holds value of property disposed.
     */
    private boolean disposed = false;    //InfoNode
    //Panels
    private SearchResultsTreePanel searchResultsTreePanel;
    private DescriptionPane descriptionPane;
    private JPanel metaCatalogueTreePanel;
    public final static String NAVIGATOR_HOME = System.getProperty("user.home") + "/.navigator/";

    /** Creates a new instance of Navigator */
    public Navigator(ProgressObserver progressObserver) throws Exception {
        this.logger = Logger.getLogger(this.getClass());

        this.progressObserver = progressObserver;
        this.propertyManager = PropertyManager.getManager();

        this.preferences = Preferences.userNodeForPackage(this.getClass());

        this.exceptionManager = ExceptionManager.getManager();

        this.init();

    }
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    public Navigator() throws Exception {
        this(new ProgressObserver());
    }

    private void init() throws Exception {
        //LAFManager.getManager().changeLookAndFeel(LAFManager.WINDOWS);
        if (StaticDebuggingTools.checkHomeForFile("cismetDebuggingInitEventDispatchThreadHangMonitor")) {
            EventDispatchThreadHangMonitor.initMonitoring();
        }
        if (StaticDebuggingTools.checkHomeForFile("cismetBeansbindingDebuggingOn")) {
            System.setProperty("cismet.beansdebugging", "true");
        }
        if (StaticDebuggingTools.checkHomeForFile("cismetCheckForEDThreadVialoation")) {
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        }

        initConnection();

        try {
            checkNavigatorHome();
            initUI();
            initWidgets();
            initDialogs();
            initPlugins();
            initEvents();
            initWindow();
            //Not in EDT
            if (container instanceof LayoutedContainer) {
                SwingUtilities.invokeLater(new Runnable() {
                    //UGLY WINNING

                    public void run() {
                        ((LayoutedContainer) container).loadLayout(LayoutedContainer.DEFAULT_LAYOUT, true, Navigator.this);
                    }
                });

            }
            if (!StaticDebuggingTools.checkHomeForFile("cismetTurnOffInternalWebserver")) {
                initHttpServer();
            }
        } catch (InterruptedException iexp) {
            logger.error("navigator start interrupted: " + iexp.getMessage() + "\n disconnecting from server");
            SessionManager.getSession().logout();
            SessionManager.getConnection().disconnect();
            this.progressObserver.reset();
        }

        //From Hell
        KeyStroke configLoggerKeyStroke = KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK);
        Action configAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        Log4JQuickConfig.getSingletonInstance().setVisible(true);
                    }
                });
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(configLoggerKeyStroke, "CONFIGLOGGING");
        getRootPane().getActionMap().put("CONFIGLOGGING", configAction);

    }

    private void checkNavigatorHome() {
        try {
            File file = new File(NAVIGATOR_HOME);
            if (file.exists()) {
                logger.debug("Navigator Verzeichniss vorhanden.");
            } else {
                logger.debug("Navigator Verzeichniss nicht vorhanden --> wird angelegt");
                file.mkdir();
                logger.debug("Navigator Verzeichniss erfolgreich angelegt");
            }
        } catch (Exception ex) {
            logger.error("Fehler beim überprüfen/anlegen des Navigator Homeverzeichnisses", ex);
        }
    }

    // #########################################################################
    private void initConnection() throws ConnectionException, InterruptedException {
        progressObserver.setProgress(25,
                I18N.getString("Sirius.navigator.Navigator.initConnection().progress.connection"));
        Connection connection = ConnectionFactory.getFactory().createConnection(propertyManager.getConnectionClass(), propertyManager.getConnectionInfo().getCallserverURL());
        ConnectionSession session = null;
        ConnectionProxy proxy = null;

        progressObserver.setProgress(50,
                I18N.getString("Sirius.navigator.Navigator.initConnection().progress.login"));
        // autologin
        if (propertyManager.isAutoLogin()) {
            logger.info("performing autologin of user '" + propertyManager.getConnectionInfo().getUsername() + "'");
            try {
                session = ConnectionFactory.getFactory().createSession(connection, propertyManager.getConnectionInfo(), true);
                proxy = ConnectionFactory.getFactory().createProxy(propertyManager.getConnectionProxyClass(), session);
                SessionManager.init(proxy);
            } catch (UserException uexp) {
                logger.error("autologin failed", uexp);
                session = null;
            }
        }

        // autologin = false || autologin failed
        if (!propertyManager.isAutoLogin() || session == null) {
            logger.info("performing login");
            try {
                session = ConnectionFactory.getFactory().createSession(connection, propertyManager.getConnectionInfo(), false);
            } catch (UserException uexp) {
            } //should never happen
            proxy = ConnectionFactory.getFactory().createProxy(propertyManager.getConnectionProxyClass(), session);
            SessionManager.init(proxy);

            loginDialog = new LoginDialog(this);
            loginDialog.setLocationRelativeTo(null);
            loginDialog.show();
        }

        PropertyManager.getManager().setEditable(this.hasPermission(SessionManager.getProxy().getClasses(), PermissionHolder.WRITEPERMISSION));
        //PropertyManager.getManager().setEditable(true);
        logger.info("initConnection(): navigator editor enabled: " + PropertyManager.getManager().isEditable());
    }
    // #########################################################################

    private void initUI() throws InterruptedException {
        progressObserver.setProgress(100,
                I18N.getString("Sirius.navigator.Navigator.initUI().progress.create"));

        // vergiss es 2 GHz, keine sanduhr
        //Toolkit.getDefaultToolkit().getSystemEventQueue().push(new WaitCursorEventQueue(200));

        menuBar = new MutableMenuBar();
        toolBar = new MutableToolBar(propertyManager.isAdvancedLayout());
        container = new LayoutedContainer(toolBar, menuBar, propertyManager.isAdvancedLayout());
        if (container instanceof LayoutedContainer) {
            menuBar.registerLayoutManager((LayoutedContainer) container);
        }
        statusBar = new MutableStatusBar();
        popupMenu = new MutablePopupMenu();


        progressObserver.setProgress(150,
                I18N.getString("Sirius.navigator.Navigator.initUI().progress.register"));
        this.setContentPane(new JPanel(new BorderLayout(), true));
        this.setJMenuBar(menuBar);

        //this.getContentPane().add(toolBar, BorderLayout.NORTH);
        //this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        //this.getContentPane().add(container.getContainer() , BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(container.getContainer(), BorderLayout.CENTER);

        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);


    }
    // #########################################################################

    private void initWidgets() throws Exception {
        // MetaCatalogueTree ---------------------------------------------------
        progressObserver.setProgress(200, 
                I18N.getString("Sirius.navigator.Navigator.initWidgets().progress.catalogue"));
        RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());
        metaCatalogueTree = new MetaCatalogueTree(rootTreeNode, PropertyManager.getManager().isEditable(), true, propertyManager.getMaxConnections());
        // dnd
        MetaTreeNodeDnDHandler dndHandler = new MetaTreeNodeDnDHandler(metaCatalogueTree);

        MutableConstraints catalogueTreeConstraints = new MutableConstraints(propertyManager.isAdvancedLayout());
        catalogueTreeConstraints.addAsScrollPane(ComponentRegistry.CATALOGUE_TREE,
                metaCatalogueTree,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().metaCatalogueTree.ui.name"),
                I18N.getString("Sirius.navigator.Navigator.initWidgets().metaCatalogueTree.ui.tooltip"),
                resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.initWidgets().metaCatalogueTree.ui.icon")),
                MutableConstraints.P1, MutableConstraints.ANY_INDEX, true);
        container.add(catalogueTreeConstraints);

        // SearchResultsTree ---------------------------------------------------
        progressObserver.setProgress(225,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().progress.searchresults"));
        searchResultsTree = new SearchResultsTree();
        searchResultsTreePanel = new SearchResultsTreePanel(searchResultsTree, propertyManager.isAdvancedLayout());
        // dnd
        new MetaTreeNodeDnDHandler(searchResultsTree);

        MutableConstraints searchResultsTreeConstraints = new MutableConstraints(propertyManager.isAdvancedLayout());
        searchResultsTreeConstraints.addAsComponent(ComponentRegistry.SEARCHRESULTS_TREE,
                searchResultsTreePanel,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().searchresults.ui.name"),
                I18N.getString("Sirius.navigator.Navigator.initWidgets().searchresults.ui.tooltip"),
                resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.initWidgets().searchresults.ui.icon")),
                MutableConstraints.P1, MutableConstraints.ANY_INDEX);
        container.add(searchResultsTreeConstraints);

        // AttributePanel ------------------------------------------------------
        progressObserver.setProgress(250,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().progress.attributeviewer"));
        attributeViewer = new AttributeViewer();
        FloatingFrameConfigurator configurator = new FloatingFrameConfigurator(
                ComponentRegistry.ATTRIBUTE_VIEWER,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeviewer.ui.name"));
        configurator.setTitleBarEnabled(false);

        MutableConstraints attributePanelConstraints = new MutableConstraints(propertyManager.isAdvancedLayout());
        attributePanelConstraints.addAsFloatingFrame(ComponentRegistry.ATTRIBUTE_VIEWER,
                attributeViewer,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeviewer.ui.name"),
                I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeviewer.ui.tooltip"),
                resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeviewer.ui.icon")),
                MutableConstraints.P2, 0, false, configurator, false);
        container.add(attributePanelConstraints);

        // AttributeEditor .....................................................
        if (PropertyManager.getManager().isEditable()) {
            progressObserver.setProgress(275,
                    I18N.getString("Sirius.navigator.Navigator.initWidgets().progress.attributeeditor"));
            //HELL
//            if (StaticDebuggingTools.checkHomeForFile("cidsExperimentalBeanEditorsEnabled")) {
            attributeEditor = new NavigatorAttributeEditorGui();
//            } else {
//                attributeEditor = new AttributeEditor();
//            }
            configurator = new FloatingFrameConfigurator(ComponentRegistry.ATTRIBUTE_EDITOR,
                    I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeeditor.ui.name"));
            configurator.setTitleBarEnabled(false);

            final MutableConstraints attributeEditorConstraints = new MutableConstraints(true);
            attributeEditorConstraints.addAsFloatingFrame(ComponentRegistry.ATTRIBUTE_EDITOR,
                    attributeEditor,
                    I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeeditor.ui.name"),
                    I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeeditor.ui.tooltip"),
                    resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.initWidgets().attributeeditor.ui.icon")),
                    MutableConstraints.P3, 1, false, configurator, false);
            container.add(attributeEditorConstraints);

            // verschieben nach position 1 oder zwei beim Dr�cken von
            // SHIFT + F2 / F3
            InputMap inputMap = attributeEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = attributeEditor.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, KeyEvent.SHIFT_DOWN_MASK, true), MutableConstraints.P2);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_DOWN_MASK, true), MutableConstraints.P3);

            actionMap.put(MutableConstraints.P2, new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    attributeEditorConstraints.setPosition(MutableConstraints.P2);
                }
            });

            actionMap.put(MutableConstraints.P3, new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    attributeEditorConstraints.setPosition(MutableConstraints.P3);
                }
            });

            logger.info("attribute editor enabled");
        } else {
            logger.info("attribute editor disabled");
        }

        // DescriptionPane -----------------------------------------------------
        progressObserver.setProgress(325, 
                I18N.getString("Sirius.navigator.Navigator.initWidgets().progress.descriptionpane"));

        descriptionPane = new DescriptionPane();



        configurator = new FloatingFrameConfigurator(ComponentRegistry.DESCRIPTION_PANE,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().descriptionpane.name"));
        //configurator.setTitleBarEnabled(false);

        MutableConstraints descriptionPaneConstraints = new MutableConstraints(propertyManager.isAdvancedLayout());
        descriptionPaneConstraints.addAsFloatingFrame(ComponentRegistry.DESCRIPTION_PANE,
                descriptionPane,
                I18N.getString("Sirius.navigator.Navigator.initWidgets().descriptionpane.name"),
                I18N.getString("Sirius.navigator.Navigator.initWidgets().descriptionpane.tooltip"),
                resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.initWidgets().descriptionpane.icon")),
                MutableConstraints.P3, 0, false, configurator, false);
        container.add(descriptionPaneConstraints);
    }

    private void initDialogs() throws Exception {
        progressObserver.setProgress(350,
                I18N.getString("Sirius.navigator.Navigator.initDialogs().progress.searchdialog"));

        //searchDialog = new SearchDialog(this, this.searchResultsTree, "Suche", SessionManager.getProxy().getClassTreeNodes(SessionManager.getSession().getUser()), PropertyManager.getManager().getMaxSearchResults());
        searchDialog = new SearchDialog(this, SessionManager.getProxy().getSearchOptions(), SessionManager.getProxy().getClassTreeNodes());

        progressObserver.setProgress(550,
                I18N.getString("Sirius.navigator.Navigator.initDialogs().progress.registerwidges"));
        ComponentRegistry.registerComponents(this, container, menuBar, toolBar, popupMenu, metaCatalogueTree, searchResultsTree, attributeViewer, attributeEditor, searchDialog, descriptionPane);
    }
    // #########################################################################

    private void initPlugins() throws Exception {
        progressObserver.setProgress(575,
                I18N.getString("Sirius.navigator.Navigator.initPlugins().progress.preloadplugins"));
        PluginRegistry.getRegistry().preloadPlugins();

        progressObserver.setProgress(650,
                I18N.getString("Sirius.navigator.Navigator.initPlugins().progress.loadplugins"));
        PluginRegistry.getRegistry().loadPlugins();

        progressObserver.setProgress(850,
                I18N.getString("Sirius.navigator.Navigator.initPlugins().progress.activateplugins"));
        PluginRegistry.getRegistry().activatePlugins();
    }
    // #########################################################################

    private void initEvents() throws InterruptedException {
        progressObserver.setProgress(900,
                I18N.getString("Sirius.navigator.Navigator.initEvents().progress.eventhandling"));
        StatusChangeListener statusChangeListener = new StatusChangeListener(statusBar);

        metaCatalogueTree.addStatusChangeListener(statusChangeListener);
        descriptionPane.addStatusChangeListener(statusChangeListener);
        searchDialog.addStatusChangeListener(statusChangeListener);

        CatalogueSelectionListener catalogueSelectionListener = new CatalogueSelectionListener(attributeViewer, descriptionPane);
        metaCatalogueTree.addTreeSelectionListener(catalogueSelectionListener);
        searchResultsTree.addTreeSelectionListener(catalogueSelectionListener);

        metaCatalogueTree.addComponentListener(new CatalogueActivationListener(metaCatalogueTree, attributeViewer, descriptionPane));
        searchResultsTree.addComponentListener(new CatalogueActivationListener(searchResultsTree, attributeViewer, descriptionPane));

        CataloguePopupMenuListener cataloguePopupMenuListener = new CataloguePopupMenuListener(popupMenu);
        metaCatalogueTree.addMouseListener(cataloguePopupMenuListener);
        searchResultsTree.addMouseListener(cataloguePopupMenuListener);

        //Runtime.getRuntime().addShutdownHook(new ShutdownListener());
    }
    // #########################################################################

    private void initWindow() throws InterruptedException {
        progressObserver.setProgress(950, 
                I18N.getString("Sirius.navigator.Navigator.initWindow().progress.window"));
        this.setTitle(I18N.getString("Sirius.navigator.Navigator.title"));
        this.setIconImage(resourceManager.getIcon(
                I18N.getString("Sirius.navigator.Navigator.icon")).getImage());
        this.restoreWindowState();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ClosingListener());
        progressObserver.setProgress(1000, 
                I18N.getString("Sirius.navigator.Navigator.initWindow().progress.finished"));
    }
    // .........................................................................

    private boolean hasPermission(MetaClass[] classes, Sirius.server.newuser.permission.Permission permission) {

        String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();

        //propertyManager.getConnectionInfo().getUsergroup()+"@"+propertyManager.getConnectionInfo().getUsergroupDomain();


        for (int i = 0; i < classes.length; i++) {
            try {

                // falsch aufgerufen schlob SessionManager.getSession().getUser().getUserGroup().getKey()
                PermissionHolder perm = classes[i].getPermissions();
                if (logger.isDebugEnabled()) {
                    logger.debug(" usergroup can edit ?? " + key + " permissions :: " + perm);                //logger.debug(perm +" \n" +key);
                }
                if (perm != null && perm.hasPermission(key, permission)) //xxxxxxxxxxxxxxxxxxxxxx user????
                {
                    if (logger.isDebugEnabled()) {
                        logger.debug("permission '" + permission + "' found in class '" + classes[i] + "'");
                    }
                    return true;
                }

                //                if(classes[i].getPermissions().hasPermission(permission)) //xxxxxxxxxxxxxxxxxxxxxx user????
                //                {
                //                    if(logger.isDebugEnabled())logger.debug("permission '" + permission + "' found in class '" + classes[i] + "'");
                //                    return true;
                //                }
            } catch (Exception exp) {
                logger.error("hasPermission(): could not check permissions", exp);
            }
        }

        logger.warn("permission '" + permission + "' not found, disabling editor");
        return false;
    }

    public void setVisible(final boolean visible) {
        logger.info("setting main window visible to '" + visible + "'");

        if (SwingUtilities.isEventDispatchThread()) {
            doSetVisible(visible);
        } else {
            logger.debug("doSetVisible(): synchronizing method");
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    doSetVisible(visible);
                }
            });
        }



        /*if(SwingUtilities.isEventDispatchThread())
        {
        PluginRegistry.getRegistry().setPluginsVisible(visible);
        }
        else
        {
        logger.debug("setPluginsVisible(): synchronizing method");
        SwingUtilities.invokeLater(new Runnable()
        {
        public void run()
        {
        PluginRegistry               PluginRegistry.getRegistry().setPluginsVisible(visible);
        }
        });
        }*/

        //PluginRegistry.getRegistry().setPluginsVisible(visible);
    }

    private void doSetVisible(final boolean visible) {
        super.setVisible(visible);

        //PluginRegistry.getRegistry().setPluginsVisible(visible);

        if (visible) {
            this.searchResultsTreePanel.setButtonsEnabled();
            this.container.setDividerLocations(0.23, 0.60);
            this.menuBar.repaint();
            this.toolBar.repaint();


            //container.select(ComponentRegistry.SEARCHRESULTS_TREE);
            //descriptionPane.setPage("http://www.cismet.de");

            this.toFront();
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                PluginRegistry.getRegistry().setPluginsVisible(visible);
            }
        });
    }
    // .........................................................................

    public void dispose() {


        logger.info("dispose() called");
        if (container instanceof LayoutedContainer) {
            logger.info("saving Layout");
            ((LayoutedContainer) container).saveLayout(LayoutedContainer.DEFAULT_LAYOUT, this);
        }
        Navigator.this.saveWindowState();

        PluginRegistry.destroy();

        SessionManager.getConnection().disconnect();
        SessionManager.destroy();
        MethodManager.destroy();
        ComponentRegistry.destroy();

        if (!Navigator.this.isDisposed()) {
            Navigator.super.dispose();
            Navigator.this.setDisposed(true);
        } else {
            logger.warn("...............................");
        }
    }

    private void saveWindowState() {
        int windowHeight = this.getHeight();
        int windowWidth = this.getWidth();
        int windowX = (int) this.getLocation().getX();
        int windowY = (int) this.getLocation().getY();
        boolean windowMaximised = (this.getExtendedState() == MAXIMIZED_BOTH);

        logger.info("saving window state: \nwindowHeight=" + windowHeight + ", windowWidth=" + windowWidth + ", windowX=" + windowX + ", windowY=" + windowY + ", windowMaximised=" + windowMaximised);

        this.preferences.putInt("windowHeight", windowHeight);
        this.preferences.putInt("windowWidth", windowWidth);
        this.preferences.putInt("windowX", windowX);
        this.preferences.putInt("windowY", windowY);
        this.preferences.putBoolean("windowMaximised", windowMaximised);
        logger.info("saved window state");
    }

    private void restoreWindowState() {
        logger.info("restoring window state ...");
        int windowHeight = this.preferences.getInt("windowHeight", PropertyManager.getManager().getHeight());
        int windowWidth = this.preferences.getInt("windowWidth", PropertyManager.getManager().getWidth());
        int windowX = this.preferences.getInt("windowX", 0);
        int windowY = this.preferences.getInt("windowY", 0);
        boolean windowMaximised = this.preferences.getBoolean("windowMaximised", propertyManager.isMaximizeWindow());

        logger.info("restoring window state: \nwindowHeight=" + windowHeight + ", windowWidth=" + windowWidth + ", windowX=" + windowX + ", windowY=" + windowY + ", windowMaximised=" + windowMaximised);

        this.setSize(windowWidth, windowHeight);
        this.setLocation(windowX, windowY);

        if (windowMaximised) {
            this.setExtendedState(MAXIMIZED_BOTH);
        }
    }

    private class ClosingListener extends WindowAdapter {

        /** Invoked when the user attempts to close the window
         * from the window's system menu.  If the program does not
         * explicitly hide or dispose the window while processing
         * this event, the window close operation will be cancelled.
         *
         */
        public void windowClosing(WindowEvent e) {
            if (exceptionManager.showExitDialog(Navigator.this)) {
                dispose();
                logger.info("closing navigator");
                System.exit(0);
            }
        }
    }

    /*private class ShutdownListener extends Thread
    {
    public void run()
    {
    if(Navigator.this.isDisposed())
    {
    Navigator.this.logger.info("ShutdownListener: clean shutdown initiated");
    }
    else
    {
    Navigator.this.logger.warn("ShutdownListener: unclean shutdown initiated, invokinbg dispose()");
    Navigator.this.dispose();
    }
    }
    }*/    // -------------------------------------------------------------------------
    public static void main(String args[]) {
        final boolean release = true;

        try {
            // cmdline arguments ...............................................

            // <RELEASE>
            if (release) {
                if (args.length < 5) {
                    String errorString = new String("\nusage: navigator %1 %2 %3 %4 %5 %6(%5)\n%1 = navigator config file \n%2 = navigator working directory \n%3 = plugin base directory \n%4 = navigator search forms base directory \n%5 navigator search profile store (optional, default: %1/profiles \n\nexample: java Sirius.navigator.Navigator c:\\programme\\cids\\navigator\\navigator.cfg c:\\programme\\cids\\navigator\\ c:\\programme\\cids\\navigator\\plugins\\ c:\\programme\\cids\\navigator\\search\\ c:\\programme\\cids\\navigator\\search\\profiles\\");
                    System.out.println(errorString);

                    //System.exit(1);
                    throw new Exception(errorString);
                } else {
                    System.out.println("-------------------------------------------------------");
                    System.out.println("C I D S   N A V I G A T 0 R   C O N F I G U R A T I 0 N");
                    System.out.println("-------------------------------------------------------");
                    System.out.println("log4j.properties = " + args[0]);
                    System.out.println("navigator.cfg    = " + args[1]);
                    System.out.println("basedir          = " + args[2]);
                    System.out.println("plugindir        = " + args[3]);
                    System.out.println("searchdir        = " + args[4]);
                    if (args.length > 5) {
                        System.out.println("profilesdir      = " + args[5]);
                    }
                    System.out.println("-------------------------------------------------------");


                    // log4j configuration .....................................
                    Properties properties = new Properties();
                    boolean l4jinited = false;
                    try {
                        URL log4jPropertiesURL = new URL(args[0]);
                        properties.load(log4jPropertiesURL.openStream());

                        l4jinited = true;
                    } catch (Throwable t) {
                        System.err.println("could not lode log4jproperties will try to load it from file" + t.getMessage());
                        t.printStackTrace();
                    }

                    try {
                        if (!l4jinited) {
                            properties.load(new BufferedInputStream(new FileInputStream(new File(args[0]))));
                        }
                    } catch (Throwable t) {

                        System.err.println("could not lode log4jproperties " + t.getMessage());
                        t.printStackTrace();

                    }

                    PropertyConfigurator.configure(properties);

                    // log4j configuration .....................................

                    PropertyManager.getManager().configure(args[1], args[2], args[3], args[4], (args.length > 5 ? args[5] : null));
                    resourceManager.setLocale(PropertyManager.getManager().getLocale());
                }
            }//</RELEASE>
            else {
                PropertyConfigurator.configure(ClassLoader.getSystemResource("Sirius/navigator/resource/cfg/log4j.debug.properties"));

                // ohne plugins:
                //PropertyManager.getManager().configure("D:\\cids\\res\\Sirius\\navigator\\resource\\cfg\\navigator.cfg", System.getProperty("user.home") + "\\.navigator\\", System.getProperty("user.home") + "\\.navigator\\plugins\\", "D:\\cids\\dist\\client\\search\\", null);

                // mit plugins:
                PropertyManager.getManager().configure("D:\\cids\\res\\Sirius\\navigator\\resource\\cfg\\navigator.cfg", System.getProperty("user.home") + "\\.navigator\\", "D:\\cids\\dist\\client\\plugins\\", "D:\\cids\\dist\\client\\search\\", null);
                resourceManager.setLocale(PropertyManager.getManager().getLocale());

                // Properties ausgeben:
                PropertyManager.getManager().print();
            }

            // configuration ...................................................


            // look and feel ...................................................
            LAFManager.getManager().changeLookAndFeel(PropertyManager.getManager().getLookAndFeel());



// look and feel ...................................................

            // configuration ...................................................


            // run .............................................................
            //Navigator navigator = new Navigator();
            //navigator.logger.debug("new navigator instance created");
            //navigator.setVisible(true);

            //logger.debug("SPLASH");
            NavigatorSplashScreen navigatorSplashScreen =
                    new NavigatorSplashScreen(PropertyManager.getManager().getSharedProgressObserver(),
                    resourceManager.getIcon(I18N.getString("Sirius.navigator.Navigator.splashscreenicon")));

            navigatorSplashScreen.pack();
            navigatorSplashScreen.setLocationRelativeTo(null);
            navigatorSplashScreen.toFront();
            navigatorSplashScreen.show();

            // run .............................................................
        } catch (Throwable t) {
            // error .............................................................
            Logger.getLogger(Navigator.class).fatal("could not create navigator instance", t);
            ExceptionManager.getManager().showExceptionDialog(
                    ExceptionManager.FATAL,
                    resourceManager.getExceptionName("nx01"),
                    resourceManager.getExceptionMessage("nx01"), t);

            System.exit(1);
            // error .............................................................
        }
    }

    /**
     * Getter for property disposed.
     * @return Value of property disposed.
     */
    public boolean isDisposed() {

        return this.disposed;
    }

    /**
     * Setter for property disposed.
     * @param disposed New value of property disposed.
     */
    private synchronized void setDisposed(boolean disposed) {

        this.disposed = disposed;
    }

    private void initHttpServer() {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    Server server = new Server();
                    Connector connector = new SelectChannelConnector();
                    connector.setPort(propertyManager.getHttpInterfacePort());
                    server.setConnectors(new Connector[]{connector});

                    Handler param = new AbstractHandler() {

                        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                            Request base_request = (request instanceof Request) ? (Request) request : HttpConnection.getCurrentConnection().getRequest();
                            base_request.setHandled(true);
                            response.setContentType("text/html");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().println("<html><head><title>HTTP interface</title></head><body><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\"><tr><td width=\"30%\" align=\"center\" valign=\"middle\"><img border=\"0\" src=\"http://www.cismet.de/images/cismetLogo250M.png\" ><br></td><td width=\"%\">&nbsp;</td><td width=\"50%\" align=\"left\" valign=\"middle\"><font face=\"Arial\" size=\"3\" color=\"#1c449c\">... and <b><font face=\"Arial\" size=\"3\" color=\"#1c449c\">http://</font></b> just works</font><br><br><br></td></tr></table></body></html>");
                        }
                    };
                    Handler hello = new AbstractHandler() {

                        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                            try {
                                if (request.getLocalAddr().equals(request.getRemoteAddr())) {

                                    logger.info("HttpInterface angesprochen");
                                    if (target.equalsIgnoreCase("/executeSearch")) {
                                        String query = request.getParameter("query");
                                        String domain = request.getParameter("domain");
                                        String classId = request.getParameter("classId");
                                        HashMap dataBeans = ComponentRegistry.getRegistry().getSearchDialog().getSearchFormManager().getFormDataBeans();
                                        Object object = dataBeans.get(query + "@" + domain);
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        Set keys = request.getParameterMap().keySet();
                                        Iterator it = keys.iterator();
                                        while (it.hasNext()) {
                                            String key = it.next().toString();
                                            if (!(key.equalsIgnoreCase("query") || key.equalsIgnoreCase("domain") || key.equalsIgnoreCase("classId"))) {
                                                params.put(key, request.getParameter(key));
                                            }
                                        }
                                        if (object != null) {
                                            FormDataBean parambean = (FormDataBean) object;
                                            for (String key : params.keySet()) {
                                                parambean.setBeanParameter(key, params.get(key));
                                            }
                                            Vector v = new Vector();
                                            String cid = classId + "@" + domain;
                                            v.add(cid);
                                            LinkedList searchFormData = new LinkedList();
                                            searchFormData.add(parambean);
                                            ComponentRegistry.getRegistry().getSearchDialog().search(v, searchFormData, Navigator.this, false);
                                        }
                                    }
                                    if (target.equalsIgnoreCase("/showAkuk")) {
                                        String domain = request.getParameter("domain");
                                        String classId = request.getParameter("classId");
                                        String objectIds = request.getParameter("objectIds");
                                    } else {
                                        logger.warn("Unbekanntes Target: " + target);
                                    }

                                } else {
                                    logger.warn("Sombody tries to access the HTTP Interface from a different Terminal. Rejected.");
                                }
                            } catch (Throwable t) {
                                logger.error("Error while handling HttpRequests", t);
                            }
                        }
                    };

                    HandlerCollection handlers = new HandlerCollection();
                    handlers.setHandlers(new Handler[]{param, hello});
                    server.setHandler(handlers);

                    server.start();
                    server.join();
                } catch (Throwable t) {
                    logger.error("Error in  Navigator HttpInterface on port " + propertyManager.getHttpInterfacePort(), t);
                }
            }
        });
        CismetThreadPool.execute(t);
    }
}


