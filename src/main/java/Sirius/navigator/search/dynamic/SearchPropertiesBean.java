/*
 * SearchPropertiesBean.java
 *
 * Created on 18. November 2003, 12:02
 */

package Sirius.navigator.search.dynamic;

/**
 *
 * @author  pascal
 */
public class SearchPropertiesBean
{
    
    /** Holds value of property formDataBeans. */
    private java.util.List formDataBeans;
    
    /** Holds value of property classNodeKeys. */
    private java.util.List classNodeKeys;
    
    /** Holds value of property appendSearchResults. */
    private boolean appendSearchResults;
    
    /** Creates a new instance of SearchPropertiesBean */
    public SearchPropertiesBean()
    {
    }
    
    /** Getter for property formDataBeans.
     * @return Value of property formDataBeans.
     *
     */
    public java.util.List getFormDataBeans()
    {
        return this.formDataBeans;
    }
    
    /** Setter for property formDataBeans.
     * @param formDataBeans New value of property formDataBeans.
     *
     */
    public void setFormDataBeans(java.util.List formDataBeans)
    {
        this.formDataBeans = formDataBeans;
    }
    
    /** Getter for property classNodeKeys.
     * @return Value of property classNodeKeys.
     *
     */
    public java.util.List getClassNodeKeys()
    {
        return this.classNodeKeys;
    }
    
    /** Setter for property classNodeKeys.
     * @param classNodeKeys New value of property classNodeKeys.
     *
     */
    public void setClassNodeKeys(java.util.List classNodeKeys)
    {
        this.classNodeKeys = classNodeKeys;
    }
    
    /** Getter for property appendSearchResults.
     * @return Value of property appendSearchResults.
     *
     */
    public boolean isAppendSearchResults()
    {
        return this.appendSearchResults;
    }
    
    /** Setter for property appendSearchResults.
     * @param appendSearchResults New value of property appendSearchResults.
     *
     */
    public void setAppendSearchResults(boolean appendSearchResults)
    {
        this.appendSearchResults = appendSearchResults;
    }
    
}
