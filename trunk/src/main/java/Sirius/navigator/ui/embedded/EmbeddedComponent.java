/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EmbeddedComponent.java
 *
 * Created on 27. M\u00E4rz 2003, 09:39
 */
package Sirius.navigator.ui.embedded;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface EmbeddedComponent {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property visible.
     *
     * @return  Value of property visible.
     */
    boolean isVisible();

    /**
     * Setter for property visible.
     *
     * @param  visible  New value of property visible.
     */
    void setVisible(boolean visible);

    /**
     * Getter for property enabled.
     *
     * @return  Value of property enabled.
     */
    boolean isEnabled();

    /**
     * Setter for property enabled.
     *
     * @param  enabled  New value of property enabled.
     */
    void setEnabled(boolean enabled);

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    String getId();

    /**
     * Setter for property id.
     *
     * @return  DOCUMENT ME!
     */
    // public void setId(String id);

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    String getName();

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    void setName(String name);
}
