/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.interfaces;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface PluginMethod {

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns the unique id of this action.
     *
     * @return  DOCUMENT ME!
     */
    String getId();

    /**
     * Performs the plugin action.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    void invoke() throws Exception;
}
