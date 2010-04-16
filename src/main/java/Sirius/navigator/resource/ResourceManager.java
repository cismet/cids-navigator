package Sirius.navigator.resource;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.navigator.exception.*;
import Sirius.util.image.*;
import Sirius.navigator.connection.*;
import Sirius.navigator.connection.proxy.*;


/**
 * The deprecated methods should not be used, because they are obsolete since
 * the migration to the netbeans internationalisation API.
 * @author  pascal
 */
public class ResourceManager
{
    private final static Logger logger = Logger.getLogger(ResourceManager.class);

    public final static String VALUE_STRING = "%VALUE%";//NOI18N
    public final static String ERROR_STRING = "[ ERROR ]";//NOI18N
    public final static char ERROR_MNEMONIC = 'X';//NOI18N

    private ImageIcon ERROR_ICON = null;

    private static ResourceManager manager = null;

    private ImageHashMap remoteIconCache = null;
    private final Hashtable localIconCache;

    private ResourceBundle resourcesBundle;
    private ResourceBundle errorcodesBundle;

    /** Creates a new instance of ResourceManager */
    private ResourceManager()
    {
        logger.info("creating new singleton resource manager instance");//NOI18N
        localIconCache = new Hashtable();
        ERROR_ICON = getIcon("x.gif");//NOI18N

        if(!setLocale(new Locale("de", "DE")))//NOI18N
        {
            logger.fatal("could not load default resource bundles");//NOI18N
        }
    }

    public final static ResourceManager getManager()
    {
        if(manager == null)
        {
            manager = new ResourceManager();
        }

        return manager;
    }

    // i182 ====================================================================

    /**
     * @deprecated
     * @param locale
     * @return
     */
    public /*synchronized*/ boolean setLocale(Locale locale)
    {
//        logger.info("setting new locale '" + locale.getDisplayName() + "'");//NOI18N
//        try
//        {
//            if(this.resourcesBundle == null || !this.getLocale().equals(locale))
//            {
//                if(logger.isDebugEnabled())logger.debug("loading resource bundle '" + this.getClass().getPackage().getName() + ".i18n.resources' for language '" + locale.getDisplayLanguage() + "'");//NOI18N
//                resourcesBundle = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".i18n.resources", locale);//NOI18N
//
//                if(logger.isDebugEnabled())logger.debug("loading resource bundle '" + this.getClass().getPackage().getName() + ".i18n.errorcodes' for language '" + locale.getDisplayLanguage() + "'");//NOI18N
//                errorcodesBundle = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".i18n.errorcodes", locale);//NOI18N
//
//                return true;
//            }
//        }
//        catch(MissingResourceException mrex)
//        {
//            logger.error("could not load resource bundles for locale '" + locale.getDisplayName() + "'", mrex);//NOI18N
//
//        }
//
//        return false;

        return true;
    }

    /**
     * @deprecated use Locale.getDefault() instead
     *
     * @return
     */
    public Locale getLocale()
    {
//        return resourcesBundle.getLocale();
        return Locale.getDefault();
    }

    @Deprecated
    public String getString(String key)
    {
        try
        {
            return resourcesBundle.getString(key);
        }
        catch(MissingResourceException mrex)
        {
            logger.error(mrex.getMessage());
            return ERROR_STRING;
        }
    }

    @Deprecated
    public char getMnemonic(String key)
    {
        try
        {
            return resourcesBundle.getString(key).charAt(0);
        }
        catch(MissingResourceException mrex)
        {
            logger.error(mrex.getMessage());
            return ERROR_MNEMONIC;
        }
    }

    // buttons .......................................................
    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public String getButtonText(String key)
    {
        return this.getString("button." + key);//NOI18N
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public char getButtonMnemonic(String key)
    {
        return this.getMnemonic("button." + key + ".mnemonic");//NOI18N
        //return this.getString("button." + key + ".mnemonic").charAt(0);
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public String getButtonTooltip(String key)
    {
        return this.getString("button." + key + ".tooltip");//NOI18N
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public Icon getButtonIcon(String key)
    {
        return this.getIcon(this.getString("button." + key + ".icon"));//NOI18N
    }

    // menu + menu items .......................................................
    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public String getMenuText(String key)
    {
        return this.getString("menu." + key);//NOI18N
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public char getMenuMnemonic(String key)
    {
        return this.getMnemonic("menu." + key + ".mnemonic");//NOI18N
        //return this.getString("menu." + key + ".mnemonic").charAt(0);
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public String getMenuTooltip(String key)
    {
        return this.getString("menu." + key + ".tooltip");//NOI18N
    }

    /**
     * @deprecated
     *
     * @param key
     * @return
     */
    public Icon getMenuIcon(String key)
    {
        return this.getIcon(this.getString("menu." + key + ".icon"));//NOI18N
    }

    @Deprecated
    // Edit mbrill : Key Format Annahmen wurden entfernt um die Konsistenz der I18N Keys zu wahren
    public KeyStroke getMenuAccelerator(String key)
    {
        return KeyStroke.getKeyStroke(this.getString(key));
    }

    // exceptions ==============================================================

    /**
     * @deprecated
     * @param errorcode
     * @return
     */
    public String getExceptionName(String errorcode)
    {
        return this.getException(errorcode + ".name");//NOI18N
    }

    /**
     * @deprecated
     *
     * @param errorcode
     * @return
     */
    public String getExceptionMessage(String errorcode)
    {
        return this.getException(errorcode + ".message");//NOI18N
    }

    /**
     * @deprecated
     *
     * @param errorcode
     * @param values
     * @return
     */
    public String getExceptionMessage(String errorcode, String[] values)
    {
        String message = this.getExceptionMessage(errorcode);
        if(values != null && !message.equals(ERROR_STRING))
        {
            for(int i = 0; i < values.length; i++)
            {
                message = message.replaceFirst(VALUE_STRING, values[i]);
            }
        }

        return message;
    }


    private String getException(String errorcode)
    {
        try
        {
            return errorcodesBundle.getString(errorcode);
        }
        catch(MissingResourceException mrex)
        {
            logger.error(mrex.getMessage());
            return ERROR_STRING;
        }
    }

    // ICON ====================================================================

    public ImageIcon getIcon(String name)
    {
        if(logger.isDebugEnabled())logger.debug("searching icon '" + name + "'");//NOI18N

        if(remoteIconCache == null)
        {
            if(SessionManager.isConnected())
            {
                logger.info("initializing remote icon cache");//NOI18N

                try
                {
                    remoteIconCache = SessionManager.getProxy().getDefaultIcons();

                    if(logger.isDebugEnabled())
                    {
                        logger.debug("remote icons cached: ");//NOI18N
                        Iterator keys = remoteIconCache.keySet().iterator();

                        while(keys.hasNext())
                        {
                           logger.debug(keys.next());
                        }
                    }
                }
                catch(ConnectionException cexp)
                {
                    logger.error("could not initializing remote icon cache: '" + cexp.getMessage() + "'");//NOI18N
                }
            }
        }

        if(remoteIconCache != null && remoteIconCache.containsKey(name))
        {
            if(logger.isDebugEnabled())logger.debug("icon '" + name + "' found in remote icon cache");//NOI18N
            return remoteIconCache.get(name);
        }
        else if(localIconCache.containsKey(name))
        {
            if(logger.isDebugEnabled())logger.debug("icon '" + name + "' found in local icon cache");//NOI18N
            return (ImageIcon)localIconCache.get(name);
        }
        else
        {
            ImageIcon icon = findIcon(name);
            if(icon != null)
            {
                if(logger.isDebugEnabled())logger.debug("icon '" + name + "' added to local icon cache");//NOI18N
                localIconCache.put(name, icon);
                return icon;
            }
            else
            {
                logger.error("!!!could not find icon !!! '" + name + "'");//NOI18N
                return ERROR_ICON;
            }
        }
    }

    private ImageIcon findIcon(String name)
    {
        try
        {
            URL iconURL = this.getClass().getResource("img/" + name);//NOI18N
            if(iconURL != null)
            {
                return new ImageIcon(iconURL);
            }
            else
            {
                return null;
            }
        }
        catch(Exception exp)
        {
            logger.error("could not load icon '" + name + "'", exp);//NOI18N
            return null;
        }
    }


    /*public InputStream getI18nNavigatorResourceAsStream(String resourceName)  throws IOException
    {
        String localeString = this.getLocale().getLanguage() + "_" + this.getLocale().getCountry();
        String i18nResouceName

        if(logger.isDebugEnabled()




        try
        {
            if(logger.isDebugEnabled())logger.debug("loading navigator resource '" + PropertyManager.getManager().getBasePath() + "res/" + resourceName + "'");
            return this.getResourceAsStream(PropertyManager.getManager().getBasePath() + "res/" + resourceName);
        }
        catch(IOException ioexp)
        {
            if(logger.isDebugEnabled())logger.debug("loading navigator resource 'Sirius/Navigator/resource/" + resourceName + "'");
            return ClassLoader.getSystemResourceAsStream("Sirius/Navigator/resource/" + resourceName);
        }
    }*/

    /**
     * resources in or in a subdirectory of thwe navigator's 'resource' directory
     */
    public InputStream getNavigatorResourceAsStream(String resourceName)  throws IOException
    {
        try
        {
            if(logger.isDebugEnabled())logger.debug("loading navigator resource '" + PropertyManager.getManager().getBasePath() + "res/" + resourceName + "'");//NOI18N
            return this.getResourceAsStream(PropertyManager.getManager().getBasePath() + "res/" + resourceName);//NOI18N
        }
        catch(IOException ioexp)
        {
            if(logger.isDebugEnabled())logger.debug("loading navigator resource '" + this.getClass().getPackage().getName() + resourceName + "'");//NOI18N
            return this.getClass().getResourceAsStream(resourceName);
        }
    }

    public InputStream getResourceAsStream(String path)  throws IOException
    {
        try
        {
            URL url = new URL(path);
            return url.openStream();
        }
        catch(MalformedURLException uexp)
        {
            if(logger.isDebugEnabled())logger.debug("no remote url: '" + path + "' loading resource from local filesystem: '" + uexp.getMessage() + "'");//NOI18N
            File file = new File(path);
            return new FileInputStream(file);
        }
    }

    public URI pathToIURI(String path)
    {
        URL url = null;

        try
        {
            url = new URL(path);
            //return url.openStream();
        }
        catch(MalformedURLException uexp)
        {
            if(logger.isDebugEnabled())logger.debug("no valid url: '" + path + "' trying to build url for local filesystem: " + uexp.getMessage());//NOI18N
            try
            {
                if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)//NOI18N
                {
                    url = new URL("file:/" + path);//NOI18N
                }
                else
                {
                     url = new URL("file://" + path);//NOI18N
                }
            }
            catch(MalformedURLException exp)
            {
                logger.error("could not transform path '" + path + "' to local URL: " + exp.getMessage());//NOI18N
            }
        }

        if(url != null)
        {
            try
            {
                return new URI(url.toString());
            }
            catch(URISyntaxException usexp)
            {
                logger.error("could not transform path '" + path + "' to URI : " + usexp.getMessage());//NOI18N
            }
        }

        return null;
    }

        public String pathToIURIString(String path)
        {
            URI uri = this.pathToIURI(path);

            return uri != null ? uri.toString() : null;
        }

    // test ....................................................................


    /*public static void main(String args[])
    {
        try
        {
            org.apache.log4j.BasicConfigurator.configure();

            ResourceManager manager = ResourceManager.getManager();
            manager.logger.setLevel(org.apache.log4j.Level.DEBUG);

            JButton jb = new JButton(manager.getButtonText("ok"), manager.getIcon("x.gif"));
            jb.setMnemonic(manager.getButtonMnemonic("ok"));
            jb.setToolTipText(manager.getButtonTooltip("ok"));

            JFrame jf = new JFrame("ResourceManager");
            jf.setIconImage(manager.getIcon("x.gif").getImage());
            jf.getContentPane().add(jb);
            jf.setSize(300,200);
            jf.setVisible(true);

            System.out.println(manager.pathToIURI("D:\\work\\web\\Sirius\\Navigator\\plugins/plugin.xsd"));
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }*/

}
