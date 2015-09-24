/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.tools.metaobjectrenderer;

import java.util.Collection;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface CidsBeanAggregationHandler {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getPriority();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean consume();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getSourceMetaClassTablename();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getTargetMetaClassTablename();

    /**
     * DOCUMENT ME!
     *
     * @param   cidsBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Collection<CidsBean> getAggregatedBeans(final CidsBean cidsBean);
}
