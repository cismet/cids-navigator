package Sirius.navigator.plugin;

/*******************************************************************************

  Copyright (c)     :       EIG (Environmental Informatics Group)
                            http://www.htw-saarland.de/eig
                            Prof. Dr. Reiner Guettler
                            Prof. Dr. Ralf Denzer

                            HTWdS
                            Hochschule fuer Technik und Wirtschaft des Saarlandes
                            Goebenstr. 40
                            66117 Saarbruecken
                            Germany

  Programmers       :       Pascal <pascal.dihe@enviromatics.net>

  Project           :       WuNDA 2
  Version           :       1.0
  Purpose           :
  Created           :       02/15/2003
  History           :       


*******************************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import org.apache.log4j.*;

import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.ui.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.plugin.context.*;
import Sirius.navigator.ui.embedded.*;

/**
 * blah<p>
 *
 * @version 1.0 02/15/2003
 * @author Pascal
 */
public class PluginDescriptor
{
    private final static Logger logger = Logger.getLogger(PluginDescriptor.class);
    
    public final static String XML_DESCRIPTOR = "plugin.xml";  // NOI18N
    
    /** Holds value of property metaInfo. */
    private PluginMetaInfo metaInfo = null;
    
    private HashMap methodDescriptors = null;
    
    private HashMap pluginUIs = null;
    
    /** Holds value of property id. */
    private String id = null;
    
    /** Holds value of property name. */
    private String name = null;
    
    /** Holds value of property version. */
    //private float version = 0;
    
    /** Holds value of property activated. */
    private boolean activated = false;
    
    /** Utility field used by bound properties. */
    private javax.swing.event.SwingPropertyChangeSupport propertyChangeSupport =  new javax.swing.event.SwingPropertyChangeSupport(this);
    
    /** Holds value of property plugin. */
    private PluginSupport plugin;
    
    //private HashMap mappingTable = new HashMap();
    
    /** Holds value of property context. */
    private PluginContext context;    
    
    /** Holds value of property pluginMenu. */
    private PluginMenu pluginMenu;
    
    /** Holds value of property pluginPopupMenu. */
    private PluginMenu pluginPopupMenu;
    
    /** Holds value of property pluginPath. */
    private final String pluginPath;
    
    /** Holds value of property pluginToolBar. */
    private EmbeddedToolBar pluginToolBar;
    
    /** Holds value of property loaded. */
    private boolean loaded = false;
    
    /** Holds value of property unloadable. */
    private boolean unloadable;
    
    /** Holds value of property progressObservable. */
    private boolean progressObservable;
    
    /** Holds value of property deactivateable. */
    private boolean deactivateable;
    
    /** Holds value of property propertyObservable. */
    private boolean propertyObservable;
    
    /** Holds value of property internationalized. */
    private boolean internationalized;
    
    /**
     * Holds value of property users.
     */
    private java.util.Collection users;
    
    /**
     * Holds value of property usergroups.
     */
    private java.util.Collection usergroups;
    
    public PluginDescriptor(String pluginPath)
    {
        this.pluginPath = pluginPath;
        this.users = new HashSet();
        this.usergroups = new HashSet();
    }
    
    /** 
     * 
     * Creates a new instance of PluginDescriptor 
     *
     * @param pluginRootUrl the root directory of all plugins
     * @param name the name of this plugin
     */
    //public PluginDescriptor(URL pluginRootUrl, String name) throws MalformedURLException
    //{
        /*this();
        
        logger.setLevel(Level.DEBUG);
        logger.info("creating new descriptor for plugin '" + name + "'");
        this.setName(name);
        if(logger.isDebugEnabled())logger.debug("setting plugin base url: '" + pluginRootUrl.toString() + "/" + this.getName() + "'");
        this.pluginUrl = new URL(pluginRootUrl.toString() + "/" + this.getName());*/
    //}
    
    /** Getter for property pluginInfo.
     * @return Value of property pluginInfo.
     *
     */
    public PluginMetaInfo getMetaInfo()
    {
        return this.metaInfo;
    }
    
    /** Setter for property pluginInfo.
     * @param pluginInfo New value of property pluginInfo.
     *
     */
    public void setMetaInfo(PluginMetaInfo metaInfo)
    {
        this.metaInfo = metaInfo;
    }
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName()
    {
        if(this.getMetaInfo() != null)
        {
            return this.getMetaInfo().getName();
        }
        else
        {
            return "null";  // NOI18N
        }
        
        
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    /*public void setName(String name)
    {
        this.name = name;
    }*/
    
    /** Getter for property version.
     * @return Value of property version.
     *
     */
    /*public float getVersion()
    {
        return this.version;
    }*/
    
    /** Setter for property version.
     * @param version New value of property version.
     *
     */
    /*public void setVersion(float version)
    {
        this.version = version;
    }*/
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    /** Getter for property active.
     * @return Value of property active.
     *
     */
    public boolean isActivated()
    {
        return this.activated;
    }
    
    /** Setter for property active.
     * @param active New value of property active.
     *
     */
    public void setActivated(boolean activated)
    {
        boolean oldActive = this.activated;
        this.activated = activated;
        propertyChangeSupport.firePropertyChange("activated", new Boolean(oldActive), new Boolean(activated));  // NOI18N
    }
    
    public PluginMethodDescriptor getMethodDescriptor(String id)
    {
        Object object = methodDescriptors.get(id);
        
        if(object != null && object.getClass().isAssignableFrom(PluginMethodDescriptor.class))
        {
            return (PluginMethodDescriptor)object;
        }
        else
        {
            return null;
        }
    }
    
    public Iterator getMethodDescriptors()
    {
        return methodDescriptors.values().iterator();
    }
    
    protected void setMethodDescriptors(HashMap methodDescriptors)
    {
        this.methodDescriptors = methodDescriptors;
    }
    
    public PluginUIDescriptor getUIDescriptor(String id)
    {
        Object object = pluginUIs.get(id);
        
        if(object != null && object.getClass().isAssignableFrom(PluginUIDescriptor.class))
        {
            return (PluginUIDescriptor)object;
        }
        else
        {
            return null;
        }
    }
    
    public Iterator getPluginUIDescriptors()
    {
        return pluginUIs.values().iterator();
    }
    
    protected void setUIDescriptors(HashMap pluginUIs)
    {
        this.pluginUIs = pluginUIs;
    }
    
   
    public String toString()
    {
        //return id;
        return this.getName();
    }
    
    public boolean equals(Object object)
    {
        if(object instanceof PluginDescriptor && this.getId().equals(((PluginDescriptor)object).getId())) 
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    /** 
     * Getter for property plugin.
     *
     * @return the plugin instance associated with this plugin descriptor
     */
    public PluginSupport getPlugin()
    {
        return this.plugin;
    }
    
    /** Setter for property plugin.
     * @param instance New value of property plugin.
     *
     */
    protected void setPlugin(PluginSupport plugin)
    {
        this.plugin = plugin;
    }
    
    /** Getter for property context.
     * @return Value of property context.
     *
     */
    public PluginContext getContext()
    {
        return this.context;
    }
    
    /** Setter for property context.
     * @param context New value of property context.
     *
     */
    protected void setContext(PluginContext context)
    {
        this.context = context;
    }
    
    /** Getter for property pluginMenu.
     * @return Value of property pluginMenu.
     *
     */
    public PluginMenu getPluginMenu()
    {
        return this.pluginMenu;
    }
    
    /** Setter for property pluginMenu.
     * @param pluginMenu New value of property pluginMenu.
     *
     */
    protected void setPluginMenu(PluginMenu pluginMenu)
    {
        this.pluginMenu = pluginMenu;
    }
    
    /** Getter for property pluginPopupMenu.
     * @return Value of property pluginPopupMenu.
     *
     */
    public PluginMenu getPluginPopupMenu()
    {
        return this.pluginPopupMenu;
    }
    
    /** Setter for property pluginPopupMenu.
     * @param pluginPopupMenu New value of property pluginPopupMenu.
     *
     */
    protected void setPluginPopupMenu(PluginMenu pluginPopupMenu)
    {
        this.pluginPopupMenu = pluginPopupMenu;
    }
    
    public boolean isPluginToolBarAvailable()
    {
        return this.pluginToolBar != null ? true : false;
    }
    
    public boolean isPluginMenuAvailable()
    {
        return this.pluginMenu != null ? true : false;
    }
    
    public boolean isPluginPopupMenuAvailable()
    {
        return this.pluginPopupMenu != null ? true : false;
    }
    
    public boolean isPluginMethodsAvailable()
    {
        return methodDescriptors != null ? true :  false;
    }
    
    public boolean isPluginMethodAvailable(String methodId)
    {
        if(isPluginMethodsAvailable())
        {
            return methodDescriptors.containsKey(methodId);
        }
        else
        {
            return false;
        }
    }
    
    public boolean isPluginUIDescriptorsAvailable()
    {
        return pluginUIs != null ? true :  false;
    }
    
    /** Getter for property pluginPath.
     * @return Value of property pluginPath.
     *
     */
    public String getPluginPath()
    {
        return this.pluginPath;
    }
    
    /** Setter for property pluginPath.
     * @param pluginPath New value of property pluginPath.
     *
     */
    public void setPluginPath(String pluginPath)
    {
    }
    
    /** Getter for property pluginToolBar.
     * @return Value of property pluginToolBar.
     *
     */
    public EmbeddedToolBar getPluginToolBar()
    {
        return this.pluginToolBar;
    }
    
    /** Setter for property pluginToolBar.
     * @param pluginToolBar New value of property pluginToolBar.
     *
     */
    public void setPluginToolBar(EmbeddedToolBar pluginToolBar)
    {
        this.pluginToolBar = pluginToolBar;
    }
    
    /** Getter for property loaded.
     * @return Value of property loaded.
     *
     */
    public boolean isLoaded()
    {
        return this.loaded;
    }
    
    /** Setter for property loaded.
     * @param loaded New value of property loaded.
     *
     */
    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }
    
    /** Getter for property loadable.
     * @return Value of property loadable.
     *
     */
    public boolean isUnloadable()
    {
        return this.unloadable;
    }
    
    /** Setter for property loadable.
     * @param loadable New value of property loadable.
     *
     */
    public void setUnloadable(boolean unloadable)
    {
        this.unloadable = unloadable;
    }
    

    
    /** Getter for property progressObservable.
     * @return Value of property progressObservable.
     *
     */
    public boolean isProgressObservable()
    {
        return this.progressObservable;
    }
    
    /** Setter for property progressObservable.
     * @param progressObservable New value of property progressObservable.
     *
     */
    public void setProgressObservable(boolean progressObservable)
    {
        this.progressObservable = progressObservable;
    }
    
    
    /** Getter for property deactivateable.
     * @return Value of property deactivateable.
     *
     */
    public boolean isDeactivateable()
    {
        return this.deactivateable;
    }
    
    /** Setter for property deactivateable.
     * @param deactivateable New value of property deactivateable.
     *
     */
    public void setDeactivateable(boolean deactivateable)
    {
        this.deactivateable = deactivateable;
    }
    
    /** Getter for property propertyObservable.
     * @return Value of property propertyObservable.
     *
     */
    public boolean isPropertyObservable()
    {
        return this.propertyObservable;
    }
    
    /** Setter for property propertyObservable.
     * @param propertyObservable New value of property propertyObservable.
     *
     */
    public void setPropertyObservable(boolean propertyObservable)
    {
        this.propertyObservable = propertyObservable;
    }
    
    
    /** Getter for property internationalized.
     * @return Value of property internationalized.
     *
     */
    public boolean isInternationalized()
    {
        return this.internationalized;
    }
    
    /** Setter for property internationalized.
     * @param internationalized New value of property internationalized.
     *
     */
    public void setInternationalized(boolean internationalized)
    {
        this.internationalized = internationalized;
    }
    
    /**
     * Getter for property users.
     * @return Value of property users.
     */
    public java.util.Collection getUsers()
    {
        return this.users;
    }
    
    /**
     * Getter for property usergroups.
     * @return Value of property usergroups.
     */
    public java.util.Collection getUsergroups()
    {
        return this.usergroups;
    }
    
    /**
     * Hilfsmethode f\u00FCr digester
     */
    public void addUser(String user)
    {
        if(logger.isDebugEnabled())logger.debug("adding user: " + user);  // NOI18N
        this.getUsers().add(user);
    }
    
    /**
     * Hilfsmethode f\u00FCr digester
     */
    public void addUsergroup(String usergroup)
    {
        if(logger.isDebugEnabled())logger.debug("adding usergroup: " + usergroup);  // NOI18N
        this.getUsergroups().add(usergroup);
    }  
}
