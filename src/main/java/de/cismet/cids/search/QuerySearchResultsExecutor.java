/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public interface QuerySearchResultsExecutor {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    HashMap<String, String> getAttributeNames();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<String> getAttributeKeys();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    MetaClass getMetaClass();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getWhereCause();
}
