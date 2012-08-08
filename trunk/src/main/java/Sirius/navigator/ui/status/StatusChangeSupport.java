/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * StatusChangeSupport.java
 *
 * Created on 17. April 2003, 12:02
 */
package Sirius.navigator.ui.status;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface StatusChangeSupport {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addStatusChangeListener(StatusChangeListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void removeStatusChangeListener(StatusChangeListener listener);
}
