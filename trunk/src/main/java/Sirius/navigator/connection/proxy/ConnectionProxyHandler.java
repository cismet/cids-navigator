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

import org.apache.log4j.Logger;

import java.lang.reflect.*;

/**
 * A proxy interface for extending a <code>Connection</code> implementation, e.g. to add caching support.
 *
 * @author   Pascal
 * @version  1.0 12/22/2002
 */
public abstract class ConnectionProxyHandler implements InvocationHandler {

    //~ Static fields/initializers ---------------------------------------------

    // log4j
    protected static final Logger logger = Logger.getLogger(ConnectionProxyHandler.class);

    //~ Instance fields --------------------------------------------------------

    protected final Connection connection;
    protected final ConnectionSession session;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ConnectionProxy.
     *
     * @param  session  DOCUMENT ME!
     */
    public ConnectionProxyHandler(final ConnectionSession session) {
        this.session = session;
        this.connection = session.getConnection();
    }

    /**
     * implement this method, to extend default behaviour.
     */
    // public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
