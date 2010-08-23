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

import java.beans.*;

/**
 * Stores all informations to create a connection to remote server.
 *
 * @version 1.0 12/22/2002
 * @author Pascal
 */
public class ConnectionInfo extends Object implements java.io.Serializable 
{   
    private final javax.swing.event.SwingPropertyChangeSupport propertySupport;
        
    /** Holds value of property username. */
    private String username;
    
    /** Holds value of property password. */
    private String password;
    
    /** Holds value of property usergroup. */
    private String usergroup;
    
    /** Holds value of property userDomain. */
    private String userDomain;
    
    /** Holds value of property usergroupDomain. */
    private String usergroupDomain;
    
    /** Holds value of property callserverURL. */
    private String callserverURL;
    
    /** Creates new ConnectionInfo */
    public ConnectionInfo() 
    {
        propertySupport = new javax.swing.event.SwingPropertyChangeSupport( this );
    }

    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.addPropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.removePropertyChangeListener(l);
    }
    
    /** Getter for property username.
     * @return Value of property username.
     *
     */
    public String getUsername() {
        return this.username;
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     *
     */
    public void setUsername(String username) {
        String oldUsername = this.username;
        this.username = username;
        propertySupport.firePropertyChange("username", oldUsername, username);  // NOI18N
    }
    
    /** Getter for property password.
     * @return Value of property password.
     *
     */
    public String getPassword() {
        return this.password;
    }
    
    /** Setter for property password.
     * @param password New value of property password.
     *
     */
    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        propertySupport.firePropertyChange("password", oldPassword, password);  // NOI18N
    }
    
    /** Getter for property usergroup.
     * @return Value of property usergroup.
     *
     */
    public String getUsergroup() {
        return this.usergroup;
    }
    
    /** Setter for property usergroup.
     * @param usergroup New value of property usergroup.
     *
     */
    public void setUsergroup(String usergroup) {
        String oldUsergroup = this.usergroup;
        this.usergroup = usergroup;
        propertySupport.firePropertyChange("usergroup", oldUsergroup, usergroup);  // NOI18N
    }
    
    /** Getter for property localserver.
     * @return Value of property localserver.
     *
     */
    public String getUserDomain() {
        return this.userDomain;
    }
    
    /** Setter for property localserver.
     * @param localserver New value of property localserver.
     *
     */
    public void setUserDomain(String userDomain) {
        String oldUserDomain = this.userDomain;
        this.userDomain = userDomain;
        propertySupport.firePropertyChange("userDomain", oldUserDomain, userDomain);  // NOI18N
    }
    
    /** Getter for property usergroupDomain.
     * @return Value of property usergroupDomain.
     *
     */
    public String getUsergroupDomain() {
        return this.usergroupDomain;
    }
    
    /** Setter for property usergroupDomain.
     * @param usergroupDomain New value of property usergroupDomain.
     *
     */
    public void setUsergroupDomain(String usergroupDomain) {
        String oldUsergroupDomain = this.usergroupDomain;
        this.usergroupDomain = usergroupDomain;
        propertySupport.firePropertyChange("usergroupDomain", oldUsergroupDomain, usergroupDomain);  // NOI18N
    }
    
    /** Getter for property callserverURL.
     * @return Value of property callserverURL.
     *
     */
    public String getCallserverURL() {
        return this.callserverURL;
    }
    
    /** Setter for property callserverURL.
     * @param callserverURL New value of property callserverURL.
     *
     */
    public void setCallserverURL(String callserverURL) {
        String oldCallserverURL = this.callserverURL;
        this.callserverURL = callserverURL;
        propertySupport.firePropertyChange("callserverURL", oldCallserverURL, callserverURL);  // NOI18N
    }
    
}
