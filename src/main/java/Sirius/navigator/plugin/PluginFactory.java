package Sirius.navigator.plugin;
import Sirius.navigator.connection.SessionManager;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

import org.xml.sax.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.digester.*;

import Sirius.navigator.resource.*;
import Sirius.navigator.ui.embedded.*;
import Sirius.navigator.plugin.ui.*;
import Sirius.navigator.plugin.context.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.server.newuser.permission.PermissionHolder;





/**
 * blah<p>
 *
 * @version 1.0 02/15/2003
 * @author Pascal
 */
public final class PluginFactory
{
    /** singleton shared instance*/
    //private static PluginFactory factory = null;

    private static final ResourceManager resource = ResourceManager.getManager();

    private final Logger logger;
    private final Log log;
    
    private final PreloadPluginRuleSet preloadRuleSet;
    private final LoadPluginRuleSet loadRuleSet;
    
    private final String schemaLocation;
    
    /**
     * Creates the singleton shared instance of PluginFactory
     */
    protected PluginFactory()
    {
        logger = Logger.getLogger(this.getClass());
        //log = new Log4jFactory().getInstance("navigator.plugin.factory.digester");
        Logger digesterLogger = Logger.getLogger(PluginFactory.LoadPluginRuleSet.class);
        digesterLogger.setLevel(Level.WARN);
        log = new org.apache.commons.logging.impl.Log4JLogger(digesterLogger);
        
        preloadRuleSet = new PreloadPluginRuleSet();
        loadRuleSet = new LoadPluginRuleSet();
        
        schemaLocation = resource.pathToIURIString(PropertyManager.getManager().getPluginPath() + "plugin.xsd");
        
    }
    
    /**
     * Return the singleton PluginFactory instance
     *
     * @return the singleton PluginFactory instance
     */
    /*public final static PluginFactory getFactory()
    {
        if(factory == null)
        {
            factory = new PluginFactory();
        }
     
        return factory;
    }*/
    
    protected void preloadPlugin(PluginDescriptor descriptor, boolean validating) throws IOException, SAXException, URISyntaxException, FileNotFoundException
    {
        //InputStream inputStream = this.getXMLDescriptorInputStream(descriptor.getUrl());
        InputStream inputStream = this.getXMLDescriptorInputStream(descriptor.getPluginPath());
        Digester digester = new Digester();
        
        if(validating)
        {
            logger.info("plugin xml schema validation turned off to improve performance");
            //if(logger.isDebugEnabled())logger.debug("enabling xml schema validation: '" + this.getSchemaLocation() + "'");
            //digester.setSchema(this.getSchemaLocation());
            //digester.setValidating(validating);
        }
        
        digester.setLogger(log);
        digester.push(descriptor);
        digester.addRuleSet(preloadRuleSet);
        
        digester.parse(inputStream);
        inputStream.close();
    }
    
    protected void loadPlugin(PluginDescriptor descriptor) throws IOException, SAXException, URISyntaxException, FileNotFoundException, InvocationTargetException
    {
        //InputStream inputStream = this.getXMLDescriptorInputStream(descriptor.getUrl());
        InputStream inputStream = this.getXMLDescriptorInputStream(descriptor.getPluginPath());
        Digester digester = new Digester();
        
        digester.setLogger(log);
        digester.push(new PluginFactory.FactoryCore(descriptor));
        digester.addRuleSet(loadRuleSet);
        
        digester.parse(inputStream);
        inputStream.close();
    }
    
    public String getSchemaLocation()
    {
        return this.schemaLocation;
    }
    
    // helper methods ----------------------------------------------------------
    
    /**
     * Return the plugin descriptor XML configuration file (plugin.xml)
     *
     * @return the input stream, that reads the XML file
     */
    private InputStream getXMLDescriptorInputStream(URL pluginUrl) throws URISyntaxException, FileNotFoundException, IOException
    {
        if(logger.isDebugEnabled())logger.debug("loading plugin XML descriptor '" + pluginUrl.toString() + "/" + PluginDescriptor.XML_DESCRIPTOR + "'");
        
        File file = new File(new URI(pluginUrl.toString() + "/" + PluginDescriptor.XML_DESCRIPTOR));
        return new BufferedInputStream(new FileInputStream(file), 8192);
    }
    
    private InputStream getXMLDescriptorInputStream(String pluginPath) throws FileNotFoundException, MalformedURLException, IOException
    {
        String pluginDescriptorPath = pluginPath + PluginDescriptor.XML_DESCRIPTOR;
        if(logger.isDebugEnabled())logger.debug("loading plugin XML descriptor from remote URL '" + pluginDescriptorPath + "'");
        
        return new BufferedInputStream(resource.getResourceAsStream(pluginDescriptorPath), 16384);
        
        /*if(pluginPath.indexOf("http://") == 0 || pluginPath.indexOf("https://") == 0)
        {
            if(logger.isDebugEnabled())logger.debug("loading plugin XML descriptor from remote URL '" + pluginDescriptorPath + "'");
            URL pluginURL = new URL(pluginPath);
            return new BufferedInputStream(pluginURL.openStream());
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("loading plugin XML descriptor from local filesystem '" + pluginDescriptorPath + "'");
            File file = new File(pluginDescriptorPath);
            return new BufferedInputStream(new FileInputStream(file));
        }*/
    }
    
    // Digester RuleSets -------------------------------------------------------
    
    private class PreloadPluginRuleSet extends RuleSetBase
    {
        public void addRuleInstances(Digester digester)
        {
            // <plugin> ........................................................
            // set PluginDescriptor Bean properties (<plugin> attributes -> properties)
            digester.addSetProperties("plugin");
            
            // <metainfo> ......................................................
            // create PluginMetaInfo object
            digester.addObjectCreate("plugin/metainfo", "Sirius.navigator.plugin.PluginMetaInfo");
            // add PluginMetaInfo to PluginDescriptor
            digester.addSetNext("plugin/metainfo","setMetaInfo", "Sirius.navigator.plugin.PluginMetaInfo");
            // set PluginMetaInfo Bean properties (<plugin> attributes -> properties)
            //digester.addSetProperties("plugin/metainfo/properties");
            digester.addSetProperties("plugin/metainfo");
            // set PluginMetaInfo description (<description></description> -> description)
            digester.addCallMethod("plugin/metainfo/description", "setDescription", 0);
            
            // <rights> ........................................................
            // set the usernames
            digester.addCallMethod("plugin/deployment/rights/users/name", "addUser", 0);
            // set the usergroupnames
            digester.addCallMethod("plugin/deployment/rights/usergroups/name", "addUsergroup", 0);
            
            
            
        }
    }
    
    private class LoadPluginRuleSet extends RuleSetBase
    {
        public void addRuleInstances(Digester digester)
        {
            // <properties> section ............................................
            // capabilities
            digester.addCallMethod("plugin/properties/capabilities", "setCapabilities", 5, new String[] {"java.lang.Boolean", "java.lang.Boolean", "java.lang.Boolean", "java.lang.Boolean", "java.lang.Boolean"});
            digester.addCallParam("plugin/properties/capabilities/progressobservable", 0);
            digester.addCallParam("plugin/properties/capabilities/propertyobservable", 1);
            digester.addCallParam("plugin/properties/capabilities/internationalized", 2);
            digester.addCallParam("plugin/properties/capabilities/unloadable", 3);
            digester.addCallParam("plugin/properties/capabilities/deactivateable", 4);
            
            // internationalization
            digester.addCallMethod("plugin/properties/internationalization/defaultlocale", "setDefaultLocale", 0);
            // add PluginLocale object
            digester.addCallMethod("plugin/properties/internationalization/locales/locale", "addLocale", 4);
            digester.addCallParam("plugin/properties/internationalization/locales/locale/name", 0);
            digester.addCallParam("plugin/properties/internationalization/locales/locale/language", 1);
            digester.addCallParam("plugin/properties/internationalization/locales/locale/country", 2);
            digester.addCallParam("plugin/properties/internationalization/locales/locale/resourcefile", 3);
            
            // <deployment> section ............................................
            // set plugin class name
            digester.addCallMethod("plugin/deployment/pluginclass", "setClassName", 0);
            
            // add libraries (jar files)
            digester.addCallMethod("plugin/deployment/libraries/jar", "addLibrary", 0);
            
            // add the plugin parameters
            digester.addCallMethod("plugin/deployment/params/param", "addParameter", 2);
            digester.addCallParam("plugin/deployment/params/param/name", 0);
            digester.addCallParam("plugin/deployment/params/param/value", 1);
            
            // add the attribute id <-> attribute name mappings
            digester.addCallMethod("plugin/deployment/mappings/attribute", "addAttributeMapping", 2);
            digester.addCallParam("plugin/deployment/mappings/attribute/name", 0);
            digester.addCallParam("plugin/deployment/mappings/attribute/id", 1);
            
            // it's time to create the plugin
            digester.addCallMethod("plugin/deployment", "createPluginInstance");
            
            // .................................................................
            // add the method
            digester.addCallMethod("plugin/methods/method", "addMethod", 5, new String[]
            {"java.lang.String", "java.lang.String", "java.lang.String", "java.lang.Boolean", "java.lang.Long"});
            digester.addCallParam("plugin/methods/method/id", 0);
            digester.addCallParam("plugin/methods/method/name", 1);
            digester.addCallParam("plugin/methods/method/description", 2);
            digester.addCallParam("plugin/methods/method/multithreaded", 3);
            digester.addCallParam("plugin/methods/method/availability", 4);
            // register methods
            //digester.addCallMethod("plugin/methods", "registerMethods");
            //..................................................................
            
            // register methods
            // digester.addCallMethod("plugin/deployment/events", "registerMethods");
            // register event listeners
            // @deprecated! use PluginContext.getMetadata.addXXXListener()
            //digester.addCallMethod("plugin/deployment/events/selection/nodes", "registerNodeSelectionEvent", 0);
            //digester.addCallMethod("plugin/deployment/events/selection/attributes", "registerAttributeSelectionEvent", 0);
            
            // <ui><widget><component> section ---------------------------------
            
            // create PluginComponentProxy
            digester.addObjectCreate("plugin/ui/widgets/widget/component", "Sirius.navigator.plugin.PluginUIDescriptor");
            digester.addSetNext("plugin/ui/widgets/widget/component","addUIDescriptor", "Sirius.navigator.plugin.PluginUIDescriptor");
            digester.addCallMethod("plugin/ui/widgets/widget/component/id", "setId", 0);
            digester.addCallMethod("plugin/ui/widgets/widget/component/name", "setName", 0);
            digester.addCallMethod("plugin/ui/widgets/widget/component/tooltip", "setToolTip", 0);
            digester.addCallMethod("plugin/ui/widgets/widget/component/icon", "setIconName", 0);
            digester.addCallMethod("plugin/ui/widgets/widget/component/position", "setPosition", 0);
            digester.addCallMethod("plugin/ui/widgets/widget/component/preferredindex", "setPreferredIndex", 0, new String[]
            {"java.lang.Integer"});
            digester.addCallMethod("plugin/ui/widgets/widget/component/componentevents", "setPluginComponentEventsEnabled", 0, new String[]
            {"java.lang.Boolean"});
            // <ui><widget><container> section ---------------------------------
            // no container
            digester.addCallMethod("plugin/ui/widgets/widget/container/none", "addAsComponent");
            // panel
            digester.addCallMethod("plugin/ui/widgets/widget/container/panel/layout", "addAsPanel", 0);
            // scrollpane
            digester.addCallMethod("plugin/ui/widgets/widget/container/scrollpane", "addAsScrollPane");
            // floatingframe
            digester.addObjectCreate("plugin/ui/widgets/widget/container/floatingframe", "Sirius.navigator.plugin.ui.PluginFloatingFrameConfigurator");
            digester.addSetNext("plugin/ui/widgets/widget/container/floatingframe","addAsFloatingFrame", "Sirius.navigator.plugin.ui.PluginFloatingFrameConfigurator");
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/menubaravailable", "setMenuBarAvailable", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/swapmenubar", "setSwapMenuBar", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/disablemenubar", "setDisableMenuBar", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/toolbaravailable", "setToolBarAvailable", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/swaptoolbar", "setSwapToolBar", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/disabletoolbar", "setDisableToolBar", 0, new String[]
            {"java.lang.Boolean"});
            digester.addCallMethod("plugin/ui/widgets/widget/container/floatingframe/floatingevents", "setFloatingEventsEnabled", 0, new String[]
            {"java.lang.Boolean"});
            // <ui><actions><toolbar> section ----------------------------------
            digester.addObjectCreate("plugin/ui/actions/toolbar/properties", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/toolbar/properties","createPluginToolBar", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/toolbar/properties/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/toolbar/properties/floatable", "setFloatable", 0, new String[]
            {"java.lang.Boolean"});
            
            digester.addObjectCreate("plugin/ui/actions/toolbar/buttons/button", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/toolbar/buttons/button","addToolBarButton", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/toolbar/buttons/button/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/toolbar/buttons/button/mnemonic", "setMnemonic", 0);
            digester.addCallMethod("plugin/ui/actions/toolbar/buttons/button/tooltip", "setTooltip", 0);
            digester.addCallMethod("plugin/ui/actions/toolbar/buttons/button/icon", "setIconName", 0);
            digester.addCallMethod("plugin/ui/actions/toolbar/buttons/button/method", "setMethodId", 0);
            
            // <ui><actions><menubar> section ----------------------------------
            digester.addObjectCreate("plugin/ui/actions/menu/properties", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/menu/properties","createPluginMenu", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/menu/properties/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/menu/properties/mnemonic", "setMnemonic", 0);
            digester.addCallMethod("plugin/ui/actions/menu/properties/icon", "setIconName", 0);
            
            digester.addObjectCreate("plugin/ui/actions/menu/items/item", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/menu/items/item","addMenuItem", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/menu/items/item/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/mnemonic", "setMnemonic", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/tooltip", "setTooltip", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/icon", "setIconName", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/method", "setMethodId", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/accelerator", "setAccelerator", 0);
            digester.addCallMethod("plugin/ui/actions/menu/items/item/separator", "setSeparator", 0, new String[]
            {"java.lang.Boolean"});
            
            // <ui><actions><popup> section ----------------------------------
            digester.addObjectCreate("plugin/ui/actions/popupmenu/properties", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/popupmenu/properties","createPluginPopupMenu", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/popupmenu/properties/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/properties/mnemonic", "setMnemonic", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/properties/icon", "setIconName", 0);
            
            digester.addObjectCreate("plugin/ui/actions/popupmenu/items/item", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addSetNext("plugin/ui/actions/popupmenu/items/item","addPopupMenuItem", "Sirius.navigator.plugin.PluginActionDescriptor");
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/name", "setName", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/mnemonic", "setMnemonic", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/tooltip", "setTooltip", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/icon", "setIconName", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/method", "setMethodId", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/accelerator", "setAccelerator", 0);
            digester.addCallMethod("plugin/ui/actions/popupmenu/items/item/separator", "setSeparator", 0, new String[]
            {"java.lang.Boolean"});
            
            //@deprecated
            //digester.addCallMethod("plugin/deployment/methods/method/id", "setId", 0);
            //digester.addCallMethod("plugin/deployment/methods/method/name", "setName", 0);
            //digester.addCallMethod("plugin/deployment/methods/method/description", "setDescription", 0);
            /*digester.addObjectCreate("plugin/deployment/methods/method", "Sirius.navigator.pluginsupport.PluginMethodDescriptor");
            digester.addSetNext("plugin/deployment/methods/method","addMethodDescriptor", "Sirius.navigator.pluginsupport.PluginMethodDescriptor");
             
            digester.addBeanPropertySetter("plugin/deployment/methods/method/id");
            digester.addBeanPropertySetter("plugin/deployment/methods/method/name");
            digester.addBeanPropertySetter("plugin/deployment/methods/method/description");*/
            
            //digester.addCallMethod("plugin/ui/widgets/widget/component/id", "setId");
            //digester.addCallMethod("plugin/ui/widgets/widget/component/name", "setName");
            
        }
        
        //@deprecated
        /*
        private class PluginCreationFactory extends AbstractObjectCreationFactory
        {
            public Object createObject(Attributes attributes) throws java.lang.Exception
            {
                String classname = attributes.getValue("pluginclass");
                PluginFactory.this.logger.info("creating plugin instance '" + classname + "'");
         
                Class plugin = digester.getClassLoader().loadClass(classname);
                return plugin.newInstance();
            }
        }
         */
    }
    
    // helper factory ----------------------------------------------------------
    
    private class FactoryCore
    {
        /** a list of libraries, this plugin needs. */
        private ArrayList libraries = new ArrayList();
        
        private HashMap mappingTable = new HashMap();
        
        private HashMap paramTable = new HashMap();
        
        private HashMap methodDescriptors = null;
        
        private HashMap uiDescriptors = null;
        
        private HashMap pluginLocales = null;
        
        //private ArrayList actionDescriptors = new ArrayList();
        
        private PluginDescriptor descriptor = null;
        
        private String className = null;
        
        private PluginUIDescriptor uiDescriptor = null;
        
        private PluginMenu pluginMenu = null;
        private PluginMenu pluginPopupMenu = null;
        private EmbeddedToolBar pluginToolBar = null;
        private String defaultLocale = null;
        
        
        private FactoryCore(PluginDescriptor descriptor)
        {
            logger.info("new Plugin Factory Core instance created");
            this.descriptor = descriptor;
        }
        
        private PluginDescriptor getDescriptor()
        {
            return this.descriptor;
        }
        
        // <properties> set apabilities ========================================
        
        public void setCapabilities(Boolean progressObservable, Boolean propertyObservable, Boolean internationalized, Boolean unloadable, Boolean deactivateable)
        {
            if(logger.isDebugEnabled())logger.debug("setting plugin capabilities");
            this.descriptor.setProgressObservable(progressObservable.booleanValue());
            this.descriptor.setPropertyObservable(propertyObservable.booleanValue());
            this.descriptor.setInternationalized(internationalized.booleanValue());
            this.descriptor.setUnloadable(unloadable.booleanValue());
            this.descriptor.setDeactivateable(deactivateable.booleanValue());
        }
        
        // <properties> add locale .............................................
        
        public void setDefaultLocale(String defaultLocale)
        {
            this.defaultLocale = defaultLocale;
        }
        
        public void addLocale(String name, String language, String country, String resourceFile)
        {
            if(logger.isDebugEnabled())logger.debug("adding new plugin locale '" + name + "'");
            if(this.pluginLocales == null)
            {
                this.pluginLocales = new HashMap();
            }
            
            PluginLocale locale = new PluginLocale(name, language, country, resourceFile);
            this.pluginLocales.put(locale.getName(), locale);
        }
        
        // <deployment> section part 1 =========================================
        
        public void setClassName(String className)
        {
            if(logger.isDebugEnabled())logger.debug("setting plugin class name " + className + "'");
            this.className = className;
        }
        
        public void addLibrary(String jar)
        {
            if(logger.isDebugEnabled())logger.debug("adding library " + jar + "'");
            libraries.add(jar);
        }
        
        /**
         * Maps meta attribute names to attribute ids.<p>
         * The mapping is defined in the plugin descriptor (plugin.xml).
         *
         * @param attributeName the name of the meta attribute, e.g. 'Koordinate'
         * @param attributeId the id of the meta attribute, e.g. XYZ123
         */
        public void addAttributeMapping(String attributeName, String attributeId)
        {
            if(logger.isDebugEnabled())logger.debug("adding attribute mapping '" + attributeName + "' = '" + attributeId + "'");
            if(mappingTable.containsKey(attributeName))
            {
                if(logger.isDebugEnabled())logger.debug("attribute '" + attributeName + "' already in map, adding id to String array");
                
                //String[] currentIds = (String[])mappingTable.get(attributeName);
                //String[] newIds = new String[currentIds.length + 1];
                
                //System.arraycopy(currentIds, 0, newIds, 0, currentIds.length);
                //newIds[currentIds.length] = attributeId;
                
                Set idSet = (Set)mappingTable.get(attributeName);
                idSet.add(attributeId);
                
                //logger.fatal("size: " + ((Set)mappingTable.get(attributeName)).size());
                
            }
            else
            {
                //mappingTable.put(attributeName, new String[]{attributeId});
                
                HashSet idSet = new HashSet();
                idSet.add(attributeId);
                mappingTable.put(attributeName, idSet);
            }
        }
        
        public void addParameter(String paramName, String paramValue)
        {
            if(logger.isDebugEnabled())logger.debug("adding parameter " + paramName + "' = '" + paramValue + "'");
            paramTable.put(paramName, paramValue);
        }
        
        // create the plugin instance ==========================================
        
        public void createPluginInstance() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException
        {
            logger.info("creating new plugin '" + className + "' instance");
            
            PluginProgressObserver progressObserver = null;
            if(descriptor.isProgressObservable())
            {
                logger.debug("setting plugin progress observer");
                progressObserver = new PluginProgressObserver(this.descriptor.getName());
                PropertyManager.getManager().getSharedProgressObserver().setSubProgressObserver(progressObserver);
            }
            else if(logger.isDebugEnabled())
            {
                logger.debug("plugin " + descriptor.getName() + "' is not progressobservable (" + descriptor.isProgressObservable() + ")");
            }
                        
            PluginContext context = new PluginContext(this.descriptor, progressObserver, this.paramTable, this.mappingTable, this.pluginLocales, this.defaultLocale);
            descriptor.setContext(context);
            if(descriptor.isInternationalized() && !context.getI18n().isInternationalized())
            {
                logger.warn("internationalization broken, check plugin descriptor &  resource files");
                descriptor.setInternationalized(false);
            }
            
            // load jar files
            URL[] urls = new URL[libraries.size()+1];
            String jarBase = resource.pathToIURIString(descriptor.getPluginPath() + "lib/");
            
            // /res implements classpath ...
            urls[0] = new URL(resource.pathToIURIString(descriptor.getPluginPath() + "res/"));
            
  
            for (int i = 0; i < libraries.size(); i++)
            {
                if(logger.isDebugEnabled())logger.debug("loading plugin library: '" + jarBase + libraries.get(i).toString() + "'");
                urls[i+1] = new URL(jarBase + libraries.get(i).toString());
            }
            
            logger.debug("the current classloader is '" + this.getClass().getClassLoader().getClass().getName() + "'");
            //URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
            Class pluginClass=null;
            if (urls.length>0) { //vorher 1 geht aber trotzdem weil der URLClassLOader den normalen als Fallback hat ///HELL
                PluginClassLoader classLoader = new PluginClassLoader(urls, this.getClass().getClassLoader());
                logger.debug("the current url parent classloader is '" + classLoader.getParent().getClass().getName() + "'");
            
                URL[] jarURLs = classLoader.getURLs();
                for (int i = 0; i < jarURLs.length; i++)
                {
                    logger.debug("loadign jar file at '" + jarURLs[i] + "'");
                }
            
            
            // create plugin instance
            logger.info("creating plugin instance of class '" + className + "'");
            pluginClass = classLoader.loadClass(className);
            }
            else {
                pluginClass = this.getClass().getClassLoader().loadClass(className); // HELL \u00C4nderung quick & Dirty
            }
            
            
            // ...........................................
            Constructor pluginConstructor = pluginClass.getConstructor(new Class[] {Sirius.navigator.plugin.context.PluginContext.class});
            Object pluginObject = pluginConstructor.newInstance(new Object[] {descriptor.getContext()});
            // just to be shure:
            if(descriptor.isProgressObservable())
            {
                progressObserver.setFinished(true);
            }
            // ..................................................................
            logger.info("plugin instance created!");
            descriptor.setPlugin((PluginSupport)pluginObject);
        }
        
        // <deployment> section part 2 =========================================
        
        public void addMethod(String id, String name, String description, Boolean multithreaded, Long availability)
        {
            if(logger.isDebugEnabled())logger.debug("addingr new plugin method: id='" + id + "', name='" + name + "'");
            Object object = descriptor.getPlugin().getMethod(id);
            
            if(object != null && object instanceof PluginMethod)
            {
                if(methodDescriptors == null)
                {
                    methodDescriptors = new HashMap();
                    descriptor.setMethodDescriptors(methodDescriptors);
                }
                
                PluginMethodDescriptor methodDescriptor = new PluginMethodDescriptor(id, name, description, multithreaded.booleanValue(), availability.longValue(), (PluginMethod)object);
                methodDescriptors.put(methodDescriptor.getId(), methodDescriptor);
            }
            else
            {
                logger.error("plugin method '" + id + "' could not be found: '" + object + "'" );
            }
        }
        
        /*public void registerMethods()
        {
            if(logger.isDebugEnabled())logger.debug("registering plugin methods");
            descriptor.setMethodDescriptors(methodDescriptors);
        }*/
        
        // <ui> section part 1 =========================================
        
        public void addUIDescriptor(PluginUIDescriptor uiDescriptor)
        {
            if(logger.isDebugEnabled())logger.debug("adding plugin ui descriptor " + uiDescriptor.getName() + "'");
            if(uiDescriptors == null)
            {
                uiDescriptors = new HashMap();
                descriptor.setUIDescriptors(uiDescriptors);
            }
            
            this.uiDescriptor = uiDescriptor;
            uiDescriptors.put(this.uiDescriptor.getId(), this.uiDescriptor);
        }
        
        private PluginUI getPluginUI()
        {
            if(this.uiDescriptor != null)
            {
                PluginUI pluginUI = descriptor.getPlugin().getUI(uiDescriptor.getId());
                
                if(pluginUI != null)
                {
                    if(uiDescriptor.getIconName() != null)
                    {
                        uiDescriptor.setIcon(this.getImageIcon(uiDescriptor.getIconName()));
                    }
                    
                    return pluginUI;
                }
                else
                {
                    logger.error("plugin ui '" + uiDescriptor.getId() + "' could not be found");
                    uiDescriptors.remove(uiDescriptor.getId());
                    return null;
                }
            }
            else
            {
                logger.fatal("synchronization error: plugin ui descriptor generation failed");
                return null;
            }
        }
        
        public void addAsComponent()
        {
            PluginUI pluginUI = this.getPluginUI();
            
            if(pluginUI != null)
            {
                uiDescriptor.addAsComponent(pluginUI);
                uiDescriptor = null;
            }
        }
        
        public void addAsPanel(String layout)
        {
            PluginUI pluginUI = this.getPluginUI();
            
            if(pluginUI != null)
            {
                uiDescriptor.addAsPanel(pluginUI, layout);
                uiDescriptor = null;
            }
        }
        
        public void addAsScrollPane()
        {
            PluginUI pluginUI = this.getPluginUI();
            
            if(pluginUI != null)
            {
                uiDescriptor.addAsScrollPane(pluginUI);
                uiDescriptor = null;
            }
        }
        
        public void addAsFloatingFrame(PluginFloatingFrameConfigurator configurator)
        {
            PluginUI pluginUI = this.getPluginUI();
            configurator.setAdvancedLayout(PropertyManager.getManager().isAdvancedLayout());
            
            if(pluginUI != null)
            {
                if(pluginUI instanceof FloatingPluginUI)
                {
                    uiDescriptor.addAsFloatingFrame((FloatingPluginUI)pluginUI, configurator);
                }
                else
                {
                    logger.error("wrong plugin ui type'" + pluginUI.getClass().getName() + "',  'Sirius.navigator.plugin.interfaces.FloatingPluginUI' expected");
                    uiDescriptors.remove(uiDescriptor.getId());
                }
            }
        }
        
        // <ui> section part 2 <actions> =======================================
        
        public void createPluginToolBar(PluginActionDescriptor actionDescriptor)
        {
            logger.debug("createPluginToolBar, floatable: '" + actionDescriptor.isFloatable() + "'");
            pluginToolBar = new EmbeddedToolBar(descriptor.getId());
            pluginToolBar.setName(actionDescriptor.getName());
            pluginToolBar.setRollover(PropertyManager.getManager().isAdvancedLayout());
            pluginToolBar.setFloatable(actionDescriptor.isFloatable());
            descriptor.setPluginToolBar(pluginToolBar);
        }
        
        public void addToolBarButton(PluginActionDescriptor actionDescriptor)
        {
            if(descriptor.isPluginMethodAvailable(actionDescriptor.getMethodId()))
            {
                if(logger.isDebugEnabled())logger.debug("creating new plugin toolbar button: '" + actionDescriptor.getName() + "'");
                PluginToolBarButton button = new PluginToolBarButton(descriptor.getMethodDescriptor(actionDescriptor.getMethodId()).getMethod());
                
                this.setItemProperties(button, actionDescriptor);
                
                pluginToolBar.addButton(button);
            }
            else
            {
                logger.error("plugin toolbar button '" + actionDescriptor.getName() + "' refers to an unknown plugin method: '" + actionDescriptor.getMethodId() + "'");
            }
        }
        
        public void createPluginMenu(PluginActionDescriptor actionDescriptor)
        {
            logger.debug("createPluginMenu");
            pluginMenu = new PluginMenu(descriptor.getId(), actionDescriptor.getName());
            //pluginMenu.setText(actionDescriptor.getName());
            pluginMenu.setMnemonic(actionDescriptor.getMnemonic());
            pluginMenu.setIcon(this.getImageIcon(actionDescriptor.getIconName()));
            descriptor.setPluginMenu(pluginMenu);
        }
        
        public void addMenuItem(PluginActionDescriptor actionDescriptor)
        {
            if(descriptor.isPluginMethodAvailable(actionDescriptor.getMethodId()))
            {
                if(logger.isDebugEnabled())logger.debug("creating new plugin menu item: '" + actionDescriptor.getName() + "'");
                PluginMethodDescriptor methodDescriptor = descriptor.getMethodDescriptor(actionDescriptor.getMethodId());
                Sirius.server.localserver.method.Method method = null;
                String methodKey = actionDescriptor.getMethodId() + '@' + this.descriptor.getId();
                
                try
                {
                    method = SessionManager.getProxy().getMethod(methodKey);
                    
                    if(method != null)
                    {
                        if(method.getPermissions().hasPermission(SessionManager.getSession().getUser().getUserGroup().getKey(),PermissionHolder.READPERMISSION))
                        {
                            PluginMenuItem menuItem = new PluginMenuItem(methodDescriptor.getMethod(), method);
                            
                            this.setItemProperties(menuItem,  actionDescriptor);
                            KeyStroke accelerator = actionDescriptor.getAccelerator();
                            if(accelerator != null)
                            {
                                menuItem.setAccelerator(accelerator);
                            }
                            
                            pluginMenu.addItem(menuItem);
                            if(actionDescriptor.isSeparator())
                            {
                                pluginMenu.addSeparator();
                            }
                        }
                        else
                        {
                            if(logger.isDebugEnabled())logger.warn("no permission to show method '" + method.getKey() + "'");
                        }
                    }
                    else
                    {
                        logger.error("method '" + methodKey + "' is not registered, ignoring method");
                    }
                }
                catch(Throwable t)
                {
                    logger.warn("could not retrieve method '" + methodKey + "'");
                }
            }
            else
            {
                logger.error("plugin menu item '" + actionDescriptor.getName() + "' refers to an unknown plugin method: '" + actionDescriptor.getMethodId() + "'");
            }
        }
        
        public void createPluginPopupMenu(PluginActionDescriptor actionDescriptor)
        {
            logger.debug("createPluginPopupMenu");
            pluginPopupMenu = new PluginMenu(descriptor.getId(), actionDescriptor.getName());
            //pluginPopupMenu.setText(actionDescriptor.getName());
            pluginPopupMenu.setMnemonic(actionDescriptor.getMnemonic());
            pluginPopupMenu.setIcon(this.getImageIcon(actionDescriptor.getIconName()));
            descriptor.setPluginPopupMenu(pluginPopupMenu);
        }
        
        public void addPopupMenuItem(PluginActionDescriptor actionDescriptor)
        {
            if(descriptor.isPluginMethodAvailable(actionDescriptor.getMethodId()))
            {
                if(pluginPopupMenu == null)
                {
                    pluginPopupMenu = new PluginMenu(descriptor.getId(), descriptor.getMetaInfo().getName());
                    descriptor.setPluginPopupMenu(pluginPopupMenu);
                }
                
                if(logger.isDebugEnabled())logger.debug("creating new plugin menu item: '" + actionDescriptor.getName() + "'");
                //PluginMenuItem menuItem = new PluginMenuItem(descriptor.getMethodDescriptor(actionDescriptor.getMethodId()).getMethod());
                PluginMethodDescriptor methodDescriptor = descriptor.getMethodDescriptor(actionDescriptor.getMethodId());
                Sirius.server.localserver.method.Method method = null;
                String methodKey = actionDescriptor.getMethodId() + '@' + this.descriptor.getId();
                
                try
                {
                    method = SessionManager.getProxy().getMethod(methodKey);
                    if(method != null)
                    {
                        if(method.getPermissions().hasPermission(SessionManager.getSession().getUser().getUserGroup().getKey(),PermissionHolder.READPERMISSION))
                        {
                            PluginMenuItem menuItem = new PluginMenuItem(methodDescriptor.getMethod(), method);
                            
                            this.setItemProperties(menuItem,  actionDescriptor);
                            
                            KeyStroke accelerator = actionDescriptor.getAccelerator();
                            if(accelerator != null)
                            {
                                menuItem.setAccelerator(accelerator);
                            }
                            
                            //pluginPopupMenu.setAvailability(actionDescriptor.getAvailability());
                            
                            pluginPopupMenu.addItem(menuItem);
                            if(actionDescriptor.isSeparator())
                            {
                                pluginPopupMenu.addSeparator();
                            }
                        }
                        else
                        {
                            if(logger.isDebugEnabled())logger.warn("no permission to show method '" + method.getKey() + "'");
                        }
                    }
                    else
                    {
                        logger.error("method '" + methodKey  + "' is not available, ignoring method");
                    }
                }
                catch(Throwable t)
                {
                    logger.warn("could not retrieve method '" + methodKey + "'");
                }
            }
            else
            {
                logger.error("plugin menu item '" + actionDescriptor.getName() + "' refers to an unknown plugin method: '" + actionDescriptor.getMethodId() + "'");
            }
        }
        
        private void setItemProperties(AbstractButton item, PluginActionDescriptor actionDescriptor)
        {
            item.setText(actionDescriptor.getName());
            item.setMnemonic(actionDescriptor.getMnemonic());
            item.setToolTipText(actionDescriptor.getTooltip());
            item.setIcon(this.getImageIcon(actionDescriptor.getIconName()));
            /*Icon icon = descriptor.getPlugin().getImageIcon(actionDescriptor.getIconId());
            if(icon != null)
            {
                item.setIcon(icon);
            }*/
        }
        
        private ImageIcon getImageIcon(String icon)
        {
            String iconURL = null;
            if(icon.indexOf("/") == 0 || icon.indexOf("\\") == 0)
            {
                iconURL = descriptor.getContext().getEnvironment().getDocumentBase() + icon;
            }
            else
            {
                iconURL = descriptor.getContext().getEnvironment().getDocumentBase() + "/" + icon;
            }
            
            return descriptor.getContext().getResource().getImageIcon(iconURL);
        }
    }
    
    // test --------------------------------------------------------------------
    
    /*public static void main(String args[])
    {
        try
        {
            //BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("D:\\work\\web\\Sirius\\Navigator\\plugins\\plugin.xml"));
     
            String name = "example";
            PluginFactory factory = PluginFactory.getFactory();
            //factory.logger.setLevel(Level.DEBUG);
     
            PluginDescriptor descriptor = new PluginDescriptor(new URL("file:///D:/work/web/Sirius/Navigator/plugins"), name);
     
            factory.logger.info("preloading plugin '" +  name + "'");
            factory.preloadPlugin(descriptor);
     
            factory.logger.info("loading plugin '" +  name + "'");
            factory.loadPlugin(descriptor);
     
            System.out.println(descriptor.getName());
            System.out.println(descriptor.getMetaInfo().getDisplayname());
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }
    }*/
}
