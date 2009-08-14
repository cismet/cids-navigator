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

import java.rmi.*;

import org.apache.log4j.*;

import Sirius.server.newuser.*;
import Sirius.server.newuser.permission.*;
import Sirius.navigator.exception.ConnectionException;



/**
 * Stores a <code>Connection</code> and a <code>User</code> Object
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public class ConnectionSession 
{
    private final static Logger logger = Logger.getLogger(ConnectionSession.class);
    
    private final Connection connection;
    private final ConnectionInfo connectionInfo;
   // private final Permission writePermission;
    
    private boolean loggedin = false; 
    private User user;
    
    
    
    protected ConnectionSession(Connection connection, ConnectionInfo connectionInfo, boolean autoLogin) throws ConnectionException, UserException
    {
        logger.debug("creating new connection session");
        
        this.connection = connection;
        this.connectionInfo = connectionInfo;
      //  this.writePermission = new Permission(PermissionHolder.WRITE, "write", "accessExplicit");
        
        if(autoLogin)
        {
            loggedin = login();
        }
    }
    
    /** Creates a new instance of ConnectionSession */
    protected ConnectionSession(Connection connection, ConnectionInfo connectionInfo) throws ConnectionException, UserException
    {
        this(connection, connectionInfo, true);
    }
    
    /** Creates a new instance of ConnectionSession */
    protected ConnectionSession(Connection connection) throws ConnectionException, UserException
    {
        this(connection, new ConnectionInfo(), false); 
    }
    
    public Connection getConnection()
    {
        return this.connection;
    }
    
    public User getUser()
    { 
        return this.user;
    }
    
//    public Permission getWritePermission()
//    {
//        return this.writePermission;
//    }
    
    public void login(String usergroupDomain, String usergroup, String userDomain, String username, String password) throws ConnectionException, UserException
    {
        if(loggedin && user != null && connectionInfo.getUsergroupDomain().equals(usergroupDomain) && connectionInfo.getUsergroup().equals(usergroup) && connectionInfo.getUserDomain().equals(userDomain) && connectionInfo.getUsername().equals(username) && connectionInfo.getPassword().equals(password))
        {
            logger.warn("can't perform login: this user '" + connectionInfo.getUsername() + "' is already logged in");
        }
        else
        {
            if(loggedin && user != null)
            {
                logger.info("logging out user '" + connectionInfo.getUsername() + "'");
            }
            
            connectionInfo.setUsername(username);
            connectionInfo.setPassword(password);
            connectionInfo.setUsergroup(usergroup);
            connectionInfo.setUserDomain(userDomain);
            connectionInfo.setUsergroupDomain(usergroupDomain);
            
            loggedin = login();
        }  
    }
    
    private boolean login() throws ConnectionException, UserException
    {
        if(!connection.isConnected())
        {
            logger.error("can't login: no connection established");
            throw new ConnectionException("can't login: no connection established", ConnectionException.ERROR);
        }

        try
	{
            logger.debug("logging in user '" +  connectionInfo.getUsergroupDomain() + "' '" + connectionInfo.getUsergroup() + "' '" + connectionInfo.getUserDomain() + "' '" + connectionInfo.getUsername() + "' '" /*+ connectionInfo.getPassword() + "'"*/);
            this.user = connection.getUser(connectionInfo.getUsergroupDomain(), connectionInfo.getUsergroup(), connectionInfo.getUserDomain(), connectionInfo.getUsername(), connectionInfo.getPassword());
	}
	catch (UserException ue)
	{
            logger.warn("can't login: wrong user informations", ue);
            throw ue;
	}
	catch(ConnectionException ce)
	{
            logger.fatal("[ServerError] can't login");
            //throw new ConnectionException("[ServerError] can't login", ConnectionException.FATAL, re);
            throw ce;
        }

        return true;
    }
    
    public void logout()
    {
        this.loggedin = false;
        this.user = null;
    }
    
    public boolean isLoggedin()
    {
        return this.loggedin;
    }
    
    public boolean isConnected()
    {
        try
        {
            return this.connection.isConnected();
        }
        catch(Exception ex)
        {
            logger.fatal("An unexpected exception occoured in method 'Connection.isConnected()'", ex);
            return false;
        }
    }
    
    public ConnectionInfo getConnectionInfo()
    {
        return this.connectionInfo;
    }
}
