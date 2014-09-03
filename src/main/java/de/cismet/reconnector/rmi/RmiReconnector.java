/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector.rmi;

import java.net.MalformedURLException;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;

import de.cismet.reconnector.Reconnector;
import de.cismet.reconnector.ReconnectorException;
import org.openide.util.NbBundle;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class RmiReconnector<R extends Remote> extends Reconnector<R> {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RmiReconnector.class);

    public static final String WRONG_VERSION = NbBundle.getMessage(RmiReconnector.class, "wrong_version");
    public static final String CONNECTION_LOST = NbBundle.getMessage(RmiReconnector.class, "connection_lost");
    public static final String LOOKUP_FAILED = NbBundle.getMessage(RmiReconnector.class, "lookup_failed");
    public static final String UNKNOWN = NbBundle.getMessage(RmiReconnector.class, "unknown_error");

    //~ Instance fields --------------------------------------------------------

    private String serviceUrl;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RmiReconnector object.
     *
     * @param  serviceClass  DOCUMENT ME!
     * @param  serviceUrl    DOCUMENT ME!
     */
    public RmiReconnector(final Class serviceClass, final String serviceUrl) {
        super(serviceClass);
        this.serviceUrl = serviceUrl;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ReconnectorException getReconnectorException(final Throwable exception) throws Throwable {
        if (exception instanceof ConnectException) {
            return new ReconnectorException(CONNECTION_LOST);
        } else if (exception instanceof ServerException) {
            final Throwable serverCause = exception.getCause();
            if (serverCause instanceof RemoteException) {
                final Throwable remoteCause = (RemoteException)exception.getCause();
                if (remoteCause.getCause() instanceof NullPointerException) {
                    return new ReconnectorException(CONNECTION_LOST);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(exception, exception);
        }
        throw exception;
    }

    @Override
    protected R connectService() throws ReconnectorException {
        try {
//            //TODO ab hier entfernen !!! nur zum Testen !
//            try {
//
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {
//                if (LOG.isDebugEnabled()) {
//                  log.debug("Sleep interrupted", ex);
//                }
//            }
//            //TODO bis hier entfernen !!! nur zum Testen !
            return (R)Naming.lookup(serviceUrl);
        } catch (NotBoundException nbe) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[NetworkError] could not connect to '" + serviceUrl + "'", nbe); //NOI18N
            }
            throw new ReconnectorException(LOOKUP_FAILED);
        } catch (MalformedURLException mue) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("'" + serviceUrl + "' is not a valid URL", mue); //NOI18N
            }
            throw new ReconnectorException(LOOKUP_FAILED);
        } catch (RemoteException re) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[ServerError] could not connect to '" + serviceUrl + "'", re); //NOI18N
            }
            throw new ReconnectorException(LOOKUP_FAILED);
        }
    }
}
