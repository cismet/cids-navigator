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
 * Created on 12. November 2003, 13:37
 */
package Sirius.navigator.search.dynamic;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface FormDataBean extends Cloneable {

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property beanParameterNames.
     *
     * @return  Value of property beanParameterNames.
     */
    Collection getBeanParameterNames();

    /**
     * Getter for property dataMap.
     *
     * @return  Value of property dataMap.
     */
    LinkedHashMap getDataMap();

    /**
     * Setter for property dataMap.
     *
     * @param  dataMap  New value of property dataMap.
     */
    void setDataMap(LinkedHashMap dataMap);

    /**
     * Setter for property parameterNamesMap.
     *
     * @param  parameterNamesMap  New value of property parameterNamesMap.
     */
    void setParameterNamesMap(Map parameterNamesMap);

    /**
     * Getter for property formId.
     *
     * @return  Value of property formId.
     */
    String getFormId();

    /**
     * Setter for property formId.
     *
     * @param  formId  New value of property formId.
     */
    void setFormId(String formId);

    /**
     * Getter for property queryParameterNames.
     *
     * @return  Value of property queryParameterNames.
     */
    Collection getQueryParameterNames();

    /**
     * .........................................................................
     *
     * @param  name   DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    void setBeanParameter(String name, Object value);

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getBeanParameter(String name);

    /**
     * DOCUMENT ME!
     *
     * @param  name   DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    void setQueryParameter(String name, Object value);

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getQueryParameter(String name);

    /**
     * Getter for property parameterCount.
     *
     * @return  Value of property parameterCount.
     */
    int getParameterCount();

    /**
     * Getter for property parameterNamesMap.
     *
     * @return  Value of property parameterNamesMap.
     */
    Map getParameterNamesMap();

    /**
     * Getter for property queryId.
     *
     * @return  Value of property queryId.
     */
    String getQueryId();

    /**
     * Setter for property queryId.
     *
     * @param  queryId  New value of property queryId.
     */
    void setQueryId(String queryId);

    /**
     * Clears all form data.
     */
    void clear();

    /**
     * returns.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CloneNotSupportedException  DOCUMENT ME!
     */

    //J-
    Object clone() throws CloneNotSupportedException;
    //J+
}
