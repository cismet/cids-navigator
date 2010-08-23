/*
 * FormDataBean.java
 *
 * Created on 11. November 2003, 15:52
 */

package Sirius.navigator.search.dynamic;

import java.util.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class DefaultFormDataBean implements FormDataBean
{
    protected final Logger logger;
    
    /** Holds value of property queryId. */
    private String queryId;
    
    /** Holds value of property formId. */
    private String formId;
    
    /** Holds value of property dataMap. */
    private java.util.LinkedHashMap dataMap;
    
    /** Holds value of property parameterNamesMap. */
    private java.util.LinkedHashMap parameterNamesMap;
    
    /** Creates a new instance of FormDataBean */
    public DefaultFormDataBean()
    {
       this.logger = Logger.getLogger(this.getClass());
    }
    
    public DefaultFormDataBean(Map parameterNamesMap)
    {
        this();
        this.setParameterNamesMap(parameterNamesMap);
    }
    
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getFormId()
    {
        return this.formId;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setFormId(String formId)
    {
        this.formId = formId;
    }
    
    /** Getter for property dataMap.
     * @return Value of property dataMap.
     *
     */
    public java.util.LinkedHashMap getDataMap()
    {
        return this.dataMap;
    }
    
    /** Setter for property dataMap.
     * @param dataMap New value of property dataMap.
     *
     */
    public void setDataMap(java.util.LinkedHashMap dataMap)
    {
        this.dataMap = dataMap;
    }
    
    public Object getBeanParameter(String name)
    {
        if(this.getDataMap().containsKey(name))
        {
            return this.getDataMap().get(name);
        }
        else
        {
            logger.warn("object '" + name + "' not found in data map");//NOI18N
            return null;
        }
    }
    
    public void setBeanParameter(String name, Object value)
    {
        if(this.getDataMap().containsKey(name))
        {
            this.getDataMap().put(name, value);
        }
        else
        {
            logger.warn("bean parameter name '" + name + "' not found in data map");//NOI18N
        }
    }
    
    public void setQueryParameter(String name, Object value)
    {
        if(this.parameterNamesMap.containsKey(name))
        {
            this.setBeanParameter(this.parameterNamesMap.get(name).toString(), value);
        }
        else
        {
            logger.warn("query parameter name '" + name + "' not found in parameter map");//NOI18N
        }
    }
    
    public Object getQueryParameter(String name)
    {
        if(this.parameterNamesMap.containsKey(name))
        {
            return this.getBeanParameter(this.parameterNamesMap.get(name).toString());
            
            /*Object value = this.getBeanParameter(this.parameterNamesMap.get(name).toString());
            if(value != null)
            {
                return String.valueOf(value);
            }*/
        }
        else
        {
            logger.warn("query parameter '" + name + "' not found in query parameter map");//NOI18N
        }
        
        return null;
    }
    
    public Collection getBeanParameterNames()
    {
        return this.getDataMap().keySet();
    } 
    
    public Collection getQueryParameterNames()
    {
        return this.parameterNamesMap.keySet();
    } 
    
    public Map getParameterNamesMap()
    {
        return this.parameterNamesMap;
    }
        
    /** Setter for property parameterMap.
     * name = queryParameterId
     * value = beanParameterId
     *
     * @param parameterMap New value of property parameterMap.
     *
     */
    public void setParameterNamesMap(java.util.Map parameterNamesMap)
    {
        if(this.parameterNamesMap == null)
        {
            this.parameterNamesMap = new LinkedHashMap(parameterNamesMap.size());
        }
        else
        {
            this.parameterNamesMap.clear();
        }
        
        this.parameterNamesMap.putAll(parameterNamesMap);
        
        if(this.dataMap == null)
        {
            this.dataMap = new LinkedHashMap(parameterNamesMap.size());

            Iterator iterator = this.parameterNamesMap.values().iterator();
            while(iterator.hasNext())
            {
                this.dataMap.put(iterator.next(), null);
            }
        }
        /*else if(this.dataMap.size() != this.parameterNamesMap.size())
        {
            throw new RuntimeException("size of dataMap and parameterNamesMap unequal: " + this.dataMap.size() + " != " + this.parameterNamesMap.size());
        }*/
    }
    
    public int getParameterCount()
    {
        return this.dataMap.size();
    }
    
    /** Getter for property queryId.
     * @return Value of property queryId.
     *
     */
    public String getQueryId()
    {
        return this.queryId;
    }
    
    /** Setter for property queryId.
     * @param queryId New value of property queryId.
     *
     */
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }
    
    public void clear()
    {
        logger.debug("clearing data map values (" + this.dataMap.size() + ")");//NOI18N
        
        Iterator parameterNames = this.dataMap.keySet().iterator();
        while(parameterNames.hasNext())
        {
            this.dataMap.put(parameterNames.next(), null);
        }
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("\nthis.getQueryId():          ").append(this.getQueryId());//NOI18N
        buffer.append("\nthis.getFormId():           ").append(this.getFormId());//NOI18N
        buffer.append("\nthis.getParameterCount():  ").append(this.getParameterCount());//NOI18N
        
        Iterator iterator = this.getQueryParameterNames().iterator();
        while(iterator.hasNext())
        {
            String queryParameterName = iterator.next().toString();
            buffer.append("\nqueryParameterName: '").append(queryParameterName).append("' == beanParameterName: '").append(this.parameterNamesMap.get(queryParameterName)).append("' | value = ").append(this.getQueryParameter(queryParameterName));//NOI18N
  
        }
        
        return buffer.toString();
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        FormDataBean formDataBean = new DefaultFormDataBean();
                
        formDataBean.setParameterNamesMap(this.getParameterNamesMap());
        //formDataBean.setDataMap(this.getDataMap());
        formDataBean.setQueryId(this.getQueryId());
        formDataBean.setFormId(this.getFormId());

        return formDataBean;
    }
    
    public boolean equals(Object obj)
    {
        if(obj != null && FormDataBean.class.isInstance(obj))
        {
            return this.getQueryId().equals(((FormDataBean)obj).getQueryId());
        }
        
        return false;
    }
}
