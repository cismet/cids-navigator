/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * FormDataBean.java
 *
 * Created on 11. November 2003, 15:52
 */
package Sirius.navigator.search.dynamic;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultFormDataBean implements FormDataBean {

    //~ Instance fields --------------------------------------------------------

    protected final Logger logger;

    /** Holds value of property queryId. */
    private String queryId;

    /** Holds value of property formId. */
    private String formId;

    /** Holds value of property dataMap. */
    private java.util.LinkedHashMap dataMap;

    /** Holds value of property parameterNamesMap. */
    private java.util.LinkedHashMap parameterNamesMap;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of FormDataBean.
     */
    public DefaultFormDataBean() {
        this.logger = Logger.getLogger(this.getClass());
    }

    /**
     * Creates a new DefaultFormDataBean object.
     *
     * @param  parameterNamesMap  DOCUMENT ME!
     */
    public DefaultFormDataBean(final Map parameterNamesMap) {
        this();
        this.setParameterNamesMap(parameterNamesMap);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    @Override
    public String getFormId() {
        return this.formId;
    }

    /**
     * Setter for property name.
     *
     * @param  formId  name New value of property name.
     */
    @Override
    public void setFormId(final String formId) {
        this.formId = formId;
    }

    /**
     * Getter for property dataMap.
     *
     * @return  Value of property dataMap.
     */
    @Override
    public java.util.LinkedHashMap getDataMap() {
        return this.dataMap;
    }

    /**
     * Setter for property dataMap.
     *
     * @param  dataMap  New value of property dataMap.
     */
    @Override
    public void setDataMap(final java.util.LinkedHashMap dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public Object getBeanParameter(final String name) {
        if (this.getDataMap().containsKey(name)) {
            return this.getDataMap().get(name);
        } else {
            logger.warn("object '" + name + "' not found in data map"); // NOI18N
            return null;
        }
    }

    @Override
    public void setBeanParameter(final String name, final Object value) {
        if (this.getDataMap().containsKey(name)) {
            this.getDataMap().put(name, value);
        } else {
            logger.warn("bean parameter name '" + name + "' not found in data map"); // NOI18N
        }
    }

    @Override
    public void setQueryParameter(final String name, final Object value) {
        if (this.parameterNamesMap.containsKey(name)) {
            this.setBeanParameter(this.parameterNamesMap.get(name).toString(), value);
        } else {
            logger.warn("query parameter name '" + name + "' not found in parameter map"); // NOI18N
        }
    }

    @Override
    public Object getQueryParameter(final String name) {
        if (this.parameterNamesMap.containsKey(name)) {
            return this.getBeanParameter(this.parameterNamesMap.get(name).toString());

            /*Object value = this.getBeanParameter(this.parameterNamesMap.get(name).toString());
             * if(value != null) { return String.valueOf(value);}*/
        } else {
            logger.warn("query parameter '" + name + "' not found in query parameter map"); // NOI18N
        }

        return null;
    }

    @Override
    public Collection getBeanParameterNames() {
        return this.getDataMap().keySet();
    }

    @Override
    public Collection getQueryParameterNames() {
        return this.parameterNamesMap.keySet();
    }

    @Override
    public Map getParameterNamesMap() {
        return this.parameterNamesMap;
    }

    /**
     * Setter for property parameterMap. name = queryParameterId value = beanParameterId
     *
     * @param  parameterNamesMap  New value of property parameterMap.
     */
    @Override
    public void setParameterNamesMap(final java.util.Map parameterNamesMap) {
        if (this.parameterNamesMap == null) {
            this.parameterNamesMap = new LinkedHashMap(parameterNamesMap.size());
        } else {
            this.parameterNamesMap.clear();
        }

        this.parameterNamesMap.putAll(parameterNamesMap);

        if (this.dataMap == null) {
            this.dataMap = new LinkedHashMap(parameterNamesMap.size());

            final Iterator iterator = this.parameterNamesMap.values().iterator();
            while (iterator.hasNext()) {
                this.dataMap.put(iterator.next(), null);
            }
        }
        /*else if(this.dataMap.size() != this.parameterNamesMap.size())
         * { throw new RuntimeException("size of dataMap and parameterNamesMap unequal: " + this.dataMap.size() + " != "
         * + this.parameterNamesMap.size());}*/
    }

    @Override
    public int getParameterCount() {
        return this.dataMap.size();
    }

    /**
     * Getter for property queryId.
     *
     * @return  Value of property queryId.
     */
    @Override
    public String getQueryId() {
        return this.queryId;
    }

    /**
     * Setter for property queryId.
     *
     * @param  queryId  New value of property queryId.
     */
    @Override
    public void setQueryId(final String queryId) {
        this.queryId = queryId;
    }

    @Override
    public void clear() {
        if (logger.isDebugEnabled()) {
            logger.debug("clearing data map values (" + this.dataMap.size() + ")"); // NOI18N
        }

        final Iterator parameterNames = this.dataMap.keySet().iterator();
        while (parameterNames.hasNext()) {
            this.dataMap.put(parameterNames.next(), null);
        }
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("\nthis.getQueryId():          ").append(this.getQueryId());       // NOI18N
        buffer.append("\nthis.getFormId():           ").append(this.getFormId());        // NOI18N
        buffer.append("\nthis.getParameterCount():  ").append(this.getParameterCount()); // NOI18N

        final Iterator iterator = this.getQueryParameterNames().iterator();
        while (iterator.hasNext()) {
            final String queryParameterName = iterator.next().toString();
            buffer.append("\nqueryParameterName: '")
                    .append(queryParameterName)
                    .append("' == beanParameterName: '")
                    .append(this.parameterNamesMap.get(queryParameterName))
                    .append("' | value = ")
                    .append(this.getQueryParameter(queryParameterName)); // NOI18N
        }

        return buffer.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final FormDataBean formDataBean = new DefaultFormDataBean();

        formDataBean.setParameterNamesMap(this.getParameterNamesMap());
        // formDataBean.setDataMap(this.getDataMap());
        formDataBean.setQueryId(this.getQueryId());
        formDataBean.setFormId(this.getFormId());

        return formDataBean;
    }

    @Override
    public boolean equals(final Object obj) {
        if ((obj != null) && FormDataBean.class.isInstance(obj)) {
            return this.getQueryId().equals(((FormDataBean)obj).getQueryId());
        }

        return false;
    }
}
