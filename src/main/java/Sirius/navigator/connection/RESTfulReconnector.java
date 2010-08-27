package Sirius.navigator.connection;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import de.cismet.reconnector.ReconnectorException;
import de.cismet.reconnector.Reconnector;
import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;
import de.cismet.lookupoptions.options.ProxyOptionsPanel;
import de.cismet.security.Proxy;


/**
 *
 * @author jruiz
 */
public class RESTfulReconnector <R extends CallServerService> extends Reconnector<R> {

    private final static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RESTfulReconnector.class);

    private String callserverURL;
    private Proxy proxy;
    private RESTfulReconnectorErrorPanel errorPanel;

    public RESTfulReconnector(final Class serviceClass, final String callserverURL, final Proxy proxy) {
        super(serviceClass);

        this.callserverURL = callserverURL;
        this.proxy = proxy;
        ProxyOptionsPanel pop = new ProxyOptionsPanel();
        pop.setProxy(Proxy.fromPreferences());
        this.errorPanel = new RESTfulReconnectorErrorPanel(pop, this);
    }

    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    protected ReconnectorException getReconnectorException(final Throwable exception) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug(exception);
        }
        if (exception instanceof UniformInterfaceException || exception instanceof ClientHandlerException) {
            String errormessage = org.openide.util.NbBundle.getMessage(RESTfulReconnector.class, "RESTfulReconnector.errormessage");
            if (LOG.isDebugEnabled()) {
                LOG.debug(errormessage);
            }
            errorPanel.setErrorMessage(errormessage);
            return new ReconnectorException(errorPanel);
        } else  {
            throw exception;
        }
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
        return (R) new RESTfulSerialInterfaceConnector(callserverURL, proxy);
    }

}
