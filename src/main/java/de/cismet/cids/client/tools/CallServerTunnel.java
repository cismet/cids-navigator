/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.client.tools;

import Sirius.navigator.connection.SessionManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.HashMap;

import de.cismet.cids.server.actions.HttpTunnelAction;
import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.security.Tunnel;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CallServerTunnel implements Tunnel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String tunnelActionName = "httpTunnelAction";

    //~ Instance fields --------------------------------------------------------

    private String callserverName = "not set";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CallServerTunnel object.
     *
     * @param  callserverName  DOCUMENT ME!
     */
    public CallServerTunnel(final String callserverName) {
        this.callserverName = callserverName;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isResponsible(final ACCESS_METHODS method, final String url) {
        return true;
    }

    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final ACCESS_METHODS method,
            final HashMap<String, String> options) throws Exception {
        final ServerActionParameter urlSAP = new ServerActionParameter<URL>(HttpTunnelAction.PARAMETER_TYPE.URL
                        .toString(),
                url);
        final Object nullBody = null;
        final ServerActionParameter methodSAP = new ServerActionParameter<ACCESS_METHODS>(
                HttpTunnelAction.PARAMETER_TYPE.METHOD.toString(),
                method);
        final ServerActionParameter optionsSAP = new ServerActionParameter<HashMap<String, String>>(
                HttpTunnelAction.PARAMETER_TYPE.OPTIONS.toString(),
                options);

        final StringBuilder parameter = new StringBuilder();
        final BufferedReader reader = new BufferedReader(requestParameter);

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }
        final ServerActionParameter parameterSAP = new ServerActionParameter<String>(
                HttpTunnelAction.PARAMETER_TYPE.REQUEST.toString(),
                parameter.toString());

        final byte[] result = (byte[])SessionManager.getProxy()
                    .executeTask(
                            tunnelActionName,
                            callserverName,
                            nullBody,
                            urlSAP,
                            parameterSAP,
                            methodSAP,
                            optionsSAP);
        return new ByteArrayInputStream(result);
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ACCESS_HANDLER_TYPES getHandlerType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAccessMethodSupported(final ACCESS_METHODS method) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
