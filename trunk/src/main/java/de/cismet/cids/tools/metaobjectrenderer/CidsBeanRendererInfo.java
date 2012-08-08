/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaObject;

import java.util.Collection;

import javax.swing.JComponent;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface CidsBeanRendererInfo {

    //~ Instance fields --------------------------------------------------------

    @Deprecated
    String WIDTH_RATIO = "WIDTH_RATIO"; // NOI18N

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getSingleRendererClassName();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getAggregationRenderer();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    double getWidthRatio();
}
