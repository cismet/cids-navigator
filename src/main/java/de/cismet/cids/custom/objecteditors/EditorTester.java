/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.objecteditors;

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaObject;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.cismet.cids.dynamics.CidsBeanStore;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;

import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.interaction.CismapBroker;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class EditorTester extends javax.swing.JFrame implements ClientConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EditorTester.class);

    private static final String SRS = "EPSG:4326";

    private static final String WMS_CALL =
        "http://osm.wheregroup.com/cgi-bin/osm_basic.xml?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&SERVICE=WMS&LAYERS=Grenzen,Landwirtschaft,Industriegebiet,Bauland,Gruenflaeche,unkultiviertes_Land,Park,Naherholungsgebiet,Wald,Wiese,Fussgaengerzone,Gebaeude,Wasser,Fluesse,Baeche,Kanal,Wasserbecken,Insel,Kueste,Inselpunkte,Strand,Fussgaengerweg,Radweg,Wege,Wohnstrasse,Zufahrtswege,einfache_Strasse,Landstrasse,Bundesstrasse,Kraftfahrstrasse,Autobahn,Ortschaft,Weiler,Stadtteil,Dorf,Stadt,Grossstadt,Bahn,Bahnhof,Airport,Kirchengelaende,Friedhof,Kirche,Graeber,copyright&STYLES=,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,&SRS=EPSG:4326&FORMAT=image/png;%20mode=24bit&BGCOLOR=0xffffff&TRANSPARENT=TRUE&EXCEPTIONS=application/vnd.ogc.se_inimage"
                + "&BBOX=<cismap:boundingBox>"
                + "&WIDTH=<cismap:width>"
                + "&HEIGHT=<cismap:height>";

    //~ Instance fields --------------------------------------------------------

    private ConnectionProxy proxy;
    private CidsBeanStore cidsBeanStore;
    private String className;
    private String domain;
    private Class editorClass;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JPanel mapPanel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form EditorTestFrame.
     *
     * @param   className    DOCUMENT ME!
     * @param   editorClass  DOCUMENT ME!
     * @param   domain       DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected EditorTester(final String className, final Class editorClass, final String domain) throws Exception {
        initComponents();

        this.className = className;
        this.editorClass = editorClass;
        this.domain = domain;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void init() throws Exception {
        initProxy();
        initMap();
        initEditor(editorClass);
        if (LOG.isDebugEnabled()) {
            LOG.debug("EditorTester constructor done");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ConnectionProxy getProxy() {
        return proxy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    protected abstract Connection getConnection() throws ConnectionException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected abstract ConnectionInfo getConnectionInfo();

    /**
     * DOCUMENT ME!
     *
     * @param   objectId  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void loadMetaObject(final int objectId) throws Exception {
        final int classId = ClassCacheMultiple.getMetaClass(domain, className).getId();
        final MetaObject metaObject = proxy.getMetaObject(objectId, classId, domain, getClientConnectionContext());

        if (metaObject != null) {
            cidsBeanStore.setCidsBean(metaObject.getBean());
        } else {
            cidsBeanStore.setCidsBean(null);
            throw new Exception("Metaobject nicht gefunden!");
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void initMap() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("initMap");
        }
        final MappingComponent mappingComponent = new MappingComponent();
        final Dimension d = new Dimension(300, 300);
        mappingComponent.setPreferredSize(d);
        mappingComponent.setSize(d);

        mapPanel.add(mappingComponent, BorderLayout.CENTER);

        final ActiveLayerModel mappingModel = new ActiveLayerModel();
        mappingModel.addHome(new XBoundingBox(6.7d, 49.1, 7.1d, 49.33d, SRS, false));
        mappingModel.setSrs(SRS);

        final SimpleWMS swms = new SimpleWMS(new SimpleWmsGetMapUrl(WMS_CALL));
        mappingModel.addLayer(swms);
        mappingComponent.setInteractionMode(MappingComponent.SELECT);
        mappingComponent.setMappingModel(mappingModel);
        mappingComponent.gotoInitialBoundingBox();
        mappingComponent.unlock();

        CismapBroker.getInstance().setMappingComponent(mappingComponent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   editorClass  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void initEditor(final Class editorClass) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("initEditor");
        }
        final Object editorO = editorClass.newInstance();
        if (editorO instanceof CidsBeanStore) {
            cidsBeanStore = (CidsBeanStore)editorO;
            if (editorO instanceof JComponent) {
                final JComponent editorComponent = (JComponent)editorO;
                editorPanel.add((JComponent)CidsObjectEditorFactory.getInstance().getComponentWrapper().wrapComponent(
                        editorComponent));
            } else {
                throw new Exception("editor class not instance of JComponent");
            }
            cidsBeanStore.setCidsBean(null);
        } else {
            throw new Exception("editor class not instance of CidsBeanStore");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void initProxy() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("initConnection");
        }
        final Connection connection = getConnection();
        final ConnectionInfo connectionInfo = getConnectionInfo();

        final ConnectionSession session = ConnectionFactory.getFactory()
                    .createSession(connection, connectionInfo, true);

        proxy = ConnectionFactory.getFactory()
                    .createProxy("Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler", session);

        SessionManager.init(proxy);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        editorPanel = new javax.swing.JPanel();
        mapPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(EditorTester.class, "EditorTester.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(1000, 800));

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                EditorTester.class,
                "EditorTester.editorPanel.TabConstraints.tabTitle"),
            editorPanel); // NOI18N

        mapPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                EditorTester.class,
                "EditorTester.mapPanel.TabConstraints.tabTitle"),
            mapPanel); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EditorTester.class, "EditorTester.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel());
        jSpinner1.setEditor(new javax.swing.JSpinner.NumberEditor(jSpinner1, ""));
        jSpinner1.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel1.add(jSpinner1);

        jToggleButton1.setText(org.openide.util.NbBundle.getMessage(
                EditorTester.class,
                "EditorTester.jToggleButton1.text")); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton1ActionPerformed(evt);
                }
            });
        jPanel1.add(jToggleButton1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton1ActionPerformed
        try {
            loadMetaObject((Integer)jSpinner1.getValue());
        } catch (Exception ex) {
            LOG.error("Fehler beim Laden des Objektes", ex);
            JOptionPane.showMessageDialog(
                this,
                "<html>Fehler beim Laden des Objektes.<br/>Siehe Log-Ausgabe.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                                                  //GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  tester  DOCUMENT ME!
     */
    protected static void run(final EditorTester tester) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        tester.init();
                        tester.setExtendedState(tester.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                        tester.setVisible(true);
                    } catch (Exception ex) {
                        LOG.fatal("ERROR", ex);
                        System.exit(1);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void run();

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }
}
