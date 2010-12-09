/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EmbeddedComponentsMap.java
 *
 * Created on 27. M\u00E4rz 2003, 09:43
 */
package Sirius.navigator.ui.embedded;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface EmbeddedComponentsMap {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property name.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property name.
     */
    String getName(String id);

    /**
     * Setter for property name.
     *
     * @param   id  name New value of property name.
     *
     * @return  DOCUMENT ME!
     */
    // public void setName(String id, String name);

    /**
     * Getter for property visible.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property visible.
     */
    boolean isVisible(String id);

    /**
     * Setter for property visible.
     *
     * @param  id       DOCUMENT ME!
     * @param  visible  New value of property visible.
     */
    void setVisible(String id, boolean visible);

    /**
     * Getter for property enabled.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property enabled.
     */
    boolean isEnabled(String id);

    /**
     * Setter for property enabled.
     *
     * @param  id       DOCUMENT ME!
     * @param  enabled  New value of property enabled.
     */
    void setEnabled(String id, boolean enabled);

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isAvailable(String id);

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     */
    void add(EmbeddedComponent component);

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    void remove(String id);

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    EmbeddedComponent get(String id);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    java.util.Iterator getEmbeddedComponents();
}
