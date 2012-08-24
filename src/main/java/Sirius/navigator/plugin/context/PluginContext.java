/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.context;

import Sirius.navigator.exception.*;
import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.plugin.listener.*;
import Sirius.navigator.resource.*;
//import Sirius.server.search.*;
//import Sirius.server.search.wundasearch.*;
//import Sirius.server.search.wundasearch.types.*;
import Sirius.navigator.search.dynamic.*;
import Sirius.navigator.tools.*;
import Sirius.navigator.types.iterator.*;
import Sirius.navigator.ui.*;

import org.apache.log4j.Logger;

import java.applet.*;

import java.awt.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;
//import Sirius.navigator.ui.progress.ProgressObservable;
//import Sirius.navigator.ui.progress.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginContext {

    //~ Static fields/initializers ---------------------------------------------

    public static final String CATALOGUE_TREE = ComponentRegistry.CATALOGUE_TREE;
    public static final String SEARCHRESULTS_TREE = ComponentRegistry.SEARCHRESULTS_TREE;

    private static final Logger logger = Logger.getLogger(PluginContext.class);

    private static final PropertyManager properties = PropertyManager.getManager();
    private static final ResourceManager resources = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    // private final String pluginId;
    // private final String pluginBasePath;

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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of Context whitout progress observer and basic internationalization support.
     *
     * @param  pluginDescriptor  DOCUMENT ME!
     * @param  paramTable        DOCUMENT ME!
     * @param  mappingTable      DOCUMENT ME!
     */
    public PluginContext(final PluginDescriptor pluginDescriptor,
            final HashMap paramTable,
            final HashMap mappingTable) {
        this(pluginDescriptor, null, paramTable, mappingTable, null, null);
    }

    /**
     * Creates a new instance of Context.
     *
     * @param  pluginDescriptor  DOCUMENT ME!
     * @param  progressObserver  DOCUMENT ME!
     * @param  paramTable        DOCUMENT ME!
     * @param  mappingTable      DOCUMENT ME!
     * @param  pluginLocales     DOCUMENT ME!
     * @param  defaultLocale     DOCUMENT ME!
     */
    public PluginContext(final PluginDescriptor pluginDescriptor,
            final PluginProgressObserver progressObserver,
            final HashMap paramTable,
            final HashMap mappingTable,
            final HashMap pluginLocales,
            final String defaultLocale) {
        // this.pluginId = pluginId;
        // this.pluginBasePath = pluginBasePath;

        this.pluginDescriptor = pluginDescriptor;

        this.environment = new Environment(pluginDescriptor, progressObserver, paramTable, mappingTable);
        this.userInterface = new UserInterface();
        this.resource = new Resource();
        this.metadata = new Metadata();
        this.search = new Search();
        this.i18n = new I18n(pluginLocales, defaultLocale);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property environment.
     *
     * @return  Value of property environment.
     */
    public PluginContext.Environment getEnvironment() {
        return this.environment;
    }
    /**
     * Hell.
     *
     * @return  DOCUMENT ME!
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Setter for property environment.
     *
     * @param  environment  New value of property environment.
     */
    protected void setEnvironment(final PluginContext.Environment environment) {
        this.environment = environment;
    }

    /**
     * Getter for property metadata.
     *
     * @return  Value of property metadata.
     */
    public PluginContext.Metadata getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for property metadata.
     *
     * @param  metadata  New value of property metadata.
     */
    protected void setMetadata(final PluginContext.Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Getter for property resource.
     *
     * @return  Value of property resource.
     */
    public PluginContext.Resource getResource() {
        return this.resource;
    }

    /**
     * Setter for property resource.
     *
     * @param  resource  New value of property resource.
     */
    protected void setResource(final PluginContext.Resource resource) {
        this.resource = resource;
    }

    /**
     * Getter for property userInterface.
     *
     * @return  Value of property userInterface.
     */
    public PluginContext.UserInterface getUserInterface() {
        return this.userInterface;
    }

    /**
     * Setter for property userInterface.
     *
     * @param  userInterface  New value of property userInterface.
     */
    protected void setUserInterface(final PluginContext.UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * Getter for property search.
     *
     * @return  Value of property search.
     */
    public Search getSearch() {
        return this.search;
    }

    /**
     * Setter for property search.
     *
     * @param  search  New value of property search.
     */
    public void setSearch(final Search search) {
        this.search = search;
    }

    /**
     * Getter for property i18n.
     *
     * @return  Value of property i18n.
     */
    public PluginContext.I18n getI18n() {
        return this.i18n;
    }

    /**
     * Setter for property i18n.
     *
     * @param  i18n  New value of property i18n.
     */
    public void setI18n(final PluginContext.I18n i18n) {
        this.i18n = i18n;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public NavigatorToolkit getToolkit() {
        return NavigatorToolkit.getToolkit();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class Environment {

        //~ Instance fields ----------------------------------------------------

        private final HashMap mappingTable;
        private final HashMap paramTable;

        private final String documentBase;
        private final String codeBase;

        private final Logger pluginLogger;

        private final PluginProgressObserver progressObserver;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Environment object.
         *
         * @param  pluginDescriptor  DOCUMENT ME!
         */
        private Environment(final PluginDescriptor pluginDescriptor) {
            this(pluginDescriptor, null, null, null);
        }

        /**
         * Creates a new Environment object.
         *
         * @param  pluginDescriptor  DOCUMENT ME!
         * @param  progressObserver  DOCUMENT ME!
         * @param  paramTable        DOCUMENT ME!
         * @param  mappingTable      DOCUMENT ME!
         */
        private Environment(final PluginDescriptor pluginDescriptor,
                final PluginProgressObserver progressObserver,
                final HashMap paramTable,
                final HashMap mappingTable) {
            this.pluginLogger = Logger.getLogger("Sirius.navigator.plugin.plugins." + pluginDescriptor.getName()); // NOI18N

            this.progressObserver = progressObserver;
            this.mappingTable = mappingTable;
            this.paramTable = paramTable;

            this.documentBase = pluginDescriptor.getPluginPath() + "res/"; // NOI18N
            this.codeBase = pluginDescriptor.getPluginPath() + "lib/";     // NOI18N
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isApplet() {
            return properties.isApplet();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isApplication() {
            return properties.isApplication();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isProgressObservable() {
            return (this.progressObserver != null) ? true : false;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getDocumentBase() {
            return this.documentBase;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getCodeBase() {
            return this.codeBase;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public AppletContext getAppletContext() {
            return PropertyManager.getManager().getAppletContext();
        }

        /**
         * Translates a meta attribute name into a meta attribute id.
         *
         * <p>XML descriptor</p>
         *
         * @param   attributeName  the name of the meta attribute, e.g. 'Koordinate'
         *
         * @return  the id of the meta attribute (e.g. XYZ123) or null if the parameter could not be found
         */
        /*public String[] getAttributeMappings(String attributeName)
         * { if(this.mappingTable == null) {     PluginContext.logger.warn("attribute '" + attributeName + "' could not
         * be not found, no mappings loaded");     return null; }  Object object = mappingTable.get(attributeName);
         * if(object != null) {     return (String[])object; } else {     PluginContext.logger.warn("attribute '" +
         * attributeName + "' not found");     return null; }}*/

        public Collection getAttributeMappings(final String attributeName) {
            if (this.mappingTable == null) {
                PluginContext.logger.warn("attribute '" + attributeName
                            + "' could not be not found, no mappings loaded"); // NOI18N
            }

            final Object object = mappingTable.get(attributeName);
            if (object != null) {
                return (Collection)object;
                    // return (String[])object;
            } else {
                PluginContext.logger.warn("attribute '" + attributeName + "' not found"); // NOI18N
            }

            // besser als nix ...
            return new LinkedList();
        }

        /**
         * Translates a meta attribute name into a meta attribute id.
         *
         * <p>XML descriptor</p>
         *
         * @param   attributeName  the name of the meta attribute, e.g. 'Koordinate'
         *
         * @return  the id of the meta attribute (e.g. XYZ123) or null if the parameter could not be found
         */
        public String getAttributeMapping(final String attributeName) {
            /*String[] attributeMappings = this.getAttributeMappings(attributeName);
             * if(attributeMappings != null && attributeMappings.length > 0) { return attributeMappings[0];}*/

            final Collection attributeMapping = this.getAttributeMappings(attributeName);

            return (attributeMapping.size() > 0) ? attributeMapping.iterator().next().toString() : null;
        }

        /**
         * Translates an attribute name into an attribute id.
         *
         * @param   paramName  the name of the parameter
         *
         * @return  the value of the parameter or null if the parameter could not be found
         */
        public String getParameter(final String paramName) {
            if (PluginContext.logger.isDebugEnabled()) {
                PluginContext.logger.debug("retrieving parameter '" + paramName + "'");                                  // NOI18N
            }
            if (this.paramTable == null) {
                PluginContext.logger.warn("parameter '" + paramName + "' could not be not found, no parameters loaded"); // NOI18N
                return null;
            }

            final Object object = paramTable.get(paramName);
            if (object != null) {
                return (String)object;
            } else {
                PluginContext.logger.warn("parameter '" + paramName + "' not found"); // NOI18N
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Logger getLogger() {
            return this.pluginLogger;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isDebug() {
            return PluginContext.logger.isDebugEnabled();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public PluginProgressObserver getProgressObserver() {
            return this.progressObserver;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class UserInterface {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new UserInterface object.
         */
        private UserInterface() {
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public JFrame getFrame() {
            return ComponentRegistry.getRegistry().getMainWindow();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   component  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public JFrame getFrameFor(final Component component) {
            return ComponentRegistry.getRegistry().getWindowFor(component);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   pluginUI  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public JFrame getFrameFor(final PluginUI pluginUI) {
            return ComponentRegistry.getRegistry().getWindowFor(pluginUI.getComponent());
        }

        /**
         * DOCUMENT ME!
         *
         * @param  url  DOCUMENT ME!
         */
        public void showDocumentInDefaultBrowser(final URL url) {
            PropertyManager.getManager().getAppletContext().showDocument(url, "_blank"); // NOI18N
        }

        /**
         * DOCUMENT ME!
         *
         * @param   url  DOCUMENT ME!
         *
         * @throws  MalformedURLException  DOCUMENT ME!
         */
        public void showDocumentInDefaultBrowser(final String url) throws MalformedURLException {
            this.showDocumentInDefaultBrowser(new URL(url));
        }

        /**
         * DOCUMENT ME!
         *
         * @param  owner           DOCUMENT ME!
         * @param  name            DOCUMENT ME!
         * @param  message         DOCUMENT ME!
         * @param  detailMessages  DOCUMENT ME!
         */
        public void showWarningDialog(final JFrame owner,
                final String name,
                final String message,
                final Collection detailMessages) {
            ExceptionManager.getManager()
                    .showExceptionDialog(owner, ExceptionManager.PLUGIN_WARNING, name, message, detailMessages);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  owner           DOCUMENT ME!
         * @param  name            DOCUMENT ME!
         * @param  message         DOCUMENT ME!
         * @param  detailMessages  DOCUMENT ME!
         */
        public void showErrorDialog(final JFrame owner,
                final String name,
                final String message,
                final Collection detailMessages) {
            ExceptionManager.getManager()
                    .showExceptionDialog(owner, ExceptionManager.PLUGIN_ERROR, name, message, detailMessages);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class Resource {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Resource object.
         */
        private Resource() {
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   resource  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        public InputStream getPluginResourceAsStream(final String resource) throws IOException {
            final String path = getEnvironment().getDocumentBase() + resource;
            if (PluginContext.logger.isDebugEnabled()) {
                PluginContext.logger.debug("loading resource '" + path + "'"); // NOI18N
            }

            try {
                if (getEnvironment().isApplet())                                                 // oder webstart, dann
                                                                                                 // ist applet auch true
                {
                    if (PluginContext.logger.isDebugEnabled()) {
                        logger.debug("ich denk ich bin ein applet: path:" + path);               // NOI18N
                    }
                    final URL url = new URL(path);
                    final URLConnection connection = url.openConnection();
                    return connection.getInputStream();
                } else {
                    final File file = new File(path);
                    return new FileInputStream(file);
                }
            } catch (IOException ioexp) {
                PluginContext.logger.error("resource '" + path + "' could not be found", ioexp); // NOI18N
                throw ioexp;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   resource  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        public InputStream getResourceAsStream(final String resource) throws IOException {
            if (PluginContext.logger.isDebugEnabled()) {
                PluginContext.logger.debug("loading resource '" + resource + "'"); // NOI18N
            }
            return resources.getResourceAsStream(resource);

            /*if(resource.startsWith("http://") || resource.startsWith("https://") || resource.startsWith("file://"))
             * { URL url = new URL(resource); URLConnection connection = url.openConnection(); return
             * connection.getInputStream(); } else { File file = new File(resource); return new
             * FileInputStream(file);}*/
        }

        /**
         * DOCUMENT ME!
         *
         * @param   url  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Image getImage(final URL url) {
            if (PluginContext.logger.isDebugEnabled()) {
                PluginContext.logger.debug("loading image '" + url.toString() + "'"); // NOI18N
            }
            return Toolkit.getDefaultToolkit().getImage(url);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   image  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Image getImage(final String image) {
            if (PluginContext.logger.isDebugEnabled()) {
                PluginContext.logger.debug("loading image '" + image + "'"); // NOI18N
            }

            try {
                final URL url = new URL(image);
                return this.getImage(url);
            } catch (MalformedURLException e) {
                final Image i = Toolkit.getDefaultToolkit().getImage(image);
                if (i != null) {
                    return i;
                } else {
                    if (PluginContext.logger.isDebugEnabled()) {
                        PluginContext.logger.debug("could not load image from url '" + image + "' returnvalue is null"); // NOI18N
                    }
                    return null;
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   file  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public ImageIcon getImageIcon(final String file) {
            final Image image = this.getImage(file);
            if (image != null) {
                return new ImageIcon(image);
            } else {
                PluginContext.logger.error("could not load image '" + file + "'"); // NOI18N
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   url  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public ImageIcon getImageIcon(final URL url) {
            final Image image = this.getImage(url);
            if (image != null) {
                return new ImageIcon(image);
            } else {
                PluginContext.logger.error("could not load image '" + url.toString() + "'"); // NOI18N
                return null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class Metadata {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Metadata object.
         */
        private Metadata() {
            super();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Collection getSelectedNodes() {
            return ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodes();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   fromCatalogue  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Collection getSelectedNodes(final String fromCatalogue) {
            if (fromCatalogue.equals(PluginContext.CATALOGUE_TREE)) {
                return ComponentRegistry.getRegistry().getCatalogueTree().getSelectedNodes();
            } else if (fromCatalogue.equals(PluginContext.SEARCHRESULTS_TREE)) {
                return ComponentRegistry.getRegistry().getSearchResultsTree().getSelectedNodes();
            } else {
                logger.warn("unknown catalogue '" + fromCatalogue + "', retunring nodes of selected catalogue"); // NOI18N
                return this.getSelectedNodes();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public TreeNodeIterator getSelectedNodesIterator() {
            return new TreeNodeIterator(this.getSelectedNodes());
        }

        /**
         * DOCUMENT ME!
         *
         * @param   fromCatalogue  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public TreeNodeIterator getSelectedNodesIterator(final String fromCatalogue) {
            return new TreeNodeIterator(this.getSelectedNodes(fromCatalogue));
        }

        /**
         * DOCUMENT ME!
         *
         * @param  metaNodeSelectionListener  DOCUMENT ME!
         */
        public void addMetaNodeSelectionListener(final MetaNodeSelectionListener metaNodeSelectionListener) {
            ComponentRegistry.getRegistry().getCatalogueTree().addTreeSelectionListener(metaNodeSelectionListener);
            ComponentRegistry.getRegistry().getSearchResultsTree().addTreeSelectionListener(metaNodeSelectionListener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  metaNodeSelectionListener  DOCUMENT ME!
         */
        public void removeMetaNodeSelectionListener(final MetaNodeSelectionListener metaNodeSelectionListener) {
            ComponentRegistry.getRegistry().getCatalogueTree().removeTreeSelectionListener(metaNodeSelectionListener);
            ComponentRegistry.getRegistry()
                    .getSearchResultsTree()
                    .removeTreeSelectionListener(metaNodeSelectionListener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   metaAttributeSelectionListener  DOCUMENT ME!
         *
         * @throws  RuntimeException  DOCUMENT ME!
         */
        public void addMetaAttributeSelectionListener(
                final MetaAttributeSelectionListener metaAttributeSelectionListener) {
            PluginContext.logger.error(
                "method 'addSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented"); // NOI18N
            throw new RuntimeException(
                "method 'addSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented"); // NOI18N
        }

        /**
         * DOCUMENT ME!
         *
         * @param   metaAttributeSelectionListener  DOCUMENT ME!
         *
         * @throws  RuntimeException  DOCUMENT ME!
         */
        public void removeMetaAttributeSelectionListener(
                final MetaAttributeSelectionListener metaAttributeSelectionListener) {
            PluginContext.logger.error(
                "method 'removeSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented"); // NOI18N
            throw new RuntimeException(
                "method 'removeSirius.server.localserver.attribute.AttributeSelectionListener' is not implemented"); // NOI18N
        }
    }

    // #########################################################################

    /**
     * Dynmaic Search.
     *
     * @version  $Revision$, $Date$
     */
    public class Search {

        //~ Instance fields ----------------------------------------------------

        // FIXME include this in plugin descriptor
        // private final Properties searchProperties;

        // private final FormDataBean textSearchData;
        // private final FormDataBean boundingBoxSearchData;

        private boolean appendSearchResults = false;
        private final HashMap dataBeans;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Search object.
         */
        public Search() {
            if (logger.isDebugEnabled()) {
                logger.debug("initializing search form data beans"); // NOI18N
            }

            this.dataBeans = ComponentRegistry.getRegistry().getSearchDialog().getSearchFormManager()
                        .getFormDataBeans();

            // logger.debug("loading plugin search properties file 'search.properties'");

            // FIXME include this in plugin descriptor
            /*this.searchProperties = new Properties();
             * try { this.searchProperties.load(this.getClass().getResourceAsStream("search.properties")); }
             * catch(IOException exp) { logger.error("could not load search properties file 'search.properties'",
             * exp);}*/

            /*this.searchProperties = PropertyManager.getManager().getProperties();
             * LinkedHashMap parameterNamesMap = new LinkedHashMap();
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c1"), "c1");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c2"), "c2");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c3"), "c3");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.c4"), "c4"); // umgebungsbereich:
             * 0 parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d1"), "umgebungsBereich");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d2"), "umgebungsBereich");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d3"), "umgebungsBereich");
             * parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.d4"), "umgebungsBereich");
             * //parameterNamesMap.put(this.searchProperties.getProperty("box.parameter.umgebungsBereich"),
             * "umgebungsBereich"); this.boundingBoxSearchData = new DefaultFormDataBean(parameterNamesMap);
             * this.boundingBoxSearchData.setQueryId(this.searchProperties.getProperty("box.queryId"));
             * parameterNamesMap.clear();
             * parameterNamesMap.put(this.searchProperties.getProperty("text.parameter.searchText"), "searchText");
             * parameterNamesMap.put(this.searchProperties.getProperty("text.parameter.caseSensitiv"), "caseSensitiv");
             * this.textSearchData = new
             * DefaultFormDataBean(parameterNamesMap);this.textSearchData.setQueryId(this.searchProperties.getProperty("text.queryId"));*/
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return      DOCUMENT ME!
         *
         * @deprecated  ClassSelection is not needed anymore
         */
        /*private final Vector getClassSelection(Collection themeStrings)
         * { HashMap classSelectionMap = new HashMap(); Iterator iterator = themeStrings.iterator();
         * while(iterator.hasNext()) {     String theme = null;     try     {         theme =
         * iterator.next().toString();         if(logger.isDebugEnabled())logger.debug("parsing theme string '" + theme
         * + "'");         if(theme.indexOf("@") != -1)         {             int classId =
         * Integer.parseInt(theme.substring(0, theme.indexOf("@")));             String domain =
         * theme.substring(theme.indexOf("@")+1, theme.length()); if(!classSelectionMap.containsKey(domain)) {
         * LsClassSelection lsClassSelection = new LsClassSelection(domain); lsClassSelection.addClassID(classId);
         * classSelectionMap.put(domain, lsClassSelection);             }   else             {
         * ((LsClassSelection)classSelectionMap.get(domain)).addClassID(classId);             }     }     }
         * catch(Exception exp)     {         logger.error("could not create ls class selection from string '" + theme +
         * "': " + exp.getMessage());     } }  return new Vector(classSelectionMap.values());}*/

        /**
         * DOCUMENT ME!
         *
         * @return      DOCUMENT ME!
         *
         * @deprecated  DOCUMENT ME!
         */
        /* public void performBoundingBoxSearch(JFrame owner, Collection themeStrings, int[] boundingBox) throws
         * Exception {  logger.debug("performing bounding box search");   this.boundingBoxSearchData.clear();
         * this.boundingBoxSearchData.setBeanParameter("c1", new Integer(boundingBox[0]));
         * this.boundingBoxSearchData.setBeanParameter("c2", new Integer(boundingBox[1]));
         * this.boundingBoxSearchData.setBeanParameter("c3", new Integer(boundingBox[2]));
         * this.boundingBoxSearchData.setBeanParameter("c4", new Integer(boundingBox[3]));
         * this.boundingBoxSearchData.setBeanParameter("umgebungsBereich", new Integer(0));
         * //if(logger.isDebugEnabled())logger.debug(this.boundingBoxSearchData.toString());   LinkedList searchFormData
         * = new LinkedList();  searchFormData.add(this.boundingBoxSearchData);   this.performSearch(themeStrings,
         * searchFormData, owner, this.appendSearchResults);*/

        /*Vector classSelection = this.getClassSelection(themeStrings);
         * if(classSelection.size() != 0 && boundingBox.length == 4) { if(logger.isDebugEnabled())logger.debug("creating
         * bounding box: ("+boundingBox[0]+","+boundingBox[1]+","+boundingBox[2]+","+boundingBox[3]+")");  // y & x
         * vertauschen (xMin, yMin, xMax, yMax) -> (yMin, xMin, yMax, cMax) n\u00F6! BoundingBox bb = new
         * BoundingBox(new Coordinate(boundingBox[0], boundingBox[1]), new Coordinate(boundingBox[2], boundingBox[3]));
         * BoundingBoxSearchType searchType = new BoundingBoxSearchType(bb, "SICAD_COORDINATE", "SICAD_COORDINATE",
         * Long.MAX_VALUE, PropertyManager.getManager().getMaxSearchResults());  Vector searchTypes = new Vector(1,0);
         * searchTypes.add(searchType);  logger.info("performing new plugin search");
         * ComponentRegistry.getRegistry().getSearchDialog().performPluginSearch(owner, searchTypes, classSelection); }
         * else { logger.error("could not perform bounding box search: insufficient parameters"); throw new
         * Exception("could not perform bounding box search: insufficient parameters");}*/
        // }

        /**
         * DOCUMENT ME!
         *
         * @return      DOCUMENT ME!
         *
         * @deprecated  DOCUMENT ME!
         */
        /*public void performAttributeSearch(JFrame owner, Collection themeStrings, Collection attributeValues) throws
         * Exception { logger.debug("performing attribute search");  this.textSearchData.clear();
         * this.textSearchData.setBeanParameter("searchText", this.collectionToSQLString(attributeValues));
         * this.textSearchData.setBeanParameter("caseSensitiv", new Boolean(false));  LinkedList searchFormData = new
         * LinkedList(); searchFormData.add(this.textSearchData);  this.performSearch(themeStrings, searchFormData,
         * owner, this.appendSearchResults);  /*Vector classSelection = this.getClassSelection(themeStrings);
         * if(classSelection.size() != 0 && attributeValues.size() != 0) {     Vector searchTypes = new
         * Vector(attributeValues.size(), 0);     Iterator iterator = attributeValues.iterator();
         * while(iterator.hasNext())     {         TextSearchType textSearchType = new
         * TextSearchType(iterator.next().toString(), "SICAD_OBJECT", "SICAD_OBJECT", Long.MAX_VALUE,
         * PropertyManager.getManager().getMaxSearchResults());         searchTypes.add(textSearchType);     }
         * logger.info("performing new plugin search");
         * ComponentRegistry.getRegistry().getSearchDialog().performPluginSearch(owner, searchTypes, classSelection); }
         * else {     logger.error("could not perform attribute search: insufficient parameters");     throw new
         * Exception("could not perform attribute search: insufficient parameters"); }*/
        // }

        public HashMap getDataBeans() {
            return this.dataBeans;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  classNodeKeys   DOCUMENT ME!
         * @param  searchFormData  DOCUMENT ME!
         */
        public void performSearch(final Collection classNodeKeys, final Collection searchFormData) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and "
                            + searchFormData.size() + " searchFormData"); // NOI18N
            }

            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  classNodeKeys  DOCUMENT ME!
         * @param  formData       DOCUMENT ME!
         */
        public void performSearch(final Collection classNodeKeys, final FormDataBean formData) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with " + classNodeKeys.size()
                            + " classNodeKeys and one searchFormData"); // NOI18N
            }

            final LinkedList searchFormData = new LinkedList();
            searchFormData.add(formData);
            ComponentRegistry.getRegistry().getSearchDialog().search(classNodeKeys, searchFormData);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  classNodeKeys        DOCUMENT ME!
         * @param  searchFormData       DOCUMENT ME!
         * @param  owner                DOCUMENT ME!
         * @param  appendSearchResults  DOCUMENT ME!
         */
        public void performSearch(final Collection classNodeKeys,
                final Collection searchFormData,
                final Component owner,
                final boolean appendSearchResults) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with " + classNodeKeys.size() + " classNodeKeys and "
                            + searchFormData.size() + " searchFormData"); // NOI18N
            }

            ComponentRegistry.getRegistry()
                    .getSearchDialog()
                    .search(classNodeKeys, searchFormData, owner, appendSearchResults);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  classNodeKeys        DOCUMENT ME!
         * @param  formData             DOCUMENT ME!
         * @param  owner                DOCUMENT ME!
         * @param  appendSearchResults  DOCUMENT ME!
         */
        public void performSearch(final Collection classNodeKeys,
                final FormDataBean formData,
                final Component owner,
                final boolean appendSearchResults) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with " + classNodeKeys.size()
                            + " classNodeKeys and one searchFormData"); // NOI18N
            }

            final LinkedList searchFormData = new LinkedList();
            searchFormData.add(formData);
            ComponentRegistry.getRegistry()
                    .getSearchDialog()
                    .search(classNodeKeys, searchFormData, owner, appendSearchResults);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  formDataBean         DOCUMENT ME!
         * @param  owner                DOCUMENT ME!
         * @param  appendSearchResults  DOCUMENT ME!
         */
        public void performSearch(final FormDataBean formDataBean,
                final Component owner,
                final boolean appendSearchResults) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with one searchFormData (" + formDataBean.getFormId() + ")"); // NOI18N
            }
            ComponentRegistry.getRegistry().getSearchDialog().search(formDataBean, owner, appendSearchResults);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  formDataBean  DOCUMENT ME!
         */
        public void performSearch(final FormDataBean formDataBean) {
            if (logger.isDebugEnabled()) {
                logger.debug("performing search with one searchFormData (" + formDataBean.getFormId() + ")"); // NOI18N
            }
            ComponentRegistry.getRegistry().getSearchDialog().search(formDataBean);
        }

        /*
         *  SearchProgressDialog searchProgressDialog = ComponentRegistry.getRegistry().getSearchProgressDialog();
         * searchProgressDialog.setLocationRelativeTo(owner); searchProgressDialog.show(classNodeKeys, searchOptions);
         * if(!searchProgressDialog.isCanceld()) {     if(logger.isDebugEnabled())logger.debug("showing search results
         * in search results tree"); MethodManager.getManager().showSearchResults(searchProgressDialog.getResultNodes(),
         * true); } else
         * if(logger.isDebugEnabled()) {     logger.debug("search canceld, don't do anything"); }}*/

        // .....................................................................
    }

    // -------------------------------------------------------------------------

    /**
     * Internationalization Support.
     *
     * @version  $Revision$, $Date$
     */
    public class I18n {

        //~ Static fields/initializers -----------------------------------------

        private static final String ERROR_STRING = ResourceManager.ERROR_STRING;

        //~ Instance fields ----------------------------------------------------

        private final HashMap pluginLocales;
        private final boolean internationalized;

        private ResourceBundle resourceBundle = null;
        private PluginLocale pluginLocale = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new I18n object.
         *
         * @param  pluginLocales  DOCUMENT ME!
         * @param  defaultLocale  DOCUMENT ME!
         */
        public I18n(final HashMap pluginLocales, final String defaultLocale) {
            if (!PluginContext.this.pluginDescriptor.isInternationalized() || (pluginLocales == null)
                        || (pluginLocales.size() == 0)
                        || (defaultLocale == null)) {
                this.internationalized = false;
                this.pluginLocales = null;
            } else {
                this.internationalized = true;
                this.pluginLocales = pluginLocales;
                this.setLocale(defaultLocale);
            }
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isInternationalized() {
            return this.internationalized;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   locale  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String setLocale(final Locale locale) {
            if (this.isInternationalized()) {
                final Iterator iterator = this.pluginLocales.values().iterator();
                while (iterator.hasNext()) {
                    final PluginLocale pluginLocale = (PluginLocale)iterator.next();
                    if (locale.equals(pluginLocale.getLocale())) {
                        this.setLocale(pluginLocale.getName());
                        return this.pluginLocale.getName();
                    }
                }
            }

            logger.warn("Locale '" + locale + "' not supported"); // NOI18N
            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   name  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public synchronized Locale setLocale(final String name) {
            if (this.isInternationalized() && this.pluginLocales.containsKey(name)) {
                this.pluginLocale = (PluginLocale)pluginLocales.get(name);
                try {
                    this.setResourceBundle(this.pluginLocale.getResourceFile());
                    return this.getLocale();
                } catch (IOException ioexp) {
                    logger.error("could not load resource file '" + this.pluginLocale.getResourceFile() + "':\n"
                                + ioexp.getMessage()); // NOI18N
                    return null;
                }
            } else {
                getEnvironment().getLogger().error("[I18N] unknown language/country '" + name + "'"); // NOI18N
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Locale getLocale() {
            if (this.isInternationalized()) {
                return this.pluginLocale.getLocale();
            } else {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   key  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getString(final String key) {
            try {
                return resourceBundle.getString(key);
            } catch (Exception exp) {
                getEnvironment().getLogger().error("[I18N] could not find key '" + key + "':\n" + exp.getMessage()); // NOI18N
                return ERROR_STRING;
            }
        }

        /**
         * .....................................................................
         *
         * @param   resourceFile  DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        protected void setResourceBundle(final String resourceFile) throws IOException {
            final InputStream inputStream = getResource().getPluginResourceAsStream(resourceFile);
            this.resourceBundle = new PropertyResourceBundle(new BufferedInputStream(inputStream));
        }
    }
}
