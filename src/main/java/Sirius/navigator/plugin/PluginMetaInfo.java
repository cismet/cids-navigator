package Sirius.navigator.plugin;

import java.beans.*;

/**
 *
 * @author  pascal
 */
public class PluginMetaInfo extends Object implements java.io.Serializable
{
    
    /** Holds value of property author. */
    private String author;
    
    /** Holds value of property company. */
    private String company;
    
    /** Holds value of property contact. */
    private String contact;
    
    /** Holds value of property description. */
    private String description;
    
    /** Holds value of property homepage. */
    private String homepage;
    
    /** Holds value of property displayname. */
    private String name;
    
    /** Holds value of property version. */
    private String version;
    
    /** Holds value of property copyright. */
    private String copyright;
    
    /** Creates new PluginInfo */
    public PluginMetaInfo()
    {
        
    }
    
    
    /** Getter for property author.
     * @return Value of property author.
     *
     */
    public String getAuthor()
    {
        return this.author;
    }
    
    /** Setter for property author.
     * @param author New value of property author.
     *
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }
    
    /** Getter for property company.
     * @return Value of property company.
     *
     */
    public String getCompany()
    {
        return this.company;
    }
    
    /** Setter for property company.
     * @param company New value of property company.
     *
     */
    public void setCompany(String company)
    {
        this.company = company;
    }
    
    /** Getter for property contact.
     * @return Value of property contact.
     *
     */
    public String getContact()
    {
        return this.contact;
    }
    
    /** Setter for property contact.
     * @param contact New value of property contact.
     *
     */
    public void setContact(String contact)
    {
        this.contact = contact;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /** Getter for property homepage.
     * @return Value of property homepage.
     *
     */
    public String getHomepage()
    {
        return this.homepage;
    }
    
    /** Setter for property homepage.
     * @param homepage New value of property homepage.
     *
     */
    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }
    
    /** Getter for property displayname.
     * @return Value of property displayname.
     *
     */
    public String getName()
    {
        return this.name;
    }
    
    /** Setter for property displayname.
     * @param displayname New value of property displayname.
     *
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String toString()
    {
        return name;
    }
    
    /** Getter for property version.
     * @return Value of property version.
     *
     */
    public String getVersion()
    {
        return this.version;
    }
    
    /** Setter for property version.
     * @param version New value of property version.
     *
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    /** Getter for property copyright.
     * @return Value of property copyright.
     *
     */
    public String getCopyright()
    {
        return this.copyright;
    }
    
    /** Setter for property copyright.
     * @param copyright New value of property copyright.
     *
     */
    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }
    
}
