/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginException.java
 *
 * Created on 18. Februar 2003, 12:03
 */
package Sirius.navigator.plugin.exceptions;

import Sirius.navigator.exception.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginException extends NavigatorException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginException.
     *
     * @param  level      DOCUMENT ME!
     * @param  errorcode  DOCUMENT ME!
     * @param  values     DOCUMENT ME!
     * @param  cause      DOCUMENT ME!
     */
    public PluginException(final int level, final String errorcode, final String[] values, final Throwable cause) {
        super(level, errorcode, values, cause);
    }
}
