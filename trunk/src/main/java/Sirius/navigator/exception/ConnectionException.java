/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.exception;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
*/

import java.io.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ConnectionException extends NavigatorException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public ConnectionException(final String message) {
        super(message);
    }

    /**
     * Creates a new ConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     */
    public ConnectionException(final String message, final int level) {
        super(message, level);
    }

    /**
     * Creates a new ConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public ConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new ConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public ConnectionException(final String message, final int level, final Throwable cause) {
        super(message, level, cause);
    }
}
