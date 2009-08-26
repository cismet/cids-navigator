package Sirius.navigator.connection;

import org.apache.log4j.*;

import Sirius.navigator.connection.proxy.*;

import de.cismet.tools.CurrentStackTrace;

public final class SessionManager {

    private final static Logger logger = Logger.getLogger(SessionManager.class);
    private static SessionManager manager = null;
    private ConnectionProxy proxy;
    private static double nonce = Math.random();

    private SessionManager(ConnectionProxy proxy) {
        this.proxy = proxy;
        logger.info("singleton shared instance of SessionManager created");
    }
    private final static Object blocker = new Object();

    public final static void init(ConnectionProxy proxy) {
        logger.debug("init SessionManager " + nonce, new CurrentStackTrace());
        synchronized (blocker) {
            if (manager == null) {
                manager = new SessionManager(proxy);

            } else {
                manager.proxy = proxy;
                logger.warn("SessionManager has already been initialized");
                //throw new RuntimeException("SessionManager has alreadyt been initialized");
            }
        }
    }

    public final static void destroy() {
        logger.info("destroy SessionManager" + manager, new CurrentStackTrace());
        synchronized (blocker) {
            logger.warn("destroying singelton SessionManager instance");
//            manager = null;
        }
    }

    public final static boolean isConnected() {
        if (manager == null) {
            return false;
        } else {
            return getManager().proxy.isConnected();
        }
    }

    public final static boolean onClientSide() {
        return isConnected();
    }

    private final static SessionManager getManager() {
        if (manager == null) {
            Throwable t = new CurrentStackTrace();
            logger.warn("SessionManager has not been initialized", t);
            throw new RuntimeException("SessionManager has not been initialized", t);
        }

        return manager;
    }

    public final static Connection getConnection() {
        try {
            return getManager().proxy.getSession().getConnection();
        } catch (Exception e) {
            logger.error("Error in getConnection()\nmaybe the manager is null" + getManager(), e);
        }
        return null;
    }

    public final static ConnectionSession getSession() {
        return getManager().proxy.getSession();
    }

    public final static ConnectionProxy getProxy() {
        return getManager().proxy;
    }
}
