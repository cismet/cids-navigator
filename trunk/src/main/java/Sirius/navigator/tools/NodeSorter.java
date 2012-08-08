/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * NodeSorter.java
 *
 * Created on 26. Juni 2007, 15:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Sirius.navigator.tools;

import Sirius.server.middleware.types.*;

import Sirius.util.NodeComparator;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   schlob
 * @version  $Revision$, $Date$
 */
public class NodeSorter {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(NodeSorter.class);

    public static final boolean ASCENDING = true;

    public static final boolean DESCENDING = false;

    public static final String DEFAULT_COMPARATOR = "Sirius.util.NodeComparator"; // NOI18N

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NodeSorter object.
     */
    private NodeSorter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   nodes  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Node[] sortNodes(final Node[] nodes) {
        final Comparator comparator = new NodeComparator();
        Arrays.sort(nodes, comparator);
        return nodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   nodes            DOCUMENT ME!
     * @param   comparatorClass  DOCUMENT ME!
     * @param   ascending        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Throwable  DOCUMENT ME!
     */
    public static Node[] sortNodes(final Node[] nodes, String comparatorClass, final boolean ascending)
            throws Throwable {
        if ((comparatorClass == null) || (comparatorClass.length() == 0)) {
            comparatorClass = DEFAULT_COMPARATOR;
        }
//
//            if(comparatorCache.containsKey(comparatorClass))
//            {
//                comparator = (Comparator)comparatorCache.get(comparatorClass);
//            }
//            else
//            {

        Comparator comparator = null;
        try {
            logger.info("creating new comparator instance: comparatorClass");                             // NOI18N
            comparator = (Comparator)Class.forName(comparatorClass).newInstance();
        } catch (Throwable t) {
            logger.error("could not create comparator class '" + comparatorClass + "', sorting aborted"); // NOI18N
            return nodes;
        }
//            }

        if (logger.isDebugEnabled()) {
            logger.debug("sorting nodes ascending by comparator '" + comparatorClass + "'"); // NOI18N
        }

        Arrays.sort(nodes, comparator);

        if (ascending) {
            return nodes;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("sorting nodes descending by comparator '" + comparatorClass + "'"); // NOI18N
            }

            // Arrays.sort(nodes, comparator);
            final Node[] tempNodes = new Node[nodes.length];

            for (int i = 0; i < tempNodes.length; i++) {
                tempNodes[i] = nodes[tempNodes.length - i - 1];
            }

            return tempNodes;
        }
    }
}
