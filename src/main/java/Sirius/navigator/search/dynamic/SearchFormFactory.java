/*
 * SearchFormFactory.java
 *
 * Created on 12. November 2003, 10:29
 */

package Sirius.navigator.search.dynamic;

import java.awt.*;
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
import Sirius.navigator.plugin.*;

/**
 *
 * @author  pascal
 */
public class SearchFormFactory
{   
    private final Logger logger;
    private final Log log;
    private static final ResourceManager resource = ResourceManager.getManager();
    
    /** Creates a new instance of SearchFormFactory */
    protected SearchFormFactory()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        Logger digesterLogger = Logger.getLogger(SearchFormFactory.LoadSearchFormsRuleSet.class);
        digesterLogger.setLevel(Level.WARN);
        this.log = new org.apache.commons.logging.impl.Log4JLogger(digesterLogger);
    }
    
    protected java.util.List createSearchForms(String searchFormsPath, String searchFormsDescriptor, SearchContext searchContext) throws IOException, SAXException, URISyntaxException, FileNotFoundException
    {
        if(logger.isDebugEnabled())logger.debug("loading search forms descriptor '" + searchFormsDescriptor + "' in '"+ searchFormsPath + "'");
        InputStream inputStream = this.getXMLDescriptorInputStream(searchFormsPath, searchFormsDescriptor);
        
        LinkedList searchFormContainerList = new LinkedList();
        Digester digester = new Digester();
        
        digester.setLogger(log);
        //digester.push(new SearchFormFactory.FactoryCore(searchFormManager));
        digester.push(new SearchFormFactory.FactoryCore(searchFormsPath, searchFormContainerList, searchContext));
        digester.addRuleSet(new LoadSearchFormsRuleSet());
        
        digester.parse(inputStream);
        inputStream.close();
        
        return searchFormContainerList;
    }
    
    private InputStream getXMLDescriptorInputStream(URL searchUrl, String searchFormsDescriptor) throws URISyntaxException, FileNotFoundException, IOException
    {
        if(logger.isDebugEnabled())logger.debug("loading search forms XML descriptor '" + searchUrl.toString() + "/" + searchFormsDescriptor + "'");
        
        File file = new File(new URI(searchUrl.toString() + "/" + searchFormsDescriptor));
        return new BufferedInputStream(new FileInputStream(file), 4096);
    }
    
    private InputStream getXMLDescriptorInputStream(String searchPath, String searchFormsDescriptor) throws FileNotFoundException, MalformedURLException, IOException
    {
        String searchDescriptorPath = searchPath + "/" + searchFormsDescriptor;
        if(logger.isDebugEnabled())logger.debug("loading search forms XML descriptor from remote URL '" + searchDescriptorPath + "'");
        
        return new BufferedInputStream(resource.getResourceAsStream(searchDescriptorPath), 8192);
    }
    
    // RuleSet
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private class LoadSearchFormsRuleSet extends RuleSetBase
    {
        public void addRuleInstances(Digester digester)
        {
            // add libraries (jar files) & create classLoader ..................
            digester.addCallMethod("search/libraries/jar", "addLibrary", 0);
            digester.addCallMethod("search/libraries", "createClassLoader");
            
            // add form properties  ............................................
            digester.addCallMethod("search/categories/category/forms/form/properties/property", "addProperty", 2);
            digester.addCallParam("search/categories/category/forms/form/properties/property/name", 0);
            digester.addCallParam("search/categories/category/forms/form/properties/property/value", 1);
            
            // add the query parameter name <-> form parameter name mapping ....
            digester.addCallMethod("search/categories/category/forms/form/mappings/queryParameter", "addParameterMapping", 2);
            digester.addCallParam("search/categories/category/forms/form/mappings/queryParameter/queryName", 0);
            digester.addCallParam("search/categories/category/forms/form/mappings/queryParameter/formName", 1);
            
            // form i182 .......................................................
            digester.addCallMethod("search/categories/category/forms/form/resourceBundle", "setFormResourceBundle", 0);
            // form visible ....................................................
            digester.addCallMethod("search/categories/category/forms/form/visible", "setFormVisible", 0, new String[] {"java.lang.Boolean"});
            
            // create the search form  .........................................
            digester.addCallMethod("search/categories/category/forms/form", "createSearchForm", 5);
            digester.addCallParam("search/categories/category/forms/form/name", 0);
            digester.addCallParam("search/categories/category/forms/form/formClass", 1);
            digester.addCallParam("search/categories/category/forms/form/formDataClass", 2);
            digester.addCallParam("search/categories/category/forms/form/formId", 3);
            digester.addCallParam("search/categories/category/forms/form/queryId", 4);
            //digester.addCallParam("search/categories/category/forms/form/internationalized", 4);
            
            // category i182 ...................................................
            digester.addCallMethod("search/categories/category/resourceBundle", "setCategoryResourceBundle", 0);
            // category visible ................................................
            digester.addCallMethod("search/categories/category/visible", "setContainerVisible", 0, new String[] {"java.lang.Boolean"});
            
            // create the search form  container ................................
            //digester.addCallMethod("search/categories/category", "createSearchFormContainer", 3, new String[]
            //{"java.lang.String", "java.lang.String", "java.lang.Boolean"});
            digester.addCallMethod("search/categories/category", "createSearchFormContainer", 2);
            digester.addCallParam("search/categories/category/name", 0);
            digester.addCallParam("search/categories/category/categoryClass", 1);
            //digester.addCallParam("search/categories/category/internationalized", 2);
            
            // add all containers to the manager
            //digester.addCallMethod("search/categories", "initSearchFormManager");
        }
    }
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    
    private class FactoryCore
    {
        //private final SearchFormManager searchFormManager;
        private final String searchFormsPath;
        private java.util.List searchFormContainerList;
        private final SearchContext searchContext;
        
        private ClassLoader formClassLoader;
        
        private HashMap formPropertiesMap;
        private LinkedHashMap parameterMappingMap;
        private LinkedList librariesList;
        private LinkedList searchFormsList;
        
        private ResourceBundle categoryResourceBundle;
        private ResourceBundle formResourceBundle;
        
        private boolean formVisible = true;
        private boolean containerVisible = true;
        
        private FactoryCore(String searchFormsPath, java.util.List searchFormContainerList, SearchContext searchContext)
        {
            //this.searchFormManager = searchFormManager;
            this.searchFormsPath = searchFormsPath;
            this.searchFormContainerList = searchFormContainerList;
            
            this.formPropertiesMap = new HashMap();
            this.parameterMappingMap = new LinkedHashMap();
            this.librariesList = new LinkedList();
            this.searchFormsList = new LinkedList();
            this.searchContext = searchContext;
        }
        
        // .....................................................................
        
        public void addLibrary(String jar)
        {
            if(logger.isDebugEnabled())logger.debug("adding library " + jar + "'");
            librariesList.add(jar);
        }
        
        public void createClassLoader() throws MalformedURLException
        {
            if(this.librariesList.size() > 0)
            {
                if(logger.isDebugEnabled())logger.debug("loading libraries @ '" + this.searchFormsPath + "'");
                // load jar files
                URL[] urls = new URL[this.librariesList.size()];
                //String jarBase = ResourceManager.getManager().pathToIURIString(PropertyManager.getManager().getSearchFormPath() + "lib/");
                String jarBase = resource.pathToIURIString(this.searchFormsPath + "/lib/");
                for (int i = 0; i < this.librariesList.size(); i++)
                {
                    if(logger.isDebugEnabled())logger.debug("loading search form library: '" + jarBase + this.librariesList.get(i).toString() + "'");
                    urls[i] = new URL(jarBase + this.librariesList.get(i).toString());
                }

                this.formClassLoader = new PluginClassLoader(urls, this.getClass().getClassLoader());
            }
            else
            {
                
                logger.warn("no form libraries loaded, using default classloader");
                this.formClassLoader = this.getClass().getClassLoader();
            }
        }
        
        public void addProperty(String name, String value)
        {
            if(logger.isDebugEnabled())logger.debug("adding property: '" + name + "' = '" + value + "'");
            this.formPropertiesMap.put(name, value);
        }
        
        public void addParameterMapping(String queryName, String formName)
        {
            if(logger.isDebugEnabled())logger.debug("adding parameter mapping: '" + queryName + "' == '" + formName + "'");
            this.parameterMappingMap.put(queryName, formName);
        }
        
        public void setFormResourceBundle(String resourceBundleName)
        {
           this.formResourceBundle = this.getResourceBundle(resourceBundleName);            
        }
        
        public void setFormVisible(Boolean formVisible)
        {
            this.formVisible = formVisible.booleanValue();
        }
        
        public void setContainerVisible(Boolean containerVisible)
        {
            this.containerVisible = containerVisible.booleanValue();
        }
        
        public void createSearchForm(String name, String formClass, String formDataClass, String formId, String queryId) throws ClassNotFoundException, InstantiationException, IllegalAccessException
        {
            logger.info(name + ", " + formClass  + ", " + formDataClass + formId + ", " + ", " + queryId); // + ", " + internationalized);
            if(logger.isDebugEnabled())logger.debug("creating data bean instance of class '" + formDataClass + "'");
            Class dataBeanClass = this.formClassLoader.loadClass(formDataClass);
            
            FormDataBean dataBean = (FormDataBean)dataBeanClass.newInstance();
            dataBean.setFormId(formId);
            dataBean.setQueryId(queryId);
            dataBean.setParameterNamesMap(this.parameterMappingMap);
            
            // clear parameter map:
            this.parameterMappingMap.clear();
            
            if(logger.isDebugEnabled())logger.debug("creating form instance of class '" + formClass + "'");
            Class searchFormClass = this.formClassLoader.loadClass(formClass);
            
            SearchForm searchForm = (SearchForm)searchFormClass.newInstance();
            searchForm.setName(name);
            searchForm.setFormId(formId);
            searchForm.setQueryId(queryId);
            searchForm.setDataBean(dataBean);
            if(this.formResourceBundle != null)
            {
                //searchForm.setLocale(ResourceManager.getManager().getLocale());
                searchForm.setResourceBundle(this.formResourceBundle);
                
                // clear reference
                this.formResourceBundle = null;
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("form '" + name + "' does not support i182");
            }
            if(this.formPropertiesMap.size() > 0)
            {
                searchForm.setFormProperties(formPropertiesMap);
                // clear properties map
                formPropertiesMap.clear();
            }
            
            searchForm.setVisible(this.formVisible);
            this.formVisible = true;
            
            
            searchForm.setSearchContext(this.searchContext);
            
            
            if(logger.isDebugEnabled())logger.debug("new search form '" + searchForm.getName() + "' created");
            this.searchFormsList.add(searchForm);
        }
        
        public void setCategoryResourceBundle(String resourceBundleName)
        {
           this.categoryResourceBundle = this.getResourceBundle(resourceBundleName);            
        }
        
        public void createSearchFormContainer(String name, String categoryClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException
        {
            if(logger.isDebugEnabled())logger.debug("creating form container instance of class '" + categoryClass + "'");
            Class searchFormContainerClass = this.formClassLoader.loadClass(categoryClass);
            
            SearchFormContainer searchFormContainer = (SearchFormContainer)searchFormContainerClass.newInstance();
            searchFormContainer.setName(name);
            if(this.categoryResourceBundle != null)
            {
                //searchForm.setLocale(ResourceManager.getManager().getLocale());
                searchFormContainer.setResourceBundle(this.categoryResourceBundle);
                
                // clear reference
                this.categoryResourceBundle = null;
            }
            else if(logger.isDebugEnabled())
            {
                logger.warn("form container '" + name + "' does not support i182");
            }
            
            searchFormContainer.setVisible(this.containerVisible);
            this.containerVisible = true;
            
            try
            {
                //logger.debug("initalizing searchFormContainer '" + searchFormContainer.getName() + "'");
                searchFormContainer.intFormContainer(); 
            }
            catch(FormInitializationException fiexp)
            {
                // TODO show message dialog
                logger.fatal(fiexp.getMessage(), fiexp);
            }
            
            searchFormContainer.setSearchForms(this.searchFormsList);
            
            this.searchFormsList.clear();
            
            if(logger.isDebugEnabled())logger.debug("new search form container '" + searchFormContainer.getName() + "' created");
            this.searchFormContainerList.add(searchFormContainer);  
        }
        
        private ResourceBundle getResourceBundle(String resourceBundleName)
        {
            Locale locale = resource.getLocale();
            if(logger.isDebugEnabled())logger.debug("loading resource bundle '" + resourceBundleName + "' for locale '" + locale + "'");
            
            try
            {
                return ResourceBundle.getBundle(resourceBundleName, locale, this.formClassLoader);
            }
            catch(Throwable t)
            {
                logger.error("could not load resource bundle '" + resourceBundleName + "' for locale '" + locale + "'", t);
                return null;
            }
            
        }
        
        /*public void initSearchFormManager()
        {
            this.searchFormManager.setSearchFormContainers(this.searchFormContainerList);
        }*/ 
    } 
}
