/*
 * FormDataBean.java
 *
 * Created on 12. November 2003, 13:37
 */

package Sirius.navigator.search.dynamic;

import java.util.*;

/**
 *
 * @author  pascal
 */
public interface FormDataBean extends Cloneable 
{
    /** Getter for property beanParameterNames.
     * @return Value of property beanParameterNames.
     *
     */
    public Collection getBeanParameterNames();
    
    /** Getter for property dataMap.
     * @return Value of property dataMap.
     *
     */
    public LinkedHashMap getDataMap();
    
    /** Setter for property dataMap.
     * @param dataMap New value of property dataMap.
     *
     */
    public void setDataMap(LinkedHashMap dataMap);
        
    /** Setter for property parameterNamesMap.
     * @param parameterNamesMap New value of property parameterNamesMap.
     *
     */
    public void setParameterNamesMap(Map parameterNamesMap);
    
    /** Getter for property formId.
     * @return Value of property formId.
     *
     */
    public String getFormId();
    
    /** Setter for property formId.
     * @param formId New value of property formId.
     *
     */
    public void setFormId(String formId);
    
    /** Getter for property queryParameterNames.
     * @return Value of property queryParameterNames.
     *
     */
    public Collection getQueryParameterNames(); 

    // .........................................................................
    
    public void setBeanParameter(String name, Object value);
    
    public Object getBeanParameter(String name);
    
    public void setQueryParameter(String name, Object value);
    
    public Object getQueryParameter(String name);  
    
    /** Getter for property parameterCount.
     * @return Value of property parameterCount.
     *
     */
    public int getParameterCount();
    
    /** Getter for property parameterNamesMap.
     * @return Value of property parameterNamesMap.
     *
     */
    public Map getParameterNamesMap();
    
    /** Getter for property queryId.
     * @return Value of property queryId.
     *
     */
    public String getQueryId();
    
    /** Setter for property queryId.
     * @param queryId New value of property queryId.
     *
     */
    public void setQueryId(String queryId);
    
    /**
     * Clears all form data
     */
    public void clear();
    
    /**
     * returns
     */
    public Object clone() throws CloneNotSupportedException;
    
}
