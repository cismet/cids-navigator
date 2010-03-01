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
 *
 * @author  pascal
 */
public class ResourceManager
{
    private final static Logger logger = Logger.getLogger(ResourceManager.class);
    
    public final static String VALUE_STRING = "%VALUE%";
    public final static String ERROR_STRING = "[ ERROR ]";
    public final static char ERROR_MNEMONIC = 'X';
    
    private ImageIcon ERROR_ICON = null;
    
    private static ResourceManager manager = null;
    
    private ImageHashMap remoteIconCache = null;
    private final Hashtable localIconCache;
    
    private ResourceBundle resourcesBundle;
    private ResourceBundle errorcodesBundle;
    
    /** Creates a new instance of ResourceManager */
    private ResourceManager()
    {
        logger.info("creating new singleton resource manager instance");
        localIconCache = new Hashtable();
        ERROR_ICON = getIcon("x.gif");
        
        if(!setLocale(new Locale("de", "DE")))
        {
            logger.fatal("could not load default resource bundles");
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
        
    public /*synchronized*/ boolean setLocale(Locale locale)
    {
        logger.info("setting new locale '" + locale.getDisplayName() + "'");
        try
        {
            if(this.resourcesBundle == null || !this.getLocale().equals(locale))
            {
                if(logger.isDebugEnabled())logger.debug("loading resource bundle '" + this.getClass().getPackage().getName() + ".i18n.resources' for language '" + locale.getDisplayLanguage() + "'");
                resourcesBundle = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".i18n.resources", locale);

                if(logger.isDebugEnabled())logger.debug("loading resource bundle '" + this.getClass().getPackage().getName() + ".i18n.errorcodes' for language '" + locale.getDisplayLanguage() + "'");
                errorcodesBundle = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".i18n.errorcodes", locale);

                return true;
            }
        }
        catch(MissingResourceException mrex)
        {
            logger.error("could not load resource bundles for locale '" + locale.getDisplayName() + "'", mrex);
            
        }
        
        return false;
    }
    
    public Locale getLocale()
    {
        return resourcesBundle.getLocale();
    }
    
    
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
    
    public String getButtonText(String key)
    {
        return this.getString("button." + key);
    }
    
    public char getButtonMnemonic(String key)
    {
        return this.getMnemonic("button." + key + ".mnemonic");
        //return this.getString("button." + key + ".mnemonic").charAt(0);
    }
    
    public String getButtonTooltip(String key)
    {
        return this.getString("button." + key + ".tooltip");
    }
    
    public Icon getButtonIcon(String key)
    {
        return this.getIcon(this.getString("button." + key + ".icon"));
    }
    
    // menu + menu items .......................................................
    
    public String getMenuText(String key)
    {
        return this.getString("menu." + key);
    }
    
    public char getMenuMnemonic(String key)
    {
        return this.getMnemonic("menu." + key + ".mnemonic");
        //return this.getString("menu." + key + ".mnemonic").charAt(0);
    }
    
    public String getMenuTooltip(String key)
    {
        return this.getString("menu." + key + ".tooltip");
    }
    
    public Icon getMenuIcon(String key)
    {
        return this.getIcon(this.getString("menu." + key + ".icon"));
    }

    // Edit mbrill : Key Format Annahmen wurden entfernt um die Konsistenz der I18N Keys zu wahren
    public KeyStroke getMenuAccelerator(String key)
    {
        return KeyStroke.getKeyStroke(this.getString(key));
    }
    
    // exceptions ==============================================================
    
    public String getExceptionName(String errorcode)
    {
        return this.getException(errorcode + ".name");
    }
    
    public String getExceptionMessage(String errorcode)
    {
        return this.getException(errorcode + ".message");
    }
    
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
        if(logger.isDebugEnabled())logger.debug("searching icon '" + name + "'");
        
        if(remoteIconCache == null)
        {
            if(SessionManager.isConnected())
            {
                logger.info("initializing remote icon cache");
                
                try
                {
                    remoteIconCache = SessionManager.getProxy().getDefaultIcons();
                    
                    if(logger.isDebugEnabled())
                    {
                        logger.debug("remote icons cached: ");
                        Iterator keys = remoteIconCache.keySet().iterator();
                        
                        while(keys.hasNext())
                        {
                           logger.debug(keys.next());
                        }
                    }    
                }
                catch(ConnectionException cexp)
                {
                    logger.error("could not initializing remote icon cache: '" + cexp.getMessage() + "'");
                }
            }
        }
        
        if(remoteIconCache != null && remoteIconCache.containsKey(name))
        {
            if(logger.isDebugEnabled())logger.debug("icon '" + name + "' found in remote icon cache");
            return remoteIconCache.get(name);
        }
        else if(localIconCache.containsKey(name))
        {
            if(logger.isDebugEnabled())logger.debug("icon '" + name + "' found in local icon cache");
            return (ImageIcon)localIconCache.get(name);
        }
        else
        {
            ImageIcon icon = findIcon(name);
            if(icon != null)
            {
                if(logger.isDebugEnabled())logger.debug("icon '" + name + "' added to local icon cache");
                localIconCache.put(name, icon);
                return icon;
            }
            else
            {
                logger.error("!!!could not find icon !!! '" + name + "'");
                return ERROR_ICON;
            }
        }
    }
    
    private ImageIcon findIcon(String name)
    {
        try
        {
            URL iconURL = this.getClass().getResource("img/" + name);
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
            logger.error("could not load icon '" + name + "'", exp);
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
            if(logger.isDebugEnabled())logger.debug("loading navigator resource '" + PropertyManager.getManager().getBasePath() + "res/" + resourceName + "'");
            return this.getResourceAsStream(PropertyManager.getManager().getBasePath() + "res/" + resourceName);
        }
        catch(IOException ioexp)
        {
            if(logger.isDebugEnabled())logger.debug("loading navigator resource '" + this.getClass().getPackage().getName() + resourceName + "'");
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
            if(logger.isDebugEnabled())logger.debug("no remote url: '" + path + "' loading resource from local filesystem: '" + uexp.getMessage() + "'");
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
            if(logger.isDebugEnabled())logger.debug("no valid url: '" + path + "' trying to build url for local filesystem: " + uexp.getMessage());
            try
            {
                if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
                {
                    url = new URL("file:/" + path);
                }
                else
                {
                     url = new URL("file://" + path);
                }
            }
            catch(MalformedURLException exp)
            {
                logger.error("could not transform path '" + path + "' to local URL: " + exp.getMessage());
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
                logger.error("could not transform path '" + path + "' to URI : " + usexp.getMessage());
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
