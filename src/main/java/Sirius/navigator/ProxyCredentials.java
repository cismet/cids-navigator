/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator;

import Sirius.navigator.connection.SessionManager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.ProxyHandler;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
public class ProxyCredentials {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(ProxyCredentials.class);

    //~ Instance fields --------------------------------------------------------

    private final Entry[] credentials;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProxyCredentials object.
     */
    public ProxyCredentials() {
        this(null);
    }

    /**
     * Creates a new ProxyCredentials object.
     *
     * @param  credentials  DOCUMENT ME!
     */
    public ProxyCredentials(@JsonProperty("credentials") final Entry[] credentials) {
        this.credentials = credentials;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  confAttr           DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public static void initFromConfAttr(final String confAttr, final ConnectionContext connectionContext) {
        try {
            final String credentialsJson = SessionManager.getProxy()
                        .getConfigAttr(SessionManager.getSession().getUser(), confAttr, connectionContext);
            final ProxyCredentials proxyCredentials =
                new ObjectMapper().readValue(credentialsJson, ProxyCredentials.class);

            final ProxyCredentials.Entry[] credentials = (proxyCredentials != null) ? proxyCredentials.getCredentials()
                                                                                    : null;
            if (credentials != null) {
                for (final ProxyCredentials.Entry entry : credentials) {
                    if (entry != null) {
                        try {
                            final String[] hostAndPort = entry.getHost().split(":");
                            if (hostAndPort.length == 2) {
                                final String host = hostAndPort[0];
                                final int port = Integer.valueOf(hostAndPort[1]);
                                final String username = entry.getUsername();
                                final String password = entry.getPassword();
                                final String domain = entry.getDomain();
                                ProxyHandler.getInstance().addHostCredentials(host, port, username, password, domain);
                            }
                        } catch (final Exception ex) {
                            LOG.warn(String.format("not adding credentials entry: %s", entry.toString()), ex);
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            LOG.warn("ProxyCredentials couldn't be initialized", ex);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    @Setter
    public static class Entry {

        //~ Instance fields ----------------------------------------------------

        private final String host;
        private final String username;
        private final String password;
        private final String domain;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Entry object.
         */
        public Entry() {
            this(null, null, null, null);
        }

        /**
         * Creates a new Entry object.
         *
         * @param  host      DOCUMENT ME!
         * @param  username  DOCUMENT ME!
         * @param  password  DOCUMENT ME!
         * @param  domain    DOCUMENT ME!
         */
        public Entry(@JsonProperty("host") final String host,
                @JsonProperty("username") final String username,
                @JsonProperty("password") final String password,
                @JsonProperty("domain") final String domain) {
            this.host = host;
            this.username = username;
            this.password = password;
            this.domain = domain;
        }
    }
}
