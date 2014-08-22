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

import Sirius.server.newuser.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.openide.util.Exceptions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;

import de.cismet.cids.server.actions.HttpTunnelAction;
import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.commons.security.Tunnel;

import de.cismet.netutil.tunnel.TunnelTargetGroup;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CallServerTunnel implements Tunnel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String tunnelActionName = "httpTunnelAction";
    private static ObjectMapper mapper = new ObjectMapper();
    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            CallServerTunnel.class);

    //~ Instance fields --------------------------------------------------------

    private String callserverName = "not set";
    private volatile User user = null;
    private HashMap<String, String[]> tunnelTargetGroupRegExs = new HashMap<String, String[]>();
    private volatile ArrayList<String> userKeyList = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CallServerTunnel object.
     *
     * @param  callserverName  DOCUMENT ME!
     */
    public CallServerTunnel(final String callserverName) {
        try {
            this.callserverName = callserverName;
            final TunnelTargetGroup[] tGroups = mapper.readValue(CallServerTunnel.class.getResourceAsStream(
                        "/de/cismet/cids/client/tools/tunnelTargets.json"),
                    TunnelTargetGroup[].class);
            for (final TunnelTargetGroup ttg : tGroups) {
                tunnelTargetGroupRegExs.put(ttg.getTargetGroupkey(), ttg.getTargetExpressions());
            }
        } catch (Exception ex) {
            LOG.warn(
                "Problem during Parsing of the TunnelTargetGroups. Will alwas return false in isResponsible(). No tunnel functionality available.",
                ex);
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isResponsible(final ACCESS_METHODS method, final String url) {
        switch (method) {
            case POST_REQUEST_NO_TUNNEL:
            case GET_REQUEST_NO_TUNNEL: {
                return false;
            }
            case HEAD_REQUEST:
            case GET_REQUEST:
            case POST_REQUEST: {
                try {
                    for (final String key : tunnelTargetGroupRegExs.keySet()) {
                        if (getUserKeyList().contains(key.trim())) {
                            for (final String regex : tunnelTargetGroupRegExs.get(key)) {
                                if (url.matches(regex)) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug(new StringBuilder("Tunnel hit: ").append(key).append('(').append(
                                                regex).append(") for:\n").append(url).append('\n').append(method));
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.warn(
                        "Exception in isResponsible of CallserverTunnel. Will return false. No tunnel functionality available.",
                        e);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuilder("Tunnel miss for:\n").append(url).append('\n').append(method));
        }
        return false;
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
        if (result != null) {
            return new ByteArrayInputStream(result);
        } else {
            return null;
        }
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

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ArrayList<String> getUserKeyList() {
        try {
            if (userKeyList == null) {
                synchronized (this) {
                    if (userKeyList == null) {
                        user = SessionManager.getSession().getUser();
                        final String configAttr = SessionManager.getProxy().getConfigAttr(user, "tunnel.targetgroups");
                        if (configAttr != null) {
                            final String[] keys = configAttr.split(",");
                            userKeyList = new ArrayList<String>(keys.length);
                            for (final String s : keys) {
                                userKeyList.add(s.trim());
                            }
                        } else {
                            userKeyList = new ArrayList<String>(1);
                        }
                    }
                }
            }
            return userKeyList;
        } catch (Exception e) {
            LOG.warn("Exception during retrieval of tunnelUserKeyList. Will be empty.", e);
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        try {
            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "WUNDA_BLAU",
                "Administratoren",
                "admin",
                "kif");
            final CallServerTunnel cst = new CallServerTunnel("kjdfhg");

            System.out.println(cst.isResponsible(
                    ACCESS_METHODS.GET_REQUEST,
                    "http://chaos.wuppertal-intra.de/weird/path/to/nonsense"));
            System.out.println(cst.isResponsible(ACCESS_METHODS.GET_REQUEST, "http://www.google.de"));
            System.out.println(cst.isResponsible(ACCESS_METHODS.GET_REQUEST, "http://s10221./path/to/X"));
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            System.exit(0);
        }
    }
}
