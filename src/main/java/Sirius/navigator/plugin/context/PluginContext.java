package Sirius.navigator.plugin.context;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.server.middleware.types.*;
//import Sirius.server.search.*;
//import Sirius.server.search.wundasearch.*;
//import Sirius.server.search.wundasearch.types.*;
import Sirius.navigator.search.dynamic.*;
import Sirius.navigator.tools.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.types.iterator.*;
import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.plugin.listener.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.method.*;
//import Sirius.navigator.ui.progress.ProgressObservable;
//import Sirius.navigator.ui.progress.*;

/**
 *
 * @author  pascal
 */
public class PluginContext
{
    public final static String CATALOGUE_TREE = ComponentRegistry.CATALOGUE_TREE;
    public final static String SEARCHRESULTS_TREE = ComponentRegistry.SEARCHRESULTS_TREE;
    
    private final static Logger logger = Logger.getLogger(PluginContext.class);
    
    private final static PropertyManager properties = PropertyManager.getManager();
    private final static ResourceManager resources = ResourceManager.getManager();
    
    //private final String pluginId;
    //private final String pluginBasePath;
    
    /** Holds value of property environment. */
    private PluginContext.Environment environment;
    
    /** Holds value of property metadata. */
    private PluginContext.Metadata metadata;
    
    /** Holds value of property resource. */
    private PluginContext.Resource resource;
    
    /** Holds value of property userInterface. */
    private PluginContext.UserInterface userInterface;
    
    private final PluginDescriptor pluginDescriptor;
    
    /** Holds value of property search. */
    private Search search;
    
    /** Holds value of property i18n. */
    private PluginContext.I18n i18n = null;
    
    /** Creates a new instance of Context
     *  whitout progress observer and basic internationalization support
     */
    public PluginContext(PluginDescriptor pluginDescriptor, HashMap paramTable, HashMap mappingTable)
    {
        this(pluginDescriptor, null, paramTable, mappingTable, null, null);
    }
    
    /** Creates a new instance of Context */
    public PluginContext(PluginDescriptor pluginDescriptor, PluginProgressObserver progressObserver, HashMap paramTable, HashMap mappingTable, HashMap pluginLocales, String defaultLocale)
    {
        //this.pluginId = pluginId;
        //this.pluginBasePath = pluginBasePath;
        
        this.pluginDescriptor = pluginDescriptor;
        
        this.environment = new Environment(pluginDescriptor, progressObserver, paramTable, mappingTable);
        this.userInterface = new UserInterface();
        this.resource = new Resource();
        this.metadata = new Metadata();
        this.search = new Search();
        this.i18n = new I18n(pluginLocales, defaultLocale);
    }
    
    /** Getter for property environment.
     * @return Value of property environment.
     *
     */
    public PluginContext.Environment getEnvironment()
    {
        return this.environment;
    }
    
    //Hell  
    public Logger getLogger() {
            return logger;
        }

      /** Setter for property environment.
     * @param environment New value of property environment.
     *
     */
    protected void setEnvironment(PluginContext.Environment environment)
    {
        this.environment = environment;
    }
    
    /** Getter for property metadata.
     * @return Value of property metadata.
     *
     */
    public PluginContext.Metadata getMetadata()
    {
        return this.metadata;
    }
    
    /** Setter for property metadata.
     * @param metadata New value of property metadata.
     *
     */
    protected void setMetadata(PluginContext.Metadata metadata)
    {
        this.metadata = metadata;
    }
    
    /** Getter for property resource.
     * @return Value of property resource.
     *
     */
    public PluginContext.Resource getResource()
    {
        return this.resource;
    }
    
    /** Setter for property resource.
     * @param resource New value of property resource.
     *
     */
    protected void setResource(PluginContext.Resource resource)
    {
        this.resource = resource;
    }
    
    /** Getter for property userInterface.
     * @return Value of property userInterface.
     *
     */
    public PluginContext.UserInterface getUserInterface()
    {
        return this.userInterface;
    }
    
    /** Setter for property userInterface.
     * @param userInterface New value of property userInterface.
     *
     */
    protected void setUserInterface(PluginContext.UserInterface userInterface)
    {
        this.userInterface = userInterface;
    }
    
    /** Getter for property search.
     * @return Value of property search.
     *
     */
    public Search getSearch()
    {
        return this.search;
    }
    
    /** Setter for property search.
     * @param search New value of property search.
     *
     */
    public void setSearch(Search search)
    {
        this.search = search;
    }
    
    /** Getter for property i18n.
     * @return Value of property i18n.
     *
     */
    public PluginContext.I18n getI18n()
    {
        return this.i18n;
    }
    
    /** Setter for property i18n.
     * @param i18n New value of property i18n.
     *
     */
    public void setI18n(PluginContext.I18n i18n)
    {
        this.i18n = i18n;
    }
    
    public NavigatorToolkit getToolkit()
    {
        return NavigatorToolkit.getToolkit();
    }
    
    public class Environment
    {
        private final HashMap mappingTable;
        private final HashMap paramTable;
        
        private final String documentBase;
        private final String codeBase;
        
        private final Logger pluginLogger;
        
        private final PluginProgressObserver progressObserver;
        
        private Environment(PluginDescriptor pluginDescriptor)
        {
            this(pluginDescriptor, null, null, null);
        }
        
        private Environment(PluginDescriptor pluginDescriptor, PluginProgressObserver progressObserver, HashMap paramTable, HashMap mappingTable)
        {
            this.pluginLogger = Logger.getLogger("Sirius.navigator.plugin.plugins." + pluginDescriptor.getName());
            
            this.progressObserver = progressObserver;
            this.mappingTable = mappingTable;
            this.paramTable = paramTable;
            
            this.documentBase = pluginDescriptor.getPluginPath() + "res/";
            this.codeBase = pluginDescriptor.getPluginPath() + "lib/";
        }
        
        public boolean isApplet()
        {
            return properties.isApplet();
        }
        
        public boolean isApplication()
        {
            return properties.isApplication();
        }
        
        public boolean isProgressObservable()
        {
            return this.progressObserver != null ? true : false;
        }
        
        public String getDocumentBase()
        {
            return this.documentBase;
        }
        
        public String getCodeBase()
        {
            return this.codeBase;
        }
        
        public AppletContext getAppletContext()
        {
            return PropertyManager.getManager().getAppletContext();
        }
        
        /**
         * Translates a meta attribute name into a meta attribute id.<p>
         * XML descriptor
         *
         * @param attributeName the name of the meta attribute, e.g. 'Koordinate'
         * @return the id of the meta attribute (e.g. XYZ123) or null if the parameter could not be found
         */
        /*public String[] getAttributeMappings(String attributeName)
        {
            if(this.mappingTable == null)
            {
                PluginContext.logger.warn("attribute '" + attributeName + "' could not be not found, no mappings loaded");
                return null;
            }
         
            Object object = mappingTable.get(attributeName);
            if(object != null)
            {
                return (String[])object;
            }
            else
            {
                PluginContext.logger.warn("attribute '" + attributeName + "' not found");
                return null;
            }
        }*/
        
        public Collection getAttributeMappings(String attributeName)
        {
            if(this.mappingTable == null)
            {
                PluginContext.logger.warn("attribute '" + attributeName + "' could not be not found, no mappings loaded");
            }
            
            Object object = mappingTable.get(attributeName);
            if(object != null)
            {
                return (Collection)object;
                //return (String[])object;
            }
            else
            {
                PluginContext.logger.warn("attribute '" + attributeName + "' not found");
            }
            
            // besser als nix ...
            return new LinkedList();
        }
        
        /**
         * Translates a meta attribute name into a meta attribute id.<p>
         * XML descriptor
         *
         * @param attributeName the name of the meta attribute, e.g. 'Koordinate'
         * @return the id of the meta attribute (e.g. XYZ123) or null if the parameter could not be found
         */
        public String getAttributeMapping(String attributeName)
        {
            /*String[] attributeMappings = this.getAttributeMappings(attributeName);
             
            if(attributeMappings != null && attributeMappings.length > 0)
            {
                return attributeMappings[0];
            }*/
            
            Collection attributeMapping = this.getAttributeMappings(attributeName);
            
            return attributeMapping.size() > 0 ? attributeMapping.iterator().next().toString() : null;
        }
        
        /**
         * Translates an attribute name into an attribute id.
         *
         * @param paramName the name of the parameter
         * @return the value of the parameter or null if the parameter could not be found
         */
        public String getParameter(String paramName)
        {
            PluginContext.logger.debug("retrieving parameter '" + paramName + "'");
            if(this.paramTable == null)
            {
                PluginContext.logger.warn("parameter '" + paramName + "' could not be not found, no parameters loaded");
                return null;
            }
            
            Object object = paramTable.get(paramName);
            if(object != null)
            {
                return (String)object;
            }
            else
            {
                PluginContext.logger.warn("parameter '" + paramName + "' not found");
                return null;
            }
        }
        
        public Logger getLogger()
        {
            return this.pluginLogger;
        }
        
        public boolean isDebug()
        {
            return PluginContext.logger.isDebugEnabled();
        }
        
        /**
         * Returns the locale of the operating system or the locale of the
         * web browser.
         *
         * @return the system locale object
         */
        public Locale getDefaultLocale()
        {
            if(this.isApplet())
            {
                Enumeration enu = this.getAppletContext().getApplets();
                if(enu.hasMoreElements())
                {
                    return ((Applet)enu.nextElement()).getLocale();
                }
            }
            
            return Locale.getDefault();
        }
        
        /**
         * Returns the current locale of the navigator.<p>
         * This method can return a different locae than <code>getSystemLocale()</code>
         *
         * @return the navigator locale object
         */
        public Locale getNavigatorLocale()
        {
            return ResourceManager.getManager().getLocale();
        }
        
        public PluginProgressObserver getProgressObserver()
        {
            return this.progressObserver;
        }
    }
    
    public class UserInterface
    {
        private UserInterface()
        {
            
        }
        
        public JFrame getFrame()
        {
            return ComponentRegistry.getRegistry().getMainWindow();
        }
        
        public JFrame getFrameFor(Component component)
        {
            return ComponentRegistry.getRegistry().getWindowFor(component);
        }
        
        public JFrame getFrameFor(PluginUI pluginUI)
        {
            return ComponentRegistry.getRegistry().getWindowFor(pluginUI.getComponent());
        }
        
        public void showDocumentInDefaultBrowser(URL url)
        {
            PropertyManager.getManager().getAppletContext().showDocument(url, "_blank");
        }
        
        public void showDocumentInDefaultBrowser(String url) throws MalformedURLException
        {
            this.showDocumentInDefaultBrowser(new URL(url));
        }
        
        public void showWarningDialog(JFrame owner, String name, String message, Collection detailMessages)
        {
            ExceptionManager.getManager().showExceptionDialog(owner, ExceptionManager.PLUGIN_WARNING, name, message, detailMessages);
        }
        
        public void showErrorDialog(JFrame owner, String name, String message, Collection detailMessages)
        {
            ExceptionManager.getManager().showExceptionDialog(owner, ExceptionManager.PLUGIN_ERROR, name, message, detailMessages);
        }
    }
    
    public class Resource
    {
        private Resource()
        {
            
        }
        
        public InputStream getPluginResourceAsStream(String resource) throws IOException
        {
            String path = getEnvironment().getDocumentBase() + resource;
            if(PluginContext.logger.isDebugEnabled())PluginContext.logger.debug("loading resource '" + path + "'");
            
            try
            {
                if(getEnvironment().isApplet()) // oder webstart, dann ist applet auch true
                {
                    logger.debug("ich denk ich bin ein applet: path:"+path);
                    URL url = new URL(path);
                    URLConnection connection = url.openConnection();
                    return connection.getInputStream();
                }
                else
                {
                    File file = new File(path);
                    return new FileInputStream(file);
                }
            }
            catch(IOException ioexp)
            {
                PluginContext.logger.error("resource '" + path + "' could not be found", ioexp);
                throw ioexp;
            }
        }
        
        public InputStream getResourceAsStream(String resource) throws IOException
        {
            if(PluginContext.logger.isDebugEnabled())PluginContext.logger.debug("loading resource '" + resource + "'");
            return ResourceManager.getManager().getResourceAsStream(resource);
            
            
            /*if(resource.startsWith("http://") || resource.startsWith("https://") || resource.startsWith("file://"))
            {
                URL url = new URL(resource);
                URLConnection connection = url.openConnection();
                return connection.getInputStream();
            }
            else
            {
                File file = new File(resource);
                return new FileInputStream(file);
            }*/
        }
        
        
        public Image getImage(URL url)
        {
            if(PluginContext.logger.isDebugEnabled())PluginContext.logger.debug("loading image '" + url.toString() + "'");
            return Toolkit.getDefaultToolkit().getImage(url);
        }
        
        public Image getImage(String image)
        {
            if(PluginContext.logger.isDebugEnabled())PluginContext.logger.debug("loading image '" + image + "'");
            
            try
            {
                URL url = new URL(image);
                return this.getImage(url);
            }
            catch (MalformedURLException e)
            {
                
                Image i=Toolkit.getDefaultToolkit().getImage(image);
                if (i!=null) {
                    return i;
                }
                else {
                    PluginContext.logger.debug("could not load image from url '" + image + "' returnvalue is null");
                    return null;
                }
            }
        }
        
          
        public ImageIcon getImageIcon(String file)
        {
            Image image = this.getImage(file);
            if(image != null)
            {
                return new ImageIcon(image);
            }
            else
            {
                PluginContext.logger.error("could not load image '" + file + "'");
                return null;
            }
        }
        
        public ImageIcon getImageIcon(URL url)
        {
            Image image = this.getImage(url);
            if(image != null)
            {
                return new ImageIcon(image);
            }
            else
            {
                PluginContext.logger.error("could not load image '" + url.toString() + "'");
                return null;
            }
        }
        
    }
    
    public class Metadata
    {
        private Metadata()
        {
            super();
        }
        
        public Collection getSelectedNodes()
        {
            return ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodes();
        }
        
        public Collection getSelectedNodes(String fromCatalogue)
        {
            if(fromCatalogue.equals(PluginContext.CATALOGUE_TREE))
            {
                return ComponentRegistry.getRegistry().getCatalogueTree().getSelectedNodes();
            }
            else if(fromCatalogue.equals(PluginContext.SEARCHRESULTS_TREE))
            {
                return  ComponentRegistry.getRegistry().getSearchResultsTree().getSelectedNodes();
            }
            else
            {
                logger.warn("unknown catalogue '" + fromCatalogue + "', retunring nodes of selected catalogue");
                return this.getSelectedNodes();
            }
        }
        
        public TreeNodeIterator getSelectedNodesIterator()
        {
            return new TreeNodeIterator(this.getSelectedNodes());
        }
        
        public TreeNodeIterator getSelectedNodesIterator(String fromCatalogue)
        {
            return new TreeNodeIterator(this.getSelectedNodes(fromCatalogue));
        }
        
        
        
        public void addMetaNodeSelectionListener(MetaNodeSelectionListener metaNodeSelectionListener)
        {
            ComponentRegistry.getRegistry().getCatalogueTree().addTreeSelectionListener(metaNodeSelectionListener);
            ComponentRegistry.getRegistry().getSearchResultsTree().addTreeSelectionListener(metaNodeSelectionListener);
        }
        
        public void removeMetaNodeSelectionListener(MetaNodeSelectionListener metaNodeSelectionListener)
        {
            ComponentRegistry.getRegistry().getCatalogueTree().removeTreeSelectionListener(metaNodeSelectionListener);
            ComponentRegistry.getRegistry().getSearchResultsTree().removeTreeSelectionListener(metaNodeSelectionListener);
        }
        
        public void addMetaAttributeSelectionListener(MetaAttributeSelectionListener metaAttributeSelectionListener)
        {
            PluginContext.logger.error("method 'addSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented");
            throw new RuntimeException("method 'addSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented");
        }
        
        public void removeMetaAttributeSelectionListener(MetaAttributeSelectionListener metaAttributeSelectionListener)
        {
            PluginContext.logger.error("method 'removeSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented");
            throw new RuntimeException("method 'removeSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented");
        }
    }
    
    // #########################################################################
    
    /**
     * Dynmaic Search
     */
    public class Search
    {
        // FIXME include this in plugin descriptor
        //private final Properties searchProperties;
        
        //private final FormDataBean textSearchData;
        //private final FormDataBean boundingBoxSearchData;
        
        private boolean appendSearchResults = false;
        private final HashMap dataBeans;
        
        public Search()
        {
            logger.debug("initilizing search form data beans");
            
            this.dataBeans = ComponentRegistry.getRegistry().getSearchDialog().getSearchFormManager().getFormDataBeans();
            
            
            //logger.debug("loading plugin search properties file 'search.properties'");
            
            // FIXME include this in plugin descriptor
            /*this.searchProperties = new Properties();
             
            try
            {
                this.searchProperties.load(this.getClass().getResourceAsStream("search.properties"));
            }
            catch(IOException exp)
            {
                logger.error("could not load search properties file 'search.properties'", exp);
            }*/
            
            /*this.searchProperties = PropertyManager.getManager().getProperties();
             
            LinkedHashMap parameterNamesMap = new LinkedHashMap();
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c1"), "c1");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c2"), "c2");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c3"), "c3");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c4"), "c4");
             
            // umgebungsbereich: 0
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d1"), "umgebungsBereich");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d2"), "umgebungsBereich");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d3"), "umgebungsBereich");
            parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d4"), "umgebungsBereich");
            //parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.umgebungsBereich"), "umgebungsBereich");
             
            this.boundingBoxSearchData = new DefaultFormDataBean(parameterNamesMap);
            this.boundingBoxSearchData.setQueryId(this.searchProperties.getProperty("box.queryId"));
             
            parameterNamesMap.clear();
            parameterNamesMap.put(this.searchProperties.getProperty("text.parameter.searchText"), "searchText");
            parameterNamesMap.put(this.searchProperties.getProperty("text.parameter.caseSensitiv"), "caseSensitiv");
             
            this.textSearchData = new DefaultFormDataBean(parameterNamesMap);
            this.textSearchData.setQueryId(this.searchProperties.getProperty("text.queryId"));*/
        }
        
        /**
         * @deprecated ClassSelection is not needed anymore
         */
        /*private final Vector getClassSelection(Collection themeStrings)
        {
            HashMap classSelectionMap = new HashMap();
            Iterator iterator = themeStrings.iterator();
         
            while(iterator.hasNext())
            {
                String theme = null;
                try
                {
                    theme = iterator.next().toString();
                    if(logger.isDebugEnabled())logger.debug("parsing theme string '" + theme + "'");
                    if(theme.indexOf("@") != -1)
                    {
                        int classId = Integer.parseInt(theme.substring(0, theme.indexOf("@")));
                        String domain = theme.substring(theme.indexOf("@")+1, theme.length());
         
                        if(!classSelectionMap.containsKey(domain))
                        {
                            LsClassSelection lsClassSelection = new LsClassSelection(domain);
                            lsClassSelection.addClassID(classId);
                            classSelectionMap.put(domain, lsClassSelection);
                        }
                        else
                        {
                            ((LsClassSelection)classSelectionMap.get(domain)).addClassID(classId);
                        }
                    }
                }
                catch(Exception exp)
                {
                    logger.error("could not create ls class selection from string '" + theme + "': " + exp.getMessage());
                }
            }
         
            return new Vector(classSelectionMap.values());
        }*/
        
        /**
         *
         * @param themeStrings Collection of Strings <classid>@<domain>
         * @param boundingBox int values <xmin><ymin><xmax><ymax>
         * @deprecated
         */
       /* public void performBoundingBoxSearch(JFrame owner, Collection themeStrings, int[] boundingBox) throws Exception
        {
            logger.debug("performing bounding box search");
        
            this.boundingBoxSearchData.clear();
            this.boundingBoxSearchData.setBeanParameter("c1", new Integer(boundingBox[0]));
            this.boundingBoxSearchData.setBeanParameter("c2", new Integer(boundingBox[1]));
            this.boundingBoxSearchData.setBeanParameter("c3", new Integer(boundingBox[2]));
            this.boundingBoxSearchData.setBeanParameter("c4", new Integer(boundingBox[3]));
            this.boundingBoxSearchData.setBeanParameter("umgebungsBereich", new Integer(0));
        
            //if(logger.isDebugEnabled())logger.debug(this.boundingBoxSearchData.toString());
        
            LinkedList searchFormData = new LinkedList();
            searchFormData.add(this.boundingBoxSearchData);
        
            this.performSearch(themeStrings, searchFormData, owner, this.appendSearchResults);*/
        
            /*Vector classSelection = this.getClassSelection(themeStrings);
            if(classSelection.size() != 0 && boundingBox.length == 4)
            {
                if(logger.isDebugEnabled())logger.debug("creating bounding box: ("+boundingBox[0]+","+boundingBox[1]+","+boundingBox[2]+","+boundingBox[3]+")");
             
                // y & x vertauschen (xMin, yMin, xMax, yMax) -> (yMin, xMin, yMax, cMax) n\u00F6!
                BoundingBox bb = new BoundingBox(new Coordinate(boundingBox[0], boundingBox[1]), new Coordinate(boundingBox[2], boundingBox[3]));
             
                BoundingBoxSearchType searchType = new BoundingBoxSearchType(bb, "SICAD_COORDINATE", "SICAD_COORDINATE", Long.MAX_VALUE, PropertyManager.getManager().getMaxSearchResults());
             
                Vector searchTypes = new Vector(1,0);
                searchTypes.add(searchType);
             
                logger.info("performing new plugin search");
                ComponentRegistry.getRegistry().getSearchDialog().performPluginSearch(owner, searchTypes, classSelection);
            }
            else
            {
                logger.error("could not perform bounding box search: insufficient parameters");
                throw new Exception("could not perform bounding box search: insufficient parameters");
            }*/
        //}
        
        
        /**
         *
         * @param themeStrings Collection of Strings <classid>@<domain>
         * @param boundingBox int values <xmin><ymin><xmax><ymax>
         * @deprecated
         */
        /*public void performAttributeSearch(JFrame owner, Collection themeStrings, Collection attributeValues) throws Exception
        {
            logger.debug("performing attribute search");
         
            this.textSearchData.clear();
            this.textSearchData.setBeanParameter("searchText", this.collectionToSQLString(attributeValues));
            this.textSearchData.setBeanParameter("caseSensitiv", new Boolean(false));
         
            LinkedList searchFormData = new LinkedList();
            searchFormData.add(this.textSearchData);
         
            this.performSearch(themeStrings, searchFormData, owner, this.appendSearchResults);
         
            /*Vector classSelection = this.getClassSelection(themeStrings);
            if(classSelection.size() != 0 && attributeValues.size() != 0)
            {
                Vector searchTypes = new Vector(attributeValues.size(), 0);
                Iterator iterator = attributeValues.iterator();
         
                while(iterator.hasNext())
                {
                    TextSearchType textSearchType = new TextSearchType(iterator.next().toString(), "SICAD_OBJECT", "SICAD_OBJECT", Long.MAX_VALUE, PropertyManager.getManager().getMaxSearchResults());
                    searchTypes.add(textSearchType);
                }
         
                logger.info("performing new plugin search");
                ComponentRegistry.getRegistry().getSearchDialog().performPluginSearch(owner, searchTypes, classSelection);
            }
            else
            {
                logger.error("could not perform attribute search: insufficient parameters");
                throw new Exception("could not perform attribute search: insufficient parameters");
            }*/
        //}
        
        public HashMap getDataBeans()
        {
            return this.dataBeans;
        }
        
        public void performSearch(Collection classNodeKeys, Collection searchFormData)
        {
            logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and " + searchFormData.size() + " searchFormData");
            
            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData);
        }
        
        public void performSearch(Collection classNodeKeys, FormDataBean formData)
        {
            logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and one searchFormData");
            
            LinkedList searchFormData = new LinkedList();
            searchFormData.add(formData);
            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData);
        }
        
        public void performSearch(Collection classNodeKeys, Collection searchFormData, Component owner, boolean appendSearchResults)
        {
            logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and " + searchFormData.size() + " searchFormData");
            
            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData, owner, appendSearchResults);
        }
        
        public void performSearch(Collection classNodeKeys, FormDataBean formData, Component owner, boolean appendSearchResults)
        {
            logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and one searchFormData");
            
            LinkedList searchFormData = new LinkedList();
            searchFormData.add(formData);
            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData, owner, appendSearchResults);
        }
        
        public void performSearch(FormDataBean formDataBean, Component owner, boolean appendSearchResults)
        {
            logger.debug("performing search with one searchFormData (" + formDataBean.getFormId() + ")");
            ComponentRegistry.getRegistry().getSearchDialog().search(formDataBean, owner, appendSearchResults);
        }
        
        public void performSearch(FormDataBean formDataBean)
        {
            logger.debug("performing search with one searchFormData (" + formDataBean.getFormId() + ")");
            ComponentRegistry.getRegistry().getSearchDialog().search(formDataBean);
        }

        /*
         
            SearchProgressDialog searchProgressDialog = ComponentRegistry.getRegistry().getSearchProgressDialog();
         
            searchProgressDialog.setLocationRelativeTo(owner);
            searchProgressDialog.show(classNodeKeys, searchOptions);
         
            if(!searchProgressDialog.isCanceld())
            {
                if(logger.isDebugEnabled())logger.debug("showing search results in search results tree");
                MethodManager.getManager().showSearchResults(searchProgressDialog.getResultNodes(), true);
            }
            else if(logger.isDebugEnabled())
            {
                logger.debug("search canceld, don't do anything");
            }
        }*/
        
        // .....................................................................
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Internationalization Support
     */
    public class I18n
    {
        private final static String ERROR_STRING = ResourceManager.ERROR_STRING;
        
        private final HashMap pluginLocales;
        private final boolean internationalized;
        
        private ResourceBundle resourceBundle = null;
        private PluginLocale pluginLocale = null;
        
        public I18n(HashMap pluginLocales, String defaultLocale)
        {
            if(!PluginContext.this.pluginDescriptor.isInternationalized() || pluginLocales == null || pluginLocales.size() == 0 || defaultLocale == null)
            {
                this.internationalized = false;
                this.pluginLocales = null;
                
            }
            else
            {
                this.internationalized = true;
                this.pluginLocales =  pluginLocales;
                this.setLocale(defaultLocale);
            }
        }
        
        public boolean isInternationalized()
        {
            return this.internationalized;
        }
        
        public String setLocale(Locale locale)
        {
            if(this.isInternationalized())
            {
                Iterator iterator = this.pluginLocales.values().iterator();
                while(iterator.hasNext())
                {
                    PluginLocale pluginLocale = (PluginLocale)iterator.next();
                    if(locale.equals(pluginLocale.getLocale()))
                    {
                        this.setLocale(pluginLocale.getName());
                        return this.pluginLocale.getName();
                    }
                }
            }
            
            logger.warn("Locale '" + locale + "' not supported");
            return null;
        }
        
        public synchronized Locale setLocale(String name)
        {
            if(this.isInternationalized() && this.pluginLocales.containsKey(name))
            {
                this.pluginLocale = (PluginLocale)pluginLocales.get(name);
                try
                {
                    this.setResourceBundle(this.pluginLocale.getResourceFile());
                    return this.getLocale();
                }
                catch(IOException ioexp)
                {
                    logger.error("could not load resource file '" + this.pluginLocale.getResourceFile() + "':\n" + ioexp.getMessage());
                    return null;
                }
            }
            else
            {
                getEnvironment().getLogger().error("[I18N] unknown language/country '" + name + "'");
                return null;
            }
        }
        
        public Locale getLocale()
        {
            if(this.isInternationalized())
            {
                return this.pluginLocale.getLocale();
            }
            else
            {
                return null;
            }
        }
        
        public String getString(String key)
        {
            try
            {
                return resourceBundle.getString(key);
            }
            catch(Exception exp)
            {
                getEnvironment().getLogger().error("[I18N] could not find key '" + key + "':\n" + exp.getMessage());
                return ERROR_STRING;
            }
        }
        
        // .....................................................................
        
        protected void setResourceBundle(String resourceFile) throws IOException
        {
            InputStream inputStream = getResource().getPluginResourceAsStream(resourceFile);
            this.resourceBundle = new PropertyResourceBundle(new BufferedInputStream(inputStream));
        }
    }
}
