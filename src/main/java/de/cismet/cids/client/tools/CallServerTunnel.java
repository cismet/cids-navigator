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
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.newuser.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.BasicScheme;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.io.IOUtils;

import org.openide.util.Exceptions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.cismet.cids.server.actions.HttpTunnelAction;
import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.commons.security.AccessHandler;
import de.cismet.commons.security.Tunnel;
import de.cismet.commons.security.exceptions.CannotReadFromURLException;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.netutil.tunnel.TunnelTargetGroup;

import de.cismet.security.GUICredentialsProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CallServerTunnel implements Tunnel, ConnectionContextProvider {

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

    private final transient Map<String, TunnelGUICredentialsProvider> credentialsForURLS =
        new HashMap<String, TunnelGUICredentialsProvider>();

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CallServerTunnel object.
     *
     * @param  callserverName  DOCUMENT ME!
     */
    public CallServerTunnel(final String callserverName) {
        this.callserverName = callserverName;

        try {
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

        byte[] result = null;
        Object res = null;
        try {
            TunnelGUICredentialsProvider cp = credentialsForURLS.get(url.toString());
            if (cp == null) {
                res = SessionManager.getProxy()
                            .executeTask(
                                    tunnelActionName,
                                    callserverName,
                                    nullBody,
                                    getConnectionContext(),
                                    urlSAP,
                                    parameterSAP,
                                    methodSAP,
                                    optionsSAP);
            } else {
                res = executeWithCreds(
                        tunnelActionName,
                        callserverName,
                        nullBody,
                        urlSAP,
                        parameterSAP,
                        methodSAP,
                        optionsSAP,
                        cp.getCredentials());
            }

            if ((res instanceof CannotReadFromURLException)) {
                if (cp == null) {
                    cp = createSynchronizedCP(url);
                }

                UsernamePasswordCredentials creds = null;
                do {
                    cp.getCredentials(new BasicScheme(), null, -1, false);
                    creds = cp.getCredentials();
                } while ((creds == null) && !cp.isAuthenticationCanceled());
                res = executeWithCreds(
                        tunnelActionName,
                        callserverName,
                        nullBody,
                        urlSAP,
                        parameterSAP,
                        methodSAP,
                        optionsSAP,
                        creds);
            }
            result = (byte[])res;
        } catch (final Exception ex) {
            if (ex.getCause() instanceof Exception) {
                throw (Exception)ex.getCause();
            }
        }

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
                        final String configAttr = SessionManager.getProxy()
                                    .getConfigAttr(user, "tunnel.targetgroups", getConnectionContext());
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
                null,
                "admin",
                "leo",
                true);
            final CallServerTunnel cst = new CallServerTunnel("kjdfhg");

            System.out.println(cst.isResponsible(
                    ACCESS_METHODS.GET_REQUEST,
                    "http://chaos.wuppertal-intra.de/weird/path/to/nonsense"));
            System.out.println(cst.isResponsible(ACCESS_METHODS.GET_REQUEST, "http://www.google.de"));
            System.out.println(cst.isResponsible(ACCESS_METHODS.GET_REQUEST, "http://s10221./path/to/X"));

            final InputStream is = cst.doRequest(new URL("http://cismet.de/"),
                    new StringReader(""),
                    ACCESS_METHODS.GET_REQUEST,
                    new LinkedHashMap());
            if (is != null) {
                System.out.println(IOUtils.toString(is));
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected CredentialsProvider getCredentialProvider(final URL url) {
        TunnelGUICredentialsProvider cp = credentialsForURLS.get(url.toString());
        if (cp == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no Credential Provider available for url: " + url);
            }
            cp = createSynchronizedCP(url);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credential Provider available for url: " + url);
            }
        }

        return cp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized TunnelGUICredentialsProvider createSynchronizedCP(final URL url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Credential Provider should be created synchronously"); // NOI18N
        }

        TunnelGUICredentialsProvider cp = credentialsForURLS.get(url.toString());
        if (cp == null) {
            cp = new TunnelGUICredentialsProvider(url);
            if (LOG.isDebugEnabled()) {
                LOG.debug("A new Credential Provider instance was created for: " + url.toString()); // NOI18N
            }
            credentialsForURLS.put(url.toString(), cp);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credential Provider was already available: " + url.toString());          // NOI18N
            }
        }

        return cp;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetCredentials() {
        for (final GUICredentialsProvider prov : credentialsForURLS.values()) {
            prov.setUsernamePassword(null);
        }
        credentialsForURLS.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   taskname      DOCUMENT ME!
     * @param   taskdomain    DOCUMENT ME!
     * @param   body          DOCUMENT ME!
     * @param   urlSAP        DOCUMENT ME!
     * @param   parameterSAP  DOCUMENT ME!
     * @param   methodSAP     DOCUMENT ME!
     * @param   optionsSAP    DOCUMENT ME!
     * @param   creds         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private Object executeWithCreds(final String taskname,
            final String taskdomain,
            final Object body,
            final ServerActionParameter urlSAP,
            final ServerActionParameter parameterSAP,
            final ServerActionParameter methodSAP,
            final ServerActionParameter optionsSAP,
            final UsernamePasswordCredentials creds) throws Exception {
        final ServerActionParameter credentialsSAP;
        if (creds != null) {
            final HashMap<String, String> credOptions = new HashMap<String, String>();
            credOptions.put(HttpTunnelAction.CREDENTIALS_USERNAME_KEY, creds.getUserName());
            credOptions.put(HttpTunnelAction.CREDENTIALS_PASSWORD_KEY, creds.getPassword());
            credentialsSAP = new ServerActionParameter<HashMap<String, String>>(
                    HttpTunnelAction.PARAMETER_TYPE.CREDENTIALS.toString(),
                    credOptions);
        } else {
            credentialsSAP = null;
        }
        return SessionManager.getProxy()
                    .executeTask(
                        taskname,
                        taskdomain,
                        body,
                        getConnectionContext(),
                        urlSAP,
                        parameterSAP,
                        methodSAP,
                        optionsSAP,
                        credentialsSAP);
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class TunnelGUICredentialsProvider extends GUICredentialsProvider {

        //~ Instance fields ----------------------------------------------------

        private URL url;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MyGCR object.
         *
         * @param  url  DOCUMENT ME!
         */
        public TunnelGUICredentialsProvider(final URL url) {
            super(url, ComponentRegistry.getRegistry().getMainWindow());

            this.url = url;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean testConnection(final UsernamePasswordCredentials creds) {
            try {
                final ServerActionParameter urlSAP = new ServerActionParameter<URL>(HttpTunnelAction.PARAMETER_TYPE.URL
                                .toString(),
                        url);

                final ServerActionParameter parameterSAP = new ServerActionParameter<String>(
                        HttpTunnelAction.PARAMETER_TYPE.REQUEST.toString(),
                        "");
                final ServerActionParameter methodSAP = new ServerActionParameter<ACCESS_METHODS>(
                        HttpTunnelAction.PARAMETER_TYPE.METHOD.toString(),
                        AccessHandler.ACCESS_METHODS.GET_REQUEST);
                final ServerActionParameter optionsSAP = new ServerActionParameter<HashMap<String, String>>(
                        HttpTunnelAction.PARAMETER_TYPE.OPTIONS.toString(),
                        null);

                final Object res = executeWithCreds(
                        tunnelActionName,
                        callserverName,
                        null,
                        urlSAP,
                        parameterSAP,
                        methodSAP,
                        optionsSAP,
                        creds);
                if (res instanceof CannotReadFromURLException) {
                    return false;
                }
                final byte[] instanceTest = (byte[])res;
                return true;
            } catch (final Exception ex) {
                return false;
            }
        }
    }
}
