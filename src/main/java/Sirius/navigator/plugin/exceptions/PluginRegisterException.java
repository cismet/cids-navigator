/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginRegisterException.java
 *
 * Created on 18. Juni 2003, 09:45
 */
package Sirius.navigator.plugin.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginRegisterException extends PluginException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginRegisterException.
     *
     * @param  errorcode  DOCUMENT ME!
     * @param  values     DOCUMENT ME!
     * @param  cause      DOCUMENT ME!
     */
    public PluginRegisterException(final String errorcode, final String[] values, final Throwable cause) {
        super(PluginException.ERROR, errorcode, values, cause);
    }
}
