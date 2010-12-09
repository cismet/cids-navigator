/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection.proxy;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                http://www.enviromatics.net
                                Prof. Dr. Reiner Guettler
                                Prof. Dr. Ralf Denzer

                                HTW
                                University of Applied Sciences
                                Goebenstr. 40
                                66117 Saarbruecken, Germany

        Programmers     :       Pascal <pascal@enviromatics.net>

        Project         :       Sirius
        Version         :       1.0
        Purpose         :
        Created         :       12/20/2002
        History         :

*******************************************************************************/

import Sirius.navigator.connection.*;

import Sirius.server.middleware.types.*;

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
