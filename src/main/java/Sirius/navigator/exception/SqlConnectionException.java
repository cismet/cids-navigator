/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.exception;
/**
 * The SqlConnectionException class is used int the @link{Sirius.navigator.method.MethodManager} to indicate that a SQL
 * Exception in the Server was thrown, which should be visualised with a custom ErrorDialog This class extends
 * &#064;link{ConncetionExecption} since the @link(Conncetion} interface enforces the relevant method to throw a
 * &#064;link{ConncetionExecption}.
 *
 * @version  $Revision$, $Date$
 */
public class SqlConnectionException extends ConnectionException {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SQLConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public SqlConnectionException(final String message) {
        super(message);
    }

    /**
     * Creates a new SQLConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     */
    public SqlConnectionException(final String message, final int level) {
        super(message, level);
    }

    /**
     * Creates a new SQLConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public SqlConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new SQLConnectionException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public SqlConnectionException(final String message, final int level, final Throwable cause) {
        super(message, level, cause);
    }
}
