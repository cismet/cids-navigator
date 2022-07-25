/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import Sirius.server.middleware.interfaces.proxy.CatalogueService;
import Sirius.server.middleware.interfaces.proxy.MetaService;
import Sirius.server.middleware.interfaces.proxy.SearchService;
import Sirius.server.middleware.interfaces.proxy.UserService;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.newuser.User;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;

import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.Frame;

import java.rmi.Naming;
import java.rmi.Remote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.cids.tools.metaobjectrenderer.CidsObjectRendererFactory;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.ProxyHandler;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DevelopmentTools {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DevelopmentTools.class);

    public static final String RESTFUL_CALLSERVER_CALLSERVER = "http://localhost:9986/callserver/binary";
    private static final int DEFAULT_RENDERER_WIDTH = 800;
    private static final int DEFAULT_RENDERER_HEIGHT = 600;

    private static final Option OPTION__CALLSERVER = new Option("c", "callserver-url", true, "Callserver Url");
    private static final Option OPTION__DOMAIN = new Option("d", "domain", true, "Domain");
    private static final Option OPTION__GROUP = new Option("g", "usergroup", true, "Usergroup");
    private static final Option OPTION__USER = new Option("u", "username", true, "User Loginname");
    private static final Option OPTION__PASSWORD = new Option("x", "password", true, "Password");
    private static final Option OPTION__COMPRESSION = new Option(
            "z",
            "disable-gzip",
            false,
            "Disable Gzip compression");
    private static final Option OPTION__SHOW_MODE = new Option("m", "show-mode", true, "Show mode");
    private static final Option OPTION__OBJECT_ID = new Option("o", "object-id", true, "Object id(s)");
    private static final Option OPTION__TABLE_NAME = new Option("t", "table-name", true, "Table name");
    private static final Option OPTION__LOG4J_HOST = new Option("h", "log4j-host", true, "Log4j host");
    private static final Option OPTION__LOG4J_PORT = new Option("p", "log4j-port", true, "Log4j port");
    private static final Option OPTION__LOG4J_LEVEL = new Option("l", "log4j-level", true, "Log4j level");

    static {
        OPTION__CALLSERVER.setRequired(true);
        OPTION__DOMAIN.setRequired(true);
    }

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static enum ShowMode {

        //~ Enum constants -----------------------------------------------------

        RENDERER, EDITOR, AGGREGATION_RENDERER, AGGREGAGTION_EDITOR, NONE
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   group   DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     * @param   pass    DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void initSessionManagerFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass) throws Exception {
        System.out.println("start");
        // lookup des callservers
        final Remote r;
        final SearchService ss;
        final CatalogueService cat;
        final MetaService meta;
        final UserService us;
        final User u;

        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        r = (Remote)Naming.lookup("rmi://localhost/callServer");
        System.out.println("server gefunden");
        ss = (SearchService)r;
        cat = (CatalogueService)r;
        meta = (MetaService)r;
        us = (UserService)r;
        u = us.getUser(domain, group, domain, user, pass);
        System.out.println("user angemeldet");
        ConnectionSession session = null;
        ConnectionProxy proxy = null;
        final ConnectionInfo info = new ConnectionInfo();
        info.setCallserverURL("rmi://localhost/callServer");
        info.setUsername(user);
        info.setUsergroup(group);
        info.setPassword(pass);
        info.setUserDomain(domain);
        info.setUsergroupDomain(domain);

        final Connection connection = ConnectionFactory.getFactory()
                    .createConnection(
                        "Sirius.navigator.connection.RMIConnection",
                        DevelopmentTools.class.getSimpleName(),
                        info.getCallserverURL(),
                        false,
                        getConnectionContext());

        session = ConnectionFactory.getFactory().createSession(connection,
                info, true, getConnectionContext());
        proxy = ConnectionFactory.getFactory()
                    .createProxy(
                            "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler",
                            session,
                            getConnectionContext());
        System.out.println("sessionmanager initialisieren");
        SessionManager.init(proxy);

        ClassCacheMultiple.setInstance(domain, getConnectionContext());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void initSessionManagerFromRestfulConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled) throws Exception {
        initSessionManagerFromRestfulConnection(
            RESTFUL_CALLSERVER_CALLSERVER,
            domain,
            group,
            user,
            pass,
            compressionEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void initSessionManagerFromRestfulConnection(final String callserverUrl,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled) throws Exception {
        System.out.println("start");
        // lookup des callservers
        final Remote r;
        final SearchService ss;
        final CatalogueService cat;
        final MetaService meta;
        final UserService us;
        final User u;

        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final ConnectionInfo info = new ConnectionInfo();
        info.setCallserverURL(callserverUrl);
        info.setUsername(user);
        info.setUsergroup(group);
        info.setPassword(pass);
        info.setUserDomain(domain);
        info.setUsergroupDomain(domain);

        final Sirius.navigator.connection.Connection connection = ConnectionFactory.getFactory()
                    .createConnection(
                        "Sirius.navigator.connection.RESTfulConnection",
                        info.getCallserverURL(),
                        DevelopmentTools.class.getSimpleName(),
                        ProxyHandler.getInstance().getProxy(),
                        compressionEnabled,
                        getConnectionContext());
        final ConnectionSession session = ConnectionFactory.getFactory()
                    .createSession(connection, info, true, getConnectionContext());
        final ConnectionProxy conProxy = ConnectionFactory.getFactory()
                    .createProxy(
                        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler",
                        session,
                        getConnectionContext());
        SessionManager.init(conProxy);

        ClassCacheMultiple.setInstance(domain, getConnectionContext());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void initSessionManagerFromPureRestfulConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled) throws Exception {
        System.out.println("start");
        // lookup des callservers
        final Remote r;
        final SearchService ss;
        final CatalogueService cat;
        final MetaService meta;
        final UserService us;
        final User u;

        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final ConnectionInfo info = new ConnectionInfo();
        info.setCallserverURL("http://localhost:8890/");
        info.setUsername(user);
        info.setUsergroup(group);
        info.setPassword(pass);
        info.setUserDomain(domain);
        info.setUsergroupDomain(domain);

        final Sirius.navigator.connection.Connection connection = ConnectionFactory.getFactory()
                    .createConnection(
                        "Sirius.navigator.connection.PureRESTfulConnection",
                        info.getCallserverURL(),
                        DevelopmentTools.class.getSimpleName(),
                        ProxyHandler.getInstance().getProxy(),
                        compressionEnabled,
                        getConnectionContext());
        final ConnectionSession session = ConnectionFactory.getFactory()
                    .createSession(connection, info, true, getConnectionContext());
        final ConnectionProxy conProxy = ConnectionFactory.getFactory()
                    .createProxy(
                        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler",
                        session,
                        getConnectionContext());
        SessionManager.init(conProxy);

        ClassCacheMultiple.setInstance(domain, getConnectionContext());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRMIConnectionOnLocalhost(domain, group, user, pass);
        }
        return createCidsBeanFromCurrentSession(domain, table, objectId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static CidsBean createCidsBeanFromRestfulConnectionOnLocalhost(
            final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId) throws Exception {
        return createCidsBeanFromRestfulConnection(
                RESTFUL_CALLSERVER_CALLSERVER,
                domain,
                group,
                user,
                pass,
                true,
                table,
                objectId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromCurrentSession(
            final String domain,
            final String table,
            final int objectId) throws Exception {
        System.out.println("MO abfragen");

        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table, getConnectionContext());

        final MetaObject mo = SessionManager.getConnection()
                    .getMetaObject(SessionManager.getSession().getUser(),
                        objectId,
                        mc.getId(),
                        domain,
                        getConnectionContext());
        final CidsBean cidsBean = mo.getBean();
        System.out.println("cidsBean erzeugt");
        return cidsBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromRestfulConnection(final String callserverUrl,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final int objectId) throws Exception {
        if (!SessionManager.isInitialized()) {
            if ((user == null) || (pass == null)) {
                showSimpleLoginDialog(callserverUrl, domain, compressionEnabled, ConnectionContext.createDeprecated());
            } else {
                initSessionManagerFromRestfulConnection(callserverUrl, domain, group, user, pass, compressionEnabled);
            }
        }
        return createCidsBeanFromCurrentSession(domain, table, objectId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromRestfulConnection(final String callserverUrl,
            final String domain,
            final String group,
            final boolean compressionEnabled,
            final String table,
            final int objectId) throws Exception {
        return createCidsBeanFromRestfulConnection(
                callserverUrl,
                domain,
                group,
                null,
                null,
                compressionEnabled,
                table,
                objectId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromPureRestfulConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final boolean compressionEnabled) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromPureRestfulConnectionOnLocalhost(domain, group, user, pass, compressionEnabled);
        }
        return createCidsBeanFromCurrentSession(domain, table, objectId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserver          DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRestfulConnection(final String callserver,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table) throws Exception {
        return createCidsBeansFromRestfulConnection(
                callserver,
                domain,
                group,
                user,
                pass,
                compressionEnabled,
                table,
                0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserver          DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   limit               DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRestfulConnection(final String callserver,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final int limit) throws Exception {
        return createCidsBeansFromRestfulConnection(
                callserver,
                domain,
                group,
                user,
                pass,
                compressionEnabled,
                table,
                null,
                limit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain     DOCUMENT ME!
     * @param   table      DOCUMENT ME!
     * @param   condition  DOCUMENT ME!
     * @param   limit      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromCurrentSession(final String domain,
            final String table,
            final String condition,
            final int limit) throws Exception {
        String limitS = "";
        String whereS = "";

        if (limit > 0) {
            limitS = "LIMIT " + limit;
        }
        if ((condition != null) && (condition.trim().length() > 0)) {
            whereS = "WHERE " + condition;
        }
        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table, getConnectionContext());

        final String query = "SELECT "
                    + mc.getID()
                    + ", "
                    + mc.getPrimaryKey()
                    + " FROM "
                    + mc.getTableName()
                    + " "
                    + whereS
                    + " order by "
                    + mc.getPrimaryKey()
                    + " "
                    + limitS;
        final MetaObject[] metaObjects = SessionManager.getConnection()
                    .getMetaObjectByQuery(SessionManager.getSession().getUser(), query, getConnectionContext());
        final CidsBean[] cidsBeans = new CidsBean[metaObjects.length];
        for (int i = 0; i < metaObjects.length; i++) {
            final MetaObject metaObject = metaObjects[i];
            cidsBeans[i] = metaObject.getBean();
        }
        return cidsBeans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserver          DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   condition           DOCUMENT ME!
     * @param   limit               DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRestfulConnection(
            final String callserver,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final String condition,
            final int limit) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRestfulConnection(callserver, domain, group, user, pass, compressionEnabled);
        }
        return createCidsBeansFromCurrentSession(domain, table, condition, limit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   group   DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     * @param   pass    DOCUMENT ME!
     * @param   table   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table) throws Exception {
        return createCidsBeansFromRMIConnectionOnLocalhost(domain, group, user, pass, table, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   group   DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     * @param   pass    DOCUMENT ME!
     * @param   table   DOCUMENT ME!
     * @param   limit   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int limit) throws Exception {
        return createCidsBeansFromRMIConnectionOnLocalhost(domain, group, user, pass, table, null, limit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain     DOCUMENT ME!
     * @param   group      DOCUMENT ME!
     * @param   user       DOCUMENT ME!
     * @param   pass       DOCUMENT ME!
     * @param   table      DOCUMENT ME!
     * @param   condition  DOCUMENT ME!
     * @param   limit      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final String condition,
            final int limit) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRMIConnectionOnLocalhost(domain, group, user, pass);
        }
        return createCidsBeansFromCurrentSession(domain, table, condition, limit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   group   DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     * @param   pass    DOCUMENT ME!
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static ArrayList<ArrayList> executeServerSearch(final String domain,
            final String group,
            final String user,
            final String pass,
            final CidsServerSearch search) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRMIConnectionOnLocalhost(domain, group, user, pass);
        }
        final Collection res = SessionManager.getConnection()
                    .customServerSearch(SessionManager.getSession().getUser(), search, getConnectionContext());

        return (ArrayList<ArrayList>)res;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     * @param   c     DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void showReportForBeans(final String path, final Collection c) throws Exception {
        showReportForBeans(path, c, Collections.EMPTY_MAP);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path        DOCUMENT ME!
     * @param   c           DOCUMENT ME!
     * @param   parameters  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void showReportForBeans(final String path, final Collection c, final Map parameters)
            throws Exception {
        System.out.print("Lade JasperReport ...");
        final JasperReport jasperReport = (JasperReport)JRLoader.loadObject(DevelopmentTools.class.getResourceAsStream(
                    path));
        System.out.println(" geladen.\nErstelle Datenquelle ...");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(c);

        boolean hasEntries = false;
        try {
            hasEntries = dataSource.next();
        } finally {
            dataSource.moveFirst();
        }
        System.out.println("Datenquelle erstellt. Daten verfügbar? " + hasEntries + ".");
        if (!hasEntries) {
            return;
        }
        System.out.print("Fülle Report ...");
        // print aus report und daten erzeugen
        final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/thorsten/xxx.pdf");

        System.out.print(" gefüllt.\nZeige Report an ...");
        final JRViewer aViewer = new JRViewer(jasperPrint);
        final JFrame aFrame = new JFrame(path); // NOI18N
        aFrame.getContentPane().add(aViewer);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        aFrame.setSize(700, 970);
        final java.awt.Insets insets = aFrame.getInsets();
        aFrame.setSize(aFrame.getWidth() + insets.left + insets.right,
            aFrame.getHeight()
                    + insets.top
                    + insets.bottom
                    + 20);
        aFrame.setLocation((screenSize.width - aFrame.getWidth()) / 2,
            (screenSize.height - aFrame.getHeight())
                    / 2);
        aFrame.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     * @param   w  DOCUMENT ME!
     * @param   h  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JFrame showTestFrame(final JComponent c, final int w, final int h) {
        final JFrame jf = new JFrame("Test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(c, BorderLayout.CENTER);
        jf.setSize(
            w,
            h);
        jf.setVisible(
            true);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        jf.setBounds(
            (screenSize.width - w)
                    / 2,
            (screenSize.height - h)
                    / 2,
            w,
            h);
        return jf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     * @param   w         DOCUMENT ME!
     * @param   h         DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void createEditorInFrameFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        showEditor(cb, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cb  DOCUMENT ME!
     * @param   w   DOCUMENT ME!
     * @param   h   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static JFrame showEditor(final CidsBean cb, final int w, final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final JComponent c = CidsObjectEditorFactory.getInstance().getEditor(cb.getMetaObject());
        return showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     * @param   w         DOCUMENT ME!
     * @param   h         DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void createEditorInFrameFromRestfulConnection(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        createEditorFromRestfulConnection(
            RESTFUL_CALLSERVER_CALLSERVER,
            domain,
            group,
            user,
            pass,
            true,
            table,
            objectId,
            w,
            h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserver          DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     * @param   w                   DOCUMENT ME!
     * @param   h                   DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void createEditorFromRestfulConnection(final String callserver,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRestfulConnection(
                callserver,
                domain,
                group,
                user,
                pass,
                compressionEnabled,
                table,
                objectId);
        showEditor(cb, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserver          DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     * @param   w                   DOCUMENT ME!
     * @param   h                   DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void createEditorFromRestfulConnection(final String callserver,
            final String domain,
            final String group,
            final boolean compressionEnabled,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRestfulConnection(
                callserver,
                domain,
                group,
                compressionEnabled,
                table,
                objectId);
        showEditor(cb, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     * @param   title     DOCUMENT ME!
     * @param   w         DOCUMENT ME!
     * @param   h         DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void createRendererInFrameFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final String title,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        showRenderer(cb, title, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cb     DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     * @param   w      DOCUMENT ME!
     * @param   h      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static JFrame showRenderer(final CidsBean cb, final String title, final int w, final int h)
            throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final JComponent c = CidsObjectRendererFactory.getInstance().getSingleRenderer(cb.getMetaObject(), title);
        return showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beans  DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     * @param   w      DOCUMENT ME!
     * @param   h      DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void createAggregationRendererInFrameFromRMIConnectionOnLocalhost(final Collection<CidsBean> beans,
            final String title,
            final int w,
            final int h) throws Exception {
        showAggregationRenderer(beans, title, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beans  DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     * @param   w      DOCUMENT ME!
     * @param   h      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static JFrame showAggregationRenderer(final Collection<CidsBean> beans,
            final String title,
            final int w,
            final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final ArrayList<MetaObject> mos = new ArrayList<>(beans.size());

        for (final CidsBean b : beans) {
            mos.add(b.getMetaObject());
        }
        final JComponent c = CidsObjectRendererFactory.getInstance().getAggregationRenderer(mos, title);
        return showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     * @param   title               DOCUMENT ME!
     * @param   w                   DOCUMENT ME!
     * @param   h                   DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void createRendererFromRestfulConnection(
            final String callserverUrl,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final int objectId,
            final String title,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRestfulConnection(
                callserverUrl,
                domain,
                group,
                user,
                pass,
                true,
                table,
                objectId);
        showRenderer(cb, title, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   table               DOCUMENT ME!
     * @param   objectId            DOCUMENT ME!
     * @param   title               DOCUMENT ME!
     * @param   limit               DOCUMENT ME!
     * @param   condition           DOCUMENT ME!
     * @param   w                   DOCUMENT ME!
     * @param   h                   DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void createAggregationRendererFromRestfulConnection(
            final String callserverUrl,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final String table,
            final int objectId,
            final String title,
            final int limit,
            final String condition,
            final int w,
            final int h) throws Exception {
        final Collection<CidsBean> beans = Arrays.asList(createCidsBeansFromRestfulConnection(
                    callserverUrl,
                    domain,
                    group,
                    user,
                    pass,
                    compressionEnabled,
                    table,
                    condition,
                    limit));
        showAggregationRenderer(beans, title, w, h);
    }
    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   group     DOCUMENT ME!
     * @param   user      DOCUMENT ME!
     * @param   pass      DOCUMENT ME!
     * @param   table     DOCUMENT ME!
     * @param   objectId  DOCUMENT ME!
     * @param   w         DOCUMENT ME!
     * @param   h         DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Deprecated
    public static void createEditorInFrameFromRMIConnectionOnLocalhostInScrollPane(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        showEditor(cb, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static ConnectionContext getConnectionContext() {
        return ConnectionContext.createDummy();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     * @param  connectionContext   DOCUMENT ME!
     */
    public static void showSimpleLoginDialog(final String callServerURL,
            final String domain,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) {
        showRestfulConnectionLoginDialog(callServerURL, domain, null, compressionEnabled, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  callServerURL       DOCUMENT ME!
     * @param  domain              DOCUMENT ME!
     * @param  username            DOCUMENT ME!
     * @param  compressionEnabled  DOCUMENT ME!
     * @param  connectionContext   DOCUMENT ME!
     */
    public static void showRestfulConnectionLoginDialog(final String callServerURL,
            final String domain,
            final String username,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) {
        final CidsAuthentification cidsAuth = new CidsAuthentification(
                callServerURL,
                domain,
                DevelopmentTools.class.getSimpleName(),
                compressionEnabled,
                connectionContext);
        final JXLoginPane login = new JXLoginPane(cidsAuth);

        final JXLoginPane.JXLoginDialog loginDialog = new JXLoginPane.JXLoginDialog((Frame)null, login);

        login.setUserName(username);
        login.setPassword("".toCharArray());

        try {
            ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
        } catch (final Exception ex) {
            LOG.info("could nor request focus", ex);
        }
        StaticSwingTools.showDialog(loginDialog);

        if (loginDialog.getStatus() != JXLoginPane.Status.SUCCEEDED) {
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        System.out.println("##############");
        System.out.println("### Tester ###");

        final CommandLine cmd;
        try {
            cmd =
                new DefaultParser().parse(new Options().addOption(OPTION__CALLSERVER).addOption(OPTION__DOMAIN)
                            .addOption(OPTION__GROUP).addOption(OPTION__USER).addOption(OPTION__PASSWORD).addOption(
                        OPTION__COMPRESSION).addOption(OPTION__TABLE_NAME).addOption(OPTION__OBJECT_ID).addOptionGroup(
                        new OptionGroup().addOption(OPTION__LOG4J_HOST).addOption(OPTION__LOG4J_PORT).addOption(
                            OPTION__LOG4J_LEVEL)),
                    args);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
            System.exit(1);
            throw new IllegalStateException();
        }

        final String callserverUrl = cmd.getOptionValue(OPTION__CALLSERVER.getOpt());
        final String domain = cmd.getOptionValue(OPTION__DOMAIN.getOpt());
        final String user = cmd.hasOption(OPTION__USER.getOpt()) ? cmd.getOptionValue(OPTION__USER.getOpt()) : null;
        final String password = cmd.hasOption(OPTION__PASSWORD.getOpt()) ? cmd.getOptionValue(OPTION__PASSWORD.getOpt())
                                                                         : null;
        final String group = cmd.hasOption(OPTION__GROUP.getOpt()) ? cmd.getOptionValue(OPTION__GROUP.getOpt()) : null;
        final boolean compressionEnabled = cmd.hasOption(OPTION__COMPRESSION.getOpt());
        final String tableName = cmd.hasOption(OPTION__TABLE_NAME.getOpt())
            ? cmd.getOptionValue(OPTION__TABLE_NAME.getOpt()) : null;
        final String objectId = cmd.hasOption(OPTION__OBJECT_ID.getOpt())
            ? cmd.getOptionValue(OPTION__OBJECT_ID.getOpt()) : null;
        final String log4jHost = cmd.hasOption(OPTION__LOG4J_HOST.getOpt())
            ? cmd.getOptionValue(OPTION__LOG4J_HOST.getOpt()) : "localhost";
        final int log4jPort = cmd.hasOption(OPTION__LOG4J_PORT.getOpt())
            ? Integer.parseInt(cmd.getOptionValue(OPTION__LOG4J_PORT.getOpt())) : 4445;
        final String log4jLevel = cmd.hasOption(OPTION__LOG4J_LEVEL.getOpt())
            ? cmd.getOptionValue(OPTION__LOG4J_LEVEL.getOpt()) : "WARN";

        System.out.println(StringUtils.repeat("#", 20));
        System.out.println(OPTION__CALLSERVER.getDescription() + ": " + callserverUrl);
        System.out.println(OPTION__DOMAIN.getDescription() + ": " + domain);
        System.out.println(OPTION__USER.getDescription() + ": " + user);
        System.out.println(OPTION__PASSWORD.getDescription() + ": " + "<censored>");
        System.out.println(OPTION__GROUP.getDescription() + ": " + group);
        System.out.println(OPTION__COMPRESSION.getDescription() + ": " + compressionEnabled);
        System.out.println(OPTION__TABLE_NAME.getDescription() + ": " + tableName);
        System.out.println(OPTION__COMPRESSION.getDescription() + ": " + objectId);
        System.out.println(OPTION__LOG4J_HOST.getDescription() + ": " + log4jHost);
        System.out.println(OPTION__LOG4J_PORT.getDescription() + ": " + log4jPort);
        System.out.println(OPTION__LOG4J_LEVEL.getDescription() + ": " + log4jLevel);
        System.out.println(StringUtils.repeat("#", 20));
        System.out.println("start:");

        System.out.println("* log4j configuration");
        Log4JQuickConfig.configure4LumbermillOn(log4jHost, log4jPort, log4jLevel);

        try {
            if ((user == null) || (password == null)) {
                showRestfulConnectionLoginDialog(
                    callserverUrl,
                    domain,
                    user,
                    compressionEnabled,
                    getConnectionContext());
            } else {
                initSessionManagerFromRestfulConnection(
                    callserverUrl,
                    domain,
                    group,
                    user,
                    password,
                    compressionEnabled);
            }

            if (!SessionManager.isInitialized()) {
                throw new Exception("session not initialized");
            }
        } catch (final Exception ex) {
            LOG.error("error while initializing connection", ex);
        }

        System.out.println(String.format("* showing renderer for %s@%s", objectId, tableName));
        try {
            final CidsBean[] cidsBeans = createCidsBeansFromCurrentSession(
                    domain,
                    tableName,
                    String.format("id in (%s)", objectId),
                    0);
            if (cidsBeans == null) {
                throw new Exception("could not find any cidsBeans");
            } else if (cidsBeans.length > 1) {
                showRenderer(cidsBeans[0], "test", DEFAULT_RENDERER_WIDTH, DEFAULT_RENDERER_HEIGHT);
            } else {
                showAggregationRenderer(Arrays.asList(cidsBeans),
                    "test",
                    DEFAULT_RENDERER_WIDTH,
                    DEFAULT_RENDERER_HEIGHT);
            }
        } catch (final Exception ex) {
            final String message = String.format("Error while showing Renderer for %d@%s", objectId, tableName);
            LOG.error(message, ex);
        }
    }
}
