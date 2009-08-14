package Sirius.navigator.connection.proxy;

/*******************************************************************************

 	Copyright (c)	:	EIG (Environmental Informatics Group)
				http://www.enviromatics.net
				Prof. Dr. Reiner Guettler
				Prof. Dr. Ralf Denzer

				HTW
				University of Applied Sciences
				Goebenstr. 40
 				66117 Saarbruecken, Germany

	Programmers	:	Pascal <pascal@enviromatics.net>

 	Project		:	Sirius
	Version		:	1.0
 	Purpose		:
	Created		:	12/20/2002
	History		:

*******************************************************************************/

import java.lang.reflect.*;

import org.apache.log4j.Logger;

import Sirius.navigator.connection.*;

/**
 * A proxy interface for extending a <code>Connection</code> implementation, 
 * e.g. to add caching support.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public abstract class ConnectionProxyHandler implements InvocationHandler
{
    // log4j
    protected final static Logger logger = Logger.getLogger(ConnectionProxyHandler.class);
    
    protected final Connection connection;
    protected final ConnectionSession session;
    
    /** Creates a new instance of ConnectionProxy */
    public ConnectionProxyHandler(ConnectionSession session) 
    {
        this.session = session;
        this.connection = session.getConnection(); 
    }
           
    /**
     * implement this method, to extend default behaviour.
     */
    //public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
