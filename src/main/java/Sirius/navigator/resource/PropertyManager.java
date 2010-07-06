/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.resource;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       01.10.1999
 * History                      :
 *
 *******************************************************************************/
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.tools.BrowserControl;
import Sirius.navigator.ui.LAFManager;
import Sirius.navigator.ui.progress.*;

import org.apache.log4j.*;

import java.applet.*;

import java.beans.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class PropertyManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(PropertyManager.class);
    private static final PropertyManager manager = new PropertyManager();

    private static final String HEADER = "Navigator Configuration File";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String SORT_TOKEN_SEPARATOR = ",";
    public static final String SORT_NAME_TOKEN = "%name%";
    public static final String SORT_ID_TOKEN = "%id%";
    public static final int MIN_SERVER_THREADS = 3;
    public static final int MAX_SERVER_THREADS = 10;
    public static final int MIN_SEARCH_RESULTS = 20;
    public static final int MAX_SEARCH_RESULTS = 300;

    //~ Instance fields --------------------------------------------------------

    private final Properties properties;
    private final ConnectionInfo connectionInfo;
    private ArrayList pluginList = null;
    private String basePath = null;
    private String pluginPath = null;
    private String searchFormPath = null;
    private String profilesPath = null;
    private int width;
    private int height;
    private boolean maximizeWindow;
    private boolean advancedLayout;
    private String lookAndFeel;
    private String connectionClass;
    private String connectionProxyClass;
    private boolean autoLogin;
    private int maxConnections;
    private int maxSearchResults;
    private boolean sortChildren;
    private boolean sortAscending;
    private int httpInterfacePort = -1;
    private boolean connectionInfoSaveable;
    private boolean loadable;
    private boolean saveable;
    private boolean applet = false;
    private boolean application = true;
    private AppletContext appletContext = null;
    private final ProgressObserver sharedProgressObserver;
    private String language;
    private String country;
    private java.util.Locale locale;
    private boolean editable;
    private boolean autoClose = false;

    private transient String proxyURL;
    private transient String proxyUsername;
    private transient String proxyPassword;
    private transient String proxyDomain;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PropertyManager object.
     */
    private PropertyManager() {
        this.properties = new Properties();
        this.connectionInfo = new ConnectionInfo();
        this.connectionInfo.addPropertyChangeListener(new ConnectionInfoChangeListener());
        this.sharedProgressObserver = new ProgressObserver(1000, 100);

        setWidth(1024);
        setHeight(768);
        setMaximizeWindow(false);
        setAdvancedLayout(false);
        setLookAndFeel(LAFManager.getManager().getDefaultLookAndFeel().getName());

        setConnectionClass("Sirius.navigator.connection.RMIConnection");
        setConnectionProxyClass("Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler");
        setAutoLogin(false);
        setMaxConnections(MIN_SERVER_THREADS);
        setMaxSearchResults(MIN_SEARCH_RESULTS);
        setSortChildren(false);
        setSortAscending(false);

        setCountry("DE");
        setLanguage("de");

        setLoadable(true);
        setSaveable(false);
        setConnectionInfoSaveable(false);

        connectionInfo.setCallserverURL("rmi://192.168.0.12/callServer");
        connectionInfo.setPassword("");
        connectionInfo.setUserDomain("");
        connectionInfo.setUsergroup("");
        connectionInfo.setUsergroupDomain("");
        connectionInfo.setUsername("");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  maximizeWindow  DOCUMENT ME!
     */
    public void setMaximizeWindow(final String maximizeWindow) {
        if ((maximizeWindow != null) && (maximizeWindow.equalsIgnoreCase(TRUE) || maximizeWindow.equals("1"))) {
            this.setMaximizeWindow(true);
        } else if ((maximizeWindow != null) && (maximizeWindow.equalsIgnoreCase(FALSE) || maximizeWindow.equals("0"))) {
            this.setMaximizeWindow(false);
        } else {
            this.setMaximizeWindow(false);
            logger.warn("setMaximizeWindow(): invalid property 'maximizeWindow': '" + maximizeWindow
                        + "', setting default value to '" + this.maximizeWindow + "'");
        }
    }

    /**
     * Setter for property maximizeWindow.
     *
     * @param  maximizeWindow  New value of property maximizeWindow.
     */
    public void setMaximizeWindow(final boolean maximizeWindow) {
        this.maximizeWindow = maximizeWindow;
        properties.setProperty("maximizeWindow", String.valueOf(maximizeWindow));
    }

    /**
     * Getter for property maximizeWindow.
     *
     * @return  Value of property maximizeWindow.
     */
    public boolean isMaximizeWindow() {
        return this.maximizeWindow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width   DOCUMENT ME!
     * @param  height  DOCUMENT ME!
     */
    public void setSize(final int width, final int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width   DOCUMENT ME!
     * @param  height  DOCUMENT ME!
     */
    public void setSize(final String width, final String height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     */
    public void setWidth(final String width) {
        try {
            final int iwidth = Integer.parseInt(width);
            this.setWidth(iwidth);
        } catch (Exception exp) {
            logger.warn("setWidth(): invalid property 'witdh': '" + exp.getMessage() + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     */
    public void setWidth(final int width) {
        this.width = width;
        properties.setProperty("width", String.valueOf(width));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  height  DOCUMENT ME!
     */
    public void setHeight(final String height) {
        try {
            final int iheight = Integer.parseInt(height);
            this.setHeight(iheight);
        } catch (Exception exp) {
            logger.warn("setHeight(): invalid property 'height': '" + exp.getMessage() + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  height  DOCUMENT ME!
     */
    public void setHeight(final int height) {
        this.height = height;
        properties.setProperty("height", String.valueOf(height));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProxyURL() {
        return proxyURL;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProxyDomain() {
        return proxyDomain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * Getter for property connectionClass.
     *
     * @return  Value of property connectionClass.
     */
    public String getConnectionClass() {
        return this.connectionClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  proxyDomain  DOCUMENT ME!
     */
    public void setProxyDomain(final String proxyDomain) {
        this.proxyDomain = proxyDomain;
        properties.setProperty("navigator.proxy.domain", proxyDomain); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  proxyPassword  DOCUMENT ME!
     */
    public void setProxyPassword(final String proxyPassword) {
        this.proxyPassword = proxyPassword;
        properties.setProperty("navigator.proxy.password", proxyPassword); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  proxyURL  DOCUMENT ME!
     */
    public void setProxyURL(final String proxyURL) {
        this.proxyURL = proxyURL;
        properties.setProperty("navigator.proxy.url", proxyURL); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  proxyUsername  DOCUMENT ME!
     */
    public void setProxyUsername(final String proxyUsername) {
        this.proxyUsername = proxyUsername;
        properties.setProperty("navigator.proxy.username", proxyUsername); // NOI18N
    }

    /**
     * Setter for property connectionClass.
     *
     * @param  connectionClass  New value of property connectionClass.
     */
    public void setConnectionClass(final String connectionClass) {
        this.connectionClass = connectionClass;
        properties.setProperty("connectionClass", this.connectionClass);
    }

    /**
     * Getter for property connectionProxyClass.
     *
     * @return  Value of property connectionProxyClass.
     */
    public String getConnectionProxyClass() {
        return this.connectionProxyClass;
    }

    /**
     * Setter for property connectionProxyClass.
     *
     * @param  connectionProxyClass  New value of property connectionProxyClass.
     */
    public void setConnectionProxyClass(final String connectionProxyClass) {
        this.connectionProxyClass = connectionProxyClass;
        properties.setProperty("connectionProxyClass", this.connectionProxyClass);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  autoLogin  DOCUMENT ME!
     */
    public void setAutoLogin(final String autoLogin) {
        if ((autoLogin != null) && (autoLogin.equalsIgnoreCase(TRUE) || autoLogin.equals("1"))) {
            this.setAutoLogin(true);
        } else if ((autoLogin != null) && (autoLogin.equalsIgnoreCase(FALSE) || autoLogin.equals("0"))) {
            this.setAutoLogin(false);
        } else {
            this.setAutoLogin(false);
            logger.warn("setAutoLogin(): invalid property 'autoLogin': '" + autoLogin + "', setting default value to '"
                        + this.autoLogin + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  autoLogin  DOCUMENT ME!
     */
    public void setAutoLogin(final boolean autoLogin) {
        this.autoLogin = autoLogin;
        properties.setProperty("autoLogin", String.valueOf(this.autoLogin));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isAutoLogin() {
        return this.autoLogin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maxConnections  DOCUMENT ME!
     */
    public void setMaxConnections(final String maxConnections) {
        try {
            final int imaxConnections = Integer.parseInt(maxConnections);
            this.setMaxConnections(imaxConnections);
        } catch (Exception exp) {
            logger.warn("setMaxConnections(): invalid property 'maxConnections': '" + exp.getMessage() + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maxConnections  DOCUMENT ME!
     */
    public void setMaxConnections(final int maxConnections) {
        if ((maxConnections < MIN_SERVER_THREADS) || (maxConnections > MAX_SERVER_THREADS)) {
            this.maxConnections = MIN_SERVER_THREADS;
            properties.setProperty("maxConnections", String.valueOf(MIN_SERVER_THREADS));
            logger.warn("setMaxConnections(): invalid property 'maxConnections': '" + maxConnections
                        + "', setting default value to '" + MIN_SERVER_THREADS + "'");
        } else {
            this.maxConnections = maxConnections;
            properties.setProperty("maxConnections", String.valueOf(maxConnections));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getMaxConnections() {
        return this.maxConnections;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maxSearchResults  DOCUMENT ME!
     */
    public void setMaxSearchResults(final String maxSearchResults) {
        try {
            final int imaxSearchResults = Integer.parseInt(maxSearchResults);
            this.setMaxSearchResults(imaxSearchResults);
        } catch (NumberFormatException nfe) {
            logger.warn("setMaxSearchResults(): invalid property 'maxSearchResults': '" + nfe.getMessage() + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maxSearchResults  DOCUMENT ME!
     */
    public void setMaxSearchResults(final int maxSearchResults) {
        if ((maxSearchResults < MIN_SEARCH_RESULTS) || (maxSearchResults > MAX_SEARCH_RESULTS)) {
            this.maxSearchResults = MIN_SEARCH_RESULTS;
            properties.setProperty("maxSearchResults", String.valueOf(this.maxSearchResults));
            logger.warn("setMaxSearchResults(): invalid property 'maxSearchResults': '" + maxSearchResults
                        + "', setting default value to '" + MIN_SEARCH_RESULTS + "'");
        } else {
            this.maxSearchResults = maxSearchResults;
            properties.setProperty("maxSearchResults", String.valueOf(maxSearchResults));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getMaxSearchResults() {
        return this.maxSearchResults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  advancedLayout  DOCUMENT ME!
     */
    public void setAdvancedLayout(final String advancedLayout) {
        if ((advancedLayout != null) && (advancedLayout.equalsIgnoreCase(TRUE) || advancedLayout.equals("1"))) {
            this.setAdvancedLayout(true);
        } else if ((advancedLayout != null) && (advancedLayout.equalsIgnoreCase(FALSE) || advancedLayout.equals("0"))) {
            this.setAdvancedLayout(false);
        } else {
            this.setAdvancedLayout(false);
            logger.warn("setAdvancedLayout(): invalid property 'advancedLayout': '" + advancedLayout
                        + "', setting default value to '" + this.advancedLayout + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  advancedLayout  DOCUMENT ME!
     */
    public void setAdvancedLayout(final boolean advancedLayout) {
        this.advancedLayout = advancedLayout;
        properties.setProperty("advancedLayout", String.valueOf(this.advancedLayout));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isAdvancedLayout() {
        return this.advancedLayout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  lookAndFeelName  DOCUMENT ME!
     */
    public void setLookAndFeel(final String lookAndFeelName) {
        if (LAFManager.getManager().isInstalledLookAndFeel(lookAndFeelName)) {
            this.lookAndFeel = lookAndFeelName;
        } else {
            // this.lookAndFeel = LNF_METAL;
            this.lookAndFeel = LAFManager.getManager().getDefaultLookAndFeel().getName();
            logger.warn("setLookAndFeel(): invalid property 'lookAndFeel': '" + lookAndFeelName
                        + "', setting default value to '" + this.lookAndFeel + "'");
        }
        properties.setProperty("lookAndFeel", this.lookAndFeel);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getLookAndFeel() {
        return this.lookAndFeel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sortChildren  DOCUMENT ME!
     */
    public void setSortChildren(final String sortChildren) {
        if ((sortChildren != null) && (sortChildren.equalsIgnoreCase(TRUE) || sortChildren.equals("1"))) {
            this.setSortChildren(true);
        } else if ((sortChildren != null) && (sortChildren.equalsIgnoreCase(FALSE) || sortChildren.equals("0"))) {
            this.setSortChildren(false);
        } else {
            this.setSortChildren(false);
            logger.warn("setSortChildren(): invalid property 'sortChildren': '" + sortChildren
                        + "', setting default value to '" + this.sortChildren + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sortChildren  DOCUMENT ME!
     */
    public void setSortChildren(final boolean sortChildren) {
        this.sortChildren = sortChildren;
        properties.setProperty("sortChildren", String.valueOf(this.sortChildren));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSortChildren() {
        return this.sortChildren;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sortAscending  DOCUMENT ME!
     */
    public void setSortAscending(final String sortAscending) {
        if ((sortAscending != null) && (sortAscending.equalsIgnoreCase(TRUE) || sortAscending.equals("1"))) {
            this.setSortAscending(true);
        } else if ((sortAscending != null) && (sortAscending.equalsIgnoreCase(FALSE) || sortAscending.equals("0"))) {
            this.setSortAscending(false);
        } else {
            this.setSortAscending(false);
            logger.warn("setSortAscending(): invalid property 'sortAscending': '" + sortAscending
                        + "', setting default value to '" + this.sortAscending + "'");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sortAscending  DOCUMENT ME!
     */
    public void setSortAscending(final boolean sortAscending) {
        this.sortAscending = sortAscending;
        properties.setProperty("sortAscending", String.valueOf(this.sortAscending));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSortAscending() {
        return this.sortAscending;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  connectionInfoSaveable  DOCUMENT ME!
     */
    public void setConnectionInfoSaveable(final String connectionInfoSaveable) {
        if ((connectionInfoSaveable != null)
                    && (connectionInfoSaveable.equalsIgnoreCase(TRUE) || connectionInfoSaveable.equals("1"))) {
            this.setConnectionInfoSaveable(true);
        } else if ((connectionInfoSaveable != null)
                    && (connectionInfoSaveable.equalsIgnoreCase(FALSE) || connectionInfoSaveable.equals("0"))) {
            this.setConnectionInfoSaveable(false);
        } else {
            this.setConnectionInfoSaveable(false);
            logger.warn("connectionInfoSaveable(): invalid property 'connectionInfoSaveable': '"
                        + connectionInfoSaveable + "', setting default value to '" + this.connectionInfoSaveable + "'");
        }
    }

    /**
     * Setter for property connectionInfoSaveable.
     *
     * @param  connectionInfoSaveable  New value of property connectionInfoSaveable.
     */
    public void setConnectionInfoSaveable(final boolean connectionInfoSaveable) {
        this.connectionInfoSaveable = this.isSaveable() & connectionInfoSaveable;
        properties.setProperty("connectionInfoSaveable", String.valueOf(this.connectionInfoSaveable));
    }

    /**
     * Getter for property connectionInfoSaveable.
     *
     * @return  Value of property connectionInfoSaveable.
     */
    public boolean isConnectionInfoSaveable() {
        return this.connectionInfoSaveable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  loadable  DOCUMENT ME!
     */
    public void setLoadable(final String loadable) {
        if ((loadable != null) && (loadable.equalsIgnoreCase(TRUE) || loadable.equals("1"))) {
            this.setLoadable(true);
        } else if ((loadable != null) && (loadable.equalsIgnoreCase(FALSE) || loadable.equals("0"))) {
            this.setLoadable(false);
        } else {
            this.setLoadable(false);
            logger.warn("loadable(): invalid property 'loadable': '" + loadable + "', setting default value to '"
                        + this.loadable + "'");
        }
    }

    /**
     * Setter for property loadable.
     *
     * @param  loadable  New value of property loadable.
     */
    public void setLoadable(final boolean loadable) {
        this.loadable = loadable;
        properties.setProperty("loadable", String.valueOf(this.loadable));
    }

    /**
     * Getter for property loadable.
     *
     * @return  Value of property loadable.
     */
    public boolean isLoadable() {
        return this.loadable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  saveable  DOCUMENT ME!
     */
    public void setSaveable(final String saveable) {
        if ((saveable != null) && (saveable.equalsIgnoreCase(TRUE) || saveable.equals("1"))) {
            this.setSaveable(true);
        } else if ((saveable != null) && (saveable.equalsIgnoreCase(FALSE) || saveable.equals("0"))) {
            this.setSaveable(false);
        } else {
            this.setSaveable(false);
            logger.warn("saveable(): invalid property 'saveable': '" + saveable + "', setting default value to '"
                        + this.saveable + "'");
        }
    }

    /**
     * Setter for property saveable.
     *
     * @param  saveable  New value of property saveable.
     */
    public void setSaveable(final boolean saveable) {
        this.saveable = saveable;
        properties.setProperty("saveable", String.valueOf(this.saveable));
    }

    /**
     * Getter for property saveable.
     *
     * @return  Value of property saveable.
     */
    public boolean isSaveable() {
        return this.saveable;
    }

    /**
     * .........................................................................
     *
     * @return  DOCUMENT ME!
     */
    public ConnectionInfo getConnectionInfo() {
        return this.connectionInfo;
    }

    /**
     * .........................................................................
     */
    private void load() {
        final Enumeration keys = properties.keys();
        while (keys.hasMoreElements()) {
            final String key = (String)keys.nextElement();
            this.setProperty(key, properties.getProperty(key));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  property  DOCUMENT ME!
     * @param  value     DOCUMENT ME!
     */
    private synchronized void setProperty(final String property, final String value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setting property '" + property + "' to '" + value + "'");
            /*if(property.equalsIgnoreCase("title"))
             * { this.setTitle(value); }else*/
        }
        if (property.equalsIgnoreCase("width")) {
            this.setWidth(value);
        } else if (property.equalsIgnoreCase("height")) {
            this.setHeight(value);
        } else if (property.equalsIgnoreCase("maximizeWindow")) {
            this.setMaximizeWindow(value);
        } else if (property.equalsIgnoreCase("advancedLayout")) {
            this.setAdvancedLayout(value);
        } else if (property.equalsIgnoreCase("lookAndFeel")) {
            this.setLookAndFeel(value);
        } else if (property.equalsIgnoreCase("autoLogin")) {
            this.setAutoLogin(value);
        } else if (property.equalsIgnoreCase("connectionClass")) {
            this.setConnectionClass(value);
        } else if (property.equalsIgnoreCase("connectionProxyClass")) {
            this.setConnectionProxyClass(value);
        } else if (property.equalsIgnoreCase("maxConnections")) {
            this.setMaxConnections(value);
        } else if (property.equalsIgnoreCase("maxSearchResults")) {
            this.setMaxSearchResults(value);
        } else if (property.equalsIgnoreCase("sortChildren")) {
            this.setSortChildren(value);
        } else if (property.equalsIgnoreCase("sortAscending")) {
            this.setSortAscending(value);
        } else if (property.equalsIgnoreCase("saveable")) {
            this.setSaveable(value);
        } else if (property.equalsIgnoreCase("loadable")) {
            this.setLoadable(value);
        } else if (property.equalsIgnoreCase("language")) {
            this.setLanguage(value);
        } else if (property.equalsIgnoreCase("country")) {
            this.setCountry(value);
        } else if (property.equalsIgnoreCase("connectionInfoSaveable")) {
            this.setConnectionInfoSaveable(value);
        } else if (property.equalsIgnoreCase("callserverURL")) {
            this.connectionInfo.setCallserverURL(value);
        } else if (property.equalsIgnoreCase("password")) {
            this.connectionInfo.setPassword(value);
        } else if (property.equalsIgnoreCase("userDomain")) {
            this.connectionInfo.setUserDomain(value);
        } else if (property.equalsIgnoreCase("usergroup")) {
            this.connectionInfo.setUsergroup(value);
        } else if (property.equalsIgnoreCase("usergroupDomain")) {
            this.connectionInfo.setUsergroupDomain(value);
        } else if (property.equalsIgnoreCase("username")) {
            this.connectionInfo.setUsername(value);
        } else if (property.equals("navigator.proxy.url")) {
            this.setProxyURL(value);
        } else if (property.equals("navigator.proxy.username")) {
            this.setProxyUsername(value);
        } else if (property.equals("navigator.proxy.password")) {
            this.setProxyPassword(value);
        } else if (property.equals("navigator.proxy.domain")) {
            this.setProxyDomain(value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  inStream  DOCUMENT ME!
     */
    public void load(final InputStream inStream) {
        if (this.isLoadable()) {
            try {
                this.properties.load(inStream);
                this.load();
            } catch (Exception exp) {
                logger.fatal("could not load properties: " + exp.getMessage(), exp);
            }
        } else {
            logger.error("could not load properties: properties not loadable");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void configure() {
        this.load(this.getClass().getResourceAsStream("cfg/navigator.cfg"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cfgFile         DOCUMENT ME!
     * @param   basePath        DOCUMENT ME!
     * @param   pluginPath      DOCUMENT ME!
     * @param   searchFormPath  DOCUMENT ME!
     * @param   profilesPath    DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void configure(final String cfgFile,
            final String basePath,
            final String pluginPath,
            final String searchFormPath,
            final String profilesPath) throws Exception {
        this.applet = false;
        this.application = true;

        if (basePath != null) {
            logger.info("setting base path to '" + basePath + "'");
            this.basePath = basePath;
        } else {
            this.getBasePath();
        }

        if (pluginPath != null) {
            logger.info("setting base plugin to '" + pluginPath + "'");
            this.pluginPath = pluginPath;
        } else {
            this.getPluginPath();
        }

        if (searchFormPath != null) {
            logger.info("setting search form path to '" + searchFormPath + "'");
            this.searchFormPath = searchFormPath;
        } else {
            this.getSearchFormPath();
        }

        if (profilesPath != null) {
            logger.info("setting profiles path to '" + profilesPath + "'");
            this.profilesPath = profilesPath;
        } else {
            this.getProfilesPath();
        }

        if (cfgFile != null) {
            if ((cfgFile.indexOf("http://") == 0) || (cfgFile.indexOf("https://") == 0)
                        || (cfgFile.indexOf("file://") == 0)) {
                final URL url = new URL(cfgFile);
                this.load(url.openStream());

                logger.info("config file loaded from url (assuming webstart)");
                this.applet = true;
            } else {
                final File file = new File(cfgFile);
                this.load(new BufferedInputStream(new FileInputStream(cfgFile)));
            }
        } else {
            throw new Exception("loading of config file '" + cfgFile + "' failed");
        }

        try {
            final String parameter = this.properties.getProperty("plugins");
            setHttpInterfacePort(new Integer(properties.getProperty("httpInterfacePort", "9099")));
            setAutoClose(new Boolean(properties.getProperty("closeWithoutAsking", "false")));

            if ((parameter != null) && (parameter.length() > 0)) {
                pluginList = new ArrayList();
                final StringTokenizer tokenizer = new StringTokenizer(parameter, ";");
                while (tokenizer.hasMoreTokens()) {
                    final String plugin = tokenizer.nextToken().trim() + "/";
                    logger.info("adding plugin from config file: '" + plugin + "'");
                    pluginList.add(pluginPath + "/" + plugin);
                }
            }
        } catch (Exception except) {
            logger.fatal(except, except);
        }

        this.isPluginListAvailable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  applet  DOCUMENT ME!
     */
    public void configure(final JApplet applet) {
        if (logger.isDebugEnabled()) {
            logger.debug("configure property manager (applet)");
        }
        this.pluginList = new ArrayList();
        this.applet = true;
        this.application = false;
        this.appletContext = applet.getAppletContext();

        this.basePath = applet.getCodeBase().toString(); // + "/";
        logger.info("setting base path to '" + this.basePath + "'");

        this.pluginPath = this.basePath + "plugins/";
        logger.info("setting plugins path to '" + this.pluginPath + "'");

        this.searchFormPath = this.basePath + "search/";
        logger.info("setting search forms path to '" + this.searchFormPath + "'");

        this.readAppletParameters(applet);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  applet  DOCUMENT ME!
     */
    private void readAppletParameters(final JApplet applet) {
        // configfile
        String parameter = applet.getParameter("configfile");
        if ((parameter != null) && (parameter.length() > 0)) {
            if (logger.isDebugEnabled()) {
                logger.debug("loading configfile from remote url '" + this.getBasePath() + parameter + "'");
            }
            try {
                final URL url = new URL(this.getBasePath() + parameter);
                this.load(new BufferedInputStream(url.openStream()));
            } catch (Exception exp) {
                if (logger.isDebugEnabled()) {
                    logger.debug("could not load configfile, trying to load file from local filesystem\n"
                                + exp.getMessage());
                }
                try {
                    final File file = new File(parameter);
                    this.load(new BufferedInputStream(new FileInputStream(file)));
                } catch (Exception ioexp) {
                    logger.error("could not load configfile, using default configuration\n" + ioexp.getMessage());
                    this.configure();
                }
            }
        }

        parameter = applet.getParameter("language");
        if ((parameter != null) && (parameter.length() > 0)) {
            ResourceManager.getManager().setLocale(new Locale(parameter));
        }

        parameter = applet.getParameter("plugins");
        if ((parameter != null) && (parameter.length() > 0)) {
            final StringTokenizer tokenizer = new StringTokenizer(parameter, ";");
            while (tokenizer.hasMoreTokens()) {
                final String plugin = this.pluginPath + tokenizer.nextToken().trim() + "/";
                logger.info("adding plugin '" + plugin + "'");
                pluginList.add(plugin);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  outStream  DOCUMENT ME!
     */
    public void save(final OutputStream outStream) {
        if (this.isSaveable()) {
            try {
                this.properties.store(outStream, HEADER);
            } catch (Exception exp) {
                logger.fatal("could not save properties: " + exp.getMessage(), exp);
            }
        } else {
            logger.error("could not save properties: properties not saveable");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void print() {
        properties.list(System.out);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static PropertyManager getManager() {
        return manager;
    }

    /**
     * Getter for property applet.
     *
     * @return  Value of property applet.
     */
    public boolean isApplet() {
        return this.applet;
    }

    /**
     * Getter for property application.
     *
     * @return  Value of property application.
     */
    public boolean isApplication() {
        return this.application;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getBasePath() {
        if (this.basePath == null) {
            this.basePath = System.getProperty("user.home") + System.getProperty("file.separator") + ".navigator"
                        + System.getProperty("file.separator");
            logger.info("no base path set, setting default base path to '" + this.basePath + "'");

            final File file = new File(this.basePath);
            if (!file.exists()) {
                logger.warn("base path does not exist, creating base path");
                if (!file.mkdirs()) {
                    logger.error("could not create base path");
                }
            }
        }

        return this.basePath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPluginPath() {
        if (this.pluginPath == null) {
            if (this.getBasePath().startsWith("http") || this.getBasePath().startsWith("file")) {
                this.pluginPath = this.getBasePath() + "plugins/";
            } else {
                this.pluginPath = this.getBasePath() + "plugins" + System.getProperty("file.separator");
            }

            logger.info("no plugin path set, setting default plugin path to '" + this.pluginPath + "'");
        }

        return this.pluginPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSearchFormPath() {
        if (this.searchFormPath == null) {
            if (this.getBasePath().startsWith("http")) {
                this.searchFormPath = this.basePath + "search/";
            } else {
                this.searchFormPath = this.basePath + "search" + System.getProperty("file.separator");
            }
            logger.info("no search form path set, setting default search form path to '" + this.searchFormPath + "'");
        }

        return this.searchFormPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProfilesPath() {
        if ((this.profilesPath == null) || this.profilesPath.equals("AUTO")) {
            if (this.getBasePath().startsWith("http")) {
                logger.info("no profiles path set and base path == URL, settting default profiles path to user.home");
                this.profilesPath = new StringBuffer().append(System.getProperty("user.home"))
                            .append(System.getProperty("file.separator"))
                            .append(".navigator")
                            .append(System.getProperty("file.separator"))
                            .append("profiles")
                            .append(System.getProperty("file.separator"))
                            .toString();
            } else {
                this.profilesPath = this.basePath + "profiles" + System.getProperty("file.separator");
            }

            logger.info("no profiles form path set, setting default search form path to '" + this.profilesPath + "'");

            final File file = new File(this.profilesPath);
            if (!file.exists()) {
                logger.warn("profiles path does not exist, creating base path");
                if (!file.mkdirs()) {
                    logger.error("could not create profiles path");
                }
            }
        }

        return this.profilesPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPluginListAvailable() {
        if (this.pluginList == null) {
            this.pluginList = new ArrayList();
            final File file = new File(this.pluginPath);
            if (file.exists() && file.isDirectory()) {
                final File[] plugins = file.listFiles();
                if ((plugins != null) && (plugins.length > 0)) {
                    for (int i = 0; i < plugins.length; i++) {
                        if (plugins[i].isDirectory()) {
                            if (!plugins[i].getName().equalsIgnoreCase("CVS")) {
                                final String plugin = plugins[i].getPath() + System.getProperty("file.separator");
                                logger.info("adding plugin '" + plugin + "'");
                                pluginList.add(plugin);
                            } else {
                                if (logger.isDebugEnabled()) {
                                    logger.warn("plugin directory with name 'CVS' found. ignoring plugin!");
                                }
                            }
                        }
                    }
                }
            } else {
                logger.warn("'" + this.pluginPath + "' does not exist or is no valid plugin directory");
            }
        }

        return (this.pluginList.size() > 0) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Iterator getPluginList() {
        if (this.isPluginListAvailable()) {
            return this.pluginList.iterator();
        } else {
            logger.warn("sorry, no plugins could be found");
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getProperties() {
        return this.properties;
    }

    /**
     * Getter for property appletContext.
     *
     * @return  Value of property appletContext.
     */
    public AppletContext getAppletContext() {
        if (this.appletContext == null) {
            return BrowserControl.getControl();
        } else {
            return this.appletContext;
        }
    }

    /**
     * Getter for property sharedProgressObserver.
     *
     * @return  Value of property sharedProgressObserver.
     */
    public synchronized ProgressObserver getSharedProgressObserver() {
        return this.sharedProgressObserver;
    }

    /**
     * Getter for property language.
     *
     * @return  Value of property language.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for property language.
     *
     * @param  language  New value of property language.
     */
    public void setLanguage(final String language) {
        if (language.trim().length() == 2) {
            this.language = language.toLowerCase();
        } else {
            logger.warn("malformed language code '" + language + "', setting to default (de)");
            this.language = "de";
        }
    }

    /**
     * Getter for property country.
     *
     * @return  Value of property country.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Setter for property country.
     *
     * @param  country  New value of property country.
     */
    public void setCountry(final String country) {
        if (country.length() == 2) {
            this.country = country.toUpperCase();
        } else {
            logger.warn("malformed country code '" + country + "', setting to default (de)");
            this.language = "de";
        }
    }

    /**
     * Getter for property locale.
     *
     * @return  Value of property locale.
     */
    public java.util.Locale getLocale() {
        return new Locale(this.getLanguage(), this.getCountry());
    }

    /**
     * Setter for property locale.
     *
     * @param  locale  New value of property locale.
     */
    public void setLocale(final java.util.Locale locale) {
        this.setLanguage(locale.getLanguage());
        this.setCountry(locale.getCountry());
    }

    /**
     * Getter for property editable.
     *
     * @return  Value of property editable.
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Setter for property editable.
     *
     * @param  editable  New value of property editable.
     */
    public void setEditable(final boolean editable) {
        this.editable = editable;
    }

    /**
     * Getter for property webstart.
     *
     * @return  Value of property webstart.
     */
    public boolean isWebstart() {
        return this.isApplet() & this.isApplication();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getHttpInterfacePort() {
        return httpInterfacePort;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  httpInterfacePort  DOCUMENT ME!
     */
    public void setHttpInterfacePort(final int httpInterfacePort) {
        this.httpInterfacePort = httpInterfacePort;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isAutoClose() {
        return autoClose;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  autoClose  DOCUMENT ME!
     */
    public void setAutoClose(final boolean autoClose) {
        this.autoClose = autoClose;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    private class ConnectionInfoChangeListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        /**
         * This method gets called when a bound property is changed.
         *
         * @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
         */
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (isConnectionInfoSaveable()) {
                properties.setProperty(evt.getPropertyName(), evt.getNewValue().toString());
            }
        }
    }
}
