/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.exception;

import Sirius.navigator.resource.ResourceManager;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class NavigatorException extends Exception {

    //~ Static fields/initializers ---------------------------------------------

    public static final int WARNING = ExceptionManager.WARNING;
    public static final int ERROR = ExceptionManager.ERROR;
    public static final int FATAL = ExceptionManager.FATAL;

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    protected final int level;
    protected final String name;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public NavigatorException(final String message) {
        this(message, FATAL, null);
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public NavigatorException(final String message, final Throwable cause) {
        this(message, FATAL, cause);
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     */
    public NavigatorException(final String message, final int level) {
        this(message, level, null);
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  level      DOCUMENT ME!
     * @param  errorcode  DOCUMENT ME!
     */
    public NavigatorException(final int level, final String errorcode) {
        super(resource.getExceptionMessage(errorcode));
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  level    DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public NavigatorException(final String message, final int level, final Throwable cause) {
        super(message, cause);
        this.level = level;
        this.name = this.getClass().getName();
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  level      DOCUMENT ME!
     * @param  errorcode  DOCUMENT ME!
     * @param  cause      DOCUMENT ME!
     */
    public NavigatorException(final int level, final String errorcode, final Throwable cause) {
        super(resource.getExceptionMessage(errorcode), cause);
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }

    /**
     * Creates a new NavigatorException object.
     *
     * @param  level      DOCUMENT ME!
     * @param  errorcode  DOCUMENT ME!
     * @param  values     DOCUMENT ME!
     * @param  cause      DOCUMENT ME!
     */
    public NavigatorException(final int level, final String errorcode, final String[] values, final Throwable cause) {
        super(resource.getExceptionMessage(errorcode, values), cause);
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return this.name;
    }
}
