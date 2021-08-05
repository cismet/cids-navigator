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

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.awt.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ProgressMonitorInputStream;

import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.actions.WebDavTunnelAction;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.Proxy;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class WebDavTunnelHelper {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(WebDavTunnelHelper.class);

    //~ Instance fields --------------------------------------------------------

    private final Proxy proxy;
    private final String username;
    private final String password;
    private final Boolean useNtAuth;
    private final String domain;
    private final String actionName;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebDavHelper object.
     *
     * @param  domain  DOCUMENT ME!
     */
    public WebDavTunnelHelper(final String domain) {
        this(domain, WebDavTunnelAction.TASK_NAME);
    }

    /**
     * Creates a new WebDavHelper object.
     *
     * @param  domain      DOCUMENT ME!
     * @param  actionName  DOCUMENT ME!
     */
    public WebDavTunnelHelper(final String domain, final String actionName) {
        this(domain, actionName, Proxy.fromPreferences(), null, null, false);
    }

    /**
     * Creates a new WebDavHelper object.
     *
     * @param  domain     DOCUMENT ME!
     * @param  proxy      DOCUMENT ME!
     * @param  useNtAuth  DOCUMENT ME!
     */
    public WebDavTunnelHelper(final String domain, final Proxy proxy, final Boolean useNtAuth) {
        this(domain, WebDavTunnelAction.TASK_NAME, proxy);
    }

    /**
     * Creates a new WebDavHelper object.
     *
     * @param  domain      DOCUMENT ME!
     * @param  actionName  DOCUMENT ME!
     * @param  proxy       DOCUMENT ME!
     */
    public WebDavTunnelHelper(final String domain, final String actionName, final Proxy proxy) {
        this(domain, actionName, proxy, null, null, null);
    }

    /**
     * Creates a new WebDavHelper object.
     *
     * @param       domain     DOCUMENT ME!
     * @param       proxy      DOCUMENT ME!
     * @param       username   DOCUMENT ME!
     * @param       password   DOCUMENT ME!
     * @param       useNtAuth  DOCUMENT ME!
     *
     * @deprecated  client side credentials are not a good solution better should use specific tunnelaction
     *              implementation which knows the credentials
     */
    @Deprecated
    public WebDavTunnelHelper(final String domain,
            final Proxy proxy,
            final String username,
            final String password,
            final Boolean useNtAuth) {
        this(domain, WebDavTunnelAction.TASK_NAME, proxy, username, password, useNtAuth);
    }

    /**
     * Creates a new WebDavHelper object.
     *
     * @param  domain      DOCUMENT ME!
     * @param  actionName  DOCUMENT ME!
     * @param  proxy       DOCUMENT ME!
     * @param  username    DOCUMENT ME!
     * @param  password    DOCUMENT ME!
     * @param  useNtAuth   DOCUMENT ME!
     */
    @Deprecated
    public WebDavTunnelHelper(final String domain,
            final String actionName,
            final Proxy proxy,
            final String username,
            final String password,
            final Boolean useNtAuth) {
        this.proxy = proxy;
        this.username = username;
        this.password = password;
        this.useNtAuth = useNtAuth;
        this.domain = domain;
        this.actionName = actionName;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   prefix        DOCUMENT ME!
     * @param   originalFile  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String generateWebDAVFileName(final String prefix, final File originalFile) {
        final String[] fileNameSplit = originalFile.getName().split("\\.");
        String webFileName = prefix + System.currentTimeMillis() + "-" + Math.abs(originalFile.getName().hashCode());
        if (fileNameSplit.length > 1) {
            final String ext = fileNameSplit[fileNameSplit.length - 1];
            webFileName += "." + ext;
        }
        return webFileName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Collection<ServerActionParameter> createParams() {
        final Collection<ServerActionParameter> params = new ArrayList<>();
        if (proxy != null) {
            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.PROXY.toString(),
                    proxy));
        }
        if (username != null) {
            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.USERNAME.toString(),
                    username));
        }
        if (password != null) {
            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.PASSWORD.toString(),
                    password));
        }
        if (useNtAuth != null) {
            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.NTAUTH.toString(),
                    useNtAuth));
        }

        return params;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   toUpload           DOCUMENT ME!
     * @param   parent             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void uploadFileToWebDAV(final String fileName,
            final File toUpload,
            final Component parent,
            final ConnectionContext connectionContext) throws Exception {
        uploadFileToWebDAV(fileName, new FileInputStream(toUpload), parent, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   toUpload           DOCUMENT ME!
     * @param   parent             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void uploadFileToWebDAV(final String fileName,
            final InputStream toUpload,
            final Component parent,
            final ConnectionContext connectionContext) throws Exception {
        uploadFileToWebDAV(fileName, toUpload, null, parent, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   toUpload           DOCUMENT ME!
     * @param   webDavDirectory    DOCUMENT ME!
     * @param   parent             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void uploadFileToWebDAV(final String fileName,
            final File toUpload,
            final String webDavDirectory,
            final Component parent,
            final ConnectionContext connectionContext) throws Exception {
        uploadFileToWebDAV(fileName, new FileInputStream(toUpload), webDavDirectory, parent, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   toUpload           DOCUMENT ME!
     * @param   webDavDirectory    DOCUMENT ME!
     * @param   parent             DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void uploadFileToWebDAV(final String fileName,
            final InputStream toUpload,
            final String webDavDirectory,
            final Component parent,
            final ConnectionContext connectionContext) throws Exception {
        final BufferedInputStream bfis = new BufferedInputStream(new ProgressMonitorInputStream(
                    parent,
                    "Bild wird übertragen...",
                    toUpload));
        final byte[] bytes = IOUtils.toByteArray(bfis);
        try {
            final Collection<ServerActionParameter> params = createParams();

            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.PUT.toString(),
                    ((webDavDirectory != null) ? webDavDirectory : "")
                            + encodeURL(fileName)));
            SessionManager.getProxy()
                    .executeTask(
                        actionName,
                        domain,
                        bytes,
                        connectionContext,
                        params.toArray(new ServerActionParameter[0]));
        } finally {
            IOUtils.closeQuietly(bfis);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public boolean deleteFileFromWebDAV(final String fileName, final ConnectionContext connectionContext)
            throws Exception {
        return deleteFileFromWebDAV(fileName, null, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   webDavDirectory    DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public boolean deleteFileFromWebDAV(final String fileName,
            final String webDavDirectory,
            final ConnectionContext connectionContext) throws Exception {
        if ((fileName != null) && (fileName.length() > 0)) {
            final Collection<ServerActionParameter> params = createParams();

            params.add(new ServerActionParameter<>(
                    WebDavTunnelAction.PARAMETER_TYPE.DELETE.toString(),
                    ((webDavDirectory != null) ? webDavDirectory : "")
                            + encodeURL(fileName)));
            SessionManager.getProxy()
                    .executeTask(
                        actionName,
                        domain,
                        (Object)null,
                        connectionContext,
                        params.toArray(new ServerActionParameter[0]));
            return true;
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public InputStream getFileFromWebDAV(final String fileName,
            final ConnectionContext connectionContext) throws Exception {
        return getFileFromWebDAV(fileName, null, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   webDavDirectory    DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public InputStream getFileFromWebDAV(final String fileName,
            final String webDavDirectory,
            final ConnectionContext connectionContext) throws Exception {
        final Collection<ServerActionParameter> params = createParams();

        final String encodedFileName = WebDavTunnelHelper.encodeURL(fileName);
        params.add(new ServerActionParameter<>(
                WebDavTunnelAction.PARAMETER_TYPE.GET.toString(),
                ((webDavDirectory != null) ? webDavDirectory : "")
                        + encodedFileName));
        final byte[] result = (byte[])SessionManager.getProxy()
                    .executeTask(
                            actionName,
                            domain,
                            (Object)null,
                            connectionContext,
                            params.toArray(new ServerActionParameter[0]));
        return new ByteArrayInputStream(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public boolean checkFileOnWebDAV(final String fileName,
            final ConnectionContext connectionContext) throws Exception {
        return checkFileOnWebDAV(fileName, null, connectionContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fileName           DOCUMENT ME!
     * @param   webDavDirectory    DOCUMENT ME!
     * @param   connectionContext  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public boolean checkFileOnWebDAV(final String fileName,
            final String webDavDirectory,
            final ConnectionContext connectionContext) throws Exception {
        final Collection<ServerActionParameter> params = createParams();

        final String encodedFileName = WebDavTunnelHelper.encodeURL(fileName);
        params.add(new ServerActionParameter<>(
                WebDavTunnelAction.PARAMETER_TYPE.HEAD.toString(),
                ((webDavDirectory != null) ? webDavDirectory : "")
                        + encodedFileName));
        final int result = (int)SessionManager.getProxy()
                    .executeTask(
                            actionName,
                            domain,
                            (Object)null,
                            connectionContext,
                            params.toArray(new ServerActionParameter[0]));
        return 200 == result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String encodeURL(final String url) {
        try {
            if (url == null) {
                return null;
            }
            final String[] tokens = url.split("/", -1);
            StringBuilder encodedURL = null;

            for (final String tmp : tokens) {
                if (encodedURL == null) {
                    encodedURL = new StringBuilder(URLEncoder.encode(tmp, "UTF-8"));
                } else {
                    encodedURL.append("/").append(URLEncoder.encode(tmp, "UTF-8"));
                }
            }

            if (encodedURL != null) {
                // replace all + with %20 because the method URLEncoder.encode() replaces all spaces with '+', but
                // the web dav client interprets %20 as a space.
                return encodedURL.toString().replaceAll("\\+", "%20");
            } else {
                return "";
            }
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Unsupported encoding.", e);
        }
        return url;
    }
}
