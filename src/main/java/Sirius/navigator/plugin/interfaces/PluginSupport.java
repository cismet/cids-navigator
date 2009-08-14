package Sirius.navigator.plugin.interfaces;

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

import java.beans.*;
import java.util.*;
import javax.swing.*;

import Sirius.navigator.plugin.*;



/**
 * blah<p>
 *
 * @version 1.0 02/15/2003
 * @author Pascal
 */
public interface PluginSupport
{    
    //public final static String PROPERTY_LOADED = "loaded";
    //public final static String PROPERTY_ACTIVE = "active";
    //public final static String PROPERTY_VISIBLE = "visible";
    
    
    public PluginUI getUI(String id);
    
    public Iterator getUIs();
    
    
    public PluginMethod getMethod(String id);
    
    public Iterator getMethods();
    
    
    public PluginProperties getProperties();
        
    
    //public MetaSelectionListener getMetaSelectionListener();
    
    //public ResourceBundle getResourceBundle(Locale locale);
    
    //public ImageIcon getImageIcon(String id);
    
    
    //public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    
    //public void addPropertyChangeListener(PropertyChangeListener listener);
    
    //public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
    
    //public void removePropertyChangeListener(PropertyChangeListener listener);
    
    
    public void setActive(boolean active);
    
    public void setVisible(boolean visible);
}
