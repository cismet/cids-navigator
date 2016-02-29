/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection.proxy;

import Sirius.navigator.connection.Connection;

/**
 * An interfaces, that defines methods, that should be implemented by the <code>ConnectionProxyHandler.</code>
 *
 * <p>Thre can be only one connection per application, but with this interface it is possible to assing session specific
 * features (user caching, etc.) to a connetion via a proxy class. The proxy class is stored in the user's <code>
 * ConnectionSession</code>.</p>
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public interface ConnectionProxy extends Connection, ProxyInterface {

    // public Node[] p_getChildren(int nodeID, String localserver) throws ConnectionException;
}
