/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.server.middleware.types.MetaClass;

import java.util.HashMap;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class QuerySearchResultsAction {

    //~ Instance fields --------------------------------------------------------

    private MetaClass metaClass;
    private List<String> attributeKeys;
    private HashMap<String, String> attributeNames;
    private String whereCause;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getName();
    /**
     * DOCUMENT ME!
     */
    public abstract void doAction();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HashMap<String, String> getAttributeNames() {
        return attributeNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<String> getAttributeKeys() {
        return attributeKeys;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attributeKeys  DOCUMENT ME!
     */
    public void setAttributeKeys(final List<String> attributeKeys) {
        this.attributeKeys = attributeKeys;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attributeNames  DOCUMENT ME!
     */
    public void setAttributeNames(final HashMap<String, String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  metaClass  DOCUMENT ME!
     */
    public void setMetaClass(final MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getWhereCause() {
        return whereCause;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  whereCause  DOCUMENT ME!
     */
    public void setWhereCause(final String whereCause) {
        this.whereCause = whereCause;
    }
}
