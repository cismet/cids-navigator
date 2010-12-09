/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.lookupoptions.options.ProxyOptionsPanel;

import de.cismet.reconnector.Reconnector;
import de.cismet.reconnector.ReconnectorException;

import de.cismet.security.Proxy;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class RESTfulReconnector<R extends CallServerService> extends Reconnector<R> {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RESTfulReconnector.class);

    //~ Instance fields --------------------------------------------------------

    private String callserverURL;
    private Proxy proxy;
    private RESTfulReconnectorErrorPanel errorPanel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RESTfulReconnector object.
     *
     * @param  serviceClass   DOCUMENT ME!
     * @param  callserverURL  DOCUMENT ME!
     * @param  proxy          DOCUMENT ME!
     */
    public RESTfulReconnector(final Class serviceClass, final String callserverURL, final Proxy proxy) {
        super(serviceClass);

        this.callserverURL = callserverURL;
        this.proxy = proxy;
        final ProxyOptionsPanel pop = new ProxyOptionsPanel();
        pop.setProxy(proxy);
        this.errorPanel = new RESTfulReconnectorErrorPanel(pop, this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  proxy  DOCUMENT ME!
     */
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    protected ReconnectorException getReconnectorException(final Throwable exception) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getReconnectorException(exception) invoked", exception);
        }
        boolean error = false;
        if (exception instanceof UniformInterfaceException) {
            if (exception instanceof UniformInterfaceException) {
                final int status = ((UniformInterfaceException)exception).getResponse().getStatus();
                error = (status == 503) || (status == 407);
            }
        } else if (exception instanceof ClientHandlerException) {
            error = true;
        }

        if (error) {
            final String message = org.openide.util.NbBundle.getMessage(
                    RESTfulReconnector.class,
                    "RESTfulReconnector.errormessage");
            if (LOG.isDebugEnabled()) {
                LOG.debug(message);
            }
            errorPanel.setError(message, exception);
            return new ReconnectorException(errorPanel);
        }

        throw exception;
    }

    @Override
    protected R connectService() {
//        //TODO ab hier entfernen !!! nur zum Testen !
//        try {
//
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            log.debug("Sleep interrupted", ex);
//        }
//        //TODO bis hier entfernen !!! nur zum Testen !
        return (R)new RESTfulSerialInterfaceConnector(callserverURL, proxy);
    }
}
