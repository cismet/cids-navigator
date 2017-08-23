/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import Sirius.server.middleware.types.LightweightMetaObject;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.error.ErrorInfo;

import java.awt.EventQueue;
import java.awt.Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.netutil.Proxy;

import de.cismet.tools.Converter;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class PerformanceComparisonDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(PerformanceComparisonDialog.class);
    private static final String SEPERATOR_SIGN = ";";

    //~ Instance fields --------------------------------------------------------

    private final HashMap<Integer, MetaClass> classMap = new HashMap<>();
    private final List<TestInfo> testInfos;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form PerformanceComparisonDialog.
     *
     * @param  testInfos  DOCUMENT ME!
     */
    public PerformanceComparisonDialog(final List<TestInfo> testInfos) {
        super(new javax.swing.JFrame(), true);

        this.testInfos = testInfos;

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(
                PerformanceComparisonDialog.class,
                "PerformanceComparisonDialog.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton1,
            org.openide.util.NbBundle.getMessage(
                PerformanceComparisonDialog.class,
                "PerformanceComparisonDialog.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        jPanel1.add(jButton1, gridBagConstraints);

        jProgressBar1.setEnabled(false);
        jProgressBar1.setString(org.openide.util.NbBundle.getMessage(
                PerformanceComparisonDialog.class,
                "PerformanceComparisonDialog.jProgressBar1.string")); // NOI18N
        jProgressBar1.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jProgressBar1, gridBagConstraints);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed

        jProgressBar1.setIndeterminate(true);
        jButton1.setEnabled(false);
        final LinkedHashMap<TestInfo, Map<String, Object>> resultsMaps = new LinkedHashMap<>();

        new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    for (final TestInfo testInfo : testInfos) {
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    jProgressBar1.setString("testing performance on " + testInfo.getName());
                                }
                            });
                        appendMessage("######");
                        appendMessage("testing now on " + testInfo.getName() + ": " + testInfo.getCallserverUrl() + " ("
                                    + testInfo.getDomain() + ")");
                        appendMessage("######");
                        appendMessage("* login...");
                        final boolean loggedIn = login(testInfo);
                        if (loggedIn) {
                            for (final String objectKey : testInfo.getObjectKeys()) {
                                try {
                                    final String[] objectAndClassAndDomain = objectKey.split("@");
                                    final int objectId = Integer.parseInt(objectAndClassAndDomain[0]);
                                    final int classId = Integer.parseInt(objectAndClassAndDomain[1]);
                                    final String domain = objectAndClassAndDomain[2];

                                    final MetaClass mc;
                                    if (classMap.containsKey(classId)) {
                                        mc = classMap.get(classId);
                                    } else {
                                        mc = ClassCacheMultiple.getMetaClass(testInfo.getDomain(), classId);
                                        classMap.put(classId, mc);
                                    }

                                    appendMessage("* testing performance on " + mc.getTableName() + " (" + classId
                                                + "), objectId: " + objectId);
                                    final Map<String, Object> resultsMap = new LinkedHashMap<>();

                                    appendMessage("* testing getMo...");
                                    final MetaObject mo = testGetMo(domain, classId, objectId, resultsMap);

                                    appendMessage("* testing serialization...");
                                    testSerialization(mo, testInfo.isCompressionEnabled(), resultsMap);

                                    appendMessage("* testing getBean...");
                                    final CidsBean bean = testGetBean(mo, resultsMap);

                                    appendMessage("* testing updateMo...");
                                    testUpdateMo(bean.getMetaObject(), resultsMap);

                                    appendMessage("* results for: " + mc.getTableName() + " (" + classId + ") ; "
                                                + objectId);
                                    for (final String key : resultsMap.keySet()) {
                                        final Object value = resultsMap.get(key);
                                        appendMessage("  - " + key + ": " + value);
                                    }

                                    resultsMaps.put(testInfo, resultsMap);
                                } catch (final Exception ex) {
                                    appendException(ex);
                                }
                            }
                        } else {
                            appendMessage("* login aborted !");
                            appendMessage(testInfo.getName() + " | login aborted");
                        }
                        appendMessage("tests on " + testInfo.getName() + " done");
                        appendMessage("");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    jProgressBar1.setIndeterminate(false);
                    jProgressBar1.setString("all tests done");
                    jButton1.setEnabled(true);
                }
            }.execute();
    } //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    private void appendMessage(final String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            jTextArea1.append(message + "\n");
            System.out.println(message);
            LOG.info(message);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        appendMessage(message);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ex  DOCUMENT ME!
     */
    private void appendException(final Exception ex) {
        if (SwingUtilities.isEventDispatchThread()) {
            jTextArea1.append(ex.getMessage() + "\n");
            jTextArea1.append("STACKTRACE: \n");
            for (final StackTraceElement ste : ex.getStackTrace()) {
                jTextArea1.append(ste.toString() + "\n");
            }

            Throwable cause = ex.getCause();
            while (cause != null) {
                jTextArea1.append("\n\n");
                jTextArea1.append("CAUSE: ");
                jTextArea1.append(cause.getMessage());
                jTextArea1.append("\n");
                jTextArea1.append("STACKTRACE: \n");
                for (final StackTraceElement ste : cause.getStackTrace()) {
                    jTextArea1.append(ste.toString() + "\n");
                }

                cause = cause.getCause();
            }
            jTextArea1.append("FAILURE\n\n");
        } else {
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        appendException(ex);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain      DOCUMENT ME!
     * @param   classId     DOCUMENT ME!
     * @param   objectId    DOCUMENT ME!
     * @param   resultsMap  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private MetaObject testGetMo(final String domain,
            final int classId,
            final int objectId,
            final Map<String, Object> resultsMap) throws Exception {
        final long timeMs = System.currentTimeMillis();
        final MetaObject mo = SessionManager.getConnection()
                    .getMetaObject(SessionManager.getSession().getUser(), objectId, classId, domain);
        final long durationMs = System.currentTimeMillis() - timeMs;

        resultsMap.put("getMo (duration in ms)", durationMs);

        return mo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mo                  DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   resultsMap          DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void testSerialization(final MetaObject mo,
            final boolean compressionEnabled,
            final Map<String, Object> resultsMap) throws Exception {
        final int bytes = Converter.serialiseToString(mo, compressionEnabled).getBytes().length;
        resultsMap.put("size (" + (compressionEnabled ? "compressed" : "uncompressed") + ", size in bytes)", bytes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mo          DOCUMENT ME!
     * @param   resultsMap  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private CidsBean testGetBean(final MetaObject mo, final Map<String, Object> resultsMap) throws Exception {
        final long timeMs = System.currentTimeMillis();
        final CidsBean cidsBean = mo.getBean();
        final long durationMs = System.currentTimeMillis() - timeMs;

        resultsMap.put("getBean (duration in ms)", durationMs);

        return cidsBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mo          DOCUMENT ME!
     * @param   resultsMap  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void testUpdateMo(final MetaObject mo, final Map<String, Object> resultsMap) throws Exception {
        mo.forceStatus(MetaObject.MODIFIED);

        final long timeMs = System.currentTimeMillis();
        SessionManager.getConnection().updateMetaObject(SessionManager.getSession().getUser(), mo, mo.getDomain());
        final long durationMs = System.currentTimeMillis() - timeMs;

        resultsMap.put("updateMo (duration in ms)", durationMs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();

//        final String[] args = new String[] {
//                "local;http://localhost:9986/callserver/binary;admin@Administratoren@WRRL_DB_MV;true;132@257@WRRL_DB_MV;132@257@WRRL_DB_MV",
////                "local;http://localhost:9986/callserver/binary;admin@Administratoren@WRRL_DB_MV;true;132@257@WRRL_DB_MV;132@257@WRRL_DB_MV",
//                "leela;https://wrrldbmv-callserver.cismet.de/callserver/binary;admin@Administratoren@WRRL_DB_MV;true;132@257@WRRL_DB_MV;132@257@WRRL_DB_MV",
////                "fiswrrl;https://fis-wasser-mv.de/callserver/binary;admin@Administratoren@WRRL_DB_MV;false;132@257@WRRL_DB_MV;132@257@WRRL_DB_MV"
//            };

        final List<TestInfo> testInfos = new ArrayList<>(args.length);
        for (final String arg : args) {
            final String[] infos = arg.split(SEPERATOR_SIGN);
            final String name = infos[0];
            final String callserver = infos[1];
            final String userDomainGroup = infos[2];
            final String[] userAndDomainAndGroup = userDomainGroup.split("@");
            final String user = userAndDomainAndGroup[0];
            final String group = userAndDomainAndGroup[1];
            final String domain = userAndDomainAndGroup[2];
            final boolean compressionEnabled = Boolean.parseBoolean(infos[3]);
            final List<String> objectKeys = new ArrayList<>();
            for (int objectKeyIndex = 4; objectKeyIndex < infos.length; objectKeyIndex++) {
                objectKeys.add(infos[objectKeyIndex]);
            }
            testInfos.add(new TestInfo(name, callserver, compressionEnabled, domain, group, user, objectKeys));
        }

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        final PerformanceComparisonDialog dialog = new PerformanceComparisonDialog(testInfos);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                                @Override
                                public void windowClosing(final java.awt.event.WindowEvent e) {
                                    System.exit(0);
                                }
                            });
                        dialog.pack();
                        StaticSwingTools.showDialog(dialog);
                        System.exit(0);
                    } catch (final Exception ex) {
                        LOG.fatal(ex, ex);
                        final ErrorInfo errorInfo = new ErrorInfo(
                                "errpr",
                                ex.getMessage(),
                                null,
                                null,
                                ex,
                                Level.ALL,
                                null);
                        JXErrorPane.showDialog(null, errorInfo);
                        System.exit(1);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   testInfo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean login(final TestInfo testInfo) {
        LightweightMetaObject.clearCache();
        final CidsAuthentification cidsAuth = new CidsAuthentification(testInfo.getCallserverUrl(),
                testInfo.getDomain(),
                testInfo.isCompressionEnabled());
        final JXLoginPane login = new JXLoginPane(cidsAuth);

        final JXLoginPane.JXLoginDialog loginDialog = new JXLoginPane.JXLoginDialog((Frame)null, login);
        login.setBannerText("Login on " + testInfo.getName());
        login.setUserName(testInfo.getUser() + "@" + testInfo.getGroup());
        login.setPassword("".toCharArray());

        try {
            ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
        } catch (final Exception ex) {
            LOG.info("could nor request focus", ex);
        }
        StaticSwingTools.showDialog(loginDialog);

        return loginDialog.getStatus() == JXLoginPane.Status.SUCCEEDED;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class CidsAuthentification extends LoginService {

        //~ Static fields/initializers -----------------------------------------

        public static final String CONNECTION_PROXY_CLASS =
            "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler";
        public static final String CONNECTION_CLASS = "Sirius.navigator.connection.RESTfulConnection";

        //~ Instance fields ----------------------------------------------------

        private final String callserverURL;
        private final String domain;
        private final boolean compressionEnabled;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CidsAuthentification object.
         *
         * @param  callserverURL       DOCUMENT ME!
         * @param  domain              DOCUMENT ME!
         * @param  compressionEnabled  DOCUMENT ME!
         */
        public CidsAuthentification(final String callserverURL, final String domain, final boolean compressionEnabled) {
            this.callserverURL = callserverURL;
            this.domain = domain;
            this.compressionEnabled = compressionEnabled;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   name      DOCUMENT ME!
         * @param   password  DOCUMENT ME!
         * @param   server    DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  Exception  DOCUMENT ME!
         */
        @Override
        public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
            final String[] split = name.split("@");
            final String user = (split.length > 1) ? split[0] : name;
            final String group = (split.length > 1) ? split[1] : null;

            try {
                final Connection connection = ConnectionFactory.getFactory()
                            .createConnection(
                                CONNECTION_CLASS,
                                callserverURL,
                                Proxy.fromPreferences(),
                                compressionEnabled);
                final ConnectionInfo connectionInfo = new ConnectionInfo();
                connectionInfo.setCallserverURL(callserverURL);
                connectionInfo.setPassword(new String(password));
                connectionInfo.setUserDomain(domain);
                connectionInfo.setUsergroup(group);
                connectionInfo.setUsergroupDomain(domain);
                connectionInfo.setUsername(user);
                final ConnectionSession session = ConnectionFactory.getFactory()
                            .createSession(connection, connectionInfo, true);
                final ConnectionProxy proxy = ConnectionFactory.getFactory()
                            .createProxy(CONNECTION_PROXY_CLASS, session);
                SessionManager.init(proxy);

                ClassCacheMultiple.setInstance(domain);
                return true;
            } catch (Throwable t) {
                LOG.error("Fehler beim Anmelden", t);
                return false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class TestInfo {

        //~ Instance fields ----------------------------------------------------

        private final String name;
        private final String callserverUrl;
        private final boolean compressionEnabled;
        private final String domain;
        private final String group;
        private final String user;
        private final List<String> objectKeys;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TestInfo object.
         *
         * @param  name                DOCUMENT ME!
         * @param  callserverUrl       DOCUMENT ME!
         * @param  compressionEnabled  DOCUMENT ME!
         * @param  domain              DOCUMENT ME!
         * @param  group               DOCUMENT ME!
         * @param  user                DOCUMENT ME!
         * @param  objectKeys          DOCUMENT ME!
         */
        public TestInfo(final String name,
                final String callserverUrl,
                final boolean compressionEnabled,
                final String domain,
                final String group,
                final String user,
                final List<String> objectKeys) {
            this.name = name;
            this.callserverUrl = callserverUrl;
            this.compressionEnabled = compressionEnabled;
            this.domain = domain;
            this.group = group;
            this.user = user;
            this.objectKeys = objectKeys;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getCallserverUrl() {
            return callserverUrl;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getDomain() {
            return domain;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isCompressionEnabled() {
            return compressionEnabled;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public List<String> getObjectKeys() {
            return objectKeys;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getGroup() {
            return group;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getUser() {
            return user;
        }
    }
}
