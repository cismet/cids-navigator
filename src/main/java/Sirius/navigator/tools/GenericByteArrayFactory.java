/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 *
 * ... and it just works.
 *
 ***************************************************
 */
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.RESTfulConnection;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.openide.util.Exceptions;

import java.util.Base64;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.AbstractConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.netutil.ProxyHandler;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class GenericByteArrayFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            GenericByteArrayFactory.class);
    private static final String CONNECTIONPROXYHANDLER_CLASS =
        "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler";

    private static final Option OPTION__LOGGER = new Option("l", "logger", false, "Logger");
    private static final Option OPTION__CALLSERVER_URL = new Option("c", "callserver-url", true, "Callserver");
    private static final Option OPTION__USER = new Option("u", "user", true, "User");
    private static final Option OPTION__GROUP = new Option("g", "group", true, "Group");
    private static final Option OPTION__DOMAIN = new Option("d", "domain", true, "Domain");
    private static final Option OPTION__PASSWORD = new Option("p", "password", true, "Password");
    private static final Option OPTION__JWT = new Option("j", "jwt", true, "JSON Web Token");
    private static final Option OPTION__GENERATOR = new Option("C", "class", true, "Generator Class name");
    private static final Option OPTION__CONFIGURATION = new Option(
            "P",
            "parameters",
            true,
            "JSON Configuration for generator");

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final CommandLine cmd;
        try {
            cmd =
                new DefaultParser().parse(new Options().addOption(OPTION__CALLSERVER_URL).addOption(OPTION__LOGGER)
                            .addOption(OPTION__USER).addOption(OPTION__PASSWORD).addOption(OPTION__GROUP).addOption(
                        OPTION__DOMAIN).addOption(OPTION__JWT).addOption(
                        OPTION__GENERATOR).addOption(OPTION__CONFIGURATION),
                    args);
        } catch (final ParseException ex) {
            Exceptions.printStackTrace(ex);
            System.exit(1);
            throw new IllegalStateException();
        }

        final boolean loggerEnabled = cmd.hasOption(OPTION__LOGGER.getOpt());
        if (loggerEnabled) {
            Log4JQuickConfig.configure4LumbermillOnLocalhost();
        } else {
            Log4JQuickConfig.configure4LumbermillOnLocalhost("OFF");
        }

        final String callserverUrl = cmd.hasOption(OPTION__CALLSERVER_URL.getOpt())
            ? cmd.getOptionValue(OPTION__CALLSERVER_URL.getOpt()) : null;
        final String login = cmd.hasOption(OPTION__USER.getOpt()) ? cmd.getOptionValue(OPTION__USER.getOpt()) : null;
        final String group = cmd.hasOption(OPTION__GROUP.getOpt()) ? cmd.getOptionValue(OPTION__GROUP.getOpt()) : null;
        final String domain = cmd.hasOption(OPTION__DOMAIN.getOpt()) ? cmd.getOptionValue(OPTION__DOMAIN.getOpt())
                                                                     : null;
        final String password = cmd.hasOption(OPTION__PASSWORD.getOpt()) ? cmd.getOptionValue(OPTION__PASSWORD.getOpt())
                                                                         : null;
        final String jwt = cmd.hasOption(OPTION__JWT.getOpt()) ? cmd.getOptionValue(OPTION__JWT.getOpt()) : null;
        final String configuration = cmd.hasOption(OPTION__CONFIGURATION.getOpt())
            ? cmd.getOptionValue(OPTION__CONFIGURATION.getOpt()) : null;
        final String generator = cmd.hasOption(OPTION__GENERATOR.getOpt())
            ? cmd.getOptionValue(OPTION__GENERATOR.getOpt()) : null;

        try {
            final ConnectionContext connectionContext = ConnectionContext.create(
                    AbstractConnectionContext.Category.OTHER,
                    GenericByteArrayFactory.class.getCanonicalName());

            if (jwt != null) {
                initSessionManager(
                    callserverUrl,
                    domain,
                    null,
                    "jwt",
                    jwt,
                    true,
                    connectionContext);
            } else {
                initSessionManager(
                    callserverUrl,
                    domain,
                    group,
                    login,
                    password,
                    true,
                    connectionContext);
            }

            final Class generatorClass = Class.forName(generator);
            if (ByteArrayFactory.class.isAssignableFrom(generatorClass)) {
                final ByteArrayFactory generatorInstance = (ByteArrayFactory)generatorClass.newInstance();
                final byte[] bytes = generatorInstance.create(configuration);
                System.out.println(Base64.getEncoder().encodeToString(bytes));
                System.exit(0);
            } else {
                throw new Exception("class not instance of " + ByteArrayFactory.class.getSimpleName());
            }
        } catch (final Throwable t) {
            Exceptions.printStackTrace(t);
            LOG.error(t, t);
            System.exit(1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   callserverUrl       DOCUMENT ME!
     * @param   domain              DOCUMENT ME!
     * @param   group               DOCUMENT ME!
     * @param   user                DOCUMENT ME!
     * @param   pass                DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     * @param   connectionContext   DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private static void initSessionManager(final String callserverUrl,
            final String domain,
            final String group,
            final String user,
            final String pass,
            final boolean compressionEnabled,
            final ConnectionContext connectionContext) throws Exception {
        final ConnectionInfo info = new ConnectionInfo();
        info.setCallserverURL(callserverUrl);
        info.setUsername(user);
        info.setUsergroup(group);
        info.setPassword(pass);
        info.setUserDomain(domain);
        info.setUsergroupDomain(domain);

        final Sirius.navigator.connection.Connection connection = ConnectionFactory.getFactory()
                    .createConnection(
                        RESTfulConnection.class.getCanonicalName(),
                        info.getCallserverURL(),
                        GenericByteArrayFactory.class.getSimpleName(),
                        ProxyHandler.getInstance().getProxy(),
                        compressionEnabled,
                        connectionContext);
        final ConnectionSession session = ConnectionFactory.getFactory()
                    .createSession(connection, info, true, connectionContext);
        final ConnectionProxy conProxy = ConnectionFactory.getFactory()
                    .createProxy(
                        CONNECTIONPROXYHANDLER_CLASS,
                        session,
                        connectionContext);
        SessionManager.init(conProxy);

        ClassCacheMultiple.setInstance(domain, connectionContext);
    }
}
