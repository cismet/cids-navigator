/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * TreeNodeRestriction.java
 *
 * Created on 24. April 2003, 12:45
 */
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class TreeNodeRestriction implements Restriction {

    //~ Instance fields --------------------------------------------------------

    private final Logger logger = Logger.getLogger(TreeNodeRestriction.class);

    private final long typeRestriction;
    private final String domainRestriction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreeNodeRestriction object.
     */
    public TreeNodeRestriction() {
        this(PURE + OBJECT + CLASS, null);
    }

    /**
     * Creates a new instance of TreeNodeRestriction.
     *
     * @param  typeRestriction  DOCUMENT ME!
     */
    public TreeNodeRestriction(final long typeRestriction) {
        this(typeRestriction, null);
    }

    /**
     * Creates a new TreeNodeRestriction object.
     *
     * @param  typeRestriction    DOCUMENT ME!
     * @param  domainRestriction  DOCUMENT ME!
     */
    public TreeNodeRestriction(final long typeRestriction, final String domainRestriction) {
        this.typeRestriction = typeRestriction;
        this.domainRestriction = domainRestriction;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   object  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultMetaTreeNode applyRestriction(final Object object) {
        if (logger.isDebugEnabled()) {
            logger.debug("apply restriction on '" + object.toString() + "' (" + object.getClass().getName() + ")"); // NOI18N
        }

        final DefaultMetaTreeNode node;
        if (!(object instanceof DefaultMetaTreeNode)) {
            return null;
        } else {
            node = (DefaultMetaTreeNode)object;
        }

        if (node.isRootNode() || node.isWaitNode()) {
            return null;
        } else if ((node.isPureNode() && ((PURE & typeRestriction) != 0))
                    || (node.isObjectNode() && ((OBJECT & typeRestriction) != 0))
                    || (node.isClassNode() && ((CLASS & typeRestriction) != 0))) {
            if ((this.domainRestriction == null) || this.domainRestriction.equals(node.getDomain())) {
                return node;
            }
        }

        return null;
    }

    @Override
    public long getTypeRestriction() {
        return this.typeRestriction;
    }

    /*public final static void main(String args[])
     * { TreeNodeRestriction treeNodeRestriction = new TreeNodeRestriction();
     * System.out.println(treeNodeRestriction.typeRestriction); System.out.println(treeNodeRestriction.CLASS &
     * treeNodeRestriction.typeRestriction);}*/
}
