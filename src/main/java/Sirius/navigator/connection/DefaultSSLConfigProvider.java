/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
        SSLConfig sslConfig = null;
        final InputStream is;
        final BufferedInputStream bis;

        cidsPrefs = Preferences.userNodeForPackage(DefaultSSLConfigProvider.class);
        clientCertPWForKeystoreAndKey = cidsPrefs.get(CLIENT_CERT_PASS_PREFS_KEY, "").toCharArray();

        if (LOG.isDebugEnabled()) {
            LOG.debug("retrieving default SSL config"); // NOI18N
        }

        if (!LOCAL_SERVER_CERT_FILE.exists()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No local Server Certificate. Try if there is a server cert provided in the classpath"); // NOI18N
            }
            is = getClass().getResourceAsStream(SERVER_CERT_FILE_NAME);                                            // NOI18N
            if (is != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Getting server cert from the classpath");                                           // NOI18N
                }
                bis = new BufferedInputStream(is);
                if (!CLIENT_CERT_KEYSTORE_FILE.exists()) {
                    try {
                        sslConfig = SSLConfigFactory.getDefault().createClientConfig(bis);
                    } catch (final SSLConfigFactoryException ex) {
                        LOG.warn("cannot create config from default server certificate", ex);                      // NOI18N
                        sslConfig = null;
                    } finally {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (final IOException e) {
                                LOG.warn("cannot close certificate inputstream", e);                               // NOI18N
                            }
                        }
                    }
                } else {
                    try {
                        sslConfig = SSLConfigFactory.getDefault()
                                    .createClientConfig(
                                            bis,
                                            CLIENT_CERT_KEYSTORE_FILE.getAbsolutePath(),
                                            clientCertPWForKeystoreAndKey,
                                            clientCertPWForKeystoreAndKey);
                    } catch (final SSLConfigFactoryException ex) {
                        LOG.warn("cannot create config from default server certificate", ex);                      // NOI18N
                        sslConfig = null;
                    } finally {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (final IOException e) {
                                LOG.warn("cannot close certificate inputstream", e);                               // NOI18N
                            }
                        }
                    }
                }
            }
        } else {
            try {
                if (!CLIENT_CERT_KEYSTORE_FILE.exists()) {
                    sslConfig = SSLConfigFactory.getDefault()
                                .createClientConfig(new FileInputStream(LOCAL_SERVER_CERT_FILE));
                } else {
                    sslConfig = SSLConfigFactory.getDefault()
                                .createClientConfig(LOCAL_SERVER_CERT_FILE.getAbsolutePath(),
                                        CLIENT_CERT_KEYSTORE_FILE.getAbsolutePath(),
                                        clientCertPWForKeystoreAndKey,
                                        clientCertPWForKeystoreAndKey);
                }
            } catch (final Exception ex) {
                LOG.warn("cannot create ssl config ", ex);                                                         // NOI18N
                sslConfig = null;
            }
        }

        return sslConfig;
    }
}
