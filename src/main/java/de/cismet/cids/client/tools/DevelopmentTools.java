/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

import java.awt.BorderLayout;

import java.rmi.Naming;
import java.rmi.Remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.cids.tools.metaobjectrenderer.CidsObjectRendererFactory;

import de.cismet.cids.utils.jasperreports.CidsBeanDataSource;

import de.cismet.netutil.Proxy;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author thorsten
 * @version $Revision$, $Date$
 */
public class DevelopmentTools {

    //~ Methods ----------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
        r = (Remote) Naming.lookup("rmi://localhost/callServer");
        System.out.println("server gefunden");
        ss = (SearchService) r;
        cat = (CatalogueService) r;
        meta = (MetaService) r;
        us = (UserService) r;
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
                        info.getCallserverURL());

        session = ConnectionFactory.getFactory().createSession(connection,
                info, true);
        proxy = ConnectionFactory.getFactory()
                .createProxy(
                        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler",
                        session);
        System.out.println("sessionmanager initialisieren");
        SessionManager.init(proxy);

        ClassCacheMultiple.setInstance(domain);
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void initSessionManagerFromRestfulConnectionOnLocalhost(final String domain,
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
        final ConnectionInfo info = new ConnectionInfo();
        info.setCallserverURL("http://localhost:9986/callserver/binary");
        info.setUsername(user);
        info.setUsergroup(group);
        info.setPassword(pass);
        info.setUserDomain(domain);
        info.setUsergroupDomain(domain);

        final Sirius.navigator.connection.Connection connection = ConnectionFactory.getFactory()
                .createConnection(
                        "Sirius.navigator.connection.RESTfulConnection",
                        info.getCallserverURL(),
                        Proxy.fromPreferences());
        final ConnectionSession session = ConnectionFactory.getFactory().createSession(connection, info, true);
        final ConnectionProxy conProxy = ConnectionFactory.getFactory()
                .createProxy("Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler", session);
        SessionManager.init(conProxy);

        ClassCacheMultiple.setInstance(domain);
    }

    /**
     * 
     * @param domain
     * @param group
     * @param user
     * @param pass
     * @throws Exception 
     */
    public static void initSessionManagerFromPureRestfulConnectionOnLocalhost(final String domain,
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
                        Proxy.fromPreferences());
        final ConnectionSession session = ConnectionFactory.getFactory().createSession(connection, info, true);
        final ConnectionProxy conProxy = ConnectionFactory.getFactory()
                .createProxy("Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler", session);
        SessionManager.init(conProxy);

        ClassCacheMultiple.setInstance(domain);
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param objectId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
        System.out.println("MO abfragen");

        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table);

        final MetaObject mo = SessionManager.getConnection()
                .getMetaObject(SessionManager.getSession().getUser(), objectId, mc.getId(), domain);
        final CidsBean cidsBean = mo.getBean();
        System.out.println("cidsBean erzeugt");
        return cidsBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param objectId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static CidsBean createCidsBeanFromRestfulConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRestfulConnectionOnLocalhost(domain, group, user, pass);
        }
        System.out.println("MO abfragen");

        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table);

        final MetaObject mo = SessionManager.getConnection()
                .getMetaObject(SessionManager.getSession().getUser(), objectId, mc.getId(), domain);
        final CidsBean cidsBean = mo.getBean();
        System.out.println("cidsBean erzeugt");
        return cidsBean;
    }
    
    /**
     * 
     * @param domain
     * @param group
     * @param user
     * @param pass
     * @param table
     * @param objectId
     * @return
     * @throws Exception 
     */
    public static CidsBean createCidsBeanFromPureRestfulConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId) throws Exception {
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromPureRestfulConnectionOnLocalhost(domain, group, user, pass);
        }
        System.out.println("MO abfragen");

        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table);

        final MetaObject mo = SessionManager.getConnection()
                .getMetaObject(SessionManager.getSession().getUser(), objectId, mc.getId(), domain);
        final CidsBean cidsBean = mo.getBean();
        System.out.println("cidsBean erzeugt");
        return cidsBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param limit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param condition DOCUMENT ME!
     * @param limit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static CidsBean[] createCidsBeansFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
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
        if (!SessionManager.isInitialized()) {
            initSessionManagerFromRMIConnectionOnLocalhost(domain, group, user, pass);
        }
        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, table);

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
                .getMetaObjectByQuery(SessionManager.getSession().getUser(), query);
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
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param search DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
                .customServerSearch(SessionManager.getSession().getUser(), search);

        return (ArrayList<ArrayList>) res;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @Deprecated
    public static void showReportForCidsBeans(final String path,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final String table) throws Exception {
        System.out.print("Lade JasperReport ...");
        final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(DevelopmentTools.class.getResourceAsStream(
                path));
        System.out.println(" geladen.\nErstelle Datenquelle ...");
        final JRRewindableDataSource dataSource = new CidsBeanDataSource(createCidsBeansFromRMIConnectionOnLocalhost(
                domain,
                group,
                user,
                pass,
                table));
        boolean hasEntries = false;
        try {
            hasEntries = dataSource.next();
        } catch (JRException e) {
        } finally {
            dataSource.moveFirst();
        }
        System.out.println("Datenquelle erstellt. Daten verfügbar? " + hasEntries + ".");
        if (!hasEntries) {
            return;
        }
        System.out.print("Fülle Report ...");
        // print aus report und daten erzeugen
        final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
        System.out.print(" gefüllt.\nZeige Report an ...");
        final JRViewer aViewer = new JRViewer(jasperPrint);
        final JFrame aFrame = new JFrame(path); // NOI18N
        aFrame.getContentPane().add(aViewer);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        aFrame.setSize(screenSize.width / 2, screenSize.height / 2);
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
     * @param path DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void showReportForBeans(final String path, final Collection c) throws Exception {
        showReportForBeans(path, c, Collections.EMPTY_MAP);
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param parameters DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void showReportForBeans(final String path, final Collection c, final Map parameters)
            throws Exception {
        System.out.print("Lade JasperReport ...");
        final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(DevelopmentTools.class.getResourceAsStream(
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
     * @param path DOCUMENT ME!
     * @param cidsBeans DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    @Deprecated
    public static void showReportForCidsBeans(final String path,
            final CidsBean[] cidsBeans) throws Exception {
        System.out.print("Lade JasperReport ...");
        final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(DevelopmentTools.class.getResourceAsStream(
                path));
        System.out.println(" geladen.\nErstelle Datenquelle ...");
        final JRRewindableDataSource dataSource = new CidsBeanDataSource(cidsBeans);
        boolean hasEntries = false;
        try {
            hasEntries = dataSource.next();
        } catch (JRException e) {
        } finally {
            dataSource.moveFirst();
        }
        System.out.println("Datenquelle erstellt. Daten verfügbar? " + hasEntries + ".");
        if (!hasEntries) {
            return;
        }
        System.out.print("Fülle Report ...");
        // print aus report und daten erzeugen
        final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
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
     * @param c DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public static void showTestFrame(final JComponent c, final int w, final int h) {
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
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param objectId DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void createEditorInFrameFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        final JComponent c = CidsObjectEditorFactory.getInstance().getEditor(cb.getMetaObject());
        showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param objectId DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void createRendererInFrameFromRMIConnectionOnLocalhost(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final String title,
            final int w,
            final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        final JComponent c = CidsObjectRendererFactory.getInstance().getSingleRenderer(cb.getMetaObject(), title);
        showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param beans DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void createAggregationRendererInFrameFromRMIConnectionOnLocalhost(final Collection<CidsBean> beans,
            final String title,
            final int w,
            final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final ArrayList<MetaObject> mos = new ArrayList<MetaObject>(beans.size());

        for (final CidsBean b : beans) {
            mos.add(b.getMetaObject());
        }
        final JComponent c = CidsObjectRendererFactory.getInstance().getAggregationRenderer(mos, title);
        showTestFrame(c, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param domain DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param pass DOCUMENT ME!
     * @param table DOCUMENT ME!
     * @param objectId DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void createEditorInFrameFromRMIConnectionOnLocalhostInScrollPane(final String domain,
            final String group,
            final String user,
            final String pass,
            final String table,
            final int objectId,
            final int w,
            final int h) throws Exception {
        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"); // NOI18N
        final String heavyComps = System.getProperty("contains.heavyweight.comps");
        if ((heavyComps != null) && heavyComps.equals("true")) {
            com.jgoodies.looks.Options.setPopupDropShadowEnabled(false);
        }
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        final CidsBean cb = createCidsBeanFromRMIConnectionOnLocalhost(domain, group, user, pass, table, objectId);
        final JComponent c = CidsObjectEditorFactory.getInstance().getEditor(cb.getMetaObject());
        final JScrollPane jsp = new JScrollPane(c);
        showTestFrame(jsp, w, h);
    }
}
