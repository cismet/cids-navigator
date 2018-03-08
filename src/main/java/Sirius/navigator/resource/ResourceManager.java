/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.resource;

import Sirius.navigator.connection.*;
import Sirius.navigator.exception.*;

import Sirius.util.image.*;
import de.cismet.connectioncontext.AbstractConnectionContext.Category;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import org.apache.log4j.Logger;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

/**
 * The deprecated methods should not be used, because they are obsolete since the migration to the netbeans
 * internationalisation API.
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ResourceManager implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(ResourceManager.class);

    public static final String VALUE_STRING = "%VALUE%";   // NOI18N
    public static final String ERROR_STRING = "[ ERROR ]"; // NOI18N
    public static final char ERROR_MNEMONIC = 'X';         // NOI18N

    private static ResourceManager manager = null;

    //~ Instance fields --------------------------------------------------------

    private ImageIcon ERROR_ICON = null;

    private ImageHashMap remoteIconCache = null;
    private final Hashtable localIconCache;
    private final ConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

// private ResourceBundle resourcesBundle;
// private ResourceBundle errorcodesBundle;

    /**
     * Creates a new instance of ResourceManager.
     */
    private ResourceManager(final ConnectionContext connectionContext) {
        LOG.info("creating new singleton resource manager instance"); // NOI18N
        this.connectionContext = connectionContext;
        localIconCache = new Hashtable();
        ERROR_ICON = getIcon("x.gif");                                   // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final ResourceManager getManager() {
        if (manager == null) {
            manager = new ResourceManager(ConnectionContext.create(Category.STATIC, ResourceManager.class.getSimpleName()));
        }

        return manager;
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public String getString(final String key) {
        LOG.error(
            "The ResourceManager.getString() method was called. This method should not be used.",
            new Throwable()); // NOI18N
        return "";            // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public char getMnemonic(final String key) {
        LOG.error(
            "The ResourceManager.getMnemonic() method was called. This method should not be used.",
            new Throwable()); // NOI18N
        return 'a';
    }

    // buttons .......................................................
    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getButtonText(final String key) {
        return this.getString("button." + key); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public char getButtonMnemonic(final String key) {
        return this.getMnemonic("button." + key + ".mnemonic"); // NOI18N
        // return this.getString("button." + key + ".mnemonic").charAt(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getButtonTooltip(final String key) {
        return this.getString("button." + key + ".tooltip"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public Icon getButtonIcon(final String key) {
        return this.getIcon(this.getString("button." + key + ".icon")); // NOI18N
    }

    // menu + menu items .......................................................
    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getMenuText(final String key) {
        return this.getString("menu." + key); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public char getMenuMnemonic(final String key) {
        return this.getMnemonic("menu." + key + ".mnemonic"); // NOI18N
        // return this.getString("menu." + key + ".mnemonic").charAt(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getMenuTooltip(final String key) {
        return this.getString("menu." + key + ".tooltip"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       key  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public Icon getMenuIcon(final String key) {
        return this.getIcon(this.getString("menu." + key + ".icon")); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    // Edit mbrill : Key Format Annahmen wurden entfernt um die Konsistenz der I18N Keys zu wahren
    public KeyStroke getMenuAccelerator(final String key) {
        return KeyStroke.getKeyStroke(this.getString(key));
    }

    // exceptions ==============================================================

    /**
     * DOCUMENT ME!
     *
     * @param       errorcode  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getExceptionName(final String errorcode) {
        return this.getException(errorcode + ".name"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       errorcode  DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getExceptionMessage(final String errorcode) {
        return this.getException(errorcode + ".message"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param       errorcode  DOCUMENT ME!
     * @param       values     DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public String getExceptionMessage(final String errorcode, final String[] values) {
        String message = this.getExceptionMessage(errorcode);
        if ((values != null) && !message.equals(ERROR_STRING)) {
            for (int i = 0; i < values.length; i++) {
                message = message.replaceFirst(VALUE_STRING, values[i]);
            }
        }

        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   errorcode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getException(final String errorcode) {
        LOG.error(
            "The ResourceManager.getException() method was called. This method should not be used.",
            new Throwable()); // NOI18N
        return "";            // NOI18N
    }

    /**
     * ICON ====================================================================.
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ImageIcon getIcon(final String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("searching icon '" + name + "'"); // NOI18N
        }

        if (remoteIconCache == null) {
            if (SessionManager.isConnected()) {
                LOG.info("initializing remote icon cache"); // NOI18N

                try {
                    remoteIconCache = SessionManager.getProxy().getDefaultIcons(getConnectionContext());

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("remote icons cached: "); // NOI18N
                        final Iterator keys = remoteIconCache.keySet().iterator();

                        while (keys.hasNext()) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(keys.next());
                            }
                        }
                    }
                } catch (ConnectionException cexp) {
                    LOG.error("could not initializing remote icon cache: '" + cexp.getMessage() + "'"); // NOI18N
                }
            }
        }

        if ((remoteIconCache != null) && remoteIconCache.containsKey(name)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("icon '" + name + "' found in remote icon cache");    // NOI18N
            }
            return remoteIconCache.get(name);
        } else if (localIconCache.containsKey(name)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("icon '" + name + "' found in local icon cache");     // NOI18N
            }
            return (ImageIcon)localIconCache.get(name);
        } else {
            final ImageIcon icon = findIcon(name);
            if (icon != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("icon '" + name + "' added to local icon cache"); // NOI18N
                }
                localIconCache.put(name, icon);
                return icon;
            } else {
                LOG.error("!!!could not find icon !!! '" + name + "'");         // NOI18N
                return ERROR_ICON;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ImageIcon findIcon(final String name) {
        try {
            final URL iconURL = this.getClass().getResource("img/" + name); // NOI18N
            if (iconURL != null) {
                return new ImageIcon(iconURL);
            } else {
                return null;
            }
        } catch (Exception exp) {
            LOG.error("could not load icon '" + name + "'", exp);        // NOI18N
            return null;
        }
    }

    /**
     * resources in or in a subdirectory of the navigator's 'resource' directory. The returned resources will be
     * internationalised.
     *
     * @param   resourceName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public InputStream getNavigatorResourceAsStream(final String resourceName) throws IOException {
        final Iterator<String> it = org.openide.util.NbBundle.getLocalizingSuffixes();

        while (it.hasNext()) {
            final String suffix = it.next();
            String fileExtension = ""; // NOI18N
            String resourceNameBase = resourceName;
            final String resourceUri;

            if (resourceName.lastIndexOf(".") != -1) {                                       // NOI18N
                fileExtension = resourceName.substring(resourceName.lastIndexOf("."));       // NOI18N
                resourceNameBase = resourceName.substring(0, resourceName.lastIndexOf(".")); // NOI18N
            }

            resourceUri = PropertyManager.getManager().getBasePath() + "res/" + resourceNameBase + suffix
                        + fileExtension; // NOI18N

            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("loading navigator resource '" + resourceUri + "'"); // NOI18N
                }
                return this.getResourceAsStream(resourceUri);                         // NOI18N
            } catch (IOException ioexp) {
                LOG.warn("Resource with the uri '" + resourceUri + "' not found"); // NOI18N
                final String altResourceName = resourceNameBase + suffix + fileExtension;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("loading navigator resource '" + this.getClass().getPackage().getName() + "."
                                + altResourceName + "'");                             // NOI18N
                }
                final InputStream is = this.getClass().getResourceAsStream(altResourceName);

                if (is != null) {
                    return is;
                } else {
                    LOG.warn("Resource with name '" + altResourceName + "' not found"); // NOI18N
                }
            }
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading navigator resource '" + PropertyManager.getManager().getBasePath() + "res/"
                            + resourceName + "'");                                                               // NOI18N
            }
            return this.getResourceAsStream(PropertyManager.getManager().getBasePath() + "res/" + resourceName); // NOI18N
        } catch (IOException ioexp) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading navigator resource '" + this.getClass().getPackage().getName() + resourceName
                            + "'");                                                                              // NOI18N
            }
            return this.getClass().getResourceAsStream(resourceName);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public InputStream getResourceAsStream(final String path) throws IOException {
        try {
            final URL url = new URL(path);
            return url.openStream();
        } catch (MalformedURLException uexp) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no remote url: '" + path + "' loading resource from local filesystem: '"
                            + uexp.getMessage() + "'"); // NOI18N
            }
            final File file = new File(path);
            return new FileInputStream(file);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public URI pathToIURI(final String path) {
        URL url = null;

        try {
            url = new URL(path);
            // return url.openStream();
        } catch (MalformedURLException uexp) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no valid url: '" + path + "' trying to build url for local filesystem: "
                            + uexp.getMessage());                                                          // NOI18N
            }
            try {
                if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)                  // NOI18N
                {
                    url = new URL("file:/" + path);                                                        // NOI18N
                } else {
                    url = new URL("file://" + path);                                                       // NOI18N
                }
            } catch (MalformedURLException exp) {
                LOG.error("could not transform path '" + path + "' to local URL: " + exp.getMessage()); // NOI18N
            }
        }

        if (url != null) {
            try {
                return new URI(url.toString());
            } catch (URISyntaxException usexp) {
                LOG.error("could not transform path '" + path + "' to URI : " + usexp.getMessage()); // NOI18N
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String pathToIURIString(final String path) {
        final URI uri = this.pathToIURI(path);

        return (uri != null) ? uri.toString() : null;
    }

    // test ....................................................................

    /*public static void main(String args[])
     * { try {     org.apache.log4j.BasicConfigurator.configure();
     *
     * ResourceManager manager = ResourceManager.getManager(); manager.logger.setLevel(org.apache.log4j.Level.DEBUG);
     *
     * JButton jb = new JButton(manager.getButtonText("ok"), manager.getIcon("x.gif"));
     * jb.setMnemonic(manager.getButtonMnemonic("ok"));     jb.setToolTipText(manager.getButtonTooltip("ok"));
     *
     * JFrame jf = new JFrame("ResourceManager");     jf.setIconImage(manager.getIcon("x.gif").getImage());
     * jf.getContentPane().add(jb);     jf.setSize(300,200);     jf.setVisible(true);
     *
     * System.out.println(manager.pathToIURI("D:\\work\\web\\Sirius\\Navigator\\plugins/plugin.xsd")); }
     * catch(Throwable t) {     t.printStackTrace(); }}*/
}
