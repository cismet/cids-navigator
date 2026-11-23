/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.cert.Certificate;

import java.util.Collections;
import java.util.prefs.Preferences;

import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigFactory;
import de.cismet.cids.server.ws.SSLConfigFactoryException;
import de.cismet.cids.server.ws.SSLConfigProvider;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = SSLConfigProvider.class)
public final class DefaultSSLConfigProvider implements SSLConfigProvider {

    //~ Static fields/initializers ---------------------------------------------

    public static final String SERVER_CERT_FILE_NAME = "server.cert.der";
    public static final String CLIENT_CERT_KEYSTORE_FILE_NAME = "client.keystore";
    public static final String FILE_SEP = System.getProperty("file.separator");
    private static final String EXTENSION = (((System.getProperty("directory.extension")) != null)
            ? (System.getProperty("directory.extension")) : "");
    public static final String CIDS_DIR = System.getProperty("user.home") + FILE_SEP + ".cids" + EXTENSION;
    public static final File LOCAL_SERVER_CERT_FILE = new File(CIDS_DIR + FILE_SEP
                    + SERVER_CERT_FILE_NAME);
    public static final File CLIENT_CERT_KEYSTORE_FILE = new File(CIDS_DIR + FILE_SEP
                    + CLIENT_CERT_KEYSTORE_FILE_NAME);
    public static final String CLIENT_CERT_PASS_PREFS_KEY = "CLIENT_CERT_PASS";
    public static final String SECONDARY_KEYSTORE = "de/cismet/security/secondary.jks";
    public static final String SECONDARY_KEYSTORE_PW = "de/cismet/security/secondary.pw";

    private static final transient Logger LOG = Logger.getLogger(DefaultSSLConfigProvider.class);

    //~ Instance fields --------------------------------------------------------

    Preferences cidsPrefs;
    char[] clientCertPWForKeystoreAndKey;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultSSLConfigProvider object.
     */
    public DefaultSSLConfigProvider() {
        final File cismetDir = new File(CIDS_DIR);
        if (!cismetDir.exists()) {
            final boolean success = cismetDir.mkdir();
            if (!success) {
                LOG.error("Could not create " + CIDS_DIR);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(CIDS_DIR + "created.");
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("CISMET_DIR=" + CIDS_DIR);
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public SSLConfig getSSLConfig() {
        cidsPrefs = Preferences.userNodeForPackage(DefaultSSLConfigProvider.class);
        clientCertPWForKeystoreAndKey = cidsPrefs.get(CLIENT_CERT_PASS_PREFS_KEY, "").toCharArray();

        if (LOG.isDebugEnabled()) {
            LOG.debug("retrieving default SSL config"); // NOI18N
        }

        final KeyStore serverKeystore;
        try {
            serverKeystore = SSLConfigFactory.getDefault()
                        .createKeystoreFromFile(null, null, SSLConfig.KEYSTORE_TYPE_JAVA);
            addServerCertificate(serverKeystore);
            addSecondaryCertificates(serverKeystore);

            if (serverKeystore.size() == 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("neither a server certificate nor secondary certificates found"); // NOI18N
                }
                return null;
            }
        } catch (final Exception ex) {
            LOG.warn("cannot create ssl config ", ex);                                          // NOI18N
            return null;
        }

        KeyStore clientKeystore = null;
        if (CLIENT_CERT_KEYSTORE_FILE.exists()) {
            try {
                clientKeystore = SSLConfigFactory.getDefault()
                            .createKeystoreFromFile(
                                    CLIENT_CERT_KEYSTORE_FILE,
                                    clientCertPWForKeystoreAndKey,
                                    SSLConfig.KEYSTORE_TYPE_JAVA);
            } catch (final SSLConfigFactoryException ex) {
                LOG.warn("cannot create ssl config ", ex); // NOI18N
                return null;
            }
        }

        return new DefaultSSLConfig(serverKeystore, clientKeystore, clientCertPWForKeystoreAndKey);
    }

    /**
     * Adds the server certificate to the given keystore. The local server certificate file is preferred, otherwise the
     * server certificate from the classpath is used, if present.
     *
     * @param  keystore  the keystore the certificate should be added to
     */
    private void addServerCertificate(final KeyStore keystore) {
        try {
            final Certificate serverCert;
            if (LOCAL_SERVER_CERT_FILE.exists()) {
                serverCert = SSLConfigFactory.getDefault()
                            .createCertificateFromFile(LOCAL_SERVER_CERT_FILE, SSLConfig.CERTIFICATE_TYPE_X509);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No local Server Certificate. Try if there is a server cert provided in the classpath"); // NOI18N
                }
                try(final InputStream is = getClass().getResourceAsStream(SERVER_CERT_FILE_NAME)) {
                    if (is == null) {
                        return;
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Getting server cert from the classpath");                                           // NOI18N
                    }
                    serverCert = SSLConfigFactory.getDefault()
                                .createCertificateFromStream(new BufferedInputStream(is),
                                        SSLConfig.CERTIFICATE_TYPE_X509);
                }
            }
            keystore.setCertificateEntry("cids-server-jetty", serverCert);                                             // NOI18N
        } catch (final Exception ex) {
            LOG.warn("cannot add server certificate", ex);                                                             // NOI18N
        }
    }

    /**
     * Adds the trusted certificates of the secondary keystore from the classpath (the same keystore the
     * WebAccessManager uses) to the given keystore. So the certificates must not be contained in the trust store of the
     * jvm.
     *
     * @param  keystore  the keystore the certificates should be added to
     */
    private void addSecondaryCertificates(final KeyStore keystore) {
        final ClassLoader classLoader = getClass().getClassLoader();
        try(final InputStream jksInputStream = classLoader.getResourceAsStream(SECONDARY_KEYSTORE);
                    final InputStream pwInputStream = classLoader.getResourceAsStream(SECONDARY_KEYSTORE_PW)) {
            if ((jksInputStream == null) || (pwInputStream == null)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("no secondary keystore found in the classpath");        // NOI18N
                }
                return;
            }
            final char[] pw = IOUtils.toString(pwInputStream, "UTF-8").toCharArray(); // NOI18N
            final KeyStore secondaryKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
            secondaryKeystore.load(jksInputStream, pw);

            for (final String alias : Collections.list(secondaryKeystore.aliases())) {
                if (secondaryKeystore.isCertificateEntry(alias)) {
                    keystore.setCertificateEntry("secondary-" + alias, secondaryKeystore.getCertificate(alias)); // NOI18N
                }
            }
        } catch (final Exception ex) {
            LOG.warn("cannot add the certificates of the secondary keystore", ex);                               // NOI18N
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class DefaultSSLConfig implements SSLConfig {

        //~ Instance fields ----------------------------------------------------

        private final transient KeyStore serverKeystore;
        private final transient KeyStore clientKeystore;
        private final char[] clientKeyPW;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DefaultSSLConfig object.
         *
         * @param  serverKeystore  DOCUMENT ME!
         * @param  clientKeystore  DOCUMENT ME!
         * @param  clientKeyPW     DOCUMENT ME!
         */
        DefaultSSLConfig(final KeyStore serverKeystore, final KeyStore clientKeystore, final char[] clientKeyPW) {
            this.serverKeystore = serverKeystore;
            this.clientKeystore = clientKeystore;
            this.clientKeyPW = clientKeyPW;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public KeyStore getServerKeystore() {
            return serverKeystore;
        }

        @Override
        public KeyStore getClientKeystore() {
            return clientKeystore;
        }

        @Override
        public char[] getServerKeyPW() {
            return null;
        }

        @Override
        public char[] getClientKeyPW() {
            return clientKeyPW;
        }
    }
}
