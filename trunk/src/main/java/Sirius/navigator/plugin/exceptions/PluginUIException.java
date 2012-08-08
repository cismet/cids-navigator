/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginUIException.java
 *
 * Created on 18. Februar 2003, 12:04
 */
package Sirius.navigator.plugin.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginUIException extends PluginException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginUIException.
     *
     * @param  level      DOCUMENT ME!
     * @param  errorcode  DOCUMENT ME!
     * @param  values     DOCUMENT ME!
     * @param  cause      DOCUMENT ME!
     */
    public PluginUIException(final int level, final String errorcode, final String[] values, final Throwable cause) {
        super(level, errorcode, values, cause);
    }
}
