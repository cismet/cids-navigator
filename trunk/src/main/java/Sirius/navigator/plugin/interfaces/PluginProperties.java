/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginProperties.java
 *
 * Created on 21. September 2004, 10:40
 */
package Sirius.navigator.plugin.interfaces;

import java.beans.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface PluginProperties {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  value         DOCUMENT ME!
     */
    void setProperty(String propertyName, Object value);

    /**
     * DOCUMENT ME!
     *
     * @param   propertyName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getProperty(String propertyName);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  listener      DOCUMENT ME!
     */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  listener      DOCUMENT ME!
     */
    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
