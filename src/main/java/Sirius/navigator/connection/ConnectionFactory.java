package Sirius.navigator.connection;

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

import java.io.*;
import java.rmi.*;
import java.net.*;
import java.lang.reflect.*;

import org.apache.log4j.*;

import Sirius.navigator.connection.proxy.*;
import Sirius.server.Server;
import Sirius.server.newuser.*;
import Sirius.server.middleware.interfaces.proxy.*;
import Sirius.navigator.exception.ConnectionException;

/**
 * A singleton factory class that creates and manages connections.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public class ConnectionFactory 
{  
    // log4j
    private final static Logger logger = Logger.getLogger(ConnectionFactory.class);
    
    // singleton shared instance
    private final static ConnectionFactory factory = new ConnectionFactory();
    
    //private java.lang.Object callserver = null;
    
    //private Connection connection = null;
   
    
    /** Creates a new instance of ConnectionManager */
    private ConnectionFactory() 
    {
       logger.debug("creating singleton shared ConnectionManager instance");
    }
    
    public final static ConnectionFactory getFactory()
    {
        return factory;
    }
    
    
    /*public void createConnection(String callserverURL, String connectionClassName, String connectionProxyClassName) throws ConnectionException
    {
        createConnection(connectionClassName, connectionProxyClassName);
        connection.connect(callserverURL); 
    }
    
    public void createConnection(ConnectionInfo connectionInfo, String connectionClassName, String connectionProxyClassName) throws ConnectionException, UserException
    {
        createConnection(connectionClassName, connectionProxyClassName);
        connection.connect(connectionInfo); 
    }
    
    public createSession()
    {
        
    }*/
    
    
    /**
     * Creates ans initializes a new shared connection instance
     */
   /* public void createConnection(Class connectionClass, Class connectionProxyClass) throws ConnectionException
    {
        Connection connection = null;
      
        try
        {
            logger.debug("creating connection class instance '" + connectionClass.getName() + "'");
            connection = (Connection)connectionClass.newInstance();
        }
        catch(Exception e)
        {
            logger.fatal("could not instantiate connection class '" + connectionClass.getName() + "'", e);
            throw new ConnectionException("could not connection proxy class '" + connectionClass.getName() + "'", e);
        }
        
        if(connectionProxyClass != null)
        {
            try
            {
                logger.debug("creating connection proxy class instance '" + connectionProxyClass.getName() + "'");
                ConnectionProxy connectionProxy = (ConnectionProxy)connectionProxyClass.getConstructor(new Class[] { Connection.class }).newInstance(new Object[] { connection });
                this.connection = (Connection)Proxy.newProxyInstance(connection.getClass().getClassLoader(), new Class[] { Connection.class }, connectionProxy);
            }
            catch(Exception e)
            {
                logger.fatal("could not instantiate connection proxy class '" + connectionProxyClass.getName() + "'", e);
                throw new ConnectionException("could not instantiate connection proxy class '" + connectionProxyClass.getName() + "'", e);
            }
        }
    }*/
    
    /**
     * Creates ans initializes a new shared connection instance
     */
    public Connection createConnection(String connectionClassName, String callserverURL) throws ConnectionException
    {
        Connection connection = createConnection(connectionClassName);
        connection.connect(callserverURL);
        return connection;
    }
    
    public Connection createConnection(String connectionClassName, String callserverURL, String username, String password) throws ConnectionException
    {
        Connection connection = createConnection(connectionClassName);
        connection.connect(callserverURL, username, password);
        return connection;
    }
    
    private Connection createConnection(String connectionClassName) throws ConnectionException
    {
        //Class connectionClass = null;
        //Connection connection = null;
        
        try
        {
            logger.debug("creating connection class instance '" + connectionClassName + "'");
            //connectionClass = Class.forName(connectionClassName);   
            //connection = (Connection)connectionClass.newInstance();
            //return connection;
            
            return (Connection)Class.forName(connectionClassName).newInstance();   
        }
        catch(ClassNotFoundException cne)
        {
            logger.fatal("connection class '" + connectionClassName + "' not found", cne);
            throw new ConnectionException("connection class '" + connectionClassName + "' not found", cne);
        }
        catch(InstantiationException ie)
        {
            logger.fatal("could not instantiate connection class '" + connectionClassName + "'", ie);
            throw new ConnectionException("could not connection proxy class '" + connectionClassName + "'", ie);
        }
        catch(IllegalAccessException iae)
        {
            logger.fatal("could not instantiate connection class '" + connectionClassName + "'", iae);
            throw new ConnectionException("could not connection proxy class '" + connectionClassName + "'", iae);
        }
    }
    
     public ConnectionSession createSession(Connection connection) throws ConnectionException
     {
        try
        {  
            return new ConnectionSession(connection);
        }
        catch(UserException ue)
        {
            logger.fatal("unexpected Exception");
            throw new RuntimeException("unexpected Exception", ue);
        }
     }
    
    public ConnectionSession createSession(Connection connection, ConnectionInfo connectionInfo) throws ConnectionException, UserException
    {
        return new ConnectionSession(connection, connectionInfo);
    }
    
    public ConnectionSession createSession(Connection connection, ConnectionInfo connectionInfo, boolean autoLogin) throws ConnectionException, UserException
    {
        return new ConnectionSession(connection, connectionInfo, autoLogin);
    }
    
    
    public ConnectionSession createSession(Connection connection, String usergroupDomain, String usergroup, String userDomain, String username, String password)  throws ConnectionException, UserException
    {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUsername(username);
        connectionInfo.setPassword(password);
        connectionInfo.setUsergroup(usergroup);
        connectionInfo.setUserDomain(userDomain);
        connectionInfo.setUsergroupDomain(usergroupDomain);
        
        return new ConnectionSession(connection, connectionInfo); 
    }
    
    public ConnectionProxy createProxy(String connectionProxyHandlerClassName, ConnectionSession connectionSession) throws ConnectionException
    {
        ConnectionProxyHandler connectionProxyHandler;
        
        try
        {
            logger.debug("creating connection proxy handler instance '" + connectionProxyHandlerClassName + "'");
            connectionProxyHandler = (ConnectionProxyHandler)Class.forName(connectionProxyHandlerClassName).getConstructor(new Class[] {ConnectionSession.class}).newInstance(new Object[] {connectionSession});  
        }
        catch(ClassNotFoundException cne)
        {
            logger.fatal("connection proxy handler class '" + connectionProxyHandlerClassName + "' not found", cne);
            throw new ConnectionException("connection proxy handler class '" + connectionProxyHandlerClassName + "' not found", cne);
        }
        catch(Exception e)
        {
            logger.fatal("could not instantiate connection proxy handler class '" + connectionProxyHandlerClassName + "'", e);
            throw new ConnectionException("could not connection proxy handler class '" + connectionProxyHandlerClassName + "'", e);
        }
        /*catch(InstantiationException ie)
        {
            logger.fatal("could not instantiate connection proxy handler class '" + connectionProxyHandlerClassName + "'", ie);
            throw new ConnectionException("could not connection proxy handler class '" + connectionProxyHandlerClassName + "'", ie);
        }
        catch(NoSuchMethodException nme)
        {
            logger.fatal("could not instantiate connection proxy handler class '" + connectionProxyHandlerClassName + "', constructor not found", nme);
            throw new ConnectionException("could not connection proxy handler proxy class '" + connectionProxyHandlerClassName + "', constructor not found", nme);
        }
        catch(IllegalAccessException iae)
        {
            logger.fatal("could not instantiate connection proxy handler class '" + connectionProxyHandlerClassName + "'", iae);
            throw new ConnectionException("could not connection proxy handler class '" + connectionProxyHandlerClassName + "'", iae);
        }*/
        
        
        try
        {
            logger.debug("creating the connection proxy");
            return (ConnectionProxy)java.lang.reflect.Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[] { ConnectionProxy.class }, connectionProxyHandler);
            //return (ConnectionProxy)Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), ConnectionProxy.class.getInterfaces(), connectionProxyHandler);
            
            //logger.debug("connectionProxyHandler: " + connectionProxyHandler);
            //Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { ConnectionProxy.class }, connectionProxyHandler);
            //logger.debug("connectionProxy: " + proxy);
            //return (ConnectionProxy)proxy;         
        }
        catch(Exception e)
        {
            logger.fatal("could not create connection proxy", e);
            throw new ConnectionException("could not create connection proxy", e);
        }
    }
    
    public ConnectionProxy createProxy(String connectionClassName, String connectionProxyHandlerClassName, ConnectionInfo connectionInfo, boolean autoLogin) throws ConnectionException, UserException
    {
        Connection connection = createConnection(connectionClassName, connectionInfo.getCallserverURL());
        ConnectionSession connectionSession = createSession(connection, connectionInfo, autoLogin);
        
        return createProxy(connectionProxyHandlerClassName, connectionSession);
    }
    
    public ConnectionProxy createProxy(String connectionClassName, String connectionProxyHandlerClassName, ConnectionInfo connectionInfo) throws ConnectionException, UserException
    {
        return createProxy(connectionClassName, connectionProxyHandlerClassName, connectionInfo, true);
    }
        
       
    
    /*private void createConnection(String connectionClassName, String connectionProxyClassName) throws ConnectionException
    {
        Class connectionClass = null;
        Class connectionProxyClass = null;
        
        try
        { 
            connectionClass = Class.forName(connectionClassName);    
        }
        catch(Exception e)
        {
            logger.fatal("connection class '" + connectionClassName + "' not found", e);
            throw new ConnectionException("connection class '" + connectionClassName + "' not found", e);
        }
        
        if(connectionProxyClassName != null)
        {
            try
            { 
                connectionProxyClass = Class.forName(connectionProxyClassName);    
            }
            catch(Exception e)
            {
                logger.fatal("connection class '" + connectionProxyClassName + "' not found", e);
                throw new ConnectionException("connection class '" + connectionProxyClassName + "' not found", e);
            }
        }
        
        createConnection(connectionClass, connectionProxyClass);
    }

    
    
    public Connection getConnection()
    {
        return this.connection;
    }
    
    /**
     * @return the singleton shared instance
     */
    /*public static ConnectionManager getInstance()
    {
        synchronized(logger)
        {
            if(instance == null)
            {
                instance = new ConnectionManager();
            }
        }
        
        return instance;
    }*/
    
    /*public static void main(String args[])
    {
        Logger.getRootLogger().addAppender(new ConsoleAppender(new TTCCLayout() ));
        
        ConnectionFactory connectionManager = new ConnectionFactory();
        
        try
        {
            logger.debug("########################################################################");
            Connection connection = connectionManager.createConnection("Sirius.navigator.connection.RMIConnection", "rmi://192.168.1.2/callServer");
            logger.debug("########################################################################");
            //usergroupDomain, usergroup, userDomain, username, password
            ConnectionSession connectionSession = connectionManager.createSession(connection, "SYSTEM", "ADMINISTRATOREN", "SYSTEM", "admin", "yxc");
            logger.debug("########################################################################");
            ConnectionProxy connectionProxy = connectionManager.createProxy("Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler", connectionSession);
            logger.debug("########################################################################");
            
            logger.debug("connectionProxy.isConnected(): " + connectionProxy.isConnected());
            //connectionProxy.setProperty("xxx", "yyy");
            
        }
        catch(Exception e)
        {
            System.out.println("------------------------------------------------");
            e.printStackTrace();
        }
    }*/
}
