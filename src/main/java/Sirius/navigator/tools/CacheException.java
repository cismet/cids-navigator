/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class CacheException extends Exception {

    //~ Instance fields --------------------------------------------------------

    private final Object cacheReference;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>CacheException</code> without detail message.
     */
    public CacheException() {
        this(null, null, null);
    }

    /**
     * Constructs an instance of <code>CacheException</code> with the specified detail message.
     *
     * @param  msg  the detail message.
     */
    public CacheException(final String msg) {
        this(null, msg, null);
    }

    /**
     * Constructs an instance of <code>CacheException</code> with the specified detail message and the specified cause.
     *
     * @param  msg    the detail message.
     * @param  cause  the exception cause
     */
    public CacheException(final String msg, final Throwable cause) {
        this(null, msg, cause);
    }

    /**
     * Creates a new CacheException object.
     *
     * @param  cacheReference  DOCUMENT ME!
     * @param  message         DOCUMENT ME!
     * @param  cause           DOCUMENT ME!
     */
    public CacheException(final Object cacheReference, final String message, final Throwable cause) {
        super(message, cause);

        this.cacheReference = cacheReference;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getCacheReference() {
        return cacheReference;
    }
}
