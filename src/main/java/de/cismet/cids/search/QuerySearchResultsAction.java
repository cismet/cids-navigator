/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;

import java.util.HashMap;
import java.util.List;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class QuerySearchResultsAction {

    //~ Instance fields --------------------------------------------------------

    private MetaClass metaClass;
    private List<CidsBean> beans;
    private HashMap<MemberAttributeInfo, String> maiNames;
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
    public HashMap<MemberAttributeInfo, String> getMaiNames() {
        return maiNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maiNames  DOCUMENT ME!
     */
    public void setMaiNames(final HashMap<MemberAttributeInfo, String> maiNames) {
        this.maiNames = maiNames;
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
     * @param  beans  DOCUMENT ME!
     */
    public void setBeans(final List<CidsBean> beans) {
        this.beans = beans;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<CidsBean> getBeans() {
        return beans;
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
