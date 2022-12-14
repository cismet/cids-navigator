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
package de.cismet.cids.client.tools;

import java.util.prefs.Preferences;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;
import de.cismet.netutil.ProxyProperties;
import de.cismet.netutil.ProxyPropertiesHandler;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(
    service = ProxyPropertiesHandler.class,
    position = 1000
)
public class ClientProxyPropertiesHandler implements ProxyPropertiesHandler {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROXY_MODE = "proxy.mode";                   // NOI18N
    public static final String PROXY_ENABLED = "proxy.enabled";             // NOI18N
    public static final String PROXY_HOST = "proxy.host";                   // NOI18N
    public static final String PROXY_PORT = "proxy.port";                   // NOI18N
    public static final String PROXY_USERNAME = "proxy.username";           // NOI18N
    public static final String PROXY_PASSWORD = "proxy.password";           // NOI18N
    public static final String PROXY_DOMAIN = "proxy.domain";               // NOI18N
    public static final String PROXY_EXCLUDEDHOSTS = "proxy.excludedHosts"; // NOI18N

    //~ Methods ----------------------------------------------------------------

    @Override
    public ProxyProperties loadProperties() {
        final Preferences preferences = Preferences.userNodeForPackage(Proxy.class);
        final String mode = preferences.get(PROXY_MODE, null);
        final String enabled = preferences.get(PROXY_ENABLED, null);
        final String host = preferences.get(PROXY_HOST, null);
        final String port = preferences.get(PROXY_PORT, null);
        final String username = preferences.get(PROXY_USERNAME, null);
        final String password = preferences.get(PROXY_PASSWORD, null);
        final String domain = preferences.get(PROXY_DOMAIN, null);
        final String excludedHosts = preferences.get(PROXY_EXCLUDEDHOSTS, null);

        final ProxyProperties properties = new ProxyProperties();
        if (mode != null) {
            properties.setProxyMode(ProxyHandler.Mode.valueOf(mode));
        }
        if (enabled != null) {
            properties.setProxyEnabled(Boolean.parseBoolean(enabled));
        }
        if (host != null) {
            properties.setProxyHost(host);
        }
        if (port != null) {
            properties.setProxyPort(Integer.parseInt(port));
        }
        if (username != null) {
            properties.setProxyUsername(username);
        }
        if (password != null) {
            properties.setProxyPassword(password);
        }
        if (domain != null) {
            properties.setProxyDomain(domain);
        }
        if (excludedHosts != null) {
            properties.setProxyExcludedHosts(excludedHosts);
        }
        return properties;
    }

    @Override
    public void saveProperties(final ProxyProperties properties) throws Exception {
        final ProxyHandler.Mode mode = properties.getProxyMode();
        final Boolean enabled = properties.getProxyEnabled();
        final String host = properties.getProxyHost();
        final Integer port = properties.getProxyPort();
        final String username = properties.getProxyUsername();
        final String password = properties.getProxyPassword();
        final String domain = properties.getProxyDomain();
        final String excludedHosts = properties.getProxyExcludedHosts();

        final Preferences preferences = Preferences.userNodeForPackage(Proxy.class);
        if (mode == null) {
            preferences.remove(PROXY_MODE);
        } else {
            preferences.put(PROXY_MODE, mode.name());
        }
        if (enabled == null) {
            preferences.remove(PROXY_ENABLED);
        } else {
            preferences.put(PROXY_ENABLED, Boolean.toString(enabled));
        }
        if (host == null) {
            preferences.remove((PROXY_HOST));
        } else {
            preferences.put((PROXY_HOST), host);
        }
        if (port == null) {
            preferences.remove(PROXY_PORT);
        } else {
            preferences.put(PROXY_PORT, Integer.toString(port));
        }
        if (username == null) {
            preferences.remove(PROXY_USERNAME);
        } else {
            preferences.put(PROXY_USERNAME, username);
        }
        if (password == null) {
            preferences.remove(PROXY_PASSWORD);
        } else {
            preferences.put(PROXY_PASSWORD, password);
        }
        if (domain == null) {
            preferences.remove(PROXY_DOMAIN);
        } else {
            preferences.put(PROXY_DOMAIN, domain);
        }
        if (excludedHosts == null) {
            preferences.remove(PROXY_EXCLUDEDHOSTS);
        } else {
            preferences.put(PROXY_EXCLUDEDHOSTS, excludedHosts);
        }
        preferences.flush();
    }
}
