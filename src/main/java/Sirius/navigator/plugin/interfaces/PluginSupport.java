/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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

import Sirius.navigator.plugin.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;

/**
 * blah.
 *
 * @author   Pascal
 * @version  1.0 02/15/2003
 */
public interface PluginSupport {

    //~ Methods ----------------------------------------------------------------

    // public final static String PROPERTY_LOADED = "loaded";
    // public final static String PROPERTY_ACTIVE = "active";
    // public final static String PROPERTY_VISIBLE = "visible";

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    PluginUI getUI(String id);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Iterator getUIs();

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    PluginMethod getMethod(String id);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Iterator getMethods();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    PluginProperties getProperties();

    /**
     * public MetaSelectionListener getMetaSelectionListener(); public ResourceBundle getResourceBundle(Locale locale);
     * public ImageIcon getImageIcon(String id); public void addPropertyChangeListener(String propertyName,
     * PropertyChangeListener listener); public void addPropertyChangeListener(PropertyChangeListener listener); public
     * void removePropertyChangeListener(String propertyName, PropertyChangeListener listener); public void
     * removePropertyChangeListener(PropertyChangeListener listener);
     *
     * @param  active  DOCUMENT ME!
     */
    void setActive(boolean active);

    /**
     * DOCUMENT ME!
     *
     * @param  visible  DOCUMENT ME!
     */
    void setVisible(boolean visible);
}
